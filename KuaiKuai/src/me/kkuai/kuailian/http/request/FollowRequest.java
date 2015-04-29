package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class FollowRequest extends K_Request {
	
	public FollowRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_FOLLOW;
		this.URL = HttpRequestUrl.FOLLOW;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestFollow(String fuid) {
		addParam("fuid", fuid);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
