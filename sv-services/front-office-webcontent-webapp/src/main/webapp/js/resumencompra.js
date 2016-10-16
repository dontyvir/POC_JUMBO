var $j = jQuery.noConflict();
var XBROWSER 		= navigator.appName;

//evitar F5 (refresh)
document.onkeydown = function(){  
    if(window.event && window.event.keyCode == 116){ 
     window.event.keyCode = 505;  
    } 
    if(window.event && window.event.keyCode == 505){  
     return false;     
    }  
}

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

function verDetallePedido() {
  var d = new Date();
  var url = "OrderPrint?tm="+d.getTime();
  window.open( url, 'detalle_pedido', "width=800, height=600, menubar=0, scrollbars=yes, location=0, resizable=1, status=1" );
}

function imprimir(){
	var d = new Date();
	var url = "OrderPrint?print=1&tm="+d.getTime();
	if(navigator.appName == "Microsoft Internet Explorer")
		printURL(url)
	else {
		var url = "OrderPrint?print=1&tm="+d.getTime();
		window.open( url, 'imprimr', "width=800, height=600, menubar=0, scrollbars=yes, location=0, resizable=1, status=1" );
	}
}

function validaNombreLista() {
	var obj = document.frmGuardaCompra;
	if (obj.txtNewNombre.value == '' ){
		alert("Ingrese el nombre para la lista de compra");
		obj.txtNewNombre.focus();
		obj.txtNewNombre.select();
		return false;	
	}
	return true;
}

function guarda_compra() {
	if (validaNombreLista()) {	
		document.frmGuardaCompra.action = "OrderSetList";
		document.frmGuardaCompra.submit();
	}
}

function validaDatosListaInvitado() {
	if ($j('#email_invitado').val() == '' ){
		alert("Ingrese su email");
		$j('#email_invitado').focus();
		$j('#email_invitado').select();
		return false;	
	} else if ($j('#telefono_invitado').val() == '' ){
		alert("Ingrese su telefono");
		$j('#telefono_invitado').focus();
		$j('#telefono_invitado').select();
		return false;	
	}
	if ($j('input:radio[name=opcion_compra]:checked').val() == 1) {
		if ($j('#rut_invitado').val() == '' ){
			alert("Ingrese su RUT");
			$j('#rut_invitado').focus();
			$j('#rut_invitado').select();
			return false;	
		} else if ($j('#dv_invitado').val() == '' ){
			alert("Ingrese su RUT");
			$j('#dv_invitado').focus();
			$j('#dv_invitado').select();
			return false;
		} else if ( !checkDV( $j('#rut_invitado').val() + $j('#dv_invitado').val() )) {
		    $j('#rut_invitado').focus();
		    return false;
		} else if ($j('#nombre_invitado').val() == '' ){
			alert("Ingrese su Nombre");
			$j('#nombre_invitado').focus();
			$j('#nombre_invitado').select();
			return false;	
		} else if ($j('#apellido_invitado').val() == '' ){
			alert("Ingrese su Apellido");
			$j('#apellido_invitado').focus();
			$j('#apellido_invitado').select();
			return false;	
		}
	}
	
	return true;
}

/*function guarda_compra_invitado() {
	if (validaDatosListaInvitado()) {	
		document.frmGuardaCompra.action = "OrderSetListInvitado";
		document.frmGuardaCompra.submit();
	}
}*/

function guarda_compra_invitado(){
    //if (RegSencillo_ValidaCliente()){
    if (validaDatosListaInvitado()){
        //alert();
/*email_invitado
codigo_invitado
telefono_invitado
opcion_compra

if (opcion_compra.equals("1")) {
    rut_invitado
    dv_invitado
    nombre_invitado
    apellido_invitado
}
     */
	    var params = "email_invitado="     + $j('#email_invitado').val()
	               + "&codigo_invitado="   + $j('#codigo_invitado').val()
	               + "&telefono_invitado=" + $j('#telefono_invitado').val()
	                + "&opcion_compra="     + $j('input:radio[name=opcion_compra]:checked').val()
	               + "&id_pedido=" + $j('#id_pedido').val();
	    if ($j('input:radio[name=opcion_compra]:checked').val() == 1) {
	       params += "&rut_invitado="      + $j('#rut_invitado').val()
	               + "&dv_invitado="       + $j('#dv_invitado').val()
	               + "&nombre_invitado="   + $j('#nombre_invitado').val()
	               + "&apellido_invitado=" + $j('#apellido_invitado').val()
	               + "&id_pedido=" + $j('#id_pedido').val();
	    }
	    //var params = "";
	    //alert("params=> " + params);
		var requestOptions = {
			'method': 'post',
			'parameters': params,
			'onSuccess': function(REQUEST) {
				if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
					alert("Ocurrió un error en el registro.");
					return;
				}	
				var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
				//alert("respuesta= '" + respuesta + "'");
				if (respuesta == "OK") {
				    if ($j('input:radio[name=opcion_compra]:checked').val() == 1) {
				        showMensajeExito();
				        //$j('#MensajeRegistroOK').show();
				    }
				}else if (respuesta == "1") {
					alert("El cliente ya existe.");
					return;				
				}else if(respuesta == "2") {
					alert("El largo del rut debe ser mayor a 5.");
					return;				
				}
			}
		}
		new Ajax.Request('OrderSetListInvitado', requestOptions);
    }
}


function showMensajeExito() {
  upWeb();
  showLightBoxMensajeExito();
  $j('#mensaje_dir_exito').show();
  $j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
		  closeLBoxWin();
  });
}

function upWeb() {
  if ( XBROWSER == "Microsoft Internet Explorer" ) {
    window.scrollTo(0,0);
  }
}

function showLightBoxMensajeExito() {
  tb_show('Jumbo.cl','#TB_inline?height=150&width=500&inlineId=hiddenModalContent&modal=true');
}

function modificar_sustitutos() {
	if (validaNombreLista()) {	
		document.frmGuardaCompra.action = "SustitutosForm";
		document.frmGuardaCompra.submit();
	}
}

function inhabilita_campo(){
	document.frmGuardaCompra.txtNewNombre.readOnly = true;
}

function habilita_campo(){
	document.frmGuardaCompra.txtNewNombre.readOnly = false;
}

function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];} 
    }
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
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

function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
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
	               + "&id_region="  + $j('#id_region').val()
	               + "&id_comuna="  + $j('#id_comuna').val()
	               + "&email="      + $j('#email_reg').val()
	               + "&envioEMail=" + envioEMail
	               + "&fon_cod="    + $j('#fon_cod').val()
	               + "&fon_num="    + $j('#fon_num').val()
	               + "&envioSMS="   + envioSMS
	               + "&clave="      + $j('#clave1').val()
	               + "&idPedido="   + idPedido
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
					alert("Ocurrió un error en el registro.");
					return;
				}	
				var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
				//alert("respuesta= '" + respuesta + "'");
				if (respuesta == 'OK') {
				    //logonRegSencillo('');					
				    closeLBoxWin();
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
	} else if ($j('#id_region').val() == 0) {
		alert("Debes seleccionar la Región");
		$j('#id_region').focus();
		return false;
	} else if ($j('#id_comuna').val() == 0) {
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
		alert("Debes ingresar el campo Teléfono");
		$j('#fon_num').focus();
		return false;
	} else if(isLongNumberInsert(codigo)){
		if(!/^\d{8}$/.test(numero)) {
			alert("Debe ingresar su número de contacto correctamente");
			jQuery('#fon_num').val("");	
			jQuery('#fon_num').focus();
			return false;
		}
	}else{
		if(!/^\d{7}$/.test(numero)) {
			alert("Debe ingresar su número de contacto correctamente");
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
		alert("La Clave debe tener mínimo 4 caracteres.");
		$j('#clave1').focus();
		return false;
	} else if ($j('#clave2').val() == "") {
		alert("Debes ingresar el campo 'Confirmar Clave'");
		$j('#clave2').focus();
		return false;
	} else if ($j('#clave2').val().length < 4){
		alert("La Clave debe tener mínimo 4 caracteres.");
		$j('#clave2').focus();
		return false;
	}
    if ($j('#clave1').val() != $j('#clave2').val()) {
		alert("Las claves ingresadas no coinciden");
		$j('#clave1').focus();
		return false;
	}
	if (!($j('#terminos').is(':checked'))) {
		alert("Debes aceptar los términos y condiciones");
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
		alert("Debes ingresar el campo Número");
		$j('#numero').focus();
		return false;
	} else if ($j('#id_region_dir').val() == 0) {
		alert("Debes seleccionar la Región");
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

    //VERIFICA QUE EL CLIENTE HAYA CAMBIADO SU DIRECCIÓN
	} else if ($j('#calle').val() == "Ingresa tu calle") {
		alert("Debes modificar el campo calle");
		$j('#calle').focus();
		return false;
	} else if ($j('#numero').val() == "000") {
		alert("Debes modificar el campo Número");
		$j('#numero').focus();
		return false;
	} else if ($j('#alias_rs').val() == "Nombre") {
		alert("Debes modificar el campo 'Guardar como'");
		$j('#alias_rs').focus();
		return false;
	}
	return true;
}
function validarEmail(valor){
	re=/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;     
	if(!re.exec(valor))    {
	    alert("La dirección de email es incorrecta o está mal escrita. Ingrésala nuevamente");
	    return (false);
	} else {
		return (true);
}
}
function logonRegSencillo(type_lbox) {
	  var f2 = document.RegistroSencillo;
	  goToTheSite(f2.cli_rut_reg.value, f2.clave1.value, type_lbox, f2.url_to.value);  
	}
//valida si se va a evaluar un numero de 8 digitos o de 7
function isLongNumberInsert(codigo){
	 if ((codigo == "9")||(codigo == "2")){
		return true;
	}
	return false;
}