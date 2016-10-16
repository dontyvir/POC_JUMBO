function checklength(nomtxt) {
	var max = 250;
	var txt;
	txt=document.getElementById(nomtxt.name);
	var n = txt.value.length;
	if (n>=max)	{
		alert('Haz excedido el limite máximo de caracteres permitidos');
		txt.value = txt.value.substring(0, max-1); 
		return false;
	}
}
function down( campo, incremento, maximo ) {
	var valor = 0;
	if (campo.value == "" || campo.value == "undefined") {
		campo.value = 0;
	}
	valor = parseFloat(campo.value) - parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	if ( valor < 0 ) {
		campo.value = 0;
		return;
	} else {
		campo.value = valor;
	}
}
function up( campo, incremento, maximo ) {
	var valor = 0;
	if (campo.value == "" || campo.value == "undefined") {
		campo.value = 0;
	}
	valor = parseFloat(campo.value) + parseFloat(incremento);
	valor = Math.round(valor*1000)/1000;
	campo.value = valor;
	if ( validar_cantidad( campo, maximo ) == false ) {
		campo.value = maximo;
	}
}
function validarEmailDominio(valor,dominio){
	validRegExp = /^[^@]+@[^@]+.[a-z]{2,}$/i;
	strEmail = valor.value + '@' + dominio.value;
	if (strEmail.search(validRegExp) == -1) {
		alert("La dirección de email es incorrecta");
		valor.focus();
		return (false);
	}
	return (true);
}
function validaNumeros(myNum) {
	var bFound = /^\d+$/.test(myNum);
	return (bFound);
}
function calendar_dias(ano,mes){
	var combo = document.cliente.dia.options;
	select  = combo[document.cliente.dia.selectedIndex].value;
	if (ano != "" && mes != ""){
		if(mes == 2){
			if(anyoBisiesto(ano)){
				febrero=29;
				combo.length = null;
			    combo[0] =  new Option("...","","","");
			    for (i=1; i<=febrero; i++)
	    			combo[i] = new Option(i,i,"","");
				if	(select <= 28)
	    			document.cliente.dia.selectedIndex = select;
	    		else
		    		document.cliente.dia.selectedIndex = "";
			}else{
				febrero=28;
				combo.length = null;
			    combo[0] =  new Option("...","","","");
			    for (i=1; i<=febrero; i++)
	    			combo[i] = new Option(i,i,"","");
				if	(select <= 28)
	    			document.cliente.dia.selectedIndex = select;
	    		else
		    		document.cliente.dia.selectedIndex = "";
			}
		}else{
			totaldia = [31,28,31,30,31,30,31,31,30,31,30,31];
			combo.length = null;
		    combo[0] =  new Option("...","","","");
		    for (i=1; i<=totaldia[mes-1]; i++)
				combo[i] = new Option(i,i,"","");
			if (select <= totaldia[mes-1]){
				document.cliente.dia.selectedIndex = select;
			}else{
				document.cliente.dia.selectedIndex = "";
			}
		}
	}
}
function anyoBisiesto(anyo)	{
	if (anyo < 100)
		var fin = anyo + 1900;
	else
		var fin = anyo ;
	if (fin % 4 != 0)
		return false;
	else{
		if (fin % 100 == 0){
			if (fin % 400 == 0){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return true;
		}
	}
}