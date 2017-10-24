var addExpensesToArray = function(obj, start_date, end_date, addOnlyOnce)
{
	var temp = start_date;
	if(addOnlyOnce)
	{
		var temp_obj = jQuery.extend({}, obj);
		temp_obj.date = temp;
		globalObject.expenses_in_month.push(temp_obj);
	}
	else
	{
		while(temp <= end_date)
		{
			var temp_obj = jQuery.extend({}, obj);
			temp_obj.date = temp;
			globalObject.expenses_in_month.push(temp_obj);
			var d = new Date(temp);
			if(obj.repeat_period == 1)
			{
				temp = addDaysToDate(d, 1);
			}
			else if(obj.repeat_period == 2)
			{
				temp = addDaysToDate(d, 7);
			}
			else if(obj.repeat_period == 3)
			{
				return;
			}
			else if(obj.repeat_period == 4)
			{
				return;
			}
		}
	}
}

var findStartOfWeeklyExpense = function(start_date, epochOfFirstDayOfMonth){
	var temp = new Date(start_date);
	temp.setTime(addDaysToDate(d_start, 7));
	while(temp < epochOfFirstDayOfMonth)
	{
		temp.setTime(addDaysToDate(d_start, 7));
	}
	return temp;
}
var findStartOfMonthlyExpense = function(start_date, today_date, today_month, today_year){
	var noOfDaysInCurrentMonth = getNoOfDaysInThisMonth(today_month, today_year);
	var start_date_arr = getDateFromEpoch(start_date);
	var start_date_date = start_date_arr[2];
	if(start_date_date > noOfDaysInCurrentMonth)
	{
		return dateToEpoch(today_year + '/' + today_month + '/' + noOfDaysInCurrentMonth);
	}
	else
	{
		return dateToEpoch(today_year + '/' + today_month + '/' + start_date_date);
	}
}
var findStartOfYearlyExpense = function(start_date, today_date, today_month, today_year){
	var start_date_arr = getDateFromEpoch(start_date);
	var start_date_month = start_date_arr[1];
	if(start_date_month == today_month)
	{
		return findStartOfMonthlyExpense(start_date, today_date, today_month, today_year);
	}
	else
	{
		return -1;
	}
}

var calculateDailyExpenses = function(obj, today_date, today_month, today_year){
	var expense_date = obj.date;
	var epochOfFirstDayOfMonth = epochOfDayOfMonth(1, today_month, today_year);
	var _epochOfLastDayOfMonth = epochOfLastDayOfMonth(today_month, today_year);
	var epochOfToday = getTodayEpoch();
	if(epochOfToday >= epochOfFirstDayOfMonth && epochOfToday <= _epochOfLastDayOfMonth)
	{
		var todayLiesInThisMonth = true;
	}
	else
	{
		var todayLiesInThisMonth = false;
		if(epochOfToday < epochOfFirstDayOfMonth)
		{
			if(expense_date >= epochOfFirstDayOfMonth && expense_date <= _epochOfLastDayOfMonth)
			{
				addExpensesToArray(obj, expense_date, _epochOfLastDayOfMonth, true);
			}
			return;
		}
	}


	if(!todayLiesInThisMonth) //Here today lies after this current month
	{
		if(expense_date <= epochOfFirstDayOfMonth)
		{
			addExpensesToArray(obj, epochOfFirstDayOfMonth, _epochOfLastDayOfMonth, false);
		}
		else if(expense_date > epochOfFirstDayOfMonth && expense_date <= _epochOfLastDayOfMonth)
		{
			addExpensesToArray(obj, expense_date, _epochOfLastDayOfMonth, false);
		}
	}
	else
	{
		if(expense_date <= epochOfFirstDayOfMonth)
		{
			addExpensesToArray(obj, epochOfFirstDayOfMonth, epochOfToday, false);
		}
		else if(expense_date > epochOfFirstDayOfMonth && expense_date <= epochOfToday)
		{
			addExpensesToArray(obj, expense_date, epochOfToday, false);
		}
		else if(expense_date > epochOfToday && expense_date <= _epochOfLastDayOfMonth)
		{
			addExpensesToArray(obj, expense_date, _epochOfLastDayOfMonth, true);
		}
	}
}
var calculateWeeklyExpenses = function(obj, today_date, today_month, today_year){
	var expense_date = obj.date;
	var epochOfFirstDayOfMonth = epochOfDayOfMonth(1, today_month, today_year);
	var _epochOfLastDayOfMonth = epochOfLastDayOfMonth(today_month, today_year);
	var epochOfToday = getTodayEpoch();
	if(epochOfToday >= epochOfFirstDayOfMonth && epochOfToday <= _epochOfLastDayOfMonth)
	{
		var todayLiesInThisMonth = true;
	}
	else
	{
		var todayLiesInThisMonth = false;
		if(epochOfToday < epochOfFirstDayOfMonth)
		{
			if(expense_date >= epochOfFirstDayOfMonth && expense_date <= _epochOfLastDayOfMonth)
			{
				addExpensesToArray(obj, expense_date, _epochOfLastDayOfMonth, true);
			}
			return;
		}
	}


	if(!todayLiesInThisMonth) //Here today lies after this current month
	{
		if(expense_date <= epochOfFirstDayOfMonth)
		{
			var start_date = findStartOfWeeklyExpense(expense_date, epochOfFirstDayOfMonth);
			addExpensesToArray(obj, start_date, _epochOfLastDayOfMonth, false);
		}
		else if(expense_date > epochOfFirstDayOfMonth && expense_date <= _epochOfLastDayOfMonth)
		{
			addExpensesToArray(obj, expense_date, _epochOfLastDayOfMonth, false);
		}
	}
	else
	{
		if(expense_date <= epochOfFirstDayOfMonth)
		{
			var start_date = findStartOfWeeklyExpense(expense_date, epochOfFirstDayOfMonth);
			addExpensesToArray(obj, start_date, epochOfToday, false);
		}
		else if(expense_date > epochOfFirstDayOfMonth && expense_date <= epochOfToday)
		{
			addExpensesToArray(obj, expense_date, epochOfToday, false);
		}
		else if(expense_date > epochOfToday && expense_date <= _epochOfLastDayOfMonth)
		{
			addExpensesToArray(obj, expense_date, _epochOfLastDayOfMonth, true);
		}
	}
}
var calculateMonthlyExpenses = function(obj, today_date, today_month, today_year){
	var expense_date = obj.date;
	var epochOfFirstDayOfMonth = epochOfDayOfMonth(1, today_month, today_year);
	var _epochOfLastDayOfMonth = epochOfLastDayOfMonth(today_month, today_year);
	var epochOfToday = getTodayEpoch();
	if(epochOfToday >= epochOfFirstDayOfMonth && epochOfToday <= _epochOfLastDayOfMonth)
	{
		var todayLiesInThisMonth = true;
	}
	else
	{
		var todayLiesInThisMonth = false;
		if(epochOfToday < epochOfFirstDayOfMonth)
		{
			if(expense_date >= epochOfFirstDayOfMonth && expense_date <= _epochOfLastDayOfMonth)
			{
				addExpensesToArray(obj, expense_date, _epochOfLastDayOfMonth, true);
			}
			return;
		}
	}


	if(!todayLiesInThisMonth) //Here today lies after this current month
	{
		if(expense_date <= epochOfFirstDayOfMonth)
		{
			var start_date = findStartOfMonthlyExpense(expense_date, today_date, today_month, today_year);
			addExpensesToArray(obj, start_date, _epochOfLastDayOfMonth, false);
		}
		else if(expense_date > epochOfFirstDayOfMonth && expense_date <= _epochOfLastDayOfMonth)
		{
			addExpensesToArray(obj, expense_date, _epochOfLastDayOfMonth, false);
		}
	}
	else
	{
		if(expense_date <= epochOfFirstDayOfMonth)
		{
			var start_date = findStartOfMonthlyExpense(expense_date, today_date, today_month, today_year);
			addExpensesToArray(obj, start_date, epochOfToday, false);
		}
		else if(expense_date > epochOfFirstDayOfMonth && expense_date <= epochOfToday)
		{
			addExpensesToArray(obj, expense_date, epochOfToday, false);
		}
		else if(expense_date > epochOfToday && expense_date <= _epochOfLastDayOfMonth)
		{
			addExpensesToArray(obj, expense_date, _epochOfLastDayOfMonth, true);
		}
	}
}
var calculateYearlyExpenses = function(obj, today_date, today_month, today_year){
	var expense_date = obj.date;
	var epochOfFirstDayOfMonth = epochOfDayOfMonth(1, today_month, today_year);
	var _epochOfLastDayOfMonth = epochOfLastDayOfMonth(today_month, today_year);
	var epochOfToday = getTodayEpoch();
	if(epochOfToday >= epochOfFirstDayOfMonth && epochOfToday <= _epochOfLastDayOfMonth)
	{
		var todayLiesInThisMonth = true;
	}
	else
	{
		var todayLiesInThisMonth = false;
		if(epochOfToday < epochOfFirstDayOfMonth)
		{
			if(expense_date >= epochOfFirstDayOfMonth && expense_date <= _epochOfLastDayOfMonth)
			{
				addExpensesToArray(obj, expense_date, _epochOfLastDayOfMonth, true);
			}
			return;
		}
	}


	if(!todayLiesInThisMonth) //Here today lies after this current month
	{
		if(expense_date <= epochOfFirstDayOfMonth)
		{
			var start_date = findStartOfYearlyExpense(expense_date, today_date, today_month, today_year);
			if(start_date != -1)
			{
				addExpensesToArray(obj, start_date, _epochOfLastDayOfMonth, false);
			}
		}
		else if(expense_date > epochOfFirstDayOfMonth && expense_date <= _epochOfLastDayOfMonth)
		{
			addExpensesToArray(obj, expense_date, _epochOfLastDayOfMonth, false);
		}
	}
	else
	{
		if(expense_date <= epochOfFirstDayOfMonth)
		{
			var start_date = findStartOfYearlyExpense(expense_date, today_date, today_month, today_year);
			if(start_date != -1)
			{
				addExpensesToArray(obj, start_date, epochOfToday, false);
			}
		}
		else if(expense_date > epochOfFirstDayOfMonth && expense_date <= epochOfToday)
		{
			addExpensesToArray(obj, expense_date, epochOfToday, false);
		}
		else if(expense_date > epochOfToday && expense_date <= _epochOfLastDayOfMonth)
		{
			addExpensesToArray(obj, expense_date, _epochOfLastDayOfMonth, true);
		}
	}
}

var printExpenses = function(){
	$('.expense-table tbody').html("");
	$('.expense-table tbody').append("<tr><th class='amount-col'>Amount</th><th class='date-col'>Date</th><th class='tag-col'>Tag</th><th class='description-col'>Description</th><th class='addedby-col'>Added by</th><th class='location-col'>Location</th></tr>");
	var data = globalObject.expenses_in_month;
	for(var i=0;i<data.length;i++)
	{
		var obj = data[i];
		let amount = obj.amount || "-";
		let dates = getDateFromEpoch(obj.date);
		let date = formCustomDateFormat(dates);
		let tag = obj.tag_name || "-";
		let description = obj.description || "-";
		let user_name = obj.user_name || "-";
		let added_date_time = obj.added_date_time || "-";
		let location = obj.location || "-";
		let lat = obj.latitude;
		if(lat == null || lat == -1 || lat == 0)
		{
			lat = "-";
		}
		let lon = obj.longitude;
		if(lon == null || lon == -1 || lon == 0)
		{
			lon = "-";
		}
		globalObject.expense_data.push({y:amount,indexLabel:amount+"(#percent%)",legendText:""+amount,description:description,date:date,tag:tag});
		let append_string = "<tr><td><i class='icon-inr'></i>"+amount+"</td><td>"+date+"</td><td>"+tag+"</td><td>"+description+"</td><td>"+user_name+"</td><td>"+location;
		if(lat != "-" && lon != "-")
		{
			append_string += "<img src='images/show_location_icon.png' class='image location-icon' alt='Show in map' data-lat="+lat+" data-lon="+lon+"></td></tr>";
		}
		else
		{
			append_string += "</td></tr>";
		}
		$('.expense-table tbody').append(append_string);
	}
	expense_chart.options.data[0].dataPoints = globalObject.expense_data;
	if(globalObject.expense_data.length != 0)
	{
		$('#empty-expense-data').css("display","none");
		$('#chartContainer2').css("display","block");
		$('.expense-table-space').css("display","block");
		expense_chart.render();
	}
	else
	{
		$('#chartContainer2').css("display","none");
		$('.expense-table-space').css("display","none");
		$('#empty-expense-data').css("display","block");
		$('#empty-expense-data').html("<img src='images/no_results.png'><div class='empty-data-text'>No expense for this month</div>");
	}
	globalObject.expense_data = [];
	globalObject.expenses_in_month = [];
}

var findExpenses = function(data, page)
{
	globalObject.expenses_in_month = [];
	for(var i=0;i<data.length;i++)
	{
		var obj = jQuery.parseJSON(data[i]);
		var date_arr = getDateFromEpoch(obj.date);
		var expense_date = date_arr[2];
		var expense_month = date_arr[1];
		var expense_year = date_arr[0];
		var today_date = globalObject.date;
		var today_month = globalObject.month;
		var today_year = globalObject.year;

		if(expense_year <= today_year && !(expense_year == today_year && expense_month > today_month))
		{
			var repeat_period = obj.repeat_period;
			if(repeat_period == 0)
			{
				if(expense_year == today_year && expense_month == today_month)
				{
					globalObject.expenses_in_month.push(obj);
				}
			}
			else if(repeat_period == 1)
			{
				calculateDailyExpenses(obj, today_date, today_month, today_year);
			}
			else if(repeat_period == 2)
			{
				calculateWeeklyExpenses(obj, today_date, today_month, today_year);
			}
			else if(repeat_period == 3)
			{
				calculateMonthlyExpenses(obj, today_date, today_month, today_year);
			}
			else if(repeat_period == 4)
			{
				calculateYearlyExpenses(obj, today_date, today_month, today_year);
			}
		}
	}
	if(page == "expense")
	{
		printExpenses();
	}
	else if(page == "budget")
	{
		get_budget_ajax_call(globalObject.month, globalObject.year);
	}
}



