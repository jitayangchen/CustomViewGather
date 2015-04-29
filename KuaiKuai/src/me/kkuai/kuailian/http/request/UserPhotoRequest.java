package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class UserPhotoRequest extends K_Request {
	
	public UserPhotoRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_USER_PHOTO;
		this.URL = HttpRequestUrl.USER_PHOTO;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestUserPhoto(String uid, String pageNo) {
		addParam("uid", uid);
		addParam("pageNo", pageNo);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
