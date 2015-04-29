package me.kkuai.kuailian.adapter;

import java.util.List;

import me.kkuai.kuailian.bean.OptionCell;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.widget.wheelview.WheelAdapter;

public class WheelSelectAreaAdapter implements WheelAdapter {

	private Log log = LogFactory.getLog(WheelSelectAreaAdapter.class);
	private List<OptionCell> optionCells;
	
	@Override
	public int getItemsCount() {
		return optionCells.size();
	}

	@Override
	public String getItem(int index) {
		return optionCells.get(index).getName();
	}

	@Override
	public int getMaximumLength() {
		return -1;
	}

	public void setOptionCells(List<OptionCell> optionCells) {
		this.optionCells = optionCells;
	}

	public List<OptionCell> getOptionCells() {
		return optionCells;
	}
	
}
