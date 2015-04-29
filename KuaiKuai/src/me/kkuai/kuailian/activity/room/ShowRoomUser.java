package me.kkuai.kuailian.activity.room;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.activity.owner.OtherProfile;
import me.kkuai.kuailian.adapter.ShowRoomUserAdapter;
import me.kkuai.kuailian.bean.RoomUserInfo;
import me.kkuai.kuailian.http.request.RoomUserListRequest;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.widget.pulltorefresh.PullToRefreshBase;
import me.kkuai.kuailian.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import me.kkuai.kuailian.widget.pulltorefresh.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class ShowRoomUser extends BaseActivity implements OnClickListener, OnItemClickListener, OnRefreshListener2<GridView> {

	private Context context;
	private LinearLayout llBack;
	private PullToRefreshGridView pullToRefreshGridView;
	private GridView mGridView;
	private String roomId;
	private String totalNum;
	private String totalPageNum;
	private String pageSize = "20";
	private List<RoomUserInfo> userInfos = new ArrayList<RoomUserInfo>();
	private ShowRoomUserAdapter roomUserAdapter;
	private int pageNo = 1;
	private int nextPage = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_room_user);
		context = this;
		
		Intent intent = getIntent();
		roomId = intent.getStringExtra("roomId");
		
		initViews();
		setListener();
		
		requestRoomAllUser();
	}
	
	@Override
	public void initViews() {
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pull_to_refresh_gridview);
		mGridView = pullToRefreshGridView.getRefreshableView();
		roomUserAdapter = new ShowRoomUserAdapter(context);
		mGridView.setAdapter(roomUserAdapter);
	}
	
	@Override
	public void setListener() {
		llBack.setOnClickListener(this);
		mGridView.setOnItemClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		RoomUserInfo roomUserInfo = userInfos.get(position);
		Intent intent = new Intent(context, OtherProfile.class);
		intent.putExtra("uid", roomUserInfo.getUid());
//		intent.putExtra("nickName", roomUserInfo.get);
		startActivity(intent);
	}
	
	private void requestRoomAllUser() {
		RoomUserListRequest request = new RoomUserListRequest(new OnDataBack() {
			
			@Override
			public void onResponse(Object result) {
				parseRoomUserList((String)result);
			}
			
			@Override
			public void onError(int Error) {
				
			}
		});
		
		request.requestRoomUserList(roomId, "1", pageSize);
	}
	
	private void parseRoomUserList(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			String status = JsonUtil.getJsonString(obj, "status");
			if ("1".equals(status)) {
				JSONObject data = JsonUtil.getJsonObject(obj, "data");
				totalNum = JsonUtil.getJsonString(data, "totalNum");
				totalPageNum = JsonUtil.getJsonString(data, "totalPageNum");
				JSONArray userLists = JsonUtil.getJsonArrayObject(data, "userList");
				for (int i = 0; i < userLists.length(); i++) {
					JSONObject userList = JsonUtil.getJsonObject(userLists, i);
					RoomUserInfo userInfo = new RoomUserInfo();
					userInfo.setUid(JsonUtil.getJsonString(userList, "uid"));
					userInfo.setSex(JsonUtil.getJsonString(userList, "sex"));
					userInfo.setLeave(JsonUtil.getJsonBoolean(userList, "isLeave"));
					userInfo.setLeaveTime(JsonUtil.getJsonString(userList, "leaveTime"));
					userInfo.setEnterTime(JsonUtil.getJsonString(userList, "enterTime"));
					userInfo.setAvatar(JsonUtil.getJsonString(userList, "avatar"));
					userInfos.add(userInfo);
				}
				roomUserAdapter.setUserInfos(userInfos);
				roomUserAdapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			log.error("parse Room User List", e);
		}
	}
	
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		pageNo = 1;
	}
	
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		pageNo++;
	}
}
