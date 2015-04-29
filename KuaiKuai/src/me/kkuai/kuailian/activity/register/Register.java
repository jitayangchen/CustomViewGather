package me.kkuai.kuailian.activity.register;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.http.request.RegisterRequest;
import me.kkuai.kuailian.http.request.SendMobileCaptchaRequest;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.utils.Preference;
import me.kkuai.kuailian.utils.SHA1;
import me.kkuai.kuailian.utils.ShowToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class Register extends BaseActivity implements OnClickListener {

	private Context context;
	private Button btnRegister;
	private TextView tvUserProtocol;
	private Intent intent;
	private LinearLayout llBack;
	private EditText etRegisterMobileNumber;
	private Button btnRequestCaptcha;
	private EditText etInputLoginPassword;
	private EditText etInputCaptcha;
	private String tel;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_register);
		initViews();
		setListener();
	}
	
	@Override
	public void initViews() {
		btnRegister = (Button) findViewById(R.id.btn_register);
		tvUserProtocol = (TextView) findViewById(R.id.tv_user_protocol);
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		etRegisterMobileNumber = (EditText) findViewById(R.id.et_register_mobile_number);
		btnRequestCaptcha = (Button) findViewById(R.id.btn_request_captcha);
		etInputLoginPassword = (EditText) findViewById(R.id.et_input_login_password);
		etInputCaptcha = (EditText) findViewById(R.id.et_input_captcha);
	}
	
	@Override
	public void setListener() {
		btnRegister.setOnClickListener(this);
		tvUserProtocol.setOnClickListener(this);
		llBack.setOnClickListener(this);
		btnRequestCaptcha.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			register();
			break;
			
		case R.id.tv_user_protocol:
			intent = new Intent(context, UserProtocol.class);
			startActivity(intent);
			break;
			
		case R.id.ll_back:
			finish();
			break;
			
		case R.id.btn_request_captcha:
			requestCaptcha();
			break;

		default:
			break;
		}
	}

	private void register() {
		tel = etRegisterMobileNumber.getText().toString().trim();
		password = etInputLoginPassword.getText().toString().trim();
		String captcha = etInputCaptcha.getText().toString().trim();
		
		if (!checkValid(tel)) {
			ShowToastUtil.showToast(context, getString(R.string.toast_mobile_illegal));
			return;
		}
		if (TextUtils.isEmpty(tel)) {
			ShowToastUtil.showToast(context, getString(R.string.toast_mobile_null));
			return ;
		} else if (TextUtils.isEmpty(password)) {
			ShowToastUtil.showToast(context, getString(R.string.toast_password_null));
			return ;
		} else if (TextUtils.isEmpty(captcha)) {
			ShowToastUtil.showToast(context, getString(R.string.toast_captcha_null));
			return ;
		}
		
		RegisterRequest request = new RegisterRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				parseRegister((String) result);
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		request.requestRegister(tel, password, captcha);
	}

	private void requestCaptcha() {
		String tel = etRegisterMobileNumber.getText().toString().trim();
		if (!checkValid(tel)) {
			ShowToastUtil.showToast(context, getString(R.string.toast_mobile_illegal));
			return;
		}
		
		String hash = SHA1.getSHA1String("mobilesms.kkuai.com" + SHA1.getSHA1String(tel) + SHA1.getSHA1String("0"));
		
		SendMobileCaptchaRequest request = new SendMobileCaptchaRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				parseMobileCaptcha((String) result);
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestSendMobileCaptcha(AppConstantValues.CAPTCHA_TYPE_REGIST, tel, hash);
	}
	
	private boolean checkValid(String telStr) {
		
		Pattern pattern = Pattern.compile("^1[3|4|5|8][0-9]\\d{8}$");
		Matcher matcher = pattern.matcher(telStr);
		
		return matcher.matches();
	}
	
	private void parseMobileCaptcha(String request) {
		try {
			JSONObject obj = new JSONObject(request);
			String status = JsonUtil.getJsonString(obj, "status");
			String statusDetail = JsonUtil.getJsonString(obj, "statusDetail");
			if ("1".equals(status)) {
				ShowToastUtil.showToast(context, getString(R.string.toast_captcha_send_success));
				btnRequestCaptcha.setEnabled(false);
			} else if ("2".equals(status)) {
				ShowToastUtil.showToast(context, statusDetail);
			}
		} catch (JSONException e) {
			log.error("parse Mobile Captcha", e);
		}
	}
	
	private void parseRegister(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			String status = JsonUtil.getJsonString(obj, "status");
			String statusDetail = JsonUtil.getJsonString(obj, "statusDetail");
			if ("1".equals(status)) {
				String token = JsonUtil.getJsonString(obj, "token");
				String userStatus = JsonUtil.getJsonString(obj, "userStatus");
				String uid = JsonUtil.getJsonString(obj, "uid");
				
				Preference.saveToken(token);
				
				Intent intent = new Intent(context, FinishRegister.class);
				intent.putExtra("uid", uid);
				intent.putExtra("tel", tel);
				intent.putExtra("password", password);
				startActivity(intent);
			} else if ("2".equals(status)) {
				ShowToastUtil.showToast(context, statusDetail);
			}
		} catch (JSONException e) {
			log.error("parse Register", e);
		}
		
	}
	
}
