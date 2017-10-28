<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>BudgetChummy :)</title>
<link rel="stylesheet" href="styles/style.css" type="text/css">
<script type="text/javascript" src="app/jquery-3.1.1.js"></script>
<script src="https://apis.google.com/js/platform.js?onload=onGoogleLoad" async defer></script>
<meta name="google-signin-client_id" content="1030027078466-lmvc5lqlkq1llqasuomhj2cnh6fv4at9.apps.googleusercontent.com">

<script type="text/javascript">
var onGoogleLoad = function(){
    gapi.load('auth2', function() {
    	gapi.auth2.init();
    });
}
	
$(document).ready(function(){

	if(<%= session.getAttribute("account_id") != null %> && <%=request.getParameter("account_id")%> != null && <%=request.getParameter("invitation_id")%> != null)
	{
		$('.org-first').css('display','none');
		$('.already-loggedin').css('display','block');
		$('.join-acc').css('display','none');
	}
	else if(<%=request.getParameter("account_id")%> != null && <%=request.getParameter("invitation_id")%> != null)
	{
		$('.org-first').css('display','none');
		$('.already-loggedin').css('display','none');
		$('.join-acc').css('display','block');
	}
	else
	{
		$('.org-first').css('display','block');
		$('.already-loggedin').css('display','none');
		$('.join-acc').css('display','none');
	}

	$("#login,#first-page-login").click(function(){
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
	$("#signup,.get-started-btn,#first-page-signup").click(function(){
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
	$('#first-page-logout').click(function(){
    	logout_ajax_call(true);
    });
	
});

</script>
</head>

<body>
<%
if(session.getAttribute("account_id") != null && request.getParameter("account_id") != null && request.getParameter("invitation_id") != null)
{
	
}
else if(session.getAttribute("account_id") != null)
{
	response.sendRedirect("home");
}
else if(session.getAttribute("user_id") != null)
{
	response.sendRedirect("ChooseAccount");
}
%>
	<div class="org-first" style="display: none;">
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
	</div>
	<div class="already-loggedin" style="display: none;">
		<div class="already-loggedin-text">
			Already logged in as a different user. Logout and try again.
		</div>
		<button id="first-page-logout" class="logout" value="Logout">Logout</button>
	</div>
	<div class="join-acc" style="display: none;">
		<div class="join-acc-body">
			<button id="first-page-login" class="login bc-btn bc-firstpage-btn" value="Login"><span>Login</span></button>
			<span class="or-text">or</span>
			<button id="first-page-signup" class="signup bc-btn bc-firstpage-btn" value="Signup"><span>Signup</span></button>
			<div class="to-join-text">to join the account</div>
		</div>
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

<script type="text/javascript" src="js/ajax-calls.js"></script>
</body>
</html>