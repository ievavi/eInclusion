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
	<%@ page import="org.einclusion.GUI.*"%>
	<%@ page import="java.util.ArrayList"%>
	<%@ page import="java.util.Iterator"%>




	<p>Menu</p>

	<table class="tg">
		<tr>
			<th class="tg-yw4l"><a href="index.jsp">Main Page</a></th>
			<th class="tg-yw4l"><a href="databaseEdit.jsp">DatabaseEdit</a></th>
			<th class="tg-yw4l"><a href="m1.jsp">M1</a></th>
			<th class="tg-yw4l"><a href="m2.jsp">M2</a></th>
			<th class="tg-yw4l"><a href="m3.jsp">M3</a></th>
			<th class="tg-yw4l"><a href="prediction.jsp">Prediction</a></th>
		</tr>
	</table>

	<h2>Database Edit</h2>

	<%
		M1Table m1Table = new M1Table();
		m1Table.readDBfiltered("All", "All");
		ArrayList<ArrayList<String>> list = M1Table.list;
		request.setAttribute("list", list);
		System.out.println(list.get(0).get(0));
	%>



	Select a file to upload:
	<br />
	<form action="EditDatabase.jsp" method="post"
		enctype="multipart/form-data">
		<input type="file" name="file" size="50" /> <br /> <input
			type="submit" value="Upload File" />
	</form>

</body>
</html>