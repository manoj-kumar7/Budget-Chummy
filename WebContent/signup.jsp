<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>Sign up | BC</title>
<script type="text/javascript" src="app/jquery-3.1.1.js"></script>

<script type="text/javascript">
$(document).ready(function(){

});

</script>
</head>
<body>
<%
if(session.getAttribute("account_id") != null)
{
	response.sendRedirect("home.jsp");
}
%>
	<form action="signUpServlet" method="post">
		<label>First Name</label><input type="text" name="first_name" id="firstname" class="textbox name">
		<label>Last Name</label><input type="text" name="last_name" id="lastname" class="textbox name">
		<label>Email</label><input type="text" name="email" id="email" class="textbox email">
		<label>Phone number</label><input type="text" name="phone_no" id="phone_no" class="textbox phone">
		<label>Password</label><input type="text" name="pword" id="pword" class="textbox pword">
		<input type="hidden" value="<%=request.getParameter("account_id") %>" name="account_id">
		<input type="hidden" value="<%=request.getParameter("invitation_id") %>" name="invitation_id">
		<input type="submit" value="Signup">
	</form>
</body>
</html>