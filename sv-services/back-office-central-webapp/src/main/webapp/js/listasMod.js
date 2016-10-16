
function modificarLista() {
	if (validaFormulario()) {
		document.frm_mod_listas.submit();
	}	
}

function cancelar() {
	document.frm_mod_listas.action = "ViewConfigListasEspeciales";
	document.frm_mod_listas.submit();
}


function validaFormulario() {
	if (Trim($('name_lista').value) == '') {
		alert("Debe ingresar el nombre de la lista.");
		$('name_lista').focus();
		return false;
	}	
	doSelectAll('ids');
	$('grps').value = doJoin('ids');
	return true;	
}

function moveToList(from,to) {
	fromList = eval('document.frm_mod_listas.' + from);
	toList = eval('document.frm_mod_listas.' + to);
	if (toList.options.length > 0 && toList.options[0].value == 'temp') {
		toList.options.length = 0;
	}
	var sel = false;
	for (i=0;i<fromList.options.length;i++) {
		var current = fromList.options[i];
		if (current.selected) {
			sel = true;
			if (current.value == 'temp') {
				alert ('You cannot move this text!');
				return;
			}
			txt = current.text;
			val = current.value;
			toList.options[toList.length] = new Option(txt,val);
			fromList.options[i] = null;
			i--;
		}
	}
	if (!sel) { 
		alert ('No ha seleccionado ninguna opción');
	}
}
function doSelectAll(campo) {
	var obj = document.frm_mod_listas;
	var fld = eval("document.frm_mod_listas." + campo);
	for(var i = 0; i < fld.length; i ++) {
		fld.options[i].selected = true;
	}
}

function doJoin(campo) {
	var obj = document.frm_mod_listas;
	var fld = eval("document.frm_mod_listas." + campo);
	var str = "";
	for(var i = 0; i < fld.length; i ++) {
		str += fld.options[i].value;
		if(i != fld.length) {		
			 str += "-=-";
		}
	}
	return str;
}
//INICIO FUNCIONES GLOBALES
var globalCallbacks = {
                onCreate: function() {
						$("tabla_loading").style.top = document.documentElement.scrollTop + 'px';
						$("loading").show();
                },
                onComplete: function() {
                        if(Ajax.activeRequestCount == 0){
                                $("loading").hide();
                        }
                }
        };
Ajax.Responders.register( globalCallbacks );
//FINAL FUNCIONES GLOBALES
