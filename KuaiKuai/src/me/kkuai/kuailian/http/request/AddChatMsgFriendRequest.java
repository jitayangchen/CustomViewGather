package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class AddChatMsgFriendRequest extends K_Request {
	
	public AddChatMsgFriendRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_ADD_CHAT_MSG_FRIEND;
		this.URL = HttpRequestUrl.ADD_CHAT_MSG_FRIEND;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestAddChatMsgFriend(String fuid, String nickName, String relationStatus) {
		addParam("friend_uid", fuid);
		addParam("nick_name", nickName);
		addParam("relation_status", relationStatus);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
