package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class FollowerListRequest extends K_Request {
	
	public FollowerListRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_FOLLOWER_LIST;
		this.URL = HttpRequestUrl.FOLLOWER_LIST;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestFollowerList(String pageNo) {
		addParam("pageNo", pageNo);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
