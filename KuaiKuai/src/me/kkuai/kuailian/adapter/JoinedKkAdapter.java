package me.kkuai.kuailian.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.bean.JoinedKk;
import me.kkuai.kuailian.dialog.DialogJoinRecord;
import me.kkuai.kuailian.dialog.DialogJoinRecord.RecordFinishListener;
import me.kkuai.kuailian.dialog.DialogLoading;
import me.kkuai.kuailian.engine.LiveRoomDataUpdate;
import me.kkuai.kuailian.engine.UpdateDataObservable;
import me.kkuai.kuailian.http.KHttpDownload;
import me.kkuai.kuailian.http.request.AfreshRoomSignupRequest;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.utils.Preference;
import me.kkuai.kuailian.utils.ShowToastUtil;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkuai.libs.managers.J_NetManager;
import com.kkuai.libs.managers.J_NetManager.OnDataBack;
import com.kkuai.libs.managers.J_NetManager.OnLoadingListener;
import com.lidroid.xutils.exception.HttpException;

public class JoinedKkAdapter extends BaseAdapter implements OnClickListener, RecordFinishListener {
	
	private Log log = LogFactory.getLog(JoinedKkAdapter.class);
	private Context context;
	private List<JoinedKk> joinedKks = new ArrayList<JoinedKk>();
	private int lastSpreadPosition;
	private boolean isFirstClick = true;
	private long audioLength;
	private JoinedKk currentItem;
	private MediaPlayer mediaPlayer;
	private int currentPlayPosition = -1;
	private DialogLoading dialogLoading;
	
	public JoinedKkAdapter(Context context) {
		this.context = context;
	}

	public List<JoinedKk> getJoinedKks() {
		return joinedKks;
	}

	public void setJoinedKks(List<JoinedKk> joinedKks) {
		currentPlayPosition = -1;
		this.joinedKks = joinedKks;
	}

	@Override
	public int getCount() {
		return joinedKks.size();
	}

	@Override
	public Object getItem(int position) {
		return joinedKks.get(position);
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
		JoinedKk joinedKk = joinedKks.get(position);
		if (joinedKk.isSpread()) {
			vh.rlSpread.setVisibility(View.VISIBLE);
			vh.iconArrow.setBackgroundResource(R.drawable.icon_joined_arrow_to_down);
		} else {
			vh.rlSpread.setVisibility(View.GONE);
			vh.iconArrow.setBackgroundResource(R.drawable.icon_joined_arrow_to_right);
		}
		if (joinedKk.isPast()) {
			vh.btnStartRecord.setVisibility(View.GONE);
			vh.tvArea.setTextColor(context.getResources().getColor(R.color.gray_666666));
			vh.tvDate.setTextColor(context.getResources().getColor(R.color.gray_666666));
		} else {
			vh.btnStartRecord.setVisibility(View.VISIBLE);
			vh.tvArea.setTextColor(context.getResources().getColor(R.color.pink));
			vh.tvDate.setTextColor(context.getResources().getColor(R.color.pink));
		}
		if (TextUtils.isEmpty(joinedKk.getPayContent())) {
			if (joinedKk.isPast()) {
				vh.tvJoinedOvertime.setVisibility(View.VISIBLE);
			} else {
				vh.tvJoinedOvertime.setVisibility(View.GONE);
			}
			vh.rlPlayAudio.setVisibility(View.GONE);
		} else {
			vh.tvJoinedOvertime.setVisibility(View.GONE);
			vh.rlPlayAudio.setVisibility(View.VISIBLE);
		}
		if (TextUtils.isEmpty(joinedKk.getVoiceTimeLength())) {
			vh.tvVoiceTime.setVisibility(View.GONE);
		} else {
			vh.tvVoiceTime.setVisibility(View.VISIBLE);
			vh.tvVoiceTime.setText(joinedKk.getVoiceTimeLength());
		}
		
		if (joinedKk.isPlaying()) {
			vh.ivPlayVoiceAnimation.setBackgroundResource(R.anim.play_voice_animation);
			AnimationDrawable playVoiceAnimation = (AnimationDrawable) vh.ivPlayVoiceAnimation.getBackground();
			playVoiceAnimation.start();
		} else {
//			Drawable drawablea = vh.ivPlayVoiceAnimation.getBackground();
//			if (AnimationDrawable instanceof drawablea) {
//				
//			}
//			AnimationDrawable playVoiceAnimation = (AnimationDrawable) 
//			if (null != playVoiceAnimation && playVoiceAnimation.isRunning()) {
//				playVoiceAnimation.stop();
//			}
			vh.ivPlayVoiceAnimation.setBackgroundResource(R.drawable.play_voice_animation_3);
		}
		vh.tvDate.setText(joinedKk.getPayTime());
//		vh.tv_test.setText(joinedKk.getPayAbidanceTime());
		vh.rlTop.setOnClickListener(this);
		vh.rlTop.setTag(position);
		vh.btnStartRecord.setOnClickListener(this);
		vh.btnStartRecord.setTag(position);
		
		vh.rlPlayAudio.setOnClickListener(this);
		vh.rlPlayAudio.setTag(position);
	}

	private View getItemView(ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_joined_kk, parent, false);
		ViewHolder vh = new ViewHolder();
		vh.tvArea = (TextView) view.findViewById(R.id.tv_area);
		vh.tvDate = (TextView) view.findViewById(R.id.tv_date);
		vh.iconArrow = (ImageView) view.findViewById(R.id.icon_arrow);
		vh.rlTop = (RelativeLayout) view.findViewById(R.id.rl_top);
		vh.rlSpread = (RelativeLayout) view.findViewById(R.id.rl_spread);
		vh.tv_test = (TextView) view.findViewById(R.id.tv_test);
		vh.btnStartRecord = (Button) view.findViewById(R.id.btn_start_record);
		vh.rlPlayAudio = (RelativeLayout) view.findViewById(R.id.rl_play_audio);
		vh.tvJoinedOvertime = (TextView) view.findViewById(R.id.tv_joined_overtime);
		vh.tvVoiceTime = (TextView) view.findViewById(R.id.tv_voice_time);
		vh.ivPlayVoiceAnimation = (ImageView) view.findViewById(R.id.iv_play_voice_animation);
		view.setTag(vh);
		return view;
	}
	
	class ViewHolder {
		TextView tvArea;
		TextView tvDate;
		ImageView iconArrow;
		RelativeLayout rlTop;
		RelativeLayout rlSpread;
		TextView tv_test;
		Button btnStartRecord;
		RelativeLayout rlPlayAudio;
		TextView tvJoinedOvertime;
		TextView tvVoiceTime;
		ImageView ivPlayVoiceAnimation;
	}
	
	@Override
	public void onClick(View v) {
		int position =  (Integer) v.getTag();
		switch (v.getId()) {
		case R.id.rl_top:
			if (isFirstClick) {
				joinedKks.get(position).setSpread(true);
				isFirstClick = false;
			} else {
				if (lastSpreadPosition == position) {
					joinedKks.get(lastSpreadPosition).setSpread(!joinedKks.get(lastSpreadPosition).isSpread());
				} else {
					joinedKks.get(lastSpreadPosition).setSpread(false);
					joinedKks.get(position).setSpread(true);
				}
			}
			lastSpreadPosition = position;
			notifyDataSetChanged();
			break;
		case R.id.btn_start_record:
			currentItem = joinedKks.get(position);
			DialogJoinRecord dialogJoinRecord = new DialogJoinRecord(context);
			dialogJoinRecord.setCanceledOnTouchOutside(false);
			dialogJoinRecord.setFinishListener(this);
			dialogJoinRecord.show();
			break;
		case R.id.rl_play_audio:
			if (position == currentPlayPosition) {
				if (mediaPlayer != null) {
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
						joinedKks.get(currentPlayPosition).setPlaying(false);
						notifyDataSetChanged();
						return ;
					} else {
						joinedKks.get(currentPlayPosition).setPlaying(true);
						notifyDataSetChanged();
						mediaPlayer.start();
						return ;
					}
				}
			}
			if (currentPlayPosition != -1) {
				joinedKks.get(currentPlayPosition).setPlaying(false);
			}
			currentPlayPosition = position;
			String payContent = joinedKks.get(position).getPayContent();
			KHttpDownload.getInstance().addDownloadUrl(payContent, new AjaxCallBack<File>() {
				public void onSuccess(File t) {
					playAudio(t.toString());
				};
			});
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onRecordFinish(String filePath, long audioLength) {
		this.audioLength = audioLength;
		Map<String, String> params = new HashMap<String, String>();
		params.put("fileType", "1");
		params.put("token", Preference.getToken());
		String uploadFileUrl = "http://www.xinxiannv.com/api/uploadfile";
		if (dialogLoading == null) {
			dialogLoading = new DialogLoading(context);
		}
		dialogLoading.show();
		J_NetManager.getInstance().uploadFile(uploadFileUrl, params, filePath, 0, new OnLoadingListener() {
			
			@Override
			public void startLoading() {
				
			}
			
			@Override
			public void onfinishLoading(String result) {
				dialogLoading.dismiss();
				parseUploadAudio(result);
			}
			
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				
			}
			
			@Override
			public void onError(HttpException error, String msg) {
				
			}
		});
	}
	
	private void afreshRoomSignup(String signUpId, String fid) {
		AfreshRoomSignupRequest request = new AfreshRoomSignupRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				try {
					JSONObject obj = new JSONObject((String)result);
					String status = JsonUtil.getJsonString(obj, "status");
					if ("1".equals(status)) {
						ShowToastUtil.showToast(context, context.getString(R.string.signup_commit_success));
						LiveRoomDataUpdate.getInstance().update(10, null);
					} else if ("2".equals(status)) {
						ShowToastUtil.showToast(context, context.getString(R.string.signup_rerecord_fail));
					}
				} catch (JSONException e) {
					log.error("afreshRoomSignup", e);
				}
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestAfreshRoomSignup(signUpId, fid);
	}
	
	private void parseUploadAudio(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			String status = JsonUtil.getJsonString(obj, "status");
			if ("1".equals(status)) {
				String fid = JsonUtil.getJsonString(obj, "fid");
				afreshRoomSignup(currentItem.getId(), fid);
			}
		} catch (JSONException e) {
			log.error("parse upload audio", e);
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
			long voiceTimeLength = mediaPlayer.getDuration() / 1000;
			joinedKks.get(currentPlayPosition).setVoiceTimeLength((int) voiceTimeLength + "'");
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					joinedKks.get(currentPlayPosition).setPlaying(false);
					notifyDataSetChanged();
				}
			});
			mediaPlayer.start();
			
			joinedKks.get(currentPlayPosition).setPlaying(true);
			notifyDataSetChanged();
		} catch (IllegalArgumentException e) {
			joinedKks.get(currentPlayPosition).setPlaying(false);
			notifyDataSetChanged();
			log.error("play audio", e);
		} catch (SecurityException e) {
			joinedKks.get(currentPlayPosition).setPlaying(false);
			notifyDataSetChanged();
			log.error("play audio", e);
		} catch (IllegalStateException e) {
			joinedKks.get(currentPlayPosition).setPlaying(false);
			notifyDataSetChanged();
			log.error("play audio", e);
		} catch (IOException e) {
			joinedKks.get(currentPlayPosition).setPlaying(false);
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
