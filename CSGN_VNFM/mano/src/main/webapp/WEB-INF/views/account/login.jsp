<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>

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
	<title>Welcome to Eluon vEPC MANO World!</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty sessionScope.accountInfo}">
        <h2>로그인 성공 </h2>
        이름 : ${sessionScope.accountInfo.userName}
 
        이메일 : <c:out value="${sessionScope.accountInfo.email}"/> 
        <a href="logout.do">로그아웃</a>
        <a href="page1.do">페이지1</a>  <a href="page2.do">페이지2</a>
    </c:when>
    <c:otherwise>
        <h2>로그인 </h2>
        <form name="form1" method="post" action="loginForm.do">
        <table>
            <tr height="40px">
                <td>유저ID</td>
                <td><input type="text" name="userId"></td>
            </tr>
            <tr height="40px">
                <td>패스워드</td>
                <td><input type="password" name="password"></td>
            </tr>
			<c:if test="${not empty errorMessage}">
				<tr height="40px">
					<td><b><font color="red">로그인 에러: </font></b></td>
					<td>
						<b><font color="red"> <c:out value="${errorMessage}" /></font></b>
					</td>
				</tr>
			</c:if>
        </table>
        <table>
            <tr>
                <td align="center"><input type="submit" value="로그인"></td>
                <td align="center"><input type="reset" value="리셋"></td>
            </tr>
        </table>
        <table>
            <tr>
                <td><a href="accountForm.do">계정요청</a></td>
            </tr>
        </table>
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>