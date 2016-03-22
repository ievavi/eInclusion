var _validFileExtensions = [ ".xlsx" ];
function ValidateSingleInput(oInput) {
	if (oInput.type == "file") {
		var sFileName = oInput.value;
		if (sFileName.length > 0) {
			var blnValid = false;
			for (var j = 0; j < _validFileExtensions.length; j++) {
				var sCurExtension = _validFileExtensions[j];
				if (sFileName.substr(sFileName.length - sCurExtension.length,
						sCurExtension.length).toLowerCase() == sCurExtension
						.toLowerCase()) {
					blnValid = true;
					break;
				}
			}

			if (!blnValid) {
				alert("Sorry, " + sFileName
						+ " is invalid, allowed extensions are: "
						+ _validFileExtensions.join(", "));
				oInput.value = "";
				return false;
			}
		}
	}
	return true;
}
var _validFileExtensions2 = [ ".xls" ];
function ValidateSingleInput2(oInput) {
	if (oInput.type == "file") {
		var sFileName = oInput.value;
		if (sFileName.length > 0) {
			var blnValid = false;
			for (var j = 0; j < _validFileExtensions2.length; j++) {
				var sCurExtension = _validFileExtensions2[j];
				if (sFileName.substr(sFileName.length - sCurExtension.length,
						sCurExtension.length).toLowerCase() == sCurExtension
						.toLowerCase()) {
					blnValid = true;
					break;
				}
			}

			if (!blnValid) {
				alert("Sorry, " + sFileName
						+ " is invalid, allowed extensions are: "
						+ _validFileExtensions2.join(", "));
				oInput.value = "";
				return false;
			}
		}

	}
	return true;
}

function UploadSuccess(oInput) {

		    
alert("Wait about 5 seconds after you press OK button to database to be updated!");
		
	

	return true;
}
