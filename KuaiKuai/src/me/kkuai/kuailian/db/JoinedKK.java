package me.kkuai.kuailian.db;

public class JoinedKK {

	public static String TABLE_NAME = "JoinedKK";
	public static String ID = "_id";
	public static String PLAY_TIME = "playTime";
	public static String ROOM_ID = "roomId";
	
	public static String CREATE_TABLE = new StringBuilder()
										.append("CREATE TABLE ")
										.append(TABLE_NAME)
										.append(" (")
										.append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
										.append(PLAY_TIME).append(" long,")
										.append(ROOM_ID).append(" text)")
										.toString();
}
