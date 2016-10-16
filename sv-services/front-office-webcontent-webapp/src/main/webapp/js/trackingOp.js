function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
  	var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
  	if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];
  	}
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
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}
function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}

function init() {
	filaSelect( $('#estado_txt').val(), $('#id_op_txt').val() );
}

function filaSelect(estado, idOp) {
	moveJumbito(distancia(estado));
	selecFila(idOp);
	$('#nombre_lista').html(idOp);
	$('#estado').html(estado);
}
var idOpOld = "";
function selecFila(idOp) {
	$('#track_' + idOp).attr('src','/FO_IMGS/img/estructura/tracking_op/ver_in.gif');
	if (idOpOld != idOp) {
		if ( $('#track_' + idOpOld) ) {
			$('#track_' + idOpOld).attr('src','/FO_IMGS/img/estructura/tracking_op/ver_off.gif');
		}
	}
	idOpOld = idOp;
}

function moveJumbito(val) {
	if (val > 0) {
		$('#img_jumbito').attr('src','/FO_IMGS/img/estructura/tracking_op/jumbin_caminador.gif');
		$('#jumbito').animate({
				left: val + 'px'
			}, 
			2000, 
			function () {
				$('#img_jumbito').attr('src','/FO_IMGS/img/estructura/tracking_op/jumbito.gif');
			}
		);
	}	
}
function distancia(estado) {
	ocultaSolapas();
	switch (estado) {
		case "Ingresado":
			var solapa1 = document.getElementById('solapa1');
			solapa1.style.visibility = 'visible';
			$('#solapa1').show('slow');//slideDown();
			return 60;
		case "Validado":
			var solapa2 = document.getElementById('solapa2');
			solapa2.style.visibility = 'visible';
			$('#solapa2').css('left','95px');
			$('#solapa2').show('slow');
			return 160;
		case "En Picking":
			var solapa3 = document.getElementById('solapa3');
			solapa3.style.visibility = 'visible';
			$('#solapa3').css('left','193px');
			$('#solapa3').show('slow');
			return 260;
		case "En Bodega":
			var solapa4 = document.getElementById('solapa4');
			solapa4.style.visibility = 'visible';
			$('#solapa4').css('left','291px');
			$('#solapa4').show('slow');
			return 350;
		case "En Camino":
			var solapa5 = document.getElementById('solapa5');
			solapa5.style.visibility = 'visible';
			$('#solapa5').css('left','389px');
			$('#solapa5').show('slow');
			return 450;
		case "Finalizado":
			var solapa6 = document.getElementById('solapa6');
			solapa6.style.visibility = 'visible';
			$('#solapa6').css('left','486px');
			$('#solapa6').show('slow');
			return 550;
	}
	return 0;
}

function ocultaSolapas() {
	$('#solapa1').hide('slow');//slideUp();
	$('#solapa2').hide('slow');
	$('#solapa3').hide('slow');
	$('#solapa4').hide('slow');
	$('#solapa5').hide('slow');
	$('#solapa6').hide('slow');
}