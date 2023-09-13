package com.model2.mvc.service.kakao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.kakao.KakaoDao;
import com.model2.mvc.service.user.UserDao;
import com.model2.mvc.service.user.UserService;

@Service
public class KakaoServiceImpl implements KakaoService{
	
	@Autowired
	@Qualifier("kakaoDao")
	private KakaoDao kakaoDao;
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	public void KakaoDao(KakaoDao kakaoDao) {
		this.kakaoDao = kakaoDao;
	}
	
	public String getAccessToken (String authorize_code) throws IOException{ 
		System.out.println("시바");
		String access_Token = "";
		String refresh_Token = "";
		String url = "https://kauth.kakao.com/oauth/token";
		System.out.println("포트포워딩");
		URL obj;
		obj = new URL(url);
		
		
		// HTTP 연결 생성
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		// HTTP 요청 메소드 설정
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		
		// HTTP 요청에 필요한 파라미터 설정
		String postParams = "grant_type=authorization_code" +
			"&client_id=" + "492090239797ebad0d3181db65216b78" +
			"&redirect_uri=" + "http://192.168.0.116:8080/user/kakao" +
			"&code=" + authorize_code;
		// HTTP 요청 헤더 설정
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// HTTP 요청 본문에 파라미터 추가
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		
		// 결과 코드가 200이라면 성공
		int responseCode = con.getResponseCode();
		System.out.println("responseCode : " + responseCode);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line = "";
		String result = "";
        
		while ((line = br.readLine()) != null) {
			result += line;
		}
		System.out.println("response body : " + result);
        
		// JSON 파싱
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> michael_jackson = objectMapper.readValue(result, Map.class);
		
		System.out.println("1234" + michael_jackson + "1234");
		
		access_Token = michael_jackson.get("access_token").toString(); // access_token 추출
		refresh_Token = michael_jackson.get("refresh_token").toString(); // refresh_token 추출

		System.out.println("11"+access_Token);
		System.out.println("22"+refresh_Token);
		
		return access_Token; // access_token으로 돌린다?	
}
	@Override
	public HashMap<String, Object> getUserInfo(String access_Token) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> userInfo = new HashMap<>();
		String postURL = "https://kapi.kakao.com/v2/user/me";
		
		try {
			URL url = new URL(postURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			
			conn.setRequestProperty("Authorization", "Bearer " + access_Token); // Bearer �쓣�뼱�벐湲� �븞�븯硫� 401�쑙 .
			
			int responseCode = conn.getResponseCode();
			System.out.println("responsecode : " + responseCode);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String line = "";
	        String result ="";

	        while ((line = br.readLine()) != null) {
				result += line;
			}
	        System.out.println("response body : " + result);

	        
	        byte[] utf8Bytes = result.getBytes("EUC-KR");
	        String eucKrStr = new String(utf8Bytes, Charset.forName("UTF-8"));

	        System.out.println(eucKrStr+"99999");
	        
//	        String nickname = properties.getAsJsonObject().get("nickname").getAsString();
//	        String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

//	        userInfo.put("nickname", nickname);
//	        userInfo.put("email", email);
			
	        ObjectMapper objectMapper = new ObjectMapper();
	        Map<String, Object> elvis_presley = objectMapper.readValue(result, Map.class);
	        
	        System.out.println("5678"+elvis_presley+"5678");
	        
	        
	        Map<String, Object> properties = (Map<String, Object>) elvis_presley.get("properties");
	        Map<String, Object> kakaoAccount = (Map<String, Object>) elvis_presley.get("kakao_account");
	        Object userid = elvis_presley.get("id");
	        
	        System.out.println(userid);
//	        String id = (String) userid;
//	        System.out.println(id);
	        
	        String id = String.valueOf(userid);
	        System.out.println(id);
	        
       
	        String nickname = properties.get("nickname").toString();
	        String email = kakaoAccount.get("email").toString();
        
	        userInfo.put("nickname", nickname);
	        userInfo.put("email", email);
	        userInfo.put("id", id);

	        System.out.println("닉네임 : "+nickname);
	        System.out.println("이메일 : "+email);
	        System.out.println("아이디 : "+id);
	        System.out.println(userInfo);
	        
	        System.out.println(userService.checkDuplication(id)+"22222222222222222222222222");
	        if(userService.checkDuplication(id)==true) {
	        	System.out.println(id);
	        	User user =  new User();
	        	user.setUserId(id);
	        	user.setEmail(email);
	        	user.setPassword(id);
	        	user.setUserName(nickname);
	        	System.out.println("00"+user+"00");
	        	kakaoDao.addUser(user);
	        }
	        
	        
	        
		} catch (IOException exception) {
			
			exception.printStackTrace();
			// TODO: handle exception
		}
		
		return userInfo;
		
	}
	@Override
	public User getUser(String userId) throws Exception {
		// TODO Auto-generated method stub
		User user = kakaoDao.getUser(userId);
		return user;
	}

	@Override
	public boolean checkDuplication(String userId) throws Exception {
		boolean result=true;
		User user=kakaoDao.getUser(userId);
		if(user != null) {
			result=false;
		}
		return result;
	}

}
