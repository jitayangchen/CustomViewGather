package me.kkuai.kuailian.service.socket.protocol;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.socket.SocketPool;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class ChatMsgStatusProtocol implements Protocol {
	
	private Log log = LogFactory.getLog(ChatMsgStatusProtocol.class);
	private String msgId;
	private String type;
	private String content;

	@Override
	public String getCmd() {
		return null;
	}

	@Override
	public String getFunction() {
		return null;
	}

	@Override
	public byte[] getContentData() {

		return content.getBytes();
	}
	
	public void setMsgStatus(Context context) {
//		{\"f\":\"setchatmsgstatus\", \"p\":{\"msg_id\":\"%@\",\"type\":\"%d\",\"token\":\"%@\"}
		String token = SocketPool.getInatance(context).getSocketService().getSocketToken();
		content = "{\"f\":\"setchatmsgstatus\", \"p\":{\"msg_id\":\"" + msgId + "\",\"type\":\"" + type + "\",\"token\":\"" + token + "\"}";
	}

	@Override
	public void parseBinary(byte[] data) {
		String str = new String(data);
		str = str.substring(4, str.length());
		try {
			JSONObject obj = new JSONObject(str);
		} catch (JSONException e) {
			log.error("parse ChatMsgStatusProtocol json ", e);
		}
	}
	

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
