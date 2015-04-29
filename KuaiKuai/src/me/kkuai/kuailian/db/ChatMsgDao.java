package me.kkuai.kuailian.db;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.bean.ChatItem;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.user.UserManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChatMsgDao {
	
	private KKuaiHelper helper;
	private String currentUid;
	
	public ChatMsgDao(Context context) {
		currentUid = UserManager.getInstance().getCurrentUser().getId();
		helper = new KKuaiHelper(context, currentUid);
	}
	
	public void createTable(String tableName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(ChatMsg.createTable("fm_" + tableName));
	}
	
	public synchronized long insert(ChatItem ci, String tableName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ChatMsg.SENDER_UID, ci.getSenderUid());
		values.put(ChatMsg.FRIEND_UID, ci.getFriendUid());
		values.put(ChatMsg.CLIENT_UNIQUE_ID, ci.getClientUniqueId());
		values.put(ChatMsg.M_ID, ci.getM_id());
		values.put(ChatMsg.SELF_UID, ci.getSelfUid());
		values.put(ChatMsg.M_MSG_ID, ci.getM_msgId());
		values.put(ChatMsg.MSG_CONTENT, ci.getMsgContent());
		values.put(ChatMsg.MSG_TYPE, ci.getMsgType());
		values.put(ChatMsg.MSG_STATUS, ci.getMsgStatus());
		values.put(ChatMsg.SEND_TIME, ci.getSendtime());
		values.put(ChatMsg.RECEIVE_TIME, ci.getReceiveTime());
		values.put(ChatMsg.READ_TIME, ci.getReadTime());
		values.put(ChatMsg.EXTRA_FLAG, ci.getExtraFlag());
		long rowId = db.insert("fm_" + tableName, "_id", values);
		db.close();
		return rowId;
	}
	
	public synchronized long insertBatch(List<ChatItem> chatItems, String tableName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		long rowId = 0;
		for (ChatItem ci : chatItems) {
			ContentValues values = new ContentValues();
			values.put(ChatMsg.SENDER_UID, ci.getSenderUid());
			values.put(ChatMsg.FRIEND_UID, ci.getFriendUid());
			values.put(ChatMsg.CLIENT_UNIQUE_ID, ci.getClientUniqueId());
			values.put(ChatMsg.M_ID, ci.getM_id());
			values.put(ChatMsg.SELF_UID, ci.getSelfUid());
			values.put(ChatMsg.M_MSG_ID, ci.getM_msgId());
			values.put(ChatMsg.MSG_CONTENT, ci.getMsgContent());
			values.put(ChatMsg.MSG_TYPE, ci.getMsgType());
			values.put(ChatMsg.MSG_STATUS, ci.getMsgStatus());
			values.put(ChatMsg.SEND_TIME, ci.getSendtime());
			values.put(ChatMsg.RECEIVE_TIME, ci.getReceiveTime());
			values.put(ChatMsg.READ_TIME, ci.getReadTime());
			values.put(ChatMsg.EXTRA_FLAG, ci.getExtraFlag());
			rowId = db.insert("fm_" + tableName, "_id", values);
		}
		db.close();
		return rowId;
	}
	
	public synchronized int deleteAll(String tableName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int delete = db.delete("fm_" + tableName, null, null);
		db.close();
		return delete;
	}
	
	public synchronized int update(ChatItem ci, String tableName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ChatMsg.M_ID, ci.getM_id());
		values.put(ChatMsg.M_MSG_ID, ci.getM_msgId());
		values.put(ChatMsg.SEND_TIME, ci.getSendtime());
		int count = db.update("fm_" + tableName, values, "cid=?", new String[]{ci.getClientUniqueId()});
		db.close();
		return count;
	}
	
	public synchronized List<ChatItem> queryBatch(String tableName, int index, int size) {
		List<ChatItem> chatItems = new ArrayList<ChatItem>();
		SQLiteDatabase db = helper.getReadableDatabase();
		StringBuilder limit = new StringBuilder();
		limit.append(index);
		limit.append(",");
		limit.append(size);
		Cursor cursor = db.query("fm_" + tableName, null, null, null, null, null, ChatMsg.SEND_TIME + " DESC", limit.toString());
		if (cursor.moveToLast()) {
			do {
				ChatItem item = new ChatItem();
				item.setSenderUid(cursor.getString(cursor.getColumnIndex(ChatMsg.SENDER_UID)));
				item.setFriendUid(cursor.getString(cursor.getColumnIndex(ChatMsg.FRIEND_UID)));
				item.setSelfUid(cursor.getString(cursor.getColumnIndex(ChatMsg.SELF_UID)));
				item.setM_id(cursor.getString(cursor.getColumnIndex(ChatMsg.M_ID)));
				item.setClientUniqueId(cursor.getString(cursor.getColumnIndex(ChatMsg.CLIENT_UNIQUE_ID)));
				item.setM_msgId(cursor.getString(cursor.getColumnIndex(ChatMsg.M_MSG_ID)));
				item.setMsgContent(cursor.getString(cursor.getColumnIndex(ChatMsg.MSG_CONTENT)));
				item.setMsgType(cursor.getString(cursor.getColumnIndex(ChatMsg.MSG_TYPE)));
				item.setMsgStatus(cursor.getString(cursor.getColumnIndex(ChatMsg.MSG_STATUS)));
				item.setExtraFlag(cursor.getString(cursor.getColumnIndex(ChatMsg.EXTRA_FLAG)));
				item.setReceiveTime(cursor.getLong(cursor.getColumnIndex(ChatMsg.RECEIVE_TIME)));
				item.setReadTime(cursor.getLong(cursor.getColumnIndex(ChatMsg.READ_TIME)));
				item.setSendtime(cursor.getLong(cursor.getColumnIndex(ChatMsg.SEND_TIME)));
//				if (item.getSendtime() >= timePoint + timeInterval) {
//					item.setShowTime(true);
//				}
//				timePoint = item.getSendtime();
				chatItems.add(item);
			} while (cursor.moveToPrevious());
		}
		
		db.close();
		return chatItems;
	}
	
	public synchronized int updateAllUnReadToRead(String tableName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ChatMsg.MSG_STATUS, AppConstantValues.MSG_STATUS_READ);
		int count = db.update("fm_" + tableName, values, ChatMsg.MSG_STATUS + "=?", new String[]{AppConstantValues.MSG_STATUS_UNREAD});
		db.close();
		return count;
	}
	
	public synchronized int queryUnReadNumber(String tableName) {
		int count = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("fm_" + tableName, null, ChatMsg.MSG_STATUS + "=?", new String[] {AppConstantValues.MSG_STATUS_UNREAD}, null, null, null);
		count = cursor.getCount();
		db.close();
		return count;
	}
	
	public synchronized long queryFirstUpdateTime(String tableName) {
		long updateTime = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("fm_" + tableName, null, null, null, null, null, ChatMsg.SEND_TIME + " DESC");
		if (cursor.moveToLast()) {
			updateTime = cursor.getLong(cursor.getColumnIndex(ChatMsg.SEND_TIME));
		}
		db.close();
		return updateTime;
	}
}
