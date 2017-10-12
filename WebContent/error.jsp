<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error | BC</title>
<link rel="stylesheet" href="styles/style.css" type="text/css">
<script type="text/javascript" src="app/jquery-3.1.1.js"></script>
<script type="text/javascript" src="app/jquery-ui.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var loggedin = <%= session.getAttribute("user_id") %>;
	if(loggedin != null && loggedin != undefined)
	{
		$('.error-body').append('<button id="error-page-logout" class="logout" value="Logout">Logout</button>');
	}
	$('#error-page-logout').click(function(){
    	logout_ajax_call(false);
    });
});

</script>
</head>
<body>
	<div class="error-body">
		<div class="error-text">
			Please verify your account to proceed. You would have received a verification mail from us.
		</div>
	</div>
<script type="text/javascript" src="js/ajax-calls.js"></script>
</body>
</html>