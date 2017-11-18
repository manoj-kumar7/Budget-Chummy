var addIncomesToArray = function(obj, start_date, end_date, addOnlyOnce)
{
	var temp = start_date;
	if(addOnlyOnce)
	{
		var temp_obj = jQuery.extend({}, obj);
		temp_obj.date = temp;
		globalObject.incomes_in_month.push(temp_obj);
	}
	else
	{
		while(temp <= end_date)
		{
			var temp_obj = jQuery.extend({}, obj);
			temp_obj.date = temp;
			globalObject.incomes_in_month.push(temp_obj);
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

var findStartOfWeeklyIncome = function(start_date, epochOfFirstDayOfMonth){
	var temp = new Date(start_date);
	while(temp < epochOfFirstDayOfMonth)
	{
		temp.setTime(addDaysToDate(temp, 7));
	}
	return temp;
}
var findStartOfMonthlyIncome = function(start_date, today_date, today_month, today_year, page, date_from, date_to){
	if(page == "search")
	{
		var date = start_date;
		while(date <= date_to)
		{
			var start_date_arr = getDateFromEpoch(start_date);
			var start_date_date = start_date_arr[2];
			var start_date_month = start_date_arr[1];
			var start_date_year = start_date_arr[0];
			var noOfDaysInNextMonth = getNoOfDaysInThisMonth(start_date_month + 1, start_date_year);
			start_date_month = start_date_month + 1;

			if(start_date_date > noOfDaysInNextMonth)
			{
				date = dateToEpoch(start_date_year + '/' + start_date_month + '/' + noOfDaysInNextMonth);
			}
			else
			{
				date = dateToEpoch(start_date_year + '/' + start_date_month + '/' + start_date_date);
			}
			if(date >= date_from && date <= date_to)
			{
				return date;
			}
			start_date = date;
		}
		return -1;
	}
	else
	{
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
}
var findStartOfYearlyIncome = function(start_date, today_date, today_month, today_year, page, date_from, date_to){
	if(page == "search")
	{
		var date = start_date;
		while(date <= date_to)
		{
			var start_date_arr = getDateFromEpoch(start_date);
			var start_date_date = start_date_arr[2];
			var start_date_month = start_date_arr[1];
			var start_date_year = start_date_arr[0];
			var noOfDaysInNextMonth = getNoOfDaysInThisMonth(start_date_month, start_date_year + 1);
			start_date_year = start_date_year + 1;

			if(start_date_date > noOfDaysInNextMonth)
			{
				date = dateToEpoch(start_date_year + '/' + start_date_month + '/' + noOfDaysInNextMonth);
			}
			else
			{
				date = dateToEpoch(start_date_year + '/' + start_date_month + '/' + start_date_date);
			}
			if(date >= date_from && date <= date_to)
			{
				return date;
			}
			start_date = date;
		}
		return -1;
	}
	else
	{
		var start_date_arr = getDateFromEpoch(start_date);
		var start_date_month = start_date_arr[1];
		if(start_date_month == today_month)
		{
			return findStartOfMonthlyIncome(start_date, today_date, today_month, today_year, page, date_from, date_to);
		}
		else
		{
			return -1;
		}
	}
}

var calculateDailyIncomes = function(obj, today_date, today_month, today_year, page, date_from, date_to){
	var income_date = obj.date;
	if(page == "search")
	{
		var epochOfFirstDayOfMonth = date_from;
		var _epochOfLastDayOfMonth = date_to;
	}
	else
	{
		var epochOfFirstDayOfMonth = epochOfDayOfMonth(1, today_month, today_year);
		var _epochOfLastDayOfMonth = epochOfLastDayOfMonth(today_month, today_year);
	}
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
			if(income_date >= epochOfFirstDayOfMonth && income_date <= _epochOfLastDayOfMonth)
			{
				addIncomesToArray(obj, income_date, _epochOfLastDayOfMonth, true);
			}
			return;
		}
	}


	if(!todayLiesInThisMonth) //Here today lies after this current month
	{
		if(income_date <= epochOfFirstDayOfMonth)
		{
			addIncomesToArray(obj, epochOfFirstDayOfMonth, _epochOfLastDayOfMonth, false);
		}
		else if(income_date > epochOfFirstDayOfMonth && income_date <= _epochOfLastDayOfMonth)
		{
			addIncomesToArray(obj, income_date, _epochOfLastDayOfMonth, false);
		}
	}
	else
	{
		if(income_date <= epochOfFirstDayOfMonth)
		{
			addIncomesToArray(obj, epochOfFirstDayOfMonth, epochOfToday, false);
		}
		else if(income_date > epochOfFirstDayOfMonth && income_date <= epochOfToday)
		{
			addIncomesToArray(obj, income_date, epochOfToday, false);
		}
		else if(income_date > epochOfToday && income_date <= _epochOfLastDayOfMonth)
		{
			addIncomesToArray(obj, income_date, _epochOfLastDayOfMonth, true);
		}
	}
}
var calculateWeeklyIncomes = function(obj, today_date, today_month, today_year, page, date_from, date_to){
	var income_date = obj.date;
	if(page == "search")
	{
		var epochOfFirstDayOfMonth = date_from;
		var _epochOfLastDayOfMonth = date_to;
	}
	else
	{
		var epochOfFirstDayOfMonth = epochOfDayOfMonth(1, today_month, today_year);
		var _epochOfLastDayOfMonth = epochOfLastDayOfMonth(today_month, today_year);
	}
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
			if(income_date >= epochOfFirstDayOfMonth && income_date <= _epochOfLastDayOfMonth)
			{
				addIncomesToArray(obj, income_date, _epochOfLastDayOfMonth, true);
			}
			return;
		}
	}


	if(!todayLiesInThisMonth) //Here today lies after this current month
	{
		if(income_date <= epochOfFirstDayOfMonth)
		{
			var start_date = findStartOfWeeklyIncome(income_date, epochOfFirstDayOfMonth);
			addIncomesToArray(obj, start_date, _epochOfLastDayOfMonth, false);
		}
		else if(income_date > epochOfFirstDayOfMonth && income_date <= _epochOfLastDayOfMonth)
		{
			addIncomesToArray(obj, income_date, _epochOfLastDayOfMonth, false);
		}
	}
	else
	{
		if(income_date <= epochOfFirstDayOfMonth)
		{
			var start_date = findStartOfWeeklyIncome(income_date, epochOfFirstDayOfMonth);
			addIncomesToArray(obj, start_date, epochOfToday, false);
		}
		else if(income_date > epochOfFirstDayOfMonth && income_date <= epochOfToday)
		{
			addIncomesToArray(obj, income_date, epochOfToday, false);
		}
		else if(income_date > epochOfToday && income_date <= _epochOfLastDayOfMonth)
		{
			addIncomesToArray(obj, income_date, _epochOfLastDayOfMonth, true);
		}
	}
}
var calculateMonthlyIncomes = function(obj, today_date, today_month, today_year, page, date_from, date_to){
	var income_date = obj.date;
	if(page == "search")
	{
		var epochOfFirstDayOfMonth = date_from;
		var _epochOfLastDayOfMonth = date_to;
	}
	else
	{
		var epochOfFirstDayOfMonth = epochOfDayOfMonth(1, today_month, today_year);
		var _epochOfLastDayOfMonth = epochOfLastDayOfMonth(today_month, today_year);
	}
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
			if(income_date >= epochOfFirstDayOfMonth && income_date <= _epochOfLastDayOfMonth)
			{
				addIncomesToArray(obj, income_date, _epochOfLastDayOfMonth, true);
			}
			return;
		}
	}


	if(!todayLiesInThisMonth) //Here today lies after this current month
	{
		if(income_date <= epochOfFirstDayOfMonth)
		{
			var start_date = findStartOfMonthlyIncome(income_date, today_date, today_month, today_year, page, date_from, date_to);
			if(start_date != -1)
			{
				addIncomesToArray(obj, start_date, _epochOfLastDayOfMonth, false);
			}
		}
		else if(income_date > epochOfFirstDayOfMonth && income_date <= _epochOfLastDayOfMonth)
		{
			addIncomesToArray(obj, income_date, _epochOfLastDayOfMonth, false);
		}
	}
	else
	{
		if(income_date <= epochOfFirstDayOfMonth)
		{
			var start_date = findStartOfMonthlyIncome(income_date, today_date, today_month, today_year, page, date_from, date_to);
			if(start_date != -1)
			{
				addIncomesToArray(obj, start_date, epochOfToday, false);
			}
		}
		else if(income_date > epochOfFirstDayOfMonth && income_date <= epochOfToday)
		{
			addIncomesToArray(obj, income_date, epochOfToday, false);
		}
		else if(income_date > epochOfToday && income_date <= _epochOfLastDayOfMonth)
		{
			addIncomesToArray(obj, income_date, _epochOfLastDayOfMonth, true);
		}
	}
}
var calculateYearlyIncomes = function(obj, today_date, today_month, today_year, page, date_from, date_to){
	var income_date = obj.date;
	if(page == "search")
	{
		var epochOfFirstDayOfMonth = date_from;
		var _epochOfLastDayOfMonth = date_to;
	}
	else
	{
		var epochOfFirstDayOfMonth = epochOfDayOfMonth(1, today_month, today_year);
		var _epochOfLastDayOfMonth = epochOfLastDayOfMonth(today_month, today_year);
	}
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
			if(income_date >= epochOfFirstDayOfMonth && income_date <= _epochOfLastDayOfMonth)
			{
				addIncomesToArray(obj, income_date, _epochOfLastDayOfMonth, true);
			}
			return;
		}
	}


	if(!todayLiesInThisMonth) //Here today lies after this current month
	{
		if(income_date <= epochOfFirstDayOfMonth)
		{
			var start_date = findStartOfYearlyIncome(income_date, today_date, today_month, today_year, page, date_from, date_to);
			if(start_date != -1)
			{
				addIncomesToArray(obj, start_date, _epochOfLastDayOfMonth, false);
			}
		}
		else if(income_date > epochOfFirstDayOfMonth && income_date <= _epochOfLastDayOfMonth)
		{
			addIncomesToArray(obj, income_date, _epochOfLastDayOfMonth, false);
		}
	}
	else
	{
		if(income_date <= epochOfFirstDayOfMonth)
		{
			var start_date = findStartOfYearlyIncome(income_date, today_date, today_month, today_year, page, date_from, date_to);
			if(start_date != -1)
			{
				addIncomesToArray(obj, start_date, epochOfToday, false);
			}
		}
		else if(income_date > epochOfFirstDayOfMonth && income_date <= epochOfToday)
		{
			addIncomesToArray(obj, income_date, epochOfToday, false);
		}
		else if(income_date > epochOfToday && income_date <= _epochOfLastDayOfMonth)
		{
			addIncomesToArray(obj, income_date, _epochOfLastDayOfMonth, true);
		}
	}
}

var printIncomes = function(){
	$('.income-table tbody').html("");
	$('.income-table tbody').append("<tr><th class='amount-col'>Amount</th><th class='date-col'>Date</th><th class='tag-col'>Tag</th><th class='description-col'>Description</th><th class='addedby-col'>Added by</th><th class='location-col'>Location</th><th class='edit-col'></th></tr>");
	var data = globalObject.incomes_in_month;
	for(var i=0;i<data.length;i++)
	{
		var obj = data[i];
		let transaction_id = obj.transaction_id;
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
		globalObject.income_data.push({y:amount,indexLabel:amount+"(#percent%)",legendText:""+amount,description:description,date:date,tag:tag});
		let append_string = "<tr data-tid='"+transaction_id+"'><td><i class='icon-inr'></i>"+amount+"</td><td>"+date+"</td><td>"+tag+"</td><td>"+description+"</td><td>"+user_name+"</td><td>"+location;
		if(lat != "-" && lon != "-")
		{
			append_string += "<i class='icon-location-arrow location-icon icon-clickable' data-lat="+lat+" data-lon="+lon+"></td><td><i class='icon-edit income-edit icon-clickable'></i></td></tr>";
		}
		else
		{
			append_string += "</td><td><i class='icon-edit income-edit icon-clickable'></i></td></tr>";
		}
		$('.income-table tbody').append(append_string);
	}
	income_chart.options.data[0].dataPoints = globalObject.income_data;
	if(globalObject.income_data.length != 0)
	{
		$('#empty-income-data').css("display","none");
		$('#chartContainer1').css("display","block");
		$('.income-table-space').css("display","block");
		income_chart.render();
	}
	else
	{
		$('#chartContainer1').css("display","none");
		$('.income-table-space').css("display","none");
		$('#empty-income-data').css("display","block");
		$('#empty-income-data').html("<img src='images/nodata2.svg'><div class='empty-data-text'>No incomes for this month</div>");
	}
	globalObject.income_data = [];
	// globalObject.incomes_in_month = [];
}

var findIncomes = function(data, page, date_from, date_to)
{
	var date_from = date_from || -1;
	var date_to = date_to || -1;
	globalObject.incomes_in_month = [];
	for(var i=0;i<data.length;i++)
	{
		var obj = jQuery.parseJSON(data[i]);
		var date_arr = getDateFromEpoch(obj.date);
		var income_date = date_arr[2];
		var income_month = date_arr[1];
		var income_year = date_arr[0];
		var today_date = globalObject.date;
		var today_month = globalObject.month;
		var today_year = globalObject.year;

		if(income_year <= today_year && !(income_year == today_year && income_month > today_month))
		{
			var repeat_period = obj.repeat_period;
			if(repeat_period == 0)
			{
				if(page == "search")
				{
					if(obj.date >= date_from && obj.date <= date_to)
					{
						globalObject.incomes_in_month.push(obj);
					}
				}
				else if(income_year == today_year && income_month == today_month)
				{
					globalObject.incomes_in_month.push(obj);
				}
			}
			else if(repeat_period == 1)
			{
				calculateDailyIncomes(obj, today_date, today_month, today_year, page, date_from, date_to);
			}
			else if(repeat_period == 2)
			{
				calculateWeeklyIncomes(obj, today_date, today_month, today_year, page, date_from, date_to);
			}
			else if(repeat_period == 3)
			{
				calculateMonthlyIncomes(obj, today_date, today_month, today_year, page, date_from, date_to);
			}
			else if(repeat_period == 4)
			{
				calculateYearlyIncomes(obj, today_date, today_month, today_year, page, date_from, date_to);
			}
		}
	}
	if(page == "income")
	{
		printIncomes();
	}
}



