<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>General evaluation of student</title>
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
		//Einclusion.main(null);
		///M1 m = new M1(); 
		EditDatabase e = new EditDatabase();
	%>
	<%
		M1Table m1Table = new M1Table();
		m1Table.readDBfiltered("M1", "Green");
		ArrayList<ArrayList<String>> list = M1Table.list;
		request.setAttribute("list", list);
	%>

	<h1>General evaluation of student</h1>
	Name your file:
	<br />
	<!--  Execute java script as action -->
	<form action="index.jsp" method="post" enctype="multipart/form-data">
		<input type="text" name="Students" size="30" /> <br /> <input
			type="submit" value="Export to xls" />
	</form>
	<p>Filter</p>
	<form action="index.jsp" method="post">
	
	<!-- <select multiple="multiple" name="prodSKUs">
	    <c:forEach items="${list}" var="productSubCategoryList">
	        <option value="${list}" ${not empty productSubCategoryMap[list] ? 'selected' : ''}>${list}</option>
	    </c:forEach>
	</select>
		 -->
		<select>
			<option value="item1">item1</option>
			<option value="item2">item2</option>
			<%
				//Java code to parse through possible categories
			%>
		</select> <input type="submit" value="Apply" />
	</form>
	<!--  <table style="width:100%"> -->
	

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