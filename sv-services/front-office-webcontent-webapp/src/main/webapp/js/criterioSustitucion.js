// ---- INI FUNCIONES PASO 1
function mostrarVentanaCriSus1(REQUEST) {
	//seleccionar el criterio del cliente para el producto
	var message = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
	if (message == 'OK') {
		var idProducto = REQUEST.responseXML.getElementsByTagName("id_producto")[0].childNodes[0].nodeValue;
		var idCriterio = REQUEST.responseXML.getElementsByTagName("id_criterio")[0].childNodes[0].nodeValue;
		var desCriterio = REQUEST.responseXML.getElementsByTagName("des_criterio")[0].childNodes[0].nodeValue;
		seleccionamosElCriterio(parseInt(idCriterio,10)-1);
		if ( parseInt(idCriterio,10) == 4 ) {
			$('textfield_cri_sus').value = desCriterio;
		}
		$('id_prod_sustituto').value = idProducto;
		showCriSustitucion();
	} else {
		alert(message);
	}
}
function escribioPreferencia1(checkValue) {
	if ( parseInt(checkValue,10) == 4 ) {
		if ( $('textfield_cri_sus').value == "Ej: marca, sabor" || Trim($('textfield_cri_sus').value) == "" ) {
			alert("Si seleccionó la opción 'Otro',\ndebe escribir su preferencia de sustitución.");
			$('textfield_cri_sus').focus();
			return false;
		}
	}
	return true;
}
function respuestaFinal1(REQUEST) {
	//seleccionar el criterio del cliente para el producto
	var message = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
	if (message == 'OK') {
		alert("Su preferencia ha sido guardada");
		hideCriSustitucion();
	} else {
		alert(message);
	}
}
// ---- FIN FUNCIONES PASO 1

// ---- INI FUNCIONES PASO 2
function mostrarVentanaCriSus2(xml) {
	try {
		//var message = REQUEST.getElementsByTagName("mensaje")[0].textContent;
		$j(xml).find('datos_producto').each(function() {
			var message = $j(this).find('mensaje').text();
			if (message == 'OK') {
				var idProducto  = $j(this).find('id_producto').text();
				var idCriterio  = $j(this).find('id_criterio').text();
				var desCriterio = $j(this).find('des_criterio').text();
				seleccionamosElCriterio(parseInt(idCriterio,10)-1);
				if ( parseInt(idCriterio,10) == 4 ) {
					$('textfield_cri_sus').value = desCriterio;
				}
				$('id_prod_sustituto').value = idProducto;
				showCriSustitucion();
			} else {
				alert(message);
			}
			
			
		});
	} catch(e) {}
}
function respuestaFinal2(xml) {
	try {
		$j(xml).find('datos_producto').each(function() {
			var message = $j(this).find('mensaje').text();
			if (message == 'OK') {
				alert("Su preferencia ha sido guardada");
				hideCriSustitucion();
			} else {
				alert(message);
			}
		});
	} catch(e) {}
}
function escribioPreferencia2(checkValue) {
	if ( parseInt(checkValue,10) == 4 ) {
		if ( $j('#textfield_cri_sus').val() == "Ej: marca, sabor" || Trim($j('#textfield_cri_sus').val()) == "" ) {
			alert("Si seleccionó la opción 'Otro',\ndebe escribir su preferencia de sustitución.");
			//$('textfield_cri_sus').focus();
			return false;
		}
	}
	return true;
}
// ---- FIN FUNCIONES PASO 2

// ---- INI FUNCIONES GLOBALS
function ventanaCriSustitucion(idProd, paso) {
	hideCriSustitucion();
	//trae el criterio del cliente
	pageTracker._trackPageview("FO/criterios/paso" + paso + "/" + idProd);
	var params			= { 'id_producto' : idProd };
	if ( paso == '1' ) {
		//Prototype
		new Ajax.Request('SustitutoClienteByProducto', {'method': 'post','parameters': params,'onSuccess': mostrarVentanaCriSus1});
	} else if ( paso == '2' ) {
		//JQuery
		$j.post("/FO/SustitutoClienteByProducto", params, mostrarVentanaCriSus2);
	}
}

function seleccionamosElCriterio(sel) {
	if ( sel == -1 ) sel = 0;
	//Esto no funciona del todo bien, por lo tanto lo hacemos a lo 1.0
	//Form.getInputs('f_cri_sus','radio','radiobutton_cri_sus')[parseInt(idCriterio,10)-1].checked = true;
	document.f_cri_sus.radiobutton_cri_sus[sel].checked = true;
}
function clicOtroCriSus(campo, paso) {
	if ( paso == '1' ) {
		if ( Trim(campo.value) == 'Ej: marca, sabor' ) {
			campo.value = "";
		}
	} else if ( paso == '2' ) {
		if ( jQuery.trim(campo.value) == 'Ej: marca, sabor' ) {
			campo.value = "";
		}
	}
}
function outOtroCriSus(campo, paso) {
	if ( paso == '1' ) {
		if ( Trim(campo.value) == '' ) {
			campo.value = "Ej: marca, sabor";
		}
	} else if ( paso == '2' ) {
		if ( jQuery.trim(campo.value) == '' ) {
			campo.value = "Ej: marca, sabor";
		}
	}
}
function cambioTextoCriSus(paso) {
	//Seleccionamos el radio "otros"
	if ( paso == '1' ) {
		Form.getInputs('f_cri_sus','radio','radiobutton_cri_sus')[3].checked = true;
	} else if ( paso == '2' ) {
		//falta para jquery
	}
}

function guardarSustituto(paso) {
	//Esto devuelve el check seleccionado
	var checkValue = radioChequeado();
	var params;
	if ( paso == '1' ) {
		if ( !escribioPreferencia1(checkValue) ) {
			return;
		}
		params = {'id_producto':$('id_prod_sustituto').value, 'id_criterio':checkValue, 'desc_criterio':$('textfield_cri_sus').value};
		//Prototype
		new Ajax.Request('SaveSustituto', {'method': 'post','parameters': params,'onSuccess': respuestaFinal1});
		
	} else if ( paso == '2' ) {
		if ( !escribioPreferencia2(checkValue) ) {
			return;
		}
		params = {'id_producto':$j('#id_prod_sustituto').val(), 'id_criterio':checkValue, 'desc_criterio':$j('#textfield_cri_sus').val()};
		//JQuery
		$j.post("/FO/SaveSustituto", params, respuestaFinal2);
	}
}
function radioChequeado() {
	//Esto es para Prototype
	//return Form.getInputs('f_cri_sus','radio','radiobutton_cri_sus').find(function(radio) { return radio.checked; }	).value;
	var obj = document.f_cri_sus;
	for (var i=0; i < obj.radiobutton_cri_sus.length; i++) {
		if ( obj.radiobutton_cri_sus[i].checked ) {
			return obj.radiobutton_cri_sus[i].value;
		}
	}
}
function showCriSustitucion() {
	//alert("ini showCriSustitucion");
	var layerCriSustitucion = document.getElementById('criteriosustitucion');
	layerCriSustitucion.style.visibility = 'visible';
	var ancho = screen.availWidth;
	var largo = screen.availHeight;
	var ypos = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
	layerCriSustitucion.style.width 	= '500px';
	layerCriSustitucion.style.height = '305px';
	layerCriSustitucion.style.left 	= (ancho-600)/2 + 'px';
	layerCriSustitucion.style.top 	= (largo-150)/2 + ypos - 50 + 'px';
	//alert("fin showCriSustitucion");
}
function hideCriSustitucion() {
	//alert("ini hideCriSustitucion");
	MM_showHideLayers('criteriosustitucion','','hide');
	var layerCriSustitucion = document.getElementById('criteriosustitucion');
	layerCriSustitucion.style.width = '0px';
	layerCriSustitucion.style.height = '0px';
	$('textfield_cri_sus').value = "Ej: marca, sabor";
	$('id_prod_sustituto').value = "0";
	//alert("fin hideCriSustitucion");	
}
// ---- FIN FUNCIONES GLOBALS