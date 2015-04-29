package me.kkuai.kuailian.activity.owner;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotoAlbum extends BaseActivity {
	
	private Context context;
	private ImageView ivImage;
	private String avatar = "http://img.qiuyuehui.com/qyh/a6/0/b35/a03/948/901/0f2/d21/cb3/6f2/239/86f4czg/6fd237bc-f209-4982-bd6b-36a56459b6eb_a.jpg";

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ivImage.setImageBitmap((Bitmap)msg.obj);
		}
	};
	private GridView gvMyGridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_photo_album);
		
		initViews();
		setListener();
	}
	
	@Override
	public void initViews() {
//		ivImage = (ImageView) findViewById(R.id.iv_image);
//		FinalBitmap finalBitmap = FinalBitmap.create(context);
//		finalBitmap.configBitmapMaxHeight(1000);
//		finalBitmap.configBitmapMaxWidth(1000);
//		finalBitmap.display(ivImage, avatar);
//		ivImage.setImageResource(R.drawable.ddddd);
//		downLoadImage();
		
		
		
		gvMyGridView = (GridView) findViewById(R.id.gv_my_grid_view);
		MyAdapter adapter = new MyAdapter();
		gvMyGridView.setAdapter(adapter);
	}
	
	@Override
	public void setListener() {
		
	}
	
	public void downLoadImage() {
		new Thread(new Runnable() {

			public void run() {
					
					InputStream bitmapIs = null;
					Bitmap bitmap = null;
					try {
						URL url = new URL(avatar);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						bitmapIs = connection.getInputStream();
						bitmap = BitmapFactory.decodeStream(bitmapIs, null, null);
						Message msg = handler.obtainMessage(0, bitmap);
						handler.sendMessage(msg);
					} catch (Exception e) {
					} finally {
						try {
							bitmapIs.close();
						} catch (IOException e) {
						}
					}
				}
		}).start();
	}
	
	class MyAdapter extends BaseAdapter {
		private FinalBitmap finalBitmap;
		public MyAdapter() {
			finalBitmap = FinalBitmap.create(context);
			finalBitmap.configBitmapMaxHeight(1000);
			finalBitmap.configBitmapMaxWidth(1000);
		}

		@Override
		public int getCount() {
			return 100;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.item_grid, parent, false);
				convertView = view;
				ViewHolder vh = new ViewHolder();
				vh.image = (ImageView) convertView.findViewById(R.id.iv_photo);
				convertView.setTag(vh);
			}
			ViewHolder vh = (ViewHolder) convertView.getTag();
			finalBitmap.display(vh.image, avatar);
			
			return convertView;
		}
		
	}
	
	class ViewHolder {
		ImageView image;
	}
}
