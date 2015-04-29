package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.utils.Preference;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;
import com.kkuai.libs.net.request.J_Request;

public abstract class K_Request extends J_Request {

	@Override
	public abstract OnDataBack getCallback();
	
	@Override
	public String getToken() {
		return Preference.getToken();
	}

}
