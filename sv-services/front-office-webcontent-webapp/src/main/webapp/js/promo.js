//FUNCIONES UTILES QUE SE EJECUTAN AL MOMENTO DE VER PROMOCIONES DESDE EL CARRO
function showResumenPromo() {
	showPromoPaso3();
}

function hideResumenPromo() {
	hidePromoPaso3();
}

function showPromoProducto() {
MM_showHideLayers('ventanapromoproducto','','show');
var layerPromoProducto = document.getElementById('ventanapromoproducto');
var ancho = screen.availWidth;
var largo = screen.availHeight;
var ypos = document.body.scrollTop;
layerPromoProducto.style.width = '395px';
layerPromoProducto.style.height = '240px';
layerPromoProducto.style.zIndex = '10';
layerPromoProducto.style.left = (ancho-395)/2 + 'px';
layerPromoProducto.style.top = (largo-400)/2 + ypos + 'px';
}

function hidePromoProducto() {
MM_showHideLayers('ventanapromoproducto','','hide');
var layerPromoProducto = document.getElementById('ventanapromoproducto');
layerPromoProducto.style.width = '0px';
layerPromoProducto.style.height = '0px';
}


//FUNCIONES UTILES QUE SE EJECUTAN AL MOMENTO DE VER PROMOCIONES DESDE EL CARRO
function showMisPromo() {
MM_showHideLayers('ventanamispromo','','show');
var layerResumenPromo = document.getElementById('ventanamispromo');
var ancho = screen.availWidth;
var largo = screen.availHeight;
var ypos = document.body.scrollTop;
layerResumenPromo.style.width = '395px';
layerResumenPromo.style.height = '240px';
layerResumenPromo.style.left = (ancho-395)/2 + 'px';
layerResumenPromo.style.top = (largo-400)/2 + ypos + 'px';
}

function hideMisPromo() {
MM_showHideLayers('ventanamispromo','','hide');
var layerResumenPromo = document.getElementById('ventanamispromo');
layerResumenPromo.style.width = '0px';
layerResumenPromo.style.height = '0px';
}


function resumen_promo() {
  frames['frm_respromo'].location.href="ViewPromocionesCh";
}

function mis_promo(){
	frames['frm_mispromo'].location.href="ViewMisPromociones";
}

//FUNCIONES UTILES QUE SE EJECUTAN AL MOMENTO DE VER PROMOCIONES DESDE EL PASO3
function showPromoPaso3() {
	MM_showHideLayers('ventanarespromo','','show');	
	var ypos = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
	var layerResumenPromo = document.getElementById('ventanarespromo');
	layerResumenPromo.style.width = '395px';
	layerResumenPromo.style.height = '240px';
	layerResumenPromo.style.left = (screen.availWidth-395)/2 + 'px';
	layerResumenPromo.style.top = (screen.availHeight-300)/2 + ypos - 50 + 'px';
}

function hidePromoPaso3() {
	MM_showHideLayers('ventanarespromo','','hide');
	var layerResumenPromo = document.getElementById('ventanarespromo');
	layerResumenPromo.style.width = '0px';
	layerResumenPromo.style.height = '0px';
}

function ver_promo(atrib){
    frames['frm_promo_paso3'].location.href="ViewPromocionesP3?atrib="+atrib;
}