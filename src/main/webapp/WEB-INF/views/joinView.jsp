<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script src="${pageContext.request.contextPath }/resources/js/login.js"></script>
<meta charset="UTF-8">
<title>회원가입</title>
</head>
<body>

	<form>
		<input id="username" type="text" placeholder="username input "><br>
		<input id="email" type="text" value="${email }" readonly="readonly"> <br>
		<input id="ageGroup"  type="text" value="${ageGroup }" readonly="readonly"> <br>
		<input id="age" type="text" placeholder="age input"> <br>
		<input id="school" type="text" placeholder="school input"><br>
		<button type="button" onclick="KinesrtUser()">회원가입</button>
	</form>


</body>
</html>