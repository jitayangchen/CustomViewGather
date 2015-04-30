package com.cn.lyric.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.cn.ui.custom.MenuTouch;
import com.example.musiclist.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class TabOnlineActivity extends Activity {
	private Close close;
	String url[] = {
			"http://mp3.easou.com/index.html?esid=VQS55LWDK9dqhQDjE3zeG_OQzMoMkozQ3&wver=t&fr=#rec%3A",
			"http://mp3.easou.com/index.html?esid=VQS55LWDK9dqhQDjE3zeG_OQzMoMkozQ3&wver=t&fr=#subject%3A%5B%22index%22%5D",
			"http://fm.baidu.com/?fr=ucyy" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online);

		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		ListView list = (ListView) findViewById(R.id.musiclistevery);

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		String dianpuname[] = { "��Ʒ�Ƽ�", "���Ű�", "������" };
		for (int i = 0; i <= 2; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("online_name", dianpuname[i]);
			listItem.add(map);
		}
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// ����Դ
				R.layout.online_items,// ListItem��XMLʵ��
				new String[] { "online_name" }, new int[] { R.id.online_name });
		list.setAdapter(listItemAdapter);
		list.setOnTouchListener(new MenuTouch());
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(TabOnlineActivity.this, BaiduOnline.class);
				intent.putExtra("url", url[arg2]);
				startActivity(intent);
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
