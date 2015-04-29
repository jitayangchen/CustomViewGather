package me.kkuai.kuailian.bean;

import java.util.List;

public class OfflineMsgBean {

	private String unrevMsgNum;
	
	private List<ChatItem> chatItems;
	
	private String friendUid;

	public String getUnrevMsgNum() {
		return unrevMsgNum;
	}

	public void setUnrevMsgNum(String unrevMsgNum) {
		this.unrevMsgNum = unrevMsgNum;
	}

	public List<ChatItem> getChatItems() {
		return chatItems;
	}

	public void setChatItems(List<ChatItem> chatItems) {
		this.chatItems = chatItems;
	}

	public String getFriendUid() {
		return friendUid;
	}

	public void setFriendUid(String friendUid) {
		this.friendUid = friendUid;
	}
	
}
