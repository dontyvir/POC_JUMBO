function datosFactura(estado) {
	document.getElementById('factura').style.visibility = estado;
	if (estado == 'visible') 
		document.getElementById('div_factura').style.height = '245px';
	else
		document.getElementById('div_factura').style.height = '0px';
}

var flag_act = false;
function showActualizaDatosLayer() {
	var frame = document.getElementById('i_act_datos');
	frame.style.height = '190px';
	if (flag_act)
		frame.src = '/FO_WebContent/layers/act_mail.html';
	var layerOlvidaClave = document.getElementById('act_datos');
	layerOlvidaClave.style.visibility = 'visible';
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	layerOlvidaClave.style.width = '350px';
	layerOlvidaClave.style.height = '190px';
	layerOlvidaClave.style.left = (ancho-350)/2 + 'px';
	layerOlvidaClave.style.top = (largo)/2 - 190 + 'px';
	flag_act = true;
}

function hideActualizaDatosveLayer() {
	var layerOlvidaClave = document.getElementById('act_datos');
	layerOlvidaClave.style.visibility = 'hidden';	
	layerOlvidaClave.style.width = '0px';
	layerOlvidaClave.style.height = '0px';
}

function validaOrderProcess() {
	var form = document.frmPedido;	
    
	if ( form.tipo_documento[1].checked == true) {
	  //VALIDACIONES PARA EL CASO SELECCION DE FACTURA
	  if (!form.fac_rut.value) {
	    try{mxTracker._trackEvent('Despacho','Datos de Factura','Debes ingresar el campo RUT');}catch(e){};
	    alert('Debes ingresar el campo RUT');
	    form.fac_rut.focus();
	    return false;
	  }
	  if (!form.fac_dv.value) {
	    try{mxTracker._trackEvent('Despacho','Datos de Factura','Debes ingresar el campo dígito verificador');}catch(e){};
	    alert('Debes ingresar el campo dígito verificador');
	    form.fac_dv.focus();
	    return false;
	  }
	  if (!checkRutField( form.fac_rut.value+"-"+form.fac_dv.value, form.fac_rut ) ) {
	    try{mxTracker._trackEvent('Despacho','Datos de Factura','El RUT no es valido');}catch(e){};
	    return false;
	  }
	  if (Trim(form.fac_razon.value) == "") {
	    try{mxTracker._trackEvent('Despacho','Datos de Factura','Debes ingresar el campo razón social');}catch(e){};
	    alert("Debes ingresar el campo razón social");
	    form.fac_razon.focus();
	    return false;
	  }
	  if (Trim(form.fac_giro.value) == "") {
	    try{mxTracker._trackEvent('Despacho','Datos de Factura','Debes ingresar el campo giro');}catch(e){};
	    alert("Debes ingresar el campo giro");
	    form.fac_giro.focus();
	    return false;
	  }
	  if (Trim(form.fac_direccion.value) == "") {
	    try{mxTracker._trackEvent('Despacho','Datos de Factura','Debes ingresar el campo dirección');}catch(e){};
	    alert("Debes ingresar el campo dirección");
	    form.fac_direccion.focus();
	    return false;
	  }
	  if (form.fac_fono.value == "") {
	    try{mxTracker._trackEvent('Despacho','Datos de Factura','Debes ingresar el campo teléfono');}catch(e){};
	    alert("Debes ingresar el campo teléfono");
	    form.fac_fono.focus();
	    return false;
	  }
	  if (Trim(form.fac_comuna.value) == "") {
	    try{mxTracker._trackEvent('Despacho','Datos de Factura','Debes ingresar el campo Comuna');}catch(e){};
	    alert("Debes ingresar el campo Comuna");
	    form.fac_comuna.focus();
	    return false;
	  }
	  if (Trim(form.fac_ciudad.value) == "") {
	    try{mxTracker._trackEvent('Despacho','Datos de Factura','Debes ingresar el campo Ciudad');}catch(e){};
	    alert("Debes ingresar el campo Ciudad");
	    form.fac_ciudad.focus();
	    return false;
	  }
	}
	if (form.terminos && form.terminos.checked == false) {
	    try{mxTracker._trackEvent('Pago','Terminos y condiciones','Debes aceptar los terminos y condiciones');}catch(e){};
		alert("Debes aceptar los términos y condiciones");
	    return false;
	}	
	return true;
}

function disabledBuy() {
  //console.log("*** DESHABILITAMOS EL BOTON COMPRAR ***");
  //$j('#confirmar').href="javascript:;";
}
function enabledBuy() {
  //console.log("*** HABILITAMOS EL BOTON COMPRAR ***");
  //$j('#confirmar').href="javascript:preOrderProcess();";
}

// BUY STEP 1: Guardamos el pedido en estado PRE-ingresado
function orderprocess() {

  var requestOptions = {	
    'method': 'post',
    'parameters': $j('#frmPedido').serialize(),
    'onSuccess': function (REQUEST) {
    	$j('#botonera_pago').show();
   	  	$j('#load_botonera_pago').hide();
      if (REQUEST.responseXML != null) {
        if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] != null) {
          //en caso de q no venga el msg, al gatillar la accion de confirmar la compra el sistema mandara pantalla de error o al login
          var mensaje = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
          var id_pedido = REQUEST.responseXML.getElementsByTagName("id_pedido")[0].childNodes[0].nodeValue;
          var monto = REQUEST.responseXML.getElementsByTagName("monto")[0].childNodes[0].nodeValue;
          //alert("mensaje: " + mensaje + "  | id_pedido: " + id_pedido + "  |  monto: " + monto);
          
          if ( mensaje != 'OK' ) {
        	  if(mensaje == 'ERR_CAPACIDAD'){
        		  $j('#Content_btn_cal_pago').hide();
        		  tb_show('Jumbo.cl','#TB_inline?height=350&width=670&inlineId=hiddenModalContent');
        		  //Maxbell Inconsistencias v3
        		  var urlCal="AjaxDespachoChart?actualiza_capacidad=true&rndmx="+Math.floor(Math.random()*10000);
        		  $j('#Content_cal_pago_horarios').html("<span class=\"cargando_cal_pago\">Cargando....<span>");
        		  new Ajax.Request(urlCal,{'requestHeaders':{"MyFOReferer":'Pago'},'onSuccess':
					  function(transport){        					  
					  	$j('#Content_cal_pago_horarios').html(transport.responseText);
					  	$j('#programado').removeClass("contenedor_calendario_2012").addClass("contenedor_calendario_pago" );
					  	$j('#programado > div').removeClass("fila_direccion_2012").addClass("fila_direccion_pago" );    
					  	$j('#programado > table > tbody > tr > td > div').each(					
								function (index) {
									if( $j(this).attr("class") == "linea_abajo_calendario"){
										$j(this).removeClass("linea_abajo_calendario").addClass("linea_abajo_calendario_pago" );  	
									}
								}
							);
					  	$j('#Content_btn_cal_pago').show();
					  }
				  }
        		  );
        		  return false;
        	  }
        	  else{
			    tag_evento_google("ERROR: OrderCreate no retorna OK. Msg->" + mensaje,document.frmPedido.rut_cli.value);
	            alert(mensaje);
	            //enabledBuy();
	            return;
        	  }
          }
		  tag_evento_google("OrderCreate retorna ok",document.frmPedido.rut_cli.value);
          var medio_pago = 0;
          if($j('#ronline').attr('checked') == true){
              medio_pago = $j('#ronline').val();
              //alert("Tarjeta Más");
          }
          if($j('#tbancaria').attr('checked') == true){
              medio_pago = $j('#tbancaria').val();
              //alert("Tarjeta Bancaria");
          }
          //var medio_pago = Form.getInputs('frmPedido','radio','forma_pago').find(function(radio) { return radio.checked; }).value;
          //alert("medio_pago: " + medio_pago);
          if ( medio_pago == 2 ) {
            $j('#TBK_ORDEN_COMPRA').val(id_pedido);
            $j('#TBK_MONTO').val(monto);
            //togglePago();
            //window.scrollTo(0,0);
            //document.webpay.target = "centroDePago";
			tag_evento_google("Submit a Webpay!",document.frmPedido.rut_cli.value);
            document.webpay.submit();
            //alert("pago con webpay");
          } else {
          	monto = monto / 100;
            $j('#numeroTransaccion').val(id_pedido);
            $j('#idCarroCompra').val(id_pedido);
            $j('#montoOperacion').val(monto);
            togglePago();
            window.scrollTo(0,0);
            document.cat.target = "centroDePago";
			tag_evento_google("Submit a CAT!",document.frmPedido.rut_cli.value);
            document.cat.submit();
            //alert("pago con tarjeta mas");
          }
        }
		else {
			tag_evento_google("ERROR: REQUEST.responseXML.getElementsByTagName('respuesta')[0] retorna null en OrderCreate.",document.frmPedido.rut_cli.value);
		}
      }
	  else {
		tag_evento_google("ERROR: REQUEST.responseXML retorna null en OrderCreate.",document.frmPedido.rut_cli.value);
	  }
    }
  };
  
  if (validaOrderProcess()) {
	  $j('#botonera_pago').hide();
	  $j('#load_botonera_pago').html($j('#loading').html());
	  new Ajax.Request('OrderCreate', requestOptions);
  }else{
	  return false;
  }
 
}

function togglePago() {
	var ele = document.getElementById("cuerpo3");
	var elePago = document.getElementById("cuerpo3Pago");
    		
   	ele.style.visibility="hidden";
	ele.style.position="absolute";
	ele.style.top="0px";
	ele.style.left="0px";
	ele.style.width="100px";
	ele.style.height="100px";
	ele.style.overflow="auto";

	elePago.style.display="block";
	elePago.style.visibility="visible";
	
} 

function MM_openBrWindow(theURL,winName,features) {
  window.open(theURL,winName,features);
}

function tag_evento_google(tag,rutcli){
	/*
		if (rutcli != "5511202" && rutcli != "99999998")
		{
			try { 
				var pageTracker = _gat._getTracker("UA-1529321-1"); 
				pageTracker._trackPageview();
				pageTracker._trackEvent('Pagos', 'intento',tag);
			} catch(err) {}
		}
	*/
}

//Calendario Despacho
var objectSelected = null;
var defaultStyle = null;
var defaultStyleSelected = null;

function cambiarEstiloCal(element, newStyle) {
	if (objectSelected != null) {
		if (element != objectSelected)
			element.className = newStyle;
	} else 
		element.className = newStyle;
}

function seleccionarElementoCal(element, newStyle, tipo) {
	if (objectSelected != null) {
		if (tipoOld == 'C') {
			objectSelected.className = 'calendarioActivoExtendido'; // elemTmp.className; // oldStyle;
		} else if (tipoOld == 'E') {
			objectSelected.className = 'calendarioExpress';
		} else {
			objectSelected.className = 'calendarioactivo';
		}
	}
	tipoOld = tipo;
	element.className = newStyle;
	objectSelected = element;
}

function send_despacho( desp, precio, pick, fecha, tipo, clave ) {
	var precio_aux = precio.replace("$","").replace(".","");
	if (tipo != 'C') {
		document.t_picking.jpicking.value = pick ;
	    document.t_picking.horas_economico.value = "";
    } else {
    	document.t_picking.jpicking.value = '0';
    	document.t_picking.horas_economico.value = pick;
    }
    document.t_picking.jprecio.value = precio_aux;
    document.t_picking.jdespacho.value = desp;
    document.t_picking.jfecha.value = fecha;
    document.t_picking.tipo_despacho.value = tipo;
    document.t_picking.jclave.value = clave;
}