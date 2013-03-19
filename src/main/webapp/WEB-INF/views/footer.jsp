<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<footer>
				
<script src="<c:url value="/resources/styling/bootstrap/"/>js/jquery.js"></script>
<script	src="<c:url value="/resources/styling/bootstrap/"/>js/bootstrap-modal.js"></script>
<script	src="<c:url value="/resources/styling/bootstrap/"/>js/bootstrap-carousel.js"></script>
<script	src="<c:url value="/resources/styling/bootstrap/"/>js/bootstrap-dropdown.js"></script>
<script	src="<c:url value="/resources/styling/bootstrap/"/>js/prettify.js"></script>
<script	src="<c:url value="/resources/scripts/"/>video.js"></script> 
<script	src="<c:url value="/resources/scripts/"/>underscore.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#playVideoModal').on('hide', function () {
		$('#commentTable tbody').remove();
	});
});

// function openVideoModal(id) {
// 	var baseUrl = "<c:url value='/shred/'/>";
// 	var url = baseUrl + id;
// 	$.getJSON(url, function(data) {
// 		$('div.modal-header h2').html(data.description);
// 		$('div.modal-header h4').html(data.owner.username);
// 		var path = "<c:url value='/resources/vidz/'/>" +  data.videoPath;
// 		var videoStr = '<source src="' + path + '"type="video/mp4">'; 			
// 		$('div.modal-body video').html(videoStr);
// 		$('div.modal-footer #createdAt').html('Created at: ' + new Date(data.timeCreated).toUTCString() );
// 		$('div.modal-footer #nRaters').html('Number of raters: ' + data.rating.numberOfRaters );							
// 		$('div.modal-footer #rating').html('Rating: ' + data.rating.rating);
// 		$('#modalShredId').attr("value", data.id);
		
// 		$('#commentTable').append("<tbody>");
// 		_.each(data.shredComments, function(c, i) {
// 			$('#commentTable tbody').append('<tr><td>' + c.text + '</td>'+
// 			'<td>' + c.commenter.username + '</td>'+
// 			'<td>' + new Date(c.timeCreated).toUTCString() + '</td>'+
// 			'<td><button type="button" class="close" onClick="deleteComment(' + c.id + ', ' + data.id + ');" >x</button></td></tr>');
// 		}); 
// 		$('#commentTable').append("</tbody>");
		
// 		$('#playVideoModal').modal('show');
// 	});
// }

function deleteComment(commentId, shredId) {
	console.log("delete " + commentId + ", " + shredId);
	var baseUrl = "<c:url value='/shred/'/>"; 
	var url = baseUrl + shredId + "/?action=deleteComment&commentId=" + commentId;
	
	$.post(url, function(data) {
		$('#commentTable tbody').remove();
		$('#commentTable').append("<tbody>");
		_.each(data.shredComments, function(c, i) {
			$('#commentTable tbody').append('<tr><td>' + c.text + '</td>'+
			'<td>' + c.commenter.username + '</td>'+
			'<td>' + new Date(c.timeCreated).toUTCString() + '</td>'+
			'<td><button type="button" class="close" onClick="deleteComment(' + c.id + ', ' + data.id + ');" >x</button></td></tr>');
		}); 
		$('#commentTable').append("</tbody>");
	});
}

function rateShred(shredId, rateVal) {
	var baseUrl = "<c:url value='/shred/'/>" + shredId;
	var url = baseUrl +  "?action=rate&rating=" + rateVal;
	$.post(url, function(data) {
		$('div.modal-footer #nRaters').html('Number of raters: ' + data.rating.numberOfRaters );							
		$('div.modal-footer #rating').html('Rating: ' + data.rating.rating);
	});
}

function commentShred(shredId, commentText) {
	console.log("text: " + commentText);
	var baseUrl = "<c:url value='/shred/'/>" + shredId;

	var url = baseUrl + "/comment/?text=" +
			commentText;
	
	$.post(url, function(data) {
		var last = _.last(data.shredComments); 
			$('#commentTable tbody').append('<tr><td>' + last.text + 
                    '</td><td>' + last.commenter.username + 
                    '</td><td>' + new Date(last.timeCreated).toUTCString() + '</td>'+
			'<td><button type="button" class="close" onClick="deleteComment(' + last.id + ', ' + data.id + ');" >x</button></td></tr>');
	});
}

</script>
</footer>