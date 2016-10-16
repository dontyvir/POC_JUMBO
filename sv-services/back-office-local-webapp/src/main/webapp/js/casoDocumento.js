var params = {};

var contenido_textarea = "";
var num_caracteres_permitidos = 399;

function valida_longitud() {
	var descripcion = $("texto").value;
	var lineas = (descripcion.split("\r\n")).length - 1;
	var largoDesc = descripcion.length;
	var restar = (lineas * 2);
	largoDesc = ( (largoDesc - restar) + (lineas * 4) );
	
   //var num_caracteres = $("texto").value.length;
   if (largoDesc > num_caracteres_permitidos){
      $("texto").value = contenido_textarea;
   } else {
      contenido_textarea = $("texto").value;
   }
}

function impresion() {	
	params = {	'id_caso':$("id_caso").value,'comentario':$("texto").value,
				'cooler1':$("cooler1").value,'cooler2':$("cooler2").value,'cooler3':$("cooler3").value,
				'cooler4':$("cooler4").value,'cooler5':$("cooler5").value,'cooler6':$("cooler6").value,
				'bin1':$("bin1").value,'bin2':$("bin2").value,'bin3':$("bin3").value,
				'bin4':$("bin4").value,'bin5':$("bin5").value,'bin6':$("bin6").value
			};	
	var requestOptions = {
						'method': 'post',
						'parameters': params,
						'onSuccess': function(REQUEST) {								
								if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
									alert("Existió un error de sistema o usted no tiene permisos para ejecutar AddDocBolCaso.\n\nPor favor, inténtelo más tarde.");
									return;
								}	
								var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
								if (mensaje != 'OK') {
									alert("Existió un error de sistema o usted no tiene permisos para ejecutar AddDocBolCaso.\n\nPor favor, inténtelo más tarde.");
									return;
								}
								window.print();
								window.close();
								opener.parent.location.reload();
							}
						};
	new Ajax.Request('AddDocBolCaso', requestOptions);	
}
	
function documentoHistorialBOL(idDoc,idCaso) {
	window.open ("ViewDocumentoCaso?id_caso="+idCaso+"&id_doc_bol="+idDoc,"mywindow","status=1,resizable=0,scrollbars=1,menubar=0,width=630,height=580"); 
}	
function documentoBOL() {
	var idCaso = document.getElementById("id_caso").value;
	window.open ("ViewDocumentoCaso?id_caso="+idCaso,"mywindow","status=1,resizable=0,scrollbars=1,menubar=0,width=630,height=580"); 
}	

function mostrarOcultarTablas() {
	$("tabla_retirar").show();
	$("tabla_enviar").show();
	$("tabla_documentos").show();
	$("tabla_envio_dinero").show();
	
	if ($("cantProdEnviar").value == 0) {
		$("tabla_enviar").hide();
	} 
	if ($("cantProdRetirar").value == 0) {
		$("tabla_retirar").hide();
	} 
	if ($("cantDocumentos").value == 0) {
		$("tabla_documentos").hide();
	}
	if ($("cantEnvioDinero").value == 0) {
		$("tabla_envio_dinero").hide();
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