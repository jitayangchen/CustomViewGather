package me.kkuai.kuailian.service.socket.protocol;

import me.kkuai.kuailian.bean.ChatItem;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.socket.SocketPool;
import me.kkuai.kuailian.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class ChatProtocol implements Protocol{

	Log log = LogFactory.getLog(ChatProtocol.class);
	
	private String clientUniqueId;
	private String m_id;
	private String m_msgId;
	private String senderUid;
	private String friendUid;
	private String selfUid;
	private String msgContent;
	private String msgType;
	private String msgStatus;
	private String isSuccess;
	private String readStatus;
	private String extraFlag;
	private long receiveTime;
	private long sendTime;
	private String receiveStatus;
	private long readTime;
	private String localMsgId;
	private String chatJson;
	private String cmd;
	private String function;
	private String go;

	public byte[] getContentData() 
	{
		if(chatJson == null)
		{
			return null;
		}
		return chatJson.getBytes();
	}

	public void parseBinary(byte[] data) 
	{
		
		try {
			// {"cmd":101,"function":"selfMsg","rid":1362722444448,"data":{"id":509,"sid":"C82BBDB1_8d_2F7530C6","time":1362722444,"touid":1613,"dealid":0,"read":false,"q_insk":"10","type":0,"msg":"了K金啦啦K金","fromuid":1542,"classid":0}}	

			String str = new String(data);
			str = str.substring(4, str.length());
			JSONObject obj = new JSONObject(str);
			if (obj.has("go") && "1".equals(JsonUtil.getJsonString(obj, "go"))) {
				friendUid = JsonUtil.getJsonString(obj, "from_uid");
				senderUid = JsonUtil.getJsonString(obj, "from_uid");
				selfUid = JsonUtil.getJsonString(obj, "to_uid");
				msgType = JsonUtil.getJsonString(obj, "msg_type");
				JSONObject content = JsonUtil.getJsonObject(obj, "msg_content");
				m_id = JsonUtil.getJsonString(obj, "id");
				m_msgId = JsonUtil.getJsonString(obj, "msg_id");
				JSONObject clientPriData = JsonUtil.getJsonObject(obj, "client_pri_data");
				clientUniqueId = JsonUtil.getJsonString(clientPriData, "cid");
				msgContent = JsonUtil.getJsonString(content, "text");
				readStatus = JsonUtil.getJsonString(obj, "read_status");
				receiveStatus = JsonUtil.getJsonString(obj, "receive_status");
				sendTime = JsonUtil.getJsonLong(obj, "send_time");
				receiveTime = JsonUtil.getJsonLong(obj, "receive_time");
			} else if (obj.has("go") && "0".equals(JsonUtil.getJsonString(obj, "go"))) {
				JSONObject JsonData = JsonUtil.getJsonObject(obj, "data");
				friendUid = JsonUtil.getJsonString(JsonData, "to_uid");
				senderUid = JsonUtil.getJsonString(JsonData, "from_uid");
				selfUid = JsonUtil.getJsonString(JsonData, "from_uid");
				sendTime = JsonUtil.getJsonLong(JsonData, "send_time");
				m_id = JsonUtil.getJsonString(JsonData, "id");
				m_msgId = JsonUtil.getJsonString(JsonData, "msg_id");
				JSONObject clientPriData = JsonUtil.getJsonObject(JsonData, "client_pri_data");
				clientUniqueId = JsonUtil.getJsonString(clientPriData, "cid");
			}
			cmd = JsonUtil.getJsonString(obj, "cmd");
			go = JsonUtil.getJsonString(obj, "go");
			
		} catch (JSONException e) {
			log.error("split data has error in chatProtocol:", e);
		}
	}
	
	public void sendChatJson(ChatItem ci, Context context) {
//		"{\"f\":\"sendchat\",\"p\":{\"toUid\":\"%u\",\"msgType\":%d, \"msgContent\":{\"text\":\"%@\"}, \"clientPlat\":%d,\"clientPriData\":{\"cid\":\"%@\"},\"token\":\"%@\"}}"
		String token = SocketPool.getInatance(context).getSocketService().getSocketToken();
		String assemblStr = "{\"f\":\"sendchat\", \"p\":{\"toUid\":\""+ci.getFriendUid()+"\", \"msgType\":\""+ ci.getMsgType() +"\", \"msgContent\":{\"text\":\""+ ci.getMsgContent() +"\"}, \"clientPlat\":\""+ "1" +"\",\"clientPriData\":{\"cid\":\""+ci.getClientUniqueId()+"\"}}}";
		if (null != token)
			assemblStr = "{\"f\":\"sendchat\", \"p\":{\"toUid\":\""+ci.getFriendUid()+"\", \"msgType\":\""+ ci.getMsgType() +"\", \"msgContent\":{\"text\":\""+ ci.getMsgContent() +"\"}, \"clientPlat\":\""+ "1" +"\",\"clientPriData\":{\"cid\":\""+ci.getClientUniqueId()+"\"},\"token\":\""+ token +"\"}}}";
		
		this.chatJson = assemblStr;
	}


	public String getChatJson() {
		return chatJson;
	}

	public void setChatJson(String chatJson) {
		this.chatJson = chatJson;
	}

	@Override
	public String getCmd() {
		return cmd;
	}

	@Override
	public String getFunction() {
		return function;
	}

	public String getClientUniqueId() {
		return clientUniqueId;
	}

	public void setClientUniqueId(String clientUniqueId) {
		this.clientUniqueId = clientUniqueId;
	}

	public String getM_id() {
		return m_id;
	}

	public void setM_id(String m_id) {
		this.m_id = m_id;
	}

	public String getM_msgId() {
		return m_msgId;
	}

	public void setM_msgId(String m_msgId) {
		this.m_msgId = m_msgId;
	}

	public String getSenderUid() {
		return senderUid;
	}

	public void setSenderUid(String senderUid) {
		this.senderUid = senderUid;
	}

	public String getFriendUid() {
		return friendUid;
	}

	public void setFriendUid(String friendUid) {
		this.friendUid = friendUid;
	}

	public String getSelfUid() {
		return selfUid;
	}

	public void setSelfUid(String selfUid) {
		this.selfUid = selfUid;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(String isRead) {
		this.readStatus = readStatus;
	}

	public String getExtraFlag() {
		return extraFlag;
	}

	public void setExtraFlag(String extraFlag) {
		this.extraFlag = extraFlag;
	}

	public long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public long getReadTime() {
		return readTime;
	}

	public void setReadTime(long readTime) {
		this.readTime = readTime;
	}

	public String getLocalMsgId() {
		return localMsgId;
	}

	public void setLocalMsgId(String localMsgId) {
		this.localMsgId = localMsgId;
	}

	public String getGo() {
		return go;
	}

	public void setGo(String go) {
		this.go = go;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	
}
