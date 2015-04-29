package me.kkuai.kuailian.http;

import me.kkuai.kuailian.utils.BitmapUtils;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class KImageLoader {
	
	private final static KImageLoader instance = new KImageLoader();
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private KImageLoadingListener imageLoadingListener;

	private KImageLoader(){
		options = new DisplayImageOptions.Builder()
//		.showStubImage(R.drawable.ic_launcher)
//		.showImageForEmptyUri(R.drawable.ic_launcher)
//		.showImageOnFail(R.drawable.ic_launcher)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		imageLoadingListener = new KImageLoadingListener();
	}
	
	public static KImageLoader getInstance() {
		return instance;
	}
	
	public ImageLoader getImageLoader() {
		return imageLoader;
	}
	
	public void displayImage(String uri, ImageView imageView) {
		if (!TextUtils.isEmpty(uri)) {
			imageLoader.displayImage(uri, imageView, options);
		}
	}
	
	public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
		if (!TextUtils.isEmpty(uri)) {
			imageLoader.displayImage(uri, imageView, options);
		}
	}
	
	public void displayRoundImage(String uri, ImageView imageView) {
		if (!TextUtils.isEmpty(uri)) {
			imageLoader.displayImage(uri, imageView, options, imageLoadingListener);
		}
	}
	
	public void displayRoundImage(String uri, ImageView imageView, DisplayImageOptions options) {
		if (!TextUtils.isEmpty(uri)) {
			imageLoader.displayImage(uri, imageView, options, imageLoadingListener);
		}
	}
	
	class KImageLoadingListener implements ImageLoadingListener {

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			ImageView imageImage = (ImageView) view;
			Bitmap roundBitmap = BitmapUtils.toRoundBitmap(loadedImage);
			if (null != roundBitmap) {
				imageImage.setImageBitmap(roundBitmap);
			}
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			
		}
		
	}
}
