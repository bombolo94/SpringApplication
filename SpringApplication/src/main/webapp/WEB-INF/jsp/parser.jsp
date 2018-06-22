<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>

<script src="js/app.js" charset="UTF-8"></script>
<link href="css/style.css" rel="stylesheet">
<title>Welcome</title>
</head>
<body>
	<h1>Upload file</h1>
	<%
		String parser = request.getParameter("parser");
		String ext = null;
		if (parser != null) {
	%>
	<h3>
		Parsing:
		<%=parser%></h3>
	<%
		if (parser.equals("xml2json")) {
				ext = ".json";
			} else {
				ext = ".xml";
			}
	%>

	<table>
		<tr>
			<td>Select a File to Load: <input type="file" id="fileToLoad">
				<button onclick="return loadFileAsText('<%=ext%>','fileToLoad')"  >Load Selected File</button>

			</td>
		</tr>
	</table>
	<form method="POST" action="uploadFile" onsubmit="return valid()">
		<textarea name=fileContent id="fileContent"
			style="width: 512px; height: 256px"></textarea>
		<input name="extension" value="<%=ext%>" hidden="true"> <input
			type="submit" value="Upload">Up
	</form>
	<%
		} else {
	%>

	<form method="POST" action="parsing">
		<table>
			<tr>
				<td>
					<fieldset>
						<legend>Parser</legend>
						XML-JSON <input type="radio" name="parser" value="xml2json" />
						JSON-XML <input type="radio" name="parser" value="json2xml" /> 
						<input type="submit" value="Upload" onclick="return radio()"> Press here to select the parser!
					</fieldset>
				</td>
			</tr>
		</table>
	</form>
	<%
		}
	%>
	<a href="./">Home</a>
</body>
</html>