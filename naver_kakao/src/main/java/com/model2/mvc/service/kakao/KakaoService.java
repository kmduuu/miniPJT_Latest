package com.model2.mvc.service.kakao;

import java.io.IOException;
import java.util.Map;

import com.model2.mvc.service.domain.User;

public interface KakaoService {

	public String getAccessToken (String authorize_code) throws IOException;
	
	public Map<String, Object> getUserInfo(String access_Token) throws Exception;
	
	public User getUser(String userId) throws Exception;
	
	public boolean checkDuplication(String userId) throws Exception;
}
