package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class OwnerLifeStreamRequest extends K_Request {
	
	public OwnerLifeStreamRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_OWNER_LIFE_STREAM;
		this.URL = HttpRequestUrl.OWNER_LIFE_STREAM;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestOwnerLifeStream(String uid, String pageNo) {
		addParam("uid", uid);
		addParam("pageNo", pageNo);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
