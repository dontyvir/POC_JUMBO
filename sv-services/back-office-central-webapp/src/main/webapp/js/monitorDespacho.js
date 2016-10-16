Event.observe(window, 'load', function(e) {
});

function validarFrmOp(form) {
	if (form.id_pedido.value=="" || parseInt(form.id_pedido.value)<=0){
		alert("Debe ingresar numero de pedido");
		return false;
	}
	return true;
}

function validarFrmRuta(form) {
	if (form.id_ruta.value=="" || parseInt(form.id_ruta.value)<=0){
		alert("Debe ingresar numero de ruta");
		return false;
	}
	return true;
}

var globalCallbacks = {
	onCreate: function() {
		$("tabla_loading").style.top = document.documentElement.scrollTop + 'px';
		$("loading").show();
	},
	onComplete: function() {
		if (Ajax.activeRequestCount == 0 ) {
			$("loading").hide();
		}
	}
};
Ajax.Responders.register( globalCallbacks );