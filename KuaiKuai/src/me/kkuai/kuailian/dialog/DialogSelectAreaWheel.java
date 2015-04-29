package me.kkuai.kuailian.dialog;

import java.util.List;

import me.kkuai.kuailian.KApplication;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.adapter.WheelSelectAreaAdapter;
import me.kkuai.kuailian.bean.OptionCell;
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

public class DialogSelectAreaWheel extends AlertDialog implements OnClickListener {
	
	private Context context;
	private WheelView wheelLeft;
	private WheelView wheelRight;
	private Button btnFinish;
	private WheelSelectAreaAdapter wheelLeftAdapter;
	private WheelSelectAreaAdapter wheelRightAdapter;
	private int screenWidth;
	private SoundPool pool;
	private int pickerSound;
	private final static int TEXT_SIZE_PROPORTION = 20;
	private OnSelectedCityListener onSelectedCityListener;
	private TextView tvTitle;
	private Button btnCancel;
	private List<OptionCell> areaData;

	public DialogSelectAreaWheel(Context context) {
		super(context, R.style.editDialogStyle);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_double_wheel);
		
		areaData = KApplication.getAreaData();
		
		initViews();
		setListener();
		
		pool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		pickerSound = pool.load(context, R.raw.number_picker_value_change, 1);
	}

	private void initViews() {
		tvTitle = (TextView) findViewById(R.id.tv_title);
		wheelLeft = (WheelView) findViewById(R.id.wheel_left);
		wheelRight = (WheelView) findViewById(R.id.wheel_right);
		wheelLeftAdapter = new WheelSelectAreaAdapter();
		wheelRightAdapter = new WheelSelectAreaAdapter();
		wheelLeftAdapter.setOptionCells(areaData);
		wheelLeft.setAdapter(wheelLeftAdapter);
		wheelLeft.setCurrentItem(0);
//		wheelLeft.setCyclic(true);
		wheelLeft.TEXT_SIZE = screenWidth / TEXT_SIZE_PROPORTION;
		
		wheelRightAdapter.setOptionCells(areaData.get(0).getChildData());
		wheelRight.setAdapter(wheelRightAdapter);
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
				wheelRightAdapter.setOptionCells(areaData.get(currentItem).getChildData());
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
			if (null != onSelectedCityListener) {
				onSelectedCityListener.selectedCityFinish(wheelRightAdapter.getOptionCells().get(wheelRight.getCurrentItem()).getId());
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
	
	public void setCurrent(String cityCode) {
		if (TextUtils.isEmpty(cityCode)) {
			return ;
		}
		String parentId = cityCode.substring(0, 2);
		List<OptionCell> leftOptionCells = wheelLeftAdapter.getOptionCells();
		List<OptionCell> rightOptionCells = wheelRightAdapter.getOptionCells();
		for (int i = 0; i < leftOptionCells.size(); i++) {
			if (leftOptionCells.get(i).getId().equals(parentId)) {
				wheelLeft.setCurrentItem(i);
				wheelRightAdapter.setOptionCells(areaData.get(i).getChildData());
				wheelRight.setAdapter(wheelRightAdapter);
				break;
			}
		}
		for (int i = 0; i < rightOptionCells.size(); i++) {
			if (rightOptionCells.get(i).getId().equals(cityCode)) {
				wheelRight.setCurrentItem(i);
				break;
			}
		}
	}
	
	public void setDialogTitle(String title) {
		tvTitle.setText(title);
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	
	public interface OnSelectedCityListener {
		void selectedCityFinish(String cityCode);
	}

	public void setOnSelectedCityListener(OnSelectedCityListener onSelectedCityListener) {
		this.onSelectedCityListener = onSelectedCityListener;
	}
	
}
