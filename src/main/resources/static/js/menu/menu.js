function w3_open() {
	$("#container").css("margin-left", "180px");
	$("#leftMenu").css("display", "block");
	$("#closeSide").css("display", "none");
	$("#errors").css({ "margin-left": "180px" });
	$("#modal").css("width","calc(100vw - 200px)");
}

function w3_close() {
	$("#container").css("margin-left", "0px");
	$("#leftMenu").css("display","none");
	$("#closeSide").css("display","inline-block");
	$("#errors").css({ "margin-left": "0px" });
	$("#modal").css("width","calc(100vw - 20px)");
}

//Check advices and update number of advices to the user, 
// check communicationId if exists update description, else insert new telephone
function drawMenuText(telephone) {
	if (telephone.communicationId != null){
		if (telephone.advices){
			var advice = "#advice"+telephone.communicationId;
			var adviceTxt = telephone.advices;
			if (!$(advice).length){
				$("#menu"+telephone.communicationId).append('<span class="icon" id="advice'+telephone.communicationId+'" data-icon="&#9993;">'+adviceTxt+'</span>');
			}else{
				$(advice).html(adviceTxt);
			}
		}else{
			addOrUpdateTelephone(telephone);
		}
	}
}

function addOrUpdateTelephone(telephone){
	if ($('#menu'+telephone.communicationId).length){
		$('#menu'+telephone.communicationId+' span').html(telephone.description);
    }else{
		var communicationId = telephone.communicationId;
		var request = '<span class="telephone">' + telephone.description + '</span>'
			+ '<span style="display: none">' + telephone.communicationId + '</span>';
		var telephoneMenu = '<li id="menu'+communicationId+'" class="green" title="'+telephone.description+'" onclick=loadMessagesContainer("'+communicationId+'","'+telephone.telephoneTo+'")>' +
		request +
		'</a></li>';
		$("#telephones").append(telephoneMenu);
    	
    }
}
