function ValidaCliente() {
	var formulario = document.cliente;	
	if (!formulario.rut.value){
		alert('Debes ingresar el campo RUT');
		formulario.rut.focus();
		return false;
	}
	if (!formulario.dv.value){
		alert('Debes ingresar el número que está después del guión en tu Rut');
		formulario.dv.focus();
		return false;
	}
    if(!checkRutField( formulario.rut.value+"-"+formulario.dv.value, formulario.rut ) ) {
		formulario.rut.focus();
		return false;
	}
	if (Trim(formulario.nombre.value) == ""){
		alert('Debes ingresar el campo Nombre');
		formulario.nombre.focus();
		return false;
	}
	if (Trim(formulario.ape_pat.value) == ""){
		alert('Debes ingresar campo Apellido Paterno');
		formulario.ape_pat.focus();
		return false;
	}
	if (Trim(formulario.ape_mat.value) == ""){
		alert('Debes ingresar el campo Apellido Materno');
		formulario.ape_mat.focus();
		return false;
	}
	if(!validaFechas()){
		return false;
	}
	if (formulario.genero[0].checked == false && formulario.genero[1].checked == false){
		alert("Debes selecionar campo Sexo");
		return false;
	}
	if ((!formulario.email1.value) || (!formulario.dominio1.value)){
		alert('Debes ingresar el campo e-mail');
		formulario.email1.focus();
		return false;
	}
	if(!validarEmailDominio(formulario.email1,formulario.dominio1)){
		return false;
	}	
	if ((!formulario.email2.value) || (!formulario.dominio2.value)){
		alert('Por favor ingresar tu dirección de email en el campo Confirmar E-mail');
		formulario.email2.focus();
		return false;
	}
	if(!validarEmailDominio(formulario.email2,formulario.dominio2)){
		return false;
	}	
	if ((formulario.email1.value != formulario.email2.value) || (formulario.dominio1.value != formulario.dominio2.value)) {
		alert("El e-mail y su confirmación deben ser iguales");
		formulario.email1.focus();
		return false;
	}
	if (!formulario.fon_num_1.value){
		alert('Debes ingresar el campo Telefono1');
		formulario.fon_num_1.focus();
		return false;
	}
    if( formulario.action.indexOf("FonoCliRegisterNew") < 0 ) {
    	if (formulario.clave_ant != undefined) {
    		if(formulario.clave_ant.value.length > 0 || formulario.clave.value.length > 0 || formulario.clave1.value.length > 0){
	    		if (!formulario.clave_ant.value) {
			        alert('Debes ingresar el campo Clave Anterior');
			        formulario.clave_ant.focus();
			        return false;
				}
		        if (!formulario.clave.value) {
		            alert('Debes ingresar el campo Clave');
		            formulario.clave.focus();
		            return false;
		        }
		        if (!formulario.clave1.value) {
		            alert('Debes ingresar el campo Repetir Clave');
		            formulario.clave1.focus();
		            return false;
	        	}
		        if((formulario.clave.value.length < 6 ) || (formulario.clave1.value.length < 6 )){
		            alert("La clave debe contener al menos 6 caracteres.");
		            formulario.clave.focus();
		            return false;
		        }
    		}
    	} else {
	        if (!formulario.clave.value){
	            alert('Debes ingresar el campo Clave');
	            formulario.clave.focus();
	            return false;
	        }
	        if (!formulario.clave1.value) {
	            alert('Debes ingresar el campo Repetir Clave');
	            formulario.clave1.focus();
	            return false;
        	}
	        if((formulario.clave.value.length < 6 ) || (formulario.clave1.value.length < 6 )) {
	            alert("La clave debe contener al menos 6 caracteres.");
	            formulario.clave.focus();
	            return false;
	        }
		}
        if (formulario.clave.value != formulario.clave1.value) {
            alert("La clave y su confirmación deben ser iguales");
			formulario.clave.focus();
            return false;
        }
        if(!formulario.pregunta.value || Trim(formulario.pregunta.value) == ""){
            alert('Debes ingresar la pregunta secreta');
            formulario.pregunta.focus();
            return false;
        }
        if(!formulario.respuesta.value || Trim(formulario.respuesta.value) == ""){
            alert('Debes ingresar la respuesta');
            formulario.respuesta.focus();
            return false;
        }
        if((formulario.action.indexOf("RegisterNew") >= 0) || (formulario.action.indexOf("FonoCliRegisterNew") >= 0)) {
        	if(!formulario.terminos.checked){
            	alert('Debes aceptar los términos y condiciones del servicio para poder proceder con el registro');
	            return false;
	        }    
        }
    }
	return true;
}

function validaFechas() {
	var formulario = document.cliente;
	dia = formulario.dia.options[formulario.dia.selectedIndex].value;
	mes = formulario.mes.options[formulario.mes.selectedIndex].value;
	ano = formulario.ano.options[formulario.ano.selectedIndex].value;
	if( dia == "" ) {
		alert("Debes seleccionar el campo día de tu Fecha de nacimiento");
		formulario.dia.focus();
		return false;
	} else if( mes == "" ) {
		alert("Debes seleccionar el campo mes de tu Fecha de nacimiento");
		formulario.mes.focus();
		return false;
	} else if( ano == "" ) {
		alert("Debes seleccionar el campo año de tu Fecha de nacimiento");
		formulario.ano.focus();
		return false;
	}
	return true;
}

function validarEmail(valor) {
	validRegExp = /^[^@]+@[^@]+.[a-z]{2,}$/i;
	strEmail = valor.value;
	if (strEmail.search(validRegExp) == -1) {
	    alert("La dirección de email es incorrecta o está mal escrita. Ingrésala nuevamente");
	    valor.focus();
	    return (false);
	}else
		return (true);
}

function validarEmailDominio(valor,dominio) {
	validRegExp = /^[^@]+@[^@]+.[a-z]{2,}$/i;
	strEmail = valor.value + '@' + dominio.value;
	
	if (strEmail.search(validRegExp) == -1) {
	    alert("La dirección de email es incorrecta o está mal escrita. Ingrésala nuevamente");
	    valor.focus();
	    return (false);
	}else
		return (true);
} 
function validaDireccion( formulario ) {
	if (Trim(formulario.alias.value) == '') {
		alert("Debes ingresar campo nombre referencial.");
		formulario.alias.focus();
		return false;
	}
	if(Trim(formulario.tipo_calle.value) == ''){
		alert("Debes seleccionar un tipo de calle");
		formulario.tipo_calle.focus();
		return false;
	}
	if(Trim(formulario.calle.value) == ''){
		alert("Debes ingresar campo dirección.");
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
	if(formulario.comentario.value.length > 250){
		alert('Has excedido el limite máximo de caracteres');
		formulario.comentario.value = formulario.comentario.value.substring(0, 256); 
		formulario.comentario.focus();
		return false;
	}
	return true;
}