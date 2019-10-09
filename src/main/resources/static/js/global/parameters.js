'use strict';
let telephone = null;

function initParameters(){
	telephone = getPathParameter('telephone');
	getMenu();
	initWebSocket();
}

//Return paramete from url by name getParameterByName
function getPathParameter(name) {
    var url = window.location.href;
    var regex = new RegExp("[?&]"+name+"(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return "";
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}