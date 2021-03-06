<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%
	if(session.getAttribute("account_id") == null)
	{
		if(session.getAttribute("user_id") != null)
		{
			response.sendRedirect("/BudgetChummy/ChooseAccount");
			return;
		}
		else
		{
			response.sendRedirect("/");
			return;
		}
	}
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Home | BC</title>
	<link rel="stylesheet" href="styles/style.css" type="text/css">
	<script type="text/javascript" src="app/jquery-3.1.1.js"></script>
	<script type="text/javascript" src="app/jquery-ui.min.js"></script>

	<script type="text/javascript" src="app/bootstrap.min.js"></script>
	<script type="text/javascript" src="app/jquery.canvasjs.min.js"></script>
	<script type="text/javascript" src="app/jquery.slimscroll.min.js"></script>
	<script type="text/javascript" src="app/canvasjs.min.js"></script>
	<script type="text/javascript" src="app/nprogress.js"></script>
	<script type="text/javascript" src="app/moment.js"></script>
	<script type="text/javascript" src="app/moment-timezone-with-data.js"></script>

	<link rel="stylesheet" href="styles/bootstrap.min.css" type="text/css">
	<link rel="stylesheet" href="styles/font-awesome.css" type="text/css">
	<link rel="stylesheet" href="styles/jquery-ui.css" type="text/css">
	<link rel="stylesheet" href="styles/nprogress.css" type="text/css">
	<script src="https://apis.google.com/js/platform.js?onload=onGoogleLoad" async defer></script>
	<meta name="google-signin-client_id" content="1030027078466-lmvc5lqlkq1llqasuomhj2cnh6fv4at9.apps.googleusercontent.com">
	<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDtFLcyhDfgarOIcwf-4qiScchMGJS25jo"></script>
	<script type="text/javascript">
	function globalFunction(){
		this.month_array=["January","February","March","April","May","June","July","August","September","October","November","December"];
		this.months_shortform = ["Jan", "Feb", "Mar", "Apr", "May" ,"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		this.income_data = [];
		this.expense_data = [];
		this.income_search_data = [];
		this.expense_search_data = [];
		this.incomes_in_month = [];
		this.expenses_in_month = [];
		this.budgets_in_month = [];
		this.d = new Date(moment.tz(moment.tz.guess()).format());
		this.date = this.d.getDate();
		this.month = this.d.getMonth() + 1;
		this.year = this.d.getFullYear();
		this.current_page = "income";
		this.income_tags_list = [];	
		this.expense_tags_list = [];	
	}
	var globalObject = new globalFunction();
	
	var onGoogleLoad = function(){
        gapi.load('auth2', function() {
        	gapi.auth2.init();
        });
	}
	$(document).ready(function(){
		getAccounts_ajax_call("home");
/* 		open_page(event,'income');
		income_ajax_call(month,year);
		$('#income-tab').addClass("active"); */
		initial_page_load = function(page){
			if(page == "expense")
			{
				open_page('expense');
				get_expense_ajax_call(globalObject.month,globalObject.year,"expense");
				$('#expense-tab').addClass("active");
				$('#budgets-right-slider').hide();
				//$('#accounts-right-slider').show('slide', {direction: 'right'}, 300);
				$('#accounts-right-slider').show();
			}
			else if(page == "budget")
			{
				open_page('budget');
				//get_budget_ajax_call(month,year);
				get_expense_ajax_call(globalObject.month, globalObject.year, "budget");
				$('#budget-tab').addClass("active");
				$('#accounts-right-slider').hide();
				//$('#budgets-right-slider').show('slide', {direction: 'right'}, 300);
				$('#budgets-right-slider').show();
			}
			else if(page == "search")
			{
				open_page('search');
				$('#search-tab').addClass("active");
				$('#budgets-right-slider').hide();
				//$('#accounts-right-slider').show('slide', {direction: 'right'}, 300);
				$('#accounts-right-slider').show();
			}
			else if(page == "users")
			{
				open_page('users');
				users_ajax_call(globalObject.month,globalObject.year);
				$('#users-tab').addClass("active");
				$('#budgets-right-slider').hide();
				//$('#accounts-right-slider').show('slide', {direction: 'right'}, 300);
				$('#accounts-right-slider').show();
			}
			else
			{
				open_page('income');
				get_income_ajax_call(globalObject.month,globalObject.year);
				$('#income-tab').addClass("active");	
				$('#budgets-right-slider').hide();
				//$('#accounts-right-slider').show('slide', {direction: 'right'}, 300);
				$('#accounts-right-slider').show();
			}
		}
		initial_page_load("income");
		
		document.getElementById("selected_month").innerHTML = globalObject.month_array[globalObject.month-1]+", "+globalObject.year;
		  
		var initializeIncomeExpenseDatePicker = function(){
			$(document).on('focus',".generic-datepicker", function(){
			    $('.generic-datepicker').datepicker({dateFormat:"yy/mm/dd",changeMonth: true,changeYear: true,yearRange: '1970:9999'});
		  	});
		}
		var initializeBudgetDatePicker = function(){
			$(document).on('focus',".budget-datepicker", function(){
			    $('.budget-datepicker').datepicker({dateFormat:"yy/mm/dd",changeMonth: true,changeYear: true,yearRange: '1970:9999'});
		  	});
		}
		var destroyIncomeExpenseDatePicker = function(){
			$('.generic-datepicker').datepicker("destroy");
		}
		var destroyBudgetDatePicker = function(){
			$('.budget-datepicker').datepicker("destroy");
		}
		initializeBudgetDatePicker();
		  
		  $(document).on('click','.plus',function(){
			  $('.generic-modal').modal('show');
			  if(globalObject.current_page == "income")
			  {
				  open_income_modal(false);
			  }
			  else if(globalObject.current_page == "expense")
			  {
				  open_expense_modal(false);
			  }
			  else if(globalObject.current_page == "budget")
			  {
				  open_budget_modal(false);
			  }
		  });
		  $(document).on('click','.home-accounts',function(){
			  var id = $(this).attr("id");
			  accountChosen_ajax_call(id, globalObject.current_page);
		  });
		  
		  //$('.center').slimscroll();
	      $(function(){
 		      $('.center-content').slimscroll({
		    		width: '100%',
		  			height: '100%',
		  			opacity: '0.2'
		      }); 
		      $('.right-content').slimscroll({
		    		width: '100%',
		  			height: '100%',
		  			opacity: '0.2'
		      });
		  });
		  $('#generic-type-dropdown').on('click',function(){
			  var selected = $('#generic-type-dropdown').val();
			  if(selected == "income")
			  {
				  open_income_modal(false);
			  }
			  else if(selected == "expense")
			  {
				  open_expense_modal(false);
			  }
			  else
			  {
				  open_budget_modal(false);
			  }
		  });
		  
		  $(document).on('click','#income-map-search',function(){
			  codeAddress('income-location');
		  });
		  $(document).on('click','#expense-map-search',function(){
			  codeAddress('expense-location');
		  });
		  $(document).on('focusout','#income-location',function(){
			  codeAddress('income-location');
		  });
		  $(document).on('focusout','#expense-location',function(){
			  codeAddress('expense-location');
		  });
		  
		  $(document).on('click','.budget-container',function(e){
			  var budget_id = $(e.target).closest('.budget-container').attr('id');
			  var budget_number = budget_id[budget_id.length - 1];
			  getBudgetDataForStat(budget_number);
		  });
		  
		  $(document).on('click','#add-user',function(){
		      $('#addUserModal').modal('show');	
		  });

		  $(document).on('click','#email-radio',function(){
			  $('#set-offline-code').css("display","none");
		  });
		  $(document).on('click','#offline-radio',function(){
			  $('#set-offline-code').css("display","block");
		  });

		  $(document).on('click','.add-account-icon',function(){
			  $('#addAccountModal').modal('show');
		  });

		  $(document).on('click','.income-edit',function(){
		  		var tid = $(this).closest('tr').attr('data-tid');
		  		$('.generic-modal').modal('show');
			  	open_income_modal(true);
			  	populateIncomeModalToEdit(tid);
		  });
		  $(document).on('click','.expense-edit',function(){
		  		var tid = $(this).closest('tr').attr('data-tid');
		  		$('.generic-modal').modal('show');
			  	open_expense_modal(true);
			  	populateExpenseModalToEdit(tid);
		  });
		  $(document).on('click','.budget-edit',function(){
		  		var bid = $(this).closest('#budget-page-header').attr('data-bid');
		  		$('.generic-modal').modal('show');
			  	open_budget_modal(true);
			  	populateBudgetModalToEdit(bid);
		  });
		  
		 $(document).on('click','#budget-type-dropdown',function(){
			 var value = $('#budget-type-dropdown').val();
			 if(value == 1)
			 {
				 $(".budget-tags").html($(".saved-tags-div"));
				 $(".budget-tags .saved-tags-div").css("display","block");
				 $(".income-expense-tags .saved-tags-div").css("display","none");
			 }
			 else
			 {
				 $('.budget-tags .saved-tags-div').css("display","none");
			 }
		 });
		 $(document).on('click','#budget-repeat-dropdown',function(){
			 var value = $('#budget-repeat-dropdown').val();
			 if(value == 0)
			 {
			 	$(".one-time-budget-div .budget-end-datepicker").css("display","block");
			 }
			 else
			 {
				$(".one-time-budget-div .budget-end-datepicker").css("display","none");
			 }
		 });	 
		  $(document).on('click','.add-btn',function(){
			  $('.add-btn').toggleClass('open');
		  });
		  $(document).on('click','.add-icon',function(){
			  $('#saved-tags-input').css("display","inline-block").focus();
			  $('#saved-tags-dropdown').css("display","none");
		  });
		  $(document).on('focusout','#saved-tags-input',function(){
		  		saveTag();
			  $('#saved-tags-dropdown').css("display","inline-block");
			  $('#saved-tags-input').css("display","none");
			  $('#save-tag-hint').css("display","none");
		  });
		  $(document).on('keyup','#saved-tags-input',function(){
			  var text = $('#saved-tags-input').val();
			  if(text == "")
			  {
				  $('#save-tag-hint').css("display","none");
			  }
			  else
			  {
				  $('#save-tag-hint').css("display","block");
			  }
		  });
		  $(document).keypress(function(e) {
			    if(e.which == 13) {
			    	e.preventDefault();
			    	saveTag();
				    $('.saved-tags-div').css("display","block");
			        $('#saved-tags-input').css("display","none");
			    }
		  });
	      $(document).on('click','.location-icon',function(event){
		  		var lati = event.target.getAttribute("data-lat");
		    	var longi = event.target.getAttribute("data-lon");
		    	showLocationInMap(lati,longi);
		    	$('#showLocationModal').modal('show');
		    	resizeMap();
		    	recenter(lati,longi);
	      });
			
		    $(document).on('click','.left-icon',function(){
		    	globalObject.month=globalObject.month-1;
		    	if(globalObject.month == 0)
		    	{
		    		globalObject.month = 12;
		    		globalObject.year = globalObject.year-1;
		    	}
		    	document.getElementById("selected_month").innerHTML = globalObject.month_array[globalObject.month-1]+", "+globalObject.year;
		    	if($('#income-tab').hasClass('active'))
		    	{
		    		get_income_ajax_call(globalObject.month,globalObject.year);
		    	}
		    	else if($('#expense-tab').hasClass('active'))
		    	{
		    		get_expense_ajax_call(globalObject.month,globalObject.year,"expense");
		    	}
		    	else if($('#budget-tab').hasClass('active'))
		    	{
		    		//get_budget_ajax_call(month,year);
		    		get_expense_ajax_call(globalObject.month,globalObject.year,"budget");
		    	}
		    });
		    $(document).on('click','.right-icon',function(){
		    	globalObject.month=globalObject.month+1;
		    	if(globalObject.month == 13)
		    	{
		    		globalObject.month = 1;
		    		globalObject.year = globalObject.year+1;
		    	}
		    	document.getElementById("selected_month").innerHTML = globalObject.month_array[globalObject.month-1]+", "+globalObject.year;
		    	if($('#income-tab').hasClass('active'))
		    	{
		    		get_income_ajax_call(globalObject.month,globalObject.year);
		    	}
		    	else if($('#expense-tab').hasClass('active'))
		    	{
		    		get_expense_ajax_call(globalObject.month,globalObject.year,"expense");
		    	}
		    	else if($('#budget-tab').hasClass('active'))
		    	{
		    		//get_budget_ajax_call(month,year);
		    		get_expense_ajax_call(globalObject.month,globalObject.year,"budget");
		    	}
		    });
		    

		    
		    $('#income-tab').click(function(){
		    	initial_page_load("income");
		    });
		    $('#expense-tab').click(function(){
		    	initial_page_load("expense");
		    });	
		    $('#budget-tab').click(function(){
		    	initial_page_load("budget");
		    });
		    $('#search-tab').click(function(){
		    	initial_page_load("search");
		    });
		    $('.search-btn').click(function(){
		    	search_ajax_call();
		    });
		    $('#users-tab').click(function(){
		    	initial_page_load("users");
		    });
		    $('#add-user-send').on('click', function(){
		    	add_user_ajax_call();	
		    });
		    $('#add-account').on('click', function(){
		    	var account_name = $('#addaccountname').val();
		    	if(account_name != "" && validateAccountName(account_name))
	    		{
		    		create_account_ajax_call(account_name);	
	    		}
		    	else
	    		{
		    		showAjaxFailureMessageHome("Invalid Input");
	    		}
		    });
		    $('#users-invitations').on('click', function(){
		    	pending_invitations_ajax_call();
		    	$('#pendingInvitationsModal').modal('show');	
		    });
		    $('.users-table-space').on('click', '.delete-user-icon', function(){
		    	var delete_user_id = $(this).closest('tr').attr('user_id');
		    	$('#confirmDeleteUserModal').modal('show');
		    	$('#confirmDeleteUserModal').attr('for-user',delete_user_id);
		    });
		    $('#delete-user').on('click', function(){
		    	var delete_user_id = $('#confirmDeleteUserModal').attr('for-user');
		    	delete_user_ajax_call(delete_user_id);
		    });
		    $('#generic-modal-form').on('click', '#income-delete', function(){
		    	delete_income_ajax_call();
		    });
		    $('#generic-modal-form').on('click', '#expense-delete', function(){
		    	delete_expense_ajax_call();
		    });
		    $('#generic-modal-form').on('click', '#budget-delete', function(){
		    	delete_budget_ajax_call();
		    });
		    $('#logout').click(function(){
		    	logout_ajax_call(false);
		    });

			$('.date-picker').datepicker({
				dateFormat : "yy/mm/dd"
			});

			var incomeSlideUpInModal = function(){
				$('.income-additional-info-div').slideUp().removeClass('active');
				$('.income-show-more span').text('Store More');
			    document.getElementById("income-additional-info").value = false;
			}
			var incomeSlideDownInModal = function(){
				$('.income-additional-info-div').slideDown().addClass('active');
				$('.income-show-more span').text('Store Less');
			    document.getElementById("income-additional-info").value = true;
			    myMap('income-location');
			    resizeMap();
			}
			var expenseSlideUpInModal = function(){
				$('.expense-additional-info-div').slideUp().removeClass('active');
				$('.expense-show-more span').text('Store More');
			    document.getElementById("expense-additional-info").value = false;
			}
			var expenseSlideDownInModal = function(){
				$('.expense-additional-info-div').slideDown().addClass('active');
				$('.expense-show-more span').text('Store Less');
			    document.getElementById("expense-additional-info").value = true;
			    myMap('expense-location');
			    resizeMap();
			}
			$(document).on('click','.income-show-more',function(e){
				  $('.income-show-more span').css("pointer-events","none");
				  if ($('.income-additional-info-div').hasClass('active')) 
				  {
				  	incomeSlideUpInModal();
				  } 
				  else 
				  {
				  	incomeSlideDownInModal();
				  }
				    setTimeout(function(){
				    	$('.income-show-more span').css("pointer-events","auto");
				    },300);
			});
			$(document).on('click','.expense-show-more',function(e){
				  $('.expense-show-more span').css("pointer-events","none");
				  if ($('.expense-additional-info-div').hasClass('active')) 
				  {
				    expenseSlideUpInModal();
				  } 
				  else 
				  {
				    expenseSlideDownInModal();
				  }
				    setTimeout(function(){
				    	$('.expense-show-more span').css("pointer-events","auto");
				    },300);
			});
			
			var findTagIdWithTagName = function(tag_name, type){
				var tag_id = -1;
				if(type == "income")
				{
					var tags = globalObject.income_tags_list;
				}
				else if(type == "expense" || type == "budget")
				{
					var tags = globalObject.expense_tags_list;
				}
				for(var j=0; j<tags.length; j++)
				{
					if(tags[j].tag_name == tag_name)
					{
						tag_id = tags[j].tag_id;
						break;
					}
				}
				return tag_id;
			}
			var is_in_tags_list = function(text, modal){
				text = text.trim();
				if(modal == "income")
				{
					var tags_list = globalObject.income_tags_list;
				}
				else if(modal == "expense" || modal == "budget")
				{
					var tags_list = globalObject.expense_tags_list;
				}
				for(var i=0; i<tags_list.length ;i++)
				{
					if(tags_list[i].tag_name.toLowerCase() == text.toLowerCase())
					{
						return true;
					}
				}
				return false;
			}
			var saveTag = function(){
				var text = $('#saved-tags-input').val().trim();
		    	if($('#generic-modal-form').find('#addIncomeModal').length)
		    	{
		    		var modal = "income";
		    	}
		    	else if($('#generic-modal-form').find('#addExpenseModal').length)
		    	{
		    		var modal = "expense";
		    	}
		    	else
		    	{
		    		var modal = "budget";
		    	}
		        if($('#saved-tags-input').length > 0 && text != "" && !is_in_tags_list(text, modal))
		        {
		        	if(modal == "income")
	        		{
		        		save_tag_ajax_call("income");
			        	get_tags_ajax_call("income");
	        		}
		        	else if(modal == "expense" || modal == "budget")
		        	{
		        		save_tag_ajax_call("expense");
			        	get_tags_ajax_call("expense");
		        	}
		        }
		        var tag_id;
		        if(modal == "income")
		        {
		        	tag_id = findTagIdWithTagName(text, "income");
		        }
		        else if(modal == "expense" || modal == "budget")
		        {
		        	tag_id = findTagIdWithTagName(text, "expense");
		        }
		        $("#saved-tags-dropdown").val(tag_id);  
			}
			var open_income_modal = function(isEdit){
				  $(".budget-modal").css("display","none");
				  $(".income-expense-modal").css("display","block");
				  $(".income-expense-tags").html($(".saved-tags-div"));
				  $(".income-expense-tags .saved-tags-div").css("display","block");
				  $(".budget-tags .saved-tags-div").css("display","none");
				  $('.generic-modal').addClass("add-income-modal");
				  $('.generic-modal').attr("id","addIncomeModal");
				  get_tags_ajax_call("income");
				  if(isEdit)
				  {
			  		$('#generic-type-dropdown').css('display', 'none');
			  		$('#generic-type-heading').css('display', 'block');
			  		$('#generic-type-heading').html("EDIT INCOME");
				  }
				  else
				  {
				  	$('#generic-type-dropdown').css('display', 'block');
			  		$('#generic-type-heading').css('display', 'none');
				  	$('#generic-type-dropdown').val("income");
				  }
				  $('.generic-amount').attr("name","income-amount");
				  $('.generic-amount').attr("id","income-amount");
				  $('.generic-datepicker').attr("name","income-date");
				  $('.generic-datepicker').attr("id","income-datepicker");
				  destroyIncomeExpenseDatePicker();
				  initializeIncomeExpenseDatePicker();
				  $('.generic-show-more').removeClass("expense-show-more");
				  $('.generic-show-more').addClass("income-show-more");
				  $('.generic-additional-info-div').removeClass("expense-additional-info-div");
				  $('.generic-additional-info-div').addClass("income-additional-info-div");
				  $('.generic-additional-info').attr("name","income-additional-info");
				  $('.generic-additional-info').attr("id","income-additional-info");
				  $('.generic-location').attr("name","income-location");
				  $('.generic-location').attr("id","income-location");
				  $('.generic-location-lat').attr("name","income-location-lat");
				  $('.generic-location-lat').attr("id","income-location-lat");
				  $('.generic-location-lon').attr("name","income-location-lon");
				  $('.generic-location-lon').attr("id","income-location-lon");
				  $('.generic-map-search').attr("id","income-map-search");
				  $('.generic-location-map').attr("id","income-location-map");
				  $('.generic-description').attr("name","income-description");
				  $('.generic-description').attr("id","income-description");
				  $('.generic-repeat-dropdown').attr("name","income-repeat");
				  $('.generic-repeat-dropdown').attr("id","income-repeat");
				  $('.generic-reminder-dropdown').attr("name","income-reminder");
				  $('.generic-reminder-dropdown').attr("id","income-reminder");
				  if(isEdit)
				  {
				  	$('.generic-save').attr("id","income-edit");
				  	$('.generic-delete').attr("id","income-delete");
				  	$('.generic-delete').show();
				  	incomeSlideDownInModal();
				  }
				  else
				  {
				  	$('.generic-save').attr("id","income-save");
				  	$('.generic-delete').hide();
				  }
			};
			var open_expense_modal = function(isEdit){
				  $(".budget-modal").css("display","none");
				  $(".income-expense-modal").css("display","block");
				  $(".income-expense-tags").html($(".saved-tags-div"));
				  $(".income-expense-tags .saved-tags-div").css("display","block");
				  $(".budget-tags .saved-tags-div").css("display","none");
				  $('.generic-modal').addClass("add-expense-modal");
				  $('.generic-modal').attr("id","addExpenseModal");
				  get_tags_ajax_call("expense");
				  if(isEdit)
				  {
			  		$('#generic-type-dropdown').css('display', 'none');
			  		$('#generic-type-heading').css('display', 'block');
			  		$('#generic-type-heading').html("EDIT EXPENSE");
				  }
				  else
				  {
				  	$('#generic-type-dropdown').css('display', 'block');
			  		$('#generic-type-heading').css('display', 'none');
				  	$('#generic-type-dropdown').val("expense");
				  }
				  $('.generic-amount').attr("name","expense-amount");
				  $('.generic-amount').attr("id","expense-amount");
				  $('.generic-datepicker').attr("name","expense-date");
				  $('.generic-datepicker').attr("id","expense-datepicker");
				  destroyIncomeExpenseDatePicker();
				  initializeIncomeExpenseDatePicker();
				  $('.generic-show-more').removeClass("income-show-more");
				  $('.generic-show-more').addClass("expense-show-more");
				  $('.generic-additional-info-div').removeClass("income-additional-info-div");
				  $('.generic-additional-info-div').addClass("expense-additional-info-div");
				  $('.generic-additional-info').attr("name","expense-additional-info");
				  $('.generic-additional-info').attr("id","expense-additional-info");
				  $('.generic-location').attr("name","expense-location");
				  $('.generic-location').attr("id","expense-location");
				  $('.generic-location-lat').attr("name","expense-location-lat");
				  $('.generic-location-lat').attr("id","expense-location-lat");
				  $('.generic-location-lon').attr("name","expense-location-lon");
				  $('.generic-location-lon').attr("id","expense-location-lon");
				  $('.generic-map-search').attr("id","expense-map-search");
				  $('.generic-location-map').attr("id","expense-location-map");
				  $('.generic-description').attr("name","expense-description");
				  $('.generic-description').attr("id","expense-description");
				  $('.generic-repeat-dropdown').attr("name","expense-repeat");
				  $('.generic-repeat-dropdown').attr("id","expense-repeat-dropdown");
				  $('.generic-reminder-dropdown').attr("name","expense-reminder");
				  $('.generic-reminder-dropdown').attr("id","expense-reminder-dropdown");
				  if(isEdit)
				  {
				  	$('.generic-save').attr("id","expense-edit");
				  	$('.generic-delete').attr("id","expense-delete");
				  	$('.generic-delete').show();
				  	expenseSlideDownInModal();
				  }
				  else
				  {
				  	$('.generic-save').attr("id","expense-save");
				  	$('.generic-delete').hide();
				  }
			}
			var open_budget_modal = function(isEdit){
				  $(".budget-modal").css("display","block");
				  get_tags_ajax_call("expense");
				  if(isEdit)
				  {
			  		$('#generic-type-dropdown').css('display', 'none');
			  		$('#generic-type-heading').css('display', 'block');
			  		$('#generic-type-heading').html("EDIT BUDGET");
				  }
				  else
				  {
				  	$('#generic-type-dropdown').css('display', 'block');
			  		$('#generic-type-heading').css('display', 'none');
				  	$('#generic-type-dropdown').val("budget");
				  }
				  $(".income-expense-modal").css("display","none");
				  if(isEdit)
				  {
				  	$('.generic-save').attr("id","budget-edit");
				  	$('.generic-delete').attr("id","budget-delete");
				  	$('.generic-delete').show();
				  }
				  else
				  {
				  	$('.generic-save').attr("id","budget-save");
				  	$('.generic-delete').hide();
				  }
				  //$('#generic-modal-form').attr("action","budget");
				  //$('#generic-modal-form').attr("method","POST");
			}
			var populateIncomeModalToEdit = function(tid){
				var incomes = globalObject.incomes_in_month;
				var income_tags = globalObject.income_tags_list;
				for(var i=0; i<incomes.length; i++)
				{
					if(incomes[i].transaction_id == tid)
					{
						var amount = incomes[i].amount;
						$('#income-amount').val(amount);
						var date_arr = getDateFromEpoch(incomes[i].date);
						var date = date_arr[0] + "/" + date_arr[1] + "/" + date_arr[2];
						$('#income-datepicker').val(date);
						var tag_id = findTagIdWithTagName(incomes[i].tag_name, "income");
						$('#saved-tags-dropdown').val(tag_id);
						var location = incomes[i].location;
						if(location != null && location != "")
						{
							$('#income-location').val(location);
						}
						var lat = incomes[i].latitude;
						var lon = incomes[i].longitude;
						if(lat && lon && lat != 0 && lon != 0)
						{
							showLocationInMap(lat,lon);
						}
						var description = incomes[i].description;
						if(description != null && description != "")
						{
							$('#income-description').val(description);
						}
						var repeat = incomes[i].repeat_period;
						if(repeat && repeat != -1)
						{
							$('#income-repeat').val(repeat);
						}
						var reminder = incomes[i].reminder_period;
						if(reminder && reminder != -1)
						{
							$('#income-reminder').val(reminder);
						}
						break;
					}
				}
				$('#edit-tid').val(tid);
			}
			var populateExpenseModalToEdit = function(tid){
				var expenses = globalObject.expenses_in_month;
				var expense_tags = globalObject.expense_tags_list;
				for(var i=0; i<expenses.length; i++)
				{
					if(expenses[i].transaction_id == tid)
					{
						var amount = expenses[i].amount;
						$('#expense-amount').val(amount);
						var date_arr = getDateFromEpoch(expenses[i].date);
						var date = date_arr[0] + "/" + date_arr[1] + "/" + date_arr[2];
						$('#expense-datepicker').val(date);
						var tag_id = findTagIdWithTagName(expenses[i].tag_name, "expense");
						$('#saved-tags-dropdown').val(tag_id);
						var location = expenses[i].location;
						if(location != null && location != "")
						{
							$('#expense-location').val(location);
						}
						var lat = expenses[i].latitude;
						var lon = expenses[i].longitude;
						if(lat && lon && lat != 0 && lon != 0)
						{
							showLocationInMap(lat,lon);
						}
						var description = expenses[i].description;
						if(description != null && description != "")
						{
							$('#expense-description').val(description);
						}
						var repeat = expenses[i].repeat_period;
						if(repeat && repeat != -1)
						{
							$('#expense-repeat-dropdown').val(repeat);
						}
						var reminder = expenses[i].reminder_period;
						if(reminder && reminder != -1)
						{
							$('#expense-reminder-dropdown').val(reminder);
						}
						break;
					}
				}
				$('#edit-tid').val(tid);
			}
			var populateBudgetModalToEdit = function(bid){
				var budgets = globalObject.budgets_in_month;
				var expense_tags = globalObject.expense_tags_list;
				for(var i=0; i<budgets.length; i++)
				{
					if(budgets[i].budget_id == bid)
					{
						var budget_type = budgets[i].budget_type;
						$('#budget-type-dropdown').val(budget_type);
						if(budget_type == 1)
						{
							$(".budget-tags").html($(".saved-tags-div"));
					 		$(".budget-tags .saved-tags-div").css("display","block");
						 	$(".income-expense-tags .saved-tags-div").css("display","none");
						 	var tag_id = findTagIdWithTagName(budgets[i].tag_name, "expense");
							$('#saved-tags-dropdown').val(tag_id);
						}
						var repeat = budgets[i].repeat_period;
						$('#budget-repeat-dropdown').val(repeat);
						var date_arr = getDateFromEpoch(budgets[i].start_date);
						var date = date_arr[0] + "/" + date_arr[1] + "/" + date_arr[2];
						$('#budget-start-datepicker').val(date);
						if(repeat == 0)
						{
							$(".one-time-budget-div .budget-end-datepicker").css("display","block");
							date_arr = getDateFromEpoch(budgets[i].end_date);
							date = date_arr[0] + "/" + date_arr[1] + "/" + date_arr[2];
							$('#budget-end-datepicker').val(date);
						}
						var amount = budgets[i].amount;
						$('#budget-amount').val(amount);
						var description = budgets[i].description;
						$('#budget-description').val(description);
						break;
					}
				}
				$('#edit-bid').val(bid);
			}
	});


	var open_page = function(page){
		globalObject.current_page = page;
		if(page == "search" || page == "users")
		{
			$('.month-changer').css("display","none");
			$('.chart-space').css("display","none");
			$('.plus').css("display","none");
		}
		else
		{
			$('.month-changer').css("display","flex");
		}
		if(page == "income")
		{
			$('.plus').addClass("income");
			$('.plus').removeClass("expense");
			$('.plus').removeClass("budget");
			$('.plus').css("display","block");
			$('.income-chart-space').css("display","block");
		}
		else if(page == "expense")
		{
			$('.plus').addClass("expense");
			$('.plus').removeClass("income");
			$('.plus').removeClass("budget");
			$('.plus').css("display","block");
			$('.expense-chart-space').css("display","block");
		}
		else if(page == "budget")
		{
			$('.plus').addClass("budget");
			$('.plus').removeClass("expense");
			$('.plus').removeClass("income");
			$('.plus').css("display","block");
		}
		var i,tabcontent,tablinks;
		tabcontent = document.getElementsByClassName("tabcontent");
	    for (i = 0; i < tabcontent.length; i++) {
	        tabcontent[i].style.display = "none";
	    }
	    tablinks = document.getElementsByClassName("tablinks");
	    for (i = 0; i < tablinks.length; i++) {
	        tablinks[i].className = tablinks[i].className.replace(" active", "");
	    }
	    document.getElementById(page+'-page').style.display = "block";
	    $('#'+page+'-page').addClass("active");

	};
	
	var modal_ajax_call = function(){
		if($('.generic-save').attr('id') == "income-save")
		{
			save_income_ajax_call(globalObject.month, globalObject.year);
		}
		else if($('.generic-save').attr('id') == "income-edit")
		{
			edit_income_ajax_call(globalObject.month, globalObject.year);
		}
		else if($('.generic-save').attr('id') == "expense-save")
		{
			save_expense_ajax_call(globalObject.month, globalObject.year);
		}
		else if($('.generic-save').attr('id') == "expense-edit")
		{
			edit_expense_ajax_call(globalObject.month, globalObject.year);
		}
		else if($('.generic-save').attr('id') == "budget-save")
		{
			save_budget_ajax_call(globalObject.month, globalObject.year);
			get_budget_ajax_call(globalObject.month, globalObject.year);
		}
		else if($('.generic-save').attr('id') == "budget-edit")
		{
			edit_budget_ajax_call(globalObject.month, globalObject.year);
			get_budget_ajax_call(globalObject.month, globalObject.year);
		}
	}
	
	var setCurrentPage = function(){
		$('.page_name').val(globalObject.current_page);
		modal_ajax_call();
	}

	
	</script>
</head>
<body>

<div class="home-body">

	<div class="left">
		<div class="tab">
		  <button class="tablinks" id="income-tab"><div class="tab-title">INCOMES</div></button>
		  <button class="tablinks" id="expense-tab"><div class="tab-title">EXPENSES</div></button>
		  <button class="tablinks" id="budget-tab"><div class="tab-title">BUDGETS</div></button>
		  <button class="tablinks" id="search-tab"><div class="tab-title">SEARCH</div></button>
		  <button class="tablinks" id="users-tab"><div class="tab-title">USERS</div></button>	 
		</div>
		<button class="plus"></button>
	</div>

<div class="center">
 	<div class="center-content">
	 	<div class="month-changer" style="display:none;">
	 		<img src="images/left_icon.png" class="icon timespan-nav-icon left-icon" alt="Prev month">
	 		<div id="selected_month"></div>
	 		<img src="images/right_icon.png" class="icon timespan-nav-icon right-icon" alt="Next month">
	 	</div>
	 	
		<div id="income-page" style="display:none;" class="tabcontent page">
			<div class="income-chart-space chart-space">
			    <div id="chartContainer1"></div>
			    <div id="empty-income-data" class="empty-data" style="display:none;"></div>
		    </div>
		    <div class="income-table-space table-space">
	   	    	<table class="income-table table"><tbody></tbody></table>
	   	    </div>
		</div>
		<div id="expense-page" style="display:none;" class="tabcontent page">
			<div class="expense-chart-space chart-space">
			    <div id="chartContainer2" style="display:none;"></div>
			    <div id="empty-expense-data" class="empty-data" style="display:none;"></div>
		   	</div>
		    <div class="expense-table-space table-space">
	   	    	<table class="expense-table table"><tbody></tbody></table>
	   	    </div>
		</div>
		<div id="budget-page" style="display:none;" class="tabcontent page">
			<div id="budget-page-header" class="budget-page-header-content" style="display:none;">
				<div id="budget-page-tag" class="budget-page-header-content"></div>
				<div id="budget-page-repeat" class="budget-page-header-content"></div>
				<div id="budget-page-amount" class="budget-page-header-content"></div>
				<i class="icon-edit budget-edit icon-clickable"></i>
			</div>
			<div id="budget-page-stat" class="budget-page-stat" style="display:none;">
				<div id="budget-page-spent" class="budget-page-stat-content"></div>
				<div id="budget-page-left" class="budget-page-stat-content"></div>
				<div id="budget-page-hint" class="budget-page-stat-content"></div>
			</div>
			<div id="budget-timespan" style="display:none;">
				<span id="budget-timespan-text">Budget time span</span>
				<span id="budget-timespan-period"></span>
			</div>
			<div id="budget-chart-space" class="budget-chart-space chart-space" style="display:none;">
		   	</div>
		   	<div id="empty-budget-data" class="empty-data" style="display:none;">
		   		<img src='images/nodata2.svg'>
		   		<div class='empty-data-text'>No budgets for this month</div>
		   	</div>
		</div>
		
		<div id="search-page" style="display:none;" class="tabcontent page">
			<div class="search-container">
				<div class="search-label-from">From</div>
				<div class="search-input"><input type="text" id="search-from" class="date-picker"></div>
				<div class="search-label-to">To</div>
				<div class="search-input"><input type="text" id="search-to" class="date-picker"></div>
				<input type="button" id="income-expense-search-btn" class="search-btn" value="Search"><br>
			</div>
			<div class="search-results-container">
				<div class="income-results">
					<div id="income-results-header" class="search-results-header">Incomes</div>
					<div id="income_search_data"></div>
					<div id="empty-search-income-data" class="empty-search-data" style="display:none;">
						No incomes in this date
					</div>
				</div>
				<div class="expense-results">
					<div id="expense-results-header" class="search-results-header">Expenses</div>
					<div id="expense_search_data"></div>
					<div id="empty-search-expense-data" class="empty-search-data" style="display:none;">
						No expenses in this date
					</div>
				</div>				
			</div>
		</div>
		<div id="users-page" style="display:none;" class="tabcontent page">
			<div class="account-info-space">
				<div id="users-account-name" class="users-account-name users-account-details">
					<div class="label">Account Name</div><span class="value"></span>
				</div>
				<div id="users-no-of-members" class="users-no-of-members users-account-details">
					<div class="label">No of members</div><span class="value"></span>
				</div>
				<div id="users-created-on" class="users-created-on users-account-details">
					<div class="label">Created on</div><span class="value"></span>
				</div>
				<div id="users-created-by" class="users-created-by users-account-details">
					<div class="label">Created by</div><span class="value"></span>
				</div>
			</div>
			<div class="users-btns">
				<button id="add-user" class="add-user">Add User</button>
				<button id="users-invitations" class="users-invitations">Pending Invitations</button>
			</div>
		    <div class="users-table-space table-space">
	   	    	<table class="users-table table"><tbody></tbody></table>
	   	    	
	   	    </div>
		</div>
		
	</div>
</div>

<div class="right">
	<div class="right-content">
		<div id="accounts-right-slider" class="right-slider">
			<div id="accounts-heading"><span class="heading">ACCOUNTS</span><span class="icon-plus-sign add-account-icon"></span></div>
			<div id="home-accounts-list"></div>
		</div>
		
		<div id="budgets-right-slider" class="right-slider" style="display:none;">
			<div id="budgets-heading"><span>BUDGETS</span></div>
			<div id="home-budgets-list"></div>
		</div>
	</div>
	<button id="logout" class="logout" value="Logout">Logout</button>
</div>
 
  <div class="modal add-account-modal" id="addAccountModal" role="dialog" style="display:none;">
    <div class="modal-dialog">

      <div class="modal-content">
        <div class="modal-header">
        	<img src="images/close-icon.png" class="icon close-icon" alt="Close" data-dismiss="modal">
			<h4 class="modal-title">Add Account</h4>
        </div>
        <div class="modal-body">
        	<div class="account-name-label">Account name</div>
			<input type="text" name="account_name" id="addaccountname" class="textbox formvalidation name">
        </div>
        <div class="modal-footer">
        	<button id="add-account" class="add-account btn" value="Add">Add</button>
        </div>
      </div>
      
    </div>
  </div>

  <div class="modal show-location-modal" id="showLocationModal" role="dialog" style="display:none;">
    <div class="modal-dialog">

      <div class="modal-content">
        <div class="modal-header">
        	<img src="images/close-icon.png" class="icon close-icon" alt="Close" data-dismiss="modal">
			<h4 class="modal-title">Location</h4>
        </div>
        <div class="modal-body">
			<div id="location-in-map" class="location-in-map">
			</div>
        </div>
        <div class="modal-footer">
        </div>
      </div>
      
    </div>
  </div>

<div class="modal add-user-modal" id="addUserModal" role="dialog" style="display:none;">
	<div class="modal-dialog">

	  <div class="modal-content">
	    <div class="modal-header">
	    	<img src="images/close-icon.png" class="icon close-icon" alt="Close" data-dismiss="modal">
			<h4 class="modal-title">Add User</h4>
	    </div>
	    <div class="modal-body">
	    	<div class="add-user-email-text">Enter the email address of the user to send invitation</div>
	    	<div>
				<input type="text" id="add-user-input" name="add-user-input" class="text add-user-input"/>
			</div>
			<div class="add-user-passcode-text">Set authentication</div>
			<div class="radio-div">
			<span class="email-radio-div">
				<input type="radio" id="email-radio" name="authentication_type" value="Email" checked> Email
			</span>
			<span class="offline-radio-div">
	 			<input type="radio" id="offline-radio" name="authentication_type" value="Offline"> Offline
 			</span>
			</div>
			<div id="set-offline-code" style="display:none;">
				<input type="text" id="offline-code" name="offline-code" maxlength="6" class="text offline-code-input"/>
			</div>
			<div class="invitation-not-sent">
			</div>
	    </div>
	    <div class="modal-footer">
	    	<button id="add-user-send" class="add-user-send btn" value="Send invitation">Send invitation</button>
	    </div>
	  </div>
	  
	</div>
</div>

<div class="modal pending_invitations-modal" id="pendingInvitationsModal" role="dialog" style="display:none;">
	<div class="modal-dialog">

	  <div class="modal-content">
	    <div class="modal-header">
	    	<img src="images/close-icon.png" class="icon close-icon" alt="Close" data-dismiss="modal">
			<h4 class="modal-title">Pending Invitations</h4>
	    </div>
	    <div class="modal-body">
	    	<div class="invitations-table-header">
	    		<span class="invitation-email">Email</span>
	    		<span class="invitation-passcode">Passcode</span>
	    	</div>
	    	<div class="no-pending-invitations" style="display:none;">No pending invitations</div>
	    	<div class="invitations-list" style="display:none;">
	    		
	    	</div>
	    </div>
	  </div>
	</div>
</div>

<div class="modal confirm-delete-user-modal" id="confirmDeleteUserModal" role="dialog" style="display:none;">
	<div class="modal-dialog">

	  <div class="modal-content">
	    <div class="modal-header">
	    	<img src="images/close-icon.png" class="icon close-icon" alt="Close" data-dismiss="modal">
			<h4 class="modal-title">Confirm Delete</h4>
	    </div>
	    <div class="modal-body">
	    	<div class="confirm-delete-user-text">
	    		Are you sure you want to delete user from this account?
	    	</div>
	    </div>
    	<div class="modal-footer">
	    	<button id="delete-user" class="delete-user btn" value="Yes">Yes</button>
	    </div>
	  </div>
	</div>
</div>

<div id="generic-modal-form">
  <div class="modal generic-modal" id="genericModal" role="dialog" style="display:none;">
    <div class="modal-dialog">

      <div class="modal-content">
        <div class="modal-header">
        	<img src="images/close-icon.png" class="icon close-icon" alt="Close" data-dismiss="modal">
			<h4 class="modal-title">
				<select id="generic-type-dropdown" class="dropdown">
				  <option value="income">ADD INCOME</option>
			 	  <option value="expense">ADD EXPENSE</option>
				  <option value="budget">ADD BUDGET</option>
				</select>
				<div id="generic-type-heading" style="display: none;"></div>
			</h4>
        </div>
        <div class="modal-body">
        	<div class="income-expense-modal" style="display:none;">
		        <div class="textbox_space">
		        	<div class="modal-icons"><img src="images/amount2.svg"></div><input type="text" placeholder="Amount" id="generic-amount" class="generic-amount textbox">
		        </div>
		        <div class="textbox_space">
		        	<div class="modal-icons"><img src="images/date1.svg"></div><input type="text" placeholder="Date" id="generic-datepicker" class="generic-datepicker textbox">
		        </div>
				<div class="income-expense-tags"></div>
		        
		        <div class="generic-additional-info-div">
		        	<input type="hidden" class="generic-additional-info" id="generic-additional-info">
		        	<div class="textbox_space">
		            	<div class="modal-icons"><img src="images/location2.svg"></div><input type="text" placeholder="Location" class="generic-location" id="generic-location" class="textbox">
		        		<img src="images/search1.svg" class="generic-map-search icon search-icon" id="generic-map-search" alt="Search">
		        	</div>
		        	<input type="hidden" class="generic-location-lat" id="generic-location-lat">
		        	<input type="hidden" class="generic-location-lon" id="generic-location-lon">
		        	
		            <div class="generic-location-map" id="generic-location-map"></div>
	     	        <div class="textbox_space">
	     	        	<div class="modal-icons"><img src="images/description1.svg"></div><input type="text" placeholder="Description" class="generic-description" id="generic-description" class="textbox">
	     	        </div>
		        	<div class="textbox_space">
			        	<div class="modal-icons"><img src="images/repeat1.svg"></div><select class="generic-repeat-dropdown dropdown" id="generic-repeat-dropdown">
			        	    <option value="0">Never</option>
						    <option value="1">Daily</option>
					 	    <option value="2">Weekly</option>
						    <option value="3">Monthly</option>
						    <option value="4">Yearly</option>
						</select>
					</div>
					<div class="textbox_space">
						<div class="modal-icons"><img src="images/reminder3.svg"></div><select class="generic-reminder-dropdown dropdown" id="generic-reminder-dropdown">
							<option value="0">Never</option>
			        	    <option value="1">One day before</option>
						    <option value="2">On the day</option>
						</select>
					</div>
		        </div>
		        <div class="generic-show-more"><span>Store More</span></div>
		        <input type="hidden" class="edit-tid" id="edit-tid">
	        </div>
	        
			<div class="saved-tags-div" style="display:none;">
				<div class="textbox_space">
					<div class="modal-icons"><img src="images/tag1.svg"></div>
					<select id="saved-tags-dropdown"  class="dropdown saved-tags-dropdown" name="tag_id"></select>
					<input type="text" id="saved-tags-input" style="display:none;">
					<img src="images/add1.svg" class="icon add-icon" alt="Add tag">
					<div id="save-tag-hint" class="hint-text" style="display:none;">Press Enter to save</div>
				</div>

			</div>
				
				
	        <div class="budget-modal" style="display:none;">
	       		<div class="textbox_space">
		       		<div class="modal-icons"><img src="images/budgettype2.svg"></div><select id="budget-type-dropdown" class="dropdown" name="budget-type">
			        	    <option value="0">All expenses</option>
						    <option value="1">Tag</option>
					</select>
				</div>
				<div class="budget-tags"></div>
				<div class="textbox_space">
					<div class="modal-icons"><img src="images/repeat1.svg"></div><select id="budget-repeat-dropdown" class="dropdown" name="budget-repeat">
			        	    <option value="0">One time budget</option>
						    <option value="1">Daily</option>
					 	    <option value="2">Weekly</option>
						    <option value="3" selected>Monthly</option>
						    <option value="4">Yearly</option>
					</select>
				</div>
				<div class="one-time-budget-div">
					<div class="textbox_space budget-start-datepicker">
						<div class="modal-icons"><img src="images/startdate1.svg"></div><input type="text" placeholder="Start Date" id="budget-start-datepicker" class="budget-datepicker textbox" name="budget-start-date">
					</div>
					<div class="textbox_space budget-end-datepicker" style="display:none;">
						<div class="modal-icons"><img src="images/enddate1.svg"></div><input type="text" placeholder="End Date" id="budget-end-datepicker" class="budget-datepicker textbox" name="budget-end-date">
					</div>
				</div>
				<div class="textbox_space">
					<div class="modal-icons"><img src="images/amount2.svg"></div><input type="text" placeholder="Amount" id="budget-amount" name="budget-amount" class="textbox">
				</div>
				<div class="textbox_space">
					<div class="modal-icons"><img src="images/description1.svg"></div><input type="text" placeholder="Description" id="budget-description" name="budget-description" class="textbox">
				</div>
				<input type="hidden" class="edit-bid" id="edit-bid">
	        </div>
	        <input type="hidden" class="page_name" name="page_name"/>
	        <button id="generic-delete" class="generic-delete transaction-delete" value="Delete">Delete</button>
        </div>
        <div class="modal-footer">
          <button class="generic-save transaction-save" id="generic-save" class="btn" value="Save" onclick="setCurrentPage();">Save</button>
        </div>
      </div>
      
    </div>
  </div>
</div>

</div>
<div id="home-ajax-success-box" style="display:none;">
	<div class="content"></div>
</div>
<div id="home-ajax-failure-box" style="display:none;">
	<div class="content"></div>
</div>

<script type="text/javascript" src="js/security_regex.js"></script>
<script type="text/javascript" src="js/map-api.js"></script>
<script type="text/javascript" src="js/charts.js"></script>
<script type="text/javascript" src="js/ajax-calls.js"></script>
<script type="text/javascript" src="js/Datehelper.js"></script>
<script type="text/javascript" src="js/budgets.js"></script>
<script type="text/javascript" src="js/incomes.js"></script>
<script type="text/javascript" src="js/expenses.js"></script>
</body>

</html>