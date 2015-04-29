package me.kkuai.kuailian.activity.login;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.activity.MainActivity;
import me.kkuai.kuailian.activity.register.Register;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.engine.LoginManage;
import me.kkuai.kuailian.engine.LoginManage.OnLoginSuccessListener;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.utils.Preference;
import me.kkuai.kuailian.utils.ShowToastUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Login extends BaseActivity implements OnClickListener {

	private Context context;
	private Button btnLoginRegister;
	private Button btnLogin;
	private Intent intent = null;
	private ImageView ivHead;
	private AutoCompleteTextView etMobileNumber;
	private EditText etPassword;
	private TextView tvLostPassword;
	private int i = 1;
	private String tel;
	private String password;
	private UserInfo currentUser;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AppConstantValues.HANDLER_RESULT_SUCCESS:
				finish();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_login);
		log.info("Login onCreate");
		initViews();
		setListener();
	}
	
	@Override
	public void initViews() {
		ivHead = (ImageView) findViewById(R.id.iv_head);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLoginRegister = (Button) findViewById(R.id.btn_login_register);
		etMobileNumber = (AutoCompleteTextView) findViewById(R.id.et_mobile_number);
		etPassword = (EditText) findViewById(R.id.et_password);
		tvLostPassword = (TextView) findViewById(R.id.tv_lost_password);
		
		etMobileNumber.setText(Preference.getUserName(context));
		etPassword.setText(Preference.getPassWord(context));
//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beauty);
//		ivHead.setImageBitmap(BitmapUtils.toRoundBitmap(bitmap));
	}
	
	@Override
	public void setListener() {
		btnLogin.setOnClickListener(this);
		btnLoginRegister.setOnClickListener(this);
		tvLostPassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			onLogin();
			break;
		case R.id.btn_login_register:
			intent = new Intent(context, Register.class);
			startActivity(intent);
			break;
		case R.id.tv_lost_password:
			Intent intent = new Intent(context, LostPssword.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void onLogin() {
		tel = etMobileNumber.getText().toString().trim();
		password = etPassword.getText().toString().trim();
		
		if ((tel.equals("") || tel == null) || (password == null || password.equals(""))) {
			ShowToastUtil.showToast(context, getString(R.string.login_toast_please_account));
			return;
		}
		LoginManage loginManage = new LoginManage(context);
		loginManage.onLogin(tel, password, new OnLoginSuccessListener() {
			
			@Override
			public void onLoginSuccess(String userStatus) {
				if (AppConstantValues.USER_STATUS_NORMAL.equals(userStatus)) {
					Intent intent = new Intent(context, MainActivity.class);
					context.startActivity(intent);
				}
				finish();
			}
			
			@Override
			public void onLoginError() {
				
			}
			
		});
	}
	
}
