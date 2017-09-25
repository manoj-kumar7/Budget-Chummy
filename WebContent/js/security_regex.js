var emailErrorText = "Invalid email address";
var passwordErrorText = "A password can contain maximum of 16 characters with lowercase, uppercase, numbers and special characters (!@#$%^-_')";
var nameErrorText = "A name can contain maximum of 16 characters with lowercase, uppercase, numbers and special characters (!@#$%^-_' )";
var cookiesDisabledMessage = "Browser cookies are disabled. Enable your cookies and try again.";

var amountRegex = new RegExp("^\\d+(\\.\\d+)?$");
var dateRegex = new RegExp("^\\d{4}\\/\\d{1,2}\\/\\d{1,2}$");
var locationRegex = new RegExp("^[a-zA-Z0-9:@#-_' ]{0,200}$");
var descriptionRegex = new RegExp("^[a-zA-Z0-9:@#-_' ]{0,400}$");
var latLonRegex = new RegExp("^[-+]?\\d+(\\.\\d+)?$");

var passcodeRegex = new RegExp("^[a-zA-Z0-9]{6}$");

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

function validatePasscode(passcode)
{
	return passcodeRegex.test(passcode);
}

function isLeapYear(year)
{
	if( year%400==0 || (year%4==0 && year%100!=0) )
	{
		return true;
	}
	return false;
}

function validateDate(date_input)
{
	var res = dateRegex.test(""+date_input);
	if(!res)
	{
		return false;
	}
	var date_arr = date_input.split("/");
	var year = date_arr[0];
	var month = date_arr[1];
	var date = date_arr[2];
	var list_of_days = [31,28,31,30,31,30,31,31,30,31,30,31];
	var is_leap = isLeapYear(year);
	if(is_leap)
	{
		list_of_days[1] = 29;
	}
	if(year >= 1970 && month >= 1 && month <= 12)
	{
		if(date >=1 && date <= list_of_days[month-1])
		{
			return true;
		}
	}
	return false;
}

function validateIncomeExpenseData(amount, date, tag_id, add_info, location, lat, lon, description, repeat, reminder)
{
	var amountSafe = amountRegex.test(""+amount);
	var dateSafe = validateDate(date);
	var tagSafe = tag_id != null && tag_id != "null" && tag_id != "" && parseInt(tag_id) > 0;
	if(!amountSafe || !dateSafe || !tagSafe)
	{
		return false;
	}
	if(add_info)
	{
		var locationSafe = locationRegex.test(""+location) || location == "" || location == null;
		var latSafe = latLonRegex.test(""+lat) || lat == "" || lat == null;
		var lonSafe = latLonRegex.test(""+lon) || lon == "" || lon == null;
		var descriptionSafe = descriptionRegex.test(""+description) || description == "" || description == null;
		var repeatSafe = parseInt(repeat) >=0 && parseInt(repeat) <= 6;
		var reminderSafe = parseInt(reminder) >= 0 && parseInt(reminder) <= 2;
		if(!locationSafe || !latSafe || !lonSafe || !descriptionSafe || !repeatSafe || !reminderSafe)
		{
			return false;
		}
	}
	return true;
}

function validateBudgetData(budget_type, tag_id, budget_repeat, start_date, end_date, amount, description)
{
	var budgetTypeSafe = parseInt(budget_type) == 0 || parseInt(budget_type) == 1;
	var tagSafe = parseInt(tag_id) == -1 || (tag_id != null && tag_id != "null" && tag_id != "" && parseInt(tag_id) > 0);
	var repeatSafe = parseInt(budget_repeat) >= 0 && parseInt(budget_repeat) <= 4;
	var startSafe = parseInt(start_date) == -1 || validateDate(start_date);
	var endSafe = parseInt(end_date) == -1 || validateDate(end_date);
	var amountSafe = amountRegex.test(""+amount);
	var descriptionSafe = descriptionRegex.test(""+description);
	if(!budgetTypeSafe || !tagSafe || !repeatSafe || !startSafe || !endSafe || !amountSafe || !descriptionSafe)
	{
		return false;
	}
	return true;
}