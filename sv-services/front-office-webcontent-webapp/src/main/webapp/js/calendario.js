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

function showCalendario() {
	MM_showHideLayers('ventana','','show');
	var layerCalendario = document.getElementById('ventana');
	var ancho = document.documentElement.scrollWidth; // screen.availWidth;
	var largo = document.documentElement.scrollHeight; // screen.availHeight;
	layerCalendario.style.width = '536px';
	layerCalendario.style.height = '305px';
	layerCalendario.style.left = (ancho-665)/2 + 'px';
	layerCalendario.style.top = '50px'; // (largo)/2 - 305 + 'px';
	var requestOptions = {
			'method': 'post',
			'parameters': 'paso=2',
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
	var layerFichaProducto = document.getElementById('ventanaficha');
	layerFichaProducto.style.visibility = 'visible';
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
	layerFichaProducto.style.width = '450px';
	layerFichaProducto.style.height = 'auto';
	layerFichaProducto.style.left = (ancho-450)/2 + 'px';
	layerFichaProducto.style.top = (largo-400)/2 + ypos - 50 + 'px';
}

function showVentanaFichaProducto() {
	var layerFichaProducto = document.getElementById('ventanaficha');
	layerFichaProducto.style.visibility = 'visible';
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
	layerFichaProducto.style.width = '500px';
	layerFichaProducto.style.height = 'auto';
	layerFichaProducto.style.left = (ancho-450)/2 + 'px';
	layerFichaProducto.style.top = (largo-400)/2 + ypos - 50 + 'px';
}

function hideFichaProducto() {
	$j('#contenedor_lightbox').css({'width':'416px','padding-top':'8px','padding-right':'8px','padding-bottom':'8px','padding-left':'8px'});
	$j('#contenedor_lightbox').hide();
	tb_remove();
	MM_showHideLayers('ventanaficha','','hide');
	var layerFichaProducto = document.getElementById('ventanaficha');
	layerFichaProducto.style.width = '0px';
	layerFichaProducto.style.height = '0px';
}

function showComentario() {
	var layerComentario = document.getElementById('comentarioproducto');
	layerComentario.style.visibility = 'visible';
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
	layerComentario.style.width = '395px';
	layerComentario.style.height = '240px';
	layerComentario.style.left = (ancho-395)/2 + 'px';
	layerComentario.style.top = (largo-400)/2 + ypos - 50 + 'px';
}

function hideComentario() {
	MM_showHideLayers('comentarioproducto','','hide');
	var layerComentario = document.getElementById('comentarioproducto');
	layerComentario.style.width = '0px';
	layerComentario.style.height = '0px';
}

function showModificarDireccion() {
	MM_showHideLayers('modificardireccion','','show');
	var layerModificarDireccion = document.getElementById('modificardireccion');
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.body.scrollTop;
	layerModificarDireccion.style.width = '283px';
	layerModificarDireccion.style.height = '170px';
	layerModificarDireccion.style.left = (ancho-283)/2;
	layerModificarDireccion.style.top = (largo-400)/2 + ypos - 50;
}

function hideModificarDireccion() {
	MM_showHideLayers('modificardireccion','','hide');
	var layerModificarDireccion = document.getElementById('modificardireccion');
	layerModificarDireccion.style.width = '0px';
	layerModificarDireccion.style.height = '0px';
}
/*
function verificalista(){
	var frame = window.document.getElementById("ifrm_carro");
	var isNav = (navigator.appName.indexOf("Netscape") != -1)
	var isIE = (navigator.appName.indexOf("Microsoft") != -1)
	if (isNav) {	
		if (frame.contentDocument.getElementById("cant_reg").value != 0)
			showGuardaLista();
		else
			alert("No existen productos en el carro");
	} else if (isIE) {
		if (document.frames['ifrm_carro'].fc.cant_reg.value != 0)
			showGuardaLista();
		else 
			alert("No existen productos en el carro");
	}
}
*/
var flaglista = false;

function showGuardaLista() {
	var frame = document.getElementById('framelista');
	var layerLista = document.getElementById('guardalista');
	if (flaglista)
		frame.src = '/FO_WebContent/layers/guardalista.html'
	layerLista.style.visibility = 'visible';
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.body.scrollTop;
	layerLista.style.width = '283px';
	layerLista.style.height = '170px';
	layerLista.style.left = (ancho-283)/2 + 'px';
	layerLista.style.top = (largo-400)/2 + ypos + 'px';
	layerLista.style.zIndex = 10;
	flaglista = true;
}

function hideGuardaLista() {
	MM_showHideLayers('guardalista','','hide');
	var layerLista = document.getElementById('guardalista');
	layerLista.style.width = '0px';
	layerLista.style.height = '0px';
	layerLista.style.zIndex = -1;
}

function hideGuardaLista2() {
	MM_showHideLayers('guardalista','','hide');
	var layerLista = document.getElementById('guardalista');
	layerLista.style.width = '0px';
	layerLista.style.height = '0px';
	layerLista.style.zIndex = -1;
}

var objectSelected = null;
var defaultStyle = null;
var defaultStyleSelected = null;

function cambiarEstilo(element, newStyle) {
	if (objectSelected != null) {
		if (element != objectSelected)
			element.className = newStyle;
	} else 
		element.className = newStyle;
}

function cambiarEstiloCal(element, newStyle) {
	if( document.t_picking.jsel.value == 1 ) {
		if (objectSelected != null) {
			if (element != objectSelected)
				element.className = newStyle;
		} else 
			element.className = newStyle;
		element.style.cursor="hand";
	}
}

function seleccionarElemento(element, newStyle, nextDefaultStyle) {
	if (objectSelected != null) {
		objectSelected.className = defaultStyleSelected;
	}
	element.className = newStyle;
	objectSelected = element;
	defaultStyleSelected = nextDefaultStyle;
}
function setEstiloNormal(normalStyle) {
	defaultStyle = normalStyle;
}


function seleccionarElementoCal(element, newStyle) {
	if (objectSelected != null) {
		objectSelected.className = defaultStyle;
	}
    if( document.t_picking.jsel.value == 1 ) {
        element.className = newStyle;
        objectSelected = element;
    }
}

function send_despacho( desp, precio, pick, fecha ) {
	var precio_aux = precio.replace("$","").replace(".","");
    document.t_picking.jpicking.value = pick ;
    document.t_picking.jprecio.value = precio_aux;
    document.t_picking.jdespacho.value = desp;
    document.t_picking.jfecha.value = fecha;
}

function mueveCalendario(sel,pa,zona_id,cant_prod) {
    var requestOptions = {
			'method': 'post',
			'parameters': "sel=" + sel + "&pa=" + pa + "&zona_id=" + zona_id + "&cant_prod=" + cant_prod + "&paso=2",
			'onSuccess': creaCalendario
	};
	new Ajax.Request('AjaxDespachoChart', requestOptions);
}

function creaCalendario(REQUEST){
	document.getElementById('jcalendario').innerHTML = REQUEST.responseText;
	tp1 = new WebFXTabPane( document.getElementById( "tabPane1" ) );
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
