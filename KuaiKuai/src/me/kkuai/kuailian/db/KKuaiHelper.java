package me.kkuai.kuailian.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KKuaiHelper extends SQLiteOpenHelper {
	
	public KKuaiHelper(Context context, String sqliteName) {
		super(context, sqliteName, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Friends.CREATE_TABLE);
		db.execSQL(JoinedKK.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
