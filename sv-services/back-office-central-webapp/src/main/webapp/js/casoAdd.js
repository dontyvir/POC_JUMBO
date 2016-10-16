function traerDatosPedido() {
	var idPedido = $("num_pedido").value;
	if (idPedido.length == 0) {
		alert("Debe ingresar un N° de Orden de Pedido");
		$("num_pedido").focus();
		return;
	}
	requestOptions = {
					'method': 'get',
					'parameters': "idPedido=" + idPedido,
					'onComplete': llenarDatos
					  };
	new Ajax.Request('DatosPedidoParaCaso', requestOptions);	
}

function rellenaCamposFono(numero1,campoCodigo,campoNumero,campoError,campoInfo){
	numero1=numero1.replace(/\D/g,"");
	numero1=Number(numero1);
	numero1 = numero1.toString();
	var numtel1= numero1; //donde dejaremos el numero sin codigo de area o numero sin normalizar
	var codtel1 = numero1.substr(0,1); //donde dejaremos el codigo de area
	if (codtel1==9){//si es celular
		numtel1=numero1.slice(-8);
		
		
	}else if (numero1.length==9){//si es telefono fijo
		if(codtel1==2){//si es stgo
			numtel1=numero1.slice(-8);
		}else{//si no, regiones
			codtel1 = numero1.substr(0,2);
			numtel1=numero1.slice(-7);
		}
	}else{
		numtel1=numero1;
	}
	if(!validateCorrectNumber(codtel1,numtel1,campoCodigo,campoNumero)){
		jQuery('#'+campoError).html("Debe actualizar n&uacute;mero de contacto ");
		jQuery('#'+campoCodigo).removeClass('campos').addClass('error_campos');
		jQuery('#'+campoNumero).val(numero1);
		jQuery('#'+campoCodigo).append("<option value='' selected>-</option>");//si no cumple formato se le saca codigo de area
	    jQuery('#'+campoNumero).focus(); 
	} else {
		jQuery('#'+campoNumero).numeric({allowThouSep:false , allowDecSep:false, allowMinus:false});
		if(isLongNumber(jQuery('#'+campoCodigo).val())){
			jQuery('#'+campoNumero).attr("maxlength",8);
		}else{
			jQuery('#'+campoNumero).attr("maxlength",7);
			
		}
		
			if(jQuery('#'+campoCodigo).val() == "9" || jQuery('#'+campoCodigo).val() == "2"){ //si es celular o fijo metropilitano, campo número debe ser de 8
				jQuery('#'+campoInfo).html("Ingrese 8 d&iacute;gitos");
			}else{
				jQuery('#'+campoInfo).html("Ingrese 7 d&iacute;gitos");
			}
		
	}
}

function llenarDatos(REQUEST) {
	if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
		alert("Existe un problema con el servicio 'DatosPedidoParaCaso', o tal vez usted no tiene permisos para realizar esta acción.");
		return;
	}
	var message = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
	var mensaje_op = REQUEST.responseXML.getElementsByTagName("mensaje_op")[0].childNodes[0].nodeValue;
	var id_pedido = REQUEST.responseXML.getElementsByTagName("id_pedido")[0].childNodes[0].nodeValue;
	var fecha_pedido = REQUEST.responseXML.getElementsByTagName("fecha_pedido")[0].childNodes[0].nodeValue;
	var direccion = REQUEST.responseXML.getElementsByTagName("direccion")[0].childNodes[0].nodeValue;
	var comuna = REQUEST.responseXML.getElementsByTagName("comuna")[0].childNodes[0].nodeValue;
	var id_local = REQUEST.responseXML.getElementsByTagName("id_local")[0].childNodes[0].nodeValue;
	var fecha_despacho = REQUEST.responseXML.getElementsByTagName("fecha_despacho")[0].childNodes[0].nodeValue;
	var cli_rut = REQUEST.responseXML.getElementsByTagName("cli_rut")[0].childNodes[0].nodeValue;
	var cli_dv = REQUEST.responseXML.getElementsByTagName("cli_dv")[0].childNodes[0].nodeValue;
	var cli_nombre = REQUEST.responseXML.getElementsByTagName("cli_nombre")[0].childNodes[0].nodeValue;
	var cli_fon_cod1 = REQUEST.responseXML.getElementsByTagName("cli_fon_cod1")[0].childNodes[0].nodeValue;
	var cli_fon_num1 = REQUEST.responseXML.getElementsByTagName("cli_fon_num1")[0].childNodes[0].nodeValue;
	var cli_fon_cod2 = REQUEST.responseXML.getElementsByTagName("cli_fon_cod2")[0].childNodes[0].nodeValue;
	var cli_fon_num2 = REQUEST.responseXML.getElementsByTagName("cli_fon_num2")[0].childNodes[0].nodeValue;
	var cli_fon_cod3 = REQUEST.responseXML.getElementsByTagName("cli_fon_cod3")[0].childNodes[0].nodeValue;
	var cli_fon_num3 = REQUEST.responseXML.getElementsByTagName("cli_fon_num3")[0].childNodes[0].nodeValue;
	var cli_num_compras = REQUEST.responseXML.getElementsByTagName("cli_num_compras")[0].childNodes[0].nodeValue;
	var cli_num_casos = REQUEST.responseXML.getElementsByTagName("cli_num_casos")[0].childNodes[0].nodeValue;
	var id_usu_fono = REQUEST.responseXML.getElementsByTagName("id_usu_fono")[0].childNodes[0].nodeValue;
	var usu_fono_nombre = REQUEST.responseXML.getElementsByTagName("usu_fono_nombre")[0].childNodes[0].nodeValue;
	
	if (printValor(mensaje_op) != "") {
		alert(mensaje_op);
	}
	rellenaCamposFono(cli_fon_cod1+cli_fon_num1, "cli_fon_cod1", "cli_fon_num1", "fon_num_1_error","fon_num_1_info");
	rellenaCamposFono(cli_fon_cod2+cli_fon_num2, "cli_fon_cod2", "cli_fon_num2", "fon_num_2_error","fon_num_2_info");
	rellenaCamposFono(cli_fon_cod3+cli_fon_num3, "cli_fon_cod3", "cli_fon_num3", "fon_num_3_error","fon_num_3_info");
	$("num_pedido_old").value = id_pedido;
	$("mensaje_pedido").innerHTML = printValor(message);
	$("fecha_pedido").value = printValor(fecha_pedido);
	$("direccion").value = printValor(direccion);
	$("comuna").value = printValor(comuna);
	$("fecha_despacho").value = printValor(fecha_despacho);
	$("cli_rut").value = printValor(cli_rut);
	$("cli_dv").value = printValor(cli_dv);
	$("cli_nombre").value = printValor(cli_nombre);
	
	$("cli_num_compras").value = printValor(cli_num_compras);
	$("cli_num_casos").value = printValor(cli_num_casos);
	$("sel_local").value = id_local;
	
	$("usu_fono_nombre").value = printValor(usu_fono_nombre);
	$("id_usu_fono").value = id_usu_fono;
	
	if (id_usu_fono == 0) {
		$("sel_teleoperador").value = 'W';
		$("usu_fono_nombre").hide();
	} else {
		$("sel_teleoperador").value = 'F';
		$("usu_fono_nombre").show();
	}
	$("sel_teleoperador").disabled = true;
	
	if (id_local == 0) {
		$("sel_local").disabled = false;
	} else {
		$("sel_local").disabled = true;
	}
	
}
function validaFono(cod,num,campo){
	if (cod==""){
		if (campo.name=="cli_fon_num1"){
			alert("Falta código de telefono 1");
			cli_fon_cod1.focus();
		}else if (campo.name=="cli_fon_num2"){
			alert("Falta código de telefono 2");
			cli_fon_cod2.focus();
		}else if (campo.name=="cli_fon_num3"){
			alert("Falta código de telefono 3");
			cli_fon_cod3.focus();
		}
		return false;
	}
	var codigo1 = cod;
	var numero1 = num;
	var tipofono;
	var numero;
	if (codigo1==9){//si es celular
		//numero = numero1.slice(- 7);
		codigo = numero1.substr(0,1);
		if (codigo<5){
			alert("El primer numero de un celular debe ser entre 5 y 9.");
			campo.value="";
			campo.focus();
			return false;
		}	
		tipofono="celular";
	}else if (codigo1==2){//si es telefono santiago
		numero = numero1.slice (- 8);
		codigo = codigo1;
		tipofono="santiago";
	}else{//si es telefono regiones
		numero = numero1;
		codigo = codigo1;
		tipofono="region";
	}
	if (numero1.toString().length <7 &&(tipofono=="region")){
		alert("Debe ingresar el número de fono correctamente.");
		campo.focus();
		return false;
	}else if(numero1.toString().length <8 &&(tipofono!="region")){
		alert("Debe ingresar el número de fono correctamente.");
		campo.focus();
		return false;
	}
	return true;
	}
function crearCaso() {
	if (validaFormulario()) {
		document.frm_add_caso.submit();
	}
}


function cancelarCreacion() {
	document.frm_add_caso.action = "ViewMonCasos";
	document.frm_add_caso.submit();
}

function validaFormulario() {
	if ($("cli_rut").value == '' || $("cli_rut").value == '0') {
		alert("Debe buscar los datos de la Orden de Pedido");
		$("num_pedido").focus();
		return false;
	}
	if ($("num_pedido").value != $("num_pedido_old").value) {
		alert("El N° de OP ha cambiado, debe realizar nuevamente la búsqueda");
		$("num_pedido").focus();
		$("num_pedido").select();
		return false;
	}
	if ($("fecha_resolucion").value == '') {
		alert("Debe ingresar la Fecha de Compromiso Solución");
		$("fecha_resolucion").focus();
		return false;
	}
	if (!comparafechas($("fecha_minima"),$("fecha_resolucion"))) {
		alert("La fecha seleccionada no es válida");
		return false;
	}
	if ($("sel_jornada").value == '0') {
		alert("Debe seleccionar la Jornada de Compromiso Solución");
		$("sel_jornada").focus();
		return false;
	}
	if ( $("comentario_call") ) {
	  if ( $("comentario_call").value == '' ) {
	    alert("Debe ingresar el comentario");
	    $("comentario_call").focus();
	    return false;
	  }
	}
	if(!(validaFono($("cli_fon_cod1").value,$("cli_fon_num1").value,document.frm_add_caso.cli_fon_num1)))return false;
	if(!(validaFono($("cli_fon_cod2").value,$("cli_fon_num2").value,document.frm_add_caso.cli_fon_num2)))return false;
	if ($("cli_fon_num3").value!=""){
		if(!(validaFono($("cli_fon_cod3").value,$("cli_fon_num3").value,document.frm_add_caso.cli_fon_num3)))return false;
	}
	//Habilitamos los combos para que el request los reconozca
	$("sel_local").disabled = false;
	$("sel_teleoperador").disabled = false;
	$("sel_estados").disabled = false;
	return true;	
}

function validaIndicaciones(text) {
	if(text.value.length > 1000){ 
		alert('Has superado el tama&ntilde;o m&aacute;ximo permitido (1000 Caracteres)'); 
		text.value=text.value.substring(0, 999); 
		return false; 
	}	
}

function limitText(limitField, max_length, divCharDisp) {
	var valor = limitField.value;
	var disponibles = max_length - valor.length;
	if (disponibles < 0) {
		valor = valor.substring(0, (max_length));
		disponibles = max_length - valor.length;
		limitField.value = valor;		
    } 
	document.getElementById(divCharDisp).innerHTML = '<p>' +(disponibles < 0 ? 0 : disponibles) + ((disponibles <= 1 )? ' Caracter Disponible': ' Caracteres Disponibles') +'</p>';	
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
function printValor(valor) {
	if (valor != '-') {
		return valor;	
	} 
	return "";
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