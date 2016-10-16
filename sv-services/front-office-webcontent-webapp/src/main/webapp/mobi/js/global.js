//INI - Funciones Iphone
addEventListener("load", function() {
	setTimeout(updateLayout, 0);
}, false);
var currentWidth = 0;
function updateLayout() {
	if (window.innerWidth != currentWidth) {
		currentWidth = window.innerWidth;
		
		var orient = currentWidth == 320 ? "profile" : "landscape";
		if (document.body) {
			document.body.setAttribute("orient", orient);
		}
		if (currentWidth == 320)
			floatingMenu.targetX=-320;
		else if (currentWidth == 480)
			floatingMenu.targetX=-480;
		setTimeout(function() {
			window.scrollTo(0, 1);
		}, 100);            
	}
}
setInterval(updateLayout, 100);
window.scrollTo(0, 1);
//FIN - Funciones Iphone

function salirDelSitio() {
	if (confirm("¿Está seguro que desea salir?")) {
		document.location.href = "LogoffMobi";
	}
}

function TrimLeft(s) {
   return s.replace(/^\s+/, "");
}

function TrimRight(s) {
   return s.replace(/\s+$/, "");
}

function Trim(s) {
   return TrimRight(TrimLeft(s));
}