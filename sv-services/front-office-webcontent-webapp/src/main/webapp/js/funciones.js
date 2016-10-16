function cambiaDireccion(){
	var valor=document.getElementById("retiro_local").value;
	//var arreglo= valor.split('_');
	//codigoLocal=arreglo[0];
	if (valor=="325"){//Los Dominicos
		$j("#indicacion").show()
		$j("#divDominicos").show();
		$j("#divFlorida").hide();
	}
	if (valor=="304"){//La Florida
		$j("#indicacion").show()
		$j("#divDominicos").hide();
		$j("#divFlorida").show();
		
	}
}

function bloqueaCaracteres(e,reg) {
	var key;
	var keychar;
	var reg;

	if(window.event) {
		// for IE, e.keyCode or window.event.keyCode can be used
		key = e.keyCode;
	}
	else if(e.which) {
		// netscape
		key = e.which;
	}
	else {
		// no event, so pass through
		return true;
	}
	keychar = String.fromCharCode(key);
	return reg.test(keychar);
}

function findPos(obj) {
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		curleft = obj.offsetLeft
		curtop = obj.offsetTop
		while (obj = obj.offsetParent) {
			curleft += obj.offsetLeft
			curtop += obj.offsetTop
		}
	}
	return [curleft,curtop];
}

function valida_cantidad(nombre,intervalo,maximo) {
	var campo = document.getElementById(nombre);
	if ((campo.value != "") && (!isNaN(campo.value))) {
		if (parseFloat(campo.value,10) < 0) {
			alert("Por favor, ingrese un valor mayor a cero");
			campo.focus();
			campo.select();
			return false;
		} else if (parseFloat(campo.value,10) == 0) {
			return true;
		} else {
			if (parseFloat(campo.value,10) > maximo) {
				if ((parseFloat(maximo,10) % intervalo) != 0) {
					var division = parseFloat(maximo,10) / parseFloat(intervalo,10);
					campo.value = parseInt(division,10) * intervalo;
				} else {
					campo.value = maximo;
				} 	
				alert("Sólo puede ingresar un máximo de " + maximo + " productos. \nSi es un cliente empresa, llame al 600 400 3000 para solicitar mayor cantidad de productos");
				return true;
			} else {
				if ((parseFloat(campo.value,10) % intervalo) != 0) {
					var division = parseFloat(campo.value,10) / parseFloat(intervalo,10);
					campo.value = (Math.round( 100 * ( parseInt(division,10) * parseFloat(intervalo,10) ) )/100 )
					campo.focus();
					return true;
				} else {
					return true
				} 
			}	 
		}			
	} else {
		alert("Por favor, ingrese valores numéricos");
		campo.value = intervalo;
		return false;
	}		
}

function makeOptionList(objeto, name,value, sel) {
	var o=new Option( name,value);
	objeto.options[objeto.length]=o;
	if( sel == 1 )
		objeto.selectedIndex = objeto.length-1;
}

function val(obj,msg) {
	if ( !obj.value.length != 0 )
	{	alert("Debes ingresar " + msg)
		obj.focus()
		return false}
	else
		return true
}

// Habilita y Deshabilidad la seccion de texto en la pagina
function disableselect(e)  { 
   return false 
} 
function reEnableselect() { 
   return true 
} 
function disableSelectCampo() {
    document.onselectstart=new Function ("return false") 
    //if NS6 
    if (window.sidebar) { 
       document.onmousedown=disableselect;
       document.onclick=reEnableselect;
    }
}
function reEnableSelectCampo() {
    document.onselectstart=new Function ("return true") 
    //if NS6 
    if (window.sidebar) { 
       document.onmousedown=reEnableselect;
       document.onclick=reEnableselect;
    }
}
function largo_campo_comentario(campo) {
	if(campo.value.length >= 250){ 
		alert('Haz superado el tamaño máximo permitido'); 
		return false; 
	}
}
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
  
function autojump(fieldName,nextFieldName,fakeMaxLength){ 
         var myForm=document.forms[document.forms.length - 1]; 
         var myField=myForm.elements[fieldName]; 
         myField.nextField=myForm.elements[nextFieldName]; 
		 if (myField.maxLength == null) 
			myField.maxLength=fakeMaxLength; 
         myField.onkeydown=autojump_keyDown; 
         myField.onkeyup=autojump_keyUp; 
} 
  
function autojump_keyDown() { 
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
var nota_paso = '0';
var ficha_paso = '0';
var ficha_carro = false;
var id_formulario = "";

function show_nota_paso1(campo,id){
	nota_campo = campo;
	nota_paso = 1;
	nota_txt = campo.value;
	id_formulario = id;
	top.frames['frm_comentarios'].location.href="/FO_WebContent/comentarios.html";
}

function show_nota( campo, id ){
	nota_campo = campo;
	nota_txt = campo.value;
	id_formulario = id;
	top.updatePosLayerComentario();
	if( top.location.href.indexOf("CategoryDisplay") != -1 )
		top.frames['frm_comentarios'].location.href="/FO_WebContent/comentarios.html";
	else
		top.frames['frm_comentarios'].location.href="/FO_WebContent/comentarios.html";
}

function load_nota() {
	top.frames['frm_comentarios'].document.frm_nota.nota.value=nota_txt;
	top.frames['frm_comentarios'].document.frm_nota.idaux.value=id_formulario;
	top.frames['frm_comentarios'].document.frm_nota.pasoaux.value=nota_paso;		
}

function add_nota_paso1(posicion) {
	$('nota' + posicion).value = top.frames['frm_comentarios'].document.frm_nota.nota.value;
	top.hideComentario();
	return false;
}

function add_nota(id_producto) {
	document.getElementById("nota_txt_" + id_producto).value = top.frames['frm_comentarios'].document.frm_nota.nota.value;
	top.hideComentario();
	return false;
}

//******************************

function categoria(id, tipo, padre){
	window.location = "CategoryDisplay?cai="+id+"&tip="+tipo+"&caip="+padre;
	//top.frames['ifrm_productos'].location.href="./categorias/CategoryProList?idcategoria="+id;
}

function categoria_iframe(id, tipo, padre){
	//window.location = "CategoryDisplay?cai="+id+"&tip="+tipo+"&caip="+padre;
	top.frames['ifrm_productos'].location.href="./categorias/CategoryProList?idcategoria="+id+"&idpadre="+padre;
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

function up( campo, incremento, maximo ) {
	var valor = 0;
	if (campo.value == "" || campo.value == "undefined") {
		campo.value = 0;
	}
	valor = parseFloat(campo.value) + parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	campo.value = valor;
	if( validar_cantidad( campo, maximo ) == false ) {
		campo.value = maximo;
	}
}
function down( campo, incremento, maximo ) {
	var valor = 0;
	if (campo.value == "" || campo.value == "undefined") {
		campo.value = 0;
	}
	valor = parseFloat(campo.value) - parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	if( valor < 0 ) {
		campo.value = 0;
		return;
	} else {
		campo.value = valor;
	}
}

function validar_cantidad(campo, maximo) {
	valcampo = campo.value;
	if ((valcampo != "") && (!isNaN(valcampo))) {
		if ( parseFloat(valcampo) > parseFloat(maximo) ) {
			alert("Sólo se pueden agregar hasta "+ maximo + " productos");
			//campo.value=maximo;
			campo.focus();
			campo.select();
			return false;
		}
	} else {
		alert("Por favor, ingrese una cantidad válida para el producto");
		campo.focus();
		campo.select();	
		return false;
	}
	return true;
}

function carga_despachos() {
	frames['jcalendario'].location.href="./layers/DespachoChart";
}

function cambia_img_nota(form, contador, idenpath) { //idenpath:1 va al directorio ../img --- idenpath:2 va al directorio img
	if (form.nota.value != '' && idenpath == '1') {
		MM_swapImage('ImageComent_'+contador,'','/FO_IMGS/img/estructura/paso2/notasb.gif',1);
	}
	if (form.nota.value != '' && idenpath == '2') {
		MM_swapImage('ImageComent_'+contador,'','/FO_IMGS/estructura/paso2/notasb.gif',1);
	}
}

//*******************************
//----- INI CALENDARIO DESPACHO -----
function doTooltip(e, tipo, hIni, hFin, mostrar) {
    if ( typeof Tooltip == "undefined" || !Tooltip.ready || mostrar != "S" ) return;
    var cntnt = wrapTipContent(tipo, hIni, hFin);
    var tip = document.getElementById( Tooltip.tipID );
    Tooltip.show(e, cntnt);
}
function hideTip() {
    if ( typeof Tooltip == "undefined" || !Tooltip.ready ) return;
    Tooltip.hide();
}
function wrapTipContent(tipo, hIni, hFin) {
	var cntnt = "";
	if (tipo == 'EXP') {
		cntnt = "<span class='tituloDespacho'>Despacho Express</span><img src='/FO_IMGS/img/estructura/calendario/expressCarro.gif' width='21' height='13' /><br>"+
				"<span class='lectura'>Puedes comprar y recibir tu pedido durante el mismo día si compras antes de las " + hIni + " horas.</span>";
	} else if (tipo == 'ECO') {
		cntnt = "<span class='tituloDespacho'>Despacho Económico</span><br>"+
				"<span class='lectura'>Tu despacho será entregado el día que tú elijas entre las " + hIni + " y las " + hFin + " horas.</span>";
	}	
	return cntnt;
}
var imageHandler = {}
//----- FIN CALENDARIO DESPACHO -----


function replaceAll( text, busca, reemplaza ) {
  while (text.toString().indexOf(busca) != -1)
    text = text.toString().replace(busca,reemplaza);
  return text;
}

function getOfficesAvailable(from){
	
	var action  = "/FO/DespachosDisponibles";	
	
	switch(from){
		case 'INIT_ML'://inicio mis listas.
			action=action+"?action="+from;
			$j.get(action,function (data){$j('#contenido_DP').show();$j('#contenido_DP').html(data);});
			tag_evento_google_dd("Despachos_Disponibles", "Abrir", "inicio");
		break;
		case 'HIDE_ML':
			$j('#cont_cabecera_DP').html('<img src="/FO_IMGS/img/estructura/banner_desplegar_despacho.jpg" border="0" alt="Abrir" onclick="javascript:getOfficesAvailable(\'SHOW_ML\')"/>');
			$j('#contenido_DP').hide();
			tag_evento_google_dd("Despachos_Disponibles", "Cerrar", "contraer");
		break;
		case 'SHOW_ML':
			$j('#cont_cabecera_DP').html('<img src="/FO_IMGS/img/estructura/banner_contraer_despacho.jpg" border="0" alt="Abrir" onclick="javascript:getOfficesAvailable(\'HIDE_ML\')"/>');
			$j('#contenido_DP').show();
			tag_evento_google_dd("Despachos_Disponibles", "Abrir", "desplegar");
		break;		

		case 'L'://link CategoryDisplay	
			$j('#contenedor_lightbox_DP').show();
			action=action+"?action="+from;
			$j.get(action,function (data){$j('#contenido_DP').show();$j('#contenido_DP').html(data);});
			tag_evento_google_dd("Despachos_Disponibles", "Abrir", "via_link");
		break;
		
		//Acciones dentro del calendario
		case 'RD':			
			$j('#direccion_despacho').attr("disabled","");
			$j('#retiro_local').attr("disabled","disabled");
			action=action+"?direccion_despacho="+$j('#direccion_despacho').val()+"&action="+from;
			$j.get(action,function (data){$j('#cont_pie_DP').html(data);});
		break;
		case 'CD':
			action=action+"?direccion_despacho="+$j('#direccion_despacho').val()+"&action="+from;
			$j.get(action,function (data){$j('#cont_pie_DP').html(data);});
		break;
		case 'RR':
			$j('#retiro_local').attr("disabled","");
			$j('#direccion_despacho').attr("disabled","disabled");
			action=action+"?retiro_local="+$j('#retiro_local').val()+"&action="+from;
			$j.get(action,function (data){$j('#cont_pie_DP').html(data);});
		break;
		case 'CR':
			action=action+"?retiro_local="+$j('#retiro_local').val()+"&action="+from;
			$j.get(action,function (data){$j('#cont_pie_DP').html(data);});
		break;
		
		default:	
	}
}

function tag_evento_google_dd(categoria, accion, etiqueta){
	try { 
		var pageTracker = _gat._getTracker("UA-1529321-1"); 
		pageTracker._trackPageview();
		pageTracker._trackEvent(categoria, accion, etiqueta);
	} catch(err) {
		
	}
}

