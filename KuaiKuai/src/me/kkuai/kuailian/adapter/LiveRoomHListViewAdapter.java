package me.kkuai.kuailian.adapter;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.adapter.OwnerGalleryAlbumAdapter.ViewHolder;
import me.kkuai.kuailian.bean.EnterRoomNewUser;
import me.kkuai.kuailian.bean.OwnerPhotoBean;
import me.kkuai.kuailian.http.KImageLoader;
import me.kkuai.kuailian.utils.Util;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LiveRoomHListViewAdapter extends BaseAdapter {
	
	private Context context;
	private List<EnterRoomNewUser> newUsers = new ArrayList<EnterRoomNewUser>();
	private KImageLoader imageLoader = KImageLoader.getInstance();
	private int showUserCount = 4;
	public int mWidth;
	private int totalNum;
	
	public LiveRoomHListViewAdapter(Context context) {
		this.context = context;
	}

	public void setmWidth(int mWidth) {
		this.mWidth = mWidth;
	}

	public void setNewUsers(List<EnterRoomNewUser> newUsers) {
		showUserCount = 4;
		this.newUsers = newUsers;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public List<EnterRoomNewUser> getNewUsers() {
		return newUsers;
	}

	@Override
	public int getCount() {
		if (showUserCount <= newUsers.size()) {
			return showUserCount + 1;
		} else {
			showUserCount = newUsers.size();
			return showUserCount + 1;
		}
	}

	@Override
	public Object getItem(int position) {
		return newUsers.get(position);
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
	
	private View getItemView(ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_room_hlistview, parent, false);
		ViewHolder vh = new ViewHolder();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mWidth, mWidth);
		params.leftMargin = Util.dip2px(context, 5);
		vh.galleryImage = (ImageView) view.findViewById(R.id.iv_gallery_photo);
		vh.galleryImage.setLayoutParams(params);
		vh.llMoreUser = (LinearLayout) view.findViewById(R.id.ll_more_user);
		vh.llMoreUser.setLayoutParams(params);
		view.setTag(vh);
		vh.tvRoomUserNum = (TextView) view.findViewById(R.id.tv_room_user_num);
		return view;
	}
	
	private void bindView(View convertView, int position) {
		final ViewHolder vh = (ViewHolder) convertView.getTag();
		if (position == showUserCount) {
			vh.galleryImage.setVisibility(View.GONE);
			vh.llMoreUser.setVisibility(View.VISIBLE);
			vh.tvRoomUserNum.setText(totalNum + "");
		} else {
			EnterRoomNewUser newUser = newUsers.get(position);
			vh.galleryImage.setVisibility(View.VISIBLE);
			vh.llMoreUser.setVisibility(View.GONE);
			imageLoader.displayImage(newUser.getAvatar(), vh.galleryImage);
		}
	}
	
	class ViewHolder {
		ImageView galleryImage;
		LinearLayout llMoreUser;
		TextView tvRoomUserNum;
	}

}
