Event.observe(window, 'load', function(e) {
});

function eliminarOP() {
	var peds = obtenerPedidos();
	if ( peds == '' ) {
		alert("Debes seleccionar un pedido");
		return;
	}
	if (!confirm("Seguro que desea eliminar pedidos?")) {
		return;
	}
	
	var idRuta = $('id_ruta').value;
	var params = { pedidos: peds, id_ruta: idRuta };
	var requestOptions = {
							'method': 'post',
							'parameters': params,
							'onSuccess': function(transport) {
								var dr = transport.responseXML.documentElement;
								var m = dr.getElementsByTagName('mensaje')[0].firstChild.data;
								if (m == 'OK') {
									alert("Los pedidos se eliminaron exitosamente");
									window.location.href = "ViewRuta?id_ruta=" + idRuta;
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
	new Ajax.Request('EliminarPedidoDeRuta', requestOptions);
	
}

function anularRuta() {
	if (confirm("Seguro de anular la Ruta?")) {
		var idRuta = $('id_ruta').value;
		var params = { id_ruta: idRuta };
		var requestOptions = {
								'method': 'post',
								'parameters': params,
								'onSuccess': function(transport) {
									var dr = transport.responseXML.documentElement;
									var m = dr.getElementsByTagName('mensaje')[0].firstChild.data;
									if (m == 'OK') {
										alert("La ruta ha sido anulada exitosamente");
										window.location.href = "ViewRuta?id_ruta=" + idRuta;
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
		new Ajax.Request('AnularRuta', requestOptions);
	}
}

function activarRuta() {
	if (confirm("Seguro de activar la Ruta?")) {
		var idRuta = $('id_ruta').value;
		var params = { id_ruta: idRuta };
		var requestOptions = {
								'method': 'post',
								'parameters': params,
								'onSuccess': function(transport) {
									var dr = transport.responseXML.documentElement;
									var m = dr.getElementsByTagName('mensaje')[0].firstChild.data;
									if (m == 'OK') {
										alert("La ruta ha sido activada exitosamente");
										window.location.href = "ViewRuta?id_ruta=" + idRuta;
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
		new Ajax.Request('ActivarRuta', requestOptions);
	}
}

function obtenerPedidos() {
	var obj = document.f_grilla;
	var pedidos = "";
	var sep = "";
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
	return pedidos;
}

function obtenerAllPedidos() {
	var obj = document.f_grilla;
	var pedidos = "";
	var sep = "";
	if (obj.pedidos) {
		if (!obj.pedidos.length) {
			pedidos = obj.pedidos.value;			
		} else {
			for (var x=0; x < obj.pedidos.length; x++) {
				pedidos += (sep + obj.pedidos[x].value);
				sep = "-=-";
			}
		}
	}
	return pedidos;
}

function abrirVentana(servlet, tipo) {
	var temp = obtenerAllPedidos();
	var pedx = temp.split('-=-');	
	for (var i=0; i < pedx.length; i++) {
		if ( tipo == 1 ) {		 
			openWin( 'ViewPrint?url='+escape('' + servlet + '?id_pedido=' + pedx[i] + ''), pedx[i], 50 + (i*20), 50 + (i*20), 700, 500 );
		} else {
			openWin( servlet + '?id_pedido=' + pedx[i], pedx[i], 50 + (i*20), 50 + (i*20), 700, 500 );
		}
	}	
}

function abrirVentanaRuta(servlet) {
	openWin( 'ViewPrint?url='+escape('' + servlet + '?id_ruta=' + $('id_ruta').value + ''), 0, 100, 100, 700, 500 );	
}

function openWin(urlStr, pedido, left, top, width, height) {
	open(urlStr, 'popUpWin' + pedido, 'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=no,copyhistory=yes,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
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