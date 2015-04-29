package me.kkuai.kuailian.activity.owner;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.KApplication;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.activity.chat.Chat;
import me.kkuai.kuailian.activity.ownerphoto.PhotoShow;
import me.kkuai.kuailian.adapter.OwnerGalleryAlbumAdapter;
import me.kkuai.kuailian.adapter.OwnerProfileListViewAdapter;
import me.kkuai.kuailian.bean.ChatItem;
import me.kkuai.kuailian.bean.LivePhotoListBean;
import me.kkuai.kuailian.bean.LivePhotoListBean.DataList;
import me.kkuai.kuailian.bean.OptionCell;
import me.kkuai.kuailian.bean.OwnerPhotoBean;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.dialog.DialogDoubleWheel;
import me.kkuai.kuailian.dialog.DialogEditBox;
import me.kkuai.kuailian.dialog.DialogLoading;
import me.kkuai.kuailian.dialog.DialogSingleWheel;
import me.kkuai.kuailian.http.KHttpRequest;
import me.kkuai.kuailian.http.KImageLoader;
import me.kkuai.kuailian.http.RequestAjaxCallBack;
import me.kkuai.kuailian.http.request.CancelFollowRequest;
import me.kkuai.kuailian.http.request.FollowRequest;
import me.kkuai.kuailian.http.request.OwnerLifeStreamRequest;
import me.kkuai.kuailian.http.request.SendChatMsgRequest;
import me.kkuai.kuailian.http.request.UserInfoRequest;
import me.kkuai.kuailian.http.request.UserPhotoRequest;
import me.kkuai.kuailian.user.PersonalData;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.BitmapUtils;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.utils.ShowToastUtil;
import me.kkuai.kuailian.utils.Util;
import me.kkuai.kuailian.widget.HorizontalListView;
import me.kkuai.kuailian.widget.XListViewOnlyLoading;
import me.kkuai.kuailian.widget.XListViewOnlyLoading.IXListViewListener;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class OtherProfile extends BaseActivity implements OnClickListener, OnItemClickListener, IXListViewListener {
	
	private Context context;
	private XListViewOnlyLoading lvListView;
	private OwnerProfileListViewAdapter adapter;
	private OwnerGalleryAlbumAdapter galleryAlbumAdapter;
	private List<LivePhotoListBean> livePhotoLists = new ArrayList<LivePhotoListBean>();
	private TextView tvLivePhoto, tvPersonalDatum;
	private Button btnSetting;
	private ImageView ivOwnerHead;
	private LinearLayout llPersonalData, llGalleryAlbum;
	private HorizontalListView galleryAlbum;
	private String avatar;
	private UserInfo userInfo;
	private UserInfo currentUserInfo;
	private LinearLayout llBack;
	private List<View> views = new ArrayList<View>();
	private int[] viewIds;
	private KHttpRequest httpRequest;
	private DialogEditBox dialogEditBox;
	private DialogSingleWheel dialogSingleWheel;
	private DialogDoubleWheel dialogDoubltWheel;
	private String requestMethod, requestParam;
	private String currentPage = AppConstantValues.OWNER_PROFILE_LIST_PHOTO_ALBUM;
	private List<OptionCell> datas;
	private int currentSingleDialog;
	
	private KImageLoader imageLoader = KImageLoader.getInstance();
	
	private TextView tvNickName, tvBirth, tvSex, tvStature, tvEducation, tvGraduationSchool, 
						tvIncome, tvCity, tvWorkTrade, tvJob, tvMarriage, tvHousingType, tvCarType, tvWeight, tvNation, tvNativePlace, tvConstellation, tvBloodType, 
						tvAgeRange, tvHeightRange, tvAreaRange, tvEducationRequirement, tvIncomeRequirement;
	private LinearLayout headView;
	private String unfilled;
	private List<OptionCell> heightCell;
	private RelativeLayout rlChat;
	private RelativeLayout rlFollow;
	private RelativeLayout rlSayHello;
	private ImageView ivFollow;
	private TextView tvFollow;
	private LinearLayout bottomSlideLine;
	private int currentSelect = 0;
	private String uid;
	private String nickName;
	private Button btnGetKMoney;
	private TextView tvOwnerTitle;
	private int lifePhotoPageNo = 1;
	private int nextPage = -1;
	private boolean pullLoadEnable = true;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;
			switch (msg.what) {
			case AppConstantValues.OWNERPROFILE_UPLOAD_HEAD_FINISH:
				parseUploadResult(result);
				break;
			case AppConstantValues.OWNERPROFILE_UPLOAD_USER_PHOTO:
				parseUPloadPhoto(result);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_owner_profile);
		httpRequest = KHttpRequest.getInstance();
		currentUserInfo = UserManager.getInstance().getCurrentUser();
		Intent intent = getIntent();
		uid = intent.getStringExtra("uid");
		nickName = intent.getStringExtra("nickName");
		initViews();
		setListener();
		
		showDialog(AppConstantValues.DIALOG_LOADING);
		getUserInfo();
		getLivePhoto();
		getOwnerPhoto();
	}
	
	@Override
	public void initViews() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		headView = (LinearLayout) inflater.inflate(R.layout.owner_profile_headview, null, false);
		lvListView = (XListViewOnlyLoading) findViewById(R.id.lv_list_view);
		lvListView.setVerticalScrollBarEnabled(false);
		lvListView.setPullLoadEnable(true);
		lvListView.setXListViewListener(this);
		tvLivePhoto = (TextView) headView.findViewById(R.id.tv_live_photo);
		tvLivePhoto.setSelected(true);
		tvPersonalDatum = (TextView) headView.findViewById(R.id.tv_personal_datum);
		ivOwnerHead = (ImageView) headView.findViewById(R.id.iv_owner_head);
		llPersonalData = (LinearLayout) headView.findViewById(R.id.ll_personal_data);
		llGalleryAlbum = (LinearLayout) headView.findViewById(R.id.ll_gallery_album);
		galleryAlbum = (HorizontalListView) headView.findViewById(R.id.gallery_album);
		btnGetKMoney = (Button) headView.findViewById(R.id.btn_get_kuai_money);
		TextView tvKuaiMoney = (TextView) headView.findViewById(R.id.tv_kuai_money);
		btnGetKMoney.setVisibility(View.GONE);
		tvKuaiMoney.setVisibility(View.GONE);
		tvOwnerTitle = (TextView) findViewById(R.id.tv_owner_title);
		tvOwnerTitle.setText(nickName);
		
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		
		galleryAlbumAdapter = new OwnerGalleryAlbumAdapter(context);
		galleryAlbumAdapter.setOwner(false);
		galleryAlbum.setAdapter(galleryAlbumAdapter);
		lvListView.addHeaderView(headView);
		adapter = new OwnerProfileListViewAdapter(context);
		lvListView.setAdapter(adapter);
		
		btnSetting = (Button) findViewById(R.id.btn_setting);
		btnSetting.setVisibility(View.GONE);
		
		bottomSlideLine = (LinearLayout) findViewById(R.id.bottom_slide_line);
		rlChat = (RelativeLayout) findViewById(R.id.rl_chat);
		rlFollow = (RelativeLayout) findViewById(R.id.rl_follow);
		ivFollow = (ImageView) findViewById(R.id.iv_follow);
		rlSayHello = (RelativeLayout) findViewById(R.id.rl_say_hello);
		tvFollow = (TextView) findViewById(R.id.tv_follow);
		
	}
	
	private void initPersonalDateView(View headView) {
		int[] viewIds = {R.id.rl_nick_name, R.id.rl_birthday, R.id.rl_sex, R.id.rl_height, R.id.rl_education, R.id.rl_graduation_school, 
						R.id.rl_income, R.id.rl_city, R.id.rl_work_trade, R.id.rl_job, R.id.rl_marriage, R.id.rl_housing_type, 
						R.id.rl_car_type, R.id.rl_weight, R.id.rl_nation, R.id.rl_native_place, R.id.rl_constellation, R.id.rl_blood_type, 
						R.id.rl_age_range, R.id.rl_height_range, R.id.rl_area_range, R.id.rl_education_requirement, R.id.rl_income_requirement};
		this.viewIds = viewIds;
		for (int i = 0; i < viewIds.length; i++) {
			View view = headView.findViewById(viewIds[i]);
			views.add(view);
		}
		
		tvNickName = (TextView) findViewById(R.id.tv_nick_name);
		tvBirth = (TextView) findViewById(R.id.tv_birth);
		tvSex = (TextView) findViewById(R.id.tv_sex);
		tvStature = (TextView) findViewById(R.id.tv_height);
		tvEducation = (TextView) findViewById(R.id.tv_education);
		tvGraduationSchool = (TextView) findViewById(R.id.tv_graduation_school);
		tvIncome = (TextView) findViewById(R.id.tv_income);
		tvCity = (TextView) findViewById(R.id.tv_city);
		tvWorkTrade = (TextView) findViewById(R.id.tv_work_trade);
		tvJob = (TextView) findViewById(R.id.tv_job);
		tvMarriage = (TextView) findViewById(R.id.tv_marriage);
		
		tvHousingType = (TextView) findViewById(R.id.tv_housing_type);
		tvCarType = (TextView) findViewById(R.id.tv_car_type);
		tvWeight = (TextView) findViewById(R.id.tv_weight);
		tvNation = (TextView) findViewById(R.id.tv_nation);
		tvNativePlace = (TextView) findViewById(R.id.tv_native_place);
		tvConstellation = (TextView) findViewById(R.id.tv_constellation);
		tvBloodType = (TextView) findViewById(R.id.tv_blood_type);
		
		tvAgeRange = (TextView) findViewById(R.id.tv_age_range);
		tvHeightRange = (TextView) findViewById(R.id.tv_height_range);
		tvAreaRange = (TextView) findViewById(R.id.tv_area_range);
		tvEducationRequirement = (TextView) findViewById(R.id.tv_education_requirement);
		tvIncomeRequirement = (TextView) findViewById(R.id.tv_income_requirement);
		
		unfilled = getString(R.string.personal_unfilled);
	}
	
	@Override
	public void setListener() {
		tvLivePhoto.setOnClickListener(this);
		tvPersonalDatum.setOnClickListener(this);
		
		llBack.setOnClickListener(this);
		
		galleryAlbum.setOnItemClickListener(this);
		
		rlChat.setOnClickListener(this);
		rlFollow.setOnClickListener(this);
		rlSayHello.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_live_photo:
			llPersonalData.setVisibility(View.GONE);
			llGalleryAlbum.setVisibility(View.VISIBLE);
			adapter.setLivePhoto(true);
			adapter.notifyDataSetChanged();
			currentPage = AppConstantValues.OWNER_PROFILE_LIST_PHOTO_ALBUM;
			setslitherArrowhead(0);
			currentSelect = 0;
			
			tvLivePhoto.setSelected(true);
			tvPersonalDatum.setSelected(false);
			break;
		case R.id.tv_personal_datum:
			llPersonalData.setVisibility(View.VISIBLE);
			llGalleryAlbum.setVisibility(View.GONE);
			lvListView.setPullLoadEnable(false);
			adapter.setLivePhoto(false);
			adapter.notifyDataSetChanged();
			initPersonalDateView(headView);
			updateUserInfo();
			currentPage = AppConstantValues.OWNER_PROFILE_LIST_PERSONAL_DATA;
			setslitherArrowhead(1);
			currentSelect = 1;
			
			tvLivePhoto.setSelected(false);
			tvPersonalDatum.setSelected(true);
			break;
		case R.id.ll_back:
			finish();
			break;
		case R.id.rl_chat:
			Intent intent = new Intent(context, Chat.class);
			intent.putExtra("from", "OtherProfile");
			intent.putExtra("friendUid", userInfo.getId());
			intent.putExtra("nickName", userInfo.getNickName());
			intent.putExtra("sex", userInfo.getSex());
			intent.putExtra("avatar", userInfo.getAvatar());
			startActivity(intent);
			break;
		case R.id.rl_follow:
			if (userInfo.isHasFollow()) {
				requestCancelFollow(uid);
			} else {
				requestFollow(uid);
			}
			break;
		case R.id.rl_say_hello:
			ChatItem ci = new ChatItem();
			ci.setFriendUid(userInfo.getId());
			ci.setMsgType("0");
			ci.setMsgContent(currentUserInfo.getNickName() + getString(R.string.profile_say_hello_msg));
			ci.setClientUniqueId("" + System.currentTimeMillis());
			sendSystemMsg(ci);
			rlSayHello.setSelected(true);
			rlSayHello.setEnabled(false);
			break;

		default:
			break;
		}
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		KApplication.photos = galleryAlbumAdapter.getPhotoUrls();
		Intent intentPhotoShow = new Intent(context, PhotoShow.class);
		intentPhotoShow.putExtra("index", position);
		intentPhotoShow.putExtra("isOwner", false);
		startActivity(intentPhotoShow);
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
		case 1:
			
			break;

		default:
			break;
		}
	}
	
	public void uploadHead(int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, requestCode);
	}
	
	private void getLivePhoto() {
		OwnerLifeStreamRequest request = new OwnerLifeStreamRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				livePhotoLists.addAll(parseLivePhoto((String)result));
				adapter.setLivePhotoLists(livePhotoLists);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		request.requestOwnerLifeStream(uid, lifePhotoPageNo + "");
	}
	
	private void getOwnerPhoto() {
		
		UserPhotoRequest request = new UserPhotoRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				ArrayList<OwnerPhotoBean> photos = (ArrayList<OwnerPhotoBean>) result;
				galleryAlbumAdapter.setPhotoUrls(photos);
				galleryAlbumAdapter.notifyDataSetChanged();
				
				dismissDialog(AppConstantValues.DIALOG_LOADING);
			}
			
			@Override
			public void onError(int Error) {
				// TODO Auto-generated method stub
				
			}
		});
		request.requestUserPhoto(uid, "1");
	}
	
	private void parseUploadResult(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has("status") && "1".equals(JsonUtil.getJsonString(obj, "status"))) {
				avatar = JsonUtil.getJsonString(obj, "avatar");
				userInfo.setAvatar(avatar);
				setHeadImage(avatar);
			} else {
				ShowToastUtil.showToast(context, JsonUtil.getJsonString(obj, "statusDetail"));
			}
		} catch (JSONException e) {
			log.error("parse upload result error ", e);
		}
	}
	
	private void parseUPloadPhoto(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has("status") && "1".equals(JsonUtil.getJsonString(obj, "status"))) {
				String fid = JsonUtil.getJsonString(obj, "fid");
				String url = JsonUtil.getJsonString(obj, "url");
//				adapter.getPhotoUrls().add(url);
				adapter.notifyDataSetChanged();
				
				AjaxParams params = new AjaxParams();
				params.put("fids", fid);
				httpRequest.post("savephotos", params, new RequestAjaxCallBack() {
					
					@Override
					public void onExactness(Object t) {
						log.info("save photo success " + t.toString());
					}
				});
			} else {
				ShowToastUtil.showToast(context, JsonUtil.getJsonString(obj, "statusDetail"));
			}
		} catch (JSONException e) {
			log.error("parse upload result error ", e);
		}
	}
	
	private void getUserInfo() {
		UserInfoRequest userInfoRequest = new UserInfoRequest(HttpRequestTypeId.TASKID_QUERY_OTHER_USER_INFO, new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				userInfo = (UserInfo) result;
				userInfo.setId(uid);
				tvOwnerTitle.setText(userInfo.getNickName());
				setHeadImage(userInfo.getAvatar());
				
				galleryAlbumAdapter.setCurrentUserSex(userInfo.getSex());
				adapter.setCurrentUserSex(userInfo.getSex());
				
				setFollow(userInfo.isHasFollow());
				if (AppConstantValues.OWNER_PROFILE_LIST_PERSONAL_DATA.equals(currentPage)) {
					updateUserInfo();
				}
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		userInfoRequest.getUserInfo(uid);	
	}
	
//	tvNickName, tvAge, tvSex, tvStature, tvEducation, tvGraduationSchool, tvIncome, tvCity
	private void updateUserInfo() {
		if (null == userInfo) {
			return;
		}
		if (TextUtils.isEmpty(userInfo.getNickName())) {
			tvNickName.setText(unfilled);
		} else {
			tvNickName.setText(userInfo.getNickName());
		}
		if (TextUtils.isEmpty(userInfo.getBirth())) {
			tvBirth.setText(unfilled);
		} else {
			tvBirth.setText(userInfo.getBirth());
		}
		if (TextUtils.isEmpty(userInfo.getSex())) {
			tvSex.setText(unfilled);
		} else {
			tvSex.setText(PersonalData.getSexById(context, userInfo.getSex()));
		}
		if (TextUtils.isEmpty(userInfo.getHeight())) {
			tvStature.setText(unfilled);
		} else {
			tvStature.setText(userInfo.getHeight() + "cm");
		}
		if (TextUtils.isEmpty(userInfo.getEducation())) {
			tvEducation.setText(unfilled);
		} else {
			tvEducation.setText(PersonalData.getEducationById(context, userInfo.getEducation()));
		}
		if (TextUtils.isEmpty(userInfo.getSchool())) {
			tvGraduationSchool.setText(unfilled);
		} else {
			tvGraduationSchool.setText(userInfo.getSchool());
		}
		if (TextUtils.isEmpty(userInfo.getIncome())) {
			tvIncome.setText(unfilled);
		} else {
			tvIncome.setText(PersonalData.getIncomeById(context, userInfo.getIncome()));
		}
		if (TextUtils.isEmpty(userInfo.getCityId())) {
			tvCity.setText(unfilled);
		} else {
			tvCity.setText(Util.getCityNameById(userInfo.getCityId()));
		}
		if (TextUtils.isEmpty(userInfo.getWorkTrade())) {
			tvWorkTrade.setText(unfilled);
		} else {
			tvWorkTrade.setText(PersonalData.getWorkById(context, userInfo.getWorkTrade()));
		}
		if (TextUtils.isEmpty(userInfo.getJob())) {
			tvJob.setText(unfilled);
		} else {
			tvJob.setText(PersonalData.getJobById(context, userInfo.getJob()));
		}
		if (TextUtils.isEmpty(userInfo.getMarriage())) {
			tvMarriage.setText(unfilled);
		} else {
			tvMarriage.setText(PersonalData.getMarriageById(context, userInfo.getMarriage()));
		}
		if (TextUtils.isEmpty(userInfo.getHousingType())) {
			tvHousingType.setText(unfilled);
		} else {
			tvHousingType.setText(PersonalData.getHousingById(context, userInfo.getHousingType()));
		}
		if (TextUtils.isEmpty(userInfo.getCarType())) {
			tvCarType.setText(unfilled);
		} else {
			tvCarType.setText(PersonalData.getCarById(context, userInfo.getCarType()));
		}
		if (TextUtils.isEmpty(userInfo.getWeight())) {
			tvWeight.setText(unfilled);
		} else {
			tvWeight.setText(userInfo.getWeight() + "kg");
		}
		if (TextUtils.isEmpty(userInfo.getNation())) {
			tvNation.setText(unfilled);
		} else {
			tvNation.setText(PersonalData.getNationById(context, userInfo.getNation()));
		}
		if (TextUtils.isEmpty(userInfo.getNativeProvinceName()) && TextUtils.isEmpty(userInfo.getNativeCityName())) {
			tvNativePlace.setText(unfilled);
		} else {
			tvNativePlace.setText(userInfo.getNativeProvinceName() + " " + userInfo.getNativeCityName());
		}
		if (TextUtils.isEmpty(userInfo.getConstellation())) {
			tvConstellation.setText(unfilled);
		} else {
			tvConstellation.setText(PersonalData.getConstellationById(context, userInfo.getConstellation()));
		}
		if (TextUtils.isEmpty(userInfo.getBloodType())) {
			tvBloodType.setText(unfilled);
		} else {
			tvBloodType.setText(PersonalData.getBloodTypeById(context, userInfo.getBloodType()));
		}
		if (TextUtils.isEmpty(userInfo.getAgeRange())) {
			tvAgeRange.setText(unfilled);
		} else if (userInfo.getAgeRange().length() == 4) {
			String ageRange = userInfo.getAgeRange().substring(0, 2) + getString(R.string.age_unit) + "-" +
					userInfo.getAgeRange().substring(2, 4) + getString(R.string.age_unit);
			tvAgeRange.setText(ageRange);
		} else {
			tvAgeRange.setText(userInfo.getAgeRange() + getString(R.string.age_unit));
		}
		if (TextUtils.isEmpty(userInfo.getHeightRange())) {
			tvHeightRange.setText(unfilled);
		} else if (userInfo.getHeightRange().length() == 6) {
			String heightRange = userInfo.getHeightRange().substring(0, 3) + "cm - " +
					userInfo.getHeightRange().substring(3, 6) + "cm";
			tvHeightRange.setText(heightRange);
		} else {
			tvHeightRange.setText(userInfo.getHeightRange() + "cm");
		}
		if (TextUtils.isEmpty(userInfo.getAreaRange())) {
			tvAreaRange.setText(unfilled);
		} else {
			tvAreaRange.setText(Util.getCityNameById(userInfo.getAreaRange()));
		}
		if (TextUtils.isEmpty(userInfo.getEducationRequirement())) {
			tvEducationRequirement.setText(unfilled);
		} else {
			tvEducationRequirement.setText(PersonalData.getEducationById(context, userInfo.getEducationRequirement()));
		}
		if (TextUtils.isEmpty(userInfo.getIncomeRequirement())) {
			tvIncomeRequirement.setText(unfilled);
		} else {
			tvIncomeRequirement.setText(PersonalData.getIncomeById(context, userInfo.getIncomeRequirement()));
		}
		
	}
	
	private void setslitherArrowhead(int pos) {
		TranslateAnimation ta = null;
		if (pos == 0) {
			ta = new TranslateAnimation(currentSelect * bottomSlideLine.getWidth(), 0,
					0, 0);
		} else {
			ta = new TranslateAnimation(currentSelect * bottomSlideLine.getWidth(),
					bottomSlideLine.getWidth() * pos, 0, 0);
		}

		ta.setInterpolator(new AccelerateDecelerateInterpolator());
		ta.setFillAfter(true);
		ta.setDuration(300);
		bottomSlideLine.startAnimation(ta);
	}
	
	private void setHeadImage(String url) {
		Bitmap defaultImage = null;
		if (AppConstantValues.SEX_MALE.equals(userInfo.getSex())) {
			defaultImage = BitmapUtils.toRoundBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_default_male));
		} else {
			defaultImage = BitmapUtils.toRoundBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_default_female));
		}
		if (null != defaultImage) {
			ivOwnerHead.setImageBitmap(defaultImage);
		}
		imageLoader.displayRoundImage(url, ivOwnerHead);
	}
	
	private void requestFollow(String fuid) {
		FollowRequest request = new FollowRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				try {
					JSONObject obj = new JSONObject((String)result);
					String status = JsonUtil.getJsonString(obj, "status");
					if ("1".equals(status)) {
						userInfo.setHasFollow(true);
						setFollow(true);
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
	
	private void requestCancelFollow(String fuid) {
		CancelFollowRequest request = new CancelFollowRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				try {
					JSONObject obj = new JSONObject((String)result);
					String status = JsonUtil.getJsonString(obj, "status");
					if ("1".equals(status)) {
						userInfo.setHasFollow(false);
						setFollow(false);
						ShowToastUtil.showToast(context, getString(R.string.text_cancel_follow_success));
					}
				} catch (JSONException e) {
					log.error("request follow", e);
				}
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestCancelFollow(fuid);
	}
	
	private void setFollow(boolean isFollow) {
		if (isFollow) {
			rlFollow.setSelected(true);
			ivFollow.setVisibility(View.GONE);
			tvFollow.setText(R.string.cancel_follow);
		} else {
			rlFollow.setSelected(false);
			ivFollow.setVisibility(View.VISIBLE);
			tvFollow.setText(R.string.owner_text_follow);
		}
	}
	
	@Override
	public void onLoadMore() {
		getLivePhoto();
	}
	
	/**
	 * parse user photo live stream
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	private List<LivePhotoListBean> parseLivePhoto(String result) {
		List<LivePhotoListBean> livePhotoLists = new ArrayList<LivePhotoListBean>();
		try {
			JSONObject obj = new JSONObject(result);
			String status = JsonUtil.getJsonString(obj, "status");
			if ("1".equals(status)) {
				nextPage = JsonUtil.getJsonInt(obj, "nextPage");
				lvListView.stopLoadMore();
				if (nextPage <= lifePhotoPageNo) {
					lvListView.setPullLoadEnable(false);
					pullLoadEnable = false;
				} else {
					lvListView.setPullLoadEnable(true);
				}
				lifePhotoPageNo++;
				JSONArray photolists = JsonUtil.getJsonArrayObject(obj, "photolist");
				for (int i = 0; i < photolists.length(); i++) {
					LivePhotoListBean livePhotoListBean = new LivePhotoListBean();
					JSONObject photolist = JsonUtil.getJsonObject(photolists, i);
					livePhotoListBean.setNid(JsonUtil.getJsonString(photolist, "nid"));
					livePhotoListBean.setContent(JsonUtil.getJsonString(photolist, "content"));
					livePhotoListBean.setType(JsonUtil.getJsonString(photolist, "type"));
					livePhotoListBean.setTime(JsonUtil.getJsonString(photolist, "time"));
					JSONObject data = JsonUtil.getJsonObject(photolist, "data");
					livePhotoListBean.setDataType(JsonUtil.getJsonString(data, "type"));
					JSONArray datalists = JsonUtil.getJsonArrayObject(data, "datalist");
					
					ArrayList<DataList> dataLists = new ArrayList<DataList>();
					for (int j = 0; null != datalists && j < datalists.length(); j++) {
						JSONObject datalist = JsonUtil.getJsonObject(datalists, j);
						DataList dataList = new LivePhotoListBean().new DataList();
						dataList.setId(JsonUtil.getJsonString(datalist, "id"));
						dataList.setPath(JsonUtil.getJsonString(datalist, "path"));
						dataLists.add(dataList);
					}
					livePhotoListBean.setDataLists(dataLists);
					livePhotoLists.add(livePhotoListBean);
			}
			}
		} catch (JSONException e) {
			log.error("parse Live Photo", e);
		}
		return livePhotoLists;
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
    	request.requestSendChatMsg(userInfo.getId(), ci.getMsgType(), msgContentJson.toString(), "1", clientPriDataJson.toString());
    }
	
}
