package me.kkuai.kuailian.adapter;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.bean.OptionCell;
import me.kkuai.kuailian.widget.wheelview.WheelAdapter;

public class WheelSingleAdapter implements WheelAdapter {

	private List<OptionCell> datas = new ArrayList<OptionCell>();
	
	@Override
	public int getItemsCount() {
		return datas.size();
	}

	@Override
	public String getItem(int index) {
		return datas.get(index).getName();
	}

	@Override
	public int getMaximumLength() {
		return 720;
	}

	public List<OptionCell> getDatas() {
		return datas;
	}

	public void setDatas(List<OptionCell> datas) {
		this.datas = datas;
	}

}
