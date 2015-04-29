package me.kkuai.kuailian.service.socket;

import android.content.Context;


public class SocketPool {
	
	public String socketHost = "115.28.52.191";
//	public String socketHost = "http://www.xinxiannv.com/";
	public String socketPort = "61535";
	private Context ctx;
	private SocketService ss;
	private static final SocketPool instance = new SocketPool();
	private SocketPool() {
		
	}
	
	public static SocketPool getInatance(Context ctx) {
		instance.ctx = ctx.getApplicationContext();
		return instance;
	}
	
	public SocketService getSocketService()
	{
		if(ss == null)
		{
			ss = new SocketService();
			ss.init(ctx);
		}
		return ss;
	}
	
}
