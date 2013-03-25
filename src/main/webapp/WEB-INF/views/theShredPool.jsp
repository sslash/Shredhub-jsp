
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="topHeader.jsp" />
<body>

	<jsp:include page="header.jsp" />
	<!-- Background image with insert shred over -->
	<!-- Carousel
    ================================================== -->
	<div id="myCarousel" class="carousel slide">
		<div class="carousel-inner">
			<div class="item active">
				<img src="<c:url value="/resources/images/"/>slashback.jpg" alt="">
				<div class="container">
					<div class="carousel-caption">

						<a href="#postShredModal" class="btn btn-large btn-primary"
							role="button" data-keyboard="true" data-toggle="modal"
							data-backdrop="true" data-target="#postShredModal">Shred!</a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<div class="marketing">
			<div class="modal hide fade" id="postShredModal" aria-hidden="true"
				tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">x</button>
					<h3 id="myModalLabel">Post a shred video</h3>
				</div>


				<div class="modal-body">
					<form method="post" class="form-horizontal"
						action="<c:url value="/shred/"/>${shredder.id}/postShred"
						enctype="multipart/form-data">

						<div class="control-group">
							<label class="control-label" for="inputText">Shred text</label>
							<div class="controls">
								<input type="text" id="inputText" placeholder="Text" name="text"
									autofocus="true">
							</div>
						</div>

						<div class="control-group">
							<label class="control-label" for="inputTags">Tags</label>
							<div class="controls">
								<input type="text" name="tags"
									placeholder="tag1, tag2, tag3, tag4" />
							</div>
						</div>

						<div class="control-group">
							<label class="control-label" for="inputFile">Video</label>
							<div class="controls">
								<input type="file" name="file" /> <input type="submit"
									class="btn" value="Add" />
							</div>
						</div>
					</form>
				</div>
			</div>
			<ul class='my-new-list'></ul>

			<div id="newShredsFromFanees">

				<h2>New shreds from fanees</h2>
				<div class="row-fluid">
					<!-- List of shreds -->
					<div class="row">
						<div class="span2">
							<a href="?action=nextShredSet" class="nextShred">&lsaquo;</a>
						</div>

						<c:forEach items="${fanShreds}" var="shred" varStatus="i"
							begin="0" end="3">

							<div class="span2">

								<a href="<c:url value='/shredpool'/>/showShred/${shred.id}">
									<img class="imageClipped"
									src="<c:url value="/resources/vidz/"/>${shred.thumbnailpath}" />
								</a>
								<p class="lead">${shred.description}</p>
								<p class="small">${shred.owner.username}</p>
							</div>
						</c:forEach>

						<div class="span2">
							<a href="?action=nextShredSet" class="nextShred">&rsaquo;</a>

						</div>
						<!--  row-fluid -->
					</div>
				</div>
			</div>
			<hr id="shred" class="soften">


			<c:if test="${showView==true}">
				<jsp:include page="showShred.jsp" />
			</c:if>
			
			
			
			<div id="shredNews">
				<h2>Shred News</h2>
				<br>
				<!-- List of shreds -->
				<div class="row-fluid">


					<div class="span3">
						<h5>Newest shreds from your fanees</h5>
						</br>
						<div id="newShredFromFaneeNewsItem">
							<div class="row-fluid">
								<c:forEach items="${FANEES}" var="newsItem" varStatus="i"
									begin="0" end="4">
									<div class="row-fluid">
										<div class="span4">
											<a href="<c:url value='/shredpool'/>/showShred/${newsItem.shred.id}"
												>
												<img class="imageClipped newsItemImage"
												src="<c:url value="/resources/vidz/"/>${newsItem.shred.thumbnailpath}"
												width="20" />
											</a>
										</div>
										<div class="span8">
											<p>
												<small>${newsItem.shred.description}</small>
											</p>
											<p>
												By:<a
													href="<c:url value='/shredder/'/>${newsItem.shred.owner.id}">${newsItem.shred.owner.username}</a>
											</p>
										</div>
									</div>
									</br>
								</c:forEach>
							</div>
						</div>
					</div>


					<div class="span3">
						<h5>New battleshreds from you fanees</h5>
						<div id="latesBattleShreds">
							<div class="row-fluid">
								<c:forEach items="${BATTLE_SHREDS}" var="newsItem" varStatus="i"
									begin="0" end="4">

									<p>
										${newsItem.battleShred.owner.username} added a shred
										in a  <a
											href="<c:url value='/battle/'/>${newsItem.battleId}">battle</a>
											 <small> at <fmt:formatDate type="both"
												dateStyle="long" timeStyle="long"
												value="${newsItem.battleShred.timeCreated}" />
										</small>
									</p>
									<hr id="shred" class="soften">
								</c:forEach>
							</div>
						</div>
						</br>
					</div>


					<div class="span3">
						<h5>New battles created with your fanees in</h5>
						</br>
						<div id="newBattlesFromFanees">
							<div class="row-fluid">
								<c:forEach items="${BATTLES}" var="newsItem" varStatus="i"
									begin="0" end="2">
									<div class="row-fluid">
										<div class="span5">
											<img class="imageClipped newsItemImage"
												src="<c:url value="/resources/images/profiles/"/>${newsItem.battler.shredder.profileImagePath}" />
										</div>
										<div class="span2">
											<h3>
												<a href="<c:url value='/battle/'/>${newsItem.id}">
													VS </a>
											</h3>
										</div>
										<div class="span5">
											<img class="imageClipped newsItemImage"
												src="<c:url value="/resources/images/profiles/"/>${newsItem.battlee.shredder.profileImagePath}" />
										</div>
									</div>
									<div class="row-fluid">
										<div class="span5">
											<p>${newsItem.battler.shredder.username}</p>
										</div>
										<div class="span2"></div>
										<div class="span5">
											<p>${newsItem.battlee.shredder.username}</p>
										</div>
									</div>
									</br>
								</c:forEach>
							</div>

						</div>
					</div>


					<div class="span3">
						<h5>Shredders you might dig</h5>
						<div id="shreddersYouMightLike">
							<div class="row-fluid">
								<c:forEach items="${POTENTIAL_FANEES}" var="newsItem"
									varStatus="i" begin="0" end="3">
									<div class="row-fluid">
										<div class="span4">
											<a href=""> <img class="imageClipped newsItemImage"
												src="<c:url value="/resources/images/profiles/"/>${newsItem.shredder.profileImagePath}" />
											</a>
										</div>
										<div class="span8">
											<p>
												<a href="<c:url value='/shredder/'/>${newsItem.shredder.id}">
													${newsItem.shredder.username}</a>
											</p>
											<p>
												<small>Guitars: <c:forEach var="guitar"
														items="${newsItem.shredder.guitars}" varStatus="g">
														<c:choose>
															<c:when test='${g.count > 1}'>,</c:when>
														</c:choose>
														${guitar}
													</c:forEach>
												</small>
											</p>
											<p>
												<small> Equiptment: <c:forEach var="equiptment"
														items="${newsItem.shredder.equiptment}" varStatus="e">
														<c:choose>
															<c:when test='${e.count > 1}'>,</c:when>
														</c:choose>
														${equiptment}
													</c:forEach>
												</small>
											</p>
										</div>
									</div>
									</br>
								</c:forEach>
							</div>

						</div>
						</br>
					</div>
				</div>
			</div>

			<hr id="shred" class="soften">

			<div id="topShreds">
				<h2>Shreds with high rating</h2>
				<br> <br> <br>

				<div class="row-fluid">
					<!-- List of shreds -->
					<div class="row">

						<div class="span1">
							<a href="?action=nextHighRatingShredSet" class="nextShred">&lsaquo;</a>
						</div>

						<c:forEach items="${topShreds}" var="shred" varStatus="i"
							begin="0" end="1">
							<div class="span5">

								<div class="row-fluid">
									<div class="span6">
										<a href="<c:url value='/shredpool'/>/showShred/${shred.id}"
											class="newShredsFromFaneesAncor"> <img
											class="imageClipped"
											src="<c:url value="/resources/vidz/"/>23shred1.jpg" />
										</a>
									</div>

									<div class="span6">
										<p class="lead">Rating: ${shred.rating.rating}</p>
										<p>By: ${shred.owner.username}</p>
										<p>Name: ${shred.description}</p>
										<p>
											Created at:
											<fmt:formatDate type="both" dateStyle="long" timeStyle="long"
												value="${shred.timeCreated}" />
										</p>

									</div>
								</div>
							</div>
						</c:forEach>
						<div class="span1">
							<a href="?action=nextHighRatingShredSet" class="nextShred">&rsaquo;</a>
						</div>
						<!--  row-fluid -->
					</div>
				</div>
			</div>

			<hr id="shred" class="soften">

			<div id="mightKnowShreds">
				<h2>Shreds from shredders you might know</h2>
				<br>
				<!-- List of shreds -->
				<div class="row-fluid">

					<div class="span1">
						<a href="?action=nextMightKnowShredsSet" class="nextShred">&lsaquo;</a>
					</div>

					<c:forEach items="${mightKnowShreds}" var="shred" varStatus="i"
						begin="0" end="2">

						<div class="span3">

							<a href="<c:url value='/shredpool'/>/showShred/${shred.id}"
								class="newShredsFromFaneesAncor"> <img class="imageClipped"
								src="<c:url value="/resources/vidz/"/>23shred1.jpg" />
							</a>
							<p class="lead">${shred.description}</p>
							<p class="small">${shred.owner.username}</p>
						</div>
					</c:forEach>
					<div class="span2">
						<a href="?action=nextMightKnowShredsSet&${tagShredsCount}"
							class="nextShred">&rsaquo;</a>
					</div>
					<!--  /row-fluid -->
				</div>
			</div>

			<hr id="shred" class="soften">

			<div id="shredsByTags">

				<h2>Shreds based on tags</h2>

				<div class="row-fluid">

					<div class="span4"></div>
					<div class="span4">
						<form action="<c:url value="/theShredPool/"/>?action=shredsByTags" method="GET"
							class="form-search">
							<input type="text" name="tagList"
								placeholder="Tag1, Tag2, Tag3.."
								class="input-medium search-query" />
							<!-- 	<input type="submit" class="btn btn-info btn-mini" /> -->
						</form>

					</div>

					<div class="span4">
					</div>
				</div>

				<br>
				<!-- List of shreds -->
				<div class="row-fluid">
					<div class="row">
						<c:forEach items="${tagShreds}" var="shred" varStatus="i"
							begin="0" end="17">

							<div class="span2">

								<a href="<c:url value='/shredpool'/>/showShred/${shred.id}"
									class="newShredsFromFaneesAncor"> <img class="imageClipped"
									src="<c:url value="/resources/vidz/"/>${shred.thumbnailpath}" />
								</a>
								<p class="lead">${shred.description}</p>
								<p>By: ${shred.owner.username}</p>
								<p>
									Tags:
									<c:forEach items="${shred.tags}" var="tag" varStatus="t">
										<c:choose>
											<c:when test='${t.count > 1}'>,</c:when>
										</c:choose>	
										${tag.label}
									</c:forEach>
								</p>

							</div>

							<c:choose>
								<c:when test="${i.count % 6 == 0 }">
									<c:if test="${i.count != 1}">
					</div>
					</c:if>
					<div class="row">
						</c:when>
						</c:choose>
						</c:forEach>
						<!--  row-fluid -->
					</div>
				</div>
			</div>
		</div>
<jsp:include page="footer.jsp" />
		
</body>
</html>