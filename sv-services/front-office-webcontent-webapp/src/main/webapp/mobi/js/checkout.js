function ValidarTJ(numero_tarjeta) {
 var cadena = numero_tarjeta.toString();
 var longitud = cadena.length;
 var cifra = null;
 var cifra_cad=null;
 var suma=0;
 for (var i=0; i < longitud; i+=2){
   cifra = parseInt(cadena.charAt(i))*2;
   if (cifra > 9){ 
     cifra_cad = cifra.toString();
     cifra = parseInt(cifra_cad.charAt(0)) + parseInt(cifra_cad.charAt(1));
   }
   suma+=cifra;
 }
 for (var i=1; i < longitud; i+=2){
   suma += parseInt(cadena.charAt(i));
 }
 if ((suma % 10) == 0){ 
    return true;
 } else {
    return false;
 }
}
function ValidarTP(numero_tarjeta) {
  largo_tar=numero_tarjeta.length;
  digito_tar=numero_tarjeta.charAt(largo_tar-1);
  Tarjeta=numero_tarjeta.substring(0, largo_tar-1);
  largo_tar=Tarjeta.length;
  var suma=0;
  var mult=2;
  if (digito_tar=="K")
    digito_tar="10";
  for (it=largo_tar-1;it>=0;it--){
   	c=Tarjeta.charAt(it);
    suma+=parseInt(c,10)*mult;
    mult++;
    if (mult>7)
  	  mult=2;
  }
  var calculado=11-suma%11;
  if (calculado==11 || calculado==10)
    calculado=0;
  if(parseInt(digito_tar)!=calculado)
  	return false;
  else
  	return true;
}
function validaTipoParis(cadena,evento){
  var longitud = cadena.length;
  if (evento == "onkeypress"){
  	if (longitud < 15) {
  		return "NO";
  	} else {	
    	if ((cadena.substring(2,3) == "1") || (cadena.substring(2,3) == "2") || (cadena.substring(2,3) == "4")) { //Tipo de tarjeta
      		if ((cadena.substring(13,14) == "0") || (cadena.substring(13,14) == "1")) //titular o adicional
        		return "PARIS";
        }		
    	if (cadena.substring(0,8) == "61528031") { //BIN Tarjeta Mas Paris 
      		return "MASPARIS";
    	}
    }	
  } else {
	if (longitud < 16) {
  		return "NO";
  	} else {	
    	if ((cadena.substring(2,3) == "1") || (cadena.substring(2,3) == "2") || (cadena.substring(2,3) == "4")) { //Tipo de tarjeta
      		if ((cadena.substring(13,14) == "0") || (cadena.substring(13,14) == "1")) //titular o adicional
        		return "PARIS";
        }		
    	if (cadena.substring(0,8) == "61528031") { //BIN Tarjeta Mas Paris 
      		return "MASPARIS";
    	}
    }
  }  	
  return "NO"; 
}  
function valida_cuenta() {
	var formulario = document.frmPedido;
	formulario.flag_valcta.value = 0;
	var url = "";
	empleado = formulario.empleado.value;
	total = formulario.total_compra.value;
	url = "ValidaCuentaCheck?clave=" + formulario.pe_clave.value + "&cuotas=" + formulario.pe_cuotas.options[formulario.pe_cuotas.selectedIndex].value + "&total=" + total;
	exec_AJAXRPC('GET', url, 'validaparischeckout');
}
function validaparischeckout(text){
	var form = document.frmPedido;
	if (text.substring(0,1) == '0'){
		form.flag_valcta.value="0";
		form.cod_valcta.value=text.substring(2,3);
		if (form.cod_valcta.value == 6) {
			alert("Datos Incorrectos, vuelva a ingresar su clave");
			return;
		} else {
			form.num_cuotas.value = "" + form.pe_cuotas.options[form.pe_cuotas.selectedIndex].value;
			form.num_tja.value = form.p_numtja.value;
			if (form.tipo_documento[1].checked == true){//VALIDACIONES PARA EL CASO SELECCION DE FACTURA
				if (!form.fac_rut.value){
					alert('Debes ingresar el campo RUT');
					form.fac_rut.focus();
					return ;
				}
				if (!form.fac_dv.value){
					alert('Debes ingresar el campo dígito verificador');
					form.fac_dv.focus();
					return ;
				}
	    		if(!checkRutField( form.fac_rut.value+"-"+form.fac_dv.value, form.fac_rut ) ) {
					return ;
				}
				if (Trim(form.fac_razon.value) == ""){
					alert("Debes ingresar el campo razón social");
					form.fac_razon.focus();
					return;
				}
				if (Trim(form.fac_giro.value) == ""){
					alert("Debes ingresar el campo giro");
					form.fac_giro.focus();
					return;
				}
				if (Trim(form.fac_direccion.value) == ""){
					alert("Debes ingresar el campo dirección");
					form.fac_direccion.focus();
					return;
				}
				if (form.fac_fono.value == ""){
					alert("Debes ingresar el campo teléfono");
					form.fac_fono.focus();
					return;
				}
				if (Trim(form.fac_comuna.value) == ""){
					alert("Debes ingresar el campo Comuna");
					form.fac_comuna.focus();
					return;
				}
				if (Trim(form.fac_ciudad.value) == ""){
					alert("Debes ingresar el campo Ciudad");
					form.fac_ciudad.focus();
					return;
				}
			}	
			document.frmPedido.submit();
			document.frmPedido.action = "OrderDisplay";
		}	
	} else {
		document.frmPedido.flag_valcta.value="1";
		document.frmPedido.cod_valcta.value=text.substring(2,3);
		if ((form.cod_valcta.value == 2) || (form.cod_valcta.value == 7)) {  
			alert("No es posible validar sus datos en este momento, por favor intentelo nuevamente");
			return;
		}
		form.num_cuotas.value = "" + form.pe_cuotas.options[form.pe_cuotas.selectedIndex].value;  
		form.num_tja.value = form.p_numtja.value;
		if (form.tipo_documento[1].checked == true){//VALIDACIONES PARA EL CASO SELECCION DE FACTURA
			if (!form.fac_rut.value){
				alert('Debes ingresar el campo RUT');
				form.fac_rut.focus();
				return ;
			}
			if (!form.fac_dv.value){
				alert('Debes ingresar el campo dígito verificador');
				form.fac_dv.focus();
				return ;
			}
	    	if(!checkRutField( form.fac_rut.value+"-"+form.fac_dv.value, form.fac_rut ) ) {
				return ;
			}
			if (Trim(form.fac_razon.value) == ""){
				alert("Debes ingresar el campo razón social");
				form.fac_razon.focus();
				return;
			}
			if (Trim(form.fac_giro.value) == ""){
				alert("Debes ingresar el campo giro");
				form.fac_giro.focus();
				return;
			}
			if (Trim(form.fac_direccion.value) == ""){
				alert("Debes ingresar el campo dirección");
				form.fac_direccion.focus();
				return;
			}
			if (form.fac_fono.value == ""){
				alert("Debes ingresar el campo teléfono");
				form.fac_fono.focus();
				return;
			}
			if (Trim(form.fac_comuna.value) == ""){
				alert("Debes ingresar el campo Comuna");
				form.fac_comuna.focus();
				return;
			}
			if (Trim(form.fac_ciudad.value) == ""){
				alert("Debes ingresar el campo Ciudad");
				form.fac_ciudad.focus();
				return;
			}
		}
		document.frmPedido.submit();
		document.frmPedido.action = "OrderDisplay";
	}
}

function validaMedioParis(){
	var form = document.frmPedido;
	if (form.empleado.value == 1){ //si es empleado
		if (form.pe_clave.value == "") {
			alert("Debe ingresar la clave de su tarjeta paris");
			form.pe_clave.focus();
			return;
		}
		form.num_cuotas.value = "" + form.pe_cuotas.options[form.pe_cuotas.selectedIndex].value;
		//ejecutar valida cuenta con clave
		valida_cuenta();
	} else { //si no es empleado paris
		if ((form.p_numtja.value == "") || (form.p_numtja.value.lenght < 16)) {
			alert("Debe ingresar el número de la tarjeta paris");
			form.p_numtja.focus();
			return;
		}
		if (validaTipoParis(form.p_numtja.value,'onblur') == "PARIS") { //si es tarjeta paris normal
			if ((form.titular[0].checked == false) && (form.titular[1].checked == false)) {
				alert("Debe indicar si su tarjeta es titular o adicional");
				return;
			}
			if (form.titular[1].checked) { //si es un adicional
				if (form.p_numtja.value.substring(13,14) == "0") {
					alert("La cuenta no corresponde a un adicional");
					return;
				}
				if (form.rut_titular.value == "") {
					alert("Debe ingresar el rut del titular de la tarjeta");
					return;
				}
				if (form.rut_titular.value == "") {
					alert("Debe ingresar el dígito verificador del rut titular de la tarjeta");
					return;
				}
				if(!checkRutField( form.rut_titular.value+"-" + form.dv_titular.value, form.rut_titular) ) {
       	 			form.rut_titular.focus();
			        return;
			    }
			    if (parseInt(form.rut_titular.value,10) != parseInt(form.p_numtja.value.substring(3,12),10)){
					alert("La tarjeta ingresada no corresponde al titular");
					return;
			    }
			    if ((form.dv_titular.value != 'K') && (form.dv_titular.value != 'k')) {
			    	if (form.dv_titular.value != form.p_numtja.value.substring(12,13)){
						alert("La tarjeta ingresada no corresponde al titular");
						return;
			    	}
			    } else {
			    	if (form.p_numtja.value.substring(12,13) != '0'){
						alert("La tarjeta ingresada no corresponde al titular");
						return;
			    	}
			    }
			    if (form.nom_titular.value == "") {
					alert("Debe ingresar el nombre del titular de la tarjeta");
					return;
				}
				if (form.pat_titular.value == "") {
					alert("Debe ingresar el apellido paterno del titular de la tarjeta");
					return;
				}
				if (form.mat_titular.value == "") {
					alert("Debe ingresar el apellido materno del titular de la tarjeta");
					return;
				}
				if (form.dir_titular.value == "") {
					alert("Debe ingresar la dirección del titular de la tarjeta");
					return;
				}
				if (form.num_titular.value == "") {
					alert("Debe ingresar la dirección del titular de la tarjeta");
					return;
				}
			} else { //si es titular
				if (parseInt(form.rut_cli.value,10) != parseInt(form.p_numtja.value.substring(3,12),10)){
					alert("La tarjeta ingresada no corresponde al titular");
					return;
			    }
				if ((form.dv_cli.value != 'K') && (form.dv_cli.value != 'k')) {
					if (form.dv_cli.value != form.p_numtja.value.substring(12,13)){
						alert("La tarjeta ingresada no corresponde al titular");
						return;
			    	}
			    } else {
			    	if (form.p_numtja.value.substring(12,13) != '0'){
						alert("La tarjeta ingresada no corresponde al titular");
						return;
			    	}	
				}
			    if (form.p_numtja.value.substring(13,14) == "1") {
					alert("La cuenta no corresponde a un titular");
					return;
				}
			}
			if( ValidarTP(form.p_numtja.value.substring(2)) == false ) {
	        	alert( "Debes ingresar una tarjeta Paris válida." );
	   	  	    return;
		    }
			form.num_cuotas.value = "" + form.p_cuotas.options[form.p_cuotas.selectedIndex].value;  
		} else if (validaTipoParis(form.p_numtja.value,'onblur') == "MASPARIS") { //si es una tarjeta mas paris
			// Digito verificador mod 10
       		if( ValidarTJ(form.p_numtja.value) == false ) {
	            alert( "Debes ingresar una tarjeta Mas Paris válida." );
        	    return;
	        }
	        form.num_cuotas.value = "" + form.mp_cuotas.options[form.mp_cuotas.selectedIndex].value;   
		} else {
			alert("Por favor, ingrese un número de tarjeta válido");
			return;
		}	
		form.num_tja.value = form.p_numtja.value;
		if (form.tipo_documento[1].checked == true){//VALIDACIONES PARA EL CASO SELECCION DE FACTURA
			if (!form.fac_rut.value){
				alert('Debes ingresar el campo RUT');
				form.fac_rut.focus();
				return ;
			}
			if (!form.fac_dv.value){
				alert('Debes ingresar el campo dígito verificador');
				form.fac_dv.focus();
				return ;
			}
		    if(!checkRutField( form.fac_rut.value+"-"+form.fac_dv.value, form.fac_rut ) ) {
				return ;
			}
			if (Trim(form.fac_razon.value) == ""){
				alert("Debes ingresar el campo razón social");
				form.fac_razon.focus();
				return;
			}
			if (Trim(form.fac_giro.value) == ""){
				alert("Debes ingresar el campo giro");
				form.fac_giro.focus();
				return;
			}
			if (Trim(form.fac_direccion.value) == ""){
				alert("Debes ingresar el campo dirección");
				form.fac_direccion.focus();
				return;
			}
			if (form.fac_fono.value == ""){
				alert("Debes ingresar el campo teléfono");
				form.fac_fono.focus();
				return;
			}
			if (Trim(form.fac_comuna.value) == ""){
				alert("Debes ingresar el campo Comuna");
				form.fac_comuna.focus();
				return;
			}
			if (Trim(form.fac_ciudad.value) == ""){
				alert("Debes ingresar el campo Ciudad");
				form.fac_ciudad.focus();
				return;
			}
		}	
		document.frmPedido.submit();
		document.frmPedido.action = "OrderDisplay";
	}	
}
/*	
function orderprocess() {
	var form = document.frmPedido;
	if (form.total_compra.value == 0) {
		alert("Debes agregar productos al carro de compras");
		return;
	}
    var jpic = form.jpicking.value;
    var jpre = form.jprecio.value;
    var jdes = form.jdespacho.value;
    var jfec = form.jfecha.value;
    var tdes = form.tipo_despacho.value;
    var zdes = form.zona_despacho.value;
    var horsdes = form.horas_economico.value;
    if( jpic == "" || jpre == "" || jdes == "" ) {
        alert( "Debes seleccionar un horario de despacho." );
        return;
    }
	if (form.sin_gente_op[0].checked == true && form.sin_gente_txt.value == ''){
		alert("Indicanos con quién dejar el pedido");
		form.sin_gente_txt.focus();
		return;
	}
	var reg_sel = form.forma_pago.options[form.forma_pago.selectedIndex].value;
	if (reg_sel == 2) { // tarjeta Paris
		validaMedioParis();
	} else {
		// Tarjeta jumbo mas
		if( reg_sel == 1 ) {
			form.num_tja.value = "" + form.j_numtja1.value + form.j_numtja2.value + form.j_numtja3.value + form.j_numtja4.value;
			form.num_cuotas.value = "" + form.j_cuotas.options[form.j_cuotas.selectedIndex].value;
		} else if (reg_sel == 3) {
			form.num_tja.value = "" + form.j_numtja1.value + form.j_numtja2.value + form.j_numtja3.value + form.j_numtja4.value;
			form.num_cuotas.value = "" + form.j_cuotas.options[form.j_cuotas.selectedIndex].value;
		} else if( reg_sel == 4 ) {
			form.num_tja.value = "" + form.t_numero.value;
			form.num_cuotas.value = "" + form.t_cuotas.options[form.t_cuotas.selectedIndex].value;
		} else if( reg_sel == 5 ) {
			form.num_tja.value = "" + form.mpaj_numtja1.value + form.mpaj_numtja2.value + form.mpaj_numtja3.value + form.mpaj_numtja4.value;
			form.num_cuotas.value = "" + form.mpaj_cuotas.value;
		} else {
			alert( "Debes seleccionar una forma de pago válida." );
			return;
		}
		if ( reg_sel == 1){//VALIDACIONES PARA EL CASO DE SELECCION DE TARJETA JUMBO MAS
			if ( form.j_numtja1.value == '' || form.j_numtja2.value == '' || form.j_numtja3.value == '' || form.j_numtja4.value == '' ){
				alert("Debes llenar todos los campos de la tarjeta");
				return;
			}
        	// largo tarjeta puede ser de 16 numeros
	        var num_tja = form.j_numtja1.value + form.j_numtja2.value + form.j_numtja3.value + form.j_numtja4.value;
    	    if( num_tja.length != 16 ) {
        	    alert( "Debes ingresar una tarjeta válida." );
            	//form.j_numtja1.focus();
	            return;
    	    }
        	// Bean utilizado 615290
	        if( num_tja.substring(0,6) != '615290' ) {
    	        alert( "Debes ingresar una tarjeta válida." );
        	    //form.j_numtja1.focus();
            	return;
	        }
        	// Digito verificador mod 10
	        if( ValidarTJ( num_tja ) == false ) {
    	        alert( "Debes ingresar una tarjeta válida." );
        	    //form.j_numtja1.focus();
            	return;
	        }
		}	
		if ( reg_sel == 3){//VALIDACIONES PARA EL CASO DE SELECCION DE TARJETA EASY MAS
			if ( form.j_numtja1.value == '' || form.j_numtja2.value == '' || form.j_numtja3.value == '' || form.j_numtja4.value == '' ){
				alert("Debes llenar todos los campos de la tarjeta");
				return;
			}
        	// largo tarjeta puede ser de 16 numeros
	        var num_tja = form.j_numtja1.value + form.j_numtja2.value + form.j_numtja3.value + form.j_numtja4.value;
    	    if( num_tja.length != 16 ) {
        	    alert( "Debes ingresar una tarjeta válida." );
            	//form.j_numtja1.focus();
	            return;
    	    }
        	// Bean utilizado 61529025
	        if( num_tja.substring(0,8) != '61529025' ) {
    	        alert( "Debes ingresar una tarjeta válida." );
        	    //form.j_numtja1.focus();
            	return;
	        }
        	// Digito verificador mod 10
	        if( ValidarTJ( num_tja ) == false ) {
    	        alert( "Debes ingresar una tarjeta válida." );
        	    //form.j_numtja1.focus();
            	return;
	        }
		}	
		if ( reg_sel == 4){//VALIDACIONES PARA LA SELECCION TARJETA BANCARIA
			if (  form.nom_tban[0].checked == false && form.nom_tban[1].checked == false && form.nom_tban[2].checked == false && form.nom_tban[3].checked == false ){
				alert("Debes elegir un tipo de Tarjeta Bancaria");
				return;
			}
			if ( form.t_numero.value == '' ){
				alert("Debes ingresar el campo Número de Tarjeta ");
				//form.t_numero.focus();
				return;
			} 
	        // largo tarjeta puede ser entre [11,19] numeros
    	    if( form.t_numero.value.length < 11 || form.t_numero.value.length > 19 ) {
        	    alert( "Debes ingresar una tarjeta válida." );
	            //form.t_numero.focus();
	            return;
   	        }
        	// Si es visa
	        if( form.nom_tban[0].checked == true ) {
    	        // Bean utilizado 4
        	    if( form.t_numero.value.substring(0,1) != '4' ) {
            	    alert( "Debes ingresar una tarjeta válida." );
                	//form.t_numero.focus();
	                return;
    	        }
        	    // Validacion digito verificador modulo 10
            	if( ValidarTJ( form.t_numero.value ) == false ) {
                	alert( "Debes ingresar una tarjeta válida." );
	                //form.t_numero.focus();
    	            return;
        	    }
	        }
        	// Si es dinners
	        if( form.nom_tban[1].checked == true ) {
    	        // Bean utilizados
        	    if( form.t_numero.value.substring(0,2) != '30' 
            	    && form.t_numero.value.substring(0,2) != '36'
                	&& form.t_numero.value.substring(0,2) != '38' ) {
	                alert( "Debes ingresar una tarjeta válida." );
    	            //form.t_numero.focus();
	                return;
    	        }
        	    // Validacion digito verificador modulo 10
	            if( ValidarTJ( form.t_numero.value ) == false ) {
    	            alert( "Debes ingresar una tarjeta válida." );
        	        //form.t_numero.focus();
	                return;
    	        }
        	}
        	// Si es mastercard
	        if( form.nom_tban[2].checked == true ) {
    	        // Bean utilizados
        	    if( form.t_numero.value.substring(0,2) != '51' 
            	    && form.t_numero.value.substring(0,2) != '52'
	                && form.t_numero.value.substring(0,2) != '53'
    	            && form.t_numero.value.substring(0,2) != '54' 
	                && form.t_numero.value.substring(0,2) != '55' ) {
	                alert( "Debes ingresar una tarjeta válida." );
    	            //form.t_numero.focus();
        	        return;
	            }
            	// Validacion digito verificador modulo 10
	            if( ValidarTJ( form.t_numero.value ) == false ) {
    	            alert( "Debes ingresar una tarjeta válida." );
        	        //form.t_numero.focus();
            	    return;
	            }
	        }
        	// Si es american express
	        if( form.nom_tban[3].checked == true ) {
    	        // Validacion digito verificador modulo 10
        	    var numero;
            	if (form.t_numero.value.length == 14)
	            	numero = '00' + form.t_numero.value;
    	        else if (form.t_numero.value.length == 15)  
	            	numero = '0' + form.t_numero.value;
    	        else
        	    	numero = form.t_numero.value;	
	            if( ValidarTJ(numero) == false ) {
    	            alert( "Debes ingresar una tarjeta válida." );
	                //form.t_numero.focus();
    	            return;
        	    }
	        }
			if ( form.t_mes.value == ''){
				alert("Debes seleccionar el mes de expiración");
				//form.t_mes.focus();
				return;
			}
			if ( form.t_ano.value == ''){
				alert("Debes seleccionar el año de expiración");
				//form.t_ano.focus();
				return;
			}
        	// Fecha de expiracion debe ser igual a superior al ano/mes en curso
	        var fecha_exp=new Date();
    	    fecha_exp.setFullYear("20"+form.t_ano.value,form.t_mes.value-1,1);
	        var fecha_hoy=new Date();
    	    fecha_hoy.setFullYear(fecha_hoy.getFullYear(),fecha_hoy.getMonth(),1);
	        if( fecha_exp < fecha_hoy ) {
    	        alert( "Debes ingresar una fecha válida para tu tarjeta." );
        	    //form.j_numtja1.focus();
            	return;
	        }
			if ( Trim(form.t_banco.value) == '' ){
				alert("Debes ingresar el campo Nombre del Banco");
				//form.t_banco.focus();
				return;
			}
		}
		if ( reg_sel == 5){//VALIDACIONES PARA EL CASO DE PAGO ALTERNATIVO
			if (Trim(form.ped_obs.value) == ''){
				alert("Debes ingresar el campo Observación");
				//form.t_banco.focus();
				return;
			}else{
				if (form.ped_obs.value.length >= 250){
					form.ped_obs.value = form.ped_obs.value.substring(0, 250-1);
				}
			}
		}
		if (form.tipo_documento[1].checked == true){//VALIDACIONES PARA EL CASO SELECCION DE FACTURA
			if (!form.fac_rut.value){
				alert('Debes ingresar el campo RUT');
				form.fac_rut.focus();
				return ;
			}
			if (!form.fac_dv.value){
				alert('Debes ingresar el campo dígito verificador');
				form.fac_dv.focus();
				return ;
			}
		    if(!checkRutField( form.fac_rut.value+"-"+form.fac_dv.value, form.fac_rut ) ) {
				return ;
			}
			if (Trim(form.fac_razon.value) == ""){
				alert("Debes ingresar el campo razón social");
				form.fac_razon.focus();
				return;
			}
			if (Trim(form.fac_giro.value) == ""){
				alert("Debes ingresar el campo giro");
				form.fac_giro.focus();
				return;
			}
			if (Trim(form.fac_direccion.value) == ""){
				alert("Debes ingresar el campo dirección");
				form.fac_direccion.focus();
				return;
			}
			if (form.fac_fono.value == ""){
				alert("Debes ingresar el campo teléfono");
				form.fac_fono.focus();
				return;
			}
			if (Trim(form.fac_comuna.value) == ""){
				alert("Debes ingresar el campo Comuna");
				form.fac_comuna.focus();
				return;
			}
			if (Trim(form.fac_ciudad.value) == ""){
				alert("Debes ingresar el campo Ciudad");
				form.fac_ciudad.focus();
				return;
			}
		}	
		document.frmPedido.submit();
		document.getElementById("confirmar_link").href="javascript:;";
		document.frmPedido.action = "OrderDisplayMobi";
	}	
}
*/
function datosFactura(estado) {
  $('div_factura').style.display = estado;
}
function ordenar_productos() {
    var form = document.ordenar;
    reg_sel = form.modo.options[form.modo.selectedIndex].value;
    top.frm_prod.location.href="CheckoutProList?modo="+reg_sel;
}
function showCalendario() {
	var fcSel = "";
	var seleccionado = "";
	var cant_prod = "10";
	var params = {};
	if ( $('fecha_seleccionada') != null ) {
		fcSel = $('fecha_seleccionada').value;
	}
	if ( $('radio_seleccionado') != null ) {
		seleccionado = $('radio_seleccionado').value;
	}
	if  ($('cant_prod') != null ) {
		if ( $('cant_prod').value != '' ) {
			cant_prod = $('cant_prod').value;
		}
	}
	params = {"cant_prod": cant_prod, "fecha_seleccionada": fcSel, "jpicking":seleccionado};
	var requestOptions = {
				'method': 'post',
				'parameters': params,
				'onSuccess': function (REQUEST) {
					$('frm_despacho').innerHTML = REQUEST.responseText;
				}
			};
	new Ajax.Request('CalendarioDespacho', requestOptions);
}
function send_despacho( desp, precio, pick, fecha, tipo, seleccionado ) {
	var precio_aux = precio.replace("$","").replace(".","");
	if (tipo != 'C') {
		$('jpicking').value = pick ;
	    $('horas_economico').value = "";
	    $('jdespacho').value = desp;
    } else {
    	$('jpicking').value = '0';
    	$('horas_economico').value = pick;
    	$('jdespacho').value = fecha;
    }
    $('jprecio').value = precio_aux;
    $('jfecha').value = fecha;
    $('tipo_despacho').value = tipo;
	$('radio_seleccionado').value = seleccionado;	
}
var flag_act = false;
function showActualizaDatosLayer() {
	var frame = document.getElementById('i_act_datos');
	frame.style.height = '190px';
	if (flag_act)
		frame.src = '/FO_WebContent/layers/act_mail.html';
	var layerOlvidaClave = document.getElementById('act_datos');
	layerOlvidaClave.style.visibility = 'visible';
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	layerOlvidaClave.style.width = '350px';
	layerOlvidaClave.style.height = '190px';
	layerOlvidaClave.style.left = (ancho-350)/2 + 'px';
	layerOlvidaClave.style.top = (largo)/2 - 190 + 'px';
	flag_act = true;
}
function hideActualizaDatosveLayer() {
	var layerOlvidaClave = document.getElementById('act_datos');
	layerOlvidaClave.style.visibility = 'hidden';	
	layerOlvidaClave.style.width = '0px';
	layerOlvidaClave.style.height = '0px';
}