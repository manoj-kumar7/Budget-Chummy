<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>BudgetChummy :)</title>
<script type="text/javascript" src="app/jquery-3.1.1.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	$("#login").click(function(){
		if(<%=request.getParameter("account_id")%> == null || <%=request.getParameter("invitation_id")%> == null)
		{
			location.href="/login.jsp";
		}
		else
		{
			location.href="/login.jsp"+
            "?account_id="+<%=request.getParameter("account_id")%>+
            "&invitation_id="+<%=request.getParameter("invitation_id")%>;
		}

	});
	$("#signup").click(function(){
		if(<%=request.getParameter("account_id")%> == null || <%=request.getParameter("invitation_id")%> == null)
		{
			location.href="/signup.jsp";
		}
		else
		{
			location.href="/signup.jsp"+
            "?account_id="+<%=request.getParameter("account_id")%>+
            "&invitation_id="+<%=request.getParameter("invitation_id")%>;
		}
	});
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
	<input type="button" name="Login" id="login" value="Login" class="login bc-btn">
	<input type="button" name="signup" id="signup" value="Signup" class="signup bc-btn">
</body>
</html>