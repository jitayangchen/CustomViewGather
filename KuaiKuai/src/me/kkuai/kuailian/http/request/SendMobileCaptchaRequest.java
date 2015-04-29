package me.kkuai.kuailian.http.request;

import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.constant.HttpRequestUrl;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class SendMobileCaptchaRequest extends K_Request {
	
	public SendMobileCaptchaRequest(OnDataBack callback) {
		this.callback = callback;
		this.REQUEST_TYPE = HttpRequestTypeId.TASKID_SEND_MOBILE_CAPTCHA;
		this.URL = HttpRequestUrl.SEND_MOBILE_CAPTCHA;
		setTokenField("");
	}

	@Override
	public OnDataBack getCallback() {
		return callback;
	}
	
	public void requestSendMobileCaptcha(String smsType, String mobile, String hash) {
		addParam("smsType", smsType);
		addParam("mobile", mobile);
		addParam("hash", hash);
		J_NetManager.getInstance().sendUnblockRequest(this);
	}

}
