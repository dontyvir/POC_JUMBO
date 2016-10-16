//VENTA EMPRESA
function ValidaFormSolicitud() {
	var formulario = document.cliente;

	if (Trim(formulario.nombre_contacto.value) == ""){
		alert('Debes ingresar el campo nombre contacto');
		formulario.nombre_contacto.focus();
		return false;
	}

	if (Trim(formulario.cargo.value) == ""){
		alert('Debes ingresar el campo cargo');
		formulario.cargo.focus();
		return false;
	}

	if(!formulario.fono_contacto1.value){
		alert('Debes ingresar el campo fono contacto1');
		formulario.fono_contacto1.focus();
		return false;
	}

	if(!formulario.fono_contacto2.value){
		alert('Debes ingresar el campo fono contacto2');
		formulario.fono_contacto2.focus();
		return false;
	}

	/*
	if(!formulario.fono_contacto3.value){
		alert('Debes ingresar el campo fono contacto3');
		formulario.fono_contacto3.focus();
		return false;
	}
	*/

	if(!validarEmail(formulario.email)){
		return false;
	}	
	
	if (!formulario.email_c.value){
		alert('Por favor ingresar tu dirección de email en el campo Confirmar E-mail');
		formulario.email_c.focus();
		return false;
	}
	
	if(!validarEmail(formulario.email_c)){
		return false;
	}	
	
	if (formulario.email.value != formulario.email_c.value){
		alert("El e-mail y su confirmación deben ser iguales");
		return false;
	}

	if (!formulario.email.value){
		alert('Debes ingresar el campo e-mail');
		formulario.email.focus();
		return false;
	}

	if (!formulario.rutemp.value){
		alert('Debes ingresar el campo rut empresa');
		formulario.rutemp.focus();
		return false;
	}

	if (!formulario.dvemp.value){
		alert('Debes ingresar el número que está después del guión en el Rut');
		formulario.dvemp.focus();
		return false;
	}

    if(!checkRutField( formulario.rutemp.value+"-"+formulario.dvemp.value, formulario.rutemp ) ) {
		return false;
	}

	if (Trim(formulario.nombre_empresa.value) == ""){
		alert('Debes ingresar el campo Nombre empresa');
		formulario.nombre_empresa.focus();
		return false;
	}

	if (Trim(formulario.razon.value) == ""){
		alert('Debes ingresar el campo razon');
		formulario.razon.focus();
		return false;
	}

	if (Trim(formulario.rubro.value) == ""){
		alert('Debes ingresar el campo Giro');
		formulario.rubro.focus();
		return false;
	}

	if(formulario.ciudadtmp.options[formulario.ciudadtmp.selectedIndex].value == '0'){
		alert('Debes seleccionar una region');
		formulario.ciudadtmp.focus();
		return false;
	}

	if(formulario.comuna.options[formulario.comuna.selectedIndex].value == '0'){
		alert('Debes seleccionar una comuna');
		formulario.comuna.focus();
		return false;
	}

}

function validaDireccion( formulario ){

	if(Trim(formulario.lugar.value) == ''){
		alert("Debes ingresar campo lugar.");
		formulario.lugar.focus();
		return false;
	}

	if(Trim(formulario.tipo_calle.value) == ''){
		alert("Debes seleccionar un tipo de calle");
		formulario.tipo_calle.focus();
		return false;
	}

	if(Trim(formulario.calle.value) == ''){
		alert("Debes ingresar campo calle.");
		formulario.calle.focus();
		return false;
	}

	if(Trim(formulario.numero.value) == ''){
		alert("Debes ingresar campo Número.");
		formulario.numero.focus();
		return false;
	}
	
	if(formulario.region.value == 0){
		alert("Debes seleccionar una Región");
		formulario.region.focus();
		return false;
	}

	if(formulario.comuna.value == 0){
		alert("Debes seleccionar una Comuna");
		formulario.comuna.focus();
		return false;
	}

	if(formulario.comuna.value == -1){
		alert("Debes ingresar el nombre de la Comuna");
		formulario.nomComuna.focus();
		return false;
	}

	if(formulario.observacion.value.length > 250){
		alert('Has excedido el limite máximo de caracteres');
		formulario.observacion.value = formulario.observacion.value.substring(0, 256); 
		formulario.observacion.focus();
		return false;
	}
	return;
}




function validaFechas(){
	var formulario = document.cliente;

	dia = formulario.dia.options[formulario.dia.selectedIndex].value;
	mes = formulario.mes.options[formulario.mes.selectedIndex].value;
	ano = formulario.ano.options[formulario.ano.selectedIndex].value;
	
	if( dia == "" ) {
		alert("Debes seleccionar el campo día de tu Fecha de nacimiento");
		formulario.dia.focus();
		return false;
	}
	else if( mes == "" ) {
		alert("Debes seleccionar el campo mes de tu Fecha de nacimiento");
		formulario.mes.focus();
		return false;
	}
	else if( ano == "" ) {
		alert("Debes seleccionar el campo año de tu Fecha de nacimiento");
		formulario.ano.focus();
		return false;
	}	
	
	return true;
}

function validarEmail(valor){
	validRegExp = /^[^@]+@[^@]+.[a-z]{2,}$/i;
	strEmail = valor.value;
	
	if (strEmail.search(validRegExp) == -1) {
	    alert("La dirección de email es incorrecta o está mal escrita. Ingrésala nuevamente");
	    valor.focus();
	    return (false);
	}else
		return (true);
} 
 
