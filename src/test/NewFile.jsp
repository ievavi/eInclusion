<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<html>
<head>
<body>
	<script>
		function upload() {
			document.getElementById('name').value = document
					.getElementById('file').value;
			if (document.getElementById('name').value != '')
				document.getElementById('form').submit();
		}
	</script>
	<form method='POST' id='form' enctype='multipart/form-data'
		action='/upload'>File: <input type='file' id='file' name='file'>&nbsp;<input
			type='hidden' id='name' name='name'><input type='hidden'
			id='sessid' name='sessid' value='134'><input type='button'
			value='Upload' onclick='upload();'>
	</form>
</body>
</html>
</body>
</html>