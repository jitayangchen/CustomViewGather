package me.kkuai.kuailian.service.receiver;

import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.event.EventCenter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LiveRoomAlarmReceiver extends BroadcastReceiver {
	
	private Log log = LogFactory.getLog(LiveRoomAlarmReceiver.class);
	public static final String ACTION = "me.kkuai.kuailian.ALARM_RECEIVER_LIVE_ROOM";

	@Override
	public void onReceive(Context context, Intent intent) {
//		String type = intent.getStringExtra("type");
//		log.info("LiveRoomAlarmReceiver type === " + type);
//		if (AppConstantValues.LIVE_ROOM_REQUEST_DATA.equals(type)) {
			log.info("LiveRoomAlarmReceiver request liveRoom data === " + System.currentTimeMillis());
			EventCenter.getInstance().fireEvent(this, AppConstantValues.LIVE_ROOM_REQUEST_DATA);
//		} else {
//			log.info("LiveRoomAlarmReceiver live room down file === " + System.currentTimeMillis());
//			EventCenter.getInstance().fireEvent(this, AppConstantValues.LIVE_ROOM_DOWN_FILE);
//		}
	}

}
