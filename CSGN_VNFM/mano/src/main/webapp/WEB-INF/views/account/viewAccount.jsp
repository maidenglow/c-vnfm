<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/loginCheck.jsp"%>

<c:if test="${ex != null}">
	<script type="text/javascript">
	<!--
		alert(${e.message});
	//-->
	</script>
</c:if>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Welcome to SKB PUSH World!</title>
</head>
<body>
	<a href="<c:url value="/account/accountForm.do?userId=${accountVO.userId}" />">수정</a>  <a href="<c:url value="/account/deleteAccount.do?userId=${accountVO.userId}" />">삭제</a>  <a href="<c:url value="/account/accountList.do" />">목록으로...</a>
	<h2>계정 정보는 다음과 같습니다.</h2>
	<hr />

	<table border="0">
		<tr>
			<td>관리자 아이디 : </td>
			<td>${accountVO.userId }</td>
		</tr>
		<tr>
			<td>패스워드 : </td>
			<td>${accountVO.password }</td>
		</tr>
		<tr>
			<td>관리자명 : </td>
			<td>${accountVO.userName }</td>
		</tr>
		<tr>
			<td>회사 : </td>
			<td>${accountVO.company }</td>
		</tr>
		<tr>
			<td>부서 : </td>
			<td>${accountVO.dept }</td>
		</tr>
		<tr>
			<td>전화번호 : </td>
			<td>${accountVO.phone }</td>
		</tr>
		<tr>
			<td>핸드폰번호 : </td>
			<td>${accountVO.handphone }</td>
		</tr>
		<tr>
			<td>이메일 : </td>
			<td>${accountVO.email }</td>
		</tr>
		<tr>
			<td>등급코드 : </td>
			<td>${accountVO.gradeCode }</td>
		</tr>
		<tr>
			<td>등급이름 : </td>
			<td>${accountVO.gradeName }</td>
		</tr>
		<tr>
			<td>관리자상태 : </td>
			<td>${accountVO.status }</td>
		</tr>
		<tr>
			<td>등록자명 : </td>
			<td>${accountVO.makeId }</td>
		</tr>
		<tr>
			<td>등록일 : </td>
			<td>${accountVO.registerDate }</td>
		</tr>
		<tr>
			<td>업데이트한 사람 : </td>
			<td>${accountVO.updateUser }</td>
		</tr>
		<tr>
			<td>업데이트일 : </td>
			<td>${accountVO.updateDate }</td>
		</tr>
		<tr>
			<td>최근접속한날짜 : </td>
			<td>${accountVO.lastLoginDate }</td>
		</tr>
	</table>
</body>
</html>