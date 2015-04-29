package me.kkuai.kuailian.adapter;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.adapter.ChatFriendsListAdapter.ViewHolder;
import me.kkuai.kuailian.db.FriendInfo;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RoomMsgListAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> msgs = new ArrayList<String>();
	
	public RoomMsgListAdapter(Context context) {
		this.context = context;
	}

	public List<String> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<String> msgs) {
		this.msgs = msgs;
	}

	@Override
	public int getCount() {
		return msgs.size();
	}

	@Override
	public Object getItem(int position) {
		return msgs.get(position);
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
		vh.tvLeft.setText(Html.fromHtml(msgs.get(position)));
	}

	private View getItemView(ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_room_msg, parent, false);
		ViewHolder vh = new ViewHolder();
		vh.tvLeft = (TextView) view.findViewById(R.id.tv_left);
		view.setTag(vh);
		return view;
	}
	
	class ViewHolder {
		TextView tvLeft;
	}

}
