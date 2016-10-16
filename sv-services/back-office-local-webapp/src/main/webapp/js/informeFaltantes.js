var $j = jQuery.noConflict();

jQuery().ready(function(){

	$j("#btnFecha input.dateField").dynDateTime({
			ifFormat: "%Y-%m-%d", 
			button: ".next()"
	});
});

// Function descargaExcelFaltantes


function descargarExcel(){
	var indiceHorario = document.getElementById("selectJornadas").selectedIndex;
	var txtHorario = document.getElementById("selectJornadas")[indiceHorario].text;
	var jornadaActual = document.getElementById("txtJornadaActual").value;
	var fechaConsulta = document.getElementById("txtFechaActual").value;
	var indicadorJornada = document.getElementById("selectJornadas").value;
	document.location.href = "InformeFaltantesExcel?jornadaActual=" + jornadaActual + "&fechaConsulta=" + fechaConsulta + "&indicadorJornada=" + indicadorJornada + "&txtHorario=" + txtHorario;
}

function validarDescarga(){
	if(document.getElementById("opcion1").checked){
		if (document.getElementById("txtJornadaActual").value == ''){
			alert("Debe ingresar el número de Jornada");
		}else{
			descargarExcel();
		}
	}
	else {
		if(document.getElementById("txtFechaActual").value == '' || document.getElementById("selectJornadas").value == 0){
			alert("Debe seleccionar Fecha y Jornada");
		}else {
			descargarExcel();
		}
	}
}


function validarGeneracion(){
	if(document.getElementById("opcion1").checked){
		if (document.getElementById("txtJornadaActual").value == ''){
			alert("Debe ingresar el número de Jornada");
		}else{
			generarInformeFaltantes();
		}
	}
	else {
		if(document.getElementById("txtFechaActual").value == '' || document.getElementById("selectJornadas").value == 0){
			alert("Debe seleccionar Fecha y Jornada");
		}else {
			generarInformeFaltantes();
		}
	}
}

function generarInformeFaltantes(){
	var fechaConsulta = document.getElementById("txtFechaActual").value;
	var horarioConsulta = document.getElementById("selectJornadas").value;
	var indiceHorario = document.getElementById("selectJornadas").selectedIndex;
	var txtHorario = document.getElementById("selectJornadas")[indiceHorario].text;
	var jornadaConsulta = document.getElementById("txtJornadaActual").value;

	var params = {'txtHorario':txtHorario,'fechaConsulta': fechaConsulta, 'horarioConsulta':horarioConsulta, 'jornadaConsulta':jornadaConsulta};
	var requestOptions 	= {
		'method': 'post',
		'parameters': params,
		'asynchronous': false,
		'onSuccess': function (REQUEST) {
			if (REQUEST.responseXML != null && REQUEST.responseXML.text != '') {
	        	if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] != null) {
	        		var error = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
	        		alert(error);
	        	}
        	}
		    else {
				respuesta = REQUEST.responseText;
				var windowPopup = window.open('','Informe_Faltantes');//,'toolbar=no,directories=no,menubar=no,status=no');
				windowPopup.document.write(respuesta);
				windowPopup.focus();
				
				
				//verNewWindow(respuesta);
				
			}
		}
	};
	new Ajax.Request('InformeFaltantesGenerar', requestOptions);
	
}

function verNewWindow(respuesta){
	windowPopup = window.open('','Informe Faltantes');//,'toolbar=no,directories=no,menubar=no,status=no');
	windowPopup.document.write(respuesta);
	windowPopup.focus();
}

function bloqueaInputs(){
	if(document.getElementById("opcion1").checked){
		document.getElementById("txtJornadaActual").disabled = false;
		document.getElementById("txtFechaActual").disabled = true;
		document.getElementById("selectJornadas").disabled = true;
		document.getElementById("btnSelectFecha").disabled = true;
		
		
	}else {
		document.getElementById("txtJornadaActual").value="";
		document.getElementById("txtJornadaActual").disabled = true;
		document.getElementById("txtFechaActual").disabled = false;
		document.getElementById("selectJornadas").disabled = false;
		document.getElementById("btnSelectFecha").disabled = false;
		
	}
}
