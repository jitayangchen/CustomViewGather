package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class RoomSignUpInfoRequest extends K_Request {
	
	public RoomSignUpInfoRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_ROOM_SIGN_UP_INFO;
		this.URL = HttpRequestUrl.ROOM_SIGN_UP_INFO;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestRoomSignUpInfo(String roomId, String timeScope, String playTimeSlot, String findTimeSlot) {
		addParam("roomId", roomId);
		addParam("timeScope", timeScope);
		addParam("playTimeSlot", playTimeSlot);
		addParam("findTimeSlot", findTimeSlot);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
