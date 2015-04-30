package com.cn.lyric.activity;

import java.util.List;

import com.cn.lyric.adapter.MusicAdapter;
import com.cn.lyric.bean.Music;
import com.cn.lyric.service.MusicService;
import com.cn.lyric.util.Utils;
import com.cn.sava.Indexviewpager;
import com.cn.sava.MusicNum;
import com.example.musiclist.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
/**
 * 
		* ����������
		* �����б�
		* @author WAH-WAY(xuwahwhy@163.com)
		*
		* <p>�޸���ʷ��(�޸��ˣ��޸�ʱ�䣬�޸�ԭ��/����)</p>
 */
public class AllListActivity extends Activity {
	private ListView listView;
	private CloseReceiver close;
	float dowm = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		listView = (ListView) this.findViewById(R.id.musiclistevery);
		
		close = new CloseReceiver();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		
		List<Music> listMusic = Utils.getMusicData(getApplicationContext());
		MusicAdapter adapter = new MusicAdapter(AllListActivity.this, listMusic);
		// layoutParams.height=800;

		// Index.mainindexback.setLayoutParams(layoutParams);
		listView.setAdapter(adapter);
//		MusicList.getMusicData(this);
		listView.setSelection(Indexviewpager.getmainlistposition());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent1 = new Intent(AllListActivity.this,
						MusicService.class);
				MusicNum.putplay(8);
				MusicNum.putisok(true);
				intent1.putExtra("_id", arg2);
				startService(intent1);
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				Indexviewpager.putmainlistposition(arg1);
			}
		});
	}

	@Override
	protected void onDestroy() {
		listView.setAdapter(null);
		this.unregisterReceiver(close);
		super.onDestroy();
	}

	public class CloseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();

		}
	}
}
