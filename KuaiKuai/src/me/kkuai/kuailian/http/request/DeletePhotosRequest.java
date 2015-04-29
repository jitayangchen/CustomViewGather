package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class DeletePhotosRequest extends K_Request {
	
	public DeletePhotosRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_DELETE_PHOTOS;
		this.URL = HttpRequestUrl.DELETE_PHOTOS;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestDeletePhotos(String photoIds) {
		addParam("photoIds", photoIds);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
