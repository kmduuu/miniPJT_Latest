package com.model2.mvc.service.naver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;

@Service
public class NaverServiceImpl implements NaverService{
	
	@Autowired
	@Qualifier("naverDao")
	private NaverDao naverDao;
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	public String getAccessToken(String authorize_code) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("111111111111111111111");
		String access_Token = "";
		String refresh_Token = "";
		System.out.println("여기서 오류 발생?");
		String url = "https://nid.naver.com/oauth2.0/token";
		
		URL obj;
		obj = new URL(url);
		
		// HTTP 연결 생성
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		// HTTP 요청 메소드 설정
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		
		// HTTP 요청에 필요한 파라미터 설정
		String postParams = "grant_type=authorization_code" + 
				"&client_id=" + "SLklwWhn8OnVgyPdEeSU" +
				"&redirect_uri=" + "http://192.168.0.116:8080/user/naver" + 
				"&code=" + authorize_code +
				"&client_secret=" + "qV43i4IrXX";
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// HTTP 요청 본문에 파라미터 추가
		System.out.println("1111111");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		System.out.println("2222222");
				
		// 결과 코드가 200이라면 성공
		int responseCode = con.getResponseCode();
		System.out.println("여기서 200 나온다. ");
		System.out.println("현재 상태 " + responseCode);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line = "";
		String result = "";
        
		while ((line = br.readLine()) != null) {
			result += line;
		}
		System.out.println("response body : " + result);
		
		
		ObjectMapper objectMapper = new ObjectMapper(); // jsonSimple 사용
		Map<String, Object> michael_jackson = objectMapper.readValue(result, Map.class);
		
		System.out.println("1234" + michael_jackson + "1234");
		
		access_Token = michael_jackson.get("access_token").toString(); // access_token 추출
		refresh_Token = michael_jackson.get("refresh_token").toString(); // refresh_token 추출
		
		System.out.println("11"+access_Token);
		System.out.println("22"+refresh_Token);
		
		
		return access_Token;
	}

	@Override
	public Map<String, Object> getUserInfo(String access_Token) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> userInfo = new HashMap<>();
		String postURL = "https://openapi.naver.com/v1/nid/me";
		
		try {
			URL url = new URL(postURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			
			conn.setRequestProperty("Authorization", "Bearer " + access_Token);
			
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
	        String result ="";

	        while ((line = br.readLine()) != null) {
				result += line;
			}
	        System.out.println("response body : " + result);

	        // [방법 1 : String 사용]
//	        String data = "{\"id}
	        
	        ObjectMapper objectMapper = new ObjectMapper();
	        Map<String, Object> elvis_presley = objectMapper.readValue(result, Map.class);
	        
	        System.out.println("5678"+elvis_presley+"5678");
	        
	        
	        Map<String, Object> properties = (Map<String, Object>) elvis_presley.get("response");
	        
	        String nickname = properties.get("name").toString();
	        String id = properties.get("id").toString().substring(0,10);
            String email = properties.get("email").toString();
	        String name = properties.get("name").toString();
            
	        userInfo.put("name", nickname);
	        userInfo.put("id", id);
	        userInfo.put("email", email);
	        
	        System.out.println("닉네임 : "+nickname);
	        System.out.println("이메일 : "+email);
	        System.out.println("아이디 : "+id);
	        System.out.println(userInfo);
	        
	    if(userService.checkDuplication(id) == true) {
	    
	    	User user = new User();
	    	user.setUserId(id);
	    	user.setEmail(email);
	    	user.setPassword(id);
	    	user.setUserName(name);
	    	naverDao.addUser(user);
	    	System.out.println("호날두"+user+"메시");
	    }
	        
		}catch (IOException exception) {
			exception.printStackTrace();
		}
		return userInfo;
	}

	@Override
	public User getUser(String userId) throws Exception {
		// TODO Auto-generated method stub
		
		return null;
	}
	public boolean checkDuplication(String userId) throws Exception {
		boolean result=true;
		User user=naverDao.getUser(userId);
		if(user != null) {
			result=false;
		}
		return result;
	}

}
