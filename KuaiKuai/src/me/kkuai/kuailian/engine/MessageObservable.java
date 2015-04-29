package me.kkuai.kuailian.engine;

import java.util.Observable;
import java.util.Observer;

public class MessageObservable extends Observable {
	
	private MessageObservable() {}
	
	private static final MessageObservable instance = new MessageObservable();
	
	public static MessageObservable getInstance() {
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
