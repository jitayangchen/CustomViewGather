package me.kkuai.kuailian.dialog;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.adapter.WheelDoubleAdapter;
import me.kkuai.kuailian.utils.ShowToastUtil;
import me.kkuai.kuailian.widget.wheelview.OnWheelChangedListener;
import me.kkuai.kuailian.widget.wheelview.WheelView;
import android.app.AlertDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogDoubleWheel extends AlertDialog implements OnClickListener {
	
	private Context context;
	private WheelView wheelLeft;
	private WheelView wheelRight;
	private Button btnFinish;
	private WheelDoubleAdapter wheelLeftAdapter;
	private WheelDoubleAdapter wheelRightAdapter;
	private int screenWidth;
	private SoundPool pool;
	private int pickerSound;
	private int min, max;
	private String unit;
	private final static int TEXT_SIZE_PROPORTION = 20;
	private TextView tvTitle;
	private DialogDoubleWheelChangeListener dialogDoubleWheelChangeListener;
	private Button btnCancel;

	public DialogDoubleWheel(Context context) {
		super(context, R.style.editDialogStyle);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_double_wheel);
		
		initViews();
		setListener();
		
		pool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		pickerSound = pool.load(context, R.raw.number_picker_value_change, 1);
	}

	private void initViews() {
		tvTitle = (TextView) findViewById(R.id.tv_title);
		wheelLeft = (WheelView) findViewById(R.id.wheel_left);
		wheelRight = (WheelView) findViewById(R.id.wheel_right);
		wheelLeftAdapter = new WheelDoubleAdapter();
		wheelRightAdapter = new WheelDoubleAdapter();
//		wheelLeft.setAdapter(wheelLeftAdapter);
//		wheelLeft.setCyclic(true);
		wheelLeft.TEXT_SIZE = screenWidth / TEXT_SIZE_PROPORTION;
		
//		wheelRight.setAdapter(wheelRightAdapter);
//		wheelRight.setCyclic(true);
		wheelRight.TEXT_SIZE = screenWidth / TEXT_SIZE_PROPORTION;
		
		btnFinish = (Button) findViewById(R.id.btn_finish);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
	}
	
	private void setListener() {
		btnFinish.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		wheelLeft.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onFinishScroll(boolean finishScroll) {
				int currentItem = wheelLeft.getCurrentItem();
				wheelRightAdapter.setMinAndMax(min + currentItem, max, unit);
				wheelRight.setAdapter(wheelRightAdapter);
				wheelRight.setCurrentItem(0);
			}
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				pool.play(pickerSound, 1, 1, 0, 0, 1);
			}
		});
		
		wheelRight.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onFinishScroll(boolean finishScroll) {
				
			}
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				pool.play(pickerSound, 1, 1, 0, 0, 1);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_finish:
			if (null != dialogDoubleWheelChangeListener) {
				dialogDoubleWheelChangeListener.dialogDoubleOk(wheelLeftAdapter.getItemValue(wheelLeft.getCurrentItem()) + wheelRightAdapter.getItemValue(wheelRight.getCurrentItem()));
			}
			dismiss();
			break;
		case R.id.btn_cancel:
			dismiss();
			break;

		default:
			break;
		}
	}
	
	public void setCurrent(String age, String type) {
		if (TextUtils.isEmpty(age)) {
			return ;
		}
		int minAge = 0, maxAge = 0;
		if ("ageRange".equals(type)) {
			minAge = Integer.parseInt(age.substring(0, 2));
			maxAge = Integer.parseInt(age.substring(2, 4));
		} else {
			minAge = Integer.parseInt(age.substring(0, 3));
			maxAge = Integer.parseInt(age.substring(3, 6));
		}
		
		wheelLeft.setCurrentItem(minAge - min);
		wheelRightAdapter.setMinAndMax(minAge, max, unit);
		wheelRight.setAdapter(wheelRightAdapter);
		wheelRight.setCurrentItem(maxAge - minAge);
	}
	
	public void setDialogTitle(String title) {
		tvTitle.setText(title);
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	
	public void setMinAndMax(int min, int max, String unit) {
		this.min = min;
		this.max = max;
		this.unit = unit;
		
		wheelLeftAdapter.setMinAndMax(min, max, unit);
		wheelRightAdapter.setMinAndMax(min, max, unit);
		
		wheelLeft.setAdapter(wheelLeftAdapter);
		wheelRight.setAdapter(wheelRightAdapter);
		
		wheelLeft.setCurrentItem(0);
		wheelRight.setCurrentItem(0);
	}
	
	public void setDialogDoubleWheelChangeListener(
			DialogDoubleWheelChangeListener dialogDoubleWheelChangeListener) {
		this.dialogDoubleWheelChangeListener = dialogDoubleWheelChangeListener;
	}

	public interface DialogDoubleWheelChangeListener {
		void dialogDoubleOk(String content);
	}
}
