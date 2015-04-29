package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class DeleteLifeRecordRequest extends K_Request {
	
	public DeleteLifeRecordRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_DELETE_PHOTOS;
		this.URL = HttpRequestUrl.DELETE_LIFE_RECORD;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestDeleteLifeRecord(String photoIds) {
		addParam("ids", photoIds);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
