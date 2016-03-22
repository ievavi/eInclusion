<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- start: Meta -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Prediction of student E-inclusion</title>
<meta name="description" content="Bootstrap Metro Dashboard">
<meta name="author" content="Dennis Ji">
<!-- end: Meta -->

<!-- start: Mobile Specific -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- end: Mobile Specific -->

<!-- start: CSS -->
<link id="bootstrap-style" href="css/bootstrap.min.css" rel="stylesheet">
<link id="base-style" href="css/style.css" rel="stylesheet">
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800&subset=latin,cyrillic-ext,latin-ext'
	rel='stylesheet' type='text/css'>
	
	<link rel="stylesheet" href="https://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
	
	<style>
#dialogoverlay{
	display: none;
	opacity: .5;
	position: fixed;
	top: 0px;
	left: 0px;
	background: #FFF;
	width: 100%;
	z-index: 100;
}
#dialogbox{
	display: none;
	position: fixed;
	background: #000;
	border-radius:8px; 
	width:500px;
	z-index: 100;
}
#dialogbox > div{ background:#FFF; margin:4px; }
#dialogbox > div > #dialogboxhead{ background: white ; font-size:19px; padding:5px; color:black; }
#dialogbox > div > #dialogboxbody{ background:#777; font-size:15px; padding:20px; color:#FFF; }
#dialogbox > div > #dialogboxfoot{ background: #C0C0C0 ; padding:5px; text-align:right; }

.DefStyle
		    {
		    	position: fixed;
		    	top: 40%;
				left: 50%;
		    	font-family:Verdana;
		    	color:Gray;
		    	font-size:12px;
		    	border-width:1px;
		    	border-color:Black;
		    	border-style:outset;
				width: 120px;
		    	padding:5px,5px,5px,5px,5px;
		    	display:none;
		    }
</style>
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
	<%
		WebTable prediction = new PredictionWeb();
		prediction.readDBfiltered("All", "All");
		ArrayList<ArrayList<String>> list = PredictionWeb.list;
		request.setAttribute("list", list);
	%>
	
	<%
		WebTable m1Table = new M1Web();
		m1Table.readDBfiltered("All", "All");
		ArrayList<ArrayList<String>> listM1 = M1Web.list;
		request.setAttribute("listM1", listM1);
	%>
	<%
		WebTable m2Table = new M2Web();
		m2Table.readDBfiltered("All", "All");
		ArrayList<ArrayList<String>> listM2 = M2Web.list;
		request.setAttribute("listM2", listM2);
	%>
	<%
		WebTable m3Table = new M3Web();
		m3Table.readDBfiltered("All", "All");
		ArrayList<ArrayList<String>> listM3 = M3Web.list;
		request.setAttribute("listM3", listM3);
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
table
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
					<li><a href="#">Prediction</a></li>
				</ul>
				<div>
					<h1>Prediction of student E-inclusion</h1>
					<div>
						<div class="box-header"></div>
						<div class="box-content">
							<form class="form-horizontal" action="prediction.jsp"
								method="post" enctype="multipart/form-data">
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
							
							<label>From</label><input type="text" id="one" ></input>
							<label>To</label><input type="text" id="two" ></input>
							<label>Name</label>
							<select id="mySelect" multiple="multiple" size="1">
							</select>
							<button type="submit" onclick="search();">Search</button>
							
							<label><font color='#55cc55'><b>Green</b> </font> -
								included, <font color='ffdd54'> <b>Yellow</b></font> - partly
								included, <font color='#ff6654'> <b>Red</b></font> - not
								included</label>
								<div class="DefStyle" id="Definition">Click to see results</div>
							<table class="table table-striped table-bordered">
								<thead>
									<tr>
										<th>Phone</th>
										<th>Topic</th>
										<th>Name</th>
										<th>Submit date</th>
										<th class="colored">M1</th>
										<th class="colored">M2</th>
										<th class="colored">M3</th>
										<th>Reliability</th>

									</tr>
								</thead>
								<tbody>
									<c:forEach items="${list}" var="item">
										<tr class="toshow">
											<td id="Nr"><c:out value="${item.get(0)}" /></td>
											<td><c:out value="${item.get(1)}" /></td>
											<td><c:out value="${item.get(2)}" /></td>
											<td><c:out value="${item.get(3)}" /></td>
											<fmt:parseNumber var="m1" type="number" 
												value="${item.get(4)}" />
											<c:choose>
												<c:when test="${m1 == 2}">
													<td class="green colored"><c:out value="${m1}" /></td>
												</c:when>
												<c:when test="${m1 == 1}">
													<td class="yellow colored"><c:out value="${m1}" /></td>
												</c:when>
												<c:when test="${m1 == 0}">
													<td class="red colored"><c:out value="${m1}" /></td>
												</c:when>
												<c:otherwise>
													<td class="gray colored" onclick="showM1('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="${m1}" /></td>
												</c:otherwise>
											</c:choose>
											<fmt:parseNumber var="m2" type="number"
												value="${item.get(5)}" />
											<c:choose>
												<c:when test="${m2 > 60}">
													<td class="green colored"><c:out value="${m2}" /></td>
												</c:when>
												<c:when test="${m2 > 25}">
													<td class="yellow colored"><c:out value="${m2}" /></td>
												</c:when>
												<c:when test="${m2 > 0}">
													<td class="red colored"><c:out value="${m2}" /></td>
												</c:when>
												<c:otherwise>
													<td class="gray colored" onclick="showM2('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="${m2}" /></td>
												</c:otherwise>
											</c:choose>
											<fmt:parseNumber var="m3" type="number"
												value="${item.get(6)}" />
											<c:choose>
												<c:when test="${m3 > 60}">
													<td class="green colored"><c:out value="${m3}" /></td>
												</c:when>
												<c:when test="${m3 > 25}">
													<td class="yellow colored"><c:out value="${m3}" /></td>
												</c:when>
												<c:when test="${m3 > 0}">
													<td class="red colored"><c:out value="${m3}" /></td>
												</c:when>
												<c:otherwise>
													<td class="gray colored" onclick="showM3('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="${m3}" /></td>
												</c:otherwise>
											</c:choose>
											<c:set var="pred" value="${item.get(7)}" />
											<c:choose>
												<c:when test="${pred == 'High'}">
													<td class="green"><c:out value="${pred}" /></td>
												</c:when>
												<c:when test="${pred == 'Medium'}">
													<td class="yellow"><c:out value="${pred}" /></td>
												</c:when>
												<c:when test="${pred =='Low'}">
													<td class="red"><c:out value="${pred}" /></td>
												</c:when>
												<c:otherwise>
													<td class="gray"><c:out value="${pred}" /></td>
												</c:otherwise>
											</c:choose>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>

				
				</div>
			</div>
		</div>
		<p id="testing"></p>
		<div class="clearfix"></div>

	</div>
	<div id="dialogoverlay"></div>
<div id="dialogbox">
  <div>
    <div id="dialogboxhead"></div>
    <div id="dialogboxbody"></div>
    <div id="dialogboxfoot"></div>
  </div>
</div>

	
	<!-- start: JavaScript-->
								
	<script>
	var a = new Array();
	var co = 0;
	<c:forEach items="${list}" var="itemM1">
	a[co++] = '<c:out value="${itemM1.get(2)}" />';
	</c:forEach>
	var select= document.getElementById('mySelect');
	for(names in a)
		select.add(new Option(a[names]));
	
	function search()
	{
		$('tr').attr('class','tos');
		$('.tos').show();
		var counter = 0;
		var one = document.getElementById("one").value;
		var two = document.getElementById("two").value;
		
		<c:forEach items="${list}" var="itemM1">
		a = '<c:out value="${itemM1.get(3)}" />';
		counter++;
		if(!(a >= one && a <= two))
		{
			
			//var b = '<c:out value="${itemM1.get(0)}" />';
			//var c = '<c:out value="${itemM1.get(1)}" />';
			//var d = '<c:out value="${itemM1.get(2)}" />';
			//var e = b + " " + c + " " + d; 
			//arr[counter++] = e;
			$('tr:nth-child('+counter+')').attr('class','tod')
			//$('.donotshow').css('display', 'none');
		}
		</c:forEach>
		$('.tod').hide();
		//for(i = 0; i< arr.length; i++)
			//alert(arr[i]);
	
			
		//var one = document.getElementById("one").value;
		//document.getElementById("body_table").style.display = "none";
		
	}
	
	var name_of_person;
	var results;
	
	function ShowDef()
	{
	    document.getElementById("Definition").style.display="block";
	}
	function HideDef()
	{
		document.getElementById("Definition").style.display="none";
	}
	
	function CustomAlert(){
	    this.render = function(dialog){
	        var winW = window.innerWidth;
	        var winH = window.innerHeight;
	        var dialogoverlay = document.getElementById('dialogoverlay');
	        var dialogbox = document.getElementById('dialogbox');
	        dialogoverlay.style.display = "block";
	        dialogoverlay.style.height = winH+"px";
	        dialogbox.style.left = (winW/2) - (550 * .5)+"px";
	        dialogbox.style.top = "100px";
	        dialogbox.style.display = "block";
	        document.getElementById('dialogboxhead').innerHTML = results + " results for: " + name_of_person;
	        document.getElementById('dialogboxbody').innerHTML = dialog;
	        document.getElementById('dialogboxfoot').innerHTML = '<button onclick="Alert.ok()">OK</button>';
	    }
		this.ok = function(){
			document.getElementById('dialogbox').style.display = "none";
			document.getElementById('dialogoverlay').style.display = "none";
		}
	}
	var Alert = new CustomAlert();
	
		function showM1(topic, name)
		{
			<c:forEach items="${listM1}" var="itemM1">
			var top = '<c:out value="${itemM1.get(1)}" />';
			var nam = '<c:out value="${itemM1.get(2)}" />';
			if(topic == top && name == nam){	
			var m = '<c:out value="${itemM1.get(3)}" />';
			var ds = '<c:out value="${itemM1.get(4)}" />';
			var la = '<c:out value="${itemM1.get(5)}" />';
			var e = '<c:out value="${itemM1.get(6)}" />';
			var i = '<c:out value="${itemM1.get(7)}" />';
			var ee = '<c:out value="${itemM1.get(8)}" />';
			var pu = '<c:out value="${itemM1.get(9)}" />';
			}
			</c:forEach>
			name_of_person = name;
			results = "M1";
			Alert.render("<ul style=\"list-style-type:none\">" + "<li>" + "motivation: " + m + "<li>" + "digital skills: " + ds + "<li>" + "learning ability: " + la + "<li>" + "e-materials: " + e + "<li>" + "instructor: " + i + "<li>" + "e-environment: " + ee + "<li>" + "predicted usage: " + pu + "<ul>");
			//Alert.render("<ul>"+"motivation: "+m+"<\ul>"+"<ul>"+"digital skills: "+ds+"<\ul>"+"<ul>"+"learning ability: "+la+"<\ul>"+"<ul>"+"e-materials: "+e+"<\ul>"+"<ul>"+"instructor: "+i+"<\ul>"+"<ul>"+"e-environment: "+ee+"<\ul>"+"<ul>"+"predicted usage: "+pu+"<\ul>");
			//alert("motivation: "+m+"\n"+"digital skills: "+ds+"\n"+"learning ability: "+la+"\n"+"e-materials: "+e+"\n"+"instructor: "+i+"\n"+"e-environment: "+ee+"\n"+"predicted usage: "+pu);
		}
		
		function showM2(topic, name)
		{
			<c:forEach items="${listM2}" var="itemM2">
			var top = '<c:out value="${itemM2.get(1)}" />';
			var nam = '<c:out value="${itemM2.get(2)}" />';
			if(topic == top && name == nam){	
			var m = '<c:out value="${itemM2.get(3)}" />';
			var la = '<c:out value="${itemM2.get(4)}" />';
			var em = '<c:out value="${itemM2.get(5)}" />';
			var ee = '<c:out value="${itemM2.get(6)}" />';
			var i = '<c:out value="${itemM2.get(7)}" />';
			}
			</c:forEach>
			name_of_person = name;
			results = "M2";
			Alert.render("<ul style=\"list-style-type:none\">" + "<li>" + "motivation: " + m + "<li>" + "learning ability: " + la + "<li>" + "e-materials: " + em + "<li>" + "e-environment: " + ee + "<li>" + "instructor: " + i + "<ul>");
			//alert("motivation: "+m+"\n"+"learning ability: "+la+"\n"+"e-materials: "+em+"\n"+"e-environment: "+ee+"\n"+"instructor: "+i);
		}
		
		function showM3(topic, name)
		{
			<c:forEach items="${listM3}" var="itemM3">
			var top = '<c:out value="${itemM3.get(1)}" />';
			var nam = '<c:out value="${itemM3.get(2)}" />';
			if(topic == top && name == nam){	
			var i = '<c:out value="${itemM3.get(3)}" />';
			var ee = '<c:out value="${itemM3.get(4)}" />';
			var em = '<c:out value="${itemM3.get(5)}" />';
			var bl = '<c:out value="${itemM3.get(6)}" />';
			}
			</c:forEach>
			name_of_person = name;
			results = "M3";
			Alert.render("<ul style=\"list-style-type:none\">" + "<li>" + "instructor: " + i + "<li>" + "e-environment: " + ee + "<li>" + "e-materials: " + em + "<li>" + "before learning: " + bl + "<ul>");
			//alert("instructor: "+i+"\n"+"e-environment: "+ee+"\n"+"e-materials: "+em+"\n"+"before learning: "+bl);
		}
	</script>
	
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
			jQuery('.table').ddTableFilter();
		});
	</script>
	<script type="text/javascript" src="ddtfc.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery('.table').ddTableFilterColor();
		});
	</script>
	<!-- end: JavaScript-->
</body>
</html>
