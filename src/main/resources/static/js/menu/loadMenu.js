function getMenu(){
	// Ajax GET request.
	console.log ('ajax get: /telephones/'+ telephone);
	$.get('/telephones/'+telephone,
		function(requests) {
	    	var menu ="";
	    	for(i=0;i<requests.length;i++){
	    		var communicationId = requests[i].communicationId;
	    		var request = '<span class="telephone">' + requests[i].description + '</span>';
	    		var advice="";
	    		if (requests[i].advices)
	    			advice= '<span class="icon" id="advice'+communicationId+'" data-icon="&#9993;">'+requests[i].advices+'</span>';
	    		menu += '<li id="menu'+communicationId+'" class="green" onclick=loadMessagesContainer("'+requests[i].communicationId+'","'+requests[i].telephoneTo+'")>' +
	    		request +
				advice +
				'</a></li>';
	    	}
	    	$('#telephones').html(menu);
	     })
        .fail(function() { //on failure
        	showErrorMessage('Error loading requests');
        });
}

//Loads all messages for a requestId
function loadMessagesContainer(newCommunicationId, newTelephoneTo){
	$('#chatContainer').load('messages.html', function() {
		$('[id^="menu"]').css('background-color','#4CAF50');
		$("#menu"+newCommunicationId).css('background-color','#0c8fff');
		closeAndCleanModal();
		closeAllErrors();
		loadMessages(newCommunicationId, newTelephoneTo);
		$('#controls').show();
	});
}