//# sourceURL=loadMessages.js
var receiveDelayedMessage = false;

/**
 *On new connection by request id, we load all the messages and move to last one.
 */
function loadMessages(newCommunicationId, newTelephoneTo) {
	subscribeMessagesTopic(newCommunicationId, newTelephoneTo);
	loadHeader(newCommunicationId, newTelephoneTo);
	loadAllMessages(newCommunicationId);
}

function loadHeader(newCommunicationId, newTelephoneTo){
	console.log ('ajax get: /telephones/description/'+newCommunicationId+'/'+newTelephoneTo);
	$.get('/telephones/description/'+newCommunicationId+'/'+newTelephoneTo,
		function(telephone) {
			//setHeader
			var title = $('#menu'+telephoneTo).attr("title");
			var header = '<span class="header-left">'+telephone.telephoneTo+'</span>'+
						 '<span class="header-right">'+telephone.description+'</span>'
			$('#dynamicInfo').html(header);	//Open connection
		})
		.fail(function() { //on failure
        	showErrorMessage("Error loading header");
        });
	
}

//On new connection by request id, we load all the messages and move to last one.
function loadAllMessages(newCommunicationId) {
	
	// Ajax GET request.
	console.log ('ajax post: /messages/communicationId/'+newCommunicationId);
    $.get('/messages/communicationId/'+newCommunicationId,
        function(json) {
    		//$('#messages').css('visibility', 'hidden');
    		for(i=0;i<json.length;i++){
	    		writeMessage(json[i]);
	    	}
        	//$('#messages').css('visibility', 'visible');
        })
        .done(function() {
        	//When finish move to last message, and check new messages messages to send reception message
        	$('#messages').scrollTop($('#messages')[0].scrollHeight);
        	
    		//Onload we check if there are messages or the request menu has indication
        	if ($("#advice"+newCommunicationId).length
        			|| receiveDelayedMessage) 
        		notifyReceptionMessage();
		})
        .fail(function() { //on failure
        	showErrorMessage('Error loading messages');
        });
}

//Load the image we click to show.
function loadImage(messageId) {
	console.log ('ajax get: /messages/image?messageId='+messageId);
	$.get('/messages/image?messageId='+messageId,
	    function(json) {
			if (json != null){
				openAttachments();
		    	$('#modal').append('<img id="attachmentsImg" class="attachments-img"></img>')
		    	.append('<input type="hidden" id="attachmentName" value="'+json.name+'">');
		    	var img = document.getElementById("attachmentsImg");
		    	img.src = json.real;
		        img.onload = function () {
		        	fileThumbnail(img, img.width, img.height);
		        	img.style.display = "block";
		        };
			}
	}).fail(function() { //on failure
    	showErrorMessage('Error loading image');
    });
}

//Load the video we click to show.
function loadVideo(messageId) {
	if (null == localStorage.getItem(messageId)){
		console.log ('ajax get: /messages/video?messageId='+messageId);
		$.get('/messages/video?messageId='+messageId, 
			function(json){
				showVideo(json);
				localStorage.setItem(messageId, JSON.stringify(json));
				}
	    ).fail(function() { //on failure
	    	showErrorMessage('Error loading video');
	    });
	}else{
    	showVideo(JSON.parse(localStorage.getItem(messageId)));
    }
}

function showVideo(json) {
	if (json != null){
		openAttachments();
		$('#modal').append('<video id="attachmentsVideo" class="attachments-video" controls></video>')
		.append('<input type="hidden" id="attachmentName" value="'+json.name+'">');
        var video = document.getElementById('attachmentsVideo');
        video.onloadeddata  = function () {
        	fileThumbnail(video, video.videoWidth, video.videoHeight);
        	video.style.display = 'block';
        };
        video.src = json.real;
	}
}

//Load the image we click to show.
function loadDoc(messageId) {
	window.location.href = "/messages/doc?messageId="+messageId;
}