package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;
import me.kkuai.kuailian.http.response.LoginResponse;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.Preference;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class RefreshTokenRequest extends K_Request implements OnDataBack {
	
	public RefreshTokenRequest() {
		this.callback = this;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_LOGIN;
		this.URL = HttpRequestUrl.LOGIN_URL;
		setTokenField("");
		refreshToken();
	}

	@Override
	public OnDataBack getCallback() {
		return this;
	}
	
	@Override
	public void onResponse(Object result) {
		LoginResponse response = (LoginResponse) result;
		Preference.saveToken(response.token);
	}
	
	@Override
	public void onError(int Error) {
		
	}
	
	private void refreshToken() {
		UserInfo userInfo = UserManager.getInstance().getCurrentUser();
		addParam("mobile", userInfo.getUserName());
		addParam("password", userInfo.getPassword());
		addParam("loginType", "0");
	}

}
