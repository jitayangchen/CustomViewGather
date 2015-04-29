package me.kkuai.kuailian.adapter;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.db.FriendInfo;
import me.kkuai.kuailian.http.KImageLoader;
import me.kkuai.kuailian.utils.DateUtil;
import me.kkuai.kuailian.widget.IconStore;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class ChatFriendsListAdapter extends BaseAdapter {
	
	private Context context;
	private List<FriendInfo> friends = new ArrayList<FriendInfo>();
	private KImageLoader imageLoader = KImageLoader.getInstance();
	
	public ChatFriendsListAdapter(Context context) {
		this.context = context;
	}
	
	public List<FriendInfo> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendInfo> friends) {
		this.friends = friends;
	}

	@Override
	public int getCount() {
		return friends.size();
	}

	@Override
	public Object getItem(int position) {
		return friends.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = getItemView(parent);
		}
		bindView(convertView, position);
		return convertView;
	}
	
	private void bindView(View convertView, int position) {
		ViewHolder vh = (ViewHolder) convertView.getTag();
		FriendInfo friendInfo = friends.get(position);
		vh.tvNickName.setText(friendInfo.getNickName());
		if (TextUtils.isEmpty(friendInfo.getLastMsg())) {
			vh.tvNewMsg.setText("");
		} else {
			IconStore store = IconStore.getInstance(context);
			SpannableString spannableString = store.textToFace(friendInfo.getLastMsg());
			vh.tvNewMsg.setText(spannableString);
		}
		if (0 != friendInfo.getLastMsgTime()) {
			String updateTime = DateUtil.millsConvertDateStr(friendInfo.getLastMsgTime());
			vh.tvLastUpdateTime.setText(DateUtil.getShowTime(updateTime));
		} else {
			vh.tvLastUpdateTime.setText("");
		}
		if (0 == friendInfo.getUnReadNumber()) {
			vh.tvMsgUnreadNum.setVisibility(View.GONE);
		} else {
			vh.tvMsgUnreadNum.setVisibility(View.VISIBLE);
			vh.tvMsgUnreadNum.setText(friendInfo.getUnReadNumber() + "");
		}
		if (AppConstantValues.SEX_FEMALE.equals(friendInfo.getSex())) {
			vh.ivHead.setImageResource(R.drawable.bg_default_female);
		} else {
			vh.ivHead.setImageResource(R.drawable.bg_default_male);
		}
		imageLoader.displayImage(friendInfo.getAvatar(), vh.ivHead);
	}

	private View getItemView(ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_chat_friends, parent, false);
		ViewHolder vh = new ViewHolder();
		vh.ivHead = (ImageView) view.findViewById(R.id.iv_head);
		vh.tvNickName = (TextView) view.findViewById(R.id.tv_nick_name);
		vh.tvNewMsg = (TextView) view.findViewById(R.id.tv_new_msg);
		vh.tvLastUpdateTime = (TextView) view.findViewById(R.id.tv_last_update_time);
		vh.tvMsgUnreadNum = (TextView) view.findViewById(R.id.tv_msg_unread_num);
		view.setTag(vh);
		return view;
	}
	
	class ViewHolder {
		ImageView ivHead;
		TextView tvNickName;
		TextView tvNewMsg;
		TextView tvLastUpdateTime;
		TextView tvMsgUnreadNum;
	}

}
