package me.kkuai.kuailian.service.socket;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.socket.protocol.HeartbeatProtocol;
import me.kkuai.kuailian.service.socket.protocol.Protocol;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class HeartbeatMsgClient implements SocketListener {
	
	private Log l = LogFactory.getLog(HeartbeatMsgClient.class);
	public Context mContext;
	public Timer mTimer;
	public TimerTask mTimerTask ;
	private long periodTime = 60000; // heartbeat interval
	private HeartbeatProtocol pro = new HeartbeatProtocol();
	private int heartFlag = 0;
	private WakeLock wakeLock = null;
	private boolean stopSend = false;
	private SendHeartThread sendHeart;
	private ScheduledThreadPoolExecutor exec;
	
	private final static HeartbeatMsgClient instance = new HeartbeatMsgClient();
	
	private HeartbeatMsgClient(){
		
	}
	
	public static HeartbeatMsgClient getInstance(Context mContext) {
		instance.mContext = mContext;
		return instance;
	}
	
	//execute heart beat
	@Deprecated
	public void executeHeartbeat(long setExcuteTime){
//		acquireWakeLock();
//		sendHeart = new SendHeartThread();
//		sendHeart.start();
		exec = new ScheduledThreadPoolExecutor(1);
		exec.scheduleAtFixedRate(new SendHeartThread(), 60, 60, TimeUnit.SECONDS);
		
	}
	
	public void sendData() {
		l.debug("send time: " + Calendar.getInstance().getTime().toGMTString());
		SocketService ss = SocketPool.getInatance(mContext).getSocketService();
		ss.addRequest(pro);
	}
	
	public void onDataReceived(Protocol p) {
		l.debug("recv time: " + Calendar.getInstance().getTime().toGMTString());
		heartFlag = 0;
	}
	
	@Deprecated
	public void stop() {
		l.debug("stop heart beat");
		heartFlag = 0;
		stopSend = true;
//		if (sendHeart != null) {
//			sendHeart.interrupt();
//			sendHeart = null;
//		}
//		releaseWakeLock();
		
		if (null != exec) {
			exec.shutdown();
		}
	}
	
	@Deprecated
	public synchronized void start() {
//		stopSend = false;
//		executeHeartbeat(periodTime);
	}

	public void onConnectionStatusChanged(String newStatus) {
		
	}
	
	//获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行  
	@Deprecated
    private void acquireWakeLock() {  
        if (null == wakeLock) {  
            PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);  
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PostLocationService");  
            if (null != wakeLock) {  
                wakeLock.acquire();  
            }  
        }  
    }
    
    //释放设备电源锁  
	@Deprecated
    private void releaseWakeLock() {  
        if (null != wakeLock) {  
            wakeLock.release();  
            wakeLock = null;  
        }  
    } 
    
	@Deprecated
    class SendHeartThread implements Runnable {
		   @Override
		   public void run(){
			   while (!stopSend) {
				   try {
					   Thread.sleep(periodTime);
				   } catch (InterruptedException e) {
					   e.printStackTrace();
				   }
				   
				   if (stopSend) {
					   return;
				   }
				   if(heartFlag >= 3) {
					   l.debug("connect in heartbeat .....");
					   SocketPool.getInatance(mContext).getSocketService().onConnectionError();  
					   heartFlag = 0;
				   }
				   else {
					   heartFlag ++;
					   sendData();
				   }
			   }
		   }
	}
    
    public void sendHeart() {
    	if(heartFlag >= 3) {
    		l.debug("connect in heartbeat .....");
		    SocketPool.getInatance(mContext).getSocketService().onConnectionError();  
		    heartFlag = 0;
	    } else {
		    heartFlag ++;
		    sendData();
	    }
    }

}
