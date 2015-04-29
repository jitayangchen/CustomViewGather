package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class EditUserInfoRequest extends K_Request {
	
	public EditUserInfoRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_EDIT_USER_INFO;
		this.URL = HttpRequestUrl.EDIT_USER_INFO;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestEditUserInfo(String requestParam, String editContext) {
		addParam(requestParam, editContext);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
