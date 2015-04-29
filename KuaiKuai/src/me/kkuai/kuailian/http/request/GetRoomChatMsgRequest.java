package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class GetRoomChatMsgRequest extends K_Request {
	
	public GetRoomChatMsgRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_ROOM_CHAT_MSG;
		this.URL = HttpRequestUrl.ROOM_CHAT_MSG;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestRoomChatMsg(String roomId, String signupId, String lastUpdateTime) {
		addParam("room_id", roomId);
		addParam("signup_id", signupId);
		addParam("last_update_time", lastUpdateTime);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
