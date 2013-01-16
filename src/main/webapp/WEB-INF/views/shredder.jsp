<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="en">
<jsp:include page="topHeader.jsp" />
<body>
	<jsp:include page="header.jsp" />
	<div class="container">
		<div class="marketing">
			<h1>${currentShredder.username}</h1>
			<p class="marketing-byline">${currentShredder.description}</p>

			<div class="row-fluid">

				<div class="span6">
					<img alt="Profile image"
						src="<c:url value="/resources/images/"/>${currentShredder.profileImagePath}"
						width="300px" class="img-rounded" />
				</div>
				<div class="span6">
					<p>Level: ${currentShredder.level.level}
						(${currentShredder.level.levelTitle})</p>
					<p>Experience points: ${currentShredder.level.xp}</p>

					<!-- FAN OR BECOME FAN -->
					<p>
						<c:if test="${currentShredder.id != shredder.id}">
							<c:choose>
								<c:when test="${isFan==true}">
									<p>
										<i class="icon-ok"></i> You're a fan
									</p>
								</c:when>
								<c:otherwise>
									<form method="POST"
										action="<c:url value="/shredder/"/>${currentShredder.id}?action=follow">
										<input type="submit" class="btn btn-success"
											value="Become fan">
									</form>
								</c:otherwise>

							</c:choose>
						</c:if>
				</div>
			</div>

			<hr class="soften">
			<h2>Shred specs</h2>
			</br>

			<div class="row-fluid">
				<div class="span3">
					<p>Birthdate:</p>
				</div>

				<!--/span-->
				<div class="span9">
					<p>${currentShredder.birthdate}</p>
				</div>
				<!--/span-->
			</div>

			<div class="row-fluid">
				<div class="span3">
					<p>Country:</p>
				</div>

				<!--/span-->
				<div class="span9">
					<p>${currentShredder.country}</p>
				</div>
				<!--/span-->
			</div>

			<div class="row-fluid">
				<div class="span3">
					<p>Email:</p>
				</div>

				<!--/span-->
				<div class="span9">
					<p>${currentShredder.email}</p>
				</div>
				<!--/span-->
			</div>
			<div class="row-fluid">
				<div class="span3">
					<p>Guitar:</p>
				</div>

				<!--/span-->
				<div class="span9">
					<c:forEach var="guitar" items="${currentShredder.guitars}">
						<p>${guitar}</p>
					</c:forEach>
				</div>
			</div>
			<!-- row fluid -->

			<div class="row-fluid">
				<div class="span3">
					<p>Equiptment:</p>
				</div>

				<!--/span-->
				<div class="span9">
					<c:forEach var="eqpt" items="${currentShredder.equiptment}">
						<p>${eqpt}</p>
					</c:forEach>
				</div>
				<!-- row fluid -->
			</div>


			<hr class="soften">
			<div id="battleRelationship">

				<c:choose>
					<c:when test="${battleRelationship == 'same_shredder'}">
						<p>LOL; SAME</p>
					</c:when>
					<c:when test="${battleRelationship == 'in_battle'}">
						<h2>You're currently in a battle against
							${currentShredder.username}</h2>
						</br>
						<a href="<c:url value='/battle/'/>${battle.id}">Go to battle</a>
					</c:when>

					<c:when test="${battleRelationship == 'req_sent'}">
						<c:choose>
							<c:when
								test="${battle.battler.shredder.id == currentShredder.id }">
								<h2>${currentShredder.username} wants to battle you!</h2>
								</br>

								<a href="#battleRequestModal" class="btn btn-info" role="button"
									data-keyboard="true" data-toggle="modal" data-backdrop="true"
									data-target="#battleRequestModal">Check it out</a>

								<!--  Accept battle request modal -->
								<div id="battleRequestModalTemplate">
									<div class="modal hide fade videoView" id="battleRequestModal"
										aria-hidden="true" tabindex="10" role="dialog"
										aria-labelledby="myModalLabel">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal"
												aria-hidden="true">x</button>
											<h3 id="myModalLabel">Batle request</h3>
											<p id="loginError" class="text-error"></p>
										</div>

										<!-- FORM INPUT -->
										<div class="modal-body">
											<h4 class="lead">${currentShredder.username} says:</h4>
											<p>${battle.battleCategori.categoriText}</p>

											<video id="videoInModal" class="video-js vjs-default-skin"
												controls preload="auto" width="640">
												<source src="<c:url value="/resources/vidz/"/>23shred1.jpg"
													type='video/mp4'>
  												<source src="<c:url value="/resources/vidz/"/>23shred1.jpg"
														type='video/webm'>		
						
											</video>

											<p>Created at: ${battle.timeCreated}</p>
										</div>
										<div class="modal-footer">
											<button id="acceptBattle" class="btn btn-success"
												onclick="acceptBattle(${battle.id}); return false;">Accept!</button>
										</div>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<h2>A battle request has been sent to
									${currentShredder.username}</h2>
							</c:otherwise>
						</c:choose>




					</c:when>
					<c:otherwise>
						<h2>Challenge ${currentShredder.username} to a battle</h2>
						<a href="#battleModal" role="button" class="btn btn-success "
							data-keyboard="true" data-toggle="modal" data-backdrop="true"
							data-target="#battleModal">Battle!</a>

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

									<div class="controls">
										<p>I: Choose a battle style</p>
										<p>II: Add you first shred video</p>
										<p>III: Send the request</p>
									</div>

									<!-- Image upload -->
									<form method="POST" class="form-horizontal"
										enctype="multipart/form-data"
										action="<c:url value='/'/>battle/${currentShredder.id}/">

										<div class="control-group">
											<label class="control-label">Set battle style</label>
											<div class="controls">

												<select class="input-large" name="battleStyle">
													<option>Bet you can't shred this</option>
													<option>Shred something better in the same key</option>
													<option>Shred something better using this scale</option>
													<option>Shred faster then this</option>
												</select>
											</div>
										</div>


										<!-- Shred video -->
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


					</c:otherwise>


				</c:choose>



			</div>
			<hr class="soften">

			<h2>${currentShredder.username}'s fanees</h2>
			<div id="fanees">
				<div class="row-fluid">
					<div class="span1">
						<a class="nextShred" href="#">&lsaquo;</a>
					</div>
					<c:forEach var="fanee" items="${currentShredder.fanees}"
						varStatus="i" begin="0" end="4">
						<div class="span2">
							<a href="<c:url value='/shredder/'/>${fanee.id}"> <img
								class="imageClipped"
								src="<c:url value="/resources/images/"/>${fanee.profileImagePath}" />
							</a>
							<p>${fanee.username}</p>
						</div>
					</c:forEach>

					<div class="span1">
						<a class="nextShred" href="#">&rsaquo;</a>
					</div>
				</div>

			</div>

			<hr class="soften">
			<h2>${currentShredder.username}'s shreds</h2>
			<div id="shreds">

				<div class="row-fluid">
					<div class="span1">
						<a class="nextShred" href="#">&lsaquo;</a>
					</div>
					<c:forEach var="shred" items="${shreds}" varStatus="i" begin="0"
						end="4">
						<div class="span2">
							<a href="" onclick="openVideoModal(${shred.id}); return false;">
								<img class="imageClipped"
								src="<c:url value="/resources/vidz/"/>23shred1.jpg" />
							</a>

							<p>${shred.description}</p>
						</div>
					</c:forEach>

					<div class="span1">
						<a class="nextShred" href="#">&rsaquo;</a>
					</div>
				</div>
			</div>

			<div id="newShredsModalBox">
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

						<video id="videoInModal" class="video-js vjs-default-skin"
							controls preload="auto" width="640">

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
			</div>
		</div>
	</div>

	<footer>
		<p>&copy; Mikey 2012</p>
	</footer>



	<!-- Le javascript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script
		src="<c:url value="/resources/styling/bootstrap/"/>js/jquery.js"></script>
	<script
		src="<c:url value="/resources/styling/bootstrap/"/>js/bootstrap-modal.js"></script>
	<script
		src="<c:url value="/resources/styling/bootstrap/"/>js/bootstrap-typeahead.js"></script>
	<script
		src="<c:url value="/resources/styling/bootstrap/"/>js/prettify.js"></script>
</body>
</html>
