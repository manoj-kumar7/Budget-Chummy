<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Authentication | BC</title>
</head>
<body>
	<form action="joinAccountServlet" method="post">
		<label>Enter passcode</label><input type="text" name="passcode" id="passcode" class="textbox passcode">
		<input type="hidden" value="<%=request.getParameter("account_id") %>" name="account_id">
		<input type="hidden" value="<%=request.getParameter("invitation_id") %>" name="invitation_id">
		<input type="submit" value="Join Account">
	</form>
</body>
</html>