<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/loginCheck.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Welcome to Eluon vEPC MANO World!</title>
	<script language="JavaScript">
	<!--
	function pageMove(curPage){
		 TheForm = document.searchForm;
		 TheForm.currentPage.value = curPage;
		 TheForm.submit();
	}
	

	function checkAll(checkList, boolCheck) {
		varchkSize = 0;
		checkedCount = checkList.length;

		if (typeof (checkedCount) != "undefined") {
			for ( var i = 0; i < chkSize; i++) {
				checkList[i].checked = boolCheck;
			}
		}
	}
	
	//-->
	</script>
</head>
<body>
	<a href="<c:url value="/account/accountForm.do" />">[계정 추가]</a>

	<h2>현재 등록된 계정 정보는 다음과 같습니다.</h2>
	<hr/>

	<form name="searchForm" method="post" action="<c:out value="${contextPath}" />/account/accountList.do">
		<input type="hidden" name="currentPage" value="<c:out value="${currentPage}"/>">
		기간: <input type="text" name="registerStartDate" value="<c:out value="${registerStartDate}" />" >- <input type="text" name="registerEndDate" value="<c:out value="${registerEndDate}" />">20140101 - 20141231<br>
		관리자명: <input type="text" name="userName" value="<c:out value="${userName}" />"><br>
		관리자 전화번호: <input type="text" name="handphone" value="<c:out value="${handphone}" />">  예제: 010-1000-1000<br>
		관리자상태:   <input type="checkbox" onclick="javascript:checkAll(document.getElementsByName('gradeCodes'), this.checked);" /> 전체
					<input type="checkbox" name="gradeCodes" value="AG001" /> 슈퍼관리자
					<input type="checkbox" name="gradeCodes" value="AG002" /> 관리자
					<input type="checkbox" name="gradeCodes" value="AG003" /> 일반
		<br>
		<input type="submit">
	</form>


	<table border="0">
		<thead>
			<tr>
				<td>관리자 아이디</td>
				<td>패스워드</td>
				<td>관리자명</td>
				<td>회사</td>
				<td>부서</td>
				<td>전화번호</td>
				<td>핸드폰번호</td>
				<td>이메일</td>
				<td>등급코드</td>
				<td>등급이름</td>
				<td>등록자명</td>
				<td>등록일</td>				
			</tr>
		</thead>
		<tbody>
		<c:forEach var="accountVO" items="${accountList}">
			<tr>
				<td><a href="<c:url value="/account/viewAccount.do?userId=${accountVO.userId}"/>">${accountVO.userId }</a></td>
				<td>${accountVO.password }</td>
				<td>${accountVO.userName }</td>
				<td>${accountVO.company }</td>
				<td>${accountVO.dept }</td>
				<td>${accountVO.phone }</td>
				<td>${accountVO.handphone }</td>
				<td>${accountVO.email }</td>
				<td>${accountVO.gradeCode }</td>
				<td>${accountVO.gradeName }</td>
				<td>${accountVO.makeId }</td>
				<td>${accountVO.registerDate }</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<!--페이지 넘버 시작-->
	<c:if test="${not empty accountList}">
	<%@ include file="/WEB-INF/views/common/paging.jsp" %>
	</c:if>
	<!--페이지 넘버 끝-->
</body>
</html>