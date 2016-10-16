var $j = jQuery.noConflict();
jQuery().ready(function(){
	$j('a#aNuevo').click(function(e){	
		$j.get("/JumboBOCentral/ViewDestacadoNewForm"
			,function(datos){
				$j("div#div_form").html(datos);
				eventos();
		});
	});
	
	$j('a').click(function(e){
		/*e.preventDefault();*/
		id = $j(this).attr('id');

		if(id.indexOf('mod') == 0){
			destacado_id = $j(this).attr('name');
			$j.ajax({type: "GET", cache: false, url: "/JumboBOCentral/ViewDestacadoUpdForm", data: "id="+destacado_id
				,success: function(datos){
					$j("div#div_form").html(datos);
					eventos();
				}
			});
		}else if(id.indexOf('ver') == 0){
			destacado_id = $j(this).attr('name');
			$j.ajax({type: "GET", cache: false, url: "/JumboBOCentral/ViewDestacado", data: "id="+destacado_id
				,success: function(datos){
					$j("div#div_form").html(datos);
				}
			});
		}
	});
	
});

function eventos(){
	$j('input#hora_ini').timepickr({rangeMin: ['00']});
	$j('input#hora_fin').timepickr({rangeMin: ['00']});
	$j('input#hora_ini').next().addClass('dark');
	$j('input#hora_fin').next().addClass('dark');
	
	jQuery("#multi1 input, #multi2 input").dynDateTime({
			ifFormat: "%d-%m-%Y", 
			button: ".next()" //next sibling
	});

	$j('form#updDestacado').submit(function(e){
		return validar();
	});
		
	$j('form#addDestacado').submit(function(e){
		if (validar()){ 
			if($j('input#archivo').val() == ''){
				alert( "El archivo excel es obligatorio"); 
				return false;
			}
			return true;
		}
		return false;
	});
}

function validar(){
	sum=0;
	$j("input:checkbox[@name='local']").each( function(){
		if (this.checked){ 
        	sum+=1;
        } 
	});
	if(sum == 0) {
		alert( "Debe seleccionar uno o más locales"); 
		return false;
	}
	if($j('input#descripcion').val() == ''){
		alert( "La descripción es obligatoria"); 
		return false;
	}
	if($j('input#fecha_ini').val() == ''){
		alert( "La fecha de inicio es obligatoria"); 
		return false;
	}
	if($j('input#hora_ini').val() == ''){
		alert( "La hora de inicio es obligatoria"); 
		return false;
	}
	if($j('input#fecha_fin').val() == ''){
		alert( "La fecha de termino es obligatoria"); 
		return false;
	}
	if($j('input#hora_fin').val() == ''){
		alert( "La hora de termino es obligatoria"); 
		return false;
	}
	
	if($j('input#imagen').val() == ''){
		alert( "La imagen es obligatoria"); 
		return false;
	}
	return true;
}