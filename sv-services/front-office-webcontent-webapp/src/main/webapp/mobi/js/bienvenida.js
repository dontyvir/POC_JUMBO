function submit_direccion() {
	var formulario = document.form1;
	if (formulario.loc_id.value == "") {
		alert("Para poder ingresar debes agregar una direcci�n de despacho");
		return;
	}
	formulario.submit();
}