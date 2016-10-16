function disabledBuy() {
  $('confirmar').href="javascript:;";
}
function enabledBuy() {
  $('confirmar').href="javascript:preOrderProcess();";
}
function preOrderCreate() {
  disabledBuy();
  //console.log("Ir a guardar el pedido");
  var requestOptions = {
    'asynchronous':false,
    'method': 'post',
    'parameters': {id_pedido:$('id_pedido').value},
    'onSuccess': function (REQUEST) {
      if (REQUEST.responseXML != null) {
        if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] != null) {
          //en caso de q no venga el msg, al gatillar la accion de confirmar la compra el sistema mandara pantalla de error o al login
          var mensaje = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
          var id_pedido = REQUEST.responseXML.getElementsByTagName("id_pedido")[0].childNodes[0].nodeValue;
          var monto = REQUEST.responseXML.getElementsByTagName("monto")[0].childNodes[0].nodeValue;
          var medio_pago = REQUEST.responseXML.getElementsByTagName("medio_pago")[0].childNodes[0].nodeValue;
          
          if ( mensaje != 'OK' ) {
            alert(mensaje);
            enabledBuy();
            return;
          }
          
          if ( medio_pago == 'TBK' ) {
            $('TBK_ORDEN_COMPRA').value = id_pedido;
            $('TBK_MONTO').value = monto;
            document.webpay.submit();
          } else {
          /*** esto es para que no paguen con CAT 
            alert("Estimado cliente:\nEn estos momentos el medio de pago Tarjeta Más no se encuentra disponible.\nEstamos trabajando para usted.\n\nMuchas gracias.");
            return;
          **/
            
          	monto = monto / 100;
            $('numeroTransaccion').value = id_pedido;
            $('idCarroCompra').value = id_pedido;
            $('montoOperacion').value = monto;
            document.cat.submit();

          }
        }
      }
    }
  };
  new Ajax.Request('OrderCreate', requestOptions);
}
