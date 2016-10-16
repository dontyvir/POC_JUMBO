var $j = jQuery.noConflict();
var disabotonagracarro = 0;
var ficha_paso = '1'; //indica que es el paso1
var ID="";
var FECHA="";
var strTitulo='Mis Compras en Jumbo.cl';
var strImage="/FO_IMGS/img/no_compras_jumbo-cl.jpg";

jQuery().ready(function() { 
	$j('#menuvertical2').accordion();
	if($j('#opcion').val()=='1'){//opcion.value=='1'
		$j('#opcMisListas').removeClass('selected');
		$j('#opcMiCuenta').addClass('selected');
		$j('#menuvertical2 li .accordion_toggle').find('#mListas1').click();
		llenarDatosPersonales(-1);
	}else {
		$j('#opcMiCuenta').removeClass('selected');
		$j('#opcMisListas').addClass('selected');
		$j('#menuvertical2 li .accordion_toggle').find('#mListas2').click();
		llenarListadosCompras(-1);
		
		$j('#texto_titulo').html('Mis Compras en Jumbo.cl');
	}
	

	
	$j('#menuvertical2 li .accordion_toggle').click(function(e) {
		id = $j(this).children('.cabecera').attr("id");
		
		/*+ 20121206 Modificar diseño página "Mis Listas FPenalozaP*/
		document.getElementById("listas").style.border="none";
		document.getElementById("cuenta").style.border="none";
		/*- 20121206 Modificar diseño página "Mis Listas FPenalozaP*/
		
		if(id =='mListas1'){
			$j('#opcMisListas').removeClass('selected');
			$j('#opcMiCuenta').addClass('selected');
			llenarDatosPersonales(-1);
			
			/*+ 20121206 Modificar diseño página "Mis Listas FPenalozaP*/			
			document.getElementById("listas").style.borderTop="#ccc solid 1px";
			/*- 20121206 Modificar diseño página "Mis Listas FPenalozaP*/
					
		}else if(id =='mListas2'){
			$j('#opcMiCuenta').removeClass('selected');
			$j('#opcMisListas').addClass('selected');
			llenarListadosCompras(-1);
			
			/*+ 20121206 Modificar diseño página "Mis Listas FPenalozaP*/			
			document.getElementById("cuenta").style.borderBottom="#ccc solid 1px";
			document.getElementById("cuenta").style.borderRight="#ccc solid 1px";
			/*- 20121206 Modificar diseño página "Mis Listas FPenalozaP*/

//			llenarCarroCompras();
		}
	});
});

function actualizaLista() { 
	if(ID!="" && FECHA!="")
		muestraProductos2(ID,FECHA); 
}
// --- INI FUNCIONES CALENDARIO NO USADAS EN ESTE PASO ---
function cambiarEstiloCal(element, newStyle) {}
function send_despacho(desp, precio, pick, fecha) {}
function seleccionarElementoCal(element, newStyle) {}
// --- FIN FUNCIONES CALENDARIO NO USADAS EN ESTE PASO ---

// ---- INI FUNCIONES DRW ----
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; 
  for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) 
  	x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; 
  if(d.images) { 
  	if(!d.MM_p) 
		d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; 
	for(i=0; i<a.length; i++)
    	if (a[i].indexOf("#")!=0) { 
			d.MM_p[j]=new Image; 
			d.MM_p[j++].src=a[i];
    	}
   }
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  
  if(!d) 
  	d=document; 
  if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) 
  	x=d.all[n]; 
  for (i=0;!x&&i<d.forms.length;i++) 
  	x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) 
  	x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) 
  	x=d.getElementById(n); 
  return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; 
  for(i=0;i<(a.length-2);i+=3)
   	if ((x=MM_findObj(a[i]))!=null) {
		document.MM_sr[j++]=x; 
		if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];
   }
}

function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) 
  	if ((obj=MM_findObj(args[i]))!=null) { 
		v=args[i+2];
    	if (obj.style) { 
			obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; 
		}
		obj.visibility=v; 
	}
}
// ---- FIN FUNCIONES DRW ----

// ---- INI VENTANAS MOVILES ----
function showCalendario() {
	var layerCalendario = document.getElementById('ventana');
	layerCalendario.style.visibility = 'visible';
	var ancho = document.documentElement.scrollWidth; // screen.availWidth;
	var largo = document.documentElement.scrollHeight; // screen.availHeight;
	layerCalendario.style.width = '536px';
	layerCalendario.style.height = '305px';
	layerCalendario.style.left = (ancho-665)/2 + 'px';
	layerCalendario.style.top = '50px'; // (largo)/2 - 305 + 'px';
	var requestOptions = {
				'method': 'post',
				'parameters': 'paso=1',
				'onSuccess': creaCalendario
			};
	new Ajax.Request('AjaxDespachoChart', requestOptions);
}
function hideCalendario() {
	MM_showHideLayers('ventana','','hide');
	var layerCalendario = document.getElementById('ventana');
	layerCalendario.style.width = '0px';
	layerCalendario.style.height = '0px';
	MM_showHideLayers('ventana_explicacion','','hide');
}
function showFichaProducto() {
	MM_showHideLayers('ventana_ficha','','show');
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
	$j('#ventana_ficha').style.width 	= '450px';
	$j('#ventana_ficha').style.height = 'auto';
	$j('#ventana_ficha').style.left 	= (ancho-450)/2 + 'px';
	$j('#ventana_ficha').style.top 	= (largo-400)/2 + ypos - 50 + 'px';
}
function hideFichaProducto() {
	MM_showHideLayers('ventana_ficha','','hide');
	$j('#ventana_ficha').style.width = '0px';
	$j('#ventana_ficha').style.height = '0px';
}
function showPromoProducto() {
	MM_showHideLayers('ventana_promo','','show');
	var layerPromoProducto = document.getElementById('ventana_promo');
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.body.scrollTop;
	layerPromoProducto.style.width = '395px';
	layerPromoProducto.style.height = '240px';
	layerPromoProducto.style.left = 100;
	layerPromoProducto.style.top = (largo-450)/2 + ypos;
}
function hidePromoProducto() {
	MM_showHideLayers('ventana_promo','','hide');
	var layerPromoProducto = document.getElementById('ventana_promo');
	layerPromoProducto.style.width = '0px';
	layerPromoProducto.style.height = '0px';
}
function showComentario() {
	var ypos = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
	$j('#comentarioproducto').style.width = '395px';
	$j('#comentarioproducto').style.height = '240px';
	$j('#comentarioproducto').style.left = (screen.availWidth-395)/2 + 'px';
	$j('#comentarioproducto').style.top = (screen.availHeight-300)/2 + ypos - 50 + 'px';
	$j('#comentarioproducto').style.visibility = 'visible';
}
function hideComentario() {
	MM_showHideLayers('comentarioproducto','','hide');
	$j('#comentarioproducto').style.width = '0px';
	$j('#comentarioproducto').style.height = '0px';
}
function showCarroVacio() {
	MM_showHideLayers('carrovacio','','show');
	var layerCarroVacio = document.getElementById('carrovacio');
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.body.scrollTop;
	layerCarroVacio.style.width = '283px';
	layerCarroVacio.style.height = '170px';
	layerCarroVacio.style.left = (ancho-283)/2 + 'px';
	layerCarroVacio.style.top = (largo-400)/2 + ypos + 'px';
}
function hideCarroVacio() {
	MM_showHideLayers('carrovacio','','hide');
	var layerCarroVacio = document.getElementById('carrovacio');
	layerCarroVacio.style.width = '0px';
	layerCarroVacio.style.height = '0px';
}
function showSinCompras() {
	MM_showHideLayers('sincompras','','show');
	var layerSinCompras = document.getElementById('sincompras');
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.body.scrollTop;
	layerSinCompras.style.width = '283px';
	layerSinCompras.style.height = '206px';
	layerSinCompras.style.left = (ancho-283)/2 + 'px';
	layerSinCompras.style.top = (largo-400)/2 + ypos + 'px';
}
function hideSinCompras() {
	MM_showHideLayers('sincompras','','hide');
	var layerSinCompras = document.getElementById('sincompras');
	layerSinCompras.style.width = '0px';
	layerSinCompras.style.height = '0px';
}
function showModificarDireccion() {
	MM_showHideLayers('modificardireccion','','show');
	var layerModificarDireccion = document.getElementById('modificardireccion');
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.body.scrollTop;
	layerModificarDireccion.style.width = '283px';
	layerModificarDireccion.style.height = '170px';
	layerModificarDireccion.style.left = (ancho-283)/2 + 'px';
	layerModificarDireccion.style.top = (largo-400)/2 + ypos + 'px';
}
function hideModificarDireccion() {
	MM_showHideLayers('modificardireccion','','hide');
	var layerModificarDireccion = document.getElementById('modificardireccion');
	layerModificarDireccion.style.width = '0px';
	layerModificarDireccion.style.height = '0px';
}
function showEvento() {
	MM_showHideLayers('ventana_evento','','show');
	var layerEvento = document.getElementById('ventana_evento');
	var largo = screen.availHeight;
	var ancho = document.documentElement.scrollWidth;
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
// ---- FIN VENTANAS MOVILES ----

// ---- INI LISTA DE COMPRAS ----
function verificalista(){
	if ($j('#cant_reg').value != 0) {
		showGuardaLista();
	} else {
		alert("No existen productos en el carro");
	}
}
var flaglista = false;

function showGuardaLista() {
	var frame = document.getElementById('framelista');
	if (flaglista)
		frame.src = '/FO_WebContent/layers/guardalista.html'
    $j('#guardalista').css('visibility',"visible");
    $j('#guardalista').css('width','283px');
    $j('#guardalista').css('height','150px');
    $j('#guardalista').css('left',(screen.availWidth-283*2)/2 + 'px');
    $j('#guardalista').css('top',(screen.availHeight-400)/2 + document.body.scrollTop + 'px');
    $j('#guardalista').css('zIndex','10');
	flaglista = true;
}
function hideGuardaLista() { //	TODO: SE USA?????????
	MM_showHideLayers('guardalista','','hide');
	var layerLista = document.getElementById('guardalista');
	layerLista.style.width = '0px';
	layerLista.style.height = '0px';
	layerLista.style.zIndex = -1;
}
function hideGuardaLista2() {
	llenarListadosCompras(2);
	MM_showHideLayers('guardalista','','hide');
	$j('#guardalista').style.width = '0px';
	$j('#guardalista').style.height = '0px';
	$j('#guardalista').style.zIndex = -1;
}

// ---- FIN LISTA DE COMPRAS ----

if(navigator.appName == "Microsoft Internet Explorer") { 
function printURL(sHref) { 
  if(document.getElementById && document.all && sHref){ 
    if(!self.oPrintElm){ 
      var aHeads = document.getElementsByTagName('HEAD'); 
      if(!aHeads || !aHeads.length) 
        return false; 
      if(!self.oPrintElm) 
        self.oPrintElm = document.createElement('LINK'); 
      self.oPrintElm.rel = 'alternate'; 
      self.oPrintElm.media = 'print'; 
      aHeads[0].appendChild(self.oPrintElm); 
      } 
      self.oPrintElm.href = sHref; 
      self.focus(); 
      self.print(); 
      return true; 
    } 
  else 
	return false; 
  } 
} 

function imprimir() {
	var tupla			= ID; //obtieneTupla();
	var seleccionados 	= ID;//obtieneSeleccionados();
	
	if (navigator.appName == "Microsoft Internet Explorer") {

		var url = "UltComprasProList?tupla=" + tupla + "&seleccionados=" + seleccionados;

		printURL(url);
	} else {
		top.frames['frm_print_prod'].location.href="UltComprasProList?tupla=" + tupla + "&seleccionados=" + seleccionados;
		setTimeout(function() {
			top.frames['frm_print_prod'].print();
		},2500);
	}	
}

function exportar() {
	var obj = document.exportar_excel;
	obj.tupla.value = ID;//obtieneTupla();	
	obj.submit();
}
function getListasOrigen() {
  //1:Ultimas compras Jumbo O Mis listas guardadas
  //2:Compras en local
  //4:Listas Sugeridas
  var r1 = "";var r2 = "";var r4 = "";
      if ( (ID.split("-"))[1] == 'I' ) {
        r1 = "1";
      } else if ( (ID.split("-"))[1] == 'L' ) {
        r2 = "2";      
      } else if ( (ID.split("-"))[1] == 'P' ) {
        r4 = "4";      
      }
//  alert(r1 + r2 + r4);
  return r1 + r2 + r4;
}
// INICIO INDRA 22-10-2012
function validarProductos(ids){
	
	if (ids != null && ids.length > 0){
		var params = {'ids':ids};
			
		$j.ajax({
			type: 'POST',
			url: 'ValidaProductosDespublicadosListas',
			data: params,
			asynchronous: false,
			success: function(data) {
			  
				if ( data != null && data != "0") {

					//$j('#el_listado_de_compras').html("");
					//$j('#el_listado_de_compras').html(data);
					llenarCarroCompras();
					document.getElementById("respuestaRescateComprasPaso1").innerHTML = respuesta;
					if (document.getElementById("existen_despublicados_carro") && document.getElementById("existen_despublicados_carro").value != '' && eval(document.getElementById("existen_despublicados_carro").value)> 0){
						muestraLightboxListas();
					}
					
					//var existen_despublicados = eval(document.getElementById("existen_despublicados_carro").value);
					//if ( existen_despublicados > 0) {
					//	muestraLightboxListas();
					//}
				}
			}
		});
		
	} 
}

function validarProductosPorIdLista(idLista) {
	var borrarProductos = 'javascript:borrarProductosLista()';
	var reemplazarProductos = 'javascript:reemplazarProductosLista()';
	
	if (idLista != null && idLista != ""	){
		var params			= {'idLista':idLista, 'borrarProductos' : borrarProductos, 'reemplazarProductos' : reemplazarProductos};
		var requestOptions 	= {
			'method': 'post',
			'parameters': params,
			'asynchronous': false,
			'onSuccess': function (REQUEST) {
				var respuesta = REQUEST.responseText;
				limpiarDiv();
				
				document.getElementById("respuestaLightBox").innerHTML = respuesta;
				llenarCarroCompras();
				
				if (document.getElementById("existen_despublicados_carro") && document.getElementById("existen_despublicados_carro").value != '' && eval(document.getElementById("existen_despublicados_carro").value)> 0){
					muestraLightboxListas();
				}
				
				//var existen_despublicados = eval(document.getElementById("existen_despublicados_carro").value);
				//if ( existen_despublicados > 0) {
					
				//}
			}
		};
		new Ajax.Request('/FO/ValidaProductosDespublicadosListas', requestOptions);
		return true;
	}
}

function obtenerProductosLista() {
    
    var cadenaProductos = ""; 
    var id = 0;
    var cantImg = document.getElementsByClassName("modulo_foto_130_h").length;
    //var inputs = document.getElementById('fp').getInputs();
    
    for (var x = 0; x < cantImg; x++){
        var imgName = document.getElementsByClassName("modulo_foto_130_h")[x].children[0].id; // IE: vacio
        cadenaProductos += imgName.substr(4, imgName.lenght) + ",";
    }
    return cadenaProductos.substring(0, cadenaProductos.length-1);
}
// FIN INDRA 22-10-2012

function agregar_carro() {
	/*Estado de los productos de la lista*/
	var estadoProductos = false;
	var fp = document.getElementById('fp');
	var selecciono = false;
	for ( var i=0; i <= fp.elements.length-1; i++ ) {
		if (fp.elements[i].type == 'checkbox') {
			if (fp.elements[i].checked) {
				selecciono = true				
				break;	
			}
		} 		
	}
	if (!selecciono) {
		alert("Debes seleccionar productos para agregar al carro de compras.");
		return;
	}
	var pars = $j('#fp').serialize();
//	validarProductos(obtenerProductosLista());
	$j.ajax({
		type: 'POST',
		url: 'AddProductosCarroPaso1',
		data: pars,
		success: function(data) {			
			if ( $j(data).find("respuesta").text() != null) {
				var resp = $j(data).find("respuesta").text();
				if ( resp == "OK") {
					pageTracker._trackEvent("AgregarLista", "" + getListasOrigen()); 
					muestraProductos2(ID,FECHA); // -> actualizaListaProductos2(ID,FECHA) : carga lista
					validarProductosPorIdLista(ID);					
				}
			}
		}
	});
	window.setTimeout("actualizaCarro1()", 1000);
}

	function actualizaCarro1() {
		  $j.get("/FO/OrderItemDisplay", function(datos) {
			    $j('div#carro_de_compras').html(datos);
			    descuento = $j('input#total_desc_carro_compras_sf').val();
			    $j('input#_total').val($j('input#total_desc_carro_compras_tc').val());
			    $j('input#_total_desc').val($j('input#total_desc_carro_compras_tc').val());

			    $j('div#_total_ahorro').text($j('input#total_desc_carro_compras_tc_sf').val());//total_carro_compras - promo_desc_carro_compras
			    $j('div#_total_tarjeta_mas').text($j('input#total_desc_carro_compras').val());
			    $j('div#_total_ahorro_tarjeta_mas').text($j('input#total_desc_carro_compras_sf').val());
			    //por compatibilidad con codigo antiguo que sobrevivio
					$j('input#_total_desc_txt').val($j('input#promo_desc_carro_compras').val()); 
				  $j('div#inputtotdesc').hide();
			  });}
	

function selectall(){
    var elementos = document.fp.elements;    
    for (var i = 0 ; i < elementos.length; ++i){
    	if (document.forms['fp'].elements[i].name.indexOf("id_prod_") != -1) {
	    	if (document.getElementById("todos").checked == true){
		  		document.forms['fp'].elements[i].checked = true;
		    } else {
			    document.forms['fp'].elements[i].checked = false;
			}
		}		    
    }
}

function obtieneTupla() {
	var cantreg 		= document.form1.cantreg.value;
	var tupla   		= "";
	var coma    		= '';
	for (var i=0; i < cantreg; i++) {
		if (document.acceso2.elements["idult_"+i].checked == true) {
			tupla = tupla  +  coma + document.acceso2.elements["idult_"+i].value;
			coma = ',';
		}
	}
	return tupla;
}

function obtieneTupla2(obj) {
	var cantreg 		= document.form1.cantreg.value;
	var tupla   		= "";
	var coma    		= '';
	for (var i=0; i < cantreg; i++) {
		if(typeof(obj.elements["idult_"+i])!='undefined'){
			if (obj.elements["idult_"+i].checked == true) {
				tupla = tupla  +  coma + obj.elements["idult_"+i].value;
				coma = ',';
			}
		}
	}
	return tupla;
}
function obtieneSeleccionados() {
	var seleccionados = "";
	if ( document.fp ) {
		var cant = document.fp.elements.length;
		var aux = 0;
		for (var i = 0 ; i < cant; ++i) {
			if ((document.fp.elements[i].type == "checkbox") && (document.fp.elements[i].name.substring(0,3) == "id_")) {
				if (document.fp.elements[i].checked) {
					if (aux == 0) {
						seleccionados = document.fp.elements[i].value;
						aux++;
					} else {
						seleccionados = seleccionados + ',' + document.fp.elements[i].value;
						aux++;
					}
				}	
			} 
		}
	}
	return seleccionados;
}

/*
* Recorre las compras para enviar los id a la busqueda de productos
*/

function actualizaListaProductos() {
	var tupla			= obtieneTupla();
	var seleccionados 	= obtieneSeleccionados();
	var todos			= "";
	var primera			= null;

	
	if ($j('#todos')) {
		todos = $j('#todos').checked;
	}

	if ( $j('#primera_carga') ) {
		primera = $j('#primera_carga').value;
	}
	var params			= {'tupla':tupla,'seleccionados':seleccionados,'todos':todos,'primera_carga':primera};
	var requestOptions 	= {
				'method': 'post',
				'parameters': params,
				'onSuccess': function (REQUEST) {
					$j('#el_listado_de_productos').html("");
					$j('#el_listado_de_productos').html(REQUEST.responseText);
					cargaAcordeonProductos();
					if ( $j('#check_all_prods').val() == 'checked' ) {
						$j('#todos').checked = true;
					} else {
						$j('#todos').checked = false;
					}				
				}
			};
	new Ajax.Request('UltComprasProList', requestOptions);
}

function actualizaListaProductos3(obj) {
	var tupla			= obtieneTupla2(obj);
	var seleccionados 	= obtieneSeleccionados();
	var todos			= "";
	var primera			= null;
	
	
	
	
	if ($j('#todos')) {
		todos = $j('#todos').checked;
	}

	if ( $j('#primera_carga') ) {
		primera = $j('#primera_carga').value;
	}
	var params			= {'tupla':tupla,'seleccionados':seleccionados,'todos':todos,'primera_carga':primera};
	var requestOptions 	= {
				'method': 'post',
				'parameters': params,
				'onSuccess': function (REQUEST) {
					$j('#el_listado_de_productos').html("");
					$j('#el_listado_de_productos').html(REQUEST.responseText);
					cargaAcordeonProductos();

					$j('#texto_titulo').html(strTitulo);
					if ( $j('#check_all_prods').val() == 'checked' ) {
						$j('#todos').checked = true;
					} else {
						$j('#todos').checked = false;
					}				
				}
			};
	new Ajax.Request('UltComprasProList', requestOptions);
}

// INICIO INDRA 22-10-2012 (MODIFICACION)
function actualizaListaProductos2(id, fecha) {

	var tupla			= id;
	var seleccionados 	= id;
	var todos			= "";
	var primera			= null;
	var mx_content =  null;
	var itemLista = $j("#texto_titulo").html();
	var listaGuardada = $j("#nombre_titulo").html();
	
	if(itemLista=="" || itemLista==null || listaGuardada==null || listaGuardada=="")
		mx_content=strTitulo;
	else
		mx_content=itemLista.concat(" | "+listaGuardada);
	
	if ( $j('#primera_carga') ) {
		primera = $j('#primera_carga').value;
	}
	
	var params			= {'tupla':tupla,'seleccionados':seleccionados,'todos':todos,'primera_carga':primera,'mx_content':mx_content};
	var requestOptions 	= {
				'method': 'post',
				'parameters': params,
				'onSuccess': function (REQUEST) {
				
					$j('#el_listado_de_productos').html("");
					$j('#el_listado_de_productos').html(REQUEST.responseText);
					var v_inputs = document.getElementById("el_listado_de_productos").getElementsByTagName("input")
					var cantidad = 0;
					for (var n = 0  ; n <v_inputs.length ; n++){
						if (v_inputs[n].name == "max_productos"){
							cantidad = eval(v_inputs[n].value);
							break;
						}
					}
					if( id == "" || cantidad == 0) {
						//cambia imagen	

						$j('.cont_herramientas').html("");		
						$j('.cont_check_todos').html("");		
						$j('.cont_check_todos').html("");					
						$j('.menu_abajo_paso1_2011').html("");
						$j('#img_sinlista').attr("src",strImage);

					} else {
						
						$j('#fecha_titulo').html(fecha);
						
						if(typeof(img_sinlista)=='object'){
									$j('.cont_herramientas').html("");		
									$j('.cont_check_todos').html("");		
									$j('.cont_check_todos').html("");					
									$j('.menu_abajo_paso1_2011').html("");
									$j('#img_sinlista').attr("src",strImage);
						}
						
					}
					$j('#texto_titulo').html(strTitulo);
					
					var nombreTitulo = $j("#ficha_nombre_" + id).val();
					if(nombreTitulo.length > 0){
						$j('#nombre_titulo').html($j("#ficha_nombre_" + id).val());
					} else{
						//$j('#nombre_titulo').html(fecha);
					}
						
					
					if ( $j('#check_all_prods').val() == 'checked' ) {
						$j('#todos').checked = true;
					} else {
						$j('#todos').checked = false;
					}				
				}
			};
	new Ajax.Request('UltComprasProList', requestOptions);
}

function borrarProductosLista() {
	tb_remove();
	//closeLBoxWin();
}

function reemplazarProductosLista() {
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
	htmlPopup = document.getElementById("idContPopupCarro").innerHTML;
	var fin = '</body></html>';
	var html = inicio + htmlPopup + fin;
	// 20121114 INDRA	
	windowPopup = window.open('','','width=430, height=430');
	// 20121114 INDRA
	windowPopup.document.write( html );
	windowPopup.focus();

	window.location.href = "CategoryDisplay";
}

// FIN INDRA 22-10-2012

function ficha_paso1(idprod, idform) {
	top.ficha_carro = false;
	if (document.getElementById('nota' + idform)) {
		var elem = document.getElementById('nota' + idform);
		top.nota_txt = elem.value;
	} else {
		top.nota_txt = "";
	}
	top.id_formulario = idform;
	top.ficha_paso = '1';
	top.frames['frm_ficha'].location.href="ProductDisplay?idprod="+idprod;
}

function ficha(idprod, idform){
/*no consulta la ficha
	top.mod_icon = idform;
	updatePosLayerFicha();//	top.updatePosLayerFicha();
	top.frames['frm_ficha'].location.href="ProductDisplay?idprod="+idprod;
*/
}
var cX = 0; var cY = 0;

function UpdateCursorPosition(e){ 
	cX = e.pageX; 
	cY = e.pageY;
}
function UpdateCursorPositionDocAll(e){ 
	cX = event.clientX; 
	cY = event.clientY;
}

if(document.all) { document.onmousemove = UpdateCursorPositionDocAll; }
else { document.onmousemove = UpdateCursorPosition; }

function ir_paso2() {
	// Se revisa si hay productos en el carro de compras y existen compras anteriores
	var cantreg = 0;	
	if (document.fc && document.fc.cant_reg) {
		cantreg = document.fc.cant_reg.value; // Cantidad de productos en el carro de compras
	}
    var cantult = 0;
    if (document.form1 && document.form1.cantreg) {
    	cantult = document.form1.cantreg.value; // Cantidad de ultimas compras
    }
	if (cantreg == 0 && cantult > 0 ) {
        showCarroVacio();
	} else {
		location.href= "CategoryDisplay";	
	}	
}

function alert_no_compras() {
	if( $j('#cantreg').value <= 0 ) {
		showSinCompras();
	}
}

function mueveCalendario(sel,pa,zona_id,cant_prod) {
    var requestOptions = {
			'method': 'post',
			'parameters': "sel=" + sel + "&pa=" + pa + "&zona_id=" + zona_id + "&cant_prod=" + cant_prod + "&paso=1",
			'onSuccess': creaCalendario
	};
	new Ajax.Request('AjaxDespachoChart', requestOptions);
}

function creaCalendario(REQUEST){
	document.getElementById('jcalendario').innerHTML = REQUEST.responseText;
	tp1 = new WebFXTabPane( document.getElementById( "tabPane1" ) );
}

function llenarListadosCompras(sel) {
	var params = {};
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function (REQUEST) {
					$j('#el_listado_de_compras').html("");
					$j('#el_listado_de_productos').html("");
			$j('#el_listado_de_compras').html(REQUEST.responseText);
//alert(REQUEST.responseText);
			alert_no_compras();
			if (sel == -1) {
				sel = $j('#lista_seleccionada').val();
			}
			cargaAcordeonListas(1);
//			actualizaListaProductos();
		}
	};
	new Ajax.Request('/FO/UltComprasDisplayForm', requestOptions);
}

function eliminar_lista(id,nombre,tipo) {
	if (confirm("¿Está seguro que desea eliminar la lista " + nombre + "?")) {
		var requestOptions = {
			'method': 'post',
			'parameters': "id_lista=" + id,
			'onSuccess': function(REQUEST) {
				if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
					alert("Ocurrió un error al eliminar la lista");
					return;
				}	
				var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
				if (mensaje == 'OK') {
					if (tipo == 'I') {
						llenarListadosCompras(0);
					} else {
						llenarListadosCompras(2);
					}
					alert("La lista se ha eliminado exitosamente");
					return;
				} else {
					alert(mensaje);
					return;
				}			
			}
		};
		new Ajax.Request('DeleteList', requestOptions);
	}
}

if ($j("#mostrar_evento").value == 'SI') {
	showEvento();
}

var acordeonListas;
var acordeonProductos;

function cargaAcordeonListas(row) {
/*	acordeonListas = new accordion('acor_lista_de_compras');
	acordeonListas.selecciona($j('#acor_lista_de_compras .accordion_toggle').get(row));	*/
//	$j('#acor_lista_de_compras').accordion({ collapsible: true });
}

function cargaAcordeonProductos() {

//	$j('#acor_lista_de_productos').accordion({ collapsible: true });
//	acordeonProductos = new accordion('acor_lista_de_productos');
//	activaTodos('acor_lista_de_productos','accordion_toggle','accordion_toggle_active');
	/*if ($$j('#acor_lista_de_productos .accordion_toggle') != '') {
		for (var i=0; i < $$j('#acor_lista_de_productos .accordion_toggle').length; i++) {
			acordeonProductos.act($$j('#acor_lista_de_productos .accordion_toggle')[i]);
		}
	}*/
}

function muestraProductos(check) {
	if (check.checked) {
		document.location.href = "#producto_listado";
	}
	actualizaListaProductos();
}

// INICIO INDRA 22-10-2012 (MODIFICACION)
function muestraProductos2(idLista,fecha) {
	/*
	
	
	//ID = idLista;
	*/
	
	if ( idLista == "" && ID != "") {
		idLista = ID;
	} else {
		ID = idLista;
	}
	//FECHA = fecha;
	if ( fecha == "" && FECHA != "") {
		fecha = FECHA;
	} else {
		FECHA = fecha;
	}
//	document.location.href = "#producto_listado";
	actualizaListaProductos2(idLista,fecha);

}
// FIN INDRA 22-10-2012

function llenarDatosPersonales(sel) {
	var params = {};
	var requestOptions = {
				'method': 'post',
				'parameters': params,
				'onSuccess': function (REQUEST) {
					$j('#el_listado_de_compras').html("");
					$j('#el_listado_de_productos').html("");
					$j('#el_listado_de_productos').html(REQUEST.responseText);
				}
			};
	new Ajax.Request('/FO/DatosPersonalesForm', requestOptions);
}
/***************************************************************************************************/

// inicio magno aperez
//primera ventana modal para confirmar el correo
function enviar_correo(numVentana) {
  
  upWeb();
  showJumboConfirmacionMail1(numVentana);
  
  var email = null;
  
  if(numVentana==1){
	
	//obtengo el email del titular	
	ocultarModalesEnvioCorreo();
	recuperarCorreo();
	
  } else if(numVentana==2){
	
	
	ocultarModalesEnvioCorreo();
  	document.getElementById('modalJumboConfirmacionMail2').style.display="";
  	document.getElementById('nombreEmail').value = "";
  	document.getElementById('dominioEmail').value= "";
  	$j('#error_correo_envio').html("");
  	
  } else if(numVentana==3){
  	var nombreCorreo = $j('#nombreEmail').val();
  	var dominioCorreo = $j('#dominioEmail').val();
  	var email2 = $j('#nombreEmail').val()+"@"+$j('#dominioEmail').val();//obtener de la ventana modal 2
  	
  	
  	/*if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/.test(valor)){
   		alert("La dirección de email " + valor + " es correcta.");
   		return true;
  	} else {
   		alert("La dirección de email es incorrecta.");
   		return false;
  	}*/
  	if(validarEmail(email2)){
  		ocultarModalesEnvioCorreo();
	  	document.getElementById('modalJumboConfirmacionMail3').style.display="";
	  	document.getElementById('correo_lista_producto').value = email2;
		$j('#email_lista2').html("<strong>"+email2+"</strong>");	
		$j('#error_correo_envio').html("");
  	} else {
  		var error = "El mail ingresado no es válido. Por favor vuelve a intentarlo.";
  		ocultarModalesEnvioCorreo();
	  	document.getElementById('modalJumboConfirmacionMail2').style.display="";
  		$j('#error_correo_envio').html("<strong>"+error+"</strong>");
  	}
  	/*if( nombreCorreo.length > 0 && dominioCorreo.length > 0 ) {
  		
  		
  		
	  	ocultarModalesEnvioCorreo();
	  	document.getElementById('modalJumboConfirmacionMail3').style.display="";
	  	document.getElementById('correo_lista_producto').value = email2;
		$j('#email_lista2').html("<strong>"+email2+"</strong>");	
		$j('#error_correo_envio').html("");
  	} else {
  		var error = "El mail ingresado no es válido. Por favor vuelve a intentarlo.";
  		ocultarModalesEnvioCorreo();
	  	document.getElementById('modalJumboConfirmacionMail2').style.display="";
  		$j('#error_correo_envio').html("<strong>"+error+"</strong>");
  	}*/
  	
  
  } else if(numVentana==4){
  	//crear metodo asincrono que envie el correo con adjunto
	//ventana modal de exito de envio de correo o de error.
	
  	ocultarModalesEnvioCorreo();
  	enviarCorreo();	
	
  } 
	
  //greenBack();
  //alert("PASO_ACTUAL: " + PASO_ACTUAL);
  //$j('#jumbo_confirmacion_mail').show();
  $j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
		  closeLBoxWin();
  });
}

function validarEmail(valor) {
	//var valor = document.getElementById("nombreEmail").value + "@" + document.getElementById("dominioEmail").value;
	//alert(valor);
  if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/.test(valor)){
   //alert("La dirección de email " + valor + " es correcta.");
   return true;
  } else {
   //alert("La dirección de email es incorrecta.");
   return false;
  }
}

function showJumboConfirmacionMail1(numVentana) {
  tb_show('Jumbo.cl','#TB_inline?height=450&width=450&inlineId=hiddenModalJumboConfirmacionMail'+numVentana+'&modal=true');
}
//--------------

//envio correo
function enviarCorreo() {
	var obj = document.enviar_correo;
	//alert('el valor de id es : '+ID);
	  var correo = $j("#correo_lista_producto").val();
	  //alert(correo);
	  $j.post("/FO/EnviarCorreoComprasProList", 
	  	{	solicitud: "enviarCorreo", 
	  		correoEnvio: correo, 
	  		tupla: ID
	  	}, 
	  	function(datos){
	    	$j(datos).find('comuna').each(function() {
		    	$j('#mensaje_error_exito').html("");
		    	var respuesta  = $j(this).find('respuesta').text();	    	
				var mensaje = null;
				if ( respuesta == "OK" ) {
					
					document.getElementById('modalJumboConfirmacionMail4').style.display="";
					mensaje = "Tu lista ha sido enviada exitosamente";
					
				} else {
					//revisar por que no despliega mensaje
					document.getElementById('modalJumboConfirmacionMail4').style.display="";
					mensaje = "No ha sido posible enviar el correo. Por favor vuelve a intentarlo";
				}
				$j('#mensaje_error_exito').html("<strong>"+mensaje+"</strong>");
	    	});
	  	});
	
	//obj.tupla.value = ID; //obtieneTupla();	
	//obj.submit();	
}


//recupero correo para envio de lista con excel
function recuperarCorreo() {
	var obj = document.enviar_correo;
	
	  $j.post("/FO/EnviarCorreoComprasProList", {solicitud: "email"}, 
	  	function(datos){
	    	$j(datos).find('comuna').each(function() {
		    	var respuesta  = $j(this).find('respuesta').text();	    	

			  $j('#mensaje_error_exito').html("");
		      if ( respuesta == "OK" ) {
		      	var emailTitular  = $j(this).find('emailTitular').text();
		      	document.getElementById('modalJumboConfirmacionMail1').style.display="";
		      	document.getElementById('botones_tr_ventana1').style.display="";
		      	document.getElementById('correo_lista_producto').value = emailTitular;
				$j('#email_lista').html("Tu lista será enviada al correo " + "<strong>" + emailTitular+"</strong>");
		      	
		      } else {
		      	ocultarModalesEnvioCorreo();		      	
				showJumboConfirmacionMail1(4);
				document.getElementById('modalJumboConfirmacionMail4').style.display="";
				//document.getElementById('botones_tr_ventana1').style.display="none";
				$j('#mensaje_error_exito').html("<strong>"+"Se ha producido un error al recuperar correo"+" </strong>");
		      }
		      		      
	    	});
	  	});
	
	//obj.tupla.value = ID;//obtieneTupla();	
	//obj.submit();	
}

function ocultarModalesEnvioCorreo(){
	document.getElementById('modalJumboConfirmacionMail1').style.display="none";
	document.getElementById('modalJumboConfirmacionMail2').style.display="none";
	document.getElementById('modalJumboConfirmacionMail3').style.display="none";
	document.getElementById('modalJumboConfirmacionMail4').style.display="none";
}
// fin magno

//INICIO INDRA
function muestraLightboxListas(){
  showLightBoxListas();
	$j("#myTable1").tablesorter({widgets: ['zebra'], headers: { 0: { sorter: false }}});
	
  $j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
		  closeLBoxWin();
  });
}
function showLightBoxListas() {
//  tb_show('Jumbo.cl','#TB_inline?height=316&width=523&inlineId=hiddenModalContent&modal=true'); 
  tb_show('Jumbo.cl','#TB_inline?height=500&width=700&inlineId=hiddenModalProdDesp&modal=true');   
}

function closeLBoxWin() {
  tb_remove();  
}

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
//FIN INDRA
