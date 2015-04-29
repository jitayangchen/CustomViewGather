package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class KkuaiStartRequest extends K_Request {
	
	public KkuaiStartRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_CLIENT_UPDATE_CHECK;
		this.URL = HttpRequestUrl.KKUAI_START;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestKKuaiStart(String client, String ver, String from, String sysver, String mac, String lang, String devFlag, String dev) {
		addParam("client", client);
		addParam("ver", ver);
		addParam("from", from);
		addParam("sysver", sysver);
		addParam("mac", mac);
		addParam("lang", lang);
		addParam("devFlag", devFlag);
		addParam("dev", dev);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
