package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class ChatFriendListRequest extends K_Request {
	
	public ChatFriendListRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_CHAT_FRIEND_LIST;
		this.URL = HttpRequestUrl.CHAT_FRIEND_LIST;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestChatFriendList(String lastUpdateTime, String pageNo, String pageSize, String searchDirection) {
		addParam("last_update_time", lastUpdateTime);
		addParam("page_no", pageNo);
		addParam("page_size", pageSize);
		addParam("search_direction", searchDirection);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
