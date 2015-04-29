package me.kkuai.kuailian.bean;

public class ChatItem {
	private String senderUid;
	private String friendUid;
	private String selfUid;
	private String clientUniqueId;
	private String m_id;
	private String m_msgId;
	private String msgContent;
	private String msgType;
	private String msgStatus;
	private long receiveTime;
	private long readTime;
	private long sendtime;
	private String extraFlag;
	private boolean isPlaying = false;
	private boolean isShowTime = false;
	
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
	
	public long getSendtime() {
		return sendtime;
	}
	public void setSendtime(long sendtime) {
		this.sendtime = sendtime;
	}
	public long getReadTime() {
		return readTime;
	}
	public void setReadTime(long readTime) {
		this.readTime = readTime;
	}
	public boolean isShowTime() {
		return isShowTime;
	}
	public void setShowTime(boolean isShowTime) {
		this.isShowTime = isShowTime;
	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
}
