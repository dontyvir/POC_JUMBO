function listarSubCategorias(value) {
	new Ajax.Updater('subcategorias', 'SubCategorias', {
		parameters: { categorias: value }
	});
}
function buscador(){
	buscar = document.p2.elements["buscar"].value;
	idCategoria = document.p2.elements["categorias"].value;
	idSubCategoria = document.p2.elements["subcategorias"].value;
	if(Trim(buscar) != ""){
		accionActual = "buscar";
	} else {
		if (idSubCategoria == 0){
			alert("Ingrese una o más palabras o seleccione una subcategoria");
			return;
		}
		accionActual = "mostrar";
	}
	verFotos = document.p2.elements["ver_fotos"].checked;
	params = "accion=" + accionActual + "&idCategoria=" + idCategoria + "&idSubCategoria=" + idSubCategoria + "&buscar=" + buscar + "&ver_fotos=" + verFotos;
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			$('resultado').innerHTML = REQUEST.responseText;
			initLightbox();
		}
	};
	new Ajax.Request('PasoDosResultadoFormMobi', requestOptions);
}
function mostrar(value){
	idCategoria = document.p2.elements["categorias"].value;
	verFotos = document.p2.elements["ver_fotos"].checked;	
	params = "accion=mostrar&idCategoria=" + idCategoria + "&idSubCategoria=" + value + "&ver_fotos=" + verFotos;
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			$('resultado').innerHTML = REQUEST.responseText;
			initLightbox();
		}
	};
	new Ajax.Request('PasoDosResultadoFormMobi', requestOptions);
}
function ordenarPor(value){
	buscar = document.p2.elements["buscar"].value;
	idMarca = document.p2.elements["marca"].value;
	idCategoria = document.p2.elements["categorias"].value;
	idSubCategoria = document.p2.elements["subcategorias"].value;
	accion = document.p2.elements["ultima_accion"].value;
	verFotos = document.p2.elements["ver_fotos"].checked;	
	params = "accion="+accion+"&idCategoria=" + idCategoria + "&idSubCategoria=" + idSubCategoria 
		   + "&buscar=" + buscar + "&ordenar_por=" + value + "&ver_fotos=" + verFotos + "&idMarca=" + idMarca;
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			$('resultado').innerHTML = REQUEST.responseText;
		}
	};
	new Ajax.Request('PasoDosResultadoFormMobi', requestOptions);
}

function filtrarMarca(value){
	buscar = document.p2.elements["buscar"].value;	
	idMarca = document.p2.elements["marca"].value;
	idCategoria = document.p2.elements["categorias"].value;
	idSubCategoria = document.p2.elements["subcategorias"].value;
	accion = document.p2.elements["ultima_accion"].value;
	verFotos = document.p2.elements["ver_fotos"].checked;	
	params = "accion="+accion+"&idCategoria=" + idCategoria + "&idSubCategoria=" + idSubCategoria 
		   + "&buscar=" + buscar + "&ordenar_por=" + value + "&ver_fotos=" + verFotos + "&idMarca=" + idMarca;
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			$('resultado').innerHTML = REQUEST.responseText;
		}
	};
	new Ajax.Request('PasoDosResultadoFormMobi', requestOptions);
}

function mostrarCantidad(value){
	buscar = document.p2.elements["buscar"].value;
	idMarca = document.p2.elements["marca"].value;	
	idCategoria = document.p2.elements["categorias"].value;
	idSubCategoria = document.p2.elements["subcategorias"].value;
	accion = document.p2.elements["ultima_accion"].value;
	verFotos = document.p2.elements["ver_fotos"].checked;	
	params = "accion="+accion+"&idCategoria=" + idCategoria + "&idSubCategoria=" + idSubCategoria 
	       + "&buscar=" + buscar + "&mostrar_cantidad=" + value + "&ver_fotos=" + verFotos + "&idMarca=" + idMarca;
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			$('resultado').innerHTML = REQUEST.responseText;
		}
	};
	new Ajax.Request('PasoDosResultadoFormMobi', requestOptions);
}
function sigAnt(value){
	buscar = document.p2.elements["buscar"].value;
	idMarca = document.p2.elements["marca"].value;		
	idCategoria = document.p2.elements["categorias"].value;
	idSubCategoria = document.p2.elements["subcategorias"].value;
	accion = document.p2.elements["ultima_accion"].value;
	verFotos = document.p2.elements["ver_fotos"].checked;		
	params = "accion="+accion+"&idCategoria=" + idCategoria + "&idSubCategoria=" + idSubCategoria 
	       + "&buscar=" + buscar + "&start=" + value + "&ver_fotos=" + verFotos + "&idMarca=" + idMarca;
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			$('resultado').innerHTML = REQUEST.responseText;
			initLightbox();
		}
	};
	new Ajax.Request('PasoDosResultadoFormMobi', requestOptions);
}
function agregarCarro(id, cantidad, imagen){
	params = "productoId="+id+"&cantidad=" + cantidad;
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			if(REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null)
				return;
			var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue
			if(respuesta == 'ok'){
				actualizaCarro();
				if (imagen == 'agregar') {
					$('imagen_'+id).src = '/FO_IMGS/img/estructura/paso2/modificar.gif';
				}
			}
		}
	};
	new Ajax.Request('PasoDosAgregarCarro', requestOptions);
}
function submit_form(imagen, id ) {
	cantidad = $('cantidad_'+id).value;
	agregarCarro(id, cantidad, imagen);
}
function borrar() {
	$('buscar').value = "";
}