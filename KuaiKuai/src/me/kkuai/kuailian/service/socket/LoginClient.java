package me.kkuai.kuailian.service.socket;

import java.util.LinkedList;
import java.util.List;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.socket.protocol.LoginProtocol;
import me.kkuai.kuailian.service.socket.protocol.Protocol;
import android.content.Context;

public class LoginClient implements SocketListener {

	protected Context mContext;
	protected Log l = LogFactory.getLog(getClass());
	protected volatile String connStatus; // marks current connection status
	
	private final static LoginClient instance = new LoginClient();
	
	private LoginClient(){}
	
	public static LoginClient getInstance(Context mContext) {
		instance.mContext = mContext;
		return instance;
	}

	public void onDataReceived(Protocol p) {
		l.info("login seccess");
		LoginProtocol login = (LoginProtocol) p;
		SocketPool.getInatance(mContext).getSocketService().setSocketToken(login.getToken());
	}

	@Override
	public void onConnectionStatusChanged(String newStatus) {
		
	}


}
