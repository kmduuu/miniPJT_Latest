package com.model2.mvc.web.user;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.kakao.KakaoService;
import com.model2.mvc.service.naver.NaverService;
import com.model2.mvc.service.user.UserService;

//==> 회원관리 Controller
@Controller
@RequestMapping("/user/*")
public class UserController {
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	@Autowired
	@Qualifier("kakaoServiceImpl")
	private KakaoService kakaoService;
	@Autowired
	@Qualifier("naverServiceImpl")
	private NaverService naverService;
	
	//setter Method 구현 않음
		
	public UserController(){
		System.out.println(this.getClass());
	}
	@Value("${pageunit}")
	int pageUnit;
	@Value("${pagesize}")
	int pageSize;
	
	
	
	@RequestMapping( value="addUser", method=RequestMethod.GET )
	public String addUser() throws Exception{
	
		System.out.println("/user/addUser : GET");
		
		return "redirect:/user/addUserView.jsp";
	}
	
	@RequestMapping( value="addUser", method=RequestMethod.POST )
	public String addUser( @ModelAttribute("user") User user ) throws Exception {

		System.out.println("/user/addUser : POST");
		//Business Logic
		System.out.println("/user/addUser : " + user);
		System.out.println(user.getAddr());
		System.out.println(user.getUserId());
		userService.addUser(user);
		
		return "redirect:/user/loginView.jsp";
	}
	

	@RequestMapping( value="getUser", method=RequestMethod.GET )
	public String getUser( @RequestParam("userId") String userId , Model model ) throws Exception {
		
		System.out.println("/user/getUser : GET");
		//Business Logic
		User user = userService.getUser(userId);
		// Model 과 View 연결
		model.addAttribute("user", user);
		
		return "forward:/user/getUser.jsp";
	}
	

	@RequestMapping( value="updateUser", method=RequestMethod.GET )
	public String updateUser( @RequestParam("userId") String userId , Model model ) throws Exception{

		System.out.println("/user/updateUser : GET");
		//Business Logic
		User user = userService.getUser(userId);
		// Model 과 View 연결
		model.addAttribute("user", user);
		
		return "forward:/user/updateUser.jsp";
	}

	@RequestMapping( value="updateUser", method=RequestMethod.POST )
	public String updateUser( @ModelAttribute("user") User user , Model model , HttpSession session) throws Exception{

		System.out.println("/user/updateUser : POST");
		//Business Logic
		userService.updateUser(user);
		
		String sessionId=((User)session.getAttribute("user")).getUserId();
		if(sessionId.equals(user.getUserId())){
			session.setAttribute("user", user);
		}
		
		return "redirect:/user/getUser?userId="+user.getUserId();
	}
	
	
	@RequestMapping( value="login", method=RequestMethod.GET )
	public String login() throws Exception{
		
		System.out.println("/user/logon : GET");

		return "redirect:/user/loginView.jsp";
	}
	
	@RequestMapping( value="login", method=RequestMethod.POST )
	public String login(@ModelAttribute("user") User user , HttpSession session ) throws Exception{
		
		System.out.println("/user/login : POST");
		//Business Logic
		User dbUser=userService.getUser(user.getUserId());
		
		if( user.getPassword().equals(dbUser.getPassword())){
			session.setAttribute("user", dbUser);
		}
		
		System.out.println(user+"홍길동");
		return "redirect:/index.jsp";
	}
		
	
	@RequestMapping( value="logout", method=RequestMethod.GET )
	public String logout(HttpSession session ) throws Exception{
		
		System.out.println("/user/logout : POST");
		
		session.invalidate();
		
		return "redirect:/index.jsp";
	}
	
	
	@RequestMapping( value="checkDuplication", method=RequestMethod.POST )
	public String checkDuplication( @RequestParam("userId") String userId , Model model ) throws Exception{
		
		System.out.println("/user/checkDuplication : POST");
		//Business Logic
		boolean result=userService.checkDuplication(userId);
		// Model 과 View 연결
		model.addAttribute("result", new Boolean(result));
		model.addAttribute("userId", userId);

		return "forward:/user/checkDuplication.jsp";
	}

	
	@RequestMapping( value="listUser" )
	public String listUser( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/user/listUser : GET / POST");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=userService.getUserList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/user/listUser.jsp";
	}
	
	@RequestMapping( value="kakao" , method=RequestMethod.GET)
	public String kakaoLogin(@RequestParam(value = "code", required = false) String code, HttpSession session, HttpServletRequest request) throws Exception {
		
		System.out.println("######" + code);
		
		// 카카오에서 인가 받은 코드를 이용하여 엑세스 토큰 발급 요청
		String access_Token = kakaoService.getAccessToken(code);
		System.out.println("여기는 컨트롤러."+access_Token);
		
		// 발급받은 엑세스 토큰을 이용하여 카카오 사용자 정보 요청
		Map<String, Object> userInfo = kakaoService.getUserInfo(access_Token);
		System.out.println("login Controller : " + userInfo);
		
		// 가져온 사용자 정보를 세션에 저장
		
		User user = new User();
		String id = (String) userInfo.get("id");
		System.out.println(id);
		user.setUserId(id);
		session = request.getSession();
		session.setAttribute("user", user);
		
		System.out.println(user);
		System.out.println(user.getUserId()+"즐라탄");
		
		// 로그인 후 메인 페이지로 이동
		return "redirect:/index.jsp";
		
	}
	
	@RequestMapping(value = "naver", method=RequestMethod.GET)
	public String naverLogin(@RequestParam(value = "code", required = false) String code, HttpSession session, HttpServletRequest request) throws Exception {
		
		System.out.println("####" + code+"ronaldo");
		// 네이버에서 인가받은 코드를 사용하여 엑세스 토큰 발급 요청
		String access_Token = "";
	    access_Token = naverService.getAccessToken(code);
		System.out.println("여기는 컨트롤러 " + access_Token);
		
		// 토큰으로 userInfo 요청
		Map<String, Object> userInfo = naverService.getUserInfo(access_Token);
		System.out.println("login Controller : " + userInfo);
		
		// 세션에 내 정보 저장
		User user = new User(); // 인스턴스 생성
		String id = (String) userInfo.get("id");
		System.out.println(id+"최성락호날두");
		user.setUserId(id);
		//session = request.getSession();
		session.setAttribute("user", user);
		session = request.getSession();
		System.out.println(user);
		System.out.println(user.getUserId()+"즐라탄");
		
		return "redirect:/index.jsp";
		
	}
}
