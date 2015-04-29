package me.kkuai.kuailian.service.receiver;

import java.io.File;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.utils.FileUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class VersionUpdateCompleteReceiver extends BroadcastReceiver {

	private Log log = LogFactory.getLog(VersionUpdateCompleteReceiver.class);
	
	@Override
	public void onReceive(Context context, Intent intent) {
		log.info("Kuaikuai.apk download complete!");
		
		Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.setDataAndType(Uri.fromFile(new File(FileUtils.getTempFilePath(), "Kuaikuai.apk")), "application/vnd.android.package-archive"); 
		installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(installIntent);
	}

}
