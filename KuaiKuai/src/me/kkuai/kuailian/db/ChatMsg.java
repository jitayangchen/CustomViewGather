package me.kkuai.kuailian.db;

public class ChatMsg {
	
	public static String ID = "_id";
	public static String SENDER_UID = "senderUid";
	public static String FRIEND_UID = "friendUid";
	public static String SELF_UID = "selfUid";
	public static String M_ID = "m_id";
	public static String M_MSG_ID = "m_msgId";
	public static String CLIENT_UNIQUE_ID = "clientUniqueId";
	public static String MSG_CONTENT = "msgContent";
	public static String MSG_TYPE = "msgType";
	public static String MSG_STATUS = "msgStatus";
	public static String EXTRA_FLAG = "extraFlag";
	public static String RECEIVE_TIME = "receiveTime";
	public static String READ_TIME = "readTime";
	public static String SEND_TIME = "sendTime";
	
	public static String createTable(String tableName) {
		String CREATE_TABLE = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ").append(tableName)
				.append(" (")
				.append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				.append(SENDER_UID).append(" text,")
				.append(FRIEND_UID).append(" text,")
				.append(SELF_UID).append(" text,")
				.append(M_ID).append(" text,")
				.append(M_MSG_ID).append(" text,")
				.append(CLIENT_UNIQUE_ID).append(" text,")
				.append(MSG_CONTENT).append(" text,")
				.append(MSG_TYPE).append(" text,")
				.append(MSG_STATUS).append(" text,")
				.append(RECEIVE_TIME).append(" long,")
				.append(SEND_TIME).append(" long,")
				.append(READ_TIME).append(" long,")
				.append(EXTRA_FLAG).append(" text)")
				.toString();
		
		return CREATE_TABLE;
	}
	
}
