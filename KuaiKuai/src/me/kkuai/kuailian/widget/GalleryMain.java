package me.kkuai.kuailian.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class GalleryMain extends Gallery {

	public GalleryMain(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GalleryMain(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GalleryMain(Context context) {
		super(context);
	}
	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int keyCode; 

        if (isScrollingLeft(e1, e2)) {       
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT; 
        } else { 
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT; 

        } 

        onKeyDown(keyCode, null); 
//
//        return super.onFling(e1, e2, 0, velocityY); 
        return true; 
	}
	
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }

}
