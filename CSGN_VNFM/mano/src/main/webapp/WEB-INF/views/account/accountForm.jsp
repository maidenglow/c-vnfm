<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Welcome to Eluon vEPC MANO World!</title>
	<c:if test="${ex != null}">
		<script type="text/javascript">
		<!--
		alert("${ex.message}");
		//-->
		</script>
	</c:if>
	<script type="text/javascript">
	function goHome(){
		location.href="<c:url value='/account/accountForm.do'/>";
	}
	</script>
</head>
<body>
	<h2>계정 정보를 입력하세요</h2>
	<hr />

	<form action="<c:url value="/account/accountForm.do"/>" method="post">
		<table border="0">
			<tr>
				<td>아이디 : </td>
				<td><input type="text" name="userId" value="${accountVO.userId}" /></td>
			</tr>
			<c:if test="${not empty accountVO.userId}">
				<tr>
					<td><input type="hidden" name="updateFlag" value="Y" /></td>
				</tr>
			</c:if>
			<tr>
				<td>패스워드 : </td>
				<td><input type="text" name="password" value="${accountVO.password}" /></td>
			</tr>
			<tr>
				<td>이름 : </td>
				<td><input type="text" name="userName" value="${accountVO.userName}" /></td>
			</tr>
			<tr>
				<td>회사 : </td>
				<td><input type="text" name="company" value="${accountVO.company}" /></td>
			</tr>
			<tr>
				<td>부서 : </td>
				<td><input type="text" name="dept" value="${accountVO.dept}" /></td>
			</tr>
			<tr>
				<td>전화번호 : </td>
				<td><input type="text" name="phone" value="${accountVO.phone}" /></td>
			</tr>
			<tr>
				<td>핸드폰번호 : </td>
				<td><input type="text" name="handphone" value="${accountVO.handphone}" /></td>
			</tr>
			<tr>
				<td>이메일 : </td>
				<td><input type="text" name="email" value="${accountVO.email}" /></td>
			</tr>
			<c:choose>
			<c:when test="${not empty accountVO.userId}">
			<tr>
				<td>관리자등급 :</td>
				<td>
					<select name="gradeCode">
						<c:forEach var="accountGradeVO" items="${accountGradeList}">
							<option <c:if test="${accountGradeVO.gradeCode == accountVO.gradeCode}">selected</c:if> value="<c:out value="${accountGradeVO.gradeCode}"/>"><c:out value="${accountGradeVO.gradeName}"/></option>									
						</c:forEach>
					</select>
				</td>
			</tr>

			</c:when>
			<c:otherwise>			

			<tr>
				<td>관리자등급 :</td>
				<td>
					<select name="gradeCode">
						<c:forEach var="accountGradeVO" items="${accountGradeList}">
							<option <c:if test="${accountGradeVO.gradeCode == accountVO.gradeCode}">selected</c:if> value="<c:out value="${accountGradeVO.gradeCode}"/>"><c:out value="${accountGradeVO.gradeName}"/></option>									
						</c:forEach>
					</select>
				</td>
			</tr>
			
			</c:otherwise>
			</c:choose>


		</table>
		<input type="submit" value="등록" />&nbsp; <input type="button" value="재작성" onclick="goHome();"> <br />
		<a href="<c:url value="/account/accountList.do" />">목록으로...</a>
	</form>
</body>
</html>