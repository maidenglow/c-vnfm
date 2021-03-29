<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 

[
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${veryfyList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.security_no}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	],

	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${veryfyList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.item}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	]
]








