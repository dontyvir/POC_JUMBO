var creando = true;

function crearEvento() {
	if (validaFormulario()) {
		var idEvento 	 = $("id_evento").value;
		var nombreEvento = $("nombre").value;
		
		var params = {'id_evento':idEvento,'nombre_evento':nombreEvento};
		
		requestOptions = {
						'method': 'get',
						'parameters': params,
						'onComplete': function(REQUEST) {
								if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
									alert("Existe un problema al validar el 'Evento', por favor intente más tarde.");
									return;
								}
								var message = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
								if (message == 'OK') {
									if (creando) {
										document.frm_add_evento.action = "AddEvento";
									} else {
										document.frm_add_evento.action = "ModEvento";
									}
									document.frm_add_evento.submit();
								} else {
									alert(message);
								}
							}
						};
		new Ajax.Request('ValidaEvento', requestOptions);
	}
}

function cancelarCreacion() {
	document.frm_add_evento.action = "ViewMonEventos";
	document.frm_add_evento.submit();
}

function validaFormulario() {	
	// asi se valida el espacio en blanco var.value.replace(/^\s*/, "").replace(/\s*$/,"") == ''
	$("nombre").value = Trim($("nombre").value);
	if (Trim($("nombre").value) == '') {
		alert("Debe ingresar el nombre del evento.");
		$("nombre").focus();
		return false;
	}
	$("descripcion").value = Trim($("descripcion").value);
	if (Trim($("descripcion").value) == '') {
		alert("Debe ingresar la descripción del evento.");
		$("descripcion").focus();
		return false;
	}
	if ($("tipo_evento").value == '0') {
		alert("Debe seleccionar el tipo de evento.");
		$("tipo_evento").focus();
		return false;
	}
	$("fecha_inicio").value = Trim($("fecha_inicio").value);
	if (Trim($("fecha_inicio").value) == '') {
		alert("Debe ingresar la fecha de inicio del evento.");
		$("fecha_inicio").focus();
		return false;
	}	
	if (creando) {
		// validamos q la fecha inicio sea mayor o igual a la fecha de hoy 
		if (!comparafechas($("fecha_actual"),$("fecha_inicio"))) {
			alert("La fecha de inicio debe ser mayor o igual a la fecha actual.");
			return false;
		}
		
	} else {
		// validamos q la fecha inicio sea mayor o igual a la fecha de inicio original (cuando se creo) 
		if ($("fecha_inicio_old").value != $("fecha_inicio").value) {
			if (!comparafechas($("fecha_actual"),$("fecha_inicio"))) {
				alert("La fecha de inicio debe ser mayor o igual a la fecha actual.");
				return false;
			}
		}
	}
	
	$("fecha_fin").value = Trim($("fecha_fin").value);
	if (Trim($("fecha_fin").value) == '') {
		alert("Debe ingresar la fecha de fin del evento.");
		$("fecha_fin").focus();
		return false;
	}
	
	if (!comparafechas($("fecha_inicio"),$("fecha_fin"))) {
		alert("La fecha fin debe ser mayor o igual a la fecha inicio.");
		return false;
	}
	
	$("ocurrencia").value = Trim($("ocurrencia").value);
	if (Trim($("ocurrencia").value) == '') {
		alert("Debe ingresar la ocurrencia del evento.");
		$("ocurrencia").focus();
		return false;
	}
	
	$("orden").value = Trim($("orden").value);
	if (Trim($("orden").value) == '') {
		alert("Debe ingresar el orden del evento.");
		$("orden").focus();
		return false;
	}
	
	if (!creando) {
		if ($("estado").value == '0') {
			alert("Debe seleccionar el estado del evento.");
			$("estado").focus();
			return false;
		}
	}
	
	$("flash").value = Trim($("flash").value);	
	if (Trim($("flash").value) != '') {
		if ($("flash").value.length > 4) {
			var ext = $("flash").value.substring($("flash").value.length-4,$("flash").value.length);
			if (ext != '.swf' && ext != '.SWF') {
				alert("La extensión del archivo Flash no es válido.");
				$("flash").focus();
				return false;
			}
			
		} else {
			alert("El nombre del archivo ingresado no es válido.");
			$("flash").focus();
			return false;
		}
		
	}	
	return true;	
}
function cambioValidacion(campo) {
	if (campo.checked) {
		$('validacion').value = "S";
	} else {
		$('validacion').value = "N";
	}
}

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

var contenido_textarea = "";
var num_caracteres_permitidos = 254;

function valida_longitud() {
	var descripcion = $("descripcion").value;
	var lineas = (descripcion.split("\r\n")).length - 1;
	var largoDesc = descripcion.length;
	
	var restar = (lineas * 2);
	largoDesc = ( (largoDesc - restar) + (lineas * 4) );
	
	if (largoDesc > num_caracteres_permitidos) {
		$("descripcion").value = contenido_textarea;
	} else {
		contenido_textarea = $("descripcion").value;
	}
}

function init() {
	if ($("id_evento").value == 0) {
		$("titulo").innerHTML = "Crear un Nuevo Evento";
		creando = true;
		$("tabla_estado").hide();
	} else {
		$("titulo").innerHTML = "Modificar Evento N° " + $("id_evento").value;
		creando = false;
		$("tabla_estado").show();
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
