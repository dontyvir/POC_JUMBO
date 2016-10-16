Event.observe(window, 'load', function(e) {
});

function cambiarEstado() {
	if (!confirm("Seguro que desea cambiar de estado?")) {
		return;
	}
	
	var idPedido = $('id_pedido').value;
	var idRuta = $('id_ruta').value;
	var params = { id_pedido: idPedido, id_ruta: idRuta };
	var requestOptions = {
							'method': 'post',
							'parameters': params,
							'onSuccess': function(transport) {
								var dr = transport.responseXML.documentElement;
								var m = dr.getElementsByTagName('mensaje')[0].firstChild.data;
								if (m == 'OK') {
									alert("El pedido quedó en estado 'En despacho'");
									window.location.href = "ViewModDetPedidoRuta?id_pedido=" + idPedido + "&id_ruta=" + idRuta;
								} else {
									alert(m);
								}
							},
							'onFailure': function(transport) {
								alert('The request failed');
							},
							'onException': function(err) {
								// When an exception is encountered while executing the callbacks
								alert('Ocurrió un error');
							}							
						};
	new Ajax.Request('CambiaEstadoPedido', requestOptions);
}

function aReprogramar(alertar, estado, id_pedido_real, id_ruta) {
	if (alertar) {
		if (!confirm("Precaución: El pedido que quiere reprogramar está en estado '" + estado + "'.\nPor lo tanto debe considerar esta condición al momento de reprogramar.")) {
			return;
		}
	}
	popUpWindow('ViewCalJorForm?id_pedido=' + id_pedido_real + '&id_ruta=' + id_ruta + '&origen=1', 100, 100,680, 570);
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