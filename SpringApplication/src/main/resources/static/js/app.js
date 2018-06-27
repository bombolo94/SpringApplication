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
		var textToSaveAsBlob = new Blob([ textToSave ]);
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

function destroyClickedElement(event) {
	document.body.removeChild(event.target);
}

function loadFile(id) {
	var fileToLoad = document.getElementById(id).files[0];
	if(fileToLoad!=null){
		if (document.getElementById(id).value.endsWith(".xml")) {
			loadAsText(id,4,".json",fileToLoad);
		} else if (document.getElementById(id).value.endsWith(".json")) {
			loadAsText(id,5,".xml",fileToLoad);
		}else if (document.getElementById(id).value.endsWith(".txt")) {
			loadAsText(id,0,".txt",fileToLoad);
		}
		
	}
}

function loadAsText(id,n,ext, fileToLoad){
	var nFU = document.getElementById(id).value;
	var fileReader = new FileReader();
	fileReader.onload = function(fileLoadedEvent) {

		if(ext==".json" || ext==".xml"){
			var textFromFileLoaded = fileLoadedEvent.target.result;
			var p = nFU.substring(0, nFU.length - n);
			var inputFileName = document.createElement('input');
			inputFileName.name = "inputFileName";
			inputFileName.value = p + ext;
			inputFileName.setAttribute('form', 'formParser');
			inputFileName.style.visibility = "hidden";

			var input = document.createElement('textarea');
			var oFormObject = document.getElementById("formParser");
			input.name = 'fileContent';
			input.cols = 80;
			input.rows = 20;
			input.id = 'fileContent';
			input.setAttribute('form', 'formParser');
			var button = document.createElement("button");
			button.setAttribute('type', 'submit');
			button.innerHTML = "Upload";
			var oBody = document.getElementById("parserdiv");
			input.value = textFromFileLoaded;
			oBody.appendChild(inputFileName);
			oBody.appendChild(input);
			oBody.appendChild(button);
			button.onclick = function() {
				oFormObject.submit();
			};
		}else{
			var textFromFileLoaded = fileLoadedEvent.target.result;
			var fieldset = document.createElement('fieldset');
			var legend = document.createElement('legend');
			legend.innerHTML = "Template";

			var myDiv = document.getElementById("templateChoice");
			var label = document.createElement("label");
			label.innerHTML = "Template Message";
			var checkbox = document.createElement("input");
			checkbox.setAttribute("type", "checkbox");
			checkbox.setAttribute("name", "template");
			checkbox.setAttribute("value", "templateMessage");

			var checkbox1 = document.createElement("input");
			var label1 = document.createElement("label");
			label1.innerHTML = "Template Schematron";
			checkbox1.setAttribute("type", "checkbox");
			checkbox1.setAttribute("name", "template");
			checkbox1.setAttribute("value", "schematron");
			checkbox1.innerHTML = "Template Schematron";

			checkbox.setAttribute('form', 'formGeneratorF');
			checkbox1.setAttribute('form', 'formGeneratorF');
			myDiv.appendChild(fieldset);
			fieldset.appendChild(legend);
			fieldset.appendChild(label);
			fieldset.appendChild(checkbox);
			fieldset.appendChild(label1);
			fieldset.appendChild(checkbox1);
			var button = document.createElement("button");
			button.setAttribute('type', 'submit');
			button.innerHTML = "Genera Template";
			myDiv.appendChild(button);

			button.onclick = function() {
				oFormObject.submit();
			};

			var input = document.createElement('textarea');
			var oFormObject = document.getElementById("formGeneratorF");
			input.name = 'fileContent';
			input.cols = 80;
			input.rows = 20;
			input.id = 'fileContent';
			input.setAttribute('form', 'formGeneratorF');
			
			var oBody = document.getElementById("generaT");
			input.value = textFromFileLoaded;
			oBody.appendChild(input);
		}
		
	};
	fileReader.readAsText(fileToLoad, "UTF-8");
	
}


function inputForm() {
	var parser = document.getElementsByName("input");
	cont = 0;
	for (var i = 0, length = parser.length; i < length; i++) {
		if (parser[i].checked) {
			cont++;
		}
	}

	if (cont > 0) {
		document.getElementById("formInput").submit();
		return true;
	} else {

		return false;
	}
}


function create_zip() {
	var fileToLoadS = document.getElementById("inputTextToSaveS").value
	var nameS = document.getElementById("inputFileNameToSaveAsS").value
	var fileToLoadM = document.getElementById("inputTextToSaveM").value
	var nameM = document.getElementById("inputFileNameToSaveAsM").value
	var zip = new JSZip();
	zip.file(nameS, fileToLoadS);
	zip.file(nameM, fileToLoadM);

	zip.generateAsync({
		type : "blob"
	}).then(function(blob) {
		saveAs(blob, "FileGenerated.zip");
	});

}
