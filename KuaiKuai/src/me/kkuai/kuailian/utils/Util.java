package me.kkuai.kuailian.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

import me.kkuai.kuailian.KApplication;
import me.kkuai.kuailian.bean.OptionCell;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Util {
	
	private static Log log = LogFactory.getLog(Util.class);

	public static void close(InputStream is) {
		try {
			is.close();
		} catch (Exception e) {
		}
	}
	
	public static void close(OutputStream os) {
		try {
			os.close();
		} catch (Exception e) {
		}
	}
	
	public static void close(Writer writer) {
		try {
			writer.close();
		} catch (Exception e) {
		}
	}
	
	/**
	 * read data from stream.
	 * 
	 * @param is
	 * @return
	 */
	public static byte[] readDataFromIS(InputStream is) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] data = new byte[50];
		int readLen = 0;
		while ((readLen = is.read(data)) > 0)
			os.write(data, 0, readLen);
		data = os.toByteArray();
		os.close();
		return data;
	}
	
	/**
	 * get Screen height
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.heightPixels;
	}

	public static double getScreenDensity(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.density;
	}
	
	public static void hiddenSoftKeyborad(View v, Context context) {
		try {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		} catch (Exception e) {
			log.error("have no getCurrentFocus", e);
		}
	}
	
	public static int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	} 
	
	public static int px2dip(Context context, float pxValue){ 
	        final float scale = context.getResources().getDisplayMetrics().density; 
	        return (int)(pxValue / scale + 0.5f); 
	}
	
	/**
	 * get package version name
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String ver = "";
		try {
			PackageManager pkgMgr = context.getPackageManager();
			PackageInfo info = pkgMgr.getPackageInfo(context.getPackageName(), 0);
			ver = info.versionName;
		} catch (NameNotFoundException e) {
			log.error("get Version Name", e);
		}
		return ver;
	}
	
	/**
	 * get package version code
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionCode(Context context) {
		int ver = 0;
		try {
			PackageManager pkgMgr = context.getPackageManager();
			PackageInfo info = pkgMgr.getPackageInfo(context.getPackageName(), 0);
			ver = info.versionCode;
		} catch (NameNotFoundException e) {
			log.error("get Version Name", e);
		}
		return ver + "";
	}
	
	/**
	 * get system version
	 * 
	 * @return
	 */
	public static String getSysVer() {
		return Build.VERSION.RELEASE;
	}
	
	/**
	 * get mac address
	 * @param ctx
	 * @return
	 */
	public static String getMacAddress(Context ctx) {
		
		String macAddress = null;
		WifiManager wifiMgr = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
		    macAddress = info.getMacAddress();
		}
		
		return macAddress;
	}
	
	public static String getDeviceModel() {
		return Build.MODEL;
	}
	
	/**
	 * get device IMEI, for cell phone only
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getDeviceIMEI(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	
	public static String getCityNameById(String cityId) {
		String cityName = "";
		if (!TextUtils.isEmpty(cityId)) {
			if (cityId.length() == 4) {
				String provincial = cityId.substring(0, 2);
				List<OptionCell> areaData = KApplication.getAreaData();
				OptionCell pcell = null;
				for (OptionCell optionCell : areaData) {
					if (optionCell.getId().equals(provincial)) {
						pcell = optionCell;
						break;
					}
				}
				if (pcell != null) {
					List<OptionCell> cells = pcell.getChildData();
					for (OptionCell cell : cells) {
						if (cell.getId().equals(cityId)) {
							cityName = cell.getName();
							break;
						}
					}
				}
			} else {
				List<OptionCell> areaData = KApplication.getAreaData();
				for (OptionCell optionCell : areaData) {
					if (optionCell.getId().equals(cityId)) {
						cityName = optionCell.getName();
						break;
					}
				}
			}
		}
		return cityName;
	}
}
