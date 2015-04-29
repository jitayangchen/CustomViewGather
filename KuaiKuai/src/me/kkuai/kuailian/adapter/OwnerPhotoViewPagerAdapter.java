package me.kkuai.kuailian.adapter;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.bean.OwnerPhotoBean;
import me.kkuai.kuailian.http.KImageLoader;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.widget.photoview.PhotoView;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class OwnerPhotoViewPagerAdapter extends PagerAdapter implements OnClickListener {

	private Log log = LogFactory.getLog(OwnerPhotoViewPagerAdapter.class);
	private Context context;
	private List<OwnerPhotoBean> photos = new ArrayList<OwnerPhotoBean>();
	private KImageLoader imageLoader = KImageLoader.getInstance();
	private OnItemClickListener onItemClickListener;
	private boolean isChanged = false;

	public OwnerPhotoViewPagerAdapter(Context context) {
		this.context = context;
	}

	public List<OwnerPhotoBean> getPhotos() {
		return photos;
	}

	public void setPhotos(List<OwnerPhotoBean> photos) {
		this.photos = photos;
	}

	@Override
	public int getCount() {
		return photos.size();
	}
	
	@Override
	public int getItemPosition(Object object) {
		log.info("------         getItemPosition       ------");
		return POSITION_NONE;
//		return super.getItemPosition(object);
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0.equals(arg1);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View itemView = getItemView(container);
		ViewHolder vh = (ViewHolder) itemView.getTag();
		vh.ivOwnerPhoto.setImageResource(R.drawable.bg_default_female);
		imageLoader.displayImage(photos.get(position).getPhotoUrl(), vh.ivOwnerPhoto);
		vh.ivOwnerPhoto.setOnClickListener(this);
		vh.ivOwnerPhoto.setTag(position);
		container.addView(itemView);
		return itemView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	
	private View getItemView(ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_owner_photo_view_pager, parent, false);
		ViewHolder vh = new ViewHolder();
		vh.ivOwnerPhoto = (PhotoView) view.findViewById(R.id.iv_owner_photo);
		view.setTag(vh);
		return view;
	}
	
	class ViewHolder {
		PhotoView ivOwnerPhoto;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_owner_photo:
			if (null != onItemClickListener) {
				int position = (Integer) v.getTag();
				onItemClickListener.onItemClick(position);
			}
			break;

		default:
			break;
		}
	}
	
	public interface OnItemClickListener {
		void onItemClick(int position);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

}
