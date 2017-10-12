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
			response.sendRedirect("/BudgetChummy/");
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
	<script type="text/javascript" src="app/jstz.min.js"></script>

	<link rel="stylesheet" href="styles/bootstrap.min.css" type="text/css">
	<link rel="stylesheet" href="styles/font-awesome.css" type="text/css">
	<link rel="stylesheet" href="styles/jquery-ui.css" type="text/css">
	<link rel="stylesheet" href="styles/nprogress.css" type="text/css">
	<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDtFLcyhDfgarOIcwf-4qiScchMGJS25jo"></script>
	<script type="text/javascript">
	function globalFunction(){
		this.month_array=["January","February","March","April","May","June","July","August","September","October","November","December"];
		this.months_shortform = ["Jan", "Feb", "Mar", "Apr", "May" ,"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		this.income_data = [];
		this.expense_data = [];
		this.income_search_data = [];
		this.expense_search_data = [];
		this.expenses_in_month = [];
		this.budgets_in_month = [];
		this.d = new Date();
		this.month = this.d.getMonth() + 1;
		this.year = this.d.getFullYear();
		this.current_page = "income";
		this.tags_list = [];	
	}
	var globalObject = new globalFunction();
	
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
		  
		  
		  $(document).on('focus',"#income-datepicker", function(){
			    $(this).datepicker({dateFormat:"yy/mm/dd"});
		  });
		  $(document).on('focus',"#expense-datepicker", function(){
			    $(this).datepicker({dateFormat:"yy/mm/dd"});
		  });
		  $(document).on('focus',".budget-datepicker", function(){
			    $(this).datepicker({dateFormat:"yy/mm/dd"});
		  });	
		  $(document).on('click','.plus',function(){
			  $('.generic-modal').modal('show');
			  if(globalObject.current_page == "income")
			  {
				  open_income_modal();
			  }
			  else if(globalObject.current_page == "expense")
			  {
				  open_expense_modal();
			  }
			  else if(globalObject.current_page == "budget")
			  {
				  open_budget_modal();
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
				  open_income_modal();
			  }
			  else if(selected == "expense")
			  {
				  open_expense_modal();
			  }
			  else
			  {
				  open_budget_modal();
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
			    	var text = $('#saved-tags-input').val();
			        if($('#saved-tags-input').length > 0 && text != "" && !is_in_tags_list(text))
			        {
			        	if($('#generic-modal-form').find('#addIncomeModal').length)
		        		{
			        		save_tag_ajax_call("income");
				        	get_tags_ajax_call("income");
		        		}
			        	else if($('#generic-modal-form').find('#addExpenseModal').length)
			        	{
			        		save_tag_ajax_call("expense");
				        	get_tags_ajax_call("expense");
			        	}
			        	else
			        	{
			        		save_tag_ajax_call("expense");
				        	get_tags_ajax_call("expense");
			        	}
			        }
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
		    $('#logout').click(function(){
		    	logout_ajax_call(false);
		    });

			$('.date-picker').datepicker({
				dateFormat : "yy/mm/dd"
			});

			$(document).on('click','.income-show-more span',function(e){
				  $('.income-show-more span').css("pointer-events","none");
				  $('.income-additional-info-div').slideToggle().toggleClass('active');
				  if ($('.income-additional-info-div').hasClass('active')) {
				    $('.income-show-more span').text('Store necessary information');
				    document.getElementById("income-additional-info").value = true;
				    myMap('income-location');
				    resizeMap();
				  } else {
				    $('.income-show-more span').text('Store additional information');
				    document.getElementById("income-additional-info").value = false;
				  }
				    setTimeout(function(){
				    	$('.income-show-more span').css("pointer-events","auto");
				    },300);
			});
			$(document).on('click','.expense-show-more span',function(e){
				  $('.expense-show-more span').css("pointer-events","none");
				  $('.expense-additional-info-div').slideToggle().toggleClass('active');
				  if ($('.expense-additional-info-div').hasClass('active')) {
				    $('.expense-show-more span').text('Store necessary information');
				    document.getElementById("expense-additional-info").value = true;
					myMap('expense-location');
				    resizeMap();
				  } else {
				    $('.expense-show-more span').text('Store additional information');
				    document.getElementById("expense-additional-info").value = false;
				  }
				    setTimeout(function(){
				    	$('.expense-show-more span').css("pointer-events","auto");
				    },300);
			});
			
			var is_in_tags_list = function(text){
				text = text.trim();
				for(var i=0; i<globalObject.tags_list.length ;i++)
				{
					if(globalObject.tags_list[i].toLowerCase() == text.toLowerCase())
					{
						return true;
					}
				}
				return false;
			}
			var open_income_modal = function(){
				  $(".budget-modal").css("display","none");
				  $(".income-expense-modal").css("display","block");
				  $(".income-expense-tags").html($(".saved-tags-div"));
				  $(".income-expense-tags .saved-tags-div").css("display","block");
				  $(".budget-tags .saved-tags-div").css("display","none");
				  $('.generic-modal').addClass("add-income-modal");
				  $('.generic-modal').attr("id","addIncomeModal");
				  get_tags_ajax_call("income");
				  $('#generic-type-dropdown').val("income");
				  $('.generic-amount').attr("name","income-amount");
				  $('.generic-amount').attr("id","income-amount");
				  $('.generic-datepicker').attr("name","income-date");
				  $('.generic-datepicker').attr("id","income-datepicker");
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
				  $('.generic-save').attr("id","income-save");
				  //$('#generic-modal-form').attr("action","income");
				  //$('#generic-modal-form').attr("method","POST");
			};
			var open_expense_modal = function(){
				  $(".budget-modal").css("display","none");
				  $(".income-expense-modal").css("display","block");
				  $(".income-expense-tags").html($(".saved-tags-div"));
				  $(".income-expense-tags .saved-tags-div").css("display","block");
				  $(".budget-tags .saved-tags-div").css("display","none");
				  $('.generic-modal').addClass("add-expense-modal");
				  $('.generic-modal').attr("id","addExpenseModal");
				  get_tags_ajax_call("expense");
				  $('#generic-type-dropdown').val("expense");
				  $('.generic-amount').attr("name","expense-amount");
				  $('.generic-amount').attr("id","expense-amount");
				  $('.generic-datepicker').attr("name","expense-date");
				  $('.generic-datepicker').attr("id","expense-datepicker");
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
				  $('.generic-save').attr("id","expense-save");
				  //$('#generic-modal-form').attr("action","expense");
				  //$('#generic-modal-form').attr("method","POST");
			}
			var open_budget_modal = function(){
				  $(".budget-modal").css("display","block");
				  get_tags_ajax_call("expense");
				  $('#generic-type-dropdown').val("budget");
				  $(".income-expense-modal").css("display","none");
				  $('.generic-save').attr("id","budget-save");
				  //$('#generic-modal-form').attr("action","budget");
				  //$('#generic-modal-form').attr("method","POST");
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
		else if($('.generic-save').attr('id') == "expense-save")
		{
			save_expense_ajax_call(globalObject.month, globalObject.year);
		}
		else if($('.generic-save').attr('id') == "budget-save")
		{
			save_budget_ajax_call();
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
		   		<img src='images/no_results.png'>
		   		<div class='empty-data-text'>No budgets for this month</div>
		   	</div>
		</div>
		
		<div id="search-page" style="display:none;" class="tabcontent page">
			<div class="search-container">
				<div class="search-label">Select date</div>
				<div class="search-input"><input type="text" class="date-picker"></div>
				<input type="button" id="income-expense-search-btn" class="search-btn" value="Search"><br>
			</div>
			<div class="search-results-container">
				<div class="income-results">
					<div id="income-results-header" class="search-results-header">Incomes</div>
					<div id="income_search_data"></div>
					<div id="empty-search-income-data" class="empty-search-data" style="display:none;">
						No income in this date
					</div>
				</div>
				<div class="expense-results">
					<div id="expense-results-header" class="search-results-header">Expenses</div>
					<div id="expense_search_data"></div>
					<div id="empty-search-expense-data" class="empty-search-data" style="display:none;">
						No expense in this date
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
			<div id="accounts-heading"><span>ACCOUNTS</span></div>
			<div id="home-accounts-list"></div>
		</div>
		
		<div id="budgets-right-slider" class="right-slider" style="display:none;">
			<div id="budgets-heading"><span>BUDGETS</span></div>
			<div id="home-budgets-list"></div>
		</div>
	</div>
	<button id="logout" class="logout" value="Logout">Logout</button>
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
			</h4>
        </div>
        <div class="modal-body">
        	<div class="income-expense-modal" style="display:none;">
		        <div class="textbox_space">
		        	<label>Amount</label><input type="text" id="generic-amount" class="generic-amount textbox">
		        </div>
		        <div class="textbox_space">
		        	<label>Date</label><input type="text" id="generic-datepicker" class="generic-datepicker textbox">
		        </div>
				<div class="income-expense-tags"></div>
		        <div class="generic-show-more"><span>Store additional information</span></div>
		      
		        <div class="generic-additional-info-div">
		        	<input type="hidden" class="generic-additional-info" id="generic-additional-info">
		        	<div class="textbox_space">
		            	<label>Location</label><input type="text" class="generic-location" id="generic-location" class="textbox">
		        		<img src="images/search-icon.png" class="generic-map-search icon search-icon" id="generic-map-search" alt="Search">
		        	</div>
		        	<input type="hidden" class="generic-location-lat" id="generic-location-lat">
		        	<input type="hidden" class="generic-location-lon" id="generic-location-lon">
		        	
		            <div class="generic-location-map" id="generic-location-map"></div>
	     	        <div class="textbox_space">
	     	        	<label>Description</label><input type="text" class="generic-description" id="generic-description" class="textbox">
	     	        </div>
		        	<div class="textbox_space">
			        	<label>Repeat</label><select class="generic-repeat-dropdown dropdown" id="generic-repeat-dropdown">
			        	    <option value="0">Never</option>
						    <option value="1">Daily</option>
					 	    <option value="2">Weekly</option>
						    <option value="3">Monthly</option>
						    <option value="4">Yearly</option>
						    <option value="5">Weekdays</option>
						    <option value="6">Weekends</option>
						</select>
					</div>
					<div class="textbox_space">
						<label>Reminder</label><select class="generic-reminder-dropdown dropdown" id="generic-reminder-dropdown">
							<option value="0">Never</option>
			        	    <option value="1">One day before</option>
						    <option value="2">On the day</option>
						</select>
					</div>
		        </div>
	        </div>
	        
			<div class="saved-tags-div" style="display:none;">
				<div class="textbox_space">
					<label>Tag</label>
					<select id="saved-tags-dropdown"  class="dropdown saved-tags-dropdown" name="tag_id"></select>
					<input type="text" id="saved-tags-input" style="display:none;">
					<img src="images/add-tag-icon.ico" class="icon add-icon" alt="Add tag">
					<div id="save-tag-hint" class="hint-text" style="display:none;">Press Enter to save</div>
				</div>

			</div>
				
				
	        <div class="budget-modal" style="display:none;">
	       		<div class="textbox_space">
		       		<label>Type</label><select id="budget-type-dropdown" class="dropdown" name="budget-type">
			        	    <option value="0">All expenses</option>
						    <option value="1">Tag</option>
					</select>
				</div>
				<div class="budget-tags"></div>
				<div class="textbox_space">
					<label>Repeat</label><select id="budget-repeat-dropdown" class="dropdown" name="budget-repeat">
			        	    <option value="0">One time budget</option>
						    <option value="1">Daily</option>
					 	    <option value="2">Weekly</option>
						    <option value="3" selected>Monthly</option>
						    <option value="4">Yearly</option>
					</select>
				</div>
				<div class="one-time-budget-div">
					<div class="textbox_space budget-start-datepicker">
						<label>Start date</label><input type="text" id="budget-start-datepicker" class="budget-datepicker textbox" name="budget-start-date">
					</div>
					<div class="textbox_space budget-end-datepicker" style="display:none;">
						<label>End date</label><input type="text" id="budget-end-datepicker" class="budget-datepicker textbox" name="budget-end-date">
					</div>
				</div>
				<div class="textbox_space">
					<label>Amount</label><input type="text" id="budget-amount" name="budget-amount" class="textbox">
				</div>
				<div class="textbox_space">
					<label>Description</label><input type="text" placeholder="optional" id="budget-description" name="budget-description" class="textbox">
				</div>
	        </div>
	        <input type="hidden" class="page_name" name="page_name"/>
        </div>
        <div class="modal-footer">
          <button class="generic-save" id="generic-save" class="btn" value="Save" onclick="setCurrentPage();">Save</button>
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
</body>

</html>