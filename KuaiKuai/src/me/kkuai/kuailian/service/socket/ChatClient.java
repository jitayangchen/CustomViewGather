package me.kkuai.kuailian.service.socket;

import me.kkuai.kuailian.bean.ChatItem;
import me.kkuai.kuailian.engine.ChatMessage;
import me.kkuai.kuailian.engine.SynchronFriends;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.socket.protocol.ChatProtocol;
import me.kkuai.kuailian.service.socket.protocol.Protocol;
import android.content.Context;

public class ChatClient implements SocketListener {

	private Context mContext;
	private Log log = LogFactory.getLog(getClass());
	private ChatMsgChangeListener msgChangeListener;
	private final static ChatClient instance = new ChatClient();
	private ChatMessage chatMessage = null;
	private SynchronFriends synchronFriends = null;
	
	private ChatClient(){}
	
	public static ChatClient getInstance(Context mContext) {
		instance.mContext = mContext;
		return instance;
	}

	public void onDataReceived(Protocol p) {
//		ChatMsgDao chatMsgDao = new ChatMsgDao(mContext);
		ChatProtocol cp = (ChatProtocol) p;
		if (null == chatMessage) {
			chatMessage = new ChatMessage(mContext, cp.getFriendUid());
		} else if (!chatMessage.getFriendUid().equals(cp.getFriendUid())) {
			chatMessage = new ChatMessage(mContext, cp.getFriendUid());
		}
		if (null == synchronFriends) {
			synchronFriends = new SynchronFriends(mContext);
		}
		ChatItem ci = new ChatItem();
		if (SocketUtils.NEW_CHAT_MSG.equals(cp.getGo())) {
			ci.setSenderUid(cp.getSenderUid());
			ci.setFriendUid(cp.getFriendUid());
			ci.setSelfUid(cp.getSelfUid());
			ci.setClientUniqueId(cp.getClientUniqueId());
			ci.setM_id(cp.getM_id());
			ci.setM_msgId(cp.getM_msgId());
			ci.setMsgType(cp.getMsgType());
			ci.setMsgStatus(cp.getMsgStatus());
			ci.setSendtime(cp.getSendTime());
			ci.setMsgStatus(cp.getReadStatus());
			ci.setReadTime(cp.getReadTime());
			ci.setReceiveTime(cp.getReceiveTime());
			ci.setMsgContent(cp.getMsgContent());
			ci.setFriendUid(cp.getSenderUid());
			log.info("ChatClient --- " + cp.getMsgContent());
			if (null != msgChangeListener && msgChangeListener.getFriendUid().equals(cp.getSenderUid())) {
				msgChangeListener.newChatMsg(ci);
			} else {
				SocketUtils.showNotification(mContext, cp.getMsgContent(), cp.getSenderUid());
			}
			chatMessage.insertMsg(ci);
			synchronFriends.updateFriendToDatabase(ci, cp.getFriendUid());
			chatMessage.setMsgReceived(ci.getM_msgId());
		} else if (SocketUtils.SEND_MSG_RETURN.equals(cp.getGo())) {
			ci.setM_id(cp.getM_id());
			ci.setM_msgId(cp.getM_msgId());
			ci.setClientUniqueId(cp.getClientUniqueId());
			ci.setSendtime(cp.getSendTime());
			chatMessage.updateMsg(ci, cp.getFriendUid());
		}
	}

	public synchronized void onConnectionStatusChanged(String newStatus) {
		
	}

	public interface ChatMsgChangeListener {
		void newChatMsg(ChatItem ci);
		String getFriendUid();
	}
	
	public void setMsgChangeListener(ChatMsgChangeListener msgChangeListener) {
		this.msgChangeListener = msgChangeListener;
	}
}
