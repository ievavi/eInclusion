<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="TableCSSCode.css" rel="stylesheet" type="text/css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<!--  
<script type="text/javascript" src="http://rbayliss.net/sites/default/files/js/js_yZYI52CYqKUA7iHsJ3TPDOAs6c3E4t3eNYj6CxvUbI8.js"></script>
-->

<script type="text/javascript" src="ddtf.js"></script>
<title>Insert title here</title>

<script type="text/javascript">
jQuery(document).ready(function () {
	  jQuery('#form_filtr').ddTableFilter();
	});
</script>

</head>
<body>
	<p>Menu</p>

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
<br />
<br />
	<%@ page import="org.einclusion.GUI.*"%>
	<%@ page import="java.util.ArrayList"%>
	<%@ page import="java.util.Iterator"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	
		<%
		PredictionTable prediction = new PredictionTable();
		prediction.readDBfiltered("Prediction", "Green");
		ArrayList<ArrayList<String>> list = prediction.list;
		request.setAttribute("list", list);
	%>
		
		<table id="form_filtr" class="CSSTableGenerator"><tbody>
		<tr>
			<th>Phone</th>
			<th>Topic</th>
			<th>Name</th>
			<th>M1</th>
			<th>M2</th>
			<th>M3</th>
			<th>Reliability</th>
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
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>