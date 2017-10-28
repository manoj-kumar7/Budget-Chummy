<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>Sign up | BC</title>
<link rel="stylesheet" href="styles/style.css" type="text/css">
<script type="text/javascript" src="app/jquery-3.1.1.js"></script>
<script type="text/javascript" src="app/nprogress.js"></script>
<script src="https://apis.google.com/js/platform.js" async defer></script>
<meta name="google-signin-client_id" content="1030027078466-lmvc5lqlkq1llqasuomhj2cnh6fv4at9.apps.googleusercontent.com">
<link rel="stylesheet" href="styles/nprogress.css" type="text/css">
<script type="text/javascript">
$(document).ready(function(){
	$(document).on('focusout','.formvalidation',function(e){// No I18N
		var text = $(this).val();
		var id = $(this).attr('id');
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
		else if($(e.target).hasClass('name'))
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
				$('#hintbox').css('top',$('#'+id).offset().top).css('left',$('#'+id).offset().left + $('#'+id).width() + 20);
				$('#hintbox').show();
			}	
		}
	});
	
	var signup = function(){
		if($('#signup-form-div').find('.validation-err-box').length || $('#email').val()=="" || $('#pword').val()=="" || $('#firstname').val()=="")
		{
			var validation_fields = $('.formvalidation');
			for(var i=0;i<validation_fields.length;i++)
			{
				var id = $(validation_fields[i]).attr('id');
				if($(validation_fields[i]).hasClass('validation-err-box') || (id!="lastname" && $(validation_fields[i]).val()==""))
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
					else if($(validation_fields[i]).hasClass('first_name'))
					{
						$('#hintbox .content').text(nameErrorText);
						$('#hintbox').css('top',$('#'+id).offset().top).css('left',$('#'+id).offset().left + $('#'+id).width() + 20);
					}
					$('#hintbox').show();
					break;
				}
			}	
		}
		else
		{
			signup_ajax_call();
		}
	}

	$(document).on('keypress', function(e){
		if(e.keyCode == 13 || e.which == 13){
			signup();
		}
	});

	$('#signup').on('click', function(){
		signup();
	});


});
var onSignIn = function(googleUser) {
  var profile = googleUser.getBasicProfile();
  var user_name = profile.getName();
  var email = profile.getEmail();
  var id_token = googleUser.getAuthResponse().id_token;
  google_signup(id_token);
}
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
	<div class="signup-page">
		<div id="signup-page-heading" class="page-heading">SIGN UP</div>
		<div id="signup-form-div">
				<div class="firstpage-textbox-space">
					<div class="textbox_space">
						<input type="text" placeholder="First name" name="first_name" id="firstname" class="textbox name first_name formvalidation">
					</div>
					<div class="textbox_space">
						<input type="text" placeholder="Last name" name="last_name" id="lastname" class="textbox name last_name formvalidation">
					</div>
					<div class="textbox_space">
						<input type="text" placeholder="Email" name="email" id="email" class="textbox email formvalidation">
					</div>
					<div class="textbox_space">
						<input type="password" placeholder="Password" name="pword" id="pword" class="textbox pword formvalidation">
					</div>
					<input type="hidden" id="account_id" value="<%=request.getParameter("account_id") %>" name="account_id">
					<input type="hidden" id="invitation_id" value="<%=request.getParameter("invitation_id") %>" name="invitation_id">
					<button id="signup" class="signup bc-btn bc-firstpage-btn" value="Signup"><span>Signup</span></button>
					<div class="or-text">or</div>
					<button class="g-signin2" data-onsuccess="onSignIn"></button>
				</div>
			
		</div>
	</div>
	<div class="activation-link-sent" style="display:none;">
		<div class="activation-link-sent-text">
			Activation link is sent to your email address. Please activate your account to proceed.
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