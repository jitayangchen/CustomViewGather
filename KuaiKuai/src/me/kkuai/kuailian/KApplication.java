package me.kkuai.kuailian;

import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.bean.OptionCell;
import me.kkuai.kuailian.bean.OwnerPhotoBean;
import me.kkuai.kuailian.data.JsonParser;
import me.kkuai.kuailian.http.request.RefreshTokenRequest;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.service.CoreService;
import me.kkuai.kuailian.service.RoomService;
import me.kkuai.kuailian.service.socket.SocketPool;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.utils.ShowToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Xml;

import com.kkuai.libs.J_LibsConfig;
import com.kkuai.libs.J_SDK;
import com.kkuai.libs.exceptions.J_StatisticsException;
import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_StatisticsManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class KApplication extends Application implements UncaughtExceptionHandler {
	
	private Log log = LogFactory.getLog(KApplication.class);
	public static Context context;
	private int position = 0;
	public static List<OwnerPhotoBean> photos = null;
	public static boolean isGetKCoin = true;
	public static int coin = 0;
	public static List<String> follows = new ArrayList<String>();
	public static List<String> firstLoves = new ArrayList<String>();
	public static List<String> loves = new ArrayList<String>();
//	private UncaughtExceptionHandler orgHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		
		initK_libs();
//		orgHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		initImageLoader(getApplicationContext());
		
		assetManager = getAssets();
		new Thread() {
			public void run() {
				readFirstArea();
			};
		}.start();
	}
	
	private void initK_libs() {
		J_LibsConfig config = new J_LibsConfig();
		JsonParser jsonParser = new JsonParser();
		config.setJ_JsonParser(jsonParser);
		config.LOG_ENABLE = false;
		LogFactory.isDebugEnable = false;
		J_SDK.start(config, context);
//		initStatisticsManager();
		initNetManager();
	}
	
	public static void refreshToken() {
//		J_NetManager.getInstance().setFreshTokenRules(RequestParams.TOKEN_EXPIRE);
		J_NetManager.getInstance().setFreshTokenRequest(new RefreshTokenRequest());
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		com.nostra13.universalimageloader.utils.L.disableLogging();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		log.error("KApplication error ", ex);
		UserManager.getInstance().setCurrentUser(null);
//		KHttpRequest.getInstance().stop();
		SocketPool.getInatance(getApplicationContext()).getSocketService().stop();
		Intent service = new Intent(this, CoreService.class);
		stopService(service);
		Intent roomService = new Intent(this, RoomService.class);
		stopService(roomService);
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("me.kkuai.kuailian",
				"me.kkuai.kuailian.activity.Splash"));
		intent.setAction(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public static List<OptionCell> getAreaData() {
		return optionCell;
	}
	
	private static List<OptionCell> optionCell;
	private AssetManager assetManager;
	/**
	 * read native area data
	 */
	public void readFirstArea() {
		try {
			InputStream fis =  assetManager.open("firstarea.xml");
			XmlPullParser pullParser = Xml.newPullParser();
			pullParser.setInput(fis, "UTF-8");
			int event = pullParser.getEventType();
			
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					optionCell = new ArrayList<OptionCell>();
					break;
				case XmlPullParser.START_TAG:
					if ("firstarea".equals(pullParser.getName())) {
						String firstarea = pullParser.nextText();
						parseFirstArea(firstarea);
					}
					break;
				case XmlPullParser.END_TAG:
					if ("firstarea".equals(pullParser.getName())) {
						readCitys();
					}
					break;

				default:
					break;
				}
				event = pullParser.next();
			}
		} catch (Exception e) {
			log.error("read First Area", e);
		}
	}
	
	public void readCitys() {
		try {
			InputStream fis =  assetManager.open("citys.xml");
			XmlPullParser pullParser = Xml.newPullParser();
			pullParser.setInput(fis, "UTF-8");
			int event = pullParser.getEventType();
			List<OptionCell> cells = null;
			
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if ("cityinfo".equals(pullParser.getName())) {
						cells = new ArrayList<OptionCell>();
						String city = pullParser.nextText();
						parseCitys(cells, city);
					}
					break;
				case XmlPullParser.END_TAG:
					if ("cityinfo".equals(pullParser.getName())) {
						optionCell.get(position++).setChildData(cells);
					}
					break;

				default:
					break;
				}
				event = pullParser.next();
			}
		} catch (Exception e) {
			log.error("read Citys", e);
		}
	}
	
	private void parseFirstArea(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			String status = JsonUtil.getJsonString(obj, "status");
			String msg = JsonUtil.getJsonString(obj, "statusDetail");
			if ("1".equals(status)) {
				JSONArray pacLists = JsonUtil.getJsonArrayObject(obj, "pacList");
				for (int i = 0; i < pacLists.length(); i++) {
					JSONObject pacList = JsonUtil.getJsonObject(pacLists, i);
					OptionCell cell = new OptionCell();
					cell.setId(JsonUtil.getJsonString(pacList, "id"));
					cell.setLevel(JsonUtil.getJsonString(pacList, "level"));
					cell.setName(JsonUtil.getJsonString(pacList, "name"));
					cell.setParentId(JsonUtil.getJsonString(pacList, "parentId"));
					optionCell.add(cell);
				}
			} else {
				ShowToastUtil.showToast(this, msg);
			}
			readCitys();
		} catch (JSONException e) {
			log.error("parse First Area", e);
		}
	}
	
	private void parseCitys(List<OptionCell> cells, String city) {
		try {
			JSONObject obj = new JSONObject(city);
			String status = JsonUtil.getJsonString(obj, "status");
			String msg = JsonUtil.getJsonString(obj, "statusDetail");
			if ("1".equals(status)) {
				JSONArray pacLists = JsonUtil.getJsonArrayObject(obj, "pacList");
				for (int i = 0; i < pacLists.length(); i++) {
					JSONObject pacList = JsonUtil.getJsonObject(pacLists, i);
					OptionCell cell = new OptionCell();
					cell.setId(JsonUtil.getJsonString(pacList, "id"));
					cell.setLevel(JsonUtil.getJsonString(pacList, "level"));
					cell.setName(JsonUtil.getJsonString(pacList, "name"));
					cell.setParentId(JsonUtil.getJsonString(pacList, "parentId"));
					cells.add(cell);
				}
				optionCell.get(position++).setChildData(cells);
			} else {
				ShowToastUtil.showToast(this, msg);
			}
		} catch (JSONException e) {
			log.error("parse Citys", e);
		}
	}
	
	private void initStatisticsManager(){
		Bundle bundle = new Bundle();
//		bundle.putString(J_StatisticsManager.PARAM_APP_KEY, BAIDU_STATISTICS_KEY);   //appKey
		bundle.putString(J_StatisticsManager.PARAM_APP_CHANNEL, "Raven");   //渠道号
		bundle.putBoolean(J_StatisticsManager.PARAM_CRASH_LOG_ENABLE, true);  //是否抓取崩溃日志，仅限百度
		bundle.putBoolean(J_StatisticsManager.PARAM_IS_DEBUG_ON, true);    //是否开启调试模式（日志是否打印）
		bundle.putBoolean(J_StatisticsManager.PARAM_CRASH_LOG_SEND_WITH_WIFI_ONLY, true);    //仅在WiFi环境下上传崩溃日志
		bundle.putInt(J_StatisticsManager.PARAM_TYPE, J_StatisticsManager.STATISTICS_BAIDU);  //要启动哪种统计平台
		bundle.putInt(J_StatisticsManager.PARAM_CRASH_LOG_SEND_DELAY, 20);  //0~30，发送错误日志的延时时间，仅限百度，单位:秒
		bundle.putInt(J_StatisticsManager.PARAM_CRASH_LOG_SEND_INTERVAL, 20);  //1~24，发送错误日志间隔，仅限百度，单位:小时
		
		J_StatisticsManager statisticsManager = J_StatisticsManager.getInstance();
		try {
			statisticsManager.init(context, bundle);
		} catch (J_StatisticsException e) {
			e.printStackTrace();
		}
	}
	
	private void initNetManager(){
		J_NetManager.TIMEOUT_HTTP_REQUEST = 10000;
		J_NetManager.TIMEOUT_DOWNLOAD_REQUEST = 2000;
		J_NetManager.TIMEOUT_UPLOAD_REQUEST = 2000;
		J_NetManager.RETRY_HTTP_TIMES = 1 ;
	}
}
