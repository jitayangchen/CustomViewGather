package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class AddLifeRecordRequest extends K_Request {
	
	public AddLifeRecordRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_ADD_LIFE_RECORD;
		this.URL = HttpRequestUrl.ADD_LIFE_RECORD;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestAddLifeRecord(String fids, String content) {
		addParam("fids", fids);
		addParam("content", content);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
