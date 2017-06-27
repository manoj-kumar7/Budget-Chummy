<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>Create Account | BC</title>
</head>
<body>
<%
if(session.getAttribute("account_id") != null)
{
	response.sendRedirect("home.jsp");
}
else if(session.getAttribute("user_id") == null)
{
	response.sendRedirect("FirstPage.jsp");
}
%>
	<form action="createAccountServlet" method="post">
		<label>Account name</label><input type="text" name="account_name" id="accountname" class="textbox name">

		<input type="submit" value="Create">
	</form>
</body>
</html>