function closeModal(evt) {
	if (evt.target.id == 'modal'){
		closeAndCleanModal();
	}
}

//Open panel to write attachments
function openModal(){
	$('#modal').empty();
	$('#modal').css({ 'z-index': '1' });
	$('#modal').css({ 'background': 'rgba(0,0,0,0.4)' });   
}

//Close panel to write attachments and restore defaults
function closeAndCleanModal(){
	readerImageReal=null;
	readerVideoReal=null;
	$('#modal').empty();
	$('#modal').css({ 'z-index': '-1' });
	$('#modal').css({ 'background': 'none' });
}