var XBROWSER = navigator.appName;
var $j = jQuery.noConflict();
var regClient=0;
jQuery().ready(
		function(){
			$j("#btnContinuarloader").hide();
			$j("#continuarFormSG6").click(
					function(e) {
						e.preventDefault();
						registrarReserva();
					}
			);
			
			$j("#btnCerrar").click(
					function() {
						if(regClient==1){
							showRegistrarClienteSencillo();
							$j('#apDiv1').hide();
						} else {
							window.location.reload(true);
						}
						
						
					}
			);
			
			
		}		
);

function validaFechaCaducidad(){
	if($j('#caducada').val() == 'Y'){
		var msj="Estimado cliente. <br>La fecha para realizar la preventa ha caducado. Gracias por preferirnos.";
		$j('#coco_01').html(msj);
		MM_effectAppearFade('apDiv1', 1000, 0, 100, false);
		window.close();
	}
}

function cargarDatosCliente(){
	regClient=0;
	if($j('#rut').val().indexOf("-") < 5){
		alert("Debe ingresar correctamente un rut con gui\u00f3n y d\u00edgito verificador");
		$j('#rut').focus();
	} else {
		var rutForm = $j('#rut').val().split("-");		
		//completo hiddens
		if(rutForm[1]=='k'){
			rutForm[1]='K';
		}
		
		$j('#rutHdn').val(rutForm[0]);
		
		$j('#dvHdn').val(rutForm[1]);
		$j('#criteria').val("findByRut");
		var dataForm=$j("#clientePreventaS6Form").serialize();
		$j.ajax({
			type: "post",
			url: "/FO/PreventaSG6", 
			beforeSend:function(xhr){
				},
			data:dataForm, 
		    success: function(response) {
		    	$j('#coco_01').empty();
		    	var presp = eval('('+response+')');
	            var status = parseInt(presp.cod);	           
	            switch(status){
	            	case 0:
	            		$j('#nombres').val(presp.nombreHdn);
	            		$j('#apellido').val(presp.apellidoHdn);
	            		$j('#email').val(presp.emailHdn);
	            		$j('#telefono').val(presp.telefonoHdn);
	            		$j('#cli_idHdn').val(presp.cli_id);
	            		
	            		$j("#btnContinuar").show();
	            		$j("#btnContinuarloader").hide();
					break;
					case 1:
						regClient=1;
						var msj="Para realizar la reserva de su equipo, debe registrarse en Jumbo.cl";
						$j('#coco_01').html(msj);
	            		MM_effectAppearFade('apDiv1', 1000, 0, 100, false);
						break;	
					case 3: 
						alert("El rut ingresado no es válido. Intente nuevamente.");
						window.location.reload(true);
						break;	
					default:
						alert("Error al procesar su solicitud, intente mas tarde.");
						window.location.reload(true);
						break;
				}	    
	      
			},
		    error: function() {
		    	alert("No es posible procesar su solicitud, intente mas tarde.");
		    	window.location.reload(true);
		    }		    
		}); 
	}	
}

function registrarReserva(){
	var rutForm = $j('#rut').val().split("-");		
	var idModelo= $j('#modelo').val();
	if(null == rutForm || rutForm == "" || rutForm[0] == "12345678"){
		alert("Debe ingresar un rut de cliente válido.");
	} else if(idModelo == 0){
		validaModelo();
	}else if ($j('#acepta:checked').val() == "" || $j('#acepta:checked').val() == null ){
		alert("Debes aceptar las Bases Legales");
		$j('#acepta').focus();
	} else {
		//completo hiddens
		$j('#rutHdn').val(rutForm[0]);
		$j('#dvHdn').val(rutForm[1]);
		$j('#criteria').val("registrarPreventa");
		$j('#nombreHdn').val($j('#nombres').val());
		$j('#apellidoHdn').val($j('#apellido').val());
		$j('#emailHdn').val($j('#email').val());
		$j('#telefonoHdn').val($j('#telefono').val());
		$j('#id_modelohdn').val($j('#modelo').val());
		var dataForm=$j("#clientePreventaS6Form").serialize();
		$j("#btnContinuar").hide();
		$j("#btnContinuarloader").show();
		$j.ajax({
			type: "post",
			url: "/FO/PreventaSG6", 
			beforeSend:function(xhr){
				},
			data:dataForm, 
		    success: function(response) {
		    	$j('#coco_01').empty();
		    	var presp = eval('('+response+')');
	            var status = parseInt(presp.cod);	           
	            switch(status){
	            	case 0:
	            		var msj="¡El formulario fue enviado con éxito! <br>Enviaremos a tu e-mail, la información con el detalle de reserva.";
						$j('#coco_01').html(msj);
	            		MM_effectAppearFade('apDiv1', 1000, 0, 100, false);
					break;
					case 1:
						var msj="Estimado cliente, usted ya ha alcanzado el máximo de equipos reservados.";
						$j('#coco_01').html(msj);
	            		MM_effectAppearFade('apDiv1', 1000, 0, 100, false);
	            		$j("#btnContinuar").show();
						$j("#btnContinuarloader").hide();
						break;	
					case 3: 
						alert("El equipo seleccionado no se encuentra disponible.");
						$j("#btnContinuar").show();
						$j("#btnContinuarloader").hide();
						break;	
					case 4:
						alert("Los datos ingresados son incorrectos.");
						$j("#btnContinuar").show();
						$j("#btnContinuarloader").hide();
						break;
					default:
						alert("Error al procesar su solicitud, intente mas tarde.");
						window.location.reload(true);
						break;
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

function showRegistrarClienteSencillo() {
	  upWeb();
	  showLightBoxRegistroReserva();
	  $j('#registra_cliente').show();
	  $j(document).click(function(e){
		  id = $j(this).val();
	  });
	//traemos las regiones
	  var id_comuna = 0;
	  var id_region = 0;
	  var region = 0;
	  
	  if (typeof $j("#id_region").val() === "undefined"){
	    id_region = 0;
	  }else{
	    id_region = $j("#id_region").val();
	  }
	  
	  if (typeof $j("#id_comuna").val() === "undefined"){
	    id_comuna = 0;
	  }else{
	    id_comuna = $j("#id_comuna").val();
	  }
	  
	  
	  //alert("$j('#id_region').val(): " + $j("#id_region").val());
	  //alert("$j('#id_comuna').val(): " + $j("#id_comuna").val());
	  /*if (id_comuna > 0){
	    id_comuna = $j("#id_comuna").val();
	  }*/
	  
	  //alert("showRegistro 1 ==> id_region: " + id_region + " |  id_region_reg: " + $j("#id_region_reg").val());
	  if (id_region > 0){
	    $j("#id_region_reg").val(id_region);
	    region = id_region;
	    //alert("showRegistro 2 ==> id_region: " + id_region + " |  id_region_reg: " + $j("#id_region_reg").val());
	  }
	  
	  if ($j("#id_region_reg").val() > 0){
	    region = $j("#id_region_reg").val();
	  }

	  //alert("region: " + region);
	  //$j.get("/FO/RegionesConCobertura",{id_region:$j("#id_region_reg").val()},function(datos) {
	  $j.get("/FO/RegionesConCobertura",{id_region:region}, function(datos) {
	    $j("#id_region_reg").html(datos);
	    //TODO aca seleccionar combo de region
	  });
	  
	  //alert("id_region: " + id_region);
	  //if (id_region > 0){
	    //alert("showRegistro 3 ==> id_region: " + id_region + " |  id_comuna: " + id_comuna);
	    getComunasByRegionRegistro(id_region, id_comuna);
	  //}
	  document.getElementById("envioEMail").checked=1;
	  document.getElementById("envioSMS").checked=1;
	  document.getElementById("terminos").checked=1;
}

function getComunasByRegionRegistro(idRegion, idComuna) {
	  //var idComuna=0;
	  //if ( idRegion == 0 ) {
	  //  return;
	  //}
	  /*if ($j("#id_comuna").val() > 0){
	    idComuna = $j("#id_comuna").val();
	  }*/
	  if (typeof idRegion === "undefined"){
	    idRegion=0;
	    //alert("something is undefined");
	  }
	  
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
function showLightBoxRegistroReserva() {
	  tb_show('Jumbo.cl','#TB_inline?height=450&width=620&inlineId=hiddenModalContent&modal=true');
	}

function logonRegSencillo(msg){
	closeLBoxWin();
}
function validaModelo(){
	var idModelo= $j('#modelo').val();
	if(idModelo == 0){
		alert("El modelo de samsung seleccionado se encuentra agotado.\nFavor seleccione otro modelo.");
		$j('#modelo').focus();
	} else if ( idModelo == 1){
		alert("Debe seleccionar un modelo de celular Samsung de la lista.");
		$j('#modelo').focus();
	}
}

function cleanDatosPreventa(){
	$j('#nombres').val("");
	$j('#apellido').val("");
	$j('#email').val("");
	$j('#telefono').val("");
}