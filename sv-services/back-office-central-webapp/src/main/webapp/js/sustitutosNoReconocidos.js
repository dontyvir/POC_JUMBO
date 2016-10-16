var $j = jQuery.noConflict();
jQuery().ready(function(){
	$j('#loading').hide();
	evento0();
});


function evento0(){
	$j('button#buscar').click(function(e) {
		e.preventDefault();
		$j('#loading').show();
		pedidoId = $j('input#pedidoId').val();
		$j.ajax({type: "GET", cache: false, url: "ViewSustitutosPedido", data: "pedidoId=" + pedidoId
			,success:function(datos){
				$j('div#pedido').html(datos);
				evento1();
				$j('div#barra').html("");
				$j('div#resultado').html("");
			}
		});
		$j('#loading').hide();
	});
}

function evento1(){
	$j('div#pedido button').click(function(e) {
		e.preventDefault();
		$j('#loading').show();
		id = $j(this).attr('id');
		if (id.indexOf('barra_') == 0 ){
			dpickId = id.substring(6);
			var barra = $j('input#text_' + dpickId).val();
			localId = $j('input#local_' + dpickId).val();
			descripcion = $j('input#descripcion_' + dpickId).val();
			$j.get("ViewSustitutosBarra", {barra: barra, dpickingId: dpickId, localId: localId, descripcion: descripcion}
				,function(datos){
					$j('div#barra').html(datos);
					evento2();
					$j('div#resultado').html("");
				});
		}
		$j('#loading').hide();
	});
}
	
function evento2(){	
	$j('button#grabar').click(function(e) {
		e.preventDefault();
		detalleId = $j("input#detalleId").val();
		var barra = $j("input#cbarra").val();
		precio = $j("input#precio").val();
		productoId = $j("input#productoId").val();
		descripcionSust = $j("input#descripcion_sust").val();
		$j.ajax({type: "POST", cache: false, url: "UpdSustituto", data: "cbarra="+barra +"&dpickingId="+ dpickId +"&precio=" + precio + "&productoId=" + productoId + "&descripcionSust=" + descripcionSust 
			,success:function(datos){
				$j('div#resultado').html(datos);
			}
		});
	});
}