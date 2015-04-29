package me.kkuai.kuailian.engine;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.bean.ChatItem;
import me.kkuai.kuailian.bean.OfflineMsgBean;
import me.kkuai.kuailian.db.ChatMsgDao;
import me.kkuai.kuailian.db.FriendsDao;
import me.kkuai.kuailian.http.request.OfflineChatMsgNumRequest;
import me.kkuai.kuailian.http.request.SetAllOfflineMsgStatusRequest;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class OfflineMessage {
	
	private Log log = LogFactory.getLog(OfflineMessage.class);
	private Context context;
	private FriendsDao friendsDao;
	private ChatMsgDao chatMsgDao;

	public OfflineMessage(Context context) {
		this.context = context;
		friendsDao = new FriendsDao(context);
		chatMsgDao = new ChatMsgDao(context);
	}
	
	public void getOfflineMessage() {
		OfflineChatMsgNumRequest request = new OfflineChatMsgNumRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				List<OfflineMsgBean> parseOfflineMessage = parseOfflineMessage((String) result);
				if (parseOfflineMessage.size() > 0) {
					insertMsgToDataBase(parseOfflineMessage);
				}
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestOfflineChatMsgNum();
	}
	
	public void insertMsgToDataBase(final List<OfflineMsgBean> parseOfflineMessage) {
		new Thread() {
			@Override
			public void run() {
				for (OfflineMsgBean offlineMsgBean : parseOfflineMessage) {
					List<ChatItem> chatItems = offlineMsgBean.getChatItems();
					
					chatMsgDao.createTable(offlineMsgBean.getFriendUid());
					log.info("parseOfflineMessage.size() === " + parseOfflineMessage.size());
					if (offlineMsgBean.getChatItems().size() == 20) {
						chatMsgDao.deleteAll(offlineMsgBean.getFriendUid());
					}
					
					ChatItem lastChatItem = chatItems.get(chatItems.size() - 1);
					friendsDao.updateLastMsg(lastChatItem, offlineMsgBean.getFriendUid());
					
					chatMsgDao.insertBatch(chatItems, offlineMsgBean.getFriendUid());
				}
			}
		}.start();
	}
	
	private List<OfflineMsgBean> parseOfflineMessage(String request) {
		List<OfflineMsgBean> offlineMsgBeans = new ArrayList<OfflineMsgBean>();
		try {
			JSONObject obj = new JSONObject(request);
			String status = JsonUtil.getJsonString(obj, "status");
			if ("1".equals(status)) {
				JSONArray datas = JsonUtil.getJsonArrayObject(obj, "data");
				for (int i = 0; i < datas.length(); i++) {
					JSONObject data = JsonUtil.getJsonObject(datas, i);
					OfflineMsgBean offlineMsgBean = new OfflineMsgBean();
					offlineMsgBean.setUnrevMsgNum(JsonUtil.getJsonString(data, "unrev_msg_num"));
					offlineMsgBean.setFriendUid(JsonUtil.getJsonString(data, "friend_uid"));
					JSONArray msgLists = JsonUtil.getJsonArrayObject(data, "msgList");
					
					List<ChatItem> chatItems = new ArrayList<ChatItem>();
					for (int j = 0; j < msgLists.length(); j++) {
						JSONObject msgList = JsonUtil.getJsonObject(msgLists, j);
						ChatItem chatItem = new ChatItem();
						chatItem.setFriendUid(JsonUtil.getJsonString(msgList, "from_uid"));
						chatItem.setSelfUid(JsonUtil.getJsonString(msgList, "to_uid"));
						chatItem.setSenderUid(JsonUtil.getJsonString(msgList, "from_uid"));
						chatItem.setM_id(JsonUtil.getJsonString(msgList, "id"));
						chatItem.setM_msgId(JsonUtil.getJsonString(msgList, "msg_id"));
						chatItem.setMsgType(JsonUtil.getJsonString(msgList, "msg_type"));
						JSONObject msgContentObj = JsonUtil.getJsonObject(msgList, "msg_content");
						String msgContent = JsonUtil.getJsonString(msgContentObj, "text");
						chatItem.setMsgContent(msgContent);
						JSONObject clientPriDataObj = JsonUtil.getJsonObject(msgList, "client_pri_data");
						chatItem.setClientUniqueId(JsonUtil.getJsonString(clientPriDataObj, "cid"));
						chatItem.setSendtime(JsonUtil.getJsonLong(msgList, "send_time"));
						chatItem.setMsgStatus(JsonUtil.getJsonString(msgList, "read_status"));
						chatItem.setReadTime(JsonUtil.getJsonLong(msgList, "read_time"));
						chatItem.setReceiveTime(JsonUtil.getJsonLong(msgList, "receive_time"));
						chatItems.add(chatItem);
					}
					
					offlineMsgBean.setChatItems(chatItems);
					offlineMsgBeans.add(offlineMsgBean);
					
					aetAllOfflineMsgStatusRequest(offlineMsgBean.getFriendUid());
				}
			}
		} catch (JSONException e) {
			log.error("parse Offline Message", e);
		}
		return offlineMsgBeans;
	}
	
	private void aetAllOfflineMsgStatusRequest(String fuid) {
		SetAllOfflineMsgStatusRequest request = new SetAllOfflineMsgStatusRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestSetAllOfflineMsgStatus(fuid);
	}
	
}
