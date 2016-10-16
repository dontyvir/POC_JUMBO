var XBROWSER = navigator.appName;
var $j = jQuery.noConflict();
jQuery().ready(function() {
	$j("#btnContinuarloader").hide();
	$j("#continuarFormPR").click(function(e) {
		e.preventDefault();
		continuar();
	});

	$j("#btnCerrar").click(function() {
		window.location.reload(true);
	});

});

function continuar() {
	var campos_validos = true;
	$j('#nombre').val($j.trim($j('#nombre').val()));
	$j('#direccion').val($j.trim($j('#direccion').val()));

	var validamail = !/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/.test($j(
			'#email').val());
	if ($j('#nombre').val() == "" || $j('#nombre').val() == "Nombre") {
		alert("Debe ingresar su nombre");
		$j('#nombre').focus();
		campos_validos = false;
	} else if ($j.trim($j('#apellido').val()) == ""
			|| $j.trim($j('#apellido').val()) == "Apellido") {
		alert("Debe ingresar su apellido");
		$j('#apellido').focus();
		campos_validos = false;
	} else if ($j('#rut').val() == "" || $j('#rut').val() == "12345678-9") {
		alert("Debe ingresar un rut");
		$j('#rut').focus();
		campos_validos = false;
	} else if ($j('#rut').val().indexOf("-") < 5) {
		alert("Debe ingresar correctamente un rut con gui\u00f3n y d\u00edgito verificador");
		$j('#rut').focus();
		campos_validos = false;
	} else if (!checkRutField($j('#rut').val())) {
		$j('#rut').focus();
		campos_validos = false;
	} else if ($j.trim($j('#dia').val()) == "" || $j('#dia').val() == "...") {
		alert("Debes ingresar tu dia de nacimiento");
		$j('#dia').focus();
		campos_validos = false;
	} else if ($j.trim($j('#mes').val()) == "" || $j('#mes').val() == "...") {
		alert("Debes ingresar tu mes de nacimiento");
		$j('#mes').focus();
		campos_validos = false;
	} else if ($j.trim($j('#ano').val()) == "" || $j('#ano').val() == "...") {
		alert("Debes ingresar tu año de nacimiento");
		$j('#ano').focus();
		campos_validos = false;
	} else if (($j('#email').val() != "" || $j('#email').val() == "sucorreo@dominio.com")
			&& (validamail == true)) {
		alert("Debes ingresar correctamente tu email");
		$j('#email').focus();
		campos_validos = false;
	} 
	
	else if ($j.trim($j('#email').val()) == "" || $j('#email').val() == "sucorreo@dominio.com")  {
		alert("Debes ingresar  tu email");
		$j('#email').focus();
		campos_validos = false;
	}else if ($j.trim($j('#direccion').val()) == ""
			|| $j('#direccion').val() == "Calle, Número") {
		alert("Debes ingresar tu direcci\u00f3n");
		$j('#direccion').focus();
		campos_validos = false;
	} else if ($j('#comuna').val() == "" || $j('#comuna').val() == "Seleccione"
			|| $j('#comuna').val() == null) {
		alert("Debes ingresar tu comuna");
		$j('#comuna').focus();
		campos_validos = false;
	} else if ($j('#gif_captcha').val() == ""
			|| $j('#gif_captcha').val() == "Captcha") {
		alert("Debe ingresar el codigo de verificación que se muestra en la imagen.");
		$j('#gif_captcha').val('');
		$j('#gif_captcha').focus();
		campos_validos = false;
	} else if ($j('#acepta:checked').val() == ""
			|| $j('#acepta:checked').val() == null) {
		alert("Debes aceptar las Bases Legales");
		$j('#acepta').focus();
		campos_validos = false;
	}

	if (campos_validos) {

		var rutForm = $j('#rut').val().split("-");
		// completo hiddens
		$j('#rutHdn').val(rutForm[0]);
		$j('#dvHdn').val(rutForm[1]);
		$j('#emailHdn').val($j('#email').val());
		$j('#nombreHdn').val($j('#nombre').val());
		$j('#apellidoHdn').val($j('#apellido').val());
		$j('#fechaNacimientoHdn').val(
				$j('#dia').val() + "/" + $j('.mes').val() + "/"
						+ $j('.ano').val());

		$j('#direccionHdn').val($j('#direccion').val());
		$j('#comunaHdn').val($j('#comuna').val());
		$j('#acepta_informacionHdn').val($j('#informacion:checked').val());
		$j('#acepta_basesHdn').val($j('#acepta:checked').val());
		$j('#captcha_Hdn').val($j('#gif_captcha').val().toUpperCase());

		var dataForm = $j("#clientePRForm").serialize();
		$j("#btnContinuar").hide();
		$j("#btnContinuarloader").show();

		$j
				.ajax({
					type : "post",
					url : "/FO/ClientePR",
					beforeSend : function(xhr) {
						// xhr.setRequestHeader('Content-type',
						// 'application/x-www-form-urlencoded;
						// charset=ISO-8859-1');
						// xhr.setRequestHeader('Accept', "text/html;
						// charset=utf-8");
					},
					data : dataForm,
					success : function(response) {
						$j('#coco_01').empty();
						var presp = eval('(' + response + ')');
						var status = parseInt(presp.cod);
						switch (status) {
						case 100:
							alert("Codigo de verificación invalido.");
							$j('#gif_captcha').val('');
							$j('#gif_captcha').focus();
							$j("#btnContinuar").show();
							$j("#btnContinuarloader").hide();
							break;
						case 200:
							var msj = "¡Muchas Gracias!,Tus datos han sido enviados correctamente.";
							$j('#coco_01').html(msj);
							MM_effectAppearFade('apDiv1', 1000, 0, 100, false);
							break;
						case 201:
							var msj = "El cliente ya se encuentra registrado";
							$j('#coco_01').html(msj);
							MM_effectAppearFade('apDiv1', 1000, 0, 100, false);
							break;
						case 500:
							alert("Los datos ingresados son invalidos.");
							window.location.reload(true);
							break;
						default:
							alert("Error al procesar su solicitud, intente mas tarde.");
							window.location.reload(true);
						}

					},
					error : function() {
						alert("No es posible procesar su solicitud, intente mas tarde.");
						window.location.reload(true);
					}
				});

	}
}

function MM_effectAppearFade(targetElement, duration, from, to, toggle) {
	if (to != 0)
		$j('#' + targetElement).show();
	Spry.Effect.DoFade(targetElement, {
		duration : duration,
		from : from,
		to : to,
		toggle : toggle
	});
	if (to == 0)
		$j('#' + targetElement).hide();
}
function MM_swapImgRestore() { // v3.0
	var i, x, a = document.MM_sr;
	for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
		x.src = x.oSrc;
}
function MM_preloadImages() { // v3.0
	var d = document;
	if (d.images) {
		if (!d.MM_p)
			d.MM_p = new Array();
		var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
		for (i = 0; i < a.length; i++)
			if (a[i].indexOf("#") != 0) {
				d.MM_p[j] = new Image;
				d.MM_p[j++].src = a[i];
			}
	}
}

function MM_findObj(n, d) { // v4.01
	var p, i, x;
	if (!d)
		d = document;
	if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
		d = parent.frames[n.substring(p + 1)].document;
		n = n.substring(0, p);
	}
	if (!(x = d[n]) && d.all)
		x = d.all[n];
	for (i = 0; !x && i < d.forms.length; i++)
		x = d.forms[i][n];
	for (i = 0; !x && d.layers && i < d.layers.length; i++)
		x = MM_findObj(n, d.layers[i].document);
	if (!x && d.getElementById)
		x = d.getElementById(n);
	return x;
}

function MM_swapImage() { // v3.0
	var i, j = 0, x, a = MM_swapImage.arguments;
	document.MM_sr = new Array;
	for (i = 0; i < (a.length - 2); i += 3)
		if ((x = MM_findObj(a[i])) != null) {
			document.MM_sr[j++] = x;
			if (!x.oSrc)
				x.oSrc = x.src;
			x.src = a[i + 2];
		}

}

function calendario_dias(ano, mes) {
	var combo = document.cliente.dia.options;
	select = combo[document.cliente.dia.selectedIndex].value;
	if (ano != "" && mes != "") {
		if (mes == 2) {
			if (anyoBisiesto(ano)) {
				febrero = 29;
				combo.length = null;
				combo[0] = new Option("...", "", "", "");
				for (i = 1; i <= febrero; i++)
					combo[i] = new Option(i, i, "", "");
				if (select <= 28)
					document.cliente.dia.selectedIndex = select;
				else
					document.cliente.dia.selectedIndex = "";
			} else {
				febrero = 28;
				combo.length = null;
				combo[0] = new Option("...", "", "", "");
				for (i = 1; i <= febrero; i++)
					combo[i] = new Option(i, i, "", "");
				if (select <= 28)
					document.cliente.dia.selectedIndex = select;
				else
					document.cliente.dia.selectedIndex = "";
			}
		} else {
			totaldia = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];
			combo.length = null;
			combo[0] = new Option("...", "", "", "");
			for (i = 1; i <= totaldia[mes - 1]; i++)
				combo[i] = new Option(i, i, "", "");
			if (select <= totaldia[mes - 1]) {
				document.cliente.dia.selectedIndex = select;
			} else {
				document.cliente.dia.selectedIndex = "";
			}

		}
	}
}
