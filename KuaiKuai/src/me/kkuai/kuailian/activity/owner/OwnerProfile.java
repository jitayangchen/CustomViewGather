package me.kkuai.kuailian.activity.owner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.KApplication;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.activity.owner.set.SetUp;
import me.kkuai.kuailian.activity.ownerphoto.PhotoShow;
import me.kkuai.kuailian.adapter.OwnerGalleryAlbumAdapter;
import me.kkuai.kuailian.adapter.OwnerProfileListViewAdapter;
import me.kkuai.kuailian.bean.LivePhotoListBean;
import me.kkuai.kuailian.bean.LivePhotoListBean.DataList;
import me.kkuai.kuailian.bean.OptionCell;
import me.kkuai.kuailian.bean.OwnerPhotoBean;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.dialog.DialogDoubleWheel;
import me.kkuai.kuailian.dialog.DialogDoubleWheel.DialogDoubleWheelChangeListener;
import me.kkuai.kuailian.dialog.DialogEditBox;
import me.kkuai.kuailian.dialog.DialogEditBox.DialogOkListener;
import me.kkuai.kuailian.dialog.DialogLoading;
import me.kkuai.kuailian.dialog.DialogSelectAreaWheel;
import me.kkuai.kuailian.dialog.DialogSelectAreaWheel.OnSelectedCityListener;
import me.kkuai.kuailian.dialog.DialogSelectBirthdayWhell;
import me.kkuai.kuailian.dialog.DialogSelectBirthdayWhell.DialogBirthdayOkListener;
import me.kkuai.kuailian.dialog.DialogSelectCamera;
import me.kkuai.kuailian.dialog.DialogSelectCamera.OnSelectPhotoListener;
import me.kkuai.kuailian.dialog.DialogSingleWheel;
import me.kkuai.kuailian.dialog.DialogSingleWheel.DialogSingleWheelOkListener;
import me.kkuai.kuailian.http.KImageLoader;
import me.kkuai.kuailian.http.UploadFile;
import me.kkuai.kuailian.http.request.EditUserInfoRequest;
import me.kkuai.kuailian.http.request.GetAccountInfoRequest;
import me.kkuai.kuailian.http.request.OwnerLifeStreamRequest;
import me.kkuai.kuailian.http.request.PullCurrentDayCoinRequest;
import me.kkuai.kuailian.http.request.UserInfoRequest;
import me.kkuai.kuailian.http.request.UserPhotoRequest;
import me.kkuai.kuailian.user.PersonalData;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.BitmapUtils;
import me.kkuai.kuailian.utils.FileUtils;
import me.kkuai.kuailian.utils.FileUtils.CopyBitmapFinishListener;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.utils.ShowToastUtil;
import me.kkuai.kuailian.utils.Util;
import me.kkuai.kuailian.utils.cropimage.CropImageActivity;
import me.kkuai.kuailian.widget.HorizontalListView;
import me.kkuai.kuailian.widget.XListViewOnlyLoading;
import me.kkuai.kuailian.widget.XListViewOnlyLoading.IXListViewListener;
import me.kkuai.kuailian.widget.popupwindow.PopupWindowProfileSelectAge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.TextView;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class OwnerProfile extends BaseActivity implements OnClickListener, OnItemClickListener, IXListViewListener, DialogOkListener, DialogSingleWheelOkListener, DialogBirthdayOkListener, OnSelectedCityListener, DialogDoubleWheelChangeListener {
	
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
	private UserInfo currentUser;
	private LinearLayout llBack;
	private PopupWindowProfileSelectAge popupWindow;
	private List<View> views = new ArrayList<View>();
	private int[] viewIds;
	private OwnerDataOnClick ownerDataOnClick;
	private DialogEditBox dialogEditBox;
	private DialogSelectBirthdayWhell dialogSelectBirthday;
	private DialogSingleWheel dialogSingleWheel;
	private DialogDoubleWheel dialogDoubltWheel;
	private DialogSelectAreaWheel areaWheel ;
	private String requestMethod, requestParam;
	private String currentPage = AppConstantValues.OWNER_PROFILE_LIST_PHOTO_ALBUM;
	private List<OptionCell> datas;
	private int currentSingleDialog;
	private Button btnGetKMoney;
	private TextView tvKuaiCoin;
	private TextView tvOwnerTitle;
	
	private KImageLoader imageLoader = KImageLoader.getInstance();
	
	private TextView tvNickName, tvBirth, tvSex, tvStature, tvEducation, tvGraduationSchool, 
						tvIncome, tvCity, tvWorkTrade, tvJob, tvMarriage, tvHousingType, tvCarType, tvWeight, tvNation, tvNativePlace, tvConstellation, tvBloodType, 
						tvAgeRange, tvHeightRange, tvAreaRange, tvEducationRequirement, tvIncomeRequirement;
	private LinearLayout headView;
	private String unfilled;
	private List<OptionCell> heightCell;
	private LinearLayout bottomSlideLine;
	private int currentSelect = 0;
	private int lifePhotoPageNo = 1;
	private int nextPage = -1;
	private boolean pullLoadEnable = true;
	private OnSelectPhotoListenerImpl onSelectPhotoListenerImpl;
	private boolean isUploadHeadImage = false;
	private boolean isRefresh = false;
	private File cameraPath;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;
			switch (msg.what) {
			case AppConstantValues.OWNERPROFILE_UPLOAD_HEAD_FINISH:
				parseUploadResult(result);
				break;
			case AppConstantValues.OWNERPROFILE_UPLOAD_USER_PHOTO:
//				parseUPloadPhoto(result);
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
		currentUser = UserManager.getInstance().getCurrentUser();
		initViews();
		setListener();
		
		showDialog(AppConstantValues.DIALOG_LOADING);
//		getUserInfo();
		getLivePhoto();
		getOwnerPhoto();
		
		setHeadImage(currentUser.getAvatar());
		
		if (AppConstantValues.OWNER_PROFILE_LIST_PERSONAL_DATA.equals(currentPage)) {
			updateUserInfo();
		}
		
		onSelectPhotoListenerImpl = new OnSelectPhotoListenerImpl();
		tvOwnerTitle.setText(currentUser.getNickName());
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
		tvKuaiCoin = (TextView) headView.findViewById(R.id.tv_kuai_money);
		tvKuaiCoin.setText(KApplication.coin + "");
		if (KApplication.isGetKCoin) {
			btnGetKMoney.setSelected(true);
			btnGetKMoney.setEnabled(false);
			btnGetKMoney.setBackgroundResource(R.drawable.bg_btn_cancel_normal);
			btnGetKMoney.setText(R.string.personal_have_received);
		}
		tvOwnerTitle = (TextView) findViewById(R.id.tv_owner_title);
		
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		
		galleryAlbumAdapter = new OwnerGalleryAlbumAdapter(context);
		galleryAlbumAdapter.setCurrentUserSex(currentUser.getSex());
		galleryAlbumAdapter.setOwner(true);
		galleryAlbum.setAdapter(galleryAlbumAdapter);
		lvListView.addHeaderView(headView);
		adapter = new OwnerProfileListViewAdapter(context);
		adapter.setCurrentUserSex(currentUser.getSex());
		lvListView.setAdapter(adapter);
		
		popupWindow = new PopupWindowProfileSelectAge(context);
		
		btnSetting = (Button) findViewById(R.id.btn_setting);
		
		bottomSlideLine = (LinearLayout) findViewById(R.id.bottom_slide_line);
		LinearLayout llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
		llBottom.setVisibility(View.GONE);
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
			view.setOnClickListener(ownerDataOnClick);
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
		ownerDataOnClick = new OwnerDataOnClick();
		tvLivePhoto.setOnClickListener(this);
		tvPersonalDatum.setOnClickListener(this);
		btnSetting.setOnClickListener(this);
		ivOwnerHead.setOnClickListener(this);
		btnGetKMoney.setOnClickListener(this);
		
		llBack.setOnClickListener(this);
		
		galleryAlbum.setOnItemClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_setting:
			Intent intent = new Intent(context, SetUp.class);
			startActivity(intent);
			break;
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
			if (pullLoadEnable) {
				lvListView.setPullLoadEnable(true);
			}
			break;
		case R.id.tv_personal_datum:
			llPersonalData.setVisibility(View.VISIBLE);
			llGalleryAlbum.setVisibility(View.GONE);
			if (pullLoadEnable) {
				lvListView.setPullLoadEnable(false);
			}
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
		case R.id.iv_owner_head:
			isUploadHeadImage = true;
			showDialog(5);
			break;
		case R.id.ll_back:
			finish();
			break;
		case R.id.btn_get_kuai_money:
			requestKuaiCoin();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			isUploadHeadImage = false;
			showDialog(5);
		} else {
			KApplication.photos = galleryAlbumAdapter.getPhotoUrls();
			Intent intentPhotoShow = new Intent(context, PhotoShow.class);
			intentPhotoShow.putExtra("index", position - 1);
			intentPhotoShow.putExtra("isOwner", true);
			startActivityForResult(intentPhotoShow, 0);
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 3:
			dialogDoubltWheel = new DialogDoubleWheel(context);
			dialogDoubltWheel.setDialogDoubleWheelChangeListener(this);
			dialogDoubltWheel.setScreenWidth(mScreenWidth);
			return dialogDoubltWheel;
		case 1:
			dialogEditBox = new DialogEditBox(context);
			dialogEditBox.setDialogOkListener(OwnerProfile.this);
			return dialogEditBox;
		case 2:
			dialogSelectBirthday = new DialogSelectBirthdayWhell(context);
			dialogSelectBirthday.setScreenWidth(mScreenWidth);
			dialogSelectBirthday.setDialogBirthdayOkListener(OwnerProfile.this);
			return dialogSelectBirthday;
		case 0:
			dialogSingleWheel = new DialogSingleWheel(context);
			dialogSingleWheel.setDialogSingleWheelOkListener(OwnerProfile.this);
			dialogSingleWheel.setScreenWidth(mScreenWidth);
			return dialogSingleWheel;
		case 4:
			areaWheel = new DialogSelectAreaWheel(context);
			areaWheel.setScreenWidth(mScreenWidth);
			areaWheel.setOnSelectedCityListener(this);
			return areaWheel;
		case AppConstantValues.DIALOG_LOADING:
			DialogLoading dialogLoading = new DialogLoading(context);
			return dialogLoading;
		case 5:
			DialogSelectCamera selectCamera = new DialogSelectCamera(context);
			selectCamera.setOnSelectPhotoListener(onSelectPhotoListenerImpl);
			return selectCamera;

		default:
			break;
		}
		return super.onCreateDialog(id);
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
	
	public void uploadPhotoByCamera(int requestCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraPath = new File(FileUtils.getTempFilePath(), "photoTemp.jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraPath));
		startActivityForResult(intent, requestCode);
	}
	
	public void uploadPhotoByAlbum(int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000 && resultCode == RESULT_OK) {
			InputStream is;
			try {
				Uri uri = data.getData();
				is = context.getContentResolver().openInputStream(uri);
				FileUtils.copyBitmapToTempFile(is, new CopyBitmapFinishListener() {
					
					@Override
					public void copyBitmapFinish(String filePath) {
						log.info("图片路径 --- " + filePath);
						Intent intent = new Intent(context, CropImageActivity.class);
						intent.putExtra("path", filePath);
						startActivityForResult(intent, 2000);
					}
				});
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else if (requestCode == 2100 && resultCode == RESULT_OK) {
			Intent intent = new Intent(context, CropImageActivity.class);
			intent.putExtra("path", cameraPath.getAbsolutePath());
			startActivityForResult(intent, 2000);
		} else if (requestCode == 2200 && resultCode == RESULT_OK) {
			final Intent intentUpload = new Intent(context, UploadPhoto.class);
			
//			Bitmap decodeFile = BitmapFactory.decodeFile(cameraPath.getAbsolutePath());
//			log.info("decodeFile.getWidth() === " + decodeFile.getWidth() + " --- decodeFile.getHeight() === " + decodeFile.getHeight());
			
			String createBitmap = BitmapUtils.createBitmap(cameraPath.getAbsolutePath());
			intentUpload.putExtra("filePath", createBitmap);
			startActivityForResult(intentUpload, 4000);
			
//			showDialog(AppConstantValues.DIALOG_LOADING);
//			new Thread() {
//				public void run() {
////					Bitmap decodeFile = BitmapUtils.createBitmap(cameraPath.getAbsolutePath(), mScreenWidth, mScreenHeight);
//					Bitmap decodeFile = BitmapUtils.getImage(cameraPath.getAbsolutePath());
//					log.info("decodeFile.getWidth() === " + decodeFile.getWidth() + " --- decodeFile.getHeight() === " + decodeFile.getHeight());
//					File file = new File(cameraPath.getAbsolutePath());
//					try {
//						FileOutputStream fos = new FileOutputStream(file);
//						decodeFile.compress(Bitmap.CompressFormat.PNG, 100, fos);
//						fos.flush();
//						fos.close();
//					} catch (FileNotFoundException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					dismissDialog(AppConstantValues.DIALOG_LOADING);
//					intentUpload.putExtra("filePath", file.getAbsolutePath());
//					startActivityForResult(intentUpload, 4000);
//				};
//			}.start();
			
			
			
		} else if (requestCode == 2000 && resultCode == RESULT_OK) {
			String path = data.getStringExtra("path");
			uploadHeadImage(path);
		} else if (requestCode == 3000 && resultCode == RESULT_OK) {
			InputStream is;
			try {
				Uri uri = data.getData();
				is = context.getContentResolver().openInputStream(uri);
				
				Bitmap decodeFile = BitmapFactory.decodeStream(is);
				float bitmapWidth = decodeFile.getWidth();
				float bitmapHeight = decodeFile.getHeight();
				float w = bitmapWidth;
				float h = bitmapHeight;
				if (bitmapWidth > 500) {
					w = 500;
					float i = bitmapHeight / bitmapWidth;
					h = w * i;
					decodeFile = Bitmap.createScaledBitmap(decodeFile, (int)w, (int)h, true);
				}
				File file = new File(FileUtils.getTempFilePath(), FileUtils.PHOTO_NAME);
				try {
					FileOutputStream fos = new FileOutputStream(file);
					decodeFile.compress(Bitmap.CompressFormat.PNG, 100, fos);
					fos.flush();
					fos.close();
					
					Intent intentUpload = new Intent(context, UploadPhoto.class);
					intentUpload.putExtra("filePath", file.getAbsolutePath());
					startActivityForResult(intentUpload, 4000);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
//				showDialog(AppConstantValues.DIALOG_LOADING);
//				FileUtils.copyBitmapToTempFile(is, new CopyBitmapFinishListener() {
//					
//					@Override
//					public void copyBitmapFinish(final String filePath) {
//						log.info("图片路径 --- " + filePath);
//						dismissDialog(AppConstantValues.DIALOG_LOADING);
//						Intent intentUpload = new Intent(context, UploadPhoto.class);
//						intentUpload.putExtra("filePath", filePath);
//						startActivityForResult(intentUpload, 4000);
//					}
//				});
			} catch (FileNotFoundException e) {
				log.error("openInputStream ", e);
			}
		} else if (resultCode == 4000) {
			onRefresh();
		}
	}
	
	private void getLivePhoto() {
		OwnerLifeStreamRequest request = new OwnerLifeStreamRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				if (isRefresh) {
					livePhotoLists.clear();
					isRefresh = false;
				}
				livePhotoLists.addAll(parseLivePhoto((String)result));
				adapter.setLivePhotoLists(livePhotoLists);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		request.requestOwnerLifeStream(currentUser.getId(), lifePhotoPageNo + "");
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
		request.requestUserPhoto(currentUser.getId(), "1");
	}
	
	private void parseUploadResult(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has("status") && "1".equals(JsonUtil.getJsonString(obj, "status"))) {
				avatar = JsonUtil.getJsonString(obj, "avatar");
				currentUser.setAvatar(avatar);
				setHeadImage(avatar);
			} else {
				ShowToastUtil.showToast(context, JsonUtil.getJsonString(obj, "statusDetail"));
			}
		} catch (JSONException e) {
			log.error("parse upload result error ", e);
		}
	}
	
	/*private void parseUPloadPhoto(String result) {
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
	}*/
	
	private void getUserInfo() {
		UserInfoRequest userInfoRequest = new UserInfoRequest(HttpRequestTypeId.TASKID_QUERY_USER_INFO, new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				UserInfo currentUser = (UserInfo) result;
				setHeadImage(currentUser.getAvatar());
				if (AppConstantValues.OWNER_PROFILE_LIST_PERSONAL_DATA.equals(currentPage)) {
					updateUserInfo();
				}
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		userInfoRequest.getUserInfo(currentUser.getId());
	}
	
	@Override
	public void dialogBirthdayOk(String editContext) {
		requestUpdateInfo(requestParam, editContext);
	}
	
	@Override
	public void dialogOk(String editContext) {
		switch (currentSingleDialog) {
//		case value:
//			
//			break;

		default:
			break;
		}
		if ("nickName".equals(requestParam)) {
			if (!TextUtils.isEmpty(editContext) && editContext.length() > 8) {
				ShowToastUtil.showToast(context, getString(R.string.toast_nickname_exceed));
				return ;
			}
		}
		requestUpdateInfo(requestParam, editContext);
	}
	
	@Override
	public void dialogSingleWheelOk(OptionCell cell) {
		switch (currentSingleDialog) {
		case R.id.rl_sex:
			break;

		default:
			break;
		}
		requestUpdateInfo(requestParam, cell.getId());
	}
	
	@Override
	public void dialogDoubleOk(String content) {
		requestUpdateInfo(requestParam, content);
	}
	
	private void requestUpdateInfo(String requestParam, String editContext) {
		EditUserInfoRequest request = new EditUserInfoRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				getUserInfo();
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestEditUserInfo(requestParam, editContext);
	}
	
	private void updateUserInfo() {
		tvNickName.setText(currentUser.getNickName());
		tvBirth.setText(currentUser.getBirth() + "");
		if (TextUtils.isEmpty(currentUser.getSex())) {
			tvSex.setText(unfilled);
		} else {
			tvSex.setText(PersonalData.getSexById(context, currentUser.getSex()));
		}
		tvStature.setText(currentUser.getHeight() + "cm");
		if (TextUtils.isEmpty(currentUser.getEducation())) {
			tvEducation.setText(unfilled);
		} else {
			tvEducation.setText(PersonalData.getEducationById(context, currentUser.getEducation()));
		}
		tvGraduationSchool.setText(currentUser.getSchool());
		if (TextUtils.isEmpty(currentUser.getIncome())) {
			tvIncome.setText(unfilled);
		} else {
			tvIncome.setText(PersonalData.getIncomeById(context, currentUser.getIncome()));
		}
		if (TextUtils.isEmpty(currentUser.getCityId())) {
			tvCity.setText(unfilled);
		} else {
			tvCity.setText(Util.getCityNameById(currentUser.getCityId()));
		}
		if (TextUtils.isEmpty(currentUser.getWorkTrade())) {
			tvWorkTrade.setText(unfilled);
		} else {
			tvWorkTrade.setText(PersonalData.getWorkById(context, currentUser.getWorkTrade()));
		}
		if (TextUtils.isEmpty(currentUser.getJob())) {
			tvJob.setText(unfilled);
		} else {
			tvJob.setText(PersonalData.getJobById(context, currentUser.getJob()));
		}
		if (TextUtils.isEmpty(currentUser.getMarriage())) {
			tvMarriage.setText(unfilled);
		} else {
			tvMarriage.setText(PersonalData.getMarriageById(context, currentUser.getMarriage()));
		}
		if (TextUtils.isEmpty(currentUser.getHousingType())) {
			tvHousingType.setText(unfilled);
		} else {
			tvHousingType.setText(PersonalData.getHousingById(context, currentUser.getHousingType()));
		}
		if (TextUtils.isEmpty(currentUser.getCarType())) {
			tvCarType.setText(unfilled);
		} else {
			tvCarType.setText(PersonalData.getCarById(context, currentUser.getCarType()));
		}
		if (TextUtils.isEmpty(currentUser.getWeight())) {
			tvWeight.setText(unfilled);
		} else {
			tvWeight.setText(currentUser.getWeight() + "kg");
		}
		if (TextUtils.isEmpty(currentUser.getNation())) {
			tvNation.setText(unfilled);
		} else {
			tvNation.setText(PersonalData.getNationById(context, currentUser.getNation()));
		}
		if (TextUtils.isEmpty(currentUser.getNativeProvinceName())) {
			tvNativePlace.setText(unfilled);
		} else {
			tvNativePlace.setText(currentUser.getNativeProvinceName() + " " + currentUser.getNativeCityName());
		}
		if (TextUtils.isEmpty(currentUser.getConstellation())) {
			tvConstellation.setText(unfilled);
		} else {
			tvConstellation.setText(PersonalData.getConstellationById(context, currentUser.getConstellation()));
		}
		if (TextUtils.isEmpty(currentUser.getBloodType())) {
			tvBloodType.setText(unfilled);
		} else {
			tvBloodType.setText(PersonalData.getBloodTypeById(context, currentUser.getBloodType()));
		}
		if (TextUtils.isEmpty(currentUser.getAgeRange())) {
			tvAgeRange.setText(unfilled);
		} else if (currentUser.getAgeRange().length() == 4) {
			String ageRange = currentUser.getAgeRange().substring(0, 2) + getString(R.string.age_unit) + " - " +
							  currentUser.getAgeRange().substring(2, 4) + getString(R.string.age_unit);
			tvAgeRange.setText(ageRange);
		} else {
			tvAgeRange.setText(currentUser.getAgeRange() + getString(R.string.age_unit));
		}
		if (TextUtils.isEmpty(currentUser.getHeightRange())) {
			tvHeightRange.setText(unfilled);
		} else if (currentUser.getHeightRange().length() == 6) {
			String heightRange = currentUser.getHeightRange().substring(0, 3) + "cm - " +
								 currentUser.getHeightRange().substring(3, 6) + "cm";
			tvHeightRange.setText(heightRange);
		} else {
			tvHeightRange.setText(currentUser.getHeightRange() + "cm");
		}
		if (TextUtils.isEmpty(currentUser.getAreaRange())) {
			tvAreaRange.setText(unfilled);
		} else {
			tvAreaRange.setText(Util.getCityNameById(currentUser.getAreaRange()));
		}
		if (TextUtils.isEmpty(currentUser.getEducationRequirement())) {
			tvEducationRequirement.setText(unfilled);
		} else {
			tvEducationRequirement.setText(PersonalData.getEducationById(context, currentUser.getEducationRequirement()));
		}
		if (TextUtils.isEmpty(currentUser.getIncomeRequirement())) {
			tvIncomeRequirement.setText(unfilled);
		} else {
			tvIncomeRequirement.setText(PersonalData.getIncomeById(context, currentUser.getIncomeRequirement()));
		}
	}
	
	private class OwnerDataOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_nick_name:
				showDialog(1);
				dialogEditBox.setDialogTitle(getString(R.string.personal_dialog_input_nickname));
				dialogEditBox.setContent(tvNickName.getText().toString());
				requestParam = "nickName";
				break;
			case R.id.rl_birthday:
				showDialog(2);
				dialogSelectBirthday.setCurrent(currentUser.getBirth());
				requestParam = "birth";
				break;
			case R.id.rl_sex:
				requestParam = "sex";
				datas = PersonalData.getPersonalSex(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getSex());
				dialogSingleWheel.setDialogTitle(getString(R.string.toast_sex_null));
				break;
			case R.id.rl_height:
				List<OptionCell> heightCell = getHeightCell();
				showDialog(0);
				dialogSingleWheel.setDatas(heightCell);
				if (null != currentUser.getHeight() && !"".equals(currentUser.getHeight())) {
					int index = Integer.parseInt(currentUser.getHeight()) - 100;
					dialogSingleWheel.setCurrentItem(index + "");
				} else {
					dialogSingleWheel.setCurrentItem("70");
				}
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_height));
				requestParam = "height";
				break;
			case R.id.rl_education:
				requestParam = "education";
				datas = PersonalData.getPersonalEducation(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getEducation());
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_education));
				break;
			case R.id.rl_graduation_school:
				showDialog(1);
				dialogEditBox.setDialogTitle(getString(R.string.personal_dialog_input_school));
				dialogEditBox.setContent(tvGraduationSchool.getText().toString());
				requestParam = "school";
				break;
			case R.id.rl_income:
				requestParam = "income";
				datas = PersonalData.getPersonalIncome(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getIncome());
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_income));
				break;
			case R.id.rl_city:
				showDialog(4);
				areaWheel.setCurrent(currentUser.getCityId());
				requestParam = "cityId";
				areaWheel.setDialogTitle(getString(R.string.personal_dialog_select_city));
				break;
			case R.id.rl_work_trade:
				requestParam = "workTrade";
				datas = PersonalData.getPersonalWork(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getWorkTrade());
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_worktrade));
				break;
			case R.id.rl_job:
				requestParam = "job";
				datas = PersonalData.getPersonalJob(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getJob());
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_job));
				break;
			case R.id.rl_marriage:
				requestParam = "marriage";
				datas = PersonalData.getPersonalMarriage(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getMarriage());
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_marriage));
				break;
			case R.id.rl_housing_type:
				requestParam = "housingType";
				datas = PersonalData.getPersonalHousing(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getHousingType());
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_housing_type));
				break;
			case R.id.rl_car_type:
				requestParam = "carType";
				datas = PersonalData.getPersonalCar(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getCarType());
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_car_type));
				break;
			case R.id.rl_weight:
				requestParam = "weight";
				showDialog(0);
				datas = PersonalData.getPersonalWeight();
				dialogSingleWheel.setDatas(datas);
				for (int i = 0; i < datas.size(); i++) {
					if (datas.get(i).getId().equals(currentUser.getWeight())) {
						dialogSingleWheel.setCurrentItem(i+"");
						break;
					}
				}
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_weight));
				break;
			case R.id.rl_nation:
				requestParam = "nation";
				datas = PersonalData.getPersonalNation(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getNation());
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_nation));
				break;
			case R.id.rl_native_place:
				requestParam = "nativeCityId";
				showDialog(4);
				areaWheel.setCurrent(currentUser.getNativeCityId());
				areaWheel.setDialogTitle(getString(R.string.personal_dialog_select_native_place));
				break;
			case R.id.rl_constellation:
				requestParam = "constellation";
				datas = PersonalData.getPersonalConstellation(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getConstellation());
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_constellation));
				break;
			case R.id.rl_blood_type:
				requestParam = "blood";
				datas = PersonalData.getPersonalBloodType(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getBloodType());
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_blood_type));
				break;
			case R.id.rl_age_range:
				requestParam = "s_age";
				showDialog(3);
				dialogDoubltWheel.setMinAndMax(18, 99, getString(R.string.age_unit));
				dialogDoubltWheel.setCurrent(currentUser.getAgeRange(), "ageRange");
				dialogDoubltWheel.setDialogTitle(getString(R.string.personal_dialog_select_age_range));
				break;
			case R.id.rl_height_range:
				requestParam = "s_height";
				showDialog(3);
				dialogDoubltWheel.setMinAndMax(100, 300, "cm");
				dialogDoubltWheel.setCurrent(currentUser.getHeightRange(), "heightRange");
				dialogDoubltWheel.setDialogTitle(getString(R.string.personal_dialog_select_height_range));
				break;
			case R.id.rl_area_range:
				requestParam = "s_cityId";
				showDialog(4);
				areaWheel.setCurrent(currentUser.getAreaRange());
				areaWheel.setDialogTitle(getString(R.string.personal_dialog_select_area_range));
				break;
			case R.id.rl_education_requirement:
				requestParam = "s_education";
				datas = PersonalData.getPersonalEducation(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getEducationRequirement());
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_education_range));
				break;
			case R.id.rl_income_requirement:
				requestParam = "s_income";
				datas = PersonalData.getPersonalIncome(context);
				showDialog(0);
				dialogSingleWheel.setDatas(datas);
				dialogSingleWheel.setCurrentItem(currentUser.getIncomeRequirement());
				dialogSingleWheel.setDialogTitle(getString(R.string.personal_dialog_select_income_range));
				break;

			default:
				break;
			}
		}

		private List<OptionCell> getHeightCell() {
			if (null == heightCell) {
				heightCell = new ArrayList<OptionCell>();
				for (int i = 0; i < 200; i++) {
					OptionCell cell = new OptionCell();
					cell.setId(100 + i + "");
					cell.setName(100 + i + " cm");
					heightCell.add(cell);
				}
			}
			
			return heightCell;
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
		if (AppConstantValues.SEX_MALE.equals(currentUser.getSex())) {
			defaultImage = BitmapUtils.toRoundBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_default_male));
		} else {
			defaultImage = BitmapUtils.toRoundBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_default_female));
		}
		if (null != defaultImage) {
			ivOwnerHead.setImageBitmap(defaultImage);
		}
		imageLoader.displayRoundImage(url, ivOwnerHead);
	}
	
	/**
	 * pull kuai money
	 */
	private void requestKuaiCoin() {
		PullCurrentDayCoinRequest request = new PullCurrentDayCoinRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				int coin = (Integer) result;
				
				btnGetKMoney.setSelected(true);
				btnGetKMoney.setEnabled(false);
				btnGetKMoney.setBackgroundResource(R.drawable.bg_btn_cancel_normal);
				btnGetKMoney.setText(R.string.personal_have_received);
				requestAccountInfo();
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestPullCurrentDayCoin();
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
						
						tvKuaiCoin.setText(KApplication.coin + "");
						if (KApplication.isGetKCoin) {
							btnGetKMoney.setSelected(true);
							btnGetKMoney.setEnabled(false);
						}
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
	
	@Override
	public void selectedCityFinish(String cityCode) {
		requestUpdateInfo(requestParam, cityCode);
	}
	
	private void onRefresh() {
		isRefresh = true;
		lifePhotoPageNo = 1;
		getLivePhoto();
		getOwnerPhoto();
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
	
	private class OnSelectPhotoListenerImpl implements OnSelectPhotoListener {

		@Override
		public void selectPhotoAlbum() {
			if (isUploadHeadImage) {
				uploadPhotoByAlbum(1000);
			} else {
				uploadPhotoByAlbum(3000);
			}
		}

		@Override
		public void selectPhotoCamera() {
			if (isUploadHeadImage) {
				uploadPhotoByCamera(2100);
			} else {
				uploadPhotoByCamera(2200);
			}
		}
		
	}
	
	private void uploadHeadImage(String filePath) {
		UploadFile uploadFile = new UploadFile();
		uploadFile.setHandler(handler);
		uploadFile.executeUploadFile(filePath, AppConstantValues.UPLOAD_FILE_TYPE_AVATAR);
	}
	
}
