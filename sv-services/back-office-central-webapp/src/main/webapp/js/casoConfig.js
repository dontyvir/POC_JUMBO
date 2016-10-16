var params = {};

function ventanaObjeto(accion,descripcion,puntaje,id_objeto,tipo,activado) {
	limpiarMsjAndWin();
	
	win = new Window('window_id', {title: "&nbsp;&nbsp; "+accion});
	
	var filaPuntaje = "";
	if (tipo == 'Q') {
		filaPuntaje =	"<tr height='20' valign='middle'>"+
						"	<td><h5>&nbsp;&nbsp; Puntaje</h5></td>"+
						"	<td><h5>:</h5></td>"+
						"	<td><h5><input type='text' id='puntaje_popup' name='puntaje_popup' value='"+puntaje+"' onKeyPress='SoloNumeros(this)' onKeyUp='SoloNumeros(this)' style='width:170px'/></h5></td>"+
						"</tr>";
	}
	
	win.getContent().innerHTML = "<h5>Ingrese los datos solicitados:</h5><br>"+
								 "<table width='100%' border='0' align='center' cellpadding='0' cellspacing='0'>"+
								 "	<tr height='20' valign='middle'>"+
								 "		<td width='25%'><h5>&nbsp;&nbsp; Descripción</h5></td>"+
								 "		<td width='5%'><h5>:</h5></td>"+
								 "		<td width='70%'><h5>"+
								 "			<input type='text' id='descripcion_popup' name='descripcion_popup' value='"+descripcion+"' style='width:230px' maxlength='64'/></h5>"+
								 "		</td>"+
								 "	</tr>"+
								 filaPuntaje+
								 "	<tr height='20' valign='middle'>"+
								 "		<td><h5>&nbsp;&nbsp; Activado</h5></td>"+
								 "		<td><h5>:</h5></td>"+
								 "		<td><h5>"+
								 "			<input type='checkbox' id='checkbox_activado' name='checkbox_activado' value='checkbox'></h5>"+
								 "		</td>"+
								 "	</tr>"+
								 "	<tr height='20' valign='middle'>"+
								 "		<td><input type='hidden' id='tipo_obj' name='tipo_obj' value='"+tipo+"'/></td>"+
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
	
	if (activado == '1') {
		$("checkbox_activado").checked = true;
	} else {
		$("checkbox_activado").checked = false;
	}
	$("descripcion_popup").focus();
}

//-- INI Guardar Nuevo Objeto -----------------------------------------------------------------------------------
function guardarObj() {	
	var idObjeto = $("id_objeto").value;
	var tipo	 = $("tipo_obj").value;
	var descObj	 = $("descripcion_popup").value;
	var puntaje	 = "";
	var divname  = "";
	var objeto 	 = "";
	var activado = "";
	
	if (Trim(descObj) == '') {
		alert("Debe ingresar la descripción.");
		$("descripcion_popup").focus();
		return;
	}
	if ($("tipo_obj").value == 'Q') {
		puntaje = $("puntaje_popup").value;
		divname = "qui_";
		objeto 	 = "El tipo de quiebre";
		if (Trim(puntaje) == '') {
			alert("Debe ingresar el puntaje del Quiebre.");
			$("puntaje_popup").focus();
			return;
		}
	} else if ($("tipo_obj").value == 'R') {
		divname = "res_";
		objeto 	 = "El responsable";
	} else if ($("tipo_obj").value == 'J') {
		divname = "jor_";
		objeto 	 = "La jornada";
	} else if ($("tipo_obj").value == 'M') {
		divname = "mot_";
		objeto 	 = "El motivo";
	}
	
	if ($("checkbox_activado").checked) {
		activado = "1";
	} else {
		activado = "0";
	}

	params = { 	'tipo':tipo, 'id_objeto':idObjeto, 'desc_objeto':descObj, 'puntaje':puntaje, 'activado':activado };
	if (idObjeto == 0) {
		var requestOptions = {
							'method': 'post',
							'parameters': params,
							'onSuccess': respuestaAddObjeto
							  };
		new Ajax.Request('AddModConfigCaso', requestOptions);
		
	} else {
		var myAjax = new Ajax.Updater(
									  divname+idObjeto,
									  'AddModConfigCaso', 
									  	{
											'method': 'post',
											'parameters': params,
											'onSuccess': function(REQUEST) {
												limpiarMsjAndWin();
												setTimeout(function() {
													$("mensaje2").innerHTML = objeto +" se guardó correctamente.";
												},100);
												
											},
											'onFailure': function(REQUEST) {
												$("msj_popup").innerHTML = objeto +" no se guardó correctamente.";
												
											}
										}
									);
	}	
}
function respuestaAddObjeto(REQUEST) {	
	if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
		$("msj_popup").innerHTML = "Error al guardar";
		return;
	}
	var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
	var idObjeto = REQUEST.responseXML.getElementsByTagName("id_objeto_new")[0].childNodes[0].nodeValue;
	if (mensaje != 'OK') {
		$("msj_popup").innerHTML = mensaje;
		return;
	} else {
		limpiarMsjAndWin();		
	}
	var descActivado = "";
	if (params.activado == '1') {
		descActivado = "Si";
	} else {
		descActivado = "No";
	}
	
	
	if (params.tipo == 'Q') {
		var filaQuiebre =	"<tr id='qui_"+idObjeto+"'>" +
								"<td align='left'>"+idObjeto+"</td>" +
								"<td align='left'>"+params.desc_objeto+"</td>" +
								"<td align='left'>"+params.puntaje+"</td>" +
								"<td align='center'>"+descActivado+"&nbsp;</td>" +
								"<td align='center' nowrap>"+
									"<a href=\"javascript:ventanaObjeto('Modificar Tipo de Quiebre', '" + params.desc_objeto + "', '" + params.puntaje + "', '" + idObjeto + "','Q','" + params.activado + "');\"><img src='img/editicon.gif' border='0' height='17' width='19' title='Editar Tipo de Quiebre'></a>&nbsp;"+
									"<a href=\"javascript:aEliminar('" + idObjeto + "','Q');\"><img src='img/trash.gif' border='0' height='16' width='16' title='Eliminar Tipo de Quiebre'></a>"+
								"</td>" +
							"</tr>";
		var tablaQuiebre = document.getElementById('tabla_quiebre');
		new Insertion.After(tablaQuiebre,filaQuiebre);
		cantQuiebres++;
		if (cantQuiebres > 0) {
			$("msj_quiebre").innerHTML = "";
		}
		$("mensaje2").innerHTML = "El tipo de quiebre se ha guardado exitosamente.";
		
	} else if (params.tipo == 'R') {
		var filaResponsable =	"<tr id='res_"+idObjeto+"'>" +
								"<td align='left'>"+idObjeto+"</td>" +
								"<td align='left'>"+params.desc_objeto+"</td>" +
								"<td align='center'>"+descActivado+"&nbsp;</td>" +
								"<td align='center' nowrap>"+
									"<a href=\"javascript:ventanaObjeto('Modificar Responsable', '" + params.desc_objeto + "', '0', '" + idObjeto + "','R','" + params.activado + "');\"><img src='img/editicon.gif' border='0' height='17' width='19' title='Editar Responsable'></a>&nbsp;"+
									"<a href=\"javascript:aEliminar('" + idObjeto + "','R');\"><img src='img/trash.gif' border='0' height='16' width='16' title='Eliminar Responsable'></a>"+
								"</td>" +
							"</tr>";
		var tablaResponsable = document.getElementById('tabla_responsable');
		new Insertion.After(tablaResponsable,filaResponsable);
		cantResponsables++;
		if (cantResponsables > 0) {
			$("msj_responsable").innerHTML = "";
		}
		$("mensaje2").innerHTML = "El responsable se ha guardado exitosamente.";
		
	} else if (params.tipo == 'M') {
		var filaMotivo =	"<tr id='mot_"+idObjeto+"'>" +
								"<td align='left'>"+idObjeto+"</td>" +
								"<td align='left'>"+params.desc_objeto+"</td>" +
								"<td align='center'>"+descActivado+"&nbsp;</td>" +
								"<td align='center' nowrap>"+
									"<a href=\"javascript:ventanaObjeto('Modificar Motivo', '" + params.desc_objeto + "', '0', '" + idObjeto + "','M','" + params.activado + "');\"><img src='img/editicon.gif' border='0' height='17' width='19' title='Editar Motivo'></a>&nbsp;"+
									"<a href=\"javascript:aEliminar('" + idObjeto + "','M');\"><img src='img/trash.gif' border='0' height='16' width='16' title='Eliminar Motivo'></a>"+
								"</td>" +
							"</tr>";
		var tablaMotivo = document.getElementById('tabla_motivo');
		new Insertion.After(tablaMotivo,filaMotivo);
		cantMotivos++;
		if (cantMotivos > 0) {
			$("msj_motivo").innerHTML = "";
		}
		$("mensaje2").innerHTML = "El motivo se ha guardado exitosamente.";
		
	} else if (params.tipo == 'J') {			
		var filaJornada =	"<tr id='jor_"+idObjeto+"'>" +
								"<td align='left'>"+idObjeto+"</td>" +
								"<td align='left'>"+params.desc_objeto+"</td>" +
								"<td align='center'>"+descActivado+"&nbsp;</td>" +
								"<td align='center' nowrap>"+
									"<a href=\"javascript:ventanaObjeto('Modificar Jornada', '" + params.desc_objeto + "', '0', '" + idObjeto + "','J','" + params.activado + "');\"><img src='img/editicon.gif' border='0' height='17' width='19' title='Editar Jornada'></a>&nbsp;"+
									"<a href=\"javascript:aEliminar('" + idObjeto + "','J');\"><img src='img/trash.gif' border='0' height='16' width='16' title='Eliminar Jornada'></a>"+
								"</td>" +
							"</tr>";
		var tablaJornada = document.getElementById('tabla_jornada');
		new Insertion.After(tablaJornada,filaJornada);
		cantJornadas++;
		if (cantJornadas > 0) {
			$("msj_jornada").innerHTML = "";
		}
		//Parche para engañar al explorador y evitar que la solapa se 'desforme'
		setTimeout(function() {
			$("mensaje2").innerHTML = "La jornada se ha guardado exitosamente.";
		},100);
	} 	
}
//-- FIN Guardar Nuevo Objeto -----------------------------------------------------------------------------------



//INICIO ELIMINAR UN OBJETO
function aEliminar(id_objeto, tipo_eliminado) {
	limpiarMsjAndWin();
	if(confirm("Está seguro de querer eliminar")) {
		
		var params1 = {'id_objeto':id_objeto,'tipo_eliminado':tipo_eliminado};
		
		var requestOptions = {
								'method': 'post',
								'parameters': params1,
								'onSuccess': function (REQUEST) {
									
									if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
										if (tipo_eliminado == 'Q') {
											$("mensaje1").innerHTML = "El tipo de quiebre no pudo ser eliminado.";
										} else if (tipo_eliminado == 'R') {
											$("mensaje1").innerHTML = "El responsable no pudo ser eliminado.";
										} else if (tipo_eliminado == 'J') {
											$("mensaje1").innerHTML = "La jornada no pudo ser eliminada.";
										} else if (tipo_eliminado == 'M') {
											$("mensaje1").innerHTML = "El motivo no pudo ser eliminado.";
										}
										return;
									}	
									var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
									if (mensaje != 'OK') {
										$("mensaje1").innerHTML = mensaje;
										return;
									}
									var row_eliminar;									
									if (tipo_eliminado == 'Q') {
										row_eliminar = eval("$('qui_"+id_objeto+"')");
										if (row_eliminar != null) {
											row_eliminar.remove();
											cantQuiebres--;
										}
										if (cantQuiebres <= 0) {
											$("msj_quiebre").innerHTML = "No existen tipos de quiebre.";
										}
										$("mensaje2").innerHTML = "El tipo de quiebre ha sido eliminado.";
										
									} else if (tipo_eliminado == 'R') {
										row_eliminar = eval("$('res_"+id_objeto+"')");
										if (row_eliminar != null) {
											row_eliminar.remove();
											cantResponsables--;
										}
										if (cantResponsables <= 0) {
											$("msj_responsable").innerHTML = "No existen responsables.";
										}
										$("mensaje2").innerHTML = "El responsable ha sido eliminado.";
										
									} else if (tipo_eliminado == 'M') {
										row_eliminar = eval("$('mot_"+id_objeto+"')");
										if (row_eliminar != null) {
											row_eliminar.remove();
											cantMotivos--;
										}
										if (cantMotivos <= 0) {
											$("msj_motivo").innerHTML = "No existen motivos.";
										}
										$("mensaje2").innerHTML = "El motivo ha sido eliminado.";
										
									} else if (tipo_eliminado == 'J') {
										row_eliminar = eval("$('jor_"+id_objeto+"')");
										if (row_eliminar != null) {
											row_eliminar.remove();
											cantJornadas--;
										}
										if (cantJornadas <= 0) {
											$("msj_jornada").innerHTML = "No existen jornadas.";
										}
										//Parche para engañar al explorador y evitar que la solapa se 'desforme'
										setTimeout(function() {
											$("mensaje2").innerHTML = "La jornada ha sido eliminada.";
										},100);
										
									}
									
								}
							};
		new Ajax.Request('DelDatoConfigDeCaso', requestOptions);
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
	$("mensaje1").innerHTML = "";
	$("mensaje2").innerHTML = "";
	
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