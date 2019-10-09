//# sourceURL=sockets.js
'use strict';
var socket = null;
let telephoneTo = null;
let communicationId = null
var messagesSubscription = null;
let subsId = null;
let header = null;

//Receive update messages to update menu requests
function initWebSocket(){
    var socketWs = new SockJS('/ws');
    socket = Stomp.over(socketWs);
    socket.connect({}, onConnected, onError);
    event.preventDefault();
}

function onConnected() {
	socket.send('/app/add/user',{},JSON.stringify({'telephone': telephone}));
	socket.subscribe('/topic/telephones/'+telephone, onTelephoneReceived);
    console.log ('subscribe to: /topic/telephones/'+telephone);
}

//If connection have been lost, it has been unsuscribed
function subscribeMessagesTopic(newCommunicationId, newTelephoneTo){
	if (null != messagesSubscription){
		messagesSubscription.unsubscribe();
		messagesSubscription=null;
		console.log ('unsubscribe to: /topic/messages/' + communicationId + '/' + telephone + '/' + telephoneTo);
	}
	
	communicationId = newCommunicationId;
	telephoneTo = newTelephoneTo;
	messagesSubscription = socket.subscribe('/topic/messages/'+communicationId+'/'+telephone+'/'+telephoneTo, onMessageReceived);

	console.log ('subscribe to: /topic/messages/' + communicationId + '/'+ telephone + '/' + telephoneTo);
}

function onError(error) {
	showErrorMessage('Error in connection');
}

function onTelephoneReceived(payload) {
	drawMenuText(JSON.parse(payload.body));
}

function onMessageReceived(payload) {
	drawMessageText(JSON.parse(payload.body));
}