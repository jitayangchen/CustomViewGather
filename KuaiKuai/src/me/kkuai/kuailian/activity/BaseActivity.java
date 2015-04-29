package me.kkuai.kuailian.activity;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.ActivityManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
	public Log log;
	public int mScreenWidth;
	public int mScreenHeight;
	private ActivityManager manager = ActivityManager.getActivityManager(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log = LogFactory.getLog(this.getClass());
		manager.putActivity(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		DisplayMetrics metric = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(metric);
//		mScreenWidth = metric.widthPixels;
//		mScreenHeight = metric.heightPixels;
		
		mScreenWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		mScreenHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight();
	}
	
	public void initViews() {
		
	}
	
	public void setListener() {
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		log.info("onSaveInstanceState");
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		log.info("onRestoreInstanceState");
		UserInfo currentUser = UserManager.getInstance().getCurrentUser();
		if (null == currentUser) {
			manager.exit();
			Intent intent = new Intent(this, Splash.class);
			startActivity(intent);
		}
	}
}
