var map;
var geocoder;
var direc='';
var bounds;

function initialize() {
	map = new GMap2(document.getElementById("map_canvas"));
	map.setCenter(new GLatLng(-33.428, -70.620), 13);
	//map.addControl(new GSmallMapControl());
	//map.addControl(new GMapTypeControl());
	map.setUIToDefault();
	  
	geocoder = new GClientGeocoder();
}

function initialize(lat, lng, zoom, nom_local) {
	map = new GMap2(document.getElementById("map_canvas"));
	var point = new GLatLng(lat, lng);
	map.setCenter(point, zoom);
	
	//map.addControl(new GSmallMapControl());
	//map.addControl(new GMapTypeControl());
	map.setUIToDefault();
	
	bounds = new GLatLngBounds();
	
	geocoder = new GClientGeocoder();
	asignaLocal(lat, lng, nom_local);
}

function initializeRuta(lat, lng, zoom, nom_local) {
	map = new GMap2(document.getElementById("map_canvas"));
	var point = new GLatLng(lat, lng);
	map.setCenter(point, zoom);
	map.setUIToDefault();
	
	bounds = new GLatLngBounds();
	
	geocoder = new GClientGeocoder();
	asignaLocal(lat, lng, nom_local);
	dibujaMapa();
}

function asignaLocal(lat, lng, nom_local){
    var point = new GLatLng(lat, lng);
    
    bounds.extend(point);
    
    map.clearOverlays();
	var baseIcon = new GIcon(G_DEFAULT_ICON);
    baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
    baseIcon.iconSize = new GSize(20, 34);
    baseIcon.shadowSize = new GSize(37, 34);
    baseIcon.iconAnchor = new GPoint(9, 34);
    baseIcon.infoWindowAnchor = new GPoint(9, 2);
	
	var Icono = new GIcon(baseIcon);
    Icono.image = "http://gmaps-samples.googlecode.com/svn/trunk/markers/green/blank.png";

    // Set up our GMarkerOptions object
    markerOptions = { icon:Icono };
    var marker = new GMarker(point, markerOptions);
    GEvent.addListener(marker, "click", function() {
        //GInfoWindowOptions options2 = new GInfoWindowOptions();
        
        marker.openInfoWindowHtml("<b>" + nom_local + "</b>", {maxWidth:50});
    });
    map.addOverlay(marker);
}


function addAddressToMap(response) {
    map.clearOverlays();
    document.getElementById("Direcciones").innerHTML = '';
    if (!response || response.Status.code != 200) {
        alert("Lo sentimos, pero no es posible ubicar esta dirección.");
    } else {
        var numRes = response.Placemark.length;
        //alert("Numero de resultados: " + numRes);
	    if (numRes == 1){
	        place = response.Placemark[0];
		    point = new GLatLng(place.Point.coordinates[1], place.Point.coordinates[0]);
	        //document.getElementById("message").innerHTML = "Latitud: " + point.lat() + ", Longitud: " + point.lng();
	        document.getElementById("lat").value = point.lat();
	        document.getElementById("lng").value = point.lng();

	        map.setCenter(point, 15);
            marker = new GMarker(point, {draggable: true});
        		
            GEvent.addListener(map, "click", function(overlay,latlng) {
                if (latlng) {
			        marker.setLatLng(latlng);
		            map.addOverlay(marker);
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
                direc = direc + '<a href="javascript:void(0)" onclick="showLocation(\'' + place.address + '\');return false;">' + place.address + '</a><br>';
		        //document.getElementById("message").innerHTML = "Latitud: " + point.lat() + ", Longitud: " + point.lng();
		    }
		    document.getElementById("Direcciones").innerHTML = direc;
		    //Amunátegui 42, La Florida, Chile
		    direc='';
        }
    }
}


// showLocation() is called when you click on the Search button
// in the form.  It geocodes the address entered into the form
// and adds a marker to the map at that location.
function showLocation(address) {
   //var address = document.forms[0].q.value;
   geocoder.getLocations(address, addAddressToMap);
}


function createMarker(point, op) {
    var baseIcon = new GIcon(G_DEFAULT_ICON);
    baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
    baseIcon.iconSize = new GSize(20, 34);
    baseIcon.shadowSize = new GSize(37, 34);
    baseIcon.iconAnchor = new GPoint(9, 34);
    baseIcon.infoWindowAnchor = new GPoint(9, 2);

    // Create a lettered icon for this point using our icon class
    var Icono = new GIcon(baseIcon);
    Icono.image = "http://gmaps-samples.googlecode.com/svn/trunk/markers/red/blank.png";

    // Set up our GMarkerOptions object
    markerOptions = { icon:Icono };
    var marker = new GMarker(point, markerOptions);

    GEvent.addListener(marker, "click", function() {
        marker.openInfoWindowHtml("OP: <b>" + op + "</b>");
    });
    return marker;
}

function createMarkerRuta(point, op, marca) {
    var baseIcon = new GIcon(G_DEFAULT_ICON);
    baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
    baseIcon.iconSize = new GSize(20, 34);
    baseIcon.shadowSize = new GSize(37, 34);
    baseIcon.iconAnchor = new GPoint(9, 34);
    baseIcon.infoWindowAnchor = new GPoint(9, 2);

    // Create a lettered icon for this point using our icon class
    var letter = marca;//String.fromCharCode("A".charCodeAt(0) + marca);
    var Icono = new GIcon(baseIcon);
    Icono.image = "http://www.google.com/mapfiles/marker" + letter + ".png";
    Icono.printImage = "http://www.google.com/mapfiles/marker" + letter + ".png";
    Icono.mozPrintImage = "http://www.google.com/mapfiles/marker" + letter + ".png";

    // Set up our GMarkerOptions object
    markerOptions = { icon:Icono };
    var marker = new GMarker(point, markerOptions);
    //marker.setImage("http://www.google.com/mapfiles/marker" + letter + ".png");

    GEvent.addListener(marker, "click", function() {
        marker.openInfoWindowHtml("OP: <b>" + op + "</b>");
    });
    return marker;
}


/*function createMarker2(lat, lng, op) {
	//var lat = $('lat_' + peds[i]).value;
	//var lng = $('lng_' + peds[i]).value;
	var point = new GLatLng(lat, lng);
	//alert("Latitud: " + lat + "\rLongitud: " + lng + "\rPedido: " + peds[i]);
	map.addOverlay(createMarker(point, op));
}*/


function createMarker2(lat, lng, op, marca) {
	var point = new GLatLng(lat, lng);
	if (marca != " "){
	    bounds.extend(point);
	    map.addOverlay(createMarkerRuta(point, op, marca));
        
        //Ajustar el zoom según los límites
        map.setZoom(map.getBoundsZoomLevel(bounds));
	    
        //Centrar el mapa de acuerdo a los límites
        map.setCenter(bounds.getCenter());
	}
}
