			var income_chart = new CanvasJS.Chart("chartContainer1",
		    	    {
		    	      title:{
		    	        text: ""
		    	      },
		    	      height:300,
		    	      animationEnabled: true,
		    	      animationDuration: 1000,
		    	      toolTip:{
		    	    	  content: "Tag: {tag}<br>Date: {date}",
		    	    	  animationEnabled:true
		    	      },
		    	      data: [
		    	      {
		   	    	   showInLegend: true,
		    	       type: "doughnut",
		    	       dataPoints: income_data
		    	     }
		    	     ]
		    });
		    var expense_chart = new CanvasJS.Chart("chartContainer2",
		    	    {
		    	      title:{
		    	        text: ""
		    	      },
		    	      height:300,
		    	      animationEnabled: true,
		    	      animationDuration: 1000,
		    	      toolTip:{
		    	    	  content: "Tag: {tag}<br>Date: {date}",
		    	    	  animationEnabled:true
		    	      },
		    	      data: [
		    	      {
		   	    	   showInLegend: true,
		    	       type: "doughnut",
		    	       dataPoints: expense_data
		    	     }
		    	     ]
		    });
		    
			      
		    // var budget_chart = function(){
		    //      var data = google.visualization.arrayToDataTable([
		    //            ['Date', 'Expense'],
		    //            ['Sep 1', 1000],
		    //            ['Sep 2', 1170],
		    //            ['Sep 3', 660],
		    //            ['Sep 4', 1030],
		    //            ['Sep 1', 1000],
		    //            ['Sep 2', 1170],
		    //            ['Sep 3', 660],
		    //            ['Sep 4', 1030],
		    //            ['Sep 1', 1000],
		    //            ['Sep 2', 1170],
		    //            ['Sep 3', 660],
		    //            ['Sep 4', 1030],
		    //            ['Sep 1', 1000],
		    //            ['Sep 2', 1170],
		    //            ['Sep 3', 660],
		    //            ['Sep 4', 1030],
		    //            ['Sep 1', 1000],
		    //            ['2', 1170],
		    //            ['3', 660],
		    //            ['4', 1030],
		    //            ['1', 1000],
		    //            ['2', 1170],
		    //            ['3', 660],
		    //            ['4', 1030],
		    //            ['1', 1000],
		    //            ['2', 1170],
		    //            ['3', 660],
		    //            ['4', 1030],
		    //            ['1', 1000],
		    //            ['2', 1170],
		    //            ['3', 660],
		    //            ['4', 1030],
		    //          ]);

		    //         var options = {
		    //            chart: {
		                 
		    //            }
		    //          };

		    //          var chart = new google.charts.Bar(document.getElementById('budget-chart-space'));
		    //          chart.draw(data, google.charts.Bar.convertOptions(options));
		    // };