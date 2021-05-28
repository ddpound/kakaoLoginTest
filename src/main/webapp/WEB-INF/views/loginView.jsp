<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>카카오 로그인 테스트</title>
</head>
<body>

	<form style="border: 5px black solid; width: 700px; height: 400px;"
		action="">
		<input type="text" placeholder="input Id"> <br> <input type="password" placeholder="input password"> <br> 
		<a href="https://kauth.kakao.com/oauth/authorize?client_id=a924c282a86092b8472e6c2885aafe4a&redirect_uri=http://localhost:8080/auth/kakao/callback&response_type=code">
			<img style="margin-top: 10px;" width="200px" src="${pageContext.request.contextPath }/resources/image/kakaoLogin.png">
		</a>
	</form>



</body>
</html>