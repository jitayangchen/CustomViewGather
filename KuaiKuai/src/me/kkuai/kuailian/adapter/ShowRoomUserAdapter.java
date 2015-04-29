package me.kkuai.kuailian.adapter;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.adapter.AutoViewPagerAdapter.ViewHolder;
import me.kkuai.kuailian.bean.RoomUserInfo;
import me.kkuai.kuailian.http.KImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ShowRoomUserAdapter extends BaseAdapter {
	
	private Context context;
	private List<RoomUserInfo> userInfos = new ArrayList<RoomUserInfo>();
	private KImageLoader imageLoader = KImageLoader.getInstance();
	
	public ShowRoomUserAdapter(Context context) {
		this.context = context;
	}

	public List<RoomUserInfo> getUserInfos() {
		return userInfos;
	}

	public void setUserInfos(List<RoomUserInfo> userInfos) {
		this.userInfos = userInfos;
	}

	@Override
	public int getCount() {
		return userInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return userInfos.get(position);
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
		ViewHolder vh = (ViewHolder) convertView.getTag();
		RoomUserInfo roomUserInfo = userInfos.get(position);
		imageLoader.displayImage(roomUserInfo.getAvatar(), vh.ivUserPhoto);
		return convertView;
	}
	
	private View getItemView(ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_room_user_grid, parent, false);
		ViewHolder vh = new ViewHolder();
		vh.ivUserPhoto = (ImageView) view.findViewById(R.id.iv_room_user);
		view.setTag(vh);
		return view;
	}
	
	class ViewHolder {
		ImageView ivUserPhoto;
	}

}
