function muestra_categoriasTop(){
    //var params = "cab="+ $j('#cab').val() + "&int=" + $j('#int').val() + "&ter=" + $j('#ter').val();
    var params = "";
    //alert("params=> " + params);
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			$j('#categoriasTop').html(REQUEST.responseText);
		}
	}
	new Ajax.Request('AjaxCategoriasTop', requestOptions);
}

function muestra_categoriasLeft(){
	var params = "";
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			$j('#categoriasLeft').html(REQUEST.responseText);
			$j('#menuvertical').accordion({keepOpen:false});
			$j('#menuvertical li .terminal').click(function(e) {
				$j('#loading').show();
				e.preventDefault();
				ids = $j(this).attr("id");
				catsId = ids.split("_");
				
				var objter = $j('#' + catsId[0] + '_' + catsId[1] + '_' + catsId[2]);
				objter.css("color", "#1FA22E");
				
				if (XID_SUBCAT != catsId[2]){
				    var objter2 = $j('#' + XID_CAB + '_' + XID_CAT + '_' + XID_SUBCAT);
				    objter2.css("color", "#666");
	            }
				
				XID_CAB = catsId[0];
				XID_CAT	= catsId[1];
				XID_SUBCAT = catsId[2];
				
				mostrar(catsId[0],catsId[1],catsId[2]);
			});
	
	        $j('#menuvertical li .intermedia').click(function(e) {
				e.preventDefault();
				ids = $j(this).attr("id");
				catsId = ids.split("_");
	        
				var objint  = $j('#' + catsId[0] + '_' + catsId[1]);
				objint.css("color", "#1FA22E");
				
				if (XID_CAT != catsId[1]){
				    var objint2  = $j('#' + XID_CAB + '_' + XID_CAT);
				    objint2.css("color", "#666");
	            }

		        var objter2 = $j('#' + XID_CAB + '_' + XID_CAT + '_' + XID_SUBCAT);
			    objter2.css("color", "#666");
	            
				XID_CAB = catsId[0];
				XID_CAT	= catsId[1];
				XID_SUBCAT = -1;
//13112012 VMatheu 
				mostrar(XID_CAB,XID_CAT,XID_SUBCAT);
//-13112012 VMatheu 
	        });
	
			$j('#menuvertical li .cabecera').click(function(e) {
				ids = $j(this).attr("id");
				
				var objcab = $j('#' + ids);
				objcab.css("color","#1FA22E");
				
				if (XID_CAB != ids){
				    var objcab2  = $j('#' + XID_CAB);
				    objcab2.css("color","#666");
				}
				
			    var objint2  = $j('#' + XID_CAB + '_' + XID_CAT);
			    objint2.css("color", "#666");
				    
    	        var objter2 = $j('#' + XID_CAB + '_' + XID_CAT + '_' + XID_SUBCAT);
			    objter2.css("color", "#666");

				XID_CAB = ids
				XID_CAT	= 0;
				XID_SUBCAT = 0;
				
				//mostrarCategorias(ids, 0);
			});
		}
		
	}
	new Ajax.Request('AjaxCategoriasLeft', requestOptions);
}