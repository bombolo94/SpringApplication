<%@page import="org.springframework.web.multipart.MultipartFile"%>
<%@page import="org.springframework.ui.Model"%>
<%@ page import="java.io.*"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>

<script src="js/app.js" charset="UTF-8"></script>
<link href="css/style.css" rel="stylesheet">
<title>Confirmation</title>
</head>
<body>
	<h1>Confirmation</h1>
	<div class="central">

		<%
			String stringFile = (String) request.getAttribute("stringFile");
			String fileToSaveName = (String) request.getAttribute("fileToSaveName");
		%>


		<table>
			<tr>
				<td>Text to Save:</td>
			</tr>
			<tr>
				<td colspan="3"><textarea id="inputTextToSave"
						style="width: 530px; height: 350px"><%=stringFile%></textarea></td>
			</tr>
			<tr>
				<td>Save As:</td>
				<td><input id="inputFileNameToSaveAs"
					value="<%=fileToSaveName%>" style="width: 352px"></input></td>
				<td><button onclick="return saveFile()">Save</button></td>
			</tr>

		</table>
		<a href="./">Home</a>
	</div>
</body>
</html>