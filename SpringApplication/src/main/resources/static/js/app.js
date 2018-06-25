function valid() {
	var file = document.getElementById("fileContent").value;
	if (file == '') {
		alert('Please Selected File');
		return false;
	} else {

		return true;
	}

}

function radioParsing() {
	var parser = document.getElementsByName("input");
	cont = 0;
	for (var i = 0, length = parser.length; i < length; i++) {
		if (parser[i].checked) {
			cont++;
		}
	}
	
	var parser = document.getElementsByName("parser");
	cont = 0;
	for (var i = 0, length = parser.length; i < length; i++) {
		if (parser[i].checked) {
			cont++;
		}
	}

	if (cont == 0) {
		alert("Radio must be checked");
		return false;
	} else {
		 document.getElementById("formParser").submit();
		return true;
	}
}

function saveFile() {
	var fileNameToSaveAs = document.getElementById("inputFileNameToSaveAs").value
	if (fileNameToSaveAs == '') {
		alert("Please Digit a name for the file");
		return false;

	} else {
		var textToSave = document.getElementById("inputTextToSave").value;
		var textToSaveAsBlob = new Blob([ textToSave]);
		var textToSaveAsURL = window.URL.createObjectURL(textToSaveAsBlob);
		var downloadLink = document.createElement("a");
		downloadLink.download = fileNameToSaveAs;
		downloadLink.innerHTML = "Download File";
		downloadLink.href = textToSaveAsURL;
		downloadLink.onclick = destroyClickedElement;
		downloadLink.style.display = "none";
		document.body.appendChild(downloadLink);

		downloadLink.click();
		return true;
	}

}

function saveTextAsFile(ext) {
	var fileNameToSaveAs = document.getElementById("inputFileNameToSaveAs").value
	if (fileNameToSaveAs == '') {
		alert("Please Digit a name for the file");
		return false;

	} else {
		fileNameToSaveAs = fileNameToSaveAs + ext;
		var textToSave = document.getElementById("inputTextToSave").value;
		var textToSaveAsBlob = new Blob([ textToSave, {
			charset : "UTF-8"
		} ]);
		var textToSaveAsURL = window.URL.createObjectURL(textToSaveAsBlob);
		alert(textToSaveAsBlob);
		var downloadLink = document.createElement("a");
		downloadLink.download = fileNameToSaveAs;
		downloadLink.innerHTML = "Download File";
		downloadLink.href = textToSaveAsURL;
		downloadLink.onclick = destroyClickedElement;
		downloadLink.style.display = "none";
		document.body.appendChild(downloadLink);

		downloadLink.click();
		return true;
	}

}

function destroyClickedElement(event) {
	document.body.removeChild(event.target);
}

function loadFileAsText(id) {
	var fileToLoad = document.getElementById(id).files[0];
	if (document.getElementById(id).value.endsWith(".xml")) {
		var nFU = document.getElementById(id).value;
		var p = nFU.substring(0, nFU.length - 4);
		document.getElementById('inputFileName').value=p+".json";
		var fileReader = new FileReader();
		fileReader.onload = function(fileLoadedEvent) {
			var textFromFileLoaded = fileLoadedEvent.target.result;
			document.getElementById("fileContent").value = textFromFileLoaded;
		};
		fileReader.readAsText(fileToLoad, "UTF-8");
		return true;
	} else if (document.getElementById(id).value.endsWith(".json")) {
		var nFU = document.getElementById(id).value;
		var p = nFU.substring(0, nFU.length - 5);
		document.getElementById('inputFileName').value=p+".xml";
		var fileReader = new FileReader();
		fileReader.onload = function(fileLoadedEvent) {
			var textFromFileLoaded = fileLoadedEvent.target.result;
			document.getElementById("fileContent").value = textFromFileLoaded;
		};
		fileReader.readAsText(fileToLoad, "UTF-8");
		return true;
	

	}else if(ext=".txt"){
		var fileReader = new FileReader();
		fileReader.onload = function(fileLoadedEvent) {
			var textFromFileLoaded = fileLoadedEvent.target.result;
			document.getElementById("fileContent").value = textFromFileLoaded;
		};
		fileReader.readAsText(fileToLoad, "UTF-8");
		return true;
	} else {
		alert('Please enter a valid name file.');						
		return false;
	}

}

function inputForm(){
	var parser = document.getElementsByName("input");
	cont = 0;
	for (var i = 0, length = parser.length; i < length; i++) {
		if (parser[i].checked) {
			cont++;
		}
	}
	
	if(cont>0){
		 document.getElementById("formInput").submit();
		return true;
	}else{
		
		return false;
	}
}
function ciao(t,name,content){
	if(t<2){
		if(t==0){
			var zip = new JSZip();
		}
		zip.file(name, content);
	}else{
		zip.generateAsync({type:"blob"}).then(function (blob) {
			saveAs(blob, "FileGenerated.zip");
		 });
	}
	
	
}

function zio(outContent){
	alert("bella");
	alert(outContent[0]);
	
}

function create_zip() {
	var fileToLoad = document.getElementById("inputTextToSave").value
	var name = document.getElementById("inputFileNameToSaveAs").value
	var zip = new JSZip();
	zip.file(name, fileToLoad);
	
	zip.generateAsync({type:"blob"}).then(function (blob) {
        saveAs(blob, "FileGenerated.zip");
	 });
	
}


