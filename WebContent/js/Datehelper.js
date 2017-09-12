
var dateToEpoch = function(date){
	return new Date(date).valueOf();
}

var epochOfFirstDayOfMonth = function(month, year){
	return new Date(year+"/"+month+"/"+01).valueOf();
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

var formCustomDateFormat = function(dates){
	var y = dates[0];
	var m = dates[1];
	var d = dates[2];
	return months_shortform[m-1] + " " + d + ", " + y;
}

var getCurrentRealMonth = function(){
	var d = new Date();
	return d.getMonth() + 1;
}

var getCurrentRealYear = function(){
	var d = new Date();
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
	var d = new Date();
	let date = d.getDate();
	let m = d.getMonth() + 1;
	let y = d.getFullYear();
	let date_string = y+"/"+m+"/"+date;
	var d1 = new Date(date_string);
	return d1.valueOf();
}

var calculateWeekStartEndDates = function(start_date){
	if(month == getCurrentRealMonth() && year == getCurrentRealYear())
	{
		var today = getTodayEpoch();
	}
	else
	{
		var today = epochOfFirstDayOfMonth(month, year);
	}
	var d_start = new Date(start_date);
	var d_end = new Date(d_start);
	d_end.setDate(d_start.getDate() + 6);
	while(1)
	{
		if(today >= d_start && today <= d_end)
		{
			return [d_start, d_end];
		}
		else
		{
			d_start.setDate(d_start.getDate() + 7);
			d_end.setDate(d_start.getDate() + 6);
		}
	}
}

var calculateMonthStartEndDates = function(start_date){
	var d_start = new Date(start_date);
	var d_end = new Date(start_date);
	var date = d_start.getDate();
	d_start.setFullYear(year, month-1, date);
	var m = month;
	if(m >= 12)
	{
		m = 0;
	}
	if(date == 1)
	{
		date = new Date(year, m, 0).getDate();
	}
	else
	{
		date = date - 1;
	}
	d_end.setFullYear(year, m, date);
	return [d_start, d_end];
}

var calculateYearStartEndDates = function(start_date){
	var d_start = new Date(start_date);
	var d_end = new Date(start_date);
	var date = d_start.getDate();
	var m = d_start.getMonth();
	d_start.setFullYear(year, month-1, date);
	if(date == 1)
	{
		date = new Date(year+1, month-1, 0).getDate();
	}
	else
	{
		date = date - 1;
	}
	d_end.setFullYear(year+1, month-1, date);
	return [d_start, d_end];
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