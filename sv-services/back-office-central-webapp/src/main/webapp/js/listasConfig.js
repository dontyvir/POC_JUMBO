var params = {};

function ventanaObjeto(accion,descripcion,tipo_grupo_lista,id_objeto,activado) {
	limpiarMsjAndWin();
	
	win = new Window('window_id', {title: "&nbsp;&nbsp; "+accion, minWidth:400, width:400, minHeight:260, height:260});
	
	win.getContent().innerHTML = "<h5>Ingrese los datos solicitados:</h5><br>"+
								 "<table width='100%' border='0' align='center' cellpadding='0' cellspacing='0'>"+
								 "	<tr height='20' valign='middle'>"+
								 "		<td width='25%'><h5>&nbsp;&nbsp; Nombre</h5></td>"+
								 "		<td width='5%'><h5>:</h5></td>"+
								 "		<td width='70%'><h5>"+
								 "			<input type='text' id='descripcion_popup' name='descripcion_popup' value='"+descripcion+"' style='width:230px' maxlength='250'/></h5>"+
								 "		</td>"+
								 "	</tr>"+
								 "	<tr height='20' valign='middle'>"+
								 "		<td><h5>&nbsp;&nbsp; Tipo</h5></td>"+
								 "		<td><h5>:</h5></td>"+
								 "		<td><h5>"+
								 "			<select name=\"tipo_grupo_lista\" id=\"tipo_grupo_lista\">"+select_tipo_grupo+"</select></h5>"+
								 "		</td>"+
								 "	</tr>"+
								 "	<tr height='20' valign='middle'>"+
								 "		<td><h5>&nbsp;&nbsp; Activado</h5></td>"+
								 "		<td><h5>:</h5></td>"+
								 "		<td><h5>"+
								 "			<input type='checkbox' id='checkbox_activado' name='checkbox_activado' value='checkbox'></h5>"+
								 "		</td>"+
								 "	</tr>"+
								 "	<tr height='20' valign='middle'>"+
								 "		<td></td>"+
								 "		<td><input type='hidden' id='id_objeto' name='id_objeto' value='"+id_objeto+"'/></td>"+
								 "		<td>"+
								 "			<input type='button' value='Aceptar' onclick='guardarObj()'/>&nbsp;"+
								 "			<input type='button' value='Cerrar' onclick='Windows.close(\"window_id\", event)'/>"+
								 "		</td>"+
								 "	</tr>"+
								 "	<tr height='20' valign='middle'>"+
								 "		<td align='center' colspan='4'><b><span id='msj_popup' style='color:#FF0000'></span></b></td>"+
								 "	</tr>"+
								 "</table>"; 
	win.setDestroyOnClose(); 
	win.showCenter(); 
	
	if (activado == '1' || activado == 'S') {
		$("checkbox_activado").checked = true;
	} else {
		$("checkbox_activado").checked = false;
	}
	$("descripcion_popup").focus();
	
	$("tipo_grupo_lista").value = tipo_grupo_lista;
}

//-- INI Guardar Nuevo Objeto -----------------------------------------------------------------------------------
function guardarObj() {
	var idObjeto = $("id_objeto").value;
	var tipo	 = $("tipo_grupo_lista").value;
	var descObj	 = $("descripcion_popup").value;
	var activado = "";
	
	if (Trim(descObj) == '') {
		alert("Debe ingresar el nombre.");
		$("descripcion_popup").focus();
		return;
	}
	
	if ($("checkbox_activado").checked) {
		activado = "1";
	} else {
		activado = "0";
	}

	var params = { 'tipo':tipo, 'id_objeto':idObjeto, 'desc_objeto':descObj, 'activado':activado };
	
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
				$("msj_popup").innerHTML = "Error al guardar";
				return;
			}
			var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
			if ( mensaje != 'OK' ) {
				$("msj_popup").innerHTML = mensaje;
				return;
			}
			if ( idObjeto == 0 ) {
				window.location.href="ViewConfigListasEspeciales?mensaje=El grupo de lista se ha guardado exitosamente";
			} else {
				window.location.href="ViewConfigListasEspeciales?mensaje=El grupo de lista se ha modificado exitosamente";
			}
		}
	};
	new Ajax.Request('AddModConfigListaGrupo', requestOptions);
}
//-- FIN Guardar Nuevo Objeto -----------------------------------------------------------------------------------

//INICIO ELIMINAR UN OBJETO
function aEliminar(id_objeto, tipo_eliminado) {
	limpiarMsjAndWin();
	if(confirm("Está seguro de querer eliminar")) {		
		var params1 = {'id_objeto':id_objeto, 'accion':'DEL'};		
		var requestOptions = {
			'method': 'post',
			'parameters': params1,
			'onSuccess': function (REQUEST) {									
				if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
					$("mensaje1").innerHTML = "El grupo no pudo ser eliminado.";
					return;
				}	
				var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
				if (mensaje != 'OK') {
					$("mensaje1").innerHTML = mensaje;
					return;
				}
				window.location.href="ViewConfigListasEspeciales?mensaje=El grupo de lista ha sido eliminado";
			}
		};
		new Ajax.Request('AddModConfigListaGrupo', requestOptions);
	}	
}
function eliminarLista(idLista, idGrupo) {
	if (confirm("Está seguro de querer eliminar")) {		
		var params1 = {'id_lista':idLista, 'id_grupo':idGrupo, 'accion':'DEL'};		
		var requestOptions = {
			'method': 'post',
			'parameters': params1,
			'onSuccess': function (REQUEST) {
				if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
					$("mensaje1").innerHTML = "El grupo no pudo ser eliminado.";
					return;
				}	
				var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
				if (mensaje != 'OK') {
					$("mensaje1").innerHTML = mensaje;
					return;
				}
				window.location.href="ViewConfigListasEspeciales?mensaje=La lista ha sido eliminada";
			}
		};
		new Ajax.Request('AddModListasEspeciales', requestOptions);
	}	
}
//INICIO ELIMINAR UN PRODUCTO


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

function limpiarMsjAndWin() {	
	var popup = $("window_id");
	if (popup != null) {
		Windows.close("window_id", event);
		//win.hide();
	}	
}
function SoloNumeros(oObjeto) {
	oObjeto.value = oObjeto.value.replace(/^\s*/, "").replace(/\s*$/,"");
	if (oObjeto.value.substring(oObjeto.value.length-1,oObjeto.value.length) >= 0 ) {
	
	} else {
		oObjeto.value = oObjeto.value.substring(0,oObjeto.value.length-1);
	}	
}
function eliminarMarco(idMarco) {
	if ( confirm("Desea eliminar el marco?") ) {
		$('id_marco').value = idMarco;
		document.f1.submit();	
	}
}

var globalCallbacks = {
                onCreate: function(){
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