package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class AfreshRoomSignupRequest extends K_Request {

	public AfreshRoomSignupRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_AFRESH_ROOM_SIGNUP;
		this.URL = HttpRequestUrl.AFRESH_ROOM_SIGNUP;
	}
	
	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestAfreshRoomSignup(String signUpId, String fid) {
		addParam("sId", signUpId);
		addParam("fId", fid);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
