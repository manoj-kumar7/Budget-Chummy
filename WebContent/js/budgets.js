var getTotalExpense = function(expenses, start_date, end_date){
	var total = 0;
	for(var i=0; i<expenses.length; i++)
	{
		var obj = expenses[i];
		if(obj.date >= start_date && obj.date <= end_date)
		{
			total = total + obj.amount;
		}
	}
	return total;
}

var getTagExpense = function(expenses, tag_name, start_date, end_date){
	var total = 0;
	for(var i=0; i<expenses.length; i++)
	{
		var obj = expenses[i];
		if(obj.tag_name == tag_name && obj.date >= start_date && obj.date <= end_date)
		{
			total = total + obj.amount;
		}
	}
	return total;
}

var filterBudget = function(budgets)
{
	globalObject.budgets_in_month = [];
	if(budgets != null && budgets != "" && budgets.length != 0)
	{
		for(var i=0; i<budgets.length; i++)
		{
			var obj = jQuery.parseJSON(budgets[i]);
			if(obj.repeat_period == 0)
			{
				if(obj.end_date >= epochOfFirstDayOfMonth(globalObject.month, globalObject.year))
				{
					globalObject.budgets_in_month.push(obj);
				}
			}
			else
			{
				globalObject.budgets_in_month.push(obj);
			}
		}
		analyseBudget();
	}
	else
	{
		$('#home-budgets-list').html("<div class='empty-data-text'>No budgets for this month</div>");
		$('#budget-page-header').css("display","none");
		$('#budget-page-stat').css("display","none");
		$('#budget-chart-space').css("display","none");
		$('#budget-timespan').css("display","none");
		$('#empty-budget-data').css("display","block");
		return;
	}
	
}

var getStartEndDatesOfBudget = function(obj){
	var start_date, end_date, dates;
	if(obj.repeat_period == 0)
	{
		start_date = obj.start_date;
		end_date = obj.end_date;
	}
	else if(obj.repeat_period == 1)
	{
		if(globalObject.month == getCurrentRealMonth())
		{
			start_date = end_date = getTodayEpoch();
		}
		else
		{
			start_date = end_date = epochOfFirstDayOfMonth(globalObject.month, globalObject.year);
		}
	}
	else if(obj.repeat_period == 2)
	{
		dates = calculateWeekStartEndDates(obj.start_date);
		start_date = dates[0];
		end_date = dates[1];
	}
	else if(obj.repeat_period == 3)
	{
		dates = calculateMonthStartEndDates(obj.start_date);
		start_date = dates[0];
		end_date = dates[1];
	}
	else if(obj.repeat_period == 4)
	{
		dates = calculateYearStartEndDates(obj.start_date);
		start_date = dates[0];
		end_date = dates[1];
	}
	return [start_date, end_date];
}

var analyseBudget = function(){
	var expenses= globalObject.expenses_in_month;
	var budgets = globalObject.budgets_in_month;
	$('#home-budgets-list').html("");
	var tag_name, budget_amount, repeat_periods=["One time budget", "Daily", "Weekly", "Monthly", "Yearly"], repeat, amount_used, amount_used_text, amount_left, amount_over = 0, amount_left_or_over_text;
	var budget_bar_spent, budget_bar_spent_width, budget_bar_left_width;
	var start_date, end_date, dates;

	var count = 1;
	for(var i=0; i<budgets.length; i++)
	{
		var obj = budgets[i];
		dates = getStartEndDatesOfBudget(obj);
		start_date = dates[0];
		end_date = dates[1];
		if(obj.budget_type == 0)
		{
			tag_name = "All Expenses";
			amount_spent = getTotalExpense(expenses, start_date, end_date);

		}
		else if(obj.budget_type == 1)
		{
			tag_name = obj.tag_name;
			amount_spent = getTagExpense(expenses, tag_name, start_date, end_date);
		}
		budget_amount = obj.amount;
		repeat = repeat_periods[obj.repeat_period];
		amount_used_text = amount_spent + " used";
		amount_left = budget_amount - amount_spent;
		if(amount_left < 0)
		{
			amount_over = -(amount_left);
			amount_left_or_over_text = amount_over + " over";
		}
		else
		{
			amount_left_or_over_text = amount_left + " left";
		}
		budget_bar_spent = ( amount_spent / budget_amount ) * 100;
		if(budget_bar_spent > 100)
		{
			budget_bar_spent_width = '100%';
			budget_bar_left_width = '0%';
		}
		else
		{
			budget_bar_spent_width = budget_bar_spent + '%';
			budget_bar_left_width = (100 - budget_bar_spent) + '%';
		}
		$('#home-budgets-list').append('<div id="budget'+count+'" class="budget-container">' +
											'<div class="budget-header">' +
												'<span class="budget-tag text">'+tag_name+'</span>' +
												'<span class="seperator-hyphen text">-</span>' +
												'<span class="budget-amount text"><i class="icon-inr"></i>'+budget_amount+'</span>' +
												'<span class="budget-repeat text">'+repeat+'</span>' +
											'</div>' +
											'<div class="budget-status">' +
												'<div class="budget-status-left-bar budget-status-bar"></div><div class="budget-status-right-bar budget-status-bar"></div>' +
												'<span class="budget-amount-used text"><i class="icon-inr"></i>'+amount_used_text+'</span>' +
												'<span class="budget-amount-left text"><i class="icon-inr"></i>'+amount_left_or_over_text+'</span>' +
											'</div>' +
									   '</div>');
		$('#home-budgets-list').find('#budget'+count).find('.budget-status-left-bar').css('width',budget_bar_spent_width);
		$('#home-budgets-list').find('#budget'+count).find('.budget-status-right-bar').css('width',budget_bar_left_width);
		count++;
	}
	getBudgetDataForStat(1);
	// google.charts.setOnLoadCallback(budget_chart());
}

var getBudgetDataForStat = function(budget_number){
	var obj = globalObject.budgets_in_month[parseInt(budget_number)-1];
	var start_date, end_date, dates, dates1, amount_left_per_day, amount_left_per_day_text = "";
	var timespan_string = "";
	if(obj == undefined || obj == null)
	{
		$('#budget-page-header').css("display","none");
		$('#budget-page-stat').css("display","none");
		$('#budget-chart-space').css("display","none");
		$('#budget-timespan').css("display","none");
		$('#empty-budget-data').css("display","block");
		return;
	}
	$('.budget-container').removeClass('active');
	$('#budget'+budget_number).addClass('active');
	
	$('#budget-page-header').css("display","block");
	$('#budget-page-stat').css("display","block");
	$('#budget-timespan').css("display","block");
	$('#budget-chart-space').css("display","block");
	$('#empty-budget-data').css("display","none");
	if(obj.tag_name == null || obj.tag_name == "")
	{
		$("#budget-page-tag").text("All Expenses");
	}
	else
	{
		$("#budget-page-tag").text(obj.tag_name);
	}
	
	if(obj.repeat_period == 0)
	{
		$("#budget-page-repeat").html("<span class='budget-repeat-tag-span'>One time budget</span>");
	}
	else if(obj.repeat_period == 1)
	{
		$("#budget-page-repeat").html("<span class='budget-repeat-tag-span'>Daily</span>");
	}
	else if(obj.repeat_period == 2)
	{
		$("#budget-page-repeat").html("<span class='budget-repeat-tag-span'>Weekly</span>");
	}
	else if(obj.repeat_period == 3)
	{
		$("#budget-page-repeat").html("<span class='budget-repeat-tag-span'>Monthly</span>");
	}
	else if(obj.repeat_period == 4)
	{
		$("#budget-page-repeat").html("<span class='budget-repeat-tag-span'>Yearly</span>");
	}
	
	$("#budget-page-amount").html('<i class="icon-inr"></i><span>'+obj.amount+'</span>');
	
	dates = getStartEndDatesOfBudget(obj);
	start_date = dates[0];
	end_date = dates[1];

	if(obj.tag_name == null || obj.tag_name == "")
	{
		var amount_spent = getTotalExpense(globalObject.expenses_in_month, start_date, end_date);
	}
	else
	{
		var amount_spent = getTagExpense(globalObject.expenses_in_month, obj.tag_name, start_date, end_date);
	}
	$("#budget-page-spent").html('<i class="icon-inr"></i><span>'+amount_spent+'</span>' + " spent");
	
	var amount_left = obj.amount - amount_spent;
	if(amount_left < 0)
	{
		amount_over = -(amount_left);
		var amount_left_or_over_text = amount_over + " over";
	}
	else
	{
		var amount_left_or_over_text = amount_left + " left";
	}
	$("#budget-page-left").html('<i class="icon-inr"></i><span>'+amount_left_or_over_text+'</span>');

	if(amount_left < 0)
	{
		$("#budget-page-hint").html("");
	}
	else
	{
		var diff_days = calculateNoOfDaysToEndBudget(start_date, end_date);
		if(diff_days == -1)
		{
			$("#budget-page-hint").html("");
		}
		else
		{
			amount_left_per_day = (amount_left / diff_days).toFixed(2);
			amount_left_per_day_text = amount_left_per_day_text + amount_left_per_day + " left per day";
			$("#budget-page-hint").html('<i class="icon-inr"></i><span>'+amount_left_per_day_text+'</span>');
		}

	}

	
	dates = getDateFromEpoch(start_date);
	timespan_string = timespan_string + formCustomDateFormat(dates);
	timespan_string = timespan_string + " - ";

	dates = getDateFromEpoch(end_date);
	timespan_string = timespan_string + formCustomDateFormat(dates);
	$('#budget-timespan #budget-timespan-period').html(timespan_string);
}
