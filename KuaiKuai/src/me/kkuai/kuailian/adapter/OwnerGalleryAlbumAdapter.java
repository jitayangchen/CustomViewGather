package me.kkuai.kuailian.adapter;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.bean.OwnerPhotoBean;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.http.KImageLoader;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.utils.BitmapUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class OwnerGalleryAlbumAdapter extends BaseAdapter {
	
	private Log log = LogFactory.getLog(OwnerGalleryAlbumAdapter.class);
	private Context context;
	private List<OwnerPhotoBean> photoItems = new ArrayList<OwnerPhotoBean>();
	private KImageLoader imageLoader = KImageLoader.getInstance();
	private boolean isOwner = true;
	private String currentUserSex = AppConstantValues.SEX_FEMALE;
	
	public OwnerGalleryAlbumAdapter(Context context) {
		this.context = context;
	}

	public List<OwnerPhotoBean> getPhotoUrls() {
		return photoItems;
	}

	public void setPhotoUrls(List<OwnerPhotoBean> photoItems) {
		this.photoItems = photoItems;
	}

	@Override
	public int getCount() {
		if (isOwner) {
			return photoItems.size() + 1;
		} else {
			return photoItems.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return photoItems.get(position + 1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = getItemView(parent);
		}
		bindView(convertView, position);
		return convertView;
	}
	
	private void bindView(View convertView, int position) {
		final ViewHolder vh = (ViewHolder) convertView.getTag();
		if (isOwner) {
			if (position == 0) {
				vh.galleryImage.setVisibility(View.GONE);
				vh.iconCamera.setVisibility(View.VISIBLE);
			} else {
				OwnerPhotoBean ownerPhotoBean = photoItems.get(position - 1);
				vh.galleryImage.setVisibility(View.VISIBLE);
				vh.iconCamera.setVisibility(View.GONE);
//				String path = ownerPhotoBean.getPhotoUrl().substring(0, ownerPhotoBean.getPhotoUrl().length() - 4) + "_a_86.jpg";
				
				if (AppConstantValues.SEX_FEMALE.equals(currentUserSex)) {
					vh.galleryImage.setImageResource(R.drawable.bg_default_female);
				} else {
					vh.galleryImage.setImageResource(R.drawable.bg_default_male);
				}
				String path = ownerPhotoBean.getPhotoUrl();
				imageLoader.displayImage(path, vh.galleryImage);
			}
		} else {
			OwnerPhotoBean ownerPhotoBean = photoItems.get(position);
//			String path = ownerPhotoBean.getPhotoUrl().substring(0, ownerPhotoBean.getPhotoUrl().length() - 4) + "_a_86.jpg";
			if (AppConstantValues.SEX_FEMALE.equals(currentUserSex)) {
				vh.galleryImage.setImageResource(R.drawable.bg_default_female);
			} else {
				vh.galleryImage.setImageResource(R.drawable.bg_default_male);
			}
			String path = ownerPhotoBean.getPhotoUrl();
			imageLoader.displayImage(path, vh.galleryImage);
		}
	}

	private View getItemView(ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_owner_gallery_album, parent, false);
		ViewHolder vh = new ViewHolder();
		vh.galleryImage = (ImageView) view.findViewById(R.id.iv_gallery_photo);
		vh.iconCamera = (ImageView) view.findViewById(R.id.iv_icon_camera);
		view.setTag(vh);
		return view;
	}
	
	class ViewHolder {
		ImageView galleryImage;
		ImageView iconCamera;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

	public void setCurrentUserSex(String currentUserSex) {
		this.currentUserSex = currentUserSex;
	}

}
