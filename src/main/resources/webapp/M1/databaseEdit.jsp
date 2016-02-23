<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- start: Meta -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit database</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js">
	
</script>
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
					<div>
						<div class="box-header" data-original-title></div>
						<div class="box-content">
							<table class="upload">
								<tr>
									<td>Select a file to upload: <br />
										<form method="post" action="/fileupload"
											enctype="multipart/form-data">
											Choose file (create) <input type="file" name="file">
											<input type="submit" value="submit">
										</form>

										<form method="post" action="/fileupload"
											enctype="multipart/form-data">
											Choose file (update) <input type="file" name="upload">
											<input type="submit" value="submit">
										</form>

										<form method="get"
											action="${pageContext.request.contextPath}/fileupload"
											enctype="multipart/form-data">
											<input type="submit" name="button1" value="Open CSV file" /><br />
											<input type="submit" name="button2" value="Create Template" />
											<input type="submit" name="button3" value="Open Template" /><br />
											<input type="submit" name="button4" value="Example" /><br />
											<input type="submit" name="button5" value="exportAllData" /><br />
											<input type="submit" name="button6" value="openExported" /><br />
										</form>
								</tr>

								</tr>

								</td>
								</tr>

								<tr>
									<td></td>
								</tr>

							</table>
						</div>
					</div>


				</div>
			</div>
		</div>

		<div class="modal hide fade" id="myModal">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">Ã</button>
				<h3>Settings</h3>
			</div>
			<div class="modal-body">
				<p>Here settings can be configured...</p>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn" data-dismiss="modal">Close</a> <a href="#"
					class="btn btn-primary">Save changes</a>
			</div>
		</div>

		<div class="clearfix"></div>

		<footer>

		<p>
			<span style="text-align: left; float: left; display: none">&copy;
				2013 <a href="http://jiji262.github.io/Bootstrap_Metro_Dashboard/"
				alt="Bootstrap_Metro_Dashboard">Bootstrap Metro Dashboard</a>
			</span>

		</p>

		</footer>

		<!-- start: JavaScript-->

		<script src="js/jquery-1.9.1.min.js"></script>
		<script src="js/jquery-migrate-1.0.0.min.js"></script>

		<script src="js/jquery-ui-1.10.0.custom.min.js"></script>
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
				jQuery('.table').ddTableFilter();
			});
		</script>
		<!-- end: JavaScript-->
</body>
</html>
