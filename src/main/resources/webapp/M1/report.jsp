<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- start: Meta -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Report page</title>
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
<%@ page import="java.util.TreeSet"%>
<%@ page import="java.util.Iterator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	WebTable table = new PredictionWeb();
	table.readDBfiltered("All", "All");
	ArrayList<ArrayList<String>> list = PredictionWeb.list;
	TreeSet <String> dates = new TreeSet<String>();
	TreeSet <String> topics = new TreeSet<String>();
	for(ArrayList<String> i: list) {
		topics.add(i.get(1));
		dates.add(i.get(3));
	}
	request.setAttribute("dates", dates);
	request.setAttribute("topics", topics);
	
%>
	
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
						<li><a href="report.jsp"><i class="icon-table"></i><span
								class="hidden-tablet"> Report</span></a></li>
						<li><a href="ReadMe.pdf" target="_blank"><i class="icon-table"></i><span
								class="hidden-tablet"> Instructions </span></a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<!-- start: Header -->
	
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
					<li><i class="icon-home"></i> <a href="index.html">Home</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#">M1</a></li>
				</ul>
				
				<div>
					<h1>Report charts</h1>
					<div>
						<div class="box-header"></div>
						<div class="box-content">
							<form class="form-horizontal" action="M1.jsp" method="post"
								enctype="multipart/form-data">
								<fieldset>
									<div class="control-group">
										<label class="control-label" for="focusedInput">Name
											your file: </label>
										<div class="controls">
											<input name="Students" class="input-xlarge focused"
												id="focusedInput" type="text">
											<button type="submit" class="btn btn-primary">Export
												to xls</button>
										</div>
									</div>
								</fieldset>
							</form>
							
						</div>
					</div>
				</div>
				<form action="/M1/prediction.jsp" method="get">
    				<input type="submit" value="< Back">
				</form>
				<form action="" method="get">
   					<div style="display:inline;">
	   					<select name="from" class="from">
	   						<c:forEach items="${dates}" var="from">
							<option>${from}</option>
							</c:forEach>
					    </select>
					    <select name="from" class="from">
	   						<c:forEach items="${dates}" var="to">
							<option>${to}</option>
							</c:forEach>
					    </select>
					    <select name="from" class="from">
	   						<c:forEach items="${topics}" var="topic">
							<option>${topic}</option>
							</c:forEach>
					    </select>
					    
				    </div>
   					<p><input type="button" value="Select" onclick="addParameters()"></p>
  				</form>
				
				<div>
					<img src="/report?type=image1" alt="Chart #1" />
				</div>
				<div>
					<img src="/report?type=image2" alt="Chart #2" />
				</div>
			</div>
		</div>

	<script type="text/javascript">
		function addParameters() {
			
		}
	</script>
</body>
</html>