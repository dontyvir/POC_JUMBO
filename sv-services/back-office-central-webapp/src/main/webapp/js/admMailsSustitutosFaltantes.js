var params = {};

function ventanaObjeto(accion,nombres,apellidos,mail,id_mail,activado) {
	limpiarMsjAndWin();
	
	win = new Window('window_id', {title: "&nbsp;&nbsp; "+accion, height: 230});
	
	win.getContent().innerHTML= "<br>" +
								"<table width='100%' border='0' align='center' cellpadding='0' cellspacing='0'>" +
								 "<tr height='20' valign='middle'>" +
								  "<td width='25%'><h5>&nbsp;&nbsp; Nombres</h5></td>" +
								  "<td width='5%'><h5>:</h5></td>" +
								  "<td width='70%'>" +
								   "<h5>" +
								    "<input type='text' id='nombres' name='nombres' value='"+nombres+"' style='width:230px' maxlength='64'/>" +
								   "</h5>" +
								  "</td>" +
								 "</tr>" +
								 "<tr height='20' valign='middle'>" +
								  "<td width='25%'><h5>&nbsp;&nbsp; Apellidos</h5></td>" +
								  "<td width='5%'><h5>:</h5></td>" +
								  "<td width='70%'>" +
								   "<h5>" +
								    "<input type='text' id='apellidos' name='apellidos' value='"+apellidos+"' style='width:230px' maxlength='64'/>" +
								   "</h5>" +
								  "</td>" +
								 "</tr>" +
								 "<tr height='20' valign='middle'>" +
								  "<td width='25%'><h5>&nbsp;&nbsp; E-Mail</h5></td>" +
								  "<td width='5%'><h5>:</h5></td>" +
								  "<td width='70%'>" +
								   "<h5>" +
								    "<input type='text' id='mail' name='mail' value='"+mail+"' style='width:230px' maxlength='64'/>" +
								   "</h5>" +
								  "</td>" +
								 "</tr>" +
								 "<tr height='20' valign='middle'>" +
								  "<td><h5>&nbsp;&nbsp; Activado</h5></td>" +
								  "<td><h5>:</h5></td>" +
								  "<td>" +
								   "<h5>" +
								    "<input type='checkbox' id='checkbox_activado' name='checkbox_activado' value='checkbox'>" +
								   "</h5>" +
								  "</td>" +
								 "</tr>" +
								 "<tr height='20' valign='middle'>" +
								  "<td></td>" +
								  "<td><input type='hidden' id='id_mail' name='id_mail' value='"+id_mail+"'/></td>" +
								  "<td>" +
								   "<input type='button' value='Aceptar' onclick='guardarMail()'/>&nbsp;" +
								   "<input type='button' value='Cerrar' onclick='Windows.close(\"window_id\", event)'/>" +
								  "</td>" +
								 "</tr>" +
								 "<tr height='20' valign='middle'>" +
								  "<td align='center' colspan='4'><b><span id='msj_popup' style='color:#FF0000'></span></b></td>" +
								 "</tr>" +
								"</table>"; 

	win.setDestroyOnClose(); 
	win.showCenter(); 
	
	if (activado == '1') {
		$("checkbox_activado").checked = true;
	} else {
		$("checkbox_activado").checked = false;
	}
	$("nombres").focus();
}

//-- INI Guardar Nuevo Objeto -----------------------------------------------------------------------------------
function guardarMail() {	
	var idMail	 = $("id_mail").value;
	var nombres	 = $("nombres").value;
	var apellidos= $("apellidos").value;
	var mail	 = $("mail").value;
	
	if (Trim(nombres) == '') {
		alert("Debe ingresar el 'Nombre'.");
		$("nombres").focus();
		return;
	}
	if (Trim(apellidos) == '') {
		alert("Debe ingresar el 'Apellido'.");
		$("apellidos").focus();
		return;
	}
	if (Trim(mail) == '') {
		alert("Debe ingresar el 'Mail'.");
		$("mail").focus();
		return;
	}
	if (!validaCorreo($("mail"))) {
		return;
	}	
	var activado = "";
	if ($("checkbox_activado").checked) {
		activado = "1";
	} else {
		activado = "0";
	}

	params = { 	'id_mail':idMail, 'nombres':nombres, 'apellidos':apellidos, 'mail':mail, 'activado':activado };

	if ( idMail == 0 ) {
		var requestOptions = {
							'method': 'post',
							'parameters': params,
							'onSuccess': respuestaAddObjeto
							  };
		new Ajax.Request('AddModMailsSyF', requestOptions);
		
	} else {
		var myAjax = new Ajax.Updater(
									  'mail_' + idMail,
									  'AddModMailsSyF', 
									  	{
											'method': 'post',
											'parameters': params,
											'onSuccess': function(REQUEST) {
												limpiarMsjAndWin();
												setTimeout(function() {
													$("mensaje2").innerHTML = "La información se guardó correctamente.";
												},100);												
											},
											'onFailure': function(REQUEST) {
												$("msj_popup").innerHTML = "La información no se guardó correctamente.";												
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
	if (mensaje != 'OK') {
		$("msj_popup").innerHTML = mensaje;
		return;
	} else {
		limpiarMsjAndWin();		
	}
	var idMail = REQUEST.responseXML.getElementsByTagName("id_mail_new")[0].childNodes[0].nodeValue;
	var fcIni = REQUEST.responseXML.getElementsByTagName("fc_ini")[0].childNodes[0].nodeValue;
	var fcMod = REQUEST.responseXML.getElementsByTagName("fc_mod")[0].childNodes[0].nodeValue;
	
	var descActivado = "";
	if (params.activado == '1') {
		descActivado = "Si";
	} else {
		descActivado = "No";
	}	
	var filaMail =	"<tr id='mail_" + idMail + "'>" +
						"<td align='left' height=\"30\">" + idMail + "</td>" +
						"<td align='left'>" + params.nombres + " " + params.apellidos + "</td>" +
						"<td align='left'>" + params.mail + "</td>" +
						"<td align='center'>" + descActivado + "&nbsp;</td>" +
						"<td align='center'>" + fcIni + "</td>" +
						"<td align='center'>" + fcMod + "</td>" +
						"<td align=\"center\" nowrap>" +
						 "<a href=\"javascript:ventanaObjeto('Modificar Mail','" + params.nombres + "','" + params.apellidos + "','" + params.mail + "','" + idMail + "','" + params.activado + "');\">" +
						  "<img src=\"img/editicon.gif\" border=\"0\" height=\"17\" width=\"19\" title=\"Editar Mail\">" +
						 "</a>&nbsp;" +
						 "<a href=\"javascript:aEliminar('" + idMail + "');\">" +
						  "<img src=\"img/trash.gif\" border=\"0\" height=\"16\" width=\"16\" title=\"Eliminar Mail\">" +
						 "</a>" +
						"</td>" +
					"</tr>";
	var tablaMails = document.getElementById('tabla_mails');
	new Insertion.After(tablaMails,filaMail);
	cantMails++;
	if (cantMails > 0) {
		$("msj_mails").innerHTML = "";
	}
	$("mensaje2").innerHTML = "La información se guardó exitosamente.";	
}
//-- FIN Guardar Nuevo Objeto -----------------------------------------------------------------------------------



//INICIO ELIMINAR UN OBJETO
function aEliminar(id_mail) {
	limpiarMsjAndWin();
	if(confirm("Está seguro de querer eliminar este 'Mail'")) {
		
		var params1 = {'id_mail':id_mail,'accion':'DEL'};
		
		var requestOptions = {
								'method': 'post',
								'parameters': params1,
								'onSuccess': function (REQUEST) {
									
									if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
										$("mensaje1").innerHTML = "El Mail no pudo ser eliminado.";
										return;
									}	
									var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
									if (mensaje != 'OK') {
										$("mensaje1").innerHTML = mensaje;
										return;
									}
									var row_eliminar = eval("$('mail_"+id_mail+"')");
									if (row_eliminar != null) {
										row_eliminar.remove();
										cantMails--;
									}
									if (cantMails <= 0) {
										$("msj_mails").innerHTML = "No existen mail's para listar.";
									}
									$("mensaje2").innerHTML = "El Mail ha sido eliminado.";
								}
							};
		new Ajax.Request('AddModMailsSyF', requestOptions);
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

function validaCorreo(valor) {
	var strMail = valor.value;
	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/.test(strMail)){
		//alert("La dirección de email " + valor + " es correcta.")
		return (true);
	} else {
		alert("La dirección de email es incorrecta.");
		valor.focus();
		return (false);
	}
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