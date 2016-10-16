var $j = jQuery.noConflict();

// ---- INI TOTALES DEL CARRO ---
function set_total_display( total ,total_desc, total_mas ,total_mas_desc) {
	var total_aux = top.document.ftotal._total.value;
	top.document.ftotal._total.value=total;
	
    $j('div#_total_ahorro').text(total_desc);
    $j('div#_total_tarjeta_mas').text(total_mas);
    $j('div#_total_ahorro_tarjeta_mas').text(total_mas_desc);
	
	if( total_aux != "0" ) {
		// Si existen compras anteriores => recarga
		if( top.frames['if2'] != 'undefined' && top.frames['if2'] != null ) {	
			top.frames['if2'].actualizaListaProductos();
		}
	}
}
function set_total_desc( total, desc, totalDesc) {
	top.document.ftotal_desc._total_desc.value=total;
	top.document.ftotal_desc._total_desc_txt.value=desc;
	var layerTotalDesc = top.document.getElementById('inputtotdesc');
	var layerTablaCarro = top.document.getElementById('tablacarro');
	if (totalDesc != '$0'){
		layerTotalDesc.style.visibility = 'visible';
		layerTotalDesc.style.height = '70px';
		layerTotalDesc.style.display = 'block';
		if (top.ficha_paso == '1'){
			$j('#td_ifrm_carro').style.height = '497px';
		} else if ((top.ficha_paso == '2') || (top.ficha_paso == '2b')) {
			var layerTdCarro = top.document.getElementById('td_ifrm_carro');
			var layerCarro = top.document.getElementById('ifrm_carro');
			layerTdCarro.style.height = '320px';
			layerCarro.style.height = '320px';
		}
	} else {
		layerTotalDesc.style.visibility = 'hidden';
		layerTotalDesc.style.height = '0px';
		layerTotalDesc.style.display = 'none';
		if (top.ficha_paso == '1') {
			$j('#td_ifrm_carro').style.height = '585px';
		} else if ((top.ficha_paso == '2') || (top.ficha_paso == '2b')) {
			var layerTdCarro = top.document.getElementById('td_ifrm_carro');
			var layerCarro = top.document.getElementById('ifrm_carro');
			layerTdCarro.style.height = '400px';
			layerCarro.style.height = '400px';
		}
	}
}
// ---- FIN TOTALES DEL CARRO ---

// ---- INI EDIT CARRO ---
function mc(carr_id, campo, maximo, idProd) {
	if ( validar_cantidad(campo, maximo) == false ) {
		document.fc.reset();
		return;
	}
	cantidadaux = campo.value;
	var d = new Date();
	if ( cantidadaux == 0 ) {
		if (!confirm("Este producto será eliminado del carro de compra\n ¿Esta seguro de realizar la operación?")) {
			document.fc.reset();
			return;
		}
		try{mxTracker._trackEvent('Carro','Modificar carro','Eliminar producto');}catch(e){};
	}
	var params = "id_producto="+idProd+"&carr_id0="+carr_id+"&cantidad0="+cantidadaux+"&registros=1&tm="+d.getTime();
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
				alert("Ocurrió un error al modificar el carro.");
				return;
			}	
			var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
			if (respuesta == 'OK') {
				if ( cantidadaux == 0 ) {
					actualizaLista();
				}
				llenarCarroCompras();
			} else {
				alert(mensaje);
			}			
		}
	};
	new Ajax.Request('ModProductosCarroPaso1', requestOptions);
}
function add( campo, incremento, maximo, id, idProd) {
	var valor = 0;
	if (campo.value == "" || campo.value == "undefined") {
		campo.value = 0;
	}
	valor = parseFloat(campo.value) + parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	campo.value = valor;
	mc(id,campo,maximo,idProd);
	if (campo.value > maximo){
		campo.value = maximo;
	}
	try{mxTracker._trackEvent('Carro','Modificar carro','IncrementarProducto');}catch(e){};
}

function limpiarCarro(){
	var intNOREENVIO=1;
	if (confirm("Está seguro que desea eliminar todos los productos del carro")) {
		try{mxTracker._trackEvent('Mi Carro','Modificar	carro','Limpiar');}catch(e){};
		$j.post("/FO/LimpiarMiCarro", {noreenviar: intNOREENVIO}, function(datos) {
			$j(datos).find('respuesta').each(function() {
				var respuesta  = $j(this).find('mensaje').text();
				if ( respuesta == "OK" ) {
					actualizaLista();
					llenarCarroCompras();
				}
			});
		});
	}
}
//Por compatibilidad
function limpiaContadorCarro() {
}

function del( campo, incremento, maximo, id, idProd ) {
	limpiaContadorCarro(); 
	var valor = 0;
	if (campo.value == "" || campo.value == "undefined"){
		campo.value = 0;
	}
	valor = parseFloat(campo.value) - parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	if( valor < 0 ) {
		campo.value = 0;
	} else {
		campo.value = valor;
	}
	mc(id,campo,maximo,idProd);
	try{mxTracker._trackEvent('Carro','Modificar carro','RestarProducto');}catch(e){};
}
function cambia_cantidad(nombre, incremento, maximo, id, idProd) {
	if (valida_cantidad(nombre,incremento,maximo)) {
		mc(id,document.getElementById(nombre),maximo,idProd);
	}
}
// ---- FIN EDIT CARRO ---
function ficha(idprod, idform){
	  upWeb();
	  showLightBoxCategories();
	$j('#ventanaficha').show();
	  $j('#logo_jumbo').hide();
	  $j('#contenedor_lightbox').show();
	  $j('#comunas_sesion').hide();
	  $j('#change_comuna_layer_msj').hide();
	  $j('#change_login_session').hide();
	  $j('#olvido_clave').hide();
	  $j('#registra_cliente').hide();
	  $j('#cont_inputs_close').hide();
	  $j('#opcionesRegistro').hide();
	  $j('#error_rut_existe').hide();
	  $j('#agrega_direccion').hide();
	  $j('#mensaje_dir_exito').hide();
	  $j('#producto_no_disponible').hide();
	  $j('#clienteSinRegistro').hide();
	  $j('#contenedor_lightbox').css({'width':'500px','padding-top':'0px','padding-right':'0px','padding-bottom':'20px','padding-left':'0px'});
	top.ficha_carro = true;	
    
	var cabecera_nombre = document.getElementById("cab_nom").value;
	var categoria_nombre = document.getElementById("cat_nom").value;
	var subcategoria_nombre = document.getElementById("subcat_nom").value;
	
	top.frames['frm_ficha'].location.href="ProductDisplay?idprod="+idprod+"&cabecera_nombre="+cabecera_nombre+"&categoria_nombre="+categoria_nombre+"&subcategoria_nombre="+subcategoria_nombre;
	//top.frames['frm_ficha'].location.href="ProductDisplay?idprod="+idprod;
}

// ---- INI ACTUALIZACION DEL CARRO ----
function llenarCarroCompras() {
  $j('#carro_de_compras').html('');
  var params = {};
  var requestOptions = {
    'method': 'post',
    'parameters': params,
    'onSuccess': function ( REQUEST ) {
      $j('#carro_de_compras').html( REQUEST.responseText );
      set_total_display( $j('#total_desc_carro_compras_tc').val() ,$j('#total_desc_carro_compras_tc_sf').val(),  $j('#total_desc_carro_compras').val(), $j('#total_desc_carro_compras_sf').val());
    }
  };
  new Ajax.Request('OrderItemDisplay', requestOptions);
}
// ---- FIN ACTUALIZACION DEL CARRO ----


function validar_cantidad(campo, maximo) {
	valcampo = campo.value;
	if ((valcampo != "") && (!isNaN(valcampo))) {
		if ( parseFloat(valcampo) > parseFloat(maximo) ) {
			alert("Sólo se pueden agregar hasta "+ maximo + " productos");
			campo.focus();
			campo.select();
			return false;
		}
	} else {
		alert("Por favor, ingrese una cantidad válida para el producto");
		campo.focus();
		campo.select();	
		return false;
	}	
	return true;
}
function isNumeric(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}

function getElementosByName(tag, name) {
     var elem = document.getElementsByTagName(tag);
     var arr = new Array();
     for(i = 0,iarr = 0; i < elem.length; i++) {
          att = elem[i].getAttribute("name");
          if(att == name) {
               arr[iarr] = elem[i];
               iarr++;
          }
     }
     return arr;
}
// INICIO INDRA 22-10-2012
function obtenerProductosCarro(){
	var	v_inputs = document.forms.namedItem("fc");
	var cantidad = v_inputs.length;

	var cadenaProductos = "";
	for (var x = 0; x < v_inputs.length; x++){
		if (v_inputs[x].type == "hidden"){
			prod = v_inputs[x].id;
			if (!isNaN(parseInt(prod.substr(5,prod.length)))) {
				cadenaProductos = cadenaProductos + prod.substr(5,prod.length) + ",";
				cantidad--;
			}
		}
		if (cantidad == 0) break;
	}
	return cadenaProductos.substr(0, cadenaProductos.length - 1);
}
// 20121106 INDRA
function limpiarDiv(){
	if (document.getElementById("respuestaValidacionCarroCompras")){
		document.getElementById("respuestaValidacionCarroCompras").innerHTML = "";
	}
	if (document.getElementById("respuestaRescateComprasPaso1")){
		document.getElementById("respuestaRescateComprasPaso1").innerHTML = "";
	}
	if (document.getElementById("respuestaRescateComprasPaso1b")){
		document.getElementById("respuestaRescateComprasPaso1b").innerHTML = "";
	}
}
// 20121106 INDRA

function validarListaProductosCarro(ids){
	var validacion;
	var borrarProductos = 'javascript:borrarProductosCarro()';
	var reemplazarProductos = 'javascript:reemplazarProductosCarro()';

	if (ids != null && ids.length > 0){
		var params			= {'ids':ids, 'borrarProductos' : borrarProductos, 'reemplazarProductos' : reemplazarProductos};
		var requestOptions 	= {
			'method': 'post',
			'parameters': params,
			'asynchronous': false,
			'onSuccess': function (REQUEST) {
				var respuesta = REQUEST.responseText;
				// 20121106 INDRA
				limpiarDiv();
				if (document.getElementById("respuestaLightBox")){
					document.getElementById("respuestaLightBox").innerHTML = "";
					document.getElementById("respuestaLightBox").innerHTML = respuesta;
				}
				// 20121106 INDRA
			}
		};
		new Ajax.Request('/FO/ValidaProductosDespublicadosCarro', requestOptions);
	}
}

// 20121113 INDRA
function verPopup(htmlPopup){
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
	//htmlPopup = $j("#idContPopupCarro").html();//document.getElementById("popup_carro").value;
	window.location.href = '/FO/CategoryDisplay';
	windowPopup = window.open('','','width=410, height=410');
	windowPopup.document.write( inicio + htmlPopup + fin);
	windowPopup.focus();
}
// 20121113 INDRA
function reemplazarProductosCarro(){
	var productos = document.getElementById("productosList_carro").value;
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
					// 20121113 INDRA
					var htmlPopup = document.getElementById("idContPopupCarro").innerHTML;
					verPopup(htmlPopup);
					// 20121113 INDRA
				} else {
					alert("Error en la Eliminación de Productos desde el Carro");
				}
			}
		};
		new Ajax.Request('/FO/EliminaProductosCarro', requestOptions);
	}
}

function borrarProductosCarro(){
	var productos = document.getElementById("productosList_carro").value;
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
					window.location.href="/FO/MiCarro";
				} else {
					alert("Error en la Eliminación de Productos desde el Carro");
				}
			}
		};
		new Ajax.Request('/FO/EliminaProductosCarro', requestOptions);
	}
}

function validarProductosCarro(){
	var listaProductos = obtenerProductosCarro();
	if (listaProductos && listaProductos.length > 0){
		validarListaProductosCarro(listaProductos);

		if (document.getElementById("existen_despublicados_carro") && document.getElementById("existen_despublicados_carro").value != '' && eval(document.getElementById("existen_despublicados_carro").value)> 0){
			
			muestraLBProdDesp();
		} else {
			window.location.href="/FO/MiCarro";
		}		
	} else {
		alert("Carro de compras Vacio");
		try{mxTracker._trackEvent('Carro','Alertas','Carro vacio');}catch(e){};
	}
}
function closeLBoxWin() {
  tb_remove();  
}

function muestraLBProdDesp(){
  showLightBoxProdDesp();
// 20121113 INDRA
//  greenBack();
	//$j("#myTable1").tablesorter({widgets: ['zebra'], headers: { 0: { sorter: false }}});
	
//  $j(document).click(function(e){
//	  id = $j(this).val();
//	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
//		  closeLBoxWin();
//  });
// 20121113 INDRA
}

//(Catalogo Externo) Nelson Sepulveda 07/08/2014----------------------------------------------------------
function verPopupCatalogo(htmlPopup){
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
	
	window.location.href = '/FO/CategoryDisplays';
	windowPopup = window.open('','','width=410, height=410');
	windowPopup.document.write( inicio + htmlPopup + fin);
	windowPopup.focus();
}

function reemplazarProductosCatalogoCarro(){
	var htmlPopup = document.getElementById("idContPopupCarroCatalogo").innerHTML;
	verPopupCatalogo(htmlPopup);
}
//------------------------------------------------------------------------------------------------------------------

function showLightBoxProdDesp() {
//  tb_show('Jumbo.cl','#TB_inline?height=316&width=523&inlineId=hiddenModalContent&modal=true'); 
  tb_show('Jumbo.cl','#TB_inline?height=500&width=700&inlineId=hiddenModalProdDesp&modal=true');   
}
// FIN INDRA 22-10-2012

//dinamic scrollbar position
function pos(obj){
	//var node = document.fc;
	var FieldID = "";
	var position = 0;
	if(obj != null){
	  FieldID = "Inner_"+obj.value;
	  position = findPos(top.document.getElementById(FieldID));
	//alert("FieldID."+FieldID +".position."+position );
	if(position > 500){
		position = position - 500;
	}else {
		if(position - 400 > 0){
			position = position - 400;
		}else {
			position = position - 300;
			}
	}
	$j('div#carro_de_compras').scrollTop(position);
   }
}

//Finds y value of given object
function findPos(obj) {
    var curtop = 0;
    if(obj != null){
	    if (obj.offsetParent) {
	        do {
	            curtop += obj.offsetTop;
	        } while (obj = obj.offsetParent);
	        //alert("curtop: {" +curtop+"}");
	        return [curtop];
	    }
   }
}
