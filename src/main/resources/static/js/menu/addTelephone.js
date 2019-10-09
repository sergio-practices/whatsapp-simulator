function openAddTelephoneForm(){
	$('#modal').css({ 'height': 'calc(100vh)' });
	openModal();
	$('#modal').load('addtelephone.html');
}

function addTelephone(){
	var jsonData = {};
	jsonData['telephone'] = telephone;
	jsonData['telephoneTo'] = document.getElementById('telephoneTo').value;
	jsonData['description'] = document.getElementById('description').value;
	console.log ('ajax post: /telephones/telephone' + jsonData);

	$.ajax({url:'/telephones/telephone',
	    type: 'POST',
	    data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).success(function(telephone) { //on failure
		addOrUpdateTelephone(telephone);
		closeAndCleanModal();
	}).fail(function() { //on failure
		showErrorMessage('Error in connection');
	});
}