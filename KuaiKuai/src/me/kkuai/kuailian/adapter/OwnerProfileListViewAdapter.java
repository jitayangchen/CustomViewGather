package me.kkuai.kuailian.adapter;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.ownerphoto.SinglePhotoShow;
import me.kkuai.kuailian.bean.LivePhotoListBean;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.http.KImageLoader;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OwnerProfileListViewAdapter extends BaseAdapter implements OnClickListener {
	
	private Log log = LogFactory.getLog(OwnerProfileListViewAdapter.class);
	private Context context;
	private List<LivePhotoListBean> livePhotoLists = new ArrayList<LivePhotoListBean>();
	private boolean isLivePhoto = true;
	private KImageLoader imageLoader = KImageLoader.getInstance();
	private String currentUserSex = AppConstantValues.SEX_FEMALE;

	public OwnerProfileListViewAdapter(Context context) {
		this.context = context;
	}

	public List<LivePhotoListBean> getLivePhotoLists() {
		return livePhotoLists;
	}

	public void setLivePhotoLists(List<LivePhotoListBean> livePhotoLists) {
		this.livePhotoLists = livePhotoLists;
	}

	public void setLivePhoto(boolean isLivePhoto) {
		this.isLivePhoto = isLivePhoto;
	}

	@Override
	public int getCount() {
		int count;
		if (isLivePhoto) {
			count = livePhotoLists.size();
		} else {
			count = 0;
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return livePhotoLists.get(position);
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
		LivePhotoListBean livePhotoListBean = livePhotoLists.get(position);
		ViewHolder vh = (ViewHolder) convertView.getTag();
		if (livePhotoListBean.getDataLists().size() > 0) {
			if (AppConstantValues.SEX_FEMALE.equals(currentUserSex)) {
				vh.ivPhotoFirst.setImageResource(R.drawable.bg_default_female);
			} else {
				vh.ivPhotoFirst.setImageResource(R.drawable.bg_default_male);
			}
			String path = livePhotoListBean.getDataLists().get(0).getPath();
			imageLoader.displayImage(path, vh.ivPhotoFirst);
			
			vh.ivPhotoFirst.setVisibility(View.VISIBLE);
			vh.ivPhotoFirst.setOnClickListener(this);
			vh.ivPhotoFirst.setTag(path);
		} else {
			vh.ivPhotoFirst.setVisibility(View.GONE);
		}
		if (null != livePhotoListBean.getContent()) {
			vh.tvContent.setVisibility(View.VISIBLE);
			vh.tvContent.setText(livePhotoListBean.getContent());
		} else {
			vh.tvContent.setVisibility(View.GONE);
		}
		String date = livePhotoListBean.getTime().substring(0, 7);
		String day = livePhotoListBean.getTime().substring(8, 10);
		vh.tvDate.setText(date);
		vh.tvDay.setText(day);
	}

	private View getItemView(ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_live_photo, parent, false);
		ViewHolder vh = new ViewHolder();
		vh.ivPhotoFirst = (ImageView) view.findViewById(R.id.iv_live_photo);
		vh.tvContent = (TextView) view.findViewById(R.id.tv_content);
		vh.tvDate = (TextView) view.findViewById(R.id.tv_date);
		vh.tvDay = (TextView) view.findViewById(R.id.tv_day);
		view.setTag(vh);
		return view;
	}

	class ViewHolder {
		private ImageView ivPhotoFirst;
		private ImageView ivPhotoSecond;
		private ImageView ivPhotoThird;
		private TextView tvDate;
		private TextView tvDay;
		private TextView tvContent;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_live_photo:
			String path = (String) v.getTag();
			Intent intent = new Intent(context, SinglePhotoShow.class);
			intent.putExtra("path", path);
			context.startActivity(intent);
			break;

		default:
			break;
		}
	}

	public void setCurrentUserSex(String currentUserSex) {
		this.currentUserSex = currentUserSex;
	}
	
}
