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
					<input type="submit" value="Genera Template" >
				</form>
			</td>
		</tr>
		<%  String fileContentS = (String) request.getAttribute("fileContentS");
			String fileNameOutS = (String) request.getAttribute("fileNameOutS");	
			String fileContentM = (String) request.getAttribute("fileContentM");
			String fileNameOutM = (String) request.getAttribute("fileNameOutM");
			
			
			if( (fileContentS!=null && fileNameOutS!=null) && (fileContentM==null && fileNameOutM==null) ){ %>
				<tr>
					<td><textarea id="inputTextToSave" style="width:512px;height:256px"><%=fileContentS %></textarea></td>
				</tr>
				<tr>
					<td>Filename to Save As:<input id="inputFileNameToSaveAs" value="<%=fileNameOutS %>" style="width:352px"></td>
					<td><button onclick="return saveFile()" >Save File</button></td>
				</tr>
				<%System.out.println(fileContentS); %>
			<%}else if( (fileContentM!=null && fileNameOutM!=null) && (fileContentS==null && fileNameOutS==null)){%>
				<tr>
					<td><textarea id="inputTextToSave" style="width:512px;height:256px"><%=fileContentM %></textarea></td>
				</tr>
				<tr>
					<td>Filename to Save As:<input id="inputFileNameToSaveAs" value="<%=fileNameOutM %>" style="width:352px"></td>
				
					<td><button onclick="return saveFile()" >Save File</button></td>
				</tr>
			<%}else if( (fileContentM!=null && fileNameOutM!=null) && (fileContentS!=null && fileNameOutS!=null)){ %>
				<tr>
					<td><textarea id="inputTextToSaveS" style="width:512px;height:256px"><%=fileContentS %></textarea></td>
					<td>Filename to Save As:<input id="inputFileNameToSaveAsS" value="<%=fileNameOutS %>" style="width:352px"></td>
				</tr>
				<tr>
					<td><textarea id="inputTextToSaveM" style="width:512px;height:256px"><%=fileContentM %></textarea></td>
					<td>Filename to Save As:<input id="inputFileNameToSaveAsM" value="<%=fileNameOutM %>" style="width:352px"></td>
				</tr>	
				<tr>
					<td><button>Save File As Zip</button></td>
				</tr>
			<% }%>	
		
				
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
					<input type="submit" value="Genera Template" >
				</form>
			</td>
		</tr>
		<% }%>
		
	</table>
	<% }else{%>

	<h3>Please chose your ways...</h3>

	<form id="formInput" method="POST" action="inputForm" onclick=" return inputForm()">
		<table>
			<tr>
				<td>
					<fieldset>
						<legend>Input</legend>

						U <input type="radio" name="input" value="inpU" /> 
						F <input type="radio" name="input" value="inpF" />
					</fieldset>
				</td>
			</tr>
		</table>
	</form>

	<% }%>


	<a href="./">Home</a>

</body>
</html>