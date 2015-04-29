package me.kkuai.kuailian.engine;

import me.kkuai.kuailian.KApplication;
import me.kkuai.kuailian.activity.register.FinishRegister;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.http.request.LoginRequest;
import me.kkuai.kuailian.http.request.UserInfoRequest;
import me.kkuai.kuailian.http.response.LoginResponse;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.CoreService;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.Preference;
import android.content.Context;
import android.content.Intent;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class LoginManage {
	
	private Log log = LogFactory.getLog(LoginManage.class);
	private Context context;
	private OnLoginSuccessListener loginSuccessListener;
	
	public LoginManage(Context context) {
		this.context = context;
	}

	public void onLogin(final String tel, final String password, final OnLoginSuccessListener loginSuccessListener) {
		LoginRequest request = new LoginRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				LoginResponse response = (LoginResponse) result;
				Preference.saveToken(response.token);
				
				UserInfo userInfo = response.userInfo;
				
				if (AppConstantValues.USER_STATUS_UNPERFECT.equals(userInfo.getUserStatus())) {
					Intent intent = new Intent(context, FinishRegister.class);
					intent.putExtra("uid", userInfo.getId());
					intent.putExtra("tel", tel);
					intent.putExtra("password", password);
					context.startActivity(intent);
					loginSuccessListener.onLoginSuccess(AppConstantValues.USER_STATUS_UNPERFECT);
				} else {
					userInfo.setUserName(tel);
					userInfo.setPassword(password);
					UserManager.getInstance().setCurrentUser(userInfo);
					
					Preference.setAutoLogin(context, true);
					Preference.saveUserName(context, tel);
					Preference.savePassWord(context, password);
					Preference.saveUid(context, response.userInfo.getId());
					
					Intent service = new Intent(context, CoreService.class);
					service.putExtra("tel", tel);
					service.putExtra("password", password);
					context.stopService(service);
					context.startService(service);
					
					KApplication.refreshToken();
					getUserInfo();
					loginSuccessListener.onLoginSuccess(AppConstantValues.USER_STATUS_NORMAL);
					
				}
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.login(tel, password, "0");
	}
	
	private void getUserInfo() {
		
		UserInfoRequest userInfoRequest = new UserInfoRequest(HttpRequestTypeId.TASKID_QUERY_USER_INFO, new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				UserInfo response = (UserInfo) result;
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		UserInfo currentUser = UserManager.getInstance().getCurrentUser();
		userInfoRequest.getUserInfo(currentUser.getId());
	}
	
	public interface OnLoginSuccessListener {
		void onLoginSuccess(String userStatus);
		void onLoginError();
	}

	public void setLoginSuccessListener(OnLoginSuccessListener loginSuccessListener) {
		this.loginSuccessListener = loginSuccessListener;
	}
	
}
