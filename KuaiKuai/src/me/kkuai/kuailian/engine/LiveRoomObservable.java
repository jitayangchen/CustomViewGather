package me.kkuai.kuailian.engine;

import java.util.Observable;
import java.util.Observer;

public class LiveRoomObservable extends Observable {
	
	private static final LiveRoomObservable instance = new LiveRoomObservable();

	private LiveRoomObservable() {}
	
	public static LiveRoomObservable getInstance() {
		return instance;
	}
	
	public void addObserver(Observer observer) {
		super.addObserver(observer);
	}

	public void updateObserver(Object data) {
		setChanged();
		notifyObservers(data);
	}
	
}
