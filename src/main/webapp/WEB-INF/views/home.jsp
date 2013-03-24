<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="topHeader.jsp" />
<body>


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
				<a class="brand" href="#">Shredhub</a>
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li class=""><a href="#shredPool">The Shred pool</a></li>
						<li class="">
							<!-- TODO: change to window.user.username -->
						</li>
						<li class=""><a href="#battles">Battles</a></li>
						<li class=""><a href="#shredders">Shredders</a></li>
						<li class=""><a href="#about">About</a></li>
						<li class=""><a href="#contact">Contact</a></li>

						<li class="">
							<p class="navbar-text pull-right">
								<a href="#signInModal" role="button" class="navbar-link"
									data-keyboard="true" data-toggle="modal" data-backdrop="true"
									data-target="#signInModal">Sign in</a>
						</li>
						
						<li class="">
							<p class="navbar-text pull-right">
								<a href="#" role="button" class="navbar-link"
									data-keyboard="true" data-toggle="modal" data-backdrop="true"
									data-target="#newShredder">Register</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<!-- Big carusell -->
	<div id="myCarousel" class="carousel slide">
		<!-- Carousel items -->
		<div class="carousel-inner">

			<div class="item active">
				<img src="<c:url value="/resources/images/"/>John_Petrucci.jpg"
					width="100%" />
				<div class="container">
					<div class="carousel-caption">
						<h1>Share your shred vidz</h1>
						<p class="lead">Record and upload a video of you shredding
							away a blasting guitar lick. Get it rated by other shredders and
							add one or more tags to it, so others will see it in their shred
							pool.</p>
					</div>
				</div>
			</div>
			<div class="item">
				<img src="<c:url value="/resources/images/"/>slash.jpg" width="100%" />
				<div class="container">
					<div class="carousel-caption">
						<h1>Watch your favorite shredder</h1>
						<p class="lead">You get a shred pool that contains the latest
							shred videos from all of your favorite shredders. You can also
							get recommended shreds based on your interests.</p>
					</div>
				</div>
			</div>
			<div class="item">
				<img src="<c:url value="/resources/images/"/>morse.jpg" width="100%" />
				<div class="container">
					<div class="carousel-caption">
						<h1>Battle against another shredder</h1>
						<p class="lead">Challenge a shredder to a unique guitar
							battle. Let other shredders watch and rate your performance, and
							you will earn experience points. Points will help you level up to
							eventually become a shred master!</p>
					</div>
				</div>
			</div>
			<div class="item">
				<img src="<c:url value="/resources/images/"/>stevelukather.jpg"
					width="100%" />
				<div class="container">
					<div class="carousel-caption">
						<h1>Become the best shredder on Shredhub</h1>
						<p class="lead">Each time you post a shred or someone watches
							your shreds you gain experience points. These will help you
							become a more attractive shredder so you'll eventually become
							famous and awesome.</p>
					</div>
				</div>
			</div>
		</div>
		<!-- Carousel nav -->
		<a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a>
		<a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>
	</div>
	
		<c:if test="${showView==true}">
			<jsp:include page="showShred.jsp" />
		</c:if>



<!-- Sign in Modal -->
	<div class="modal hide fade" id="signInModal" aria-hidden="true"
		tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">x</button>
			<h3 id="myModalLabel">Sign in</h3>
		</div>
		<!-- FORM INPUT -->
		<div class="modal-body">
			<form name="f" action="<c:url value='j_spring_security_check' />" class="form-inline"  method="POST" >			  
				

				<div class="control-group">
					
					<input type="text" class="input-small" placeholder="Username"
						name="j_username"> <input type="password"
						class="input-small" placeholder="Password" name="j_password">
					<label class="checkbox"> <input type="checkbox">
						Remember me
					</label>
					<input type="submit" class="btn" value="Login">
					</div>
			</form>
		</div>
	</div> 

	<div class="container">
		<div class="marketing">
			<h1>Shredhub</h1>
			<p class="marketing-byline">Welcome to Shredhub. Take a look at
				the current top shreds</p>


			<div class="row-fluid">
				<!-- List of shreds -->
				<div class="row">

				<c:forEach var="shred" items="${topShreds}" end="8" varStatus="i">
				
				<div class="span4">						
						
							<a href="<c:url value='/'/>showShred/${shred.id}"class="newShredsFromFaneesAncor"> 
								<!-- onclick="openVideoModal(${shred.id}); return false;"-->
								
								<img class="imageClipped"
								src="<c:url value="/resources/vidz/"/>${shred.thumbnailpath}" />
							</a>
							<p class="lead">${shred.description}</p>
							<p class="small">${shred.owner.username}</p>
					</div>

					<c:choose>
						<c:when test="${i.count % 3 == 0 }">
							
							<c:choose>
								<c:when test="${ i.count != 1 }">
									</div>
								</c:when>
							</c:choose>							
							<div class="row">
						</c:when>
					</c:choose>
					
			</c:forEach>
			<!--  row-fluid -->
		</div>
	</div>
	</div>
<jsp:include page="footer.jsp" />
</body>
</html>