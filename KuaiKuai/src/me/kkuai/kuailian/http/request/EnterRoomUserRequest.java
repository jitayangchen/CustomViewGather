package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class EnterRoomUserRequest extends K_Request {
	
	public EnterRoomUserRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_ENTER_ROOM_USER;
		this.URL = HttpRequestUrl.ENTER_ROOM_USER;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestEnterRoomUser(String roomId, String lastUpdateTime) {
		addParam("room_id", roomId);
		addParam("last_update_time", lastUpdateTime);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
