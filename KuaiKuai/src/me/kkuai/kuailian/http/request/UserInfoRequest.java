package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestUrl;
import me.kkuai.kuailian.user.UserInfo;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class UserInfoRequest extends K_Request {
	
	public UserInfoRequest(int requestType, OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = requestType;
		this.URL = HttpRequestUrl.QUERY_USER_INFO;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void getUserInfo(String uid) {
		addParam("uid", uid);
		addParam("fields", UserInfo.userAttribute());
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
