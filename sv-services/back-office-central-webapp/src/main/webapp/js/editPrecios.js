function cambio( fila, valor ) {
  if ( valor == "" || valor == "0" ) {
    alert("Debe ingresar el valor del producto");
    $('precio_'+fila).value = productos[fila].precio_nuevo;
    return;
  }
  // actualizamos el total
  $('total').value = ( parseInt($('total').value,10) - parseInt(productos[fila].precio_nuevo,10) );
  $('total').value = ( parseInt($('total').value,10) + parseInt(valor,10) );
  $('total_str').innerHTML = $('total').value;
  
  //guardamos nuevo valor en variable
  productos[fila].precio_nuevo = valor;
  
  //actualizamos el monto excedido
  $('excedido').value = ( parseInt($('total_reservado').value,10) - parseInt($('total').value,10) );
  $('excedido_str').innerHTML = $('excedido').value;
  
  if ( estaExcedido() ) {
    $('excedido_str').style.color = "red";
  } else {
    $('excedido_str').style.color = "blue";
  }
  
  //ponemos el precio como va a quedar en la BD
  $('nvo_precio_'+fila).innerHTML = parseInt( productos[fila].precio_nuevo / productos[fila].cantidad )
  
}

function estaExcedido() {
  if ( parseInt($('excedido').value,10) < 0 ) {
    return true;
  }
  return false;
}

function validar(e) {
  tecla = (document.all) ? e.keyCode : e.which;
  if (tecla==8) return true;
  patron =/^[0-9]$/;
  te = String.fromCharCode(tecla);
  return patron.test(te);
} 

function cambiarPrecios() {
  if ( estaExcedido() ) {
    alert("No puede guardar los precios hasta que el monto de exceso sea positivo");
    return;
  }
  var prodsCambiados = "";
  var coma = "";
  for ( var i = 0; i < productos.length; i++ ) {
    if ( parseInt(productos[i].precio_original,10) != parseInt(productos[i].precio_nuevo,10) ) {
      prodsCambiados += coma + ( productos[i].id_picking + "#" + parseInt( productos[i].precio_nuevo / productos[i].cantidad ) + "#" + parseInt( productos[i].precio_old ) + "#" + parseInt( productos[i].id_producto) + "#" + productos[i].desc_producto);
      coma = "-=-";
    }
  }
  if ( prodsCambiados == "" ) {
    alert("No hay cambios para enviar");
    return;
  }
  $('datos_cambiados').value = prodsCambiados;
  document.f1.submit();
}
