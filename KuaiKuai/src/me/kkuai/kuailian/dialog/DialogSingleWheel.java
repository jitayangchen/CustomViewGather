package me.kkuai.kuailian.dialog;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.adapter.WheelSingleAdapter;
import me.kkuai.kuailian.bean.OptionCell;
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

public class DialogSingleWheel extends AlertDialog implements OnClickListener {
	
	private Context context;
	private WheelView wheelSingle;
	private List<OptionCell> datas = new ArrayList<OptionCell>();
	private SoundPool pool;
	private int pickerSound;
	private WheelSingleAdapter singleAdapter;
	private final static int TEXT_SIZE_PROPORTION = 20;
	private int screenWidth;
	private TextView tvDialogTitle;
	private Button btnFinish;
	private Button btnCancel;
	private DialogSingleWheelOkListener dialogSingleWheelOkListener;

	public DialogSingleWheel(Context context) {
		super(context);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_single_wheel);
		
		initViews();
		setListener();
		
		pool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		pickerSound = pool.load(context, R.raw.number_picker_value_change, 1);
	}

	private void initViews() {
		wheelSingle = (WheelView) findViewById(R.id.wheel_single);
		singleAdapter = new WheelSingleAdapter();
//		singleAdapter.setDatas(datas);
//		wheelSingle.setAdapter(singleAdapter);
//		wheelSingle.setCyclic(true);
		wheelSingle.TEXT_SIZE = screenWidth / TEXT_SIZE_PROPORTION;
		
		tvDialogTitle = (TextView) findViewById(R.id.tv_dialog_title);
		btnFinish = (Button) findViewById(R.id.btn_finish);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
	}

	private void setListener() {
		wheelSingle.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onFinishScroll(boolean finishScroll) {
				
			}
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				pool.play(pickerSound, 1, 1, 0, 0, 1);
			}
		});
		
		btnFinish.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_finish:
			dialogSingleWheelOkListener.dialogSingleWheelOk(datas.get(wheelSingle.getCurrentItem()));
			dismiss();
			break;
		case R.id.btn_cancel:
			dismiss();
			break;

		default:
			break;
		}
	}

	public List<OptionCell> getDatas() {
		return datas;
	}

	public void setDatas(List<OptionCell> datas) {
		this.datas = datas;
//		if (null == singleAdapter) {
//			singleAdapter = new WheelSingleAdapter();
//		}
		singleAdapter.setDatas(datas);
		wheelSingle.setAdapter(singleAdapter);
	}
	
	public void setCurrentItem(String index) {
		if (TextUtils.isEmpty(index)) {
			wheelSingle.setCurrentItem(0);
		} else {
			int i = Integer.parseInt(index);
			wheelSingle.setCurrentItem(i);
		}
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	
	public void setDialogTitle(String title) {
		tvDialogTitle.setText(title);
	}
	
	public void setDialogSingleWheelOkListener(DialogSingleWheelOkListener dialogSingleWheelOkListener) {
		this.dialogSingleWheelOkListener = dialogSingleWheelOkListener;
	}

	public interface DialogSingleWheelOkListener {
		void dialogSingleWheelOk(OptionCell cell);
	}

}
