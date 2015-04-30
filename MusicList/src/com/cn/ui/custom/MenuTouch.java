package com.cn.ui.custom;

import com.cn.lyric.activity.IndexGroupActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MenuTouch implements OnTouchListener {
	int startY = 0;
	int y = 0;
	int base = IndexGroupActivity.base;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		int action = arg1.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			y = (int) arg1.getRawY();
			startY = (int) IndexGroupActivity.bottomlistlin.getTranslationY();

		}
		
		if (action == MotionEvent.ACTION_MOVE) {
			int a = startY + ((int) arg1.getRawY() - y);
			if (a <= base && a >= 0) {
				IndexGroupActivity.bottomlistlin.setTranslationY(a);
			}
		}
		if (action == MotionEvent.ACTION_UP) {
			if (IndexGroupActivity.bottomlistlin.getTranslationY() > base / 2) {
				IndexGroupActivity.bottomlistlin.setTranslationY(base);
			} else {
				IndexGroupActivity.bottomlistlin.setTranslationY(0);
			}
		}
		return false;
	}

}
