var XBROWSER = navigator.appName;
var $j = jQuery.noConflict();
jQuery().ready(
		function(){
			$j("#btnContinuarloader").hide();
			$j("#continuarFormKCC").click(
					function(e) {
						e.preventDefault();
						continuar();
					}
			);
			
			$j("#btnCerrar").click(
					function() {window.location.reload(true);}
			);
			
			
		}		
);

function continuar() {
    var campos_validos = true;
    $j('#nombres').val($j.trim($j('#nombres').val()));
    $j('#direccion').val($j.trim($j('#direccion').val()));
    $j('#boleta').val($j.trim($j('#boleta').val()));
    
    
    
    var validamail = !/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/.test( $j('#email').val());
    if ($j('#nombres').val() == "" || $j('#nombres').val() == "Nombre y Apellido") {
		alert("Debe ingresar su nombre y apellido");
		$j('#nombres').focus();
		campos_validos = false;
    } else if($j.trim($j('#nombres').val())==""){
		alert("Debe ingresar su nombre y apellido");
		
		$j('#nombres').focus();
		$j('#nombres').val("");
		campos_validos = false;
    } else if ($j('#rut').val() == "" || $j('#rut').val() == "12345678-9") {
		alert("Debe ingresar un rut");
		$j('#rut').focus();
		campos_validos = false;
    } else if($j('#rut').val().indexOf("-") < 5){
		alert("Debe ingresar correctamente un rut con gui\u00f3n y d\u00edgito verificador");
		$j('#rut').focus();
		campos_validos = false;
    }else if (!checkRutField($j('#rut').val())){
    	$j('#rut').focus();
		campos_validos = false;
	} else if (($j('#email').val() != "" || $j('#email').val() != "Email") &&
			(validamail == true)
	    	) {
	    	alert("Debes ingresar correctamente tu email");
	    	$j('#email').focus();
	    	campos_validos = false;  
	} else if ($j.trim($j('#direccion').val()) == "" || $j('#direccion').val() == "Calle, Número") {
		alert("Debes ingresar tu direcci\u00f3n");
		$j('#direccion').focus();
		campos_validos = false;
	} else if ($j('#comuna').val() == "" || $j('#comuna').val() == "Seleccione" || $j('#comuna').val() == null) {
		alert("Debes ingresar tu comuna");
		$j('#comuna').focus();
		campos_validos = false;
	}else if ($j.trim($j('#boleta').val()) == "" || $j('#boleta').val() == "N\u00famero boleta") {
		alert("Debes ingresar el n\u00famero de tu Boleta");
		$j('#boleta').focus();
		$j('#boleta').val("");
		campos_validos = false;
	
	}else if ($j('input:radio[name=sexo]:checked').val()=="" || $j('input:radio[name=sexo]:checked').val()== null){
		alert("Debe seleccionar el Genero del Bebe");
		$j('#sexo').focus();
		campos_validos = false;
	}else if ($j('input:radio[name=panal]:checked').val() == "" || $j('input:radio[name=panal]:checked').val() == null ){
		alert("Debes seleccionar talla del Pañal");
		$j('#panal').focus();
		campos_validos = false;
	}else if ($j('input:radio[name=panal]:checked').val() == "" || $j('input:radio[name=panal]:checked').val() == null ){
		alert("Debes seleccionar talla del Pañal");
		$j('#panal').focus();
		campos_validos = false;
	}else if ($j('#gif_captcha').val() == "" || $j('#gif_captcha').val() == "Captcha") {
		alert("Debe ingresar el codigo de verificación que se muestra en la imagen.");
		$j('#gif_captcha').val('');
		$j('#gif_captcha').focus();
		campos_validos = false;
    }else if ($j('#acepta:checked').val() == "" || $j('#acepta:checked').val() == null ){
		alert("Debes aceptar las Bases Legales");
		$j('#acepta').focus();
		campos_validos = false;
	}
    
	if (campos_validos) {
		
		var rutForm = $j('#rut').val().split("-");		
		//completo hiddens
		$j('#rutHdn').val(rutForm[0]);
		$j('#dvHdn').val(rutForm[1]);
		$j('#emailHdn').val($j('#email').val());
		$j('#sexoHdn').val($j('input:radio[name=sexo]:checked').val());
		$j('#tallaHdn').val($j('input:radio[name=panal]:checked').val());
		$j('#nombreHdn').val($j('#nombres').val());
		$j('#direccionHdn').val($j('#direccion').val());
		$j('#comunaHdn').val($j('#comuna').val());	
		$j('#numero_boletaHdn').val($j('#boleta').val());
		$j('#anno_bebeHdn').val($j('#annos').val());
		$j('#meses_bebeHdn').val($j('#meses').val());		
		$j('#acepta_informacionHdn').val($j('#informacion:checked').val());
		$j('#acepta_basesHdn').val($j('#acepta:checked').val());
		$j('#captcha_Hdn').val($j('#gif_captcha').val().toUpperCase());
		
		var dataForm=$j("#clienteKccForm").serialize();
		$j("#btnContinuar").hide();
		$j("#btnContinuarloader").show();
		
		$j.ajax({
			type: "post",
			url: "/FO/ClienteKCC", 
			beforeSend:function(xhr){
				//xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded; charset=ISO-8859-1');
				//xhr.setRequestHeader('Accept', "text/html; charset=utf-8");
				},
			data:dataForm, 
		    success: function(response) {
		    	$j('#coco_01').empty();
		    	var presp = eval('('+response+')');
	            var status = parseInt(presp.cod);	           
	            switch(status){
	            	case 100:
	            		alert("Codigo de verificación invalido.");
	            		$j('#gif_captcha').val('');
	            		$j('#gif_captcha').focus();
	            		$j("#btnContinuar").show();
	            		$j("#btnContinuarloader").hide();
					break;
					case 200:
						var msj="¡Muchas Gracias!,Tus datos han sido enviados correctamente, dentro de 3 semanas recibirás tu muestra gratis.Visítanos en Clubhuggies.cl";
						$j('#coco_01').html(msj);
	            		MM_effectAppearFade('apDiv1', 1000, 0, 100, false);
						break;
					case 201:
						var msj="El cliente ya se encuentra registrado";
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
		    error: function() {
		    	alert("No es posible procesar su solicitud, intente mas tarde.");
		    	window.location.reload(true);
		    }		    
		}); 
		
	}
}

function MM_effectAppearFade(targetElement, duration, from, to, toggle)
{
	if (to!=0) $j('#'+targetElement).show();
	Spry.Effect.DoFade(targetElement, {duration: duration, from: from, to: to, toggle: toggle});
	if (to==0) $j('#'+targetElement).hide();	
}
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}
    }
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];
   }
 
 
}
