package com.pepoc.androidpiedemo;

import java.util.ArrayList;

import net.kenyang.piechart.PieChart;
import net.kenyang.piechart.PieChart.OnSelectedLisenter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		PieChart pie = (PieChart) findViewById(R.id.pieChart);
		
		ArrayList<Float> alPercentage = new ArrayList<Float>();
		for (int i = 0; i < 20; i++) {
			alPercentage.add(5.0f);
		}
		
		try {
			  // setting data
			  pie.setAdapter(alPercentage);

			  // setting a listener 
			  pie.setOnSelectedListener(new OnSelectedLisenter() {
			    @Override
			    public void onSelected(int iSelectedIndex) {
			      Toast.makeText(context, "Select index:" + iSelectedIndex, Toast.LENGTH_SHORT).show();
			    }
			  });  
			} catch (Exception e) {
			  if (e.getMessage().equals(PieChart.ERROR_NOT_EQUAL_TO_100)){
			    Log.e("kenyang","percentage is not equal to 100");
			  }
			}
	}

}
