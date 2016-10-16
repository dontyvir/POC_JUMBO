
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

function  aceptarcotizacion(){
	var form = document.frmPedido;
	
	if (form.sin_gente_txt.value == ''){
		alert("Indicanos con quién dejar el pedido");
		form.sin_gente_txt.focus();
		return;
	}
/*
	var reg_sel = form.valor_medio_pago.value;
	if( reg_sel == 1 ) {// Tarjeta jumbo mas
		form.num_tja.value = "" + form.j_numtja1.value + form.j_numtja2.value + form.j_numtja3.value + form.j_numtja4.value;
		form.num_cuotas.value = "" + form.j_cuotas.options[form.j_cuotas.selectedIndex].value;
	}
	else if( reg_sel == 4 ) {// Tarjeta Bancaria
		form.num_tja.value = "" + form.t_numero.value;
		form.num_cuotas.value = "" + form.t_cuotas.options[form.t_cuotas.selectedIndex].value;
	} 
	else if( reg_sel == 5 ) {// Línea de crédito
		//form.num_tja.value = "" + form.mpaj_numtja1.value + form.mpaj_numtja2.value + form.mpaj_numtja3.value + form.mpaj_numtja4.value;
		//form.num_cuotas.value = "" + form.mpaj_cuotas.value;
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

	if ( reg_sel == 3){//VALIDACIONES PARA EL CASO DE PAGO ALTERNATIVO (Cheque)
		if (Trim(form.ped_obs.value) == ''){
			alert("Debes ingresar el campo Observación");
			//form.t_banco.focus();
			return;
		}else{
			if (form.ped_obs.value.length >= 2047){
				form.ped_obs.value = form.ped_obs.value.substring(0, 2048-1);
			}
		}
	}
	*/
	if( confirm("Esta seguro que desea aceptar la cotización.") ) {
		document.frmPedido.submit();
	}

}

