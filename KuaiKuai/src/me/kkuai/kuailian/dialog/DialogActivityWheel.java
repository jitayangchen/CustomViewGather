package me.kkuai.kuailian.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.adapter.WheelSingleAdapter;
import me.kkuai.kuailian.utils.ShowToastUtil;
import me.kkuai.kuailian.widget.wheelview.WheelView;

public class DialogActivityWheel extends BaseActivity implements OnClickListener {
	
	private Context context;
	private WheelView wheelLeft;
	private WheelView wheelRight;
	private Button btnFinish;
	private WheelSingleAdapter wheelLeftAdapter;
	private WheelSingleAdapter wheelRightAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.dialog_wheel);
		
		initViews();
		setListener();
	}
	
	@Override
	public void initViews() {
		wheelLeft = (WheelView) findViewById(R.id.wheel_left);
		wheelRight = (WheelView) findViewById(R.id.wheel_right);
		wheelLeftAdapter = new WheelSingleAdapter();
		wheelRightAdapter = new WheelSingleAdapter();
		wheelLeft.setAdapter(wheelLeftAdapter);
		wheelLeft.setVisibleItems(5);
		wheelLeft.setCurrentItem(1);
		wheelLeft.setCyclic(true);
		wheelLeft.TEXT_SIZE = 20;
		
		wheelRight.setAdapter(wheelRightAdapter);
		wheelRight.setCyclic(true);
		wheelRight.setVisibleItems(5);
		wheelRight.setCurrentItem(1);
		
		btnFinish = (Button) findViewById(R.id.btn_finish);
	}
	
	public void setListener() {
		btnFinish.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_finish:
			ShowToastUtil.showToast(context, wheelLeftAdapter.getItem(wheelLeft.getCurrentItem()));
			break;

		default:
			break;
		}
	}
}
