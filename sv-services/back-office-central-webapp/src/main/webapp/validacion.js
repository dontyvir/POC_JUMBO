/*

****************************************************************
*
* Versi?n 1.1  (29-12-2004)
* Creador: Jorge Sep?lveda
* KID CHILE LTDA.
*
****************************************************************



En SUBMIT:
onClick="if(verify(document.MainForm)) MainForm.submit();"



En Alguna Parte
<SCRIPT LANGUAGE="JavaScript">
<!--
 document.MainForm.cod_articulo.obligatorio=true;
 document.MainForm.cod_articulo.numerico=true;
 document.MainForm.cod_articulo.min=0;
 document.MainForm.cod_articulo.max=1000;
//-->
</SCRIPT>


*/






function isblank(s)
{
	for(var i=0;i<s.length;i++){
		var c= s.charAt(i);
		if((c!=' ')&&(c!='\n')&&(c!='\t'))return false;
	}
	return true;
}



function verify(f){

	var msg;
	var CamposVacios = ""; var Errores = "";
	var e=f.elements;
	for(var i=0; i < e.length; i++){
		if(((e[i].type == "hidden")   ||
			(e[i].type == "text")     ||
			(e[i].type == "textarea") ||
			(e[i].type == "select-one")) &&
			(e[i].obligatorio==true)){
			// checkea si es vacío
			if((e[i].value == null) || (e[i].value=="") || isblank(e[i].value)){
				CamposVacios += "\n           " + e[i].label;
				continue;
			}
			// checkea los que se suponen numéricos
			if(e[i].numerico || (e[i].min != null) || (e[i].max != null)){
				var v = parseFloat(e[i].value);
				if(isNaN(v) ||
					((e[i].min != null) && (v<e[i].min)) ||
					((e[i].max != null) && (v>e[i].max))) {
					Errores += "-El campo " + e[i].label + " debe ser un número";
					if(e[i].min!=null)
						Errores += " , mayor que " + e[i].min;
					if(e[i].max!=null)
						Errores += " , menor que " + e[i].max;
					else if(e[i].max!=null)
						Errores += " , menor que " + e[i].max;
					Errores += ".\n";
				}
			}
		}
	}
	if(!CamposVacios && !Errores){
		if(confirm('Esta a punto de ingresar la informacion presente, si esta seguro, presione Aceptar. Caso contrario, presione Cancelar'))
			return true;
		else
			return false;
	}
	msg =  "________________________________________________________\n\n";
	msg += "El formulario no fue enviado debido a el(los) siguiente(s) error(es).\n";
	msg += "Por favor corrija el(los) error(es) y reenvielo.\n";
	msg +=  "________________________________________________________\n\n";

	if(CamposVacios){
		msg += "- El(los) siguiente(s) campo(s) esta(n) vacio(s):" + CamposVacios + "\n";
		if(Errores) msg += "\n";
	}
	msg += Errores;
	alert(msg);
	return false;	
}






/**** Validaci?n de Rut *********/


/**
 *  Retorna Digito Verificador de la parte num?rica de un rut,
 *  Ojo que retorna k (min?scula)
 *  @param int rut  (parte num?rica del rut)
 *  @return char Digito Verificador
 */
function getDV( rut ){

	largo = rut.length;
	if ( largo < 1 )
		return 'x';
	
	var dvr = '0'

	suma = 0
	mul  = 2

	for (i=rut.length-1 ; i >= 0; i--)
	{
		suma = suma + rut.charAt(i) * mul
		if (mul == 7)
			mul = 2
		else    
			mul++
	}

	res = suma % 11
	if (res==1)
		dvr = 'k'
	else if (res==0)
		dvr = '0'
	else
	{
		dvi = 11-res
		dvr = dvi + ""
	}
	
	return dvr;
}



/*********************************************************************
*		Ingresa Solo N?meros
*********************************************************************/
function KeyIsNumber(evt)
{
var isNav = (navigator.appName.indexOf("Netscape") != -1)
var isIE = (navigator.appName.indexOf("Microsoft") != -1)

	if (isNav) {
		if ( evt.which == 13 || evt.which == 44 || evt.which == 8 || (evt.which >= 48 &&  evt.which <=57) )
		return true;
	return false;
	}
	else if (isIE)
		{evt = window.event;
		if ( evt.keyCode == 13 || evt.keyCode == 44 || evt.keyCode == 8 || (evt.keyCode >= 48 && evt.keyCode <= 57) )
			return true;
		return false;
		}
	else {
		alert("Su browser no es soportado por esta aplicaci?n")
	}
	return false
}


/************************************************************
**														   **		 
**                             AJAX                        **
**                                                         **  
************************************************************/

////////////////////////////////////////////////
	var request = false;
	var funcexec = '';
	function define_AJAXRPC(){
	   try {
		 request = new XMLHttpRequest();
	   } catch (trymicrosoft) {
		 try {
		   request = new ActiveXObject("Msxml2.XMLHTTP");
		 } catch (othermicrosoft) {
		   try {
			 request = new ActiveXObject("Microsoft.XMLHTTP");
		   } catch (failed) {
			 request = false;
		   }  
		 }
	   }
		if (!request)
			alert("Error initializing XMLHttpRequest!");
	}

	function send_AJAXRPC(method, url, mfunc){
		request.open(method, url, true);
		request.onreadystatechange = receive_AJAXRPC;
		funcexec = mfunc;
		request.send(null);
	}

	function receive_AJAXRPC(){
     if (request.readyState == 4) {
       if (request.status == 200) {
		 if (funcexec.length > 0) {
			eval(funcexec+"('" + request.responseText + "')");
		 }
		 else {
			alert('No se ha definido nombre de funci?n para respuesta');
		 }
       } 
	   else {
         alert("status is " + request.status);
	   }
     }
	}

	/************************************************************************************************/
	/* Funcion exec_AJAXRPC																			*/
	/*		- metodo:	"POST" o "GET"																*/
	/*		- url:		Url de la CGI que se invocar?												*/
	/*		- fprocesa:	Funci?n que procesar? la respuesta. Esta funci?n debe tener un ?nico		*/
	/*					par?metro de entrada que corresponde al texto retornado por la CGI			*/
	/************************************************************************************************/
	function exec_AJAXRPC(metodo, url, fprocesa){
		define_AJAXRPC();
		send_AJAXRPC(metodo, url, fprocesa);
	}

////////////////////////////////////////////



