package com.cn.receiver;

import com.cn.lyric.service.MusicService;
import com.cn.sava.MusicNum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotiReceiveNext extends BroadcastReceiver {
	@Override
public void onReceive(Context context, Intent intent) {
		Intent intent2=new Intent(context,MusicService.class);
		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent2.putExtra("play",5);
		MusicNum.putplay(5);
		MusicNum.putisok(true);
		context.startService(intent2);
	}
}