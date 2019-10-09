//# sourceURL=messages.js

function writeMessage(jsonMessage){
	var output='';
	if (jsonMessage.videos != null){
		output += '<div class="fileTopLeft play pointer" onclick="loadVideo(\''+jsonMessage.id+'\');">'
				+ "<img src='" + jsonMessage.videos[0].thumbnail + "'/>"
				+ "</div>";
	}
	if (jsonMessage.images != null){
		output += '<div class="fileTopLeft pointer" onclick="loadImage(\''+jsonMessage.id+'\');">'
    			+ '<img src="' + jsonMessage.images[0].thumbnail + '"/>'
    			+ '</div>';
	}
	if (jsonMessage.docs != null){
		output += '<div class="fileTopLeft pointer" onclick="loadDoc(\''+jsonMessage.id+'\');">'
    			+ '<img src="' + jsonMessage.docs[0].thumbnail + '"/>'
    			+ '</div>';
	}
	if (jsonMessage.text != null){
		output += '<div class="textBottomLeft">'+jsonMessage.text+'</div>';
	} 			
	if (jsonMessage.telephone == telephone
			|| jsonMessage.telephone == undefined){
		output += '<div class="checkTopRight">'
		if (jsonMessage.dates != null){
			output += (jsonMessage.dates.send == undefined) ? '<div class="check-ko"/>' : '<div class="check-ok"/>';
			output += (jsonMessage.dates.receive == undefined) ? '<div class="check-ko"/>' : '<div class="check-ok"/>';
		}
		output += '</div>';
		$('#messages').append($('<p id="user_'+jsonMessage.dates.user+'" class="triangle-border-green left">').html(output));
    }else{
		if (jsonMessage.dates != null 
				&& jsonMessage.dates.receive == undefined){
			receiveDelayedMessage = true;//There is a message not readed. Send response message.
		}
		$('#messages').append($("<p class='triangle-border-red right'>").html(output));
		setTabTitle(jsonMessage.telephone);//Set tab title to know there is a new message
	}
	$('#messages').scrollTop($('#messages')[0].scrollHeight);
}

//Message can be a message or a notification
function drawMessageText(response) {
	if (response.type == 'messageDelayed'){
    	$('.advice'+telephone).val('');
    	$('.check-ko').attr('class', 'check-ok');
    }else{
    	// If find a message with this id, it means its only local. We remove it.
    	// Its the message we sent, to update local, you can use dates.user as a key
    	if ($('#user_'+response.dates.user))
    		$('#user_'+response.dates.user).remove();
    	writeMessage(response);
    }
}

function notifyReceptionMessage() {
	console.log ('ajax post: /telephones/notification/'+communicationId+'/'+telephone+'/'+telephoneTo);
	$.ajax({url:'/telephones/notification/'+communicationId+'/'+telephone+'/'+telephoneTo,
        type: 'POST',
        contentType: 'application/json',
		success: function() {
			$('#advice'+communicationId).remove();
		}
    }).fail(function() { //on failure
    	showErrorMessage('Error in connection');
    });
}
