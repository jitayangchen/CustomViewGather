package me.kkuai.kuailian.service.socket;

import java.util.regex.Pattern;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.chat.Chat;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SocketUtils {

	// static UserManager um = ServicePool.getinstance().getUserManager();
	public static final String TOKEN_OUT_OF_DATE = "token_out_of_date_relogin";
	
	public static final String MSGTYPE_CHAT = "0";
	public static final String MSGTYPE_SYSTEM = "1";
	
	public static final String CHAT_MSG_READED = "102";
	public static final String LOGIN_SUCCESS = "21";
	public static final String SEND_MSG_RETURN = "0";
	public static final String NEW_CHAT_MSG = "1";
	public static final String CHAT_READ_STATUS = "chat_setchatreaded";
	
	public static final String HEART_BEAT = "heartbeat";
	
	public static final String ONLINE_STATES = "124";
	public static final String AUDIO_TYPE = "1";

	public static final String PUSH_MSG = "push_messages";

	public static long periodTime = 5000;
	public static Log l = LogFactory.getLog(SocketUtils.class);


	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
	
	public static void showNotification(Context context, String content, String friendUid) {
		NotificationManager ntfMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher, content, System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_SOUND;
		Intent intent = new Intent(context, Chat.class);
		intent.putExtra("friendUid", friendUid);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, "New Message", content, contentIntent);
		ntfMgr.notify(R.string.app_name, notification);
	}
}