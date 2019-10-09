//# sourceURL=thumbnail.js
var canvasWidth = 320;

//Create a video and its miniature into a canvas
function fileThumbnail(file, width, height){
	var canvasWidthVideo;
	var canvasHeightVideo;
	if (width > height){
		canvasWidthVideo = canvasWidth;
		canvasHeightVideo = ((height * canvasWidth)/width);
	}else{
		canvasWidthVideo = ((width * canvasWidth)/height);
		canvasHeightVideo = canvasWidth;
	}
	
	//Create the miniature associated
	createCanvas(file, canvasWidthVideo, canvasHeightVideo);
}

//Create a miniature not visible into a canvas to send and receive in messages
function createCanvas(file, canvasWidth, canvasHeight){
	var canvas = document.getElementById('attachmentsCanvas');
	canvas.width = canvasWidth; canvas.style.width  = canvasWidth;
	canvas.height = canvasHeight; canvas.style.height = canvasHeight;
	
	//Depending orientation, we transform canvas
	var ctx = canvas.getContext('2d');
	ctx.fillStyle = '#fff'; //for transparent backgrounds
	ctx.fillRect(0, 0, canvasWidth, canvasHeight);
	ctx.drawImage(file, 0, 0, canvasWidth, canvasHeight);
}