package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class RegisterRequest extends K_Request {
	
	public RegisterRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_REGISTER;
		this.URL = HttpRequestUrl.REGISTER;
		setTokenField("");
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestRegister(String mobile, String password, String validCode) {
		addParam("mobile", mobile);
		addParam("password", password);
		addParam("validCode", validCode);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
