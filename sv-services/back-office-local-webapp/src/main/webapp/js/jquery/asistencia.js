var $j = jQuery.noConflict();

jQuery().ready(function(){
	$j('#encargado').change(function(e) {
		asistenciasPickeadores();
	});
	
	$j("#multi input.dateField").dynDateTime({
			ifFormat: "%d-%m-%Y", 
			button: ".next()", //next sibling
			onUpdate: calendario
	});
});

function calendario(cal) {
	asistenciasPickeadores();
}

function asistenciasPickeadores(){
	encargadoId = $j('#encargado').val();
	fecha = $j('#fecha').val();
	$j.ajax({type: "GET", cache: false, url: "/JumboBOLocal/Asistencia_SeguimientoPickeadoresView", data: "encargadoId="+encargadoId+"&fecha="+fecha
			,success: function(datos){
				$j("div#pickeadores").html(datos);
				eventos();
			}
	});
}
function eventos(){
	$j("div#pickeadores input").blur(function(){
		valor = $j(this).val();
		if(asis.indexOf(valor+"-") == -1){
			alert("Registro ingresado incorrecto: " + valor);
			$j(this).val("");
		}
	});
}
