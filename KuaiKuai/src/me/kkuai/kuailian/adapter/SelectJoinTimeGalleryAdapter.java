package me.kkuai.kuailian.adapter;

import java.util.Calendar;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.utils.DateUtil;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelectJoinTimeGalleryAdapter extends BaseAdapter {
	
	private Context context;
	private int selectedPosition = 0;
	private long currentHourMillis;
	private static final long hour2Millis = 60 * 60 * 1000;
	
	public SelectJoinTimeGalleryAdapter(Context context) {
		this.context = context;
		String millsConvertDateHour = DateUtil.millsConvertDateHour(System.currentTimeMillis());
		currentHourMillis = DateUtil.DateHourConvertMills(millsConvertDateHour);
	}

	@Override
	public int getCount() {
		return 24;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = getItemView(parent);
		}
		bindView(convertView, position);
		return convertView;
	}
	
	private void bindView(View convertView, int position) {
		ViewHolder vh = (ViewHolder) convertView.getTag();
		if (position == selectedPosition) {
			vh.rlItemEntirety.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
		} else {
			vh.rlItemEntirety.setBackgroundColor(context.getResources().getColor(R.color.gray_dfdfdf));
		}
		vh.tvJoinTime.setText(getTime(position));
	}

	private View getItemView(ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_select_join_time, parent, false);
		ViewHolder vh = new ViewHolder();
		vh.tvJoinTime = (TextView) view.findViewById(R.id.tv_join_time);
		vh.rlItemEntirety = (RelativeLayout) view.findViewById(R.id.rl_item_entirety);
		view.setTag(vh);
		return view;
	}
	
	class ViewHolder {
		TextView tvJoinTime;
		RelativeLayout rlItemEntirety;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}
	
	private String getTime(int position) {
		long time = hour2Millis * position + currentHourMillis;
		return DateUtil.millsConvertDateMinute(time);
	}
	
	public long getSelectedMillis(int position) {
		return hour2Millis * position + currentHourMillis;
	}

}
