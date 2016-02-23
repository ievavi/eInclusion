<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="TableCSSCode.css" rel="stylesheet" type="text/css">
<title>Edit database</title>
</head>
<body>
	<%@ page import="org.webengine.*"%>
	<table class="tg">
		<tr>
			<th class="tg-yw4l"><a href="index.jsp">Main Page</a></th>
			<th class="tg-yw4l"><a href="databaseEdit.jsp">DatabaseEdit</a></th>
			<th class="tg-yw4l"><a href="M1.jsp">M1</a></th>
			<th class="tg-yw4l"><a href="M2.jsp">M2</a></th>
			<th class="tg-yw4l"><a href="M3.jsp">M3</a></th>
			<th class="tg-yw4l"><a href="prediction.jsp">Prediction</a></th>
		</tr>
	</table>

	<h2>Database Edit</h2>
	<table class="upload">
		<tr>
		<td>
	Select a file to upload:
	<br />
	<form method="post" action="/fileupload" enctype="multipart/form-data">
		Choose file <input type="file" name="file"> 
		<input type="submit" value="submit">
	</form>
 
	<form method="get" action="${pageContext.request.contextPath}/fileupload" enctype="multipart/form-data">
	<tr>
	<td>
		<input type="submit" name="button1" value="Open CSV file" /> 
		</td>
		</tr>
			<tr>
	<td>
		<input type="submit" name="button2" value="Create Template" /> 
		</td>
			<td>
		<input type="submit" name="button3" value="Open Template" /> 
		</td>
		</tr>
					<tr>
	<td>
		<input type="submit" name="button4" value="Example" /> 
		</td>
		</tr>
				</td>
		</tr>
					<tr>
	<td>
		<input type="submit" name="button5" value="exportAllData" /> 
		</td>
		</tr>
				</tr>
					<tr>
	<td>
		<input type="submit" name="button6" value="openExported" /> 
		</td>
		</tr>
	</form>

	</td>
	</tr>
	
	<tr><td>
	<form method="post" action="/fileupload" enctype="multipart/form-data">
		Choose file2 <input type="file" name="upload"> 
		<input type="submit" value="submit">
	</form>
	</td></tr>
	
	</table>
	

</body>
</html>