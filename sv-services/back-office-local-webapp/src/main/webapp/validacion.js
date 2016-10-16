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
 document.MainForm.cod_articulo.label='C?digo de Producto';
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



function verify(flag,f){

	var msg;
	var CamposVacios = ""; var Errores = "";
	var e=f.elements;
	for(var i=0; i < e.length; i++){
		if(((e[i].type == "hidden")   ||
			(e[i].type == "text")     ||
			(e[i].type == "textarea") ||
			(e[i].type == "select-one")) &&
			(e[i].obligatorio==true)){
			// checkea si es vac?o
			if((e[i].value == null) || (e[i].value=="") || isblank(e[i].value)){
				CamposVacios += "\n           " + e[i].label;
				continue;
			}
			// checkea los que se suponen num?ricos
			if(e[i].numerico || (e[i].min != null) || (e[i].max != null)){
				var v = parseFloat(e[i].value);
				if(isNaN(v) ||
					((e[i].min != null) && (v<e[i].min)) ||
					((e[i].max != null) && (v>e[i].max))) {
					Errores += "-El campo " + e[i].label + " debe ser un numero";
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
	//confirm('Esta a punto de modificar la informacion presente, si esta seguro, presione Aceptar. Caso contrario, presione Cancelar')
		if(flag == 0)
			return true;
		if	(flag != 0){
			confirm('Esta a punto de modificar la informacion presente, si esta seguro, presione Aceptar. Caso contrario, presione Cancelar');
			return true;
		}
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

