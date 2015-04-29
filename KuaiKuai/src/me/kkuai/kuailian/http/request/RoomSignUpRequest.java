package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class RoomSignUpRequest extends K_Request {
	
	public RoomSignUpRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_ROOM_SIGN_UP;
		this.URL = HttpRequestUrl.ROOM_SIGN_UP;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestRoomSignUp(String roomId, String playTime, String payAbidanceTime, String playContentType, String playContent) {
		addParam("roomId", roomId);
		addParam("playTime", playTime);
		addParam("payAbidanceTime", payAbidanceTime);
		addParam("playContentType", playContentType);
		addParam("playContent", playContent);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}
	
	public void requestRoomSignUpNoContent(String roomId, String playTime, String payAbidanceTime, String playContentType) {
		addParam("roomId", roomId);
		addParam("playTime", playTime);
		addParam("payAbidanceTime", payAbidanceTime);
		addParam("playContentType", playContentType);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
