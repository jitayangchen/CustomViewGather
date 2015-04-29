package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class HomePageDataRequest extends K_Request {
	
	public HomePageDataRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_HOME_PAGE_DATA;
		this.URL = HttpRequestUrl.HOME_PAGE_DATA;
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestHomePageData(String pageNo) {
		addParam("pageNo", pageNo);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
