package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class CheckClientUpdateRequest extends K_Request {
	
	public CheckClientUpdateRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_CLIENT_UPDATE_CHECK;
		this.URL = HttpRequestUrl.CLIENT_UPDATE_CHECK;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestClientUpdateCheck(String ver, String plat, String from, String subfrom) {
		addParam("ver", ver);
		addParam("plat", plat);
		addParam("from", from);
		addParam("subfrom", subfrom);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
