<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>BudgetChummy :)</title>
<link rel="stylesheet" href="styles/style.css" type="text/css">
<script type="text/javascript" src="app/jquery-3.1.1.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	$("#login").click(function(){
		if(<%=request.getParameter("account_id")%> == null || <%=request.getParameter("invitation_id")%> == null)
		{
			location.href="login";
		}
		else
		{
			location.href="login"+
            "?account_id="+<%=request.getParameter("account_id")%>+
            "&invitation_id="+<%=request.getParameter("invitation_id")%>;
		}

	});
	$("#signup,.get-started-btn").click(function(){
		if(<%=request.getParameter("account_id")%> == null || <%=request.getParameter("invitation_id")%> == null)
		{
			location.href="signup";
		}
		else
		{
			location.href="signup"+
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
	response.sendRedirect("home");
}
else if(session.getAttribute("user_id") != null)
{
	response.sendRedirect("ChooseAccount");
}
%>
	<div id="firstpage-top-text" class="firstpage-top">Already have an account?<span id="login" class="firstpage-top-btn"> Login </span> or <span id="signup" class="firstpage-top-btn"> Signup </span></div>
	
	<div class="bc-lead-picture">
		<img src="images/bcHome.jpg">
	</div>
	
	<div class="bc-lead-container">
		<div class="bc-lead-tagline">
			BUDGETING = AWARENESS
		</div>
		<div class="bc-lead-tagline-exp">	
			And who doesn't want that for free
		</div>
		<div class="bc-lead-content">
			Budget Chummy allows you to analyze past spending behaviors while also enabling you to budget for the future.
		</div>
	</div>
	<div class="get-started-btn-div">
		<button class="get-started-btn btn bc-firstpage-btn"><span>Get Started</span></button>
	</div>
	<!--   div class="lead-impressions-div">
		<div class="interface-desc lead-impressions">
			<img src="images/simple_and_functional_ui.png" width=300 height=225 alt="Simple and Functional interface">
			<div class="desc-text">Simple and Functional interface</div>
		</div>
		<div class="fast-secure-consistent-desc lead-impressions">
			<img src="images/simple_and_functional_interface.png" width=300 height=225 alt="Fast, Secure and Consistent">
			<div class="desc-text">Fast, Secure and Consistent</div>
		</div>
		<div class="future-predict-desc lead-impressions">
			<img src="images/bc_predicts_future.png" width=300 height=225 alt="Future savings prediction">
			<div class="desc-text">Future savings prediction</div>
		</div>
	</div -->
	
</body>
</html>