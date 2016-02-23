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
			<td>Select a file to upload: <br />
				<form method="post" action="/fileupload"
					enctype="multipart/form-data">
					Choose file <input type="file" name="file"> <input
						type="submit" value="submit">
				</form>

				<form method="get"
					action="${pageContext.request.contextPath}/fileupload"
					enctype="multipart/form-data">
					<tr>
						<td><input type="submit" name="button1" value="Open CSV fail" />
						</td>
					</tr>
					<tr>
						<td><input type="submit" name="button2"
							value="Create Template" /></td>
						<td><input type="submit" name="button3" value="Open Template" />
						</td>
					</tr>
				</form> <!-- 				<form method="post" action="/fileupload" --> <!-- 					enctype="multipart/form-data"> -->
				<!-- 					Choose file <input type="file" name="file"> <input --> <!-- 						type="submit" value="submit"> -->
				<!-- 				</form> -->
		<tr>
			<td><input type="submit" name="button4" value="Update Database" />
			</td>
			<td><input type="submit" name="button5" value="Example" /></td>
		</tr>
		</td>
		</tr>
	</table>
	<%@ page import="org.einclusion.GUI.EditDatabasePanel"%>
	<%@ page import="java.util.TreeSet"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%
		EditDatabasePanel editPanel = new EditDatabasePanel();
// 		editPanel.getTopics();
		editPanel.getTopics(editPanel.conn, editPanel.stmt);
		TreeSet<String> topics = editPanel.treeSetTopics;
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + topics.size());
		for(String topic : topics) System.out.println("topic #######################################"+topic);
		request.setAttribute("topics", topics);
	%>
<form method="get" action="databaseEdit.jsp" enctype="multipart/form-data">	
<select name="topic">
		<c:forEach items="${topics}" var="item">
			<option value="${item}">${item}</option>
		</c:forEach>
	</select>
	<input type="submit" name="deleteButton" value="Search names" />
</form>
<!-- <select name="birthday_month"> -->
<%--     <c:forEach items="${topics}" var="topic"> --%>
<%--         <option value="${topic.key}" ${topic.key == selectedMonth ? 'selected' : ''}>${topic.value}</option> --%>
<%--     </c:forEach> --%>
<!-- </select> -->


</body>
</html>