package me.kkuai.kuailian.adapter;

import me.kkuai.kuailian.widget.wheelview.WheelAdapter;

public class WheelDoubleAdapter implements WheelAdapter {

	private int min, max;
	private String unit;
	
	@Override
	public int getItemsCount() {
		return max - min + 1;
	}
	
	public String getItemValue(int index) {
		return min + index + "";
	}

	@Override
	public String getItem(int index) {
		return min + index + unit;
	}

	@Override
	public int getMaximumLength() {
		return -1;
	}

	public void setMinAndMax(int min, int max, String unit) {
		this.min = min;
		this.max = max;
		this.unit = unit;
	}
}
