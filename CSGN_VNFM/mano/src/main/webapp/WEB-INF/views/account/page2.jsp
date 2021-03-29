<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ page session="false" %>

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
	<title>Welcome to SkbPush World!</title>
</head>
<body>
<h2>Session 사용</h2>
<p>세션 이름 : <b>${sessionScope.ACCOUNTInfo.userName}</b></p>
<P>페이지 상단에 <%@ page session="false" %> 선언함</P>
</body>
</html>