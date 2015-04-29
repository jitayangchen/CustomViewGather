package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class LiveRoomDataRequest extends K_Request {
	
	public LiveRoomDataRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_LIVE_ROOM_DATA;
		this.URL = HttpRequestUrl.LIVE_ROOM_DATA;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestRoomData(String roomId) {
		addParam("room_id", roomId);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
