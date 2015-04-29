package me.kkuai.kuailian.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HttpRequestQueue {
	
	private Map<UUID, HttpRequestBean> uuid2Request = new HashMap<UUID, HttpRequestBean>();
	private Map<UUID, HttpRequesterListener> uuid2RequestListener = new HashMap<UUID, HttpRequesterListener>();
	private List<UUID> uuids = new ArrayList<UUID>();
	
	public UUID add(HttpRequesterListener requesterListener, HttpRequestBean httpRequestBean) {
		UUID uuid = getUUID();
		uuid2Request.put(uuid, httpRequestBean);
		uuid2RequestListener.put(uuid, requesterListener);
		uuids.add(uuid);
		return uuid;
	}
	
	public UUID getNextRequestUUID() {
		UUID uuid = null;
		if (uuids.size() > 0) {
			uuid = uuids.get(0);
		}
		return uuid;
	}
	
	public HttpRequestBean getHttpRequestBean(UUID uuid) {
		return uuid2Request.get(uuid);
	}
	
	public HttpRequesterListener getHttpRequesterListener(UUID uuid) {
		return uuid2RequestListener.get(uuid);
	}
	
	public void remove(UUID uuid) {
		uuid2Request.remove(uuid);
		uuid2RequestListener.remove(uuid);
		uuids.remove(uuid);
	}
	
	public void removeAll() {
		uuid2Request.clear();
		uuid2RequestListener.clear();
		uuids.clear();
	}
	
	public UUID getUUID()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID().toString());
        sb.append(new Date().getTime());
        sb.append(UUID.randomUUID().toString());
        
        return UUID.nameUUIDFromBytes(sb.toString().getBytes());
    }
}
