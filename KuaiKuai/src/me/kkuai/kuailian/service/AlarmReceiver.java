package me.kkuai.kuailian.service;

import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.event.EventCenter;
import me.kkuai.kuailian.service.socket.HeartbeatMsgClient;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	
	private Log log = LogFactory.getLog(AlarmReceiver.class);
	public static final String ACTION = "me.kkuai.kuailian.ALARM_RECEIVER_HEARTBEAT";

	@Override
	public void onReceive(Context context, Intent intent) {
		log.info("AlarmReceiver send sendHeart");
		HeartbeatMsgClient.getInstance(context).sendHeart();
		EventCenter.getInstance().fireEvent(this, AppConstantValues.HEART_BEAT_AGAIN_SEND);
	}

}
