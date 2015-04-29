package me.kkuai.kuailian.db;

import android.test.AndroidTestCase;

public class SQLiteTest extends AndroidTestCase {

	public void testDelete() {
		ChatMsgDao chatMsgDao = new ChatMsgDao(getContext());
		chatMsgDao.deleteAll("9");
	}
	
	public void testQuery() {
		JoinedKKDao joinedKKDao = new JoinedKKDao(getContext());
		long queryPlayTime = joinedKKDao.queryPlayTime();
	}
}
