package me.kkuai.kuailian.adapter;

import android.content.Context;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.widget.wheelview.WheelAdapter;

public class WheelSelectBirthdayAdapter implements WheelAdapter {
	
	private Context context;
	private int minValue;
	private int maxValue;
	private int wheelWidth;
	
	public WheelSelectBirthdayAdapter(Context context) {
		this.context = context;
	}
	
	public void setValueRange(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	public int getItemsCount() {
		return maxValue - minValue + 1;
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			int value = minValue + index;
			return Integer.toString(value);
		}
		return null;
	}

	@Override
	public int getMaximumLength() {
		return -1;
	}

}
