package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class SetAllOfflineMsgStatusRequest extends K_Request {
	
	public SetAllOfflineMsgStatusRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_SET_ALL_OFFLINE_MSG_STATUS;
		this.URL = HttpRequestUrl.SET_ALL_OFFLINE_MSG_STATUS;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestSetAllOfflineMsgStatus(String fuid) {
		addParam("friend_uid", fuid);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
