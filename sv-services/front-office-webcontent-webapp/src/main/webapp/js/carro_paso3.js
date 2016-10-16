function eliminar_prod( id ) {
	if( confirm( "Esta seguro que desea eliminar el producto." ) ) {
		fila = document.getElementById("fila"+id);
		fila.style.background = '#F0F0F0';
		document.form_pro.elements["cantidad"+id].value = 0;
		document.form_pro.elements["cantidad"+id].selectedIndex = 0;
		actualizaProducto(id);
	}
}
function actualizaProducto(contador) {
	if ( $('carr_id'+contador).value == 0 ) {
		return;
	}
	var nota = "";
	if ($('nota'+contador)) {
		nota = $('nota'+contador).value;
	}
	var cantidad = 0;
	if ( $('cantidad'+contador) != null ) {
	  cantidad = $('cantidad'+contador).value;
	} 
	var params = {'carr_id':$('carr_id'+contador).value, 'cantidad':cantidad, 'nota':nota};
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function (REQUEST) {
			if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
				alert("Error al guardar el producto.");
				return;
			}
			var message = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
			if (message == 'OK') {
				llenarCarroCompras();
			} else {
				alert(message);
			}
		}
	};
	new Ajax.Request('ModProductoCarro', requestOptions);
	

}
function actualizar_productos() {
	for ( var i = 0; i < parseInt(document.form_pro.registros.value); i++ ) {
		if( document.form_pro.elements["maximo"+i] != 'undefined' && document.form_pro.elements["maximo"+i] != null && validar_cantidad(document.form_pro.elements["cantidad"+i], document.form_pro.elements["maximo"+i].value) == false ) {
			return;
		}
		if (document.form_pro.elements["cantidad"+i] != null) {
			if ( document.form_pro.elements["cantidad"+i].value == "" ) {
				alert("El campo cantidad no puede estar vacío");
				document.form_pro.elements["cantidad"+i].focus();
				return;
			}
			
			if(isNaN(document.form_pro.elements["cantidad"+i].value)){
				alert("El campo cantidad debe ser numérico");
				document.form_pro.elements["cantidad"+i].focus();
				return;
			}
		}		
	}
	/*
	var fp = document.form_pro;
	var longitudFormulario = fp.elements.length;
	var cadenaFormulario = "";
	var sepCampos = "";
	for ( var i=0; i <= fp.elements.length-1; i++ ) {
		cadenaFormulario += sepCampos + fp.elements[i].name + '=' + encodeURI(fp.elements[i].value);
		sepCampos="&";
	}
	*/
	var pars = Form.serialize($('form_pro'));
	var requestOptions = {
		'method': 'post',
		'parameters': pars,
		'onSuccess': respModificaProductos
	};
	new Ajax.Request('ModProductosCarroPaso1', requestOptions);
}

function respModificaProductos(REQUEST) {
	if (REQUEST.responseXML != null) {
		if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] != null) {
			var mensaje = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
			if (mensaje == 'OK') {
				llenarCarroCompras();
			} else {
				alert(respuesta);
			}
		}
	} else {
		alert("Ocurrió un error al actualizar el carro");
	}
	
}


function modificar_totalizador() {
	top.document.getElementById("totalizador").innerHTML='<strong>' + document.form_pro.total_actual.value + '</strong>';
    top.document.frmPedido.total_compra.value=document.form_pro.total_actual_sf.value;
    top.document.frmPedido.cant_prod.value = document.form_pro.total_producto_pedido.value;
	top.showCalendario(1,document.form_pro.total_producto_pedido.value);
}

function llenarCarroCompras() {
	var params = {};
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function (REQUEST) {
			$('carro_de_compras_paso3').innerHTML = REQUEST.responseText;
			cargaAcordeonProductos();
			init();
			$('loading').hide();
		}
	};
	new Ajax.Request('CheckoutProList', requestOptions);
}

function init() {
	modificar_totalizador();
	if ( $('text_total_desc_j_sf').value == '$0' && cantCupones() == 0) {
		escondePromos();
	} else {
		muestraPromos();
	}
	document.ftotal_desc._total_desc_j.value 		= $('text_total_desc_j').value;
	document.ftotal_desc._total_desc_j_txt.value	= $('text_promo_desc_j').value;
	document.ftotal_desc._total_desc_tc.value		= $('text_total_desc_tc').value;
	document.ftotal_desc._total_desc_tc_txt.value	= $('text_promo_desc_tc').value;
	clearTable();
	loadRows();
	if( $('text_msg_cupon').value != '' ) {
		alert( $('text_msg_cupon').value );
	}
}

var acordeonProductos;

function cargaAcordeonProductos() {
	acordeonProductos = new accordion('acor_lista_de_productos');
	activaTodos('acor_lista_de_productos','accordion_toggle','accordion_toggle_active');	
	/*if ($$('#acor_lista_de_productos .accordion_toggle') != '') {
		for (var i=0; i < $$('#acor_lista_de_productos .accordion_toggle').length; i++) {
			acordeonProductos.act($$('#acor_lista_de_productos .accordion_toggle')[i]);
		}
	}*/
}

function sumar( campo, incremento, maximo, contador ) {
	var valor = 0;
	if (campo.value == "" || campo.value == "undefined") {
		campo.value = 0;
	}
	valor = parseFloat(campo.value) + parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	campo.value = valor;
	if( validar_cantidad( campo, maximo ) == false ) {
		campo.value = maximo;
	}
	actualizaProducto(contador);
}
function restar( campo, incremento, maximo, contador ) {
	var valor = 0;
	if (campo.value == "" || campo.value == "undefined") {
		campo.value = 0;
	}
	valor = parseFloat(campo.value) - parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	if( valor < 0 ) {
		campo.value = 0;
		return;
	} else {
		campo.value = valor;
	}
	actualizaProducto(contador);
}

function cambiaCantidad(cantidad, intervalo, maximo, contador) {
	if ( valida_cantidad(cantidad,intervalo,maximo) ) {
		actualizaProducto(contador);
	}
}