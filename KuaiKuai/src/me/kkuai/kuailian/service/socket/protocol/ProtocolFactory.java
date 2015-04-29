package me.kkuai.kuailian.service.socket.protocol;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.owner.PhotoAlbum;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.socket.SocketPool;
import me.kkuai.kuailian.service.socket.SocketUtils;
import me.kkuai.kuailian.utils.JsonUtil;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public class ProtocolFactory
{
	static Log l = LogFactory.getLog(ProtocolFactory.class);
	
	protected static ProtocolFactory pf = new ProtocolFactory();
	
	public static ProtocolFactory getInstance()
	{
		return pf;
	}
	
	public Protocol parseRawMessage(byte[] data, Context context){
		String resultStr = new String(data);
		resultStr = resultStr.substring(4, resultStr.length());
		l.debug("parse raw message ===== > : " + resultStr);
		Protocol protocol = null;
		
		try {
 			JSONObject resultJson = new JSONObject(resultStr);
			
			if (resultJson.has("status") && "1".equals(JsonUtil.getJsonString(resultJson, "status")) || "pushchatmsg".equals(JsonUtil.getJsonString(resultJson, "function"))) {
				if (resultJson.has("cmd")) {
					String cmd = JsonUtil.getJsonString(resultJson, "cmd");
					if (SocketUtils.LOGIN_SUCCESS.equals(cmd)) {
						protocol = new LoginProtocol();
					} else if (SocketUtils.NEW_CHAT_MSG.equals(cmd)) {
						protocol = new ChatProtocol();
					} else if (SocketUtils.SEND_MSG_RETURN.equals(cmd)) {
						protocol = new ChatProtocol();
					}
				}
				protocol.parseBinary(data);
			
				// token out of date
			} else if (resultJson.has("status") && resultJson.getInt("status") == 10) {
				SocketPool.getInatance(context).getSocketService().sendLoginVertify();
			}
		} catch (Exception e) {
			l.error("parseRawMessage has exception : ", e);
		}
		
		return protocol;
	}
	
	/**
	 * read response header info
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static void readHeader(InputStream is)throws IOException
	{
		StringBuilder ss = new StringBuilder();
		StringBuilder ts = new StringBuilder();
		byte[] oneByte = new byte[1];
        while(true)
        {
        	if(is.read(oneByte,0,1)>0) 
        	{
        		ss.append((int)oneByte[0]);
        		ts.append((char)oneByte[0]);
               	if(ss.toString().indexOf("13101310") != -1)
            		break;
        	}

        }
	}
	
	/**
	 * check begin
	 */
	
//	public static String checkBeginData(DataInputStream is, Context context)throws IOException
//	{
//		String redirectHost = null;
//		Protocol p = ProtocolFactory.getInstance().parseData(is, context);
//		if(p!=null)
//		{
//			if(p instanceof RedirectProtocol) //redirect host ip)
//			{
//				redirectHost = ((RedirectProtocol)p).getRedirectHost();
//				redirectHost = "Redirect:" + redirectHost;
//			}
//			else if(p instanceof ServerConnectAcknowledgementProtocol)
//			{
//				redirectHost = "ConnectSuccess";
//			}
//			else if(p instanceof LoginFailProtocol)
//			{
//				redirectHost = "LoginFail";
//			}
//		}
//		else
//			redirectHost = "LoginFail";
//			
//		return redirectHost;
//		
//	}
	
	protected String getSDPath()
	{
		 File sdDir = null;
		 boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //�ж�sd���Ƿ����?
		 if (sdCardExist)
		 {
			 sdDir = Environment.getExternalStorageDirectory();
		 }
		 return sdDir.toString(); 
	}
	
	/**
	 * read data & generate related protocol
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public synchronized Protocol parseData(DataInputStream is, Context context) throws IOException
	{
		Protocol pro = null;
		try
		{
			
			//  读取数据长度
			int sectionLen = readSectionLen(is);
			
			l.debug("receive message length is ========> : " + sectionLen);
			
			byte[] revData = null;
			
			if (sectionLen > 16) {
				
				revData = readSectionData(is, sectionLen);
				
//				String resultStr = new String(revData);
//				l.info("推送消息成功 --- " + resultStr);
//				NotificationManager ntfMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//				Notification notification = new Notification(me.kkuai.kuailian.R.drawable.ic_launcher, resultStr, System.currentTimeMillis());
//				notification.flags = Notification.FLAG_AUTO_CANCEL;
//				notification.defaults = Notification.DEFAULT_SOUND;
//				Intent intent = new Intent(context, PhotoAlbum.class);
//				PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//						intent, PendingIntent.FLAG_UPDATE_CURRENT);
//				notification.setLatestEventInfo(context, "New Message", resultStr, contentIntent);
//				ntfMgr.notify(me.kkuai.kuailian.R.string.app_name, notification);
				
				//  解密
//				unSecret(revData, 0, sectionLen-16);
//				
//				//  读取指纹信息
//				byte[] printData = readSectionData(is, 16);
//				// 将接收的数据解密
//				String token = SocketPool.getInatance(context).getSocketService().getSokectToken();
//				// 验证指纹的正确
//				fingerprintIsOk(printData, 0, printData.length, token);
//			
				pro = parseRawMessage(revData, context);
				
			} else {
				// receive heart msg  when sectionLen ==1 
				
				revData = readSectionData(is, sectionLen);
				
				String resultStr = new String(revData);
				SocketPool.getInatance(context).getSocketService().heartBeatClient.onDataReceived(pro);
				l.debug("receive heartMsg  ===== > : " + resultStr);
			}
			
//			// 将接收的数据解密
//			String token = ServicePool.getinstance(context).getHttpService().getToken();
//			byte[] unSecretType = SocketUtils.unSecretAndFingerprintIsOK(revData, 0, revData.length, token);
					
			
		}
		catch (SocketException e) 
		{
			l.error("parseData has socketException:", e);
		}
		catch (IOException e) 
		{
			l.error("parseData has IOExcetion:" , e);
		}
		return pro;
	
	}
	
	protected int readSectionLen(DataInputStream streamReader)throws IOException {
		byte[] data = readSectionData(streamReader, 4);
		return bytesToInt(data);
	}
	
	protected byte[] readSectionData(DataInputStream streamReader, int expectLen) 
			throws IOException, SocketException, OutOfMemoryError {
        byte[] wholeData = new byte[expectLen];
        int readLen = 0;
        int temp = 0;
		while(readLen < expectLen)
		{
			l.debug("Socket DataInputStream read() isblock");
			
			readLen += streamReader.read(wholeData, readLen, expectLen - readLen);
			
			l.debug("readLen=>" + readLen + "---expectLen=>" + expectLen + "---temp=>" + temp);
		}
        return wholeData;
	}
	
	//convert byteArray to int
	public static int bytesToInt(byte b[]) 
	{
	      int intValue=0;
	      for(int i=0;i<b.length;i++){
	        intValue +=(b[i] & 0xFF)<<(8*(3-i));
	      }
	      return intValue;
     }
	
	//covert int to byteArray
	public static byte[] intToByteArray(int value){
		  byte[] b=new byte[4];
		  for(int i=0;i<4;i++){
		   b[i]=(byte)(value >> 8*(3-i) & 0xFF);
		  }
		  return b;
	}
	
	// -------------------------------   协议   ------------------------
		/**
		 * 对所给数据附加指纹并加密
		 * 
		 * @param b
		 * @param off
		 * @param len
		 * @return
		 */
		public static byte[] fingerprintAndunSecret(byte[] b, int off, int len, String salt) {
			if (b != null && b.length > 0) {
				if (off < 0 || off > b.length - 1
						|| (len > 0 && (off + len) > b.length)) {
					throw new IndexOutOfBoundsException();
				}
				if (len < 0)
					len = b.length - off;

				// 指纹 
				byte[] se = fingerprint(b, off, len, salt);
				unSecret(b, off, len);
				unSecret(se, 0, -1);
				byte[] buf = new byte[len + 16];
				System.arraycopy(b, off, buf, 0, len);
				System.arraycopy(se, 0, buf, len, 16);
				return buf;
			}
			return null;
		}

		/**
		 * 对所给数据解密并进行指纹验证
		 * 
		 * @param b
		 * @param off
		 * @param len
		 * @return
		 */
		public static byte[] unSecretAndFingerprintIsOK(byte[] b, int off, int len,
				String salt) {
			if (b != null && b.length > 0) {
				if (off < 0 || off > b.length - 1
						|| (len > 0 && (off + len) > b.length)) {
					throw new IndexOutOfBoundsException();
				}
				if (len < 0)
					len = b.length - off;

				unSecret(b, off, len);
				if (fingerprintIsOk(b, off, len, salt)) {
					byte[] buf = new byte[len - 16];
					System.arraycopy(b, off, buf, 0, buf.length);
					return buf;
				}
			}
			return null;
		}

		/**
		 * 关键字节
		 */
		private static final int SecretKey = 0x38;

		/**
		 * 加解密
		 * 
		 * @param b
		 * @param off
		 * @param len
		 */
		public static void unSecret(byte[] b, int off, int len) {
			if (b != null && b.length > 0) {
				if (off < 0 || off > b.length - 1
						|| (len > 0 && (off + len) > b.length)) {
					throw new IndexOutOfBoundsException();
				}
				if (len < 0)
					len = b.length - off;

				for (int i = off, j = 0; i < b.length && j < len; i++, j++)
					b[i] = (byte) (((int) b[i]) ^ SecretKey);
			}
		}

		/**
		 * 盐
		 */
		public static final byte[] SecretSaltKey = { 0x23, 0x3d, 0x63, 0x45, 0x1d,
				0x7d };

		/**
		 * 判断指纹
		 * 
		 * @param b
		 * @param off
		 * @param len
		 * @return
		 */
		public static boolean fingerprintIsOk(byte[] b, int off, int len,
				String salt) {
			if (b != null && b.length > 0) {
				if (off < 0 || off > b.length - 1
						|| (len > 0 && (off + len) > b.length)) {
					throw new IndexOutOfBoundsException();
				}
				if (len < 0)
					len = b.length - off;

				byte[] res = fingerprint(b, off, len - 16, salt);
				for (int i = 0, j = off + len - 16; i < res.length; i++) {
					if (res[i] != b[i + j])
						return false;
				}
				return true;
			}
			return false;
		}

		/**
		 * 生成指纹
		 * 
		 * @param b
		 * @param off
		 * @param len
		 * @return
		 */
		public static byte[] fingerprint(byte[] b, int off, int len, String salt) {
			if (b != null && b.length > 0) {
				try {
					MessageDigest md = MessageDigest.getInstance("MD5");
					md.update(b, off, len);
					md.update(SecretSaltKey);
					if (salt != null && !"".equals(salt)) {
						try {
							md.update(salt.getBytes("GBK"));
							
						} catch (Exception e) {
							l.error("generate fingerprint error : ", e);
						}
					}
					b = md.digest();
					md.reset();
				} catch (Exception e) {
				}
			}
			return b;
		}
}
