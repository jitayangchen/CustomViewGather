package me.kkuai.kuailian.constant;

public class AppConstantValues {
	
	public static final String CLIENT = "1";
	public static final String FROM = "001";
	
	public static final String SEX_MALE = "0";
	public static final String SEX_FEMALE = "1";
	
	public static final String USER_STATUS_UNPERFECT = "0";
	public static final String USER_STATUS_NORMAL = "1";
	public static final String USER_STATUS_BLACK_LIST = "2";
	public static final String USER_STATUS_BLACK_LIST_DETERMINE = "3";
	public static final String USER_STATUS_DELETED = "4";
	public static final String USER_STATUS_NOT_PASS_VERIFY = "5";
	
	public static final String CAPTCHA_TYPE_REGIST = "0";
	public static final String CAPTCHA_TYPE_FORGET_PASSWORD = "1";

	public static final String OWNER_PROFILE_LIST_PERSONAL_DATA = "user_info";
	public static final String OWNER_PROFILE_LIST_PHOTO_ALBUM = "photo_album";
	
	public static final int UPLOAD_FILE_TYPE_AVATAR = 0;
	public static final int UPLOAD_FILE_TYPE_PHOTO = 1;
	public static final int UPLOAD_FILE_TYPE_AUDIO = 2;
	public static final int UPLOAD_FILE_TYPE_VIDEO = 3;
	
	public static final String PLAY_CONTENT_TYPE_AUDIO = "0";
	public static final String PLAY_CONTENT_TYPE_VIDEO = "1";
	
	public static final String HEART_BEAT_STOP = "heart_beat_stop";
	public static final String HEART_BEAT_START = "heart_beat_start";
	public static final String NETWORK_STATUS_CHANGE = "network_status_change";
	public static final String HEART_BEAT_SEND_STATUS_CHANGE = "heart_beat_send_status_change";
	public static final String HEART_BEAT_AGAIN_SEND = "heart_beat_again_send";
	
	public static final String LIVE_ROOM_REQUEST_DATA = "live_room_request_data";
	public static final String LIVE_ROOM_DOWN_FILE = "live_room_down_file";
	public static final String EVENT_LOGOUT = "event_logout";
	
	public static final String ROOM_MALE_ROOM = "1";
	public static final String ROOM_FEMALE_ROOM = "2";
	
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	
	/**
	 * handler return value
	 */
	public static final int OWNERPROFILE_UPLOAD_HEAD_FINISH = 50001;
	public static final int OWNERPROFILE_UPLOAD_USER_PHOTO = 50002;
	public static final int UPLOAD_AUDIO_SECCESS = 50003;
	public static final int HANDLER_RESULT_SUCCESS = 50004;
	public static final int FRIENDS_QUERY_RETURN = 50005;
	public static final int GET_DATABASE_MSG_RETURN = 50006;
	public static final int LOAD_MSG_RETURN = 50007;
	
	public static final int DIALOG_DEAFULT_SMILEY = 1001;
	public static final int DIALOG_SIGNUP_RECORD = 1002;
	public static final int DIALOG_LOADING = 1003;
	public static final int DIALOG_SINGLE_CONFIRM = 1004;
	public static final int DIALOG_NORMAL = 1005;
	
	/**
	 * chat message status
	 */
	public static final String SET_MSG_STATUS_TYPE_RECEIVED = "0";
	public static final String SET_MSG_STATUS_TYPE_READ = "1";
	
	public static final String MSG_STATUS_UNREAD = "0";
	public static final String MSG_STATUS_READ = "1";
	public static final String MSG_STATUS_FAIL = "2";
	public static final String MSG_STATUS_SUCCESS = "3";
	
	public static final String CHAT_MSG_TYPE_TEXT = "0";
	public static final String CHAT_MSG_TYPE_IMAGE = "1";
	public static final String CHAT_MSG_TYPE_VOICE = "2";
	
	public static final int REQUEST_CODE_UPDATE_JOIN_TIME = 1;
	
}
