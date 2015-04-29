package me.kkuai.kuailian.utils;

import me.kkuai.kuailian.KApplication;
import me.kkuai.kuailian.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Preference {
	
	public static void setAutoLogin(Context context, boolean v) {
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sprefs.edit();
		String key = context.getString(R.string.preference_auto_login_key);
		e.putBoolean(key, v);
		e.commit();
	}
	
	public static boolean isAutoLogin(Context context) {
		boolean v = false;
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.preference_auto_login_key);
		try {
			v = sprefs.getBoolean(key, false);
		} catch (ClassCastException e) {
			// if exception, do nothing; that is return default value of false.
		}
		return v;
	}
	
	public static void saveUserName(Context context, String value) {
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sprefs.edit();
		String key = context.getString(R.string.preference_user_name);
		e.putString(key, value);
		e.commit();
	}
	
	public static void savePassWord(Context context, String value) {
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sprefs.edit();
		String key = context.getString(R.string.preference_password);
		e.putString(key, value);
		e.commit();
	}
	
	public static void saveUid(Context context, String value) {
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sprefs.edit();
		String key = context.getString(R.string.preference_uid);
		e.putString(key, value);
		e.commit();
	}
	
	public static String getUserName(Context context) {
		String v = "";
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.preference_user_name);
		v = sprefs.getString(key, "");
		return v;
	}
	
	public static String getPassWord(Context context) {
		String v = "";
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.preference_password);
		v = sprefs.getString(key, "");
		return v;
	}
	
	public static String getUid(Context context) {
		String v = "";
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.preference_uid);
		v = sprefs.getString(key, "");
		return v;
	}
	
	public static void saveToken(String value) {
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(KApplication.context);
		Editor e = sprefs.edit();
		String key = KApplication.context.getString(R.string.preference_http_token);
		e.putString(key, value);
		e.commit();
	}
	
	public static String getToken() {
		String v = "";
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(KApplication.context);
		String key = KApplication.context.getString(R.string.preference_http_token);
		v = sprefs.getString(key, "");
		return v;
	}
}
