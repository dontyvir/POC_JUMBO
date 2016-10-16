var params = {};

function guardarCaso() {
	if ($("sel_estados").value == 0) {
		alert("Debe seleccionar el Estado.");
		$("sel_estados").focus();
		return;
	}
	if (Trim($("fecha_resolucion").value) == '') {
		alert("Debe ingresar la Fecha de Compromiso Solución.");
		$("fecha_resolucion").focus();
		return;
	}
	
	if (!comparafechas($("fecha_minima"),$("fecha_resolucion"))) {
		alert("La fecha seleccionada no es válida");
		$("fecha_resolucion").focus();
		return;
	}
	if ($("sel_jornada").value == 0) {
		alert("Debe seleccionar la Jornada de Compromiso Solución.");
		$("sel_jornada").focus();
		return;
	}
	if (Trim($("comentario_log").value) == '') {
		alert("Debe ingresar un Comentario.");
		$("comentario_log").focus();
		return;
	}
	
	if ($("sel_estados").value == '7') {	
		if ($("satisfaccion_cliente").value == '') {
			alert("Debe seleccionar la Conformidad del Cliente.");
			$("satisfaccion_cliente").focus();
			return;
		}
		if ($("fecha_entrega").value == '') {
			alert("Debe ingresar la Fecha de Entrega del Caso.");
			$("fecha_entrega").focus();
			return;
		}
		if ($("hora_entrega").value == '') {
			alert("Debe ingresar la Hora de Entrega del Caso.");
			$("hora_entrega").focus();
			return;
		}
		if (!comparafechas($("fecha_entrega"),$("fecha_actual"))) {
			alert("La fecha de Entrega del Caso, no puede ser mayor a la fecha actual.");
			$("fecha_entrega").focus();
			return;
		}
		if ($("fecha_entrega").value == $("fecha_actual").value) {
			if (!comparahoras($("hora_entrega"),$("hora_actual"))) {
				alert("La Hora de Entrega no puede ser mayor a la hora actual.");
				$("hora_entrega").focus();
				return;
			}
		}
		
	}
	$("caso_escalamiento").disabled = false;
	$("sel_estados").disabled = false;
	document.frm_add_edit_caso.submit();
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
	ocultarVentanas();
	$("id_producto").value = "0";  //Nos aseguramos de que sea un producto nuevo
	$("add_edit_prod").show();
	$("anula_producto").hide();
	$("salir_comentario").hide();	
	
	$("p_tipo_accion").disabled = false;	
	
	//Bloqueamos esto y cuando guardamos los habilitamos
	$("ps_descripcion").disabled = true;
	$("ps_cantidad").disabled = true;
	$("ps_unidad").disabled = true;
	$("comentario_bol").disabled = true;
	$("precio_enviar").disabled = true;
}

function anularCaso() {
	$("add_edit_prod").hide();
	$("anula_producto").show();
	$("salir_comentario").hide();
	$("comentario_anular").focus();
}

function vantanaComentario() {
	cambioEstado();
	$("add_edit_prod").hide();
	$("anula_producto").hide();
	$("salir_comentario").show();
	$("comentario_log").focus();
}

function cambioEstado() {
	if ($("sel_estados").value == '7') {
		$("fila_conformidad").show();
		$("fila_escalamiento").hide();
	} else {
		$("satisfaccion_cliente").value == "";
		$("fila_conformidad").hide();
		$("fila_escalamiento").show();
	}
}

function salir() {
	document.frm_add_edit_caso.action = "ViewMonCasos";
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
	
	if (valor == 'R' || valor == 'P') {
		$("tabla_precio_retirar").show();
		$("tabla_precio_enviar").hide();		
	} else if (valor == 'E') {
		$("tabla_precio_retirar").hide();
		$("tabla_precio_enviar").show();
	} else {
		$("tabla_precio_retirar").hide();
		$("tabla_precio_enviar").hide();		
	}
	if (valor == 'P') {
		$('txt_precio_enviar').innerHTML = "Valor por Unidad";
	} else {
		$('txt_precio_enviar').innerHTML = "Precio por Unidad";
	}
	
	if (valor == 'D') {
		$("tabla_cant_uni").hide();
		$("tabla_pickeador").hide();
	} else {
		$("tabla_cant_uni").show();
		$("tabla_pickeador").show();
	}
}



//INICIO ELIMINAR UN PRODUCTO
function eliminarProducto(id_producto, tipoEliminado,descProd) {
	ocultarVentanas();
	if(confirm("Está seguro de querer eliminar el producto")) {
		
		var idCaso = $("id_caso").value;
		var params1 = {'id_caso':idCaso,'id_producto':id_producto,'nombre_prod':descProd};
		
		var requestOptions = {
								'method': 'post',
								'parameters': params1,
								'onSuccess': function (REQUEST) {
									if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
										alert("El producto no pudo ser eliminado");
										return;
									}	
									var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
									if (mensaje != 'OK') {
										alert(mensaje);
										return;
									}
									var row_eliminar = eval("$('prod_"+id_producto+"')");
									if (row_eliminar != null) {
										row_eliminar.remove();
										if (tipoEliminado == 'R') {
											prodRetirar--;
											if (prodRetirar <= 0) {
												$("msj_prods_retirar").innerHTML = "No existen productos para retirar.";
											}
										} else if (tipoEliminado == 'E') {
											prodEnviar--;
											if (prodEnviar <= 0) {
												$("msj_prods_enviar").innerHTML  = "No existen productos para enviar.";
											}
										} else if (tipoEliminado == 'D') {
											prodDocs--;
											if (prodDocs <= 0) {
												$("msj_prods_docs").innerHTML 	 = "No existen documentos.";
											}
										}
									}
									
								}
							};
		new Ajax.Request('DelProductoDeCaso', requestOptions);
	}	
}
//INICIO ELIMINAR UN PRODUCTO


//INICIO MODIFICAR UN PRODUCTO
function traerInfoProducto(id_producto) {	
	ocultarVentanas();
	$("add_edit_prod").show();
	$("anula_producto").hide();
	$("salir_comentario").hide();
	
	$("msj_add_prod").innerHTML = "<font color='blue'><b>Buscando información...</b></font>";
	var requestOptions = {
					'method': 'post',
					'parameters': "idProducto=" + id_producto,
					'onSuccess': llenarFormModificar
					  };
	new Ajax.Request('DetalleProductoParaCaso', requestOptions);	
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

	//Si el tipo es Enviar tiene la posibilidad de agregar un producto solucion, de lo contrario sólo puede ingresar un comentario	
	$("ps_descripcion").value = printValor(ps_descripcion);
	$("ps_cantidad").value = printValor(ps_cantidad);
	$("ps_unidad").value = printValorCombo(ps_unidad);
	$("comentario_bol").value = printValor(comentario_bol);		
	
	$("precio_retirar").value = precio;
	$("precio_enviar").value = precio;
	
	//Bloqueamos esto y cuando guardamos los habilitamos
	$("ps_descripcion").disabled = true;
	$("ps_cantidad").disabled = true;
	$("ps_unidad").disabled = true;
	$("comentario_bol").disabled = true;
	$("precio_enviar").disabled = true;
	
	$("p_tipo_accion").disabled = true;
	
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
	var tipo = $("p_tipo_accion").value;
	var ppDescripcion = $("pp_descripcion").value;
	var ppCantidad = $("pp_cantidad").value;
	var ppUnidad = $("pp_unidad").value;
	var selQuiebre = $("sel_quiebre").value;
	var selMotivo = $("sel_motivo").value;
	var selResponsable = $("sel_responsable").value;
	var pickeador = $("pickeador").value;
	var comentarioBoc = $("comentario_boc").value;
	var idCaso = $("id_caso").value;
	var idProducto = $("id_producto").value;
	var precioRetirar = $("precio_retirar").value;
	
	var precio = 0;
	
	if (tipo == 0) {
		alert("Debe seleccionar el tipo");
		$("p_tipo_accion").focus();
		return;
	}
	if (Trim(ppDescripcion) == '') {
		alert("Debe ingresar la descripción.");
		$("pp_descripcion").focus();
		return;
	}
	if (tipo != 'D') {
		if (Trim(ppCantidad) == '') {
			alert("Debe ingresar la cantidad.");
			$("pp_cantidad").focus();
			return;
		}
	}
	if (selQuiebre == 0) {
		alert("Debe seleccionar el Tipo de Quiebre");
		$("sel_quiebre").focus();
		return;
	}
	if (selResponsable == 0) {
		alert("Debe seleccionar el Responsable");
		$("sel_responsable").focus();
		return;
	}
	if (tipo == 'R') {
		if (Trim(precioRetirar) == '') {
			alert("Debe ingresar el precio por unidad.");
			$("precio_retirar").focus();
			return;
		}
	}
	if (tipo == 'P') {
		if (Trim(precioRetirar) == '') {
			alert("Debe ingresar el valor por unidad.");
			$("precio_retirar").focus();
			return;
		}
	}	
	ppCantidad = formateaCantidad(ppCantidad);
	
	$("ps_descripcion").disabled = false;
	$("ps_cantidad").disabled = false;
	$("ps_unidad").disabled = false;
	$("precio_enviar").disabled = false;
	$("comentario_bol").disabled = false;
	$("p_tipo_accion").disabled = false;
	
	var psDescripcion = $("ps_descripcion").value;
	var psCantidad = $("ps_cantidad").value;
	var psUnidad = $("ps_unidad").value;
	var precioEnviar = $("precio_enviar").value;
	var comentarioBol = $("comentario_bol").value;
	
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
	params = {  'idCaso':idCaso, 'tipo':tipo, 'ppCantidad':ppCantidad, 'ppUnidad':ppUnidad, 'ppDescripcion':ppDescripcion, 'comentarioBoc':comentarioBoc,
				'quiebre':selQuiebre, 'responsable':selResponsable, 'pickeador':pickeador, 'idProducto':idProducto,
				'psDescripcion':psDescripcion, 'psCantidad':psCantidad, 'psUnidad':psUnidad, 'comentarioBol':comentarioBol,
				'precio':precio, 'motivo':selMotivo};
					
	if (idProducto == 0) {
		var requestOptions = {
							'method': 'post',
							'parameters': params,
							'onSuccess': respuestaAddProducto
							  };
		new Ajax.Request('AddModProductoParaCaso', requestOptions);
		
	} else { 
		var myAjax = new Ajax.Updater(
									  'prod_'+idProducto,
									  'AddModProductoParaCaso', 
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
}
function respuestaAddProducto(REQUEST) {
	if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
		$("msj_add_prod").innerHTML = "<font color='red'><b>El producto no pudo ser guardado.</b></font>";
		return;
	}
	var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
	var idProducto = REQUEST.responseXML.getElementsByTagName("id_producto_new")[0].childNodes[0].nodeValue;
	var quiebre = REQUEST.responseXML.getElementsByTagName("quiebre")[0].childNodes[0].nodeValue;
	var responsable = REQUEST.responseXML.getElementsByTagName("responsable")[0].childNodes[0].nodeValue;
	var precio = REQUEST.responseXML.getElementsByTagName("precio")[0].childNodes[0].nodeValue;
	var pickeador = REQUEST.responseXML.getElementsByTagName("pickeador")[0].childNodes[0].nodeValue;
	var producto_desc_log = REQUEST.responseXML.getElementsByTagName("producto_desc_log")[0].childNodes[0].nodeValue;
	if (pickeador == '--') {
		pickeador = "";
	}
	if (mensaje != 'OK') {
		$("msj_add_prod").innerHTML = "<font color='red'><b>"+mensaje+"</b></font>";
		return;
	} else {	
		ocultarVentanas();
	}
	
	if (params.tipo == 'E') {
		var filaEnviar =	'<tr id="prod_'+idProducto+'">' +
								'<td align="left">'+params.ppDescripcion+' / '+params.ppCantidad+' / '+params.ppUnidad+'</td>' +
								'<td align="left">'+params.psDescripcion+' / '+params.psCantidad+' / '+params.psUnidad+'</td>' +
								'<td align="left">'+quiebre+'</td>' +
								'<td align="left">'+responsable+'</td>' +
								'<td align="left">'+pickeador+'&nbsp;</td>' +
								'<td align="left">'+params.comentarioBoc+'&nbsp;</td>' +
								'<td align="left">'+params.comentarioBol+'&nbsp;</td>' +
								'<td align="right">'+precio+'&nbsp;</td>' +
								'<td align="center" nowrap>' +
								  '<a href="javascript:traerInfoProducto('+idProducto+');"><img src="img/editicon.gif" border="0" height="17" width="19" title="Editar Producto a Enviar"></a>&nbsp;'+
								  '<a href="javascript:eliminarProducto('+idProducto+',\'E\',\''+producto_desc_log+'\');"><img src="img/trash.gif" border="0" height="16" width="16" title="Eliminar Producto a Enviar"></a>&nbsp;'+
								  '<a href="javascript:cambiarEnvioDinero('+idProducto+');"><img src="img/envio_dinero.gif" border="0" height="16" width="16" title="Cambiar a \'Envío de Dinero\'"></a>'+
							    '</td>' +
							'</tr>';
		var tablaEnviar = document.getElementById('tabla_enviar');
		new Insertion.After(tablaEnviar,filaEnviar);
		prodEnviar++;
		if (prodEnviar > 0) {
			$("msj_prods_enviar").innerHTML = "";
		}
		
	} else if (params.tipo == 'R') {
		var filaRetirar =	'<tr id="prod_'+idProducto+'">' +
								'<td align="left">'+params.ppDescripcion+' / '+params.ppCantidad+' / '+params.ppUnidad+'</td>' +
								'<td align="left">'+quiebre+'</td>' +
								'<td align="left">'+responsable+'</td>' +
								'<td align="left">'+pickeador+'</td>' +
								'<td align="left">'+params.comentarioBoc+'&nbsp;</td>' +
								'<td align="left">'+params.comentarioBol+'&nbsp;</td>' +
								'<td align="right">'+precio+'&nbsp;</td>' +
								'<td align="center" nowrap><a href="javascript:traerInfoProducto('+idProducto+');"><img src="img/editicon.gif" border="0" height="17" width="19" title="Editar Producto a Retirar"></a>&nbsp;<a href="javascript:eliminarProducto('+idProducto+',\'R\',\''+producto_desc_log+'\');"><img src="img/trash.gif" border="0" height="16" width="16" title="Eliminar Producto a Retirar"></a></td>' +
							'</tr>';
		var tablaRetirar = document.getElementById('tabla_retirar');
		new Insertion.After(tablaRetirar,filaRetirar);	
		prodRetirar++;
		if (prodRetirar > 0) {
			$("msj_prods_retirar").innerHTML = "";
		}
		
	} else if (params.tipo == 'P') {
		var filaEnvioDinero =	'<tr id="dinero_'+idProducto+'">' +
									'<td align="left">'+params.ppDescripcion+' / '+params.ppCantidad+' / '+params.ppUnidad+'</td>' +
									'<td align="left">'+quiebre+'</td>' +
									'<td align="left">'+responsable+'</td>' +
									//'<td align="left">'+pickeador+'</td>' +
									'<td align="left">'+params.comentarioBoc+'&nbsp;</td>' +
									'<td align="left">'+params.comentarioBol+'&nbsp;</td>' +
									'<td align="right">'+precio+'&nbsp;</td>' +
									'<td align="center" nowrap><a href="javascript:traerInfoProducto('+idProducto+');"><img src="img/editicon.gif" border="0" height="17" width="19" title="Editar Envío de Dinero"></a>&nbsp;<a href="javascript:eliminarProducto('+idProducto+',\'P\',\''+producto_desc_log+'\');"><img src="img/trash.gif" border="0" height="16" width="16" title="Eliminar Envío de Dinero"></a></td>' +
								'</tr>';
		var tablaEnvioDinero = document.getElementById('tabla_envio_dinero');
		new Insertion.After(tablaEnvioDinero,filaEnvioDinero);	
		prodEnviarDinero++;
		if (prodEnviarDinero > 0) {
			$("msj_envio_dinero").innerHTML = "";
		}
		
	} else if (params.tipo == 'D') {			
		var filaDocs =	'<tr id="prod_'+idProducto+'">' +
							'<td align="left">'+params.ppDescripcion+'</td>' +
							'<td align="left">'+quiebre+'</td>' +
							'<td align="left">'+responsable+'</td>' +
							'<td align="left">'+params.comentarioBoc+'</td>' +
							'<td align="left">'+params.comentarioBol+'</td>' +
							'<td align="center" nowrap><a href="javascript:traerInfoProducto('+idProducto+');"><img src="img/editicon.gif" border="0" height="17" width="19" title="Editar Documento"></a>&nbsp;<a href="javascript:eliminarProducto('+idProducto+',\'D\',\''+producto_desc_log+'\');"><img src="img/trash.gif" border="0" height="16" width="16" title="Eliminar Documento"></a></td>' +
						'</tr>';
		var tablaDocs = document.getElementById('tabla_documentos');
		new Insertion.After(tablaDocs,filaDocs);	
		prodDocs++;	
		if (prodDocs > 0) {
			$("msj_prods_docs").innerHTML = "";
		}
		
	}
	
	
}
//FIN ACTUALIZAR UN PRODUCTO


//INICIO FUNCIONES GLOBALES
function comparafechas(campo1,campo2) {
  var fecha1 = campo1.value.substring(6,10) + campo1.value.substring(3,5) + campo1.value.substring(0,2);
  var fecha2 = campo2.value.substring(6,10) + campo2.value.substring(3,5) + campo2.value.substring(0,2);
  if (parseInt(fecha2,10) >= parseInt(fecha1,10)) {
    return true;
  } else {
    return false;
  }
}

function comparahoras(ingresado, maximo) {
	var h1 = ingresado.value.substring(0,2);
	var m1 = ingresado.value.substring(3,5);
	var h2 = maximo.value.substring(0,2);  
	var m2 = maximo.value.substring(3,5);
	
	if ( ( parseInt(h2,10) + 1 ) >= parseInt(h1,10)) {
		//alert("hora actual es mayor o igual a hora ingresada...");
		//alert("si son iguales las horas, vemos que los minutos ingresados no sean mayores al actual");
		//alert("hora actual: " + (parseInt(h2,10) + 1) + " hora ingresada: " + parseInt(h1,10) );
		if ( ( parseInt(h2,10) + 1 ) == parseInt(h1,10)) {
			//alert("son iguales las horas... minutos actual: " + parseInt(m2,10) + " minutos ingresados: " + parseInt(m1,10) );		
			if (parseInt(m2,10) >= parseInt(m1,10)) {
				return true;
			} else {
				return false;
			}			
		}
		return true;
	} else {
		//alert("hora ingresada es mayor a la actual... tudo mal.");
		return false;
	}
}

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

//--- BUSCAR PRODUCTOS ---
function viewBuscarProductos() {
	cerrarVentana();	
	win = new Window('window_id', {title: "Buscar Productos", minWidth:500, width:500, minHeight:420, height:420});
	win.getContent().innerHTML ="<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> " +
								"  <tr> " +
								"    <td width=\"10%\" height=\"35\" align=\"right\">OP :&nbsp;</td>" +
								"    <td width=\"20%\"><input type=\"text\" name=\"id_op\" id=\"id_op\" style=\"width:100px;\" onKeyPress=\"SoloNumeros(this)\" onKeyUp=\"SoloNumeros(this)\"/></td>" +
								"    <td width=\"5%\" align=\"right\">&nbsp;</td>" +
								"    <td width=\"35%\"><input type=\"button\" name=\"Submit\" value=\"Buscar\" onClick=\"javascript:buscarProductos();\"></td>" +
								"    <td width=\"30%\" align=\"center\">&nbsp;</td>" +
								"  </tr>" +
								"</table>" +
								"<br>" +
								"<table width=\"98%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">" +
								"	<tr>" +
								"		<td width=\"6\" height=\"28\" align=\"left\"><img src=\"img/tabla/tb_izq.gif\" width=\"4\" height=\"28\" alt=\"\"/></td>" +
								"		<td width=\"100%\" height=\"28\" style=\"background-image: url(img/tabla/fn_tb.gif)\" class=\"tit_listas\">:: Listado de Productos</td>" +
								"		<td width=\"4\" height=\"28\" align=\"right\"><img src=\"img/tabla/tb_der.gif\" width=\"4\" height=\"28\" alt=\"\"/></td>" +
								"	</tr>" +
								"	<tr>" +
								"		<td colspan=\"3\" class=\"td_lista_de_productos\" align=\"center\"><div id=\"resultado_busqueda_productos\"></div></td>" +
								"	</tr>" +
								"	<tr>" +
								"		<td width=\"6\" height=\"7\"><img src=\"img/tabla/tb_izq_ab.gif\" width=\"4\" height=\"7\" alt=\"\"/></td>" +
								"		<td height=\"7\" style=\"background: url(img/tabla/fn_tb_ab.gif);\"></td>" +
								"		<td width=\"54\" height=\"7\"><img src=\"img/tabla/tb_der_ab.gif\" width=\"4\" height=\"7\" alt=\"\"/></td>" +
								"	</tr>" +
								"</table>" +
								"<center><br><input type=\"button\" name=\"cerrar\" value=\"Cerrar Ventana\" onClick=\"javascript:cerrarVentana();\"></center>";
	win.setDestroyOnClose(); 
	win.showCenter(); 
	
	$('resultado_busqueda_productos').style.height = '300px';
	$('resultado_busqueda_productos').style.width = '99%';
	$('resultado_busqueda_productos').style.overflow = 'auto';	
}

function buscarProductos() {
	if ( $('id_op').value == "" ) {
		alert("Debes ingresar el N° de OP");
		$('id_op').focus();
		return;
	}
	var params			= {'id_op':$('id_op').value};
	var requestOptions 	= {
				'method': 'post',
				'parameters': params,
				'onSuccess': function (REQUEST) {
					$('resultado_busqueda_productos').innerHTML = REQUEST.responseText;									
				}
			};
	new Ajax.Request('ViewProductosPorOpRonda', requestOptions);
}

function seleccionaFila(fila) {
	$('pp_descripcion').value = $('desc_' + fila).value;
	$('precio_retirar').value = $('precio_' + fila).value;
}
function overFila(fila) {
	$('fila_' + fila).style.background = "#CCCCCC";
}
function outFila(fila) {
	$('fila_' + fila).style.background = "#FFFFFF";
}

//--- Cambiar a envio dinero ---
function cambiarEnvioDinero(idprod) {
	if (confirm("Está seguro de cambiar a 'Envío de Dinero'?")) {
		var params			= {'id_producto':idprod, 'id_caso':$('id_caso').value, 'producto':$('pp_descripcion').value};
		var requestOptions 	= {
					'method': 'post',
					'parameters': params,
					'onSuccess': function (REQUEST) {
						if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
							$("msj_add_prod").innerHTML = "<font color='red'><b>El producto no pudo ser cambiado.</b></font>";
							return;
						}
						var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
						if (mensaje == "OK") {
							window.location.reload();							
						} else {
							$("msj_add_prod").innerHTML = mensaje;
						}									
					}
				};
		new Ajax.Request('CambiaEnvioDineroEnCaso', requestOptions);
	}
}

//--- HORA ---
function ventanaHora() {
	cerrarVentana();	
	win = new Window('window_id', {title: "HORA", minWidth:150, width:150, minHeight:100, height:100});
	win.getContent().innerHTML = "<table width=\"150\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">" +
								 "	<tr>" +
								 "     <td colspan=\"3\" align=\"center\"  height=\"35\">Seleccione la Hora </td>" +
								 "  </tr>" +
								 "  <tr>" +
								 "    <td colspan=\"3\" height=\"4\"></td>" +
								 "  </tr>" +
								 "  <tr>" +
								 "    <td align=\"right\">" +
								 "	  <select name=\"lstHora\" id=\"lstHora\">" +
								 "       <option value=\"00\">00</option><option value=\"01\">01</option><option value=\"02\">02</option><option value=\"03\">03</option><option value=\"04\">04</option><option value=\"05\">05</option>" +
								 "       <option value=\"06\">06</option><option value=\"07\">07</option><option value=\"08\">08</option><option value=\"09\">09</option><option value=\"10\">10</option><option value=\"11\">11</option>" +
								 "       <option value=\"12\">12</option><option value=\"13\">13</option><option value=\"14\">14</option><option value=\"15\">15</option><option value=\"16\">16</option><option value=\"17\">17</option>" +
								 "       <option value=\"18\">18</option><option value=\"19\">19</option><option value=\"20\">20</option><option value=\"21\">21</option><option value=\"22\">22</option><option value=\"23\">23</option>" +
								 "    </select>" +
								 "	  </td>" +
								 "    <td align=\"center\">:</td>" +
								 "    <td>" +
								 "    <select name=\"lstMinutos\" id=\"lstMinutos\">" +
								 "       <option value=\"00\">00</option><option value=\"01\">01</option><option value=\"02\">02</option><option value=\"03\">03</option><option value=\"04\">04</option><option value=\"05\">05</option>" +
								 "       <option value=\"06\">06</option><option value=\"07\">07</option><option value=\"08\">08</option><option value=\"09\">09</option><option value=\"10\">10</option><option value=\"11\">11</option>" +
								 "       <option value=\"12\">12</option><option value=\"13\">13</option><option value=\"14\">14</option><option value=\"15\">15</option><option value=\"16\">16</option><option value=\"17\">17</option>" +
								 "       <option value=\"18\">18</option><option value=\"19\">19</option><option value=\"20\">20</option><option value=\"21\">21</option><option value=\"22\">22</option><option value=\"23\">23</option>" +
								 "       <option value=\"24\">24</option><option value=\"25\">25</option><option value=\"26\">26</option><option value=\"27\">27</option><option value=\"28\">28</option><option value=\"29\">29</option>" +
								 "       <option value=\"30\">30</option><option value=\"31\">31</option><option value=\"32\">32</option><option value=\"33\">33</option><option value=\"34\">34</option><option value=\"35\">35</option>" +
								 "		 <option value=\"36\">36</option><option value=\"37\">37</option><option value=\"38\">38</option><option value=\"39\">39</option><option value=\"40\">40</option><option value=\"41\">41</option>" +
								 "		 <option value=\"42\">42</option><option value=\"43\">43</option><option value=\"44\">44</option><option value=\"45\">45</option><option value=\"46\">46</option><option value=\"47\">47</option>" +
								 "       <option value=\"48\">48</option><option value=\"49\">49</option><option value=\"50\">50</option><option value=\"51\">51</option><option value=\"52\">52</option><option value=\"53\">53</option>" +
								 "		 <option value=\"54\">54</option><option value=\"55\">55</option><option value=\"56\">56</option><option value=\"57\">57</option><option value=\"58\">58</option><option value=\"59\">59</option>" +
								 "    </select>" +
								 "    </td>" +
								 "  </tr>" +
								 "  <tr>" +
								 "  <td colspan=\"3\" align=\"center\" height=\"4\"></td>" +
								 "  </tr>" +
								 "  <tr>" +
								 "    <td colspan=\"3\" align=\"center\"><input name=\"btnAceptar\" type=\"button\" id=\"btnAceptar2\" value=\"Aceptar\"  onClick=\"colocarHora()\"></td>" +
								 "  </tr>" +
								 "</table>"; 
	win.setDestroyOnClose(); 
	win.showCenter(); 
}
function colocarHora() {
	$("hora_entrega").value = $("lstHora").value + ":" + $("lstMinutos").value;;
	$("hora_entrega").select();
	$("hora_entrega").focus();
	cerrarVentana();
}
function cerrarVentana() {
	var popup = $("window_id");
	if (popup != null) {
		Windows.close("window_id", event);
		//win.hide();
	}	
}
/*
function openNewWindow(url,h,w,UrlVar,NameControlOValorVar, NameWindow) {
	if (UrlVar != '') {
		url=url + UrlVar + NameControlOValorVar;
	}
	
	var l = (screen.width - w) / 2;
	var t = (screen.height - h) / 2;
	var Wnd;
	//
	Wnd=open(url,NameWindow, "top=" + t + ",left=" + l + ", width=" + w + ", height=" + h + " , status=no,toolbar=no,scrollbars=no,location=no,statusbar=no,menubar=no,resizable=no,copyhistory=no,directories=no");
	//if (parseInt(navigator.appVersion) >= 4) win.window.focus();
}
function CheckTime(str) {
	hora = str.value;
	if (hora == '') {
		return;
	}
	if (hora.length > 8) {
		alert("Introdujo una cadena mayor a 8 caracteres");
		return;
	}
	if (hora.length != 8) {
		alert("Introducir HH:MM:SS");
		return;
	}
	a = hora.charAt(0); //<=2
	b = hora.charAt(1); //<4
	c = hora.charAt(2); //:
	d = hora.charAt(3); //<=5
	e = hora.charAt(5); //:
	f = hora.charAt(6); //<=5
	if ((a==2 && b>3) || (a>2)) {
		alert("El valor que introdujo en la Hora no corresponde, introduzca un digito entre 00 y 23");
		return;
	}
	if (d > 5) {
		alert("El valor que introdujo en los minutos no corresponde, introduzca un digito entre 00 y 59");
		return;
	}
	if (f > 5) {
		alert("El valor que introdujo en los segundos no corresponde");
		return;
	}
	if (c != ':' || e != ':') {
		alert("Introduzca el caracter ':' para separar la hora, los minutos y los segundos");
		return;
	}
}
*/
//--- FIN HORA  ----

function hideComentarioCall() {
  $('ver_comentario_call').show();
  $('comentario_call_center').hide();
}

function showComentarioCall() {
  $('ver_comentario_call').hide();
  $('comentario_call_center').show();
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