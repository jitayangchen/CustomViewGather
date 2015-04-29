package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class GetAccountInfoRequest extends K_Request {
	
	public GetAccountInfoRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_GET_ACCOUNT_INFO;
		this.URL = HttpRequestUrl.GET_ACCOUNT_INFO;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestAccountInfo() {
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
