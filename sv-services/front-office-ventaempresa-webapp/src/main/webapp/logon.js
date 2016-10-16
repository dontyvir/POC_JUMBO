function logon() {

	var formulario = document.acceso;
	if (!document.acceso.rut.value){
		alert('Debes ingresar el campo RUT');
		document.acceso.rut.focus();
		return false;
	}
	if (!document.acceso.dv.value){
		alert('Debes ingresar el campo d?gito verificador');
		document.acceso.dv.focus();
		return false;
	}
	
	if (!document.acceso.clave.value){
		alert('Debes ingresar el campo Clave');
		document.acceso.clave.focus();
		return false;
	}
	
    if( formulario.clave.value != "" && formulario.clave.value.length < 4 ){
            alert("El RUT o la clave que ingresaste es incorrecto");
            formulario.clave.focus();
            return false;
    }

    if(!checkRutField( document.acceso.rut.value+"-"+document.acceso.dv.value, document.acceso.rut) ) {
        document.acceso.rut.focus();
        return false;
    }

    formulario.submit();

}


function logon_fono(){
	var formulario = document.acceso;
	if (!document.acceso.rut.value){
		alert('Debes ingresar el campo RUT');
		document.acceso.rut.focus();
		return false;
	}
	if (!document.acceso.dv.value){
		alert('Debes ingresar el campo d?gito verificador');
		document.acceso.dv.focus();
		return false;
	}
	
	if (!document.acceso.clave.value){
		alert('Debes ingresar el campo Clave');
		document.acceso.clave.focus();
		return false;
	}
	
    if( formulario.clave.value != "" && formulario.clave.value.length < 4 ){
            alert("El RUT o la clave que ingresaste es incorrecto");
            formulario.clave.focus();
            return false;
    }

    if(!checkRutField( document.acceso.rut.value+"-"+document.acceso.dv.value, document.acceso.rut) ) {
        document.acceso.rut.focus();
        return false;
    }

	if (!document.acceso.rut2.value){
		alert('Debes ingresar el campo RUT');
		document.acceso.rut2.focus();
		return false;
	}
	if (!document.acceso.dv2.value){
		alert('Debes ingresar el campo d?gito verificador');
		document.acceso.dv2.focus();
		return false;
	}

    if(!checkRutField( document.acceso.rut2.value+"-"+document.acceso.dv2.value, document.acceso.rut2) ) {
        document.acceso.rut2.focus();
        return false;
    }

	formulario.submit();

}