package me.kkuai.kuailian.http.response;

import me.kkuai.kuailian.user.UserInfo;

import com.kkuai.libs.net.response.J_Response;

public class LoginResponse extends J_Response {

	public UserInfo userInfo;
	public String token;
}
