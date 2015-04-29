package me.kkuai.kuailian.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import android.app.Activity;
import android.content.Context;

/**
 * 
 * @author yc
 *
 */
public class ActivityManager {
	
	private Log log = LogFactory.getLog(ActivityManager.class);
	private Context context;
	
	private static ActivityManager activityManager;
	
	
	public static ActivityManager getActivityManager(Context context){
		if(activityManager == null){
			activityManager = new ActivityManager(context);
		}
		return activityManager;
	}
	
	private ActivityManager(Context context){
		this.context = context;
	}
	
	private final HashMap<String, Activity> taskMap = new HashMap<String, Activity>();
	
	/**
	 * 往应用task map加入activity
	 */
	public final void putActivity(Activity atv) {
		taskMap.put(atv.toString(), atv);
	}
	
	/**
	 * 往应用task map加入activity
	 */
	public final void removeActivity(Activity atv) {
		taskMap.remove(atv.toString());
	}
	
	/**
	 * 清除应用的task栈，如果程序正常运行这会导致应用退回到桌面
	 */
	public final void exit() {
		for (Iterator<Entry<String, Activity>> iterator = taskMap.entrySet().iterator(); iterator.hasNext();) {
			Activity activityReference =  iterator.next().getValue();
			if (activityReference != null) {
				activityReference.finish();
			}
			log.info("finish() ====== " + activityReference.toString());
		}
		taskMap.clear();
	}

}
