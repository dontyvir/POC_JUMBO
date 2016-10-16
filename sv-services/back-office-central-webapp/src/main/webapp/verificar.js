function verificar() {
	var formulario = document.frmMod;
	if (!document.frmMod.rut.value){
		alert('Debes ingresar campo RUT');
		document.frmMod.rut.focus();
		return;
	}
	if (!document.frmMod.dv.value){
		alert('Debes ingresar campo dígito verificador');
		document.frmMod.dv.focus();
		return;
	}
	if(!checkRutField( document.frmMod.rut.value+"-"+document.frmMod.dv.value, document.frmMod.rut) ) {
        document.frmMod.rut.focus();
        return;
    }
}
