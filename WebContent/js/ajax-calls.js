			var login_ajax_call=function(){
				NProgress.start();
				var email = $('#email').val();
				var pword = $('#pword').val();
				var account_id = $('#account_id').val();
				var invitation_id = $('#invitation_id').val();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/login",
					data:{email:email,pword:pword,account_id:account_id,invitation_id:invitation_id},
					async: false,
					success:function(data){
						NProgress.done();
						//showAjaxSuccessMessage("Logged in successfully");
						if(account_id=="null" || invitation_id=="null")
						{
							location.href = "ChooseAccount";
						}
						else
						{
							location.href = "AccountAuthentication?account_id="+account_id+"&invitation_id="+invitation_id;
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						if(jqXHR.status == 401)
						{
							showAjaxFailureMessage("Invalid email and/or password");
						}
						else if(jqXHR.status == 403)
						{
							location.href = "error";
						}
					}
				});	
			}
			var google_login = function(id_token){
				var account_id = $('#account_id').val();
				var invitation_id = $('#invitation_id').val();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/login/google",
					data:{id_token:id_token,account_id:account_id,invitation_id:invitation_id},
					async: false,
					success:function(data){
						//showAjaxSuccessMessage("Signed up successfully");
						if(account_id=="null" || invitation_id=="null")
						{
							location.href = "ChooseAccount";
						}
						else
						{
							location.href = "AccountAuthentication?account_id="+account_id+"&invitation_id="+invitation_id;
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						if(jqXHR.status == 401)
						{
							showAjaxFailureMessage("Invalid email and/or password");
						}
						else if(jqXHR.status == 403)
						{
							location.href = "error";
						}
						else if(jqXHR.status == 9001)
						{
							showAjaxFailureMessage("Your google account needs to be verified");
						}
					}
				});	
			}			
			var signup_ajax_call=function(){
				NProgress.start();
				var firstname = $('#firstname').val();
				var lastname = $('#lastname').val();
				var email = $('#email').val();
				var pword = $('#pword').val();
				var account_id = $('#account_id').val();
				var invitation_id = $('#invitation_id').val();
				var created_date_time = getAccountTimeZoneEpoch();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/signUp",
					data:{first_name:firstname,last_name:lastname,email:email,pword:pword,account_id:account_id,invitation_id:invitation_id, created_date_time:created_date_time},
					async: false,
					success:function(data){
						NProgress.done();
						//showAjaxSuccessMessage("Signed up successfully");
						if(account_id=="null" || invitation_id=="null")
						{
							// location.href = "CreateAccount";
							$('#signup-form-div').css('display', 'none');
							$('.activation-link-sent').css('display', 'block');
						}
						else
						{
							location.href = "AccountAuthentication?account_id="+account_id+"&invitation_id="+invitation_id;
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						if(jqXHR.status == 401)
						{
							showAjaxFailureMessage("This email address already has a BC account");
						}
					}
				});	
			}
			var google_signup = function(id_token){
				var account_id = $('#account_id').val();
				var invitation_id = $('#invitation_id').val();
				var created_date_time = getAccountTimeZoneEpoch();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/signUp/google",
					data:{id_token:id_token,account_id:account_id,invitation_id:invitation_id, created_date_time:created_date_time},
					async: false,
					success:function(data){
						//showAjaxSuccessMessage("Signed up successfully");
						if(account_id=="null" || invitation_id=="null")
						{
							location.href = "CreateAccount";
						}
						else
						{
							location.href = "AccountAuthentication?account_id="+account_id+"&invitation_id="+invitation_id;
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						if(jqXHR.status == 400)
						{
							showAjaxFailureMessage("Invalid credentials");
						}
						else if(jqXHR.status == 401)
						{
							showAjaxFailureMessage("This email address already has a BC account");
						}
						else if(jqXHR.status == 9001)
						{
							showAjaxFailureMessage("Your google account needs to be verified");
						}
					}
				});	
			}
			var activate_account_ajax_call = function(code, email){
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/activateAccount",
					data:{code:code, email:email},
					async: false,
					success:function(data){
						$('.account-activated').css('display','block');
					},
					error:function(jqXHR, txtStatus, errThrown){
						$('.account-not-activated').css('display','block');
					}
				});	
			}

			var create_account_ajax_call=function(accountname){
				var timezone = getAccountTimeZone();
				var created_date_time = getAccountTimeZoneEpoch();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/createAccount",
					data:{account_name:accountname, timezone:timezone, created_date_time:created_date_time},
					async: false,
					success:function(data){
						//showAjaxSuccessMessage("Signed up successfully");
						location.href = "home";
					},
					error:function(jqXHR, txtStatus, errThrown){
						$('#create-account').prop('disabled',false);
						enterKeyPressed = false;
						//showAjaxFailureMessage("");
					}
				});	
			}
			
			var join_account_ajax_call = function(){
				var passcode = $('#passcode').val();
				var account_id = $('#account_id').val();
				var invitation_id = $('#invitation_id').val();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/joinAccount",
					data:{passcode: passcode,account_id:account_id,invitation_id:invitation_id},
					async: false,
					success:function(data){
						location.href = "home";
					},
					error:function(jqXHR, txtStatus, errThrown){
						if(jqXHR.status == 400)
						{
							location.href = "login";
						}
						else if(jqXHR.status == 401)
						{
							showAjaxFailureMessage("Incorrect passcode");
						}
						else if(jqXHR.status == 402)
						{
							showAjaxFailureMessage("An invitation is required to join this account");
						}
					}
				});	
			}

			var get_income_ajax_call=function(month,year)
		 	{
				NProgress.start();
				$.ajax({
					type:"GET",
					url:"/BudgetChummy/api/v1/income",
					data:{month:month,year:year},
					success:function(data){
						NProgress.done();
						if(data!=null)
						{
							findIncomes(data, "income");
						}
						else
						{
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
		 	}
			
			var get_expense_ajax_call=function(month,year,page)
		 	{
				NProgress.start();
				$.ajax({
					type:"GET",
					url:"/BudgetChummy/api/v1/expense",
					data:{month:month,year:year,page:page},
					success:function(data){
						NProgress.done();
						if(page == "expense" && data != null)
						{
							findExpenses(data, page);
						}
						else if(page == "budget")
						{
							globalObject.expenses_in_month = [];
							if(data != null && data.length != 0)
							{
								for(var i=0;i<data.length;i++)
								{
									var obj = jQuery.parseJSON(data[i]);
									globalObject.expenses_in_month.push(obj);
								}
							}
							findExpenses(data, page);
							//get_budget_ajax_call(globalObject.month, globalObject.year);
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
		 	}

			var get_budget_ajax_call=function(month, year)
			{
				NProgress.start();
				$.ajax({
					type:"GET",
					url:"/BudgetChummy/api/v1/budget",
					data:{month:month,year:year},
					success:function(data){
						NProgress.done();
						filterBudget(data);
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
			}
			
			var save_income_ajax_call=function(month,year)
		 	{
				var amount = $('.generic-amount').val();
				var date = $('.generic-datepicker').val();
				var tag_id = $('.saved-tags-dropdown').val();
				var add_info = $('.generic-additional-info').val();
				var location = $('.generic-location').val();
				var lat = $('.generic-location-lat').val();
				var lon = $('.generic-location-lon').val();
				var description = $('.generic-description').val();
				var repeat = $('.generic-repeat-dropdown').val();
				var reminder = $('.generic-reminder-dropdown').val();
				var created_date_time = getAccountTimeZoneEpoch();
				var ok = validateIncomeExpenseData(amount, date, tag_id, add_info, location, lat, lon, description, repeat, reminder);
				if(!ok)
				{
					showAjaxFailureMessageHome("Invalid Input");
					return;
				}
				amount = parseFloat(amount).toFixed(2);
				date = dateToEpoch(date);
				$('.generic-save').attr("disabled", "disabled");
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/income",
					data:{amount:amount, date:date, tag_id:tag_id, add_info:add_info, location: location, location_lat:lat, location_lon:lon, description:description, repeat:repeat, reminder:reminder, created_date_time:created_date_time},
					success:function(data){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						$('#generic-modal-form').find('.close-icon').click();
						get_income_ajax_call(globalObject.month, globalObject.year);
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
		 	}
			
			var save_expense_ajax_call=function(month,year)
		 	{
				var amount = $('.generic-amount').val();
				var date = $('.generic-datepicker').val();
				var tag_id = $('.saved-tags-dropdown').val();
				var add_info = $('.generic-additional-info').val();
				var location = $('.generic-location').val();
				var lat = $('.generic-location-lat').val();
				var lon = $('.generic-location-lon').val();
				var description = $('.generic-description').val();
				var repeat = $('.generic-repeat-dropdown').val();
				var reminder = $('.generic-reminder-dropdown').val();
				var created_date_time = getAccountTimeZoneEpoch();
				var ok = validateIncomeExpenseData(amount, date, tag_id, add_info, location, lat, lon, description, repeat, reminder);
				if(!ok)
				{
					showAjaxFailureMessageHome("Invalid Input");
					return;
				}
				amount = parseFloat(amount).toFixed(2);
				date = dateToEpoch(date);
				$('.generic-save').attr("disabled", "disabled");
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/expense",
					data:{amount:amount, date:date, tag_id:tag_id, add_info:add_info, location: location, location_lat:lat, location_lon:lon, description:description, repeat:repeat, reminder:reminder, created_date_time:created_date_time},
					success:function(data){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						$('#generic-modal-form').find('.close-icon').click();
						if($('#expense-tab').hasClass('active'))
						{
							get_expense_ajax_call(globalObject.month, globalObject.year, "expense");
						}
						else if($('#budget-tab').hasClass('active'))
						{
							get_expense_ajax_call(globalObject.month, globalObject.year, "budget");
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
		 	}
			
			var save_budget_ajax_call=function(month,year)
		 	{
				var budget_type = $('#budget-type-dropdown').val();
				var tag_id = $('.saved-tags-dropdown').val() || -1;
				var budget_repeat = $('#budget-repeat-dropdown').val();
				var start_date = $('#budget-start-datepicker').val() || -1;
				var end_date = $('#budget-end-datepicker').val() || -1;
				var amount = $('#budget-amount').val();
				var description = $('#budget-description').val();
				var ok = validateBudgetData(budget_type, tag_id, budget_repeat, start_date, end_date, amount, description);
				if(!ok)
				{
					showAjaxFailureMessageHome("Invalid Input");
					return;
				}
				amount = parseFloat(amount).toFixed(2);
				start_date = dateToEpoch(start_date);
				end_date = dateToEpoch(end_date);
				$('.generic-save').attr("disabled", "disabled");
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/budget",
					data:{budget_type:budget_type, tag_id:tag_id, budget_repeat:budget_repeat, budget_start_date:start_date, budget_end_date:end_date, budget_amount:amount, budget_description:description},
					success:function(data){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						$('#generic-modal-form').find('.close-icon').click();
						get_budget_ajax_call(month, year);
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
		 	}
		 	var edit_income_ajax_call=function(month,year)
		 	{
		 		var tid = $('#edit-tid').val();
				var amount = $('.generic-amount').val();
				var date = $('.generic-datepicker').val();
				var tag_id = $('.saved-tags-dropdown').val();
				var add_info = $('.generic-additional-info').val();
				var location = $('.generic-location').val();
				var lat = $('.generic-location-lat').val();
				var lon = $('.generic-location-lon').val();
				var description = $('.generic-description').val();
				var repeat = $('.generic-repeat-dropdown').val();
				var reminder = $('.generic-reminder-dropdown').val();
				var ok = validateIncomeExpenseData(amount, date, tag_id, add_info, location, lat, lon, description, repeat, reminder);
				if(!ok)
				{
					showAjaxFailureMessageHome("Invalid Input");
					return;
				}
				amount = parseFloat(amount).toFixed(2);
				date = dateToEpoch(date);
				$('.generic-save').attr("disabled", "disabled");
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/income/edit",
					data:{transaction_id: tid, amount:amount, date:date, tag_id:tag_id, add_info:add_info, location: location, location_lat:lat, location_lon:lon, description:description, repeat:repeat, reminder:reminder},
					success:function(data){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						$('#generic-modal-form').find('.close-icon').click();
						get_income_ajax_call(globalObject.month, globalObject.year);
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
		 	}
		 	var edit_expense_ajax_call=function(month,year)
		 	{
		 		var tid = $('#edit-tid').val();
				var amount = $('.generic-amount').val();
				var date = $('.generic-datepicker').val();
				var tag_id = $('.saved-tags-dropdown').val();
				var add_info = $('.generic-additional-info').val();
				var location = $('.generic-location').val();
				var lat = $('.generic-location-lat').val();
				var lon = $('.generic-location-lon').val();
				var description = $('.generic-description').val();
				var repeat = $('.generic-repeat-dropdown').val();
				var reminder = $('.generic-reminder-dropdown').val();
				var ok = validateIncomeExpenseData(amount, date, tag_id, add_info, location, lat, lon, description, repeat, reminder);
				if(!ok)
				{
					showAjaxFailureMessageHome("Invalid Input");
					return;
				}
				amount = parseFloat(amount).toFixed(2);
				date = dateToEpoch(date);
				$('.generic-save').attr("disabled", "disabled");
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/expense/edit",
					data:{transaction_id: tid, amount:amount, date:date, tag_id:tag_id, add_info:add_info, location: location, location_lat:lat, location_lon:lon, description:description, repeat:repeat, reminder:reminder},
					success:function(data){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						$('#generic-modal-form').find('.close-icon').click();
						get_expense_ajax_call(globalObject.month, globalObject.year, "expense");
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
		 	}
		 	var edit_budget_ajax_call=function(month,year)
		 	{
		 		var bid = $('#edit-bid').val();
				var budget_type = $('#budget-type-dropdown').val();
				var tag_id = $('.saved-tags-dropdown').val() || -1;
				var budget_repeat = $('#budget-repeat-dropdown').val();
				var start_date = $('#budget-start-datepicker').val() || -1;
				var end_date = $('#budget-end-datepicker').val() || -1;
				var amount = $('#budget-amount').val();
				var description = $('#budget-description').val();
				var ok = validateBudgetData(budget_type, tag_id, budget_repeat, start_date, end_date, amount, description);
				if(!ok)
				{
					showAjaxFailureMessageHome("Invalid Input");
					return;
				}
				amount = parseFloat(amount).toFixed(2);
				start_date = dateToEpoch(start_date);
				end_date = dateToEpoch(end_date);
				$('.generic-save').attr("disabled", "disabled");
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/budget/edit",
					data:{budget_id:bid, budget_type:budget_type, tag_id:tag_id, budget_repeat:budget_repeat, budget_start_date:start_date, budget_end_date:end_date, budget_amount:amount, budget_description:description},
					success:function(data){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						$('#generic-modal-form').find('.close-icon').click();
						get_budget_ajax_call(month, year);
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
		 	}

		 	var delete_income_ajax_call = function(){
		 		var tid = $('#edit-tid').val();
				$('.generic-delete').attr("disabled", "disabled");
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/income/delete",
					data:{transaction_id: tid},
					success:function(data){
						NProgress.done();
						$('.generic-delete').removeAttr("disabled");
						$('#generic-modal-form').find('.close-icon').click();
						get_income_ajax_call(globalObject.month, globalObject.year, "expense");
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						$('.generic-delete').removeAttr("disabled");
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
		 	}
		 	var delete_expense_ajax_call = function(){
		 		var tid = $('#edit-tid').val();
				$('.generic-delete').attr("disabled", "disabled");
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/expense/delete",
					data:{transaction_id: tid},
					success:function(data){
						NProgress.done();
						$('.generic-delete').removeAttr("disabled");
						$('#generic-modal-form').find('.close-icon').click();
						get_expense_ajax_call(globalObject.month, globalObject.year, "expense");
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						$('.generic-delete').removeAttr("disabled");
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
		 	}
		 	var delete_budget_ajax_call = function(){
		 		var bid = $('#edit-bid').val();
				$('.generic-delete').attr("disabled", "disabled");
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/budget/delete",
					data:{budget_id: bid},
					success:function(data){
						NProgress.done();
						$('.generic-delete').removeAttr("disabled");
						$('#generic-modal-form').find('.close-icon').click();
						// get_budget_ajax_call(globalObject.month, globalObject.year);
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						$('.generic-delete').removeAttr("disabled");
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
		 	}
			
			var search_ajax_call=function()
		 	{
				var date_from = $('#search-from').val();
				var date_to = $('#search-to').val();
				if(!validateDate(date_from) || !validateDate(date_to))
				{
					showAjaxFailureMessageHome("Invalid Input");
					return;
				}
				date_from = dateToEpoch(date_from);
				date_to = dateToEpoch(date_to);
				NProgress.start();
				$.ajax({
					type:"GET",
					url:"/BudgetChummy/api/v1/search",
					data:{date_from:date_from, date_to:date_to},
					success:function(data){
						NProgress.done();
						if(data!=null)
						{
							var obj;
							$.each(data, function(key, value) {
								if(key=="income_data")
								{
									findIncomes(value, "search", date_from, date_to);
									// for(var i=0;i<value.length;i++)
									// {
									// 	obj = jQuery.parseJSON(value[i]);
									// 	globalObject.income_search_data.push(obj);
									// }
								}
								else
								{
									findExpenses(value, "search", date_from, date_to);
									// for(var i=0;i<value.length;i++)
									// {
									// 	obj = jQuery.parseJSON(value[i]);
									// 	globalObject.expense_search_data.push(obj);
									// }									
								}
							});
							if(globalObject.incomes_in_month.length == 0)
							{
								$('#income_search_data').css('display','none');
								$('#empty-search-income-data').css('display','block');
							}
							else
							{
								$('#income_search_data').css('display','block');
								$('#empty-search-income-data').css('display','none');
								var income_search_data_string = "";
								for(var i=0;i<globalObject.incomes_in_month.length;i++)
								{
									let dates = getDateFromEpoch(globalObject.incomes_in_month[i].date);
									let date = formCustomDateFormat(dates);
									income_search_data_string += '<div class="income-bag search-bag"><div class="search-tag"><div class="tag-label">Tag</div><div class="tag-value">' + globalObject.incomes_in_month[i].tag_name + '</div></div><div class="search-amount"><div class="amount-label">Amount</div><div class="amount-value">' + globalObject.incomes_in_month[i].amount + '</div></div><div class="search-date"><div class="date-label">Date</div><div class="date-value">' + date + '</div></div></div>';			
								}
							}
							$('#income_search_data').html(income_search_data_string);
							if(globalObject.expenses_in_month.length == 0)
							{
								$('#expense_search_data').css('display','none');
								$('#empty-search-expense-data').css('display','block');
							}
							else
							{
								$('#expense_search_data').css('display','block');
								$('#empty-search-expense-data').css('display','none');
								var expense_search_data_string = "";
								for(var i=0;i<globalObject.expenses_in_month.length;i++)
								{
									let dates = getDateFromEpoch(globalObject.expenses_in_month[i].date);
									let date = formCustomDateFormat(dates);
									expense_search_data_string += '<div class="expense-bag search-bag"><div class="search-tag"><div class="tag-label">Tag</div><div class="tag-value">' + globalObject.expenses_in_month[i].tag_name + '</div></div><div class="search-amount"><div class="amount-label">Amount</div><div class="amount-value">' + globalObject.expenses_in_month[i].amount + '</div></div><div class="search-date"><div class="date-label">Date</div><div class="date-value">' + date + '</div></div></div>';			
								}
							}
							$('#expense_search_data').html(expense_search_data_string);
						}
						else
						{
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}

				});
				globalObject.income_search_data=[];
				globalObject.expense_search_data=[];
		 	}
			
			var users_ajax_call = function(){
				NProgress.start();
				$.ajax({
					type:"GET",
					url:"/BudgetChummy/api/v1/getUsers",
					success:function(data){
						NProgress.done();
						if(data != null)
						{
							var obj;
							obj = jQuery.parseJSON(data[0]);
							$('#users-account-name .value').html(obj.account_name);
							$('#users-no-of-members .value').html(obj.no_of_members);
							$('#users-created-on .value').html(obj.created_date_time);
							$('#users-created-by .value').html(obj.created_by);
							$('.users-table tbody').html("");
							$('.users-table tbody').append("<tr><th>User Name</th><th>Email</th><th>Role</th><th>Actions</th></tr>");														
							for(var i=1;i<data.length;i++)
							{
								obj = jQuery.parseJSON(data[i]);
								$('.users-table tbody').append("<tr user_id='"+obj.user_id+"'><td>"+obj.first_name+"</td><td>"+obj.email+"</td><td>"+obj.role+"</td><td><i class='icon-trash delete-user-icon'></i></td></tr>");
							}
							$('.users-table-space').css("display","block");
						}
						else
						{
							$('.users-table-space').css("display","none");
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}
				});				
			}
			
			var add_user_ajax_call = function(){
				$('#add-user-send').attr("disabled", "disabled");
				var to = $('#add-user-input').val();
				if(!validateEmail(to))
				{
					showAjaxFailureMessageHome("Invalid email id");
 					$('#add-user-send').removeAttr("disabled");
					return;
				}
				var authentication_type = $('input[name=authentication_type]:checked').val();
				if(authentication_type === "Offline")
				{
					var passcode = $('#offline-code').val();
					if(!validatePasscode(passcode))
					{
						showAjaxFailureMessageHome("Invalid passcode");
	 					$('#add-user-send').removeAttr("disabled");
						return;
					}
				}
				else
				{
					var passcode = '-1';
				}
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/addUser",
					data:{to: to, authentication_type: authentication_type, passcode: passcode},
					success:function(data){
						NProgress.done();
						$('#add-user-send').removeAttr("disabled");
						if(data == "" || data == null)
						{
							$('#addUserModal').find('.invitation-not-sent').html("");
							$('#addUserModal').modal('hide');
							showAjaxSuccessMessageHome("Invitation sent successfully");
						}
						else
						{
							var obj = jQuery.parseJSON(data[0]);
							if(obj.invitation_status === "joined")
							{
								$('#addUserModal').find('.invitation-not-sent').html("The user already exists in this account");
							}
							else if(obj.invitation_status === "not joined")
							{
								$('#addUserModal').find('.invitation-not-sent').html("An invitation is already sent to this email");
							}
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						$('#add-user-send').removeAttr("disabled");
						$('#addUserModal').find('.invitation-not-sent').html("");
						if(jqXHR.status == 400)
						{
							$('#addUserModal').modal('hide');
							location.href = "login";
						}
						else if(jqXHR.status == 401)
						{
							$('#addUserModal').find('.invitation-not-sent').html("You need to be an admin to send invitations");
						}
					}
				});				
			}
			var delete_user_ajax_call = function(delete_user_id){
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/deleteUser",
					data:{delete_user_id:delete_user_id},
					success:function(data){
						NProgress.done();
						showAjaxSuccessMessageHome("User deleted");
						$('#confirmDeleteUserModal').modal('hide');
						location.reload(true);
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						if(jqXHR.status == 400)
						{
							location.href = "login";
						}
						else if(jqXHR.status == 401)
						{
							showAjaxFailureMessageHome("You need to be an admin to delete users");
						}
						$('#confirmDeleteUserModal').modal('hide');
					}
				});					
			}
			var pending_invitations_ajax_call = function(){
				NProgress.start();
				$.ajax({
					type:"GET",
					url:"/BudgetChummy/api/v1/invitations",
					success:function(data){
						NProgress.done();
						if(data == "" || data == null)
						{
							$('#pendingInvitationsModal').find('.no-pending-invitations').css('display', 'block');
							$('#pendingInvitationsModal').find('.invitations-list').css('display', 'none');
						}
						else
						{
							$('#pendingInvitationsModal').find('.no-pending-invitations').css('display', 'none');
							$('#pendingInvitationsModal').find('.invitations-list').css('display', 'block');
							$('#pendingInvitationsModal').find('.invitations-list').html("");
							for(var i=0; i<data.length; i++)
							{
								var obj = jQuery.parseJSON(data[i]);
								$('#pendingInvitationsModal').find('.invitations-list').append("<div class='invitation-item'><div class='invitation-item-email'>"+obj.sent_to+"</div><div class='invitation-item-passcode'>"+obj.passcode+"</div></div>");
							}
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						NProgress.done();
						if(jqXHR.status == 400)
						{
							location.href = "login";
						}
					}
				});				
			}
			var getAccounts_ajax_call = function(page)
			{
				$.ajax({
					type:"GET",
					url:"/BudgetChummy/api/v1/getAccounts",
					data:{page:page},
					success:function(data){
						var obj;
						if(data != null)
						{
							if(page=="home")
							{
								var len = data.length-1;
							}
							else
							{
								var len = data.length;
							}
							for(var i=0;i<len;i++)
							{
								obj = jQuery.parseJSON(data[i]);
								if(page=="home")
								{
									$("#home-accounts-list").append("<div class='home-accounts' id="+obj.account_id+"><span>"+obj.account_name+"</span></div>");
								}
								else
								{
									$("#accounts-list").append("<div class='accounts' id="+obj.account_id+"><span>"+obj.account_name+"</span></div>");
								}
							}
							if(page=="home")
							{
								obj = jQuery.parseJSON(data[i]);
								if(obj.current_account != -1)
								{
									$("#home-accounts-list").find("#"+obj.current_account).addClass("active");
									$("#home-accounts-list").find("#"+obj.current_account).prepend("<i class='icon-ok'></i>");
								}
							}
							else
							{
								$("#accounts-list").append('<div class="create-new-account-div"><img src="images/create-account-img.svg" class="create-account-img"><div id="create-new-account">CREATE ACCOUNT</div></div>');
							}
						}
						else
						{
							if(page=="home")
							{
								$("#home-accounts-list").append("You dont have any accounts...");
							}
							else
							{
								$("#accounts-list").append("You dont have any accounts...");
							}
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
						else if(jqXHR.status == 403)
						{
							location.href = "error";
						}
					}
				});
			}
			
			var get_tags_ajax_call = function(tag_type){
				$.ajax({
					type:"GET",
					url:"/BudgetChummy/api/v1/tags",
					data:{tag_type:tag_type},
					async: false,
					success:function(data){
						if(data != null)
						{
							$('#saved-tags-dropdown').html("");
							if(tag_type == "income")
							{
								globalObject.income_tags_list = [];
							}
							else if(tag_type == "expense")
							{
								globalObject.expense_tags_list = [];
							}
							for(var i=0;i<data.length;i++)
							{
								var obj = jQuery.parseJSON(data[i]);
								if(tag_type == "income")
								{
									globalObject.income_tags_list.push({'tag_id': obj.tag_id, 'tag_name': obj.tag_name});
								}
								else if(tag_type == "expense")
								{
									globalObject.expense_tags_list.push({'tag_id': obj.tag_id, 'tag_name': obj.tag_name});
								}
								$("#saved-tags-dropdown").append("<option value='"+obj.tag_id+"'>"+obj.tag_name+"</option>");
							}
						}
						else
						{
							
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}
				});				
			}
			
			var save_tag_ajax_call = function(tag_type){
				var tag = $('#saved-tags-input').val();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/tags",
					data:{tag_name:tag, tag_type:tag_type},
					async: false,
					success:function(data){
						if(data != null)
						{

						}
						else
						{
							
						}
					},
					error:function(jqXHR, txtStatus, errThrown){
						if(jqXHR.status == 401)
						{
							location.href = "login";
						}
					}
				});				
			}
			
			var accountChosen_ajax_call=function(id, curr_page){
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/accountChosen",
					data:{account_id:id, page_name:curr_page},
					async: false,
					success:function(data)
					{
						location.href = "home";
					}
				});	
			}
			
			var logout_ajax_call = function(join_account){
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/logout",
					async: false,
					success:function()
					{
						google_logout();
						if(!join_account)
						{
							location.href = "BC";
						}
						else
						{
							location.reload(true);
						}
					}
				});	
			}
			
			var google_logout = function(){
			    var auth2 = gapi.auth2.getAuthInstance();
			    auth2.signOut().then(function () {
			      console.log('User signed out.');
			    });
			}
			var showAjaxSuccessMessage=function(data){
				$('#ajax-success-box .content').text(data);
				$('#ajax-success-box').show();
				$('#ajax-success-box').stop( true, true ).fadeOut(3000);
			}
			
			var showAjaxFailureMessage=function(data){
				$('#ajax-failure-box .content').text(data);
				$('#ajax-failure-box').show();
				$('#ajax-failure-box').stop( true, true ).fadeOut(3000);
			}
			var showAjaxSuccessMessageHome=function(data){
				$('#home-ajax-success-box .content').text(data);
				$('#home-ajax-success-box').show();
				$('#home-ajax-success-box').stop( true, true ).fadeOut(3000);
			}
			
			var showAjaxFailureMessageHome=function(data){
				$('#home-ajax-failure-box .content').text(data);
				$('#home-ajax-failure-box').show();
				$('#home-ajax-failure-box').stop( true, true ).fadeOut(3000);
			}