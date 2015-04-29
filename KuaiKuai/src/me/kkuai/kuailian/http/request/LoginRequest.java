package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class LoginRequest extends K_Request {
	
	public LoginRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_LOGIN;
		this.URL = HttpRequestUrl.LOGIN_URL;
		setTokenField("");
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void login(String userName,String password, String loginType) {
		addParam("mobile", userName);
		addParam("password", password);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
