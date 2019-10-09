//Show error messages
function showErrorMessage(message){
	 var div = $('<div></div>').html(message);
	 var span = $('<span class="error-close" onClick="closeError(this)">&#10060;</span>');
	$('#errors').append($('<p class="triangle-obtuse">').append(div).append(span));
}

//Close error messages
function closeError(element){
	$(element).parent().remove();
}

function closeAllErrors(){
	$("#errors").empty();
}