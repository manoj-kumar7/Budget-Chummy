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
			
			var signup_ajax_call=function(){
				NProgress.start();
				var firstname = $('#firstname').val();
				var lastname = $('#lastname').val();
				var email = $('#email').val();
				var pword = $('#pword').val();
				var account_id = $('#account_id').val();
				var invitation_id = $('#invitation_id').val();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/signUp",
					data:{first_name:firstname,last_name:lastname,email:email,pword:pword,account_id:account_id,invitation_id:invitation_id},
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

			var create_account_ajax_call=function(){
				var accountname = $('#accountname').val();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/createAccount",
					data:{account_name:accountname},
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
						$('.loader').removeClass('active');
						if(data!=null)
						{
							$('.income-table tbody').html("");
							$('.income-table tbody').append("<tr><th class='amount-col'>Amount</th><th class='date-col'>Date</th><th class='tag-col'>Tag</th><th class='description-col'>Description</th><th class='addedby-col'>Added by</th><th class='location-col'>Location</th></tr>");
							for(var i=0;i<data.length;i++)
							{
								var obj = jQuery.parseJSON(data[i]);
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
								let append_string = "<tr><td><i class='icon-inr'></i>"+amount+"</td><td>"+date+"</td><td>"+tag+"</td><td>"+description+"</td><td>"+user_name+"</td><td>"+location;
								if(lat != "-" && lon != "-")
								{
									append_string += "<img src='images/show_location_icon.png' class='image location-icon' alt='Show in map' data-lat="+lat+" data-lon="+lon+"></td></tr>";
								}
								else
								{
									append_string += "</td></tr>";
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
					    		$('#empty-income-data').html("<img src='images/no_results.png'><div class='empty-data-text'>No income for this month</div>");
					    	}
					    	globalObject.income_data=[];
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
						$('.loader').removeClass('active');
						if(page == "expense" && data != null)
						{
							$('.expense-table tbody').html("");
							$('.expense-table tbody').append("<tr><th class='amount-col'>Amount</th><th class='date-col'>Date</th><th class='tag-col'>Tag</th><th class='description-col'>Description</th><th class='addedby-col'>Added by</th><th class='location-col'>Location</th></tr>");
							for(var i=0;i<data.length;i++)
							{
								var obj = jQuery.parseJSON(data[i]);
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
					    	globalObject.expense_data=[];
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
							get_budget_ajax_call(globalObject.month, globalObject.year);
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
						$('.loader').removeClass('active');
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
		 		$('.generic-save').attr("disabled", "disabled");
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
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/income",
					data:{amount:amount, date:date, tag_id:tag_id, add_info:add_info, location: location, location_lat:lat, location_lon:lon, description:description, repeat:repeat, reminder:reminder},
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
		 		$('.generic-save').attr("disabled", "disabled");
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
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/expense",
					data:{amount:amount, date:date, tag_id:tag_id, add_info:add_info, location: location, location_lat:lat, location_lon:lon, description:description, repeat:repeat, reminder:reminder},
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
		 		$('.generic-save').attr("disabled", "disabled");
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
				NProgress.start();
				$.ajax({
					type:"POST",
					url:"/BudgetChummy/api/v1/budget",
					data:{budget_type:budget_type, tag_id:tag_id, budget_repeat:budget_repeat, budget_start_date:start_date, budget_end_date:end_date, budget_amount:amount, budget_description:description},
					success:function(data){
						NProgress.done();
						$('.generic-save').removeAttr("disabled")
						$('#generic-modal-form').find('.close-icon').click();
						// get_budget_ajax_call(month, year);
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
			
			var search_ajax_call=function()
		 	{
				var date = $('.date-picker').val();
				if(!validateDate(date))
				{
					showAjaxFailureMessageHome("Invalid Input");
					return;
				}
				date = dateToEpoch(date);
				NProgress.start();
				$.ajax({
					type:"GET",
					url:"/BudgetChummy/api/v1/search",
					data:{date:date},
					success:function(data){
						NProgress.done();
						$('.loader').removeClass('active');
						if(data!=null)
						{
							var obj;
							$.each(data, function(key, value) {
								if(key=="income_data")
								{
									for(var i=0;i<value.length;i++)
									{
										obj = jQuery.parseJSON(value[i]);
										globalObject.income_search_data.push(obj);
									}
								}
								else
								{
									for(var i=0;i<value.length;i++)
									{
										obj = jQuery.parseJSON(value[i]);
										globalObject.expense_search_data.push(obj);
									}									
								}
							});
							if(globalObject.income_search_data.length == 0)
							{
								$('#income_search_data').css('display','none');
								$('#empty-search-income-data').css('display','block');
							}
							else
							{
								$('#income_search_data').css('display','block');
								$('#empty-search-income-data').css('display','none');
								var income_search_data_string = "";
								for(var i=0;i<globalObject.income_search_data.length;i++)
								{
									income_search_data_string += '<div class="income-bag search-bag"><div class="search-tag"><div class="tag-label">Tag</div><div class="tag-value">' + globalObject.income_search_data[i].tag_name + '</div></div><div class="search-amount"><div class="amount-label">Amount</div><div class="amount-value">' + globalObject.income_search_data[i].amount + '</div></div></div>';			
								}
							}
							$('#income_search_data').html(income_search_data_string);
							if(globalObject.expense_search_data.length == 0)
							{
								$('#expense_search_data').css('display','none');
								$('#empty-search-expense-data').css('display','block');
							}
							else
							{
								$('#expense_search_data').css('display','block');
								$('#empty-search-expense-data').css('display','none');
								var expense_search_data_string = "";
								for(var i=0;i<globalObject.expense_search_data.length;i++)
								{
									expense_search_data_string += '<div class="expense-bag search-bag"><div class="search-tag"><div class="tag-label">Tag</div><div class="tag-value">' + globalObject.expense_search_data[i].tag_name + '</div></div><div class="search-amount"><div class="amount-label">Amount</div><div class="amount-value">' + globalObject.expense_search_data[i].amount + '</div></div></div>';			
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
						$('.loader').removeClass('active');
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
								$('.users-table tbody').append("<tr><td>"+obj.first_name+"</td><td>"+obj.email+"</td><td>"+obj.role+"</td><td><i class='icon-trash delete-user-icon'></i></td></tr>");
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
					success:function(data){
						if(data != null)
						{
							$('#saved-tags-dropdown').html("");
							for(var i=0;i<data.length;i++)
							{
								var obj = jQuery.parseJSON(data[i]);
								globalObject.tags_list.push(obj.tag_name);
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