function eliminarEvento(id_evento,sel_tipo_evento,sel_est,fc_ini,fc_fin) {
	if(confirm("Está seguro de querer eliminar el evento")) {
		window.location = "DelEvento?id_evento=" + id_evento + "&sel_tipo_evento=" + sel_tipo_evento + "&sel_est=" + sel_est + "&fc_ini=" + fc_ini + "&fc_fin=" + fc_fin;
	}
} 
function realizaBusqueda() {
	if (validaFormulario()) {
		document.frm_filtro.submit();
	}	
}

function validaFormulario() {	
	if (Trim( $("fc_ini").value ) != '' && Trim( $("fc_fin").value ) == '') {
		alert("Debe ingresar la fecha fin de búsqueda.");
		return false;
	}
	if (Trim( $("fc_ini").value ) == '' && Trim( $("fc_fin").value ) != '') {
		alert("Debe ingresar la fecha inicio de búsqueda.");
		return false;
	}
	if (Trim( $("fc_ini").value ) != '' && Trim( $("fc_fin").value ) != '') {
		if (!comparafechas($("fc_ini"),$("fc_fin"))) {
			alert("La fecha fin debe ser mayor o igual a la fecha inicio.");
			return false;
		}
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
