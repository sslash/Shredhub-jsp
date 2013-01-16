$(document).ready(function() {
		$('#playVideoModal').on('hide', function () {
			$('#commentTable tbody').remove();
		});
	});

function openVideoModal(id) {
		var baseUrl = "<c:url value='/shred/'/>";
		var url = baseUrl + id;
		$.getJSON(url, function(data) {
			$('div.modal-header h2').html(data.description);
			$('div.modal-header h4').html(data.owner.username);
			var path = "<c:url value='/resources/images/'/>" +  data.videoPath;
			var videoStr = '<source src="' + path + '"type="video/mp4">'; 			
			$('div.modal-body video').html(videoStr);
			$('div.modal-footer #createdAt').html('Created at: ' + new Date(data.timeCreated).toUTCString() );
			$('div.modal-footer #nRaters').html('Number of raters: ' + data.rating.numberOfRaters );							
			$('div.modal-footer #rating').html('Rating: ' + data.rating.rating);
			$('#modalShredId').attr("value", data.id);
			
			$('#commentTable').append("<tbody>");
			_.each(data.shredComments, function(c) {
				$('#commentTable tbody').append('<tr><td>' + c.text + 
                    '</td><td>' + c.commenter.username + 
                    '</td><td>' + new Date(c.timeCreated).toUTCString() + '</td></tr>');
			});
			$('#commentTable').append("</tbody>");
			
			$('#playVideoModal').modal('show');
		});
	}
	
	function rateShred(rateVal) {
		var baseUrl = "<c:url value='/shred/'/>";
		var url = baseUrl + $('input#modalShredId').attr("value") + "?action=rate&rating=" + rateVal;
		$.post(url, function(data) {
			$('div.modal-footer #nRaters').html('Number of raters: ' + data.rating.numberOfRaters );							
			$('div.modal-footer #rating').html('Rating: ' + data.rating.rating);
		});
	}
	
	function commentShred(commentText) {
		console.log("text: " + commentText);
		var baseUrl = "<c:url value='/shred/'/>";
	
		var url = baseUrl + $('input#modalShredId').attr("value") + "/comment/?text=" +
				commentText;
		
		$.post(url, function(data) {
			var last = _.last(data.shredComments); 
				$('#commentTable tbody').append('<tr><td>' + last.text + 
	                    '</td><td>' + last.commenter.username + 
	                    '</td><td>' + new Date(last.timeCreated).toUTCString() + '</td></tr>');
		});
	}
	