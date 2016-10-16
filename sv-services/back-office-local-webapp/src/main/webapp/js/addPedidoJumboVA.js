Event.observe(window, 'load', function(e) {
  Event.observe('addPedidoJumboVA', 'submit', checkForm);
});

ADD_PEDIDO_EXT = true;

function validaFono(cod,num,campo){
	if (cod==""){
		if (campo.name=="fon_num_1"){
			alert("Falta código de telefono 1");
			fon_cod_1.focus();
		}else if (campo.name=="fon_num_2"){
			alert("Falta código de telefono 2");
			fon_cod_2.focus();
		}
		return false;
	}
	var codigo1 = cod;
	var numero1 = num;
	var numero=numero1;
	var tipofono="";
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
	if (numero.length <7 && tipofono=="region"){
		alert("Debe ingresar el número de fono correctamente.");
		campo.focus();
		return false;
	}else if(numero.length <8 && tipofono!="region"){
		alert("Debe ingresar el número de fono correctamente.");
		campo.focus();
		return false;
	}
	return true;
}

function checkForm(evt) {
	if( $('rut').value.trim() == '' || (!/^\d+$/.test($('rut').value)) ) {
		alert('Ingrese el Rut del cliente');
		$('rut').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if( $('dv').value.trim() == '' ) {
		alert('Ingrese el Dígito Verificador del cliente');
		$('dv').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if (! checkRutField( $('rut').value + '-' + $('dv').value) ) {
		$('rut').focus();
		Event.stop(evt);
		return;
	}	
	if( ($('nro_guia').value.trim() == '') || (!/^\d+$/.test($('nro_guia').value)) ) {
		alert('Ingrese un número de guía');
		$('nro_guia').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if( $('nombre').value.trim() == '' ) {
		alert('Ingrese el Nombre del cliente');
		$('nombre').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if( $('direccion').value.trim() == '' ) {
		alert('Ingrese la Dirección del cliente');
		$('direccion').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if( $('region').value == 0 ) {
		alert('Seleccione la región');
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if( $('comuna').value == 0 ) {
		alert('Seleccione la comuna');
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if(!(validaFono($('fon_cod_1').value,$('fon_num_1').value,document.addPedidoJumboVA.fon_num_1))){
		Event.stop(evt); // Stop the form from submitting
		return;
	} 
	if(!($('fon_num_2').value=="")){
		if(!(validaFono($('fon_cod_2').value,$('fon_num_2').value,document.addPedidoJumboVA.fon_num_2))){
			Event.stop(evt); // Stop the form from submitting
			return;
		} 
	}

		$('fono_fijo').value=$('fon_cod_1').value+$('fon_num_1').value;
		$('fono_celu').value=$('fon_cod_2').value+$('fon_num_2').value;
	if( $('fono_fijo').value.trim() == '' ) {
		alert('Ingrese el Teléfono del cliente');
		$('fon_num_1').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if( $('fono_celu').value.trim() == '' ) {
		alert('Ingrese el Celular del cliente');
		$('fon_num_2').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if( $('mail').value.trim() == '' ) {
		alert('Ingrese el Mail del cliente');
		$('mail').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	var er_email = /^(.+\@.+\..+)$/;
	if(!er_email.test($('mail').value)) { 
       alert('Ingrese correctamente el Mail del cliente');
		$('mail').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
    }
	if( $('zona').value == 0 ) {
		alert('Seleccione la Zona de Despacho');
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if( $('txt_fecha').value.trim() == '' ) {
		alert('Ingrese la Fecha de Despacho');
		$('txt_fecha').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if( $('jdespacho').value == 0 ) {
		alert('Seleccione la Jornada de Despacho');
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if( $('boleta1').value.trim() == '' || (!/^\d+$/.test($('boleta1').value)) ) {
		alert('Ingrese el N° de Boleta/Factura');
		$('boleta1').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if( $('bins').value.trim() == '' || (!/^\d+$/.test($('bins').value)) ) {
		alert('Ingrese la cantidad de Bins');
		$('bins').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	if( $('monto').value.trim() == '' || (!/^\d+$/.test($('monto').value)) ) {
		alert('Ingrese el monto del Pedido');
		$('monto').focus();
		Event.stop(evt); // Stop the form from submitting
		return;
	}
	var tipo_doc;
	var boleta_facts;
	if (document.addPedidoJumboVA.tipo_doc[0].checked) {
		tipo_doc = document.addPedidoJumboVA.tipo_doc[0].value;
		boleta_facts = $('boleta1').value;
	} else {
		tipo_doc = document.addPedidoJumboVA.tipo_doc[1].value;
		boleta_facts = traeFacturas();
	}
	var tipo_jumbo_va;
	if (document.addPedidoJumboVA.tipo_jumbo_va[0].checked) {
		tipo_jumbo_va = document.addPedidoJumboVA.tipo_jumbo_va[0].value;
		
	} else {
		tipo_jumbo_va = document.addPedidoJumboVA.tipo_jumbo_va[1].value;
	}
			
	var params = { 	nro_guia: $('nro_guia').value, tipo_pedido: 'A',
					rut: $('rut').value, dv: $('dv').value,
					nombre: $('nombre').value, direccion: $('direccion').value, indicacion: $('indicacion').value,
					comuna: $('comuna').value, fono_fijo: $('fono_fijo').value,
					fono_celu: $('fono_celu').value, zona: $('zona').value,
					fc_despacho: $('txt_fecha').value, jdespacho: $('jdespacho').value,
					bins: $('bins').value, boleta: boleta_facts,
					tipo_doc: tipo_doc, monto: $('monto').value,
					tipo_jumbo_va: tipo_jumbo_va, bins2: $('bins2').value,
					mail: $('mail').value				
				 };
	var requestOptions = {
							'method': 'post',
							'parameters': params,
							'onSuccess': function(transport) {
								var dr = transport.responseXML.documentElement;
								var m = dr.getElementsByTagName('mensaje')[0].firstChild.data;
								alert("El pedido se agregó exitosamente");
								window.location.href = "ViewMonitorDespacho?fecha=" + params.fc_despacho + "&id_jornada=" + params.jdespacho + "&id_zona=" + params.zona + "&tipo=A&reprogramada=-1";
							},
							'onFailure': function(transport) {
								alert('The request failed');
							},
							'onException': function(err) {
								// When an exception is encountered while executing the callbacks
								alert('Ocurrió un error al guardar.');
							}							
						};
	new Ajax.Request('AgregaPedidoExterno', requestOptions);
		
	Event.stop(evt);
}

function buscarInfo() {
	if (! checkRutField( $('rut').value + '-' + $('dv').value) ) {
		return;
	}
	$("msj_buscando").innerHTML = "<font color='blue'><b>Buscando información...</b></font>";
	var requestOptions = {
							'method': 'post',
							'parameters': "rut=" + $('rut').value,
							'onSuccess': llenarForm							
						};
	new Ajax.Request('TraeDatosDePedidoJumboVA', requestOptions);
}
function llenarForm(REQUEST) {
	if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
		$("msj_buscando").innerHTML = "<font color='red'><b>No existe información</b></font>";
		return;
	}
	var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
	if (mensaje != "OK") {
		$("msj_buscando").innerHTML = "<font color='red'><b>" + mensaje + "</b></font>";
	} else {
		var dir = REQUEST.responseXML.getElementsByTagName("direccion")[0].childNodes[0].nodeValue;
		$("direccion").value = dir.trim();
		$("nombre").value = REQUEST.responseXML.getElementsByTagName("nombre")[0].childNodes[0].nodeValue;
		$("fono_fijo").value = REQUEST.responseXML.getElementsByTagName("fono1")[0].childNodes[0].nodeValue;
		$("fono_celu").value = REQUEST.responseXML.getElementsByTagName("fono2")[0].childNodes[0].nodeValue;
		$("mail").value = REQUEST.responseXML.getElementsByTagName("mail")[0].childNodes[0].nodeValue;
		
		var id_region = REQUEST.responseXML.getElementsByTagName("id_region")[0].childNodes[0].nodeValue;
		$("region").value = id_region;
		
		var id_comuna = REQUEST.responseXML.getElementsByTagName("id_comuna")[0].childNodes[0].nodeValue;
		llenarComunas(id_region, id_comuna);
		$("msj_buscando").innerHTML = "";
		
		var numero1 = document.getElementById("fono_fijo").value;
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
		if (!validateCorrectNumber(codtel1, numtel1, "fon_cod_1", "fon_num_1")) {
			jQuery('#fon_num_1_error').html(
					"Debe actualizar n&uacute;mero de contacto 1");
			jQuery('#fon_num_1').removeClass('campos').addClass('error_campos');
			jQuery('#fon_num_1')
					.val(document.getElementById("fono_fijo").value);
			jQuery('#fon_cod_1').append("<option value='' selected>-</option>");// si
																				// no
																				// cumple
																				// formato
																				// se
																				// le
																				// saca
																				// codigo
																				// de
																				// area
			jQuery("#fon_num_1").focus();
		} else {
			jQuery("#fon_num_1").numeric({
				allowThouSep : false,
				allowDecSep : false,
				allowMinus : false
			});
			if (isLongNumber(jQuery("#fon_cod_1").val())) {
				jQuery("#fon_num_1").attr("maxlength", 8);
			} else {
				jQuery("#fon_num_1").attr("maxlength", 7);

			}

			if (jQuery("#fon_cod_1").val() == "9"
					|| jQuery("#fon_cod_1").val() == "2") { // si es celular o
															// fijo
															// metropilitano,
															// campo número debe
															// ser de 8
				jQuery("#fon_num_1_info").html("Ingrese 8 d&iacute;gitos");
			} else {
				jQuery("#fon_num_1_info").html("Ingrese 7 d&iacute;gitos");
			}

		}
		var numero2 = REQUEST.responseXML.getElementsByTagName("fono2")[0].childNodes[0].nodeValue;
		numero2=numero2.replace(/\D/g,"");
		numero2=Number(numero2);
		numero2 = numero2.toString();
		var numtel2= numero2; //donde dejaremos el numero sin codigo de area o numero sin normalizar
		var codtel2 = numero2.substr(0,1); //donde dejaremos el codigo de area
		if (codtel2==9){//si es celular
			numtel2=numero2.slice(-8);
			
		}else if (numero2.length==9){//si es telefono fijo
			if(codtel2==2){//si es stgo
				numtel2=numero2.slice(-8);
			}else{//si no, regiones
				codtel1 = numero2.substr(0,2);
				numtel2=numero2.slice(-7);
			}
		}else{
			numtel2=numero2;
		}
		if(!validateCorrectNumber(codtel2,numtel2,"fon_cod_2","fon_num_2")){
			jQuery('#fon_num_2_error').html("Debe actualizar n&uacute;mero de contacto 2");
			jQuery('#fon_num_2').removeClass('campos').addClass('error_campos');
			jQuery('#fon_num_2').val(document.getElementById("fono_celu").value);
			jQuery('#fon_cod_2').append("<option value='' selected>-</option>");//si no cumple formato se le saca codigo de area
		    jQuery("#fon_num_2").focus(); 
		} else {
			jQuery("#fon_num_2").numeric({allowThouSep:false , allowDecSep:false, allowMinus:false});
			if(isLongNumber(jQuery("#fon_cod_2").val())){
				jQuery("#fon_num_2").attr("maxlength",8);
			}else{
				jQuery("#fon_num_2").attr("maxlength",7);
				
			}
			
				if(jQuery("#fon_cod_2").val() == "9" || jQuery("#fon_cod_2").val() == "2"){ //si es celular o fijo metropilitano, campo número debe ser de 8
					jQuery("#fon_num_2_info").html("Ingrese 8 d&iacute;gitos");
				}else{
					jQuery("#fon_num_2_info").html("Ingrese 7 d&iacute;gitos");
				}
			
		}
	}

	
}
function llenarComunas(idRegion, idComuna) {
	new Ajax.Updater('comuna', 'ComunasByRegion', { 
													parameters: { id_region: idRegion },
													onComplete : function() {
														setTimeout(function() {
															$("comuna").value = idComuna;
															llenarZonas();
														},500);														
													}
												  });
}
function llenarZonas() {
	new Ajax.Updater('zona', 'ZonasByComuna', { 
												parameters: { id_comuna: $("comuna").value },
												onComplete : function() {
													//cambioZona();
												}
											  });
}
function cambioFecha() {
	cambioZona();
}
function cambioZona() {
	if ( $('zona').value == 0 ) {
		alert("Debe seleccionar una zona de despacho");
		return;
	}
	traeJornadas();
}
function traeJornadas() {
	new Ajax.Updater('jdespacho', 'JornadasDespacho', { 
												parameters: { fecha: $("txt_fecha").value, zona: $('zona').value, tipo_pedido: 'V' },
												onComplete : function() {
													//cambioZona();
												}
											  });
}

function addDoc() {
	if (document.addPedidoJumboVA.tipo_doc[0].checked) {
		return;
	}
	var x = 2;
	if ( document.addPedidoJumboVA.boleta.length ) {
		x = document.addPedidoJumboVA.boleta.length + 1;
	}
	var atributos = {
		type	: 'text',
		value   : '',
		style   : 'width:200px',
		id   	: 'boleta' + x,
		name 	: 'boleta'
	}; 
	var obj1 = new Element('BR', atributos);
	$('documentos').insert(obj1);
	
	var obj = new Element('input', atributos);
	$('documentos').insert(obj);
}

function traeFacturas() {
	var facts = "";
	var sep = "";
	if ( document.addPedidoJumboVA.boleta.length ) {
		for ( var x = 1; document.addPedidoJumboVA.boleta.length >= x; x++ ) {
			facts += sep + $("boleta" + x + "").value;
			sep = "-=-";
		}
	} else {
		facts = document.addPedidoJumboVA.boleta.value;
	}
	return facts;
}

function selBoleta() {
	if ( document.addPedidoJumboVA.boleta.length ) {
		var largo = document.addPedidoJumboVA.boleta.length;
		for ( var v = 2; v <= largo; v++ ) {
			if ( $("boleta" + v + "") ) {
				$("boleta" + v + "").remove();
			}
		}
	}
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