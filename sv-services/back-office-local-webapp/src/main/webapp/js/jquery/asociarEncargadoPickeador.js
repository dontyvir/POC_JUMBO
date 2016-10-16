var $j = jQuery.noConflict();

jQuery().ready(function(){
	$j("input#patron").autocomplete("/JumboBOLocal/Asociar_ListaEncargadosView", {
		minChars: 2,
		width: 300,
		max: 40,
		mustMatch: false,
		selectFirst: false,
		formatItem: formatItem,
		formatResult: formatResult
	});
	
	$j('input#buscar').click(function(e) {
		buscar();
	});
	$j('input#patron').keypress(function(e) {
		if(e.keyCode == 13) {
			buscar();
		}
	});
	
	$j('img#imgBuscar').click(function(e) {
		e.preventDefault();
		buscar();
	});
	
	$j('div#pickeadores a').click(function(e) {
    	buscar($j(this).attr('id'));
    });
	
	
});

function formatItem(row) {
	return row[1] + " => " + row[0];
}
function formatResult(row) {
	return row[0];
}

function buscar(text){
	var texto = $j("input#patron").val();
	if (text != null && typeof text != 'undefined'){
		texto = text;
		$j("input#patron").val(texto);
	}
	
	expr = /\s+\*/;
	texto = texto.replace(expr, '*');
	$j("input#patron").val(texto);

	$j.ajax({type: "GET", cache: false, url: "/JumboBOLocal/Asociar_PickeadoresPorEncargadoView", data: "loginEncargado=" + texto
				,success: function(datos){
					$j("div#pickeadores").html(datos);
				}
			});
}