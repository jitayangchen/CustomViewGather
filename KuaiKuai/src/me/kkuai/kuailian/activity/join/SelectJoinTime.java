package me.kkuai.kuailian.activity.join;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.adapter.SelectJoinTimeGalleryAdapter;
import me.kkuai.kuailian.http.request.RoomSignUpInfoRequest;
import me.kkuai.kuailian.utils.DateUtil;
import me.kkuai.kuailian.widget.PieChart;
import me.kkuai.kuailian.widget.PieChart.OnSelectedLisenter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class SelectJoinTime extends BaseActivity implements OnClickListener {
	
	private Context context;
	private Gallery gallerySelectJoinTime;
	private String roomId;
	private List<Integer> times = null;
	private PieChart timePiecChart;
	private SelectJoinTimeGalleryAdapter galleryAdapter;
	private TextView tvSelectedTime;
	private String currentRequestTimeHour;
	private static long threeMinute2Millis = 3 * 60 * 1000;
	private LinearLayout llBack;
	private TextView tvSaveTime;
	private long startTime;
	private TextView tvSelectedDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_join_time);
		context = this;
		
		Intent intent = getIntent();
		roomId = intent.getStringExtra("roomId");
		
		initViews();
		setListener();
		
		currentRequestTimeHour = DateUtil.millsConvertDateHour(System.currentTimeMillis());
		requestRoomInfo(currentRequestTimeHour);
		tvSelectedDate.setText(currentRequestTimeHour.substring(0, 10));
	}
	
	public void initViews() {
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		tvSelectedDate = (TextView) findViewById(R.id.tv_selected_date);
		tvSaveTime = (TextView) findViewById(R.id.tv_save_time);
		tvSelectedTime = (TextView) findViewById(R.id.tv_selected_time);
		gallerySelectJoinTime = (Gallery) findViewById(R.id.gallery_select_join_time);
		timePiecChart = (PieChart) findViewById(R.id.time_piec_chart);
		ArrayList<Float> alPercentage = new ArrayList<Float>();
		for (int i = 0; i < 20; i++) {
			alPercentage.add(5.0f);
		}
		try {
			timePiecChart.setAdapter(alPercentage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		timePiecChart.setOnSelectedListener(new OnSelectedLisenter() {
			
			@Override
			public void onSelected(int iSelectedIndex) {
				setSelectedTime(iSelectedIndex);
				log.info("iSelectedIndex === " + iSelectedIndex);
			}
		});
		
		
		galleryAdapter = new SelectJoinTimeGalleryAdapter(context);
		gallerySelectJoinTime.setAdapter(galleryAdapter);
		gallerySelectJoinTime.setUnselectedAlpha(1.0f);
		
		gallerySelectJoinTime.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				log.info("Click position === " + position);
				galleryAdapter.setSelectedPosition(position);
				galleryAdapter.notifyDataSetChanged();
				
				currentRequestTimeHour = DateUtil.millsConvertDateHour(galleryAdapter.getSelectedMillis(position));
				requestRoomInfo(currentRequestTimeHour);
				
				tvSelectedDate.setText(currentRequestTimeHour.substring(0, 10));
			}
		});
	}
	
	@Override
	public void setListener() {
		llBack.setOnClickListener(this);
		tvSaveTime.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.tv_save_time:
			Intent intent = new Intent();
			intent.putExtra("playTime", startTime);
			setResult(1000, intent);
			finish();
			break;

		default:
			break;
		}
	}
	
	private void requestRoomInfo(String time) {
		
		RoomSignUpInfoRequest request = new RoomSignUpInfoRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				times = (List<Integer>) result;
				
				tvSelectedTime.setText(getString(R.string.select_nuselect_time));
				timePiecChart.setTimes(times);
				timePiecChart.setiSelectedIndex(-1);
				timePiecChart.invalidate();
				
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		request.requestRoomSignUpInfo(roomId, "1", time, "1");
	}
	
	private void setSelectedTime(int position) {
		long requestTime = DateUtil.DateHourConvertMills(currentRequestTimeHour);
		startTime = requestTime + (position*threeMinute2Millis);
		long endTime = startTime + threeMinute2Millis;
		tvSelectedTime.setText(DateUtil.millsConvertDateMinute(startTime) + "-" + DateUtil.millsConvertDateMinute(endTime));
	}
	
}
