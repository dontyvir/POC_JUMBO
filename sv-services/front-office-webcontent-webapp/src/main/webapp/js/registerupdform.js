
function submit_cliente() {
	var formulario = document.cliente;
	if( ValidaModificar() == false ) {
		return;
	}
	var url = "RegisterUpdate?";
    var i = 0;
    var txt = "";
    for( i = 0; i < formulario.elements.length; i++ ) {
    	if ( formulario.elements[i].name != "genero" && formulario.elements[i].name != "sms" ){
    		if (formulario.elements[i].type == "checkbox"){
    			if (formulario.elements[i].checked){
	    			url += formulario.elements[i].name+"="+formulario.elements[i].value + "&";
	    		}
	    	}
    		else{
			    url += formulario.elements[i].name+"="+formulario.elements[i].value + "&";
			} 
		}
    }
	if( formulario.sms.checked == true )
	    url += "sms=1&";
	else
		url += "sms=0&";
    if( formulario.genero[0].checked == true )
	    url += "genero=F";
	else
		url += "genero=M";

	exec_AJAXRPC('GET', url, 'proreg');
}

function proreg( texto ) {
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;

	if( texto == "1" ) {
		document.getElementById('Layer1').style.width = '450px';
		document.getElementById('Layer1').style.height = '240px';
		document.getElementById('Layer1').style.left = (ancho-450)/2 + 'px';
		document.getElementById('Layer1').style.top = (largo-400)/2 + ypos - 50 + 'px';
		MM_showHideLayers('Layer1','','show');
	}
	else if( texto == "2" ) {
		document.getElementById('Layer2').style.width = '450px';
		document.getElementById('Layer2').style.height = '240px';
		document.getElementById('Layer2').style.left = (ancho-450)/2 + 'px';
		document.getElementById('Layer2').style.top = (largo-400)/2 + ypos - 50 + 'px';
		MM_showHideLayers('Layer2','','show');
		return;
	}
	else {
		document.getElementById('Layer3').style.width = '450px';
		document.getElementById('Layer3').style.height = '240px';
		document.getElementById('Layer3').style.left = (ancho-450)/2 + 'px';
		document.getElementById('Layer3').style.top = (largo-400)/2 + ypos - 50 + 'px';
		MM_showHideLayers('Layer3','','show');
		return;
	}
		
	var formulario = document.cliente;		
	
	formulario.clave_ant.value = "";
	formulario.clave.value = "";
	formulario.clave1.value = "";
		
}

function carga_fechas() {
	var formulario = document.cliente;

	objeto = formulario.dia;
	cant = objeto.options.length;
	for( var i = 0; i < cant; i++ ) {
		if( objeto.options[i].value == formulario.auxdia.value ) {
			objeto.selectedIndex = i;
			break;
		}
	}	

	objeto = formulario.mes;
	cant = objeto.options.length;
	for( var i = 0; i < cant; i++ ) {
		if( objeto.options[i].value == formulario.auxmes.value ) {
			objeto.selectedIndex = i;
			break;
		}
	}	
	
	objeto = formulario.ano;
	cant = objeto.options.length;
	for( var i = 0; i < cant; i++ ) {
		if( objeto.options[i].value == formulario.auxano.value ) {
			objeto.selectedIndex = i;
			break;
		}
	}		

}

function ValidaModificar() {

	var formulario = document.cliente;

	//PARCHE para que no pregunte por la clave cuando el cliente no desea modificarla
	if(formulario.clave.value == "" && formulario.clave1.value == ""){
		formulario.clave_ant.value ="";
	}
	
    if( ValidaCliente() == false )
    	return false;

    if( formulario.clave_ant.value != "" && formulario.clave.value != "" && formulario.clave1.value != "" ) {

        if (!formulario.clave_ant.value){
            alert('Debes ingresar el campo Clave Anterior');
            formulario.clave_ant.focus();
            return false;
        }

        if (!formulario.clave.value){
            alert('Debes ingresar el campo Nueva Clave');
            formulario.clave.focus();
            return false;
        }
        
        if (!formulario.clave1.value){
            alert('Debes ingresar campo Repetir Nueva Clave');
            formulario.clave1.focus();
            return false;
        }

        if((formulario.clave_ant.value.length < 4 ) || (formulario.clave.value.length < 4 ) || (formulario.clave.value.length < 4 )){
            alert("La clave debe contener al menos 4 caracteres.");
            //formulario.clave.focus();
            return false;
        }

        if (formulario.clave.value != formulario.clave1.value){
            alert("La nueva clave y su confirmación deben ser iguales.");
            return false;
        }
    }
    
    for( i = 0; i < formulario.elements.length; i++ ) {
    	nom = formulario.elements[i].name;
    	if ( nom.indexOf("pre_fecha_") == 0){
    		sFec = formulario.elements[i].value;
    		if(!validarFecha(sFec)){
    			formulario.elements[i].focus();
    			return false;
    		}
    	}else if(nom.indexOf("pre_numero_") == 0) {
    		sNum = formulario.elements[i].value;
    		sNum = $j.trim(sNum);
    		num = parseInt(sNum);
    		if (sNum != "" && (isNaN(num) || num < 0) ){
    			formulario.elements[i].focus();
    			alert("Error en número: " + sNum);
    			return false;
    		}
    	}
    }
    
	return true;
}

//supuesto: sFec viene con formato dd/mm/aaaa
function validarFecha(sFec){
	if(sFec == "")
		return true;
	patron = /^\d{2}\/\d{2}\/\d{4}$/;
	if(!patron.test(sFec)){
		alert("Error en formato fecha: " + sFec);
		return false;
	}
	fa = sFec.split("/");
	sAgno = fa[2];
	sMes = fa[1];
	sDia = fa[0];
	
	if(sMes.indexOf("0") == 0)
		sMes = sMes.substring(1);
	if(sDia.indexOf("0") == 0)
		sDia = sDia.substring(1);
		
	agno = parseInt(sAgno);
	mes = parseInt(sMes) - 1;
	dia = parseInt(sDia);

	ff = new Date(agno, mes, dia);
	if(ff.getDate() == dia && ff.getMonth() == mes && ff.getFullYear() == agno){
		return true;
	}
	alert("Fecha no válida: " + sFec);
	return false;
}

function carga_codigos_fono() {
	var formulario = document.cliente;

	objeto = formulario.fon_cod_1;
	cant = objeto.options.length;
	for( var i = 0; i < cant; i++ ) {
		if( objeto.options[i].value == formulario.auxcodfon1.value ) {
			objeto.selectedIndex = i;
			break;
		}
	}	

	objeto = formulario.fon_cod_2;
	cant = objeto.options.length;
	for( var i = 0; i < cant; i++ ) {
		if( objeto.options[i].value == formulario.auxcodfon2.value ) {
			objeto.selectedIndex = i;
			break;
		}
	}	
}


