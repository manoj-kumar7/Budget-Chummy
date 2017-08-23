<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Home | BC</title>
	<link rel="stylesheet" href="styles/style.css" type="text/css">
	<script type="text/javascript" src="app/jquery-3.1.1.js"></script>
	<script type="text/javascript" src="app/jquery-ui.min.js"></script>
	<script type="text/javascript" src="app/bootstrap.js"></script>
	<script type="text/javascript" src="app/bootstrap.min.js"></script>
	<script type="text/javascript" src="app/jquery.canvasjs.min.js"></script>
	<script type="text/javascript" src="app/jquery.slimscroll.min.js"></script>
	<script type="text/javascript" src="app/canvasjs.min.js"></script>
	<script type="text/css" src="styles/bootstrap.min.css"></script>
	<link rel="stylesheet" href="styles/bootstrap.min.css" type="text/css">
	<link rel="stylesheet" href="styles/jquery-ui.css" type="text/css">
	<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDtFLcyhDfgarOIcwf-4qiScchMGJS25jo"></script>
	<script type="text/javascript">
	var month_array=["January","February","March","April","May","June","July","August","September","October","November","December"];
	var income_data = [];
	var expense_data = [];
	var income_search_data = [];
	var expense_search_data = [];
	var d = new Date();
	var month = d.getMonth() + 1;
	var year = d.getFullYear();
	var current_page = "income";
	var tags_list = [];
	
	window.onload=function(){
		getAccounts_ajax_call();
/* 		open_page(event,'income');
		income_ajax_call(month,year);
		$('#income-tab').addClass("active"); */
		if(<%=request.getParameter("page")%> == "expense")
		{
			open_page('expense');
			expense_ajax_call(month,year);
			$('#expense-tab').addClass("active");
		}
		else if(<%=request.getParameter("page")%> == "budget")
		{
			open_page('budget');
			//budget_ajax_call(month,year);
			$('#budget-tab').addClass("active");
		}
		else if(<%=request.getParameter("page")%> == "search")
		{
			open_page('search');
			$('#search-tab').addClass("active");
		}
		else if(<%=request.getParameter("page")%> == "users")
		{
			open_page('users');
			users_ajax_call(month,year);
			$('#users-tab').addClass("active");
		}
		else
		{
			open_page('income');
			income_ajax_call(month,year);
			$('#income-tab').addClass("active");	
		}
		document.getElementById("selected_month").innerHTML = month_array[month-1]+", "+year;
		  
		  
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
			  if(current_page == "income")
			  {
				  open_income_modal();
			  }
			  else if(current_page == "expense")
			  {
				  open_expense_modal();
			  }
			  else if(current_page == "budget")
			  {
				  open_budget_modal();
			  }
		  });
		  $(document).on('click','.accounts',function(){
			  var id = $(this).attr("id");
			  $('#account_id').val(id);
			  $('.page_name').val(current_page);
			  $('#accountChosenForm').submit();
		  });
		  
		  //$('.center').slimscroll();
	      $(function(){
 		      $('.center-content').slimscroll({
		    		width: '100%',
		  			height: '100%',
		  			opacity: '0'
		      }); 
		      $('.right-content').slimscroll({
		    		width: '100%',
		  			height: '100%',
		  			opacity: '0'
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
				 $(".one-time-budget-div").css("display","block");
			 }
			 else
			 {
				 $(".one-time-budget-div").css("display","none");
			 }
		 });	 
		  $(document).on('click','.add-btn',function(){
			  $('.add-btn').toggleClass('open');
		  });
		  $(document).on('click','.add-icon',function(){
			  $('.saved-tags-dropdown-div').css("display","none");
			  $('#saved-tags-input').css("display","block").focus();
		  });
		  $(document).on('focusout','#saved-tags-input',function(){
			  $('.saved-tags-dropdown-div').css("display","block");
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
			  var text = $('#saved-tags-input').val();
			    if(e.which == 13) {
			    	e.preventDefault();
			        if($('#saved-tags-input').length > 0 && text != "" && !is_in_tags_list(text))
			        {
			        	save_tag_ajax_call();
			        	get_tags_ajax_call();
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
		    	month=month-1;
		    	if(month == 0)
		    	{
		    		month = 12;
		    		year = year-1;
		    	}
		    	document.getElementById("selected_month").innerHTML = month_array[month-1]+", "+year;
		    	if($('#income-tab').hasClass('active'))
		    	{
		    		income_ajax_call(month,year);
		    	}
		    	else if($('#expense-tab').hasClass('active'))
		    	{
		    		expense_ajax_call(month,year);
		    	}	
		    });
		    $(document).on('click','.right-icon',function(){
		    	month=month+1;
		    	if(month == 13)
		    	{
		    		month = 1;
		    		year = year+1;
		    	}
		    	document.getElementById("selected_month").innerHTML = month_array[month-1]+", "+year;
		    	if($('#income-tab').hasClass('active'))
		    	{
		    		income_ajax_call(month,year);
		    	}
		    	else if($('#expense-tab').hasClass('active'))
		    	{
		    		expense_ajax_call(month,year);
		    	}
		    });
		    

		    
		    $('#income-tab').click(function(){
		    	location.href="home.jsp?page='income'";
		    });
		    $('#expense-tab').click(function(){
		    	location.href="home.jsp?page='expense'";
		    });	
		    $('#budget-tab').click(function(){
		    	location.href="home.jsp?page='budget'";
		    });
		    $('#search-tab').click(function(){
		    	location.href="home.jsp?page='search'";
		    });
		    $('.search-btn').click(function(){
		    	search_ajax_call();
		    });
		    $('#users-tab').click(function(){
		    	location.href="home.jsp?page='users'";
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
				for(var i=0; i<tags_list.length ;i++)
				{
					if(tags_list[i].toLowerCase() == text.toLowerCase())
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
				  get_tags_ajax_call();
				  $('.genericModal').addClass("add-income-modal");
				  $('.genericModal').attr("id","addIncomeModal");
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
				  $('#generic-modal-form').attr("action","incomeServlet");
				  $('#generic-modal-form').attr("method","POST");
			};
			var open_expense_modal = function(){
				  $(".budget-modal").css("display","none");
				  $(".income-expense-modal").css("display","block");
				  $(".income-expense-tags").html($(".saved-tags-div"));
				  $(".income-expense-tags .saved-tags-div").css("display","block");
				  $(".budget-tags .saved-tags-div").css("display","none");
				  get_tags_ajax_call();
				  $('.generic-modal').addClass("add-expense-modal");
				  $('.generic-modal').attr("id","addExpenseModal");
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
				  $('#generic-modal-form').attr("action","expenseServlet");
				  $('#generic-modal-form').attr("method","POST");
			}
			var open_budget_modal = function(){
				  $(".budget-modal").css("display","block");
				  get_tags_ajax_call();
				  $('#generic-type-dropdown').val("budget");
				  $(".income-expense-modal").css("display","none");
				  $('#generic-save').attr("id","budget-save");
				  $('#generic-modal-form').attr("action","budgetServlet");
				  $('#generic-modal-form').attr("method","POST");
			}
	};


	var open_page = function(page){
		current_page = page;
		if(page == "search" || page == "users")
		{
			$('.month-changer').css("display","none");
			$('.chart-space').css("display","none");
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
	var setCurrentPage = function(){
		$('.page_name').val(current_page);
	}

	
	</script>
</head>
<body>

<%
if(session.getAttribute("account_id") == null)
{
	response.sendRedirect("/BudgetChummy/FirstPage.jsp");
}
%>
<div class="home-body">

	<div class="left">
		<div class="tab">
		  <button class="tablinks" id="income-tab"><div class="tab-title">INCOME</div></button>
		  <button class="tablinks" id="expense-tab"><div class="tab-title">EXPENSE</div></button>
		  <button class="tablinks" id="budget-tab"><div class="tab-title">BUDGET</div></button>
		  <button class="tablinks" id="search-tab"><div class="tab-title">SEARCH</div></button>
		  <button class="tablinks" id="users-tab"><div class="tab-title">USERS</div></button>	 
		</div>
	</div>

<div class="center">
 	<div class="center-content">
	 	<div class="month-changer" style="display:none;">
	 		<img src="images/left_icon.png" class="image left-icon" alt="Prev month">
	 		<div id="selected_month"></div>
	 		<img src="images/right_icon.png" class="image right-icon" alt="Next month">
	 	</div>
		<div id="income-page" style="display:none;" class="tabcontent page">
			<div class="income-chart-space chart-space">
			    <div id="chartContainer1"></div>
			    <div id="empty-income-data" style="display:none;"></div>
		    </div>
		    <div class="income-table-space table-space">
	   	    	<table class="income-table table"><tbody></tbody></table>
	   	    </div>
		</div>
		<div id="expense-page" style="display:none;" class="tabcontent page">
			<div class="expense-chart-space chart-space">
			    <div id="chartContainer2" style="display:none;"></div>
			    <div id="empty-expense-data" style="display:none;"></div>
		   	</div>
		    <div class="expense-table-space table-space">
	   	    	<table class="expense-table table"><tbody></tbody></table>
	   	    </div>
		</div>
		<div id="budget-page" style="display:none;" class="tabcontent page"></div>
		
		<div id="search-page" style="display:none;" class="tabcontent page">
			Select date : <input type="text" class="date-picker">
			<input type="button" class="btn search-btn" value="Search"><br>
			Income for this month:<br><div id="income_search_data"></div>
			Expenses for this month:<br><div id="expense_search_data"></div>
		</div>
		<div id="users-page" style="display:none;" class="tabcontent page">
			<div class="account-info-space">
				<div id="users-account-name" class="users-account-name users-account-details"><pre>Account Name    :<div> </div></pre></div>
				<div id="users-no-of-members" class="users-no-of-members users-account-details"><pre>No of Members   :<div> </div></pre></div>
				<div id="users-created-on" class="users-created-on users-account-details"><pre>Created on      :<div> </div></pre></div>
				<div id="users-created-by" class="users-created-by users-account-details"><pre>Created by      :<div> </div></pre></div>
			</div>
			<div class="users-btns">
				<button id="add-user" class="add-user btn">Add User</button>
				<button id="users-invitations" class="users-invitations btn">Pending Invitations</button>
			</div>
		    <div class="users-table-space table-space">
	   	    	<table class="users-table table"><tbody></tbody></table>
	   	    	
	   	    </div>
		</div>
	</div>
</div>
<div class="right">
	<div class="right-content">
		<form id="accountChosenForm" action="accountChosenServlet" method="post">
			<div id="accounts-heading"><span>ACCOUNTS</span></div>
			<div id="accounts-list"></div>
			<input type="hidden" id="account_id" name="account_id" />
			<input type="hidden" class="page_name" name="page_name" />
		</form>
	</div>
</div>
	<button class="plus"></button>

<form action="logoutServlet" method="post">
	  <input type="submit" class="logout" value="Logout">
</form>
 

  <div class="modal show-location-modal" id="showLocationModal" role="dialog" style="display:none;">
    <div class="modal-dialog">

      <div class="modal-content">
        <div class="modal-header">
        	<button type="button" class="btn close-btn" data-dismiss="modal">&times;</button>
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

<form action="addUserServlet" method="post">
  <div class="modal add-user-modal" id="addUserModal" role="dialog" style="display:none;">
    <div class="modal-dialog">

      <div class="modal-content">
        <div class="modal-header">
        	<button type="button" class="btn close-btn" data-dismiss="modal">&times;</button>
			<h4 class="modal-title">Add User</h4>
        </div>
        <div class="modal-body">
        	Enter the email address of the user to send invitation
        	<div>
				<input type="text" id="add-user-input" name="add-user-input" class="text add-user-input"/>
			</div>
			Set authentication
			<div>
				<input type="radio" id="email-radio" name="authentication_type" value="Email" checked> Email
	 			<input type="radio" id="offline-radio" name="authentication_type" value="Offline"> Offline<br>
 			</div>
 			<div id="set-offline-code" style="display:none;">
 				<input type="text" id="offline-code" name="offline-code" class="text offline-code-input"/>
 			</div>
        </div>
        <div class="modal-footer">
        	<input type="submit" id="add-user-send" class="add-user-send btn" value="Send"/>
        </div>
      </div>
      
    </div>
  </div>
</form>

<form id="generic-modal-form" action="" method="">
  <div class="modal generic-modal" id="genericModal" role="dialog" style="display:none;">
    <div class="modal-dialog">

      <div class="modal-content">
        <div class="modal-header">
        	<button type="button" class="btn close-btn" data-dismiss="modal">&times;</button>
			<h4 class="modal-title">
				<select id="generic-type-dropdown">
				  <option value="income">ADD INCOME</option>
			 	  <option value="expense">ADD EXPENSE</option>
				  <option value="budget">ADD BUDGET</option>
				</select>
			</h4>
        </div>
        <div class="modal-body">
        	<div class="income-expense-modal" style="display:none;">
		        <div><input type="text" placeholder="Amount" id="generic-amount" class="generic-amount textbox"></div>
		        <div><input type="text" placeholder="Date" id="generic-datepicker" class="generic-datepicker textbox"></div>
				<div class="income-expense-tags"></div>
		        <div class="generic-show-more"><span>Store additional information</span></div>
		      
		        <div class="generic-additional-info-div">
		        	<input type="hidden" class="generic-additional-info" id="generic-additional-info">
		            <input type="text" placeholder="Location" class="generic-location" id="generic-location" class="textbox">
		        	<input type="hidden" class="generic-location-lat" id="generic-location-lat">
		        	<input type="hidden" class="generic-location-lon" id="generic-location-lon">
		        	<input type="button" class="generic-map-search" id="generic-map-search" class="btn" value="Search">
		            <div class="generic-location-map" id="generic-location-map"></div>
	     	        <div><input type="text" placeholder="Description" class="generic-description" id="generic-description" class="textbox"></div>
		        	<div><select class="generic-repeat-dropdown" id="generic-repeat-dropdown">
		        	    <option value="0">Never</option>
					    <option value="1">Daily</option>
				 	    <option value="2">Weekly</option>
					    <option value="3">Monthly</option>
					    <option value="4">Yearly</option>
					    <option value="5">Weekdays</option>
					    <option value="6">Weekends</option>
					</select></div>
					<div><select class="generic-reminder-dropdown" id="generic-reminder-dropdown">
						<option value="0">Never</option>
		        	    <option value="1">One day before</option>
					    <option value="2">On the day</option>
					</select></div>
		        </div>
	        </div>
	        
			<div class="saved-tags-div" style="display:none;">
				<div class="saved-tags-dropdown-div">
		        	<div><select id="saved-tags-dropdown" name="tag_id">
					</select><img src="images/add-icon.png" class="image add-icon" alt="Add tag"></div>
				</div>
				<div class="saved-tags-input-div">
					<div><input id="saved-tags-input" placeholder="Enter tag" style="display:none;"></div>
					<div id="save-tag-hint" class="hint-text" style="display:none;">Press Enter to save</div>
				</div>
			</div>
				
				
	        <div class="budget-modal" style="display:none;">
	       		<div><select id="budget-type-dropdown" name="budget-type">
		        	    <option value="0">All expenses</option>
					    <option value="1">Tag</option>
				 	    <option value="2">All expenses except tag</option>
				</select></div>
				<div class="budget-tags"></div>
				<div><select id="budget-repeat-dropdown" name="budget-repeat">
		        	    <option value="0">One time budget</option>
					    <option value="1">Daily</option>
				 	    <option value="2">Weekly</option>
					    <option value="3" selected>Monthly</option>
					    <option value="4">Yearly</option>
				</select></div>
				<div class="one-time-budget-div" style="display:none;">
					<div><input type="text" placeholder="Start Date" id="budget-start-datepicker" class="budget-datepicker textbox" name="budget-start-date"></div>
					<div><input type="text" placeholder="End Date" id="budget-end-datepicker" class="budget-datepicker textbox" name="budget-end-date"></div>
				</div>
				<div><input type="text" placeholder="Amount" id="budget-amount" name="budget-amount" class="textbox"></div>
				<div><input type="text" placeholder="Description(optional)" id="budget-description" name="budget-description" class="textbox"></div>
	        </div>
	        <input type="hidden" class="page_name" name="page_name"/>
        </div>
        <div class="modal-footer">
          <input type="submit" class="generic-save" id="generic-save" class="btn" value="Save" onclick="setCurrentPage();">
        </div>
      </div>
      
    </div>
  </div>
</form>

</div>
<script type="text/javascript" src="js/map-api.js"></script>
<script type="text/javascript" src="js/charts.js"></script>
<script type="text/javascript" src="js/ajax-calls.js"></script>
</body>

</html>