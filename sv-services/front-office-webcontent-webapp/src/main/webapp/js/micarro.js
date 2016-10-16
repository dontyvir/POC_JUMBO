var XBROWSER = navigator.appName;
var $j = jQuery.noConflict();
var flaglista = false;
var criterio;
var aux;
var k = 0;
var anterior = 0;
var actual = 0;

function confirmaEliminarProducto(posicion, id_carro){
	if (confirm("Está seguro que desea eliminar el producto")){
		var params = "id_carro="+id_carro;
		var requestOptions = {
			'method': 'post',
			'parameters': params,
			'onSuccess': function(REQUEST) {
				if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
					alert("Ocurrió un error al tratar de eliminar el producto del carro.");
					return;
				}
				var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
				if (respuesta == "OK") {
					$j('#producto_' + posicion).remove();
					cantidad_productos += -1;
					updateTotalizador();
				} else if (respuesta == "CARRO_VACIO"){
					$j('#producto_' + posicion).remove();
					cantidad_productos += -1;
					updateTotalizador();
				    window.location.href="/FO/LimpiarMiCarro";
				} else {
					alert(respuesta);
				}	
			}		
		}
		try{mxTracker._trackEvent('MiCarro','ModificarCarro','EliminarProducto');}catch(e){};
		new Ajax.Request('EliminarProductoMiCarro', requestOptions);
	}
}

function eliminaProducto(posicion, id_carro){
		var params = "id_carro="+id_carro;
		var requestOptions = {
			'method': 'post',
			'parameters': params,
			'onSuccess': function(REQUEST) {
				if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
					alert("Ocurrió un error al tratar de eliminar el producto del carro.");
					return;
				}	
				var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
				if (respuesta == "OK") {
					$j('#producto_' + posicion).remove();
					cantidad_productos += -1;
					updateTotalizador();
				} else if (respuesta == "CARRO_VACIO"){
					$j('#producto_' + posicion).remove();
					cantidad_productos += -1;
					updateTotalizador();
				    window.location.href="/FO/LimpiarMiCarro";
				} else {
					alert(respuesta);
				}	
			}		
		}
		new Ajax.Request('EliminarProductoMiCarro', requestOptions);
}

function limpiarCarroSeccionMiCarro(){
	if (cantidad_productos > 0) {
		if (confirm("Está seguro que desea eliminar todos los productos del carro")) {
			try{mxTracker._trackEvent('Mi Carro','Modificar	carro','Limpiar');}catch(e){};
			window.location.href="/FO/LimpiarMiCarro";
		}
	} else {
		alert("Tu Carro de compras está Vacío");
	}
}

function limpiarCarro(){
	if (cantidad_productos > 0) {
		if (confirm("Está seguro que desea eliminar todos los productos del carro")) {
			try{mxTracker._trackEvent('Mi Carro','Modificar	carro','Limpiar');}catch(e){};
			window.location.href="/FO/LimpiarMiCarro";
		}
	} else {
		alert("Tu Carro de compras está Vacío");
	}
}

function guardarCarro(){
	if (cantidad_productos > 0) {
		if (id_cliente == 1) {
			showLoginSession();
		} else {
			showGuardaCarro();
		}
	} else {
		alert("Tu Carro de compras está Vacío");
	}
}

function modificaCantidad(contador, intervalo, maximo, id, precio) {
	var params = "cantidad="+$j('#producto'+contador).val()+"&id_carro="+id;
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
				alert("Ocurrió un error al modificar la cantidad del producto");
				return;
			}	
			var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
			if (respuesta != "OK") {
				alert(respuesta);
			} else {
				var valor = parseFloat($j('#producto' + contador).val()) * parseFloat(precio);
				$j('#subtotal' + contador).html(formateaPrecio(valor));
				updateTotalizador();
			}	
		}		
	}
	new Ajax.Request('ModificarCantidadProductoMiCarro', requestOptions);
}

function formateaPrecio(num){
	var numero = Math.round(num) + "";
	var numero_out = "";
    var j = 1;
    for (i = numero.length ; i > 0; i--) {
    	numero_out += numero.charAt(i - 1) + "";
    if (j != numero.length && j % 3 == 0)
       numero_out += ".";
       j++;
    }
    numero = "$";
    for (i = numero_out.length - 1 ; i >= 0; i--)
    	numero += numero_out.charAt(i);
    return numero;
}

function updateTotalizador(){
	$j.get("/FO/ActualizaTotalesMiCarro", {}
	    ,function(datos){
	    	$j("div#totalizador").html(datos);
	});
}

function add(contador, incremento, maximo, id, precio) {
	var valor = 0;
	if ($j('#producto' + contador).val() == "" || $j('#producto' + contador).val() == "undefined") {
		$j('#producto' + contador).val(0);
	}
	valor = parseFloat($j('#producto' + contador).val()) + parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	$j('#producto' + contador).val(valor);
	if ($j('#producto' + contador).val() > maximo){
		$j('#producto' + contador).val(maximo);
		alert("Solo se puede agregar un maximo de " + maximo + " productos");
	}
	modificaCantidad(contador, incremento, maximo, id, precio);
	try{mxTracker._trackEvent('MiCarro','ModificarCarro','IncrementarProducto');}catch(e){};
}

function del(contador, incremento, maximo, id, precio) {
	var valor = 0;
	if ($j('#producto' + contador).val() == "" || $j('#producto' + contador).val() == "undefined"){
		$j('#producto' + contador).val(0);
	}
	valor = parseFloat($j('#producto' + contador).val()) - parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	if( valor < 0 ) {
		$j('#producto' + contador).val(0);
	} else {
		$j('#producto' + contador).val(valor);
	}
	if ($j('#producto' + contador).val() == 0){
		if (confirm("Está seguro que desea eliminar el producto")){
			eliminaProducto(contador, id);
		} else {
			$j('#producto' + contador).val(incremento);
			modificaCantidad(contador, incremento, maximo, id, precio);
		}
	} else {
		modificaCantidad(contador,incremento, maximo, id, precio);
	}
	try{mxTracker._trackEvent('MiCarro','ModificarCarro','RestarProducto');}catch(e){};
}

function cambia_cantidad(contador, intervalo, maximo, id, precio){
	if (valida_cantidad(contador,intervalo,maximo)){
		if ($j('#producto' + contador).val() == 0)
			confirmaEliminarProducto(contador, id);
		else		
			modificaCantidad(contador, intervalo, maximo, id, precio);
	}
}

function valida_cantidad(contador,intervalo,maximo) {
	if (($j('#producto' + contador).val() != "") && (!isNaN($j('#producto' + contador).val()))) {
		if (parseFloat($j('#producto' + contador).val(),10) < 0) {
			alert("Por favor, ingrese un valor mayor a cero");
			$j('#producto' + contador).val(intervalo);
			$j('#producto' + contador).focus();
			return true;
		} else if (parseFloat($j('#producto' + contador).val(),10) == 0) {
			return true;
		} else {
			if (parseFloat($j('#producto' + contador).val(),10) > maximo) {
				if ((parseFloat(maximo,10) % intervalo) != 0) {
					var division = parseFloat(maximo,10) / parseFloat(intervalo,10);
					$j('#producto' + contador).val(parseInt(division,10) * intervalo);
				} else {
					$j('#producto' + contador).val(maximo);
				} 	
				alert("Sólo puede ingresar un máximo de " + maximo + " productos");
				return true;
			} else {
				if ((parseFloat($j('#producto' + contador).val(),10) % intervalo) != 0) {
					var division = parseFloat($j('#producto' + contador).val(),10) / parseFloat(intervalo,10);
					$j('#producto' + contador).val((Math.round( 100 * ( parseInt(division,10) * parseFloat(intervalo,10) ) )/100 ));
					return true;
				} else {
					return true
				} 
			}	 
		}			
	} else {
		alert("Por favor, ingrese valores numéricos");
		$j('#producto' + contador).val(intervalo);
		$j('#producto' + contador).focus();
		return true;
	}		
}


function selectTodos(idCategoria, posicion) {
	//posicion ->  0:Criterio Jumbo, 1:Misma marca, 2:Mismo tamano, 4:No sustituir
	if (confirm("Al cambiar el criterio de sustitución de una categoría, se modificarán todos los criterios ya elegidos para los productos de esa categoría")) {
		var prods = $j('[name^=radiobutton_' + idCategoria + '_]');
		for ( var i = 0; i < prods.length; i++ ) {
			if (prods[i].value == posicion)
				prods[i].checked = true;
		}
	}	
}

function seleccionaTodos(posicion) {
	//posicion ->  0:Criterio Jumbo, 1:Misma marca, 2:Mismo tamano, 4:No sustituir
	if (confirm("Al cambiar el criterio de sustitución del carro completo, se modificarán todos los criterios ya elegidos para todos los productos.")) {
		var prods = $j('[name^=radiobutton_]');
		for ( var i = 0; i < prods.length; i++ ) {
			if (prods[i].value == posicion)
				prods[i].checked = true;
		}
	}	
}

function seleccionaTodos2(posicion){
	//posicion ->  0:Criterio Jumbo, 1:Misma marca, 2:Mismo tamano, 4:No sustituir 
	var flag=true;
	var prods=$j('[name^=radiobutton_]');
	for ( var i = 0; i < prods.length; i++ ) {
			if (prods[i].value != posicion && prods[i].value != 6){
				flag=false;
				break;	
			}	
	}
	if(!flag){
		//desplegar lightbox
		upWeb();
		showLightBoxAlerta1();
		//$j("#myTable1").tablesorter({widgets: ['zebra'], headers: { 0: { sorter: false }}});
	
		$j(document).click(function(e){
	  		id = $j(this).val();
	  		if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
		 		 closeLBoxWin();
  		});
	}else{
		for ( var i = 0; i < prods.length; i++ ){
				prods[i].value = posicion;
				prods[i].checked = true;
		}
	}
}



function alertaCarroMasivo(posicion){
	//posicion ->  0:Criterio Jumbo, 1:Misma marca, 2:Mismo tamano, 4:No sustituir 
	var flag=true;
	var prods=$j('[name^=radiobutton_]');
	k = 0;   //k lo usamos en la funcion aceptar
	criterio = posicion;  //criterio lo ocupamos en la funcion aceptar.
	anterior = actual;
	actual = posicion;
	for ( var i = 0; i < prods.length; i++ ) {	       
	        if(prods[i].value != posicion && prods[i].checked)
	        {
	        	flag=false;
				break;	
	        }				
	}
	if(flag == false){
		//despliega alerta 1
		showLightBoxAlerta1();
  		
	}else{	
		cambiarCriterioGeneral(criterio);
		limpiaCategoriaGeneral();		
	}
		
}


function alertaPorCategoria(idCategoria, posicion) {			
	//posicion ->  0:Criterio Jumbo, 1:Misma marca, 2:Mismo tamano, 4:No sustituir 
	var contchecked = 0;
	var flag=true;
	var prods = $j('[name^=radiobutton_' + idCategoria + '_]');
	aux = idCategoria;	//lo usamos en la funcion aceptar
	criterio = posicion;  //criterio lo ocupamos en la funcion aceptar.
	k=1;	//k=1 indica estamos haciendo la validacion por categoria.
	
	for ( var i = 0; i < prods.length; i++ ) {	       
	        if(prods[i].value != posicion && prods[i].checked)
	        {
	        	flag=false;
				break;	
	        }				
	}
	if(flag == false){
		//despliega alerta 1
		showLightBoxAlerta1();						
	}else{
		cambiarCriterioGeneral(criterio);	
	}
		
}

function aceptar(opcion){

 	 if(k==1){//caso por categoria
 	 	var prods = $j('[name^=radiobutton_' + aux + '_]'); 
 	 }	
 	 else{//caso carro masivo
 	 	var prods=$j('[name^=radiobutton_]');
 	 }
 	
	 for(i=0; i< opcion.length; i++)
	 {
	 	if(opcion[i].checked)
	 	{
	 		if(opcion[i].value == 1)
	 		{
	 			//aplicar criterios a todos los productos
	 			cambiarCriterioGeneral(criterio);
				
	 		}
	 		else if(opcion[i].value == 2){
	 			cambiarCriterio(criterio);
	 		} 
	 	}
	 }
 
 //limpiamos y cerramos la ventana
 document.getElementById("formulario").reset();
 closeLBoxWin();
 	 
}

function cancelar(){
	
	if(k==0){//caso carro masivo 
		var radios = $j('[name="asigna_todo"]');	
		for( var j=0;j<radios.length;j++){
			 if(radios[j].value == anterior){				
				radios[j].checked = true;
				actual = anterior;
			 }	
		}
		
		if(anterior == 0){
			for( var j=0;j<radios.length;j++){
				radios[j].checked = false;
			}
			actual = 0;
		}
					
		closeLBoxWin();
	}
	else if(k==1){//caso por categoria							
			closeLBoxWin();
	}					
}

function limpiaCategoriaGeneral(){
	//limpiamos los criterios generales de todas las categorias
	var cat=$j('[name^=intermedia_]');
	for(var h=0; h < cat.length; h++){
		cat[h].checked = false;
	}
}

function showLightBoxAlerta1() {
	limpiarAlerta1();
	//upWeb();
	//tb_show('Jumbo.cl','#TB_inline?height=500&width=700&inlineId=hiddenAlertaUno&modal=true');   
	tb_show('Jumbo.cl','#TB_inline?height=358&width=400&inlineId=hiddenAlertaUno&modal=true');   
					
	$j(document).click(function(e){
	  	id = $j(this).val();
	  	if(e.target.id=='TB_overlay') //cierra si hace click fuera de la ventana
		 	//closeLBoxWin();
		 	cancelar();
  	});
  
}

/*Funcion encargada de desplegar alerta 2*/
function showLightBoxAlerta2(){
  tb_show('Jumbo.cl','#TB_inline?height=500&width=700&inlineId=hiddenAlerta2&modal=true');
}

/*Funcion encargada de validar si algun producto no tiene criterio seleccionado*/
function  validaCriterios(){
	var prods = $j('[name^=seleccionado_]');
	for ( var i = 0; i < prods.length; i++ ) {
		if (prods[i].value=="1"){
			//levantar ligthbox
			showAlerta2();
			return false;
		}
	}
	try{mxTracker._trackPageview ('/MiCarro/Continuar Compra/Sin Criterio Sustitucion');}catch(e){};
	return true;
}
/*Funcion encargada de cambiar valor del campo indicador de seleccion de criterio de un producto*/
function criterioSeleccionado(id){
	var prod=document.getElementById("seleccionado_"+id);
	if(prod.value=="1"){
		prod.value="2";
	}
}

/*Funcion encargada de establecer el criterio selecionado por el usuario a los productos sin criterio seleccionado*/
function cambiarCriterio(criterio){

	if(k==0){//caso carro masivo
		var prods = $j('[name^=seleccionado_]');
		criterio = $j('[name="asigna_todo"]:checked').val(); 
		for ( var i = 0; i < prods.length; i++ ) {
			if (prods[i].value=="1"){
				idCategoria = (prods[i].name.split('_'))[1];
				idProducto = (prods[i].name.split('_'))[2];
				var radios = $j('[name^=radiobutton_'+idCategoria+'_'+idProducto+']');
				for( var j=0;j<radios.length;j++){
					if (radios[j].value == criterio)
						radios[j].checked = true;
				}
				prods[i].value="2";		
			}
		}
		closeLBoxWin();
	}
	else if(k==1){//caso por categoria
		var prods = $j('[name^=seleccionado_' + aux + '_]');  
		for ( var i = 0; i < prods.length; i++ ) {
			if (prods[i].value=="1"){
				idCategoria = (prods[i].name.split('_'))[1];
				idProducto = (prods[i].name.split('_'))[2];
				var radios = $j('[name^=radiobutton_'+idCategoria+'_'+idProducto+']');
				for( var j=0;j<radios.length;j++){
					if (radios[j].value == criterio)
						radios[j].checked = true;
				}
				prods[i].value="2";		
			}
		}
		closeLBoxWin();			
		
	}		
}

function cambiarCriterioGeneral(criterio){

	if(k==0){//caso carro masivo
		var prods = $j('[name^=seleccionado_]');
		criterio = $j('[name="asigna_todo"]:checked').val(); 
		for ( var i = 0; i < prods.length; i++ ) {		
				idCategoria = (prods[i].name.split('_'))[1];
				idProducto = (prods[i].name.split('_'))[2];
				var radios = $j('[name^=radiobutton_'+idCategoria+'_'+idProducto+']');
				for( var j=0;j<radios.length;j++){
					if (radios[j].value == criterio)
						radios[j].checked = true;
				}	
				prods[i].value="2";
			
		}
		limpiaCategoriaGeneral();
		closeLBoxWin();
	}
	else if(k==1){//caso por categoria
		var prods = $j('[name^=seleccionado_' + aux + '_]');  
		for ( var i = 0; i < prods.length; i++ ) {
				idCategoria = (prods[i].name.split('_'))[1];
				idProducto = (prods[i].name.split('_'))[2];
				var radios = $j('[name^=radiobutton_'+idCategoria+'_'+idProducto+']');
				for( var j=0;j<radios.length;j++){
					if (radios[j].value == criterio)
						radios[j].checked = true;
				}	
				prods[i].value="2";
			
		}
		closeLBoxWin();			
		
	}		
}


/*Funcion encargada de establecer el criterio selecionado por el usuario a los productos sin criterio seleccionado*/
function setearCriterio(){
	var prods = $j('[name^=seleccionado_]');
	var criterio = $j('[name="criterioaplicado"]:checked').val(); 
	var criterioTA = "";
	if(criterio==1)
		criterioTA = "Criterio Jumbo";
	else
		criterioTA = "No Sustituir";
	try{mxTracker._trackEvent('MiCarro','sin criterio susticion',criterioTA);}catch(e){};
	for ( var i = 0; i < prods.length; i++ ) {
		if (prods[i].value=="1"){
			idCategoria = (prods[i].name.split('_'))[1];
			idProducto = (prods[i].name.split('_'))[2];
			var radios = $j('[name^=radiobutton_'+idCategoria+'_'+idProducto+']');
			for( var j=0;j<radios.length;j++){
				if (radios[j].value == criterio)
					radios[j].checked = true;
			}
			prods[i].value="2";		
		}
	}
	closeLBoxWin();
	//guardaMiCarro();
	guardaMiCarroSinValidarCriterios();		
}


function validaSustitutos() {
	var prods = $j('[name^=radiobutton_]');
	var idcategoria;
	var idproducto;
	for ( var i = 0; i < prods.length; i++ ) {
		if (prods[i].checked){
			if (prods[i].value == "4") {
				idCategoria = (prods[i].name.split('_'))[1];
				idProducto = (prods[i].name.split('_'))[2];
				if (($j('#text_'+ idCategoria + '_' + idProducto).val() == "") || ($j('#text_'+ idCategoria + '_' + idProducto).val() == "Ej: marca, sabor")){
					alert("debe indicar el criterio para el producto");
					try{mxTracker._trackPageview ('/MiCarro/Continuar Compra/Sin Criterio Sustitucion Producto');}catch(e){};
					document.location.href = "#ancla_" + idCategoria + '_' + idProducto;
					$j('#text_'+ idCategoria + '_' + idProducto).focus();
					return false;
				}
			}
		}
	}
	return true;
}


// BUY STEP 1: Validamos carro de compra
function ValidaCarroCompras() {
  var resp = false;
  //Antes de comprar verificamos el carro del cliente
  var requestOptions = {
    'asynchronous':false,
    'method': 'post',
    'parameters': null,
    'onSuccess': function (REQUEST) {
      if (REQUEST.responseXML != null) {
        if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] != null) {
          //en caso de q no venga el msg, al gatillar la accion de confirmar la compra el sistema mandara pantalla de error o al login
          var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
          //alert("mensaje: " + mensaje);
          if (mensaje != 'OK') {
            var cantidad = REQUEST.responseXML.getElementsByTagName("cantidad")[0].childNodes[0].nodeValue;
            //alert("cantidad: " + cantidad);
            var msg = "Tienes " + mensaje + ".\n";
            msg += "¿Deseas reemplazar" + cantidad + "?";
            //alert(msg);
            if ( confirm( msg ) ) {
              //document.location.href = "MiCarro";
              resp = false;
            }else{
              resp = true;
            }
          }else{
            resp = true;
          }
        }
        /*INDRA 2012-11-12*/
        else {
	      	resp = true;        
        }
        /*INDRA 2012-11-12*/
      }
      /*INDRA 2012-11-08*/
      else {
      	resp = true;
      }
      /*INDRA 2012-11-08*/
    }
  };
  new Ajax.Request('ValidaCarroCompras', requestOptions);
  return resp;
}

function guardaMiCarroSinValidarCriterios(){
	if (ValidaCarroCompras()){
		var prods = $j('[name^=radiobutton_]');
		var categorias = "";
		var productos = "";
		var criterios = "";
		var cat;
		var id;
		var valor;
		if (validaSustitutos()){
			for ( var i = 0; i < prods.length; i++ ) {
				if (prods[i].checked) {
					cat = (prods[i].name.split('_'))[1];
					id = (prods[i].name.split('_'))[2];
					valor = prods[i].value;
					if (productos == "") {
						categorias = categorias + cat;
						productos = productos + id;
						criterios = criterios + valor;
					} else {
						categorias = categorias + '_' + cat;
						productos = productos + '_' + id;
						criterios = criterios + '_' + valor;
					}
				}
			}
			$j('#categorias').val(categorias);
			$j('#productos').val(productos);
			$j('#criterios').val(criterios);
			document.criterios.action = "GuardaCriteriosMiCarro";
			document.criterios.submit();
		}
  	}
}

function guardaMiCarro() {
  if(validaCriterios()){
  	if (ValidaCarroCompras()){
		var prods = $j('[name^=radiobutton_]');
		var categorias = "";
		var productos = "";
		var criterios = "";
		var cat;
		var id;
		var valor;
		if (validaSustitutos()){
			for ( var i = 0; i < prods.length; i++ ) {
				if (prods[i].checked) {
					cat = (prods[i].name.split('_'))[1];
					id = (prods[i].name.split('_'))[2];
					valor = prods[i].value;
					if (productos == "") {
						categorias = categorias + cat;
						productos = productos + id;
						criterios = criterios + valor;
					} else {
						categorias = categorias + '_' + cat;
						productos = productos + '_' + id;
						criterios = criterios + '_' + valor;
					}
				}
			}
			$j('#categorias').val(categorias);
			$j('#productos').val(productos);
			$j('#criterios').val(criterios);
			document.criterios.action = "GuardaCriteriosMiCarro";
			document.criterios.submit();
		}
  	}
  }
}

function changeComuna1() {
	var nombreComuna;
	  if ( $j('#id_region').val() == "0" || $j('#id_comuna').val() == "0" ) {
		  if ( $j('#localretiro').val() == "0" ){
	    alert("Seleccione su región y comuna o Local de Retiro");
	    try{mxTracker._trackEvent('SeleccionRegionComuna','Alerta','Region&Comuna');}catch(e){};
	    return;
		  }
	  }
  dataLayer.push({
	     'Region': $j("#id_region option:selected").html(),
	     'Comuna': $j("#id_comuna option:selected").html(),
	     'event': 'Seleccion-Region-Comuna'
	});
  if ($j("#id_comuna").val()!="0"){
		comuna=$j("#id_comuna").val();
	} else if ($j("#localretiro").val()!="0"){
		nombreComuna=$j("#localretiro option:selected").text();
		comuna=$j("#localretiro").val();
	}
  $j.post("/FO/ChangeComunaSession", {id_comuna: comuna, nombre_comuna:nombreComuna}, function(datos){
    $j(datos).find('comuna').each(function() {
      var respuesta  = $j(this).find('mensaje').text();
      $j('#comuna_user_str').html( $j(this).find('nombre_comuna').text() );
      $j('#comuna_user_id').val( $j(this).find('id_comuna').text() );      
      if ( respuesta == "OK" ) {
        closeLBoxWin();
        window.location.href="/FO/MiCarro";
      }
    });
  });
}

function getProductos(){
//	var params = "filtro=" + $j('input:radio[name=filtro]:checked').val();
	var params = "filtro=" + $j('#filtro').val();
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			$j('#carro').html(REQUEST.responseText);
		}		
	}
	new Ajax.Request('AjaxMiCarro', requestOptions);
}


function showRegionesCobertura() {
  showChangeComuna();  
  //traemos las regiones
  $j.get("/FO/RegionesConCobertura",function(datos) {
    $j("#id_region").html(datos);
    //TODO aca seleccionar combo de region
  });
}
function getComunasByRegion(idRegion) {
  if ( idRegion == 0 ) {
    return;
  }
  $j.get("/FO/ComunasConCoberturaByRegion",{id_region:idRegion},function(datos) {
    $j("#id_comuna").html(datos);
    $j("#id_comuna").removeAttr('disabled');
  });
}


function closeLBoxWin() {
  if ( document.getElementById("ingreso_clientes") != null && document.getElementById("ingreso_clientes_lbox") != null ) {
    var norm = document.getElementById("ingreso_clientes");
    var box = document.getElementById("ingreso_clientes_lbox");
    box.style.visibility="hidden";
    box.style.width="0px";
    box.style.height="0px";
    norm.style.display="block";
    norm.style.visibility="visible";
    norm.style.width="230px";
    norm.style.height="306px";
  }
  tb_remove();  
}
function show_lbox() {
  var norm = document.getElementById("ingreso_clientes");
  var box = document.getElementById("ingreso_clientes_lbox");
  norm.style.visibility="hidden";
  norm.style.width="0px";
  norm.style.height="0px";
  box.style.display="block";
  box.style.visibility="visible";
  box.style.width="230px";
  box.style.height="306px";
}

function getRecuperaClave1() {
  if ( $j('#cli_rut_recupera').val() == "" ) {
    alert("Por favor, ingrese su Rut.");
    return;
  }
  $j.post("/FO/ChangeComunaSession", {id_comuna: $j("#id_comuna").val()}, function(datos){
    $j(datos).find('comuna').each(function() {
      var respuesta  = $j(this).find('mensaje').text();
      $j('#comuna_user_str').html( $j(this).find('nombre_comuna').text() );
      $j('#comuna_user_id').val( $j(this).find('id_comuna').text() );
      
      if ( respuesta == "OK" ) {
        if ( XACT_CARRO == "ADD_CARRO" ) {
          validaProductoLocal();
        } else {
          window.location.reload();
        }
      }
    });
  });
}

function showChangeComuna() {
  upWeb();
  showLightBoxCategories();
  $j('#comunas_sesion').show();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  //contenedor alerta 2
  //$j('#contenedor_alerta2').hide();
}

function showChangeComunaMsj() {
  upWeb();
  greenBack();
  $j('#change_comuna_layer_msj').show();
  //$j('#contenedor_alerta2').hide();
  $j('#comunas_sesion').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  //contenedor alerta 2
  //$j('#contenedor_alerta2').hide();
}

/*function showLoginSession() {
  upWeb();
  showLightBoxCategories();  
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').show();
  $j('#olvido_clave').hide();
}*/

function showRecuperaClave() {
  upWeb();
  showLightBoxCategories();  
  $j('#olvido_clave').show();  
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  //contenedor alerta 2
  //$j('#contenedor_alerta2').hide();
}

function whiteBack() {
  $j('#TB_ajaxContent').css({backgroundImage:'url("/FO_IMGS/images/html/fondo_layer_recupera.gif")'});
}
function greenBack() {
  $j('#TB_ajaxContent').css({backgroundImage:'url("/FO_IMGS/images/html/fondo_layer_categorias.gif")'});
}

function justLogin() {
  document.acceso2.url_to.value = "/FO/MiCarro";
  showLoginSession();
}

function showGuardaCarro() {
  upWeb();
  tb_show('Jumbo.cl','#TB_inline?height=358&width=400&inlineId=guardacarroLBOX&modal=true');  
  $j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay')
		  closeLBoxWin();
  });
}

/*Funcion encargada de levantar el lightbox de la alerta 2*/
function showAlerta2() {
	setearAlerta2();
	//upWeb();
	tb_show('Jumbo.cl','#TB_inline?height=358&width=400&inlineId=hiddenModalContent2&modal=true');
	try{mxTracker._trackPageview ('/MiCarro/Continuar Compra/Sin Criterio Sustitucion General');}catch(e){};
  	/*showLightBoxCategories(); 
  	//contenedor alerta 2
  	$j('#contenedor_alerta2').show(); 
  	$j('#guardacarro').hide();
  	$j('#contenedor_lightbox').hide();
  	$j('#registra_cliente').hide();  
  	$j('#opciones_registro').hide();  
  	$j('#olvido_clave').hide();  
  	$j('#comunas_sesion').hide();
  	$j('#change_comuna_layer_msj').hide();
  	$j('#change_login_session').hide();*/
  	$j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
		  closeLBoxWin();
		  //$j('#contenedor_alerta2').hide();
  });
}

/*Funcion encargada de setear criterio jumbo como opcion seleccionada por defecto*/
function limpiarAlerta1(){
	var criteriosAlerta = $j('[name^=opcion]'); 
	for( var i=0;i<criteriosAlerta.length;i++){
		if (criteriosAlerta[i].value == 1)
			criteriosAlerta[i].checked = true;
	}
}

/*Funcion encargada de setear criterio jumbo como opcion seleccionada por defecto*/
function setearAlerta2(){
	var criteriosAlerta = $j('[name^=criterioaplicado]'); 
	for( var i=0;i<criteriosAlerta.length;i++){
		if (criteriosAlerta[i].value == 1)
			criteriosAlerta[i].checked = true;
	}
}

function upWeb() {
  if ( XBROWSER == "Microsoft Internet Explorer" ) {
    window.scrollTo(0,0);
  }
}

function showLightBoxCategories() {
  tb_show('Jumbo.cl','#TB_inline?height=358&width=400&inlineId=hiddenModalContent&modal=true');  
}

function guardanombre() {
	if ($j('#nombrecarro').val() == 'Ej: compra mensual'){
		alert('Debes ingresar el nombre de la lista');
		$j('#nombrecarro').focus();
		return;
	} else if ($j('#nombrecarro').val() == ''){
		alert('Debes ingresar el nombre de la lista');
		$j('#nombrecarro').focus();
		return;
	}
    var requestOptions = {
			'method': 'post',
			'parameters': "nombre=" + $j('#nombrecarro').val(),
			'onSuccess': validaNombre
	};
	new Ajax.Request('/FO/SaveList', requestOptions);
}

function validaNombre(REQUEST){
	if (REQUEST.responseXML.getElementsByTagName("mensaje")[0] == null) {
		document.getElementById("error").innerHTML = "Ocurrió un error al almacenar la lista";
		$j('#nombrecarro').focus();
		return;
	}
	var mensaje = REQUEST.responseXML.getElementsByTagName("mensaje")[0].childNodes[0].nodeValue;
	if (mensaje == 'OK') {
		closeLBoxWin();
		//window.location.href="respuestaGuardarLista.html";
		return;
	} else {
		alert(mensaje);
		$j('#nombrecarro').focus();
		return;
	}
}

function showCriterios(numero){
	$j('#lista_criterios' + numero).show();
	$j('#flecha_criterio' + numero).attr("src","/FO_IMGS/img/arrow2.gif");
	$j('#link_criterio' + numero).attr("href", "javascript:hideCriterios(" + numero + ");");
	
}

function hideCriterios(numero){
	$j('#lista_criterios' + numero).hide();
	$j('#flecha_criterio' + numero).attr("src","/FO_IMGS/img/arrow.gif");
	$j('#link_criterio' + numero).attr("href", "javascript:showCriterios(" + numero + ");");
}


