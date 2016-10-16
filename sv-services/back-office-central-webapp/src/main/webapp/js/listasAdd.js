
function subirListas() {
	if (validaFormulario()) {
		document.frm_add_listas.submit();
	}	
}

function cancelar() {
	document.frm_add_listas.action = "ViewConfigListasEspeciales";
	document.frm_add_listas.submit();
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

function cerrarMsj() {
	$("msj_respuesta").hide();
	$("cargo_listas").value = "NO";
}

function init() {
	if ($("cargo_listas").value == 'SI') {
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
