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
#dialogoverlay {
	display: none;
	opacity: .5;
	position: fixed;
	top: 0px;
	left: 0px;
	background: #FFF;
	width: 100%;
	z-index: 100;
}

#dialogbox {
	display: none;
	position: fixed;
	background: #000;
	border-radius: 8px;
	width: 500px;
	z-index: 100;
}
#dialogbox > div{ background:#FFF; margin:4px; }
#dialogbox > div > #dialogboxhead{ background: white ; font-size:19px; padding:5px; color:black; }
#dialogbox > div > #dialogboxbody{ background:#777; font-size:15px; padding:20px; color:#FFF; }
#dialogbox > div > #dialogboxfoot{ background: #C0C0C0 ; padding:5px; text-align:right; }

.DefStyle
		    {
		    	position: fixed;
		    	top: 100px;
				left: 100px;
		    	font-family:Verdana;
		    	color:Gray;
		    	font-size:12px;
		    	border-width:1px;
		    	border-color:Black;
		    	border-style:outset;
				width: 60px;
		    	padding:5px,5px,5px,5px,5px;
		    	background-color:white;
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
	 <%
	 WebTable prW = new PredictionWeb();
	 prW.readDBfiltered("All", "All");
	 ArrayList<String> koef = PredictionWeb.coef11();
	 request.setAttribute("koef", koef);
	 %>
	 
	  <%
	 WebTable prWSAL = new PredictionWeb();
	  prWSAL.readDBfiltered("All", "All");
	 ArrayList<String> koefSAL = PredictionWeb.coefSAL();
	 request.setAttribute("koefSAL", koefSAL);
	 %>
	 
	  <%
	 WebTable prWELM = new PredictionWeb();
	 prWELM.readDBfiltered("All", "All");
	 ArrayList<String> koefELM = PredictionWeb.coefELM();
	 request.setAttribute("koefELM", koefELM);
	 %>
	 
	  <%
	 WebTable prWELE = new PredictionWeb();
	 prWELE.readDBfiltered("All", "All");
	 ArrayList<String> koefELE = PredictionWeb.coefELE();
	 request.setAttribute("koefELE", koefELE);
	 %>
	 
	   <%
	 WebTable prM2 = new PredictionWeb();
	 prM2.readDBfiltered("All", "All");
	 ArrayList<String> koefM2 = PredictionWeb.coefM2();
	 request.setAttribute("koefM2", koefM2);
	 %>
	 
	    <%
	String koefMM = WebTable.coef5();
	 request.setAttribute("koefMM", koefMM);
	 %>
	 
	   <%
	 WebTable prM1 = new PredictionWeb();
	 prM2.readDBfiltered("All", "All");
	 String koefM1 = PredictionWeb.coefM1();
	 request.setAttribute("koefM1", koefM1);
	 %>
	 
	   <%
	 WebTable prDS = new PredictionWeb();
	 prDS.readDBfiltered("All", "All");
	 String koefDS = PredictionWeb.coefDS();
	 request.setAttribute("koefDS", koefDS);
	 %>
	
	  <%
	 WebTable prLA = new PredictionWeb();
	 prLA.readDBfiltered("All", "All");
	 String koefLA = PredictionWeb.coefLA();
	 request.setAttribute("koefLA", koefLA);
	 %>
	 
	   <%
	 WebTable prE = new PredictionWeb();
	 prE.readDBfiltered("All", "All");
	 String koefE = PredictionWeb.coefE();
	 request.setAttribute("koefE", koefE);
	 %>
	 
	   <%
	 WebTable prI = new PredictionWeb();
	 prI.readDBfiltered("All", "All");
	 String koefI = PredictionWeb.coefI();
	 request.setAttribute("koefI", koefI);
	 %>
	 
	  <%
	 WebTable prEE = new PredictionWeb();
	 prEE.readDBfiltered("All", "All");
	 String koefEE = PredictionWeb.coefEE();
	 request.setAttribute("koefEE", koefEE);
	 %>
	 
	  <%
	 WebTable prP = new PredictionWeb();
	 prP.readDBfiltered("All", "All");
	 String koefP = PredictionWeb.coefP();
	 request.setAttribute("koefP", koefP);
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
				style="width: 55%; margin: auto;">
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="databaseEdit.jsp">Home</a>
						<i class="icon-angle-right"></i></li>
					<li><a href="#">Prediction</a></li>
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
										<label for="fromM2" style="display:inline-block; width:50px;">From* (0-100)%</label> <input type="text"
											id="fromM2" name="fromM2"
											onkeypress="return isNumberKey(event)" onchange="correctValueM(this)" maxlength=3
											style="width: 63px" value=<%=fieldFromM2%>>
									</div>

									<div>
										<label for="toM2" style="display:inline-block; width:50px;">To* (0-100)%</label> <input type="text"
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
										<label for="fromM3" style="display:inline-block; width:50px;">From* (0-100)%</label> <input type="text"
											id="fromM3" name="fromM3" onchange="correctValueM(this)"
											onkeypress="return isNumberKey(event)" maxlength=3
											style="width: 63px" value=<%=fieldFromM3%>>
									</div>

									<div>
										<label for="toM3" style="display:inline-block; width:50px;">To* (0-100)%</label> <input type="text"
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





							<label>Precision level: <font color='#55cc55'> <b>Green</b> </font> -
								high (e-included), <font color='#ff6654'> <b>Red</b></font> - high (not e-included), 
								<font color='#FFCD01'> <b>Yellow</b></font> - low </label>
									<label>Prediction: <font color='#55cc55'><b>Green</b> </font> - e-included, <font color='#ff6654'> <b>Red</b></font> - not e-included</label>

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
										<th class="colored">Prediction</th>
										<th  class="colored">Precision</th>

									</tr>
								</thead>
								<tbody>
									<c:forEach items="${list}" var="item">
									<fmt:parseNumber var="m3" type="number"	value="${item.get(6)}" />
										<fmt:parseNumber var="m1" type="number" value="${item.get(4)}" />
										<fmt:parseNumber var="m2" type="number"	value="${item.get(5)}" />
										<fmt:parseNumber var="vote" type="number"	value="${item.get(8)}" />
										<!-- fmt:parseNumber var="m1ValueRisk" type="number"	value=0 /-->
										
										<c:if
											test="${(param.from == null || param.to == null || param.from == '' || param.to == '')
													|| item.get(3) >= param.from &&  item.get(3) <= param.to}">
											<c:if
												test="${(param.fromM2 == null || param.toM2 == null || param.fromM2 == '' || param.toM2 == '')
														|| m2 >= param.fromM2 &&  m2 <= param.toM2}">
												<c:if
													test="${(param.fromM3 == null || param.toM3 == null || param.fromM3 == '' || param.toM3 == '')
															|| m3 >= param.fromM3 &&  m3 <= param.toM3}">
										
										
													<tr class="toshow">
														<td id="Nr"><c:out value="${item.get(0)}" /></td>
														<td><c:out value="${item.get(1)}" /></td>
														<td><c:out value="${item.get(2)}" /></td>
														<td><c:out value="${item.get(3)}" /></td>
														
													<c:choose>
												<c:when test="${vote == 2.00}">
													<td class="green colored"><c:out
															value="" /></td>
															<!--td class="green colored"><c:out
															value="${item.get(8)}" /></td-->
															<c:set var="m1ValueRisk" value="1" scope="session" />
												</c:when>
												<c:when test="${vote == 0.00}">
													<!-- td class="red colored"><c:out value="${item.get(8)}" /></td>  -->
													<td class="red colored"><c:out value="" />
													<c:set var="m1ValueRisk" value="1" scope="session" />
													</td> 
													 
													  
												</c:when>
												<c:otherwise>
													<!--<td class="gray colored"><c:out value="${item.get(9)}" /></td> -->
												<td class="gray colored"><c:out value="" /></td>
												
												</c:otherwise>
											</c:choose>
											</c:if></c:if></c:if>
														<!-- c:if test="${m1ValueRisk==0}"-->
														<c:choose>
															<c:when test="${m1 == 2 && vote == 2.00 }">
															
															
																<td class="green colored" onclick="showM1('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="" /></td>
															
															</c:when>
															
															<c:when test="${vote == 0.00 }">
																<td class="gray colored" onclick="showM1('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="" /></td>
															</c:when>
															
																<c:when test="${m1 == 0 && m2 == 2 }">
																<td class="gray colored" onclick="showM1('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="" /></td>
															</c:when>
															
															<c:otherwise>
																<td class="gray colored" onclick="showM1('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="" /></td>
															</c:otherwise>
														</c:choose>
														<!-- /c:if -->
														<c:choose>
															<c:when test="${m2 > 80 && m1 == 2 &&vote==2.00}">
																<td class="green colored" onclick="showM2('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="" /></td>
															</c:when>
															<c:when test="${m1==0 &&vote==0.00 }">
																<td class="gray colored" onclick="showM2('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="" /></td>
															</c:when>
															
															
															
															
															<c:when test="${vote==0.00}">
																<td class="gray colored" onclick="showM2('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="" /></td>
															</c:when>
															
															
															<c:otherwise>
																<td class="red colored" onclick="showM2('<c:out value="${item.get(1)}" />','<c:out value="${item.get(2)}" />')" onmouseout="HideDef();" onmouseover="ShowDef();"><c:out value="" /></td>
															</c:otherwise>
														</c:choose>
														
														<c:choose>
															
															<c:when test="${vote==2.00 && m1==2 && m2>80}">
																<td class="green"><c:out value="No risk" /></td>
															</c:when>
															
															
															<c:otherwise>
																<td class="red"><c:out value="Risk" /></td>
															</c:otherwise>
														</c:choose>
														
														<c:set var="pred" value="${item.get(8)}" />
														<c:choose>
															<c:when test="${vote==0.00}">
																<td class="red"><c:out value="High" /></td>
															</c:when>
															<c:when test="${vote==2.00 && m1==2 && m2>80}">
																<td class="green"><c:out value="High" /></td>
															</c:when>
															
															<c:when test="${vote==2.00 && m1==0}">
																<td class="orange"><c:out value="Medium" /></td>
															</c:when>
															<c:when test="${vote==2.00 && m1==2 && m2<81}">
																<td class="yellow"><c:out value="Low" /></td>
															</c:when>
															<c:otherwise>
																<td class="gray"><c:out value="" /></td>
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
	
	function ShowDef(e)
	{
		var posX = e.clientX + "px";
		var posY = e.clientY + "px";
		document.getElementById("Definition").style.left = posX;
		document.getElementById("Definition").style.top = posY;
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
			
			<c:forEach var="aaa1" items="${koefM1}">
			var aaa2 = '<c:out value="${aaa1}" />';
			</c:forEach>
			
			<c:forEach var="ds1" items="${koefDS}">
			var ds2 = '<c:out value="${ds1}" />';
			</c:forEach>
			
			<c:forEach var="la1" items="${koefLA}">
			var la2 = '<c:out value="${la1}" />';
			</c:forEach>
			
			<c:forEach var="e1" items="${koefE}">
			var e2 = '<c:out value="${e1}" />';
			</c:forEach>
			
			<c:forEach var="i1" items="${koefI}">
			var i2 = '<c:out value="${i1}" />';
			</c:forEach>
			
			<c:forEach var="ee1" items="${koefEE}">
			var ee2 = '<c:out value="${ee1}" />';
			</c:forEach>
			
			<c:forEach var="p1" items="${koefP}">
			var p2 = '<c:out value="${p1}" />';
			</c:forEach>
			
			
			<c:forEach items="${listM1}" var="itemM1">
			var top = '<c:out value="${itemM1.get(1)}" />';
			var nam = '<c:out value="${itemM1.get(2)}" />';
			if(topic == top && name == nam){	
			var m = '<c:out value="${itemM1.get(3)}" />';
			var ds = '<c:out value="${itemM1.get(4)}" />';
			var la = '<c:out value="${itemM1.get(5)}" />';
			var e = '<c:out value="${itemM1.get(6)}" />';
			var i = '<c:out value="${itemM1.get(7)}" />';
			var ee ='<c:out value="${itemM1.get(8)}" />';
			var pu = '<c:out value="${itemM1.get(9)}" />';
			
			var m1Value = ( parseFloat(m)-1)/( parseFloat(aaa2)-1)*100;
			
			var dsValue = ( parseFloat(ds)-1)/( parseFloat(ds2)-1)*100;
			var laValue = ( parseFloat(la)-1)/( parseFloat(la2)-1)*100;
			var eValue = ( parseFloat(e)-1)/( parseFloat(e2)-1)*100;
			var iValue = ( parseFloat(i)-1)/( parseFloat(i2)-1)*100;
			var eeValue = ( parseFloat(ee)-1)/( parseFloat(ee2)-1)*100;
			var puValue = ( parseFloat(pu)-1)/( parseFloat(p2)-1)*100;
			}
			</c:forEach>
			
           
			
		
			
			name_of_person = name;
			results = "M2";
			Alert.render("<ul style=\"list-style-type:none\">" + "<li>" + "motivation: " + m   + "<li>" + "e-included approximation: " +"<b>"+Math.round(m1Value)+"%"+"</b>"+"<li>" + "digital skills: " + ds + "<li>" + "e-included approximation: " +"<b>"+Math.round(dsValue)+"%"+"</b>"+ "<li>" + "learning ability: " + la + "<li>" + "e-included approximation: " +"<b>"+Math.round(laValue)+"%"+"</b>"+ "<li>" + "e-materials: " + e +"<li>" + "e-included approximation: " +"<b>"+Math.round(eValue)+"%"+"</b>"+  "<li>" + "instructor: " + i + "<li>" + "e-included approximation: " +"<b>"+Math.round(iValue)+"%"+"</b>"+ "<li>" + "e-environment: " + ee + "<li>" + "e-included approximation: " +"<b>"+Math.round(eeValue)+"%"+"</b>"+  "<li>" + "predicted usage: " + pu +  "<li>" + "e-included approximation: " +"<b>"+Math.round(puValue)+"%"+"</b>"+  "<ul>");
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
			
			
			<c:forEach items="${koef}" var="koef">
			
			var aaKoef = '<c:out value="${koef}" />';
			
			var bb = ((m*aaKoef-aaKoef*1)/((aaKoef*5)-(aaKoef*1)))*100;
			
			</c:forEach>
			
			
            <c:forEach items="${koefSAL}" var="koefSAL">
			var koefSALVal = '<c:out value="${koefSAL}" />';
			var koefSALValFrontend = ((la*koefSALVal-koefSALVal*1)/((koefSALVal*5)-(koefSALVal*1)))*100;
			
			</c:forEach>
			
			<c:forEach items="${koefELM}" var="koefELM">
			var koefELMVal = '<c:out value="${koefELM}" />';
			var koefELMValFrontend = ((em*koefELMVal-koefELMVal*1)/((koefELMVal*5)-(koefELMVal*1)))*100;
			
			</c:forEach>
			
			<c:forEach items="${koefELE}" var="koefELE">
			var koefELEVal = '<c:out value="${koefELE}" />';
			var koefELEValFrontend = ((ee*koefELEVal-koefELEVal*1)/((koefELEVal*5)-(koefELEVal*1)))*100;
			 
			</c:forEach>
			
		  var bb2 =  parseFloat(aaKoef)/( parseFloat(koefSALVal)+ parseFloat(koefELMVal)+ parseFloat(koefELEVal)+ parseFloat(aaKoef))*100;
		  var koefSAL2 = parseFloat(koefSALVal)/(parseFloat(koefSALVal)+parseFloat(koefELMVal)+parseFloat(koefELEVal)+parseFloat(aaKoef))*100;
		  var koefELM2 = parseFloat(koefELMVal)/(parseFloat(koefSALVal)+parseFloat(koefELMVal)+parseFloat(koefELEVal)+parseFloat(aaKoef))*100;
		  var koefELE2 = parseFloat(koefELEVal)/(parseFloat(koefSALVal)+parseFloat(koefELMVal)+parseFloat(koefELEVal)+parseFloat(aaKoef))*100;
			
			
			
			name_of_person = name;
			results = "M3";
			Alert.render(" <ul style=\"list-style-type:none\">" + "<li>" + "motivation: " + m+" " + "<li>" + "e-included approximation: "+"<b>"+bb+"%"+"</b>" +"<li>"+ "size of the effect: " +Math.round(bb2)+" "+"%"+"<li>" + "learning ability: " + la + "<li>" + "e-included aproximation: "+"<b>"+Math.round(koefSALValFrontend)+"</b>"+"%"  +"<li>"+ "size of the effect: " +Math.round(koefSAL2)+" "+"%"+"<li>" + "e-materials: " + em  + "<li>" + "e-included aproximation: "+"<b>"+Math.round(koefELMValFrontend)+"</b>"+"%"  +"<li>"+ "size of the effect: " +Math.round(koefELM2)+" "+"%"+"<li>" + "e-environment: " + ee +  "<li>" + "e-included aproximation: "+"<b>"+Math.round(koefELEValFrontend)+"</b>"+"%"  +"<li>"+ "size of the effect: " +Math.round(koefELE2)+" "+"%"+ "<ul>");
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
			Alert.render("<ul style=\"list-style-type:none\">" + "<li>" + " " + "" + "<li>" + "e-environment: " + ee + "<li>" + "e-materials: " + em + "<li>" + "before learning: " + bl + "<ul>");
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
