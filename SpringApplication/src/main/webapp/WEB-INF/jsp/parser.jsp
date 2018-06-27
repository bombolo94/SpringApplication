<%@ page contentType="text/html; charset=UTF-8"%>
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
		if (parser != null) {
	%>
	<div class="central">
		<h3>
			Parsing:
			<%=parser%></h3>

		<table>
			<tr>
				<td>Select a File to Load: <input type="file" id="fileToLoad"></td>
					<td><button onclick="return loadFile('fileToLoad')">Load File</button></td>

			</tr>
		</table>
		<form id="formParser" method="POST" action="uploadFile" onsubmit="return valid()">
			<div id="parserdiv"></div>
		</form>
		<a href="./">Home</a>
	</div>
	<%
		} else {
	%>
	<div class="central">
		<form id="formParser" method="POST" action="parsing"
			onclick=" return radioParsing()">
			<table>
				<tr>
					<td>
						<fieldset style="width: 300px">
							<legend>Parser</legend>
							XML-JSON <input type="radio" name="parser" value="xml2json" />
							JSON-XML <input type="radio" name="parser" value="json2xml" />
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