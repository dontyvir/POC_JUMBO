var params = {};
function cargaFono() {
	jQuery('#fon_cod_1').append(getAllCodPhones());
	var codigo1 = jQuery('#codigo').val();
	var numero1 = jQuery('#numero').val();
	initialize('fon_cod_1', 'fon_num_1');
	if (!validateCorrectNumber(codigo1, numero1, 'fon_cod_1', 'fon_num_1')) {
		jQuery('#fon_num_1_error').html('Debe actualizar n&uacute;mero de contacto');
		jQuery('#fon_num_1').removeClass('campos').addClass('error_campos');
		jQuery('#fon_num_1').focus();
	}
	onChange('fon_cod_1','fon_num_1','fon_num_1_info');
}
 
function ventanaObjeto(accion,descripcion,id_objeto,activado,id_empresa,toDo,param1,param2) {
	limpiarMsjAndWin();
	
	win = new Window('window_id', {title: "&nbsp;&nbsp; "+accion, minWidth:400, width:400, minHeight:260, height:260});
	
	var datosExtra = "";
	var nombre = "Nombre";
	
	if ( toDo == "ModAddDelChoferTrans" ) {
		datosExtra = "	<tr valign='middle'>"+
					 "		<td width='25%' height='30'>&nbsp;&nbsp; Rut</td>"+
					 "		<td width='5%'>:</td>"+
					 "		<td width='70%'>"+
					 "			<input type='text' id='rut' name='rut' value='"+param1+"' style='width:80px' maxlength='8' onKeyPress=\"SoloNumeros(this)\" onKeyUp=\"SoloNumeros(this)\" />-"+
					 "			<input type='text' id='dv' name='dv' value='"+param2+"' style='width:10px' maxlength='1'/>"+
					 "		</td>"+
					 "	</tr>";
	} else if ( toDo == "ModAddDelPatenteTrans" ) {
		nombre 	   = "Patente";
		datosExtra = "	<tr valign='middle'>"+
					 "		<td width='25%' height='30'>&nbsp;&nbsp; Cant. Bins</td>"+
					 "		<td width='5%'>:</td>"+
					 "		<td width='70%'>"+
					 "			<input type='text' id='bins' name='bins' value='"+param1+"' style='width:80px' maxlength='8' onKeyPress=\"SoloNumeros(this)\" onKeyUp=\"SoloNumeros(this)\" />" +
					 "		</td>"+
					 "	</tr>";
	} else if ( toDo == "ModAddDelFonoTrans" ) {
		nombre 	   = "Descripción";
		datosExtra = " <tr valign='middle'>"+
					 "		<td width='25%' height='30'>&nbsp;&nbsp; Fono</td>"+
					 "		<td width='5%'>:</td>"+
					 "		<td width='70%'>"+
					 "			<select name='fon_cod_1' class='campos' id='fon_cod_1' style='width:50px'></select>"+
					 "			<input name='fon_num_1' type='text'  class='campo' id='fon_num_1' size='8' maxlength='8' style='margin-top:5px;' />"+
					 "			<input type='hidden' id='codigo' name='codigo' value='"+param1+"' style='width:10px' maxlength='1' onKeyPress=\"SoloNumeros(this)\" onKeyUp=\"SoloNumeros(this)\"/>"+
					 "			<input type='hidden' id='numero' name='numero' value='"+param2+"' style='width:80px' maxlength='7' onKeyPress=\"SoloNumeros(this)\" onKeyUp=\"SoloNumeros(this)\"/>"+
					 "			<div class='cont_acepto_info' id='fon_num_1_info'></div>"+
			         "			<div class='cont_alerta_error' id='fon_num_1_error'></div>"+
					 "		</td>"+
					 "	</tr>";
}
	
	win.getContent().innerHTML = "<h4>Ingrese los datos solicitados:</h4><br>"+
								 "<table width='100%' border='0' align='center' cellpadding='0' cellspacing='0'>"+
								 "	<tr valign='middle'>"+
								 "		<td width='25%' height='30'>&nbsp;&nbsp; "+nombre+"</td>"+
								 "		<td width='5%'>:</td>"+
								 "		<td width='70%'>"+
								 "			<input type='text' id='descripcion_popup' name='descripcion_popup' value='"+descripcion+"' style='width:230px' maxlength='250'/>"+
								 "		</td>"+
								 "	</tr>"+
								 datosExtra +
								 "	<tr valign='middle'>"+
								 "		<td height='30'>&nbsp;&nbsp; Activado</td>"+
								 "		<td>:</td>"+
								 "		<td>"+
								 "			<input type='checkbox' id='checkbox_activado' name='checkbox_activado' value='checkbox'>"+
								 "		</td>"+
								 "	</tr>"+
								 "	<tr valign='middle'>"+
								 "		<td height='30'>&nbsp;&nbsp; Empresa</td>"+
								 "		<td>:</td>"+
								 "		<td>"+
								 "			<select name=\"id_empresa\" id=\"id_empresa\">"+select_empresa+"</select>"+
								 "		</td>"+
								 "	</tr>"+
								 "	<tr valign='middle'>"+
								 "		<td height='30'></td>"+
								 "		<td><input type='hidden' id='id_objeto' name='id_objeto' value='"+id_objeto+"'/>"+
								 "		<input type='hidden' id='toDo' name='toDo' value='"+toDo+"'/>"+
								 "		</td>"+
								 "		<td>"+
								 "			<input type='button' value='Aceptar' onclick='guardarObj()'/>&nbsp;"+
								 "			<input type='button' value='Cerrar' onclick='Windows.close(\"window_id\", event)'/>"+
								 "		</td>"+
								 "	</tr>"+
								 "	<tr valign='middle'>"+
								 "		<td align='center' colspan='4' height='30'><b><span id='msj_popup' style='color:#FF0000'></span></b></td>"+
								 "	</tr>"+
								 "</table>"; 
	win.setDestroyOnClose(); 
	win.showCenter(); 
	
	if (activado == '1' || activado == 'S') {
		$("checkbox_activado").checked = true;
	} else {
		$("checkbox_activado").checked = false;
	}
	if ( id_empresa != '0' ) {
		$("id_empresa").value = id_empresa;
	}
	$("descripcion_popup").focus();

}

//-- INI Guardar Nuevo Objeto -----------------------------------------------------------------------------------
function guardarObj() {
	var idObjeto = $("id_objeto").value;
	var accion	 = $("toDo").value;
	var descObj	 = $("descripcion_popup").value;
	var idEmpresa = $("id_empresa").value;
	var activado = "";
	var params;
	
	if ($("checkbox_activado").checked) {
		activado = "S";
	} else {
		activado = "N";
	}
	
	
	if ( accion == "ModAddDelChoferTrans" ) {
		var rut_txt = $("rut").value;
		var dv_txt = $("dv").value;
	
		if (Trim(descObj) == '') {
			alert("Debe ingresar el nombre del chofer.");
			$("descripcion_popup").focus();
			return;
		}
		if (Trim(rut_txt) == '') {
			alert("Debe ingresar el rut del chofer.");
			$("rut").focus();
			return;
		}
		if (Trim(dv_txt) == '') {
			alert("Debe ingresar el digito del chofer.");
			$("dv").focus();
			return;
		}
		//VALIDA RUT
		if ( !checkRutField(rut_txt+"-"+dv_txt) ) {
			$("rut").focus();
			return;
		}
		params = { 'id_chofer':idObjeto, 'nombre':descObj, 'activado':activado, 'rut':rut_txt, 'dv':dv_txt, 'id_empresa':idEmpresa, 'do':'' };
	
	} else if ( accion == "ModAddDelPatenteTrans" ) {
		var bins = $("bins").value;
	
		if (Trim(descObj) == '') {
			alert("Debe ingresar la Patente.");
			$("descripcion_popup").focus();
			return;
		}
		if (Trim(bins) == '') {
			alert("Debe ingresar la cantidad máxima de bins.");
			$("bins").focus();
			return;
		}
			
		params = { 'id_patente':idObjeto, 'patente':descObj, 'activado':activado, 'bins':bins, 'id_empresa':idEmpresa, 'do':'' };
		
	} else if ( accion == "ModAddDelFonoTrans" ) {
		
		var codigo1 = fon_cod_1.value;
		var numero1 = fon_num_1.value;
		var tipofono;
		var numero;
		var codigo;
		if (fon_cod_1.value==""){
			alert("Falta ingresar código de telefono");
			fon_cod_1.focus();
			return ;
		}
		if (codigo1==9){//si es celular
			
			numero= numero1.substring(numero1.length-7);
			codigo = numero1.substring(0,1);
			if (codigo<5){
				alert("El primer numero de un celular debe ser entre 5 y 9.");
				fon_num_1.value="";
				$("fon_num_1").focus();
				return;
			}	
			tipofono="celular";
		}else if (codigo1==2){//si es telefono santiago
			numero= numero1.substring(numero1.length-8);
			codigo = codigo1;
			tipofono="santiago";
		}else{//si es telefono regiones
			numero = numero1;
			codigo = codigo1;
			tipofono="region";
		}

	
		if (Trim(descObj) == '') {
			alert("Debe ingresar la descripción.");
			$("descripcion_popup").focus();
			return;
		}
		if (Trim(codigo) == '') {
			alert("Debe ingresar el código del fono.");
			$("codigo").focus();
			return;
		}
		if (Trim(numero) == '') {			
			alert("Debe ingresar el número del fono.");
			$("fon_num_1").focus();
			return;
		}			
		if ((Trim(numero)).length <7 &&(tipofono=="region")){
			alert("Debe ingresar el número de fono correctamente.");
			$("fon_num_1").focus();
			return;
		}else if((Trim(numero1)).length <8 &&(tipofono!="region")){
			alert("Debe ingresar el número de fono correctamente.");
			$("fon_num_1").focus();
			return;
		}
		params = { 'id_fono':idObjeto, 'nombre':descObj, 'activado':activado, 'codigo':codigo, 'numero':numero, 'id_empresa':idEmpresa, 'do':'' };
	}
	
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
				$("msj_popup").innerHTML = "Error al guardar";
				return;
			}
			var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
			if ( mensaje != 'OK' ) {
				$("msj_popup").innerHTML = mensaje;
				return;
			}
			if ( idObjeto == 0 ) {
				window.location.href="ViewMantenedorTransporte?mensaje=Se ha creado exitosamente";
			} else {
				window.location.href="ViewMantenedorTransporte?mensaje=Se ha modificado exitosamente";
			}
		}
	};
	new Ajax.Request(accion, requestOptions);
}
//-- FIN Guardar Nuevo Objeto -----------------------------------------------------------------------------------

//INICIO ELIMINAR UN OBJETO
function aEliminar(id_objeto, accion) {
	limpiarMsjAndWin();
	if(confirm("Está seguro de querer eliminar")) {		
		var params1 = {'id_objeto':id_objeto, 'do':'DEL'};		
		var requestOptions = {
			'method': 'post',
			'parameters': params1,
			'onSuccess': function (REQUEST) {									
				if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
					$("mensaje1").innerHTML = "No pudo ser eliminado.";
					return;
				}	
				var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
				if (mensaje != 'OK') {
					$("mensaje1").innerHTML = mensaje;
					return;
				}
				window.location.href="ViewMantenedorTransporte?mensaje=Se ha eliminado exitosamente";
			}
		};
		new Ajax.Request(accion, requestOptions);
	}	
}
//INICIO ELIMINAR UN PRODUCTO


//INICIO FUNCIONES GLOBALES
function limpiarMsjAndWin() {	
	var popup = $("window_id");
	if (popup != null) {
		Windows.close("window_id", event);
		//win.hide();
	}	
}
function SoloNumeros(oObjeto) {
	oObjeto.value = oObjeto.value.replace(/^\s*/, "").replace(/\s*$/,"");
	if (oObjeto.value.substring(oObjeto.value.length-1,oObjeto.value.length) >= 0 ) {
	
	} else {
		oObjeto.value = oObjeto.value.substring(0,oObjeto.value.length-1);
	}	
}

var globalCallbacks = {
                onCreate: function(){
					$("loading").show();
                },
                onComplete: function() {
                        if(Ajax.activeRequestCount == 0){
                                $("loading").hide();
                        }
                }
        };
Ajax.Responders.register( globalCallbacks );
//FINAL FUNCIONES GLOBALES