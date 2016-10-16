Event.observe(window, 'load', function(e) {
	llenarFonos();
	llenarChofer();
	llenarPatente();
 // Event.observe('addPedidoJumboVA', 'submit', checkForm);
});

var fono 	= 0;
var chofer 	= 0;
var patente = 0;
var tipo 	= "";
var id		= 0;

function llenarDatos(tipox, idx) {
	tipo	= tipox;
	id 		= idx;
	fono 	= $('fono').value;
	chofer 	= $('chofer').value;
	patente = $('patente').value;
}
function llenarFonos() {
	new Ajax.Updater('fono', 'TraeFonosTransporte', { 
													parameters: { id: id, tipo: tipo },
													onComplete : function() {
														setTimeout(function() {
															$("fono").value = fono;
														},500);														
													},
													'onFailure': function(transport) {
														alert('The request failed');
													},
													'onException': function(err) {
														// When an exception is encountered while executing the callbacks
														alert('Ocurrió un error.');
													}
												  });
}
function llenarChofer() {
	new Ajax.Updater('chofer', 'TraeChoferesTransporte', { 
													parameters: { id: id, tipo: tipo },
													onComplete : function() {
														setTimeout(function() {
															$("chofer").value = chofer;
														},500);														
													},
													'onFailure': function(transport) {
														alert('The request failed');
													},
													'onException': function(err) {
														// When an exception is encountered while executing the callbacks
														alert('Ocurrió un error.');
													}
												  });
}
function llenarPatente() {
	new Ajax.Updater('patente', 'TraePatentesTransporte', { 
													parameters: { id: id, tipo: tipo },
													onComplete : function() {
														setTimeout(function() {
															$("patente").value = patente;
														},500);														
													},
													'onFailure': function(transport) {
														alert('The request failed');
													},
													'onException': function(err) {
														// When an exception is encountered while executing the callbacks
														alert('Ocurrió un error.');
													}
												  });
}
function cambioChofer() {
	llenarDatos("C",$('chofer').value);
	llenarFonos();
	llenarPatente();
}
function cambioFono() {
	llenarDatos("F",$('fono').value);
	llenarChofer();
	llenarPatente();
}
function cambioPatente() {
	llenarDatos("P",$('patente').value);
	llenarChofer();
	llenarFonos();
	if ( $('patente').value == 0 ) {
		$('max_bins').innerHTML = "0";
	} else {
		var temp = $('patente').value.split('-=-');
		$('max_bins').innerHTML = temp[1];
	}
}

function agregarARuta() {
	var pedidos = obtenerPedidos();
	if ( pedidos == "" ) {
		alert("Para agregar a ruta debes seleccionar pedidos.");
		return;
	}
	if ( $('id_ruta_existente').value == "0" ) {
		alert("Debes seleccionar una ruta existente.");
		return;
	}
	
	var temp = $('id_ruta_existente').value.split('-=-');
	var fecha_ruta = temp[0];
	var ruta_existente = temp[1];
	var cant_max_bins = temp[2];
		
	//validar si son de distintas fechas
	if (!verificamosFechas(fecha_ruta, pedidos)) {
		if (!confirm("Existen pedidos con fecha distinta a la ruta.\nDesea agregarlos?")) {
			return;
		}
	}
	
	//validar si tiene capacidad el camion
	if (!verificamosCantidadBins(cant_max_bins, pedidos)) {
		if (!confirm("Sobrepasa la capacidad de transporte.\nDesea agregarlos de todas formas?")) {
			return;
		}
	}
	
	var params = { pedidos: pedidos, id_ruta: ruta_existente };
	var requestOptions = {
							'method': 'post',
							'parameters': params,
							'onSuccess': function(transport) {
								var dr = transport.responseXML.documentElement;
								var m = dr.getElementsByTagName('mensaje')[0].firstChild.data;
								if ( m == 'OK' ) {
									alert("Los pedidos se agregaron a la ruta.");
									window.location.href = "ViewMonitorDespacho?fecha=" + $('fecha').value + "&id_zona=" + $('id_zona').value + "&tipo=" + $('tipo').value + "&reprogramada=-1&id_pedido=" + $('id_pedido').value;
								} else {
									alert(m);
								}
							},
							'onFailure': function(transport) {
								alert('The request failed');
							},
							'onException': function(err) {
								// When an exception is encountered while executing the callbacks
								alert('Ocurrió un error.');
							}							
						};
	new Ajax.Request('AgregarPedidoARuta', requestOptions);
}
function verificamosFechas(fecha_ruta, pedidos) {
	var peds = pedidos.split('-=-');
	for (var i=0; i < peds.length; i++) {
		if ( $('fec_' + peds[i]).value != fecha_ruta) {
			return false;
		}
	}
	return true;
}

function verificamosCantidadBins(cant_max_bins, pedidos) {
	var peds = pedidos.split('-=-');
	var suma = 0;
	for (var i=0; i < peds.length; i++) {
		suma += parseInt($('bins_' + peds[i]).value,10);
	}
	if (suma > cant_max_bins) {
		return false;
	}
	return true;
}

function crearRuta() {
	var pedidos = obtenerPedidos();
	if ( pedidos == "" ) {
		alert("Para agregar una ruta debes seleccionar pedidos.");
		return;
	}
	if ( $('chofer').value == "0" ) {
		alert("Debes seleccionar un chofer.");
		return;
	}
	if ( $('fono').value == "0" ) {
		alert("Debes seleccionar un fono.");
		return;
	}
	if ( $('patente').value == "0" ) {
		alert("Debes seleccionar una patente.");
		return;
	}
	
	var temp = $('patente').value.split('-=-');
	var idx_patente = temp[0];
	var cant_max_bins = temp[1];
	//validar si tiene capacidad el camion
	if (!verificamosCantidadBins(cant_max_bins, pedidos)) {
		if (!confirm("Sobrepasa la capacidad de transporte.\nDesea agregarlos de todas formas?")) {
			return;
		}
	}

	var params = { pedidos: pedidos, chofer: $('chofer').value, fono: $('fono').value, patente: idx_patente };
	var requestOptions = {
							'method': 'post',
							'parameters': params,
							'onSuccess': function(transport) {
								var dr = transport.responseXML.documentElement;
								var m = dr.getElementsByTagName('mensaje')[0].firstChild.data;
								alert("La Ruta se agregó exitosamente");
								var id_ruta = dr.getElementsByTagName('id_ruta')[0].firstChild.data;
								//window.location.href = "ViewMonitorDespacho?fecha=" + $('fecha').value + "&id_jornada=" + $('id_jornada').value + "&id_zona=" + $('id_zona').value + "&tipo=" + $('tipo').value + "&reprogramada=-1";
								window.location.href = "ViewRuta?id_ruta=" + id_ruta;
							},
							'onFailure': function(transport) {
								alert('The request failed');
							},
							'onException': function(err) {
								// When an exception is encountered while executing the callbacks
								alert('Ocurrió un error al guardar.');
							}							
						};
	new Ajax.Request('AgregaRutaDespacho', requestOptions);	
}
function obtenerPedidos() {
	var obj = document.f_grilla;
	var pedidos = "";
	var sep = "";
	
	if (obj.pedidos) {
		if (!obj.pedidos.length) {
			if (obj.pedidos.checked) {
				pedidos = obj.pedidos.value;
			}	
		} else {
			for (var x=0; x < obj.pedidos.length; x++) {
				if (obj.pedidos[x].checked) {
					pedidos += (sep + obj.pedidos[x].value);
					sep = "-=-";
				}
			}
		}
	}
	return pedidos;
}

function cambioBinsSeleccionados() {
	var pedidos = obtenerPedidos();
	var suma = 0;
	var cont = 0;
	if (pedidos.length) {
		var peds = pedidos.split('-=-');
		for (var i=0; i < peds.length; i++) {
			suma += parseInt($('bins_' + peds[i]).value,10);
			
            //*** Actualiza Puntos dentro del Mapa ***
			var lat = $('lat_' + peds[i]).value;
			var lng = $('lng_' + peds[i]).value;
			var point = new GLatLng(lat, lng);
			//alert('lat: ' + lat + '\rlng: ' + lng);
			
			if (lat != 0 && lng != 0){
			    cont++;
			    bounds.extend(point);
			    map.addOverlay(createMarker(point, peds[i]));
			}
			
			/*****
			var lat2 = parseFloat(lat) + parseFloat(0.001);
			var lng2 = parseFloat(lng) + parseFloat(0.0005);
			bounds.extend(new GLatLng(lat2, lng2));
			alert('lat2: ' + lat2 + '\rlng2: ' + lng2);
			******/
			
			//alert("Latitud: " + lat + "\rLongitud: " + lng + "\rPedido: " + peds[i]);
			
			
			
            //****************************************
		}
		if (cont > 0){
            //Ajustar el zoom según los límites
            map.setZoom(map.getBoundsZoomLevel(bounds));
	        
            //Centrar el mapa de acuerdo a los límites
            map.setCenter(bounds.getCenter());
		}
	}
	$('bins_seleccionados').innerHTML = suma;
}

function cambioRutaExistente() {
	if ( $('id_ruta_existente').value == 0 ) {
		$('bins_disponibles').innerHTML = "0";
	} else {
		var temp = $('id_ruta_existente').value.split('-=-');
		$('bins_disponibles').innerHTML = temp[2];
	}
}

var globalCallbacks = {
	onCreate: function() {
		$("tabla_loading").style.top = document.documentElement.scrollTop + 'px';
		$("loading").show();
	},
	onComplete: function() {
		if (Ajax.activeRequestCount == 0 ) {
			$("loading").hide();
		}
	}
};
Ajax.Responders.register( globalCallbacks );