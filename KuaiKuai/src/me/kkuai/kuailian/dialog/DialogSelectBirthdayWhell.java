package me.kkuai.kuailian.dialog;

import java.util.Arrays;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.adapter.WheelSelectBirthdayAdapter;
import me.kkuai.kuailian.dialog.DialogEditBox.DialogOkListener;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
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

public class DialogSelectBirthdayWhell extends AlertDialog implements OnClickListener {
	
	private Log log = LogFactory.getLog(DialogSelectBirthdayWhell.class);
	private Context context;
	private WheelView wheelYear;
	private WheelView wheelMonth;
	private WheelView wheelDay;
	private SoundPool pool;
	private int pickerSound;
	private final static int TEXT_SIZE_PROPORTION = 20;
	private int screenWidth;
	private WheelSelectBirthdayAdapter yearAdapter;
	private WheelSelectBirthdayAdapter monthAdapter;
	private WheelSelectBirthdayAdapter dayAdapter;
	// 添加大小月月份并将其转换为list,方便之后的判断
	private String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	private String[] months_little = { "4", "6", "9", "11" };
	private List<String> list_big;
	private List<String> list_little;
	private Button btnCancel;
	private Button btnFinish;
	private DialogBirthdayOkListener dialogBirthdayOkListener;

	public DialogSelectBirthdayWhell(Context context) {
		super(context);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_select_birthday_whell);
		
		initViews();
		setListener();
		
		list_big = Arrays.asList(months_big);
		list_little = Arrays.asList(months_little);
		
		pool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		pickerSound = pool.load(context, R.raw.number_picker_value_change, 1);
	}

	private void initViews() {
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnFinish = (Button) findViewById(R.id.btn_finish);
		
		wheelYear = (WheelView) findViewById(R.id.wheel_year);
		wheelMonth = (WheelView) findViewById(R.id.wheel_month);
		wheelDay = (WheelView) findViewById(R.id.wheel_day);
		
		yearAdapter = new WheelSelectBirthdayAdapter(context);
		yearAdapter.setValueRange(1970, 2010);
		
		monthAdapter = new WheelSelectBirthdayAdapter(context);
		monthAdapter.setValueRange(1, 12);
		
		dayAdapter = new WheelSelectBirthdayAdapter(context);
		dayAdapter.setValueRange(1, 31);
		
		wheelYear.setAdapter(yearAdapter);
		wheelYear.setCyclic(true);
		wheelYear.TEXT_SIZE = screenWidth / TEXT_SIZE_PROPORTION;
		wheelYear.setLabel(context.getString(R.string.year));
		
		wheelMonth.setAdapter(monthAdapter);
		wheelMonth.setCyclic(true);
		wheelMonth.TEXT_SIZE = screenWidth / TEXT_SIZE_PROPORTION;
		wheelMonth.setLabel(context.getString(R.string.month));
		
		wheelDay.setAdapter(dayAdapter);
		wheelDay.setCyclic(true);
		wheelDay.TEXT_SIZE = screenWidth / TEXT_SIZE_PROPORTION;
		wheelDay.setLabel(context.getString(R.string.day));
		
	}

	private void setListener() {
		
		btnCancel.setOnClickListener(this);
		btnFinish.setOnClickListener(this);
		
		wheelYear.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onFinishScroll(boolean finishScroll) {
				changeDay();
				log.info("wheelYear.getCurrentItem() === " + wheelYear.getCurrentItem());
			}
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				log.info("oldValue === " + oldValue + " --- newValue ===" + newValue);
				pool.play(pickerSound, 1, 1, 0, 0, 1);
			}
		});
		wheelMonth.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onFinishScroll(boolean finishScroll) {
				changeDay();
				log.info("wheelMonth.getCurrentItem() === " + wheelMonth.getCurrentItem());
			}
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				log.info("oldValue === " + oldValue + " --- newValue ===" + newValue);
				pool.play(pickerSound, 1, 1, 0, 0, 1);
			}
		});
		wheelDay.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onFinishScroll(boolean finishScroll) {
				
			}
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				log.info("oldValue === " + oldValue + " --- newValue ===" + newValue);
				pool.play(pickerSound, 1, 1, 0, 0, 1);
			}
		});
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	
	public void setCurrent(String content) {
		if (TextUtils.isEmpty(content)) {
			return ;
		}
		String[] date = content.split("-");
		int year = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int day = Integer.parseInt(date[2]);
		
		wheelYear.setCurrentItem(year - 1970);
		wheelMonth.setCurrentItem(month - 1);
		wheelDay.setCurrentItem(day - 1);
	}
	
	@Override
	public void show() {
		super.show();
		wheelYear.setCurrentItem(20);
		changeDay();
	}
	
	private void changeDay() {
		int year = wheelYear.getCurrentItem() + 1970;
		String month = String.valueOf(wheelMonth.getCurrentItem() + 1);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(month)) {
			dayAdapter.setValueRange(1, 31);
			wheelDay.setAdapter(dayAdapter);
		} else if (list_little.contains(month)) {
			dayAdapter.setValueRange(1, 30);
			wheelDay.setAdapter(dayAdapter);
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
				dayAdapter.setValueRange(1, 29);
				wheelDay.setAdapter(dayAdapter);
			} else {
				dayAdapter.setValueRange(1, 28);
				wheelDay.setAdapter(dayAdapter);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.btn_finish:
			if (null != dialogBirthdayOkListener) {
				String birthdayDate = (wheelYear.getCurrentItem() + 1970) + "-" + (wheelMonth.getCurrentItem() + 1) + "-" + 
						(wheelDay.getCurrentItem() + 1);
				dialogBirthdayOkListener.dialogBirthdayOk(birthdayDate);
			}
			dismiss();
			break;

		default:
			break;
		}
	}

	public void setDialogBirthdayOkListener(DialogBirthdayOkListener dialogBirthdayOkListener) {
		this.dialogBirthdayOkListener = dialogBirthdayOkListener;
	}

	public interface DialogBirthdayOkListener {
		void dialogBirthdayOk(String editContext);
	}

}
