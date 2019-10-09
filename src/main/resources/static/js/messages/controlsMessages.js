//# sourceURL=controlsMessages.js		
window.addEventListener("keyup", function(event) {
  // Number 13 is the "Enter" key on the keyboard
  if (event.keyCode === 13) {
	  sendButton();
  }
});

function resetFiles(){
	//To upload more than once same video (val is repeated)
	//Clean at click, because we need value until send file to get filename.
	$('#files').val("");
	$('#files').click();
}

//When download or upload we need this object
function attachFiles(evt) {
    var file = evt.target.files[0]; // FileList object
    if (file){
    	openAttachments();
    	if(file.type.match("image")){
    		var readerImageReal = new FileReader();
	    	readerImageReal.onload = function () {
	    		//BLOB blob = new Blob([readerImageReal.result], {type: "image/png"});
	    		
	    		$('#modal').append("<img id='attachmentsImg' class='attachments-img'></img>");
		    	var img = document.getElementById("attachmentsImg");
		        //BLOB img.src = window.URL.createObjectURL(file); 
		        img.src = readerImageReal.result;
		        img.style.display = "block";      
	    	 }
		     readerImageReal.readAsDataURL(file);
	    } else if (file.type.match("video")){
	    	var readerVideoReal = new FileReader();
	    	readerVideoReal.onloadend  = function () {
		    	$('#modal').append("<video id='attachmentsVideo' class='attachments-video' controls></video>");
		        var video = document.getElementById("attachmentsVideo");
		        video.src = readerVideoReal.result;
		        video.style.display = "block";

	    	}
	        readerVideoReal.readAsDataURL(file);
	    }else if (file.type.match('application/pdf')
    				|| file.type.match('application/msword') 
    				|| file.type.match('application/vnd.openxmlformats-officedocument.wordprocessingml.document')){
	    	var readerDocReal = new FileReader();
	    	readerDocReal.onload = function () {
		    	$('#modal').append('<img id="docImg" class="attachments-img"></img>');
		    	var img = document.getElementById("docImg");
	        	if(file.type.match('application/pdf')){
			    	img.src = '../img/pdf.png';
			    	$('#modal').append('<object id="attachmentsDoc" type="application/pdf"></object>');	
		        }else if (file.type.match('application/msword') 
		        		|| file.type.match('application/vnd.openxmlformats-officedocument.wordprocessingml.document')){
		        	img.src = "../img/word.png";
		        	$('#modal').append('<object	id="attachmentsDoc" style="display:none" type="application/msword"></object>');
		        }
		    	var object = document.getElementById('attachmentsDoc');
		    	object.data = readerDocReal.result;
		    	img.style.display = 'block';
	    	}
	    	readerDocReal.readAsDataURL(file);
	    }else{
	    	showErrorMessage('Fyle type not allowed');
	    }
    	
    	if ($('#modal'))
    		$('#modal').append('<input type="hidden" id="attachmentName" value="'+file.name+'">');
    }
}

function sendButton(){
	var text = ($('#inputSend').val().trim()=="")?null:$('#inputSend').val();
	
	var jsonData = {};
 	if (text !=null 
 			|| $('#attachmentsImg').length
 			|| $('#attachmentsDoc').length
 			|| $('#attachmentsVideo').length){
 		jsonData['type'] = 'create';
	    if (text != null){
	    	jsonData['text'] = text;
	    }
	    if ($('#attachmentsImg').length){
	    	var jsonImages = {};
	    	var img = document.getElementById('attachmentsImg');
	    	fileThumbnail(img, img.width, img.height);

	    	jsonData['images'] = [];
	    	jsonImages['name'] = $('#attachmentName').val();
	    	jsonImages['real'] = $('#attachmentsImg').attr('src');
	    	jsonImages['thumbnail'] = document.getElementById('attachmentsCanvas').toDataURL('image/jpeg', 0.5);
		    jsonData['images'].push(jsonImages);
		}
	    if ($('#attachmentsVideo').length){
	    	var video = document.getElementById('attachmentsVideo');
	    	fileThumbnail(video, video.videoWidth, video.videoHeight);
    		
	    	var jsonVideos = {};
	    	jsonData['videos'] = [];
	    	jsonVideos['name'] = $('#attachmentName').val();
	    	jsonVideos['real'] = $('#attachmentsVideo').attr('src');
	    	jsonVideos['thumbnail'] = document.getElementById('attachmentsCanvas').toDataURL('image/jpeg', 0.5);
		    jsonData['videos'].push(jsonVideos);
	    }
	    if ($('#attachmentsDoc').length){
	    	var jsonDocs = {};
	    	jsonData['docs'] = [];
	    	jsonDocs['name'] = $('#attachmentName').val();
	    	jsonDocs['real'] = $('#attachmentsDoc').attr('data');
	    	jsonDocs['thumbnail'] = document.getElementById('docImg').src;
		    jsonData['docs'].push(jsonDocs);
		}
	    var jsonDates = {};
	    jsonDates['user'] = Date.now();
	    jsonData['dates'] = jsonDates;
	    
	    //We write the message before response to ensure there is conection. 
	    //On response, we update this message.
	    writeMessage(jsonData);

	    sendMessage(jsonData);
	    
	    //Textarea with message is cleaned
	    if (text!=null){
	    	$('#inputSend').val('');
	    }
	    if ($('#attachmentsImg').length
	 			|| $('#attachmentsDoc').length
	 			|| $('#attachmentsVideo').length){
	    	closeAndCleanModal();
	    }
	}
}

function sendMessage(json) {
	console.log ('ajax post: /messages/send/'+communicationId+'/'+telephone+'/'+telephoneTo);
	$.ajax({url:'/messages/send/'+communicationId+'/'+telephone+'/'+telephoneTo,
        type: 'POST',
        data: JSON.stringify(json),
		contentType: 'application/json',
		success: function(message) {
			drawMessageText(message);
		}
    }).fail(function() { //on failure
    	showErrorMessage('Error in connection');
    });
}

function openAttachments(){
	$('#modal').css({ 'height': 'calc(100vh - 100px)' });
	openModal();
	$('#modal').append('<canvas id="attachmentsCanvas" class="attachments-canvas" style="display: none;"></canvas>');
}

function base64ToArrayBuffer (base64) {
    base64 = base64.replace(/^data\:(.*)\;base64,/gmi, '');
    var binaryString = window.atob(base64);
    var len = binaryString.length;
    var bytes = new Uint8Array(len);
    for (var i = 0; i < len; i++) {
        bytes[i] = binaryString.charCodeAt(i);
    }
    return bytes.buffer;
}