<%@ page contentType="text/html; charset=EUC-KR" %>
<%@ page pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="ko">
	
<head>
	<meta charset="UTF-8">
	
	<!-- 참조 : http://getbootstrap.com/css/   참조 -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	
	<!--  ///////////////////////// Bootstrap, jQuery CDN ////////////////////////// -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" >
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" >
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" ></script>
	<script src="https://developers.kakao.com/sdk/js/kakao.js" charset="utf-8"></script>
	<script src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
	<!--  ///////////////////////// CSS ////////////////////////// -->
	<style>
    	 body >  div.container{ 
        	border: 3px solid #D6CDB7;
            margin-top: 10px;
        }
    </style>
    
    <!--  ///////////////////////// JavaScript ////////////////////////// -->
	<script type="text/javascript" charset="UTF-8">
		//============= "로그인"  Event 연결 =============
		$( function() {
			
			$("#userId").focus();
			
			//==> DOM Object GET 3가지 방법 ==> 1. $(tagName) : 2.(#id) : 3.$(.className)
			$("button").on("click" , function() {
				var id=$("input:text").val();
				var pw=$("input:password").val();
				
				if(id == null || id.length <1) {
					alert('ID 를 입력하지 않으셨습니다.');
					$("#userId").focus();
					return;
				}
				
				if(pw == null || pw.length <1) {
					alert('패스워드를 입력하지 않으셨습니다.');
					$("#password").focus();
					return;
				}
				
				$("form").attr("method","POST").attr("action","/user/login").attr("target","_parent").submit();
			});
		});	
	
		
		/*  $(function (){
		$("a.btn-primary:nth-of-type(2)").on("click", function(){
			//$(location).attr("href","https://kauth.kakao.com/oauth/authorize?client_id=04012ee167a54fddf374766087a27fea&scope=profile_nickname,profile_image,account_email&redirect_uri=http://127.0.0.1:8080/user/kakaoLogin&response_type=code").submit();
			window.open('https://kauth.kakao.com/oauth/authorize?client_id=de7a7191e54450e6a2c21681d9443c93&scope=profile_nickname,profile_image,account_email&redirect_uri=http://192.168.0.116:8080/user/loginView.jsp&response_type=code','_blank','toolbar=no,location=no,status=no,menubar=no, scrollbars=auto,resizable=no,directories=no,width=400,height=400, top=10,left=10');
			
      	  	var email = response.kakao_account.email;
      	  	var name = response.properties.nickname;
      	 	var birthdate = response.kakao_account.birthday;
      	 	var gender = response.kakao_account.gender;
      	 	alert(email);
			});
		});   */
		
		//============= 회원가입화면이동 =============
		$( function() {
			//==> DOM Object GET 3가지 방법 ==> 1. $(tagName) : 2.(#id) : 3.$(.className)
			$("a.btn-primary:first-of-type").on("click" , function() {
				self.location = "/user/addUser"
			});
		});
		
		$( function () {
			$('.kakao-login-btn').on("click", function(){
				  //두 번째 버튼을 클릭한 경우 실행되는 코드
				  alert("클릭완료");
				Kakao.init('492090239797ebad0d3181db65216b78'); //발급받은 키 중 javascript키를 사용해준다.
		
			 Kakao.Auth.login({
				success: function (response) {
				   Kakao.API.request({
				     url: '/v2/user/me',
				      success: function (response) {
				         console.log(response)
				         alert("xiu~~")
			 	 }
			}) 
	            Kakao.Auth.authorize({
					redirectUri: 'http://192.168.0.116:8080/user/kakao'		 
				});     
			},
			fail : function(error) {
			   alert(JSON.stringfy(error));
			   alert("호날두");
			}
			      
			});
			});	
		}); 
		
		
		  /* $( function (){
			$('.Naver-login-btn').on("click", function(){
				
		        const naverLogin = new naver.LoginWithNaverId({
		            clientId: 'SLklwWhn8OnVgyPdEeSU',
		            callbackUrl: 'http://192.168.0.116:8080/user/naver',
		            isPopup: false, // 팝업 방식으로 로그인 처리
		          });    
		        
		        alert("좀 작동해라.");
		        
		        naverLogin.init();
		        naverLogin.getLoginStatus(function(status) {
		          if (status) {
		        	console.log(accessToken);
		            const accessToken = naverLogin.getAccessToken();
		            const email = naverLogin.getUserProperty('email');
		            // 로그인 성공 처리
		          } else {
		            console.log('로그인 실패');
		          }
		          
		        });
			})
		})   */
		
	</script>		
	
</head>

<body>

	<!-- ToolBar Start /////////////////////////////////////-->
	<div class="navbar  navbar-default">
        <div class="container">
        	<a class="navbar-brand" href="/index.jsp">Model2 MVC Shop</a>
   		</div>
   	</div>
   	
   	<!-- ToolBar End /////////////////////////////////////-->	
	
	<!--  화면구성 div Start /////////////////////////////////////-->
	<div class="container">
		<!--  row Start /////////////////////////////////////-->
		<div class="row">
		
			<div class="col-md-6">
					<img src="/images/logo-spring.png" class="img-rounded" width="100%" />
			</div>
	   	 	
	 	 	<div class="col-md-6">
	 	 	
		 	 	<br/><br/>
				
				<div class="jumbotron">	 	 	
		 	 		<h1 class="text-center">로 &nbsp;&nbsp;그 &nbsp;&nbsp;인</h1>

			        <form class="form-horizontal">
		  
					  <div class="form-group">
					    <label for="userId" class="col-sm-4 control-label">아 이 디</label>
					    <div class="col-sm-6">
					      <input type="text" class="form-control" name="userId" id="userId"  placeholder="아이디" autocomplete="username">
					    </div>
					  </div>
					  
					  <div class="form-group">
					    <label for="password" class="col-sm-4 control-label">패 스 워 드</label>
					    <div class="col-sm-6">
					      <input type="password" class="form-control" name="password" id="password" placeholder="패스워드" autocomplete="current-password">
					    </div>
					  </div>
					  
					  <div class="form-group">
					    <div class="col-sm-6">
					      <button type="button" class="btn btn-primary"  >로 &nbsp;그 &nbsp;인</button>
					      <a class="btn btn-primary btn" href="#" role="button">회 &nbsp;원 &nbsp;가 &nbsp;입</a>
					      <br>
					      <br>
						  <a href="#" class="kakao-login-btn">
  							<img src="/images/kakao_login_medium_narrow.png" alt="카카오 로그인 버튼">
						  </a>
						  <br>
						  <a href="https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=SLklwWhn8OnVgyPdEeSU&redirect_uri=http://192.168.0.116:8080/user/naver&state=Naver" class="Naver-login-btn">
						  <img id="naverIdLogin" src="/images/btnG_완성형.png" width="184" alt="네이버 로그인 버튼" />
						  </a>
					      
					    </div>
					  </div>
			
					</form>
			   	 </div>
			
			</div>
			
  	 	</div>
  	 	<!--  row Start /////////////////////////////////////-->
  	 	
 	</div>
 	<!--  화면구성 div end /////////////////////////////////////-->

</body>

</html>
