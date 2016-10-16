function previsualizar() {
	$('banner').hide();
	var flash = $("nombre_flash").value;

	if ( flash.length <= 4 ) {
		alert("Debe ingresar un nombre válido de Flash.");
		$("nombre_flash").focus();
	} else {
		var exten = flash.substring(flash.length-4, flash.length);
		if (exten == '.swf' || exten == '.SWF') {
			mostrarFlash(flash);
			$('banner').show();
			$("btn_aceptar").disabled = false;			

		} else {
			alert("La extensión del archivo Flash no es válido.");
			$("nombre_flash").focus();
		}
	}
}

function validaFormulario() {	
	// asi se valida el espacio en blanco var.value.replace(/^\s*/, "").replace(/\s*$/,"") == ''
	if ( $("nombre_marco").value.replace(/^\s*/, "").replace(/\s*$/,"") == '' ) {
		alert("Debe ingresar el nombre del marco.");
		$("nombre_marco").focus();
		return false;
	}
	if ( $("nombre_flash").value.replace(/^\s*/, "").replace(/\s*$/,"") == '' ) {
		alert("Debe ingresar el nombre del flash.");
		$("nombre_flash").focus();
		return false;
	}
	return true;	
}

function crearEvento() {
	if ( validaFormulario() ) {
		document.f1.submit();
	}
}

function modificaFlash() {
	$("btn_aceptar").disabled = true;
	$('banner').hide();
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
