function muestraDireccion() {
	var id = $('sel_dir_desp').value;
	$('nombre_txt').innerHTML = document.form1.elements["nombre_"+id].value;
	$('direccion_txt').innerHTML = document.form1.elements["calle_"+id].value + ", " + document.form1.elements["numero_"+id].value + ", " + document.form1.elements["dpto_"+id].value;
	$('comuna_txt').innerHTML = document.form1.elements["comuna_txt_"+id].value;
	$('region_txt').innerHTML = document.form1.elements["region_txt_"+id].value;
}
var _comuna = 0;
function editaDireccion() {
	var id = $('sel_dir_desp').value;
	var formulario = document.despachos;
	 _comuna = document.form1.elements["comuna_"+id].value;
	 _com_inicial = 0;
	 formulario.dir_id.value =  id;
	 formulario.alias.value =  document.form1.elements["nombre_"+id].value;
	 formulario.calle.value =  document.form1.elements["calle_"+id].value;
	 formulario.numero.value =  document.form1.elements["numero_"+id].value;
	 formulario.departamento.value =  document.form1.elements["dpto_"+id].value;
	 formulario.region.value =  document.form1.elements["region_"+id].value;
	 formulario.comuna.value =  document.form1.elements["comuna_"+id].value;
	 formulario.comentario.value =  document.form1.elements["comentario_"+id].value;	 	 	 
	 formulario.tipo_calle.value =  document.form1.elements["tipocalle_"+id].value;
	 formulario.accion.value = "upd";
	 formulario.action = "AddressUpdate";
	 buscar_comunas();
	 document.location.href = "#ancla_modificar";
}
function eliminaDireccion() {
	if ( $('sel_dir_desp').length > 1 ) {
		if(confirm("Est� seguro que desea eliminar la direcci�n de despacho.")){
			window.location = "AddressDeleteMobi?id="+$('sel_dir_desp').value;
		}
	} else {
		alert("Acci�n no permitida.");
	}
}
function buscar_comunas() {
	var formulario = document.despachos;
	reg_sel = formulario.region.options[formulario.region.selectedIndex].value;
	var d = new Date();
	var url = "ComunasList?reg_id="+reg_sel+"&tm="+d.getTime();
	top.frames['ifcomuna'].location.href=url;
}
var _com_inicial = 1;
function vaciar_comentario() {
	var formulario = document.despachos;
	if( _com_inicial == 1 ) {
		formulario.comentario.value = "";
	}
	_com_inicial = 0;
}
function checklength(nomtxt) {
	var max = 250;
	var txt;
	txt=document.getElementById(nomtxt.name);
	var n = txt.value.length;
	if (n>=max)	{
		alert('Haz excedido el limite m�ximo de caracteres permitidos');
		txt.value = txt.value.substring(0, max-1); 
		return false;
	}
}
function cancelar_despachos() {
	location.href = "BienvenidaFormMobi";
}
function submit_despachos() {
	var formulario = document.despachos;   
	if ( !validaDireccion( formulario ) ) {
    	return;
	}
	if (formulario.comentario.value == 'ej: timbre malo') {
		formulario.comentario.value = '';
	}	
	if (formulario.accion.value == '') {
		formulario.action = "AddressAddMobi";
	} else {
		if(confirm("Esta seguro que desea modificar la direcci�n de despacho.")) {
			formulario.action = "AddressUpdateMobi";
		} else {
			return;
		}
	}	
	formulario.submit();
}
function validaDireccion( formulario ) {
	if (Trim(formulario.alias.value) == '') {
		alert("Debes ingresar campo nombre referencial.");
		formulario.alias.focus();
		return false;
	}
	if(Trim(formulario.tipo_calle.value) == ''){
		alert("Debes seleccionar un tipo de calle");
		formulario.tipo_calle.focus();
		return false;
	}
	if(Trim(formulario.calle.value) == ''){
		alert("Debes ingresar campo direcci�n.");
		formulario.calle.focus();
		return false;
	}
	if(Trim(formulario.numero.value) == ''){
		alert("Debes ingresar campo N�mero.");
		formulario.numero.focus();
		return false;
	}
	if(formulario.region.value == 0){
		alert("Debes seleccionar una Regi�n");
		formulario.region.focus();
		return false;
	}
	if(formulario.comuna.value == 0){
		alert("Debes seleccionar una Comuna");
		formulario.comuna.focus();
		return false;
	}
	if(formulario.comentario.value.length > 250){
		alert('Has excedido el limite m�ximo de caracteres');
		formulario.comentario.value = formulario.comentario.value.substring(0, 256); 
		formulario.comentario.focus();
		return false;
	}
	return true;
}
function procom() {
	var formulario = document.despachos;
	texto = top.frames['ifcomuna'].document.frmcom.com.value;
	var response = texto.split("|");
	var cant = formulario.comuna.options.length;
	for( var i = 0; i < cant; i++ )
		formulario.comuna.options[i] = null;
	formulario.comuna.length=0;
	makeOptionList(formulario.comuna,'Seleccionar',0,0);
	for( i = 0; i < response.length-1; i++ ) {
		comunas = response[i].split("-");
		if( comunas[0] == _comuna )
			makeOptionList(formulario.comuna,comunas[1],comunas[0],1);
		else
			makeOptionList(formulario.comuna,comunas[1],comunas[0],0);
	}
}