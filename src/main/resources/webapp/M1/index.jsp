<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>General evaluation of student</title>
	<link href="TableCSSCode.css" rel="stylesheet" type="text/css">
	<!--  <link rel="stylesheet" type="text/css" href="theme.css"> -->
</head>
<body>
	<%@ page import="org.einclusion.model.*"%>
	<%@ page import="org.webengine.*"%>
	<%@ page import="org.einclusion.GUI.*"%>
	<%@ page import="java.util.ArrayList"%>
	<%@ page import="java.util.*"%>
	<%@ page import="java.util.Iterator"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	<%
		//Einclusion.main(null);
		M1Table m1Table = new M1Table();
		m1Table.readDBfiltered("All", "All");
		//list for buttons
		
		TreeSet<String> treeSet = M1Table.ts;
		request.setAttribute("button", treeSet);
		// list of all data
		ArrayList<ArrayList<String>> list = M1Table.list;
	
		
		request.setAttribute("list", list);
		
// 		Set<String> map = new HashSet<String>();
// 		for(ArrayList<String> aList : list ){
// 			for (String elem : aList.get(1)){
// 				System.out.println(elem);
// 				map.add(elem);
// 			}		
// 		}
	%>
	<table class="tg">
		<tr>
			<th class="tg-yw4l"><a href="index.jsp">Main Page</a></th>
			<th class="tg-yw4l"><a href="databaseEdit.jsp">DatabaseEdit</a></th>
			<th class="tg-yw4l"><a href="M1.jsp">M1</a></th>
			<th class="tg-yw4l"><a href="M2.jsp">M2</a></th>
			<th class="tg-yw4l"><a href="M3.jsp">M3</a></th>
			<th class="tg-yw4l"><a href="prediction.jsp">Prediction</a></th>
		</tr>
	</table>
	


</body>
</html>