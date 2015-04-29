package me.kkuai.kuailian.engine;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.bean.ChatItem;
import me.kkuai.kuailian.db.FriendInfo;
import me.kkuai.kuailian.db.FriendsDao;
import me.kkuai.kuailian.http.request.AddChatMsgFriendRequest;
import me.kkuai.kuailian.http.request.ChatFriendListRequest;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.utils.DateUtil;
import me.kkuai.kuailian.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class SynchronFriends {
	
	private Log log = LogFactory.getLog(SynchronFriends.class);
	private Context context;
	private String pageNo = "1";
	private String pageSize = "20";
	private FriendsDao friendsDao;
	private List<String> friendsUid = new ArrayList<String>();
	private ChatItem lastMsg = null;
	private boolean isRequestAddFriend = false;

	public SynchronFriends(Context context) {
		this.context = context;
		friendsDao = new FriendsDao(context);
	}
	
	public void updateFriendToDatabase(ChatItem ci, String fuid) {
		lastMsg = ci;
		if (isRequestAddFriend) {
			return ;
		}
		if (isExistCurrentCollection(fuid)) {
			friendsDao.updateLastMsg(ci, fuid);
		} else if (checkIsExistFriend(fuid)) {
			friendsDao.updateLastMsg(ci, fuid);
			friendsUid.add(fuid);
		} else {
			addChatFriend(fuid);
			FriendInfo friendInfo = new FriendInfo();
			friendInfo.setUid(fuid);
			friendInfo.setCreatetTime(System.currentTimeMillis());
			friendInfo.setLastUpdateTime(System.currentTimeMillis());
			friendInfo.setLastMsg(lastMsg.getMsgContent());
			friendInfo.setLastMsgId(lastMsg.getM_id());
			friendInfo.setLastMsgTime(lastMsg.getSendtime());
			friendInfo.setLastMsgSenderUid(lastMsg.getSenderUid());
			friendsDao.insertAndLastMsg(friendInfo);
			
			friendsUid.add(fuid);
		}
		MessageObservable.getInstance().updateObserver(lastMsg);
	}
	
	public void addChatFriend(final String fuid) {
		AddChatMsgFriendRequest request = new AddChatMsgFriendRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				FriendInfo friendInfo = parseAddChatFriend((String) result);
				if (null != lastMsg) {
					friendInfo.setLastMsg(lastMsg.getMsgContent());
					friendInfo.setLastMsgId(lastMsg.getM_id());
					friendInfo.setLastMsgTime(lastMsg.getSendtime());
					friendInfo.setLastMsgSenderUid(lastMsg.getSenderUid());
				}
				friendsDao.insertAndLastMsg(friendInfo);
				
				friendsUid.add(fuid);
				isRequestAddFriend = false;
				MessageObservable.getInstance().updateObserver(lastMsg);
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		isRequestAddFriend = true;
		request.requestAddChatMsgFriend(fuid, "", "0");
	}
	
	public void synchronOfflineFriends() {
		long lastUpdateTimelong = friendsDao.queryFrientLastUpdateTime();
		String lastUpdateTime;
		if (lastUpdateTimelong == 0) {
			lastUpdateTime = "0";
		} else {
			lastUpdateTime = DateUtil.millsConvertDateStr(lastUpdateTimelong);
		}
		requestFriendsList(lastUpdateTime);
	}
	
	private void requestFriendsList(String lastUpdateTime) {
		ChatFriendListRequest request = new ChatFriendListRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				List<FriendInfo> friends = (List<FriendInfo>) result;
				if (friends.size() > 0) {
					friendsDao.insertALL(friends);
				}
				
				OfflineMessage offlineMessage = new OfflineMessage(context);
				offlineMessage.getOfflineMessage();
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestChatFriendList(lastUpdateTime, pageNo, pageSize, "0");
	}
	
	public boolean isExistCurrentCollection(String fuid) {
		for (int i = 0; i < friendsUid.size(); i++) {
			if (fuid.equals(friendsUid.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkIsExistFriend(String fuid) {
		boolean isExistFriend = false;
		
		FriendInfo friendInfo = friendsDao.queryFrientById(fuid);
		if (null != friendInfo) {
			isExistFriend = true;
		}
		return isExistFriend;
	}
	
	private FriendInfo parseAddChatFriend(String result) {
		FriendInfo friendInfo = null;
		try {
			JSONObject obj = new JSONObject(result);
			String status = JsonUtil.getJsonString(obj, "status");
			if ("1".equals(status)) {
				JSONObject data = JsonUtil.getJsonObject(obj, "data");
				friendInfo = new FriendInfo();
				friendInfo.setNickName(JsonUtil.getJsonString(data, "nick_name"));
				friendInfo.setLastUpdateTime(DateUtil.DateConvertMills(JsonUtil.getJsonString(data, "last_update_time")));
				friendInfo.setMemo(JsonUtil.getJsonString(data, "memo"));
				friendInfo.setRelationStatus(JsonUtil.getJsonString(data, "relation_status"));
				friendInfo.setCreatetTime(DateUtil.DateConvertMills(JsonUtil.getJsonString(data, "createt_time")));
				friendInfo.setAvatar(JsonUtil.getJsonString(data, "avatar"));
				friendInfo.setUid(JsonUtil.getJsonString(data, "friend_uid"));
			}
		} catch (JSONException e) {
			log.error("parse add chat msg friend", e);
		} catch (ParseException e) {
			log.error("date convert mills", e);
		}
		return friendInfo;
	}
	
}
