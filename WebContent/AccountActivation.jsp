<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Verification | BC</title>
<link rel="stylesheet" href="styles/style.css" type="text/css">
<script type="text/javascript" src="app/jquery-3.1.1.js"></script>
<script type="text/javascript" src="js/ajax-calls.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		var code = <%=request.getParameter("code")%>;
		var email = <%=request.getParameter("email")%>;
		var loggedin = <%=session.getAttribute("user_id")%>;
		if(loggedin != null && loggedin != undefined)
		{
			$('.account-activated').append('<button id="activation-page-access" class="access" value="Access BC">Access BC</button>');
		}
		else
		{
			$('.account-activated').append('<button id="activation-page-login" class="login" value="Login">Login</button>');
		}
		activate_account_ajax_call(code, email);
		$('#activation-page-login').on('click',function(){
	    	location.href = "login";
	    });
		$('#activation-page-access').on('click',function(){
	    	location.href = "ChooseAccount";
	    });    
	});
</script>
</head>
<body>
	<div class="account-activated" style="display: none;">
		<div class="account-activated-text">
			Your account is activated successfully.
		</div>
	</div>
	<div class="account-not-activated" style="display: none;">
		<div class="account-not-activated-text">
			Invalid activation code and/or email.
		</div>
	</div>
</body>
</html>