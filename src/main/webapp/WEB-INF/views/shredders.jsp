<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<jsp:include page="topHeader.jsp" />
<body>
<jsp:include page="header.jsp" />

	<div class="container">
	<div class="marketing">
		<h1>Shredders</h1>
		
		<br>  
		<input type="text" id="shredderSearch"
			class="input-medium search-query" placeholder="Search for a shredder">
		
		<p class="text-error"></p>
		<br>
		<hr>  
	<c:forEach items="${shredders}" var="shredder" varStatus="i">
		<div class="row-fluid">
			<div class="span2">
				<img src="<c:url value="/resources/images/profiles/"/>${shredder.profileImagePath}"
					alt="Profile img" width="304">
				<!--/span2-->
			</div>
			<div class="span10">
				<a href="<c:url value='/shredder/'/>${shredder.id}">${shredder.username}</a>
				<p class="lead">${shredder.description}</p>
				<p class="lead">Level: ${shredder.level.level}</p>
				<a href="<c:url value="/shredder/"/>${shredder.id}/?action=follow">Become fan</a>

				<!-- /span10 -->
			</div>
			<!-- /row-fluid -->
		</div>
		<hr>
		</c:forEach>
		<!-- /marketing -->
		<a href="<c:url value='/shredder/'/>nextPage">Next</a>
	</div>
	<!-- /container -->
</div>
<hr>
<jsp:include page="footer.jsp" />
</body>
</html>