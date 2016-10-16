var $j = jQuery.noConflict();
var ACTIVAGUARDAR=0;

function getImagenProd() {
	return PATH_IMG + '/1x1trans.gif';
}
	

function previsualizar() {
	var imagen = $j("#imagen").val();
	if ( imagen.length <= 4 ) {
		alert("Debe ingresar un nombre válido de archivo.");
		$j("#imagen").focus();
	} else {
		var exten = imagen.substring(imagen.length-4, imagen.length);
		if (exten == '.gif' || exten == '.GIF' || exten == '.jpg' || exten == '.JPG') {
			$j("#btn_aceptar").disabled = false;
			$j('#img_prod').attr('src', PATH_IMG + "/" + $j("#imagen").val());
		} else {
			alert("La extensión del archivo no es válido.");
			$j("#imagen").focus();
		}
	}
}
function previsualizarPromocion() {
	var imagen = $j("#imagen").val();
	if ( imagen.length <= 4 ) {
		ACTIVAGUARDAR=0;
				$j("#btn_aceptar").attr('disabled', 'disabled');//.disabled = false;
		alert("Debe ingresar un nombre válido de archivo.");
		$j("#imagen").focus();
	} else {
		var exten = imagen.substring(imagen.length-4, imagen.length);
		if (exten == '.gif' || exten == '.GIF' || exten == '.jpg' || exten == '.JPG') {
			$j('#img_prom').attr('src', PATH_IMG + "/" + $j("#imagen").val());
			ACTIVAGUARDAR=1;
			if ($j("#id_producto").val()!="0" && ACTIVAGUARDAR==1)
				$j("#btn_aceptar").attr('disabled', '');//.disabled = false;
			else
				$j("#btn_aceptar").attr('disabled', 'disabled');//.disabled = false;
		} else {
			ACTIVAGUARDAR=0;
				$j("#btn_aceptar").attr('disabled', 'disabled');//.disabled = false;
			alert("La extensión del archivo no es válido.");
			$j("#imagen").focus();
		}
	}
}

function previsualizarProducto() {
	var imagen = $j("#sap").val();
	
	$j.get("/JumboBOCentral/ConsultaProductoSAP",{codsap:  imagen},
    function(datos) {
			$j("div#div_detalle_producto").html(datos);
//			ACTIVAGUARDAR=ACTIVAGUARDAR+1;
			if ($j("#id_producto").val()!="0" && ACTIVAGUARDAR==1){
			alert($j("#id_producto").val());
				$j("#btn_aceptar").attr('disabled', '');//.disabled = false;
			}
			else
				$j("#btn_aceptar").attr('disabled', 'disabled');//.disabled = false;
//			$j("label#cat").html("Resultados Búsqueda: " + texto);
//			eventosProductos();
//			$j('#loading').hide();
  });
	
}
function validaFormulario() {
	if ( $j('#nombre').val().replace(/^\s*/, "").replace(/\s*$/,"") == '' ) {
		alert("Debe ingresar el nombre del producto");
		$j('#nombre').focus();
		return false;
	}
	if ( $j('#descripcion').val().replace(/^\s*/, "").replace(/\s*$/,"") == '' ) {
		alert("Debe ingresar la descripción del producto");
		$j('#descripcion').focus();
		return false;
	}
	if ( $j('#precio').val().replace(/^\s*/, "").replace(/\s*$/,"") == '' ) {
		alert("Debe ingresar la descripción del precio");
		$j('#precio').focus();
		return false;
	}
	if ( $j('#fc_inicio').val().replace(/^\s*/, "").replace(/\s*$/,"") == '' ) {
		alert("Debe ingresar la fecha de inicio");
		$j('#fc_inicio').focus();
		return false;
	}
	if ( $j('#fc_termino').val().replace(/^\s*/, "").replace(/\s*$/,"") == '' ) {
		alert("Debe ingresar la fecha de término");
		$j('#fc_termino').focus();
		return false;
	}	
	if (!comparafechas($j('#fc_inicio'),$j('#fc_termino'))) {
		alert("La fecha de término debe ser mayor o igual a la fecha de inicio.");
		return false;
	}
	if ( $j('#id_prod_carrusel').val() == '0' ) {
		if ( $j('#imagen').val().replace(/^\s*/, "").replace(/\s*$/,"") == '' ) {
			alert("Debe seleccionar la imagen");
			return false;
		}
	}
	return true;	
}

function guardar() {
//	if ( validaFormulario() ) {
		document.f1.submit();
//	}
}

function comparafechas(campo1,campo2) {
  var fecha1 = campo1.value.substring(6,10) + campo1.value.substring(3,5) + campo1.value.substring(0,2);
  var fecha2 = campo2.value.substring(6,10) + campo2.value.substring(3,5) + campo2.value.substring(0,2);
  if (parseInt(fecha2,10) >= parseInt(fecha1,10)) {
    return true;
  } else {
    return false;
  }
}
var CONTENIDO_TEXTAREA = "";
var NRO_FILAS_MAX = 4;
var MAX_CARACTERES = 70;

function validaLongitud() {
	var descripcion = $j('#descripcion').val();
	var lineas = descripcion.split("\r\n");	
	if ( lineas.length > NRO_FILAS_MAX ) {
		$j("#descripcion").val() = CONTENIDO_TEXTAREA;
	} else {
		if ( $j("#descripcion").val().length > MAX_CARACTERES ) {
			$j("#descripcion").val() = CONTENIDO_TEXTAREA;
		} else {
			CONTENIDO_TEXTAREA = $j("#descripcion").val();
		}
	}
}

var globalCallbacks = {
                onCreate: function() {
						$j("#tabla_loading").style.top = document.documentElement.scrollTop + 'px';
                        $j("#loading").show();
                },
                onComplete: function() {
                        if(Ajax.activeRequestCount == 0){
                                $j("#loading").hide();
                        }
                }
        };
Ajax.Responders.register( globalCallbacks );


//FINAL FUNCIONES GLOBALES
