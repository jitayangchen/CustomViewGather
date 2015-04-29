package me.kkuai.kuailian.activity.owner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.adapter.BatchUploadPhotoAdapter;
import me.kkuai.kuailian.bean.OwnerPhotoBean;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.dialog.DialogLoading;
import me.kkuai.kuailian.http.UploadFile;
import me.kkuai.kuailian.http.request.AddLifeRecordRequest;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.utils.Preference;
import me.kkuai.kuailian.utils.ShowToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;
import com.kkuai.libs.managers.J_NetManager.OnLoadingListener;
import com.lidroid.xutils.exception.HttpException;

public class UploadPhoto extends BaseActivity implements OnClickListener {

	private Context context;
	private EditText etPhotoDescription;
	private GridView gvBatchUploadPhoto;
	private BatchUploadPhotoAdapter photoAdapter;
	private static String PHOTO_NAME = "_tempImage.jpg";
	private Button btnRelease;
	private List<OwnerPhotoBean> ownerPhotos = new ArrayList<OwnerPhotoBean>();
	private String filePath;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AppConstantValues.OWNERPROFILE_UPLOAD_USER_PHOTO:
				dismissDialog(AppConstantValues.DIALOG_LOADING);
				String result = (String) msg.obj;
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
		setContentView(R.layout.activity_upload_photo);
		context = this;
		
		Intent intent = getIntent();
		filePath = intent.getStringExtra("filePath");
		initViews();
		setListener();
	}
	
	@Override
	public void initViews() {
		etPhotoDescription = (EditText) findViewById(R.id.et_photo_description);
		gvBatchUploadPhoto = (GridView) findViewById(R.id.gv_batch_upload_photo);
		photoAdapter = new BatchUploadPhotoAdapter(context);
		gvBatchUploadPhoto.setAdapter(photoAdapter);
		
		btnRelease = (Button) findViewById(R.id.btn_release);
	}
	
	@Override
	public void setListener() {
		btnRelease.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_release:
			showDialog(AppConstantValues.DIALOG_LOADING);
			
//			uploadLivePhoto(filePath);
			
			UploadFile uploadFile = new UploadFile();
			uploadFile.setHandler(handler);
			uploadFile.executeUploadFile(filePath, AppConstantValues.UPLOAD_FILE_TYPE_PHOTO);
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
	
	private void parseUPloadPhoto(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has("status") && "1".equals(JsonUtil.getJsonString(obj, "status"))) {
				String fid = JsonUtil.getJsonString(obj, "fid");
				String url = JsonUtil.getJsonString(obj, "url");
				
				String etContent = etPhotoDescription.getText().toString().trim();
				String content = "";
				if (!TextUtils.isEmpty(etContent)) {
					content = etContent;
				}
				requestAddLifePhoto(fid, content);
			} else {
				ShowToastUtil.showToast(context, JsonUtil.getJsonString(obj, "statusDetail"));
			}
		} catch (JSONException e) {
			log.error("parse upload result error ", e);
		}
	}
	
	private void uploadLivePhoto(String filePath) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fileType", "0");
		params.put("token", Preference.getToken());
		String uploadFileUrl = "http://www.xinxiannv.com/api/uploadfile";
		J_NetManager.getInstance().uploadFile(uploadFileUrl, params, filePath, 0, new OnLoadingListener() {
			
			@Override
			public void startLoading() {
				
			}
			
			@Override
			public void onfinishLoading(String result) {
				log.info(result);
				parseUPloadPhoto(result);
			}
			
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				
			}
			
			@Override
			public void onError(HttpException error, String msg) {
				log.error(msg, error);
			}
		});
	}
	
	private void requestAddLifePhoto(String fids, String content) {
		AddLifeRecordRequest request = new AddLifeRecordRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				try {
					JSONObject obj = new JSONObject((String)result);
					String status = JsonUtil.getJsonString(obj, "status");
					if ("1".equals(status)) {
						setResult(4000);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestAddLifeRecord(fids, content);
	}
}
