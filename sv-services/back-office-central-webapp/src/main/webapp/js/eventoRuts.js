
function subirRuts() {
	if (validaFormulario()) {
		document.frm_add_ruts.submit();
	}	
}

function cancelar() {
	document.frm_add_ruts.action = "ViewMonEventos";
	document.frm_add_ruts.submit();
}

function liberar() {
	window.location = "LiberaEvento?id_evento=" + $("id_evento").value;
}

function validaFormulario() {	
	var file = $("upFile").value;
	if (Trim( file ) == '') {
		alert("Debe seleccionar un archivo que contenga los Rut's.");
		return false;
	}
	var ext = file.substring(file.length-3,file.length);
	if (ext != 'xls' && ext != 'XLS') {
		alert("El archivo seleccionado debe ser una planilla, con extensión 'xls'.");
		return false;
	}
	return true;	
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

function cerrarMsj() {
	$("msj_respuesta").hide();
	$("cargo_rut").value = "NO";
}

function init() {
	if ($("cargo_rut").value == 'SI') {
		$("msj_respuesta").show();
	} else {
		$("msj_respuesta").hide();
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
