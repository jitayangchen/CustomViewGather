package com.cn.lyric.activity;

import java.util.List;

import com.cn.lyric.adapter.MusicAdapter;
import com.cn.lyric.bean.Music;
import com.cn.lyric.service.MusicService;
import com.cn.lyric.util.Utils;
import com.cn.sava.Indexviewpager;
import com.cn.sava.MusicNum;
import com.cn.ui.custom.MenuTouch;
import com.cn.ui.custom.ScreenInfo;
import com.example.musiclist.R;

import android.annotation.SuppressLint;
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
		* 功能描述：
		* 获取所有的歌曲
		* @author WAH-WAY(xuwahwhy@163.com)
		*
		* <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class TabIMainActivity extends Activity// implements OnTouchListener
{
	private ListView listView;
	private CloseReceiver close;
	int lastX = 0;
	int lastY = 0;
	ScreenInfo s;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.iactivity_main);
		listView = (ListView) this.findViewById(R.id.musiclistevery);
		listView.setOnTouchListener(new MenuTouch());
		
		close = new CloseReceiver();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		
		s = new ScreenInfo(TabIMainActivity.this);
		
		base = (int) IndexGroupActivity.bottomlistlin.getTranslationY();
		
		List<Music> listMusic = Utils.getMusicData(getApplicationContext());
		
		MusicAdapter adapter = new MusicAdapter(TabIMainActivity.this, listMusic);
		listView.setAdapter(adapter);
		Utils.getMusicData(this);
		listView.setSelection(Indexviewpager.getmainlistposition());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent1 = new Intent(TabIMainActivity.this,
						MusicService.class);
				MusicNum.putplay(8);
				MusicNum.putisok(true);
				intent1.putExtra("_id", arg2);
				startService(intent1);

				Intent intent = new Intent(TabIMainActivity.this,
						MusicPlayingActivity.class);
				startActivity(intent);
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

	int startY = 386;
	int x = 0;
	int base = 0;

	// @SuppressLint("NewApi")
	// @Override
	// public boolean onTouch(View v, MotionEvent arg1) {
	// int action = arg1.getAction();
	// if (action == MotionEvent.ACTION_DOWN) {
	// x = (int) arg1.getRawY();
	// startY = (int) Index.bottomlistlin.getTranslationY();
	// }
	// if (action == MotionEvent.ACTION_MOVE) {
	// int a = startY + ((int) arg1.getRawY() - x);
	// if (a <= 386 && a >= 0) {
	// Index.bottomlistlin.setTranslationY(a);
	// }
	// }
	// if (action == MotionEvent.ACTION_UP) {
	// if (Index.bottomlistlin.getTranslationY() > base / 2) {
	// Index.bottomlistlin.setTranslationY(386);
	// } else {
	// Index.bottomlistlin.setTranslationY(0);
	// }
	// }
	// return false;
	// }
}
