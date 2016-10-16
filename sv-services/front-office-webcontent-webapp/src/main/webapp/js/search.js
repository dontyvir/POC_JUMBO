

function buscar(){
	str   = Trim(document.f_search.patron.value);
	largo = str.length;

	if(largo <= 1){
		alert("Ingresa los productos que quieres buscar");
		document.f_search.patron.focus();
		return;
	}
	
	if (document.f_search.patron.value == ""){
		alert("Ingresa los productos que quieres buscar");
		document.f_search.patron.focus();
		return;
	}
	
    if((document.f_search.patron.value.length < 2 )){
        alert("El patrón debe contener al menos 2 caracteres.");
        document.f_search.patron.focus();
        return ;
    }	
    
	document.f_search.submit();
}

function buscar_cat(valor){
	location = "SearchItemDisplay?lu=ca&patron="+valor;
}

function buscar_avanzada(){
	str   = Trim(document.frm_Avanzada.patron.value);
	largo = str.length;

	if(largo <= 1){
		alert("Ingresa los productos que quieres buscar");
		document.frm_Avanzada.patron.focus();
		return;
	}


	if (document.frm_Avanzada.patron.value.substring(0,3) == 'ej:'){
		alert("Ingresa los productos que quieres buscar");
		document.frm_Avanzada.patron.value = "";
		return;
	}

	if (document.frm_Avanzada.patron.value == ""){
		alert("Ingresa los productos que quieres buscar");
		document.frm_Avanzada.patron.focus();
		return;
	}

	if (document.frm_Avanzada.patron.value.length < 2){
		alert("El patrón debe contener al menos 2 caracteres.");
		document.frm_Avanzada.patron.focus();
		return;
	}
	
	document.frm_Avanzada.action = "SearchItemDisplay";
	
	document.frm_Avanzada.submit();
}

function limpiaBusqueda(){
	var f1 = document.frm_Avanzada;
	if (f1.patron.value.substring(0,3) == 'ej:') {
		f1.patron.value = '';
	}
}
