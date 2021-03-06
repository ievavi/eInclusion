<%@page import="java.util.Arrays"%>
<%@page import="org.apache.jasper.tagplugins.jstl.ForEach"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- start: Meta -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Coefficients</title>
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
								<li><a href="coefficients.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> Coefficients </span></a></li>
						<li><a style="visibility:hidden" href="ReadMe.pdf" target="_blank"><i
								class="icon-table"></i><span class="hidden-tablet">
									Instructions </span></a></li>
						
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

				<div id="content" class="input-xxlarge"
			style="width: 85%; margin: auto;">
			<ul class="breadcrumb">
				<li><i class="icon-home"></i> <a href="report.jsp">Home</a> <i
					class="icon-angle-right"></i></li>
				<li"><a href="#">Coefficients</a></li>
			</ul>
		<div>
					<h1 class="text-success" align="center">Prediction of student
						E-inclusion</h1>
					<div>
						<div class="box-header"></div>
						<div class="box-content" align="center">
							<form class="form-horizontal" action="prediction.jsp"
								method="post" enctype="multipart/form-data">
								<fieldset>
									<label class="text-success ui-icon-disk" for="focusedInput">Name
										your file: </label> <input name="Students"
										class="input-xlarge focused" id="focusedInput" type="text">
									<button type="submit" class="btn btn-primary">Export
										to xls</button>
						</div>
					</div>

					</fieldset>
					</form>
			<h1 class="text-success" align="center">Coefficients</h1>

				<% 
				ArrayList<ArrayList<String>> list = WebTable.coef();
					request.setAttribute("list", list);
				%>
				<table class="table table-striped table-bordered">
					<thead>
						<tr>
							
							<th class=skip-filter>Coefficient</th>
							<th class=skip-filter>Relative</th>
							<th class=skip-filter>Values of centroids, clasters and linear regression</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="user" items="${list}">
							<tr>
								
								<td><c:out value="${user.get(1)}" /></td>
								<td><c:out value="${user.get(2)}" /></td>
								<td><c:out value="${user.get(3)}" /></td>
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
		<script src="src/jquery.table2excel.js"></script>
	<script type="text/javascript">
		$("form").submit(function() {
			var n = $("input:first").val()
			$(".table").table2excel({
				exclude : ".noExl",
				name : "Excel Document Name",
				filename : n
			});
		});
	</script>
	<script type="text/javascript" src="ddtf.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery('table').ddTableFilter();
		});
	</script>

	<!-- end: JavaScript-->
</body>
</html>
