package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class SetUserEnterRoomRequest extends K_Request {
	
	public SetUserEnterRoomRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_SET_USER_ENTER_ROOM;
		this.URL = HttpRequestUrl.SET_USER_ENTER_ROOM;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void setUserEnterRoom(String roomId, String signupId) {
		addParam("room_id", roomId);
		addParam("signup_id", signupId);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
