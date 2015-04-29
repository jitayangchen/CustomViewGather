package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class SendChatMsgRequest extends K_Request {
	
	public SendChatMsgRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_SEND_CHAT_MSG;
		this.URL = HttpRequestUrl.SEND_CHAT_MSG;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestSendChatMsg(String toUid, String msgType, String msgContent, String clientPlat, String clientPriData) {
		addParam("toUid", toUid);
		addParam("msgType", msgType);
		addParam("msgContent", msgContent);
		addParam("clientPlat", clientPlat);
		addParam("clientPriData", clientPriData);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
