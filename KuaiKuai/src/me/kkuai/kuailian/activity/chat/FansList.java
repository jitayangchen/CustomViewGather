package me.kkuai.kuailian.activity.chat;

import android.content.Context;
import android.os.Bundle;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;

public class FansList extends BaseActivity {
	
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fans_list);
		context = this;
		
		initViews();
		setListener();
	}
	
	@Override
	public void initViews() {
		
	}
	
	@Override
	public void setListener() {
		
	}
}
