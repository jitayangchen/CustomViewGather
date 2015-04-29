package me.kkuai.kuailian.adapter;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.bean.Follower;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.http.KImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FollowerListAdapter extends BaseAdapter {
	
	private Context context;
	private List<Follower> followers = new ArrayList<Follower>();
	private KImageLoader imageLoader = KImageLoader.getInstance();
	
	public FollowerListAdapter(Context context) {
		this.context = context;
	}

	public List<Follower> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Follower> followers) {
		this.followers = followers;
	}

	@Override
	public int getCount() {
		return followers.size();
	}

	@Override
	public Object getItem(int position) {
		return followers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = getItemView();
		}
		ViewHolder vh = (ViewHolder) convertView.getTag();
		Follower follower = followers.get(position);
		vh.tvNickName.setText(follower.getNickName());
		vh.tvDetailInfo.setText(follower.getCityName());
		if (AppConstantValues.SEX_FEMALE.equals(follower.getSex())) {
			vh.ivHeadSculpture.setImageResource(R.drawable.bg_default_female);
		} else {
			vh.ivHeadSculpture.setImageResource(R.drawable.bg_default_male);
		}
		imageLoader.displayImage(follower.getAvatar(), vh.ivHeadSculpture);
		return convertView;
	}
	
	private View getItemView() {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_follower_list, null, false);
		ViewHolder vh = new ViewHolder();
		vh.ivHeadSculpture = (ImageView) view.findViewById(R.id.iv_head_sculpture);
		vh.tvNickName = (TextView) view.findViewById(R.id.tv_nick_name);
		vh.tvDetailInfo = (TextView) view.findViewById(R.id.tv_detail_info);
		view.setTag(vh);
		return view;
	}
	
	class ViewHolder {
		ImageView ivHeadSculpture;
		TextView tvNickName;
		TextView tvDetailInfo;
	}

}
