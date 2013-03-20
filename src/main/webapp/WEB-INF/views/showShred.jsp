		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<script type="text/javascript">
			$(document).ready(function(){
				$('#playVideoModal').modal('show');
			});
		</script>
		<div class="modal hide fade videoView" id="playVideoModal"
				aria-hidden="false" data-show="true" tabindex="10" role="dialog"
				aria-labelledby="myModalLabel">
				<input type="hidden" id="modalShredId" value=""></input>

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">x</button>

					<h2>${currShred.description}</h2>
					<h4>${currShred.owner.username}</h4>
				</div>

				<div class="modal-body">
					<!-- video.js --> 

					<video id="videoInModal" class="video-js vjs-default-skin" controls
						preload="auto" width="640">
						
						<source src="<c:url value='/resources/vidz/'/>${currShred.videoPath}" type="video/mp4"> 		

					</video>
				</div>

				<div class="modal-footer">
					<div class="videoDetailsTmpl">
						<div class="row-fluid">
							<div class="span6">
								<p>
									<small id="createdAt"> Created at: </small>
								</p>
								<p class="lead" id="nRaters">Number of raters: ${currShred.rating.numberOfRaters}</p>
								<p class="lead" id="rating">Rating: ${currShred.rating.rating}</p>

								<p class="small">
									Rate it: <input type="range" id="rateValue" min="0" max="10"
										name="rating" value="5">
									<button id="rateButton" class="btn btn-small btn-primary"
										onclick="rateShred(${currShred.id}, $('#rateValue').val()); return false;">\m/</button>
								</p>
								<input type="text" name=text id="shredCommentText"
									placeholder="Leave a comment!">
								<button id="commentButton" class="btn btn-small btn-primary"
									onclick="commentShred(${currShred.id},$('#shredCommentText').val()); return false;">Submit</button>
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
									<tbody>
										<c:forEach items="${currShred.shredComments}" var="c">
											<tr>
												<td> ${c.text} </td>
												<td> ${c.commenter.username} </td>
												<td> ${c.timeCreated} </td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
