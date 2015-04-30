package com.cn.lyric;

import java.util.TreeMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LyricView extends View {
	private float mX; // ��ĻX����е㣬��ֵ�̶������ָ����X�м���ʾ
	private float offsetY; // �����Y���ϵ�ƫ��������ֵ����ݸ�ʵĹ�����С
	private float touchY; // ���������Viewʱ������Ϊ��ǰ�����Y������
	private int lrcIndex = 0; // ������TreeMap���±�
	private int wordsize = 0;// ��ʾ������ֵĴ�Сֵ
	private int diatance = 25;// ���ÿ�еļ��
	Paint paint = new Paint();// ���ʣ����ڻ����Ǹ����ĸ��
	Paint paintHL = new Paint(); // ���ʣ����ڻ������ĸ�ʣ�����ǰ���������

	public LyricView(Context context) {
		super(context);
		init();
	}

	public LyricView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (IndexLrc.haslrc) {
			paintHL.setTextSize(wordsize + 3);
			paint.setTextSize(wordsize);
			LyricObject temp = IndexLrc.lrc_map.get(lrcIndex);
			try {
				canvas.drawText(temp.lrc, mX, offsetY + (wordsize + diatance)
						* lrcIndex, paintHL);
			} catch (Exception e) {

			}
			// ����ǰ���֮ǰ�ĸ��
			for (int i = lrcIndex - 1; i >= 0; i--) {
				temp = IndexLrc.lrc_map.get(i);
				if (offsetY + (wordsize + diatance) * i < 0) {
					break;
				}
				try {
					canvas.drawText(temp.lrc, mX, offsetY
							+ (wordsize + diatance) * i, paint);
				} catch (Exception e) {

				}
			}
			// ����ǰ���֮��ĸ��
			for (int i = lrcIndex + 1; i < IndexLrc.lrc_map.size(); i++) {
				temp = IndexLrc.lrc_map.get(i);
				if (offsetY + (wordsize + diatance) * i > 1280) {
					break;
				}
				try {
					canvas.drawText(temp.lrc, mX, offsetY
							+ (wordsize + diatance) * i, paint);
				} catch (Exception e) {

				}
			}
		} else if (LyricStatue.searching) {
			paint.setTextSize(32);
			canvas.drawText("���������...", mX, 400, paint);
		} else if (LyricStatue.downloading) {
			paint.setTextSize(32);
			canvas.drawText("�������ظ��...", mX, 400, paint);
		} else {
			paint.setTextSize(32);
			canvas.drawText("�ֶ����Ҹ��", mX, 400, paint);
		}
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float tt = event.getY();
		if (!IndexLrc.haslrc) {
			return super.onTouchEvent(event);
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			touchY = tt - touchY;
			offsetY = offsetY + touchY;
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		touchY = tt;
		return true;
	}

	public void init() {
		IndexLrc.lrc_map = new TreeMap<Integer, LyricObject>();
		offsetY = 440;
		paint = new Paint();
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setColor(Color.parseColor("#b7b7b7"));
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setAlpha(180);

		paintHL = new Paint();
		paintHL.setTextAlign(Paint.Align.CENTER);
		paintHL.setColor(Color.parseColor("#ffffff"));
		paintHL.setTypeface(Typeface.DEFAULT_BOLD);
		paintHL.setAntiAlias(true);
		paintHL.setAlpha(255);
	}

	/**
	 * ���ݸ����������Ǿ���ȷ���������Ĵ�С
	 */
	public void SetTextSize() {
		if (!IndexLrc.haslrc) {
			return;
		}
		wordsize = 32;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mX = w * 0.5f;
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * ��ʹ������ٶ� ���ظ�ʹ������ٶ�
	 */
	public Float SpeedLrc() {
		float speed = 0;
		if (offsetY + (wordsize + diatance) * lrcIndex > 220) {
			speed = ((offsetY + (wordsize + diatance) * lrcIndex - 220) / 20);
		} else if (offsetY + (wordsize + diatance) * lrcIndex < 220) {
			speed = (offsetY + (wordsize + diatance) * lrcIndex - 220) / 20;
		}
		return speed;
	}

	/**
	 * ����ǰ�ĸ����Ĳ���ʱ�䣬�Ӹ����������һ�� ��ǰ�����Ĳ���ʱ�� ���ص�ǰ��ʵ�����ֵ
	 */
	public int SelectIndex(int time) {
		if (!IndexLrc.haslrc) {
			return 0;
		}
		int index = 0;
		for (int i = 0; i < IndexLrc.lrc_map.size(); i++) {
			LyricObject temp = IndexLrc.lrc_map.get(i);
			if (temp.begintime < time) {
				++index;
			}
		}
		lrcIndex = index - 1;
		if (lrcIndex < 0) {
			lrcIndex = 0;
		}
		return lrcIndex;
	}

	public float getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

	public int getSIZEWORD() {
		return wordsize;
	}

	public void setSIZEWORD(int sIZEWORD) {
		wordsize = sIZEWORD;
	}

}
