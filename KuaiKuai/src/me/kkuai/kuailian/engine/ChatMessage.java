package me.kkuai.kuailian.engine;

import me.kkuai.kuailian.bean.ChatItem;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.db.ChatMsgDao;
import me.kkuai.kuailian.db.FriendsDao;
import me.kkuai.kuailian.http.request.SendChatMsgRequest;
import me.kkuai.kuailian.http.request.SetChatMsgStatusRequest;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class ChatMessage {

	private Log log = LogFactory.getLog(ChatMessage.class);
	private Context context;
	private ChatMsgDao chatMsgDao;
	private String friendUid;
	private FriendsDao friendsDao = null;
	
	public ChatMessage(Context context, String friendUid) {
		this.context = context;
		this.friendUid = friendUid;
		chatMsgDao = new ChatMsgDao(context);
		chatMsgDao.createTable(friendUid);
	}
	
	public void sendMessage(ChatItem ci) {
		SendChatMsgRequest request = new SendChatMsgRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		JSONObject clientPriDataJson = new JSONObject();
		JSONObject msgContentJson = new JSONObject();
		try {
			if ("0".equals(ci.getMsgType())) {
				msgContentJson.put("text", ci.getMsgContent());
			}
			clientPriDataJson.put("cid", ci.getClientUniqueId());
		} catch (JSONException e) {
			log.error("client PriData Json", e);
		}
		request.requestSendChatMsg(friendUid, ci.getMsgType(), msgContentJson.toString(), "1", clientPriDataJson.toString());
		insertMsg(ci);
		updateFriendLastMsg(ci, ci.getFriendUid());
	}
	
	public void sendRoomMessage(ChatItem ci, String content) {
		insertMsg(ci);
		updateFriendLastMsg(ci, ci.getFriendUid());
	}

	public String getFriendUid() {
		return friendUid;
	}
	
	public void insertMsg(ChatItem ci) {
		chatMsgDao.insert(ci, ci.getFriendUid());
	}
	
	public void setMsgReceived(String msgId) {
		SetChatMsgStatusRequest request = new SetChatMsgStatusRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		request.requestSetChatMsgStatus(AppConstantValues.SET_MSG_STATUS_TYPE_RECEIVED, msgId);
	}
	
	public void updateMsg(ChatItem ci, String tableName) {
		chatMsgDao.update(ci, tableName);
	}
	
	public void updateFriendLastMsg(ChatItem ci, String fuid) {
		if (null == friendsDao) {
			friendsDao = new FriendsDao(context);
		}
		friendsDao.updateLastMsg(ci, fuid);
	}
	
}
