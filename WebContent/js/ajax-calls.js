			var income_ajax_call=function(month,year)
		 	{
				$.ajax({
					type:"GET",
					url:"incomeServlet",
					data:{month:month,year:year},
					success:function(data){
						if(data!=null)
						{
							$('.income-table tbody').html("");
							$('.income-table tbody').append("<tr><th class='amount-col'>Amount</th><th class='date-col'>Date</th><th class='tag-col'>Tag</th><th class='description-col'>Description</th><th class='addedby-col'>Added by</th><th class='addedon-col'>Added on</th><th class='location-col'>Location</th></tr>");
							for(var i=0;i<data.length;i++)
							{
								var obj = jQuery.parseJSON(data[i]);
								income_data.push({y:obj.amount,indexLabel:obj.amount+"(#percent%)",legendText:""+obj.amount,description:obj.description,date:obj.date});
								$('.income-table tbody').append("<tr><td>"+obj.amount+"</td><td>"+obj.date+"</td><td>"+obj.tag_name+"</td><td>"+obj.description+"</td><td>"+obj.user_name+"</td><td>"+obj.added_date_time+"</td><td>"+obj.location+"  <img src='images/show_location_icon.png' class='image location-icon' alt='Show in map' data-lat="+obj.latitude+" data-lon="+obj.longitude+"></td></tr>");
							}
					    	income_chart.options.data[0].dataPoints = income_data;
					    	if(income_data.length != 0)
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
					    		$('#empty-income-data').html("No income for this month");
					    	}
					    	income_data=[];
						}
						else
						{
						}
					}

				});
		 	}
			
			var expense_ajax_call=function(month,year)
		 	{
				$.ajax({
					type:"GET",
					url:"expenseServlet",
					data:{month:month,year:year},
					success:function(data){
						if(data!=null)
						{
							$('.expense-table tbody').html("");
							$('.expense-table tbody').append("<tr><th class='amount-col'>Amount</th><th class='date-col'>Date</th><th class='tag-col'>Tag</th><th class='description-col'>Description</th><th class='addedby-col'>Added by</th><th class='addedon-col'>Added on</th><th class='location-col'>Location</th></tr>");
							for(var i=0;i<data.length;i++)
							{
								var obj = jQuery.parseJSON(data[i]);
								expense_data.push({y:obj.amount,indexLabel:obj.amount+"(#percent%)",legendText:""+obj.amount,description:obj.description,date:obj.date});
								$('.expense-table tbody').append("<tr><td>"+obj.amount+"</td><td>"+obj.date+"</td><td>"+obj.tag_name+"</td><td>"+obj.description+"</td><td>"+obj.user_name+"</td><td>"+obj.added_date_time+"</td><td>"+obj.location+"  <img src='images/show_location_icon.png' class='image location-icon' alt='Show in map' data-lat="+obj.latitude+" data-lon="+obj.longitude+"></td></tr>");
							}
					    	expense_chart.options.data[0].dataPoints = expense_data;
					    	if(expense_data.length != 0)
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
					    		$('#empty-expense-data').html("No expense for this month");
					    	}		
					    	expense_data=[];
						}
						else
						{
						}
					}

				});
		 	}

			var search_ajax_call=function()
		 	{
				var date = $('.date-picker').val();
				$.ajax({
					type:"GET",
					url:"searchServlet",
					data:{date:date},
					success:function(data){
						if(data!=null)
						{
							var obj;
							$.each(data, function(key, value) {
								if(key=="income_data")
								{
									for(var i=0;i<value.length;i++)
									{
										obj = jQuery.parseJSON(value[i]);
										income_search_data.push(obj);
									}
								}
								else
								{
									for(var i=0;i<value.length;i++)
									{
										obj = jQuery.parseJSON(value[i]);
										expense_search_data.push(obj);
									}									
								}
							});
							if(income_search_data.length == 0)
							{
								$('#income_search_data').html("No income in this date");
							}
							else
							{
								var income_search_data_string = "";
								for(var i=0;i<income_search_data.length;i++)
								{
									income_search_data_string += income_search_data[i].amount + income_search_data[i].description;			
								}
							}
							$('#income_search_data').html(income_search_data_string);
							if(expense_search_data.length == 0)
							{
								$('#expense_search_data').html("No expenses in this date");
							}
							else
							{
								var expense_search_data_string = "";
								for(var i=0;i<expense_search_data.length;i++)
								{
									expense_search_data_string += expense_search_data[i].amount + expense_search_data[i].description;			
								}
							}
							$('#expense_search_data').html(expense_search_data_string);
						}
						else
						{
						}
					}

				});
				income_search_data=[];
				expense_search_data=[];
		 	}
			
			var users_ajax_call = function(){
				$.ajax({
					type:"GET",
					url:"getUsersServlet",
					success:function(data){
						if(data != null)
						{
							var obj;
							obj = jQuery.parseJSON(data[0]);
							$('#users-account-name div').html(obj.account_name);
							$('#users-no-of-members div').html(obj.no_of_members);
							$('#users-created-on div').html(obj.created_date_time);
							$('#users-created-by div').html(obj.created_by);
							$('.users-table tbody').html("");
							$('.users-table tbody').append("<tr><th>User Name</th><th>Email</th><th>Role</th><th>Actions</th></tr>");														
							for(var i=1;i<data.length;i++)
							{
								obj = jQuery.parseJSON(data[i]);
								$('.users-table tbody').append("<tr><td>"+obj.first_name+"</td><td>"+obj.email+"</td><td>"+obj.role+"</td><td><img src='images/delete_user_icon.png' class='image delete-user-icon' alt='Delete User'></td></tr>");
							}
							$('.users-table-space').css("display","block");
						}
						else
						{
							$('.users-table-space').css("display","none");
						}
					}
				});				
			}
			
//			var add_user_ajax_call = function(){
//				$.ajax({
//					type:"POST",
//					url:"addUserServlet",
//					success:function(data){
//						if(data != null)
//						{
//							for(var i=0;i<data.length;i++)
//							{
//								var obj = jQuery.parseJSON(data[i]);
//								$("#accounts-list").append("<div class='accounts' onclick=javascript:document.getElementById('account_id').value="+obj.account_id+";document.forms[0].submit();>"+obj.account_name+"</div><br>");
//							}
//						}
//					}
//				});				
//			}
			var getAccounts_ajax_call = function()
			{
				$.ajax({
					type:"GET",
					url:"getAccountsServlet",
					success:function(data){
						if(data != null)
						{
							for(var i=0;i<data.length;i++)
							{
								var obj = jQuery.parseJSON(data[i]);
								$("#accounts-list").append("<div class='accounts' id="+obj.account_id+">"+obj.account_name+"</div><br>");
							}
						}
						else
						{
							$("#accounts-list").append("You dont have any accounts...");
						}
					}
				});
			}
			
			var get_tags_ajax_call = function(){
				$.ajax({
					type:"GET",
					url:"tagsServlet",
					success:function(data){
						if(data != null)
						{
							$('#saved-tags-dropdown').html("");
							for(var i=0;i<data.length;i++)
							{
								var obj = jQuery.parseJSON(data[i]);
								tags_list.push(obj.tag_name);
								$("#saved-tags-dropdown").append("<option value='"+obj.tag_id+"'>"+obj.tag_name+"</option>");
							}
						}
						else
						{
							
						}
					}
				});				
			}
			
			var save_tag_ajax_call = function(){
				var tag = $('#saved-tags-input').val();
				$.ajax({
					type:"POST",
					url:"tagsServlet",
					data:{tag_name:tag},
					async: false,
					success:function(data){
						if(data != null)
						{

						}
						else
						{
							
						}
					}
				});				
			}