package me.kkuai.kuailian.service;

import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.event.EventCenter;
import me.kkuai.kuailian.service.event.EventListener;
import me.kkuai.kuailian.service.socket.SocketPool;
import me.kkuai.kuailian.service.socket.SocketService;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CoreService extends Service implements EventListener {
	
	private Log log = LogFactory.getLog(CoreService.class);
	private SocketService socketService;
	private UserInfo currentUser;
	private Heartbeat heartbeat;
	private long periodTime = 60000;
	
	@Override
	public void onCreate() {
		super.onCreate();
		log.info("----------- Create CoreService -----------");
		socketService = SocketPool.getInatance(getApplicationContext()).getSocketService();
		currentUser = UserManager.getInstance().getCurrentUser();
		heartbeat = new Heartbeat();
		
		EventCenter.getInstance().registerListener(this, AppConstantValues.HEART_BEAT_SEND_STATUS_CHANGE);
		EventCenter.getInstance().registerListener(this, AppConstantValues.HEART_BEAT_AGAIN_SEND);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		log.info("----------- Start CoreService -----------");
		
//		if (socketService.isConnected) {
//			return;
//		}
		
//		new Thread() {
//			public void run() {
//				for (int i = 0; i < 2000; i++) {
//					try {
//						Thread.sleep(10000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					log.info("onStart --- " + i);
//				}
//			};
//		}.start();
//		
//		heartbeat.start();
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		log.info("----------- onStartCommand CoreService -----------");
		if (!socketService.isConnected) {
			heartbeat.start();
		}
//		return Service.START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		log.info("----------- Stop CoreService -----------");
		EventCenter.getInstance().removeListener(this, AppConstantValues.HEART_BEAT_SEND_STATUS_CHANGE);
		EventCenter.getInstance().removeListener(this, AppConstantValues.HEART_BEAT_AGAIN_SEND);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	class Heartbeat extends Thread {
		public void run() {
			socketService.stop();
			socketService.start();
			socketService.sendLoginVertify();
		};
	}
	
	/**
	 * 定时重连(60秒后)
	 * 
	 * @param startTime
	 */
	public void scheduleReconnect() {
		long now = System.currentTimeMillis();
		Intent i = new Intent();
		i.setClass(this, AlarmReceiver.class);
		i.setAction(AlarmReceiver.ACTION);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
//		alarmMgr.setRepeating(AlarmManager.RTC, now + periodTime, periodTime, pi);
		alarmMgr.set(AlarmManager.RTC, now + periodTime, pi);
	}
	
	/**
	 * 取消重连定时器
	 */
	public void cancelReconnect() {
		Intent i = new Intent();
		i.setClass(this, AlarmReceiver.class);
		i.setAction(AlarmReceiver.ACTION);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmMgr.cancel(pi);
	}
	
	@Override
	public void onEvent(Object source, String event) {
		if (AppConstantValues.HEART_BEAT_SEND_STATUS_CHANGE.equals(event)) {
			if (AppConstantValues.HEART_BEAT_START.equals(source)) {
				log.info("heart beat start");
				scheduleReconnect();
			} else {
				log.info("heart beat stop");
				cancelReconnect();
			}
		} else if (AppConstantValues.HEART_BEAT_AGAIN_SEND.equals(event)) {
			log.info("heart beat again send");
			scheduleReconnect();
		}
	}
	
}
