/*
Si queremos usar más de un acordeon debemos crear el estilo correspondiente y definimos

<ul id="menu1" 
..
..

y

<ul id="menu2"
..

$(document).ready(function() {
iniciarAcordeon('1');
iniciarAcordeon('2');

*/
function iniciarAcordeon(acord) {
	$('ul.acord' + acord + ' ul').hide();
	$('ul.acord' + acord + ' li a').click(
		function() {
			var checkElement = $(this).next();
			var parent = this.parentNode.parentNode.id;
			if ( checkElement.css('display') == 'none' ) {
				marcarActivo(this.id); //Vamos a abrir la seccion
			} else {
				marcarDesactivo(this.id); //Vamos a cerrar la seccion
			}
			if($('#' + parent).hasClass('acordeon')) {
				$(this).next().slideToggle('normal');				
				return false;
			}
		}
	);
}
function expandAll(acordeon) {
	$('ul.acord'+acordeon+' div').show();
}
function hideAll(acordeon) {
	$('ul.acord'+acordeon+' div').hide();
}
function expandById(id) {
	marcarActivo(id);
	$('#'+id).next().slideDown('normal');
}
function hideById(id) {
	marcarDesactivo(id);
	$('#'+id).next().slideUp('normal');
}
function marcarActivo(id) {
	$('#' + id).removeClass('acordDesactivo');
	$('#' + id).addClass('acordActivo');
	$('#' + id).css('color','#FFFFFF'); // Esto se puso aca por q x extraña razon no tomaba el color del estilo
}
function marcarDesactivo(id) {
	$('#' + id).removeClass('acordActivo');
	$('#' + id).addClass('acordDesactivo');
	$('#' + id).css('color','#666666');
}
$(document).ready(function() {
	iniciarAcordeon('1');
	onLoadSustitutos();
	var abrir_acordeon = $('#prod_abrir_acordeon').attr('value').split(',');
	for ( var i = 0; i < abrir_acordeon.length; i++ ) {
		expandById( 'cat_' + abrir_acordeon[i] );
	}
});