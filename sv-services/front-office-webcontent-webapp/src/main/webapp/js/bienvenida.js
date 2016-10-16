function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
  	var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
  	if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];
  	}
  }
}
function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}
function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}
function showCalendario() {
	MM_showHideLayers('ventana','','show');
	var layerCalendario = document.getElementById('ventana');
	var ancho = document.documentElement.scrollWidth; // screen.availWidth;
	var largo = document.documentElement.scrollHeight; // screen.availHeight;
	layerCalendario.style.width = '536px';
	layerCalendario.style.height = '305px';
	layerCalendario.style.left = (ancho-665)/2 + 'px';
	layerCalendario.style.top = '330px'; // (largo)/2 - 305 + 'px';
}

function hideCalendario() {
	MM_showHideLayers('ventana','','hide');
	var layerCalendario = document.getElementById('ventana');
	layerCalendario.style.width = '0px';
	layerCalendario.style.height = '0px';
	MM_showHideLayers('ventana_explicacion','','hide');
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
	layerEvento.style.top = (largo-100)/2 + 'px';
}

function hideEvento() {
	MM_showHideLayers('ventana_evento','','hide');
	var layerEvento = document.getElementById('ventana_evento');
	layerEvento.style.width = '0px';
	layerEvento.style.height = '0px';
}

function showMapa() {
	MM_showHideLayers('ventana_mapa','','show');
	var layerMapa = document.getElementById('ventana_mapa');
	//var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ancho = document.documentElement.scrollWidth;
	//var largo = document.documentElement.scrollHeight;
	layerMapa.style.width = '515px';
	layerMapa.style.height = '370px';
	layerMapa.style.left = (ancho+100)/2 + 'px';
	layerMapa.style.top = (largo-500)/2 + 'px';
}

function hideMapa() {
	MM_showHideLayers('ventana_mapa','','hide');
	var layerMapa = document.getElementById('ventana_mapa');
	layerMapa.style.width = '0px';
	layerMapa.style.height = '0px';
}

/*****/
function showReconfirmarMapa() {
	MM_showHideLayers('reconfirmar_mapa','','show');
	/*var layerMapa = document.getElementById('reconfirmar_mapa');
	var largo = screen.availHeight;
	var ancho = document.documentElement.scrollWidth;
	layerMapa.style.width = '500px';
	layerMapa.style.height = '300px';
	layerMapa.style.left = (ancho+100)/2 + 'px';
	layerMapa.style.top = (largo-500)/2 + 'px';*/
}

function hideReconfirmarMapa() {
	MM_showHideLayers('reconfirmar_mapa','','hide');
	/*var layerMapa = document.getElementById('reconfirmar_mapa');
	layerMapa.style.width = '0px';
	layerMapa.style.height = '0px';*/
}
/*****/


function cambiarEstiloCal(element, newStyle) {}
function seleccionarElementoCal(element, newStyle) {}
function setEstiloNormal(normalStyle) {}

function send_despacho( desp, precio, pick, fecha ) {
	var precio_aux = precio.replace("$","").replace(".","");
    document.t_picking.jpicking.value = pick ;
    document.t_picking.jprecio.value = precio_aux;
    document.t_picking.jdespacho.value = desp;
    document.t_picking.jfecha.value = fecha;
}

function guardarDireccion() {
  if ($('tipo_despacho1').checked) {
    if ( $('loc_id_des').value == "" ) {
      alert("Para poder ingresar debes seleccionar una dirección de despacho");
      return false;
    }
    $('loc_id').value = $('loc_id_des').value;
    $('forma_despacho').value = "D";
  } else if ($('tipo_despacho2').checked) {
    if ( $('loc_id_ret').value == "" ) {
      alert("Para poder ingresar debes seleccionar un local para retirar tu pedido");
      return false;
    }
    $('loc_id').value = $('loc_id_ret').value;
    $('forma_despacho').value = "R";
  } else {
    if ( $('loc_id_des').value == "" ) {
      alert("Para poder ingresar debes seleccionar una dirección de despacho");
      return false;
    }
    $('loc_id').value = $('loc_id_des').value;
    $('forma_despacho').value = "N";
  }
  var requestOptions = {
    'method': 'post',
    'parameters': {},
    'asynchronous': false,
    'onSuccess': function(REQUEST) {
      if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
        alert("Ocurrió un error al guardar la información");
        return false;
      }
      var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
      if ( mensaje == 'OK') {
         return true;
      } else {
        return false;
      }
    }
  };
  var deleteCart = "";
  if ( document.form1.rbtnelicarro && document.form1.rbtnelicarro[1].checked ) {
    deleteCart = "1";
  }
  var rndmx = Math.floor(Math.random()*10000);
  new Ajax.Request("ChangeDireccionSession?loc_id=" + $('loc_id').value + "&forma_despacho=" + $('forma_despacho').value + "&rbtnelicarro=" + deleteCart + "&rndmx=" + rndmx, requestOptions);
}

function sel_direccion() {
	var combo;
	if ($('tipo_despacho1').checked) {
		combo = $('loc_id_des');
	} else if ($('tipo_despacho2').checked) {
		combo = $('loc_id_ret');
	} else {
		combo = $('loc_id_des');
	}
    if (combo) {
        var arr_aux = combo.value.split( "--" );
        var rndmx = Math.floor(Math.random()*10000);
        var requestOptions = {
			'method': 'post',
			'parameters': {},
			'onSuccess': creaCalendario
		};
		new Ajax.Request("AjaxDespachoChart?zona_id=" + arr_aux[3] + "&rndmx=" + rndmx, requestOptions);
    }
}

function mueveCalendario(sel,pa,zona_id,cant_prod) {
    var requestOptions = {
			'method': 'post',
			'parameters': {},
			'onSuccess': creaCalendario
	};
	var rndmx = Math.floor(Math.random()*10000);
	new Ajax.Request("AjaxDespachoChart?sel=" + sel + "&pa=" + pa + "&zona_id=" + zona_id + "&cant_prod=" + cant_prod + "&paso=0" + "&rndmx=" + rndmx, requestOptions);
}

function creaCalendario(REQUEST){
	document.getElementById('jcalendario').innerHTML = REQUEST.responseText;
	tp1 = new WebFXTabPane( document.getElementById( "tabPane1" ) );
	if ( document.getElementById('down') != null ) {
		document.location.href = "#down";
	}
	showCalendario();
}

function ver_carro(){
	var layerCarro = document.getElementById('carroguardado');
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	layerCarro.style.left = (ancho+50)/2;
	layerCarro.style.top = (largo-400)/2;
	var carro = document.getElementById('carro')
	carro.src = "BienvenidaCarDisplay";
}

function validarDir(){
    var dir = $F('loc_id_des');//$('loc_id_des').value;
	//alert(dir);
	if (dir.indexOf("false") >= 0){
	    var msg = "";
		$$('select#loc_id_des option').each(function(o){
      		if(o.selected){msg = o.text}
		});
		address = msg.substring(msg.indexOf(":")+1) + ", Chile" ;
		//alert(address);
		hideReconfirmarMapa();
		showMapa();
        initialize();
        showLocation(address);
	}else{
	    showReconfirmarMapa();
	    hideMapa();
	}
}

function RevalidarDir(){
    var dir = $F('loc_id_des');//$('loc_id_des').value;
	//alert(dir);
	var pos = dir.indexOf("true");
	if (pos >= 0){
	    var latlng = dir.substring(pos+6);
	    //alert(latlng);
	    var pos2 = latlng.indexOf("--");
	    var lat = latlng.substring(0, pos2);
	    var lng = latlng.substring(pos2 + 2);
	    //alert("latitud: " + lat + "\rlongitud: " + lng);
		//address = msg.substring(msg.indexOf(":")+1) + ", Chile" ;
		//alert(address);
		
		showMapa();
        initialize();
        showLocationPoint(lat, lng);
	}
}

function cambioFormaDesapacho() {
	if ($('tipo_despacho1').checked) {
		$('combo_direcciones').show();
		$('combo_locales').hide();
		$('link_auto_jumbo').hide();
		$('msj_calendario').innerHTML = "Podr&aacute;s seleccionar el horario de despacho al final de la compra.";
		$('titulo_calendario').innerHTML = "Disponibilidad de despacho";
	} else if ($('tipo_despacho2').checked) {
		$('combo_direcciones').hide();
		$('combo_locales').show();
		$('link_auto_jumbo').show();
		$('msj_calendario').innerHTML = "Podr&aacute;s seleccionar el horario para retirar tu pedido al final de la compra.";
		$('titulo_calendario').innerHTML = "Horarios disponibles para retirar tu pedido";
	} else {
		$('combo_direcciones').show();
		$('combo_locales').hide();
		$('link_auto_jumbo').hide();
		$('msj_calendario').innerHTML = "Podr&aacute;s seleccionar el horario de despacho al final de la compra.";
		$('titulo_calendario').innerHTML = "Disponibilidad de despacho";
	}
	hideCalendario();
}

function linkJumboAuto() {
	var arr = $('loc_id_ret').value.split('--');
	window.open("/FO_WebContent/statics/autojumbo" + arr[0] + ".html",'_blank');
}

function init() {
var ancho = screen.availWidth;
var largo = screen.availHeight;
$("loading").style.position = 'absolute';
$("loading").style.width = '112px';
$("loading").style.height = '50px';
$("loading").style.left = (ancho-112)/2 + 'px';
$("loading").style.top = (largo)/2 -200 + 'px';

if ($("mostrar_evento").value == 'SI') {
	showEvento();
}
cambioFormaDesapacho();
if ($('loc_id_ret').length == 0) {
	$('tipo_despacho2').disabled = true;
}
if ( $('tiene_retiro').value == 'S' ) {
	$('web_normal').hide();
	$('web_retiro').show();
	//$('txt_cal_ret').innerHTML = "o tu retiro en local";
} else {
	$('web_normal').show();
	$('web_retiro').hide();
	//$('txt_cal_ret').innerHTML = "";
}
}

var globalCallbacks = {
                onCreate: function(){
                        $("loading").show();
                },
                onComplete: function() {
                        if(Ajax.activeRequestCount == 0){
                                $("loading").hide();
                        }
                }
        };
Ajax.Responders.register( globalCallbacks );