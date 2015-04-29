package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class OfflineChatMsgNumRequest extends K_Request {
	
	public OfflineChatMsgNumRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_OFFLINE_CHAT_MSG_NUM;
		this.URL = HttpRequestUrl.OFFLINE_CHAT_MSG_NUM;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestOfflineChatMsgNum() {
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
