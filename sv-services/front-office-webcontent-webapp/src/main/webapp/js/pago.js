var XBROWSER = navigator.appName;
var $j = jQuery.noConflict();

function add_cupon() {
	if ( Trim($j('#cupon').val()) == '' ) {
		alert("Debe ingresar el número de cupón.");
		$j('#cupon').focus();
		try{mxTracker._trackEvent('Pago','Ingresar cupon','Debe ingresar el numero de cupon');}catch(e){};
		return;
	}
	var params = "?cupones=" + $j('#cupon').val();
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			if (REQUEST.responseXML != null) {
       			var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
       			var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
       			if (respuesta == "OK") {
       				try{mxTracker._trackEvent('Pago','Ingresar cupon','Cupon ingresado');}catch(e){};
       				window.location.reload(true);
       			} else{
       				alert(mensaje);	
       				try{mxTracker._trackEvent('Pago','Ingresar cupon',mensaje);}catch(e){};
       			}
       			$j('#cupon').val('');
          	}
		}
	};
	new Ajax.Request('CuponDsctoCheck', requestOptions);
}

function getTotalizador() {
    tag_evento_google("Inicio llamada getTotalizador",document.frmPedido.rut_cli.value);
	var params = "";
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			if (REQUEST.responseText != null) {
			    tag_evento_google("llamada a getTotalizador retorna OK",document.frmPedido.rut_cli.value);
    			$j('#totales').html("" + REQUEST.responseText);
    			$j('#confirmar').attr('href','javascript:orderprocess();');

    		}	
			else {
				tag_evento_google("ERROR: REQUEST.responseText da null en getTotalizador",document.frmPedido.rut_cli.value);
			}
		}
	};
	new Ajax.Request('AjaxPago', requestOptions);
}


// Tablas dinámicas para los cupones
function agregaFilaCupon( numero ) {
	var x=window.document.getElementById('t_cupones').insertRow(-1);
	var c1=x.insertCell(0);
	var c2=x.insertCell(1);
	var c3=x.insertCell(2);
	c1.innerHTML='<span class="tit_campos" style="font-weight: bold;width:120px;">Cupon Ingresado</span>';
	c2.innerHTML='<input type="text" name="aa" readonly="readonly" class="campo" maxlength="30" size="26" value="' + numero + '">';
	c3.innerHTML='<A HREF="javascript:eliminaFilaCupon('+x.rowIndex+')" title="Eliminar cupón"><img src="/FO_IMGS/img/estructura/paso3/eliminar.gif" width="14" height="10" border="0" alt="Eliminar Cupón" title="Eliminar Cupón" align="left" /></a>';
}	

function eliminaFilaCupon(r) {
	var form = document.form_pro;
    var cupones = form.cupones.value.split("-=-");
	form.cupones.value = "";
	for( var i = 0; i < cupones.length-1; i++ ) {
		if( i != r-1 ) {
			form.cupones.value += cupones[i] + "-=-";
		}
	}  
	actualizar_productos();
}

function DescuentosPromoTMAS(){
    if ($j('#PromocionesTMAS').is(':hidden')) {
        //alert("invisible");
        $j('#PromocionesTMAS').show();
    }else if ($j('#PromocionesTMAS').is(":visible")){
        //alert("visible");
        $j('#PromocionesTMAS').hide();
    }
}


function DescuentosPromoWEBPAY(){
    if ($j('#PromocionesWEBPAY').is(':hidden')) {
        //alert("invisible");
        $j('#PromocionesWEBPAY').show();
    }else if ($j('#PromocionesWEBPAY').is(":visible")){
        //alert("visible");
        $j('#PromocionesWEBPAY').hide();
    }
}
