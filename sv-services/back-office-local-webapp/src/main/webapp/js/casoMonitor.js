function bus_num(form){
	if (form.nro_pedido.value == ""){ 
		alert("Debe ingresar N� de pedido");
		return false;
	}
	if (validar_solonumero(form.nro_pedido, "N� de Pedido es un valor Numerico")) return false;
	//form.action.value='bus_num';
	return true;
}

function cambioFcSeleccion(tipo) {
	var obj = document.frm_filtro;
	for (var i=0; i < obj.bus_fecha_sel.length; i++) {
		if( obj.bus_fecha_sel[i].value == tipo ) {
			obj.bus_fecha_sel[i].checked = true;
		}
	}	
}

function cambioCliSeleccion(tipo) {
	var obj = document.frm_cli;
	for (var i=0; i < obj.cli_datos_sel.length; i++) {
		if( obj.cli_datos_sel[i].value == tipo ) {
			obj.cli_datos_sel[i].checked = true;
		}
	}
}

function bus_caso(form){
	if (form.nro_caso.value == ""){ 
		alert("Debe ingresar N� de caso");
		return false;
	}
	if (validar_solonumero(form.nro_caso, "N� de caso es un valor Numerico")) return false;
	//form.action.value='bus_num';
	return true;
}


function bus_cli(form){
	//form.action.value='bus_cli';
	if (form.cli_datos.value == ""){
		alert("Debe ingresar criterio de b�squeda");
		return false;
	}
	if (form.cli_datos_sel[0].checked == true){
			if (validar_solonumero(form.cli_datos, "Rut es un campo num�rico, Ingr�selo sin puntos, ni gui�n y sin d�gito verificador")) return false;
		
	}
	if (form.cli_datos_sel[1].checked == true){
		if(validar_letranumero(form.cli_datos,"El apellido solo permite letras y n�meros")) return false;
	}
	return true;
	
}    

function realizarBusqueda() {
	var obj = document.frm_filtro;
	if( (obj.fc_ini.value != "") && (obj.fc_fin.value == "") ) {
		alert("Debe ingresar la Fecha Fin de b�squeda");
		return;
	}
	if( (obj.fc_ini.value == "") && (obj.fc_fin.value != "") ) {
		alert("Debe ingresar la Fecha Inicial de b�squeda");
		return;
	}
	
	if( (obj.fc_ini.value != "") && (obj.fc_fin.value != "") ) {
		if (!comparafechas(obj.fc_ini,obj.fc_fin)) {
			alert("La Fecha Fin debe ser mayor o igual a la Fecha Inicial de b�squeda");
			return;
		}
	}
	
	obj.submit();
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
