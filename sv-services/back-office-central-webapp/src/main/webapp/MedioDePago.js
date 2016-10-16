


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

function validar_form(form){

	var reg_sel = form.mpago.value;
    var num_tja = "", num_cuotas = "";
    //var num_mp_ant = form.num_mp_ant.value;
    var mpago_ant  = form.mpago_ant.value;	
    
    // Tarjeta jumbo mas
	if( reg_sel == "CAT" ) {
		num_tja    = "" + form.num_mp_jumbo_1.value + form.num_mp_jumbo_2.value + form.num_mp_jumbo_3.value + form.num_mp_jumbo_4.value;
		num_cuotas = "" + form.j_cuotas.value; //form.j_cuotas.options[form.j_cuotas.selectedIndex].value;
	}// Tarjetas Bancarias
	else if( reg_sel == "TBK" ) {
		num_tja    = "" + form.num_mp.value;
		num_cuotas = "" + form.t_cuotas.value;
	}
	else if( reg_sel == "PAR" ) {
		num_tja    = "" + form.num_mp_paris.value; // + form.num_mp_paris_2.value + form.num_mp_paris_3.value + form.num_mp_paris_4.value + form.num_mp_paris_5.value;
		num_cuotas = "" + form.cuotas_tp.value;
		//num_mlp    = "" + form.meses_libre_pago_tp.value;
	}
	else {
		alert( "Debes seleccionar una forma de pago válida." );
		return false;
	}
	
	//alert("num_tja   : " + num_tja);
	//alert("largo     : " + num_tja.length);
	//alert("num_cuotas: " + num_cuotas);
	

    /****************************************************************************************************/
    /****************************************************************************************************/
    /****************************************************************************************************/
    /****************************************************************************************************/
	if ( reg_sel == "CAT"){//VALIDACIONES PARA EL CASO DE SELECCION DE TARJETA JUMBO MAS
		/*if ( form.num_mp_jumbo_1.value == '' || form.num_mp_jumbo_2.value == '' || form.num_mp_jumbo_3.value == '' || form.num_mp_jumbo_4.value == '' ){
			alert("Debes llenar todos los campos de la tarjeta");
			return false;
		}*/
        if (num_tja == "" && num_cuotas == ""){
            alert("Debes ingresar al menos uno de los campos solicitados");
            return false;
        }
        
        if (mpago_ant == "TBK" || mpago_ant == "PAR"){
            if (Trim(num_tja) == ""){
                alert("Debe ingresar el Número de Tarjeta.");
                return false;
            }
        }
        
        
        if (num_tja.length > 0){
            // largo tarjeta puede ser de 16 numeros
            if( num_tja.length != 16 ) {
                alert( "Debes ingresar una tarjeta válida." );
                //form.num_mp_jumbo_1.focus();
                return false;
            }
            
            if (num_tja == "1111111111111111"){
                return true;
            }else{
                // Bean utilizado 615290 (Jumbo Más), 61528031 (Más Paris) y 61529025 (Más Easy)
                if( num_tja.substring(0,6) != '615290' && 
                      num_tja.substring(0,8) != '61528031' &&
                        num_tja.substring(0,8) != '61529025') {
                    alert( "Debes ingresar una tarjeta válida.");
                    //form.num_mp_jumbo_1.focus();
                    return false;
                }
            
                // Digito verificador mod 10
                if( ValidarTJ( num_tja ) == false ) {
                    alert( "Debes ingresar una tarjeta válida." );
                    //form.num_mp_jumbo_1.focus();
                    return false;
                }
            }
        }
	}	
	

    /***************************************************************************************************/
    /***************************************************************************************************/
    /***************************************************************************************************/
	if ( reg_sel == "TBK"){//VALIDACIONES PARA LA SELECCION TARJETA BANCARIA
		/*if (  form.nom_tban[0].checked == false && form.nom_tban[1].checked == false && form.nom_tban[2].checked == false && form.nom_tban[3].checked == false ){
			alert("Debes elegir un tipo de Tarjeta Bancaria");
			return false;
		}*/
	
		/*if ( form.num_mp.value == '' ){
			alert("Debes ingresar el campo Número de Tarjeta ");
			//form.num_mp.focus();
			return false;
		}*/

        if (mpago_ant == "CAT" || mpago_ant == "PAR"){
            
            if (!form.nom_tban){
                alert("Debe ingresar el Tipo de Tarjeta.");
                return false;
            }else if (Trim(num_tja) == ""){
                alert("Debe ingresar el Número de Tarjeta.");
                return false;
            }else if (form.exp_mes.value == ''){
                alert("Debe ingresar el Mes de Expiración.");
                return false;
            }else if (form.exp_ano.value == ''){
                alert("Debe ingresar el Año de Expiración.");
                return false;
            }else if (Trim(form.t_banco.value) == ''){
                alert("Debe ingresar el Banco al que pertenece la Terjeta.");
                return false;
            }
        }

        if (num_tja == '' && num_cuotas == '' 
           && (form.nom_tban[0].checked == false && form.nom_tban[1].checked == false && form.nom_tban[2].checked == false && form.nom_tban[3].checked == false)
             && form.exp_mes.value == '' && form.exp_ano.value == '' 
               && Trim(form.t_banco.value) == ''){
            alert( "Debes ingresar al menos uno de los campos solicitados." );
            return false;
        }

        if (form.num_mp.value.length > 0){
            // largo tarjeta puede ser entre [11,19] numeros
            if( form.num_mp.value.length < 11 || form.num_mp.value.length > 19 ) {
                alert( "Debes ingresar una tarjeta válida." );
                //form.num_mp.focus();
                return false;
            }

            // Si es visa
            if( form.nom_tban[0].checked == true ) {
                // Bean utilizado 4
                if( form.num_mp.value.substring(0,1) != '4' ) {
                    alert( "Debes ingresar una tarjeta válida." );
                    //form.num_mp.focus();
                    return false;
                }
                // Validacion digito verificador modulo 10
                if( ValidarTJ( form.num_mp.value ) == false ) {
                    alert( "Debes ingresar una tarjeta válida." );
                    //form.num_mp.focus();
                   return false;
                }
            }

            // Si es dinners
            if( form.nom_tban[1].checked == true ) {
                // Bean utilizados
                if( form.num_mp.value.substring(0,2) != '30' 
                    && form.num_mp.value.substring(0,2) != '36'
                    && form.num_mp.value.substring(0,2) != '38' ) {
                    alert( "Debes ingresar una tarjeta válida." );
                    //form.num_mp.focus();
                    return false;
                }
                // Validacion digito verificador modulo 10
                if( ValidarTJ( form.num_mp.value ) == false ) {
                    alert( "Debes ingresar una tarjeta válida." );
                    //form.num_mp.focus();
                    return false;
                }
            }
    
            // Si es mastercard
            if( form.nom_tban[2].checked == true ) {
                // Bean utilizados
                if( form.num_mp.value.substring(0,2) != '51' 
                    && form.num_mp.value.substring(0,2) != '52'
                    && form.num_mp.value.substring(0,2) != '53'
                    && form.num_mp.value.substring(0,2) != '54' 
                    && form.num_mp.value.substring(0,2) != '55' ) {
                    alert( "Debes ingresar una tarjeta válida." );
                    //form.num_mp.focus();
                    return false;
                }
                // Validacion digito verificador modulo 10
                if( ValidarTJ( form.num_mp.value ) == false ) {
                    alert( "Debes ingresar una tarjeta válida." );
                    //form.num_mp.focus();
                    return false;
                }
            }
    
            // Si es american express
            if( form.nom_tban[3].checked == true ) {
                // Validacion digito verificador modulo 10
                var numero;
                if (form.num_mp.value.length == 14)
                    numero = '00' + form.num_mp.value;
                else if (form.num_mp.value.length == 15)  
                    numero = '0' + form.num_mp.value;
                else
                    numero = form.num_mp.value;	
                if( ValidarTJ(numero) == false ) {
                    alert( "Debes ingresar una tarjeta válida." );
                    //form.num_mp.focus();
                    return false;
                }
            }

        }


		/*if ( form.exp_mes.value == ''){
			alert("Debes seleccionar el mes de expiración");
			//form.exp_mes.focus();
			return false;
		}
	
		if ( form.exp_ano.value == ''){
			alert("Debes seleccionar el año de expiración");
			//form.exp_ano.focus();
			return false;
		}*/

		if (form.exp_mes.value != ''){
		    if (form.exp_ano.value == ''){
		        alert("Debes seleccionar el año de expiración");
		        return false;
		    }else{
                var fecha_exp=new Date();
                fecha_exp.setFullYear("20"+form.exp_ano.value,form.exp_mes.value-1,1);
                var fecha_hoy=new Date();
                fecha_hoy.setFullYear(fecha_hoy.getFullYear(),fecha_hoy.getMonth(),1);
		        
                if( fecha_exp < fecha_hoy ) {
                    alert( "Debes ingresar una fecha válida para tu tarjeta." );
                    //form.num_mp_jumbo_1.focus();
                    return false;
                }
		    }
        }

		if (form.exp_ano.value != ''){
		    if (form.exp_mes.value == ''){
		        alert("Debes seleccionar el mes de expiración");
		        return false;
		    }else{
                var fecha_exp=new Date();
                fecha_exp.setFullYear("20"+form.exp_ano.value,form.exp_mes.value-1,1);
                var fecha_hoy=new Date();
                fecha_hoy.setFullYear(fecha_hoy.getFullYear(),fecha_hoy.getMonth(),1);
		        
                if( fecha_exp < fecha_hoy ) {
                    alert( "Debes ingresar una fecha válida para tu tarjeta." );
                    //form.num_mp_jumbo_1.focus();
                    return false;
                }
		    }
        }
        
        // Fecha de expiracion debe ser igual a superior al ano/mes en curso

		/*if (Trim(form.t_banco.value) == '' ){
			alert("Debes ingresar el campo Nombre del Banco");
			//form.t_banco.focus();
			return false;
		}*/
		
	}

    /***************************************************************************************************/
    /***************************************************************************************************/
    /***************************************************************************************************/
	if ( reg_sel == "PAR"){//VALIDACIONES PARA LA SELECCION TARJETA PARIS
	
	    var tipo_Tarjeta_ant = form.tipo_TP_ant.value;	//Indica si es Titular (1) o Adicional (0)
	    var tipo_Tarjeta;
	    
	    
	    
	    if (form.titular[0].checked == true){
	        tipo_Tarjeta = "1";
	    }else if (form.titular[1].checked == true){
	        tipo_Tarjeta = "0";
	    }
	    //alert('tipo_Tarjeta: ' + tipo_Tarjeta);
	    
	    
	    if (num_tja == '' && num_cuotas == '' 
             && (form.titular[0].checked == false && form.titular[1].checked == false)){
               //&& num_mlp == ''){
            alert( "Debes ingresar al menos uno de los campos solicitados." );
            return false;
        }
	    
	    
	    
	    
        if ((mpago_ant == "PAR") && tipo_Tarjeta_ant == "1"){
            if (tipo_Tarjeta == "0"){
                if (num_tja == ""){
                    alert( "Debes ingresar el número de tarjeta." );
                    return false;
                }
            
                if (num_tja.length != 16){
                    alert( "Debes ingresar una tarjeta válida." );
                    return false;
                }else{
                    if (validaTipoParis(num_tja, "") == "MASPARIS"){
                        alert("La tarjeta ingresada no corresponde a una Tarjeta Paris./nDebe ser ingresada como una tarjeta MÁS PARIS o CAT");
                        return false;
                    }
                    if (!ValidarTP(num_tja.substr(2)) || num_tja.length < 16){
                        alert( "Debes ingresar un número de tarjeta valido." );
                        return false;
                    }
                    if (num_tja.substr(2,1) == "4"){
                        alert( "NO es posible cambiar el Medio de Pago por una Tarjeta Empleado." );
                        return false;
                    }
                }
            }
            
            if (tipo_Tarjeta == "0"){ //ADICIONAL
                if (form.rut_tit.value == ""){
                    alert("Debe ingresar el RUT del Titular de la Cuenta.");
                    return false;
                }
                if (form.dv_tit.value == ""){
                    alert("Debe ingresar un Digito Verificador valido.");
                    return false;
                }
                if (form.nombre_tit.value == ""){
                    alert("Debe ingresar el Nombre del Titular de la Cuenta.");
                    return false;
                }
                if (form.appaterno_tit.value == ""){
                    alert("Debe ingresar el Apellido Paterno del Titular de la Cuenta.");
                    return false;
                }
                if (form.apmaterno_tit.value == ""){
                    alert("Debe ingresar el Apellido Materno del Titular de la Cuenta.");
                    return false;
                }
                if (form.direccion_tit.value == ""){
                    alert("Debe ingresar la Dirección del Titular de la Cuenta.");
                    return false;
                }
                if (form.numero_tit.value == ""){
                    alert("Debe ingresar el Número correspondiente a la Dirección.");
                    return false;
                }
                
                if (form.rut_tit.value != "" && form.dv_tit.value != ""){
                    if (!checkRutField(form.rut_tit.value + "-" + form.dv_tit.value)){
                        //alert("Debe ingresar un RUT Valido.");
                        return false;
                    }
                }
            }
        }else if ((mpago_ant == "PAR") && tipo_Tarjeta_ant == "0"){
            if (tipo_Tarjeta == "1"){
                if (num_tja == ""){
                    alert( "Debes ingresar el número de tarjeta." );
                    return false;
                }
            
                if (num_tja.length != 16){
                    alert( "Debes ingresar una tarjeta válida." );
                    return false;
                }else{
                    if (validaTipoParis(num_tja, "") == "MASPARIS"){
                        alert("La tarjeta ingresada no corresponde a una Tarjeta Paris./nDebe ser ingresada como una tarjeta MÁS PARIS o CAT");
                        return false;
                    }
                    if (!ValidarTP(num_tja.substr(2)) || num_tja.length < 16){
                        alert( "Debes ingresar un número de tarjeta valido." );
                        return false;
                    }
                    if (num_tja.substr(2,1) == "4"){
                        alert( "NO es posible cambiar el Medio de Pago por una Tarjeta Empleado." );
                        return false;
                    }
                }
            }else if (tipo_Tarjeta == "0"){
                if ((form.rut_tit.value != "" && form.dv_tit.value == "")
                     || (form.rut_tit.value == "" && form.dv_tit.value != "")){
                    alert("Para cambiar el RUT debes ingresar tanto\nel Número como su Dígito Verificador");
                    return false;
                }
                if (form.rut_tit.value != "" && form.dv_tit.value != ""){
                    if (!checkRutField(form.rut_tit.value + "-" + form.dv_tit.value)){
                        //alert("Debe ingresar un RUT Valido.");
                        return false;
                    }
                }
            }
        }else if (mpago_ant == "CAT" || mpago_ant == "TBK"){
            if (num_tja != ""){
                if (validaTipoParis(num_tja, "") == "MASPARIS"){
                    alert("La tarjeta ingresada no corresponde a una TARJETA PARIS/nDebe ser ingresada como una tarjeta MÁS PARIS o CAT");
                    return false;
                }
                if (!ValidarTP(num_tja.substr(2)) || num_tja.length < 16){
                    alert( "Debes ingresar un número de tarjeta valido." );
                    return false;
                }
            }else{
                alert("Debe ingresar un número de tarjeta.");
                return false;
            }

            if (num_cuotas == ""){
                alert( "Debes ingresar un número de cuotas." );
                return false;
            }
            
            /*if (num_mlp == ""){
                alert( "Debes ingresar el número de Meses Libre de Pago." );
                return false;
            }*/

            if (form.titular[0].checked == false && form.titular[1].checked == false){
                alert( "Debes ingresar el Tipo de Tarjeta Paris a usar." );
                return false;
            }
            
            if (tipo_Tarjeta == "0"){ //ADICIONAL
                if (form.rut_tit.value == ""){
                    alert("Debe ingresar el RUT del Titular de la Cuenta.");
                    return false;
                }
                if (form.dv_tit.value == ""){
                    alert("Debe ingresar un Digito Verificador valido.");
                    return false;
                }
                if (form.nombre_tit.value == ""){
                    alert("Debe ingresar el Nombre del Titular de la Cuenta.");
                    return false;
                }
                if (form.appaterno_tit.value == ""){
                    alert("Debe ingresar el Apellido Paterno del Titular de la Cuenta.");
                    return false;
                }
                if (form.apmaterno_tit.value == ""){
                    alert("Debe ingresar el Apellido Materno del Titular de la Cuenta.");
                    return false;
                }
                if (form.direccion_tit.value == ""){
                    alert("Debe ingresar la Dirección del Titular de la Cuenta.");
                    return false;
                }
                if (form.numero_tit.value == ""){
                    alert("Debe ingresar el Número correspondiente a la Dirección.");
                    return false;
                }
                
                if (form.rut_tit.value != "" && form.dv_tit.value != ""){
                    if (!checkRutField(form.rut_tit.value + "-" + form.dv_tit.value)){
                        //alert("Debe ingresar un RUT Valido.");
                        return false; 
                    }
                }
            }
        }
    }
    return true;	
}

function datosFactura(estado) {
	document.getElementById('factura').style.visibility = estado;
	if (estado == 'visible') 
		document.getElementById('div_factura').style.height = '245px';
	else
		document.getElementById('div_factura').style.height = '0px';
}


function ordenar_productos() {

    var form = document.ordenar;

    reg_sel = form.modo.options[form.modo.selectedIndex].value;

    top.frm_prod.location.href="CheckoutProList?modo="+reg_sel;

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

function validaTipoParis(numero_tarjeta,evento){
  var cadena = numero_tarjeta.toString();
  var longitud = cadena.length;
  if (evento == 'onkeypress'){
  	if (longitud < 15) {
  		return "NO";
  	} else {	
    	if ((cadena.substring(2,3) == "1") || (cadena.substring(2,3) == "2") || (cadena.substring(2,3) == "4")) { //Tipo de tarjeta
      		if ((cadena.substring(13,14) == "0") || (cadena.substring(13,14) == "1")) //titular o adicional
        		return "PARIS";
    	} else if (cadena.substring(0,8) == "61528031") { //BIN Tarjeta Mas Paris 
      		return "MASPARIS";
    	}
    	return "NO";	
    }	
  } else {
	if (longitud < 16) {
  		return "NO";
  	} else {	
    	if ((cadena.substring(2,3) == "1") || (cadena.substring(2,3) == "2") || (cadena.substring(2,3) == "4")) { //Tipo de tarjeta
      		if ((cadena.substring(13,14) == "0") || (cadena.substring(13,14) == "1")) //titular o adicional
        		return "PARIS";
    	} else if (cadena.substring(0,6) == "615280") { //BIN Tarjeta Mas Paris 
      		return "MASPARIS";
    	}
    	return "NO";	
    }
  }  	
  return "NO"; 
}  

function KeyIsNumber(evt){
    var isNav = (navigator.appName.indexOf("Netscape") != -1)
    var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
            || (evt.which >= 48 &&  evt.which <=57) ) // [0-9]
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( (evt.keyCode >= 48 && evt.keyCode <= 57) )
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}
