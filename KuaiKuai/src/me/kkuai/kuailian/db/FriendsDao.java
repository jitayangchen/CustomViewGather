package me.kkuai.kuailian.db;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.bean.ChatItem;
import me.kkuai.kuailian.user.UserManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FriendsDao {

	private KKuaiHelper helper;
	private String currentUid;
	private Context context;
	
	public FriendsDao(Context context) {
		this.context = context;
		currentUid = UserManager.getInstance().getCurrentUser().getId();
		helper = new KKuaiHelper(context, currentUid);
	}
	
	public synchronized long insert(FriendInfo friendInfo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Friends.UID, friendInfo.getUid());
		values.put(Friends.NICK_NAME, friendInfo.getNickName());
		values.put(Friends.SEX, friendInfo.getSex());
		values.put(Friends.AVATAR, friendInfo.getAvatar());
		values.put(Friends.NAME, friendInfo.getName());
		values.put(Friends.MEMO, friendInfo.getMemo());
		values.put(Friends.RELATION_STATUS, friendInfo.getRelationStatus());
		values.put(Friends.CREATET_TIME, friendInfo.getCreatetTime());
		values.put(Friends.LAST_UPDATE_TIME, friendInfo.getLastUpdateTime());
		values.put(Friends.IS_ON_LINE, friendInfo.getIsOnLine());
		long rowId = db.insert(Friends.TABLE_NAME, "_id", values);
		db.close();
		return rowId;
	}
	
	public synchronized long insertAndLastMsg(FriendInfo friendInfo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Friends.UID, friendInfo.getUid());
		values.put(Friends.NICK_NAME, friendInfo.getNickName());
		values.put(Friends.SEX, friendInfo.getSex());
		values.put(Friends.AVATAR, friendInfo.getAvatar());
		values.put(Friends.NAME, friendInfo.getName());
		values.put(Friends.MEMO, friendInfo.getMemo());
		values.put(Friends.RELATION_STATUS, friendInfo.getRelationStatus());
		values.put(Friends.CREATET_TIME, friendInfo.getCreatetTime());
		values.put(Friends.LAST_UPDATE_TIME, friendInfo.getLastUpdateTime());
		values.put(Friends.IS_ON_LINE, friendInfo.getIsOnLine());
		values.put(Friends.LAST_MSG, friendInfo.getLastMsg());
		values.put(Friends.LAST_MSG_TIME, friendInfo.getLastMsgTime());
		values.put(Friends.LAST_MSG_ID, friendInfo.getLastMsgId());
		values.put(Friends.LAST_MSG_SENDER_UID, friendInfo.getLastMsgSenderUid());
		long rowId = db.insert(Friends.TABLE_NAME, "_id", values);
		db.close();
		return rowId;
	}
	
	public synchronized void insertALL(List<FriendInfo> friendInfos) {
		SQLiteDatabase db = helper.getWritableDatabase();
		for (FriendInfo friendInfo : friendInfos) {
//			db.query(Friends.TABLE_NAME, columns, null, null, null, null, null);
			ContentValues values = new ContentValues();
			values.put(Friends.UID, friendInfo.getUid());
			values.put(Friends.NICK_NAME, friendInfo.getNickName());
			values.put(Friends.SEX, friendInfo.getSex());
			values.put(Friends.AVATAR, friendInfo.getAvatar());
			values.put(Friends.NAME, friendInfo.getName());
			values.put(Friends.MEMO, friendInfo.getMemo());
			values.put(Friends.RELATION_STATUS, friendInfo.getRelationStatus());
			values.put(Friends.CREATET_TIME, friendInfo.getCreatetTime());
			values.put(Friends.LAST_UPDATE_TIME, friendInfo.getLastUpdateTime());
			values.put(Friends.IS_ON_LINE, friendInfo.getIsOnLine());
			long rowId = db.insert(Friends.TABLE_NAME, "_id", values);
		}
		db.close();
	}
	
	public synchronized List<FriendInfo> queryAll(String[] columns) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(Friends.TABLE_NAME, columns, null, null, null, null, null);
		return null;
	}
	
	public synchronized List<FriendInfo> queryByPageSize(int pageNumber, int pageSize) {
		ChatMsgDao chatMsgDao = new ChatMsgDao(context);
		List<FriendInfo> friendInfos = new ArrayList<FriendInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		StringBuilder limit = new StringBuilder();
		limit.append(pageNumber).append(",").append(pageSize);
		Cursor cursor = db.query(Friends.TABLE_NAME, null, null, null, null, null, Friends.LAST_MSG_TIME + " DESC", limit.toString());
		while (cursor.moveToNext()) {
			FriendInfo friendInfo = new FriendInfo();
			friendInfo.setUid(cursor.getString(cursor.getColumnIndex(Friends.UID)));
			friendInfo.setNickName(cursor.getString(cursor.getColumnIndex(Friends.NICK_NAME)));
			friendInfo.setSex(cursor.getString(cursor.getColumnIndex(Friends.SEX)));
			friendInfo.setAvatar(cursor.getString(cursor.getColumnIndex(Friends.AVATAR)));
			friendInfo.setLastUpdateTime(cursor.getLong(cursor.getColumnIndex(Friends.LAST_UPDATE_TIME)));
			friendInfo.setLastMsg(cursor.getString(cursor.getColumnIndex(Friends.LAST_MSG)));
			friendInfo.setLastMsgId(cursor.getString(cursor.getColumnIndex(Friends.LAST_MSG_ID)));
			friendInfo.setLastMsgTime(cursor.getLong(cursor.getColumnIndex(Friends.LAST_MSG_TIME)));
			friendInfo.setLastMsgReadStatus(cursor.getString(cursor.getColumnIndex(Friends.LAST_MSG_READ_STATUS)));
			
			chatMsgDao.createTable(friendInfo.getUid());
			friendInfo.setUnReadNumber(chatMsgDao.queryUnReadNumber(friendInfo.getUid()));
			friendInfos.add(friendInfo);
		}
		return friendInfos;
	}
	
	public synchronized FriendInfo query(String[] columns, String selection, String[] selectionArgs) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(Friends.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		FriendInfo friendInfo = null;
		if (cursor.moveToNext()) {
//			friendInfo.setCreatetTime(cursor.getLong(cursor.getColumnIndex("name")));
		}
		
		return friendInfo;
	}
	
	public synchronized FriendInfo queryFrientById(String fuid) {
		FriendInfo friendInfo = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(Friends.TABLE_NAME, null, Friends.UID + "=?", new String[] {fuid}, null, null, null);
		if (cursor.moveToNext()) {
			friendInfo = new FriendInfo();
			friendInfo.setUid(cursor.getString(cursor.getColumnIndex(Friends.UID)));
			friendInfo.setNickName(cursor.getString(cursor.getColumnIndex(Friends.NICK_NAME)));
			friendInfo.setAvatar(cursor.getString(cursor.getColumnIndex(Friends.AVATAR)));
		}
		db.close();
		return friendInfo;
	}
	
	public synchronized long queryFrientLastUpdateTime() {
		long updateTime = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(Friends.TABLE_NAME, null, null, null, null, null, Friends.LAST_UPDATE_TIME + " DESC");
		if (cursor.moveToFirst()) {
			updateTime = cursor.getLong(cursor.getColumnIndex(Friends.CREATET_TIME));
		}
		db.close();
		return updateTime;
	}
	
	public synchronized int delete(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = db.delete(Friends.TABLE_NAME, "uid=?", new String[]{uid});
		db.close();
		return count;
	}
	
	public synchronized int update(FriendInfo friendInfo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		int count = db.update(Friends.TABLE_NAME, values, "uid=?", new String[]{friendInfo.getUid()});
		db.close();
		return count;
	}
	
	public synchronized int updateLastMsg(ChatItem ci, String fuid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Friends.LAST_MSG, ci.getMsgContent());
		values.put(Friends.LAST_MSG_TIME, ci.getSendtime());
		values.put(Friends.LAST_MSG_ID, ci.getM_msgId());
		values.put(Friends.LAST_MSG_SENDER_UID, ci.getSenderUid());
		int count = db.update(Friends.TABLE_NAME, values, "uid=?", new String[]{fuid});
		db.close();
		return count;
	}
}
