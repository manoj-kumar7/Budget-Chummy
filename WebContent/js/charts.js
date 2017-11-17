			var income_chart = new CanvasJS.Chart("chartContainer1",
		    	    {
		    	      title:{
		    	        text: ""
		    	      },
		    	      height:300,
		    	      animationEnabled: true,
		    	      animationDuration: 1000,
		    	      backgroundColor: "#f1f1f1",
		    	      toolTip:{
		    	    	  content: "Tag: {tag}<br>Date: {date}",
		    	    	  animationEnabled:true
		    	      },
		    	      data: [
		    	      {
		   	    	   showInLegend: true,
		    	       type: "doughnut",
		    	       dataPoints: globalObject.income_data
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
		    	      backgroundColor: "#f1f1f1",
		    	      toolTip:{
		    	    	  content: "Tag: {tag}<br>Date: {date}",
		    	    	  animationEnabled:true
		    	      },
		    	      data: [
		    	      {
		   	    	   showInLegend: true,
		    	       type: "doughnut",
		    	       dataPoints: globalObject.expense_data
		    	     }
		    	     ]
		    });
		    