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
<<<<<<< HEAD
<link id="bootstrap-style" href="M1/css/bootstrap.min.css" rel="stylesheet">
=======
<link id="bootstrap-style" href="M1/css/bootstrap.min.css"
	rel="stylesheet">
>>>>>>> 4fa002c8d263aead91211a49bae36af297df8dfc
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
	<div class="navbar">
		<div class="navbar-inner">
			<div>
<<<<<<< HEAD
				<a class="brand" href="databaseEdit.jsp"><span>Einclusion</span></a>
=======
				<a class="brand" href="M1/databaseEdit.jsp"><span>Einclusion</span></a>
>>>>>>> 4fa002c8d263aead91211a49bae36af297df8dfc
				<div class="nav-collapse sidebar-nav">
					<ul class="nav nav-tabs main-menu">
						<li><a href="M1/databaseEdit.jsp"><i class="icon-edit"></i><span
								class="hidden-tablet"> Edit database</span></a></li>
						<li><a href="M1/M1.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> M1</span></a></li>
						<li><a href="M1/M2.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> M2</span></a></li>
						<li><a href="M1/M3.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> M3</span></a></li>
						<li><a href="M1/prediction.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> Prediction</span></a></li>
<<<<<<< HEAD
=======
						<li><a href="ReadMe.pdf" target="_blank"><i class="icon-table"></i><span
								class="hidden-tablet"> Instructions </span></a></li>
>>>>>>> 4fa002c8d263aead91211a49bae36af297df8dfc
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

<<<<<<< HEAD
			<div id="content" class="span10">
				<div>
					<h1>Einclusion</h1>
					<div>
						<div class="box-header"></div>
						<div class="box-content">
							<h3>Einclusion is a web based tool for predicting student E-inclusion from survey answers.
							Sistēmas mērķis ir  prognozēt studentu, kuri apgūst digitālās prasmes, uzvedību pēc mācību kursa pabeigšanas.
							Sistēma ļauj pasniedzējam jau mācību kursa norises laikā konstatēt problemātiskos studentus, tas ir, studentus,
							kuri kursā mācīto neizmantos pēc kursa pabeigšanas. Sistēma ļauj noteikt, kādi ir riska faktori katram no šiem
							studentiem un tas dod iespēju pasniedzējam tos novērst, pielāgojot mācību procesu konkrētam studentam.
							Šīs sistēmas kontekstā e-iekļauts students ir tāds students, kurš pēc kursa apguves lieto jaunapgūtās
							digitālās prasmes.</h3>
=======
			<div id="content" class="span10" style="width: 65%">
				<div>
					<h1>Einclusion</h1>
					<div>
						<div class="box-content">
							<descrip>
							<br />
							The purpose of this system is predicting behavior of students,
							studying digital skills, after completing course.<br />
							<br />
							It gives instructor an opportunity to recognize students who
							would fail to use the skills taught in course after completion.
							It helps to determine risk factors for each of the students and
							allows the instructor to avert them by adjusting the studies to
							the needs of each student.<br />
							<br />
							In the scope of this system, an e-included student is such a
							student that after course completion applies the skills taught in
							it. </descrip>
>>>>>>> 4fa002c8d263aead91211a49bae36af297df8dfc
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>


<<<<<<< HEAD
	<!-- start: JavaScript-->	
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js">	
=======
	<!-- start: JavaScript-->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js">
		
>>>>>>> 4fa002c8d263aead91211a49bae36af297df8dfc
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
