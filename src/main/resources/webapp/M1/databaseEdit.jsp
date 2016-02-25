<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- start: Meta -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit database</title>
<meta charset="utf-8">
<meta name="description" content="Bootstrap Metro Dashboard">
<meta name="author" content="Dennis Ji">
<!-- end: Meta -->

<!-- start: CSS -->
<link id="bootstrap-style" href="css/bootstrap.min.css" rel="stylesheet">
<link id="base-style" href="css/style.css" rel="stylesheet">
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800&subset=latin,cyrillic-ext,latin-ext'
	rel='stylesheet' type='text/css'>
<!-- end: CSS -->


<!-- start: Favicon -->
<link rel="shortcut icon" href="img/favicon.ico">
<!-- end: Favicon -->
</head>

<body>
	<%@ page import="org.webengine.*"%>
	<%@ page import="org.einclusion.GUI.*"%>
	<%@ page import="java.util.ArrayList"%>
	<%@ page import="java.util.Iterator"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<!-- start: Header -->
	<div class="navbar">
		<div class="navbar-inner">
			<div>
				<a class="brand" href="databaseEdit.jsp"><span>Einclusion</span></a>
				<div class="nav-collapse sidebar-nav">
					<ul class="nav nav-tabs main-menu">
						<li><a href="databaseEdit.jsp"><i class="icon-edit"></i><span
								class="hidden-tablet"> Edit database</span></a></li>
						<li><a href="M1.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> M1</span></a></li>
						<li><a href="M2.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> M2</span></a></li>
						<li><a href="M3.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> M3</span></a></li>
						<li><a href="prediction.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> Prediction</span></a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<!-- start: Header -->

	<div>
		<div>
			<noscript>
				<div class="alert alert-block span10">
					<h4 class="alert-heading">Warning!</h4>
					<p>
						You need to have <a href="http://en.wikipedia.org/wiki/JavaScript"
							target="_blank">JavaScript</a> enabled to use this site.
					</p>
				</div>
			</noscript>

			<div id="content" class="span10">
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="databaseEdit.jsp">Home</a>
						<i class="icon-angle-right"></i></li>
					<li><a href="#">Edit database</a></li>
				</ul>
				<div>
					<h1>Edit database</h1>
					<div>>
						<div class="box-content">
							<table class="upload">

								<h3>Select a file to upload:</h3>
								<form method="post" action="/fileupload"
									enctype="multipart/form-data">
									Choose file: <input type="file" name="file"> <input
										type="submit" value="Upload" name="uploadFile">
								</form>
								<input type="submit" name="button1" value="Open uploaded file" /><br />
								</br>
								<h3>Select file to update database parameters</h3>
								<form method="post" action="/fileupload"
									enctype="multipart/form-data">
									Choose file: <input type="file" name="upload">
									<input type="submit" value="Update" name="uploadFile">
								</form>
								<br>
										<form method="get"
											action="${pageContext.request.contextPath}/fileupload"
											enctype="multipart/form-data">
											<input type="submit" name="button4" value="Example" /><br />
											<input type="submit" name="button2" value="Create Template" />
											<input type="submit" name="button3" value="Open Template" /><br />
											<input type="submit" name="button5" value="Export all data" />
											<input type="submit" name="button6" value="Open exported data" /><br />
										</form>
								</tr>

								</tr>

								</td>
								</tr>

								<tr>
									<td><%@ page import="org.einclusion.GUI.EditDatabasePanel"%>
										<%@ page import="java.util.TreeSet"%>
										<%@ page import="java.util.ArrayList"%>
										 <%@ taglib
											prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
										<%
											//public static String specificsParam;
											TreeSet<String> topics = new TreeSet();
											ArrayList<String> specifics = new ArrayList<String>();
											
											
											EditDatabase editPanel = new EditDatabase();
											
											editPanel.treeSetTopics.clear();
											topics.clear();

											editPanel.getTopics(editPanel.conn, editPanel.stmt);
											topics = editPanel.treeSetTopics;
											specifics = editPanel.specificsList;
											topics.add("All");
											String selectedTopic = editPanel.topicParameter;
											for (String topic : topics)
												System.out.println("topic #######################################" + topic);
											request.setAttribute("topics", topics);
											request.setAttribute("specifics", specifics);

											//  TODO read header if (header != refresh) refresh the page
											//	response.setIntHeader("Refresh", 0);
											//response.setIntHeader("topic", 0);
										%>
										<tr> <h2> Delete from database </h2> </tr>
										<tr>
										<form method="get" action="${pageContext.request.contextPath}/fileupload"
											enctype="multipart/form-data" name="f1">
											<select name="topic">
												<c:forEach items="${topics}" var="item">
													<option value="${item}">${item}</option>
												</c:forEach>
											</select>
											 <input type="submit" name="deleteButton" value="Refresh names" />
										</form>
										
										<form method="get" action="${pageContext.request.contextPath}/fileupload"
											enctype="multipart/form-data">
											<select name="specific">
												<c:forEach items="${specifics}" var="item">
													<option value="${item}" selected="${selectedTopic}">${item}</option>
												</c:forEach>
<%-- 												<option selected="selected" value="<%=%>">${item}</option> --%>
											</select>
											 <input type="submit" name="deleteFromDatabase" value="Delete from database" />
										</form>
										</tr>
										</td>
								</tr>

							</table>
							
							<label>Coefficients</label>
							<%ArrayList<ArrayList<String>> list = WebTable.coef();
							request.setAttribute("list", list);%>
							<table class="table table-striped table-bordered">
								<thead>
									<tr>
										<th>Key</th>
										<th>Coefficient</th>
										<th>Relative</th>
										<th>Value</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${list}" var="item">
										<tr>
											<td><c:out value="${item.get(0)}" /></td>
											<td><c:out value="${item.get(1)}" /></td>
											<td><c:out value="${item.get(2)}" /></td>
											<td><c:out value="${item.get(3)}" /></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>


				</div>
			</div>
		</div>
	</div>

	<!-- start: JavaScript-->
	<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js">
	</script>
	<script type="text/javascript" src="ddtf.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery('.table').ddTableFilter();
		});
	</script>

		<!-- end: JavaScript-->
</body>
</html>
