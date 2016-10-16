// 
////////////////////////////////////////////////
function KeyIsLetra(evt)
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
            || (evt.which >= 224 &&  evt.which <=252) // acentos minusculas
            || (evt.which >= 192 &&  evt.which <=220) // acentos mayusculas
            || (evt.which >= 65 &&  evt.which <=90)  // [a-z]
            || (evt.which >= 97 &&  evt.which <=122) )// [A-Z]
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( (evt.keyCode >= 224 && evt.keyCode <= 252) 
            || (evt.keyCode >= 192 &&  evt.keyCode <=220)
            || (evt.keyCode >= 65 && evt.keyCode <= 90) 
            || (evt.keyCode >= 97 && evt.keyCode <= 122) )
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}

////////////////////////////////////////////////
function KeyIsNumber(evt)
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
			|| (evt.which == 13) // ENTER
            || (evt.which >= 48 &&  evt.which <=57) ) // [0-9]
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( (evt.keyCode >= 48 && evt.keyCode <= 57) 
		    || (evt.keyCode == 13) )
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}

////////////////////////////////////////////////
function KeyIsRut(evt)
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)
	if (isNav) {
		if  ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
            || evt.which == 46 || /*punto*/ evt.which == 45 ||
			(evt.which >= 48 && evt.which <=  57) || /*[0-9]*/
			(evt.which == 75 || evt.which ==  107)    /*[kK]*/ )
		return true;
			if(evt.keyCode == 13){
			 checkDV();
			}
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( evt.keyCode == 46 || evt.keyCode == 45 || /* punto y guion */
			(evt.keyCode >= 48 && evt.keyCode <=  57) || /*[0-9]*/
			(evt.keyCode == 75 || evt.keyCode ==  107)    /*[kK]*/ )
			return true;
			/*if(evt.keyCode == 13){
			 checkDV();
			}*/
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}
////////////////////////////////////////////////
function KeyIsTelefono(evt)
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
			|| (evt.which == 13) // ENTER
            || evt.which >= 48 && evt.which <=57 ) // [0-9]
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( (evt.keyCode >= 48 && evt.keyCode <= 57) 
			|| (evt.keyCode == 13) )
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}
////////////////////////////////////////////////
function KeyIsTexto(evt)
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
            || evt.which == 32 
            || evt.which == 46  // .
            || (evt.which >= 224 && evt.which <= 252 ) // Acentos minusculas
            || (evt.which >= 192 &&  evt.which <=220) // acentos mayusculas
            || (evt.which >= 48 && evt.which <= 57 )   // [0-9]
            || (evt.which >= 65 && evt.which <= 90 )   // [a-z]
            || (evt.which >= 97 && evt.which <= 122 )  // [A-Z]
			|| (evt.which == 13)) // ENTER
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( evt.keyCode == 32 
            || evt.keyCode == 46
            || (evt.keyCode >= 224 && evt.keyCode <= 252)
            || (evt.keyCode >= 192 &&  evt.keyCode <=220)
            || (evt.keyCode >= 48 && evt.keyCode <= 57 ) 
            || (evt.keyCode >= 65 && evt.keyCode <= 90 ) 
            || (evt.keyCode >= 97 && evt.keyCode <= 122 )
			|| (evt.keyCode == 13))
		return true;
	return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false;
}
////////////////////////////////////////////////
function KeyIsTexto_R(evt)/* NO permite ' " $ / \   */
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( evt.which == 34 ||  evt.which == 36 || evt.which == 39 || evt.which == 47 || evt.which == 92 )
		return false;
	return true;
	}
	else if (isIE)
		{evt = window.event;
		if (evt.keyCode == 34 || evt.keyCode == 36 || evt.keyCode == 39 || evt.keyCode == 47 || evt.keyCode == 92)
		return false;
	return true;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}
////////////////////////////////////////////////
function KeyIsClave(evt)
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
            || evt.which == 32 
            || evt.which == 46  // .
            || (evt.which >= 224 && evt.which <= 252 ) // Acentos minusculas
            || (evt.which >= 192 &&  evt.which <=220) // acentos mayusculas
            || (evt.which >= 48 && evt.which <= 57 )   // [0-9]
            || (evt.which >= 65 && evt.which <= 90 )   // [a-z]
            || (evt.which >= 97 && evt.which <= 122 ) ) // [A-Z]
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( evt.keyCode == 32 
            || evt.keyCode == 46
            || (evt.keyCode >= 224 && evt.keyCode <= 252)
            || (evt.keyCode >= 192 &&  evt.keyCode <=220)
            || (evt.keyCode >= 48 && evt.keyCode <= 57 ) 
            || (evt.keyCode >= 65 && evt.keyCode <= 90 ) 
            || (evt.keyCode >= 97 && evt.keyCode <= 122 ) )
		return true;
	return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false;
}
////////////////////////////////////////////////


function KeyIsApellido(evt)
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
            || (evt.which >= 224 &&  evt.which <=252) // acentos
            || (evt.which >= 192 &&  evt.which <=220) // acentos mayusculas
            || (evt.which >= 65 &&  evt.which <=90)  // [a-z]
            || (evt.which >= 97 &&  evt.which <=122) // [A-Z]
            || (evt.which == 45) //guion
            || (evt.which == 32) ) //espacio
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( (evt.keyCode >= 224 && evt.keyCode <= 252) 
            || (evt.keyCode >= 192 && evt.keyCode <=220)
            || (evt.keyCode >= 65 && evt.keyCode <= 90) 
            || (evt.keyCode >= 97 && evt.keyCode <= 122) 
            || (evt.keyCode == 45)
            || (evt.keyCode == 32) )
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}
////////////////////////////////////////////////
function KeyIsCantidad(evt)
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)
var cantP = 0;

	if (isNav) {
		for (i=0; i<evt.target.value.length; i++){
			if (evt.target.value.charAt(i) == "."){
				cantP = cantP + 1;
			}
		}
		if( evt.which == 46 && cantP > 0 ) // sólo permite un punto por cantidad
			return false;

		if ( ( evt.which >= 0 && evt.which <= 9 ) // Tab delete etc
			|| (evt.which == 13) //Enter
            || evt.which == 46 ||  (evt.which >= 48 &&  evt.which <=57) )
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		for (i=0; i<evt.srcElement.value.length; i++){
			if (evt.srcElement.value.charAt(i) == "."){
				cantP = cantP + 1;
			}
		}
		if( evt.keyCode == 46 && cantP > 0 ) // sólo permite un punto por cantidad
			return false;

		if ( evt.keyCode == 13 || evt.keyCode == 46 || (evt.keyCode >= 48 && evt.keyCode <= 57) )
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}

////////////////////////////////////////////////
function KeyIsTexto_S(evt)/* NO permite ' " $ / \   */
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 )
			|| (evt.which >= 224 && evt.which <= 252) 
			|| (evt.which >= 192 && evt.which <=220)
            || (evt.which >= 65 && evt.which <= 90) 
            || (evt.which >= 97 && evt.which <= 122) 
			|| (evt.which >= 48 && evt.which <= 57 )
            || (evt.which == 45)
            || (evt.which == 32)
			|| (evt.which == 13)//Enter
			)
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( (evt.keyCode >= 224 && evt.keyCode <= 252) 
            || (evt.keyCode >= 192 && evt.keyCode <=220)
            || (evt.keyCode >= 65 && evt.keyCode <= 90) 
            || (evt.keyCode >= 97 && evt.keyCode <= 122) 
			|| (evt.keyCode >= 48 && evt.keyCode <= 57 )
            || (evt.keyCode == 45)
            || (evt.keyCode == 32)
			|| (evt.keyCode == 13))//Enter
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}
////////////////////////////////////////////////
function KeyIsTexto_Razon(evt)/* NO permite ' " $ / \   */
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( ( evt.which >= 0 && evt.which <= 9 )
			|| (evt.which >= 224 && evt.which <= 252) 
			|| (evt.which >= 192 && evt.which <=220)
            || (evt.which >= 65 && evt.which <= 90) 
            || (evt.which >= 97 && evt.which <= 122) 
			|| (evt.which >= 48 && evt.which <= 57 )
            || (evt.which == 45)
            || (evt.which == 32)
			|| (evt.which == 64)//Enter
			|| (evt.which == 46)//Punto 
			)
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( (evt.keyCode >= 224 && evt.keyCode <= 252) 
            || (evt.keyCode >= 192 && evt.keyCode <=220)
            || (evt.keyCode >= 65 && evt.keyCode <= 90) 
            || (evt.keyCode >= 97 && evt.keyCode <= 122) 
			|| (evt.keyCode >= 48 && evt.keyCode <= 57 )
            || (evt.keyCode == 45)
            || (evt.keyCode == 32)
			|| (evt.keyCode == 64)//Enter
			|| (evt.keyCode == 46))//Punto
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}
////////////////////////////////////////////////
function KeyIsTexto_MPA(evt)/* NO permite ' " / \   */
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( evt.which == 34 ||  evt.which == 39 || evt.which == 47 || evt.which == 92 )
		return false;
	return true;
	}
	else if (isIE)
		{evt = window.event;
		if (evt.keyCode == 34 || evt.keyCode == 39 || evt.keyCode == 47 || evt.keyCode == 92)
		return false;
	return true;
		}
	else {
		alert("Su browser no es soportado por esta aplicación")
	}
	return false
}

////////////////////////////////////////////////
function makeOptionList(objeto, name,value, sel) {
	var o=new Option( name,value);
	objeto.options[objeto.length]=o;
	if( sel == 1 )
		objeto.selectedIndex = objeto.length-1;
}


function val(obj,msg)
{
	if ( !obj.value.length != 0 )
	{	alert("Debes ingresar " + msg)
		obj.focus()
		return false}
	else
		return true
}

// Habilita y Deshabilidad la seccion de texto en la pagina
function disableselect(e) 
{ 
   return false 
} 
function reEnableselect() 
{ 
   return true 
} 
function disableSelectCampo( ) {
    document.onselectstart=new Function ("return false") 
    //if NS6 
    if (window.sidebar) 
    { 
       document.onmousedown=disableselect 
       document.onclick=reEnableselect
    }
}
function reEnableSelectCampo( ) {
    document.onselectstart=new Function ("return true") 
    //if NS6 
    if (window.sidebar) 
    { 
       document.onmousedown=reEnableselect
       document.onclick=reEnableselect
    }
}

function largo_campo_comentario(campo){
	if(campo.value.length >= 250){ 
		alert('Haz superado el tamaño máximo permitido'); 
		return false; 
	}
}

////////////////////////////////////////////////

function TrimRight( str ) {
	var resultStr = "";
	var i = 0;
	// Return immediately if an invalid value was passed in
	if (str+"" == "undefined" || str == null) 
		return null;
	// Make sure the argument is a string
	str += "";

	if (str.length == 0) 
		resultStr = "";
	else {
		// Loop through string starting at the end as long as there
		// are spaces.
		i = str.length - 1;
		while ((i >= 0) && (str.charAt(i) == " "))
			i--;
	
		// When the loop is done, we're sitting at the last non-space char,
		// so return that char plus all previous chars of the string.
		resultStr = str.substring(0, i + 1);
	}

	return resultStr; 
}

function TrimLeft( str ) {
	var resultStr = "";
	var i = len = 0;
	// Return immediately if an invalid value was passed in
	if (str+"" == "undefined" || str == null) 
		return null;
		// Make sure the argument is a string
		str += "";
	if (str.length == 0) 
		resultStr = "";
	else { 
		// Loop through string starting at the beginning as long as there
		// are spaces.
		// len = str.length - 1;
		len = str.length;

		while ((i <= len) && (str.charAt(i) == " "))
		i++;
		// When the loop is done, we're sitting at the first non-space char,
		// so return that char plus the remaining chars of the string.
		resultStr = str.substring(i, len);
	}
	return resultStr;
}


function Trim( str ) {
	var resultStr = "";
	
	resultStr = TrimLeft(str);
	resultStr = TrimRight(resultStr);
	
	return resultStr;
}

function checklength(nomtxt){
	var max = 250;
	var txt;
	txt=document.getElementById(nomtxt.name);
	var n = txt.value.length;
	if (n>=max)	{
		alert('Haz excedido el limite máximo de caracteres permitidos');
		txt.value = txt.value.substring(0, max-1); 
		return false;
	}

}

/**
* funcion para comprobar si una año es bisiesto
* argumento año > año extraido de la fecha introducida por el usuario
*/
function anyoBisiesto(anyo)	{
	/**
	* si el año introducido es de dos cifras lo pasamos al periodo de 1900. Ejemplo: 25 > 1925
	*/
	if (anyo < 100)
		var fin = anyo + 1900;
	else
		var fin = anyo ;

	/*
	* primera condicion: si el resto de dividir el año entre 4 no es cero > el año no es bisiesto
	* es decir, obtenemos año modulo 4, teniendo que cumplirse anyo mod(4)=0 para bisiesto
	*/
	if (fin % 4 != 0)
		return false;
	else{
		if (fin % 100 == 0){
			/**
			* si el año es divisible por 4 y por 100 y divisible por 400 > es bisiesto
			*/
			if (fin % 400 == 0){
				return true;
			}
			/**
			* si es divisible por 4 y por 100 pero no lo es por 400 > no es bisiesto
			*/
			else{
				return false;
			}
		}
		/**
		* si es divisible por 4 y no es divisible por 100 > el año es bisiesto
		*/
		else{
			return true;
		}
	}
}

function calendar_dias(ano,mes){
	var combo = document.cliente.dia.options;
	select  = combo[document.cliente.dia.selectedIndex].value;
	if (ano != "" && mes != ""){
		if(mes == 2){
			if(anyoBisiesto(ano)){
				febrero=29;
				combo.length = null;
			    combo[0] =  new Option("...","","","");
			    for (i=1; i<=febrero; i++)
	    			combo[i] = new Option(i,i,"","");
				if	(select <= 28)
	    			document.cliente.dia.selectedIndex = select;
	    		else
		    		document.cliente.dia.selectedIndex = "";
			}else{
				febrero=28;
				combo.length = null;
			    combo[0] =  new Option("...","","","");
			    for (i=1; i<=febrero; i++)
	    			combo[i] = new Option(i,i,"","");
				if	(select <= 28)
	    			document.cliente.dia.selectedIndex = select;
	    		else
		    		document.cliente.dia.selectedIndex = "";
			}
		}else{
			totaldia = [31,28,31,30,31,30,31,31,30,31,30,31];
			combo.length = null;
		    combo[0] =  new Option("...","","","");
		    for (i=1; i<=totaldia[mes-1]; i++)
				combo[i] = new Option(i,i,"","");
			if (select <= totaldia[mes-1]){
				document.cliente.dia.selectedIndex = select;
			}else{
				document.cliente.dia.selectedIndex = "";
			}
			
		}
	}
}

var downStrokeField; 
  
function autojump(fieldName,nextFieldName,fakeMaxLength, myForm){ 
         var myField=myForm.elements[fieldName]; 
         myField.nextField=myForm.elements[nextFieldName]; 
		 if (myField.maxLength == null) 
			myField.maxLength=fakeMaxLength; 
         myField.onkeydown=autojump_keyDown; 
         myField.onkeyup=autojump_keyUp; 
} 
  
function autojump_keyDown(){ 
         this.beforeLength=this.value.length; 
         downStrokeField=this; 
 } 
  
function autojump_keyUp(){ 
         if ( 
		  (this == downStrokeField) && 
		  (this.value.length > this.beforeLength) && 
		  (this.value.length >= this.maxLength) 
         ) 
         this.nextField.focus(); 
         downStrokeField=null; 
} 

//******************************
// Funciones para notas de los productos

var nota_txt = "";
var nota_campo = null;
function show_nota( campo ){
	nota_campo = campo;
	nota_txt = campo.value;
	top.updatePosLayerComentario();
	if( top.location.href.indexOf("CategoryDisplay") != -1 )
		top.frames['frm_comentarios'].location.href="../comentarios.html";
	else
		top.frames['frm_comentarios'].location.href="comentarios.html";
	/*
	 var nota= prompt('Ingrese Comentario', campo.value);
	 //form.elements["nota_"+i].value = nota;
	 if ( (nota!=null) ) 
		campo.value = nota; 
	*/
}

function load_nota() {
	top.frames['frm_comentarios'].document.frm_nota.nota.value=nota_txt;
}

function add_nota(){
	nota_campo.value = top.frames['frm_comentarios'].document.frm_nota.nota.value;
	top.MM_showHideLayers('comentarioproducto','','hide');
	return false;
}

//******************************

function alert_tamano_ventana() {
  var myWidth = 0, myHeight = 0;
  if( typeof( window.innerWidth ) == 'number' ) {
    //Non-IE
    myWidth = window.innerWidth;
    myHeight = window.innerHeight;
  } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
    //IE 6+ in 'standards compliant mode'
    myWidth = document.documentElement.clientWidth;
    myHeight = document.documentElement.clientHeight;
  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
    //IE 4 compatible
    myWidth = document.body.clientWidth;
    myHeight = document.body.clientHeight;
  }
  window.alert( 'Width = ' + myWidth + ' Height = ' + myHeight );
}

function ancho_ventana() {
  var myWidth = 0, myHeight = 0;
  if( typeof( top.window.innerWidth ) == 'number' ) {
    //Non-IE
    myWidth = top.window.innerWidth;
    myHeight = top.window.innerHeight;
  } else if( top.document.documentElement && ( top.document.documentElement.clientWidth || top.document.documentElement.clientHeight ) ) {
    //IE 6+ in 'standards compliant mode'
    myWidth = top.document.documentElement.clientWidth;
    myHeight = top.document.documentElement.clientHeight;
  } else if( top.document.body && ( top.document.body.clientWidth || top.document.body.clientHeight ) ) {
    //IE 4 compatible
    myWidth = top.document.body.clientWidth;
    myHeight = top.document.body.clientHeight;
  }
 
  return( myWidth );
}

//******************************

function hl( objeto, intervalo, maximo, selected ) {

	makeOptionList(objeto, '0',0, 1);
	for( var i = 0; i < maximo; i = i+intervalo ) {
		valor = Math.round((i+intervalo)*1000)/1000;
		if( selected == valor )
			makeOptionList(objeto, valor,valor, 1);
		else
			makeOptionList(objeto, valor,valor, 0);
	}

}

function up( campo, incremento, maximo ) {

	var valor = 0;
	if (campo.value == "" || campo.value == "undefined"){
		campo.value = 0;
	}

	valor = parseFloat(campo.value) + parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	campo.value = valor;
	if( validar_cantidad( campo, maximo ) == false )
		campo.value = maximo;

}
function down( campo, incremento, maximo ) {

	var valor = 0;
	if (campo.value == "" || campo.value == "undefined"){
		campo.value = 0;
	}

	valor = parseFloat(campo.value) - parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	if( valor < 0 ) {
		campo.value = 0;
		return;
	}
	else
		campo.value = valor;

}

function validar_cantidad(campo, maximo){

	valcampo = campo.value;
	if ( parseFloat(valcampo) > parseFloat(maximo) ){
		alert("Sólo se pueden agregar hasta "+ maximo + " productos");
		//campo.value=maximo;
		campo.focus();
		campo.select();
		return false;
	}
	return true;
}

function setCantidad( campo, incremento ) {

	var c_valor = campo.value;
	var modulo  = parseFloat(c_valor) % parseFloat(incremento);
	//alert( c_valor + "%" + incremento + "=" + modulo );
	
	if( modulo != 0 ) {
		c_valor = c_valor - modulo;
		alert( "La cantidad ingresada no es válida, debe ser multiplo de " + incremento + " se cambiará por " + c_valor );
	}

	return c_valor;

}

//*******************************

function cambiar_color_fila_on(td){
	td.bgColor="#EBFCBC";
}

function cambiar_color_fila_off(td){
	td.bgColor="#FFFFFF";
}

function cambiar_color_on( id ){
	id.style.background = '#EBFCBC';
}
function cambiar_color_off( id ){
	id.style.background = '#F0F0F0';
}

function go_page( url ) {
	document.location.href=url;
}


function winficha(idpro){
	top.mod_icon = idpro;
	window.open("ViewProductDisplay?pro_id="+idpro, "ficha", "height=250,width=440,scrollbars=yes,status=no,Titlebar=no");
}

//***************************************

function format_number( texto ) {
  
  var invertido = "";
  var largo = texto.length;

  for ( i=(largo-1); i>=0; i-- )
    invertido = invertido + texto.charAt(i);

  var dtexto = "";

  cnt = 0;

  for ( i=0; i<largo; i++ )
  {
    if ( cnt == 3 )
    {
      dtexto = dtexto + '.';
      cnt = 1;
    }
    else
    { 
      cnt++;
    }
    dtexto = dtexto + invertido.charAt(i);	
  }

  invertido = "";

  for ( i=(dtexto.length-1); i>=0; i-- )
    invertido = invertido + dtexto.charAt(i);

  return invertido;
}

function validar_input( formulario ) {

	for( i = 0; i < formulario.length; i++ ) {
	
		objeto = formulario.elements[i];
	
		if( objeto.obligatorio == true ) {
			
			if( objeto.value == '' ) {
				alert( "El campo " + objeto.label + " es obligatorio." );
				objeto.focus();
				return false;
			}
		}
		
	}
	
	return true;

}