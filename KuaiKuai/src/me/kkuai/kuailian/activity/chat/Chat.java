package me.kkuai.kuailian.activity.chat;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.BaseActivity;
import me.kkuai.kuailian.adapter.ChatMsgListAdapter;
import me.kkuai.kuailian.bean.ChatItem;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.db.ChatMsgDao;
import me.kkuai.kuailian.db.FriendInfo;
import me.kkuai.kuailian.db.FriendsDao;
import me.kkuai.kuailian.dialog.EmojiDialog;
import me.kkuai.kuailian.engine.ChatMessage;
import me.kkuai.kuailian.engine.MessageObservable;
import me.kkuai.kuailian.http.request.ChatMsgListRequest;
import me.kkuai.kuailian.service.socket.ChatClient;
import me.kkuai.kuailian.service.socket.ChatClient.ChatMsgChangeListener;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.DateUtil;
import me.kkuai.kuailian.utils.JsonUtil;
import me.kkuai.kuailian.utils.ShowToastUtil;
import me.kkuai.kuailian.utils.Util;
import me.kkuai.kuailian.widget.IconStore;
import me.kkuai.kuailian.widget.KeyboardListenerLinearLayout;
import me.kkuai.kuailian.widget.KeyboardListenerLinearLayout.KeyboardListener;
import me.kkuai.kuailian.widget.pulltorefresh.PullToRefreshBase;
import me.kkuai.kuailian.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import me.kkuai.kuailian.widget.pulltorefresh.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kkuai.libs.managers.J_NetManager.OnDataBack;

public class Chat extends BaseActivity implements OnClickListener, ChatMsgChangeListener, EmojiItemClickListener, OnRefreshListener<ListView> {
	
	private Context context;
	private LinearLayout llBack;
	private PullToRefreshListView pullRefreshChatMessage;
	private ListView lvChatMessage;
	private ChatMsgListAdapter msgListAdapter;
	private Button buttonSend;
	private EditText textMessage;
	private ImageView chatExpression;
	private UserInfo currentUser;
	private TextView tvNickName;
	private ChatMessage chatMessage = null;
	private FriendInfo friendInfo;
	private boolean isExistFriend = false;
	private FriendsDao friendsDao;
	private ChatMsgDao chatMsgDao;
	private int size = 20;
	private int index = 0;
	private List<ChatItem> chatItems = new ArrayList<ChatItem>();
	private List<ChatItem> datass;
	private long timeInterval = 5 * 60 * 1000;
	private long timePoint = 0;
	private KeyboardListenerLinearLayout llBottom;
	private boolean keyboardIsShow = false;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case AppConstantValues.HANDLER_RESULT_SUCCESS:
				ChatItem ci = (ChatItem) msg.obj;
				msgListAdapter.getChatItems().add(ci);
				msgListAdapter.notifyDataSetChanged();
				lvChatMessage.setSelection(lvChatMessage.getAdapter().getCount() + 1);
				break;
			case AppConstantValues.GET_DATABASE_MSG_RETURN:
				List<ChatItem> datas = (List<ChatItem>) msg.obj;
				chatItems.addAll(0, datas);
				if (chatItems.size() > 0) {
					msgListAdapter.setChatItems(chatItems);
					msgListAdapter.setFriendInfo(friendInfo);
					msgListAdapter.notifyDataSetChanged();
					
					lvChatMessage.setSelection(lvChatMessage.getAdapter().getCount() - 1);
//					lvChatMessage.post(new Runnable() {
//			            @Override
//			            public void run() {
//			            	lvChatMessage.setSelection(lvChatMessage.getAdapter().getCount() + 1);
//			            	lvChatMessage.clearFocus();
//			            }
//			        });
				}
				break;
			case AppConstantValues.LOAD_MSG_RETURN:
				datass = (List<ChatItem>) msg.obj;
				chatItems.addAll(0, datass);
				if (chatItems.size() > 0) {
					msgListAdapter.setChatItems(chatItems);
					msgListAdapter.setFriendInfo(friendInfo);
					msgListAdapter.notifyDataSetChanged();
				}
				log.info("datas.size() === " + datass.size());
//				lvChatMessage.setSelectionFromTop(datas.size(), 10);
				lvChatMessage.post(new Runnable() {
		            @Override
		            public void run() {
		            	lvChatMessage.setSelectionFromTop(datass.size(), 10);
		            }
		        });
				pullRefreshChatMessage.onRefreshComplete();
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		setContentView(R.layout.activity_chat);
		currentUser = UserManager.getInstance().getCurrentUser();
		
		initViews();
		setListener();
		
		friendInfo = new FriendInfo();
		Intent intent = getIntent();
		String friendUid = intent.getStringExtra("friendUid");
		String nickName = intent.getStringExtra("nickName");
		String sex = intent.getStringExtra("sex");
		String avatar = intent.getStringExtra("avatar");
		
		String from = intent.getStringExtra("from");
		
		friendInfo.setUid(friendUid);
		friendInfo.setNickName(nickName);
		friendInfo.setSex(sex);
		friendInfo.setAvatar(avatar);
		tvNickName.setText(friendInfo.getNickName());
		
		friendsDao = new FriendsDao(context);
		chatMsgDao = new ChatMsgDao(context);
		isExistFriend = checkIsExistFriend(friendUid);
	}
	
	@Override
	public void initViews() {
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		buttonSend = (Button) findViewById(R.id.button_send);
		textMessage = (EditText) findViewById(R.id.text_message);
		tvNickName = (TextView) findViewById(R.id.tv_nick_name);
		chatExpression = (ImageView) findViewById(R.id.chat_expression);
		pullRefreshChatMessage = (PullToRefreshListView) findViewById(R.id.pull_refresh_chat_message);
		lvChatMessage = pullRefreshChatMessage.getRefreshableView();
		msgListAdapter = new ChatMsgListAdapter(context);
		lvChatMessage.setAdapter(msgListAdapter);
		
		pullRefreshChatMessage.setOnRefreshListener(this);
		
		llBottom = (KeyboardListenerLinearLayout) findViewById(R.id.ll_bottom);
		llBottom.setKeyboardListener(new KeyboardListener() {
			
			@Override
			public void keyboardShowStatus(boolean isShow) {
				keyboardIsShow = isShow;
				if (isShow) {
					lvChatMessage.setSelection(lvChatMessage.getAdapter().getCount() - 1);
					log.info("lvChatMessage.getChildAt(lvChatMessage.getLastVisiblePosition()) === " + lvChatMessage.getChildAt(lvChatMessage.getLastVisiblePosition()));
				} else {
					log.info("lvChatMessage.getChildAt(lvChatMessage.getLastVisiblePosition()) === " + lvChatMessage.getChildAt(lvChatMessage.getLastVisiblePosition()));
				}
			}
		});
	}
	
	@Override
	public void setListener() {
		llBack.setOnClickListener(this);
		buttonSend.setOnClickListener(this);
		chatExpression.setOnClickListener(this);
		lvChatMessage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				ShowToastUtil.showToast(context, position + "");
			}
		});
		
		lvChatMessage.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (SCROLL_STATE_TOUCH_SCROLL == scrollState && keyboardIsShow) {
					Util.hiddenSoftKeyborad(textMessage, context);
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				log.info("onScroll");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.button_send:
			sendMessage(AppConstantValues.CHAT_MSG_TYPE_TEXT);
			break;
		case R.id.chat_expression:
			showDialog(AppConstantValues.DIALOG_DEAFULT_SMILEY);
			break;

		default:
			break;
		}
	}
	
	private void sendMessage(String msgType) {
		String msg = textMessage.getText().toString().trim();
		if (TextUtils.isEmpty(msg)) {
			ShowToastUtil.showToast(context, getString(R.string.chat_msg_is_null));
			return;
		}
		
		if (!isExistFriend) {
			log.info("create friend info where database");
			friendInfo.setCreatetTime(System.currentTimeMillis());
			friendInfo.setLastUpdateTime(System.currentTimeMillis());
			friendsDao.insert(friendInfo);
			isExistFriend = true;
			
			chatMsgDao.createTable(friendInfo.getUid());
		}
		
		
		ChatItem ci = new ChatItem();
		ci.setFriendUid(friendInfo.getUid());
		ci.setSenderUid(currentUser.getId());
		ci.setSelfUid(currentUser.getId());
		ci.setSendtime(System.currentTimeMillis());
		ci.setMsgType(msgType);
		ci.setMsgContent(msg);
		ci.setClientUniqueId("" + System.currentTimeMillis());
		
		if (null == chatMessage) {
			chatMessage = new ChatMessage(context, friendInfo.getUid());
		}
		chatMessage.sendMessage(ci);
		
		msgListAdapter.getChatItems().add(ci);
		msgListAdapter.notifyDataSetChanged();
		lvChatMessage.setSelection(msgListAdapter.getCount() + 2);
		MessageObservable.getInstance().updateObserver(ci);
		textMessage.setText("");
		/**
		 * Socket send message
		 */
//		ChatProtocol chatProtocol = new ChatProtocol();
//		chatProtocol.sendChatJson(ci, context);
//		SocketPool.getInatance(context).getSocketService().addRequest(chatProtocol);
		
	}

	@Override
	public void newChatMsg(ChatItem ci) {
		Message msg = handler.obtainMessage();
		msg.what = AppConstantValues.HANDLER_RESULT_SUCCESS;
		msg.obj = ci;
		handler.sendMessage(msg);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case AppConstantValues.DIALOG_DEAFULT_SMILEY:
			EmojiDialog emojiDialog = new EmojiDialog(context, IconStore.getInstance(context).getFaceResIDs(), Chat.this);
			return emojiDialog;

		default:
			break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void emojiItemClickListener(int position) {
		IconStore store = IconStore.getInstance(context);
		String str = store.getIconNames().get(position);
		SpannableString spannableString = store.textToFace(str);
		int index = textMessage.getSelectionStart();
		Editable mText = textMessage.getEditableText();
		if (index < 0 || index >= mText.length()) {
			mText.append(spannableString);
		} else {
			mText.insert(index, spannableString);
		}
	}
	
	private void loadChatMsg() {
		new Thread() {
			@Override
			public void run() {
				chatMsgDao.createTable(friendInfo.getUid());
				List<ChatItem> chatItems = chatMsgDao.queryBatch(friendInfo.getUid(), index * size, size);
				Message msg = handler.obtainMessage();
				msg.what = AppConstantValues.GET_DATABASE_MSG_RETURN;
				msg.obj = setMsgShowTime(chatItems);
				handler.sendMessage(msg);
				index ++;
			}
		}.start();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ChatClient.getInstance(context).setMsgChangeListener(this);
		if (isExistFriend) {
			log.info("exist friend");
			loadChatMsg();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		ChatClient.getInstance(context).setMsgChangeListener(null);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isExistFriend) {
			chatMsgDao.updateAllUnReadToRead(friendInfo.getUid());
		}
		MessageObservable.getInstance().updateObserver(null);
	}
	
	@Override
	public String getFriendUid() {
		return friendInfo.getUid();
	}
	
	public boolean checkIsExistFriend(String fuid) {
		boolean isExistFriend = false;
		
		FriendInfo friendInfo = friendsDao.queryFrientById(fuid);
		if (null != friendInfo) {
			isExistFriend = true;
		}
		return isExistFriend;
	}
	
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		chatMsgDao.createTable(friendInfo.getUid());
		List<ChatItem> chatItems = chatMsgDao.queryBatch(friendInfo.getUid(), index * size, size);
		index ++;
		if (chatItems.size() > 0) {
			Message msg = handler.obtainMessage();
			msg.what = AppConstantValues.LOAD_MSG_RETURN;
			msg.obj = setMsgShowTime(chatItems);
			handler.sendMessage(msg);
		} else {
			ChatMsgListRequest request = new ChatMsgListRequest(new OnDataBack() {
				
				@Override
				public void onResponse(Object result) {
					String json = (String) result;
					try {
						JSONObject obj = new JSONObject(json);
						String status = JsonUtil.getJsonString(obj, "status");
						if ("1".equals(status)) {
							JSONArray datas = JsonUtil.getJsonArrayObject(obj, "data");
							List<ChatItem> chatItems = new ArrayList<ChatItem>();
							for (int i = 0; i < datas.length(); i++) {
								JSONObject msgList = JsonUtil.getJsonObject(datas, i);
								ChatItem chatItem = new ChatItem();
								chatItem.setFriendUid(friendInfo.getUid());
								chatItem.setSelfUid(currentUser.getId());
								chatItem.setSenderUid(JsonUtil.getJsonString(msgList, "from_uid"));
								chatItem.setM_id(JsonUtil.getJsonString(msgList, "id"));
								chatItem.setM_msgId(JsonUtil.getJsonString(msgList, "msg_id"));
								chatItem.setMsgType(JsonUtil.getJsonString(msgList, "msg_type"));
								JSONObject msgContentObj = JsonUtil.getJsonObject(msgList, "msg_content");
								String msgContent = JsonUtil.getJsonString(msgContentObj, "text");
								chatItem.setMsgContent(msgContent);
								JSONObject clientPriDataObj = JsonUtil.getJsonObject(msgList, "client_pri_data");
								chatItem.setClientUniqueId(JsonUtil.getJsonString(clientPriDataObj, "cid"));
								chatItem.setSendtime(JsonUtil.getJsonLong(msgList, "send_time"));
								chatItem.setMsgStatus(JsonUtil.getJsonString(msgList, "read_status"));
								chatItem.setReadTime(JsonUtil.getJsonLong(msgList, "read_time"));
								chatItem.setReceiveTime(JsonUtil.getJsonLong(msgList, "receive_time"));
								chatItems.add(0, chatItem);
							}
							
							Message msg = handler.obtainMessage();
							msg.what = AppConstantValues.LOAD_MSG_RETURN;
							msg.obj = setMsgShowTime(chatItems);
							handler.sendMessage(msg);
							chatMsgDao.insertBatch(chatItems, friendInfo.getUid());
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onError(int Error) {
					
				}
			});
			long firstMsgTime = chatMsgDao.queryFirstUpdateTime(friendInfo.getUid());
			String time = "0";
			if (firstMsgTime != 0) {
				time = DateUtil.millsConvertDateStr(firstMsgTime);
			} else {
				time = DateUtil.millsConvertDateStr(System.currentTimeMillis());
			}
			request.requestChatMsgList(friendInfo.getUid(), "1", time, "20");
		}
	}
	
	private List<ChatItem> setMsgShowTime(List<ChatItem> chatItems) {
		if (chatItems.size() > 0) {
			timePoint = chatItems.get(chatItems.size() - 1).getSendtime();
		}
		for (int i = chatItems.size() - 1; i > 0; i--) {
			if (timePoint - timeInterval >= chatItems.get(i).getSendtime()) {
				timePoint = chatItems.get(i).getSendtime();
				chatItems.get(i).setShowTime(true);
			}
		}
		return chatItems;
	}
	
}
