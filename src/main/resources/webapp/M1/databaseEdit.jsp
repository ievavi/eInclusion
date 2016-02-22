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


	Select a file to upload:
	<br />
	    <form method="post" action="/fileupload" enctype="multipart/form-data">  
        Choose file <input type="file" name="file">
        <input type="submit" value="submit">

    </form>
	<br>
	<br>

</body>
</html>