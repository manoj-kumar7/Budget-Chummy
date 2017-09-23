var emailErrorText = "Invalid email address";
var passwordErrorText = "A password can contain maximum of 16 characters with lowercase, uppercase, numbers and special characters (!@#$%^-_')";
var nameErrorText = "A name can contain maximum of 16 characters with lowercase, uppercase, numbers and special characters (!@#$%^-_' )";
var cookiesDisabledMessage = "Browser cookies are disabled. Enable your cookies and try again.";

function validateEmail(email){
	var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	return re.test(email);
}

function validatePassword(pword){
	// var re = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,16}$/;
	var re = /^[a-zA-Z0-9!@#$%^-_']{1,16}$/;
	return re.test(pword);
}

function validateName(name){
	var re = /^[a-zA-Z0-9!@#$%^-_' ]{1,16}$/;
	return re.test(name);
}