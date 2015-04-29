package me.kkuai.kuailian.utils;

import java.io.IOException;

import me.kkuai.kuailian.KApplication;
import me.kkuai.kuailian.R;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.ImageView;

public class KAudioPlayer {
	
	private static KAudioPlayer audioPlayer;
	
	private MediaPlayer mediaPlayer ;
	
	private OnFinishCallback callback ;
	
	private KAudioPlayer(){};
	
	public static String path = "";
	public static boolean isPlaying = false ;
	
	public static KAudioPlayer getInstance(){
		if(audioPlayer == null){
			audioPlayer = new KAudioPlayer();
		}
		return audioPlayer;
	}
	
//	public boolean isPlaying(){
//		if(audioPlayer != null){
//			return audioPlayer.isPlaying();
//		}
//		return false ;
//	}
	
	
	/**
	 * 播放指定的音频文件
	 * @param path
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void playFile(Context context, String path,final ImageView imageView,final OnFinishCallback callback,final boolean isComMsg) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		
		this.path = path ;
		if(mediaPlayer != null){
//			imageView.setBackgroundDrawable(null);
//			imageView.setImageResource(R.drawable.voice_logo);
			stopPlayAndRelease();
//			return ;
		}
		
		this.callback = callback ;
		
//		if(isComMsg){
//			imageView.setBackgroundResource(R.anim.chatfrom_voice_anim);
//		}else{
//			imageView.setBackgroundResource(R.anim.chatto_voice_anim);
//		}
		imageView.setImageBitmap(null);
		final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground(); 
		animationDrawable.start();
		
		AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		manager.setMode(AudioManager.MODE_IN_CALL);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				if(mediaPlayer != null ){
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null ;
					animationDrawable.stop();
//					if(isComMsg){
//						imageView.setImageResource(R.drawable.chatfrom_voice_anim_01);
//					}else{
//						imageView.setImageResource(R.drawable.chatto_voice_anim_01);
//					}
					
					imageView.setBackgroundDrawable(null);
					callback.onPlayFinish();
				}
				
			}
		});
		
//		File file = new File(path);
//		FileInputStream stream = new FileInputStream(file);
		mediaPlayer.setDataSource(path);
		mediaPlayer.prepare();
		isPlaying = true ;
		mediaPlayer.start();
		
	}
	
	public interface OnFinishCallback{
		public abstract void onPlayFinish();
	}
	
	
	/**
	 * 停止Mediaplayer的播放行为
	 */
	public void stopPlayAndRelease(){
		if(mediaPlayer != null && mediaPlayer.isPlaying()){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null ;
			isPlaying = false ;
		}
	}
	
	public void stopPlay(ImageView imageView,boolean isComMsg){
		
		if(imageView != null){
			imageView.setBackgroundDrawable(null);
//			if(isComMsg){
//				imageView.setImageResource(R.drawable.chatfrom_voice_anim_01);
//			}else{
//				imageView.setImageResource(R.drawable.chatto_voice_anim_01);
//			}
			
		}
		
		if(mediaPlayer != null && mediaPlayer.isPlaying()){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null ;
			
		}
		isPlaying = false ;
	}
	
	
//	public void stopPlayAndRelease(AnimationDrawable animationDrawable, imageView){
//		if(mediaPlayer != null && mediaPlayer.isPlaying()){
//			mediaPlayer.stop();
//			mediaPlayer.release();
//			mediaPlayer = null ;
//			animationDrawable.stop();
//			imageView.setImageResource(R.drawable.voice_logo);
//			imageView.setBackgroundDrawable(null);
//			
//		}
//	}

}
