function bajarListado() {
	document.exp_lista.submit();
}
function subirRuts() {
	if (validaFormulario()) {
		document.frm_add_ruts.submit();
	}	
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
