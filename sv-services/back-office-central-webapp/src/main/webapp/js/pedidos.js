function guardarProductoConSustituto() {
	var id_cri = 1;
	for ( var i=1; i <= 5; i++ ) {
		if ($('rbtn_sustituto' + i).checked) {
			id_cri = i;
			if (id_cri == 4) {
				if (Trim($("desc_categoria").value) == '') {
					alert("Debes ingresar la descripción del sustituto.");
					$("desc_categoria").focus();
					return;
				}
			}
		}
	}
	$('id_criterio_seleccionado').value = id_cri;
	$('desc_criterio_seleccionado').value = $("desc_categoria").value;
	$("id_prod").disabled = false;
	document.form1.submit();
	cerrarVentana();
	$("id_prod").disabled = true;
}

function agregarProducto() {
	if (Trim($("id_prod").value) == '') {
		alert("Debes ingresar el ID del producto.");
		$("id_prod").focus();
		return;
	}
	if (Trim($("cant").value) == '') {
		alert("Debes ingresar la cantidad del producto.");
		$("cant").focus();
		return;
	}
	if (!validar_prod(document.form1)) {
		return;
	}
	ventanaSustitutos();
}


function ventanaSustitutos() {
	cerrarVentana();	
	win = new Window('window_id', {title: "Criterio Sustitución", minWidth:350, width:350, minHeight:200, height:200});
	win.getContent().innerHTML = "<div id='cont_sel_sustitutos'></div>";
	win.setDestroyOnClose(); 
	win.showCenter();
	
	var myAjax = new Ajax.Updater(
	  'cont_sel_sustitutos',
	  'ObtieneSustitutosPorProducto', 
	  	{
			'method': 'post',
			'parameters': "id_producto=" + $("id_prod").value + "&id_cliente=" + $("id_cliente").value,
			'onSuccess': function(REQUEST) {}
		}
	);
}

function cerrarVentana() {
	var popup = $("window_id");
	if (popup != null) {
		Windows.close("window_id", event);	
	}	
}

function abre_popup() {
	valor = "";
	if ((valor = popUpWindowModalscroll('ViewBusqProdForm', 100, 100, 600, 400))> '0') {
		if (valor != "") {
			id = valor.split("|");
			document.form1.id_prod.value = id[0];
			document.form1.intervalo.value = id[2];
			document.form1.maximo.value = id[3];
		}
	}
}
function agregar(form) {
	form.submit();
}
function validar_prod(form) {
	if ( form.id_prod.value == "" || form.cant.value == "" ) {
		alert("Debe ingresar todos los datos");
		return false;
	}
	if (validar_solonumero(form.id_prod, "Id de Producto es un campo Numerico")) return false;
	
	var campo = form.cant;
	var maximo = form.maximo.value;
	var intervalo = form.intervalo.value;
	if ((campo.value != "") && (!isNaN(campo.value))) {
		if (parseFloat(campo.value,10) < 0) {
			alert("Por favor, ingrese un valor mayor a cero");
			campo.focus();
			campo.select();
			return false;
		} else if (parseFloat(campo.value,10) != 0) {
			if (parseFloat(campo.value,10) > maximo) {
				if ((parseFloat(maximo,10) % intervalo) != 0) {
					var division = parseFloat(campo.value,10) / parseFloat(intervalo,10);
					campo.value = (Math.round( 100 * ( parseInt(division,10) * parseFloat(intervalo,10) ) )/100 )
				} else {
					var division = parseFloat(campo.value,10) / parseFloat(intervalo,10);
					campo.value = (Math.round( 100 * ( parseInt(division,10) * parseFloat(intervalo,10) ) )/100 )
				} 	
			} else {
				if ((parseFloat(campo.value,10) % intervalo) != 0) {
					var division = parseFloat(campo.value,10) / parseFloat(intervalo,10);
					campo.value = (Math.round( 100 * ( parseInt(division,10) * parseFloat(intervalo,10) ) )/100 )
					campo.focus();
				} 
			}	 
		}			
	} else {
		alert("Por favor, ingrese valores numéricos");
		campo.value = intervalo;
		return false;
	}
	
	//if (validar_solonumero(form.cant,    "Cantidad es un campo Numerico")) return false;
	if (validar_ltrnum(form.obs, "Observaciones solo permite el uso de letras, números, puntos y comas")) return false;
	if (form.obs.value.length > 255){ 
		alert("El largo máximo permitido en observaciones es de 255 caracteres");
		return false;
	}
	if (document.prod_ifr.ListProdCant[form.id_prod.value]) {
	   if (document.prod_ifr.ListProdCant[form.id_prod.value] != form.cant.value){
		   alert('El producto ya existe y tiene asignada una cantidad de '+ document.prod_ifr.ListProdCant[form.id_prod.value] );
		   return false;
	   } else {
		   alert('El producto ya existe y posee exactamente la cantidad de ' + form.cant.value);
		   return false;
	   }
	}
	return true;
}

function aReprogramar(alertar,estado,id_pedido) {
	if (alertar) {
		if (!confirm("Precaución: El pedido que quiere reprogramar está en estado '" + estado + "'.\nPor lo tanto debe considerar esta condición al momento de reprogramar.")) {
			return;
		}
	}
	popUpWindow('ViewSwitchJornadaForm?id_pedido=' + id_pedido, 100, 100,680, 570);
	//popUpWindow('ViewCalJorForm?id_pedido=' + id_pedido, 100, 100,680, 570);
}

function modificarPrecios(id_pedido) {
  
     document.location.href="ViewCambiarMontosEnOP?id_pedido="+id_pedido;
   
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


function modFechCreacion(alertar,estado,id_pedido) {
	if (alertar) {
		if (!confirm("Precaución: El pedido que quiere reprogramar está en estado '" + estado + "'.\nPor lo tanto debe considerar esta condición al momento de reprogramar.")) {
			return;
		}
	}
	popUpWindow('ViewModFechOP?id_pedido=' + id_pedido, 100, 100,680, 370);
}



