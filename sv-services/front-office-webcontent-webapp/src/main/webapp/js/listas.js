function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}
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

function salir(destino) {
	window.location.href = destino;
}

function onLoadListas() {
	var fo = new FlashObject("/FO_IMGS/img/header-sinlink.swf", "animationName", "700", "125", "8", "#FFFFFF");
	fo.addParam("allowScriptAccess", "sameDomain");
	fo.addParam("quality", "high");
	fo.addParam("scale", "noscale");
	fo.write("header");

	llenarListas();
}

/// ---- CARGA DE LISTAS -----
function llenarListas() {
	var params = {'ig':$('id_grupo').value};
	var requestOptions = {
				'method': 'post',
				'parameters': params,
				'onSuccess': function (REQUEST) {
					$('el_listado_de_compras').innerHTML = REQUEST.responseText;
					setTimeout(function() {
						actualizaListaProductos()
					},10);
				}
			};
	new Ajax.Request('ListasDisplayForm', requestOptions);
}
/// ---- CARGA DE PRODUCTOS ----
function actualizaListaProductos() {
	var tupla			= obtieneTupla();
	var seleccionados 	= obtieneSeleccionados();
	var todos			= "";
	var primera			= null;
	if ($('todos')) {
		todos = $('todos').checked;
	}
	/* Esto para que deje todo seleccionado por defecto*/
	if ( $('primera_carga') ) {
		primera = $('primera_carga').value;
	}
	
	// Y esto es para que no deje todo seleccionado.
	//primera = 'N';
	var params			= {'tupla':tupla,'seleccionados':seleccionados,'todos':todos,'primera_carga':primera};
	var requestOptions 	= {
				'method': 'post',
				'parameters': params,
				'onSuccess': function (REQUEST) {
					$('el_listado_de_productos').innerHTML = REQUEST.responseText;
					cargaAcordeonProductos()
					if ( $('check_all_prods').value == 'checked' ) {
						$('todos').checked = true;
					} else {
						$('todos').checked = false;
					}				
				}
			};
	new Ajax.Request('ProListasEspeciales', requestOptions);
}
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
	var cantreg 		= 0;
	if (document.form1.cantreg) {
		cantreg = document.form1.cantreg.value;
	}
	var tupla   		= "";
	var coma    		= '';
	for (var i=0; i < cantreg; i++) {
		if (document.form1.elements["idult_"+i].checked == true) {
			tupla = tupla  +  coma + document.form1.elements["idult_"+i].value;
			coma = ',';
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
function muestraProductos(check) {
	if (check.checked) {
		document.location.href = "#producto_listado";
	}
	actualizaListaProductos();
}
function cargaAcordeonProductos() {
	acordeonProductos = new accordion('acor_lista_de_productos');
	activaTodos('acor_lista_de_productos','accordion_toggle','accordion_toggle_active');
}
function agregar_carro() {
	var msg = "Se agregarán los productos a tu carro de compras.\nRecuerda haber seleccionado todas las Listas que quieres comprar.\n¿Deseas continuar?";
	if (!confirm(msg)) {
		return;
	}
	var fp = document.getElementById('fp');
	var selecciono = false;
	for ( var i=0; i <= fp.elements.length-1; i++ ) {
		if (fp.elements[i].type == 'checkbox') {
			if (fp.elements[i].checked) {
				if (fp.elements[i].name != 'todos') {
					selecciono = true;
				}					
			}
		} 		
	}
	if (!selecciono) {
		alert("Debes seleccionar productos para agregar al carro de compras.");
		return;
	}
	var pars = Form.serialize($('fp'));	
	var requestOptions 	= {
		'method': 'post',
		'parameters': pars,
		'onSuccess': function (REQUEST) {
			if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
				alert("Error al guardar los productos.");
				return;
			}
			var message = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
			if (message == 'OK') {
				$('msg_respuesta').innerHTML = "Los productos han sido agregados al carro";
				$('msg').show();
				actualizaListaProductos();
				setTimeout(function () {
					$('msg').hide();
					$('msg_respuesta').innerHTML = "";
				},2000);
			} else {
				alert(message);
			}
		}
	};
	new Ajax.Request('AddProductosCarroPaso1', requestOptions);
}

function ficha_listas(idprod, idform) {
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

function showFichaProducto() {
	MM_showHideLayers('ventana_ficha','','show');
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
	$('ventana_ficha').style.width 	= '450px';
	$('ventana_ficha').style.height = 'auto';
	$('ventana_ficha').style.left 	= (ancho-450)/2 + 'px';
	$('ventana_ficha').style.top 	= (largo-400)/2 + ypos - 50 + 'px';
}
function hideFichaProducto() {
	MM_showHideLayers('ventana_ficha','','hide');
	$('ventana_ficha').style.width = '0px';
	$('ventana_ficha').style.height = '0px';
}
function showComentario() {
	var ypos = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
	$('comentarioproducto').style.width = '395px';
	$('comentarioproducto').style.height = '240px';
	$('comentarioproducto').style.left = (screen.availWidth-395)/2 + 'px';
	$('comentarioproducto').style.top = (screen.availHeight-300)/2 + ypos - 50 + 'px';
	MM_showHideLayers('comentarioproducto','','show');
}
function hideComentario() {
	MM_showHideLayers('comentarioproducto','','hide');
	$('comentarioproducto').style.width = '0px';
	$('comentarioproducto').style.height = '0px';
}
function modProducto(fila) {
	if ( $('carr_id'+fila).value == 0 ) {
		return;
	}
	var nota = "";
	if ($('nota'+fila)) {
		nota = $('nota'+fila).value;
	}	
	var params = {'carr_id':$('carr_id'+fila).value, 'cantidad':$('cantidad_l'+fila).value, 'nota':nota};
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function (REQUEST) {
			if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
				alert("Error al guardar el producto.");
				return;
			}
			var message = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
			if (message == 'OK') {
				$('msg_respuesta').innerHTML = "El producto ha sido modificado";
				$('msg').show();
				actualizaListaProductos();
				setTimeout(function () {
					$('msg').hide();
					$('msg_respuesta').innerHTML = "";
				},2000);
			} else {
				alert(message);
			}
		}
	};
	new Ajax.Request('ModProductoCarro', requestOptions);	
}