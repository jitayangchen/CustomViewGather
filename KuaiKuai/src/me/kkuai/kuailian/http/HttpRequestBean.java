package me.kkuai.kuailian.http;

import net.tsz.afinal.http.AjaxParams;

public class HttpRequestBean {
	private AjaxParams params;
	private String url;
	private String method;
	
	public AjaxParams getParams() {
		return params;
	}
	public void setParams(AjaxParams params) {
		this.params = params;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
}
