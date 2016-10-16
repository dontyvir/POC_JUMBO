
var _comuna = "";

function submit_registro() {

	var formulario = document.cliente;

    if( ValidaCliente() == false || validaFechas() == false || validaDireccion( formulario ) == false )
    	return;

	formulario.submit(); 
    	
	
}

function buscar_comunas() {
	var formulario = document.cliente;

	reg_sel = formulario.region.options[formulario.region.selectedIndex].value;

	var d = new Date();
	var url = "ComunasList?id_reg="+reg_sel+"&tm="+d.getTime();
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

function buscar_rut() {
	var formulario = document.cliente;

	regRut = formulario.rutemp.value;
	
	if (regRut != ""){
		var url = "RegisterCheck?regRut="+regRut;
		exec_AJAXRPC('GET', url, 'busrut');
	}	
}

function busrut(text){
	if (text == 'true'){
		alert("El RUT ya se encuentra ingresado ");
		document.cliente.rut.focus();
	}
}

	
	
	
	
	