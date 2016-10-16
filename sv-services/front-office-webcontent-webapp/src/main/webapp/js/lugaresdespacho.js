
function cancelar_despachos() {

	location.href = "BienvenidaForm";

}

function submit_despachos() {

   var formulario = document.despachos;
   
   if( validaDireccion( formulario ) == false )
    	return;

    if (formulario.comentario.value == 'ej: timbre malo'){
		formulario.comentario.value = '';
	}
	
	if(formulario.accion.value == '')
		formulario.action = "AddressAdd";
	else{
		if(confirm("Esta seguro que desea modificar la dirección de despacho."))
			formulario.action = "AddressUpdate";
		else
			return;
	}
	
	formulario.submit()	; 	
	

}

function EliminaDireccion(id){
	if(confirm("Esta seguro que desea eliminar la dirección de despacho.")){
		window.location = "AddressDelete?id="+id;
	}else
		return;
}

var _comuna = 0;

function EditaDireccion(id){
	var formulario = document.despachos;
	 _comuna = document.form1a.elements["comuna_"+id].value;
	 _com_inicial = 0;
	 formulario.dir_id.value =  id;
	 formulario.alias.value =  document.form1a.elements["nombre_"+id].value;
	 formulario.calle.value =  document.form1a.elements["calle_"+id].value;
	 formulario.numero.value =  document.form1a.elements["numero_"+id].value;
	 formulario.departamento.value =  document.form1a.elements["dpto_"+id].value;
	 formulario.region.value =  document.form1a.elements["region_"+id].value;
	 formulario.comuna.value =  document.form1a.elements["comuna_"+id].value;
	 formulario.comentario.value =  document.form1a.elements["comentario_"+id].value;	 	 	 
	 formulario.tipo_calle.value =  document.form1a.elements["tipocalle_"+id].value;	 	 	 	 
	 formulario.accion.value = "upd";
	 formulario.action = "AddressUpdate";
	 buscar_comunas();	 
}

function buscar_comunas() {
	var formulario = document.despachos;

	reg_sel = formulario.region.options[formulario.region.selectedIndex].value;

	var d = new Date();
	var url = "ComunasList?reg_id="+reg_sel+"&tm="+d.getTime();
	top.frames['ifcomuna'].location.href=url;

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

var _com_inicial = 1;

function vaciar_comentario() {

	var formulario = document.despachos;
	
	if( _com_inicial == 1 ) {
		formulario.comentario.value = "";
	}
	_com_inicial = 0;

}



