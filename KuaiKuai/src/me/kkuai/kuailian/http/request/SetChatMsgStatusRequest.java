package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class SetChatMsgStatusRequest extends K_Request {
	
	public SetChatMsgStatusRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_SET_CHAT_MSG_STATUS;
		this.URL = HttpRequestUrl.SET_CHAT_MSG_STATUS;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestSetChatMsgStatus(String statusType, String msgId) {
		addParam("type", statusType);
		addParam("msg_id", msgId);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
