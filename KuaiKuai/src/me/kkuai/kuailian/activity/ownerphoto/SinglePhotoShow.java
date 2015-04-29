package me.kkuai.kuailian.activity.ownerphoto;

import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.http.KImageLoader;
import me.kkuai.kuailian.widget.photoview.PhotoView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SinglePhotoShow extends BaseActivity {

	private Context context;
	private PhotoView photoView;
	private KImageLoader imageLoader = KImageLoader.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		photoView = new PhotoView(context);
		setContentView(photoView);
		
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		
		imageLoader.displayImage(path, photoView);
		
		photoView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
