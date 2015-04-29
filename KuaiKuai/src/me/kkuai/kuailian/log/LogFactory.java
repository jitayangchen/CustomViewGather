package me.kkuai.kuailian.log;

import java.util.Hashtable;

public class LogFactory {
	
	private static final Hashtable<String, Log> instances = new Hashtable<String, Log>();
	
	public static boolean isDebugEnable = true;
	
	public synchronized static Log getLog(Class<?> cls) {
		String name = cls.getSimpleName();
		Log instance = instances.get(name);
		if (null == instance) {
			instance = new LogImpl(name);
			instances.put(name, instance);
		}
		return instance;
	}
	
}
