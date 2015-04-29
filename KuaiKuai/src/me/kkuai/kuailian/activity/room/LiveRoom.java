package me.kkuai.kuailian.activity.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import me.kkuai.kuailian.KApplication;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.activity.chat.FriendsList;
import me.kkuai.kuailian.activity.owner.OtherProfile;
import me.kkuai.kuailian.activity.owner.OwnerProfile;
import me.kkuai.kuailian.activity.ownerphoto.PhotoShow;
import me.kkuai.kuailian.adapter.AutoViewPagerAdapter;
import me.kkuai.kuailian.adapter.LiveRoomHListViewAdapter;
import me.kkuai.kuailian.adapter.RoomMsgListAdapter;
import me.kkuai.kuailian.bean.ChatItem;
import me.kkuai.kuailian.bean.EnterRoomNewUser;
import me.kkuai.kuailian.bean.OptionCell;
import me.kkuai.kuailian.bean.OwnerPhotoBean;
import me.kkuai.kuailian.bean.RoomLiveListBean;
import me.kkuai.kuailian.bean.RoomMsg;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.db.FriendInfo;
import me.kkuai.kuailian.db.FriendsDao;
import me.kkuai.kuailian.dialog.DialogLoading;
import me.kkuai.kuailian.engine.ChatMessage;
import me.kkuai.kuailian.engine.LiveRoomDataUpdate;
import me.kkuai.kuailian.engine.LiveRoomDataUpdate.DataUpdateListener;
import me.kkuai.kuailian.engine.LiveRoomObservable;
import me.kkuai.kuailian.http.request.EnterRoomUserRequest;
import me.kkuai.kuailian.http.request.FollowRequest;
import me.kkuai.kuailian.http.request.GetRoomChatMsgRequest;
import me.kkuai.kuailian.http.request.SendChatMsgRequest;
import me.kkuai.kuailian.http.request.SendRoomMsgRequest;
import me.kkuai.kuailian.http.request.SetLeaveLiveRoomRequest;
import me.kkuai.kuailian.http.request.SetUserEnterRoomRequest;
import me.kkuai.kuailian.http.request.UserPhotoRequest;
import me.kkuai.kuailian.service.RoomService;
import me.kkuai.kuailian.user.PersonalData;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.DateUtil;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.utils.ShowToastUtil;
import me.kkuai.kuailian.utils.Util;
import me.kkuai.kuailian.widget.HorizontalListView;
import me.kkuai.kuailian.widget.KeyboardListenerLinearLayout;
import me.kkuai.kuailian.widget.KeyboardListenerLinearLayout.KeyboardListener;
import me.kkuai.kuailian.widget.autoviewpager.AutoScrollViewPager;
import me.kkuai.kuailian.widget.autoviewpager.AutoScrollViewPager.ItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class LiveRoom extends BaseActivity implements Observer, OnClickListener, OnItemClickListener, DataUpdateListener {
	
	private Context context;
	private ImageView ivExitRoom;
	private Button ivMainChat;
	private Button btnOwnerProfile;
	private UserInfo ownerInfo;
	private LinearLayout llCurrentUserInfo;
	private AutoScrollViewPager vpCurrentLivePhoto;
	private AutoViewPagerAdapter pagerAdapter;
	private String roomId;
	private TextView tvVoiceCountDown;
	private TextView tvUserNickName, tvUserAge, tvUserHeight, tvUserCity, tvUserEdu, tvUserConstellation;
	private HorizontalListView hlvOnlineUser;
	private LiveRoomHListViewAdapter hListViewAdapter;
	private EditText etReview;
	private Button btnReview, btnSendMsg;
	private ListView lvRoomComment;
	private RoomMsgListAdapter msgListAdapter;
	private ImageView ivPlaySoundAnimation, ivIconSound;
	private AnimationDrawable animationDrawable;
	private RelativeLayout rlFirstLove;
	private Button btnFollow, btnLove;
	private Intent roomService;
	private RoomLiveListBean currentPlay;
	private KeyboardListenerLinearLayout rbllBottom;
	private RelativeLayout rlBottomNormal;
	private LinearLayout llBottomShowKeyboard;
	private ArrayList<OwnerPhotoBean> currentUserPhotos;
	private boolean isMediaPlayerMute = false;
	private boolean isOppositeSex = true;
	private FriendsDao friendsDao;
	private boolean isExistFriend;
	private FriendInfo friendInfo;
	private ChatMessage chatMessage;
	private List<String> roomMsgIds = new ArrayList<String>();
	private List<RoomMsg> roomMsgList = new ArrayList<RoomMsg>();
	private int showMsgCount = 0;
	private String lastUpdateTime;
	private int showMsgTime = -1;
	private int countDownTime = 0;
	private boolean isFollow = false;
	
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_live_room);
		context = this;
		
		ownerInfo = UserManager.getInstance().getCurrentUser();
		
		Intent intent = getIntent();
		isOppositeSex = intent.getBooleanExtra("isOppositeSex", true);
		
		initViews();
		setListener();
		initRoomInfo(isOppositeSex);
		
		showDialog(AppConstantValues.DIALOG_LOADING);
		roomService = new Intent(context, RoomService.class);
		roomService.putExtra("roomId", roomId);
		startService(roomService);
		
		LiveRoomObservable.getInstance().addObserver(this);
		LiveRoomDataUpdate.getInstance().setDataUpdateListener(this);
		
		getEnterRoomUser();
		
		friendsDao = new FriendsDao(context);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		handler.postDelayed(runnable, 10);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(runnable);
	}
	
	@Override
	public void initViews() {
		ivMainChat = (Button) findViewById(R.id.iv_main_chat);
		ivExitRoom = (ImageView) findViewById(R.id.iv_exit_room);
		btnOwnerProfile = (Button) findViewById(R.id.btn_owner_profile);
		vpCurrentLivePhoto = (AutoScrollViewPager) findViewById(R.id.vp_current_live_photo);
        vpCurrentLivePhoto.setOnTouchListener(null);
        
        llCurrentUserInfo = (LinearLayout) findViewById(R.id.ll_current_user_info);
        
		pagerAdapter = new AutoViewPagerAdapter(context);
		vpCurrentLivePhoto.setInterval(1000 * 5);
		vpCurrentLivePhoto.setScrollDurationFactor(5);
		vpCurrentLivePhoto.setBorderAnimation(false);
		vpCurrentLivePhoto.setAdapter(pagerAdapter);
		tvVoiceCountDown = (TextView) findViewById(R.id.tv_voice_count_down);
		tvUserNickName = (TextView) findViewById(R.id.tv_user_nick_name);
		tvUserAge = (TextView) findViewById(R.id.tv_user_age);
		tvUserHeight = (TextView) findViewById(R.id.tv_user_height);
		tvUserCity = (TextView) findViewById(R.id.tv_user_city);
		tvUserEdu = (TextView) findViewById(R.id.tv_user_edu);
		tvUserConstellation = (TextView) findViewById(R.id.tv_user_constellation);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headView = inflater.inflate(R.layout.room_header, null, false);
		hlvOnlineUser = (HorizontalListView) headView.findViewById(R.id.hlv_online_user);
		hListViewAdapter = new LiveRoomHListViewAdapter(context);
		hListViewAdapter.setmWidth(mScreenWidth / 5 - Util.dip2px(context, 5));
		hlvOnlineUser.setAdapter(hListViewAdapter);
		hlvOnlineUser.setOnItemClickListener(this);
		
		lvRoomComment = (ListView) findViewById(R.id.lv_room_comment);
		lvRoomComment.addHeaderView(headView);
		msgListAdapter = new RoomMsgListAdapter(context);
		lvRoomComment.setAdapter(msgListAdapter);
		etReview = (EditText) findViewById(R.id.et_review);
		btnReview = (Button) findViewById(R.id.btn_review);
		btnSendMsg = (Button) findViewById(R.id.btn_send_msg);
		
		ivPlaySoundAnimation = (ImageView) findViewById(R.id.iv_play_sound_animation);
		ivPlaySoundAnimation.setBackgroundResource(R.anim.play_sound_spectrum);
		animationDrawable = (AnimationDrawable) ivPlaySoundAnimation.getBackground();
		animationDrawable.start();
		ivIconSound = (ImageView) findViewById(R.id.iv_icon_sound);
		ivIconSound.setBackgroundResource(R.drawable.icon_sound_on);
		
		rlFirstLove = (RelativeLayout) findViewById(R.id.rl_first_love);
		btnFollow = (Button) findViewById(R.id.btn_follow);
		btnLove = (Button) findViewById(R.id.btn_love);
		
		rlBottomNormal = (RelativeLayout) findViewById(R.id.rl_bottom_normal);
		llBottomShowKeyboard = (LinearLayout) findViewById(R.id.ll_bottom_show_keyboard);
		rbllBottom = (KeyboardListenerLinearLayout) findViewById(R.id.rbll_bottom);
		rbllBottom.setKeyboardListener(new KeyboardListener() {
			
			@Override
			public void keyboardShowStatus(boolean isShow) {
				if (isShow) {
					rlBottomNormal.setVisibility(View.INVISIBLE);
					llBottomShowKeyboard.setVisibility(View.VISIBLE);
				} else {
					rlBottomNormal.setVisibility(View.VISIBLE);
					llBottomShowKeyboard.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	
	/**
	 * init room info 
	 */
	private void initRoomInfo(boolean isOppositeSex) {
		if (isOppositeSex) {
			if (AppConstantValues.SEX_FEMALE.equals(ownerInfo.getSex())) {
				roomId = AppConstantValues.ROOM_MALE_ROOM;
			} else {
				roomId = AppConstantValues.ROOM_FEMALE_ROOM;
			}
		} else {
			if (AppConstantValues.SEX_MALE.equals(ownerInfo.getSex())) {
				roomId = AppConstantValues.ROOM_MALE_ROOM;
			} else {
				roomId = AppConstantValues.ROOM_FEMALE_ROOM;
			}
		}
	}
	
	@Override
	public void setListener() {
		ivMainChat.setOnClickListener(this);
		btnOwnerProfile.setOnClickListener(this);
		llCurrentUserInfo.setOnClickListener(this);
		ivExitRoom.setOnClickListener(this);
		btnReview.setOnClickListener(this);
		rlFirstLove.setOnClickListener(this);
		btnFollow.setOnClickListener(this);
		btnLove.setOnClickListener(this);
		btnSendMsg.setOnClickListener(this);
		ivIconSound.setOnClickListener(this);
		
		vpCurrentLivePhoto.setItemClickListener(new ItemClickListener() {
			
			@Override
			public void onItemClick(int position) {
				KApplication.photos = currentUserPhotos;
				Intent intent = new Intent(context, PhotoShow.class);
				intent.putExtra("index", position);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_exit_room:
			exitLiveRoom();
			finish();
			break;
		case R.id.iv_main_chat:
			Intent chatIntent = new Intent(context, FriendsList.class);
			startActivity(chatIntent);
			break;
		case R.id.btn_owner_profile:
			Intent ownerIntent = new Intent(context, OwnerProfile.class);
			startActivity(ownerIntent);
			break;
		case R.id.ll_current_user_info:
			Intent otherIntent = new Intent(context, OtherProfile.class);
			otherIntent.putExtra("uid", currentPlay.getUserId());
			otherIntent.putExtra("nickName", currentPlay.getNickName());
			startActivity(otherIntent);
			break;
		case R.id.btn_review:
			etReview.setFocusable(true); 
			etReview.setFocusableInTouchMode(true); 
			etReview.requestFocus(); 
			InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			imm.showSoftInput(etReview, InputMethodManager.RESULT_SHOWN); 
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			break;
		case R.id.btn_send_msg:
			sendReview();
			etReview.setText("");
			break;
		case R.id.rl_first_love:
//			ChatItem ci = new ChatItem();
//			ci.setFriendUid(currentPlay.getUserId());
//			ci.setMsgType("0");
//			ci.setMsgContent(ownerInfo.getNickName() + getString(R.string.room_first_love_msg));
//			ci.setClientUniqueId("" + System.currentTimeMillis());
//			sendSystemMsg(ci);
			rlFirstLove.setSelected(true);
			rlFirstLove.setEnabled(false);
			KApplication.firstLoves.add(currentPlay.getUserId());
			
			String content = String.format(getString(R.string.room_first_love_msg), currentPlay.getNickName());
			String toContent = String.format(getString(R.string.room_first_love_to_msg), ownerInfo.getNickName());
			sendMessage(AppConstantValues.CHAT_MSG_TYPE_TEXT, content, toContent);
			break;
		case R.id.btn_follow:
			isFollow = true;
			requestFollow(currentPlay.getUserId());
			
			String content_f = String.format(getString(R.string.room_follow), currentPlay.getNickName());
			String toContent_f = String.format(getString(R.string.room_follow_to_msg), ownerInfo.getNickName());
			sendMessage(AppConstantValues.CHAT_MSG_TYPE_TEXT, content_f, toContent_f);
			break;
		case R.id.btn_love:
//			ChatItem cilove = new ChatItem();
//			cilove.setFriendUid(currentPlay.getUserId());
//			cilove.setMsgType("0");
//			cilove.setMsgContent(ownerInfo.getNickName() + getString(R.string.room_love_msg));
//			cilove.setClientUniqueId("" + System.currentTimeMillis());
//			sendSystemMsg(cilove);
			btnLove.setSelected(true);
			btnLove.setEnabled(false);
			KApplication.loves.add(currentPlay.getUserId());
			
			String content_l = String.format(getString(R.string.room_love_msg), currentPlay.getNickName());
			String toContent_l = String.format(getString(R.string.profile_say_hello_msg), ownerInfo.getNickName());
			sendMessage(AppConstantValues.CHAT_MSG_TYPE_TEXT, content_l, toContent_l);
			break;
		case R.id.iv_icon_sound:
			AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			if (isMediaPlayerMute) {
				audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
				isMediaPlayerMute = false;
				ivIconSound.setBackgroundResource(R.drawable.icon_sound_on);
			} else {
				audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
				isMediaPlayerMute = true;
				ivIconSound.setBackgroundResource(R.drawable.icon_sound_off);
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent;
		List<EnterRoomNewUser> newUsers = hListViewAdapter.getNewUsers();
		if (position == 4) {
			intent = new Intent(context, ShowRoomUser.class);
			intent.putExtra("roomId", roomId);
		} else {
			intent = new Intent(context, OtherProfile.class);
			intent.putExtra("uid", newUsers.get(position).getUid());
		}
		startActivity(intent);
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
	
	private void sendReview() {
		SendRoomMsgRequest request = new SendRoomMsgRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				RoomMsg roomMsg = (RoomMsg) result;
				roomMsgIds.add(roomMsg.getMsgId());
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		String msgContent = etReview.getText().toString().trim();
		if ("".equals(msgContent)) {
			ShowToastUtil.showToast(context, getString(R.string.chat_msg_is_null));
		} else {
			String signupId = currentPlay.getSignupId();
			msgListAdapter.getMsgs().add(0, "<font size='4' color='#000000'>" + ownerInfo.getNickName() + ":" + "</font>" + msgContent);
			msgListAdapter.notifyDataSetChanged();
			Util.hiddenSoftKeyborad(etReview, context);
			request.requestSendRoomMsg(roomId, signupId, "0", msgContent);
		}
	}
	
	/**
	 * get room current user info
	 * @param uid
	 */
	private void getCurrentUserPhoto(String uid) {
		UserPhotoRequest photoRequest = new UserPhotoRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				currentUserPhotos = (ArrayList<OwnerPhotoBean>) result;
				pagerAdapter.setPhotos(currentUserPhotos);
				pagerAdapter.notifyDataSetChanged();
				vpCurrentLivePhoto.startAutoScroll();
			}
			
			@Override
			public void onError(int Error) {
				// TODO Auto-generated method stub
				
			}
		});
		photoRequest.requestUserPhoto(uid, "1");
	}
	
	private void setEnterLiveRoom(String signupId) {
		
		SetUserEnterRoomRequest enterRoom = new SetUserEnterRoomRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		enterRoom.setUserEnterRoom(roomId, signupId);
	}
	
	/**
	 * set user leave room
	 */
	private void setLeaveLiveRoom() {
		
		SetLeaveLiveRoomRequest request = new SetLeaveLiveRoomRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		request.setLeaveRoom(roomId);
	}
	
	/**
	 * update current user info
	 * @param liveListBean
	 */
	private void updateCurrentUserInfo(RoomLiveListBean liveListBean) {
//		imageLoader.displayImage(liveListBean.getPhotoAvatar(), vpCurrentLivePhoto);
		tvUserNickName.setText(liveListBean.getNickName());
		tvUserAge.setText(liveListBean.getAge() + getString(R.string.age_unit));
		tvUserHeight.setText(liveListBean.getHeight() + getString(R.string.centimeter));
		String cityName = "";
		if (!TextUtils.isEmpty(liveListBean.getCityId())) {
			if (liveListBean.getCityId().length() == 4) {
				String provincial = liveListBean.getCityId().substring(0, 2);
				List<OptionCell> areaData = KApplication.getAreaData();
				OptionCell pcell = null;
				for (OptionCell optionCell : areaData) {
					if (optionCell.getId().equals(provincial)) {
						pcell = optionCell;
						break;
					}
				}
				if (pcell != null) {
					List<OptionCell> cells = pcell.getChildData();
					for (OptionCell cell : cells) {
						if (cell.getId().equals(liveListBean.getCityId())) {
							cityName = cell.getName();
							break;
						}
					}
				}
			} else {
				List<OptionCell> areaData = KApplication.getAreaData();
				for (OptionCell optionCell : areaData) {
					if (optionCell.getId().equals(liveListBean.getCityId())) {
						cityName = optionCell.getName();
						break;
					}
				}
			}
		}
		tvUserCity.setText(cityName);
		tvUserEdu.setText(PersonalData.getEducationById(context, liveListBean.getEducation()));
		tvUserConstellation.setText(PersonalData.getConstellationById(context, liveListBean.getConstellation()));
		
		for (String uid : KApplication.follows) {
			if (liveListBean.getUserId().equals(uid)) {
				btnFollow.setBackgroundResource(R.drawable.bg_btn_room_love_selector);
				btnFollow.setText(getString(R.string.text_followed));
				btnFollow.setSelected(true);
				btnFollow.setEnabled(false);
			} else {
				btnFollow.setBackgroundResource(R.drawable.bg_btn_room_follow_selector);
				btnFollow.setText(getString(R.string.room_text_follow));
				btnFollow.setSelected(false);
				btnFollow.setEnabled(true);
			}
		}
		
		for (String uid : KApplication.loves) {
			if (liveListBean.getUserId().equals(uid)) {
				btnLove.setSelected(true);
				btnLove.setEnabled(false);
			} else {
				btnLove.setSelected(false);
				btnLove.setEnabled(true);
			}
		}
		
		for (String uid : KApplication.firstLoves) {
			if (liveListBean.getUserId().equals(uid)) {
				rlFirstLove.setSelected(true);
				rlFirstLove.setEnabled(false);
			} else {
				rlFirstLove.setSelected(false);
				rlFirstLove.setEnabled(true);
			}
		}
	}
	
	/**
	 * get enter room new user
	 */
	private void getEnterRoomUser() {
		EnterRoomUserRequest roomUserRequest = new EnterRoomUserRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				parseEnterRoomUser((String)result);
			}
			
			@Override
			public void onError(int Error) {
				// TODO Auto-generated method stub
				
			}
		});
		
		roomUserRequest.requestEnterRoomUser(roomId, DateUtil.millsConvertDateStr(System.currentTimeMillis()));
	}
	
	/**
	 * parse enter live room user
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	private void parseEnterRoomUser(String result) {
		ArrayList<EnterRoomNewUser> enterRoomNewUsers = new ArrayList<EnterRoomNewUser>();
		try {
			JSONObject obj = new JSONObject(result);
			JSONObject data = JsonUtil.getJsonObject(obj, "data");
			int totalNum = JsonUtil.getJsonInt(data, "totalNum");
			JSONArray userLists = JsonUtil.getJsonArrayObject(data, "userList");
			for (int i = 0; i < userLists.length(); i++) {
				JSONObject userList = JsonUtil.getJsonObject(userLists, i);
				EnterRoomNewUser newUser = new EnterRoomNewUser();
				newUser.setUid(JsonUtil.getJsonString(userList, "uid"));
				newUser.setEnterTime(JsonUtil.getJsonString(userList, "enterTime"));
				newUser.setAvatar(JsonUtil.getJsonString(userList, "avatar"));
				enterRoomNewUsers.add(newUser);
			}
			hListViewAdapter.setNewUsers(enterRoomNewUsers);
			hListViewAdapter.setTotalNum(totalNum);
			hListViewAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			log.error("parseEnterRoomUser", e);
		}
	}
	
    @Override
    protected void onDestroy() {
//    	setLeaveLiveRoom();
    	super.onDestroy();
    }
    
    @Override
    public void update(Observable observable, Object data) {
    	String countDownTime = (String) data;
//    	tvVoiceCountDown.setVisibility(View.VISIBLE);
        tvVoiceCountDown.setText(countDownTime);
    }
    
    @Override
    public void dataUpdate(int type, Object data) {
    	switch (type) {
		case LiveRoomDataUpdate.CURRENT_PLAY_DATA:
			currentPlay = (RoomLiveListBean) data;
			updateCurrentUserInfo(currentPlay);
			getCurrentUserPhoto(currentPlay.getUserId());
			setEnterLiveRoom(currentPlay.getSignupId());
			isExistFriend = checkIsExistFriend(currentPlay.getUserId());
			
			break;
		case LiveRoomDataUpdate.LOAD_FINISH:
			dismissDialog(AppConstantValues.DIALOG_LOADING);
			handler.postDelayed(runnable, 10);
			break;
		case LiveRoomDataUpdate.UPDATE_ROOM_MESSAGE:
			showMsgReview((RoomMsg) data);
			break;
		case LiveRoomDataUpdate.EVENT_LOGOUT:
			log.info("------ event logout ------");
			exitLiveRoom();
			break;

		default:
			break;
		}
    }
    
    private void exitLiveRoom() {
    	if (null != animationDrawable && animationDrawable.isRunning()) {
			animationDrawable.stop();
		}
    	setLeaveLiveRoom();
    	stopService(roomService);
    }
    
    @Override
    public void onBackPressed() {
    	exitLiveRoom();
    	super.onBackPressed();
    }
    
    private void requestFollow(final String fuid) {
		FollowRequest request = new FollowRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				try {
					JSONObject obj = new JSONObject((String)result);
					String status = JsonUtil.getJsonString(obj, "status");
					if ("1".equals(status)) {
						btnFollow.setSelected(true);
						btnFollow.setEnabled(false);
						btnFollow.setBackgroundResource(R.drawable.bg_btn_room_love_selector);
						btnFollow.setText(getString(R.string.text_followed));
						KApplication.follows.add(fuid);
						ShowToastUtil.showToast(context, getString(R.string.text_follow_success));
					}
				} catch (JSONException e) {
					log.error("request follow", e);
				}
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestFollow(fuid);
	}
    
    private void sendSystemMsg(ChatItem ci) {
    	SendChatMsgRequest request = new SendChatMsgRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				ShowToastUtil.showToast(context, getString(R.string.text_send_success));
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
    	JSONObject clientPriDataJson = new JSONObject();
		JSONObject msgContentJson = new JSONObject();
		try {
			if ("0".equals(ci.getMsgType())) {
				msgContentJson.put("text", ci.getMsgContent());
			}
			clientPriDataJson.put("cid", ci.getClientUniqueId());
		} catch (JSONException e) {
			log.error("client PriData Json", e);
		}
    	request.requestSendChatMsg(currentPlay.getUserId(), ci.getMsgType(), msgContentJson.toString(), "1", clientPriDataJson.toString());
    }
    
    public boolean checkIsExistFriend(String fuid) {
		boolean isExistFriend = false;
		
		FriendInfo friendInfo = friendsDao.queryFrientById(fuid);
		if (null != friendInfo) {
			isExistFriend = true;
		}
		return isExistFriend;
	}
    
    private void sendMessage(String msgType, String content, String toContent) {
		
		if (!isExistFriend) {
			log.info("create friend info where database");
			
			friendInfo = new FriendInfo();
			
			friendInfo.setUid(currentPlay.getUserId());
			friendInfo.setNickName(currentPlay.getNickName());
			if (isOppositeSex) {
				if (AppConstantValues.SEX_FEMALE.equals(ownerInfo.getSex())) {
					friendInfo.setSex(AppConstantValues.SEX_MALE);
				} else {
					friendInfo.setSex(AppConstantValues.SEX_FEMALE);
				}
			} else {
				friendInfo.setSex(ownerInfo.getSex());
			}
			friendInfo.setAvatar(currentPlay.getPhotoAvatar());
			
			friendInfo.setCreatetTime(System.currentTimeMillis());
			friendInfo.setLastUpdateTime(System.currentTimeMillis());
			friendsDao.insert(friendInfo);
			isExistFriend = true;
		}
		
		
		ChatItem ci = new ChatItem();
		ci.setFriendUid(currentPlay.getUserId());
		ci.setSenderUid(ownerInfo.getId());
		ci.setSelfUid(ownerInfo.getId());
		ci.setSendtime(System.currentTimeMillis());
		ci.setMsgType(msgType);
		ci.setMsgContent(content);
		ci.setClientUniqueId("" + System.currentTimeMillis());
		
		if (null == chatMessage) {
			chatMessage = new ChatMessage(context, currentPlay.getUserId());
		}
		
		SendChatMsgRequest request = new SendChatMsgRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				if (!isFollow) {
					ShowToastUtil.showToast(context, getString(R.string.text_send_success));
				} else {
					isFollow = false;
				}
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		JSONObject clientPriDataJson = new JSONObject();
		JSONObject msgContentJson = new JSONObject();
		try {
			if ("0".equals(ci.getMsgType())) {
				msgContentJson.put("text", content);
			}
			clientPriDataJson.put("cid", ci.getClientUniqueId());
		} catch (JSONException e) {
			log.error("client PriData Json", e);
		}
		request.requestSendChatMsg(currentPlay.getUserId(), ci.getMsgType(), msgContentJson.toString(), "1", clientPriDataJson.toString());
		chatMessage.sendRoomMessage(ci, toContent);
		
	}
    
    Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			countDownTime += 10;
			showMsgCount++;
			if (countDownTime == 10000) {
				countDownTime = 0;
				if (null == lastUpdateTime) {
					lastUpdateTime = DateUtil.millsConvertDateStr(System.currentTimeMillis());
				}
				requestRoomMsg(roomId, currentPlay.getSignupId(), lastUpdateTime);
			}
			
			if (roomMsgList.size() > 0) {
            	log.info("showMsgCount === " + showMsgCount + " ------ showMsgTime === " + showMsgTime);
            	if (showMsgCount == showMsgTime) {
            		showMsgReview(roomMsgList.remove(0));
            		showMsgCount = 0;
				}
			}
			handler.postDelayed(this, 10);
		}
	};
	
	private void requestRoomMsg(String roomId, String signupId, String lastUpdateTime) {
		GetRoomChatMsgRequest request = new GetRoomChatMsgRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				parseRoomMsg((String) result);
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestRoomChatMsg(roomId, signupId, lastUpdateTime);
	}
	
	private void parseRoomMsg(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			String status = JsonUtil.getJsonString(obj, "status");
			if ("1".equals(status)) {
				JSONArray msgLists = JsonUtil.getJsonArrayObject(obj, "msgList");
				for (int i = 0; i < msgLists.length(); i++) {
					JSONObject msgList = JsonUtil.getJsonObject(msgLists, i);
					String msgId = JsonUtil.getJsonString(msgList, "msgId");
					if (roomMsgIds.contains(msgId)) {
						continue;
					}
					roomMsgIds.add(msgId);
					
					RoomMsg roomMsg = new RoomMsg();
					roomMsg.setSendUid(JsonUtil.getJsonString(msgList, "sendUid"));
					roomMsg.setNickName(JsonUtil.getJsonString(msgList, "nickName"));
					roomMsg.setAvatar(JsonUtil.getJsonString(msgList, "avatar"));
					roomMsg.setSex(JsonUtil.getJsonString(msgList, "sex"));
					roomMsg.setId(JsonUtil.getJsonString(msgList, "id"));
					roomMsg.setMsgId(JsonUtil.getJsonString(msgList, "msgId"));
					roomMsg.setMsgType(JsonUtil.getJsonString(msgList, "msgType"));
					JSONObject msgContent = JsonUtil.getJsonObject(msgList, "msgContent");
					roomMsg.setMsgContent(JsonUtil.getJsonString(msgContent, "text"));
					roomMsg.setSendTime(JsonUtil.getJsonLong(msgList, "sendTime"));
					roomMsgList.add(0, roomMsg);
				}
				if (roomMsgList.size() > 0) {
					showMsgTime = 1000 / roomMsgList.size();
					lastUpdateTime = DateUtil.millsConvertDateStr(roomMsgList.get(roomMsgList.size() - 1).getSendTime());
				}
				showMsgCount = 0;
			}
		} catch (JSONException e) {
			log.error("parse Room Msg", e);
		}
	}
	
	private void showMsgReview(RoomMsg roomMsg) {
		msgListAdapter.getMsgs().add(0, "<font size='4' color='#000000'>" + roomMsg.getNickName() + ":" + "</font>" + roomMsg.getMsgContent());
		msgListAdapter.notifyDataSetChanged();
	}
    
}
