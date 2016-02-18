<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>General evaluation of student</title>
<link href="TableCSSCode.css" rel="stylesheet" type="text/css">
<!--  <link rel="stylesheet" type="text/css" href="theme.css"> -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js">	
</script>
<script type="text/javascript" src="ddtf.js"></script>

<!--  
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
-->
<script src="src/jquery.table2excel.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery('.CSSTableGenerator').ddTableFilter();
	});
</script>
</head>
<body>

	<%@ page import="org.webengine.*"%>
	<%@ page import="org.einclusion.GUI.*"%>
	<%@ page import="java.util.ArrayList"%>
	<%@ page import="java.util.Iterator"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%
		M1Table m1Table = new M1Table();
		m1Table.readDBfiltered("All", "All");
		ArrayList<ArrayList<String>> list = M1Table.list;
		request.setAttribute("list", list);
	%>

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

	<h1>General evaluation of student</h1>
	Name your file:
	<br />
	<!--  Execute java script as action -->
	<form action="M1.jsp" method="post" enctype="multipart/form-data">
		<input type="text" name="Students" size="30" /> <br /> <input
			type="submit" value="Export to xls" />
	</form>
	<button>Export</button>
	<script type="text/javascript">
		$("button").click(function() {
			$(".CSSTableGenerator").table2excel({
				exclude : ".noExl",
				name : "Excel Document Name",
				filename : "myFileName"
			});
		});
	</script>

	<table class="CSSTableGenerator">
		<tr>
			<th>Phone</th>
			<th>Topic</th>
			<th>Name</th>
			<th>Motivation</th>
			<th>Digital skills</th>
			<th>Learning ability</th>
			<th>E-materials</th>
			<th>Instructor</th>
			<th>E-environment</th>
			<th>Predicted usage</th>
			<th>Submit date</th>
			<th>M1</th>

		</tr>
		<c:forEach items="${list}" var="item">

			<tr>
				<td id="Nr"><c:out value="${item.get(0)}" /></td>
				<td><c:out value="${item.get(1)}" /></td>
				<td><c:out value="${item.get(2)}" /></td>
				<td><c:out value="${item.get(3)}" /></td>
				<td><c:out value="${item.get(4)}" /></td>
				<td><c:out value="${item.get(5)}" /></td>
				<td><c:out value="${item.get(6)}" /></td>
				<td><c:out value="${item.get(7)}" /></td>
				<td><c:out value="${item.get(8)}" /></td>
				<td><c:out value="${item.get(9)}" /></td>
				<td><c:out value="${item.get(10)}" /></td>
				<td><c:out value="${item.get(11)}" /></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>