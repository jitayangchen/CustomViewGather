package me.kkuai.kuailian.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.room.LiveRoom;
import me.kkuai.kuailian.bean.RoomLiveListBean;
import me.kkuai.kuailian.bean.RoomMsg;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.engine.LiveRoomDataUpdate;
import me.kkuai.kuailian.engine.LiveRoomObservable;
import me.kkuai.kuailian.http.KHttpDownload;
import me.kkuai.kuailian.http.request.GetRoomChatMsgRequest;
import me.kkuai.kuailian.http.request.LiveRoomDataRequest;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.event.EventCenter;
import me.kkuai.kuailian.service.event.EventListener;
import me.kkuai.kuailian.service.receiver.LiveRoomAlarmReceiver;
import me.kkuai.kuailian.utils.DateUtil;
import me.kkuai.kuailian.utils.JsonUtil;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class RoomService extends Service implements EventListener {
	
	private Log log = LogFactory.getLog(RoomService.class);
	private MediaPlayer mediaPlayer;
	private String currentRoomId;
	private List<RoomLiveListBean> roomLiveLists = new ArrayList<RoomLiveListBean>();
//	private List<RoomMsg> roomMsgList = new ArrayList<RoomMsg>();
	private RoomLiveListBean currentPlay;
	private int nextPlayPosition;
	private CompletionListener completionListener;
	private boolean isFirst = true;
	private static long showTimeLength = 180000;
	private int countDownTime;
	private int nextDownloadAudioTime = 0;
//	private int showMsgCount = 0;
	private int showMsgTime = -1;
	private String lastUpdateTime;
	private List<String> roomMsgIds = new ArrayList<String>();
	
	private Handler handler = new Handler();
	
	@Override
	public void onCreate() {
		super.onCreate();
		log.info("RoomService >>> Create()");
		EventCenter.getInstance().registerListener(this, AppConstantValues.LIVE_ROOM_REQUEST_DATA);
		EventCenter.getInstance().registerListener(this, AppConstantValues.EVENT_LOGOUT);
		
		serviceNotification = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, LiveRoom.class), 0);
		Notification notification = new Notification(R.drawable.logo, "Live Room Running!", System.currentTimeMillis());
		notification.setLatestEventInfo(getApplicationContext(), "Live Room", "content", contentIntent);
		startForeground(1, notification);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		log.info("RoomService >>> onStartCommand()");
		currentRoomId = intent.getStringExtra("roomId");
		requestRoomData(currentRoomId);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		log.info("RoomService >>> onDestroy()");
		if (null != mediaPlayer) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer = null;
		}
		handler.removeCallbacks(runnable);
		cancelReconnect();
		EventCenter.getInstance().removeListener(this, AppConstantValues.LIVE_ROOM_REQUEST_DATA);
		EventCenter.getInstance().removeListener(this, AppConstantValues.EVENT_LOGOUT);
		super.onDestroy();
	}
	
	private void play() {
		log.info(" ================= play() ================= ");
		String voicePath = roomLiveLists.get(0).getNativePath();
		currentPlay = roomLiveLists.get(0);
		LiveRoomDataUpdate.getInstance().update(LiveRoomDataUpdate.CURRENT_PLAY_DATA, currentPlay);
		roomLiveLists.remove(0);
		if (null != voicePath) {
			playVoice(voicePath, 0);
		}
	}
	
	private void playVoice(String voicePath, long seekToTime) {
		log.info(" ================= playVoice() ================= ");
		if (null == mediaPlayer) {
			mediaPlayer = new MediaPlayer();
			log.info(" --- mediaPlayer = new MediaPlayer() --- ");
		}
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(voicePath);
			mediaPlayer.prepare();
			
			if (seekToTime >= mediaPlayer.getDuration()) {
				mediaPlayer.reset();
				return;
			}
			if (0 != seekToTime) {
				mediaPlayer.seekTo((int)seekToTime);
			}
			if (null == completionListener) {
				completionListener = new CompletionListener();
			}
			mediaPlayer.setOnCompletionListener(completionListener);
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			log.error("play audio", e);
		} catch (SecurityException e) {
			log.error("play audio", e);
		} catch (IllegalStateException e) {
			log.error("play audio", e);
		} catch (IOException e) {
			log.error("play audio", e);
		}
		
	}
	
	/**
	 * request room data
	 */
	private void requestRoomData(String roomId) {
		LiveRoomDataRequest roomDataRequest = new LiveRoomDataRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				roomLiveLists.addAll((List<RoomLiveListBean>) result);
				readyDownloadVoice(roomLiveLists);
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		roomDataRequest.requestRoomData(roomId);
	}
	
//	private void requestRoomMsg(String roomId, String signupId, String lastUpdateTime) {
//		GetRoomChatMsgRequest request = new GetRoomChatMsgRequest(new OnDataBack() {
//			
//			@Override
//			public void onResponse(Object result) {
//				parseRoomMsg((String) result);
//			}
//			
//			@Override
//			public void onError(int Error) {
//				
//			}
//		});
//		
//		request.requestRoomChatMsg(roomId, signupId, lastUpdateTime);
//	}
	
	private void readyDownloadVoice(List<RoomLiveListBean> liveListBeans) {
		if (isFirst) {
			currentPlay = roomLiveLists.get(0);
			LiveRoomDataUpdate.getInstance().update(LiveRoomDataUpdate.CURRENT_PLAY_DATA, currentPlay);
			roomLiveLists.remove(0);
			downloadVoiceFirst(currentPlay);
			isFirst = false;
			LiveRoomDataUpdate.getInstance().update(LiveRoomDataUpdate.LOAD_FINISH, null);
		}
	}
	
	private void downloadVoiceFirst(RoomLiveListBean liveListBean) {
		
		if (liveListBean.getHasPayTime() > showTimeLength - 3000) {
			roomLiveLists.remove(0);
			setCountDownTime(showTimeLength - liveListBean.getHasPayTime());
			if (roomLiveLists.size() > 0) {
				downloadVoice(0);
			}
			return;
		}
		setCountDownTime(showTimeLength - currentPlay.getHasPayTime());
		scheduleReconnect(showTimeLength - currentPlay.getHasPayTime());
		final long startTime = System.currentTimeMillis();
		KHttpDownload.getInstance().addDownloadUrl(liveListBean.getPlayContentAudioUrl(), new AjaxCallBack<File>() {
			public void onSuccess(File t) {
				log.info("down load success ---------------path === " + t.toString());
				long endTime = System.currentTimeMillis();
				long time = endTime - startTime;
				long seekToTime = currentPlay.getHasPayTime() + time;
				playVoice(t.toString(), seekToTime);
				
				downloadVoice(0);
			};
		});
	}
	
	private void downloadVoice(final int position) {
		if (roomLiveLists.size() < 2) {
			requestRoomData(currentRoomId);
		}
		String audioUrl = roomLiveLists.get(position).getPlayContentAudioUrl();
		KHttpDownload.getInstance().addDownloadUrl(audioUrl, new AjaxCallBack<File>() {
			public void onSuccess(File t) {
				log.info("down load success -------------------");
				roomLiveLists.get(position).setNativePath(t.toString());
			};
		});
	}
	
	class CompletionListener implements OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			log.error("----------   OnCompletionListener   ----------");
		}
		
	}
	
	Runnable runnable = new Runnable() {  
        @Override  
        public void run() {  
        	countDownTime--;
//        	showMsgCount++;
            if (countDownTime < 0) {
            	handler.removeCallbacks(runnable);
//            	showMsgCount = 0;
				return;
			}
            
            if (0 != nextDownloadAudioTime && countDownTime <= (showTimeLength - nextDownloadAudioTime)) {
				downloadVoice(0);
				nextDownloadAudioTime = 0;
			}
            
//            if (countDownTime % 10 == 0) {
//            	if (null == lastUpdateTime) {
//            		lastUpdateTime = DateUtil.millsConvertDateStr(System.currentTimeMillis());
//				}
//            	requestRoomMsg(currentRoomId, currentPlay.getSignupId(), lastUpdateTime);
//			}
            
//            if (roomMsgList.size() > 0) {
//            	log.info("showMsgCount === " + showMsgCount + " ------ showMsgTime === " + showMsgTime);
//            	if (showMsgCount == showMsgTime) {
//            		LiveRoomDataUpdate.getInstance().update(LiveRoomDataUpdate.UPDATE_ROOM_MESSAGE, roomMsgList.remove(0));
//            		showMsgCount = 0;
//				}
//			}
            
            int minute = countDownTime/60;
            int second = countDownTime%60;
            String time = minute + ":" + (second<10?"0"+second:second+"");
            LiveRoomObservable.getInstance().updateObserver(time);
            handler.postDelayed(this, 1000);  
        }  
    };
    
	private NotificationManager serviceNotification;
    
    /**
     * set down time 
     * @param time
     */
    private void setCountDownTime(long time) {
    	countDownTime = (int) (time/1000);
    	handler.removeCallbacks(runnable);
//    	roomMsgList.clear();
//    	showMsgCount = 0;
		handler.postDelayed(runnable, 1000);
    }
    
    /**
	 * start reckon by time.
	 * @param startTime
	 * @param type
	 */
	public void scheduleReconnect(long periodTime) {
		long now = System.currentTimeMillis();
		Intent i = new Intent();
		i.setClass(getApplicationContext(), LiveRoomAlarmReceiver.class);
		i.setAction(LiveRoomAlarmReceiver.ACTION);
		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
		AlarmManager alarmMgr = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
		alarmMgr.set(AlarmManager.RTC, now + periodTime, pi);
		log.debug(LiveRoomAlarmReceiver.class.getName() + "======" + periodTime);
	}
	
	/**
	 * cancel reckon by time.
	 */
	public void cancelReconnect() {
		log.info("--------- cancelReconnect() -----------");
		Intent i = new Intent();
		i.setClass(getApplicationContext(), LiveRoomAlarmReceiver.class);
		i.setAction(LiveRoomAlarmReceiver.ACTION);
		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
		AlarmManager alarmMgr = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
		alarmMgr.cancel(pi);
	}
	
	@Override
	public void onEvent(Object source, String event) {
		if (AppConstantValues.LIVE_ROOM_REQUEST_DATA.equals(event)) {
			play();
			scheduleReconnect(showTimeLength);
			int random = (int) (Math.random()*130);
			nextDownloadAudioTime = (random + 20) * 1000;
			setCountDownTime(showTimeLength);
			log.debug("nextInt ====== " + nextDownloadAudioTime);
		} else if (AppConstantValues.EVENT_LOGOUT.equals(event)) {
			LiveRoomDataUpdate.getInstance().update(LiveRoomDataUpdate.EVENT_LOGOUT, null);
		}
	}
	
//	private void parseRoomMsg(String result) {
//		try {
//			JSONObject obj = new JSONObject(result);
//			String status = JsonUtil.getJsonString(obj, "status");
//			if ("1".equals(status)) {
//				JSONArray msgLists = JsonUtil.getJsonArrayObject(obj, "msgList");
//				for (int i = 0; i < msgLists.length(); i++) {
//					JSONObject msgList = JsonUtil.getJsonObject(msgLists, i);
//					String msgId = JsonUtil.getJsonString(msgList, "msgId");
//					if (roomMsgIds.contains(msgId)) {
//						continue;
//					}
//					roomMsgIds.add(msgId);
//					
//					RoomMsg roomMsg = new RoomMsg();
//					roomMsg.setSendUid(JsonUtil.getJsonString(msgList, "sendUid"));
//					roomMsg.setNickName(JsonUtil.getJsonString(msgList, "nickName"));
//					roomMsg.setAvatar(JsonUtil.getJsonString(msgList, "avatar"));
//					roomMsg.setSex(JsonUtil.getJsonString(msgList, "sex"));
//					roomMsg.setId(JsonUtil.getJsonString(msgList, "id"));
//					roomMsg.setMsgId(JsonUtil.getJsonString(msgList, "msgId"));
//					roomMsg.setMsgType(JsonUtil.getJsonString(msgList, "msgType"));
//					JSONObject msgContent = JsonUtil.getJsonObject(msgList, "msgContent");
//					roomMsg.setMsgContent(JsonUtil.getJsonString(msgContent, "text"));
//					roomMsg.setSendTime(JsonUtil.getJsonLong(msgList, "sendTime"));
//					roomMsgList.add(0, roomMsg);
//				}
//				if (roomMsgList.size() > 0) {
//					showMsgTime = 10 / roomMsgList.size();
//					lastUpdateTime = DateUtil.millsConvertDateStr(roomMsgList.get(roomMsgList.size() - 1).getSendTime());
//				}
//				showMsgCount = 0;
//			}
//		} catch (JSONException e) {
//			log.error("parse Room Msg", e);
//		}
//	}

}
