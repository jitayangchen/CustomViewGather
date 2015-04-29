package me.kkuai.kuailian.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class KeyboardListenerLinearLayout extends LinearLayout {
	
	private KeyboardListener keyboardListener;
	private int lastBottom = 0;
	private boolean isShow = false;

	public KeyboardListenerLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		
//		Log.i("onLayout", "changed === " + changed);
//		Log.i("onLayout", "l === " + l);
//		Log.i("onLayout", "t === " + t);
//		Log.i("onLayout", "r === " + r);
//		Log.i("onLayout", "b === " + b);
		if (lastBottom != 0 && lastBottom != getBottom()) {
			lastBottom = getBottom();
			Log.i("onLayout", "isShowKeyboard === " + !isShow + "");
			isShow = !isShow;
			if (null != keyboardListener) {
				keyboardListener.keyboardShowStatus(isShow);
			}
		}
		lastBottom = getBottom();
		Log.i("onLayout", "getBottom() === " + getBottom());
	}
	
	public interface KeyboardListener {
		void keyboardShowStatus(boolean isShow);
	}

	public void setKeyboardListener(KeyboardListener keyboardListener) {
		this.keyboardListener = keyboardListener;
	}

}
