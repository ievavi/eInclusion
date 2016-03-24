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
						<li><a href="ReadMe.pdf" target="_blank"><i
								class="icon-table"></i><span class="hidden-tablet">
									Instructions </span></a></li>
						<li><a href="coefficients.jsp"><i
								class="icon-table"></i><span class="hidden-tablet">
									Coefficients </span></a></li>
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

			<div id="content" class="input-xxlarge"
			style="width: 85%; margin: auto;">
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="index.html">Home</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#">Report</a></li>
				</ul>
				
				<div>
				<h1 class="text-success" align="center">Report diagrams</h1> <br>
				<div>
					<div class="box-header"></div>
					<div class="box-content" align="center">
					<div>
					<table>
						<tr align="left">
							<td style="width:120px; font-size: 12pt">From:</td>
							<td style="width:120px; font-size: 12pt">To:</td>
							<td style="width:120px; font-size: 12pt">Topic:</td>
						</tr>
					</table>
					</div>
						<form name="form1" method="get">
   							<div style="display:inline;">
	   							<select name="from" id="from" style="width:120px">
	   								<c:forEach items="${dates}" var="from">
									<option>${from}</option>
									</c:forEach>
					    		</select>
				    			<select name="to" id="to" style="width:120px">
			   						<c:forEach items="${dates}" var="to">
									<option>${to}</option>
									</c:forEach>
							    </select>
								    <select name="topic" id="topic" style="width:120px">
						    		<option selected>All</option>
		   							<c:forEach items="${topics}" var="topic">
									<option>${topic}</option>
									</c:forEach>
							    </select>    
 				    		</div>
 				    		<p><input class="btn btn-primary" type="button" value="Draw diagram" onclick="f1()" style="width:120px"/></p>
   						</form>
					</div>
				</div>
				</div>
				
				<div id="images" align="center">
					<img src="" id="image1" />
					<img src="" id="image2" />
				</div>
			</div>
		</div>
<script type="text/javascript">
function f1() {
 		var from ="&from="+document.getElementById("from").value;
 		var to ="&to="+document.getElementById("to").value;
 		var topic = "&topic="+document.getElementById("topic").value;
 		var request1 = '/report?type=image1';
 		var request2 = '/report?type=image2';
 		request1 = request1.concat(from,to,topic);
 		request2 = request2.concat(from,to,topic);
 		document.getElementById("image1").src=request1;
 		document.getElementById("image2").src=request2;
	}
</script>

</body>
</html>