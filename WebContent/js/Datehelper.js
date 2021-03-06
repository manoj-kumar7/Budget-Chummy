
var dateToEpoch = function(date){
	return new Date(date).valueOf();
}

var epochOfDayOfMonth = function(date, month, year){
	return new Date(year+"/"+month+"/"+date).valueOf();
}

var epochOfLastDayOfMonth = function(month, year){
	var m = month + 1;
	var y = year;
	if(m > 12)
	{
		m = 0;
		y = y + 1;
	}
	var last_date_of_month = new Date(y, m - 1, 0).getDate();
	return new Date(year+"/"+month+"/"+last_date_of_month).valueOf();
}
// var epochOfFirstDayOfYear = function(year){
// 	return new Date(year+"/"+01+"/"+01).valueOf();
// }

var getDateFromEpoch = function(epoch){
	var e = new Date(epoch);
	var d = e.getDate();
	var m = e.getMonth() + 1;
	var y = e.getFullYear();
	return [y, m, d];
}

var getReminderDate = function(date, reminder_type){
	var d = new Date(date);
	if(reminder_type == 1)
	{
		d.setDate(d.getDate() - 1);
	}
	var day = d.getDate();
	var m = d.getMonth();
	var y = d.getFullYear();
	return [y, m, day];
}

var formCustomDateFormat = function(dates){
	var y = dates[0];
	var m = dates[1];
	var d = dates[2];
	return globalObject.months_shortform[m-1] + " " + d + ", " + y;
}

var getCurrentRealMonth = function(){
	var timezone = getAccountTimeZone();
	var d = new Date(moment.tz(timezone).format());
	return d.getMonth() + 1;
}

var getCurrentRealYear = function(){
	var timezone = getAccountTimeZone();
	var d = new Date(moment.tz(timezone).format());
	return d.getFullYear();
}

// var getYearFromEpoch = function(epoch){
// 	var d = new Date(epoch);
// 	return d.getFullYear();
// }

// var getTodayDate = function(){
// 	var d = new Date();
// 	return d.getDate();
// }

var getTodayEpoch = function(){
	var timezone = getAccountTimeZone();
	var d = new Date(moment.tz(timezone).format());
	let date = d.getDate();
	let m = d.getMonth() + 1;
	let y = d.getFullYear();
	let date_string = y+"/"+m+"/"+date;
	var d1 = new Date(date_string);
	return d1.valueOf();
}
var getTomorrowEpoch = function(){
	var timezone = getAccountTimeZone();
	var d = new Date(moment.tz(timezone).format());
	d.setTime(addDaysToDate(d, 1));
	let date = d.getDate();
	let m = d.getMonth() + 1;
	let y = d.getFullYear();
	let date_string = y+"/"+m+"/"+date;
	var d1 = new Date(date_string);
	return d1.valueOf();
}
var getNoOfDaysInThisMonth = function(month, year){
	var no_of_days = new Date(year, month, 0).getDate();
	return no_of_days;
}

var addDaysToDate = function(date, no_of_days)
{
	return (date.getTime() + (no_of_days * 86400000));
}

var getEndDateOfBudget = function(start_date, yearly_budget){
	var end_d = start_date.getDate();
	if(yearly_budget)
	{
		var end_m = start_date.getMonth() - 1;
		var end_y = start_date.getFullYear() + 1;
	}
	else
	{
		var end_m = start_date.getMonth();
		var end_y = start_date.getFullYear();
	}

	if(end_d == 1)
	{
		let temp_end_m = end_m + 1;
		let temp_end_y = end_y;
		if(temp_end_m > 11)
		{
			temp_end_m = 0;
			temp_end_y = temp_end_y + 1;
		}
		end_d = new Date(temp_end_y, temp_end_m, 0).getDate();
		return [end_d, end_m, end_y];
	}
	else
	{
		end_m = end_m + 1;
	}

	if(end_m > 11)
	{
		end_m = 0;
		end_y = end_y + 1;
	}

	if(end_d >= 29 && end_d <=31)
	{
		let temp_end_m = end_m + 1;
		let temp_end_y = end_y;
		if(temp_end_m > 11)
		{
			temp_end_m = 0;
			temp_end_y = temp_end_y + 1;
		}
		let last_date = new Date(temp_end_y, temp_end_m, 0).getDate();
		while(end_d > last_date)
		{
			end_d = end_d - 1;
		}
		end_d = end_d - 1;
	}
	else
	{
		end_d = end_d - 1;
	}
	return [end_d, end_m, end_y];
}

var calculateWeekStartEndDates = function(start_date){
	var d_start = new Date(start_date);
	var d_end = new Date(d_start);
	// d_end.setDate(d_start.getDate() + 6);
	d_end.setTime(addDaysToDate(d_start, 6));

	if(globalObject.month == getCurrentRealMonth() && globalObject.year == getCurrentRealYear())
	{
		var today = getTodayEpoch();
	}
	else
	{
		var today = epochOfDayOfMonth(1, globalObject.month, globalObject.year);
	}
	if(d_start >= today)
	{
		return [d_start.valueOf(), d_end.valueOf()];
	}
	while(1)
	{
		if(today >= d_start && today <= d_end)
		{
			return [d_start.valueOf(), d_end.valueOf()];
		}
		else
		{
			// d_start.setDate(d_start.getDate() + 7);
			d_start.setTime(addDaysToDate(d_start, 7));
			// d_end.setDate(d_start.getDate() + 6);
			d_end.setTime(addDaysToDate(d_start, 6));
		}
	}
}

var calculateMonthStartEndDates = function(start_date){
	var d_start = new Date(start_date);
	var d_end = new Date(start_date);
	
	// var date = d_start.getDate();
	// d_start.setFullYear(globalObject.year, globalObject.month-1, date);

	var today = epochOfLastDayOfMonth(globalObject.month, globalObject.year);
	if(d_start.valueOf() >= today)
	{
		return [d_start.valueOf(), d_end.valueOf()];
	}

	var end_date_arr = getEndDateOfBudget(d_start, false);
	var end_d = end_date_arr[0];
	var end_m = end_date_arr[1];
	var end_y = end_date_arr[2];
	d_end.setFullYear(end_y, end_m, end_d);

	while(1)
	{
		if(today >= d_start && today <= d_end)
		{
			return [d_start.valueOf(), d_end.valueOf()];
		}
		else
		{
			d_start.setTime(addDaysToDate(d_end, 1));

			end_date_arr = getEndDateOfBudget(d_start, false);
			end_d = end_date_arr[0];
			end_m = end_date_arr[1];
			end_y = end_date_arr[2];
			d_end.setFullYear(end_y, end_m, end_d);
		}
	}

	return [d_start.valueOf(), d_end.valueOf()];
}

var calculateYearStartEndDates = function(start_date){
	var d_start = new Date(start_date);
	var d_end = new Date(start_date);

	var today = epochOfLastDayOfMonth(globalObject.month, globalObject.year);
	if(d_start.valueOf() >= today)
	{
		return [d_start.valueOf(), d_end.valueOf()];
	}

	var end_date_arr = getEndDateOfBudget(d_start, true);
	var end_d = end_date_arr[0];
	var end_m = end_date_arr[1];
	var end_y = end_date_arr[2];
	d_end.setFullYear(end_y, end_m, end_d);

	while(1)
	{
		if(today >= d_start && today <= d_end)
		{
			return [d_start.valueOf(), d_end.valueOf()];
		}
		else
		{
			d_start.setTime(addDaysToDate(d_end, 1));

			end_date_arr = getEndDateOfBudget(d_start, true);
			end_d = end_date_arr[0];
			end_m = end_date_arr[1];
			end_y = end_date_arr[2];
			d_end.setFullYear(end_y, end_m, end_d);
		}
	}

	return [d_start.valueOf(), d_end.valueOf()];
}

var calculateNoOfDaysToEndBudget = function(start_date, end_date){
	var today = getTodayEpoch();
	if(today > end_date || today < start_date)
	{
		return -1;
	}
	var one_day = 1000*60*60*24;
	var diff = Math.round((end_date - today) / one_day);
	return diff + 1;
}

var getAccountTimeZone = function(){
	return moment.tz.guess();
}

var getAccountTimeZoneEpoch = function(){
	var timezone = getAccountTimeZone();
	var epochOfTimeZone = new Date(moment.tz(timezone).format()).valueOf();
	return epochOfTimeZone;
}