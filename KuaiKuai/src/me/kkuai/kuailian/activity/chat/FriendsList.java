package me.kkuai.kuailian.activity.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.activity.owner.OtherProfile;
import me.kkuai.kuailian.adapter.ChatFriendsListAdapter;
import me.kkuai.kuailian.adapter.FollowListAdapter;
import me.kkuai.kuailian.adapter.FollowerListAdapter;
import me.kkuai.kuailian.bean.Follow;
import me.kkuai.kuailian.bean.Follower;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.db.FriendInfo;
import me.kkuai.kuailian.db.FriendsDao;
import me.kkuai.kuailian.engine.MessageObservable;
import me.kkuai.kuailian.http.request.FollowListRequest;
import me.kkuai.kuailian.http.request.FollowerListRequest;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.widget.pulltorefresh.PullToRefreshListView;
import me.kkuai.kuailian.widget.pulltorefresh.PullToRefreshBase.Mode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class FriendsList extends BaseActivity implements OnClickListener, OnItemClickListener, Observer {

	private Context context;
	private LinearLayout llBack;
	private ListView lvChatFriends;
	private ListView lvFollow;
	private ListView lvFollower;
	private TextView followNullPrompt, followerNullPrompt;
	private PullToRefreshListView pullRefreshChatFriends;
	private PullToRefreshListView pullRefreshFollow;
	private PullToRefreshListView pullRefreshFollower;
	private ChatFriendsListAdapter chatFriendsListAdapter;
	private FollowListAdapter followListAdapter;
	private FollowerListAdapter followerListAdapter;
	private TextView tvFriends;
	private TextView tvFollow;
	private TextView tvFollower;
	private EditText edit_search_att;
	private RelativeLayout rl_search_text;
	private View view;
	private RelativeLayout rlChatFriends, rlFollow, rlFollower;
	private FriendsDao friendsDao;
	private RelativeLayout rlTitleFriends, rlTitleFollow, rlTitleFollower;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AppConstantValues.FRIENDS_QUERY_RETURN:
				List<FriendInfo> friends = (List<FriendInfo>) msg.obj;
				chatFriendsListAdapter.setFriends(friends);
				chatFriendsListAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_list);
		context = this;

		initViews();
		setListener();

		rlChatFriends.setVisibility(View.VISIBLE);
		getFriendsListByDataBase();
		MessageObservable.getInstance().addObserver(this);
		
		rlTitleFriends.setBackgroundResource(R.drawable.bg_top_title_left_box);
		tvFriends.setSelected(true);
	}

	@Override
	protected void onStart() {
		super.onStart();
		log.info("FriendsList onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void initViews() {
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		pullRefreshChatFriends = (PullToRefreshListView) findViewById(R.id.pull_refresh_chat_friends);
		pullRefreshFollow = (PullToRefreshListView) findViewById(R.id.pull_refresh_follow);
		pullRefreshFollower = (PullToRefreshListView) findViewById(R.id.pull_refresh_follower);
		lvChatFriends = pullRefreshChatFriends.getRefreshableView();
		pullRefreshChatFriends.setMode(Mode.PULL_FROM_END);
		lvFollow = pullRefreshFollow.getRefreshableView();
		lvFollower = pullRefreshFollower.getRefreshableView();
		
		followNullPrompt = (TextView) findViewById(R.id.follow_null_prompt);
		followerNullPrompt = (TextView) findViewById(R.id.follower_null_prompt);
		
		rlChatFriends = (RelativeLayout) findViewById(R.id.rl_chat_friends);
		rlFollow = (RelativeLayout) findViewById(R.id.rl_follow);
		rlFollower = (RelativeLayout) findViewById(R.id.rl_follower);

		rlTitleFriends = (RelativeLayout) findViewById(R.id.rl_title_friends);
		rlTitleFollow = (RelativeLayout) findViewById(R.id.rl_title_follow);
		rlTitleFollower = (RelativeLayout) findViewById(R.id.rl_title_follower);
		tvFriends = (TextView) findViewById(R.id.tv_friends);
		tvFollow = (TextView) findViewById(R.id.tv_follow);
		tvFollower = (TextView) findViewById(R.id.tv_follower);

		chatFriendsListAdapter = new ChatFriendsListAdapter(context);
		lvChatFriends.setAdapter(chatFriendsListAdapter);
		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.attention_head_search_new, null);
		rl_search_text = (RelativeLayout) view
				.findViewById(R.id.rl_search_text);
		rl_search_text.setVisibility(View.VISIBLE);
		edit_search_att = (EditText) view.findViewById(R.id.edit_search_att);
		edit_search_att.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				rl_search_text.setVisibility(View.INVISIBLE);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				rl_search_text.setVisibility(View.VISIBLE);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() == 0) {
					rl_search_text.setVisibility(View.VISIBLE);
				} else {
					rl_search_text.setVisibility(View.INVISIBLE);
				}
			}
		});
//		lvFollow.addHeaderView(view);
		followListAdapter = new FollowListAdapter(context);
		followerListAdapter = new FollowerListAdapter(context);
		lvFollow.setAdapter(followListAdapter);
		lvFollower.setAdapter(followerListAdapter);

	}

	@Override
	public void setListener() {
		llBack.setOnClickListener(this);
		lvChatFriends.setOnItemClickListener(this);
		tvFriends.setOnClickListener(this);
		tvFollow.setOnClickListener(this);
		tvFollower.setOnClickListener(this);
		
		lvFollow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position - 1 < 0) {
					return;
				}
				Follow follow = followListAdapter.getFollows().get(position - 1);
				Intent intent = new Intent(context, OtherProfile.class);
				intent.putExtra("nickName", follow.getNickName());
				intent.putExtra("uid", follow.getFuid());
				startActivity(intent);
			}
			
		});
		
		pullRefreshFollower.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position - 1 < 0) {
					return;
				}
				Follower follower = followerListAdapter.getFollowers().get(position - 1);
				Intent intent = new Intent(context, OtherProfile.class);
				intent.putExtra("uid", follower.getFuid());
				intent.putExtra("nickName", follower.getNickName());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.tv_friends:
			if (rlChatFriends.getVisibility() == View.INVISIBLE) {
				return;
			}
			invisableAll();
			rlTitleFriends.setBackgroundResource(R.drawable.bg_top_title_left_box);
			tvFriends.setSelected(true);
			rlChatFriends.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_follow:
			if (rlFollow.getVisibility() == View.INVISIBLE) {
				return;
			}
			invisableAll();
			rlTitleFollow.setBackgroundResource(R.drawable.bg_top_title_center_box);
			tvFollow.setSelected(true);
			rlFollow.setVisibility(View.VISIBLE);
			requestFollowList();
			break;
		case R.id.tv_follower:
			if (rlFollower.getVisibility() == View.INVISIBLE) {
				return;
			}
			invisableAll();
			rlTitleFollower.setBackgroundResource(R.drawable.bg_top_title_right_box);
			tvFollower.setSelected(true);
			rlFollower.setVisibility(View.VISIBLE);
			requestFollowerList();
			break;

		default:
			break;
		}
	}

	private void invisableAll() {
		rlChatFriends.setVisibility(View.GONE);
		rlFollow.setVisibility(View.GONE);
		rlFollower.setVisibility(View.GONE);
		
		tvFriends.setSelected(false);
		tvFollow.setSelected(false);
		tvFollower.setSelected(false);
		
		rlTitleFriends.setBackgroundDrawable(null);
		rlTitleFollow.setBackgroundDrawable(null);
		rlTitleFollower.setBackgroundDrawable(null);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position - 1 < 0) {
			return;
		}
		FriendInfo friendInfo = chatFriendsListAdapter.getFriends().get(position - 1);
		Intent intent = new Intent(context, Chat.class);
		intent.putExtra("friendUid", friendInfo.getUid());
		intent.putExtra("nickName", friendInfo.getNickName());
		intent.putExtra("sex", friendInfo.getSex());
		intent.putExtra("avatar", friendInfo.getAvatar());
		startActivity(intent);
	}
	
	private void requestFollowList() {
		FollowListRequest request = new FollowListRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				List<Follow> follows = (List<Follow>) result;
				if (follows.size() > 0) {
					followListAdapter.setFollows(follows);
					followListAdapter.notifyDataSetChanged();
				} else {
					followNullPrompt.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestFollowList("1");
	}
	
	private void requestFollowerList() {
		FollowerListRequest request = new FollowerListRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				parseFollowerInfo((String) result);
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestFollowerList("1");
	}
	
	private void parseFollowerInfo(String result) {
		List<Follower> followers = new ArrayList<Follower>();
		try {
			JSONObject obj = new JSONObject(result);
			String status = JsonUtil.getJsonString(obj, "status");
			if ("1".equals(status)) {
				JSONArray followLists = JsonUtil.getJsonArrayObject(obj, "followlist");
				for (int i = 0; i < followLists.length(); i++) {
					JSONObject followList = JsonUtil.getJsonObject(followLists, i);
					Follower follower = new Follower();
					follower.setFuid(JsonUtil.getJsonString(followList, "fuid"));
					follower.setNickName(JsonUtil.getJsonString(followList, "nickName"));
					follower.setAvatar(JsonUtil.getJsonString(followList, "avatar"));
					follower.setSex(JsonUtil.getJsonString(followList, "sex"));
					follower.setFollowTime(JsonUtil.getJsonString(followList, "followTime"));
					follower.setAge(JsonUtil.getJsonString(followList, "age"));
					follower.setCityId(JsonUtil.getJsonString(followList, "cityId"));
					follower.setCityName(JsonUtil.getJsonString(followList, "cityName"));
					followers.add(follower);
				}
				if (followers.size() > 0) {
					followerListAdapter.setFollowers(followers);
					followerListAdapter.notifyDataSetChanged();
				} else {
					followerNullPrompt.setVisibility(View.VISIBLE);
				}
			}
		} catch (JSONException e) {
			log.error("parse Follower Info", e);
		}
	}
	
	private void getFriendsListByDataBase() {
		if (null == friendsDao) {
			friendsDao = new FriendsDao(context);
		}
		new Thread() {
			public void run() {
				List<FriendInfo> friends = friendsDao.queryByPageSize(0, 100);
				
				Message msg = handler.obtainMessage();
				msg.what = AppConstantValues.FRIENDS_QUERY_RETURN;
				msg.obj = friends;
				handler.sendMessage(msg);
			};
		}.start();;
	}

	@Override
	public void update(Observable observable, Object data) {
		getFriendsListByDataBase();
	}

}
