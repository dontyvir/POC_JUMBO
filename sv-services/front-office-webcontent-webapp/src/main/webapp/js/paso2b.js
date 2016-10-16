var subcatId=0;
var $j = jQuery.noConflict();
var nota_paso='1';
var id_formulario;
var nota_txt;

var XACT_CARRO 		= "";
var XID_PRODUCTO 	= 0;
var XCANTIDAD 		= 0;
var XNOTACAMPO 		= "";
var XFORM;
var XID_CAB			= 0;
var XID_CAT			= 0;
var XID_SUBCAT		= 0;
var XBROWSER 		= navigator.appName;

var XITEMSXPAGINA 		= 30;
var XHORIZONTAL 		= 0;

jQuery().ready(function() { 

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
		//se bloquea cantidad de productos erroneos Riffo
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

	//////////////////////////////////
	/*idcat_flash = $j('input#int').val();
	idsubcat_flash = $j('input#ter').val();
	if(parseInt(idcat_flash) > 0 && parseInt(idsubcat_flash) > 0){
		mostrar(idcat_flash, idsubcat_flash);
	} */
	
	//////////////////////////////////
	
	/////////////////////////////////
	$j('img#imgBuscar').click(function(e) {
		buscar(1);
		e.preventDefault();

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
	
	if(typeof(bus) != 'undefined' && $j('#bus').val() != '' ){
		mostrarBusqueda($j('#bus').val());
	}else{
		if ($j('#ter').val() > 0){
			muestraterminal($j('#cab').val(), $j('#int').val(), $j('#ter').val())
		} else if ($j('#cab').val() > 0){
			muestracabecera($j('#cab').val())
		}
	}
});



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


function buscar(sug){
//alert("ok");
//var sug=null;
	var texto = $j("input#patron").val();
	var sugeren = "";
	
	if (sug != null && typeof sug != 'undefined'){
		texto = sug;
		if (typeof $j("input#sugerencias").val() != 'undefined')
			sugeren = $j("input#sugerencias").val();
	}
	expr = /\s+\*/;
	texto = texto.replace(expr, '*');
  window.location.href = "CategoryDisplay?patron=" +  texto ;
}

function mostrar(cabId, catId, subcatId) {
  XID_CAB = cabId;
  XID_CAT = catId;
  XID_SUBCAT = subcatId;
  mostrarCategoriaYSub(cabId, catId, subcatId);  
}

function mostrarterminal(cabId, catId, subcatId) {
  XID_CAB = cabId;
  XID_CAT = catId;
  XID_SUBCAT = subcatId;
  $j('#loading').show();
  mostrarCategoriaYSub(cabId, catId, subcatId); 
   
}

function mostrarCategoriaYSub(cabId, catId, subcatId) {
  $j.get("/FO/PasoDosResultado", {cab: cabId, int: catId, ter: subcatId},
    function(datos) {
      $j("div#div_productos").html(datos);
      eventosProductos();
      $j('#loading').hide();
    }
  );
}

function mostrarCategorias(cabId, catId) {
  $j.get("/FO/PasoDosPub", {cab: cabId, int: catId},function(datos) {
    $j("div#div_productos").html(datos);
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



function whiteBack() {
  $j('#TB_ajaxContent').css({backgroundImage:'url("/FO_IMGS/images/html/fondo_layer_recupera.gif")'});
}
function greenBack() {
  $j('#TB_ajaxContent').css({backgroundImage:'url("/FO_IMGS/images/html/fondo_layer_categorias.gif")'});
}

function justLogin() {
  document.acceso2.url_to.value = "/FO/CategoryDisplay";  
  showLoginSession();
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
    var arrOrden=new Array('Foto','Descripción','Precio','Cantidad','Agregar');
	//evento: ordenar por nombre, precio, precio por unidad
	
	
	$j('select#ordenar_por').change(function(e) {
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
					if ( id	== 'Descripción' ) {
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
		m = $j(':selected', $j(this)).text();
		id = $j(this).val();
//+20120210coh
//		$j('table#tabla_productos tbody tr').each(function() {
		$j('table#tabla_productos tbody tr td ul li').each(function() {
//		$j('div#lista_productos ul li div').each(function() {
//-20120210coh
//alert($j(this).text());
			if( id == 0 || $j(this).text().indexOf(m) >= 0 )
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
			
			if ( $j('#comuna_user_id').val() == 0 ) {
			  XACT_CARRO = "ADD_CARRO";
			  XID_PRODUCTO = idProducto;
			  XCANTIDAD = cantidad;
			  XNOTACAMPO = nota_campo;
			  showRegionesCobertura();
			  return;
			}
			addItemToCart(idProducto,cantidad,nota_campo);
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

function addItemToCart(idProducto,cantidad,nota_campo) {
  $j.post("/FO/PasoDosAgregarCarro", {productoId: idProducto, cantidad: cantidad, nota: nota_campo}, function(datos) {
    $j(datos).find('datos_objeto').each(function() {
      var respuesta = $j(this).text();
      if ( respuesta == "ok" ) {
        $j('img#imagen_'+idProducto).attr('src','/FO_IMGS/img/estructura/paso2/bt_actualizar_carro.gif');
        actualizaCarro();
        limpiaVariablesCarro();
      }
    });
  });
}