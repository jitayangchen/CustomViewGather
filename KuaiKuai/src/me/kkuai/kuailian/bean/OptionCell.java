package me.kkuai.kuailian.bean;

import java.util.List;

public class OptionCell {
	
	private String id;
	private String name;
	private String level;
	private String parentId;
	private List<OptionCell> childData;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public List<OptionCell> getChildData() {
		return childData;
	}
	public void setChildData(List<OptionCell> childData) {
		this.childData = childData;
	}
	
}
