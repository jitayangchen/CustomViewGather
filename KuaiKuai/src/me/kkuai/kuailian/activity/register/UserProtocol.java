package me.kkuai.kuailian.activity.register;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;

public class UserProtocol extends BaseActivity implements OnClickListener {

	private TextView tvProtocolContent;
	private String protocolContent;
	private LinearLayout llBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_protocol);
		
		initViews();
		setListener();
	}
	
	@Override
	public void initViews() {
		tvProtocolContent = (TextView) findViewById(R.id.tv_protocol_content);
		protocolContent = getString(R.string.user_protocol_content_detail);
		tvProtocolContent.setText(protocolContent);
		llBack = (LinearLayout) findViewById(R.id.ll_back);
	}
	
	@Override
	public void setListener() {
		llBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;

		default:
			break;
		}
	}
}
