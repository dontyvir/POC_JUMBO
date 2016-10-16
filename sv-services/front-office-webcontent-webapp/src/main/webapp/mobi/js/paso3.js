// -- ******  INI TOUCH EVENTS ****** --
var pageX = pageY = 0;
var sensibilidad = 40;
var paso_actual = 1;
var cambiando = 0;
var cambio_datos = 0;
var cambio_mail = 0;
function init_dw_Scroll() {
  var wndo = new dw_scrollObj('wn', 'lyr1', 'imgTbl');
  wndo.setUpScrollControls('scroll_links');
}
if ( dw_scrollObj.isSupported() ) {
    dw_Event.add( window, 'load', init_dw_Scroll);
}
// Evento START
document.addEventListener('touchstart', function(e) {
	// Posición inicial del evento touch
	var touch = e.touches[0];
	// Posición X
	pageX = touch.pageX;
	// Posición Y
	pageY = touch.pageY;
}, false);
// Evento MOVE
document.addEventListener('touchmove', function(e) {
        // Evitamos que se mueva la página
        //e.preventDefault();
	var touch = e.touches[0];
	// Variación de la posición X
	var inicio = pageX;
	var final = touch.pageX;
	if (cambiando == 0) {
		//Dirección X
		if ( inicio < final ) {
			//derecha
			if ( ( inicio + sensibilidad ) < final ) {
				cambiando++;
				movioIzquierda();
			}
		} else if (inicio > final) { 
			//izquierda
			if ( ( inicio - sensibilidad ) > final ) {
				cambiando++;			
				movioDerecha();
			}
		}
	}
	return;
}, false);
// Evento END
document.addEventListener('touchend', function(e){ return;}, false); // No hacemos nada
function selConfirmar() {
	paso_actual				= 1;
	$('img_confirmar').src	= '/FO_IMGS/images/mobi/paso3/confirmarOn.gif';
	$('img_despacho').src	= '/FO_IMGS/images/mobi/paso3/despachoOff.gif';
	$('img_pago').src 		= '/FO_IMGS/images/mobi/paso3/pagoOff.gif';
}
function selDespacho() {
	paso_actual				= 2;
	$('img_despacho').src	= '/FO_IMGS/images/mobi/paso3/despachoOn.gif';
	$('img_confirmar').src	= '/FO_IMGS/images/mobi/paso3/confirmarOff.gif';
	$('img_pago').src		= '/FO_IMGS/images/mobi/paso3/pagoOff.gif';
}
function selPago() {
	paso_actual				= 3;
	$('img_pago').src 		= '/FO_IMGS/images/mobi/paso3/pagoOn.gif';
	$('img_confirmar').src 	= '/FO_IMGS/images/mobi/paso3/confirmarOff.gif';
	$('img_despacho').src 	= '/FO_IMGS/images/mobi/paso3/despachoOff.gif';
}
function movioDerecha() {
	if ( paso_actual == 1 ) {
		irDespacho();
	} else if ( paso_actual == 2 ) {
		irPago();
	}
	setTimeout(function() {
		cambiando = 0;
	},500);
}
function movioIzquierda() {
	if ( paso_actual == 2 ) {
		mostrarCondirmarDatos();
	} else if ( paso_actual == 3 ) {
		mostrarDespacho();
	} 
	setTimeout(function() {
		cambiando = 0;
	},500);
}
// -- ******  FIN TOUCH EVENTS ****** --
/*
function formaDePagoLayer(value) {
	if (value == 1) {
		document.getElementById('jumbo2').style.visibility = 'visible';
		document.getElementById('paris').style.visibility = 'hidden';
		document.getElementById('paris-adicional').style.visibility = 'hidden';
		document.getElementById('parisempleado').style.visibility = 'hidden';
		document.getElementById('parisnormal').style.visibility = 'hidden';
		document.getElementById('masparis').style.visibility = 'hidden';
		document.getElementById('bancaria').style.visibility = 'hidden';
		$('formasDePago').style.height = '130px';
		document.getElementById('mpa').style.visibility = 'hidden';
		document.frmPedido.p_numtja.value = "";
		document.frmPedido.titular[0].checked = false;
		document.frmPedido.titular[1].checked = false;
	} else if (value == 2) {
		if (document.frmPedido.empleado.value == 1) {
		  document.getElementById('parisempleado').style.visibility = 'visible';
		  document.getElementById('jumbo2').style.visibility = 'hidden';
		  document.getElementById('paris-adicional').style.visibility = 'hidden';
		  document.getElementById('bancaria').style.visibility = 'hidden';
		  $('formasDePago').style.height = '90px';
		  document.getElementById('mpa').style.visibility = 'hidden';
		} else {
		  document.getElementById('paris').style.visibility = 'visible';
		  document.getElementById('parisnormal').style.visibility = 'hidden';	
		  document.getElementById('jumbo2').style.visibility = 'hidden';
		  document.getElementById('masparis').style.visibility = 'hidden';
		  document.getElementById('paris-adicional').style.visibility = 'hidden';
		  document.getElementById('bancaria').style.visibility = 'hidden';
		  $('formasDePago').style.height = '90px';
		  document.getElementById('mpa').style.visibility = 'hidden';
		}
	} else if (value == 3) {
		document.getElementById('jumbo2').style.visibility = 'visible';
		document.getElementById('paris').style.visibility = 'hidden';
		document.getElementById('paris-adicional').style.visibility = 'hidden';
		document.getElementById('parisempleado').style.visibility = 'hidden';
		document.getElementById('parisnormal').style.visibility = 'hidden';
		document.getElementById('masparis').style.visibility = 'hidden';
		document.getElementById('bancaria').style.visibility = 'hidden';
		$('formasDePago').style.height = '130px';
		document.getElementById('mpa').style.visibility = 'hidden';
		document.frmPedido.p_numtja.value = "";
		document.frmPedido.titular[0].checked = false;
		document.frmPedido.titular[1].checked = false;
		
	} else if (value == 4) {
		document.getElementById('bancaria').style.visibility = 'visible';
		document.getElementById('jumbo2').style.visibility = 'hidden';
		document.getElementById('paris').style.visibility = 'hidden';
		document.getElementById('paris-adicional').style.visibility = 'hidden';
		document.getElementById('parisempleado').style.visibility = 'hidden';
		document.getElementById('parisnormal').style.visibility = 'hidden';
		document.getElementById('masparis').style.visibility = 'hidden';
		$('formasDePago').style.height = '270px';
		document.getElementById('mpa').style.visibility = 'hidden';
		document.frmPedido.p_numtja.value = "";
		document.frmPedido.titular[0].checked = false;
		document.frmPedido.titular[1].checked = false;
	} else if (value == 5) {
		document.getElementById('bancaria').style.visibility = 'hidden';
		document.getElementById('jumbo2').style.visibility = 'hidden';
		document.getElementById('paris').style.visibility = 'hidden';
		document.getElementById('paris-adicional').style.visibility = 'hidden';
		document.getElementById('parisempleado').style.visibility = 'hidden';
		document.getElementById('parisnormal').style.visibility = 'hidden';
		document.getElementById('masparis').style.visibility = 'hidden';
		$('formasDePago').style.height = '230px';
		document.getElementById('mpa').style.visibility = 'visible';
		document.frmPedido.p_numtja.value = "";
		document.frmPedido.titular[0].checked = false;
		document.frmPedido.titular[1].checked = false;
	}
	if (document.frmPedido.tipo_documento[1].checked == true) {
		datosFactura('visible'); 
	} else {
		datosFactura('hidden');
	}
}

function parisLayer(numero_tarjeta,evento) {
	var numero = document.frmPedido.p_numtja.value;	
	var tipo = validaTipoParis(numero,evento);
	if (tipo == "PARIS") {
		//tarjeta paris normal
		if (document.getElementById('parisnormal').style.visibility == 'hidden') {
			if ( $('factura').style.visibility == 'visible' ) {
				$('formasDePago').style.height = '480px';
				$('parisnormal').style.top = '135px';
				$('div_factura').style.top = '225px';
			} else {
				$('formasDePago').style.height = '180px';
				$('parisnormal').style.top = '135px';			
			}
			document.getElementById('parisnormal').style.visibility = 'visible';
			document.getElementById('masparis').style.visibility = 'hidden';
			document.frmPedido.tipo_paris.value = tipo;
			return;
		}		
	} else if (tipo == "MASPARIS") {
		//tarjeta mas paris
		if (document.getElementById('masparis').style.visibility == 'hidden') {
			if ( $('factura').style.visibility == 'visible' ) {
				$('formasDePago').style.height = '440px';
				$('masparis').style.top = '135px';
				$('div_factura').style.top = '190px';
			} else {
				$('formasDePago').style.height = '140px';
				$('masparis').style.top = '135px';
			}
			$('masparis').style.visibility = 'visible';
			document.getElementById('parisnormal').style.visibility = 'hidden';
			document.getElementById('paris-adicional').style.visibility = 'hidden';	
			document.frmPedido.tipo_paris.value = tipo;
			document.frmPedido.titular[0].checked = false;
			document.frmPedido.titular[1].checked = false;		
			return;	
		}		
    } else if (tipo == "NO") {
		//Si hay algún problema con el número
		if (evento == "onblur") {		
			alert("Por favor, Ingrese un número de tarjeta válido");
		}
		document.frmPedido.tipo_paris.value="";		  
		return;	
	}
}
function titularAdicional() {
	if (document.frmPedido.titular[1].checked) {
		$('formasDePago').style.height = '400px';
		$('paris-adicional').style.top = '225px';
    	$('paris-adicional').style.visibility = 'visible';
		for ( var i=0; i < document.frmPedido.tipo_documento.length; i++ ) {
			if (document.frmPedido.tipo_documento[i].checked) {
				if ( document.frmPedido.tipo_documento[i].value == 'F' ) {
					datosFactura('visible');
				}
			}
		}
	} else {
		$('formasDePago').style.height = '180px';
	    $('paris-adicional').style.top = '220px';	
		$('paris-adicional').style.visibility = 'hidden';
		for ( var i=0; i < document.frmPedido.tipo_documento.length; i++ ) {
			if (document.frmPedido.tipo_documento[i].checked) {
				if ( document.frmPedido.tipo_documento[i].value == 'F' ) {
					datosFactura('visible');
				}
			}
		}
    }
}
*/
function muestraPromos() {
		document.getElementById('textoahorro').style.visibility = 'visible';
		document.getElementById('ahorro').style.visibility = 'visible';
		document.getElementById('promomas').style.visibility = 'visible';
		document.getElementById('fondo').style.backgroundImage = "url('/FO_IMGS/img/estructura/paso3/tabla-abajo.gif')" ;
		document.getElementById('fondo').style.backgroundRepeat = "no-repeat";
}
function escondePromos() {
		document.getElementById('textoahorro').style.visibility = 'hidden';
		document.getElementById('ahorro').style.visibility = 'hidden';
		document.getElementById('promomas').style.visibility = 'hidden';
		document.getElementById('fondo').style.backgroundImage = "url('/FO_IMGS/img/estructura/paso3/tabla-abajo-sa.gif')";
		document.getElementById('fondo').style.backgroundRepeat = "no-repeat";
}	
function carga_mes_ano_coutas() {
	var formulario = document.frmPedido;
	objeto = formulario.t_mes;
	cant = objeto.options.length;
	for( var i = 0; i < cant; i++ ) {
		if( objeto.options[i].value == formulario.t_auxmes.value ) {
			objeto.selectedIndex = i;
			break;
		}
	}	
	objeto = formulario.t_ano;
	cant = objeto.options.length;
	for( var i = 0; i < cant; i++ ) {
		if( objeto.options[i].value == formulario.t_auxano.value ) {
			objeto.selectedIndex = i;
			break;
		}
	}
	objeto = formulario.t_cuotas;
	cant = objeto.options.length;
	for( var i = 0; i < cant; i++ ) {
		if( objeto.options[i].value == formulario.t_auxcuotas.value ) {
			objeto.selectedIndex = i;
			break;
		}
	}
}
var imagen_out = '/FO_IMGS/img/estructura/paso3/bt_recalcular-gris.gif';
var imagen_over = '/FO_IMGS/img/estructura/paso3/bt_recalcular-grisb.gif';
function change_img( imagen, tipo ) {
	if( tipo == 'over' )
		MM_swapImage(imagen,'',imagen_over,1);
	else if( tipo == 'out' )
		MM_swapImage(imagen,'',imagen_out,1);
}
function insRow( numero ) {
  var x=window.document.getElementById('t_cupones').insertRow(-1)
  var c1=x.insertCell(0)
  var c2=x.insertCell(1)
  c1.innerHTML=numero;
  c2.innerHTML='<A HREF="javascript:delRow('+x.rowIndex+')" title="Eliminar cupón"><img src="/FO_IMGS/img/estructura/paso3/eliminar.gif" width="14" height="10" border="0" alt="Eliminar Cupón" title="Eliminar Cupón" /></a>';
}
// Eliminar todas las filas
function clearTable() {
	while (window.document.getElementById('t_cupones').rows.length>1)
		window.document.getElementById('t_cupones').deleteRow(1)
}  
function delRow(r) {
	var form = top.frames['frm_prod'].document.form_pro;
    var cupones = form.cupones.value.split("-=-");
	form.cupones.value = "";
	for( var i = 0; i < cupones.length-1; i++ ) {
		if( i != r-1 ) {
			form.cupones.value += cupones[i] + "-=-";
		}
	}  
	frm_prod.actualizar_productos();
}
function loadRows() {
	clearTable();
	var form = top.frames['frm_prod'].document.form_pro;
	var cupones = form.cupones.value.split("-=-");
	for( var i = 0; i < cupones.length-1; i++ ) {
		insRow( cupones[i] );
	}
}  

function add_cupon() {
	var form = top.frames['frm_prod'].document.form_pro;
	form.cupones.value += window.document.fcupones.cupon.value+"-=-";
	window.document.fcupones.cupon.value = "";
	frm_prod.actualizar_productos();
}
// -- confirmardatos --
function cambioMail() {
	$('confirmar_mail').show();
	cambio_mail = 1;
	cambioDatos();
}
function cambioDatos() {
	cambio_datos = 1;
}
// --- Mostrar las etapas del paso 3 ---
function mostrarCondirmarDatos() {
	dw_scrollObj.scrollToId('wn', 'confirmar', 'undefined', 'undefined');
	selConfirmar();
}
function mostrarDespacho() {
	dw_scrollObj.scrollToId('wn', 'despacho', 'undefined', 'undefined');
	selDespacho();
}
function mostrarPago() {
	dw_scrollObj.scrollToId('wn', 'pago', 'undefined', 'undefined');
	selPago();
}
// --- Acciones de ir ---
function irPago() {
	if ( $('jprecio') != null ) {
		if ( $('jprecio').value != '' ) {
			mostrarPago();
		} else {
			alert("Debe seleccionar una hora para su despacho.");
		}		
	} else {
		alert("Debe seleccionar una hora para su despacho.");
	}
}
function irDespacho() {
	for ( var i=0; i < document.frmPedido.sin_gente_op.length; i++ ) {
		if (document.frmPedido.sin_gente_op[i].checked) {
			if( document.frmPedido.sin_gente_op[i].value == 0 ) {
				if ( Trim($('sin_gente_txt').value) == '' ) {
					alert("Debe ingresar el nombre");
					$('sin_gente_txt').focus();
					return;
				}
			}
		}
	}
	if ( Trim($('email1').value) == '' ) {
		alert("Debe ingresar su correo");
		$('email1').focus();
		return;
	}
	if ( Trim($('dominio1').value) == '' ) {
		alert("Debe completar su correo");
		$('dominio1').focus();
		return;
	}
	if ( Trim($('fon_num_2').value) == '' ) {
		alert("Debe ingresar el número de teléfono");
		$('fon_num_2').focus();
		return;
	} else {
		if (!validaNumeros( $('fon_num_2').value )) {
			alert("Debe ingresar un número válido");
			$('fon_num_2').focus();
			return;
		}
	}
	if (cambio_datos == 1) {
		if (cambio_mail == 1) {
			if ( Trim($('email1').value) !=  Trim($('email2').value)) {
				alert("Reingrese su correo");
				$('email1').focus();
				return;			
			}
			if ( Trim($('dominio1').value) !=  Trim($('dominio2').value)) {
				alert("Reingrese su correo");
				$('dominio1').focus();
				return;			
			}
			if (!validarEmailDominio($('email1'),$('dominio1'))) {
				return;
			}
		}
		var requestOptions = {
			'method': 'post',
			'parameters': "email1=" + $('email1').value + "&dominio1=" + $('dominio1').value + "&fon_cod_2=" + $('fon_cod_2').value + "&fon_num_2=" + $('fon_num_2').value ,
			'onSuccess': function(REQUEST) {
					if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
						alert("Ocurrió un error al guardar la información");
						return;
					}
					var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
					if (mensaje == 'OK') {
						cambio_datos = 0;
						cambio_mail = 0;
						mostrarDespacho();
						return;
					} else {
						alert(mensaje);
						return;
					}			
			}
		};
		new Ajax.Request('ChangeDatosPaso3mobi', requestOptions);
	} else {
		mostrarDespacho();
	}
}
var downStrokeField; 
function autojump(fieldName,nextFieldName,fakeMaxLength){ 
	 var myForm=document.forms[document.forms.length - 1]; 
	 var myField=myForm.elements[fieldName]; 
	 myField.nextField=myForm.elements[nextFieldName]; 
	 if (myField.maxLength == null) 
		myField.maxLength=fakeMaxLength; 
	 myField.onkeydown=autojump_keyDown; 
	 myField.onkeyup=autojump_keyUp; 
} 
function autojump_keyDown(){ 
	 this.beforeLength=this.value.length; 
	 downStrokeField=this; 
} 
function autojump_keyUp() { 
	 if ( (this == downStrokeField) && (this.value.length > this.beforeLength) && (this.value.length >= this.maxLength) ) 
	 this.nextField.focus(); 
	 downStrokeField=null; 
} 

/*** COMPRAR ***/

function disabledBuy() {
  //console.log("*** DESHABILITAMOS EL BOTON COMPRAR ***");
  $j('#confirmar').href="javascript:;";
}
function enabledBuy() {
  //console.log("*** HABILITAMOS EL BOTON COMPRAR ***");
  $j('#confirmar').href="javascript:preOrderProcess();";
}

// BUY STEP 1: Validamos carro de compra
function preOrderProcess() {
    /** INI BORRAR DESPUES - SOLO PAGO TBK 
    var mpago = Form.getInputs('frmPedido','radio','forma_pago').find(function(radio) { return radio.checked; }).value;
    if ( mpago == 1 ) {    
      var ruts = ["13827994","10914467","14048436","14320349","22167634","12887776","13545334"];
      var entrar = false;
      for (var i = 0; i < ruts.length; i++ ) {
        if ( ruts[i] == document.frmPedido.rut_cli.value ) {
          entrar = true;
        }
      }
      if ( !entrar ) {
        alert("Estimado cliente:\nEn estos momentos el medio de pago Tarjeta Más no se encuentra disponible.\nEstamos trabajando para usted.\n\nMuchas gracias.");
        return;
      }
    }
    FIN BORRAR **/
  
  disabledBuy();
  
  var form = document.frmPedido;
  if ( form.total_compra.value == 0 ) {
    alert("Debes agregar productos al carro de compras");
    enabledBuy();
    return;
  }
  var jpic = form.jpicking.value;
  var jpre = form.jprecio.value;
  var jdes = form.jdespacho.value;
  var jfec = form.jfecha.value;
  var tdes = form.tipo_despacho.value;
  var zdes = form.zona_despacho.value;
  var horsdes = form.horas_economico.value;
  if ( jpic == "" || jpre == "" || jdes == "" ) {
    alert( "Debes seleccionar un horario de despacho." );
    enabledBuy();
    return;
  }
  if (form.sin_gente_op[0].checked == true && form.sin_gente_txt.value == '') {
    alert("Indicanos con quién dejar el pedido");
    form.sin_gente_txt.focus();
    enabledBuy();
    return;
  }
  if ( $('forma_pago').value == '0' ) {
    alert("Debe seleccionar el medio de pago");
    enabledBuy();
    return;
  }
  //Antes de comprar verificamos el carro del cliente
  var requestOptions = {
    'asynchronous':false,
    'method': 'post',
    'parameters': null,
    'onSuccess': function (REQUEST) {
      if (REQUEST.responseXML != null) {
        if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] != null) {
          //en caso de q no venga el msg, al gatillar la accion de confirmar la compra el sistema mandara pantalla de error o al login
          var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
          if (mensaje != 'OK') {
            var msg = "Usted tiene " + mensaje + " en el carro de compra.\n";
            msg += "Estos productos no serán agregados a su pedido.\n\n";
            msg += "¿Desea revisar los productos de su carro antes de finalizar su compra?";
            if ( confirm( msg ) ) {
              enabledBuy();
              document.location.href = "OrderItemDisplayMobi";
              return;
            }
          }
          preOrderProcessHora();
        }
      }
    }
  };
  new Ajax.Request('ValidaCarroCompras', requestOptions);
}
// BUY STEP 2: Verificamos si la venta de jornada aun esta activa
function preOrderProcessHora() {
  var form = document.frmPedido;
  //Antes de comprar verificamos si la venta de jornada aun esta activa
  var requestOptions = {
    'asynchronous':false,
    'method': 'post',
    'parameters': "id_jdespacho="+form.jdespacho.value+"&tipo_despacho="+form.tipo_despacho.value,
    'onSuccess': function (REQUEST) {
      if (REQUEST.responseXML != null) {
        if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] != null) {
          //en caso de q no venga el msg, al gatillar la accion de confirmar la compra el sistema mandara pantalla de error o al login
          var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
          if (mensaje != 'OK') {
            alert(mensaje);
            enabledBuy();
            return;
          }
          orderprocess();
        }
      }
    }
  };
  new Ajax.Request('ValidaHoraCompra', requestOptions);
}

// BUY STEP 3: Guardamos el pedido en estado PRE-ingresado
function orderprocess() {
  //console.log("Ir a guardar el pedido");
  var requestOptions = {
    'asynchronous':false,
    'method': 'post',
    'parameters': $('frmPedido').serialize(),
    'onSuccess': function (REQUEST) {
      if (REQUEST.responseXML != null) {
        if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] != null) {
          //en caso de q no venga el msg, al gatillar la accion de confirmar la compra el sistema mandara pantalla de error o al login
          var mensaje = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
          var id_pedido = REQUEST.responseXML.getElementsByTagName("id_pedido")[0].childNodes[0].nodeValue;
          var monto = REQUEST.responseXML.getElementsByTagName("monto")[0].childNodes[0].nodeValue;
          
          if ( mensaje != 'OK' ) {
            alert(mensaje);
            enabledBuy();
            return;
          }
          //var medio_pago = Form.getInputs('frmPedido','radio','forma_pago').find(function(radio) { return radio.checked; }).value;
          
          if ( $('forma_pago').value == '2' ) {
            $('TBK_ORDEN_COMPRA').value = id_pedido;
            $('TBK_MONTO').value = monto;
            document.webpay.submit();
          } else {
          	monto = monto / 100;
            $('numeroTransaccion').value = id_pedido;
            $('idCarroCompra').value = id_pedido;
            $('montoOperacion').value = monto;
            document.cat.submit();
          }
        }
      }
    }
  };
  new Ajax.Request('OrderCreate', requestOptions);
}