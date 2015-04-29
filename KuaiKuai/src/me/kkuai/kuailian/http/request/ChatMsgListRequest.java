package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class ChatMsgListRequest extends K_Request {
	
	public ChatMsgListRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_CHAT_MSG_LIST;
		this.URL = HttpRequestUrl.CHAT_MSG_LIST;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestChatMsgList(String fuid, String searchDirection, String receiveLastMsgTime, String dataNum) {
		addParam("friend_uid", fuid);
		addParam("search_direction", searchDirection);
		addParam("receive_last_msg_time", receiveLastMsgTime);
		addParam("data_num", dataNum);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
