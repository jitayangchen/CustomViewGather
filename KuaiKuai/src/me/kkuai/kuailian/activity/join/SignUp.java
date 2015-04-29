package me.kkuai.kuailian.activity.join;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import me.kkuai.kuailian.KApplication;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.adapter.JoinedKkAdapter;
import me.kkuai.kuailian.bean.JoinedKk;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.db.JoinedKKDao;
import me.kkuai.kuailian.dialog.DialogJoinRecord;
import me.kkuai.kuailian.dialog.DialogJoinRecord.RecordFinishListener;
import me.kkuai.kuailian.dialog.DialogLoading;
import me.kkuai.kuailian.dialog.DialogNormal;
import me.kkuai.kuailian.dialog.DialogNormal.DialogOnClickOkListener;
import me.kkuai.kuailian.dialog.DialogSingleConfirm;
import me.kkuai.kuailian.dialog.DialogSingleConfirm.DialogOkClickListener;
import me.kkuai.kuailian.engine.LiveRoomDataUpdate;
import me.kkuai.kuailian.engine.LiveRoomDataUpdate.DataUpdateListener;
import me.kkuai.kuailian.http.request.OwnerJoinedKKRequest;
import me.kkuai.kuailian.http.request.RoomSignUpRequest;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.DateUtil;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.utils.Preference;
import me.kkuai.kuailian.utils.ShowToastUtil;
import me.kkuai.kuailian.widget.pulltorefresh.PullToRefreshBase;
import me.kkuai.kuailian.widget.pulltorefresh.PullToRefreshBase.Mode;
import me.kkuai.kuailian.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import me.kkuai.kuailian.widget.pulltorefresh.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.JsonToken;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;
import com.kkuai.libs.managers.J_NetManager.OnLoadingListener;
import com.lidroid.xutils.exception.HttpException;

public class SignUp extends BaseActivity implements OnClickListener, RecordFinishListener, DialogOkClickListener, OnRefreshListener2, DialogOnClickOkListener, DataUpdateListener {
	
	private Context context;
	private RelativeLayout rlJoinTime, rlJoinSex, rlArea, rlJoinPrice;
	private RelativeLayout rlJoinedKkuai;
	private ListView lvJoinedKk;
	private PullToRefreshListView pullRefreshListView;
	private LinearLayout llSignupJoin;
	private LinearLayout llSignupJoinTab, llSignupJoinedTab;
	private boolean isFirstLoadJoined = true;
	private JoinedKkAdapter joinedKkAdapter;
	private LinearLayout llRecordingAudio;
	private RelativeLayout rlPlayAudio, rlRecordFinished, rlRecord;
	private Button btnSignupCommit;
	private TextView tvSignupTime;
	private TextView tvAudioTime;
	private TextView tvRepeatRecord, tvDeleteAudio, tvDataNullPrompt, tvKcoin;
	private ImageView ivPlayAnimation;
	private String audioFilePath = null;
	private long audioLength = 0;
	private UserInfo currentUser;
	private String result;
	private String roomId;
	private long playTime = 0;
	private static int SIGN_UP_NORMAL = 0;
	private static int SIGN_UP_NOCONTENT = 1;
	private MediaPlayer mediaPlayer;
	private long voiceLength;
	private long countdown;
	private AnimationDrawable animationPlay;
	private JoinedKKDao joinedKKDao = null;
	private LinearLayout llBack;
	private List<JoinedKk> joinedKks = new ArrayList<JoinedKk>();
	private int pageNo = 1;
	private int nextPage = -1;
	private boolean isPause = false;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AppConstantValues.UPLOAD_AUDIO_SECCESS:
				result = (String) msg.obj;
				parseUploadAudio(result);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		context = this;
		
		currentUser = UserManager.getInstance().getCurrentUser();
		
		initViews();
		setListener();
		initRoomInfo();
		LiveRoomDataUpdate.getInstance().setDataUpdateListener(this);
	}
	
	@Override
	public void initViews() {
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		rlJoinSex = (RelativeLayout) findViewById(R.id.rl_join_sex);
		rlArea = (RelativeLayout) findViewById(R.id.rl_area);
		rlJoinTime = (RelativeLayout) findViewById(R.id.rl_join_time);
		rlJoinPrice = (RelativeLayout) findViewById(R.id.rl_join_price);
		llRecordingAudio = (LinearLayout) findViewById(R.id.ll_recording_audio);
		btnSignupCommit = (Button) findViewById(R.id.btn_signup_commit);
		tvSignupTime = (TextView) findViewById(R.id.tv_signup_time);
		rlPlayAudio = (RelativeLayout) findViewById(R.id.rl_play_audio);
		tvAudioTime = (TextView) findViewById(R.id.tv_audio_time);
		tvRepeatRecord = (TextView) findViewById(R.id.tv_repeat_record);
		tvDeleteAudio = (TextView) findViewById(R.id.tv_delete_audio);
		rlRecordFinished = (RelativeLayout) findViewById(R.id.rl_record_finished);
		rlRecord = (RelativeLayout) findViewById(R.id.rl_record);
		ivPlayAnimation = (ImageView) findViewById(R.id.iv_play_animation);
		tvDataNullPrompt = (TextView) findViewById(R.id.tv_data_null_prompt);
		tvKcoin = (TextView) findViewById(R.id.tv_kcoin);
		tvKcoin.setText(KApplication.coin + "");
		llSignupJoinTab = (LinearLayout) findViewById(R.id.ll_signup_join_tab);
		llSignupJoinedTab = (LinearLayout) findViewById(R.id.ll_signup_joined_tab);
		llSignupJoinTab.setSelected(true);
		llSignupJoin = (LinearLayout) findViewById(R.id.ll_signup_join);
		rlJoinedKkuai = (RelativeLayout) findViewById(R.id.rl_joined_kkuai);
		pullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list_view);
		pullRefreshListView.setOnRefreshListener(this);
		lvJoinedKk = pullRefreshListView.getRefreshableView();
		joinedKkAdapter = new JoinedKkAdapter(context);
		lvJoinedKk.setAdapter(joinedKkAdapter);
	}
	
	@Override
	public void setListener() {
		llBack.setOnClickListener(this);
		rlJoinTime.setOnClickListener(this);
		llSignupJoinTab.setOnClickListener(this);
		llSignupJoinedTab.setOnClickListener(this);
		llRecordingAudio.setOnClickListener(this);
		btnSignupCommit.setOnClickListener(this);
		rlPlayAudio.setOnClickListener(this);
		tvRepeatRecord.setOnClickListener(this);
		tvDeleteAudio.setOnClickListener(this);
	}
	
	/**
	 * init room info 
	 */
	private void initRoomInfo() {
		if ("1".equals(currentUser.getSex())) {
			roomId = AppConstantValues.ROOM_FEMALE_ROOM;
		} else {
			roomId = AppConstantValues.ROOM_MALE_ROOM;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case AppConstantValues.DIALOG_SIGNUP_RECORD:
			DialogJoinRecord dialogJoinRecord = new DialogJoinRecord(context);
			dialogJoinRecord.setCanceledOnTouchOutside(false);
			dialogJoinRecord.setFinishListener(this);
			return dialogJoinRecord;
		case AppConstantValues.DIALOG_SINGLE_CONFIRM:
			DialogSingleConfirm singleConfirm = new DialogSingleConfirm(context);
			singleConfirm.setContent(getString(R.string.dialog_signup_prompt));
			singleConfirm.setClickListener(this);
			return singleConfirm;
		case AppConstantValues.DIALOG_LOADING:
			DialogLoading dialogLoading = new DialogLoading(context);
			return dialogLoading;
		case AppConstantValues.DIALOG_NORMAL:
			DialogNormal dialogNormal = new DialogNormal(context);
			dialogNormal.setDialogOnClickOkListener(this);
			dialogNormal.setContent(getString(R.string.signup_delete_voice_prompt));
			return dialogNormal;

		default:
			break;
		}
		return super.onCreateDialog(id);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			joinedKkAdapter.release();
			setResult(AppConstantValues.REQUEST_CODE_UPDATE_JOIN_TIME);
			finish();
			break;
		case R.id.rl_join_sex:
			
			break;
		case R.id.rl_area:
			
			break;
		case R.id.rl_join_time:
			Intent intent = new Intent(context, SelectJoinTime.class);
			intent.putExtra("roomId", roomId);
			startActivityForResult(intent, 1000);
			break;
		case R.id.rl_join_price:
			
			break;
		case R.id.ll_signup_join_tab:
			llSignupJoin.setVisibility(View.VISIBLE);
			rlJoinedKkuai.setVisibility(View.GONE);
			llSignupJoinTab.setSelected(true);
			llSignupJoinedTab.setSelected(false);
			joinedKkAdapter.stop();
			break;
		case R.id.ll_signup_joined_tab:
			llSignupJoin.setVisibility(View.GONE);
			rlJoinedKkuai.setVisibility(View.VISIBLE);
			llSignupJoinTab.setSelected(false);
			llSignupJoinedTab.setSelected(true);
			if (isFirstLoadJoined) {
				onRefresh();
				isFirstLoadJoined = false;
			}
			break;
		case R.id.ll_recording_audio:
			showDialog(AppConstantValues.DIALOG_SIGNUP_RECORD);
			break;
		case R.id.rl_play_audio:
			if (null != mediaPlayer && mediaPlayer.isPlaying()) {
				pauseVoice();
				isPause = true;
			} else if (isPause) {
				isPause = false;
				mediaPlayer.start();
				animationPlay.start();
				handler.postDelayed(runnable, 0);
			} else {
				playAudio(audioFilePath);
			}
			break;
		case R.id.tv_repeat_record:
			showDialog(AppConstantValues.DIALOG_SIGNUP_RECORD);
			if (null != audioFilePath) {
				File file = new File(audioFilePath);
				file.delete();
			}
			pauseVoice();
			break;
		case R.id.tv_delete_audio:
			showDialog(AppConstantValues.DIALOG_NORMAL);
			break;
		case R.id.btn_signup_commit:
			if (KApplication.coin < 50) {
				ShowToastUtil.showToast(context, getString(R.string.toast_coin_Inadequate));
				return ;
			}
			if (TextUtils.isEmpty(audioFilePath)) {
				roomSignUp(null, SIGN_UP_NOCONTENT);
				return ;
			}
			
//			UploadFile uploadFile = new UploadFile();
//			uploadFile.setHandler(handler);
//			uploadFile.executeUploadFile(audioFilePath, AppConstantValues.UPLOAD_FILE_TYPE_AUDIO);
			
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("fileType", "1");
			params.put("token", Preference.getToken());
			String uploadFileUrl = "http://www.xinxiannv.com/api/uploadfile";
			J_NetManager.getInstance().uploadFile(uploadFileUrl, params, audioFilePath, 1, new OnLoadingListener() {
				
				@Override
				public void startLoading() {
					
				}
				
				@Override
				public void onfinishLoading(String result) {
					parseUploadAudio(result);
				}
				
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					
				}
				
				@Override
				public void onError(HttpException error, String msg) {
					
				}
			});
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1000) {
			playTime = data.getLongExtra("playTime", 0);
			String millsConvertDateMinute;
			if (playTime == 0) {
				millsConvertDateMinute = "";
			} else {
				millsConvertDateMinute = DateUtil.millsConvertDateMinute(playTime);
			}
			tvSignupTime.setText(millsConvertDateMinute);
		}
	}
	
	private void roomSignUp(String fid, int type) {
		if (playTime == 0) {
			ShowToastUtil.showToast(context, getString(R.string.signup_not_selected_time));
			return;
		}
		
		RoomSignUpRequest request = new RoomSignUpRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				parseSignUp((String)result);
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		if (type == SIGN_UP_NORMAL) {
			request.requestRoomSignUp(roomId, DateUtil.millsConvertDateStr(playTime), "180", 
					AppConstantValues.PLAY_CONTENT_TYPE_AUDIO, fid);
		} else if (type == SIGN_UP_NOCONTENT) {
			request.requestRoomSignUpNoContent(roomId, DateUtil.millsConvertDateStr(playTime), "180", 
					AppConstantValues.PLAY_CONTENT_TYPE_AUDIO);
		}
		
		if (null == joinedKKDao) {
			joinedKKDao = new JoinedKKDao(context);
		}
		joinedKKDao.insert(playTime, roomId);
	}
	
	private void parseSignUp(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			String status = JsonUtil.getJsonString(obj, "status");
			if ("1".equals(status)) {
				showDialog(AppConstantValues.DIALOG_SINGLE_CONFIRM);
				if (KApplication.coin - 50 >= 0) {
					KApplication.coin -= 50;
				}
				tvKcoin.setText(KApplication.coin + "");
			} else if ("2".equals(status)) {
				String statusDetail = JsonUtil.getJsonString(obj, "statusDetail");
				ShowToastUtil.showToast(context, statusDetail);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * request joined kk
	 */
	private void requestJoinedKK() {
		showDialog(AppConstantValues.DIALOG_LOADING);
		OwnerJoinedKKRequest request = new OwnerJoinedKKRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				dismissDialog(AppConstantValues.DIALOG_LOADING);
				pullRefreshListView.onRefreshComplete();
				List<JoinedKk> joineds = parseJoinedKk((String)result);
				joinedKks.addAll(joineds);
				if (joinedKks.size() == 0) {
					tvDataNullPrompt.setVisibility(View.VISIBLE);
				} else {
					tvDataNullPrompt.setVisibility(View.GONE);
					joinedKkAdapter.setJoinedKks(joinedKks);
					joinedKkAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onError(int Error) {
				dismissDialog(AppConstantValues.DIALOG_LOADING);
				pullRefreshListView.onRefreshComplete();
			}
		});
		
		request.requestOwnerJoinedKK(pageNo + "");
	}
	
	@Override
	public void onRecordFinish(String filePath, long audioLength) {
		audioFilePath = filePath;
		this.audioLength = audioLength;
		log.info("filePath ======================== " + filePath);
		
		rlRecord.setVisibility(View.GONE);
		rlRecordFinished.setVisibility(View.VISIBLE);
		
		tvAudioTime.setText(audioLength + "s");
	}
	
	private void parseUploadAudio(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			String status = JsonUtil.getJsonString(obj, "status");
			if ("1".equals(status)) {
				String fid = JsonUtil.getJsonString(obj, "fid");
				roomSignUp(fid, SIGN_UP_NORMAL);
			}
		} catch (JSONException e) {
			log.error("parse upload audio", e);
		}
	}
	
	private void playAudio(String audioPath) {
		if (TextUtils.isEmpty(audioPath)) {
			return ;
		}
		if (null == mediaPlayer) {
			mediaPlayer = new MediaPlayer();
		}
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(audioPath);
			mediaPlayer.prepare();
			voiceLength = mediaPlayer.getDuration() / 1000;
			countdown = voiceLength;
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					animationPlay.stop();
					tvAudioTime.setText(audioLength + "s");
					ivPlayAnimation.setBackgroundResource(R.drawable.signup_play_3);
					countdown = voiceLength;
				}
			});
			mediaPlayer.start();
			handler.postDelayed(runnable, 0);
			ivPlayAnimation.setBackgroundResource(R.anim.signup_play_spectrum);
			animationPlay = (AnimationDrawable) ivPlayAnimation.getBackground();
			animationPlay.start();
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
	
	Runnable runnable = new Runnable() {  
        @Override  
        public void run() {
        	countdown--;
        	tvAudioTime.setText(countdown + "s");
        	if (countdown <= 0) {
        		handler.removeCallbacks(this);
			} else {
				handler.postDelayed(this, 1000);  
			}
        }  
    };
    
    public void onOk() {
    	isFirstLoadJoined = false;
    	llSignupJoinedTab.performClick();
    	onRefresh();
    }
    
    @Override
    public void onBackPressed() {
    	setResult(AppConstantValues.REQUEST_CODE_UPDATE_JOIN_TIME);
    	super.onBackPressed();
    	joinedKkAdapter.release();
    }
    
    @Override
    public void dataUpdate(int type, Object data) {
    	if (type == 10) {
    		onRefresh();
		}
    }
    
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
    	onRefresh();
    }
    
    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
    	pageNo++;
    	requestJoinedKK();
    }
    
    private List<JoinedKk> parseJoinedKk(String result) {
		List<JoinedKk> joinedKks = new ArrayList<JoinedKk>();
		try {
			JSONObject obj = new JSONObject(result);
			String status = JsonUtil.getJsonString(obj, "status");
			nextPage = JsonUtil.getJsonInt(obj, "nextPage");
			if (nextPage <= pageNo) {
				pullRefreshListView.setMode(Mode.PULL_FROM_START);
			} else {
				pullRefreshListView.setMode(Mode.BOTH);
			}
			if ("1".equals(status)) {
				JSONArray lists = JsonUtil.getJsonArrayObject(obj, "list");
				for (int i = 0; i < lists.length(); i++) {
					JSONObject list = JsonUtil.getJsonObject(lists, i);
					JoinedKk joinedKk = new JoinedKk();
					joinedKk.setId(JsonUtil.getJsonString(list, "id"));
					joinedKk.setRoomId(JsonUtil.getJsonString(list, "roomId"));
					joinedKk.setPayContentType(JsonUtil.getJsonString(list, "payContentType"));
					joinedKk.setPayContent(JsonUtil.getJsonString(list, "payContent"));
					joinedKk.setPayTime(JsonUtil.getJsonString(list, "payTime"));
					joinedKk.setPayAbidanceTime(JsonUtil.getJsonString(list, "payAbidanceTime"));
					joinedKk.setPast(JsonUtil.getJsonBoolean(list, "ispast"));
					joinedKks.add(joinedKk);
				}
			}
		} catch (JSONException e) {
			log.error("parse JoinedKk", e);
		}
		return joinedKks;
	}
    
    private void onRefresh() {
    	pageNo = 1;
    	joinedKks.clear();
    	requestJoinedKK();
    }

	@Override
	public void onDialogOk() {
		pauseVoice();
		rlRecord.setVisibility(View.VISIBLE);
		rlRecordFinished.setVisibility(View.GONE);
		if (null != audioFilePath) {
			File file = new File(audioFilePath);
			file.delete();
		}
	}
	
	private void pauseVoice() {
		mediaPlayer.pause();
		animationPlay.stop();
		handler.removeCallbacks(runnable);
	}
    
}
