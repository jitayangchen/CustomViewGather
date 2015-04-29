package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class RegisterSecondRequest extends K_Request {
	
	public RegisterSecondRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_REGISTER_SECOND;
		this.URL = HttpRequestUrl.REGISTER_SECOND;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestRegisterSecond(String sex, String nickName) {
		addParam("sex", sex);
		addParam("nickName", nickName);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
