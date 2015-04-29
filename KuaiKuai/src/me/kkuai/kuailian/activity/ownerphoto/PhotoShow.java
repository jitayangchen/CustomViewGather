package me.kkuai.kuailian.activity.ownerphoto;

import java.util.List;

import me.kkuai.kuailian.KApplication;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.adapter.OwnerPhotoViewPagerAdapter;
import me.kkuai.kuailian.adapter.OwnerPhotoViewPagerAdapter.OnItemClickListener;
import me.kkuai.kuailian.bean.OwnerPhotoBean;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.dialog.DialogLoading;
import me.kkuai.kuailian.dialog.DialogNormal;
import me.kkuai.kuailian.dialog.DialogNormal.DialogOnClickOkListener;
import me.kkuai.kuailian.http.request.DeleteLifeRecordRequest;
import me.kkuai.kuailian.http.request.DeletePhotosRequest;
import me.kkuai.kuailian.widget.photoview.HackyViewPager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class PhotoShow extends BaseActivity implements OnClickListener, DialogOnClickOkListener {

	private Context context;
	private HackyViewPager picViewPager;
	private OwnerPhotoViewPagerAdapter viewPagerAdapter;
	private int index;
	private Button btnBack;
	private TextView tvPhotoIndex;
	private List<OwnerPhotoBean> photos;
	private RelativeLayout rlTop;
	private Button btnDeletePhoto;
	private boolean isDeletePhoto = false;
	private DialogNormal dialogNormal;
	private boolean isOwner = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_photo_show);
		
		Intent intent = getIntent();
		index = intent.getIntExtra("index", 0);
		isOwner = intent.getBooleanExtra("isOwner", false);
		
		photos = KApplication.photos;
		KApplication.photos = null;
		
		initViews();
		setListener();
		
		viewPagerAdapter.setPhotos(photos);
		viewPagerAdapter.notifyDataSetChanged();
		picViewPager.setCurrentItem(index);
	}
	
	@Override
	public void initViews() {
		picViewPager = (HackyViewPager) findViewById(R.id.vp_owner_photo);
		viewPagerAdapter = new OwnerPhotoViewPagerAdapter(context);
		picViewPager.setAdapter(viewPagerAdapter);
		btnBack = (Button) findViewById(R.id.btn_back);
		tvPhotoIndex = (TextView) findViewById(R.id.tv_photo_index);
		tvPhotoIndex.setText((index + 1) + "/" + photos.size());
		rlTop = (RelativeLayout) findViewById(R.id.rl_top);
		btnDeletePhoto = (Button) findViewById(R.id.btn_delete_photo);
		
		if (isOwner) {
			btnDeletePhoto.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void setListener() {
		
		btnBack.setOnClickListener(this);
		btnDeletePhoto.setOnClickListener(this);
		
		viewPagerAdapter.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(int position) {
				if (rlTop.isShown()) {
					rlTop.setVisibility(View.GONE);
					btnDeletePhoto.setVisibility(View.GONE);
				} else {
					rlTop.setVisibility(View.VISIBLE);
					if (isOwner) {
						btnDeletePhoto.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		
		picViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				tvPhotoIndex.setText((position + 1) + "/" + photos.size());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			if (isDeletePhoto) {
				setResult(4000);
			}
			finish();
			break;
		case R.id.btn_delete_photo:
			showDialog(AppConstantValues.DIALOG_NORMAL);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case AppConstantValues.DIALOG_NORMAL:
			dialogNormal = new DialogNormal(context);
			dialogNormal.setContent(getString(R.string.dialog_delete_photo_prompt));
			dialogNormal.setDialogOnClickOkListener(this);
			return dialogNormal;
		case AppConstantValues.DIALOG_LOADING:
			DialogLoading dialogLoading = new DialogLoading(context);
			return dialogLoading;
		default:
			break;
		}
		return super.onCreateDialog(id);
	}
	
	private void deletePhoto(String photoId) {
		DeletePhotosRequest request = new DeletePhotosRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				dismissDialog(AppConstantValues.DIALOG_LOADING);
				log.info("-----          deletePhoto             ------");
				viewPagerAdapter.setPhotos(photos);
				viewPagerAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onError(int Error) {
				dismissDialog(AppConstantValues.DIALOG_LOADING);
				
			}
		});
		
		request.requestDeletePhotos(photoId);
	}
	
	private void deleteLifePhoto(String photoId) {
		DeleteLifeRecordRequest request = new DeleteLifeRecordRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestDeleteLifeRecord(photoId);
	}
	
	@Override
	public void onBackPressed() {
		if (isDeletePhoto) {
			setResult(4000);
		}
		super.onBackPressed();
	}
	
	@Override
	public void onDialogOk() {
		isDeletePhoto = true;
		showDialog(AppConstantValues.DIALOG_LOADING);
		OwnerPhotoBean removePhoto = photos.remove(picViewPager.getCurrentItem());
		deletePhoto(removePhoto.getPid());
		deleteLifePhoto(removePhoto.getPid());
	}
}
