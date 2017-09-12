<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Choose Account | BC</title>
	<link rel="stylesheet" href="styles/style.css" type="text/css">
	<script type="text/javascript" src="app/jquery-3.1.1.js"></script>
	<script type="text/javascript" src="app/jquery-ui.min.js"></script>
	<script type="text/javascript" src="app/bootstrap.js"></script>
	<script type="text/javascript" src="app/bootstrap.min.js"></script>
	<link rel="stylesheet" href="styles/jquery-ui.css" type="text/css">
	<script type="text/javascript" src="js/ajax-calls.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function(){
		getAccounts_ajax_call("chooseaccount");
		$('#create-new-account').click(function(){
			location.href="CreateAccount.jsp";
		});
		$(document).on('click','.accounts',function(){
			var id = $(this).attr("id");
			accountChosen_ajax_call(id, "income");
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
else if(session.getAttribute("user_id") == null)
{
	response.sendRedirect("/BudgetChummy/");
}
%>
<div id="choose-account-form-div">
	<div id="accounts-list"></div>
	<input type="button" id="create-new-account" class="btn" value="Create new Account">
</div>
</body>
</html>