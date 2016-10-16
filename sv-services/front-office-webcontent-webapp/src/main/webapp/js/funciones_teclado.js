function KeyIsNumber(e) {
	var ev = e ? e : window.event;
    if (!ev) {
      return;
    }
    var targ = ev.target ? ev.target : ev.srcElement;
    var which = -1;
    if (ev.which) {
      which = ev.which;
    } else if (ev.keyCode) {
      which = ev.keyCode;
    }

	if ( ( which >= 0  && which <= 9 ) // Tab delete etc
      || ( which >= 48 &&  which <=57) ) // [0-9]
		return true;
	return false;
}

function KeyisDv(e) {
	var key;
	var keychar;
	var reg;

	if(window.event) {
		// for IE, e.keyCode or window.event.keyCode can be used
		key = e.keyCode;
	}
	else if(e.which) {
		// netscape
		key = e.which;
	}
	else {
		// no event, so pass through
		return true;
	}
	keychar = String.fromCharCode(key);
	reg = /[0-9kK\b]/;
	return reg.test(keychar);
}

function KeyIsRut(evt) {
	var isNav = (navigator.appName.indexOf("Netscape") != -1)
	var isIE = (navigator.appName.indexOf("Microsoft") != -1)
	if (isNav) {
		if  ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
            || evt.which == 46 || evt.which == 45 ||
			(evt.which >= 48 && evt.which <=  57) || 
			(evt.which == 75 || evt.which ==  107)    )
		return true;
			if(evt.keyCode == 13){
			 checkDV();
			}
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( evt.keyCode == 46 || evt.keyCode == 45 || 
			(evt.keyCode >= 48 && evt.keyCode <=  57) || 
			(evt.keyCode == 75 || evt.keyCode ==  107)     )
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}

function KeyIsTelefono(evt) {
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
            || evt.which >= 48 && evt.which <=57 ) // [0-9]
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( evt.keyCode >= 48 && evt.keyCode <= 57 )
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}

function KeyIsTexto(e) {
	var ev = e ? e : window.event;
    if (!ev) {
      return;
    }
    var targ = ev.target ? ev.target : ev.srcElement;
    var which = -1;
    if (ev.which) {
      which = ev.which;
    } else if (ev.keyCode) {
      which = ev.keyCode;
    }
    
	if ( ( which >= 0 && which <= 9 ) // Tab delete etc
        || which == 32 
        || which == 46  // .
       	|| (which >= 224 && which <= 252 ) // Acentos minusculas
       	|| (which >= 192 &&  which <=220) // acentos mayusculas
       	|| (which >= 48 && which <= 57 )   // [0-9]
       	|| (which >= 65 && which <= 90 )   // [a-z]
		|| (which >= 97 && which <= 122 )  // [A-Z]
		|| (which == 13)) // ENTER
		return true;
	return false;
}

function KeyIsTexto_R(e) {
	var ev = e ? e : window.event;
    if (!ev) {
      return;
    }
    var targ = ev.target ? ev.target : ev.srcElement;
    var which = -1;
    if (ev.which) {
      which = ev.which;
    } else if (ev.keyCode) {
      which = ev.keyCode;
    }

	if ( which == 34 ||  which == 36 || which == 39 || which == 47 || which == 92 )
		return false;
	return true;
}

function KeyIsClave(evt) {
	var isNav = (navigator.appName.indexOf("Netscape") != -1)
	var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
            || evt.which == 32 
            || evt.which == 46  // .
            || (evt.which >= 224 && evt.which <= 252 ) // Acentos minusculas
            || (evt.which >= 192 &&  evt.which <=220) // acentos mayusculas
            || (evt.which >= 48 && evt.which <= 57 )   // [0-9]
            || (evt.which >= 65 && evt.which <= 90 )   // [a-z]
            || (evt.which >= 97 && evt.which <= 122 ) ) // [A-Z]
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( evt.keyCode == 32 
            || evt.keyCode == 46
            || (evt.keyCode >= 224 && evt.keyCode <= 252)
            || (evt.keyCode >= 192 &&  evt.keyCode <=220)
            || (evt.keyCode >= 48 && evt.keyCode <= 57 ) 
            || (evt.keyCode >= 65 && evt.keyCode <= 90 ) 
            || (evt.keyCode >= 97 && evt.keyCode <= 122 ) )
		return true;
	return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false;
}

function KeyIsApellido(evt) {
	var isNav = (navigator.appName.indexOf("Netscape") != -1)
	var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
            || (evt.which >= 224 &&  evt.which <=252) // acentos
            || (evt.which >= 192 &&  evt.which <=220) // acentos mayusculas
            || (evt.which >= 65 &&  evt.which <=90)  // [a-z]
            || (evt.which >= 97 &&  evt.which <=122) // [A-Z]
            || (evt.which == 45) //guion
            || (evt.which == 32) ) //espacio
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( (evt.keyCode >= 224 && evt.keyCode <= 252) 
            || (evt.keyCode >= 192 && evt.keyCode <=220)
            || (evt.keyCode >= 65 && evt.keyCode <= 90) 
            || (evt.keyCode >= 97 && evt.keyCode <= 122) 
            || (evt.keyCode == 45)
            || (evt.keyCode == 32) )
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}

function KeyIsTexto_S(evt) {
	var isNav = (navigator.appName.indexOf("Netscape") != -1)
	var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 )
			|| (evt.which >= 224 && evt.which <= 252) 
			|| (evt.which >= 192 && evt.which <=220)
            || (evt.which >= 65 && evt.which <= 90) 
            || (evt.which >= 97 && evt.which <= 122) 
			|| (evt.which >= 48 && evt.which <= 57 )
            || (evt.which == 45)
            || (evt.which == 32)
			|| (evt.which == 13)//Enter
			)
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( (evt.keyCode >= 224 && evt.keyCode <= 252) 
            || (evt.keyCode >= 192 && evt.keyCode <=220)
            || (evt.keyCode >= 65 && evt.keyCode <= 90) 
            || (evt.keyCode >= 97 && evt.keyCode <= 122) 
			|| (evt.keyCode >= 48 && evt.keyCode <= 57 )
            || (evt.keyCode == 45)
            || (evt.keyCode == 32)
			|| (evt.keyCode == 13))//Enter
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}

function KeyIsTexto_Razon(evt) {
	var isNav = (navigator.appName.indexOf("Netscape") != -1)
	var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( (evt.which >= 224 && evt.which <= 252) 
			|| (evt.which >= 192 && evt.which <=220)
            || (evt.which >= 65 && evt.which <= 90) 
            || (evt.which >= 97 && evt.which <= 122) 
			|| (evt.which >= 48 && evt.which <= 57 )
            || (evt.which == 45)
            || (evt.which == 32)
			|| (evt.which == 64)//Enter
			|| (evt.which == 46)//Punto 
			)
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( (evt.keyCode >= 224 && evt.keyCode <= 252) 
            || (evt.keyCode >= 192 && evt.keyCode <=220)
            || (evt.keyCode >= 65 && evt.keyCode <= 90) 
            || (evt.keyCode >= 97 && evt.keyCode <= 122) 
			|| (evt.keyCode >= 48 && evt.keyCode <= 57 )
            || (evt.keyCode == 45)
            || (evt.keyCode == 32)
			|| (evt.keyCode == 64)//Enter
			|| (evt.keyCode == 46))//Punto
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}

function KeyIsTexto_MPA(evt) {
	var isNav = (navigator.appName.indexOf("Netscape") != -1);
	var isIE = (navigator.appName.indexOf("Microsoft") != -1);

	if (isNav) {
		if ( evt.which == 34 ||  evt.which == 39 || evt.which == 47 || evt.which == 92 ) {
			return false;
		}
		return true;
	} else if (isIE) {
		evt = window.event;
		if (evt.keyCode == 34 || evt.keyCode == 39 || evt.keyCode == 47 || evt.keyCode == 92) {
			return false;
		}
		return true;
	} else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false;
}
