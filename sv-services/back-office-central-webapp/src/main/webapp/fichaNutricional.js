function guardarLeySuperOcho( form ) {
	//if(validaFicha(form)){
		form.submit();
	//}
}

/*function guardarFichaNutricional( form ) {
	//validaFicha(form)
	form.submit();
}

function validaFichaNutricional(form){	
	guardarFichaNutricional(form);	
}*/

$j('#addItemFN').click(function(e){
	var lastIndex = 0;
	var largoTabla = $j("#idTableFichaNutricional tbody tr").length;
	var total = 0;

	//Se valida que solo se puedan agregar hasta 20 items (Definidos por el negocio) como maximo.
	var cantidadItems = $j("#cantItems").val();
	total = $j("#totalFN").val();

	if(parseInt(largoTabla)<parseInt(cantidadItems)){
		$j("#idTableFichaNutricional tbody tr").each(function (index) {
			lastIndex = index + 1;										
		});
		var tituloNroItem = 'Item ';
		tituloNroItem = tituloNroItem + (lastIndex + 1);
		total++;
		var str = "<tr>";
		str = str + "<td style='width:50%'>"+tituloNroItem+"</td>";
		str = str + "<td><input name='itemFN["+total+"]' id='itemFN["+total+"]' type='text' maxlength='30' size='30' order="+total+"></td>";
		str = str + "<td><input name='itemDescripFN["+total+"]' id='itemDescripFN["+total+"]' type='text' maxlength='50' size='30'></td>";
		str = str + "<td><input name='itemDescrip2FN["+total+"]' id='itemDescrip2FN["+total+"]' type='text' maxlength='50' size='30'></td>";
		str = str + "<td class='eliminarItemFN'>";
		str = str + "<input id='btnEliminarFN_1' name='btnEliminarFN_1' type='button' value='Eliminar' />";
		str = str + "<input id='orderFN["+total+"]' name='orderFN["+total+"]' value="+total+" type='hidden'>";
		str = str + "</td>";
		str = str + "</tr>";
				
		//Se agrega una nueva fila en la tabla.
		$j("#idTableFichaNutricional tbody").append(str);
		$j("#totalFN").val(total);

	}else{
		alert("Has alcanzado el limite de " + cantidadItems + " items.");
	}		
});


/*Evento click de eliminar una fila seleccionada*/		
$j(".eliminarItemFN").live('click',function(){
	var parent = $j(this).parents().get(0);
	//console.log(parent);
	$j(parent).remove();
	
	$j("#idTableFichaNutricional tbody tr").each(function (index) {
		var indice = index + 1;
		$j(this).children(' td:nth-child(1)').text('Item ' + indice);
	});
});
		



function validaFicha(obj) {  	
	var concepto, descripcion;
	var countErrores = 0;	
	$j("#idTableFichaNutricional tbody tr").each(function (index) {
		lastIndex = index + 2;
		//Se valida que los campos no esten vacios.
		$j(this).children("td").each(function (index2) {
			switch (index2) {
			case 2:
				descripcion = $j.trim($j(this).children("input").val());
				if (descripcion == "") {
					countErrores++;
				}
				break;
			}
		});
	});
	if (countErrores > 0){
		alert("El campo Descripción es obligatorio.");
		return false;
	} else {
		return true;
	}			
}