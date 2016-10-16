function eliminarProd(idProd) {
	if (confirm("Seguro que desea eliminar el producto?")) {
		var obj = document.f1;
		obj.id_prod_carrusel.value = idProd;
		obj.submit();
	}
}

var globalCallbacks = {
                onCreate: function() {
						$("tabla_loading").style.top = document.documentElement.scrollTop + 'px';
                        $("loading").show();
                },
                onComplete: function() {
                        if(Ajax.activeRequestCount == 0){
                                $("loading").hide();
                        }
                }
        };
Ajax.Responders.register( globalCallbacks );
