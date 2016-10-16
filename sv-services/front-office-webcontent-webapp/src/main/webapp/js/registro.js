var _comuna = "";

function submit_registro() {
  var formulario = document.cliente;
  $('btn_register').href = "#";//javascript:submit_registro();
  if ( ValidaCliente() == false || validaFechas() == false || validaDireccion(formulario) == false ) {
    $('btn_register').href = "javascript:submit_registro();";
    return;
  }
  if ( $('rut').value != "" ) {
    var params = {'regRut':$('rut').value};
    new Ajax.Request(
      'RegisterCheck', {
        'method': 'post',
        'parameters': params,
        'onSuccess': function (text) {
          if (text.responseText == 'true') {
            alert("El RUT ya se encuentra ingresado");
            $('btn_register').href = "javascript:submit_registro();";
          } else {
            formulario.submit();
          }
        }
      }
    );
  }
}

function volver() {
	if (confirm("Tus datos no han sido guardados.\n¿Deseas de todos modos volver a la p\u00E1gina de incio de Jumbo.cl?")) {
		location.href = "LogonForm";
	}
}

function buscar_comunas_old() {
	var formulario = document.cliente;

	reg_sel = formulario.region.options[formulario.region.selectedIndex].value;

	var d = new Date();
	var url = "ComunasList?reg_id="+reg_sel+"&tm="+d.getTime();
	top.frames['ifcomuna'].location.href=url;
}

function buscar_comunas(formulario) {
	/*	SE AGREGA PARA SOLUCIONAR BUG DE FONO COMPRAS	*/
	var formulario = document.cliente;
	reg_sel = formulario.region.options[formulario.region.selectedIndex].value;

	var d = new Date();
	var url = "ComunasList?reg_id="+reg_sel+"&tm="+d.getTime();
	top.frames['ifcomuna'].location.href=url;
}

function buscar_comunas_RegSencillo(formulario) {
    if (formulario.id_region.selectedIndex > 0){
        //alert("id_region: " + formulario.id_region.selectedIndex);
	    reg_sel = formulario.id_region.options[formulario.id_region.selectedIndex].value;
        //alert("reg_sel: " + reg_sel);
	    
	    var d = new Date();
	    var url = "ComunasList?reg_id="+reg_sel+"&tm="+d.getTime();
	    top.frames['ifcomuna'].location.href=url;
    }
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
  if ( $('rut').value != "" ) {
    var params = { 'regRut':$('rut').value };
    new Ajax.Request(
      'RegisterCheck', {
        'method':'post',
        'parameters':params,
        'onSuccess': function (text) {
          if ( text.responseText == 'true') {
            alert("El RUT ingresado ya est\u00E1 asociado a un cliente existente");
            $('rut').focus();
          }
        }
      }
    );
  }	
}

