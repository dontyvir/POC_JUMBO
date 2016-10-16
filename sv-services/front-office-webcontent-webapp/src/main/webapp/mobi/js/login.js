function logon() {
	if ( $('rut').value == "" ) {
		mensaje("Debes ingresar el campo RUT");
		$('rut').focus();
		return;
	}
	if ( $('dv').value == "" ){
		mensaje("Debes ingresar el campo dU\00EDgito verificador");
		$('dv').focus();
		return;
	}
	if ( $('clave').value == "" ){
		mensaje("Debes ingresar el campo Clave");
		$('clave').focus();
		return;
	}
    if ( $('clave').value != "" && $('clave').value.length < 4 ){
		mensaje("El RUT o la clave que ingresaste es incorrecto");
		$('clave').focus();
		return ;
    }
    if(!checkRutFieldLogin( $('rut').value + "-" + $('dv').value, $('rut')) ) {
        $('rut').focus();
        return;
    }
	var requestOptions = {
		'method': 'post',
		'parameters': "rut=" + $('rut').value + "&dv=" +  $('dv').value + "&clave=" + $('clave').value,
		'onSuccess': validaLogin
	};
	new Ajax.Request('CheckLogin', requestOptions);
}

function validaLogin(REQUEST) {
	if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
		mensaje("ERROR");
		return;
	}	
	var msj = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
	if (msj == 'OK') {
		window.location.href="BienvenidaFormMobi";
		return;
	} else {
		mensaje(msj);
		$('clave').focus();
		return;
	}
}

function mensaje(msj) {
	$('mensaje').innerHTML = msj;
}

//Valida RUT
function checkRutFieldLogin( rut_in, rut_campo ) {
  var texto = rut_in; 
  var tmpstr = "";
  for (var i=0; i < texto.length ; i++ )
    if ( texto.charAt(i) != ' ' && texto.charAt(i) != '.' && texto.charAt(i) != '-' )
      tmpstr = tmpstr + texto.charAt(i);
  texto = tmpstr;
  largo = texto.length;

  if ( largo < 7 ) {
    mensaje("Debes ingresar el campo RUT completo");
    return false;
  }

  for (var i=0; i < largo ; i++ ) { 
    if ( texto.charAt(i) !="0" && texto.charAt(i) != "1" && texto.charAt(i) !="2" && texto.charAt(i) != "3" && texto.charAt(i) != "4" && texto.charAt(i) !="5" && texto.charAt(i) != "6" && texto.charAt(i) != "7" && texto.charAt(i) !="8" && texto.charAt(i) != "9" && texto.charAt(i) !="k" && texto.charAt(i) != "K" )  {
      mensaje("El valor ingresado no corresponde a un RUT v\u00E1lido, podr\u00EDas ingresarlo nuevamente");
      return false;
    }
  }

  if ( checkDVLogin(texto) )
   return true;
  return false;
}

function checkDVLogin( crut ) {
	largo = crut.length;
	rut = crut.substring(0, largo-1 );
	dv = crut.charAt(largo-1);
	if ( !checkCDVLogin( dv ) )
		return false

	if ( rut == null || dv == null )
		return 0

	var dvr = '0'
	suma = 0
	mul  = 2
	for (i= rut.length -1 ; i >= 0; i--) {
		suma = suma + rut.charAt(i) * mul
		if (mul == 7)
			mul = 2
		else    
			mul++
	}
	res = suma % 11
	if (res==1)
		dvr = 'k'
	else if (res==0)
		dvr = '0'
	else {
		dvi = 11-res
		dvr = dvi + ""
	}
	if ( dvr != dv.toLowerCase() ) {
		mensaje("El RUT es incorrecto, podr\u00EDas ingresarlo nuevamente");
		return false
	}
   return true
}

function checkCDVLogin( dvr ) {
	dv = dvr + ""
	if ( dv != '0' && dv != '1' && dv != '2' && dv != '3' && dv != '4' && dv != '5' && dv != '6' && dv != '7' && dv != '8' && dv != '9' && dv != 'k'  && dv != 'K') {
		mensaje("Debes ingresar un d\u00EDgito verificador v\u00E1lido");
		return false;
	}
	return true;
}