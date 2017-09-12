var lat,lon;
	  var map,marker=false;
	  lat = 13.0827; 
	  lon = 80.2707;
	  function myMap(type) {
		  geocoder = new google.maps.Geocoder();
		  var mapProp= {
		      center:new google.maps.LatLng(lat,lon),
		      zoom:6,
		  };
		  map=new google.maps.Map(document.getElementById(type+"-map"),mapProp);
		  	google.maps.event.addListener(map, 'click', function(event) {                
		        //Get the location that the user clicked.
		        var clickedLocation = event.latLng;
		        //If the marker hasn't been added.
		        if(marker === false){
		            //Create the marker.
		            marker = new google.maps.Marker({
		                position: clickedLocation,
		                map: map,
		                draggable: true //make it draggable
		            });
		            //Listen for drag events!
		            google.maps.event.addListener(marker, 'dragend', function(event){
		                markerLocation(type);
		            });
		        } else{
		            //Marker has already been added, so just change its location.
		            marker.setPosition(clickedLocation);
		        }
		        //Get the marker's location.
		        markerLocation(type);
		    });
		    
	  }
	  
	  function codeAddress(type) {
		  var address = document.getElementById(type).value;
		  geocoder.geocode({
		    'address': address
		  }, function(results, status) {
		    if (status == google.maps.GeocoderStatus.OK) {

		      map.setCenter(results[0].geometry.location);
		      var marker = new google.maps.Marker({
		        map: map,
		        position: results[0].geometry.location
		      });
		    	var currentLocation = marker.getPosition();
			    //Add lat and lng values to a field that we can save.
			    document.getElementById(type+'-lat').value = currentLocation.lat(); //latitude
			    document.getElementById(type+'-lon').value = currentLocation.lng(); //longitude
		    } else {
//		      alert('Geocode was not successful for the following reason: ' + status);
		    }
		  });
		}
	  

	//This function will get the marker's current location and then add the lat/long
	//values to our textfields so that we can save the location.
	function markerLocation(type){
	    //Get location.
	    var currentLocation = marker.getPosition();
	    //Add lat and lng values to a field that we can save.
	    document.getElementById(type+'-lat').value = currentLocation.lat(); //latitude
	    document.getElementById(type+'-lon').value = currentLocation.lng(); //longitude
		$.getJSON( "https://maps.googleapis.com/maps/api/geocode/json?latlng="+currentLocation.lat()+","+currentLocation.lng()+"&key=AIzaSyDtFLcyhDfgarOIcwf-4qiScchMGJS25jo", function( data ) {
			document.getElementById(type).value = data.results[0].formatted_address;
		});
	}
	
    function resizeMap()
    {
	    google.maps.event.trigger(map,'resize');
	    map.setZoom( map.getZoom() );
    }
    
    function showLocationInMap(lati,longi){
    	  var mapLoc= {
  		      center:new google.maps.LatLng(lati,longi),
  		      zoom:11,
  		  };
  		  map=new google.maps.Map(document.getElementById("location-in-map"),mapLoc);
    	  var marker = new google.maps.Marker({
              position: new google.maps.LatLng(lati,longi),
              animation: google.maps.Animation.DROP,
              map: map,
          });
  		  marker.setMap(map);
    }
    
    function recenter(lat,lon){
    	map.setCenter(new google.maps.LatLng(lat,lon))
    }
