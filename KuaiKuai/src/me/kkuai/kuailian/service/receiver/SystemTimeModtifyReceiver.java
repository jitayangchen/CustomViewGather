package me.kkuai.kuailian.service.receiver;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SystemTimeModtifyReceiver extends BroadcastReceiver {
	
	private Log log = LogFactory.getLog(SystemTimeModtifyReceiver.class);

	@Override
	public void onReceive(Context context, Intent intent) {
		log.info(" === SystemTimeModtifyReceiver === ");
	}

}
