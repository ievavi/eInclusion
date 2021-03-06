<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- start: Meta -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Einclusion</title>
<meta name="description" content="Bootstrap Metro Dashboard">
<meta name="author" content="Dennis Ji">
<!-- end: Meta -->

<!-- start: CSS -->
<link id="bootstrap-style" href="M1/css/bootstrap.min.css"
	rel="stylesheet">
<link id="bootstrap-style" href="M1/css/bootstrap.min.css"
	rel="stylesheet">
<link id="base-style" href="M1/css/style.css" rel="stylesheet">
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800&subset=latin,cyrillic-ext,latin-ext'
	rel='stylesheet' type='text/css'>
<!-- end: CSS -->


<!-- start: Favicon -->
<link rel="shortcut icon" href="img/favicon.ico">
<!-- end: Favicon -->

</head>

<body>
	<!-- start: Header -->
	<div style="color: highlighttext;" class="nav-tabs ">
		<div class="navbar-inner ">
			<div>
				<a class="img-rounded text-right" href="index.jsp"> <img
					src="logo.png" alt="logo" /></a>
				<div class="nav-header text-center">
					<ul class="nav nav-tabs main-menu animate ">
						<li><a href="M1/databaseEdit.jsp"><i class="icon-edit"></i><span
								class="hidden-tablet text-center"> Edit database</span></a></li>
						<li><a href="M1/M1.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> M1</span></a></li>
						<li><a href="M1/M2.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> M2</span></a></li>
						<li><a href="M1/M3.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> M3</span></a></li>
						<li><a href="M1/prediction.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> Prediction</span></a></li>
						<li><a href="M1/report.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> Report</span></a></li>
						
						<li><a class="a" href="M1/coefficients.jsp"><i
								class="icon-table"></i><span class="hidden-tablet">
									Coefficients </span></a></li>
									<li><a style="visibility:hidden" href="M1/ReadMe.pdf" target="_blank"><i
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
				<div>
					<h1 align="center" style="color: aqua;">
						<a href="index.jsp ">Einclusion </a>
					</h1>
					<div>
						<div align="center" class="text-info" style="color: graytext;">
							<descrip> <br />
							<p>
								The purpose of this system is predicting behavior of students,
								studying digital skills, after completing course.<br /> <br />
								It gives instructor an opportunity to recognize students who
								would fail to use the skills taught in course after completion.
								It helps to determine risk factors for each of the students and
								allows the instructor to avert them by adjusting the studies to
								the needs of each student.<br /> <br /> In the scope of this
								system, an e-included student is such a student that after
								course completion applies the skills taught in it.
							</p>
							<br />
							<h2 align="center" style="color: aqua;">
								<a href="M1/databaseEdit.jsp ">Edit database </a>
							</h2>
							<p align="center">Helps to add new students to database and
								update database, also provides export all data to .xlsx format
								and also provides with creating template. In this page you can
								also refresh names and delete all content from database.</p>
							<br />
							<h2 align="center" style="color: aqua;">
								<a href="M1/M1.jsp ">M1 </a>-- <a href="M1/M2.jsp ">M2 </a>-- <a
									href="M1/M3.jsp ">M3 </a>
							</h2>
							<p align="center">Shows each students factors for calculating
								coefficients and calculated coefficients.</p>
							<br />
							<h2 align="center" style="color: aqua;">
								<a href="M1/prediction.jsp ">Prediction </a>
							</h2>
							<p align="center">Shows e-inclusion level achieved by student.</p>
							<br />
							<h2 align="center" style="color: aqua;">
								<a href="M1/report.jsp ">Report charts </a>
							</h2>
							<p align="center">First diagram shows the number of students
								for each e-inclusion range for specified date and topic. Second
								diagram shows the number of students in each topic for specified
								date period.</p>
							<br />
							<h2 align="center" style="color: aqua;">
								<a href="M1/coefficients.jsp ">Coefficients</a>
							</h2>
							<p align="center">Table Coefficients is available in the
								lower part of the Edit Database page. It contains the list of
								all the coefficients used to compute predicted values. It can be
								filtered by field Key.</p>


							</descrip>
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
	<script src="M1/src/jquery.table2excel.js"></script>
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
	<script type="text/javascript" src="M1/ddtf.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery('.table').ddTableFilter();
		});
	</script>
	<script type="text/javascript" src="M1/ddtfc.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery('.table').ddTableFilterColor();
		});
	</script>
	<!-- end: JavaScript-->
</body>
</html>
