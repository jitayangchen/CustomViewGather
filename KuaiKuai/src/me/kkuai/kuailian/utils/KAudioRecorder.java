package me.kkuai.kuailian.utils;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.util.Log;

public class KAudioRecorder {
	
	public static final int AUDIO_CHANNEL_SINGLE = 1;
	public static final int AUDIO_CHANNEL_MORE = 2;
	private MediaRecorder recorder;
	private static KAudioRecorder self ;
	
	private KAudioRecorder(){};
	
	public static KAudioRecorder getInstance(){
		if(self == null){
			self = new KAudioRecorder() ;
		}
		return self ;
	} 
	
	/**
	 * 该方法默认单声道、采样率3000，类似微信等语音录制可直接调用
	 * @param path 要保存的文件的绝对地址，如果文件已经存在则会被替换
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void startRecord(String path) throws IllegalStateException, IOException{
		record(path, AUDIO_CHANNEL_SINGLE, 3000);
	}
	
	/**
	 * 
	 * @param path  要保存的文件的绝对地址 如果文件已经存在那么会被替换
	 * @param channel  声道配置，1为单声道，2为多声道
	 * @param rate  采样率 一般4000
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public  void startRecord(String path,int channel,int rate) throws IllegalStateException, IOException{
		record(path, channel, rate);
	}
	
	
	private  void record(String path,int channel,int rate) throws IllegalStateException, IOException{
		
		File file = new File(path);
		
		if(file.exists()){
			file.delete();
		}                                                       
		
		file.createNewFile();
		
		if(recorder != null){
			stopRecord();
		}
		
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//		recorder.setAudioChannels(channel);
		recorder.setAudioEncodingBitRate(rate); 
		recorder.setOutputFile(path);
		recorder.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public void onError(MediaRecorder mr, int what, int extra) {
				Log.i("AAA", "record error");
			}
		});
		recorder.prepare();
		recorder.start(); 
	}
	
	
	
	
	public void stopRecord(){
		if(recorder != null ){
			recorder.stop();
			recorder.release();
			recorder = null ;
		}
	}

	public MediaRecorder getRecorder() {
		return recorder;
	}

}
