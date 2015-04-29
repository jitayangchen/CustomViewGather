package me.kkuai.kuailian.adapter;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.adapter.ChatFriendsListAdapter.ViewHolder;
import me.kkuai.kuailian.bean.Follow;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.db.FriendInfo;
import me.kkuai.kuailian.http.KImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FollowListAdapter extends BaseAdapter {
	
	private Context context;
	private List<Follow> follows = new ArrayList<Follow>();
	private KImageLoader imageLoader = KImageLoader.getInstance();
	
	public FollowListAdapter(Context context) {
		this.context = context;
	}

	public List<Follow> getFollows() {
		return follows;
	}

	public void setFollows(List<Follow> follows) {
		this.follows = follows;
	}

	@Override
	public int getCount() {
		return follows.size();
	}

	@Override
	public Object getItem(int position) {
		return follows.get(position);
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
		ViewHolder vh = (ViewHolder) convertView.getTag();
		Follow follow = follows.get(position);
		vh.tvNickName.setText(follow.getNickName());
		vh.tvCityName.setText(follow.getCityName());
		if (AppConstantValues.SEX_FEMALE.equals(follow.getSex())) {
			vh.ivHead.setImageResource(R.drawable.bg_default_female);
		} else {
			vh.ivHead.setImageResource(R.drawable.bg_default_male);
		}
		imageLoader.displayImage(follow.getAvatar(), vh.ivHead);
	}

	private View getItemView(ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_follow, parent, false);
		ViewHolder vh = new ViewHolder();
		vh.ivHead = (ImageView) view.findViewById(R.id.iv_head);
		vh.tvNickName = (TextView) view.findViewById(R.id.tv_nick_name);
		vh.tvCityName = (TextView) view.findViewById(R.id.tv_city_name);
		view.setTag(vh);
		return view;
	}
	
	class ViewHolder {
		ImageView ivHead;
		TextView tvNickName;
		TextView tvCityName;
	}

}
