var M=false;
var N=false;

function init() 
{
	M=false;
	N=false;
	if (navigator.appName.slice(0,1)=='N') 
		N=true; 
	else 
		M=true;
	cm=null;
	wipeOn=null;
	wipeTime=null;
	if (N) 
	{
		document.captureEvents(Event.MOUSEOVER);
		document.captureEvents(Event.BLUR);
	}
	document.onmouseover=menuControl;
	document.onblur=menuControl;
}

function getPos(obj) {
	var curwidth = curheight = 0;
	if (obj != null) {
		if (obj.offsetParent) {
			curwidth = obj.offsetWidth;
			curheight = obj.offsetHeight;
			while (obj = obj.offsetParent) {
				curwidth += obj.offsetWidth;
				curheight += obj.offsetHeight;
			}
		}	
	}
	return [curwidth,curheight];
}

function show(el,m) {
	var pos = findPos(el);
	with (m.style) {
		clip='rect(0px,0px,0px,0px)';
		display='';
		left=pos[0] + el.offsetWidth + 'px';
		top=pos[1] + 'px';
	}
	if ((m != cm) && (cm != null)) 
		cm.style.display = 'none';
	if (m != null) {	
		cm = m;
		wipePos = 0;
		var aux = getPos(m);
		wipeHeight = aux[1];
		wipeWidth = aux[0];
		wipeOn = true;
		wipe();
	}	
}

function wipe() {
	if (wipeOn) {
		wipePos+=4;
		if (wipePos > wipeHeight + 4) {
			wipeOn=false;
			return;
		}
	}
	else 
		return;
	if (cm != null){
		cm.style.clip='rect(0px,' + wipeWidth + 'px,' + wipePos + 'px,0px)';
	}
	wipeTime=setTimeout('wipe()',1);
}

function hide() {
	if (cm!=null) 
		cm.style.display='none';
	cm=null;
}

function menuControl(e) {
	if (N) {
		es=e.target;
	}	
	if (M) {
		es=event.srcElement;
	}	
	esi = es.id;
	var es1;
	if (esi != void 0) {
		es1 = esi.substring(0,1);
		es2 = esi.substring(1,2);
		cmid = cm?cm.id.substring(2,3):'';
		if (es1 == 'z' && es2 != cmid){ 
			show(es,document.getElementById('sd'+es2));
		} else if (es1 != 'z' && es1 != 's') {
			wipeOn=false;
			clearTimeout(wipeTime);
			hide();
		}	
	} else {
		if (es1 != 'z' && es1 != 's') {
			wipeOn=false;
			clearTimeout(wipeTime);
			hide();
		}
	}	
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
   if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
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