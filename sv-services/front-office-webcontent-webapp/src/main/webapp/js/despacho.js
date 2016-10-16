var XBROWSER = navigator.appName;
var $j = jQuery.noConflict();
var agregadireccion = false;

jQuery().ready(function() {
	//muestraCalendario();
	$j('#direccion').attr("disabled","disabled");
	$j("input[name=tipodespacho]").click(function(){
		$j('#direccion').removeAttr('disabled');
		var tipo = $j("input[@name='tipodespacho']:checked").val();
		var idZona=0;
		var idDir=0;
		if (tipo == "R") {
			$j('#direccion').attr("disabled","disabled");
			var valor=$j("#localretiro").val();
			if(valor != undefined){
				var arreglo= valor.split('_');
				idZona=arreglo[1];
			}
		}else if(tipo == "D"){
			var valor=$j("#direccion").val();
			if(valor != undefined){
				var arreglo = valor.split('_');
				idDir=arreglo[0];
			}
		}
		var action  = "/FO/AjaxTraeDireccion?setDirSession=1&tipo="+tipo+"&idDir="+idDir+"&idZona="+idZona;
		$j.get(action,function(data){});
	});
});
function copiaRutInvitado(){
	document.getElementById("rut").value =document.getElementById("rut1").value;
	document.getElementById("dv").value =document.getElementById("dv1").value;
	
	
}
function showClienteSinRegistro(){
	
	showLightBoxCategories();
//  greenBack();
  $j('#contenedor_lightbox').show();
	$j('#clienteSinRegistro').show();
	$j('#logo_jumbo').show();
	  $j('#comunas_sesion').hide();
	  $j('#change_comuna_layer_msj').hide();
	  $j('#change_login_session').hide();
	  $j('#olvido_clave').hide();
	  $j('#registra_cliente').hide();
	  $j('#cont_inputs_close').show();
	  $j('#opcionesRegistro').hide();
	  $j('#agrega_direccion').hide();
	  $j('#error_rut_existe').hide();
	  $j('#mensaje_dir_exito').hide();
	  $j('#producto_no_disponible').hide();
	  
	//showRegionesCobertura();
}
function cambiaDireccion(){
	var valor=document.getElementById("localretiro").value;
	var arreglo= valor.split('_');
	codigoLocal=arreglo[0];
	if (codigoLocal=="5"){//Los Dominicos
		$j("#indicacion").show()
		$j("#divDominicos").show();
		$j("#divFlorida").hide();
	}
	if (codigoLocal=="4"){//La Florida
		$j("#indicacion").show()
		$j("#divDominicos").hide();
		$j("#divFlorida").show();
		
	}
}

function cambiaDireccion1(){
	var valor=document.getElementById("localretiro").value;
	var arreglo= valor.split('_');
	codigoLocal=arreglo[0];
	if (codigoLocal=="5"){//Los Dominicos
		$j("#indicacion").show()
		$j("#divDominicos").show();
		$j("#divFlorida").hide();
	}
	if (codigoLocal=="4"){//La Florida
		$j("#indicacion").show()
		$j("#divDominicos").hide();
		$j("#divFlorida").show();
		
	}
}


function muestraCalendario() {
	var tipo = $j("input[@name='tipodespacho']:checked").val();
	if (tipo==null)
		tipo="D";
	var zona;
	if (tipo == "R") {
		zona = ($j('#localretiro').val().split("_"))[1];
		if(zona==null){
			$j('#autoriza').hide();
			$j('#inferiordespacho').hide();
			$j('#observa').hide();
			$j('#retira').hide();
			$j('#titulocalendariodespacho').hide();
			$j('#titulocalendarioretiro').hide();
			$j('#autorizadespacho').hide();
			$j('#autorizaretiro').hide();
			$j('#titulo_autorizacion_retiro').hide();
			$j('#titulo_autorizacion_despacho').hide();
			return;
		}
		$j('#retira').show();
		$j('#autoriza').show();
		$j('#observa').hide();
		//if (id_cliente != 1)
		DesactivaCamposDireccion();
		if (id_cliente == 1){//se dejan por defecto los datos del depacho
			$j('#calle').val("Ingresa tu calle");
			$j('#calle').css({'color':'rgb(153, 153, 153)','font-style': 'italic'});
			$j('#numero').val("000");
			$j('#numero').css({'color':'rgb(153, 153, 153)','font-style': 'italic'});
			$j('#region').val(13);
			busca_comunas(4);
			$j('#comuna').val(4);			
		}
		showCalendario(tipo, cant, zona);
	} else if (tipo == "D") {
		$j("#localretiro").attr('disabled', 'disabled');
		if (id_cliente == 1) {
			ActivaCamposDireccion();
		
				if ($j('#tipo_calle').val() == 0) {
					alert("Debes seleccionar el Tipo de calle");
					$j('#tipo_calle').focus();
					$j('input[name=tipodespacho][value=R]').attr('checked',
							'checked')
					return false;
				} else if ($j('#calle').val() == "") {
					alert("Debes ingresar el campo calle");
					$j('#calle').focus();
					$j('input[name=tipodespacho][value=R]').attr('checked',
							'checked')
					
				} else if ($j('#numero').val() == "") {
					alert("Debes ingresar el campo Número");
					$j('input[name=tipodespacho][value=R]').attr('checked',
							'checked')
					$j('#numero').focus();
					return false;
				} /*else if ($j('#region').val() == 0) {
					alert("Debes seleccionar la Región");
					$j('input[name=tipodespacho][value=R]').attr('checked',
							'checked')
					$j('#region').focus();
					return false;
				} else if ($j('#comuna').val() == 0) {
					alert("Debes seleccionar la Comuna");
					$j('input[name=tipodespacho][value=R]').attr('checked',
							'checked')
					$j('#comuna').focus();
					return false;
				}*/ else if ($j('#alias').val() == "") {
					alert("Debes ingresar un campo Nombre Referencial");
					$j('input[name=tipodespacho][value=R]').attr('checked',
							'checked')
					$j('#alias').focus();
					return false;
				}
			
			var params = "id_comuna=" + $j('#comuna').val();
			var requestOptions = {
				'method' : 'post',
				'parameters' : params,
				'onSuccess' : function(REQUEST) {
					if (REQUEST.responseXML != null) {
						var local = REQUEST.responseXML
								.getElementsByTagName("idlocal")[0].childNodes[0].nodeValue;
						zona = REQUEST.responseXML
								.getElementsByTagName("idzona")[0].childNodes[0].nodeValue;
						showCalendario(tipo, cant, zona);
					}
				}
			
		}
		new Ajax.Request('AjaxGetZona', requestOptions);
	} else {
		// alert("direccion2: " + $j('#direccion').val() + " | zona: " +
		// ($j('#direccion').val().split("_"))[2]);
		zona = ($j('#direccion').val().split("_"))[2];
		$j('#agregadireccion').show();
		$j('#nuevadireccion').show();
		showCalendario(tipo, cant, zona);
	}
	$j('#retira').hide();
	$j('#autoriza').show();
	$j('#observa').show();
}
}


function showCalendario(sel,cant_prod,zona) {
  var requestOptions = {
    'method': 'get',
    'parameters': {},
    'onSuccess': creaCalendario
  };
  var rndmx = Math.floor(Math.random()*10000);
  new Ajax.Request("AjaxDespachoChart?sel=" + sel + "&cant_prod=" + cant_prod + "&zona_id=" + zona + "&rndmx=" + rndmx, requestOptions);
}

function creaCalendario(REQUEST){
	var a = document.getElementById('frm_despacho');
	a.innerHTML = "";
	try {
          a.innerHTML = REQUEST.responseText;
          tp1 = new WebFXTabPane( document.getElementById( "tabPane1" ) );
    } catch (e) {
    	// IE fails unless we wrap the string in another element.
        var wrappingElement = document.createElement('div');
        wrappingElement.innerHTML = REQUEST.responseText;
        a.appendChild(wrappingElement);
    }
}
function buscaClientePorRut() {
	var params = "rutCliente=" + document.getElementById("rut1").value;
	var requestOptions = {
		'method' : 'post',
		'parameters' : params,
		'onSuccess' : function(REQUEST) {
			if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
				alert("Ocurrió un error al obtener al cliente por el rut.");
				return;
			}
			var respuesta = REQUEST.responseXML
					.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
			if (respuesta == "OK") {
				var rutcliente;
				rutcliente = document.getElementById("rut1").value;// + "-" + document.getElementById("dv1").value;
				document.getElementById("cli_rut").value = rutcliente;					
				document.acceso3.url_to.value = "/FO/GuardaCriteriosMiCarro";
				showLoginSession();
				showClienteSinRegistro();
				document.acceso3.cli_rut.focus();
				document.acceso3.clave.focus();
			} 
			if (respuesta == "rutcorto"){
				alert ("El RUT es incorrecto");
				$j('#rut1').focus();
				
			}
		}
	}
	new Ajax.Request('AjaxExisteCliente', requestOptions);
}
function logon3(type_lbox) {
	  var f2 = document.acceso3;
	  if ( !validaFormLogin( f2 ) ) {
	    return;
	  }
	  goToTheSite(f2.cli_rut.value, f2.clave.value, type_lbox, f2.url_to.value);  
	}
function goToTheSite(rut_x, clave_x, type_x, url_x) {
	
	var objJSON="\"rut\":\""+rut_x+"\", \"clave\":\""+ clave_x+"\", \"type_log\":\""+type_x+"\", \"destination\":\""+ url_x+"\"";
	
	if(jQuery('#RegistroSencillo #accesoLfckfield').val() != null)
		objJSON+=", \"accesoLfckfield\":\""+jQuery('#RegistroSencillo #accesoLfckfield').val()+"\"";

	objJSON = eval ("({" + objJSON + "})"); 
		
  jQuery.post("CheckLogin", objJSON
    ,function(xml) {
      jQuery(xml).find('login').each(function() {
        var mensaje = jQuery(this).find('mensaje').text();
        var destination = jQuery(this).find('destination').text();
        if ( mensaje == 'OK' ) {
          destination = replaceAll(destination,"[q]","?");
          destination = replaceAll(destination,"[a]","&");
          if(destination == '/FO/CategoryDisplay'){
	          if(document.getElementById("cab") != null){
	        	  destination = destination + "?cab=" + document.getElementById("cab").value;
	          }
	          if(document.getElementById("int") != null){
	        	  destination = destination + "&int=" + document.getElementById("int").value;
	          }
	          if(document.getElementById("ter") != null){
	        	  destination = destination + "&ter=" + document.getElementById("ter").value;
	          }
          }
          window.location.href=destination;
          return;
        } else {
          alert(mensaje);
          //document.acceso.clave.focus();
          return;
        }
      });
    }
  );
}


function busca_comunas(comuna){
	var params = "id_region=" + $j('#region').val();
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			$j('#comuna').html(REQUEST.responseText);
			if (comuna)
				$j('#comuna').val(comuna);
		}
	}
	new Ajax.Request('ComunasConCoberturaByRegion', requestOptions);
}


function DesactivaCamposDireccion(){
    $j('#tipo_calle').attr("disabled", "true");
    $j('#calle').attr("disabled", "true");
    $j('#numero').attr("disabled", "true");
    $j('#departamento').attr("disabled", "true");
    $j('#region').attr("disabled", "true");
    $j('#comuna').attr("disabled", "true");
    $j('#alias').attr("disabled", "true");
    
	$j('#bt_guardar_dir_despacho').hide();
    $j('#bt_editar_dir_despacho').show();
    
    $j('#mensajedummy').hide();    
}

function ActivaCamposDireccion(){
    //$input.removeAttr('disabled');
	
    $j('#tipo_calle').removeAttr('disabled');
    $j('#calle').removeAttr('disabled');
    $j('#numero').removeAttr('disabled');
    $j('#departamento').removeAttr('disabled');
    $j('#region').removeAttr('disabled');
    $j('#comuna').removeAttr('disabled');
    $j('#alias').removeAttr('disabled');

	$j('#bt_guardar_dir_despacho').show();
    $j('#bt_editar_dir_despacho').hide();
    $j('#mensajedummy').show();

    /*$j('#tipo_calle').attr("disabled", "false");
    $j('#calle').attr("disabled", "false");
    $j('#numero').attr("disabled", "false");
    $j('#departamento').attr("disabled", "false");
    $j('#region').attr("disabled", "false");
    $j('#comuna').attr("disabled", "false");
    $j('#alias').attr("disabled", "false");*/
}


function guarda_direccion() {
	var campos_validos = true;
	if (!$j('#ingresadireccion').is(':hidden')) {
		if ($j('#tipo_calle').val() == 0) {
			alert("Debes seleccionar el Tipo de calle");
			$j('#tipo_calle').focus();
			campos_validos = false;
			try {
				mxTracker._trackEvent('Despacho', 'Guardar direccion',
						'Debes seleccionar el Tipo de calle');
			} catch (e) {
			}
			;
		} else if ($j('#calle').val() == "") {
			alert("Debes ingresar el campo calle");
			$j('#calle').focus();
			campos_validos = false;
			try {
				mxTracker._trackEvent('Despacho', 'Guardar direccion',
						'Debes ingresar el campo calle');
			} catch (e) {
			}
			;

		} else if ($j('#calle').val() == "Ingresa tu calle") {
			alert("Debes ingresar el campo calle");
			$j('#calle').focus();
			campos_validos = false;
			try {
				mxTracker._trackEvent('Despacho', 'Guardar direccion',
						'Debes ingresar el campo calle');
			} catch (e) {
			}
			;
		} else if ($j('#numero').val() == "") {
			alert("Debes ingresar el campo Número");
			$j('#numero').focus();
			campos_validos = false;
			try {
				mxTracker._trackEvent('Despacho', 'Guardar direccion',
						'Debes ingresar el campo Número');
			} catch (e) {
			}
			;
		} else if ($j('#numero').val() == "000") {
			alert("Debes ingresar el campo Número");
			$j('#numero').focus();
			campos_validos = false;
			try {
				mxTracker._trackEvent('Despacho', 'Guardar direccion',
						'Debes ingresar el campo Número');
			} catch (e) {
			}
			;
		} else if ($j('#region').val() == 0) {
			alert("Debes seleccionar la Región");
			$j('#region').focus();
			campos_validos = false;
			try {
				mxTracker._trackEvent('Despacho', 'Guardar direccion',
						'Debes seleccionar la Región');
			} catch (e) {
			}
			;
		} else if ($j('#comuna').val() == 0) {
			alert("Debes seleccionar la Comuna");
			$j('#comuna').focus();
			campos_validos = false;
			try {
				mxTracker._trackEvent('Despacho', 'Guardar direccion',
						'Debes seleccionar la Comuna');
			} catch (e) {
			}
			;
		} else if ($j('#alias').val() == "") {
			alert("Debes ingresar un campo Nombre Referencial");
			$j('#alias').focus();
			campos_validos = false;
			try {
				mxTracker._trackEvent('Despacho', 'Guardar direccion',
						'Debes ingresar un campo Nombre Referencial');
			} catch (e) {
			}
			;
		} else if ($j('#alias').val() == "Nombre") {
			alert("Debes ingresar un campo Nombre Referencial");
			$j('#alias').focus();
			campos_validos = false;
			try {
				mxTracker._trackEvent('Despacho', 'Guardar direccion',
						'Debes ingresar un campo Nombre Referencial');
			} catch (e) {
			}
			;
		}

		//alert("agregadireccion: " + agregadireccion + " | campos_validos: "+ campos_validos);
		if (campos_validos) {
			DesactivaCamposDireccion();
			if (agregadireccion) {
				if (id_cliente != 1) {
					var params = "tipo_calle=" + $j('#tipo_calle').val();
					params += "&calle=" + $j('#calle').val();
					params += "&numero=" + $j('#numero').val();
					params += "&departamento=" + $j('#departamento').val();
					params += "&region=" + $j('#region').val();
					params += "&comuna=" + $j('#comuna').val();
					params += "&alias=" + $j('#alias').val();
					var requestOptions = {
						'method' : 'post',
						'parameters' : params,
						'onSuccess' : function(REQUEST) {
							$j('#direccion').html(REQUEST.responseText);
							muestraCalendario();
						}
					}
					try {
						mxTracker._trackEvent('Despacho', 'Guardar direccion',
								'Direccion guardada');
					} catch (e) {
					}
					;
					new Ajax.Request('AjaxAgregaDireccion', requestOptions);
				}
				agregadireccion = false;
			} else {
				var tipo = $j("input[@name='tipodespacho']:checked").val()
				if (tipo == "D" && id_cliente != 1) {
					// alert("direccion1: " + $j('#direccion').val() + " |
					// dir_id: " + ($j('#direccion').val().split("_"))[0]);
					var id = ($j('#direccion').val().split("_"))[0];
					var params = "id_dir=" + id;
					params += "&tipo_calle=" + $j('#tipo_calle').val();
					params += "&calle=" + $j('#calle').val();
					params += "&numero=" + $j('#numero').val();
					params += "&departamento=" + $j('#departamento').val();
					params += "&region=" + $j('#region').val();
					params += "&comuna=" + $j('#comuna').val();
					params += "&alias=" + $j('#alias').val();
					var requestOptions = {
						'method' : 'post',
						'parameters' : params,
						'onSuccess' : function(REQUEST) {
							$j('#direccion').html(REQUEST.responseText);
							muestraCalendario();
						}
					}
					try {
						mxTracker._trackEvent('Despacho', 'Guardar direccion',
								'Direccion guardada');
					} catch (e) {
					}
					;
					new Ajax.Request('AjaxModificaDireccion', requestOptions);
				} else if (id_cliente == 1) {
					$j('input[name=tipodespacho][value=D]').attr('checked',
							'checked');
					changeComuna();
					muestraCalendario();
					DesactivaCamposDireccion();
					try {
						mxTracker._trackEvent('Despacho', 'Guardar direccion',
								'Direccion guardada');
					} catch (e) {
					}
					;

				}
			}
		}
	}
	
}

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

function linkJumboAuto() {
	id_local = ($j('#localretiro').val().split("_"))[0];
	window.open("/FO_WebContent/statics/autojumbo" + id_local + ".html",'_blank');
}

function nuevadireccion(){
    ActivaCamposDireccion();
	agregadireccion = true;
	$j('#tipo_calle').val(2);
	$j('#calle').val("Ingresa tu calle");
	$j('#numero').val("000");
	$j('#departamento').val("");
	$j('#region').val(3);
	$j('#comuna').html("<option value=\"1\">Seleccionar</option>");
	$j('#alias').val("casa");
}

function traedireccion() {
    //alert("dir_id: " + ($j('#direccion').val().split("_"))[0]);
	var id = ($j('#direccion').val().split("_"))[0];
	var params = "id_dir=" + id;
	var requestOptions = {
    	'method': 'post',
	    'parameters': params,
    	'onSuccess': function(REQUEST) {
    		if (REQUEST.responseXML != null) {
       			var region = REQUEST.responseXML.getElementsByTagName("region")[0].childNodes[0].nodeValue;
          		$j('#region').val(region);
          		var tipocalle = REQUEST.responseXML.getElementsByTagName("tipocalle")[0].childNodes[0].nodeValue;
          		var calle = REQUEST.responseXML.getElementsByTagName("calle")[0].childNodes[0].nodeValue;
          		var numero = REQUEST.responseXML.getElementsByTagName("numero")[0].childNodes[0].nodeValue;
          		var departamento="";
          		if (!REQUEST.responseXML.getElementsByTagName("departamento")[0].childNodes)
          			var departamento = REQUEST.responseXML.getElementsByTagName("departamento")[0].childNodes[0].nodeValue;
          		var comuna = REQUEST.responseXML.getElementsByTagName("comuna")[0].childNodes[0].nodeValue;
          		var alias = REQUEST.responseXML.getElementsByTagName("alias")[0].childNodes[0].nodeValue;
          		
          		//$j.get("/FO/ComunasConCoberturaByRegion",{id_region:region, id_comuna:comuna},function(datos) {
          		$j.get("/FO/ComunasConCoberturaByRegion",function(datos) {
    				$j("#comuna").html(datos);
			    	$j("#comuna").removeAttr('disabled');
			    	DesactivaCamposDireccion();
			  	});
				
				$j('#tipo_calle').val(tipocalle);
          		$j('#calle').val(calle);
          		$j('#numero').val(numero);
          		$j('#departamento').val(departamento);
          		$j('#alias').val(alias);
          		setTimeout($j('#comuna').val(comuna), 5000);
          		
          	}	
		}
	};
	new Ajax.Request('AjaxTraeDireccion', requestOptions);
}

function ValidaCarroCompras(local) {
  var resp = false;
  // 20121113 INDRA
  var borrarProductos = 'javascript:borrarProductosDespacho()';
  var reemplazarProductos = 'javascript:reemplazarProductosDespacho()';
  var params = {'local':local, 'borrarProductos' : borrarProductos, 'reemplazarProductos' : reemplazarProductos};
  // 20121113 INDRA
  //Antes de continuar verificamos el carro del cliente
  var requestOptions = {
    'asynchronous':false,
    'method': 'post',
    'parameters': params,
    'onSuccess': function (REQUEST) {
      if (REQUEST.responseXML != null) {
        if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] != null) {
          //en caso de q no venga el msg, al gatillar la accion de confirmar la compra el sistema mandara pantalla de error o al login
          var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
          if (mensaje != 'OK') {
            var cantidad = REQUEST.responseXML.getElementsByTagName("cantidad")[0].childNodes[0].nodeValue;
            var msg = "Existen productos sin disponibilidad para esta selección, presione Aceptar ";
            msg += "si desea revisarlos o Cancelar si desea que los quitemos de su carro ";
            msg += "y pueda proceder al siguiente paso";
            if ( confirm( msg ) ) {
              $j('#redireccion').val('1');	
              resp = false;
            }else{
              resp = true;
            }
          }else{
            resp = true;
          }
		// INICIO INDRA 22-10-2012
        } else {
        	resp = false;
	      	var respuesta = REQUEST.responseText;
			// 20121113 INDRA
			document.getElementById("respuestaLightBox").innerHTML = "";
	      	document.getElementById("respuestaLightBox").innerHTML = respuesta;
	      	//$j("#respuestaDespacho").html(respuesta);
			if (document.getElementById("existen_despublicados_carro") && document.getElementById("existen_despublicados_carro").value != '' && eval(document.getElementById("existen_despublicados_carro").value)> 0){
			// 20121113 INDRA
				//var htmlPopup = $j("#idContPopup").html();
				//document.getElementById("popup_despacho").value = htmlPopup;
				muestraLBProdDesp();
			} 
			else {
				document.despacho.action = "GuardaDatosDespacho";
				document.despacho.submit();	
			}
		// FIN INDRA 22-10-2012
        }
		// INICIO INDRA 22-10-2012
      }else {
      	resp = false;
      	var respuesta = REQUEST.responseText;
      	//$j("#respuestaDespacho").html(respuesta);
		// 20121113 INDRA
      	document.getElementById("respuestaLightBox").innerHTML = "";
      	document.getElementById("respuestaLightBox").innerHTML = respuesta;
		if (document.getElementById("existen_despublicados_carro") && document.getElementById("existen_despublicados_carro").value != '' && eval(document.getElementById("existen_despublicados_carro").value)> 0){
		// 20121113 INDRA
			//var htmlPopup = $j("#idContPopupDespacho").html();
			//document.getElementById("popup_despacho").value = htmlPopup;
			muestraLBProdDesp();
		} 
		else {
			document.despacho.action = "GuardaDatosDespacho";
			document.despacho.submit();	
		}
      // FIN INDRA 22-10-2012
	  }
    }
  };
  new Ajax.Request('ValidaCarroCompras', requestOptions);
  return resp;
}
// 20121113 INDRA
function borrarProductosDespacho(){
	var productos = document.getElementById("productosList_carro").value;
// 20121113 INDRA
	if (productos && productos.length > 0){
		var params = {'productos' : productos};
		var requestOptions ={
			'method' : 'post', 
			'parameters' : params,
			'asynchronous' : false,
			'onSuccess' : function(REQUEST){
				if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
					alert("Error al Eliminar productos despublicados del carro los productos.");
					return;
				}
				var message = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
				if (message == 'OK'){
					document.despacho.action = "GuardaDatosDespacho";
					document.despacho.submit();	
				} else {
					alert("Error en la Eliminación de Productos desde el Carro");
				}
			}
		};
		new Ajax.Request('/FO/EliminaProductosCarro', requestOptions);
	}
}

function verPopup(){
	closeLBoxWin();
	var inicio = '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">\n'
				+ '<html xmlns="http://www.w3.org/1999/xhtml">\n'
				+ '<head>'
				+ '<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />\n'
				+ '<title>Jumbo.cl</title>\n'
				+ '<script type="text/javascript" src="/FO_WebContent/js/jquery.tablesorter.min.js"></script>\n'
				+ '<script type="text/javascript" src="/FO_WebContent/js/zebra_accordion.src.js"></script>\n'
				+ '<script type="text/javascript" src="/FO_WebContent/js/thickbox.js"></script>\n'
				+ '<script type="text/javascript" src="/FO_WebContent/js/paso1.js"></script>\n'
				
				+ '<link type="text/css" rel="stylesheet" href="/FO_WebContent/css/thickbox.css" media="screen" />\n'
				+ '<link rel="stylesheet" type="text/css" href="/FO_WebContent/css/tabla.css" />\n'
				+ '<link rel="stylesheet" type="text/css" href="/FO_WebContent/css/jquery.ui.all.css" />\n'
				+ '<link rel="stylesheet" type="text/css" href="/FO_WebContent/css/demos.css" />\n'
				+ '<link rel="stylesheet" type="text/css" href="/FO_WebContent/css/zebra_accordion.css" />\n'
				+ '<link type="text/css" rel="stylesheet" href="/FO_WebContent/css/acordeon_paso1b.css" />\n'
				+ '<link type="text/css" rel="stylesheet" href="/FO_WebContent/css/popup.css" />\n'
				+ '</head><body>';	
	var fin = '</body></html>';
// 20121113 INDRA
	htmlPopup = $j("#idContPopupCarro").html();
// 20121113 INDRA
	window.location.href = '/FO/CategoryDisplay';
	windowPopup = window.open('','','width=410, height=410');
	windowPopup.document.write( inicio + htmlPopup + fin);
	windowPopup.focus();
}

// 20121113 INDRA
function reemplazarProductosDespacho(){
	var productos = document.getElementById("productosList_carro").value;
// 20121113 INDRA
	if (productos && productos.length > 0){
		var params = {'productos' : productos};
		var requestOptions ={
			'method' : 'post', 
			'parameters' : params,
			'asynchronous' : false,
			'onSuccess' : function(REQUEST){
				if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
					alert("Error al Eliminar productos despublicados del carro los productos.");
					return;
				}
				var message = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
				if (message == 'OK'){
					verPopup();
				} else {
					alert("Error en la Eliminación de Productos desde el Carro");
				}
			}
		};
		new Ajax.Request('/FO/EliminaProductosCarro', requestOptions);
	}
}
function copiaMail(){
	$j('#email').val($j('#emailinv').val());
}
function copiaTelefono1(){
	$j('#codigo > option').remove();
	$j('#telefono').val($j('#fono_num_1inv').val()); //copia numero telefono
	$j('#fon_cod_1inv option:selected').clone().appendTo("#codigo");//copia codigo
}

// FIN INDRA 22-10-2012
function continuar() {
    var campos_validos = true; //cambiar a true
	var tipo = $j("input[@name='tipodespacho']:checked").val();
	var local

	if (id_cliente == 1){
		//Inicio Validacion datos Cliente Invitado
		var rutcorrecto= checkRutValue(document.getElementById('rut1'));
		if (!rutcorrecto){
			$j('#rut1').focus();
			campos_validos = false;
			return;
		}
		if ($j('#rut1').val() == ""){
			alert("Debes ingresar tu RUT");
			$j('#rut1').focus();
			campos_validos = false;	
			return;
		}else if (($j('#nombreinv').val()).trim() == ""){
			alert("Debes ingresar tu nombre");
			$j('#nombreinv').focus();
			campos_validos = false;
			return;
		}else if (($j('#ape_patinv').val()).trim() == ""){
			alert("Debes ingresar tu apellido");
			$j('#ape_patinv').focus();
			campos_validos = false;		
			return;
		}else if ($j('#emailinv').val() == ""){
			alert("Debes ingresar tu Mail");
			$j('#emailinv').focus();
			campos_validos = false;	
			return;
		} else if(!/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/.test( $j('#emailinv').val() )) { 
			alert("Debes ingresar correctamente tu email");
			$j('#emailinv').focus();
			campos_validos = false;
			try{mxTracker._trackEvent('Despacho','Email estado compra','Debes ingresar correctamente tu email');}catch(e){};
			return;
		} else if ($j('#fono_num_1inv').val() == ""){
			alert("Debes ingresar por lo menos un número telefónico");
			$j('#fono_num_1inv').focus();
			campos_validos = false;
			return;
		}
		//Fin Validacion Datos Cliente Invitado
		if (tipo == "D") {
			if ($j('#tipo_calle').val() == 0){
				alert("Debes seleccionar el Tipo de calle");
				$j('#tipo_calle').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes seleccionar el Tipo de calle');}catch(e){};
			} else if ($j('#calle').val() == "") {
				alert("Debes ingresar el campo calle");
				$j('#calle').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes seleccionar el campo de calle');}catch(e){};
			} else if ($j('#calle').val() == "Ingresa tu calle") {
				alert("Debes ingresar el campo calle");
				$j('#calle').focus();
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes ingresar el campo calle');}catch(e){};
				campos_validos = false;
			} else if ($j('#numero').val() == "") {
				alert("Debes ingresar el campo Número");
				$j('#numero').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes ingresar el campo Número');}catch(e){};
			} else if ($j('#numero').val() == "000") {
				alert("Debes ingresar el campo Número");
				$j('#numero').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes ingresar el campo Número');}catch(e){};
			} else if ($j('#region').val() == 0) {
				alert("Debes seleccionar la Región");
				$j('#region').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes seleccionar la Región');}catch(e){};
			} else if ($j('#comuna').val() == 0) {
				alert("Debes seleccionar la Comuna");
				$j('#comuna').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes seleccionar la Comuna');}catch(e){};
			} else if ($j('#alias').val() == "") {
				alert("Debes ingresar un campo Nombre Referencial");
				$j('#alias').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes ingresar un campo Nombre Referencial');}catch(e){};
			} else if ($j('#alias').val() == "Nombre") {
				alert("Debes ingresar un campo Nombre Referencial");
				$j('#alias').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes ingresar un campo Nombre Referencial');}catch(e){};
			} else if ($j('#autorizacion_nom').val() == "") {
				alert("Debes indicarnos el nombre de la persona que autorizas para recibir el pedido");
				$j('#autorizacion_nom').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos el nombre de la persona que autorizas para recibir el pedido"');}catch(e){};
			} else if ($j('#autorizacion_ape').val() == "") {
				alert("Debes indicarnos el apellido de la persona que autorizas para recibir el pedido");
				$j('#autorizacion_ape').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos el apellido de la persona que autorizas para recibir el pedido');}catch(e){};
			} else if ($j('#autorizacion_nom').val() == "Ej: Conserje") {
				alert("Debes indicarnos a quien autorizas para recibir tu pedido");
				$j('#autorizacion').val('');
				$j('#autorizacion').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos a quien autorizas para recibir tu pedido');}catch(e){};
			} else if ($j('#email').val() == "") {
				alert("Debes ingresar tu email");
				$j('#email').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Email estado compra','Debes ingresar tu email');}catch(e){};
				return;
			} else if(!/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/.test( $j('#email').val() )) { 
				alert("Debes ingresar correctamente tu email");
				$j('#email').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Email estado compra','Debes ingresar correctamente tu email');}catch(e){};
				return;
			} else if ($j('#telefono').val() == "") {
				alert("Debes ingresar tu teléfono");
				$j('#telefono').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Teléfono estado compra','Debes ingresar tu teléfono');}catch(e){};
			} else if(isLongNumberInsert(jQuery("#codigo").val())){
					 if(!/^\d{8}$/.test(jQuery('#telefono').val())) {
						alert('Debe ingresar su número de contacto correctamente');
						$j('#telefono').focus();
						campos_validos = false;
						try{mxTracker._trackEvent('Despacho','Teléfono estado compra','Debe ingresar su número de contacto correctamente');}catch(e){};
					 }
					}else{
					if(!/^\d{7}$/.test(jQuery('#telefono').val())) {
						alert('Debe ingresar su número de contacto correctamente');
						$j('#telefono').focus();
						campos_validos = false;
						try{mxTracker._trackEvent('Despacho','Teléfono estado compra','Debe ingresar su número de contacto correctamente');}catch(e){};
					}	
			} 
		} else if (tipo == "R") {
			 if ($j('#autorizacion_nom').val() == "") {
					alert("Debes indicarnos el nombre de la persona que autorizas para recibir el pedido");
					$j('#autorizacion_nom').focus();
					campos_validos = false;
					try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos el nombre de la persona que autorizas para recibir el pedido"');}catch(e){};
				} else if ($j('#autorizacion_ape').val() == "") {
					alert("Debes indicarnos el apellido de la persona que autorizas para recibir el pedido");
					$j('#autorizacion_ape').focus();
					campos_validos = false;
					try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos el apellido de la persona que autorizas para recibir el pedido');}catch(e){};
				}else if ($j('#autorizacion_nom').val() == "Ingrese Nombre") {
					alert("Debes indicarnos el nombre de la persona que autorizas para recibir el pedido");
					$j('#autorizacion_nom').focus();
					campos_validos = false;
					try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos el nombre de la persona que autorizas para recibir el pedido"');}catch(e){};
				} else if ($j('#autorizacion_ape').val() == "Ingrese Apellido") {
					alert("Debes indicarnos el apellido de la persona que autorizas para recibir el pedido");
					$j('#autorizacion_ape').focus();
					campos_validos = false;
					try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos el apellido de la persona que autorizas para recibir el pedido');}catch(e){};
			}else if ($j('#rut').val() == 0){
				alert("Debes indicarnos tu Nombre y RUT o el de quien autorizas para retirar tu pedido");
				$j('#rut').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos tu Nombre y RUT o el de quien autorizas para retirar tu pedido');}catch(e){};
			} else if ($j('#dv').val() == "") {
				alert("Debes indicarnos el dígito verificador de la persona que retirará el pedido");
				$j('#dv').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos el dígito verificador de la persona que retirará el pedido');}catch(e){};
			} else if(!checkRutField( $j('#rut').val()+"-"+$j('#dv').val(), $j('#rut') ) ) {
				$j('#rut').focus();
				campos_validos = false;
			} else if ($j('#email').val() == "") {
				alert("Debes indicarnos tu email para recibir el estado de tu compra");
				$j('#email').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Email estado compra','Debes indicarnos tu email para recibir el estado de tu compra');}catch(e){};
				return;
			} else if(!/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/.test( $j('#email').val() )) { 
				alert("Debes ingresar correctamente tu email");
				$j('#email').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Email estado compra','Debes ingresar correctamente tu email');}catch(e){};
				return;
			} else if ($j('#telefono').val() == "") {
				alert("Debes ingresar tu teléfono");
				$j('#telefono').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Teléfono estado compra','Debes ingresar tu teléfono');}catch(e){};
			}else if(isLongNumberInsert(jQuery("#codigo").val())){
				 if(!/^\d{8}$/.test(jQuery('#telefono').val())) {
						alert('Debe ingresar su número de contacto correctamente');
						$j('#telefono').focus();
						campos_validos = false;
						try{mxTracker._trackEvent('Despacho','Teléfono estado compra','Debe ingresar su número de contacto correctamente');}catch(e){};
		}
	} else {
					if(!/^\d{7}$/.test(jQuery('#telefono').val())) {
						alert('Debe ingresar su número de contacto correctamente');
						$j('#telefono').focus();
						campos_validos = false;
						try{mxTracker._trackEvent('Despacho','Teléfono estado compra','Debe ingresar su número de contacto correctamente');}catch(e){};
					}	
			    } 
		}
	} else {//cliente registrado
		if (tipo == "D") {
			 if ($j('#autorizacion_nom').val() == "") {
					alert("Debes indicarnos el nombre de la persona que autorizas para recibir el pedido");
					$j('#autorizacion_nom').focus();
					campos_validos = false;
					try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos el nombre de la persona que autorizas para recibir el pedido"');}catch(e){};
			} else if ($j('#autorizacion_ape').val() == "") {
					alert("Debes indicarnos el apellido de la persona que autorizas para recibir el pedido");
					$j('#autorizacion_ape').focus();
					campos_validos = false;
					try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos el apellido de la persona que autorizas para recibir el pedido');}catch(e){};
			} else if ($j('#autorizacion').val() == "Ej: Conserje") {
				alert("Debes indicarnos a quien autorizas para recibir tu pedido");
				$j('#autorizacion').val('');
				$j('#autorizacion').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos a quien autorizas para recibir tu pedido');}catch(e){};
			} else if ($j('#email').val() == "") {
				alert("Debes ingresar tu email");
				$j('#email').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Email estado compra','Debes ingresar tu email');}catch(e){};
				return;
			} else if ($j('#telefono').val() == "") {
				alert("Debes ingresar tu teléfono");
				$j('#telefono').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Despacho','Teléfono estado compra','Debes ingresar tu teléfono');}catch(e){};
			} else if(isLongNumberInsert(jQuery("#codigo").val())){
				 if(!/^\d{8}$/.test(jQuery('#telefono').val())) {
						alert('Debe ingresar su número de contacto correctamente');
						$j('#telefono').focus();
						campos_validos = false;
						try{mxTracker._trackEvent('Despacho','Teléfono estado compra','Debe ingresar su número de contacto correctamente');}catch(e){};
					 }
					}else{
					if(!/^\d{7}$/.test(jQuery('#telefono').val())) {
						alert('Debe ingresar su número de contacto correctamente');
						$j('#telefono').focus();
						campos_validos = false;
						try{mxTracker._trackEvent('Despacho','Teléfono estado compra','Debe ingresar su número de contacto correctamente');}catch(e){};
					}	
			} 
		} else if (tipo == "R") {
			 if ($j('#autorizacion_nom').val() == "") {
					alert("Debes indicarnos el nombre de la persona que autorizas para recibir el pedido");
					$j('#autorizacion_nom').focus();
					campos_validos = false;
					try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos el nombre de la persona que autorizas para recibir el pedido"');}catch(e){};
				} else if ($j('#autorizacion_ape').val() == "") {
					alert("Debes indicarnos el apellido de la persona que autorizas para recibir el pedido");
					$j('#autorizacion_ape').focus();
					campos_validos = false;
					try{mxTracker._trackEvent('Despacho','Datos entrega','Debes indicarnos el apellido de la persona que autorizas para recibir el pedido');}catch(e){};

				} else if ($j('#rut').val() == 0){
				alert("Debes indicarnos el RUT de la persona que retirará el pedido");
				$j('#rut').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Retiro','Datos entrega','Debes indicarnos el RUT de la persona que retirará el pedido');}catch(e){};
			} else if ($j('#dv').val() == "") {
				alert("Debes indicarnos el dígito verificador de la persona que retirará el pedido");
				$j('#dv').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Retiro','Datos entrega','Debes indicarnos el dígito verificador de la persona que retirará el pedido');}catch(e){};
			} else if(!checkRutField( $j('#rut').val()+"-"+$j('#dv').val(), $j('#rut') ) ) {
				$j('#rut').focus();
				campos_validos = false;
			} else if ($j('#email').val() == "") {
				alert("Debes indicarnos tu email para recibir el estado de tu compra");
				$j('#email').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Retiro','Email estado compra','Debes indicarnos tu email para recibir el estado de tu compra');}catch(e){};
				return;
			} else if ($j('#telefono').val() == "") {
				alert("Debes ingresar tu teléfono");
				$j('#telefono').focus();
				campos_validos = false;
				try{mxTracker._trackEvent('Retiro','Teléfono estado compra','Debes ingresar tu teléfono');}catch(e){};
			}else if(isLongNumberInsert(jQuery("#codigo").val())){
				 if(!/^\d{8}$/.test(jQuery('#telefono').val())) {
						alert('Debe ingresar su número de contacto correctamente');
						$j('#telefono').focus();
						campos_validos = false;
						try{mxTracker._trackEvent('Despacho','Teléfono estado compra','Debe ingresar su número de contacto correctamente');}catch(e){};
					 }
					}else{
					if(!/^\d{7}$/.test(jQuery('#telefono').val())) {
						alert('Debe ingresar su número de contacto correctamente');
						$j('#telefono').focus();
						campos_validos = false;
						try{mxTracker._trackEvent('Despacho','Teléfono estado compra','Debe ingresar su número de contacto correctamente');}catch(e){};
					}	
			}
		}
	}
	
	if (campos_validos && ($j('#calle').attr('disabled') == false) && (tipo == "D")){ //LOS CAMPOS ESTAN EDITABLES
	    alert('Debe Guardar su Dirección antes de continuar.');
		try{mxTracker._trackEvent('Despacho','Datos entrega','Debe Guardar su Dirección antes de continuar');}catch(e){};
	    $j('#alias').focus();
	}else if (campos_validos){
		// Copiar datos del iframe del calendario de despachos
	    var jpic = document.t_picking.jpicking.value;
	    var jpre = document.t_picking.jprecio.value;
	    var jdes = document.t_picking.jdespacho.value;
	    var jfec = document.t_picking.jfecha.value;
	    var tdes = document.t_picking.tipo_despacho.value;
	    var zdes = document.t_picking.zona_despacho.value;
	    var horsdes = document.t_picking.horas_economico.value;
	    var jclav = document.t_picking.jclave.value;
	    
	    if ( jpic == "" || jpre == "" || jdes == "" ) {
			if (tipo == 'R' ) {
				alert( "Debes seleccionar un horario de retiro." );
				try{mxTracker._trackEvent('Despacho','Tipo Despacho','Debes seleccionar un horario de retiro');}catch(e){};
			} else {
	        	alert( "Debes seleccionar un horario de despacho." );
				try{mxTracker._trackEvent('Despacho','Tipo Despacho','Debes seleccionar un horario de despacho');}catch(e){};
	        }
	        //return false;
	    }else{
		    $j('#destino').val(tipo);
		    $j('#des_jpicking').val(jpic);
		    $j('#des_jprecio').val(jpre);
		    $j('#des_jdespacho').val(jdes);
		    $j('#des_jfecha').val(jfec);
		    $j('#des_tdespacho').val(tdes);
		    $j('#des_hh_economico').val(horsdes);
		    $j('#des_zona_despacho').val(zdes);
		    $j('#des_jclave').val(jclav);		    
		    
		    if (tipo == "R") {
		    	$j('#des_localretiro').val(($j('#localretiro').val().split("_"))[0]);
		    	$j('#aut_despacho').val($j('#autorizacion_nom').val()+' '+$j('#autorizacion_ape').val());
		    	$j('#rutautorizado').val($j('#rut').val());
		    	$j('#dvautorizado').val($j('#dv').val());
		    } else if (tipo == "D") {
		    	if (id_cliente == 1) {
		    		$j('#tipocalle_invitado').val($j('#tipo_calle').val());
		    		$j('#calle_invitado').val($j('#calle').val());
		    		$j('#numero_invitado').val($j('#numero').val());
		    		$j('#depto_invitado').val($j('#departamento').val());
		    		$j('#region_invitado').val($j('#region').val());
		    		$j('#comuna_invitado').val($j('#comuna').val());
		    		$j('#alias_invitado').val($j('#alias').val());
		    	} else {
		    	    //alert("direccion" + $j('#direccion').val());
		    		$j('#id_direccion').val(($j('#direccion').val().split("_"))[0]);
		    		$j('#local_direccion').val(($j('#direccion').val().split("_"))[1]);
		    	}
		    	$j('#aut_despacho').val($j('#autorizacion_nom').val()+' '+$j('#autorizacion_ape').val());
		    	$j('#obs_despacho').val($j('#observacion').val());
		    }
		    $j('#email_despacho').val($j('#email').val());
            $j('#codtel_despacho').val($j('#codigo').val());
            $j('#tel_despacho').val($j('#telefono').val());
            
            
            //Valida si el cliente posee productos no disponibles
			if (tipo == "R") {
				local = ($j('#localretiro').val().split("_"))[0];
			} else if (tipo == "D") {
	    		if (id_cliente == 1) {
					local = 0;
	    		} else {
	    			local = ($j('#direccion').val().split("_"))[1];
	    		}
	    	}
	    	// INICIO INDRA 22-10-2012
			var validacion = ValidaCarroCompras(local);
		    if (validacion){	
		    	if (id_cliente==1)
		    		copiarDataInvitadoSubmit();//se copia los datos del invitado dentro del form despacho
			    document.despacho.action = "GuardaDatosDespacho";
			    document.despacho.submit();	
			}
			// FIN INDRA 22-10-2012
	    }
	}
}
function copiarDataInvitadoSubmit(){
	document.getElementById("rut1_inv").value =document.getElementById("rut1").value;
	document.getElementById("nombre_inv").value =document.getElementById("nombreinv").value;
	document.getElementById("ape_pat_inv").value =document.getElementById("ape_patinv").value;
	document.getElementById("email_inv").value =document.getElementById("emailinv").value;
	document.getElementById("fono_num_1_inv").value =document.getElementById("fono_num_1inv").value;
	//document.getElementById("fono_num_2_inv").value =document.getElementById("fono_num_2inv").value;
	document.getElementById("fono_cod_1_inv").value =document.getElementById("fon_cod_1inv").value;
	//document.getElementById("fono_cod_2_inv").value =document.getElementById("fon_cod_1inv").value;
	document.getElementById("tipo_calle_inv").value =document.getElementById("tipo_calle").value;
	document.getElementById("calle_inv").value =document.getElementById("calle").value;
	document.getElementById("numero_inv").value =document.getElementById("numero").value;
	document.getElementById("departamento_inv").value =document.getElementById("departamento").value;
	document.getElementById("region_inv").value =document.getElementById("region").value;
	document.getElementById("comuna_inv").value =document.getElementById("comuna").value;
	document.getElementById("alias_inv").value =document.getElementById("alias").value;
	var elementos = document.getElementsByName("tipodespacho");
	for(var i=0; i<elementos.length; i++) {
		if (elementos[i].checked){
			document.getElementById("tipodesp").value=elementos[i].value;
			break;
		}
	}
	
	
}

function changeComuna() {
  if ( $j('#id_region').val() == "0" || $j('#id_comuna').val() == "0" ) {
    alert("Seleccione su región y comuna");
    try{mxTracker._trackEvent('SeleccionRegionComuna','Alerta','Region&Comuna');}catch(e){};
    return;
  }
  dataLayer.push({
	     'Region': $j("#id_region option:selected").html(),
	     'Comuna': $j("#id_comuna option:selected").html(),
	     'event': 'Seleccion-Region-Comuna'
	});
  $j.post("/FO/ChangeComunaSession", {id_comuna: $j("#id_comuna").val()}, function(datos){
    $j(datos).find('comuna').each(function() {
      var respuesta  = $j(this).find('mensaje').text();
      $j('#comuna_user_str').html( $j(this).find('nombre_comuna').text() );
      $j('#comuna_user_id').val( $j(this).find('id_comuna').text() );      
      if ( respuesta == "OK" ) {
        closeLBoxWin();
        window.location.href="/FO/MiCarro";
      }
    });
  });
}

function showRegionesCobertura() {
  showChangeComuna();  
  //traemos las regiones
  $j.get("/FO/RegionesConCobertura",function(datos) {
    $j("#id_region").html(datos);
    //TODO aca seleccionar combo de region
  });
}
function getComunasByRegion(idRegion) {
  if ( idRegion == 0 ) {
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

function showChangeComuna() {
  upWeb();
  showLightBoxCategories();
  $j('#comunas_sesion').show();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
}
function showChangeComunaMsj() {
  upWeb();
  greenBack();
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').show();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
}
function showLoginSession() {
  upWeb();
  showLightBoxCategories();  
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').show();
  $j('#olvido_clave').hide();
}
function showRecuperaClave() {
  upWeb();
  showLightBoxCategories();  
  $j('#olvido_clave').show();  
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
}

function whiteBack() {
  $j('#TB_ajaxContent').css({backgroundImage:'url("/FO_IMGS/images/html/fondo_layer_recupera.gif")'});
}
function greenBack() {
  $j('#TB_ajaxContent').css({backgroundImage:'url("/FO_IMGS/images/html/fondo_layer_categorias.gif")'});
}

function justLogin() {
  document.acceso2.url_to.value = "/FO/CategoryDisplay";  
  showLoginSession();
}

function upWeb() {
  if ( XBROWSER == "Microsoft Internet Explorer" ) {
    window.scrollTo(0,0);
  }
}

function showLightBoxCategories() {
  tb_show('Jumbo.cl','#TB_inline?height=358&width=400&inlineId=hiddenModalContent&modal=true');  
}


// INICIO INDRA 22-10-2012
	function muestraLBProdDesp(){
	  showLightBoxProdSinStock();
	  $j(document).click(function(e){
		  id = $j(this).val();
		  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
			  closeLBoxWin();
	  });
	}

	function showLightBoxProdSinStock() {
// 20121113 INDRA	
	  tb_show('Jumbo.cl','#TB_inline?height=500&width=700&inlineId=hiddenModalProdDesp&modal=true');   
// 20121113 INDRA
	}

	function closeLBoxWin() {
	  tb_remove();  
	}
// FIN INDRA 22-10-2012