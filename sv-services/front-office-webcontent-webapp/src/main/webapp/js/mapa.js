var map;
var geocoder;
var direc='';


    function initialize() {
        if (GBrowserIsCompatible()) {
	        map = new GMap2(document.getElementById("map_canvas"));
	        map.setCenter(new GLatLng(-33.428, -70.620), 15);
	        //map.addControl(new GSmallMapControl());
	        //map.addControl(new GMapTypeControl());
	        map.setUIToDefault();
	        
            geocoder = new GClientGeocoder();
        }
    }


    function addAddressToMap(response) {
        map.clearOverlays();
        document.getElementById("Direcciones").innerHTML = '';
        if (!response || response.Status.code != 200) {
            alert("Lo sentimos, pero no es posible ubicar esta dirección.");
            //point = map.getCenter();
            
		    point = new GLatLng(0,0);
            document.getElementById("lat").value = point.lat();
            document.getElementById("lng").value = point.lng();
	        //document.getElementById("message").innerHTML = "Latitud: " + point.lat() + ", Longitud: " + point.lng();
		    var marker = new GMarker(point, {draggable: true});
		    GEvent.addListener(map, "click", function(overlay,latlng) {
                if (latlng) {
			        marker.setLatLng(latlng);
                    document.getElementById("lat").value = point.lat();
                    document.getElementById("lng").value = point.lng();
       	            //document.getElementById("message").innerHTML = "Latitud: " + point.lat() + ", Longitud: " + point.lng();
                }
            });
            GEvent.addListener(marker, "dragstart", function() {
            });
	        GEvent.addListener(marker, "dragend", function() {
		        point = marker.getLatLng();
                document.getElementById("lat").value = point.lat();
                document.getElementById("lng").value = point.lng();
                //document.getElementById("message").innerHTML = "Latitud: " + point.lat() + ", Longitud: " + point.lng();
            });
		    map.addOverlay(marker);
		    map.setZoom(12);
        } else {
	        var numRes = response.Placemark.length;
            //alert("Numero de resultados: " + numRes);
		    if (numRes == 1){
		        place = response.Placemark[0];
			    point = new GLatLng(place.Point.coordinates[1], place.Point.coordinates[0]);
		        //document.getElementById("message").innerHTML = "Latitud: " + point.lat() + ", Longitud: " + point.lng();
		        document.getElementById("lat").value = point.lat();
		        document.getElementById("lng").value = point.lng();

                var direccion = place.address.split(",");
                //alert(direccion.length);
                if (direccion.length == 2){
                    map.setCenter(point, 13);
                }else{
		            map.setCenter(point, 15);
		        }
                marker = new GMarker(point, {draggable: true});
            		
                GEvent.addListener(map, "click", function(overlay,latlng) {
                    if (latlng) {
				        marker.setLatLng(latlng);
			            map.addOverlay(marker);
                        document.getElementById("lat").value = point.lat();
                        document.getElementById("lng").value = point.lng();
	                    //var myHtml = "The GPoint value is: " + map.fromLatLngToDivPixel(latlng) + " at zoom level " + map.getZoom();
				        //document.getElementById("message").innerHTML = "Latitud: " + latlng.lat() + ", Longitud: " + latlng.lng();
	                    //map.openInfoWindow(latlng, myHtml);
	                }
	            });

	            GEvent.addListener(marker, "dragstart", function() {
	                map.closeInfoWindow();
	            });
	
	            GEvent.addListener(marker, "dragend", function() {
			        point = marker.getLatLng();
                    document.getElementById("lat").value = point.lat();
                    document.getElementById("lng").value = point.lng();
				    //document.getElementById("message").innerHTML = "Latitud: " + point.lat() + ", Longitud: " + point.lng();
	                //marker.openInfoWindowHtml(place.address);
	            });
				map.addOverlay(marker);
				//marker.openInfoWindowHtml(place.address + '<br><b>Country code:</b> ' + place.AddressDetails.Country.CountryNameCode);
			}else if (numRes > 1){
			    //MM_showHideLayers('ventana_mapa','','show');
			    //var layerMapa = document.getElementById("ventana_mapa");
	            //layerMapa.style.width = '500px';
	            //layerMapa.style.height = '600px';
			    for (var i = 0; i < response.Placemark.length; i++) {
			        place = response.Placemark[i];
				    point = new GLatLng(place.Point.coordinates[1], place.Point.coordinates[0]);
	                direc = direc + '<img src="/FO_IMGS/img/estructura/fl.gif" alt="flechita" border="0" />&nbsp;<a href="javascript:void(0)" onclick="showLocation(\'' + place.address + '\');return false;">' + place.address + '</a><br>';
			        //document.getElementById("message").innerHTML = "Latitud: " + point.lat() + ", Longitud: " + point.lng();
			    }
			    document.getElementById("Direcciones").innerHTML = direc;
			    //Amunátegui 42, La Florida, Chile
			    direc='';
            }
	    }
    }


    function addPointToMap(punto) {
        map.clearOverlays();
        document.getElementById("Direcciones").innerHTML = '';

        //document.getElementById("message").innerHTML = "Latitud: " + point.lat() + ", Longitud: " + point.lng();
        document.getElementById("lat").value = punto.lat();
        document.getElementById("lng").value = punto.lng();

        marker = new GMarker(punto, {draggable: true});
        map.setCenter(punto, 15);
            		
        GEvent.addListener(map, "click", function(overlay,latlng) {
            if (latlng) {
                marker.setLatLng(latlng);
	            map.addOverlay(marker);
                document.getElementById("lat").value = point.lat();
                document.getElementById("lng").value = point.lng();
                //var myHtml = "The GPoint value is: " + map.fromLatLngToDivPixel(latlng) + " at zoom level " + map.getZoom();
                //document.getElementById("message").innerHTML = "Latitud: " + latlng.lat() + ", Longitud: " + latlng.lng();
                //map.openInfoWindow(latlng, myHtml);
            }
        });

        GEvent.addListener(marker, "dragstart", function() {
            map.closeInfoWindow();
        });
	
        GEvent.addListener(marker, "dragend", function() {
	        point = marker.getLatLng();
            document.getElementById("lat").value = point.lat();
            document.getElementById("lng").value = point.lng();
		    //document.getElementById("message").innerHTML = "Latitud: " + point.lat() + ", Longitud: " + point.lng();
            //marker.openInfoWindowHtml(place.address);
        });
        map.addOverlay(marker);
        //marker.openInfoWindowHtml(place.address + '<br><b>Country code:</b> ' + place.AddressDetails.Country.CountryNameCode);
    }

	// showLocation() is called when you click on the Search button
	// in the form.  It geocodes the address entered into the form
	// and adds a marker to the map at that location.
	function showLocation(address) {
	   //var address = document.forms[0].q.value;
	   geocoder.getLocations(address, addAddressToMap);
	}

    function showLocationPoint(lat, lng) {
       //var address = document.forms[0].q.value;
       addPointToMap(new GLatLng(lat,lng));
    }
	