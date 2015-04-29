package me.kkuai.kuailian.adapter;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.http.KImageLoader;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.user.PersonalData;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.utils.BitmapUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class GalleryMainAdapter extends BaseAdapter {
	
	private Log log = LogFactory.getLog(GalleryMainAdapter.class);
	private Context context;
	private List<UserInfo> userInfos = new ArrayList<UserInfo>();
	private KImageLoader imageLoader = KImageLoader.getInstance();
	private Bitmap male;
	private Bitmap female;
	
	public GalleryMainAdapter(Context context) {
		this.context = context;
		male = BitmapUtils.toRoundBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_default_male));
		female = BitmapUtils.toRoundBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_default_female));
	}
	
	public List<UserInfo> getUserInfos() {
		return userInfos;
	}


	public void setUserInfos(List<UserInfo> userInfos) {
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
		bindView(position, convertView);
		return convertView;
	}
	
	private void bindView(int position, View convertView) {
		ViewHolder vh = (ViewHolder) convertView.getTag();
		UserInfo userInfo = userInfos.get(position);
		
		if (AppConstantValues.SEX_MALE.equals(userInfo.getSex())) {
			if (null != male) {
				vh.ivMainUserPhoto.setImageBitmap(male);
			}
		} else {
			if (null != female) {
				vh.ivMainUserPhoto.setImageBitmap(female);
			}
		}
		if (null != userInfo.getAvatar()) {
			imageLoader.displayRoundImage(userInfo.getAvatar(), vh.ivMainUserPhoto);
		}
		vh.tvNickName.setText(userInfo.getNickName());
		String age = userInfo.getAge() == 0?"":userInfo.getAge() + context.getString(R.string.age_unit);
		String height = userInfo.getHeight() == null?"":userInfo.getHeight() + context.getString(R.string.centimeter);
		String edu = userInfo.getEducation() == null?"":PersonalData.getEducationById(context, userInfo.getEducation());
		vh.tvUserInfoTop.setText(age + " " + height + " " + edu);
		
		String cityName = userInfo.getCityName() == null?"":userInfo.getCityName();
		String constellation = userInfo.getConstellation() == null?"":PersonalData.getConstellationById(context, userInfo.getConstellation());
		vh.tvUserInfoBottom.setText(cityName + " " + constellation);
		
		vh.tvLoveExponent.setText(userInfo.getLoveExponent());
	}

	private View getItemView(ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_gallery_main, parent, false);
		ViewHolder vh = new ViewHolder();
		vh.ivMainUserPhoto = (ImageView) view.findViewById(R.id.iv_main_user_photo);
		vh.tvNickName = (TextView) view.findViewById(R.id.tv_nick_name);
		vh.tvUserInfoTop = (TextView) view.findViewById(R.id.tv_user_info_top);
		vh.tvUserInfoBottom = (TextView) view.findViewById(R.id.tv_user_info_bottom);
		vh.tvLoveExponent = (TextView) view.findViewById(R.id.tv_love_exponent);
		view.setTag(vh);
		return view;
	}

	class ViewHolder {
		ImageView ivMainUserPhoto;
		TextView tvNickName;
		TextView tvUserInfoTop;
		TextView tvUserInfoBottom;
		TextView tvLoveExponent;
	}
}
