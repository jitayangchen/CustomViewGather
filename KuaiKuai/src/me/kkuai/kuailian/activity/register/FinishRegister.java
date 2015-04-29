package me.kkuai.kuailian.activity.register;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.activity.MainActivity;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.http.request.RegisterSecondRequest;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.utils.Preference;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class FinishRegister extends BaseActivity implements OnClickListener {
	
	private Context context;
	private EditText etInputNickname;
	private Button btnSexMale;
	private Button btnSexFemale;
	private Button btnFinishRegister;
	private LinearLayout llBack;
	private ImageView ivMaleSelected;
	private ImageView ivFemaleSelected;
	private String sex = null;
	private String uid;
	private String tel;
	private String password;
	private String nickName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_finish_register);
		
		Intent intent = getIntent();
		uid = intent.getStringExtra("uid");
		tel = intent.getStringExtra("tel");
		password = intent.getStringExtra("password");
		
		initViews();
		setListener();
	}
	
	@Override
	public void initViews() {
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		etInputNickname = (EditText) findViewById(R.id.et_input_nickname);
		btnSexMale = (Button) findViewById(R.id.btn_sex_male);
		btnSexFemale = (Button) findViewById(R.id.btn_sex_female);
		ivMaleSelected = (ImageView) findViewById(R.id.iv_male_selected);
		ivFemaleSelected = (ImageView) findViewById(R.id.iv_female_selected);
		btnFinishRegister = (Button) findViewById(R.id.btn_finish_register);
		
	}
	
	@Override
	public void setListener() {
		btnSexMale.setOnClickListener(this);
		btnSexFemale.setOnClickListener(this);
		llBack.setOnClickListener(this);
		btnFinishRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sex_male:
			btnSexMale.setSelected(true);
			btnSexFemale.setSelected(false);
			ivMaleSelected.setVisibility(View.VISIBLE);
			ivFemaleSelected.setVisibility(View.GONE);
			sex = AppConstantValues.SEX_MALE;
			break;
			
		case R.id.btn_sex_female:
			btnSexFemale.setSelected(true);
			btnSexMale.setSelected(false);
			ivMaleSelected.setVisibility(View.GONE);
			ivFemaleSelected.setVisibility(View.VISIBLE);
			sex = AppConstantValues.SEX_FEMALE;
			break;
			
		case R.id.btn_finish_register:
			finishRegister();
			break;
			
		case R.id.ll_back:
			finish();
			break;

		default:
			break;
		}
	}
	
	private void finishRegister() {
		
		nickName = etInputNickname.getText().toString().trim();
		if (TextUtils.isEmpty(nickName)) {
			ShowToastUtil.showToast(context, getString(R.string.toast_nick_name_null));
			return ;
		}
		if (TextUtils.isEmpty(sex)) {
			ShowToastUtil.showToast(context, getString(R.string.toast_sex_null));
			return ;
		}
		
		RegisterSecondRequest request = new RegisterSecondRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				parseRegisterFinish((String) result);
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		request.requestRegisterSecond(sex, nickName);
	}
	
	private void parseRegisterFinish(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			String status = JsonUtil.getJsonString(obj, "status");
			String statusDetail = JsonUtil.getJsonString(obj, "statusDetail");
			if ("1".equals(status)) {
				UserInfo userInfo = new UserInfo();
				userInfo.setId(uid);
				userInfo.setNickName(nickName);
				userInfo.setSex(sex);
				Preference.saveUid(context, uid);
				Preference.saveUserName(context, tel);
				Preference.savePassWord(context, password);
				Preference.setAutoLogin(context, true);
				UserManager.getInstance().setCurrentUser(userInfo);
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
			} else if ("8".equals(status)) {
				ShowToastUtil.showToast(context, statusDetail);
			}
		} catch (JSONException e) {
			log.error("parse Register Finish", e);
		}
	}
}
