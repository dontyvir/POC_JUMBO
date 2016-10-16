var $j = jQuery.noConflict();

jQuery().ready(function(){
	$j("form#f1").submit(function(e){
		return validar();
	});

	$j("#multi input.dateField").dynDateTime({
			ifFormat: "%d-%m-%Y", 
			button: ".next()"
	});
});

function validar(){
	var fechaDesde = $j('#fecha_desde').val();
	var fechaHasta = $j('#fecha_hasta').val();
	var str = fechaDesde.split('-');
    var fecini = new Date(str[2] + '/' + str[1] + '/' + str[0]);
    str = fechaHasta.split('-');
    var fecfin = new Date(str[2] + '/' + str[1] + '/' + str[0]);
    var dif = fecfin - fecini
    
    if(fecfin - fecini >= 3888000000 ){
    	alert("El rango de fecha no puede superar 45 días");
    	return false;
    } 
    return true;
}
