package com.cn.lyric.activity;

import java.util.ArrayList;

import com.example.musiclist.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 
		* ����������
		* ����ҳ��
		* @author WAH-WAY(xuwahwhy@163.com)
		*
		* <p>�޸���ʷ��(�޸��ˣ��޸�ʱ�䣬�޸�ԭ��/����)</p>
 */
public class AboutActivity extends Activity {
	private Button xianshishuru, tijiao;
	private EditText liuyan;
	private Close close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);

		xianshishuru = (Button) this.findViewById(R.id.xianshishuru);
		tijiao = (Button) this.findViewById(R.id.tijiao);
		liuyan = (EditText) this.findViewById(R.id.liuyan);
		xianshishuru.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				liuyan.setVisibility(View.VISIBLE);
				tijiao.setVisibility(View.VISIBLE);
				xianshishuru.setVisibility(View.GONE);
			}
		});
		tijiao.setOnClickListener(new OnClickListener() {
			@SuppressLint("UnlocalizedSms")
			@Override
			public void onClick(View v) {
				String content = liuyan.getText().toString();
				if (content.equals("")) {
					Toast.makeText(getApplicationContext(), "��ϢΪ�գ�����ʧ��",
							Toast.LENGTH_SHORT).show();
				} else {
					SmsManager manager = SmsManager.getDefault();
					ArrayList<String> texts = manager.divideMessage(content);
					for (String text : texts) {
						manager.sendTextMessage("18720094195", null,
								"�������������ֵĽ��顿��" + text, null, null);
					}
					Toast.makeText(getApplicationContext(), "�ɹ�����л���Ľ���",
							Toast.LENGTH_SHORT).show();
					liuyan.setText("");
				}
			}
		});
	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}
}
