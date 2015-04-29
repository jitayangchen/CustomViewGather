package me.kkuai.kuailian.http;

import org.json.JSONException;
import org.json.JSONObject;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.utils.JsonUtil;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public abstract class RegistOrLoginAjaxCallBack extends AjaxCallBack<Object> {
	
	private Log log = LogFactory.getLog(RegistOrLoginAjaxCallBack.class);
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
					KHttpRequest.getInstance().setToken(JsonUtil.getJsonString(json, "token"));
					onExactness(t);
				}
			}
		} catch (JSONException e) {
			log.error("parse json ", e);
		}
	}
	
	@Override
	public void onFailure(Throwable t, int errorNo, String strMsg) {
		super.onFailure(t, errorNo, strMsg);
	}
	
	public abstract void onExactness(Object t);
}
