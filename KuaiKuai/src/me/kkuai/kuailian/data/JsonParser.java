package me.kkuai.kuailian.data;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.bean.EnterRoomNewUser;
import me.kkuai.kuailian.bean.Follow;
import me.kkuai.kuailian.bean.OwnerPhotoBean;
import me.kkuai.kuailian.bean.RoomLiveListBean;
import me.kkuai.kuailian.bean.RoomMsg;
import me.kkuai.kuailian.constant.HttpRequestTypeId;
import me.kkuai.kuailian.db.FriendInfo;
import me.kkuai.kuailian.http.response.LoginResponse;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.user.UserInfo;
import me.kkuai.kuailian.user.UserManager;
import me.kkuai.kuailian.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.JsonToken;

import com.kkuai.libs.json.parser.J_JsonParser;
import com.kkuai.libs.net.response.J_Response;

public class JsonParser extends J_JsonParser {
	
	private Log log = LogFactory.getLog(JsonParser.class);

	@Override
	public Object parsJson(String jsonStr, int whitchRequest) throws Exception {
		switch (whitchRequest) {
		case HttpRequestTypeId.TASKID_LOGIN:
			return parseLoginInfo(jsonStr);
		case HttpRequestTypeId.TASKID_QUERY_USER_INFO:
			return parseUserInfo(jsonStr);
		case HttpRequestTypeId.TASKID_QUERY_OTHER_USER_INFO:
			return parseOtherUserInfo(jsonStr);
		case HttpRequestTypeId.TASKID_HOME_PAGE_DATA:
			return parseHomePageData(jsonStr);
		case HttpRequestTypeId.TASKID_LIVE_ROOM_DATA:
			return parseRoomLiveListData(jsonStr);
		case HttpRequestTypeId.TASKID_USER_PHOTO:
			return parseOwnerPhoto(jsonStr);
		case HttpRequestTypeId.TASKID_ENTER_ROOM_USER:
			return jsonStr;
		case HttpRequestTypeId.TASKID_SET_USER_ENTER_ROOM:
			return jsonStr;
		case HttpRequestTypeId.TASKID_SET_USER_LEAVE_ROOM:
			return jsonStr;
		case HttpRequestTypeId.TASKID_OWNER_LIFE_STREAM:
			return jsonStr;
		case HttpRequestTypeId.TASKID_EDIT_USER_INFO:
			return jsonStr;
		case HttpRequestTypeId.TASKID_ROOM_SIGN_UP_INFO:
			return parseRoomInfo(jsonStr);
		case HttpRequestTypeId.TASKID_CHAT_FRIEND_LIST:
			return parseFriendsList(jsonStr);
		case HttpRequestTypeId.TASKID_ROOM_SIGN_UP:
			return jsonStr;
		case HttpRequestTypeId.TASKID_OWNER_JOINED_KK_LIST:
			return jsonStr;
		case HttpRequestTypeId.TASKID_FOLLOW_LIST:
			return parseFollowList(jsonStr);
		case HttpRequestTypeId.TASKID_FOLLOWER_LIST:
			return jsonStr;
		case HttpRequestTypeId.TASKID_SEND_ROOM_MSG:
			return parseSendRoomMsg(jsonStr);
		case HttpRequestTypeId.TASKID_FOLLOW:
			return jsonStr;
		case HttpRequestTypeId.TASKID_CANCEL_FOLLOW:
			return jsonStr;
		case HttpRequestTypeId.TASKID_PULL_CURRENT_DAY_COIN:
			return parseKuaiCoin(jsonStr);
		case HttpRequestTypeId.TASKID_ROOM_USER_LIST:
			return jsonStr;
		case HttpRequestTypeId.TASKID_REGISTER:
			return jsonStr;
		case HttpRequestTypeId.TASKID_SEND_MOBILE_CAPTCHA:
			return jsonStr;
		case HttpRequestTypeId.TASKID_REGISTER_SECOND:
			return jsonStr;
		case HttpRequestTypeId.TASKID_SEND_CHAT_MSG:
			return jsonStr;
		case HttpRequestTypeId.TASKID_OFFLINE_CHAT_MSG_NUM:
			return jsonStr;
		case HttpRequestTypeId.TASKID_ADD_CHAT_MSG_FRIEND:
			return jsonStr;
		case HttpRequestTypeId.TASKID_GET_ACCOUNT_INFO:
			return jsonStr;
		case HttpRequestTypeId.TASKID_AFRESH_ROOM_SIGNUP:
			return jsonStr;
		case HttpRequestTypeId.TASKID_CHAT_MSG_LIST:
			return jsonStr;
		case HttpRequestTypeId.TASKID_ADD_LIFE_RECORD:
			return jsonStr;
		case HttpRequestTypeId.TASKID_CLIENT_UPDATE_CHECK:
			return jsonStr;
		case HttpRequestTypeId.TASKID_DELETE_PHOTOS:
			return jsonStr;
		case HttpRequestTypeId.TASKID_ROOM_CHAT_MSG:
			return jsonStr;
		default:
			break;
		}
		return null;
	}
	
	/**
	 * parse login info
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	private J_Response parseLoginInfo(String result) throws JSONException {
		LoginResponse response = new LoginResponse();
		JSONObject obj = new JSONObject(result);
		if (obj.has("status")) {
			String status = JsonUtil.getJsonString(obj, "status");
			if ("1".equals(status)) {
				String uid = JsonUtil.getJsonString(obj, "uid");
				String userStatus = JsonUtil.getJsonString(obj, "userStatus");
				
				UserInfo userInfo = new UserInfo();
				userInfo.setId(uid);
				userInfo.setUserStatus(userStatus);
				response.userInfo = userInfo;
				response.token = JsonUtil.getJsonString(obj, "token");
			}
		}
		return response;
	}
	
	/**
	 * parse current user info
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	private UserInfo parseUserInfo(String result) throws JSONException {
		UserInfo currentUser = UserManager.getInstance().getCurrentUser();
		JSONObject obj = new JSONObject(result);
		if (obj.has("status") && "1".equals(JsonUtil.getJsonString(obj, "status"))) {
			JSONObject userinfo = JsonUtil.getJsonObject(obj, "userinfo");
			currentUser.setNickName(JsonUtil.getJsonString(userinfo, "nickName"));
			currentUser.setSex(JsonUtil.getJsonString(userinfo, "sex"));
			currentUser.setBirth(JsonUtil.getJsonString(userinfo, "birth"));
			currentUser.setAvatar(JsonUtil.getJsonString(userinfo, "avatar"));
			currentUser.setBirth(JsonUtil.getJsonString(userinfo, "birth"));
			currentUser.setWeight(JsonUtil.getJsonString(userinfo, "weight"));
			currentUser.setHeight(JsonUtil.getJsonString(userinfo, "height"));
			currentUser.setEducation(JsonUtil.getJsonString(userinfo, "education"));
			currentUser.setCarType(JsonUtil.getJsonString(userinfo, "carType"));
			currentUser.setIncome(JsonUtil.getJsonString(userinfo, "income"));
			currentUser.setWorkTrade(JsonUtil.getJsonString(userinfo, "workTrade"));
			currentUser.setJob(JsonUtil.getJsonString(userinfo, "job"));
			currentUser.setMarriage(JsonUtil.getJsonString(userinfo, "marriage"));
			currentUser.setNode(JsonUtil.getJsonString(userinfo, "node"));
			currentUser.setRegistTime(JsonUtil.getJsonString(userinfo, "registTime"));
			currentUser.setSchool(JsonUtil.getJsonString(userinfo, "school"));
			currentUser.setWordType(JsonUtil.getJsonString(userinfo, "workTrade"));
			currentUser.setHousingType(JsonUtil.getJsonString(userinfo, "housingType"));
			currentUser.setCityId(JsonUtil.getJsonString(userinfo, "cityId"));
			currentUser.setNativeCityId(JsonUtil.getJsonString(userinfo, "nativeCityId"));
			currentUser.setNativeCityName(JsonUtil.getJsonString(userinfo, "nativeCityName"));
			currentUser.setNativeProvinceName(JsonUtil.getJsonString(userinfo, "nativeProvinceName"));
			currentUser.setRegistTime(JsonUtil.getJsonString(userinfo, "registTime"));
			currentUser.setNation(JsonUtil.getJsonString(userinfo, "nation"));
			currentUser.setBloodType(JsonUtil.getJsonString(userinfo, "blood"));
			currentUser.setConstellation(JsonUtil.getJsonString(userinfo, "constellation"));
			currentUser.setEducationRequirement(JsonUtil.getJsonString(userinfo, "s_education"));
			currentUser.setIncomeRequirement(JsonUtil.getJsonString(userinfo, "s_income"));
			currentUser.setAgeRange(JsonUtil.getJsonString(userinfo, "s_age"));
			currentUser.setHeightRange(JsonUtil.getJsonString(userinfo, "s_height"));
			currentUser.setAreaRange(JsonUtil.getJsonString(userinfo, "s_cityId"));
		}
		return currentUser;
	}
	
	/**
	 * parse current user info
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	private UserInfo parseOtherUserInfo(String result) throws JSONException {
		UserInfo userInfo = new UserInfo();
		JSONObject obj = new JSONObject(result);
		if (obj.has("status") && "1".equals(JsonUtil.getJsonString(obj, "status"))) {
			JSONObject userinfo = JsonUtil.getJsonObject(obj, "userinfo");
			userInfo.setNickName(JsonUtil.getJsonString(userinfo, "nickName"));
			userInfo.setSex(JsonUtil.getJsonString(userinfo, "sex"));
			userInfo.setBirth(JsonUtil.getJsonString(userinfo, "birth"));
			userInfo.setAvatar(JsonUtil.getJsonString(userinfo, "avatar"));
			userInfo.setBirth(JsonUtil.getJsonString(userinfo, "birth"));
			userInfo.setWeight(JsonUtil.getJsonString(userinfo, "weight"));
			userInfo.setHeight(JsonUtil.getJsonString(userinfo, "height"));
			userInfo.setEducation(JsonUtil.getJsonString(userinfo, "education"));
			userInfo.setCarType(JsonUtil.getJsonString(userinfo, "carType"));
			userInfo.setIncome(JsonUtil.getJsonString(userinfo, "income"));
			userInfo.setWorkTrade(JsonUtil.getJsonString(userinfo, "workTrade"));
			userInfo.setJob(JsonUtil.getJsonString(userinfo, "job"));
			userInfo.setMarriage(JsonUtil.getJsonString(userinfo, "marriage"));
			userInfo.setNode(JsonUtil.getJsonString(userinfo, "node"));
			userInfo.setRegistTime(JsonUtil.getJsonString(userinfo, "registTime"));
			userInfo.setSchool(JsonUtil.getJsonString(userinfo, "school"));
			userInfo.setWordType(JsonUtil.getJsonString(userinfo, "workTrade"));
			userInfo.setHousingType(JsonUtil.getJsonString(userinfo, "housingType"));
			userInfo.setCityId(JsonUtil.getJsonString(userinfo, "cityId"));
			userInfo.setNativeCityId(JsonUtil.getJsonString(userinfo, "nativeCityId"));
			userInfo.setNativeCityName(JsonUtil.getJsonString(userinfo, "nativeCityName"));
			userInfo.setNativeProvinceName(JsonUtil.getJsonString(userinfo, "nativeProvinceName"));
			userInfo.setRegistTime(JsonUtil.getJsonString(userinfo, "registTime"));
			userInfo.setNation(JsonUtil.getJsonString(userinfo, "nation"));
			userInfo.setBloodType(JsonUtil.getJsonString(userinfo, "blood"));
			userInfo.setConstellation(JsonUtil.getJsonString(userinfo, "constellation"));
			userInfo.setEducationRequirement(JsonUtil.getJsonString(userinfo, "s_education"));
			userInfo.setIncomeRequirement(JsonUtil.getJsonString(userinfo, "s_income"));
			userInfo.setAgeRange(JsonUtil.getJsonString(userinfo, "s_age"));
			userInfo.setHeightRange(JsonUtil.getJsonString(userinfo, "s_height"));
			userInfo.setAreaRange(JsonUtil.getJsonString(userinfo, "s_cityId"));
			userInfo.setHasFollow(JsonUtil.getJsonBoolean(userinfo, "hasFollow"));
		}
		return userInfo;
	}
	
	/**
	 * parse home page user data
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	private List<UserInfo> parseHomePageData(String result) throws JSONException {
		JSONObject obj = new JSONObject(result);
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		String status = JsonUtil.getJsonString(obj, "status");
		if ("1".equals(status)) {
			JSONArray userLists = JsonUtil.getJsonArrayObject(obj, "userList");
			for (int i = 0; i < userLists.length(); i++) {
				UserInfo userInfo = new UserInfo();
				JSONObject userList = JsonUtil.getJsonObject(userLists, i);
				userInfo.setId(JsonUtil.getJsonString(userList, "uid"));
				userInfo.setNickName(JsonUtil.getJsonString(userList, "nickName"));
				userInfo.setAvatar(JsonUtil.getJsonString(userList, "avatar"));
				userInfo.setAge(JsonUtil.getJsonInt(userList, "age"));
				userInfo.setHeight(JsonUtil.getJsonString(userList, "height"));
				userInfo.setCityId(JsonUtil.getJsonString(userList, "cityId"));
				userInfo.setCityName(JsonUtil.getJsonString(userList, "cityName"));
				userInfo.setIncome(JsonUtil.getJsonString(userList, "income"));
				userInfo.setConstellation(JsonUtil.getJsonString(userList, "constellation"));
				userInfo.setEducation(JsonUtil.getJsonString(userList, "education"));
				userInfo.setSex(JsonUtil.getJsonString(userList, "sex"));
				userInfo.setLoveExponent((int) (Math.random()*60) + 40 + "");
				userInfos.add(userInfo);
			}
		}
		return userInfos;
	}
	
	/**
	 * parse live room play data
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	private List<RoomLiveListBean> parseRoomLiveListData(String result) throws JSONException {
		List<RoomLiveListBean> roomLiveLists = new ArrayList<RoomLiveListBean>();
		JSONObject obj = new JSONObject(result);
		String status = JsonUtil.getJsonString(obj, "status");
		if (null != status && "1".equals(status)) {
			JSONArray datas = JsonUtil.getJsonArrayObject(obj, "data");
			for (int i = 0; i < datas.length(); i++) {
				JSONObject data = JsonUtil.getJsonObject(datas, i);
				RoomLiveListBean liveListBean = new RoomLiveListBean();
				liveListBean.setConstellation(JsonUtil.getJsonString(data, "constellation"));
				liveListBean.setWeight(JsonUtil.getJsonString(data, "weight"));
				liveListBean.setPlayContentAudioUrl(JsonUtil.getJsonString(data, "playContent"));
				liveListBean.setCityId(JsonUtil.getJsonString(data, "cityId"));
				liveListBean.setPlayAbidanceTime(JsonUtil.getJsonString(data, "playAbidanceTime"));
				liveListBean.setWillChange(JsonUtil.getJsonString(data, "willChange"));
				liveListBean.setPlayType(JsonUtil.getJsonString(data, "playType"));
				liveListBean.setPhotoAvatar(JsonUtil.getJsonString(data, "avatar"));
				liveListBean.setEducation(JsonUtil.getJsonString(data, "education"));
				liveListBean.setHasPlay(JsonUtil.getJsonString(data, "hasPlay"));
				liveListBean.setSignupId(JsonUtil.getJsonString(data, "signupId"));
				liveListBean.setHeight(JsonUtil.getJsonString(data, "height"));
				liveListBean.setPlayTime(JsonUtil.getJsonString(data, "playTime"));
				liveListBean.setHasPayTime(JsonUtil.getJsonInt(data, "hasPayTime"));
				liveListBean.setNickName(JsonUtil.getJsonString(data, "nickName"));
				liveListBean.setUserId(JsonUtil.getJsonString(data, "userId"));
				liveListBean.setAge(JsonUtil.getJsonString(data, "age"));
				roomLiveLists.add(liveListBean);
				
			}
		}
		return roomLiveLists;
	}
	
	/**
	 * parse owner photo
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	private ArrayList<OwnerPhotoBean> parseOwnerPhoto(String result) throws JSONException {
		ArrayList<OwnerPhotoBean> photos = new ArrayList<OwnerPhotoBean>();
		JSONObject obj = new JSONObject(result);
		JSONArray photolists = JsonUtil.getJsonArrayObject(obj, "photolist");
		for (int i = 0; i < photolists.length(); i++) {
			JSONObject photolist = JsonUtil.getJsonObject(photolists, i);
			OwnerPhotoBean photoBean = new OwnerPhotoBean();
			photoBean.setPid(JsonUtil.getJsonString(photolist, "pid"));
			photoBean.setPhotoUrl(JsonUtil.getJsonString(photolist, "url"));
			photos.add(photoBean);
		}
		return photos;
	}
	
	private List<Integer> parseRoomInfo(String result) throws JSONException {
		List<Integer> times = new ArrayList<Integer>();
		JSONObject obj = new JSONObject(result);
		String status = JsonUtil.getJsonString(obj, "status");
		if ("1".equals(status)) {
			JSONObject timesLotMap = JsonUtil.getJsonObject(obj, "timeslotmap");
			for (int i = 0; i < 20; i++) {
				times.add(JsonUtil.getJsonInt(timesLotMap, "" + i));
			}
		}
		return times;
	}
	
	private List<FriendInfo> parseFriendsList(String result) throws JSONException {
		JSONObject obj = new JSONObject(result);
		JSONObject data = JsonUtil.getJsonObject(obj, "data");
		JSONArray friendLists = JsonUtil.getJsonArrayObject(data, "friendList");
		List<FriendInfo> friends = new ArrayList<FriendInfo>();
		for (int i = 0; i < friendLists.length(); i++) {
			FriendInfo friendInfo = new FriendInfo();
			JSONObject friendList = JsonUtil.getJsonObject(friendLists, i);
			friendInfo.setNickName(JsonUtil.getJsonString(friendList, "nickName"));
			friendInfo.setAge(JsonUtil.getJsonString(friendList, "age"));
			friendInfo.setLastUpdateTime(JsonUtil.getJsonLong(friendList, "last_update_time"));
			friendInfo.setMemo(JsonUtil.getJsonString(friendList, "memo"));
			friendInfo.setRelationStatus(JsonUtil.getJsonString(friendList, "relation_status"));
			friendInfo.setCreatetTime(JsonUtil.getJsonLong(friendList, "createt_time"));
			friendInfo.setAvatar(JsonUtil.getJsonString(friendList, "avatar"));
			friendInfo.setUid(JsonUtil.getJsonString(friendList, "friend_uid"));
			friends.add(friendInfo);
		}
		return friends;
	}
	
	private List<Follow> parseFollowList(String result) throws JSONException {
		List<Follow> follows = new ArrayList<Follow>();
		JSONObject obj = new JSONObject(result);
		String status = JsonUtil.getJsonString(obj, "status");
		if ("1".equals(status)) {
			JSONArray followlists = JsonUtil.getJsonArrayObject(obj, "followlist");
			for (int i = 0; i < followlists.length(); i++) {
				JSONObject followlist = JsonUtil.getJsonObject(followlists, i);
				Follow follow = new Follow();
				follow.setFuid(JsonUtil.getJsonString(followlist, "fuid"));
				follow.setNickName(JsonUtil.getJsonString(followlist, "nickName"));
				follow.setAvatar(JsonUtil.getJsonString(followlist, "avatar"));
				follow.setSex(JsonUtil.getJsonString(followlist, "sex"));
				follow.setFollowTime(JsonUtil.getJsonString(followlist, "followTime"));
				follow.setAge(JsonUtil.getJsonString(followlist, "age"));
				follow.setCityId(JsonUtil.getJsonString(followlist, "cityId"));
				follow.setCityName(JsonUtil.getJsonString(followlist, "cityName"));
				follows.add(follow);
			}
		}
		return follows;
	}
	
	private RoomMsg parseSendRoomMsg(String result) throws JSONException {
		RoomMsg roomMsg = new RoomMsg();
		JSONObject obj = new JSONObject(result);
		String status = JsonUtil.getJsonString(obj, "status");
		if ("1".equals(status)) {
			JSONObject data = JsonUtil.getJsonObject(obj, "data");
			String id = JsonUtil.getJsonString(data, "id");
			long sendTime = JsonUtil.getJsonLong(data, "sendTime");
			String msgId = JsonUtil.getJsonString(data, "msgId");
			
			roomMsg.setId(id);
			roomMsg.setSendTime(sendTime);
			roomMsg.setMsgId(msgId);
		}
		return roomMsg;
	}
	
	private int parseKuaiCoin(String result) throws JSONException {
		int coin = 0;
		JSONObject obj = new JSONObject(result);
		String status = JsonUtil.getJsonString(obj, "status");
		if ("1".equals(status)) {
			coin = JsonUtil.getJsonInt(obj, "coin");
		}
		return coin;
	}

}
