package me.kkuai.kuailian.db;

import me.kkuai.kuailian.user.UserManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class JoinedKKDao {
	
	private KKuaiHelper helper;

	public JoinedKKDao(Context context) {
		String currentUid = UserManager.getInstance().getCurrentUser().getId();
		helper = new KKuaiHelper(context, currentUid);
	}
	
	public synchronized long insert(long playTime, String roomId) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(JoinedKK.PLAY_TIME, playTime);
		values.put(JoinedKK.ROOM_ID, roomId);
		long rowId = db.insert(JoinedKK.TABLE_NAME, "_id", values);
		db.close();
		return rowId;
	}
	
	public synchronized long queryPlayTime() {
		long playTime = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(JoinedKK.TABLE_NAME, null, JoinedKK.PLAY_TIME + ">?", new String[] {System.currentTimeMillis() + ""}, null, null, JoinedKK.PLAY_TIME + " ASC");
		if (cursor.moveToFirst()) {
			playTime = cursor.getLong(cursor.getColumnIndex(JoinedKK.PLAY_TIME));
		}
		db.close();
		return playTime;
	}
	
	public synchronized long queryPlayingTime() {
		long playTime = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(JoinedKK.TABLE_NAME, null, JoinedKK.PLAY_TIME + "<=?", new String[] {System.currentTimeMillis() + ""}, null, null, JoinedKK.PLAY_TIME + " DESC");
		if (cursor.moveToFirst()) {
			playTime = cursor.getLong(cursor.getColumnIndex(JoinedKK.PLAY_TIME));
			long time = System.currentTimeMillis() - playTime;
			if (time > 180*1000) {
				playTime = 0;
			}
		}
		db.close();
		return playTime;
	}
}
