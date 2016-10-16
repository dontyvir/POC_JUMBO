function logon() {

	var formulario = document.acceso;
	if (!formulario.rut.value){
		alert('Debes ingresar campo Usuario');
		document.acceso.rut.focus();
		return;
	}
	
	if (!formulario.clave.value){
		alert('Debes ingresar campo Clave');
		document.acceso.clave.focus();
		return;
	}
	
    if( formulario.clave.value != "" && formulario.clave.value.length < 4 ){
            alert("El Usuario o la clave que ingresaste es incorrecto");
            formulario.clave.focus();
            return ;
    }
	
    formulario.submit();

}

function logon_cliente() {
	if (!document.acceso.dv.value){
		alert('Debes ingresar campo d?gito verificador');
		document.acceso.dv.focus();
		return;
	}
		
    if(!checkRutField( document.acceso.rut.value+"-"+document.acceso.dv.value, document.acceso.rut) ) {
        document.acceso.rut.focus();
        return;
    }
    document.acceso.submit();
}