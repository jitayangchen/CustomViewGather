package me.kkuai.kuailian.activity;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.login.Login;
import me.kkuai.kuailian.engine.LoginManage;
import me.kkuai.kuailian.engine.LoginManage.OnLoginSuccessListener;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.Preference;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

public class Splash extends BaseActivity implements OnClickListener {

	private Context context;
	private ViewPager viewPagerSplash;
	private Message mess;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent intent;
			switch (msg.what) {
			case 0:
				if (UserManager.getInstance().getCurrentUser() == null) {
					intent = new Intent(context, Login.class);
				} else {
					intent = new Intent(context, MainActivity.class);
				}
				startActivity(intent);
				finish();
				break;
			case 1:
				intent = new Intent(context, Login.class);
				startActivity(intent);
				finish();
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
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		
		initViews();
		setListener();
		
		goHomePage();
	}
	
	@Override
	public void initViews() {
		viewPagerSplash = (ViewPager) findViewById(R.id.viewPager_splash);
	}
	
	@Override
	public void setListener() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*case R.id.btn_immediately:
			if (Preference.isAutoLogin(context)) {
//				LoginManage loginManage = new LoginManage(context);
//				loginManage.setTel(Preference.getUserName(context));
//				loginManage.setPassWord(Preference.getPassWord(context));
//				loginManage.setLoginType("1");
//				loginManage.onLogin();
			} else {
			}
			Intent intent;
			if (UserManager.getInstance().getCurrentUser() == null) {
				intent = new Intent(context, Login.class);
			} else {
				intent = new Intent(context, MainActivity.class);
			}
			startActivity(intent);
			finish();
			break;*/

		default:
			break;
		}
	}
	
	private void goHomePage() {
		mess = handler.obtainMessage();
		if (Preference.isAutoLogin(context)) {
			mess.what = 0;
			autoLogin();
		} else {
			mess.what = 1;
			handler.sendMessageDelayed(mess, 3000);
		}
	}
	
	private void autoLogin() {
		
		final String tel = Preference.getUserName(context);
		final String password = Preference.getPassWord(context);
		
		LoginManage loginManage = new LoginManage(context);
		loginManage.onLogin(tel, password, new OnLoginSuccessListener() {
			
			@Override
			public void onLoginSuccess(String userStatus) {
				handler.sendMessageDelayed(mess, 2000);
			}
			
			@Override
			public void onLoginError() {
				Intent intent = new Intent(context, Login.class);
				startActivity(intent);
			}
		});
	}
	
}
