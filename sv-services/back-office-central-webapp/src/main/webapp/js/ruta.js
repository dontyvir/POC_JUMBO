Event.observe(window, 'load', function(e) {
});

FINALIZA_OP = true;

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
								} else {
									alert(m);
								}
								window.location.href = "ViewRuta?id_ruta=" + idRuta;
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

//T:a tiempo   R:retrasado   A:adelantado
function comparaFechasHoras(fcInDom,fcCompromiso, hInDom, hIniCompromiso, hFinCompromiso) {
	var fecha1 = fcInDom.value.substring(6,10) + fcInDom.value.substring(3,5) + fcInDom.value.substring(0,2);
	var fecha2 = fcCompromiso.value.substring(6,10) + fcCompromiso.value.substring(3,5) + fcCompromiso.value.substring(0,2);
	if ( parseInt(fecha1,10) == parseInt(fecha2,10) ) {
		if ( parseInt(hInDom.value.replace(":",""),10) < parseInt(hIniCompromiso.value.replace(":",""),10) ) {
			return "A";
		}
		if ( parseInt(hInDom.value.replace(":",""),10) > parseInt(hFinCompromiso.value.replace(":",""),10) ) {
			return "R";
		}	
		return "T";
	} else if ( parseInt(fecha1,10) > parseInt(fecha2,10) ) {
		return "R";
	} else {
		return "A";
	}
}

function validaHora(hora) {
	var valor = hora.value;
	var patron = /^[0-9]{2,2}\:[0-9]{2,2}$/;
	var test2 = valor.split(':');
	var hok = parseInt(test2[0])>=0 && parseInt(test2[0])<24;
	var mok = parseInt(test2[1])>=0 && parseInt(test2[1])<60;
	if ( patron.test(valor) && hok && mok) {
		return true;
	} else {
		alert("Hora no valida, formato debe ser 'hh:mm' (Ej. 22:30)");
		hora.focus();
		hora.select();
		return false;
	}
}

function cambioDevolucion(op) {
	if ( $('check_' + op).checked ) {
		$('devolucion_' + op).disabled = false;
	} else {
		$('devolucion_' + op).value = "";
		$('devolucion_' + op).disabled = true;
	}
}

function finalizarOp(op) {
	if ( $('fc_in_dom_' + op).value == '' ) {
		alert("Debe ingresar la fecha de llegada a domicilio");
		return;
	}
	if (!validaHora( $('hr_in_dom_'+op) )) {
		return;
	}
	if (!validaHora( $('hr_out_dom_'+op) )) {
		return;
	}
	if ( parseInt($('hr_out_dom_'+op).value.replace(":",""),10) <= parseInt($('hr_in_dom_'+op).value.replace(":",""),10) ) {
		alert("La hora de salida debe ser mayor a la hora de llegada a domicilio.");
		$('hr_in_dom_'+op).focus();
		$('hr_in_dom_'+op).select();
		return;
	}
	
	var cumplimiento = comparaFechasHoras( $('fc_in_dom_' + op), $('fc_despacho_' + op), $('hr_in_dom_'+op), $('hini_despacho_'+op), $('hfin_despacho_'+op));

	if ( cumplimiento != "T" ) {
		$('motivo_' + op).disabled = false;
		$('responsable_' + op).disabled = false;
		
		if ( $('motivo_' + op).value == '0' ) {
			alert("Debe seleccionar el motivo de no cumplimiento de horario de entrega");
			return;
		}
		if ( $('responsable_' + op).value == '0' ) {
			alert("Debe seleccionar el responsable de no cumplimiento de horario de entrega");
			return;
		}
				
	} else {
		$('motivo_' + op).disabled = true;
		$('responsable_' + op).disabled = true;
	}
	
	if ($('check_' + op).checked) {
		if ( $('devolucion_' + op).value == '' ) {
			alert("Debe ingresar el detalle de la observación");
			$('devolucion_' + op).focus();
			return;
		}
	}	
	
	if ( !confirm("¿Desea Finalizar el pedido?") ) {
		return;
	}
	
	
	var idRuta = $('id_ruta').value;
	var params = { id_pedido: op, id_ruta: idRuta,
				   fc_llegada: $('fc_in_dom_'+op).value, hr_in: $('hr_in_dom_'+op).value, hr_out: $('hr_out_dom_'+op).value,
				   cumplimiento: cumplimiento, motivo: $('motivo_'+op).value, responsable: $('responsable_'+op).value,
				   devolucion: $('devolucion_'+op).value, observacion: $('observacion_'+op).value };
	var requestOptions = {
							'method': 'post',
							'parameters': params,
							'onSuccess': function(transport) {
								var dr = transport.responseXML.documentElement;
								var m = dr.getElementsByTagName('mensaje')[0].firstChild.data;
								if (m == 'OK') {
									alert("El pedido finalizó correctamente");
									window.location.href = "ViewDetRuta?id_ruta=" + idRuta;
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
	new Ajax.Request('FinalizarPedido', requestOptions);
	
	
}

function inHora(op) {
	if ( $('hr_in_dom_' + op).value == "hh:mm") {
		$('hr_in_dom_' + op).value = "";
	}
}

function outHora(op) {
	if ( $('hr_out_dom_' + op).value == "hh:mm") {
		$('hr_out_dom_' + op).value = "";
	}
}
var OP_SEL = 0;
function opSel(op) {
	OP_SEL = op;
}
function llenoHora(op) {
	habilitaCombos(op);
}
function cambioFecha() {
	habilitaCombos(OP_SEL);
}

function habilitaCombos(op) {
	if ( $('fc_in_dom_' + op).value == '' ) {
		return;
	}
	if ( $('hr_in_dom_'+op).value != '' && $('hr_in_dom_'+op).value != 'hh:mm' && $('hr_in_dom_'+op).value.length == 5 ) {
		if (!validaHora( $('hr_in_dom_'+op) )) {
			return;
		}
	} else {
		return;
	}
	if ( $('hr_out_dom_'+op).value != '' && $('hr_out_dom_'+op).value != 'hh:mm' && $('hr_out_dom_'+op).value.length == 5 ) {
		if (!validaHora( $('hr_out_dom_'+op) )) {
			return;
		}
	} else {
		return;
	}
	if ( parseInt($('hr_out_dom_'+op).value.replace(":",""),10) <= parseInt($('hr_in_dom_'+op).value.replace(":",""),10) ) {
		alert("La hora de salida debe ser mayor a la hora de llegada a domicilio.");
		$('hr_in_dom_'+op).focus();
		$('hr_in_dom_'+op).select();
		return;
	}
	var cumplimiento = comparaFechasHoras( $('fc_in_dom_' + op), $('fc_despacho_' + op), $('hr_in_dom_'+op), $('hini_despacho_'+op), $('hfin_despacho_'+op));

	if ( cumplimiento != "T" ) {
		$('motivo_' + op).disabled = false;
		$('responsable_' + op).disabled = false;
				
	} else {
		$('motivo_' + op).disabled = true;
		$('responsable_' + op).disabled = true;
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