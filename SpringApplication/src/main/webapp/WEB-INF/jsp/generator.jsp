<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="org.springframework.web.servlet.ModelAndView"%>
<%@ page import="org.springframework.ui.Model"%>
<html>
<head>
<script src="js/app.js"></script>
<script src="js/jszip.js"></script>
<script src="js/FileSaver.js"></script>
<link href="css/style.css" rel="stylesheet">
<title>Generator</title>
</head>
<body>
	<h1>Welcome in Generator File</h1>
	<%
		String input = (String) request.getAttribute("input");
	%>


	<%
		if (input != null) {
	%>
	<div class="central">
		<table>
			<%
				if (input.equals("inpU")) {
			%>

			<tr>

				<td>

					<form id="templateGeneratorU" action="templateGeneratorU" method="POST">
						<fieldset>
							<legend>Template </legend>
							<br> <input type="checkbox" name="template"
								value="templateMessage" /> Template Message <br /> <input
								type="checkbox" name="template" value="schematron" /> Template
							Schematron
						</fieldset>
						
					</form>
				</td>
			</tr>
			<tr>
				<td>
					Inserisci nome dataset: <input type="text" name="datasetName" style="width: 352px" form="templateGeneratorU">
				</td>
				<td>
					<input type="submit" value="Genera Template" form="templateGeneratorU">
				</td>
			</tr>
			<%
				String fileContentS = (String) request.getAttribute("fileContentS");
						String fileNameOutS = (String) request.getAttribute("fileNameOutS");
						String fileContentM = (String) request.getAttribute("fileContentM");
						String fileNameOutM = (String) request.getAttribute("fileNameOutM");

						if ((fileContentS != null && fileNameOutS != null)
								&& (fileContentM == null && fileNameOutM == null)) {
			%>
			<tr>
				<td><textarea id="inputTextToSave"
						style="width: 512px; height: 256px"><%=fileContentS%></textarea></td>
			</tr>
			<tr>
				<td>Filename to Save As:<input id="inputFileNameToSaveAs"
					value="<%=fileNameOutS%>" style="width: 352px"></td>
				<td><button onclick="return saveFile()">Save File</button></td>
			</tr>
			<%
				} else if ((fileContentM != null && fileNameOutM != null)
								&& (fileContentS == null && fileNameOutS == null)) {
			%>
			<tr>
				<td><textarea id="inputTextToSave"
						style="width: 512px; height: 256px"><%=fileContentM%></textarea></td>
			</tr>
			<tr>
				<td>Filename to Save As:<input id="inputFileNameToSaveAs"
					value="<%=fileNameOutM%>" style="width: 352px"></td>

				<td><button onclick="return saveFile()">Save File</button></td>
			</tr>
			<%
				} else if ((fileContentM != null && fileNameOutM != null)
								&& (fileContentS != null && fileNameOutS != null)) {
			%>
			<tr>
				<td><textarea id="inputTextToSaveS"
						style="width: 512px; height: 256px"><%=fileContentS%></textarea></td>
				<td>Filename to Save As:<input id="inputFileNameToSaveAsS"
					value="<%=fileNameOutS%>" style="width: 352px"></td>
			</tr>
			<tr>
				<td><textarea id="inputTextToSaveM"
						style="width: 512px; height: 256px"><%=fileContentM%></textarea></td>
				<td>Filename to Save As:<input id="inputFileNameToSaveAsM"
					value="<%=fileNameOutM%>" style="width: 352px"></td>
			</tr>
			<tr>
				<td><button onclick="create_zip()">Save File As Zip</button></td>
			</tr>
			<%
				}
			%>
		</table>
		<a href="./">Home</a>
		<%
			} else {
		%>
		<form id="formGeneratorF" action="templateGeneratorF" method="POST">
			<table>
				<tr>
					<td>Scegli file dei dadates<input type="file" id="fileDataset">
					<button id="buttonLoad" onclick="return loadFile('fileDataset')" type="button">Load</button></td>
				</tr>
				<tr>
					<td>
						<div id="generaT"></div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="templateChoice"></div>
						
					</td>
				</tr>
			</table>
		</form>
		<br> <br> <a href="./">Home</a>
	</div>
	<%
		}
	%>
	<%
		} else {
	%>
	<div class="central">
		<h3>Please chose your ways...</h3>

		<form id="formInput" method="POST" action="inputForm"
			onclick=" return inputForm()">
			<table>
				<tr>
					<td>
						<fieldset>
							<legend>Input</legend>

							U <input type="radio" name="input" value="inpU" /> F <input
								type="radio" name="input" value="inpF" />
						</fieldset>
					</td>
				</tr>
			</table>
		</form>
		<a href="./">Home</a>
	</div>
	<%
		}
	%>




</body>
</html>