package me.kkuai.kuailian.service.socket.protocol;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class LoginProtocol implements Protocol {
	
	protected Log log = LogFactory.getLog(getClass());
	
	public String cmd;
	
	public String account;
	
	public String password;
	
	public String from;
	
	public String client;
	
	public String channelId;
	
	public String ver;
	
	public String loginJson;
	
	public String uid;
	
	public String loginStatus;
	
	public String token;
	
	public String hash;
	
	public String function;
	
	private int tryIndex = 0;
	
	
	@Override
	public String getCmd() {
		return cmd;
	}
	
	@Override
	public byte[] getContentData() {
		
		if (null == loginJson)
			return null;
		else 
			return loginJson.getBytes();
	}

	@Override
	public void parseBinary(byte[] data) {
				
		try {
			String loginStr = new String(data);
			loginStr = loginStr.substring(4, loginStr.length());
			loginJson = loginStr;
			JSONObject json = new JSONObject(loginStr);
			if (json.has("status")) {
				cmd = json.getString("cmd");
				// 登录成功和发送消息的 CMD 都为1  ， 暂时用 function  作为 cmd , 
				cmd = json.getString("cmd");
				function = json.getString("function");
				loginStatus = json.getString("status");
				uid = json.getString("uid");
				token = json.getString("token");
				
			}
		} catch (JSONException e) {
			log.error("parse socket login error : ", e);
		}
	}
	
	public void sendLoginVertify(Context context) {
		
		UserInfo user = UserManager.getInstance().getCurrentUser();
		if (null == user) 
			return; 
		
		loginJson = "{\"f\":\"login\", \"p\":{\"mobile\":\"" + user.getUserName() + "\", \"password\":\"" + user.getPassword() + "\"}}";
		
	}
	
	
	private String getVersionName(Context context) {

		try {
			PackageManager pkgMgr = context.getPackageManager();
			PackageInfo info = pkgMgr.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
		}

		return null;

	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getLoginJson() {
		return loginJson;
	}

	public void setLoginJson(String loginJson) {
		this.loginJson = loginJson;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@Override
	public String getFunction() {
		return null;
	}
	
	
	
}
