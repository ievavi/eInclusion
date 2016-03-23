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

  <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
  <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
  <script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

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
							<form id="formXls" class="form-horizontal" action="prediction.jsp"
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

							<form id="formFromToDate" method="get"
								enctype="multipart/form-data">
								<table align="center">
								<tr>
								<td>
								<div style="background-color: #f9f9f9; border:1px solid; padding: 5px; border-color: #eee;">
									<%
										String fieldFrom = request.getParameter("from");
										if (fieldFrom == null) {
											fieldFrom=""; 
										}
										
										String fieldTo = request.getParameter("to");
										if (fieldTo == null) {
												fieldTo=""; 
										}
									%>
									<label for="rangeFilter">Filter by particular date
										interval</label>
									<div>
										<label for="from" style="display:inline-block; width:100px;">From* (Clickable)</label> <input type="text"
											id="from" style="width: 63px" name="from" value=<%=fieldFrom%>>
									</div>

									<div>
										<label for="to" style="display:inline-block; width:100px;">To* (Clickable)</label> <input type="text"
											id="to" style="width: 63px" name="to" value=<%=fieldTo%>>
									</div>
									<button type="submit" style="width: 179px"
										class="btn btn-primary">Submit ranged date filter</button>
								</div>
								</td>



								
								<td>
								<div style="background-color: #f9f9f9;border:1px solid; padding: 5px; border-color: #eee;">
									<label for="rangeFilter">Filter by particular M2
										interval</label>
									<div>
										<%
											String fieldFromM2 = request.getParameter("fromM2");
											if (fieldFromM2 == null) {
												fieldFromM2=""; 
											}
											
											String fieldToM2 = request.getParameter("toM2");
											if (fieldToM2 == null) {
												fieldToM2=""; 
											}
										%>
										<label for="fromM2" style="display:inline-block; width:100px;">From* (0-100)%</label> <input type="text"
											id="fromM2" name="fromM2"
											onkeypress="return isNumberKey(event)" onchange="correctValueM(this)" maxlength=3
											style="width: 63px" value=<%=fieldFromM2%>>
									</div>

									<div>
										<label for="toM2" style="display:inline-block; width:100px;">To* (0-100)%</label> <input type="text"
											id="toM2" name="toM2" onkeypress="return isNumberKey(event)" onchange="correctValueM(this)"
											maxlength=3 style="width: 63px" value=<%=fieldToM2%>>
									</div>
									<button type="submit" style="width: 179px"
										class="btn btn-primary">Submit ranged M2 filter</button>
								</div>
								</td>



								<td>
								<div style="background-color: #f9f9f9; border:1px solid; padding: 5px; border-color: #eee;">
									<label for="rangeFilterM3">Filter by particular M3
										interval</label>
									<div>
										<%
											String fieldFromM3 = request.getParameter("fromM3");
											if (fieldFromM3 == null) {
												fieldFromM3=""; 
											}
											
											String fieldToM3 = request.getParameter("toM3");
											if (fieldToM3 == null) {
												fieldToM3=""; 
											}
										%>
										<label for="fromM3" style="display:inline-block; width:100px;">From* (0-100)%</label> <input type="text"
											id="fromM3" name="fromM3" onchange="correctValueM(this)"
											onkeypress="return isNumberKey(event)" maxlength=3
											style="width: 63px" value=<%=fieldFromM3%>>
									</div>

									<div>
										<label for="toM3" style="display:inline-block; width:100px;">To* (0-100)%</label> <input type="text"
											id="toM3" name="toM3" onkeypress="return isNumberKey(event)" onchange="correctValueM(this)"
											maxlength=3 style="width: 63px" value=<%=fieldToM3%>>
									</div>
									<button type="submit" style="width: 179px"
										class="btn btn-primary">Submit ranged M3 filter</button>
								</div>
							</td>
							
							</tr>
							</table>
							</form>
							<form>
							<div align="right">
							<button type="button" onclick="location.href = 'prediction.jsp';" style="width: 120px"
										 class="btn btn-primary">Remove all filters</button>
							</div>
							</form>





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
										<c:if
											test="${(param.from == null || param.to == null || param.from == '' || param.to == '')
													|| item.get(3) >= param.from &&  item.get(3) <= param.to}">
											<c:if
												test="${(param.fromM2 == null || param.toM2 == null || param.fromM2 == '' || param.toM2 == '')
														|| item.get(5) >= param.fromM2 &&  item.get(5) <= param.toM2}">
												<c:if
													test="${(param.fromM3 == null || param.toM3 == null || param.fromM3 == '' || param.toM3 == '')
															|| item.get(6) >= param.fromM3 &&  item.get(6) <= param.toM3}">
													<tr class="toshow">
														<td id="Nr"><c:out value="${item.get(0)}" /></td>
														<td><c:out value="${item.get(1)}" /></td>
														<td><c:out value="${item.get(2)}" /></td>
														<td><c:out value="${item.get(3)}" /></td>
														<fmt:parseNumber var="m1" type="number"
															value="${item.get(4)}" />
														<c:choose>
															<c:when test="${m1 == 2}">
																<td class="green colored" onclick="showM1('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();">><c:out value="${m1}" /></td>
															</c:when>
															<c:when test="${m1 == 1}">
																<td class="yellow colored" onclick="showM1('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();">><c:out value="${m1}" /></td>
															</c:when>
															<c:when test="${m1 == 0}">
																<td class="red colored" onclick="showM1('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();">><c:out value="${m1}" /></td>
															</c:when>
															<c:otherwise>
																<td class="gray colored" onclick="showM1('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="${m1}" /></td>
															</c:otherwise>
														</c:choose>
														<fmt:parseNumber var="m2" type="number"
															value="${item.get(5)}" />
														<c:choose>
															<c:when test="${m2 > 60}">
																<td class="green colored" onclick="showM2('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();">><c:out value="${m2}" /></td>
															</c:when>
															<c:when test="${m2 > 25}">
																<td class="yellow colored" onclick="showM2('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();">><c:out value="${m2}" /></td>
															</c:when>
															<c:when test="${m2 > 0}">
																<td class="red colored" onclick="showM2('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="${m2}" /></td>
															</c:when>
															<c:otherwise>
																<td class="gray colored" onclick="showM2('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="${m2}" /></td>
															</c:otherwise>
														</c:choose>
														<fmt:parseNumber var="m3" type="number"
															value="${item.get(6)}" />
														<c:choose>
															<c:when test="${m3 > 60}">
																<td class="green colored" onclick="showM3('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="${m3}" /></td>
															</c:when>
															<c:when test="${m3 > 25}">
																<td class="yellow colored" onclick="showM3('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="${m3}" /></td>
															</c:when>
															<c:when test="${m3 > 0}">
																<td class="red colored" onclick="showM3('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="${m3}" /></td>
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
												</c:if>
											</c:if>
										</c:if>
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
	


	<!-- start: JavaScript DATEPICKERRANGE-->
	<script type="text/javascript">
		$(function() {
			$("#from").datepicker({
				changeMonth : true,
				numberOfMonths : 1,
				dateFormat: "yy-mm-dd",
				onClose : function(selectedDate) {
					$("#to").datepicker("option", "minDate", selectedDate);
				}
			});
			$("#to").datepicker({
				changeMonth : true,
				numberOfMonths : 1,
				dateFormat: "yy-mm-dd",
				onClose : function(selectedDate) {
					$("#from").datepicker("option", "maxDate", selectedDate);
				}
			});
		});
	</script>
	
	<script type="text/javascript">
	function isNumberKey(evt){
	    var charCode = (evt.which) ? evt.which : event.keyCode
	    if (charCode > 31 && (charCode < 48 || charCode > 57))
	        return false;
        if(evt.value<0) evt.value = 0;
        if(evt.value > 100) evt.value = 100;
	    return true;
	}
	</script>

	<script type="text/javascript">
	function correctValueM(evt){
        if(evt.value<0) evt.value = 0;
        if(evt.value > 100) evt.value = 100;
	    return true;
	}
	</script>

	<!-- start: JavaScript-->
	<script src="src/jquery.table2excel.js"></script>
	<script type="text/javascript">
		$("#formXls").submit(function() {
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
