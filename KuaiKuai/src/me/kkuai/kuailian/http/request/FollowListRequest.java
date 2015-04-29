package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class FollowListRequest extends K_Request {
	
	public FollowListRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_FOLLOW_LIST;
		this.URL = HttpRequestUrl.FOLLOW_LIST;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestFollowList(String pageNo) {
		addParam("pageNo", pageNo);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
