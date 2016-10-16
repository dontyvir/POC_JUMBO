function guarda_compra() {
	if (document.frmGuardaCompra.opcioncheck.checked == false && document.frmGuardaCompra.txtNewNombre.value == 'ej: casa mamá' ){
		alert("Debes cambiar el nombre de ejemplo propuesto");
		document.frmGuardaCompra.txtNewNombre.focus();
		return;	
	}
	if (document.frmGuardaCompra.opcioncheck.checked == false && document.frmGuardaCompra.txtNewNombre.value == '' ) {
		alert("Ingrese un nombre para su compra");
		document.frmGuardaCompra.txtNewNombre.focus();
		return;	
	}
	if ( document.frmGuardaCompra.opcioncheck.checked == true ) {
		document.frmGuardaCompra.opcionradio.value = 0;
	} else {
		document.frmGuardaCompra.opcionradio.value = 1;	
	}
	document.frmGuardaCompra.action = "OrderSetListMobi";
	document.frmGuardaCompra.submit();
}

function inhabilita_campo() {
	if (document.frmGuardaCompra.opcioncheck.checked == true) {
		document.frmGuardaCompra.txtNewNombre.readOnly = true;
	} else {
		document.frmGuardaCompra.txtNewNombre.readOnly = false;
	}
}