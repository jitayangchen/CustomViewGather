package me.kkuai.kuailian.db;

public class Friends {
	public static String TABLE_NAME = "Friends";
	public static String ID = "_id";
	public static String UID = "uid";
	public static String NICK_NAME = "nickName";
	public static String SEX = "sex";
	public static String AVATAR = "avatar";
	public static String NAME = "name";
	public static String MEMO = "memo";
	public static String RELATION_STATUS = "relationStatus";
	public static String CREATET_TIME = "createtTime";
	public static String LAST_UPDATE_TIME = "lastUpdateTime";
	public static String LAST_LOGIN_TIME = "lastLoginTime";
	public static String IS_ON_LINE = "isOnLine";
	public static String UNREAD_NUMBER = "unReadNumber";
	public static String LAST_MSG = "lastMsg";
	public static String LAST_MSG_TIME = "lastMsgTime";
	public static String LAST_MSG_ID = "lastMsgId";
	public static String LAST_MSG_READ_STATUS = "lastMsgReadStatus";
	public static String LAST_MSG_SENDER_UID = "lastMsgSenderUid";
	
	public static String CREATE_TABLE = new StringBuilder()
										.append("CREATE TABLE ")
										.append(TABLE_NAME)
										.append(" (")
										.append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
										.append(UID).append(" text,")
										.append(NICK_NAME).append(" text,")
										.append(SEX).append(" text,")
										.append(AVATAR).append(" text,")
										.append(NAME).append(" text,")
										.append(MEMO).append(" text,")
										.append(RELATION_STATUS).append(" text,")
										.append(CREATET_TIME).append(" long,")
										.append(LAST_UPDATE_TIME).append(" long,")
										.append(LAST_LOGIN_TIME).append(" long,")
										.append(IS_ON_LINE).append(" integer,")
										.append(UNREAD_NUMBER).append(" integer,")
										.append(LAST_MSG).append(" text,")
										.append(LAST_MSG_TIME).append(" long,")
										.append(LAST_MSG_ID).append(" text,")
										.append(LAST_MSG_READ_STATUS).append(" text,")
										.append(LAST_MSG_SENDER_UID).append(" text)")
										.toString();
}
