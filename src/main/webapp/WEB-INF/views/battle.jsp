<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="en">
<jsp:include page="topHeader.jsp" />
<style>
body {
	padding-top: 0px;
	/* 60px to make the container go all the way to the bottom of the topbar */
}
</style>
<script type="text/javascript">
function showValue(newValue, postfixId)
{
	var id = "range" + postfixId;
	document.getElementById(id).innerHTML=newValue;
}
function openCommentBox(id) {
	var tableId="#commentBox" + id;	
	if ( $(tableId).is(":visible")) {
		$(tableId).hide();
	} else {
		$(tableId).show();
	}
}
</script>

<body data-spy="scroll" data-target=".bs-docs-sidebar">
	<jsp:include page="header.jsp" />

	<header class="jumbotron subhead" id="overview">
		<div class="container">
			<h1>Battle</h1>
			<p class="lead">${battle.battler.shredder.username} VS
				${battle.battlee.shredder.username}</p>

			<p class="muted">${battle.battleCategori.categoriText} (Round
				${battle.round})</p>
				
			<!-- Display who's the leader -->
			<c:choose>
								<c:when test='${battle.battler.battlePoints > battle.battlee.battlePoints}'>
									<p> ${battle.battler.shredder.username} Leads </p>
								</c:when>
								<c:when test='${battle.battlee.battlePoints > battle.battler.battlePoints}'>
									<p> ${battle.battlee.shredder.username} Leads </p>
								</c:when>								
								<c:otherwise>
									<p> It's even </p>
								</c:otherwise>
							</c:choose>
		</div>
	</header>
	<p></p>

	<div class="container-fluid">
		<div class="row-fluid">
			<!--  Span 2 -->
			<jsp:include page="sidenav.jsp" />
			<div class="span10">

				<div class="span6">
					<h1 class="lead">${battle.battler.shredder.username}'s Shreds</h1>
					<p class ="lead"> Battle points: ${battle.battler.battlePoints}</p> 
				</div>
				<div class="span6">
					<h1 class="lead">${battle.battlee.shredder.username}'s Shreds</h1>
					<p class ="lead"> Battle points: ${battle.battlee.battlePoints} </p>
				</div>

				<!-- VIDEOS -->
				<div class="row-fluid">
					<div class="span6">
					
					<!--  For all shreds that belongs to the battler, on the left hand side -->
						<c:forEach var="shred" items="${battle.battler.shreds}">
							<legend>Round ${shred.round}</legend>
							
							<!-- Avoid getting divide by zero error -->
							<c:choose>
								<c:when test='${shred.rating.numberOfRaters > "0"}'>
									<c:set var="rate"
										value="${shred.rating.currentRating/shred.rating.numberOfRaters}" />
								</c:when>
								<c:otherwise>
									<c:set var="rate" value="0" />
								</c:otherwise>
							</c:choose>

							<!-- video.js -->
							<video id="my_video_1" class="video-js vjs-default-skin" controls
								preload="auto" poster="" data-setup="{}">
								<source
									src="<c:url value="/resources/images/"/>${shred.videoPath}"
									type='video/mp4'>
							</video>
							
							<form action="<c:url value='/shred/'/>${shred.id}?action=rate"
								method="POST">
								<p class="lead">Rating: ${rate}.</p>
								<p>
									Rate it: <input type="range" min="0" max="10" name="rating"
										value="5" onchange="showValue(this.value, ${shred.id})" /> <span
										id="range${shred.id}">5</span>
									<button type="submit" class="btn btn-success btn-small">Ok</button>
								</p>
							</form>
							<p>
								<a class="btn " href="#" onclick="openCommentBox(${shred.id});">
									Comments </a>
							</p>

							<div id="commentBox${shred.id}" hidden="true">
								<table class="table table-condensed">
									<tr>
										<th>Text</th>
										<th>By</th>
										<th>At</th>
									</tr>

									<c:forEach var="comment" items="${shred.shredComments}">
										<tr>
											<td>${comment.text}</td>
											<td>${comment.commenter.username}</td>
											<td>${comment.timeCreated}</td>
										<tr>
									</c:forEach>
								</table>

								<!-- COMMENT -->
								<form id="comment"
									action="<c:url value='/shred/${shred.id}/comment/'/>"
									method="POST">
									<input id="text" type="text" name=text
										placeholder="Leave a comment!"> <input
										hidden="shredderId" name="shredderId" value="${shredder.id}" />
									<button type="submit" class="btn btn-primary">Submit</button>
								</form>
							</div>
						</c:forEach>
						
				<!-- If the currently logged in shredder is next up to shred, -->
				<!-- And he is the battler in the battle, meaning he is on the left side of the screen --> 
				<!-- Then display a button to upload a shred -->
				<c:if test="${(battle.currentBattler.id == shredder.id) && (battle.battler.shredder.id == shredder.id)}">
					<div class="span6">
						<div class="hero-unit">
							<a href="#battleModal" role="button"
								class="btn btn-success btn-large" data-keyboard="true"
								data-toggle="modal" data-backdrop="true"
								data-target="#battleModal">Shred back!</a>

							<!-- Battle modal -->
							<div class="modal hide fade" id="battleModal" aria-hidden="true"
								tabindex="-1" role="dialog" aria-labelledby="#battleModal">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
										aria-hidden="true">x</button>
									<h3 id="myModalLabel">Battle shredder</h3>
								</div>


								<div class="modal-body">

									<div class="control-group">

										<!-- video upload -->
										<form method="POST" class="form-horizontal"
											enctype="multipart/form-data"
											action="<c:url value='/battle'/>/${battle.id}/shred/${battle.currentBattler.id}/${fn:length(battle.battler.shreds) + 1}">
											<div class="control-group">
												<label class="control-label" for="shredVideo">Shred
													video</label>

												<div class="controls">
													<input name="shredVideo" type="file" placeholder="Browse" />
													<input type="hidden" name="shredderId"
														value="${shredder.id}">
												</div>
											</div>

											<div class="modal-footer">
												<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
												<button class="btn btn-primary">Enter</button>
											</div>

										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:if>
						<!-- Span 6 -->
					</div>


					<div class="span6">
					<!--  For all shreds that belongs to the battlee, on the right hand side -->
						<c:forEach var="shred" items="${battle.battlee.shreds}">
							<legend> Round ${shred.round}</legend>

							<c:choose>
								<c:when test='${shred.rating.numberOfRaters > "0"}'>
									<c:set var="rate"
										value="${shred.rating.currentRating/shred.rating.numberOfRaters}" />
								</c:when>
								<c:otherwise>
									<c:set var="rate" value="0" />
								</c:otherwise>
							</c:choose>

							<!-- video.js -->
							<video id="my_video_1" class="video-js vjs-default-skin" controls
								preload="auto" poster="" data-setup="{}">
								<source
									src="<c:url value="/resources/images/"/>${shred.videoPath}"
									type='video/mp4'>
  							<source
										src="<c:url value="/resources/images/"/>${shred.videoPath}"
										type='video/webm'>
						
						
							</video>


							<form action="<c:url value='/shred/'/>${shred.id}?action=rate"
								method="POST">
								<p class="lead">Rating: ${rate}.</p>
								<p>
									Rate it: <input type="range" min="0" max="10" name="rating"
										value="5" onchange="showValue(this.value, ${shred.id})" /> <span
										id="range${shred.id}">5</span>
									<button type="submit" class="btn btn-success btn-small">Ok</button>
								</p>
							</form>
							<p>
								<a class="btn " href="#" onclick="openCommentBox(${shred.id});">
									Comments </a>
							</p>
							<script type="text/javascript">
								function openCommentBox(id) {
									var tableId="#commentBox" + id;								
									$(tableId).show();
								}
							</script>

							<div id="commentBox${shred.id}" hidden="true">
								<table class="table table-condensed">
									<tr>
										<th>Text</th>
										<th>By</th>
										<th>At</th>
									</tr>

									<c:forEach var="comment" items="${shred.shredComments}">
										<tr>
											<td>${comment.text}</td>
											<td>${comment.commenter.username}</td>
											<td>${comment.timeCreated}</td>
										<tr>
									</c:forEach>
								</table>

							<!-- COMMENT -->
							<form id="comment"
								action="<c:url value='/shred/${shred.id}/comment/'/>"
								method="POST">
								<input id="text" type="text" name=text
									placeholder="Leave a comment!"> <input
									hidden="shredderId" name="shredderId" value="${shredder.id}" />
								<button type="submit" class="btn btn-primary">Submit</button>
							</form>
							
							</div>
						</c:forEach>
				
				<!-- If the currently logged in shredder is next up to shred, -->
				<!-- And he is the battlee in the battle, meaning he is on the right side of the screen --> 
				<!-- Then display a button to upload a shred -->
				<c:if test="${(battle.currentBattler.id == shredder.id) && (battle.battlee.shredder.id == shredder.id)}">
					<div class="span6">
						<div class="hero-unit">
							<a href="#battleModal" role="button"
								class="btn btn-success btn-large" data-keyboard="true"
								data-toggle="modal" data-backdrop="true"
								data-target="#battleModal">Shred back!</a>

							<!-- Battle modal -->
							<div class="modal hide fade" id="battleModal" aria-hidden="true"
								tabindex="-1" role="dialog" aria-labelledby="#battleModal">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
										aria-hidden="true">x</button>
									<h3 id="myModalLabel">Battle shredder</h3>
								</div>


								<div class="modal-body">

									<div class="control-group">

										<!-- video upload -->
										<form method="POST" class="form-horizontal"
											enctype="multipart/form-data"
											action="<c:url value='/battle'/>/${battle.id}/shred/${battle.currentBattler.id}/${fn:length(battle.battlee.shreds) +1}">
											<div class="control-group">
												<label class="control-label" for="shredVideo">Shred
													video</label>

												<div class="controls">
													<input name="shredVideo" type="file" placeholder="Browse" />
													<input type="hidden" name="shredderId"
														value="${shredder.id}">
												</div>
											</div>

											<div class="modal-footer">
												<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
												<button class="btn btn-primary">Enter</button>
											</div>

										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:if>
						<!-- Span 6 -->
					</div>

					<!-- row-fluid -->
				</div>

				<!--  End span 10 -->
			</div>

			<!--  End row-fluid -->
		</div>
		<!--  end container -->
	</div>

	<footer>
		<p>&copy; Shredhub 2012</p>
	</footer>

	<!--/.fluid-container-->


	<!-- Le javascript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->

	<script
		src="<c:url value="/resources/styling/bootstrap/"/>js/jquery.js"></script>
	<script
		src="<c:url value="/resources/styling/bootstrap/"/>js/bootstrap-modal.js"></script>
	<script
		src="<c:url value="/resources/styling/bootstrap/"/>js/bootstrap-dropdown.js"></script>
	<script
		src="<c:url value="/resources/styling/bootstrap/"/>js/bootstrap-typeahead.js"></script>
	<script
		src="<c:url value="/resources/styling/bootstrap/"/>js/prettify.js"></script>
</body>
</html>