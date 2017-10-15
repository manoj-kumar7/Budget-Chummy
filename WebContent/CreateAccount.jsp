<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>Create Account | BC</title>
<link rel="stylesheet" href="styles/style.css" type="text/css">
<script type="text/javascript" src="app/jquery-3.1.1.js"></script>

<script type="text/javascript">
var enterKeyPressed = false;
$(document).ready(function(){
	$(document).on('focusout','.formvalidation',function(e){// No I18N
		var text = $(this).val();
		if($(e.target).hasClass('name'))
		{
			if (validateName(text) || text == "")
			{
				$(e.target).removeClass('validation-err-box');
				$('#hintbox').hide();
			}
			else
			{
				$(e.target).addClass('validation-err-box');
				$('#hintbox .content').text(nameErrorText);
				$('#hintbox').css('top',$('#accountname').offset().top).css('left',$('#accountname').offset().left + $('#accountname').width() + 20);
				$('#hintbox').show();
			}
		}
	});
	
	var createAccount = function(){
		if($('#create-account-form-div').find('.validation-err-box').length || $('#accountname').val()=="")
		{
			var validation_fields = $('.formvalidation');
			for(var i=0;i<validation_fields.length;i++)
			{
				if($(validation_fields[i]).hasClass('validation-err-box') || $(validation_fields[i]).val()=="")
				{
					if($(validation_fields[i]).hasClass('name'))
					{
						$('#hintbox .content').text(nameErrorText);
						$('#hintbox').css('top',$('#accountname').offset().top).css('left',$('#accountname').offset().left + $('#accountname').width() + 20);
					}
					$('#hintbox').show();
					break;
				}
			}	
		}
		else
		{
			create_account_ajax_call();
			$('#create-account').prop('disabled',true);
			enterKeyPressed = true;
		}
	}

	$(document).on('keypress', function(e){
		if(!enterKeyPressed && e.keyCode == 13 || e.which == 13){
			createAccount();
		}
	});

	$('#create-account').on('click',function(){
		createAccount();
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
else if(session.getAttribute("user_id") == null)
{
	response.sendRedirect("login");
}
%>
	<div class="create-account-page">
		<div id="create-account-page-heading" class="page-heading">CREATE ACCOUNT</div>
		<div id="create-account-help-text" class="create-account-help-text help-text">You can have different accounts setup in Budget Chummy and work with them independently for example You can have seperate accounts for your Office, Home, Travel etc...</div>
		<div id="create-account-form-div">
			<div class="firstpage-textbox-space">
				<div class="textbox_space">
					<input type="text" name="account_name" placeholder="Account name" id="accountname" class="textbox formvalidation name">
				</div>	
				<button id="create-account" class="create-account bc-btn bc-firstpage-btn" value="Create"><span>Create</span></button>
			</div>
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
	<script type="text/javascript" src="app/moment.js"></script>
	<script type="text/javascript" src="app/moment-timezone-with-data.js"></script>
	<script type="text/javascript" src="js/Datehelper.js"></script>
	<script type="text/javascript" src="js/security_regex.js"></script>
	<script type="text/javascript" src="js/ajax-calls.js"></script>
</body>
</html>