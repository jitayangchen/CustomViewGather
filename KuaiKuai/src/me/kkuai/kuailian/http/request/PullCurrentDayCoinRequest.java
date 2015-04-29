package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class PullCurrentDayCoinRequest extends K_Request {
	
	public PullCurrentDayCoinRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_PULL_CURRENT_DAY_COIN;
		this.URL = HttpRequestUrl.PULL_CURRENT_DAY_COIN;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestPullCurrentDayCoin() {
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
