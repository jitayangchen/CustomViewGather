package me.kkuai.kuailian.http;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.JsonUtil;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class KHttpRequest {

	private Log log = LogFactory.getLog(KHttpRequest.class);
	private String token = null;
	private String baseUrl = "http://www.xinxiannv.com/api/";
	private FinalHttp finalHttp;
	private static final KHttpRequest instance = new KHttpRequest();
	
	private HttpTask ht;
	private HttpRequestQueue queue = new HttpRequestQueue();
	private RequestAjaxCallBack requestAjaxCallBack = new RequestAjaxCallBack();
	private UUID lastUUID = null;

	private KHttpRequest() {
		finalHttp = new FinalHttp();
	}
	
	public static KHttpRequest getInstance() {
		return instance;
	}
	
	public UUID addRequest(HttpRequesterListener requesterListener, AjaxParams params, String url) {
		HttpRequestBean requestBean = new HttpRequestBean();
		requestBean.setParams(params);
		requestBean.setUrl(url);
		UUID uuid = queue.add(requesterListener, requestBean);
		
		if (null == ht) {
			start();
		}
		log.info("addRequest ------" + ht.isStop);
		if (!ht.isStop) {
			synchronized (ht)
			{
				log.info("addRequest ------ notifyAll 执行");
				ht.notifyAll();
			}
		}
		return uuid;
	}
	
	public void post(String url, AjaxParams params, AjaxCallBack<Object> ajaxCallBack) {
		params.put("token", token);
		log.info(baseUrl + url + "?" + params.toString());
		finalHttp.post(baseUrl + url, params, ajaxCallBack);
	}
	
	public void postLoginOrRegister(String url, AjaxParams params, RegistOrLoginAjaxCallBack ajaxCallBack) {
		log.info("LoginOrRegister request --- " + baseUrl + url + "?" + params.toString());
		ajaxCallBack.setParams(params);
		ajaxCallBack.setUrl(baseUrl + url);
		finalHttp.post(baseUrl + url, params, ajaxCallBack);
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public void start() {
		if (null != ht) {
			return;
		}
		ht = new HttpTask();
		Thread t = new Thread(ht);
		t.start();
	}
	
	class HttpTask implements Runnable {

		public boolean isStop = false;
		
		@Override
		public void run() {
			while (true) {
				try {
					if (isStop) {
						synchronized (this)
						{
							log.info("HttpTask ------ 第一个 --- wait 执行");
							wait();
						}
					}
					lastUUID = queue.getNextRequestUUID();
					if (null == lastUUID) {
						synchronized (this)
						{
							log.info("HttpTask ------ 第二个 --- wait 执行");
							wait();
						}
					} else {
						HttpRequestBean requestBean = queue.getHttpRequestBean(lastUUID);
						post(requestBean.getUrl(), requestBean.getParams(), requestAjaxCallBack);
						isStop = true;
						log.info("HttpTask ------ 请求完成 --- wait");
					}
				} catch (InterruptedException e) {
					log.error("KHttpRequest HttpTask", e);
				}
			}
		}
		
	}
	
	class RequestAjaxCallBack extends AjaxCallBack<Object> {
		
		@Override
		public void onSuccess(Object t) {
			super.onSuccess(t);
			log.info("from server --- " + t.toString());
			try {
				JSONObject json = new JSONObject(t.toString());
				if (json.has("status")) {
					String status = JsonUtil.getJsonString(json, "status");
					if ("10".equals(status)) {
						onLogin();
					} else {
						HttpRequesterListener httpRequesterListener = queue.getHttpRequesterListener(lastUUID);
						httpRequesterListener.onReponse(lastUUID, t.toString());
						queue.remove(lastUUID);
						ht.isStop = false;
						synchronized (ht)
						{
							ht.notifyAll();
							log.info("RequestAjaxCallBack1 onSuccess ------ 唤醒线程");
						}
					}
				}
			} catch (JSONException e) {
				log.error("parse json ", e);
			}
		}
		
		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			log.info("errorNo === " + errorNo + " --- strMsg === " + strMsg, t);
			queue.remove(lastUUID);
			ht.isStop = false;
			synchronized (ht)
			{
				ht.notifyAll();
				log.info("RequestAjaxCallBack1 onFailure ------ 唤醒线程");
			}
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
				ht.isStop = false;
				synchronized (ht)
				{
					ht.notifyAll();
					log.info("RequestAjaxCallBack1 onSuccess ------ 唤醒线程");
				}
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				queue.removeAll();
				ht.isStop = false;
				synchronized (ht)
				{
					ht.notifyAll();
					log.info("RequestAjaxCallBack1 onSuccess ------ 唤醒线程");
				}
			}

		});
	}
	
	public void stop() {
		queue.removeAll();
	}
	
}
