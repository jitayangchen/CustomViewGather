package me.kkuai.kuailian.activity.login;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class LostPssword extends BaseActivity implements OnClickListener {
	
	private Context context;
	private LinearLayout llBack;
	private Button btnRequestCaptcha;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_lost_password);
		
		initViews();
		setListener();
	}
	
	@Override
	public void initViews() {
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		btnRequestCaptcha = (Button) findViewById(R.id.btn_request_captcha);
	}
	
	@Override
	public void setListener() {
		llBack.setOnClickListener(this);
		btnRequestCaptcha.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.btn_request_captcha:
			getIdentifyingCode();
			break;

		default:
			break;
		}
	}
	
	private void getIdentifyingCode() {
		
	}
}
