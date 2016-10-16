var params = {};
var REQ_RESPUESTA = null;

//-- INI Guardar Nuevo Objeto -----------------------------------------------------------------------------------

function traerTabla() {
	if (validaForm()) {			
		params = { 	'tipo_salida':'HTML','fecha_ini':$("fecha_ini").value,'fecha_fin':$("fecha_fin").value };
		
		$("fecha_ini_old").value = $("fecha_ini").value;
		$("fecha_fin_old").value = $("fecha_fin").value;
		
		var myAjax = new Ajax.Updater(
										  'div_grilla',
										  'ViewReclamosClientes', 
											{
												'method': 'post',
												'parameters': params,
												'onFailure': function(REQUEST) {
													alert("Error al buscar la información.");
												}
											}
										);	
	}
}

function exportar() {
	if (validaForm()) {		
		if ($("fecha_ini_old").value != $("fecha_ini").value) {
			alert("La fecha de inicio ha cambiado. Debe realizar la búsqueda de reclamos nuevamente.");
			return;
		}
		if ($("fecha_fin_old").value != $("fecha_fin").value) {
			alert("La fecha de término ha cambiado. Debe realizar la búsqueda de reclamos nuevamente.");
			return;
		}
		var param = "?tipo_salida=PLANILLA&fecha_ini="+$("fecha_ini").value+"&fecha_fin="+$("fecha_fin").value;
//		window.open('ViewReclamosClientes'+param,'popup','width=600,height=500,menubar=1,scrollbars=1,resizable=1');
		window.location = "ViewReclamosClientes"+param;
	}
}

//INICIO FUNCIONES GLOBALES
function validaForm() {
	if ($("fecha_ini").value == "") {
		alert("Debe ingresar la fecha de inicio de búsqueda.");
		return false;
	}
	
	if ($("fecha_fin").value == "") {
		alert("Debe ingresar la fecha de término de búsqueda.");
		return false;
	}
	
	if (!comparafechas($("fecha_ini"),$("fecha_fin"))) {
		alert("La fecha de inicio debe ser menor a la fecha de término.");
		return false;
	}
	
	if (!comparafechas($("fecha_fin"),$("fecha_actual"))) {
		alert("La fecha de término no debe ser superior a la fecha actual.");
		return false;
	}
	
	return true;
}

function printValor(valor) {
	if (valor != '-') {
		return valor;	
	} 
	return "";
}

function comparafechas(campo1,campo2) {
  var fecha1 = campo1.value.substring(6,10) + campo1.value.substring(3,5) + campo1.value.substring(0,2);
  var fecha2 = campo2.value.substring(6,10) + campo2.value.substring(3,5) + campo2.value.substring(0,2);
  if (parseInt(fecha2,10) >= parseInt(fecha1,10)) {
    return true;
  } else {
    return false;
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