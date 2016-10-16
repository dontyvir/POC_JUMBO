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

var objectSelected = null;
var defaultStyle = null;
var tipoOld = 'N';

function cambiarEstilo(element, newStyle) {
	if (objectSelected != null) {
		if (element != objectSelected)
			element.className = newStyle;
	} else 
		element.className = newStyle;
}

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

function setEstiloNormal(normalStyle) {
	defaultStyle = normalStyle;
}

function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}

function formaDePagoLayer(value) {
	if (value == 1) {
		document.getElementById('jumbo2').style.visibility = 'visible';
		document.getElementById('paris').style.visibility = 'hidden';
		document.getElementById('paris-adicional').style.visibility = 'hidden';
		document.getElementById('parisempleado').style.visibility = 'hidden';
		document.getElementById('parisnormal').style.visibility = 'hidden';
		document.getElementById('masparis').style.visibility = 'hidden';
		document.getElementById('tablaparis').style.height = '30px';
		document.getElementById('bancaria').style.visibility = 'hidden';
		document.getElementById('formasDePago').style.height = '80px';
		document.getElementById('mpa').style.visibility = 'hidden';
		document.frmPedido.p_numtja.value = "";
		document.frmPedido.titular[0].checked = false;
		document.frmPedido.titular[1].checked = false;
	}
	else if (value == 2) {
		if (document.frmPedido.empleado.value == 1){
		  document.getElementById('parisempleado').style.visibility = 'visible';
		  document.getElementById('jumbo2').style.visibility = 'hidden';
		  document.getElementById('paris-adicional').style.visibility = 'hidden';
		  document.getElementById('bancaria').style.visibility = 'hidden';
		  document.getElementById('formasDePago').style.height = '80px';
		  document.getElementById('mpa').style.visibility = 'hidden';
		} else {
		  document.getElementById('paris').style.visibility = 'visible';
		  document.getElementById('parisnormal').style.visibility = 'hidden';	
		  document.getElementById('jumbo2').style.visibility = 'hidden';
		  document.getElementById('masparis').style.visibility = 'hidden';
		  document.getElementById('paris-adicional').style.visibility = 'hidden';
		  document.getElementById('bancaria').style.visibility = 'hidden';
		  document.getElementById('formasDePago').style.height = '50px';
		  document.getElementById('mpa').style.visibility = 'hidden';
		}
	} else if (value == 3) {
		document.getElementById('jumbo2').style.visibility = 'visible';
		document.getElementById('paris').style.visibility = 'hidden';
		document.getElementById('paris-adicional').style.visibility = 'hidden';
		document.getElementById('parisempleado').style.visibility = 'hidden';
		document.getElementById('parisnormal').style.visibility = 'hidden';
		document.getElementById('masparis').style.visibility = 'hidden';
		document.getElementById('tablaparis').style.height = '30px';
		document.getElementById('bancaria').style.visibility = 'hidden';
		document.getElementById('formasDePago').style.height = '80px';
		document.getElementById('mpa').style.visibility = 'hidden';
		document.frmPedido.p_numtja.value = "";
		document.frmPedido.titular[0].checked = false;
		document.frmPedido.titular[1].checked = false;
	}else if (value == 4) {
		document.getElementById('bancaria').style.visibility = 'visible';
		document.getElementById('jumbo2').style.visibility = 'hidden';
		document.getElementById('paris').style.visibility = 'hidden';
		document.getElementById('paris-adicional').style.visibility = 'hidden';
		document.getElementById('parisempleado').style.visibility = 'hidden';
		document.getElementById('parisnormal').style.visibility = 'hidden';
		document.getElementById('masparis').style.visibility = 'hidden';
		document.getElementById('tablaparis').style.height = '30px';
		document.getElementById('formasDePago').style.height = '215px';
		document.getElementById('mpa').style.visibility = 'hidden';
		document.frmPedido.p_numtja.value = "";
		document.frmPedido.titular[0].checked = false;
		document.frmPedido.titular[1].checked = false;
	}else if (value == 5) {
		document.getElementById('bancaria').style.visibility = 'hidden';
		document.getElementById('jumbo2').style.visibility = 'hidden';
		document.getElementById('paris').style.visibility = 'hidden';
		document.getElementById('paris-adicional').style.visibility = 'hidden';
		document.getElementById('parisempleado').style.visibility = 'hidden';
		document.getElementById('parisnormal').style.visibility = 'hidden';
		document.getElementById('masparis').style.visibility = 'hidden';
		document.getElementById('tablaparis').style.height = '30px';
		document.getElementById('formasDePago').style.height = '150px';
		document.getElementById('mpa').style.visibility = 'visible';
		document.frmPedido.p_numtja.value = "";
		document.frmPedido.titular[0].checked = false;
		document.frmPedido.titular[1].checked = false;
	}
}

function parisLayer(numero_tarjeta,evento){
	var numero = document.frmPedido.p_numtja.value;	
	var tipo = validaTipoParis(numero,evento);
	if (tipo == "PARIS") {
	  //tarjeta paris normal
	  if (document.getElementById('parisnormal').style.visibility == 'hidden'){	
		  document.getElementById('formasDePago').style.height = '115px';
		  document.getElementById('tablaparis').style.height = '100px';
	      document.getElementById('parisnormal').style.visibility = 'visible';
		  document.getElementById('masparis').style.visibility = 'hidden';
		  document.frmPedido.tipo_paris.value=tipo;
		  return;
	  }	
	} else if (tipo == "MASPARIS") {
 	  //tarjeta mas paris
	  if (document.getElementById('masparis').style.visibility == 'hidden')	{
		  document.getElementById('formasDePago').style.height = '80px';
		  document.getElementById('tablaparis').style.height = '65px';
	      document.getElementById('masparis').style.visibility = 'visible';
		  document.getElementById('parisnormal').style.visibility = 'hidden';
		  document.getElementById('paris-adicional').style.visibility = 'hidden';	
		  document.frmPedido.tipo_paris.value=tipo;
		  document.frmPedido.titular[0].checked = false;
		  document.frmPedido.titular[1].checked = false;		
		  return;	
	  }	 
    } else if (tipo == "NO"){
	  //Si hay algún problema con el número
	  if (evento == "onblur") {		
	  	alert("Por favor, Ingrese un número de tarjeta válido");
	  }
	  document.frmPedido.tipo_paris.value="";		  
	  return;	
	}
}

function titularAdicional(){
	if (document.frmPedido.titular[1].checked){
		document.getElementById('formasDePago').style.height = '275px';
		document.getElementById('tablaparis').style.height = '260px';
    	document.getElementById('paris-adicional').style.visibility = 'visible';
	} else {
		document.getElementById('formasDePago').style.height = '115px';
		document.getElementById('tablaparis').style.height = '100px';
    	document.getElementById('paris-adicional').style.visibility = 'hidden';
    }
}

function muestraPromos() {
	$('promo_form').style.visibility = 'visible';
	//$('mastarjetas').style.visibility = 'visible';
	//$('mastarjetas').style.height = '200px';
	$('promo_form').show();
	//$('promo_cupones').style.backgroundImage = "url(/FO_IMGS/img/promociones/mina.jpg)";
	$('promo_cupones').style.backgroundRepeat = "no-repeat";
	$('promo_cupones').style.backgroundPosition = "left bottom";
}

function escondePromos() {
	//$('mastarjetas').style.visibility = 'hidden';
	//$('mastarjetas').style.height = '0px';

	//$('div_ad_arriba').style.height = '0px';
	//$('promo_centro').style.height = '0px';
	
	//$('div_ad_bajo').style.height = '0px';
	$('promo_form').hide();
//	$('promo_form').style.height = '0px';


	$('promo_cupones').style.backgroundImage = "";
	$('promo_cupones').style.backgroundRepeat = "";
	$('promo_cupones').style.backgroundPosition = "";
		
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

// Tablas dinámicas para los cupones
function insRow( numero ) {
  var x=window.document.getElementById('t_cupones').insertRow(-1);
  var c1=x.insertCell(0);
  var c2=x.insertCell(1);
  c1.innerHTML=numero;

  c2.innerHTML='<A HREF="javascript:delRow('+x.rowIndex+')" title="Eliminar cupón"><img src="/FO_IMGS/img/estructura/paso3/eliminar.gif" width="14" height="10" border="0" alt="Eliminar Cupón" title="Eliminar Cupón" align="left" /></a>';
}

// Eliminar todas las filas
function clearTable() {
	if ($('t_cupones')) {
		while (window.document.getElementById('t_cupones').rows.length>1) {
			window.document.getElementById('t_cupones').deleteRow(1);
		}
	}
}  

function cantCupones() {
	var form = document.form_pro;
	var cupones = form.cupones.value.split("-=-");
	return cupones.length-1;
}
  
function delRow(r) {

	var form = document.form_pro;

    var cupones = form.cupones.value.split("-=-");
	form.cupones.value = "";
	for( var i = 0; i < cupones.length-1; i++ ) {
		if( i != r-1 ) {
			form.cupones.value += cupones[i] + "-=-";
		}
	}  

	//loadRows();
	actualizar_productos();

}

function loadRows() {
	clearTable();
	var form = document.form_pro;
	var cupones = form.cupones.value.split("-=-");
	if ( cupones.length-1 > 0 ) {
		$('tb_de_cupones').show();
		for( var i = 0; i < cupones.length-1; i++ ) {
			insRow( cupones[i] );
		}
	} else {
		$('tb_de_cupones').hide();		
	}	  
}  

function add_cupon() {
	var form = document.form_pro;
	if ( Trim(document.getElementById('cupon').value) == '' ) {
		alert("Debe ingresar el número de cupón.");
		document.getElementById('cupon').focus();
		return;
	}
	form.cupones.value += document.getElementById('cupon').value+"-=-";//window.document.fcupones.cupon.value+"-=-";
	document.getElementById('cupon').value = ""; //window.document.fcupones.cupon.value = "";
	actualizar_productos();
}

function cambioFormaDespacho() {
	if ($('forma_despacho').value == 'D' || $('forma_despacho').value == 'N') {
		if ($('retiro1').checked) {
			showModificarDireccionLayer();
		} else {
			hideModificarDireccionLayer();
		}	
	} else {
		if ($('despacho2').checked) {
			showModificarDireccionLayer();
		} else {
			hideModificarDireccionLayer();
		}
	}
}

function reestableceChecks() {
	if ($('forma_despacho').value == 'D' || $('forma_despacho').value == 'N') {
		$('despacho1').checked = true;
	} else {
		$('retiro2').checked = true;
	}
}

function iniFormaDespacho() {
	if ($('forma_despacho').value == 'D' || $('forma_despacho').value == 'N') {
		$('despacho_domicilio').show();
		$('despacho_autojumbo').hide();
		$('despacho_domicilio_normal').hide();
		
		$('msj_titulo_despacho').innerHTML = "Si no  estas en casa al momento de la entrega:";
		$('msj_autorizacion').innerHTML = "Autorizas a recibir tu pedido a:";
		
		$('sin_gente_txt').removeClassName('inputOculto');
		$('retira_txt').addClassName('inputOculto');
		
		$('tabla_rut_sin_gente').hide();
		$('nota_despachadores').show();	
		
	} else if ($('forma_despacho').value == 'R') {
		$('despacho_autojumbo').show();
		$('despacho_domicilio').hide();
		$('despacho_domicilio_normal').hide();
		
		$('msj_titulo_despacho').innerHTML = "Si no puedes ir a buscar tu pedido:";
		$('msj_autorizacion').innerHTML = "Autorizas a retirar tu pedido a:";
		
		$('sin_gente_txt').addClassName('inputOculto');
		$('retira_txt').removeClassName('inputOculto');
		
		$('tabla_rut_sin_gente').show();
		$('nota_despachadores').hide();
	} else {	
		$('despacho_domicilio_normal').show();
		$('despacho_autojumbo').hide();
		$('despacho_domicilio').hide();
		
		$('sin_gente_txt').removeClassName('inputOculto');
		$('retira_txt').addClassName('inputOculto');
		
		$('tabla_rut_sin_gente').hide();
		$('nota_despachadores').show();
	
	}
}

function traerCombos() {
	var requestOptions = {
		'method': 'post',
		'parameters': 'destino=pag_form&mostrar_forma_despacho=' + traeFormaDesachoAMostrar(),
		'onSuccess': function(REQUEST) {
			$('modificar_direccion').innerHTML = REQUEST.responseText;
		}
	};
	new Ajax.Request('layers/CheckoutAddressForm', requestOptions);
}

function traeFormaDesachoAMostrar() {
	if ($('forma_despacho').value == 'D' || $('forma_despacho').value == 'N') {
		if ($('despacho1').checked) {
			return "D";
		} else {
			return "R";
		}	
	} else if ($('forma_despacho').value == 'R') {
		if ($('retiro2').checked) {
			return "R";
		} else {
			return "D";
		}
	}
	return "N";
}

function showModificarDireccionLayer() {
	var ypos = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
	$("modificardireccion").style.visibility	= 'visible';
	$("modificardireccion").style.left			= (screen.availWidth-283)/2 + 'px';
	$("modificardireccion").style.top			= (screen.availHeight-190)/2 + ypos - 50 + 'px';
	$("modificardireccion").style.width			= '283px';
	$("modificardireccion").style.height 		= '190px';
	var requestOptions = {
		'method': 'post',
		'parameters': 'destino=pag_confirmacion',
		'onSuccess': function(REQUEST) {
			$('modificar_direccion').innerHTML = REQUEST.responseText;
		}
	};
	new Ajax.Request('layers/CheckoutAddressForm', requestOptions);
}

function hideModificarDireccionLayer() {
	var layerModificarDireccion = document.getElementById('modificardireccion');
	layerModificarDireccion.style.visibility = 'hidden';	
	layerModificarDireccion.style.width = '0px';
	layerModificarDireccion.style.height = '0px';
}

function modificarFormaDespacho(fd) {
	var requestOptions = {
		'method': 'post',
		'parameters': 'direccionid=' + $('cmbDireccionDespacho').value + '&f_despacho=' + fd,
		'onSuccess': function(REQUEST) {
			if (REQUEST.responseXML != null) {
				if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] != null) {
					var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
					var formaDespa = REQUEST.responseXML.getElementsByTagName("forma_despacho")[0].childNodes[0].nodeValue;
					var direccion = REQUEST.responseXML.getElementsByTagName("direccion")[0].childNodes[0].nodeValue;
					var idLocal = REQUEST.responseXML.getElementsByTagName("id_local")[0].childNodes[0].nodeValue;
					if (mensaje == 'OK') {
						llenarCarroCompras();
						$('forma_despacho').value = formaDespa;
						$('txt_dir_desp').innerHTML = direccion;
						$('txt_dir_desp1').innerHTML = direccion;
						$('str_local_retiro').innerHTML = direccion;
						$('id_local_selec').value = idLocal;
						reestableceChecks();
						iniFormaDespacho();						
					}
				}
			}
			reestableceChecks();
			hideModificarDireccionLayer();					
		}
	};
	new Ajax.Request('CheckoutAddressUpdate', requestOptions);
}

function linkJumboAuto() {
	window.open("/FO_WebContent/statics/autojumbo" + $('id_local_selec').value + ".html",'_blank');
}