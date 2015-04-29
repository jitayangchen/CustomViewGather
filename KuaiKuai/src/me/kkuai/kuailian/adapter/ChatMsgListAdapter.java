package me.kkuai.kuailian.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;
import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.owner.OtherProfile;
import me.kkuai.kuailian.activity.owner.OwnerProfile;
import me.kkuai.kuailian.bean.ChatItem;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.db.FriendInfo;
import me.kkuai.kuailian.http.KHttpDownload;
import me.kkuai.kuailian.http.KImageLoader;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.DateUtil;
import me.kkuai.kuailian.widget.IconStore;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatMsgListAdapter extends BaseAdapter implements OnClickListener {
	
	private Log log = LogFactory.getLog(ChatMsgListAdapter.class);
	private Context context;
	private List<ChatItem> chatItems = new ArrayList<ChatItem>();
	private UserInfo currentUser;
	private FriendInfo friendInfo;
	private KImageLoader imageLoader = KImageLoader.getInstance();
	private MediaPlayer mediaPlayer;
	private int currentPlayPosition = -1;

	public ChatMsgListAdapter(Context context) {
		this.context = context;
		currentUser = UserManager.getInstance().getCurrentUser();
	}
	
	public FriendInfo getFriendInfo() {
		return friendInfo;
	}

	public void setFriendInfo(FriendInfo friendInfo) {
		this.friendInfo = friendInfo;
	}

	public List<ChatItem> getChatItems() {
		return chatItems;
	}
	
	public void setChatItems(List<ChatItem> chatItems) {
		this.chatItems = chatItems;
	}

	@Override
	public int getCount() {
		return chatItems.size();
	}

	@Override
	public Object getItem(int position) {
		return chatItems.get(position);
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
		log.info("bindView_position === " + position);
		ViewHolder vh = (ViewHolder) convertView.getTag();
		ChatItem chatItem = chatItems.get(position);
		String content = chatItem.getMsgContent();
		if (AppConstantValues.CHAT_MSG_TYPE_TEXT.equals(chatItem.getMsgType())) {
			
			vh.ivPlayVoiceAnimationLeft.setVisibility(View.GONE);
			vh.ivPlayVoiceAnimationRight.setVisibility(View.GONE);
			
			vh.tvMsgContentLeft.setVisibility(View.VISIBLE);
			vh.tvMsgContentRight.setVisibility(View.VISIBLE);
			
			SpannableString spannableString = null;
			if (!TextUtils.isEmpty(content)) {
				IconStore store = IconStore.getInstance(context);
				spannableString = store.textToFace(content);
			}
			if (chatItem.getSelfUid().equals(chatItem.getSenderUid())) {
				vh.rlLeft.setVisibility(View.GONE);
				vh.rlRight.setVisibility(View.VISIBLE);
				vh.tvMsgContentRight.setText((null == spannableString)?"":spannableString);
				imageLoader.displayImage(currentUser.getAvatar(), vh.ivHeadRight);
			} else {
				vh.rlLeft.setVisibility(View.VISIBLE);
				vh.rlRight.setVisibility(View.GONE);
				vh.tvMsgContentLeft.setText((null == spannableString)?"":spannableString);
				imageLoader.displayImage(friendInfo.getAvatar(), vh.ivHeadLeft);
			}
		} else if (AppConstantValues.CHAT_MSG_TYPE_VOICE.equals(chatItem.getMsgType())) {
			
			vh.tvMsgContentLeft.setVisibility(View.GONE);
			vh.tvMsgContentRight.setVisibility(View.GONE);
			
			vh.ivPlayVoiceAnimationLeft.setVisibility(View.VISIBLE);
			vh.ivPlayVoiceAnimationRight.setVisibility(View.VISIBLE);
			
			if (chatItem.getSelfUid().equals(chatItem.getSenderUid())) {
				vh.rlLeft.setVisibility(View.GONE);
				vh.rlRight.setVisibility(View.VISIBLE);
				imageLoader.displayImage(currentUser.getAvatar(), vh.ivHeadRight);
			} else {
				vh.rlLeft.setVisibility(View.VISIBLE);
				vh.rlRight.setVisibility(View.GONE);
				imageLoader.displayImage(friendInfo.getAvatar(), vh.ivHeadLeft);
				
				if (chatItem.isPlaying()) {
					vh.ivPlayVoiceAnimationLeft.setBackgroundResource(R.anim.play_voice_animation);
					AnimationDrawable playVoiceAnimation = (AnimationDrawable) vh.ivPlayVoiceAnimationLeft.getBackground();
					playVoiceAnimation.start();
				} else {
					vh.ivPlayVoiceAnimationLeft.setBackgroundResource(R.drawable.play_voice_animation_3);
				}
			}
			
			vh.rlLeft.setOnClickListener(this);
			vh.rlLeft.setTag(position);
			vh.rlRight.setOnClickListener(this);
			vh.rlRight.setTag(position);
		}
		
		if (chatItem.isShowTime()) {
			String time = DateUtil.millsConvertDateStr(chatItem.getSendtime());
			vh.tvMsgTime.setText(time);
		} else {
			vh.tvMsgTime.setText("");
		}
		
		vh.ivHeadRight.setOnClickListener(this);
		vh.ivHeadLeft.setOnClickListener(this);
		vh.ivHeadRight.setTag(position);
		vh.ivHeadLeft.setTag(position);
	}

	private View getItemView(ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_chat_msg_list, parent, false);
		ViewHolder vh = new ViewHolder();
		vh.rlLeft = (RelativeLayout) view.findViewById(R.id.rl_left);
		vh.rlRight = (RelativeLayout) view.findViewById(R.id.rl_right);
		vh.ivHeadLeft = (ImageView) view.findViewById(R.id.iv_head_left);
		vh.ivHeadRight = (ImageView) view.findViewById(R.id.iv_head_right);
		vh.tvMsgContentLeft = (TextView) view.findViewById(R.id.tv_msg_content_left);
		vh.tvMsgContentRight = (TextView) view.findViewById(R.id.tv_msg_content_right);
		vh.tvMsgTime = (TextView) view.findViewById(R.id.tv_msg_time);
		vh.ivPlayVoiceAnimationLeft = (ImageView) view.findViewById(R.id.iv_play_voice_animation_left);
		vh.ivPlayVoiceAnimationRight = (ImageView) view.findViewById(R.id.iv_play_voice_animation_right);
		view.setTag(vh);
		return view;
	}
	
	class ViewHolder {
		RelativeLayout rlLeft;
		RelativeLayout rlRight;
		ImageView ivHeadLeft;
		ImageView ivHeadRight;
		TextView tvMsgContentLeft;
		TextView tvMsgContentRight;
		TextView tvMsgTime;
		ImageView ivPlayVoiceAnimationLeft;
		ImageView ivPlayVoiceAnimationRight;
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		int position = (Integer) v.getTag();
		switch (v.getId()) {
		case R.id.iv_head_left:
			intent = new Intent(context, OtherProfile.class);
			intent.putExtra("uid", friendInfo.getUid());
			intent.putExtra("nickName", friendInfo.getNickName());
			context.startActivity(intent);
			break;
		case R.id.iv_head_right:
			intent = new Intent(context, OwnerProfile.class);
			context.startActivity(intent);
			break;
		case R.id.rl_left:
			if (position == currentPlayPosition) {
				if (mediaPlayer != null) {
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
						chatItems.get(currentPlayPosition).setPlaying(false);
						notifyDataSetChanged();
						return ;
					} else {
						chatItems.get(currentPlayPosition).setPlaying(true);
						notifyDataSetChanged();
						mediaPlayer.start();
						return ;
					}
				}
			}
			if (currentPlayPosition != -1) {
				chatItems.get(currentPlayPosition).setPlaying(false);
			}
			currentPlayPosition = position;
			String payContent = chatItems.get(position).getMsgContent();
			KHttpDownload.getInstance().addDownloadUrl(payContent, new AjaxCallBack<File>() {
				public void onSuccess(File t) {
					playAudio(t.toString());
				};
			});
			break;
		case R.id.rl_right:
			
			break;

		default:
			break;
		}
	}
	
	private void playAudio(String audioPath) {
		if (TextUtils.isEmpty(audioPath)) {
			return ;
		}
		log.info("audioPath === === === === === ===" + audioPath);
		if (null == mediaPlayer) {
			mediaPlayer = new MediaPlayer();
		}
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(audioPath);
			mediaPlayer.prepare();
//			long voiceTimeLength = mediaPlayer.getDuration() / 1000;
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					chatItems.get(currentPlayPosition).setPlaying(false);
					notifyDataSetChanged();
				}
			});
			mediaPlayer.start();
			
			chatItems.get(currentPlayPosition).setPlaying(true);
			notifyDataSetChanged();
		} catch (IllegalArgumentException e) {
			chatItems.get(currentPlayPosition).setPlaying(false);
			notifyDataSetChanged();
			log.error("play audio", e);
		} catch (SecurityException e) {
			chatItems.get(currentPlayPosition).setPlaying(false);
			notifyDataSetChanged();
			log.error("play audio", e);
		} catch (IllegalStateException e) {
			chatItems.get(currentPlayPosition).setPlaying(false);
			notifyDataSetChanged();
			log.error("play audio", e);
		} catch (IOException e) {
			chatItems.get(currentPlayPosition).setPlaying(false);
			notifyDataSetChanged();
			log.error("play audio", e);
		}
	}
	
	public void stop() {
		if (null != mediaPlayer && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.reset();
		}
	}
	
	public void release() {
		if (null != mediaPlayer && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

}
