package me.kkuai.kuailian.dialog;

import java.io.IOException;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.utils.FileUtils;
import me.kkuai.kuailian.utils.KAudioRecorder;
import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogJoinRecord extends AlertDialog implements OnClickListener {
	
	private Log log = LogFactory.getLog(DialogJoinRecord.class);
	private Context context;
	private TextView tvTime;
	private TextView btnCancel;
	private TextView btnOk;
	private long time = 0;
	private String filePath;
	private RecordFinishListener finishListener;
	private ImageView ivVolume;
	private RecordingVolume recordingVolume;
	private long voiceLength = 180;
	
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_join_record_audio);
		
		initViews();
		setListener();
		
		recordingVolume = new RecordingVolume();
	}

	public DialogJoinRecord(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
	}

	public DialogJoinRecord(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public DialogJoinRecord(Context context) {
		super(context);
		this.context = context;
	}
	
	private void initViews() {
		tvTime = (TextView) findViewById(R.id.tv_time);
		btnCancel = (TextView) findViewById(R.id.btn_cancel);
		btnOk = (TextView) findViewById(R.id.btn_ok);
		ivVolume = (ImageView) findViewById(R.id.iv_volume);
	}
	
	private void setListener() {
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			if (null != finishListener) {
				log.info("filePath >>>>>>>>>>>>>>>>>> " + filePath);
				finishListener.onRecordFinish(filePath, time);
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
	
	public void show() {
		super.show();
		log.info("DialogJoinRecord show()");
		tvTime.setText("");
		time = 0;
		handler.postDelayed(runnable, 1000);
		try {
			filePath = FileUtils.getTempFilePath() + "/" + System.currentTimeMillis() + ".amr";
			KAudioRecorder.getInstance().startRecord(filePath, KAudioRecorder.AUDIO_CHANNEL_MORE, 8000);
			handler.postDelayed(recordingVolume, 100);
		} catch (IllegalStateException e) {
			log.error("KAudioRecorder", e);
		} catch (IOException e) {
			log.error("KAudioRecorder", e);
		}
	}
	
	public void dismiss() {
		log.info("DialogJoinRecord dismiss()");
		stopRecord();
		super.dismiss();
	}
	
	private void stopRecord() {
		handler.removeCallbacks(runnable);
		handler.removeCallbacks(recordingVolume);
		KAudioRecorder.getInstance().stopRecord();
	}
	
	Runnable runnable = new Runnable() {  
        @Override  
        public void run() {
        	time++;
        	tvTime.setText(time + "s");
        	if (time >= voiceLength) {
        		stopRecord();
			} else {
				handler.postDelayed(this, 1000);  
			}
        }  
    };
    
    public interface RecordFinishListener {
    	void onRecordFinish(String filePath, long audioLength);
    }

	public void setFinishListener(RecordFinishListener finishListener) {
		this.finishListener = finishListener;
	}
	
	class RecordingVolume implements Runnable {

		public boolean isStop;

		@Override
		public void run() {
			MediaRecorder record = KAudioRecorder.getInstance().getRecorder();
			if (record != null) {
				int amplitude = record.getMaxAmplitude();
				int level = amplitude / 1000;
				switch (level) {
				case 0:
					ivVolume.setImageResource(R.drawable.recording_volume_0);
					break;
				case 1:
					ivVolume.setImageResource(R.drawable.recording_volume_2);
					break;
				case 2:
					ivVolume.setImageResource(R.drawable.recording_volume_3);
					break;
				case 3:
					ivVolume.setImageResource(R.drawable.recording_volume_4);
					break;
				case 4:
					ivVolume.setImageResource(R.drawable.recording_volume_5);
					break;
				default:
					ivVolume.setImageResource(R.drawable.recording_volume_5);
					break;
				}

			}

			if (!isStop)
				handler.postDelayed(this, 100);
		}
	}
	
}
