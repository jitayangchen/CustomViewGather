package me.kkuai.kuailian.engine;

public class LiveRoomDataUpdate {

	private static final LiveRoomDataUpdate instance = new LiveRoomDataUpdate();
	private DataUpdateListener dataUpdateListener;
	public final static int CURRENT_PLAY_DATA = 0;
	public final static int LOAD_FINISH = 1;
	public final static int UPDATE_ROOM_MESSAGE = 2;
	public final static int EVENT_LOGOUT = 3;
	
	private LiveRoomDataUpdate() {}
	
	public static LiveRoomDataUpdate getInstance() {
		return instance;
	}
	
	public void update(int type, Object data) {
		if (null != dataUpdateListener) {
			dataUpdateListener.dataUpdate(type, data);
		}
	}
	
	public interface DataUpdateListener {
		void dataUpdate(int type, Object data);
	}

	public void setDataUpdateListener(DataUpdateListener dataUpdateListener) {
		this.dataUpdateListener = dataUpdateListener;
	}
	
}
