package me.kkuai.kuailian.engine;

import java.util.Observable;
import java.util.Observer;

public class UpdateDataObservable extends Observable {
	
	private UpdateDataObservable() {}
	
	private static final UpdateDataObservable instance = new UpdateDataObservable();
	
	public static UpdateDataObservable getInstance() {
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
