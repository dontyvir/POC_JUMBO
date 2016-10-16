var flag = false;
function showOlvidaClaveLayer() {
	var frame = document.getElementById('texto');
	frame.style.height = '162px';
	if (flag)
		frame.src = '/FO_WebContent/layers/nuevaclave.html';
	var layerOlvidaClave = document.getElementById('ventana');
	layerOlvidaClave.style.visibility = 'visible';
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	layerOlvidaClave.style.width = '284px';
	layerOlvidaClave.style.height = '162px';
	layerOlvidaClave.style.left = (ancho-284)/2 + 'px';
	layerOlvidaClave.style.top = (largo)/2 - 162 + 'px';
	flag = true;
}

function hideOlvidaClaveLayer() {
  var layerOlvidaClave = document.getElementById('ventana');
  layerOlvidaClave.style.visibility = 'hidden';	
  layerOlvidaClave.style.width = '0px';
  layerOlvidaClave.style.height = '0px';  
  closeLBoxWin();
}

function validaFormLogin( f ) {
  if ( !f.cli_rut.value ) {
    alert('Debes ingresar el campo RUT');
    f.cli_rut.focus();
    return false;
  }
  if ( !f.clave.value ) {
    alert('Debes ingresar el campo Clave');
    f.clave.focus();
    return false;
  }
  if ( f.clave.value != "" && f.clave.value.length < 4 ) {
    alert("El RUT o la clave que ingresaste es incorrecto");
    f.clave.focus();
    return false;
  }
  if ( !checkRutValue( f.cli_rut ) ) {
    f.cli_rut.focus();
    return false;
  }
  return true;
}

function logon() {
  var f1 = document.acceso;
  if ( !validaFormLogin( f1 ) ) {
    return;
  }
  /** INI BORRAR 
	var ruts = ["13827994","13538707","10914467","14048436","14320349","22167634","12887776","13545334"];	
	var entrar = false;
	for (var i = 0; i < ruts.length; i++ ) {
	  if ( ruts[i] == f1.cli_rut.value ) {
	    entrar = true;
	  }	
	}
	if ( !entrar ) {
	  alert("Estimado cliente:\nEn estos momentos el sitio web jumbo.cl está en mantención,\npor favor intente más tarde.\n\nMuchas gracias.");
	  return;
	}
	FIN BORRAR **/  
  goToTheSite(f1.cli_rut.value, f1.clave.value, "", "UltComprasForm?opcion=2");  
}

function logon2(type_lbox) {
  var f2 = document.acceso2;
  if ( !validaFormLogin( f2 ) ) {
    return;
  }
  goToTheSite(f2.cli_rut.value, f2.clave.value, type_lbox, f2.url_to.value);  
}

function logonRegSencillo(type_lbox) {
  var f2 = document.RegistroSencillo;
  goToTheSite(f2.cli_rut_reg.value, f2.clave1.value, type_lbox, f2.url_to.value);  
}

function close_lbox() {
  var norm = document.getElementById("ingreso_clientes");
  //var box = document.getElementById("ingreso_clientes_lbox");
  box.style.visibility="hidden";
  box.style.width="0px";
  box.style.height="0px";
  norm.style.display="block";
  norm.style.visibility="visible";
  norm.style.width="230px";
  norm.style.height="306px";
}

function show_lbox() {
  var norm = document.getElementById("ingreso_clientes");
  //var box = document.getElementById("ingreso_clientes_lbox");
  norm.style.visibility="hidden";
  norm.style.width="0px";
  norm.style.height="0px";
  box.style.display="block";
  box.style.visibility="visible";
  box.style.width="230px";
  box.style.height="306px";
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

function goToTheSiteSesion(url_x) {
  url_x = replaceAll(url_x,"[q]","?");
  url_x = replaceAll(url_x,"[a]","&");
  window.location.href=url_x;
}

function recibePromociones() {
	if ( jQuery.trim( jQuery('#email').val() ) == "" ) {
		//jQuery('#msg_ingreso_mail').html('Ingrese su E-Mail.');
		alert('Debes ingresar un E-Mail válido.');
		jQuery('#email').focus();
		return;
	}
	if(!/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/.test( jQuery('#email').val() )) { 
		//jQuery('#msg_ingreso_mail').html('Ingrese correctamente su E-Mail.');
		alert('Debes ingresar un E-Mail válido.');
		jQuery('#email').focus();
		return;
	}

	jQuery.get("MailHome"
		, {email:jQuery('#email').val()}
		,function(xml){			
			jQuery(xml).find('mail').each(function() {
				var mensaje = jQuery(this).find('mensaje').text();				
				//jQuery('#msg_ingreso_mail').html(mensaje);
				jQuery('#email').val('');
				alert(mensaje);
			});
		});	
}

function getParamUrl(name) {
  var start=location.search.indexOf("?"+name+"=");
  if (start<0) start=location.search.indexOf("&"+name+"=");
  if (start<0) return '';
  start += name.length+2;
  var end=location.search.indexOf("&",start)-1;
  if (end<0) end=location.search.length;
  var result='';
  for(var i=start;i<=end;i++) {
    var c=location.search.charAt(i);
    result=result+(c=='+'?' ':c);
  }
  return unescape(result);
}

function clean() {
	//jQuery('#msg_ingreso_mail').html('');
	$j('#email').html('');
}

function getHeaderHome(){
	var params = "";
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			$j('#loginJumboFO').show();			
			$j('#utilitariosDC').html(REQUEST.responseText);
			
			var a_href = $j('#AjaxHeaderHome_CerrarSesion').attr('id');			 
			if(a_href != undefined && htmlloginJumboFO != null ){
				$j('#loginJumboFO').html(htmlloginJumboFO);
			}
		}		
	}
	new Ajax.Request('AjaxLogonForm', requestOptions);
}


jQuery().ready(function() {
  if ( document.acceso != null ) {
    //Esto es en el home
    document.acceso.cli_rut.focus();
    var ancho = screen.availWidth;
    var largo = screen.availHeight;
    var pos = findPos(document.getElementById("ingreso_clientes"));
    /*jQuery('#loading').css({"position":"absolute", "width":"112px", "height":"50px" , "left": ""+ pos[0] + "px" , "top": ""+ (pos[1]+13) + "px" });
    jQuery('#loading').ajaxStart(function() {
      if ( document.getElementById("ingreso_clientes_lbox").style.height == '0px' ) {
        jQuery(this).show();
      } else {
        jQuery('#loading_lbox').show();
      }
    });
    jQuery('#loading').ajaxStop(function() {
      jQuery(this).hide();
      jQuery('#loading_lbox').hide();
    });*/
  } else {
    //Esto es en supermercado
    $j('.loading_lbox').ajaxStart(function() {
      $j(this).show();
    });
    $j('.loading_lbox').ajaxStop(function() {
      $j(this).hide();
    }); 
  }
 
  //document.acceso2.cli_rut.value = getParamUrl('id');
  //document.acceso2.url_to.value = getParamUrl('url');
  if ( jQuery('#url_to').val() != '' ) {
    //Si ya estaba conectado lo enviamos directamente
    if ( SES_ID_LOC != "" ) {
      goToTheSiteSesion( jQuery('#url_to').val() );
      return;
    }
    show_lbox();
    tb_show('Jumbo.cl','#TB_inline?height=316&width=523&inlineId=hiddenModalContent&modal=true');
    jQuery('#TB_ajaxContent').css({backgroundImage:'url("/FO_IMGS/images/html/fondo_layer.gif")'});
    if ( document.acceso2.cli_rut.value != '' ) {
      checkRutValue(document.acceso2.cli_rut);
      document.acceso2.clave.focus();
    } else {
      document.acceso2.cli_rut.focus();
    }
  }	
});
