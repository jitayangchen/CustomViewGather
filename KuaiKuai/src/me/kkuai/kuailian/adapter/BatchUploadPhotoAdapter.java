package me.kkuai.kuailian.adapter;

import java.util.LinkedList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class BatchUploadPhotoAdapter extends BaseAdapter {
	
	private Log log = LogFactory.getLog(BatchUploadPhotoAdapter.class);
	private Context context;
	private LinkedList<String> photos = new LinkedList<String>();
	private final static String uploadImage = "upload_image";

	public BatchUploadPhotoAdapter(Context context) {
		this.context = context;
		photos.addFirst(uploadImage);
	}
	
	public LinkedList<String> getPhotos() {
		return photos;
	}

	public void setPhotos(LinkedList<String> photos) {
		this.photos = photos;
	}

	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public Object getItem(int position) {
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_grid, parent, false);
			ViewHolder vh = new ViewHolder();
			vh.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
			convertView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) convertView.getTag();
		if (uploadImage.equals(photos.get(position))) {
			vh.ivPhoto.setImageResource(R.drawable.upload_image);
		} else {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			vh.ivPhoto.setImageBitmap(BitmapFactory.decodeFile(photos.get(position), options));
		}
		return convertView;
	}

	class ViewHolder {
		ImageView ivPhoto;
	}
	
}
