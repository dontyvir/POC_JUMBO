function consultar() {
	var cantreg = document.form1.cantreg.value;
	contlocalchk = 0;
	str = '';
	coma = '';
	for (var i=0; i < cantreg ; i++) {
		//if (document.form1.elements["idult_"+i]) {
			if (document.form1.elements["idult_"+i].checked == true){
				contlocalchk++;	
				str = str  +  coma + document.form1.elements["idult_"+i].value;
				coma = ',';
			}
		//}
	}
	document.form1.tupla.value = str;
	//recorre los productos que ya han sido seleccionados
	var seleccionados="";
	var flag = 0;
	if (document.fp != undefined) {
		flag = 1
		var cant = document.fp.elements.length;
		var aux = 0;
		for ( var i=0; i < cant; ++i) {
			if ( (document.fp.elements[i].type == "checkbox") && (document.fp.elements[i].name.substring(0,3) == "id_")) {
				if (document.fp.elements[i].checked) {
					if (aux == 0) {
						seleccionados = document.fp.elements[i].value;
						aux++;
					} else {
						seleccionados = seleccionados + ',' + document.fp.elements[i].value;
						aux++;
					}
				}	
			} 
		}
	}	
	if (flag == 1) {
		params = "tupla=" + str + "&seleccionados=" + seleccionados;
	} else {
		params = "tupla=" + str;
	}	
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			$('prod_ult_compras').innerHTML = REQUEST.responseText;
		}
	};
	new Ajax.Request('UltComprasProListMobi', requestOptions);
}

function selectall() {
    var elementos = document.fp.elements;
    for ( var i=0; i < elementos.length; ++i) {
    	if (document.forms['fp'].elements[i].name.indexOf("id_prod_") != -1) {
	    	if (document.getElementById("todos").checked == true) {
		  		document.forms['fp'].elements[i].checked = true;
		    } else {
			    document.forms['fp'].elements[i].checked = false;
			}
		}		    
    }
}

function eliminar_lista(id,nombre) {
	if (confirm("¿Está seguro que desea eliminar la lista " + nombre + "?")) {
		var requestOptions = {
			'method': 'post',
			'parameters': "id_lista=" + id,
			'onSuccess': function(REQUEST) {
				if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
					alert("Ocurrió un error al eliminar la lista");
					return;
				}
				var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
				if (mensaje == 'OK') {
					alert("La lista se ha eliminado exitosamente");
					window.location.href="UltComprasFormMobi";
					return;
				} else {
					alert(mensaje);
					return;
				}			
			}
		};
		new Ajax.Request('DeleteList', requestOptions);		
	}
}

function agregar_carro() {
    var f_listas = document.fp;
	var elementos = document.fp.elements;
	cont = 0;
	for ( var i = 0 ; i < elementos.length; ++i) {
		if (document.forms['fp'].elements[i].name.indexOf("id_prod_") != -1) {
			if (document.forms['fp'].elements[i].checked == true) {
				cont = 1;
			}
		}		    
	}
	if ( document.getElementById("todos").checked == false ) {
		document.getElementById("todos").checked = true;
	}
	f_listas.submit();
}