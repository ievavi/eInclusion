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
<link id="base-style" href="css/jasny-bootstrap.min.css"
	rel="stylesheet">
<link id="base-style" href="css/style.css" rel="stylesheet">
<link id="base-style" href="css/jasny-bootstrap.css" rel="stylesheet">
<link rel="stylesheet"
	href="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.core.css">
<link rel="stylesheet"
	href="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.default.css">
<script src="js/extensions.js"></script>
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
	<%@ page import="java.util.ArrayList"%>
	<%@ page import="java.util.Iterator"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<!-- start: Header -->
	<div style="color: highlighttext;" class="nav-tabs ">
		<div class="navbar-inner ">
			<div>
				<a class="img-rounded text-right" href="../index.jsp"> <img
					src="../logo.png" alt="logo" /></a>
				<div class="nav-header text-center">
					<ul class="nav nav-tabs main-menu animate ">
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
						<li><a href="report.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> Report</span></a></li>
						
						<li><a href="coefficients.jsp"><i
								class="icon-table"></i><span class="hidden-tablet">
									Coefficients </span></a></li>
									<li><a style="visibility:hidden" href="ReadMe.pdf" target="_blank"><i
								class="icon-table"></i><span class="hidden-tablet">
									Instructions </span></a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<!-- startonsubmit="return Validate(this);: Header -->

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

		<noscript>
			<!-- <div class="alert alert-error">
				<h4 class="alert-heading">Warning!</h4>
				<p>
					You need to have <a href="http://en.wikipedia.org/wiki/JavaScript"
						target="_blank">JavaScript</a> enabled to use this site.
				</p>
			</div> -->
		</noscript>

		<div id="content" class="input-xxlarge"
			style="width: 85%; margin: auto;">
			<ul class="breadcrumb">
				<li><i class="icon-home"></i> <a href="databaseEdit.jsp">Home</a>
					<i class="icon-angle-right"></i></li>
				<li"><a href="#">Edit database</a></li>
			</ul>

			<h1 class="text-success" align="center">Edit database</h1>

			<div style="text-align: center">
				<table>

					<h3 class="text-info" align="center">Select a file to upload:</h3>
					<div class="fileinput fileinput-new" data-provides="fileinput">
						<form method="post" action="/fileupload"
							enctype="multipart/form-data">
							<span class="btn btn-default btn-file"><span
								class="fileinput-new">Select file</span><span
								class="fileinput-exists">Change</span><input type="file"
								name="file" onchange="ValidateSingleInput(this);"></span> <span
								class="fileinput-filename"></span> <a href="#"
								class="close fileinput-exists" data-dismiss="fileinput"
								style="float: none">&times;</a> <input type="submit"
								value="Upload" name="uploadFile" 
								class="btn btn-primary fileinput-exists " >
						</form "> 
					</div>
					</br>
					<div class="fileinput fileinput-new" data-provides="fileinput">

						<h3 class="text-info" align="center">Select file to update
							database parameters</h3>

						<form method="post" action="/fileupload"
							enctype="multipart/form-data">
							<span class="btn btn-default btn-file"><span
								class="fileinput-new">Select file to train model</span><span
								class="fileinput-exists">Change</span><input type="file"
								name="file2" onchange="ValidateSingleInput2(this);""></span> <span
								class="fileinput-filename"></span> <a href="#"
								class="close fileinput-exists" data-dismiss="fileinput"
								style="float: none">&times;</a> <input type="submit"
								value="Upload" name="uploadFile2"
								class="btn btn-primary fileinput-exists" >
						</form>
					</div>
					<br>
					<div class="fileinput fileinput-new" data-provides="fileinput">
						<form method="get"
							action="${pageContext.request.contextPath}/fileupload"
							enctype="multipart/form-data">
							<input type="submit" name="createOPENTemplate"
								value="Create/OPEN Template" class="btn btn-navbar  ">
						</form>
						<form method="get"
							action="${pageContext.request.contextPath}/fileupload"
							enctype="multipart/form-data">
							<input type="submit" name="exportData"
								value="Export and open all data" class="btn btn-primary">
						</form>
					</div>
					</tr>

					</tr>

					</td>
					</tr>

					<tr>
						<td>
							<%@ page import="java.util.TreeSet"%> <%@ page
								import="java.util.ArrayList"%> <%@ taglib
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
								request.setAttribute("selectedTopic", selectedTopic);

								//  TODO read header if (header != refresh) refresh the page
								//	response.setIntHeader("Refresh", 0);
								//response.setIntHeader("topic", 0);
							%>
					<tr>
						<h2 align="center">Delete from database</h2>
					</tr>
					<tr>
						<br />
						<form method="get"
							action="${pageContext.request.contextPath}/fileupload"
							enctype="multipart/form-data" name="f1">
							<select name="topic">
								<c:forEach items="${topics}" var="item">
									<option value="${item}"
										${item == selectedTopic ? 'selected="selected"' : ''}>${item}</option>
								</c:forEach>
							</select> <input type="submit" name="deleteButton" value="Refresh names"
								class="btn btn-navbar  " />
						</form>
						<p>&nbsp&nbsp</p>
						<form method="get"
							action="${pageContext.request.contextPath}/fileupload"
							enctype="multipart/form-data">
							<select>
								<c:forEach items="${specifics}" var="item">
									<option value="${item}" selected="${selectedTopic}">${item}</option>
								</c:forEach>
								<%-- 												<option selected="selected" value="<%=%>">${item}</option> --%>
							</select> <input class="btn btn-primary" type="submit"
								name="deleteFromDatabase" value="Delete from database"
								onClick="return confirm(
  'Are you sure you want to delete all data?');" />
						</form>
					</tr>
					</td>

					</tr>
				</table>

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
				<!-- Latest compiled and minified CSS -->
				<link rel="stylesheet"
					href="//cdnjs.cloudflare.com/ajax/libs/jasny-bootstrap/3.1.3/css/jasny-bootstrap.min.css">

				<!-- Latest compiled and minified JavaScript -->
				<script
					src="//cdnjs.cloudflare.com/ajax/libs/jasny-bootstrap/3.1.3/js/jasny-bootstrap.min.js"></script>
				<!-- Latest compiled and minified CSS -->
				<link rel="stylesheet"
					href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css">

				<!-- Latest compiled and minified JavaScript -->
				<script
					src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>

				<!-- (Optional) Latest compiled and minified JavaScript translation files -->
				<script
					src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/i18n/defaults-*.min.js"></script>
				<script src="bower_components/jquery/dist/jquery.min.js"></script>
				<script src="//code.jquery.com/jquery-1.11.2.min.js"></script>
				<script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>

				<!-- end: JavaScript-->
</body>
</html>
