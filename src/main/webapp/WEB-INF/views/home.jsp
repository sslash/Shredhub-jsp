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
	
		<div class="modal hide fade videoView" id="playVideoModal"
				aria-hidden="true" tabindex="10" role="dialog"
				aria-labelledby="myModalLabel">
				<input type="hidden" id="modalShredId" value=""></input>

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">x</button>

					<h2>description</h2>
					<h4>username</h4>
				</div>

				<div class="modal-body">
					<!-- video.js -->

					<video id="videoInModal" class="video-js vjs-default-skin" controls
						preload="auto" width="640">

					</video>
				</div>

				<div class="modal-footer">
					<div class="videoDetailsTmpl">
						<div class="row-fluid">
							<div class="span6">
								<p>
									<small id="createdAt"> Created at: </small>
								</p>
								<p class="lead" id="nRaters">Number of raters:</p>
								<p class="lead" id="rating">Rating:</p>

								<p class="small">
									Rate it: <input type="range" id="rateValue" min="0" max="10"
										name="rating" value="5">
									<button id="rateButton" class="btn btn-small btn-primary"
										onclick="rateShred($('#rateValue').val()); return false;">\m/</button>
								</p>
								<input type="text" name=text id="shredCommentText"
									placeholder="Leave a comment!">
								<button id="commentButton" class="btn btn-small btn-primary"
									onclick="commentShred($('#shredCommentText').val()); return false;">Submit</button>
							</div>

							<div class="span6">
								<p class="lead">Comments</p>
								<table class="table table-condensed" id="commentTable">
									<thead>
										<tr>
											<th>Text</th>
											<th>By</th>
											<th>At</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>



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
			<form name="f" class="form-inline" action="loginShredder" method="POST"> 
				<!-- action="<c:url value='j_spring_security_check' />" method="POST"> -->
				

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
						
							<a href="#shredPool"
								onclick="openVideoModal(${shred.id}); return false;"
								class="newShredsFromFaneesAncor"> <img class="imageClipped"
								src="<c:url value="/resources/vidz/"/>23shred1.jpg" />
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

</body>
</html>