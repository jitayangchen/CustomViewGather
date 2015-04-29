package me.kkuai.kuailian.http;

import org.json.JSONException;
import org.json.JSONObject;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.JsonUtil;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public abstract class RequestAjaxCallBack extends AjaxCallBack<Object> {
	
	private Log log = LogFactory.getLog(RequestAjaxCallBack.class);
	private String url;
	private AjaxParams params;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public AjaxParams getParams() {
		return params;
	}

	public void setParams(AjaxParams params) {
		this.params = params;
	}

	@Override
	public void onSuccess(Object t) {
		super.onSuccess(t);
		log.info("from server --- " + t.toString());
		try {
			JSONObject json = new JSONObject(t.toString());
			if (json.has("status")) {
				String status = JsonUtil.getJsonString(json, "status");
				if ("1".equals(status)) {
					onExactness(t);
					params = null;
				} else if ("10".equals(status)) {
					onLogin();
				}
			}
		} catch (JSONException e) {
			log.error("parse json ", e);
		}
	}
	
	public void onLogin() {
		UserInfo currentUser = UserManager.getInstance().getCurrentUser();
		AjaxParams params = new AjaxParams();
		params.put("mobile", currentUser.getUserName());
		params.put("password", currentUser.getPassword());
		params.put("loginType", "0");
		KHttpRequest.getInstance().postLoginOrRegister("login", params, new RegistOrLoginAjaxCallBack() {
			
			@Override
			public void onExactness(Object t) {
				log.info("------重新登录获取Token！------");
			}

		});
	}
	
	public abstract void onExactness(Object t);
}
