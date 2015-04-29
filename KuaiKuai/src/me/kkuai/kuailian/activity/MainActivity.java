package me.kkuai.kuailian.activity;

import java.util.List;

import me.kkuai.kuailian.KApplication;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.chat.FriendsList;
import me.kkuai.kuailian.activity.join.SignUp;
import me.kkuai.kuailian.activity.owner.OtherProfile;
import me.kkuai.kuailian.activity.owner.OwnerProfile;
import me.kkuai.kuailian.activity.room.LiveRoom;
import me.kkuai.kuailian.adapter.GalleryMainAdapter;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.db.JoinedKKDao;
import me.kkuai.kuailian.dialog.DialogLoading;
import me.kkuai.kuailian.engine.SynchronFriends;
import me.kkuai.kuailian.http.KHttpRequest;
import me.kkuai.kuailian.http.request.CheckClientUpdateRequest;
import me.kkuai.kuailian.http.request.GetAccountInfoRequest;
import me.kkuai.kuailian.http.request.HomePageDataRequest;
import me.kkuai.kuailian.http.request.KkuaiStartRequest;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.utils.DateUtil;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.utils.Util;
import me.kkuai.kuailian.widget.GalleryMain;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class MainActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	
	private Context context;
	private Button btnOwnerProfile;
	private String avatar;
	private Button ivMainChat;
	private Button btnJoin;
	private Button btnWatch;
	private RelativeLayout rlJoin, rlWatch;
	private GalleryMain galleryRecommendUser;
	private KHttpRequest httpRequest;
	private List<UserInfo> userInfos = null;
	private GalleryMainAdapter galleryMainAdapter;
	private RelativeLayout rlMainBottom;
	private TextView tvPlayTime;
	private Button btnEnterRoom;
	private long countDownTime;
	private TextView tvCountdown;
	private JoinedKKDao joinedKKDao;
	private long nextPlayTime = -1;
	private boolean isPlaying = false;
	private long playingTimeLength = 180;
	
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		avatar = intent.getStringExtra("avatar");
		
		initViews();
		setListener();
		
		httpRequest = KHttpRequest.getInstance();
		showDialog(AppConstantValues.DIALOG_LOADING);
		requestHomePageData();
//		clientUpdateCheck();
		kkuaiStart();
		
		SynchronFriends synchronFriends = new SynchronFriends(context);
		synchronFriends.synchronOfflineFriends();
		
		requestAccountInfo();
		
		showJoinedKK();
	}
	
	@Override
	public void initViews() {
		
		btnOwnerProfile = (Button) findViewById(R.id.btn_owner_profile);
		ivMainChat = (Button) findViewById(R.id.iv_main_chat);
		galleryRecommendUser = (GalleryMain) findViewById(R.id.gallery_recommend_user);
		galleryMainAdapter = new GalleryMainAdapter(context);
		galleryRecommendUser.setAdapter(galleryMainAdapter);
//		galleryRecommendUser.setSelection(1);
		galleryRecommendUser.setUnselectedAlpha(0.7f);
		btnJoin = (Button) findViewById(R.id.btn_join);
		btnWatch = (Button) findViewById(R.id.btn_watch);
		rlJoin = (RelativeLayout) findViewById(R.id.rl_join);
		rlWatch = (RelativeLayout) findViewById(R.id.rl_watch);
		
		rlMainBottom = (RelativeLayout) findViewById(R.id.rl_main_bottom);
//		rlJoinedkkCountdown = (RelativeLayout) findViewById(R.id.rl_joinedkk_countdown);
		tvPlayTime = (TextView) findViewById(R.id.tv_play_time);
		btnEnterRoom = (Button) findViewById(R.id.btn_enter_room);
		tvCountdown = (TextView) findViewById(R.id.tv_countdown);
		
	}
	
	@Override
	public void setListener() {
		btnOwnerProfile.setOnClickListener(this);
		ivMainChat.setOnClickListener(this);
		btnJoin.setOnClickListener(this);
		btnWatch.setOnClickListener(this);
		rlJoin.setOnClickListener(this);
		rlWatch.setOnClickListener(this);
		
		btnEnterRoom.setOnClickListener(this);
		
		galleryRecommendUser.setOnItemClickListener(this);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case AppConstantValues.DIALOG_LOADING:
			DialogLoading dialogLoading = new DialogLoading(context);
			return dialogLoading;

		default:
			break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_owner_profile:
			Intent intent = new Intent(context, OwnerProfile.class);
			startActivity(intent);
			break;
			
		case R.id.iv_main_chat:
			Intent chatIntent = new Intent(context, FriendsList.class);
			startActivity(chatIntent);
			break;
		case R.id.rl_join:
		case R.id.btn_join:
			Intent intentJoin = new Intent(context, SignUp.class);
			startActivityForResult(intentJoin, AppConstantValues.REQUEST_CODE_UPDATE_JOIN_TIME);
			break;
		case R.id.rl_watch:
		case R.id.btn_watch:
			Intent intentRoom = new Intent(context, LiveRoom.class);
			startActivity(intentRoom);
			break;
		case R.id.btn_enter_room:
			Intent intentOppositeSexRoom = new Intent(context, LiveRoom.class);
			intentOppositeSexRoom.putExtra("isOppositeSex", false);
			startActivity(intentOppositeSexRoom);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(context, OtherProfile.class);
		intent.putExtra("uid", userInfos.get(position).getId());
		intent.putExtra("nickName", userInfos.get(position).getNickName());
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case AppConstantValues.REQUEST_CODE_UPDATE_JOIN_TIME:
			showJoinedKK();
			break;

		default:
			break;
		}
	}
	
	private void requestHomePageData() {
		HomePageDataRequest homePageDataRequest = new HomePageDataRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				userInfos = (List<UserInfo>) result;
				galleryMainAdapter.setUserInfos(userInfos);
				galleryMainAdapter.notifyDataSetChanged();
//				galleryRecommendUser.setSelection(1);
				dismissDialog(AppConstantValues.DIALOG_LOADING);
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		homePageDataRequest.requestHomePageData("1");
	}
	
	/**
	 * check version update
	 */
	private void clientUpdateCheck() {
		CheckClientUpdateRequest request = new CheckClientUpdateRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestClientUpdateCheck(Util.getVersionName(context), "1", AppConstantValues.FROM, AppConstantValues.FROM);
	}
	
	private void kkuaiStart() {
		KkuaiStartRequest request = new KkuaiStartRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestKKuaiStart(AppConstantValues.CLIENT,
									Util.getVersionName(context), 
									AppConstantValues.FROM, 
									Util.getSysVer(), 
									Util.getMacAddress(context), 
									context.getResources().getConfiguration().locale.getLanguage(), 
									Util.getDeviceIMEI(context), 
									Util.getDeviceModel());
	}
	
	private void requestAccountInfo() {
		GetAccountInfoRequest request = new GetAccountInfoRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				try {
					JSONObject obj = new JSONObject((String) result);
					String status = JsonUtil.getJsonString(obj, "status");
					if ("1".equals(status)) {
						int coin = JsonUtil.getJsonInt(obj, "coin");
						boolean haspullcurdaycoin = JsonUtil.getJsonBoolean(obj, "haspullcurdaycoin");
						KApplication.isGetKCoin = haspullcurdaycoin;
						KApplication.coin = coin;
					}
				} catch (JSONException e) {
					log.error("request Account Info", e);
				}
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		request.requestAccountInfo();
	}
	
	private void showJoinedKK() {
		log.info("----------------showJoinedKK()------------------");
		handler.removeCallbacks(runnable);
		new AsyncTask<Object, Integer, Long>() {

			@Override
			protected Long doInBackground(Object... params) {
				if (null == joinedKKDao) {
					joinedKKDao = new JoinedKKDao(context);
				}
				long queryPlayTime = joinedKKDao.queryPlayingTime();
				log.info("queryPlayingTime()---------------- "+ queryPlayTime +" ------------------");
				if (queryPlayTime == 0) {
					queryPlayTime = joinedKKDao.queryPlayTime();
					log.info("queryPlayTime()---------------- "+ queryPlayTime +" ------------------");
					isPlaying = false;
				} else {
					isPlaying = true;
				}
				return queryPlayTime;
			}
			
			@Override
			protected void onPostExecute(Long result) {
				if (0 == result) {
					rlMainBottom.setVisibility(View.INVISIBLE);
				} else {
					rlMainBottom.setVisibility(View.VISIBLE);
					if (isPlaying) {
						countDownTime = (180*1000 - Math.abs(result - System.currentTimeMillis())) / 1000;
					} else {
						countDownTime = (result - System.currentTimeMillis()) / 1000;
					}
					
					if (isPlaying) {
						btnEnterRoom.setText(getString(R.string.main_playing));
		        		tvCountdown.setText(getString(R.string.main_playing_countdown));
					} else {
						btnEnterRoom.setText(getString(R.string.main_advance));
		        		tvCountdown.setText(getString(R.string.main_countdown));
					}
					
					handler.postDelayed(runnable, 1000);
				}
			}
		}.execute(null, null);
	}
	
	Runnable runnable = new Runnable() {  
        @Override  
        public void run() {
        	countDownTime--;
        	if (countDownTime < 0) {
//				handler.removeCallbacks(runnable);
//				rlMainBottom.setVisibility(View.INVISIBLE);
        		showJoinedKK();
			} else {
				
				String secondConvertDate = null;
				if (isPlaying) {
					secondConvertDate = DateUtil.secondConvertDate(playingTimeLength - countDownTime);
				} else {
					secondConvertDate = DateUtil.secondConvertDate(countDownTime);
				}
				tvPlayTime.setText(secondConvertDate);
	            handler.postDelayed(this, 1000);
	            
			}
        }  
    };
}
