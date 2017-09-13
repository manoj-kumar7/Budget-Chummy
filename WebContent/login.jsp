<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>Login | BC</title>
<link rel="stylesheet" href="styles/style.css" type="text/css">
<script type="text/javascript" src="app/jquery-3.1.1.js"></script>

<script type="text/javascript">
$(document).ready(function(){

	$(document).on('focusout','.formvalidation',function(e){// No I18N
		var text = $(this).val();
		if($(e.target).hasClass('email'))
		{
			if (validateEmail(text) || text == "")
			{
				$(e.target).removeClass('validation-err-box');
				$('#hintbox').hide();
			}
			else
			{
				$(e.target).addClass('validation-err-box');
				$('#hintbox .content').text(emailErrorText);
				$('#hintbox').css('top',$('#email').offset().top).css('left',$('#email').offset().left + $('#email').width() + 20);
				$('#hintbox').show();
			}
		}
		else if($(e.target).hasClass('pword'))
		{
			if (validatePassword(text) || text == "")
			{
				$(e.target).removeClass('validation-err-box');
				$('#hintbox').hide();
			}
			else
			{
				$(e.target).addClass('validation-err-box');
				$('#hintbox .content').text(passwordErrorText);
				$('#hintbox').css('top',$('#pword').offset().top).css('left',$('#pword').offset().left + $('#pword').width() + 20);
				$('#hintbox').show();
			}		
		}
	});

	var login = function(){
		if($('#login-form-div').find('.validation-err-box').length || $('#email').val()=="" || $('#pword').val()=="")
		{
			var validation_fields = $('.formvalidation');
			for(var i=0;i<validation_fields.length;i++)
			{
				if($(validation_fields[i]).hasClass('validation-err-box') || $(validation_fields[i]).val()=="")
				{
					if($(validation_fields[i]).hasClass('email'))
					{
						$('#hintbox .content').text(emailErrorText);
						$('#hintbox').css('top',$('#email').offset().top).css('left',$('#email').offset().left + $('#email').width() + 20);
					}
					else if($(validation_fields[i]).hasClass('pword'))
					{
						$('#hintbox .content').text(passwordErrorText);
						$('#hintbox').css('top',$('#pword').offset().top).css('left',$('#pword').offset().left + $('#pword').width() + 20);
					}
					$('#hintbox').show();
					break;
				}
			}	
		}
		else
		{
			if(!navigator.cookieEnabled)
			{
				showAjaxFailureMessage(cookiesDisabledMessage);
			}
			else
			{
				login_ajax_call();
			}
		}
	}

	$(document).on('keypress', function(e){
		if(e.keyCode == 13 || e.which == 13){
			login();
		}
	});
	
	$('#login').on('click',	function(){
		login();
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
	<div id="login-form-div">
		
			<div class="firstpage-textbox-space">
				<div class="textbox_space">
					<input type="text" placeholder="Email" name="email" id="email" class="textbox email formvalidation">
				</div>
				<div class="textbox_space">
					<input type="password" placeholder="Password" name="pword" id="pword" class="textbox pword formvalidation">
				</div>
				<input type="hidden" id="account_id" value="<%=request.getParameter("account_id") %>" name="account_id">
				<input type="hidden" id="invitation_id" value="<%=request.getParameter("invitation_id") %>" name="invitation_id">
				<button id="login" class="login bc-btn bc-firstpage-btn" value="Login"><span>Login</span></button>
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