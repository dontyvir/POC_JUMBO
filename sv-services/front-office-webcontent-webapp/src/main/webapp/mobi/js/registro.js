var _comuna = "";

function submit_registro() {
	var formulario = document.cliente;	
	if (!formulario.rut.value){
		alert('Debes ingresar el campo RUT');
		formulario.rut.focus();
		return;
	}
	if (!formulario.dv.value){
		alert('Debes ingresar el n\u00FAmero que est\u00E1 despu\u00E9s del gui\u00F3n en tu Rut');
		formulario.dv.focus();
		return;
	}
    if(!checkRutField( formulario.rut.value+"-"+formulario.dv.value, formulario.rut ) ) {
		formulario.rut.focus();
		return;
	}
	var requestOptions = {
		'method': 'post',
		'parameters': "regRut="+document.cliente.rut.value,
		'onSuccess': function(REQUEST) {
			if ( REQUEST.responseText == 'true' ) {
				alert("El RUT ingresado ya est\u00E1 asociado a un cliente existente");
				document.cliente.rut.focus();
				return;
			
			} else {				
				if ( !ValidaCliente() || !validaFechas() || !validaDireccion(formulario) ) {
					return;
				}
				formulario.submit();	
			}
			
		}
	};
	new Ajax.Request('RegisterCheck', requestOptions);
}

function volver() {
	if (confirm("Tus datos no han sido guardados.\n¿Deseas de todos modos volver a la p\u00E1gina de incio de Jumbo.cl Mobile?")) {
		location.href = "LogonFormMobi";
	}
}

function buscar_comunas() {
	var formulario = document.cliente;

	reg_sel = formulario.region.options[formulario.region.selectedIndex].value;

	var d = new Date();
	var url = "ComunasList?reg_id="+reg_sel+"&tm="+d.getTime();
	top.frames['ifcomuna'].location.href=url;

}

function procom() {

	var formulario = document.cliente;

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

	var formulario = document.cliente;
	if( _com_inicial == 1 ) {
		formulario.comentario.value = "";
	}
	_com_inicial = 0;

}
