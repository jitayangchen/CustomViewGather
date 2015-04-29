package me.kkuai.kuailian.adapter;

import java.util.ArrayList;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.adapter.OwnerProfileListViewAdapter.ViewHolder;
import me.kkuai.kuailian.bean.OwnerPhotoBean;
import me.kkuai.kuailian.http.KImageLoader;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AutoViewPagerAdapter extends PagerAdapter {
	
	private Log log = LogFactory.getLog(AutoViewPagerAdapter.class);
	private Context context;
	private ArrayList<OwnerPhotoBean> photos = new ArrayList<OwnerPhotoBean>();
	private KImageLoader imageLoader = KImageLoader.getInstance();
	
	public AutoViewPagerAdapter(Context context) {
		this.context = context;
	}

	public void setPhotos(ArrayList<OwnerPhotoBean> photos) {
		this.photos = photos;
	}

	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0.equals(arg1);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View itemView = getItemView(container);
		ViewHolder vh = (ViewHolder) itemView.getTag();
		imageLoader.displayImage(photos.get(position).getPhotoUrl(), vh.ivUserPhoto);
		container.addView(itemView);
		return itemView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}
	
	private View getItemView(ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_auto_view_pager, parent, false);
		ViewHolder vh = new ViewHolder();
		vh.ivUserPhoto = (ImageView) view.findViewById(R.id.iv_user_photo);
		view.setTag(vh);
		return view;
	}
	
	class ViewHolder {
		ImageView ivUserPhoto;
	}

}
