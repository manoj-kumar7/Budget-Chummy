			var income_chart = new CanvasJS.Chart("chartContainer1",
		    	    {
		    	      title:{
		    	        text: ""
		    	      },
		    	      height:300,
		    	      animationEnabled: true,
		    	      animationDuration: 1000,
		    	      toolTip:{
		    	    	  content: "Description: {description}<br>Date: {date}",
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
		    	    	  content: "Description: {description}<br>Date: {date}",
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