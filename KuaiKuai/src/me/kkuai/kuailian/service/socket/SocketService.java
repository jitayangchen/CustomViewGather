package me.kkuai.kuailian.service.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.net.SocketFactory;

import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.engine.LoginManage;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.event.EventCenter;
import me.kkuai.kuailian.service.event.EventListener;
import me.kkuai.kuailian.service.socket.protocol.LoginProtocol;
import me.kkuai.kuailian.service.socket.protocol.Protocol;
import me.kkuai.kuailian.service.socket.protocol.ProtocolFactory;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.NetWorkUtil;
import me.kkuai.kuailian.utils.Preference;
import me.kkuai.kuailian.utils.Util;
import android.content.Context;

/**
 * implements socket service to serve binary transfer
 * @author ice
 *
 */

public class SocketService implements EventListener
{
	protected Log l = LogFactory.getLog(getClass());
	// queue those requests to send to server
	protected ConcurrentLinkedQueue<Protocol> reqs = new ConcurrentLinkedQueue<Protocol>();
	// queue those received data. to send to listener or to deal with my self
	protected ConcurrentLinkedQueue<Protocol> recvs = new ConcurrentLinkedQueue<Protocol>();
	// holds protocol command ID & listener mapping
//	protected MappingItemsHolder<String, SocketListener> listeners = new MappingItemsHolder<String, SocketListener>();
	protected ConcurrentHashMap<String, SocketListener> listeners = new ConcurrentHashMap<String, SocketListener>();
	// socket server
	protected String socketHost;
	// socket port on servers
	protected String socketHostPort;
	protected SendTask st;
	protected RecvTask rt;
	protected DispatchTask dt;
	public boolean isConnected;	// marks if currently is connecting to server
	protected Context mContext;
	protected long lasttime ;
	protected Timer mTimer;
	public HeartbeatMsgClient heartBeatClient;
	protected String socketToken = null;
	protected Socket socket;

	public void registerListener(Context context)
	{
		listeners.put(SocketUtils.HEART_BEAT, HeartbeatMsgClient.getInstance(context));
		listeners.put(SocketUtils.LOGIN_SUCCESS, LoginClient.getInstance(context));
		listeners.put(SocketUtils.NEW_CHAT_MSG, ChatClient.getInstance(context));
		listeners.put(SocketUtils.SEND_MSG_RETURN, ChatClient.getInstance(context));
	}
	
	/**
	 * add request to send
	 * the request will be cached until connection ready
	 * @param p
	 */
	public void addRequest(Protocol p)
	{
		reqs.add(p);
		if (!isConnected) {
			setupConnection();
		}
		if (null == st) {
			return;
		}
		synchronized(st) {
			st.notifyAll();
		}
	}
	
	public void init(Context ctx){
		this.mContext = ctx;
		socketHost = SocketPool.getInatance(ctx).socketHost;
		socketHostPort = SocketPool.getInatance(ctx).socketPort;
		
		heartBeatClient = HeartbeatMsgClient.getInstance(ctx);
		registerListener(ctx);
	}
	
	public void start() {
		if(socketHost == null) {
			socketHost = SocketPool.getInatance(mContext).socketHost;
		}
		
		EventCenter.getInstance().registerListener(this, AppConstantValues.NETWORK_STATUS_CHANGE);
		
		setupConnection();
	}
	
	public void stop() {
		socketHost = null;
		socketToken = null;
		clearConnection();
		EventCenter.getInstance().removeListener(this, AppConstantValues.NETWORK_STATUS_CHANGE);
	}
	
	/**
	 * process for connection error
	 */
	public synchronized void onConnectionError() {
		try {
			if(!isConnected)
				return;
			l.debug("reconn in onConnectionError.........");
			reqs.clear();
			clearConnection();
			
//			检测网络状态
			if (NetWorkUtil.checkNet(mContext)) {
				delayConnect();
			}
		}
		catch (Throwable e) {
			l.error("onConnectionError error ", e);
		}
	}
	
	/**
	 * to clear current read & send tasks
	 */
	protected void clearConnection() {
		isConnected = false;
		
		if(st != null) {
			st.isStop = true;
			st.interrupt();
			st = null;
		}
		if(rt != null) {
			rt.isStop = true;
			rt.interrupt();
			if(rt.dis != null) {
				try {
					rt.dis.close();
				} catch (IOException e) {
					l.error("close rt.dis error : ", e);
				}
			}
			rt = null;
		}
		if(dt != null) {
			dt.isStop = true;
			dt.interrupt();
			dt = null;
			
		}
		
		EventCenter.getInstance().fireEvent(AppConstantValues.HEART_BEAT_STOP, AppConstantValues.HEART_BEAT_SEND_STATUS_CHANGE);
		
		if(socket != null)
			try {
				socket.close();
			}
			catch (IOException e) {
			}
		
		if (null != socketToken)
			socketToken = null;
	}
	
	protected Socket genSocket() throws UnknownHostException,SocketException, IOException {
		l.debug("the socketHost is " + socketHost + ", the socketHostPort is " + socketHostPort);
		Socket s = SocketFactory.getDefault().createSocket(socketHost, new Integer(socketHostPort).intValue());
//		s.setSoTimeout(5000);
		return s;
	}
	
	/**
	 * setup socket connect & start read & send tasks
	 */
	protected synchronized void setupConnection() {
		l.info("setupConnection() isConnected === " + isConnected);
		if(isConnected)
			return;
		l.info("setupConnection() getCurrentUser() === " + UserManager.getInstance().getCurrentUser());
		if(UserManager.getInstance().getCurrentUser() == null) {
			new Thread() {
				public void run() {
					String userName = Preference.getUserName(mContext);
					String passWord = Preference.getPassWord(mContext);
					String uid = Preference.getUid(mContext);
					UserInfo userInfo = new UserInfo();
					userInfo.setId(uid);
					userInfo.setUserName(userName);
					userInfo.setPassword(passWord);
					UserManager.getInstance().setCurrentUser(userInfo);
				};
			}.start();
		}
		l.debug("setupConnection");
		try {

			socket = genSocket();
             
			OutputStream output = socket.getOutputStream();
			DataInputStream streamReader = new DataInputStream(socket.getInputStream());
			
			st = new SendTask();
			st.isStop = false;
			st.os =  new DataOutputStream(output);
			
			rt = new RecvTask();
			rt.isStop = false;
			rt.dis = streamReader;
			
			dt = new DispatchTask();
			dt.isStop = false;
			
			st.start();
			rt.start();
			dt.start();
			
			isConnected = true;
			l.debug("setup connection ok");
			
//			heartBeatClient.start();
			EventCenter.getInstance().fireEvent(AppConstantValues.HEART_BEAT_START, AppConstantValues.HEART_BEAT_SEND_STATUS_CHANGE);
			
		}
		catch (Exception e) {
			clearConnection();
			l.error("error on initial socket connection", e);
			delayConnect();
		} 
	}
	
	
	/**
	 * start a delay task to call connect
	 */
	protected void delayConnect() {
		if(mTimer != null)
			mTimer.cancel();
		mTimer = new Timer(true);
		TimerTask tt = new TimerTask(){
			@Override
			public void run() {
//				if(!isRunning)
//					return;
				l.debug("delay call doconnect()");
				setupConnection();
				
				if (isConnected) {
					sendLoginVertify();
				}
			}
		};
		mTimer.schedule(tt, 5000);
		lasttime = System.currentTimeMillis();
	}
	
	public synchronized void sendLoginVertify() {
		
		if(UserManager.getInstance().getCurrentUser() == null)
			return;
		
		l.debug("send socket login message");
		LoginProtocol login = new LoginProtocol();
		login.sendLoginVertify(mContext);
		//  send login success info
		addRequest(login);
	}
	
	// to send request
	protected class SendTask extends Thread {
		protected void toWait() {
			synchronized (this) {
				try {
					wait();
				}
				catch (InterruptedException e) {
				}
			}
		}
		
		protected OutputStream os;
		protected boolean isStop;
		public void run() {
			
			StringBuilder sb = new StringBuilder();
			while(!isStop) {
				Protocol p = reqs.poll();
				if(p == null)
					toWait();
				else {
					try {
						if(isConnected) {
							sb.setLength(0);
							sb.append("to send message : ").append(new String(p.getContentData()));
							l.debug(sb.toString());
							byte[] chatContent = p.getContentData();
							byte[] sendDataByte = null;
							
							if (chatContent.length == 1) {
								sendDataByte = chatContent;
							} else {
								sendDataByte = ProtocolFactory.fingerprintAndunSecret(chatContent, 0, chatContent.length, socketToken);
								l.debug("finger token ==== > " + socketToken);
							}
							
							byte[] intByte = ProtocolFactory.intToByteArray(sendDataByte.length);
							os.write(intByte);
							os.write(sendDataByte);
							os.flush();
						}
					}
					catch (SocketException e) {
						l.error("it has broken pipe error, not influence user's socket link", e);
						onConnectionError();
						break;
					}
					catch (IOException e) {
						l.error("error while send protocol:" + p.getCmd(), e);
						onConnectionError();
						break;
					}
					
				}
			}
			
			Util.close(os);
		}
	}
	
	// to receive message
	protected class RecvTask extends Thread {
		protected InputStream is;
		protected boolean isStop;
		DataInputStream dis;
		public void run() {
			while(!isStop && !interrupted()) {
				try {
					Protocol p = ProtocolFactory.getInstance().parseData(dis, mContext);
					l.debug("Protocol p ====>" + p);
					if(null != p) {
						l.debug("protocol recved: " + p.toString());
						recvs.offer(p);
						synchronized (dt) {
							dt.notifyAll();
						}
					}
				}
				catch (Exception e) {
					l.error("error while reading socket", e);
					onConnectionError();
					break;
				}
			}
			
			Util.close(dis);
			Util.close(is);
		}
	}
	
	// to dispatch message
	protected class DispatchTask extends Thread {
		protected boolean isStop;
		protected void toWait() {
			synchronized (this) {
				try {
					wait();
				}
				catch (InterruptedException e) {
				}
			}
		}
		public void run() {
			while(!isStop) {
				Protocol p = recvs.poll();
				if(p == null) {
					toWait();
				}
				else {
					SocketListener sl = listeners.get(p.getCmd());
					if(sl != null) {
						try {
							sl.onDataReceived(p);	
						}
						catch (Exception e) {
							l.error("error while fire message to: " + sl.toString(), e);
						}
					}
				}
			}
		}
	}

	public String getSocketToken() {
		return socketToken;
	}

	public void setSocketToken(String socketToken) {
		this.socketToken = socketToken;
		l.info("Token ========= " + socketToken);
	}
	
	@Override
	public void onEvent(Object source, String event) {
		if (AppConstantValues.NETWORK_STATUS_CHANGE.equals(event)) {
			if (NetWorkUtil.checkNet(mContext)) {
				delayConnect();
			} else {
				onConnectionError();
			}
		}
	}
}
