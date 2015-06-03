package com.zihao.city;

import com.zihao.city.R;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {

	private CityPicker cityPicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		cityPicker = (CityPicker) findViewById(R.id.citypicker);
	}

}
