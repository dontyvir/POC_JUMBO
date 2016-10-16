//cambiar el largo del telefono segun corresponda
var xQuery = jQuery.noConflict();
function onChange(cod,number,info){
	if(xQuery("#"+number).val()==""){
		if(xQuery("#"+cod).val() == "9" || xQuery("#"+cod).val() == "2"){ //si es celular o fijo metropilitano, campo número debe ser de 8
			xQuery("#"+info).html("Ingrese 8 d&iacute;gitos");
		}else{
			xQuery("#"+info).html("Ingrese 7 d&iacute;gitos");
		}
	}
	xQuery("#"+cod).change(function() { //para cuandi cambia el combobox
		if(xQuery("#"+cod).val() == "9" || xQuery("#"+cod).val() == "2"){ 
			xQuery("#"+number).attr("maxlength",8);
			xQuery("#"+info).html("Ingrese 8 d&iacute;gitos");
		}else{
			xQuery("#"+number).attr("maxlength",7);
			xQuery("#"+info).html("Ingrese 7 d&iacute;gitos");
		}
		xQuery("#"+number).val("");
		xQuery("#"+number).focus();
	});
	
//	xQuery("#"+number).focus(function() {
//		xQuery("#"+number).val("");
//	});
	
//	xQuery("#"+number).blur(function() {
//		if(xQuery("#"+cod).val() == "9" || xQuery("#"+cod).val() == "2"){
//			xQuery("#"+info).html("Ingrese 8 d&iacute;gitos");
//		}else{
//			xQuery("#"+info).html("Ingrese 7 d&iacute;gitos");
//		}
//	});
}

function initialize(cod,number){//para iniciarlizar los imputs ( en caso que no presionan el combo)
	xQuery("#"+number).numeric({allowThouSep:false , allowDecSep:false, allowMinus:false});
	if(isLongNumber(xQuery("#"+cod).val())){
		xQuery("#"+number).attr("maxlength",8);
	}else{
		xQuery("#"+number).attr("maxlength",7);
		
	}
}

//valida si se va a evaluar un numero de 9 digitos o de 7 desde base de datos
function isLongNumber(codigo){
	 if ((codigo == "9")||(codigo == "5")||(codigo == "6")||(codigo == "7")||(codigo == "8")|| (codigo == "2")){
		return true;
	}
	return false;
}

//valida si se va a evaluar un numero de 8 digitos o de 7 para insertar
function isLongNumberInsert(codigo){
	 if ((codigo == "9")||(codigo == "2")){
		return true;
	}
	return false;
}

//Valida si el número telefonico o de celular obtenido desde BD esta correcto, de caso contrario se pide al usuario actualizarlos
//donde :
// codObtenido : es el codigo del celular o telefono obtenido desde BD.
// numObtenido : es el codigo del celular o telefono obtenido desde BD.
// codImput    : es el id del select que contiene los codigos de numeros telefonicos.
// numImput    : es el id del imput que contiene los numeros telefonicos.
function validateCorrectNumber(codObtenido,numObtenido,codImput,numImput){
	var isFoundIt = false;
	xQuery("#"+codImput+" option").each(function(){
	    if (xQuery(this).val() == codObtenido){
	        xQuery(this).attr("selected",true);
	        isFoundIt = true;
	    }else if ((codObtenido == "5")||(codObtenido == "6")||(codObtenido == "7")||(codObtenido == "8")) {
	        isFoundIt = true;
	    } 
	});
	if(isFoundIt){
		if(isLongNumber(codObtenido)){
			if(/^\d{8}$/.test(numObtenido)) {
   				xQuery("#"+numImput).val(numObtenido);	
   				return true;
			}else if(/^\d{7}$/.test(numObtenido)) {
				xQuery("#"+numImput).val(codObtenido+numObtenido);//concatenamos el codigo con el numero
				return true;
			}else{
				return false;
			}
		}else{
			if(/^\d{7}$/.test(numObtenido)) {
   				xQuery("#"+numImput).val(numObtenido);
   				return true;
			}else{
				return false;
			}		
		}
	}else{
		return false;
	}
}


//obtiene los códigos existentes para celular y red fija en chile 
//+56-9-12345678 celular
//+56-02-12345678 fijo stgo
//+56-41-1234567 fijo regiones
function getAllCodPhones(){
	var codNumberPhone = '<option value="9" selected="selected" >cel</option>'+
						 '<option value="2">2</option>'+
				         '<option value="32">32</option>'+
				         '<option value="33">33</option>'+
				         '<option value="34">34</option>'+
				         '<option value="35">35</option>'+
				         '<option value="41">41</option>'+
				         '<option value="42">42</option>'+
				         '<option value="43">43</option>'+
				         '<option value="44">44</option>'+
				         '<option value="45">45</option>'+
				         '<option value="51">51</option>'+
				         '<option value="52">52</option>'+
				         '<option value="53">53</option>'+
				         '<option value="55">55</option>'+
				         '<option value="57">57</option>'+
				         '<option value="58">58</option>'+
				         '<option value="61">61</option>'+
				         '<option value="63">63</option>'+
				         '<option value="64">64</option>'+
				         '<option value="65">65</option>'+
				         '<option value="67">67</option>'+
				         '<option value="71">71</option>'+
				         '<option value="72">72</option>'+
				         '<option value="73">73</option>'+
				         '<option value="75">75</option>';
return 	codNumberPhone;
}