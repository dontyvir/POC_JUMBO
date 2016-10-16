var $j = jQuery.noConflict();
jQuery().ready(function(){
	$j('a#pubMasiva').click(function(e){	
		$j.get("/JumboBOCentral/ViewPubMasivaForm"
			,function(datos){
				$j("div#div_form").html(datos);
				$j("#div_cargando").fadeOut("slow");
				eventos();
		});
	});

	$j('a#mktMasiva').click(function(e){	
		$j.get("/JumboBOCentral/ViewProdEnPromoUpdForm"
			,function(datos){
				$j("div#div_form").html(datos);
				$j("#div_cargando").fadeOut("slow");
				eventos_Mkt();
		});
	});
});
function eventos(){
	//////////para los checkbox
	$j('#checkAllAuto').click(function(){
		$j("input[name='local_check']").each( function(){
			this.checked = $j('#checkAllAuto').attr('checked');
		});
		vistaMotivos();
   	});
   	
   	$j("input[name='publicar']").click(function(){
		vistaMotivos();
   	});
   	
   	$j("input[name='local_check']").click(function(){
   		var checado = true;
		$j("input[name='local_check']").each( function(){
			checado = checado && $j(this).attr('checked');
		});
		$j('#checkAllAuto').attr('checked', checado);
		vistaMotivos();
   	});
	///////////////////////////
   	
	$j('input#ejecutar').click(function(e){	
		if(validar()){
			$j('#div_cargando').fadeIn("slow");
			prods = $j('textarea#productos').val();
			obs = $j('input#obs').val();
			pub = $j('input[name=publicar]:checked').val();
			evitar = $j('input:checkbox[name=evitar_pub_des]:checked').val();
			mot_id = $j('#motivo_des').val();
			$j.post("/JumboBOCentral/AddPubMasiva", {productos: prods, locales: gLocales, publicar: pub, evitarPubDes: evitar, observacion: obs, motivoId: mot_id}
				,function(datos){
					$j("div#div_resultado").html(datos);
					$j("#div_cargando").fadeOut("slow");
			});
		}
	});
}


function eventos_Mkt(){
	$j('input#ejecutar').click(function(e){	
		if(validar_mkt()){
			$j('#div_cargando').fadeIn("slow");
		}
	});
}
function validar_mkt(){
	filesMkt = $j("#excel_archivo")[0].files;		
	var tamanno = 0;
	for (var i = 0; i < filesMkt.length; i++){
		 tamanno = filesMkt[i].size;
	}
	if(tamanno == 0) {
		alert( "Debe seleccionar un archivo"); 
		return false;
	}	   
	return true;
}

function validar(){
	sum=0;
	gLocales = "";
	$j("input:checkbox[name='local_check']").each( function(){
		if (this.checked){ 
        	sum+=1;
        	gLocales += $j(this).val() +",";
        } 
	});
	if(sum == 0) {
		alert( "Debe seleccionar uno o más locales"); 
		return false;
	}
	gLocales = gLocales.substring(0, gLocales.length-1);
	
	var ex = /\d+/;
	prods = $j('textarea#productos').val();
	if(!ex.test(prods)){
		alert("Debe ingresar Ids FO (números) separados por espacios, comas, o cualquier caracter");
		return false;
	}
	return true;
}

function vistaMotivos(checado){
  	if($j('#checkAllAuto').attr('checked') && $j('#radio_despublicar').attr('checked')){
		$j('#div_motivos').fadeIn("slow");		
	}
	else{
		$j('#div_motivos').fadeOut("slow");
	}
}