<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 

<%-- [
	<c:set var="i" value="0" />
	<c:forEach var="list" items="${vldTree}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.id}"/>'<c:if test="${vldTree.size()!=i}">,</c:if>
	
	</c:forEach>
] --%>
[
     {
   		<c:set var="i" value="0" />
		<c:forEach var="list" items="${vldTree}" varStatus="sta">
		 <c:set var="i" value="${i + 1 }" />
		</c:forEach>
   		title: "vld",  key:  <c:if test="${0==i}">"vld"</c:if><c:if test="${0!=i}">"vld0"</c:if>,
          children: [
            <c:set var="i" value="0" />
			<c:forEach var="list" items="${vldTree}" varStatus="sta">
			 <c:set var="i" value="${i + 1 }" />
			 {title: "<c:out value="${list.id}"/>"}<c:if test="${vldTree.size()!=i}">,</c:if>
			</c:forEach>
          ]
        } ,
        {
        <c:set var="i" value="0" />
		<c:forEach var="list" items="${cpTree}" varStatus="sta">
		 <c:set var="i" value="${i + 1 }" />
		</c:forEach>
   		title: "cp",  key:  <c:if test="${0==i}">"cp"</c:if><c:if test="${0!=i}">"cp0"</c:if>,
          children: [
            <c:set var="i" value="0" />
			<c:forEach var="list" items="${cpTree}" varStatus="sta">
			 <c:set var="i" value="${i + 1 }" />
			 {title: "<c:out value="${list.id}"/>"}<c:if test="${cpTree.size()!=i}">,</c:if>
			</c:forEach>
          ]
        } ,
        {
        <c:set var="i" value="0" />
		<c:forEach var="list" items="${nsdTree}" varStatus="sta">
		 <c:set var="i" value="${i + 1 }" />
		</c:forEach>
   		title: "nsd",  key:  <c:if test="${0==i}">"nsd"</c:if><c:if test="${0!=i}">"nsd0"</c:if>,
          children: [
            <c:set var="i" value="0" />
			<c:forEach var="list" items="${nsdTree}" varStatus="sta">
			 <c:set var="i" value="${i + 1 }" />
			 {title: "<c:out value="${list.id}"/>"}<c:if test="${nsdTree.size()!=i}">,</c:if>
			</c:forEach>
          ]
        } ,
        {
        <c:set var="i" value="0" />
		<c:forEach var="list" items="${pnfdTree}" varStatus="sta">
		 <c:set var="i" value="${i + 1 }" />
		</c:forEach>
   		title: "pnfd",  key:  <c:if test="${0==i}">"pnfd"</c:if><c:if test="${0!=i}">"pnfd0"</c:if>,
          children: [
            <c:set var="i" value="0" />
			<c:forEach var="list" items="${pnfdTree}" varStatus="sta">
			 <c:set var="i" value="${i + 1 }" />
			 {title: "<c:out value="${list.id}"/>"}<c:if test="${pnfdTree.size()!=i}">,</c:if>
			</c:forEach>
          ]
        } ,
        {
        <c:set var="i" value="0" />
		<c:forEach var="list" items="${vduTree}" varStatus="sta">
		 <c:set var="i" value="${i + 1 }" />
		</c:forEach>
   		title: "vdu",  key:  <c:if test="${0==i}">"vdu"</c:if><c:if test="${0!=i}">"vdu0"</c:if>,
          children: [
            <c:set var="i" value="0" />
			<c:forEach var="list" items="${vduTree}" varStatus="sta">
			 <c:set var="i" value="${i + 1 }" />
			 {title: "<c:out value="${list.id}"/>"}<c:if test="${vduTree.size()!=i}">,</c:if>
			</c:forEach>
          ]
        } ,
        {
        <c:set var="i" value="0" />
		<c:forEach var="list" items="${vnfdTree}" varStatus="sta">
		 <c:set var="i" value="${i + 1 }" />
		</c:forEach>
   		title: "vnfd",  key:  <c:if test="${0==i}">"vnfd"</c:if><c:if test="${0!=i}">"vnfd0"</c:if>,
          children: [
            <c:set var="i" value="0" />
			<c:forEach var="list" items="${vnfdTree}" varStatus="sta">
			 <c:set var="i" value="${i + 1 }" />
			 {title: "<c:out value="${list.id}"/>"}<c:if test="${vnfdTree.size()!=i}">,</c:if>
			</c:forEach>
          ]
        } ,
        {
        <c:set var="i" value="0" />
		<c:forEach var="list" items="${vnffgdTree}" varStatus="sta">
		 <c:set var="i" value="${i + 1 }" />
		</c:forEach>
   		title: "vnffgd",  key:  <c:if test="${0==i}">"vnffgd"</c:if><c:if test="${0!=i}">"vnffgd0"</c:if>,
          children: [
            <c:set var="i" value="0" />
			<c:forEach var="list" items="${vnffgdTree}" varStatus="sta">
			 <c:set var="i" value="${i + 1 }" />
			 {title: "<c:out value="${list.id}"/>"}<c:if test="${vnffgdTree.size()!=i}">,</c:if>
			</c:forEach>
          ]
        } ,
     {title: "option",  key: "option",
          children: [
            {title: "security"},
            {title: "qos"}
          ]
        } 
  ]








