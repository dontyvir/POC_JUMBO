var subcatId=0;
var $j = jQuery.noConflict();
var nota_paso='1';
var id_formulario;
var nota_txt;


var XACT_CARRO 		= "";
var XID_PRODUCTO 	= 0;
var XCANTIDAD 		= 0;
var XNOTACAMPO 		= "";
var XPROD_INGRESADO = 0;
var XFORM;
var XID_CAB			= 0;
var XID_CAT			= 0;
var XID_SUBCAT		= 0;
var XBROWSER 		= navigator.appName;
var productoExisteEnLocal = false;
var desdeCatalogo = false;

//(Catalogo Externo) Nelson Sepulveda 07/08/2014.
var desdeCatalogoExt = false;

var XITEMSXPAGINA 		= 30;
var XHORIZONTAL 		= 0;

var comunaSeleccionada =false;

jQuery().ready(function() {

    muestra_categoriasTop();
    muestra_categoriasLeft();
    
	$j(function(){
		(function($j){
			$j.fn.accordion = function(custom) {
				var defaults = {
					keepOpen: false,
					startingOpen: false
				}
				var settings = $j.extend({}, defaults, custom);
				if(settings.startingOpen){
					$j(settings.startingOpen).show();
				}
			
				return this.each(function(){
					var obj = $j(this);
					$j('li a', obj).click(function(event){
						var elem = $j(this).next();
						if(elem.is('ul')){
							event.preventDefault();
							if(!settings.keepOpen){
								obj.find('ul:visible').not(elem).not(elem.parents('ul:visible')).slideUp();
							}
							elem.slideToggle();
						}
					});
				});
			};
		})(jQuery);
		
	    //$j('#menuvertical').accordion({keepOpen:false});
	
	});
	
	////Para hacer el acordión////////
	//jQuery('#navigation').accordion({active: false, alwaysOpen: false, navigation: true, header: '.head', autoheight: false});
	//////////////////////////////////
	$j('#loading').hide();
	
	$j("input#patron").autocomplete("/FO/PasoDosAutocompletar", {
		minChars: 3,
		width: 320,
		max: 40,
		mustMatch: false,
		selectFirst: false,
		/*extraParams: { t: function() { return $j("input#patron").val() } },*/
		//se bloquea cantidad de productos ya que los mostraba erroneos pedido por Zime
		//Riffo
		//formatItem: formatItem,
		formatResult: formatResult
	});
	
	$j("#acerrar").click(function(e){
		e.preventDefault();
		select.hide();
	});
	
	//////////////////////////////////
	////Para cambiar imagen de fondo en categorias
	////Luego no funciona el hover por css así es que se hace por jquery
	i=0;
	
	//////////////////////////////////
	$j('div#carro_de_compras').ready(function() {
		actualizaCarro();
	});
	//////////////////////////////////
	/*idcat_flash = $j('input#int').val();
	idsubcat_flash = $j('input#ter').val();
	if(parseInt(idcat_flash) > 0 && parseInt(idsubcat_flash) > 0){
		mostrar(idcat_flash, idsubcat_flash);
	}*/
	
	//////////////////////////////////
	/*$j('#menuvertical li .terminal').click(function(e) {
		$j('#loading').show();
		e.preventDefault();
		ids = $j(this).attr("id");
		catsId = ids.split("_");
		XID_CAB = catsId[0];
		XID_CAT	= catsId[1];
		XID_SUBCAT = catsId[2];
		mostrar(catsId[0],catsId[1],catsId[2]);
	});
	
	$j('#menuvertical li .cabecera').click(function(e) {
		ids = $j(this).attr("id");
		XID_CAB = ids
		XID_CAT	= 0;
		XID_SUBCAT = 0;
		mostrarCategorias(ids,0);
	});*/
	
	
	/////////////////////////////////
	$j('img#imgBuscar').click(function(e) {
		e.preventDefault();
		buscar();
	});
	
	$j('input#patron').keypress(function(e) {
		if(e.keyCode == 13) {
			buscar();
			e.preventDefault();
		}
	});
	
	$j('input#patron').focus(function() {
		if ($j(this).val() == 'Buscar')
			$j(this).val('');
	});
	
	//ir a categoría desde link externo
	//alert("cab: " + $j('#cab').val() + " | bus: '" + $j('#bus').val() + "' | bus:" + $j('#bus'));
	
	
	if(typeof(bus) != 'undefined' && $j('#bus').val() != ''){
		mostrarBusqueda($j('#bus').val());
	}else{
	//05-10-2012 Mauricio Farias
		if ($j('#cab').val() > 0 && $j('#int').val() > 0){
		muestraterminal($j('#cab').val(), $j('#int').val(), $j('#ter').val(), 15);
		} 
		else
		{ 
			if ($j('#cab').val() > 0 && $j('#int').val() == 0 && $j('#ter').val() == 0){
				muestracabecera($j('#cab').val(), 15);
			}		
		}
	}
});

function muestraProdHome(){
    //alert("(jQuery().ready) cab: " + $j('#cab').val() + ", int: " + $j('#int').val() + ", ter: " + $j('#ter').val());
	//ir a categoría desde link externo
//05-10-2012 Mauricio Farias
	if ($j('#cab').val() > 0 && $j('#int').val() > 0){
		muestraterminal($j('#cab').val(), $j('#int').val(), $j('#ter').val(), 15);
	} 
	else
	{ 
		if ($j('#cab').val() > 0 && $j('#int').val() == 0 && $j('#ter').val() == 0){
			muestracabecera($j('#cab').val(), 15);
		}		
	}
}

function eventosProductos() {
	    
	//se guarda el orden por defecto
//	var rowsdefault = $j('table#tabla_productos').find('tbody > tr').get();
	var rowsdefault = $j('table#tabla_productos tbody tr td').find('ul > li').get();
	/*
	$j("#tabla_productos").tablesorter({headers: {0:{sorter: false},3:{sorter: false},4:{sorter: false}},   
							cssAsc:"headerSortUp",
							textExtraction: function(node) { 
								dato = node.innerHTML;
								nombre = />[\wÑñÁáÉéÍíÓóÚú|\s|\W]+\-/;
								precio = /\$\d+\.?\d+/;
								
								txt = dato.match(nombre);
								if(txt != null)
									return txt[0];
								txt = dato.match(precio);
								if(txt != null)
									return txt[0].replace('.','');
					            return dato;
        					}
        
    });
    */
    var arrOrden=new Array('Foto','Descripcion','Precio','Cantidad','Agregar');
	//evento: ordenar por nombre, precio, precio por unidad
	
	$j('select#ordenar_por').change(function(e) {
		
//		fvasquez 27082013
		$j('#ordenar_por').blur();
//		fvasquez
		
		id = $j(this).val();
//		var $table = $j('table#tabla_productos');
		var $table = $j('table#tabla_productos tbody tr td ');
		//se carga el orden por defecto guardado más arriba en rowsdefault

		if (id == 'default') {
			$j.each(rowsdefault, function(index, row) {
//				$table.children('tbody').append(row);
				$table.children('ul').append(row);

				m = $j(':selected', $j(document).find('select#marca')).text();
				id1 = $j(document).find('select#marca').val();
				
				if( id1 == 0 || $table.children('ul').find('li:last').children('div').text().indexOf(m) >= 0 )
					$table.children('ul').find('li:last').children('div').show();
				else
					$table.children('ul').find('li:last').children('div').hide();

			});
			$j('#resultado').pajinate({
				items_per_page : XITEMSXPAGINA,
				item_container_id : '.' + $j('table#tabla_productos tbody tr td ul').attr('class'),
				nav_label_first : '<<',
				nav_label_last : '>>',
				nav_label_prev : '<',
				nav_label_next : '>'
				
			});

      	}else{
//			$j('th', $table).each(function(column) {
			for ( var i = 0; i < arrOrden.length; i++ ) {
				column=i;//arrOrden[i];
				m = $j(':selected', $j(document).find('select#marca')).text();
				id1 = $j(document).find('select#marca').val();

				//si eligió algún orden
				if (arrOrden[i] == id) {
//					var rows = $table.find('tbody > tr').get();
					var rows = $table.find('ul > li').get();
					//se hace la transformación del key a comparar para ordenar
					//esto para acelerar un poco el sort.
					$j.each(rows, function(index, row) { 
//						row.sortKey = $j(row).children('td').eq(column).text().toLowerCase();
						row.sortKey = $j(row).children('div').children('div').eq(column).text().toLowerCase(); 
			        });
			        //orden por nombre o descripción del producto
					if ( id	== 'Descripcion' ) {
//						var rows = $table.find('tbody > tr').get();
						var rows = $table.find('ul > li').get();
						rows.sort(function(a, b) {
							ka = a.sortKey;
							kb = b.sortKey;
							
				            return (ka < kb) ? -1 : ((ka > kb) ? 1 : 0);
		        	  	});

		
	       				$j.each(rows, function(index, row) {
//		       				$table.children('tbody').append(row);
		       				$table.children('ul').append(row);
							if( id1 == 0 || $table.children('ul').find('li:last').children('div').text().indexOf(m) >= 0 )
								$table.children('ul').find('li:last').children('div').show();
							else
								$table.children('ul').find('li:last').children('div').hide();

						});

			$j('#resultado').pajinate({
				items_per_page : XITEMSXPAGINA,
				item_container_id : '.' + $j('table#tabla_productos tbody tr td ul').attr('class'),
				nav_label_first : '<<',
				nav_label_last : '>>',
				nav_label_prev : '<',
				nav_label_next : '>'
				
			});
						
					}
					//orden por precio del producto
					else if (id == 'Precio' ) {
//						var rows = $table.find('tbody > tr').get();
						var rows = $table.find('ul > li').get();
						rows.sort(function(a, b) {
				            return compareNum(a.sortKey, b.sortKey);
		        	  	});
	       				$j.each(rows, function(index, row) {
//		       				$table.children('tbody').append(row);
		       				$table.children('ul').append(row);
							if(id1 == 0 || $table.children('ul').find('li:last').children('div').text().indexOf(m) >= 0)
								$table.children('ul').find('li:last').children('div').show();
							else
								$table.children('ul').find('li:last').children('div').hide();
						});

			$j('#resultado').pajinate({
				items_per_page : XITEMSXPAGINA,
				item_container_id : '.' + $j('table#tabla_productos tbody tr td ul').attr('class'),
				nav_label_first : '<<',
				nav_label_last : '>>',
				nav_label_prev : '<',
				nav_label_next : '>'
				
			});

					}
      			}
      			//orden por precios por unidad
//      			else if ( id == 'precio_por_unidad' && arrOrden[i] == 'Descripción') {
      			else if ( id == 'precio_por_unidad' && arrOrden[i] == 'Precio') {
//  					var rows = $table.find('tbody > tr').get();
  					var rows = $table.find('ul > li').get();
  					//se hace la transformación del key a comparar para ordenar
  					//esto para acelerar un poco el sort.
					$j.each(rows, function(index, row) {
//						row.sortKey = $j(row).children('td').eq(column).text();
						row.sortKey = $j(row).children('div').children("div[class^='modulo_precio_']").children('.txt_precio_medida_h').text();
			        });
					rows.sort(function(a, b) {
			            return compareNum(a.sortKey,b.sortKey);
	        	  	});
       				$j.each(rows, function(index, row) {
//	       				$table.children('tbody').append(row);
	       				$table.children('ul').append(row);
						if( id1 == 0 || $table.children('ul').find('li:last').children('div').text().indexOf(m) >= 0 )
							$table.children('ul').find('li:last').children('div').show();
						else
							$table.children('ul').find('li:last').children('div').hide();
					});


			$j('#resultado').pajinate({
				items_per_page : XITEMSXPAGINA,
				item_container_id : '.' + $j('table#tabla_productos tbody tr td ul').attr('class'),
				nav_label_first : '<<',
				nav_label_last : '>>',
				nav_label_prev : '<',
				nav_label_next : '>'
				
			});

				}
			}
		}
		
	});
	
	//evento: mostrar filtrar por marca
	$j('select#marca').change(function(e) {
		
//		fvasquez 27082013
		$j('#marca').blur();
//		fvasquez
		
		m = $j(':selected', $j(this)).text();
		id = $j(this).val();
//+20120210coh
//		$j('table#tabla_productos tbody tr').each(function() {
		$j('table#tabla_productos tbody tr td ul li div.txt_marca_h').each(function() { //modificado RR
		
//		$j('div#lista_productos ul li div').each(function() {
//-20120210coh
             
			if( id == 0 || $j(this).text() == m ) {
			   	$j(this).parent().parent().show();//modificado RR
			} else {
				$j(this).parent().parent().hide();//modificado RR
			}
		    
		});
		
		$j('#resultado').pajinate({
			items_per_page : XITEMSXPAGINA,
			item_container_id : '.' + $j('table#tabla_productos tbody tr td ul').attr('class'),
			nav_label_first : '<<',
			nav_label_last : '>>',
			nav_label_prev : '<',
			nav_label_next : '>'
			
		});
		
	});
	
	//evento: validar cantidad al salir del input
	$j('table#tabla_productos input').blur(function(){
		id = $j(this).attr('id');
		min = $j('input#min_' + id ).val();
		max = $j('input#max_' + id ).val();
		valida_cantidad(id, min, max);
	});
	
	/**
		Eventos: Flecha arriba (aumenta), flecha abajo (disminuye), agregar y modificar 
	*/
	$j('table#tabla_productos a').click(function(e){
		id = $j(this).attr('id');
		
		if ($j(this).attr('class') == 'link_cri_sus') {
			// Si el class del A seleccionado es link_cri_sus, 
			//se ha seleccionado el link de modificar criterio de sustitucion
			return;
		}
		//esto para que se ejecute el javascript de ficha que está en otro js
		if (id.indexOf('ficha') == 0 ){
			return;
		}

		
		e.preventDefault();
		
		if (id.indexOf('arriba') == 0 ){
			nom = id.substring(7);
			valor = parseFloat($j('input#' + nom ).val());
			min = parseFloat($j('input#min_' + nom ).val());
			max = parseFloat($j('input#max_' + nom ).val());
			if(valor < max && valor + min <= max){
				$j('input#' + nom ).val(Math.round( (valor + min)*100 ) / 100 );
			}else{
				alert('Sólo se pueden agregar hasta ' + max + ' productos');
				$j('input#' + nom ).val(Math.round( (max)*100 ) / 100 );
			}
		}else if (id.indexOf('abajo') == 0 ){
			nom = id.substring(6);
			valor = parseFloat($j('input#' + nom ).val());
			min = parseFloat($j('input#min_' + nom ).val());
			if(valor > min){
				$j('input#' + nom ).val(Math.round( (valor - min)*100 ) / 100 );
			}
		}else if (id.indexOf('nota') == 0 ){
			idProducto = id.substring(5);
			top.frames['frm_comentarios'].location.href="/FO_WebContent/comentarios.html";
			nota_paso='2';
			if(nota_campo == null || nota_campo == "")
				nota_txt=$j('input#nota_txt_' + idProducto).val();
			else
				nota_txt=nota_campo;
			id_formulario=idProducto;
		}else{
			//al poner "var" mantiene el valor de la variable idProducto del primer click 
			//para así cambiar la imagen "agregar" a "modificar"
			//si no tiene el var la variable se vuelve a crear con el segundo click con un nuevo valor.
			var idProducto = "";
			if (id.indexOf('agregar') == 0 ){
				idProducto = id.substring(8);
			}else if(id.indexOf('modificar') == 0 ){
				idProducto = id.substring(10);
			}
			if ($j('input#nota_txt_' + idProducto) != null && typeof($j('input#nota_txt_' + idProducto).val()) != "undefined") {
				nota_campo = $j('input#nota_txt_' + idProducto).val();
			} else {
				nota_campo = "";
			}
			cantidad = parseFloat($j('input#cantidad_' + idProducto).val());
			
			if (cantidad == 0) {
				alert("Por favor, ingrese la cantidad del producto.");
				$j('input#cantidad_' + idProducto).focus();
				return;
			}
			
			if (XID_PRODUCTO != idProducto){
			    XPROD_INGRESADO = 0;
			}else if (XCANTIDAD != cantidad){
			    XPROD_INGRESADO = 0;
			}else{
			    XPROD_INGRESADO = 1;
			}
			
			if ( $j('#comuna_user_id').val() == 0 ) {
			  XACT_CARRO = "ADD_CARRO";
			  XID_PRODUCTO = idProducto;
			  XCANTIDAD = cantidad;
			  XNOTACAMPO = nota_campo;
			  XPROD_INGRESADO = 1;
			  document.acceso3.url_to.value = "/FO/CategoryDisplay";
			  showClienteSinRegistro();			  
			  return;
			}
			//alert("XPROD_INGRESADO: " + XPROD_INGRESADO + " | XID_PRODUCTO: " + XID_PRODUCTO + " | idProducto: " + idProducto + " | XCANTIDAD: " + XCANTIDAD + " | cantidad: " + cantidad);
			XID_PRODUCTO = idProducto;
			XCANTIDAD = cantidad;
			//alert("XPROD_INGRESADO: " + XPROD_INGRESADO + " | XID_PRODUCTO: " + XID_PRODUCTO + " | idProducto: " + idProducto + " | XCANTIDAD: " + XCANTIDAD + " | cantidad: " + cantidad);
			
			if (XPROD_INGRESADO == 0){
			    addItemToCart(idProducto,cantidad,nota_campo);
			}
			/*if (XID_PRODUCTO == idProducto && XCANTIDAD != cantidad){
			    addItemToCart(idProducto,cantidad,nota_campo);
			}
			**/
			
		}
	});
	$j('div#div_sugerencias a').click(function(e) {
    	buscar($j(this).text());
    });
    
    $j('div#div_lsug a').click(function(e) {
    	buscar($j(this).text());
    });
    
    $j(".classmasv a").click(function(e) {
    	id = $j(this).attr('id');
    	var idProducto = id.substring(8);
    	cantidad = parseFloat($j('input#min_cantidad_' + idProducto).val());
    	
    	if ( $j('#comuna_user_id').val() == 0 ) {
    	  XACT_CARRO = "ADD_CARRO";
    	  XID_PRODUCTO = idProducto;
    	  XCANTIDAD = cantidad;
    	  XNOTACAMPO = "";
    	  showRegionesCobertura();
    	  return;
    	}
    	//agrega productos al carro
    	addItemToCart(idProducto,cantidad,"");

    });
}


function actualizaCarro() {
  $j.get("/FO/OrderItemDisplay", function(datos) {
    $j('div#carro_de_compras').html(datos);
    descuento = $j('input#total_desc_carro_compras_sf').val();
//+20120216coh ahora el total es con el descuento
//    $j('input#_total').val($j('input#total_carro_compras').val());
    $j('input#_total').val($j('input#total_desc_carro_compras_tc').val());
//-20120216coh
    $j('input#_total_desc').val($j('input#total_desc_carro_compras_tc').val());

    //+20120215coh
    $j('div#_total_ahorro').text($j('input#total_desc_carro_compras_tc_sf').val());//total_carro_compras - promo_desc_carro_compras
    $j('div#_total_tarjeta_mas').text($j('input#total_desc_carro_compras').val());
    $j('div#_total_ahorro_tarjeta_mas').text($j('input#total_desc_carro_compras_sf').val());
    //-20120215coh
    
    //por compatibilidad con codigo antiguo que sobrevivió
	$j('input#_total_desc_txt').val($j('input#promo_desc_carro_compras').val()); 
//+20120216coh 
	  $j('div#inputtotdesc').hide();
/*	if ( descuento == '$0' ) {
	  $j('div#inputtotdesc').hide();
	} else {
	  $j('div#inputtotdesc').show();
	}
*/
//-20120216coh 
  });
}

/*Esto para hacer compatible carro.js con paso2*/
function actualizaListaProductos(){
}
function actualizaLista(){
}
function verificalista() {
	if ( $j('#cant_reg').val() != null || typeof $j('#cant_reg').val() != 'undefined') {
		if ( $j('#cant_reg').val() == 0 ) {
			alert("No existen productos en el carro");			
		} else {
			showGuardaLista();
		}
	} else {
		alert("No existen productos en el carro");	
	}
}
function limpiarCarro(){
	var intTMP=1;
//	if (cantidad_productos > 0) {
		if (confirm("Está seguro que desea eliminar todos los productos del carro")) {
//			window.location.href="/FO/LimpiarMiCarro";
			//productoId: idProducto, cantidad: cantidad, nota: nota_campo
			try{mxTracker._trackEvent('Mi Carro','Modificar	carro','Limpiar');}catch(e){};
			  $j.post("/FO/LimpiarMiCarro", {noreenviar: intTMP}, function(datos) {
    $j(datos).find('respuesta').each(function() {
    
      var respuesta  = $j(this).find('mensaje').text();
      if ( respuesta == "OK" ) {
/*        $j('img#imagen_'+idProducto).attr('src','/FO_IMGS/img/estructura/paso2/bt_actualizar_carro.gif');
*/
        actualizaCarro();
        limpiaVariablesCarro();
        limpiaContadorCarro();
      }
    });
  });
			
			
		}
/*	} else {
		alert("Tu Carro de compras está Vacío");
	}	*/
}


function llenarCarroCompras() {
	actualizaCarro();
}


function buscar(sug){
	$j('#loading').show();
	centra_Loading();
	
	var texto = $j("input#patron").val();
	var sugeren = "";
	if (sug != null && typeof sug != 'undefined'){
		texto = sug;
		if (typeof $j("input#sugerencias").val() != 'undefined')
			sugeren = $j("input#sugerencias").val();
	}
	
	expr = /\s+\*/;
	texto = texto.replace(expr, '*');
	$j("input#patron").val(texto);
	$j.get("/FO/PasoDosResultado", {accion:"buscar", buscar: texto, sugerencias: sugeren}
	    ,function(datos){
			$j("div#div_productos").html(datos);
			$j("label#cat").html("Resultados Búsqueda: " + texto);
			eventosProductos();
			$j('#loading').hide();
	});
}

function ficha(idprod, idform) {
	top.ficha_carro = false;
	if ($j('input#nota_'+idprod).attr("className") != undefined) {
		top.nota_txt = $j('input#nota_'+idprod).val();
	} else {
		top.nota_txt = "";
	}
	top.id_formulario = idform;
	
	var cabecera_nombre = document.getElementById("cab_nom").value;
	var categoria_nombre = document.getElementById("cat_nom").value;
	var subcategoria_nombre = document.getElementById("subcat_nom").value;
	
	top.frames['frm_ficha'].location.href="ProductDisplay?idprod="+idprod+"&cabecera_nombre="+cabecera_nombre+"&categoria_nombre="+categoria_nombre+"&subcategoria_nombre="+subcategoria_nombre;
	//top.frames['frm_ficha'].location.href="ProductDisplay?idprod="+idprod;
}





function cantidadPorPagina(cantidad) {
	m = $j(':selected', $j(document).find('select#marca')).text();
	id1 = $j(document).find('select#marca').val();
	XITEMSXPAGINA=cantidad;

	$j('table#tabla_productos tbody tr td ul li').each(function() {
		if( id1 == 0 || $j(this).text().indexOf(m) >= 0 )
			$j(this).children('div').show();
		else
			$j(this).children('div').hide();
	});

	$j('#resultado').pajinate({
		items_per_page : XITEMSXPAGINA,
		item_container_id : '.' + $j('table#tabla_productos tbody tr td ul').attr('class'),
		nav_label_first : '<<',
		nav_label_last : '>>',
		nav_label_prev : '<',
		nav_label_next : '>',
		num_page_links_to_display: 5
	});
}

function toNumeric(s) {
	var i = parseFloat(s);
	return (isNaN(i)) ? 0 : i;
}

function compareNum(a,b){
	precio = /\$\d+\.{0,1}\d*/;
	numero = new RegExp(/[^0-9]/g);
	aa = a.match(precio);
	aaa = aa[0].replace(numero,"");
	bb = b.match(precio);
	bbb = bb[0].replace(numero,"");
	return toNumeric(aaa) - toNumeric(bbb);
}



function formatItem(row) {
	return " <label id='lbus'>" + row[1] + " prods.</label>" + row[0];
	//return row[0];
}

function formatResult(row) {
	return row[0];
}
function showClienteSinRegistro(){
	$j('#contenedor_lightbox').css({'width':'416px','padding-top':'8px','padding-right':'8px','padding-bottom':'8px','padding-left':'8px'});

	showLightBoxCategories();
//  greenBack();
  $j('#contenedor_lightbox').show();
	$j('#clienteSinRegistro').show();
	$j('#logo_jumbo').show();
	  $j('#comunas_sesion').hide();
	  $j('#change_comuna_layer_msj').hide();
	  $j('#change_login_session').hide();
	  $j('#olvido_clave').hide();
	  $j('#registra_cliente').hide();
	  $j('#cont_inputs_close').show();
	  $j('#opcionesRegistro').hide();
	  $j('#agrega_direccion').hide();
	  $j('#error_rut_existe').hide();
	  $j('#mensaje_dir_exito').hide();
	  $j('#producto_no_disponible').hide();
	  $j('#ventanaficha').hide();
	  
	//showRegionesCobertura();
}
function elijeOpcionSinRegistro(){
	var opcion=document.radios.radioinvitado;
	for (i=0;i<document.radios.radioinvitado.length;i++){ 
      	if (document.radios.radioinvitado[i].checked) 
         	break; 
   	} 
	opcion = document.radios.radioinvitado[i].value;
	if (opcion=="ingreso")
		justLogin();
	if (opcion=="registro")
		showRegistro();
	if (opcion=="continua")
		showRegionesCobertura();
}

function showChangeComuna() {
	//alert('showChangeComuna');
  upWeb();
  showLightBoxCategories();
//  greenBack();
  $j('#contenedor_lightbox').show();
  $j('#logo_jumbo').show();
  $j('#comunas_sesion').show();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').show();
  $j('#opcionesRegistro').hide();
  $j('#agrega_direccion').hide();
  $j('#error_rut_existe').hide();
  $j('#mensaje_dir_exito').hide();
  $j('#producto_no_disponible').hide();
  $j('#clienteSinRegistro').hide();
  $j('#ventanaficha').hide();
  //showRegionesCobertura();
}

function showChangeComunaMsj() {
	//alert('showChangeComunaMsj');
  upWeb();
  showLightBoxChangeComunaMsj();
  $j('#contenedor_lightbox').hide();
  $j('#logo_jumbo').hide();
  $j('#comunas_sesion').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').hide();
  $j('#opcionesRegistro').hide();
  $j('#agrega_direccion').hide();
  $j('#error_rut_existe').hide();
  $j('#mensaje_dir_exito').hide();
  $j('#change_comuna_layer_msj').show();
  $j('#producto_no_disponible').hide();
  $j('#clienteSinRegistro').hide();
  $j('#ventanaficha').hide();
}

function showProductoNoDisponible() {
	//alert('showProductoNoDisponible');
  upWeb();
  showLightBoxChangeComunaMsj();
  $j('#contenedor_lightbox').hide();
  $j('#logo_jumbo').hide();
  $j('#comunas_sesion').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').hide();
  $j('#opcionesRegistro').hide();
  $j('#agrega_direccion').hide();
  $j('#error_rut_existe').hide();
  $j('#mensaje_dir_exito').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#producto_no_disponible').show();	
  $j('#clienteSinRegistro').hide();
  $j('#ventanaficha').hide();
}

function showLoginSession() {
	//alert('showLoginSession');
  upWeb();
  showLightBoxCategories();  
//  whiteBack();
//$j('#TB_ajaxContent').css({backgroundColor:'white'});
  $j('#contenedor_lightbox').show();
  $j('#logo_jumbo').show();
  $j('#comunas_sesion').hide();//hide
  $j('#change_comuna_layer_msj').hide();//hide
  $j('#change_login_session').show();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').show();
  $j('#opcionesRegistro').hide();
  $j('#agrega_direccion').hide();
  $j('#error_rut_existe').hide();
  $j('#mensaje_dir_exito').hide();
  $j('#producto_no_disponible').hide();
  $j('#clienteSinRegistro').hide();
  $j('#ventanaficha').hide();
  $j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
		  closeLBoxWin();
  });
}

function showRecuperaClave() {
	//alert('showRecuperaClave');
  upWeb();
  showLightBoxCategories();  
//  whiteBack();
  $j('#contenedor_lightbox').show();
  $j('#logo_jumbo').show();
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').show();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').show();
  $j('#opcionesRegistro').hide();
  $j('#agrega_direccion').hide();
  $j('#error_rut_existe').hide();
  $j('#mensaje_dir_exito').hide();
  $j('#producto_no_disponible').hide();
  $j('#clienteSinRegistro').hide();
  $j('#ventanaficha').hide();
  try{mxTracker._trackPageview ('/Login/RecuperaClave');}catch(e){};
}

function showRegistrarSession() {
	//alert('showRegistrarSession');
  upWeb();
  showLightBoxRegistro();
  //greenBack();
  $j('#logo_jumbo').hide();
  $j('#contenedor_lightbox').hide();
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').show();
  $j('#cont_inputs_close').hide();
  $j('#opcionesRegistro').hide();
  $j('#agrega_direccion').hide();
  $j('#error_rut_existe').hide();
  $j('#mensaje_dir_exito').hide();
  $j('#producto_no_disponible').hide();
  $j('#clienteSinRegistro').hide();
  $j('#ventanaficha').hide();
  $j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
		  closeLBoxWin();
  });
}

function showMensajeRegistrarSession() {
	//alert('showMensajeRegistrarSession');
  upWeb();
  showLightBoxMensajeRegistro();
  //greenBack();
  $j('#logo_jumbo').hide();
  $j('#contenedor_lightbox').hide();
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').hide();
  $j('#opcionesRegistro').show();
  $j('#agrega_direccion').hide();
  $j('#error_rut_existe').hide();
  $j('#mensaje_dir_exito').hide();
  $j('#producto_no_disponible').hide();
  $j('#clienteSinRegistro').hide();
  $j('#ventanaficha').hide();
  $j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
		  closeLBoxWin();
  });
}

function showAgregaDireccionSession() {
	//alert('showAgregaDireccionSession');
  upWeb();
  showLightBoxAgregaDirecion();
  //greenBack();
  $j('#logo_jumbo').hide();
  $j('#contenedor_lightbox').hide();
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').hide();
  $j('#opcionesRegistro').hide();
  $j('#error_rut_existe').hide();
  $j('#agrega_direccion').show();
  $j('#mensaje_dir_exito').hide();
  $j('#producto_no_disponible').hide();
  $j('#clienteSinRegistro').hide();
  $j('#ventanaficha').hide();
  $j(document).click(function(e){
	  id = $j(this).val();
	  if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
	      //changeComunaRegistro();
		  closeLBoxWin();
  });
}

function showMensajeExitoAgregaDireccionSession() {
	//alert('showMensajeExitoAgregaDireccionSession');
  upWeb();
  showLightBoxMensajeExitoAgregaDireccion();
  //greenBack();
  $j('#logo_jumbo').hide();
  $j('#contenedor_lightbox').hide();
  $j('#comunas_sesion').hide();
  $j('#change_comuna_layer_msj').hide();
  $j('#change_login_session').hide();
  $j('#olvido_clave').hide();
  $j('#registra_cliente').hide();
  $j('#cont_inputs_close').hide();
  $j('#opcionesRegistro').hide();
  $j('#error_rut_existe').hide();
  $j('#agrega_direccion').hide();
  $j('#mensaje_dir_exito').show();
  $j('#producto_no_disponible').hide();
  $j('#clienteSinRegistro').hide();
  $j('#ventanaficha').hide();
  try{mxTracker._trackPageview('/Registro/RegistroExitoso/AgregaDireccion/IngresoExitoso');}catch(e){};
}

function whiteBack() {
  $j('#TB_ajaxContent').css({backgroundImage:'url("/FO_IMGS/images/html/fondo_layer_recupera.gif")'});
}

function greenBack() {
  $j('#TB_ajaxContent').css({backgroundImage:'url("/FO_IMGS/images/html/fondo_layer_categorias.gif")'});
}

function justLogin() {
  document.acceso2.url_to.value = "/FO/CategoryDisplay";
  showLoginSession();
  try{mxTracker._trackPageview ('/Login/IngresoDatos');}catch(e){};
}

function upWeb() {
  if ( XBROWSER == "Microsoft Internet Explorer" ) {
    window.scrollTo(0,0);
  }
}

function showLightBoxCategories() {
//  tb_show('Jumbo.cl','#TB_inline?height=316&width=523&inlineId=hiddenModalContent&modal=true'); 
  tb_show('Jumbo.cl','#TB_inline?height=330&width=400&inlineId=hiddenModalContent&modal=true');   
}


function showLightBoxChangeComunaMsj() {
//  tb_show('Jumbo.cl','#TB_inline?height=316&width=523&inlineId=hiddenModalContent&modal=true'); 
  tb_show('Jumbo.cl','#TB_inline?height=200&width=400&inlineId=hiddenModalContent&modal=true');   
}

function showLightBoxRegistro() {
  tb_show('Jumbo.cl','#TB_inline?height=450&width=620&inlineId=hiddenModalContent&modal=true');
}

function showLightBoxMensajeRegistro() {
  tb_show('Jumbo.cl','#TB_inline?height=125&width=580&inlineId=hiddenModalContent&modal=true');
}

function showLightBoxAgregaDirecion() {
  tb_show('Jumbo.cl','#TB_inline?height=400&width=625&inlineId=hiddenModalContent&modal=true');
}

function showLightBoxMensajeExitoAgregaDireccion() {
  tb_show('Jumbo.cl','#TB_inline?height=100&width=500&inlineId=hiddenModalContent&modal=true');
}

function addItemToCart(idProducto,cantidad,nota_campo) {
//	<!-- +20121009avc -->
	$j.ajax({
		type: "post",
		url: "/FO/PasoDosAgregarCarro", 
		data: {productoId: idProducto, cantidad: cantidad, nota: nota_campo}, 
	    success: function(datos) {
		    if ( $j(datos).find('respuesta').text() == "ok" ) {
        		$j('img#imagen_'+idProducto).attr('src','/FO_IMGS/img/estructura/paso2/bt_actualizar_carro.gif');
        		actualizaCarro();
        		limpiaVariablesCarro();
      		};
		},
	    error: function() {
	    	window.location.href="/FO_WebContent/sesion_expirada.html";
	    }
	    
	});
//	<!-- -20121009avc -->
}

function limpiaVariablesCarro() {
  XACT_CARRO 		= "";
  //XID_PRODUCTO 	= 0;
  //XCANTIDAD 		= 0;
  XNOTACAMPO 		= "";
}

function limpiaContadorCarro() {
//  XACT_CARRO 		= "";
  XID_PRODUCTO 	= 0;
  XCANTIDAD 		= 0;
//  XNOTACAMPO 		= "";
}
function changeComuna() {
	var nombreComuna;
  if ( $j('#id_region').val() == "0" || $j('#id_comuna').val() == "0" ) {
	  if ( $j('#localretiro').val() == "0" ){
    alert("Seleccione su región y comuna o Local de Retiro");
    try{mxTracker._trackEvent('SeleccionRegionComuna','Alerta','Region&Comuna');}catch(e){};
    return;
	  }
  }
  dataLayer.push({
	     'Region': $j("#id_region option:selected").html(),
	     'Comuna': $j("#id_comuna option:selected").html(),
	     'event': 'Seleccion-Region-Comuna'
	});
  	if ($j("#id_comuna").val()!="0"){
  		comuna=$j("#id_comuna").val();
	} else if ($j("#localretiro").val()!="0"){
		nombreComuna=$j("#localretiro option:selected").text();
		comuna=$j("#localretiro").val();
	}
  	

  $j.post("/FO/ChangeComunaSession", {id_comuna: comuna, nombre_comuna:nombreComuna}, function(datos){
	  $j(datos).find('comuna').each(function() {
      var respuesta  = $j(this).find('mensaje').text();
      $j('#comuna_user_str').html( $j(this).find('nombre_comuna').text() );
      $j('#comuna_user_id').val( $j(this).find('id_comuna').text() );
      if ( respuesta == "OK" ) {
    	  comunaSeleccionada = true;
    	//(Catalogo Externo)- Nelson Sepulveda 07/08/2014.
    	  var isCatalogoExt = document.getElementById("esCatalogoExt").value;
    	  
    	  if(isCatalogoExt == "true"){
    		  window.location.href = '/FO/CategoryDisplays';
        	  
        	  if ( XID_CAT != 0 && XID_SUBCAT == 0 ) {
                  mostrarCategorias(XID_CAB, XID_CAT);
                } else if ( XID_CAT != 0 && XID_SUBCAT != 0 ) {
                  mostrarCategoriaYSub(XID_CAB, XID_CAT, XID_SUBCAT)
                }
        	  
        	  closeLBoxWin();
    	  }else{
		        if ( XID_PRODUCTO != 0 ) {
		        	if(desdeCatalogo) {
		        		validaProductoLocalCatalogo();
		        	} else {
		        		validaProductoLocal();
		        	}
		          
		        } else {
		          closeLBoxWin();
		        }
		        actualizaCarro();
		        if ( XID_CAT != 0 && XID_SUBCAT == 0 ) {
		          mostrarCategorias(XID_CAB, XID_CAT);
		        } else if ( XID_CAT != 0 && XID_SUBCAT != 0 ) {
		          mostrarCategoriaYSub(XID_CAB, XID_CAT, XID_SUBCAT)
		        }
    	  }
      }
    });
  });
}

function botonx(){
	if (!$j('#clienteSinRegistro').is(':hidden')) {
		alert("Debe ingresar sus datos de ingreso o continuar como invitado");
		return;
	}else if (!comunaSeleccionada && !$j('#comunas_sesion').is(':hidden')){
		alert("Debes seleccionar su región y comuna");
		return;
	}else if (!$j('#registra_cliente').is(':hidden')){
		showClienteSinRegistro();
	}else{
		closeLBoxWin();
	}
	
}

function changeComunaRegistro() {
  if ( $j('#id_region_reg').val() == "0" || $j('#id_comuna_reg').val() == "0" ) {
    alert("Seleccione su región y comuna");
    return;
  }
  $j.post("/FO/ChangeComunaSession", {id_comuna: $j("#id_comuna_reg").val()}, function(datos){
    $j(datos).find('comuna').each(function() {
      var respuesta  = $j(this).find('mensaje').text();
      $j('#comuna_user_str').html( $j(this).find('nombre_comuna').text() );
      $j('#comuna_user_id').val( $j(this).find('id_comuna').text() );
      if ( respuesta == "OK" ) {
        if ( XID_PRODUCTO != 0 ) {
          validaProductoLocal();
        } else {
          closeLBoxWin();
        }
        actualizaCarro();
        if ( XID_CAT != 0 && XID_SUBCAT == 0 ) {
          mostrarCategorias(XID_CAB, XID_CAT);
        } else if ( XID_CAT != 0 && XID_SUBCAT != 0 ) {
          mostrarCategoriaYSub(XID_CAB, XID_CAT, XID_SUBCAT)
        }
      }
    });
  });
}

function changeComunaAgregaDireccion(id_region, id_comuna) {
  if ( id_region == "0" || id_comuna == "0" ) {
    alert("Seleccione su región y comuna");
    return;
  }
  $j.post("/FO/ChangeComunaSession", {id_comuna: id_comuna}, function(datos){
    $j(datos).find('comuna').each(function() {
      var respuesta  = $j(this).find('mensaje').text();
      $j('#comuna_user_str').html( $j(this).find('nombre_comuna').text() );
      $j('#comuna_user_id').val( $j(this).find('id_comuna').text() );
      if ( respuesta == "OK" ) {
        if ( XID_PRODUCTO != 0 ) {
          validaProductoLocal();
        } else {
          closeLBoxWin();
        }
        actualizaCarro();
        if ( XID_CAT != 0 && XID_SUBCAT == 0 ) {
          mostrarCategorias(XID_CAB, XID_CAT);
        } else if ( XID_CAT != 0 && XID_SUBCAT != 0 ) {
          mostrarCategoriaYSub(XID_CAB, XID_CAT, XID_SUBCAT)
        }
      }
    });
  });
}

function Registrar(){
	// 17/10/2012 : INICIO COREMETRICS {LEONARDO PAVEZ: leonardo.pavez@magnotechnology.com}
	var comuna_ = document.RegistroSencillo.id_comuna[document.RegistroSencillo.id_comuna.selectedIndex].text; 
	var region_ = document.RegistroSencillo.id_region[document.RegistroSencillo.id_region.selectedIndex].text;
	// 17/10/2012 : FIN COREMETRICS {LEONARDO PAVEZ: leonardo.pavez@magnotechnology.com}
    if (RegSencillo_ValidaCliente()){
        //alert();
	    var envioSMS   = false;
	    var envioEMail = false;
	    if ($j('#envioSMS').is(':checked')){
	        envioSMS = true;
	    }
	    if ($j('#envioEMail').is(':checked')){
	        envioEMail = true;
	    }
	    
	    var params = "cli_rut="     + $j('#cli_rut_reg').val()
	               + "&nombre="     + $j('#nombre').val()
	               + "&ape_pat="    + $j('#ape_pat').val()
	               + "&id_region="  + $j('#id_region_reg').val()
	               + "&id_comuna="  + $j('#id_comuna_reg').val()
	               + "&email="      + $j('#email_reg').val()
	               + "&envioEMail=" + envioEMail
	               + "&fon_cod="    + $j('#fon_cod').val()
	               + "&fon_num="    + $j('#fon_num').val()
	               + "&envioSMS="   + envioSMS
	               + "&clave="      + $j('#clave1').val()
	               ;
	    //var params = "";
	    //alert("params=> " + params);
  
		var requestOptions = {
			'method': 'post',
			'parameters': params,
			'onSuccess': function(REQUEST) {
				if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
					alert("Ocurrió un error en el registro.");
					return;
				}	
				var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
				//alert("respuesta= '" + respuesta + "'");
				if (respuesta == 'OK') {
	                logonRegSencillo('LBOX_S');
	        	    try{mxTracker._trackPageview ('/Registro/RegistroExitoso');}catch(e){};
				}else if (respuesta == 'CLIENTE_REGISTRADO'){
				    $j('#error_rut_existe').show();
				}
			}
		}
		new Ajax.Request('RegisterNew', requestOptions);
    }
    
}

//(Catalogo Externo) - Nelson Sepulveda 07/08/2014----------------------------------------------------------
function showLightBoxCatalogoExt() {
  tb_show('Jumbo.cl','#TB_inline?height=500&width=700&inlineId=hiddenModalCatalogoExt&modal=true');
}

function muestraLBCatalogoExt(){
	showLightBoxCatalogoExt();
	
	$j(document).click(function(e){
		id = $j(this).val();
		if(e.target.id=='TB_overlay') //cierra si hace clic fuera de la ventana
			closeLBCatalogoExt();
	});
}

function closeLBCatalogoExt() {
  tb_remove();
  $j('#contenedorCatalogoExt').hide();
}
//------------------------------------------------------------------------------------------------------------------

function MensajeRegistrar(){
    var params = "";
    
    id = $j(this).attr('id');
           
    var id_producto_catalogo = document.getElementById("id_producto_catalogo").value;
    var num_prod = document.getElementById("num_prod").value;
    
    //(Catalogo Externo) - Nelson Sepulveda 07/08/2014.
    var productos_catalogo_ext = document.getElementById("productos_catalogo_ext").value;
    var primera = document.getElementById("esPrimeraExt").value;
    var last_producto_carro = document.getElementById("ultimoProdAdd").value;
    
    //Catalogo Externo:
    if(productos_catalogo_ext != "false" && 
    	productos_catalogo_ext != "{productos_catalogo_ext}") {
    	
    	desdeCatalogoExt = true;
    	//showClienteSinRegistro();
    	if ( $j('#comuna_user_id').val() == 0 ) {
    		showClienteSinRegistro();
//    		XACT_CARRO = "ADD_CARRO";
//    		showRegionesCobertura();
//			return;
   	}
    }else{
    	//Catalogo Interno:
	    if(id_producto_catalogo != "false") {
	    	desdeCatalogo = true;
	    	
	    	if ( $j('#comuna_user_id').val() == 0 ) {   		
	    		XACT_CARRO = "ADD_CARRO";
	    		XID_PRODUCTO = id_producto_catalogo;
	    		//XCANTIDAD = 1;
	    		XCANTIDAD = num_prod;
	    		XNOTACAMPO = nota_campo;
	    		XPROD_INGRESADO = 1;
	    		showRegionesCobertura();			
				return;
			} 
	    	else {
				XACT_CARRO = "ADD_CARRO";
				XID_PRODUCTO = id_producto_catalogo;
				//XCANTIDAD = 1;
	    		XCANTIDAD = num_prod;
				XNOTACAMPO = nota_campo;
				XPROD_INGRESADO = 1;		
				validaProductoLocalCatalogo();
				return;
			}
	    	//validarListaProductosCarro(id_producto_catalogo);
	    }
    }
    
    //Catalogo Externo:
    if(primera == 0){
    	//Se muestra Lightbox.
    	$j('#contenedorCatalogoExt').show();
    	muestraLBCatalogoExt();
    }
    //Catalogo Externo:
    if(last_producto_carro != 0){
    	setTimeout(function(){
    		$j('.productoAgregado').attr('class',' ');
    		$j('#Itemcarro_'+last_producto_carro).attr('class','productoAgregado');
		}, 5000);
    }
    
    //alert("params=> " + params);
	var requestOptions = {
		'method': 'post',
		'parameters': params,
		'onSuccess': function(REQUEST) {
			if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
				alert("Ocurrió un error al Actualizar la Dirección.");
				return;
			}
			var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
			//alert("respuesta= '" + respuesta + "'");
			if (respuesta == 'OK') {
                showMensajeRegistrarSession();
			}/*else if (respuesta == 'DIREC_MODIFICADA'){
			    showMensajeExitoAgregaDireccionSession();
			}*/
		}
	};
	//new Ajax.Request('AjaxMensajeRegistrar', requestOptions);
}

/*function llenaFormDireccionRegSencillo() {
  $j.post("/FO/AjaxLlenaFormDireccionRegSencillo", function(datos){
    $j(datos).find('direccion').each(function() {
      var respuesta  = $j(this).find('mensaje').text();
      //$j('#comuna_user_str').html( $j(this).find('nombre_comuna').text() );
      //$j('#comuna_user_id').val( $j(this).find('id_comuna').text() );
      
      $j('#alias').val( $j(this).find('alias').text() );
      $j('#calle').val( $j(this).find('calle').text() );
      $j('#tipo_calle').val( $j(this).find('tipo_calle').text() );
      $j('#numero').val( $j(this).find('numero').text() );
      $j('#departamento').val( $j(this).find('depto').text() );
      $j('#comentario').val( $j(this).find('comentario').text() );
//comentario
    });
  });
}*/

function AgregaDireccion(){
    if (RegSencillo_ValidaDireccion()){
	    var params = "tipo_calle="    + $j('#tipo_calle').val()
	               + "&calle="        + $j('#calle').val()
	               + "&numero="       + $j('#numero').val()
	               + "&departamento=" + $j('#departamento').val()
	               + "&id_region="    + $j('#id_region_dir').val()
	               + "&id_comuna="    + $j('#id_comuna_dir').val()
	               + "&alias="        + $j('#alias').val()
	               + "&comentario="   + $j('#comentario').val()
	               ;
	    id_region= $j('#id_region_dir').val();
	    id_comuna= $j('#id_comuna_dir').val();
	    
	    //alert("params=> " + params);
		var requestOptions = {
			'method': 'post',
			'parameters': params,
			'onSuccess': function(REQUEST) {
				if (REQUEST.responseXML.getElementsByTagName("respuesta")[0] == null) {
					alert("Ocurrió un error al modificar el carro.");
					return;
				}
				var respuesta = REQUEST.responseXML.getElementsByTagName("respuesta")[0].childNodes[0].nodeValue;
				//alert("respuesta= '" + respuesta + "'");
				if (respuesta == 'OK') {
				    //changeComunaAgregaDireccion(id_region, id_comuna);
				    showMensajeExitoAgregaDireccionSession();
				}/*else if (respuesta == 'CLIENTE_REGISTRADO'){
				    showMensajeRegistrarSession();
				}*/
			}
		}
		new Ajax.Request('AjaxModificaDireccionRegSencillo', requestOptions);
	}
}

/*function showRegionesCobertura() {
  showChangeComuna();
  //traemos las regiones
  $j.get("/FO/RegionesConCobertura",function(datos) {
    $j("#id_region").html(datos);
    //TODO aca seleccionar combo de region
  });
}


function showRegionesCobertura() {
  showChangeComuna();
  //traemos las regiones
  $j.get("/FO/RegionesConCobertura",function(datos) {
    $j("#id_region").html(datos);
    //TODO aca seleccionar combo de region
  });
}*/

function showRegionesCobertura() {
	showChangeComuna();
	//traemos las regiones
	$j.get("/FO/RegionesConCobertura",function(datos) {
	    $j("#id_region").html(datos);
	    //TODO aca seleccionar combo de region
	});
	
	//alert("showRegistro 3 ==> id_region: " + id_region + " |  id_comuna: " + id_comuna);
	getComunasByRegion($j("#id_region").val());
	try{mxTracker._trackPageview('/SeleccionRegionComuna/Inicio');}catch(e){};
}

function getComunasByRegion(idRegion) {
	  if ( idRegion == 0 ) {
	    return;
	  }
	  $j.get("/FO/ComunasConCoberturaByRegion",{id_region:idRegion},function(datos) {
	    $j("#id_comuna").html(datos);
	    $j("#id_comuna").removeAttr('disabled');
	  });
	}

function showRegistro() {
  document.RegistroSencillo.url_to.value = "/FO/CategoryDisplay";
  showRegistrarSession();
  //traemos las regiones
  var id_comuna = 0;
  var id_region = $j("#id_region").val();
  var region = 0;
  //alert("$j('#id_comuna').val(): " + $j("#id_comuna").val());
  if ($j("#id_comuna").val() > 0){
    id_comuna = $j("#id_comuna").val();
  }
   
  //alert("showRegistro 1 ==> id_region: " + id_region + " |  id_region_reg: " + $j("#id_region_reg").val());
  if (id_region > 0){
    $j("#id_region_reg").val(id_region);
    region = id_region;
    //alert("showRegistro 2 ==> id_region: " + id_region + " |  id_region_reg: " + $j("#id_region_reg").val());
  }
  
  if ($j("#id_region_reg").val() > 0){
    region = $j("#id_region_reg").val();
  }
  
  //$j.get("/FO/RegionesConCobertura",{id_region:$j("#id_region_reg").val()},function(datos) {
  $j.get("/FO/RegionesConCobertura",{id_region:region}, function(datos) {
    $j("#id_region_reg").html(datos);
    //TODO aca seleccionar combo de region
  }); 
  
  if (id_region > 0){
    //alert("showRegistro 3 ==> id_region: " + id_region + " |  id_comuna: " + id_comuna);
    getComunasByRegionRegistro(id_region, id_comuna);
  }
  
  try{mxTracker._trackPageview('/Registro/IngresoDeDatos');}catch(e){};
}

function showAgregaDireccion() {
  showAgregaDireccionSession();
  
  $j.post("/FO/AjaxLlenaFormDireccionRegSencillo", function(datos){
  
  $j(datos).find('direccion').each(function() {
    
        //traemos los Tipos de Calles
	    $j.get("/FO/AjaxTiposCalle",{tipo_calle: $j(this).find('tipo_calle').text()},function(datos) {
	        $j("#tipo_calle").html(datos);
	        //TODO aca seleccionar combo de region
	    });
        
    
        $j('#alias').val( $j(this).find('alias').text() );
        $j('#calle').val( $j(this).find('calle').text() );
        $j('#tipo_calle').val( $j(this).find('tipo_calle').text() );
        $j('#numero').val( $j(this).find('numero').text() );
        $j('#departamento').val( $j(this).find('depto').text() );
        $j('#comentario').val( $j(this).find('comentario').text() );
        $j('#id_region_dir').val( $j(this).find('id_region').text() );
        $j('#id_comuna_dir').val( $j(this).find('id_comuna').text() );
        
        //alert("showAgregaDireccion 0 ==> id_region_dir: " + $j(this).find('id_region').text() + " |  id_comuna_dir: " + $j(this).find('id_comuna').text());

        //alert("showAgregaDireccion 1 ==> id_region_dir: " + $j(this).find('id_region').text() + " |  id_comuna_dir: " + $j(this).find('id_comuna').text());

	    //traemos las regiones
	    $j.get("/FO/RegionesConCobertura",{id_region: $j(this).find('id_region').text()},function(datos) {
	        $j("#id_region_dir").html(datos);
	        //TODO aca seleccionar combo de region
	    });
	    
	    if ($j(this).find('id_region').text() > 0){
	        //alert("showAgregaDireccion 2 ==> id_region_dir:" + $j(this).find('id_region').text() + " | id_comuna_dir:" + $j(this).find('id_comuna').text());
	        getComunasByRegionAgregaDireccion($j(this).find('id_region').text(), $j(this).find('id_comuna').text());
	    }
      
    });
  });
  try{mxTracker._trackPageview('/Registro/RegistroExitoso/AgregaDireccion');}catch(e){};
}

function ocultaEnvioSMS(){
  if ($j('#fon_cod').val() < 6 || $j('#fon_cod').val() > 9){
    $j('#DIVenvioSMS').hide();
    if ($j('#envioSMS').is(':checked')){
      $j('#envioSMS').attr('checked', false);
    }
  }else{
    $j('#DIVenvioSMS').show();
    $j('#envioSMS').attr('checked', true);
  }
}


function getComunasByRegionRegistro(idRegion, idComuna) {
  if (typeof idComuna === "undefined"){
    idComuna=0;
    //alert("something is undefined");
  }
   
  //alert("getComunasByRegionRegistro ==> idRegion:" + idRegion + " | idComuna:" + idComuna);
  $j.get("/FO/ComunasConCoberturaByRegion",{id_region:idRegion, id_comuna:idComuna},function(datos) {
    $j("#id_comuna_reg").html(datos);
    $j("#id_comuna_reg").removeAttr('disabled');
  });
}

function getComunasByRegionAgregaDireccion(idRegion, idComuna) {
  if (typeof idComuna === "undefined"){
    idComuna=0;
  }
  $j.get("/FO/ComunasConCoberturaByRegion",{id_region:idRegion, id_comuna:idComuna},function(datos) {
    $j("#id_comuna_dir").html(datos);
    $j("#id_comuna_dir").removeAttr('disabled');
  });
}

function closeLBoxWin() {
  if ( document.getElementById("ingreso_clientes") != null && document.getElementById("ingreso_clientes_lbox") != null ) {
    var norm = document.getElementById("ingreso_clientes");
    var box = document.getElementById("ingreso_clientes_lbox");
    box.style.visibility="hidden";
    box.style.width="0px";
    box.style.height="0px";
    norm.style.display="block";
    norm.style.visibility="visible";
    norm.style.width="230px";
    norm.style.height="306px";
  }
  tb_remove();
}

function show_lbox() {
  var norm = document.getElementById("ingreso_clientes");
  var box = document.getElementById("ingreso_clientes_lbox");
  norm.style.visibility="hidden";
  norm.style.width="0px";
  norm.style.height="0px";
  box.style.display="block";
  box.style.visibility="visible";
  box.style.width="230px";
  box.style.height="306px";
}

function getRecuperaClave1() {
  if ( $j('#cli_rut_recupera').val() == "" ) {
    alert("Por favor, ingrese su Rut.");
    return;
  }
  $j.post("/FO/ChangeComunaSession", {id_comuna: $j("#id_comuna").val()}, function(datos){
    $j(datos).find('comuna').each(function() {
      var respuesta  = $j(this).find('mensaje').text();
      $j('#comuna_user_str').html( $j(this).find('nombre_comuna').text() );
      $j('#comuna_user_id').val( $j(this).find('id_comuna').text() );
      
      if ( respuesta == "OK" ) {
        if ( XACT_CARRO == "ADD_CARRO" ) {
          validaProductoLocal();
        } else {
          window.location.reload();
        }
      }
    });
  });
}

function validaProductoLocal() {
  //Verificamos que producto se encuentre para esa comuna
  $j.get("/FO/ValidarProductoByLocal",{id_producto:XID_PRODUCTO},function(datos) {
    $j(datos).find('respuesta').each(function() {
      var resp  = $j(this).find('mensaje').text();
      if ( resp == "OK" ) {      
        if ( XACT_CARRO == "ADD_CARRO" ) {
          addItemToCart(XID_PRODUCTO,XCANTIDAD,XNOTACAMPO);
        } else if ( XACT_CARRO == "ADD_CARRO_FICHA" ) {
          if ( window.frames.frm_ficha && window.frames.frm_ficha.guardaProducto ) {
            window.frames.frm_ficha.guardaProducto( XFORM );
            hideFichaProducto();
          }
        }
        closeLBoxWin();
      } else {
        $j('#prod_disp_mjs').html("En este momento, el producto no se encuentra disponible<br/>para la Comuna que has seleccionado.");
        showChangeComunaMsj();
        try{mxTracker._trackPageview('/MiCarro/Productos no disponibles');}catch(e){};
      }
    });
  });
}

function validaProductoLocalCatalogo() {
	var existeEnLocal = false;
	  //Verificamos que producto se encuentre para esa comuna
	  $j.get("/FO/ValidarProductoByLocal",{id_producto:XID_PRODUCTO},function(datos) {
	    $j(datos).find('respuesta').each(function() {
	      var resp  = $j(this).find('mensaje').text();
	      if ( resp == "OK" ) {
	    	  //alert('si');
	        if ( XACT_CARRO == "ADD_CARRO" ) {
	          //addItemToCart(XID_PRODUCTO,XCANTIDAD,XNOTACAMPO);
	          //productoExisteEnLocal = true;
	          validaProductoStockCatalogo();
	          return;
	        } else if ( XACT_CARRO == "ADD_CARRO_FICHA" ) {
	          if ( window.frames.frm_ficha && window.frames.frm_ficha.guardaProducto ) {
	            window.frames.frm_ficha.guardaProducto( XFORM );
	            hideFichaProducto();
	          }
	        }
	        closeLBoxWin();	        
	      } else {
	    	//productoExisteEnLocal = false;
	        //alert("Lo siento, no se pudo agregar el producto al carro ya que no se encuentra disponible para su comuna");
	    	//showLightBoxProductoNoDisponible();
	    	$j('#prod_no_disp_mjs').html("En este momento, el producto no se encuentra disponible<br/>para la Comuna que has seleccionado.");
	    	showProductoNoDisponible();
	    	try{mxTracker._trackPageview('/MiCarro/Productos no disponibles');}catch(e){};
	    	//try{mxTracker._trackPageview('/MiCarro/Productos no disponibles');}catch(e){};
	    	//try{mxTracker._trackPageview('/MiCarro/Productos no disponibles');}catch(e){};
	        //return;
	      }
	    });
	  });
	}

function validaProductoStockCatalogo() {
	  //Verificamos que producto se encuentre para esa comuna
	  $j.get("/FO/ValidarProductoSinStock",{id_producto:XID_PRODUCTO},function(datos) {
	    $j(datos).find('respuesta').each(function() {
	      var resp  = $j(this).find('mensaje').text();
	      var cantidadMaxima = $j(this).find('cantidadmaxima').text();
	      if ( resp == "OK" ) {
	        if ( XACT_CARRO == "ADD_CARRO" ) {
		  	    var num_prod = document.getElementById("num_prod").value;
		  	    if(parseInt(num_prod) > parseInt(cantidadMaxima)) {
		  	    	alert('Sólo se pueden agregar hasta '+cantidadMaxima+' productos');
		  	    	closeLBoxWin();
		  	    	return;
		  	    }
	          addItemToCart(XID_PRODUCTO,XCANTIDAD,XNOTACAMPO);
	        } else if ( XACT_CARRO == "ADD_CARRO_FICHA" ) {
	          if ( window.frames.frm_ficha && window.frames.frm_ficha.guardaProducto ) {
	            window.frames.frm_ficha.guardaProducto( XFORM );
	            hideFichaProducto();
	          }
	        }
	        closeLBoxWin();
	      } else {
	    	//alert('NO tiene stock');
	        //alert("Lo siento, no se pudo agregar el producto al carro ya que no se encuentra disponible para su comuna");
	    	//showLightBoxProductoNoDisponible();
	    	$j('#prod_no_disp_mjs').html("En este momento, el producto no tiene stock<br/>en la Comuna que has seleccionado.");
	    	showProductoNoDisponible();
	    	//alert('NO tiene stock');
	    	//return;	    	
	    	//try{mxTracker._trackPageview('/MiCarro/Productos no disponibles');}catch(e){};
	        //return;
	      }
	    });
	  });
	}

function showEvento() {
  MM_showHideLayers('ventana_evento','','show');
  var layerEvento = document.getElementById('ventana_evento');
  //var ancho = screen.availWidth;
  var largo = screen.availHeight;
  var ancho = document.documentElement.scrollWidth;
  //var largo = document.documentElement.scrollHeight;
  layerEvento.style.width = '400px';
  layerEvento.style.height = '285px';
  layerEvento.style.left = (ancho-400)/2 + 'px';
  layerEvento.style.top = (largo-285)/2 + 'px';
}

function hideEvento() {
  MM_showHideLayers('ventana_evento','','hide');
  var layerEvento = document.getElementById('ventana_evento');
  layerEvento.style.width = '0px';
  layerEvento.style.height = '0px';
}

function AcordeonCabecera(cab){
    //alert("XID_CAB: " + XID_CAB + ", cab: " + cab);
	if (XID_CAB != cab){
	    $j('#loading').show();
	    centra_Loading();
		//abre la nueva categoria
		var elemcab = $j('#' + cab).next();
	    var objcab = $j('#' + cab);
	    if(elemcab.is('ul')){
		    objcab.find('ul:visible').not(elemcab).not(elemcab.parents('ul:visible')).slideUp();
		    elemcab.slideToggle();
	    }
	    objcab.css("color","#1FA22E");
	    
	    if (XID_CAB > 0){
	        //cierra la categoria que se encontraba abierta
		    var elemcab2 = $j('#' + XID_CAB).next();
	        var objcab2  = $j('#' + XID_CAB);
    	    if(elemcab2.is('ul')){
	    	    //objcab2.find('ul:visible').not(elemcab2).not(elemcab2.parents('ul:visible')).slideUp();
		        //elemcab2.slideToggle();
		        elemcab2.slideUp();
	        }
	        objcab2.css("color","#666");
	    }
	}

    if (XID_CAB == cab){
		//abre categoria en caso de que el usuario la haya cerrado
		var elemcab = $j('#' + cab).next();
	    var objcab = $j('#' + cab);
	    if(elemcab.is('ul')){
		    elemcab.slideDown();
	    }
	    objcab.css("color","#1FA22E");
	}
}

function AcordeonIntermedia(cab, int){
	if (XID_CAT != int){
	    $j('#loading').show();
	    centra_Loading();
	    
	    //abre la nueva categoria intermedia
	    var elemint = $j('#' + cab + '_' + int).next();
	    var objint = $j('#' + cab + '_' + int);
	    if(elemint.is('ul')){
		    objint.find('ul:visible').not(elemint).not(elemint.parents('ul:visible')).slideUp();
		    elemint.slideToggle();
	    }
	    objint.css("color","#1FA22E");
	    
	    if (XID_CAT > 0){
    	    //cierra la categoria intermedia que se encontraba abierta
	        var elemint2 = $j('#' + XID_CAB + '_' + XID_CAT).next();
	        var objint2  = $j('#' + XID_CAB + '_' + XID_CAT);
    	    if(elemint2.is('ul')){
	    	    objint2.find('ul:visible').not(elemint2).not(elemint2.parents('ul:visible')).slideUp();
		        elemint2.slideToggle();
	        }
	        objint2.css("color","#666");
	    }
	}
	
    if (XID_CAT == int){
		//abre la nueva categoria
		var elemint = $j('#' + cab + '_' + int).next();
	    var objint = $j('#' + cab + '_' + int);
	    if(elemint.is('ul')){
		    elemint.slideDown();
	    }
	    objint.css("color","#1FA22E");
	}
}

function muestracabecera(cab, numero){
	//alert("(muestracabecera) cab: " + $j('#cab').val() + ", int: " + $j('#int').val() + ", ter: " + $j('#ter').val());

	//alert("XID_CAB: " + XID_CAB + ", XID_CAT: " + XID_CAT + ", XID_SUBCAT: " + XID_SUBCAT + "\n" + "cab: " + cab );
	document.getElementById("cab").value = cab;
	var elemcab = $j('#' + cab).next();
	var objcab = $j('#' + cab);
	
    AcordeonCabecera(cab);
    
	if (XID_CAT > 0){
	    //cierra la categoria intermedia que se encontraba abierta
	    var elemint = $j('#' + XID_CAB + '_' + XID_CAT).next();
	    var objint  = $j('#' + XID_CAB + '_' + XID_CAT);
    	if(elemint.is('ul')){
	    	objint.find('ul:visible').not(elemint).not(elemint.parents('ul:visible')).slideUp();
		    elemint.slideToggle();
	    }
	    objint.css("color","#666");
	}
	
	if (XID_SUBCAT > 0){
	    var objter = $j('#' + XID_CAB + '_' + XID_CAT + '_' + XID_SUBCAT);
        objter.css("color", "#666");
    }
    
    if (numero < 15){
	    $j('#dropdown_' + numero).hide();
    }
    
	XID_CAB = cab;
	XID_CAT	= 0;
	XID_SUBCAT = 0;
	mostrarCategorias(cab,-1);
}
		 
function muestraterminal(cab, int, ter, numero){

	//alert("XID_CAB: " + XID_CAB + ", XID_CAT: " + XID_CAT + ", XID_SUBCAT: " + XID_SUBCAT + "\n" +
	//      "cab: " + cab + ", int: " + int + ", ter: " + ter );
	document.getElementById("cab").value = cab;
	document.getElementById("int").value = int;
	document.getElementById("ter").value = ter;
    AcordeonCabecera(cab);	
    AcordeonIntermedia(cab, int);
	
    var objter = $j('#' + cab + '_' + int + '_' + ter);
    objter.css("color", "#1FA22E");
    
    var objter2 = $j('#' + XID_CAB + '_' + XID_CAT + '_' + XID_SUBCAT);
    objter2.css("color", "#666");
    
	if (numero < 15){
	    $j('#dropdown_' + numero).hide();
//05-10-2012 Mauricio Farias
	}else
	{
		$j('#dropdown_0').hide();
		$j('#dropdown_1').hide();
		$j('#dropdown_2').hide();
		$j('#dropdown_3').hide();
		$j('#dropdown_4').hide();
		$j('#dropdown_5').hide();
		$j('#dropdown_6').hide();
		$j('#dropdown_7').hide();
		$j('#dropdown_8').hide();
		$j('#dropdown_9').hide();
		$j('#dropdown_10').hide();
	}
//-05-10-2012 Mauricio Farias
	

	XID_CAB = cab;
	XID_CAT	= int;
	XID_SUBCAT = ter;
	mostrar(cab,int,ter);
}

function mostrar(cabId, catId, subcatId) {
    XID_CAB = cabId;
    XID_CAT = catId;
    XID_SUBCAT = subcatId;
    mostrarCategoriaYSub(cabId, catId, subcatId);
}

function mostrarterminal(cab, int, ter) {
	
	//alert("XID_CAB: " + XID_CAB + ", XID_CAT: " + XID_CAT + ", XID_SUBCAT: " + XID_SUBCAT + "\n" +
	//      "cabId: " + cabId + ", catId: " + catId + ", subcatId: " + subcatId );
	
	document.getElementById("cab").value = cab;
	document.getElementById("int").value = int;
	document.getElementById("ter").value = ter;

    AcordeonCabecera(cab);
    AcordeonIntermedia(cab, int);

    var objter = $j('#' + cab + '_' + int + '_' + ter);
    objter.css("color", "#1FA22E");
    
    var objter2 = $j('#' + XID_CAB + '_' + XID_CAT + '_' + XID_SUBCAT);
    objter2.css("color", "#666");
    
    XID_CAB = cab;
    XID_CAT = int;
    XID_SUBCAT = ter;
    $j('#loading').show();
    centra_Loading();
    mostrarCategoriaYSub(cab, int, ter);
}

function mostrarCategoriaYSub(cabId, catId, subcatId) {
  $j.get("/FO/PasoDosResultado", {cab: cabId, int: catId, ter: subcatId},
    function(datos) {
	  $j("#div_productos").html(datos);
      eventosProductos();
      $j('#loading').hide();
    }
  );
}

function mostrarCategorias(cabId, catId) {
  $j.get("/FO/PasoDosPub", {cab: cabId, int: catId},function(datos) {
    $j("#div_productos").html(datos);
    eventosProductos();
    $j('#loading').hide();
  });
}

function mostrarBusqueda(patronBusqueda) {
	$j("input#patron").val(patronBusqueda);
	$j('#loading').show();
	  $j.get("/FO/PasoDosResultado", {accion:"buscar", buscar: patronBusqueda, sugerencias: ""},
	    function(datos) {
	      $j("div#div_productos").html(datos);
	      eventosProductos();
	      $j('#loading').hide();
	    }
	  );
}

function validarEmail(valor){
	validRegExp = /^[^@]+@[^@]+.[a-z]{2,}$/i;
	
	if (!validRegExp.test(valor)) {
	    alert("La dirección de email es incorrecta o está mal escrita. Ingrésala nuevamente");
	    return (false);
	}else
		return (true);
}

function RegSencillo_ValidaCliente(){
	if ($j('#cli_rut_reg').val() == ""){
		alert("Debes ingresar el Rut");
		$j('#cli_rut_reg').focus();
		return false;
	} else if ($j('#nombre').val() == "") {
		alert("Debes ingresar el campo Nombre");
		$j('#nombre').focus();
		return false;
	} else if ($j('#ape_pat').val() == "") {
		alert("Debes ingresar el campo Apellido");
		$j('#ape_pat').focus();
		return false;
	} else if ($j('#id_region_reg').val() == 0) {
		alert("Debes seleccionar la Región");
		$j('#id_region_reg').focus();
		return false;
	} else if ($j('#id_comuna_reg').val() == 0) {
		alert("Debes seleccionar la Comuna");
		$j('#id_comuna_reg').focus();
		return false;
	} else if ($j('#email_reg').val() == "") {
		alert("Debes ingresar el campo Mail");
		$j('#email_reg').focus();
		return false;
	} else if (!validarEmail($j('#email_reg').val())) {
		$j('#email_reg').focus();
		return false;
	} else if ($j('#fon_num').val() == "") {
		alert("Debes ingresar el campo Teléfono");
		$j('#fon_num').focus();
		return false;
	} else if ($j('#clave1').val() == "") {
		alert("Debes ingresar el campo Clave");
		$j('#clave1').focus();
		return false;
	} else if ($j('#clave1').val().length < 4){
		alert("La Clave debe tener mínimo 4 caracteres.");
		$j('#clave1').focus();
		return false;
	} else if ($j('#clave2').val() == "") {
		alert("Debes ingresar el campo 'Confirmar Clave'");
		$j('#clave2').focus();
		return false;
	} else if ($j('#clave2').val().length < 4){
		alert("La Clave debe tener mínimo 4 caracteres.");
		$j('#clave2').focus();
		return false;
	}
    if ($j('#clave1').val() != $j('#clave2').val()) {
		alert("Las claves ingresadas no coinciden");
		$j('#clave1').focus();
		return false;
	}
	if (!($j('#terminos').is(':checked'))) {
		alert("Debes aceptar los términos y condiciones");
		return false;
	}
	return true;
}

function RegSencillo_ValidaDireccion(){
	if ($j('#tipo_calle').val() == 0){
		alert("Debes seleccionar el Tipo de calle");
		$j('#tipo_calle').focus();
		return false;
	} else if ($j('#calle').val() == "") {
		alert("Debes ingresar el campo calle");
		$j('#calle').focus();
		return false;
	} else if ($j('#numero').val() == "") {
		alert("Debes ingresar el campo Número");
		$j('#numero').focus();
		return false;
	} else if ($j('#id_region_dir').val() == 0) {
		alert("Debes seleccionar la Región");
		$j('#id_region_dir').focus();
		return false;
	} else if ($j('#id_comuna_dir').val() == 0) {
		alert("Debes seleccionar la Comuna");
		$j('#id_comuna_dir').focus();
		return false;
	} else if ($j('#alias').val() == "") {
		alert("Debes ingresar el campo 'Guardar como'");
		$j('#alias').focus();
		return false;

    //VERIFICA QUE EL CLIENTE HAYA CAMBIADO SU DIRECCIÓN
	} else if ($j('#calle').val() == "Ingresa tu calle") {
		alert("Debes modificar el campo calle");
		$j('#calle').focus();
		return false;
	} else if ($j('#numero').val() == "000") {
		alert("Debes modificar el campo Número");
		$j('#numero').focus();
		return false;
	} else if ($j('#alias').val() == "Nombre") {
		alert("Debes modificar el campo 'Guardar como'");
		$j('#alias').focus();
		return false;
	}
	return true;
}

function centra_Loading(){
	var ventana_ancho = $j(window).width();
    var ventana_alto  = $j(window).height();
    //alert("Ancho: " + ventana_ancho + ", Alto: " + ventana_alto);
    
    var diff = 0;
    var mitad_diff = 0;
    if (ventana_ancho > 960){
        diff = ventana_ancho - 960;
        //alert("diff: " + diff);
        mitad_diff = diff / 2;
        //alert("mitad_diff: " + mitad_diff);
    }
    var left = mitad_diff + 391 + 16; //391;
    //alert("left: " + left);
    
    $j('#loading').css('left', left +"px");
}