package me.kkuai.kuailian.service.receiver;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LiveRoomDataUpdateReceiver extends BroadcastReceiver {
	
	private Log log = LogFactory.getLog(LiveRoomDataUpdateReceiver.class);
	public static final String ACTION = "me.kkuai.kuailian.LIVE_ROOM_DATA_UPDATE";

	@Override
	public void onReceive(Context context, Intent intent) {
		log.info("Live Room Update data === " + System.currentTimeMillis());
	}

}
