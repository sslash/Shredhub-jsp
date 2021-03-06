<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>

<meta charset="utf-8">
<c:choose>
	<c:when test="${sessionScope.shredder != null}">
		<title><c:out value="${sessionScope.shredder.username}" />'s
			Shredhub</title>
	</c:when>
	<c:otherwise>
		<title>Shredhub</title>
	</c:otherwise>
</c:choose>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">



<!-- STYLES -->
<script src="<c:url value="/resources/scripts/"/>jquery-min.js"></script>
<link
	href="<c:url value="/resources/styling/bootstrap/"/>css/bootstrap.min.css"
	rel="stylesheet">
<!-- <link
	href="<c:url value="/resources/styling/bootstrap/"/>css/prettify.css"
	rel="stylesheet"> -->
<link href="<c:url value="/resources/styling/bootstrap/"/>css/docs.css"
	rel="stylesheet">
<link href="<c:url value="/resources/styling/"/>video-js.min.css"
	rel="stylesheet">
<link href="<c:url value="/resources/styling/"/>main.css"
	rel="stylesheet">
<link
	href="<c:url value="/resources/styling/bootstrap/"/>css/bootstrap-responsive.min.css"
	rel="stylesheet">
</head>