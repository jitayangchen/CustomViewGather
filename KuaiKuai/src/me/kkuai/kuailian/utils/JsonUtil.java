package me.kkuai.kuailian.utils;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	protected static Log log = LogFactory.getLog(JsonUtil.class);
	
	public static String getJsonString(JSONObject obj,String key){
		if(obj.has(key)){
			String res = null;
			try {
				res = obj.getString(key);
//				if (res.contains("[") || res.contains("]")) 
//					res = res.substring(1, res.length()-1);
				
					
			} catch (JSONException e) {
				log.error("json:"+obj.toString()+" get property:"+key+" error!" );
			}
			return res;
		}
		return null;
	}
	
	public static int getJsonInt(JSONObject obj, String key) {
		if (obj.has(key)) {
			int res = 0;
			try {
				res = obj.getInt(key);
			} catch (JSONException e) {
				log.error("json:"+obj.toString()+" get property:"+key+" error!" );
			}
			return res;
		}
		return 0;
	}
	
	public static long getJsonLong(JSONObject obj, String key) {
		if (obj.has(key)) {
			long res = 0;
			try {
				res = obj.getLong(key);
			} catch (JSONException e) {
				log.error("json:"+obj.toString()+" get property:"+key+" error!" );
			}
			return res;
		}
		return 0;
	}
	
	public static boolean getJsonBoolean(JSONObject obj, String key) {
		boolean res = false;
		if (obj.has(key)) {
			try {
				res = obj.getBoolean(key);
			} catch (JSONException e) {
				log.error("json:"+obj.toString()+" get property:"+key+" error!" );
			}
		}
		return res;
	}
	
	public static int parseToInt(String str){
		return Integer.parseInt((str==null||str.equals(""))?"0":str);
	}
	
	public static JSONObject getJsonObject(JSONObject obj,String key){
		if(obj.has(key)){
			JSONObject res = null;
			try {
				res = obj.getJSONObject(key);
			} catch (JSONException e) {
				log.error("json:"+obj.toString()+" get property:"+key+" error!" );
			}
			return res;
		}
		return null;
	}
	
	public static JSONObject getJsonObject(JSONArray arr, int i) {
		JSONObject res = null;
		try {
			res = arr.getJSONObject(i);
		} catch (JSONException e) {
			log.error("json:"+arr.toString()+" get property:"+i+" error!" );
		}
		return res;
	}
	
	public static JSONArray getJsonArrayObject(JSONObject obj, String key) {
		if (obj.has(key)) {
			JSONArray res = null;
			try {
				res = obj.getJSONArray(key);
			} catch (JSONException e) {
				log.error("json:"+obj.toString()+" get property:"+key+" error!" );
			}
			return res;
		}
		return null;
	}

}
