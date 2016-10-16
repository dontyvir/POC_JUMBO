var params = {};

function documentoBOL() {
	var idCaso = document.getElementById("id_caso").value;
	window.open ("ViewDocumentoCaso?id_caso="+idCaso,"mywindow","status=1,resizable=0,scrollbars=1,menubar=0,width=630,height=580"); 
}	

function guardarCaso() {
	if ($("sel_estados").value == 0) {
		alert("Debe seleccionar el estado.");
		$("sel_estados").focus();
		return;
	}
	if (Trim($("comentario_log").value) == '') {
		alert("Debe ingresar un comentario");
		$("comentario_log").focus();
		return;
	}
	document.frm_add_edit_caso.submit();
	/*
	var estadoActual = $("estado_actual").value;
	var estadoNuevo = $("sel_estados").value;	
	var params2 = {'estado_actual':estadoActual,'estado_nuevo':estadoNuevo};	
	var requestOptions = {
						'method': 'post',
						'parameters': params2,
						'onSuccess': function(REQUEST) {								
								if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
									alert("Existe un error o usted no tiene permisos para ejecutar VerificaCambioEstadoCaso.");
									return;
								}	
								var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
								
								if (mensaje != 'OK') {
									alert(mensaje);
									return;
								}
								document.frm_add_edit_caso.submit();							
							}
						};
	new Ajax.Request('VerificaCambioEstCaso', requestOptions);	
	*/
}

function anularCasoYSalir() {
	if (Trim($("comentario_anular").value) == '') {
		alert("Debe ingresar la razón de la anulación del caso");
		$("comentario_anular").focus();
		return;
	}	
	document.frm_add_edit_caso.action = "AnularCaso";
	document.frm_add_edit_caso.submit();
}

function agregarProducto() {
	$("id_producto").value = "0";  //Nos aseguramos de que sea un producto nuevo
	$("add_edit_prod").show();
	$("anula_producto").hide();
	$("salir_comentario").hide();
}

function anularCaso() {
	$("add_edit_prod").hide();
	$("anula_producto").show();
	$("salir_comentario").hide();
}

function vantanaComentario() {
	$("add_edit_prod").hide();
	$("anula_producto").hide();
	$("salir_comentario").show();	
	$("comentario_log").focus();
}

function salir() {
	document.frm_add_edit_caso.action = "ViewMonitorCasos";
	document.frm_add_edit_caso.submit();
}

function ocultarVentanas() {
	$("id_producto").value = "0"; 
	$("p_tipo_accion").value = '0';
	$("pp_descripcion").value = '';
	$("pp_cantidad").value = '';
	$("pp_unidad").value = '0';
	$("sel_quiebre").value = '0';
	$("sel_responsable").value = '0';
	$("pickeador").value = '';
	$("precio_retirar").value = '';
	$("comentario_boc").value = '';
	$("msj_add_prod").innerHTML = "";
	
	$("comentario_anular").value = '';
	$("comentario_log").value = '';
	
	$("ps_descripcion").value = '';
	$("ps_cantidad").value = '';
	$("ps_unidad").value = '0';
	$("precio_enviar").value = '';
	$("comentario_bol").value = '';
	
	$("add_edit_prod").hide();
	$("anula_producto").hide();
	$("salir_comentario").hide();
}

function cambioTipoProducto(valor) {
	if (valor == 0) {
		alert("Debe seleccionar un tipo de Producto.");
		return;
	}
	if (valor == 'R' || valor == 'P') { //R: RETIRAR O P:ENVIO DINERO
		$("tabla_precio_retirar").show();
		$("tabla_precio_enviar").hide();
	} else if (valor == 'E') {
		$("tabla_precio_retirar").hide();
		$("tabla_precio_enviar").show();
	} else {
		$("tabla_precio_retirar").hide();
		$("tabla_precio_enviar").hide();
	}
	if (valor == 'D') {
		$("tabla_cant_uni").hide();
		$("tabla_pickeador").hide();
	} else {
		$("tabla_cant_uni").show();
		$("tabla_pickeador").show();
	}
}



//INICIO MODIFICAR UN PRODUCTO
function traerInfoProducto(id_producto) {	
	$("add_edit_prod").show();
	$("anula_producto").hide();
	
	$("msj_add_prod").innerHTML = "<font color='blue'><b>Buscando información...</b></font>";
	var requestOptions = {
					'method': 'post',
					'parameters': "idProducto=" + id_producto,
					'onSuccess': llenarFormModificar
					  };
	new Ajax.Request('DetalleParaProductoDeCaso', requestOptions);	
}
function llenarFormModificar(REQUEST) {
	if (REQUEST.responseXML.getElementsByTagName("id_producto")[0] == null) {
		$("msj_add_prod").innerHTML = "<font color='red'><b>No se pudo cargar el producto seleccionado!</b></font>";
		return;
	}
	var id_producto = REQUEST.responseXML.getElementsByTagName("id_producto")[0].childNodes[0].nodeValue;
	var id_caso = REQUEST.responseXML.getElementsByTagName("id_caso")[0].childNodes[0].nodeValue;
	var tipo_accion = REQUEST.responseXML.getElementsByTagName("tipo_accion")[0].childNodes[0].nodeValue;
	
	var pp_descripcion = REQUEST.responseXML.getElementsByTagName("pp_descripcion")[0].childNodes[0].nodeValue;
	var pp_cantidad = REQUEST.responseXML.getElementsByTagName("pp_cantidad")[0].childNodes[0].nodeValue;
	var pp_unidad = REQUEST.responseXML.getElementsByTagName("pp_unidad")[0].childNodes[0].nodeValue;
	var comentario_boc = REQUEST.responseXML.getElementsByTagName("comentario_boc")[0].childNodes[0].nodeValue;
	
	var ps_descripcion = REQUEST.responseXML.getElementsByTagName("ps_descripcion")[0].childNodes[0].nodeValue;
	var ps_cantidad = REQUEST.responseXML.getElementsByTagName("ps_cantidad")[0].childNodes[0].nodeValue;
	var ps_unidad = REQUEST.responseXML.getElementsByTagName("ps_unidad")[0].childNodes[0].nodeValue;
	var comentario_bol = REQUEST.responseXML.getElementsByTagName("comentario_bol")[0].childNodes[0].nodeValue;	
	
	var pickeador = REQUEST.responseXML.getElementsByTagName("pickeador")[0].childNodes[0].nodeValue;
	var id_quiebre = REQUEST.responseXML.getElementsByTagName("id_quiebre")[0].childNodes[0].nodeValue;
	var id_responsable = REQUEST.responseXML.getElementsByTagName("id_responsable")[0].childNodes[0].nodeValue;
	var id_motivo = REQUEST.responseXML.getElementsByTagName("id_motivo")[0].childNodes[0].nodeValue;
	
	var precio = REQUEST.responseXML.getElementsByTagName("precio")[0].childNodes[0].nodeValue;
	
	$("p_tipo_accion").value = printValor(tipo_accion);
	cambioTipoProducto($("p_tipo_accion").value);
	
	$("id_producto").value = id_producto;
	$("pp_descripcion").value = printValor(pp_descripcion);
	$("pp_cantidad").value = printValor(pp_cantidad);
	$("pp_unidad").value = printValorCombo(pp_unidad);	
	$("sel_quiebre").value = id_quiebre;
	$("sel_responsable").value = id_responsable;
	$("sel_motivo").value = id_motivo;
	$("pickeador").value = printValor(pickeador);
	$("comentario_boc").value = printValor(comentario_boc);
	
	$("precio_retirar").value = precio;
	$("precio_enviar").value = precio;

	//Bloqueamos esto y cuando guardamos los habilitamos
	$("p_tipo_accion").disabled = true;
	$("pp_descripcion").disabled = true;
	$("pp_cantidad").disabled = true;
	$("pp_unidad").disabled = true;
	$("sel_quiebre").disabled = true;
	$("sel_responsable").disabled = true;
	$("sel_motivo").disabled = true;
	$("pickeador").disabled = true;
	$("precio_retirar").disabled = true;
	$("comentario_boc").disabled = true;
	
	//Si el tipo es Enviar tiene la posibilidad de agregar un producto solucion, de lo contrario sólo puede ingresar un comentario	
	$("ps_descripcion").value = printValor(ps_descripcion);
	$("ps_cantidad").value = printValor(ps_cantidad);
	$("ps_unidad").value = printValorCombo(ps_unidad);
	$("comentario_bol").value = printValor(comentario_bol);	
	
	if (tipo_accion == 'E') {
		$("tabla_descripcion_ps").show();
		$("tabla_cant_uni_ps").show();
		$("msj_prod_solucion").innerHTML = "Producto Alternativo";
		
	} else {
		$("tabla_descripcion_ps").hide();
		$("tabla_cant_uni_ps").hide();
		$("msj_prod_solucion").innerHTML = "Comentario";
		
	}
	$("msj_add_prod").innerHTML = "";
}
//FIN MODIFICAR PRODUCTO





//INICIO ACTUALIZAR UN PRODUCTO
function guardarProducto() {	
	var psDescripcion = $("ps_descripcion").value;
	var psCantidad = $("ps_cantidad").value;
	var psUnidad = $("ps_unidad").value;
	var comentarioBol = $("comentario_bol").value;
	var precioEnviar = $("precio_enviar").value;
	var precio = 0;
	
	if (Trim(psDescripcion) != '') {
		if (Trim(psCantidad) == '') {
			alert("Debe ingresar la cantidad.");
			$("ps_cantidad").focus();
			return;
		}
	}
	if (Trim(psCantidad) != '') {
		if (Trim(psDescripcion) == '') {
			alert("Debe ingresar la descripción.");
			$("ps_descripcion").focus();
			return;
		}
	}	
	
	if (Trim(comentarioBol) == '') {
		alert("Debe ingresar un comentario.");
		$("comentario_bol").focus();
		return;
	}
	psCantidad = formateaCantidad(psCantidad);
	
	//Lo unico q es importante, es el comentario.. por lo tanto ahora queda con habilitar los campos y enviarlos
	$("p_tipo_accion").disabled = false;
	$("pp_descripcion").disabled = false;
	$("pp_cantidad").disabled = false;
	$("pp_unidad").disabled = false;
	$("sel_quiebre").disabled = false;
	$("sel_responsable").disabled = false;
	$("pickeador").disabled = false;
	$("precio_retirar").disabled = false;
	$("comentario_boc").disabled = false;
	
	var tipo = $("p_tipo_accion").value;
	var ppDescripcion = $("pp_descripcion").value;
	var ppCantidad = $("pp_cantidad").value;
	var ppUnidad = $("pp_unidad").value;
	var selQuiebre = $("sel_quiebre").value;
	var selResponsable = $("sel_responsable").value;
	var selMotivo = $("sel_motivo").value;
	var pickeador = $("pickeador").value;
	var comentarioBoc = $("comentario_boc").value;
	var precioRetirar = $("precio_retirar").value;
	
	var idCaso = $("id_caso").value;
	var idProducto = $("id_producto").value;

	if (ppUnidad == 0) {
		ppUnidad = "";
	}
	if (psUnidad == 0) {
		psUnidad = "";
	}
	if (tipo == 'R' || tipo == 'P') {
		if (precioRetirar != '') {
			precio = precioRetirar;
		}
	} else if (tipo == 'E') {
		if (precioEnviar != '') {
			precio = precioEnviar;
		}
	}
	params = { 	'idCaso':idCaso, 'tipo':tipo, 'ppCantidad':ppCantidad, 'ppUnidad':ppUnidad, 'ppDescripcion':ppDescripcion, 'comentarioBoc':comentarioBoc,
					'quiebre':selQuiebre, 'responsable':selResponsable, 'pickeador':pickeador, 'idProducto':idProducto,
					'psDescripcion':psDescripcion, 'psCantidad':psCantidad, 'psUnidad':psUnidad, 'comentarioBol':comentarioBol,
					'precio':precio, 'motivo':selMotivo};
					
	var myAjax = new Ajax.Updater(
									  'prod_'+idProducto,
									  'AgregaModProductoParaCaso', 
									  	{
											'method': 'post',
											'parameters': params,
											'onSuccess': ocultarVentanas,
											'onFailure': function(REQUEST) {
												$("msj_add_prod").innerHTML = "<font color='red'><b>El producto no pudo ser modificado.</b></font>";
											}
										}
									);

}
//FIN ACTUALIZAR UN PRODUCTO


//INICIO FUNCIONES GLOBALES
function printValor(valor) {
	if (valor != '-') {
		return valor;	
	} 
	return "";
}
function printValorCombo(valor) {
	if (valor != '-') {
		return valor;	
	} 
	return "0";
}
function SoloNumeros(oObjeto) {
	oObjeto.value = oObjeto.value.replace(/^\s*/, "").replace(/\s*$/,"");
	if (oObjeto.value.substring(oObjeto.value.length-1,oObjeto.value.length) >= 0 ) {
	
	} else {
		oObjeto.value = oObjeto.value.substring(0,oObjeto.value.length-1);
	}	
}
function SoloNumerosConDecimal(oObjeto) {
	oObjeto.value = oObjeto.value.replace(/^\s*/, "").replace(/\s*$/,"");
	if (oObjeto.value.substring(oObjeto.value.length-1,oObjeto.value.length) >= 0 ) {
	} else {
		var tmp = oObjeto.value.substring(oObjeto.value.length-1,oObjeto.value.length);
		if((oObjeto.value.length > 1) && (tmp == ',')) {
			if (tmp == ',') {
				var array = oObjeto.value.split(',');
				if (array.length > 2) {
					oObjeto.value = oObjeto.value.substring(0,oObjeto.value.length-1);
				}
			}
		} else {
			oObjeto.value = oObjeto.value.substring(0,oObjeto.value.length-1);
		}
	}
}
function formateaCantidad(cantidad) {
	var tmp = cantidad.substring(cantidad.length-1,cantidad.length);
	if (tmp == ',') {
		cantidad = cantidad.substring(0,cantidad.length-1);
	}
	return cantidad;
}
var globalCallbacks = {
                onCreate: function() {
						$("tabla_loading").style.top = document.documentElement.scrollTop + 'px';
                        $("loading").show();
                },
                onComplete: function() {
                        if(Ajax.activeRequestCount == 0){
                                $("loading").hide();
                        }
                }
        };
Ajax.Responders.register( globalCallbacks );
//FINAL FUNCIONES GLOBALES