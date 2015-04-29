package me.kkuai.kuailian.network;

import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.event.EventCenter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetStatusWatcher extends BroadcastReceiver {
	
	private Log log = LogFactory.getLog(NetStatusWatcher.class);

	@Override
	public void onReceive(Context context, Intent intent) {
		log.info("----------- 网络状态改变 ---------- ");
		EventCenter.getInstance().fireEvent(this, AppConstantValues.NETWORK_STATUS_CHANGE);
	}

}
