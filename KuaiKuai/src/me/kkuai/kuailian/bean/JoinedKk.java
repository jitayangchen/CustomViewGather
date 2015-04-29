package me.kkuai.kuailian.bean;

public class JoinedKk {

	private String id;
	
	private String roomId;
	
	private String payContentType;
	
	private String payContent;
	
	private String payTime;
	
	private String payAbidanceTime;
	
	private boolean isPast;
	
	private boolean isSpread = false;
	
	private String voiceTimeLength;
	
	private boolean isPlaying = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getPayContentType() {
		return payContentType;
	}

	public void setPayContentType(String payContentType) {
		this.payContentType = payContentType;
	}

	public String getPayContent() {
		return payContent;
	}

	public void setPayContent(String payContent) {
		this.payContent = payContent;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayAbidanceTime() {
		return payAbidanceTime;
	}

	public void setPayAbidanceTime(String payAbidanceTime) {
		this.payAbidanceTime = payAbidanceTime;
	}

	public boolean isPast() {
		return isPast;
	}

	public void setPast(boolean isPast) {
		this.isPast = isPast;
	}

	public boolean isSpread() {
		return isSpread;
	}

	public void setSpread(boolean isSpread) {
		this.isSpread = isSpread;
	}

	public String getVoiceTimeLength() {
		return voiceTimeLength;
	}

	public void setVoiceTimeLength(String voiceTimeLength) {
		this.voiceTimeLength = voiceTimeLength;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
}
