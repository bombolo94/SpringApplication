<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="org.springframework.web.servlet.ModelAndView"  %>
<%@ page import="org.springframework.ui.Model" %>
<html>
<head>
<script src="js/app.js"></script>
<link href="css/style.css" rel="stylesheet">
<title>Generator</title>
</head>
<body>
	<h1>Welcome in Generato File</h1>
	<%String input = (String)request.getAttribute("input"); 
	
		%>


	<% if(input!=null){%>
	<table>
		<%if(input.equals("inpU")){%>

		<tr>
		
			<td>
				<form action="templateGeneratorU" method="POST">
					Inserisci nome dataset: <input type="text" name="datasetName" style="width:352px">
					<fieldset>
					 <legend>Template </legend><br>
					 <input type="checkbox" name="template" value="templateMessage"/> Template Message
					 <br /> 
					 <input type="checkbox" name="template" value="schematron"/> Schematron 
					</fieldset>
					<input type="submit" value="genera">
				</form>
			</td>
		</tr>
		<%  String fileContent = (String) request.getAttribute("fileContent");
			String fileNameOut = (String) request.getAttribute("fileNameOut");		
			if(fileContent!=null && fileNameOut!=null){%>
				<tr>
					<td><textarea id="inputTextToSave" style="width:512px;height:256px"><%=fileContent %></textarea></td>
				<tr>
				<tr>
					<td>Filename to Save As:<input id="inputFileNameToSaveAs" value="<%=fileNameOut %>" style="width:352px"></td>
					<td><button>Save File</button></td>
				</tr>
			<%} %>	
		<%}else{	%>
		<%String ext = ".txt"; %>
		<tr>
			<td>
				Scegli file dei dadates<input type="file" id="fileDataset">
					<button onclick="return loadFileAsText('<%=ext%>','fileDataset')"  >Load Selected File</button>
			</td>
			<td>
				<form action="templateGeneratorF" method="POST">
					<fieldset>
					 <legend>Template</legend><br>
					 <input type="checkbox" name="template" value="templateMessage"/> Template Message
					 <br /> 
					 <input type="checkbox" name="template" value="schematron"/> Schematron 
					</fieldset>
					<textarea name=fileContent id="fileContent"	style="width: 512px; height: 256px"></textarea>
					<input type="submit" value="genera" >
				</form>
			</td>
		</tr>
		<% }%>
		
	</table>
	<% }else{%>

	<h3>Please chose your ways...</h3>

	<form id="form1" method="POST" action="inputForm"
		onclick=" return prova()">
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

	<% }%>


	<a href="./">Home</a>

</body>
</html>