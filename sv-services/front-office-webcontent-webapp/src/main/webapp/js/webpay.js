var $j = jQuery.noConflict();
$j(document).ready(function(){
    $j("frmPedido").submit(function(){
    	if (!validar())
    		return false;
        o_pr = $j('#precio').val();
        o_id = $j('#oferta_id').val();
        o_ca = $j('#cantidad').val();
        r = false;
        $j.ajax({
            async: false,
            type: "POST",
            url: '/pedidos',
            data: 'compra[precio_unitario]=' + o_pr + '&compra[oferta_id]=' + o_id + '&compra[cantidad]=' + o_ca,
            success: function(datos){
                $('#TBK_ORDEN_COMPRA').val(datos);
                $('#TBK_MONTO').val(o_pr * o_ca * 100);
                r = true;
            },
            error: function(xhr, ajaxOptions, thrownError){
                alert("Ha ocurrido un error, inténtelo más tarde");
                r = false;
            }
        });
        return r;
    });
    
    $j('input#cantidad').change(function(){
        cant = $j(this).val();
        if (cant < 0) {
			cant *= -1;
        }else if( isNaN(cant) || $.trim(cant).length == 0){
			cant = 1;
		}
		$j(this).val(cant);
        
        total = cant * $j('input#precio_unitario').val();
        $j(".tdtotal").html('$ ' + addPuntos(total));
    });
});


function addPuntos(nStr){
    nStr += '';
    x = nStr.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + '.' + '$2');
    }
    return x1 + x2;
}
