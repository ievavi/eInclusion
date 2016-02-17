<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>General evaluation of student</title>
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
	<p>
		<%
			/*M1.getAmountOfClusters("Robotika", "M1");*/
		%>
	</p>



	<h1>General evaluation of student</h1>
	Name your file:
	<br />
<!--  Execute java script as action -->
	<form action="index.jsp" method="post" enctype="multipart/form-data">
	<input type="text" name="Students" size="30" />
	<br />
	<input type="submit" value="Export to xls" />
</form>
<p>Filter</p>
<form action="index.jsp" method="post">
	<select>
	<option value="item1">item1</option>
	<option value="item2">item2</option>
	<%
		//Java code to parse through possible categories
	%>
	</select>
	<select>
	<option value="item1">item1</option>
	<option value="item2">item2</option>
	<%
		//Java code to parse through possible categories
	%>
	</select>
	<input type="submit" value="Apply" />
</form>
<!--  <table style="width:100%"> -->
<%
	M1Table m1Table = new M1Table();
	m1Table.readDBfiltered("All", "All");
	ArrayList<ArrayList<String>> list = M1Table.list;
	request.setAttribute("list", list);
	System.out.println(list.get(0).get(0));
%>

 
<%
 	java.util.Collection<String> coll = new java.util.ArrayList<String>();
 	coll.add("111");
 	coll.add("222");
 	request.setAttribute("coll", coll);
 %>
 
The value of attribute is = ${coll} <br></br>
 
The way to access using indes is = ${coll[0]} and ${coll[1]}<br></br>
 
The values got from looping is as follows -
<c:forEach var="nameObj" items="${coll}">
<c:out value="${nameObj}"></c:out> 
</c:forEach>


 <table>
  <c:forEach items="${list}" var="item">
    <tr>
      <td><c:out value="${item}" /></td>
    </tr>
  </c:forEach>
</table>


</body>
</html>