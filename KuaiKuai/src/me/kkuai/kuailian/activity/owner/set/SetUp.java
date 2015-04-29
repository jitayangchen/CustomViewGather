package me.kkuai.kuailian.activity.owner.set;

import java.io.File;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.activity.login.Login;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.dialog.DialogLoading;
import me.kkuai.kuailian.http.request.CheckClientUpdateRequest;
import me.kkuai.kuailian.service.CoreService;
import me.kkuai.kuailian.service.event.EventCenter;
import me.kkuai.kuailian.service.event.EventListener;
import me.kkuai.kuailian.service.socket.SocketPool;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.ActivityManager;
import me.kkuai.kuailian.utils.FileUtils;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.utils.Preference;
import me.kkuai.kuailian.utils.ShowToastUtil;
import me.kkuai.kuailian.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class SetUp extends BaseActivity implements OnClickListener {
	
	private Context context;
	private Button btnLogout;
	private Button btnExit;
	private LinearLayout llBack;
	private Button btnClearCache;
	private DialogLoading loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up);
		this.context = this;
		
		initViews();
		setListener();
	}
	
	public void initViews() {
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		btnLogout = (Button) findViewById(R.id.btn_logout);
		btnExit = (Button) findViewById(R.id.btn_exit);
		btnClearCache = (Button) findViewById(R.id.btn_clear_cache);
	}
	
	@Override
	public void setListener() {
		llBack.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		btnExit.setOnClickListener(this);
		btnClearCache.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.btn_logout:
			ActivityManager.getActivityManager(context).exit();
			Intent intent = new Intent(context, Login.class);
			startActivity(intent);
			Preference.setAutoLogin(context, false);
			
			UserManager.getInstance().setCurrentUser(null);
			SocketPool.getInatance(getApplicationContext()).getSocketService().stop();
			Intent service = new Intent(this, CoreService.class);
			stopService(service);
			
			EventCenter.getInstance().fireEvent(this, AppConstantValues.EVENT_LOGOUT);
			break;
		case R.id.btn_exit:
			clientUpdateCheck();
			break;
		case R.id.btn_clear_cache:
			clearCache();
			break;

		default:
			break;
		}
	}
	
	/**
	 * check version update
	 */
	private void clientUpdateCheck() {
		CheckClientUpdateRequest request = new CheckClientUpdateRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				parseCheckUpdate((String) result);
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
//		Util.getVersionName(context)
		request.requestClientUpdateCheck(Util.getVersionName(context), "1", AppConstantValues.FROM, AppConstantValues.FROM);
	}
	
	private void parseCheckUpdate(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			String status = JsonUtil.getJsonString(obj, "status");
			String statusDetail = JsonUtil.getJsonString(obj, "statusDetail");
			if ("19".equals(status)) {
				String url = JsonUtil.getJsonString(obj, "url");
				DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
				Uri uri = Uri.parse(url);
				Request request = new Request(uri);
				request.setDestinationInExternalPublicDir("kuaikuaiTemp", "Kuaikuai.apk");
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
				long id = downloadManager.enqueue(request);
			} else if ("18".equals(status)) {
				ShowToastUtil.showToast(context, statusDetail);
			}
		} catch (JSONException e) {
			log.error("parse check update", e);
		}
	}
	
	private void clearCache() {
		if (null == loading) {
			loading = new DialogLoading(context);
		}
		loading.show();
		new Thread() {
			public void run() {
				File folder = new File(FileUtils.getTempFilePath());
				File[] files = folder.listFiles();
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					file.delete();
				}
				loading.dismiss();
			};
		}.start();
	}
}
