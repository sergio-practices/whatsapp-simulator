var focused = true;

window.onfocus = function() {
    focused = true;
    $("#tabTitle").html( "&#10097" );
};

window.onblur = function() {
    focused = false;
}; 

function setTabTitle(telephoneFrom){
	if (!focused)
		$("#tabTitle").html( telephoneFrom + " &#9993;" );
}