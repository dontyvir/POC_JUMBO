var subcatId=0;
var $j = jQuery.noConflict();
var nota_paso='1';
var id_formulario;
var nota_txt;

var XACT_CARRO 		= "";
var XID_PRODUCTO 	= 0;
var XCANTIDAD 		= 0;
var XNOTACAMPO 		= "";
var XFORM;
var XID_CAB			= 0;
var XID_CAT			= 0;
var XID_SUBCAT		= 0;
var XBROWSER 		= navigator.appName;

var XITEMSXPAGINA 		= 30;
var XHORIZONTAL 		= 0;

jQuery().ready(function() {

	$j(function(){
		(function($j){
			$j.fn.accordion = function(custom) {
				var defaults = {
					keepOpen: false,
					startingOpen: false
				}
				var settings = $j.extend({}, defaults, custom);
				if(settings.startingOpen){
					$j(settings.startingOpen).show();
				}
			
				return this.each(function(){
					var obj = $j(this);
					$j('li a', obj).click(function(event){
						var elem = $j(this).next();
						if(elem.is('ul')){
							event.preventDefault();
							if(!settings.keepOpen){
								obj.find('ul:visible').not(elem).not(elem.parents('ul:visible')).slideUp();
							}
							elem.slideToggle();
						}
					});
				});
			};
		})(jQuery);
		
	    //$j('#menuvertical').accordion({keepOpen:false});
	
	});
	
	////Para hacer el acordi�n////////
	//jQuery('#navigation').accordion({active: false, alwaysOpen: false, navigation: true, header: '.head', autoheight: false});
	//////////////////////////////////
	$j('#loading').hide();
	
	/*$j("input#patron").autocomplete("/FO/PasoDosAutocompletar", {
		minChars: 3,
		width: 220,
		max: 40,
		mustMatch: false,
		selectFirst: false,
		//extraParams: { t: function() { return $j("input#patron").val() } },
		formatItem: formatItem,
		formatResult: formatResult
	});*/
	
	$j("#acerrar").click(function(e){
		e.preventDefault();
		select.hide();
	});
	
	//////////////////////////////////
	////Para cambiar imagen de fondo en categorias
	////Luego no funciona el hover por css as� es que se hace por jquery
	i=0;
	
	//////////////////////////////////
	/*$j('div#carro_de_compras').ready(function() {
		//actualizaCarro();
	});*/
	//////////////////////////////////
	/////////////////////////////////
	/*$j('img#imgBuscar').click(function(e) {
		e.preventDefault();
		buscar();
	});*/
	
	/*$j('input#patron').keypress(function(e) {
		if(e.keyCode == 13) {
			buscar();
			e.preventDefault();
		}
	});*/
	
	/*$j('input#patron').focus(function() {
		if ($j(this).val() == 'Buscar')
			$j(this).val('');
	});*/
});

function muestraProdHome(){
    //alert("(jQuery().ready) cab: " + $j('#cab').val() + ", int: " + $j('#int').val() + ", ter: " + $j('#ter').val());
	//ir a categor�a desde link externo
//05-10-2012 Mauricio Farias
	if ($j('#ter').val() >= 0){
		muestraterminal($j('#cab').val(), $j('#int').val(), $j('#ter').val(), 15);
	} else if ($j('#cab').val() > 0){
		muestracabecera($j('#cab').val(), 15);
	}
}



function actualizaCarro() {
  $j.get("/FO/OrderItemDisplay", function(datos) {
    $j('div#carro_de_compras').html(datos);
    descuento = $j('input#total_desc_carro_compras_sf').val();
//+20120216coh ahora el total es con el descuento
//    $j('input#_total').val($j('input#total_carro_compras').val());
    $j('input#_total').val($j('input#total_desc_carro_compras_tc').val());
//-20120216coh
    $j('input#_total_desc').val($j('input#total_desc_carro_compras_tc').val());

    //+20120215coh
    $j('div#_total_ahorro').text($j('input#total_desc_carro_compras_tc_sf').val());//total_carro_compras - promo_desc_carro_compras
    $j('div#_total_tarjeta_mas').text($j('input#total_desc_carro_compras').val());
    $j('div#_total_ahorro_tarjeta_mas').text($j('input#total_desc_carro_compras_sf').val());
    //-20120215coh
    
    //por compatibilidad con codigo antiguo que sobrevivi�
	$j('input#_total_desc_txt').val($j('input#promo_desc_carro_compras').val()); 
//+20120216coh 
	  $j('div#inputtotdesc').hide();
/*	if ( descuento == '$0' ) {
	  $j('div#inputtotdesc').hide();
	} else {
	  $j('div#inputtotdesc').show();
	}
*/
//-20120216coh 
  });
}

/*Esto para hacer compatible carro.js con paso2*/
function actualizaListaProductos(){
}

function verificalista() {
	if ( $j('#cant_reg').val() != null || typeof $j('#cant_reg').val() != 'undefined') {
		if ( $j('#cant_reg').val() == 0 ) {
			alert("No existen productos en el carro");			
		} else {
			showGuardaLista();
		}
	} else {
		alert("No existen productos en el carro");	
	}
}


function llenarCarroCompras() {
	//actualizaCarro();
}


/*function buscar(sug){
	$j('#loading').show();
	
	var texto = $j("input#patron").val();
	var sugeren = "";
	if (sug != null && typeof sug != 'undefined'){
		texto = sug;
		if (typeof $j("input#sugerencias").val() != 'undefined')
			sugeren = $j("input#sugerencias").val();
	}*/
	
	//expr = /\s+\*/;
/*	texto = texto.replace(expr, '*');
	$j("input#patron").val(texto);
	$j.get("/FO/PasoDosResultado", {accion:"buscar", buscar: texto, sugerencias: sugeren}
	    ,function(datos){
			$j("div#div_productos").html(datos);
			$j("label#cat").html("Resultados B�squeda: " + texto);
			eventosProductos();
			$j('#loading').hide();
	});
}*/

function ficha(idprod, idform, cabecera_nombre, categoria_nombre, subcategoria_nombre) {
	top.ficha_carro = false;
	if ($j('input#nota_'+idprod).attr("className") != undefined) {
		top.nota_txt = $j('input#nota_'+idprod).val();
	} else {
		top.nota_txt = "";
	}
	top.id_formulario = idform;
	top.frames['frm_ficha'].location.href="ProductDisplay?idprod="+idprod;
}

function cantidadPorPagina(cantidad) {
	m = $j(':selected', $j(document).find('select#marca')).text();
	id1 = $j(document).find('select#marca').val();
	XITEMSXPAGINA=cantidad;

	$j('table#tabla_productos tbody tr td ul li').each(function() {
		if( id1 == 0 || $j(this).text().indexOf(m) >= 0 )
			$j(this).children('div').show();
		else
			$j(this).children('div').hide();
	});

	$j('#resultado').pajinate({
		items_per_page : XITEMSXPAGINA,
		item_container_id : '.' + $j('table#tabla_productos tbody tr td ul').attr('class'),
		nav_label_first : '<<',
		nav_label_last : '>>',
		nav_label_prev : '<',
		nav_label_next : '>',
		num_page_links_to_display: 5
	});
}

function toNumeric(s) {
	var i = parseFloat(s);
	return (isNaN(i)) ? 0 : i;
}

function compareNum(a,b){
	precio = /\$\d+\.{0,1}\d*/;
	numero = new RegExp(/[^0-9]/g);
	aa = a.match(precio);
	aaa = aa[0].replace(numero,"");
	bb = b.match(precio);
	bbb = bb[0].replace(numero,"");
	return toNumeric(aaa) - toNumeric(bbb);
}



function formatItem(row) {
	return " <label id='lbus'>" + row[1] + " prods.</label>" + row[0];
	//return row[0];
}

function formatResult(row) {
	return row[0];
}

function showChangeComuna() {
  upWeb();
  showLightBoxCategories();
//  greenBack();
  $j('#contenedor_lightbox').show();
  $j('#logo_jumbo').show();
  $j('#comunas_sesion').show();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').show();
  $j('#opcionesRegistro').hide();
  $j('#agrega_direccion').hide();
  $j('#error_rut_existe').hide();
  $j('#mensaje_dir_exito').hide();
  //showRegionesCobertura();
}

function showChangeComunaMsj() {
  upWeb();
  greenBack();
  $j('#contenedor_lightbox').show();
  $j('#logo_jumbo').show();
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').show();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').show();
  $j('#opcionesRegistro').hide();
  $j('#agrega_direccion').hide();
  $j('#error_rut_existe').hide();
  $j('#mensaje_dir_exito').hide();
}

function showLoginSession() {
  upWeb();
  showLightBoxCategories();  
//  whiteBack();
//$j('#TB_ajaxContent').css({backgroundColor:'white'});
  //if (PASO_ACTUAL == 2){
	  $j('#contenedor_lightbox').show();
	  $j('#logo_jumbo').show();
	  $j('#comunas_sesion').hide();//hide
	  $j('#change_comuna_layer_msj').hide();//hide
	  $j('#change_login_session').show();
	  $j('#olvido_clave').hide();
	  $j('#registra_cliente').hide();
	  $j('#cont_inputs_close').show();
	  $j('#opcionesRegistro').hide();
	  $j('#agrega_direccion').hide();
	  $j('#error_rut_existe').hide();
	  $j('#mensaje_dir_exito').hide();
  /*}else if (PASO_ACTUAL == 3){
      $j('#comunas_sesion').hide();
      $j('#change_comuna_layer_msj').hide();
      $j('#change_login_session').show();
      $j('#olvido_clave').hide();
  }*/
  $j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
		  closeLBoxWin();
  });
  //*****//
}

function showRecuperaClave() {
  upWeb();
  showLightBoxCategories();  
//  whiteBack();
  $j('#contenedor_lightbox').show();
  $j('#logo_jumbo').show();
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').show();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').show();
  $j('#opcionesRegistro').hide();
  $j('#agrega_direccion').hide();
  $j('#error_rut_existe').hide();
  $j('#mensaje_dir_exito').hide();
}

function showRegistrarSession() {
  upWeb();
  showLightBoxRegistro();
  //greenBack();
  //alert("PASO_ACTUAL: " + PASO_ACTUAL);
  if (PASO_ACTUAL == 1){
  	  $j('#logo_jumbo').hide();
	  $j('#contenedor_lightbox').hide();
	  $j('#comunas_sesion').hide();
	  $j('#change_comuna_layer_msj').hide();
	  $j('#change_login_session').hide();
	  $j('#olvido_clave').hide();	
      $j('#registra_cliente').show();
  }else if (PASO_ACTUAL == 2 || PASO_ACTUAL == 3 || 
              PASO_ACTUAL == 4 || PASO_ACTUAL == 5){
      $j('#logo_jumbo').hide();
	  $j('#contenedor_lightbox').hide();
	  $j('#comunas_sesion').hide();
	  $j('#change_comuna_layer_msj').hide();
	  $j('#change_login_session').hide();
	  $j('#olvido_clave').hide();
	  $j('#registra_cliente').show();
	  $j('#cont_inputs_close').hide();
	  $j('#opcionesRegistro').hide();
	  $j('#agrega_direccion').hide();
	  $j('#error_rut_existe').hide();
	  $j('#mensaje_dir_exito').hide();
  }
  $j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
		  closeLBoxWin();
  });
}

function showMensajeRegistrarSession() {
  upWeb();
  showLightBoxMensajeRegistro();
  //greenBack();
  $j('#logo_jumbo').hide();
  $j('#contenedor_lightbox').hide();
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').hide();
  $j('#opcionesRegistro').show();
  $j('#agrega_direccion').hide();
  $j('#error_rut_existe').hide();
  $j('#mensaje_dir_exito').hide();
  $j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
		  closeLBoxWin();
  });
}

function showAgregaDireccionSession() {
  upWeb();
  showLightBoxAgregaDirecion();
  //greenBack();
  $j('#logo_jumbo').hide();
  $j('#contenedor_lightbox').hide();
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').hide();
  $j('#opcionesRegistro').hide();
  $j('#error_rut_existe').hide();
  $j('#agrega_direccion').show();
  $j('#mensaje_dir_exito').hide();
  $j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
	      //changeComunaRegistro();
		  closeLBoxWin();
  });
}

function showMensajeExitoAgregaDireccionSession() {
  upWeb();
  showLightBoxMensajeExitoAgregaDireccion();
  //greenBack();
  $j('#logo_jumbo').hide();
  $j('#contenedor_lightbox').hide();
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').hide();
  $j('#opcionesRegistro').hide();
  $j('#error_rut_existe').hide();
  $j('#agrega_direccion').hide();
  $j('#mensaje_dir_exito').show();
}

function whiteBack() {
  $j('#TB_ajaxContent').css({backgroundImage:'url("/FO_IMGS/images/html/fondo_layer_recupera.gif")'});
}

function greenBack() {
  $j('#TB_ajaxContent').css({backgroundImage:'url("/FO_IMGS/images/html/fondo_layer_categorias.gif")'});
}

function justLogin() {
  if (PASO_ACTUAL == 1 || PASO_ACTUAL == 2){
      document.acceso2.url_to.value = "/FO/CategoryDisplay";
  }else if (PASO_ACTUAL == 3){
      document.acceso2.url_to.value = "/FO/MiCarro";
  }else if (PASO_ACTUAL == 4){
      document.acceso2.url_to.value = "/FO/Despacho";
  }else if (PASO_ACTUAL == 5){
      document.acceso2.url_to.value = "/FO/Despacho";
  }
  showLoginSession();
}

function upWeb() {
  if ( XBROWSER == "Microsoft Internet Explorer" ) {
    window.scrollTo(0,0);
  }
}

function showLightBoxCategories() {
//  tb_show('Jumbo.cl','#TB_inline?height=316&width=523&inlineId=hiddenModalContent&modal=true'); 
  tb_show('Jumbo.cl','#TB_inline?height=330&width=400&inlineId=hiddenModalContent&modal=true');   
}

function showLightBoxRegistro() {
  tb_show('Jumbo.cl','#TB_inline?height=450&width=620&inlineId=hiddenModalContent&modal=true');
}

function showLightBoxMensajeRegistro() {
  tb_show('Jumbo.cl','#TB_inline?height=125&width=580&inlineId=hiddenModalContent&modal=true');
}

function showLightBoxAgregaDirecion() {
  tb_show('Jumbo.cl','#TB_inline?height=400&width=625&inlineId=hiddenModalContent&modal=true');
}

function showLightBoxMensajeExitoAgregaDireccion() {
  tb_show('Jumbo.cl','#TB_inline?height=100&width=500&inlineId=hiddenModalContent&modal=true');
}


/*function addItemToCart(idProducto,cantidad,nota_campo) {
  $j.post("/FO/PasoDosAgregarCarro", {productoId: idProducto, cantidad: cantidad, nota: nota_campo}, function(datos) {
    $j(datos).find('datos_objeto').each(function() {
      var respuesta = $j(this).text();
      if ( respuesta == "ok" ) {
        $j('img#imagen_'+idProducto).attr('src','/FO_IMGS/img/estructura/paso2/bt_actualizar_carro.gif');
        actualizaCarro();
        limpiaVariablesCarro();
      }
    });
  });
}*/

function limpiaVariablesCarro() {
  XACT_CARRO 		= "";
  XID_PRODUCTO 		= 0;
  XCANTIDAD 		= 0;
  XNOTACAMPO 		= "";
}

function changeComuna() {
	  var comuna = 0;
	  var nombreComuna;
	  if (PASO_ACTUAL == 4){
	      if ( $j('#region').val() == "0" || $j('#comuna').val() == "0" ) {
	    	    alert("Seleccione su regi�n y comuna "+region_nombre+comuna_nombre);
	    	    try{mxTracker._trackEvent('SeleccionRegionComuna','Alerta','Region&Comuna');}catch(e){};
	          return;
	      }else{
	          comuna = $j('#comuna').val();
	      }
	  }else{
	      if ( $j('#id_region').val() == "0" || $j('#id_comuna').val() == "0" ) {
	    	  if ( $j('#localretiro').val() == "0" ){
		          alert("Seleccione su regi�n y comuna o Local de Retiro");
		          try{mxTracker._trackEvent('SeleccionRegionComuna','Alerta','Region&Comuna');}catch(e){};
		          return;	    	  
	    	  }
	      }else{
	          comuna = $j('#id_comuna').val();
	      }
	  }
	  dataLayer.push({
		     'Region': $j("#region option:selected").html(),
		     'Comuna': $j("#comuna option:selected").html(),
		     'event': 'Seleccion-Region-Comuna'
		});
	  if ($j("#id_comuna").val()!="0"){
			comuna=$j("#id_comuna").val();
		} else if ($j("#localretiro").val()!="0"){
			nombreComuna=$j("#localretiro option:selected").text();
			comuna=$j("#localretiro").val();
		}
	  $j.post("/FO/ChangeComunaSession", {id_comuna: comuna, nombre_comuna:nombreComuna}, function(datos){
    $j(datos).find('comuna').each(function() {
      var respuesta  = $j(this).find('mensaje').text();
      $j('#comuna_user_str').html( $j(this).find('nombre_comuna').text() );
      $j('#comuna_user_id').val( $j(this).find('id_comuna').text() );
      if ( respuesta == "OK" ) {
        if (PASO_ACTUAL == 5){
            window.location.href="/FO/Despacho";
        }else{
	        if ( XID_PRODUCTO != 0 ) {
	          validaProductoLocal();
	        } else {
	          closeLBoxWin();
	        }
	        actualizaCarro();
	        if ( XID_CAT != 0 && XID_SUBCAT == 0 ) {
	          mostrarCategorias(XID_CAB, XID_CAT);
	        } else if ( XID_CAT != 0 && XID_SUBCAT != 0 ) {
	          mostrarCategoriaYSub(XID_CAB, XID_CAT, XID_SUBCAT)
	        }
        }
      }
    });
  });
}

function changeComunaRegistro() {
  if ( $j('#id_region_reg').val() == "0" || $j('#id_comuna_reg').val() == "0" ) {
    alert("Seleccione su regi�n y comuna");
    return;
  }
  $j.post("/FO/ChangeComunaSession", {id_comuna: $j("#id_comuna_reg").val()}, function(datos){
    $j(datos).find('comuna').each(function() {
      var respuesta  = $j(this).find('mensaje').text();
      $j('#comuna_user_str').html( $j(this).find('nombre_comuna').text() );
      $j('#comuna_user_id').val( $j(this).find('id_comuna').text() );
      if ( respuesta == "OK" ) {
        if ( XID_PRODUCTO != 0 ) {
          validaProductoLocal();
        } else {
          closeLBoxWin();
        }
        actualizaCarro();
        if ( XID_CAT != 0 && XID_SUBCAT == 0 ) {
          mostrarCategorias(XID_CAB, XID_CAT);
        } else if ( XID_CAT != 0 && XID_SUBCAT != 0 ) {
          mostrarCategoriaYSub(XID_CAB, XID_CAT, XID_SUBCAT)
        }
      }
    });
  });
}

function changeComunaAgregaDireccion(id_region, id_comuna) {
  if ( id_region == "0" || id_comuna == "0" ) {
    alert("Seleccione su regi�n y comuna");
    return;
  }
  $j.post("/FO/ChangeComunaSession", {id_comuna: id_comuna}, function(datos){
    $j(datos).find('comuna').each(function() {
      var respuesta  = $j(this).find('mensaje').text();
      $j('#comuna_user_str').html( $j(this).find('nombre_comuna').text() );
      $j('#comuna_user_id').val( $j(this).find('id_comuna').text() );
      if ( respuesta == "OK" ) {
        if ( XID_PRODUCTO != 0 ) {
          validaProductoLocal();
        } else {
          closeLBoxWin();
        }
        actualizaCarro();
        if ( XID_CAT != 0 && XID_SUBCAT == 0 ) {
          mostrarCategorias(XID_CAB, XID_CAT);
        } else if ( XID_CAT != 0 && XID_SUBCAT != 0 ) {
          mostrarCategoriaYSub(XID_CAB, XID_CAT, XID_SUBCAT)
        }
      }
    });
  });
}

function Registrar(){
	// 05/10/2012 : INICIO COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com} 
	 var comuna_ = document.RegistroSencillo.id_comuna[document.RegistroSencillo.id_comuna.selectedIndex].text; 
	 var region_ = document.RegistroSencillo.id_region[document.RegistroSencillo.id_region.selectedIndex].text; 
	// 05/10/2012 : FIN COREMETRICS
	
    if (RegSencillo_ValidaCliente()){
        //alert();
	    var envioSMS   = false;
	    var envioEMail = false;
	    if ($j('#envioSMS').is(':checked')){
	        envioSMS = true;
	    }
	    if ($j('#envioEMail').is(':checked')){
	        envioEMail = true;
	    }
	    
	
	    var params = "cli_rut="     + $j('#cli_rut_reg').val()
	               + "&nombre="     + $j('#nombre').val()
	               + "&ape_pat="    + $j('#ape_pat').val()
	               + "&id_region="  + $j('#id_region_reg').val()
	               + "&id_comuna="  + $j('#id_comuna_reg').val()
	               + "&email="      + $j('#email_reg').val()
	               + "&envioEMail=" + envioEMail
	               + "&fon_cod="    + $j('#fon_cod').val()
	               + "&fon_num="    + $j('#fon_num').val()
	               + "&envioSMS="   + envioSMS
	               + "&clave="      + $j('#clave1').val()
	               ;
	    
	    if($j('#RegistroSencillo #accesoLfckfield').val() != null)
	    	params += "&accesoLfckfield=" + $j('#RegistroSencillo #accesoLfckfield').val();
            
	    //var params = "";
	    //alert("params=> " + params);
		var requestOptions = {
			'method': 'post',
			'parameters': params,
			'onSuccess': function(REQUEST) {
				if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
					alert("Ocurri� un error en el registro.");
					return;
				}	
				var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
				//alert("respuesta= '" + respuesta + "'");
				if (respuesta == 'OK') {
				    //logonRegSencillo('');
				    if (PASO_ACTUAL == 1){
				        logonRegSencillo('LBOX');
				    }else{
				        logonRegSencillo('LBOX_S');
				    }
	                //showMensajeRegistrarSession();
				    //showAgregaDireccionSession();
				    //changeComunaRegistro();
				    //changeComunaAgregaDireccion();
	                // 05/10/2012 : INICIO COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com} 
	                	var id=REQUEST.responseXML.getElementsByTagName("id")[0].childNodes[0].nodeValue;
	                	coremetricsTagRegister("Registro","{cat_nombre}",id, $j('#email_reg').val(),region_, comuna_);
	                // 05/10/2012 : FIN COREMETRICS
				}else if (respuesta == 'CLIENTE_REGISTRADO'){
				    $j('#error_rut_existe').show();
				}
			}
		}
		new Ajax.Request('RegisterNew', requestOptions);
    }
}

function MensajeRegistrar(){
    var params = "";
    //alert("params=> " + params);
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
				alert("Ocurri� un error al Actualizar la Direcci�n.");
				return;
			}
			var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
			//alert("respuesta= '" + respuesta + "'");
			if (respuesta == 'OK') {
                showMensajeRegistrarSession();
			}
		}
	}
	//new Ajax.Request('AjaxMensajeRegistrar', requestOptions);
}


function AgregaDireccion(){
    if (RegSencillo_ValidaDireccion()){
	    var params = "tipo_calle="    + $j('#tipo_calle').val()
	               + "&calle="        + $j('#calle').val()
	               + "&numero="       + $j('#numero').val()
	               + "&departamento=" + $j('#departamento').val()
	               + "&id_region="    + $j('#id_region_dir').val()
	               + "&id_comuna="    + $j('#id_comuna_dir').val()
	               + "&alias="        + $j('#alias_rs').val()
	               + "&comentario="   + $j('#comentario').val()
	               ;
	    id_region= $j('#id_region_dir').val();
	    id_comuna= $j('#id_comuna_dir').val();
	    
	    //alert("params=> " + params);
		var requestOptions = {
			'method': 'post',
			'parameters': params,
			'onSuccess': function(REQUEST) {
				if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
					alert("Ocurri� un error al modificar la direcci�n.");
					return;
				}
				var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
				//alert("respuesta= '" + respuesta + "'");
				if (respuesta == 'OK') {
				    if (PASO_ACTUAL == 5){
				        window.location.href="/FO/Despacho";
				    }else{
				        //changeComunaAgregaDireccion(id_region, id_comuna);
				        showMensajeExitoAgregaDireccionSession();
				    }
				}/*else if (respuesta == 'CLIENTE_REGISTRADO'){
				    showMensajeRegistrarSession();
				}*/
			}
		}
		new Ajax.Request('AjaxModificaDireccionRegSencillo', requestOptions);
	}
}


function showRegionesCobertura() {
	showChangeComuna();
	//traemos las regiones
	$j.get("/FO/RegionesConCobertura",function(datos) {
	    $j("#id_region").html(datos);
	    //TODO aca seleccionar combo de region
	});
	
	//alert("showRegistro 3 ==> id_region: " + id_region + " |  id_comuna: " + id_comuna);
	getComunasByRegion();
}


function showRegistro() {
	
	if($j('#loginFck') != null)
		$j('#loginFck').hide();
	

	$j('#RegistroSencillo input').each(
			function(index){  
				var input = $j(this);
				if(input.attr('type') =='text')
					 input.val('');
			}
	);
	
	
  if (PASO_ACTUAL == 1 || PASO_ACTUAL == 2){
      document.RegistroSencillo.url_to.value = "/FO/CategoryDisplay";
      jQuery('#RegistroSencillo #accesoLfckfield').remove();      
  }else if (PASO_ACTUAL == 3){
      document.RegistroSencillo.url_to.value = "/FO/MiCarro";
  }else if (PASO_ACTUAL == 4){
      document.RegistroSencillo.url_to.value = "/FO/Despacho";
  }else if (PASO_ACTUAL == 5){
      document.RegistroSencillo.url_to.value = "/FO/Pago";
  }
  
  showRegistrarSession();
  //traemos las regiones
  var id_comuna = 0;
  var id_region = 0;
  var region = 0;
  
  if (typeof $j("#id_region").val() === "undefined"){
    id_region = 0;
  }else{
    id_region = $j("#id_region").val();
  }
  
  if (typeof $j("#id_comuna").val() === "undefined"){
    id_comuna = 0;
  }else{
    id_comuna = $j("#id_comuna").val();
  }
  
  
  //alert("$j('#id_region').val(): " + $j("#id_region").val());
  //alert("$j('#id_comuna').val(): " + $j("#id_comuna").val());
  /*if (id_comuna > 0){
    id_comuna = $j("#id_comuna").val();
  }*/
  
  //alert("showRegistro 1 ==> id_region: " + id_region + " |  id_region_reg: " + $j("#id_region_reg").val());
  if (id_region > 0){
    $j("#id_region_reg").val(id_region);
    region = id_region;
    //alert("showRegistro 2 ==> id_region: " + id_region + " |  id_region_reg: " + $j("#id_region_reg").val());
  }
  
  if ($j("#id_region_reg").val() > 0){
    region = $j("#id_region_reg").val();
  }

  //alert("region: " + region);
  //$j.get("/FO/RegionesConCobertura",{id_region:$j("#id_region_reg").val()},function(datos) {
  $j.get("/FO/RegionesConCobertura",{id_region:region}, function(datos) {
    $j("#id_region_reg").html(datos);
    //TODO aca seleccionar combo de region
  });
  
  //alert("id_region: " + id_region);
  //if (id_region > 0){
    //alert("showRegistro 3 ==> id_region: " + id_region + " |  id_comuna: " + id_comuna);
    getComunasByRegionRegistro(id_region, id_comuna);
  //}
  
  //+ 2012123 Habilitar funcionalidad de Bot�n Guardar Datos en link Reg�strate FPenalozaP
  document.getElementById("envioEMail").checked=1;
  document.getElementById("envioSMS").checked=1;
  document.getElementById("terminos").checked=1;
 //- 2012123 Habilitar funcionalidad de Bot�n Guardar Datos en link Reg�strate FPenalozaP
}

function showAgregaDireccion() {
  showAgregaDireccionSession();
  
  $j.post("/FO/AjaxLlenaFormDireccionRegSencillo", function(datos){
  
  $j(datos).find('direccion').each(function() {
        //traemos los Tipos de Calles
	    $j.get("/FO/AjaxTiposCalle",{tipo_calle: $j(this).find('tipo_calle').text()},function(datos) {
	        $j("#tipo_calle").html(datos);
	        //TODO aca seleccionar combo de region
	    });
    
        $j('#alias_rs').val( $j(this).find('alias').text() );
        $j('#calle').val( $j(this).find('calle').text() );
        $j('#tipo_calle').val( $j(this).find('tipo_calle').text() );
        $j('#numero').val( $j(this).find('numero').text() );
        $j('#departamento').val( $j(this).find('depto').text() );
        $j('#comentario').val( $j(this).find('comentario').text() );
        $j('#id_region_dir').val( $j(this).find('id_region').text() );
        $j('#id_comuna_dir').val( $j(this).find('id_comuna').text() );
        
        //alert("showAgregaDireccion 0 ==> id_region_dir: " + $j(this).find('id_region').text() + " |  id_comuna_dir: " + $j(this).find('id_comuna').text());

        //alert("showAgregaDireccion 1 ==> id_region_dir: " + $j(this).find('id_region').text() + " |  id_comuna_dir: " + $j(this).find('id_comuna').text());

	    //traemos las regiones
	    $j.get("/FO/RegionesConCobertura",{id_region: $j(this).find('id_region').text()},function(datos) {
	        $j("#id_region_dir").html(datos);
	        //TODO aca seleccionar combo de region
	    });
	    
	    if ($j(this).find('id_region').text() > 0){
	        //alert("showAgregaDireccion 2 ==> id_region_dir:" + $j(this).find('id_region').text() + " | id_comuna_dir:" + $j(this).find('id_comuna').text());
	        getComunasByRegionAgregaDireccion($j(this).find('id_region').text(), $j(this).find('id_comuna').text());
	    }
      
    });
  });
  
  
  
  /*
    $j.get("/FO/RegionesConCobertura",{id_region: $j("#id_region_reg").val()},function(datos) {
    $j("#id_region_reg").html(datos);
    //TODO aca seleccionar combo de region
  }); 
  
  if ($j("#id_region").val() > 0){
    alert(1);
    getComunasByRegionRegistro($j("#id_region").val());
  }
  
  */
}
function ocultaEnvioSMS(){
  if ($j('#fon_cod').val() < 6 || $j('#fon_cod').val() > 9){
    $j('#DIVenvioSMS').hide();
    if ($j('#envioSMS').is(':checked')){
      $j('#envioSMS').attr('checked', false);
    }
  }else{
    $j('#DIVenvioSMS').show();
    $j('#envioSMS').attr('checked', true);
  }
  
}

function getComunasByRegionRegistro(idRegion, idComuna) {
  //var idComuna=0;
  //if ( idRegion == 0 ) {
  //  return;
  //}
  /*if ($j("#id_comuna").val() > 0){
    idComuna = $j("#id_comuna").val();
  }*/
  if (typeof idRegion === "undefined"){
    idRegion=0;
    //alert("something is undefined");
  }
  
  if (typeof idComuna === "undefined"){
    idComuna=0;
    //alert("something is undefined");
  }
   
  //alert("getComunasByRegionRegistro ==> idRegion:" + idRegion + " | idComuna:" + idComuna);
  $j.get("/FO/ComunasConCoberturaByRegion",{id_region:idRegion, id_comuna:idComuna},function(datos) {
    $j("#id_comuna_reg").html(datos);
    $j("#id_comuna_reg").removeAttr('disabled');
  });
}

function getComunasByRegionAgregaDireccion(idRegion, idComuna) {
  if (typeof idComuna === "undefined"){
    idComuna=0;
  }
  $j.get("/FO/ComunasConCoberturaByRegion",{id_region:idRegion, id_comuna:idComuna},function(datos) {
    $j("#id_comuna_dir").html(datos);
    $j("#id_comuna_dir").removeAttr('disabled');
  });
}

function getComunasByRegion(idRegion) {
  if (idRegion == 0){
    return;
  }

  $j.get("/FO/ComunasConCoberturaByRegion",{id_region:idRegion},function(datos) {
    $j("#id_comuna").html(datos);
    $j("#id_comuna").removeAttr('disabled');
  });
}

function closeLBoxWin() {
  if ( document.getElementById("ingreso_clientes") != null && document.getElementById("ingreso_clientes_lbox") != null ) {
    var norm = document.getElementById("ingreso_clientes");
    var box = document.getElementById("ingreso_clientes_lbox");
    box.style.visibility="hidden";
    box.style.width="0px";
    box.style.height="0px";
    norm.style.display="block";
    norm.style.visibility="visible";
    norm.style.width="230px";
    norm.style.height="306px";
  }
  if($j("#loginFck") != null)
	  $j("#loginFck").hide();
  
  tb_remove();
}

function show_lbox() {
  var norm = document.getElementById("ingreso_clientes");
  var box = document.getElementById("ingreso_clientes_lbox");
  norm.style.visibility="hidden";
  norm.style.width="0px";
  norm.style.height="0px";
  box.style.display="block";
  box.style.visibility="visible";
  box.style.width="230px";
  box.style.height="306px";
}

function getRecuperaClave1() {
  if ( $j('#cli_rut_recupera').val() == "" ) {
    alert("Por favor, ingrese su Rut.");
    return;
  }
  $j.post("/FO/ChangeComunaSession", {id_comuna: $j("#id_comuna").val()}, function(datos){
    $j(datos).find('comuna').each(function() {
      var respuesta  = $j(this).find('mensaje').text();
      $j('#comuna_user_str').html( $j(this).find('nombre_comuna').text() );
      $j('#comuna_user_id').val( $j(this).find('id_comuna').text() );
      
      if ( respuesta == "OK" ) {
        if ( XACT_CARRO == "ADD_CARRO" ) {
          validaProductoLocal();
        } else {
          window.location.reload();
        }
      }
    });
  });
}

function validaProductoLocal() {
  //Verificamos que producto se encuentre para esa comuna
  $j.get("/FO/ValidarProductoByLocal",{id_producto:XID_PRODUCTO},function(datos) {
    $j(datos).find('respuesta').each(function() {
      var resp  = $j(this).find('mensaje').text();
      if ( resp == "OK" ) {      
        if ( XACT_CARRO == "ADD_CARRO" ) {
          addItemToCart(XID_PRODUCTO,XCANTIDAD,XNOTACAMPO);
        } else if ( XACT_CARRO == "ADD_CARRO_FICHA" ) {
          if ( window.frames.frm_ficha && window.frames.frm_ficha.guardaProducto ) {
            window.frames.frm_ficha.guardaProducto( XFORM );
            hideFichaProducto();
          }
        }
        closeLBoxWin();
      } else {
        $j('#prod_disp_mjs').html("En este momento, el producto no se encuentra disponible<br/>para la Comuna que has seleccionado.");
        showChangeComunaMsj();
      }
    });
  });
}

function showEvento() {
  MM_showHideLayers('ventana_evento','','show');
  var layerEvento = document.getElementById('ventana_evento');
  //var ancho = screen.availWidth;
  var largo = screen.availHeight;
  var ancho = document.documentElement.scrollWidth;
  //var largo = document.documentElement.scrollHeight;
  layerEvento.style.width = '400px';
  layerEvento.style.height = '285px';
  layerEvento.style.left = (ancho-400)/2 + 'px';
  layerEvento.style.top = (largo-285)/2 + 'px';
}

function hideEvento() {
  MM_showHideLayers('ventana_evento','','hide');
  var layerEvento = document.getElementById('ventana_evento');
  layerEvento.style.width = '0px';
  layerEvento.style.height = '0px';
}

function AcordeonCabecera(cab){
    //alert("XID_CAB: " + XID_CAB + ", cab: " + cab);
	if (XID_CAB != cab){
	    $j('#loading').show();
		//abre la nueva categoria
		var elemcab = $j('#' + cab).next();
	    var objcab = $j('#' + cab);
	    if(elemcab.is('ul')){
		    objcab.find('ul:visible').not(elemcab).not(elemcab.parents('ul:visible')).slideUp();
		    elemcab.slideToggle();
	    }
	    objcab.css("color","#1FA22E");
	    
	    if (XID_CAB > 0){
	        //cierra la categoria que se encontraba abierta
		    var elemcab2 = $j('#' + XID_CAB).next();
	        var objcab2  = $j('#' + XID_CAB);
    	    if(elemcab2.is('ul')){
	    	    //objcab2.find('ul:visible').not(elemcab2).not(elemcab2.parents('ul:visible')).slideUp();
		        //elemcab2.slideToggle();
		        elemcab2.slideUp();
	        }
	        objcab2.css("color","#666");
	    }
	}

    if (XID_CAB == cab){
		//abre categoria en caso de que el usuario la haya cerrado
		var elemcab = $j('#' + cab).next();
	    var objcab = $j('#' + cab);
	    if(elemcab.is('ul')){
		    elemcab.slideDown();
	    }
	    objcab.css("color","#1FA22E");
	}
}

function AcordeonIntermedia(cab, int){
	if (XID_CAT != int){
	    $j('#loading').show();
	    
	    //abre la nueva categoria intermedia
	    var elemint = $j('#' + cab + '_' + int).next();
	    var objint = $j('#' + cab + '_' + int);
	    if(elemint.is('ul')){
		    objint.find('ul:visible').not(elemint).not(elemint.parents('ul:visible')).slideUp();
		    elemint.slideToggle();
	    }
	    objint.css("color","#1FA22E");
	    
	    if (XID_CAT > 0){
    	    //cierra la categoria intermedia que se encontraba abierta
	        var elemint2 = $j('#' + XID_CAB + '_' + XID_CAT).next();
	        var objint2  = $j('#' + XID_CAB + '_' + XID_CAT);
    	    if(elemint2.is('ul')){
	    	    objint2.find('ul:visible').not(elemint2).not(elemint2.parents('ul:visible')).slideUp();
		        elemint2.slideToggle();
	        }
	        objint2.css("color","#666");
	    }
	}
	
    if (XID_CAT == int){
		//abre la nueva categoria
		var elemint = $j('#' + cab + '_' + int).next();
	    var objint = $j('#' + cab + '_' + int);
	    if(elemint.is('ul')){
		    elemint.slideDown();
	    }
	    objint.css("color","#1FA22E");
	}
}

function muestracabecera(cab, numero){
	//alert("(muestracabecera) cab: " + $j('#cab').val() + ", int: " + $j('#int').val() + ", ter: " + $j('#ter').val());

	//alert("XID_CAB: " + XID_CAB + ", XID_CAT: " + XID_CAT + ", XID_SUBCAT: " + XID_SUBCAT + "\n" + "cab: " + cab );

	var elemcab = $j('#' + cab).next();
	var objcab = $j('#' + cab);
	
    AcordeonCabecera(cab);
    
	if (XID_CAT > 0){
	    //cierra la categoria intermedia que se encontraba abierta
	    var elemint = $j('#' + XID_CAB + '_' + XID_CAT).next();
	    var objint  = $j('#' + XID_CAB + '_' + XID_CAT);
    	if(elemint.is('ul')){
	    	objint.find('ul:visible').not(elemint).not(elemint.parents('ul:visible')).slideUp();
		    elemint.slideToggle();
	    }
	    objint.css("color","#666");
	}
	
	if (XID_SUBCAT > 0){
	    var objter = $j('#' + XID_CAB + '_' + XID_CAT + '_' + XID_SUBCAT);
        objter.css("color", "#666");
    }
    
    if (numero < 15){
	    $j('#dropdown_' + numero).hide();
    }
    
	XID_CAB = cab;
	XID_CAT	= 0;
	XID_SUBCAT = 0;
	mostrarCategorias(cab,0);
}

function muestraterminal(cab, int, ter, numero){
	//alert("(muestraterminal) cab: " + $j('#cab').val() + ", int: " + $j('#int').val() + ", ter: " + $j('#ter').val());

	//alert("XID_CAB: " + XID_CAB + ", XID_CAT: " + XID_CAT + ", XID_SUBCAT: " + XID_SUBCAT + "\n" +
	//      "cab: " + cab + ", int: " + int + ", ter: " + ter );

    AcordeonCabecera(cab);	
    AcordeonIntermedia(cab, int);
	
    var objter = $j('#' + cab + '_' + int + '_' + ter);
    objter.css("color", "#1FA22E");
    
    var objter2 = $j('#' + XID_CAB + '_' + XID_CAT + '_' + XID_SUBCAT);
    objter2.css("color", "#666");
    
	if (numero < 15){
	    $j('#dropdown_' + numero).hide();
	}
	XID_CAB = cab;
	XID_CAT	= int;
	XID_SUBCAT = ter;
	mostrar(cab,int,ter);
}

function mostrar(cabId, catId, subcatId) {
    XID_CAB = cabId;
    XID_CAT = catId;
    XID_SUBCAT = subcatId;
    mostrarCategoriaYSub(cabId, catId, subcatId);
}

function mostrarterminal(cab, int, ter) {
	//alert("XID_CAB: " + XID_CAB + ", XID_CAT: " + XID_CAT + ", XID_SUBCAT: " + XID_SUBCAT + "\n" +
	//      "cabId: " + cabId + ", catId: " + catId + ", subcatId: " + subcatId );

    AcordeonCabecera(cab);
    AcordeonIntermedia(cab, int);

    var objter = $j('#' + cab + '_' + int + '_' + ter);
    objter.css("color", "#1FA22E");
    
    var objter2 = $j('#' + XID_CAB + '_' + XID_CAT + '_' + XID_SUBCAT);
    objter2.css("color", "#666");
    
    XID_CAB = cab;
    XID_CAT = int;
    XID_SUBCAT = ter;
    $j('#loading').show();
    mostrarCategoriaYSub(cab, int, ter);
}

function mostrarCategoriaYSub(cabId, catId, subcatId) {
  $j.get("/FO/PasoDosResultado", {cab: cabId, int: catId, ter: subcatId},
    function(datos) {
      $j("div#div_productos").html(datos);
      eventosProductos();
      $j('#loading').hide();
    }
  );
}

function mostrarCategorias(cabId, catId) {
  $j.get("/FO/PasoDosPub", {cab: cabId, int: catId},function(datos) {
    $j("div#div_productos").html(datos);
    eventosProductos();
    $j('#loading').hide();
  });
}

//Validacion antigua
//function validarEmail(valor){
//	validRegExp = /^[^@]+@[^@]+.[a-z]{2,}$/i;
//	
//	if (!validRegExp.test(valor)) {
//	    alert("La direcci�n de email es incorrecta o est� mal escrita. Ingr�sala nuevamente");
//	    return (false);
//	}else
//		return (true);
//}

//Maxbell Caballero 2014-04-14
function validarEmail(valor){
	re=/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;     
	if(!re.exec(valor))    {
	    alert("La direcci�n de email es incorrecta o est� mal escrita. Ingr�sala nuevamente");
	    return (false);
	} else {
		return (true);
}
}
//****************************

function RegSencillo_ValidaCliente(){
	var codigo = $j('#fon_cod').val();
	var numero = $j('#fon_num').val();
	
	if ($j('#cli_rut_reg').val() == ""){
		alert("Debes ingresar el Rut");
		$j('#cli_rut_reg').focus();
		return false;
	} else if ($j('#nombre').val() == "") {
		alert("Debes ingresar el campo Nombre");
		$j('#nombre').focus();
		return false;
	} else if ($j('#ape_pat').val() == "") {
		alert("Debes ingresar el campo Apellido");
		$j('#ape_pat').focus();
		return false;
	} else if ($j('#id_region_reg').val() == 0) {
		alert("Debes seleccionar la Regi�n");
		$j('#id_region_reg').focus();
		return false;
	} else if ($j('#id_comuna_reg').val() == 0) {
		alert("Debes seleccionar la Comuna");
		$j('#id_comuna_reg').focus();
		return false;
	} else if ($j('#email_reg').val() == "") {
		alert("Debes ingresar el campo Mail");
		$j('#email_reg').focus();
		return false;
	} else if (!validarEmail($j('#email_reg').val())) {
		$j('#email_reg').focus();
		return false;
	} else if ($j('#fon_num').val() == "") {
		alert("Debes ingresar el campo Tel�fono");
		$j('#fon_num').focus();
		return false;
	} else if(isLongNumberInsert(codigo)){
			if(!/^\d{8}$/.test(numero)) {
				alert("Debe ingresar su n�mero de contacto correctamente");
				jQuery('#fon_num').val("");	
				jQuery('#fon_num').focus();
				return false;
			}
		}else{
			if(!/^\d{7}$/.test(numero)) {
				alert("Debe ingresar su n�mero de contacto correctamente");
				jQuery('#fon_num').val("");	
				jQuery('#fon_num').focus();
				return false;
			}		
	 }	
	if ($j('#clave1').val() == "") {
		alert("Debes ingresar el campo Clave");
		$j('#clave1').focus();
		return false;
	} else if ($j('#clave1').val().length < 4){
		alert("La Clave debe tener m�nimo 4 caracteres.");
		$j('#clave1').focus();
		return false;
	} else if ($j('#clave2').val() == "") {
		alert("Debes ingresar el campo 'Confirmar Clave'");
		$j('#clave2').focus();
		return false;
	} else if ($j('#clave2').val().length < 4){
		alert("La Clave debe tener m�nimo 4 caracteres.");
		$j('#clave2').focus();
		return false;
	}
    if ($j('#clave1').val() != $j('#clave2').val()) {
		alert("Las claves ingresadas no coinciden");
		$j('#clave1').focus();
		return false;
	}
	if (!($j('#terminos').is(':checked'))) {
		alert("Debes aceptar los t�rminos y condiciones");
		return false;
	}
	return true;
}

function RegSencillo_ValidaDireccion(){
	if ($j('#tipo_calle').val() == 0){
		alert("Debes seleccionar el Tipo de calle");
		$j('#tipo_calle').focus();
		return false;
	} else if ($j('#calle').val() == "") {
		alert("Debes ingresar el campo calle");
		$j('#calle').focus();
		return false;
	} else if ($j('#numero').val() == "") {
		alert("Debes ingresar el campo N�mero");
		$j('#numero').focus();
		return false;
	} else if ($j('#id_region_dir').val() == 0) {
		alert("Debes seleccionar la Regi�n");
		$j('#id_region_dir').focus();
		return false;
	} else if ($j('#id_comuna_dir').val() == 0) {
		alert("Debes seleccionar la Comuna");
		$j('#id_comuna_dir').focus();
		return false;
	} else if ($j('#alias_rs').val() == "") {
		alert("Debes ingresar el campo 'Guardar como'");
		$j('#alias_rs').focus();
		return false;

    //VERIFICA QUE EL CLIENTE HAYA CAMBIADO SU DIRECCI�N
	} else if ($j('#calle').val() == "Ingresa tu calle") {
		alert("Debes modificar el campo calle");
		$j('#calle').focus();
		return false;
	} else if ($j('#numero').val() == "000") {
		alert("Debes modificar el campo N�mero");
		$j('#numero').focus();
		return false;
	} else if ($j('#alias_rs').val() == "Nombre") {
		alert("Debes modificar el campo 'Guardar como'");
		$j('#alias_rs').focus();
		return false;
	}
	return true;
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

//valida si se va a evaluar un numero de 8 digitos o de 7
//ANIBAL JARA
function isLongNumberInsert(codigo){
	 if ((codigo == "9")||(codigo == "2")){
		return true;
	}
	return false;
}


