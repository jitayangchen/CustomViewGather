package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class CancelFollowRequest extends K_Request {
	
	public CancelFollowRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_CANCEL_FOLLOW;
		this.URL = HttpRequestUrl.CANCEL_FOLLOW;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestCancelFollow(String fuid) {
		addParam("fuid", fuid);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
