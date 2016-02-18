<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Evaluation of ability to learn and knowledge sharing</title>
<link href="TableCSSCode.css" rel="stylesheet" type="text/css">
<!--  <link rel="stylesheet" type="text/css" href="theme.css"> -->
</head>
<body>

	<%@ page import="org.webengine.*"%>
	<%@ page import="org.einclusion.GUI.*"%>
	<%@ page import="java.util.ArrayList"%>
	<%@ page import="java.util.Iterator"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	<%
		//EditDatabase e = new EditDatabase();
	%>
	<%
		M2Table m2Table = new M2Table();
		m2Table.readDBfiltered("M2", "Green");
		ArrayList<ArrayList<String>> list = M1Table.list;
		request.setAttribute("list", list);
	%>

	<h1>Evaluation of ability to learn and knowledge sharing</h1>
	Name your file:
	<br />
	<!--  Execute java script as action -->
	<form action="index.jsp" method="post" enctype="multipart/form-data">
		<input type="text" name="Students" size="30" /> <br /> <input
			type="submit" value="Export to xls" />
	</form>
	<p>Filter</p>
	<form action="index.jsp" method="post">
		<select>
			<option value="item1">item1</option>
			<option value="item2">item2</option>
			<%
				//Java code to parse through possible categories
			%>
		</select> <input type="submit" value="Apply" />
	</form>
	

	<table class="CSSTableGenerator">
		<tr>
			<th>Nr</th>
			<th>Phone</th>
			<th>Topic</th>
			<th>Name</th>
			<th>Motivation</th>
			<th>Learning ability</th>
			<th>E-materials</th>
			<th>E-environment</th>
			<th>Instructor</th>
			<th>Submit date</th>
			<th>M2</th>
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