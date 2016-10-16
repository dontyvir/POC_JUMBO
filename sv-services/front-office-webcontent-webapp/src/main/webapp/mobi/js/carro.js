function cambiaPaginacion(paginacion) {
	window.location = "OrderItemDisplayMobi?paginado="+paginacion+"&ver_foto_txt="+$('ver_foto_txt').value;
}
function cambioVerFoto() {
	if ($('ver_foto').checked) {
		$('ver_foto_txt').value = "1";
	} else {
		$('ver_foto_txt').value = "0";
	}
	window.location = "OrderItemDisplayMobi?paginado="+$('cant_reg').value+"&pagina="+$('pagina').value+"&ver_foto_txt="+$('ver_foto_txt').value;
}
function mc(carr_id, campo, maximo, idProd) {
	var pag = 1;
	var regXpag = 5;
	if ($('pagina') != null) {
		pag = $('pagina').value;
	}
	if ($('paginado') != null) {
		regXpag = $('paginado').value;
	}
	if( validar_cantidad(campo, maximo) == false ) {
		document.fc.reset();
		return;
	}
	cantidadaux = campo.value;
	if (cantidadaux == 0 ) {
		if (confirm("Este producto será eliminado del carro de compra\n ¿Esta seguro de realizar la operación?")) {
			var d = new Date();
			var requestOptions = {
				'method': 'post',
				'parameters': "id_producto="+idProd+"&carr_id0="+carr_id+"&cantidad0=0&registros=1&tm="+d.getTime()+"&ver_foto_txt="+$('ver_foto_txt').value,
				'onSuccess': function(REQUEST) {
					if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
						alert("Ocurrió un error al eliminar el producto");
						return;
					}
					var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
					if (mensaje == 'OK') {
						alert("El producto se ha eliminado exitosamente");
						if($('carro_compra_'+carr_id) != null) {
							$('carro_compra_'+carr_id).remove();
						}
					} else {
						alert(mensaje);
					}
					actualizaCarro();
				}
			};
			new Ajax.Request('OrderItemUpdateMobi', requestOptions);
		}
	} else {
		var d = new Date();
	    window.location = "OrderItemUpdateMobi?id_producto="+idProd+"&carr_id0="+carr_id+"&cantidad0="+cantidadaux+"&registros=1&tm="+d.getTime()+"&pagina="+pag+"&paginado="+regXpag+"&ver_foto_txt="+$('ver_foto_txt').value;
	}
}
function del( campo, incremento, maximo, id, idProd ) {
	var valor = 0;
	if (campo.value == "" || campo.value == "undefined") {
		campo.value = 0;
	}	
	valor = parseFloat(campo.value) - parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	if( valor < 0 ) {
		campo.value = 0;
	} else {
		campo.value = valor;
	}
	mc(id,campo,maximo,idProd);
}
function showNota(prod, carro, cont) {
	$('nota_txt').value = $('nota'+cont).value;
	$('id_carro').value = carro;
	$('id_producto').value = prod;
	$('contador').value = cont;
	var ancho = document.documentElement.scrollWidth; // screen.availWidth;
	var largo = document.documentElement.scrollHeight; // screen.availHeight;
	var ypos = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
	$('notas').style.width = '200px';
	$('notas').style.height = '70px';
	$('notas').style.left = (ancho-200)/2 + 'px';
	$('notas').style.top = (largo-70)/2 + 'px';
	$('notas').style.visibility = 'visible';
	$('notas').show();
	$('nota_txt').focus();
}
function ocultaNotas() {
	$('id_carro').value = "";
	$('id_producto').value = "";
	$('nota_txt').value = "";
	$('contador').value = "";
	$('notas').style.width = '0px';
	$('notas').style.height = '0px';	
	$('notas').style.visibility = 'hidden';	
	$('notas').hide();
}
function guardarNota() {
	var requestOptions = {
		'method': 'post',
		'parameters': "id_producto="+$('id_producto').value+"&id_carro="+$('id_carro').value+"&nota="+$('nota_txt').value,
		'onSuccess': function(REQUEST) {
			if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
				alert("Ocurrió un error al modificar el carro");
				return;
			}
			var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
			if (mensaje == 'OK') {
				$('nota' + $('contador').value ).value = $('nota_txt').value;
			} else {
				alert(mensaje);
			}
			if ( $('nota_txt').value != "") {
				$('ImageComent_' + $('contador').value ).src = '/FO_IMGS/images/mobi/notasb.gif';
			} else {
				$('ImageComent_' + $('contador').value ).src = '/FO_IMGS/images/mobi/notas.gif';
			}
			ocultaNotas();
		}
	};
	new Ajax.Request('UpdateNotaCarro', requestOptions);	
}
function cancelarNota() {
	ocultaNotas();
}
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
function validar_cantidad(campo, maximo) {
	valcampo = campo.value;
	if ((valcampo != "") && (!isNaN(valcampo))) {
		if ( parseFloat(valcampo) > parseFloat(maximo) ) {
			alert("Sólo se pueden agregar hasta "+ maximo + " productos");
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
function valida_cantidad(nombre,intervalo,maximo){
	var campo = document.getElementById(nombre);
	if ((campo.value != "") && (!isNaN(campo.value))){
		if (parseFloat(campo.value,10) <= 0) {
			alert("Por favor, ingrese un valor mayor a cero");
			campo.focus();
			campo.select();
			return false;
		} else {
			if (parseFloat(campo.value,10) > maximo) {
				if ((parseFloat(maximo,10) % intervalo) != 0) {
					var division = parseFloat(maximo,10) / parseFloat(intervalo,10);
					campo.value = parseInt(division,10) * intervalo;
				} else {
					campo.value = maximo;
				} 	
				alert("Sólo puede ingresar un máximo de " + maximo + " productos. \nSi es un cliente empresa, llame al 600 400 3000 para solicitar mayor cantidad de productos");
				return false;
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