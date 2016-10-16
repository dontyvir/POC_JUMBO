var hizo_cambio = false;

function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; 
  for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) 
  	x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; 
  if(d.images) { 
  	if(!d.MM_p) 
		d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; 
	for(i=0; i<a.length; i++)
    	if (a[i].indexOf("#")!=0) { 
			d.MM_p[j]=new Image; 
			d.MM_p[j++].src=a[i];
    	}
   }
}
function MM_findObj(n, d) { //v4.01
  var p,i,x;  
  if(!d) 
  	d=document; 
  if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) 
  	x=d.all[n]; 
  for (i=0;!x&&i<d.forms.length;i++) 
  	x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) 
  	x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) 
  	x=d.getElementById(n); 
  return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; 
  for(i=0;i<(a.length-2);i+=3)
   	if ((x=MM_findObj(a[i]))!=null) {
		document.MM_sr[j++]=x; 
		if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];
   }
}

function selectTodos(idCategoria, posicion) {
	//posicion ->  0:Criterio Jumbo, 1:Misma marca, 2:Mismo tamano, 4:No sustituir
	if ( $('#checkbox_' + idCategoria + '_' + posicion ).attr('checked') ) {
		cambio();
		var obj = document.form;
		var prods = $('#prods_' + idCategoria).attr('value').split(',');
		for ( var i = 0; i < prods.length; i++ ) {
			var rad = eval("obj.radiobutton_" + idCategoria + "_" + prods[i]);
			if (rad) {
				rad[posicion].checked = true;
			}
		}
		for ( var i = 0; i <= 4; i++ ) {
			var chek = eval("obj.checkbox_" + idCategoria + "_" + i);
			if ( chek && (i != posicion) ) {
				chek.checked = false;
			}
		}
	}
}

function validarSustitutos() {
	var obj = document.form;
	var cats = $('#categorias').attr('value').split(',');
	for ( var j = 0; j < cats.length; j++ ) {
		var prods = $('#prods_' + cats[j] ).attr('value').split(',');
		for ( var i = 0; i < prods.length; i++ ) {
			var rad = eval("obj.radiobutton_" + cats[j] + "_" + prods[i]);
			var check = false;
			for ( var h = 0; h < rad.length; h++) {
				if ( rad[h].checked ) {
					check = true;
					if ( h == 3 ) {
						var tex = eval("obj.textfield_" + cats[j] + "_" + prods[i]);
						if ( jQuery.trim(tex.value) == 'Ej: marca, sabor' ) {
							expandById( 'cat_' + cats[j] );
							document.location.href = "#cat_" + cats[j];
							alert("Si seleccionó la opción 'Otro',\ndebe escribir su preferencia de sustitución.");
							tex.focus();
							tex.select();
							return;
						}
					}
				}
			}
			if ( !check ) {
				expandById( 'cat_' + cats[j] );
				document.location.href = "#cat_" + cats[j];
				alert("Debes seleccionar el criterio de sustitución.");
				rad[0].focus();
				return;
			}
		}
	}
	obj.submit();
}

function selectOpcion( idCategoria, posicion ) {
	cambio();
	var obj = document.form;
	for ( var i = 0; i <= 4; i++ ) {
		var chek = eval("obj.checkbox_" + idCategoria + "_" + i);
		if ( chek && (i != posicion) ) {
			chek.checked = false;
		}
	}
}

function cambioTexto(id_categoria, id_producto) {
	var obj = document.form;
	var rad = eval("obj.radiobutton_" + id_categoria + "_" + id_producto);
	if (rad[3]) {
		rad[3].checked = true;
	}
	cambio();
}
function cambio() {
	hizo_cambio = true;
}

function salir(destino, accion) {
	if (hizo_cambio) {
		if (!confirm("¿Está seguro que desea " + accion + " sin guardar?")) {
			return;
		}	
	}
	window.location.href = destino;
}

function showLayer() {
	MM_showHideLayers('ventana_layer','','show');
	var layerEvento = document.getElementById('ventana_layer');
	var largo = screen.availHeight;
	var ancho = document.documentElement.scrollWidth;
	layerEvento.style.width = '380px';
	layerEvento.style.height = '285px';
	layerEvento.style.left = (ancho-380)/2 + 'px';
	layerEvento.style.top = (largo-340)/2 + 'px';
}

function hideLayer() {
	MM_showHideLayers('ventana_layer','','hide');
	var layerEvento = document.getElementById('ventana_layer');
	layerEvento.style.width = '0px';
	layerEvento.style.height = '0px';
}

function onLoadSustitutos() {
	MM_preloadImages('/FO_IMGS/img/estructura/sustitutos/bt_volverb.gif','/FO_IMGS/img/estructura/sustitutos/bt_guardarb.gif');
	
	if ( $('#save').attr('value') == 'SI' ) {
		showLayer();
	}
}

function clicOtro(campo) {
	if ( jQuery.trim(campo.value) == 'Ej: marca, sabor' ) {
		campo.value = "";
	}
}
function outOtro(campo) {
	if ( jQuery.trim(campo.value) == '' ) {
		campo.value = "Ej: marca, sabor";
	}
}
