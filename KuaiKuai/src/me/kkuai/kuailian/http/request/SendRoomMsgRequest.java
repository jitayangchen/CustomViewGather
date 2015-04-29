package me.kkuai.kuailian.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class SendRoomMsgRequest extends K_Request {
	
	public SendRoomMsgRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_SEND_ROOM_MSG;
		this.URL = HttpRequestUrl.SEND_ROOM_MSG;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestSendRoomMsg(String roomId, String signupId, String msgType, String msgContent) {
		addParam("room_id", roomId);
		addParam("signup_id", signupId);
		addParam("msg_type", msgType);
		JSONObject msgContentJson = new JSONObject();
		try {
			msgContentJson.put("text", msgContent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		addParam("msg_content", msgContentJson.toString());
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
