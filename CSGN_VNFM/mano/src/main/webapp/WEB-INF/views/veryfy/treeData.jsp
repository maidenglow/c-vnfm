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
	 '<c:out value="${list.id}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	],

	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${veryfyList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.vendor}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${veryfyList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.descriptor_version}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${veryfyList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.number_of_endpoints}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${veryfyList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.root_requirement}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${veryfyList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.leaf_requirement}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${veryfyList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.qos}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${veryfyList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.test_access}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${veryfyList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.connection}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${veryfyList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.connectivity_type}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${veryfyList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.vld_security}"/>'<c:if test="${veryfyList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${qosList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.qos_no}"/>'<c:if test="${QosList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${qosList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.item}"/>'<c:if test="${QosList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${connectionList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.cp_no}"/>'<c:if test="${ConnectionList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${connectionList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.id}"/>'<c:if test="${ConnectionList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${securityList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.security_no}"/>'<c:if test="${securityList.size()!=i}">,</c:if>
	
	</c:forEach>
	],
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${securityList}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.item}"/>'<c:if test="${securityList.size()!=i}">,</c:if>
	
	</c:forEach>
	]
	,
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${vld}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.qos_ref_no}"/>'<c:if test="${vld.size()!=i}">,</c:if>
	
	</c:forEach>
	]
	,
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${vld}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.connection_ref_no}"/>'<c:if test="${vld.size()!=i}">,</c:if>
	
	</c:forEach>
	]
	,
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${vld}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.vld_security_ref_no}"/>'<c:if test="${vld.size()!=i}">,</c:if>
	
	</c:forEach>
	]
	,
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${vld}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.vld_no}"/>'<c:if test="${vld.size()!=i}">,</c:if>
	
	</c:forEach>
	]
	,
	<c:set var="i" value="0" />
	[
	<c:forEach var="list" items="${vld}" varStatus="sta">
	 <c:set var="i" value="${i + 1 }" />
	 '<c:out value="${list.version_no}"/>'<c:if test="${vld.size()!=i}">,</c:if>
	
	</c:forEach>
	]
]









