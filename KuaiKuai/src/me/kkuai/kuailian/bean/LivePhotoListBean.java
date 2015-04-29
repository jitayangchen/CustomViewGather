package me.kkuai.kuailian.bean;

import java.util.ArrayList;

public class LivePhotoListBean {

	private String nid;
	private String content;
	private String type;
	private String time;
	private ArrayList<DataList> dataLists;
	private String dataType;
	
	public class DataList {
		private String id;
		private String path;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public ArrayList<DataList> getDataLists() {
		return dataLists;
	}

	public void setDataLists(ArrayList<DataList> dataLists) {
		this.dataLists = dataLists;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	
}
