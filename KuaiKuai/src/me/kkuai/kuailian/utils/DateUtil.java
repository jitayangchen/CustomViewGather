package me.kkuai.kuailian.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;

public class DateUtil {
	
	private static Log log = LogFactory.getLog(DateUtil.class);
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATE_HOUR_TIME = "yyyy-MM-dd HH";
	public static final String FORMAT_DATETIME_MINUTE_TIME = "HH:mm";

	/**
	 * 将毫秒数转化为：yyyy-MM-dd
	 */
	
	public static String millsConvertDate(long mills) {
		Date date = new Date(mills);
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
		
		return sdf.format(date);
	}
	/**
	* 将毫秒数转化为：yyyy-MM-dd HH:mm:ss
	*/
	
	public static String millsConvertDateStr(long mills) {
		Date date = new Date(mills);
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_TIME);
		
		return sdf.format(date);
	}
	
	/**
	 * 将毫秒数转化为最小单位为：yyyy-MM-dd HH
	 */
	
	public static String millsConvertDateHour(long mills) {
		Date date = new Date(mills);
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_HOUR_TIME);
		
		return sdf.format(date);
	}
	
	/**
	 * 将毫秒数转化为最小单位为：HH:mm
	 */
	
	public static String millsConvertDateMinute(long mills) {
		Date date = new Date(mills);
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME_MINUTE_TIME);
		
		return sdf.format(date);
	}
	
	/**
	 * 把 yyyy-MM-dd HH:mm:ss 格式的时间转换为毫秒。
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static long DateConvertMills(String date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE_TIME);
		return simpleDateFormat.parse(date).getTime();
	}
	
	/**
	 * 把yyyy-MM-dd HH 格式的时间转换为毫秒。
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static long DateHourConvertMills(String date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE_HOUR_TIME);
		long time = 0;
		try {
			time = simpleDateFormat.parse(date).getTime();
		} catch (ParseException e) {
			log.error("Date Hour Convert Mills", e);
		}
		return time;
	}
	
	/**
	 * 用文字来描述时间
	 * @param insertTime  yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getShowTime(String insertTime) {
		String showTime = "";
		try {
			int year = Integer.parseInt(insertTime.substring(0, 4));
			long time = DateConvertMills(insertTime);
			long timeDifference = System.currentTimeMillis() - time;
			long minute = timeDifference/1000/60;
			if (minute/60 < 24) {
				showTime = insertTime.substring(11, 19);
			} else if (year < Calendar.getInstance().get(Calendar.YEAR)) {
				showTime = insertTime.substring(0, 10);
			} else {
				showTime = insertTime.substring(5, 16);
			}
		} catch (ParseException e) {
			log.error("DateUtil.DateConvertMills error ", e);
		}
		return showTime;
	}
	
	public static String secondConvertDate(long countDownTime) {
		long hour = countDownTime / 3600;
    	long minute = countDownTime / 60 % 60;
    	long second = countDownTime % 60;
    	return ((hour <= 0)?"":hour + ":") + ((minute < 10)?"0" + minute:minute) + ":" + ((second < 10)?"0" + second:second);
	}
	
}
