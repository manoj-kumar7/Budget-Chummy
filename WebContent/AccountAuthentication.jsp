<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Authentication | BC</title>
<link rel="stylesheet" href="styles/style.css" type="text/css">
<script type="text/javascript" src="app/jquery-3.1.1.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		$('#join-acc-btn').on('click', function(){
			var passcode = $('#passcode').val().trim();
			if(passcode != "")
			{
				join_account_ajax_call();
			}
			else
			{
				showAjaxFailureMessage("Please enter the passcode");
			}
		});
	});
</script>
</head>
<body>
	<div id="join-acc-authentication">
		<div class="firstpage-textbox-space">
			<div class="textbox_space">
				<input type="text" placeholder="Enter passcode" name="passcode" id="passcode" class="textbox name passcode formvalidation">
			</div>
			<input type="hidden" id="account_id" value="<%=request.getParameter("account_id") %>" name="account_id">
			<input type="hidden" id="invitation_id" value="<%=request.getParameter("invitation_id") %>" name="invitation_id">
			<button id="join-acc-btn" class="join-acc-btn bc-btn btn bc-firstpage-btn" value="Join Account">Join Account</button>
		</div>
	</div>
	<div id="hintbox" style="display:none;">
		<div class="content"></div>
	</div>
	<div id="ajax-success-box" style="display:none;">
		<div class="content"></div>
	</div>
	<div id="ajax-failure-box" style="display:none;">
		<div class="content"></div>
	</div>
	<script type="text/javascript" src="js/security_regex.js"></script>
	<script type="text/javascript" src="js/ajax-calls.js"></script>
</body>
</html>