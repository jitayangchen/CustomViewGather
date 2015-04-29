package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class RoomUserListRequest extends K_Request {
	
	public RoomUserListRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_ROOM_USER_LIST;
		this.URL = HttpRequestUrl.ROOM_USER_LIST;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestRoomUserList(String roomId, String pageNo, String pageSize) {
		addParam("room_id", roomId);
		addParam("page_no", pageNo);
		addParam("page_size", pageSize);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
