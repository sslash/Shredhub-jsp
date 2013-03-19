<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script type="text/javascript">
	function acceptBattle(id) {
		var the_url = "<c:url value='/battle/'/>" + id + "/accept";
		
		$.ajax({
			  type: "PUT",
			  url: the_url
			}).done(function( msg ) {
				console.log("accepted");
			});	
	}
	
	function declineBattle(id) {
		var the_url = "<c:url value='/battle/'/>" + id + "/decline";
		
		$.ajax({
			  type: "PUT",
			  url: the_url
			}).done(function( msg ) {
				console.log("declined battle request");
			});	
	}

</script>

<!-- Navbar 
    ================================================== -->
<div id="navbar-mid" class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container">
			<button type="button" class="btn btn-navbar" data-toggle="collapse"
				data-target=".nav-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="brand" href="<c:url value="/j_spring_security_logout" />">
				Logout</a> <a class="brand" href="#">Shredhub</a>
			<div class="nav-collapse collapse">
				<ul class="nav">
					<li class=""><a href="<c:url value="/shredpool"/>">The
							Shredpool</a></li>
					<li class=""><a
						href="<c:url value="/shredder/"/><c:out value="${sessionScope.shredder.id}"/> ">Profile</a>
					<li class=""><a href="#battles">Battles</a></li>
					<li class=""><a href="<c:url value="/shredder"/>">Shredders</a></li>
					<li class=""><a href="#About">About</a></li>
					<li class=""><a href="Contact">Contact</a></li>

					<li class="">
						<p class="navbar-text pull-right">
							Logged in as <a href="#" class="navbar-link"><c:out
									value="${sessionScope.shredder.username}" /> </a>
							<!--/.nav-collapse -->
					</li>
					<li class="">
						<div class="btn-group">
							<a class="btn btn-info dropdown-toggle" data-toggle="dropdown"
								href="#"> Battle Requests! <span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<c:forEach var="req" items="${battleRequests}">
									<li>
										<p>${req.battler.shredder.username}</p>
										<a class="battleRequestModal"
										onclick="acceptBattle(${req.id});" href="#">Accept</a>
									</li>
								</c:forEach>
							</ul>
						</div>
					</li>
					
						<li class="">
						<div class="btn-group">
							<a class="btn btn-info dropdown-toggle" data-toggle="dropdown"
								href="#"> You'r fanees <span class="caret"></span>
							</a>

							<ul class="dropdown-menu">
								<c:forEach var="fan" items="${fans}">
									<li><a href="<c:url value='/shredder/'/>${fan.id}">${fan.username}</a>
									</li>
								</c:forEach>
							</ul>
						</div>
					</li>
					
					
				</ul>
			</div>
		</div>
	</div>
</div>





