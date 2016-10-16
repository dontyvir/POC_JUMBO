
function showOlvidaClaveLayer() {	
	
	var frame = document.getElementById('olvido_clave');
	frame.style.visibility = 'visible';
	var largo = screen.availHeight;
	frame.style.width = '450px';
	frame.style.height = '400px';
	frame.style.position = 'absolute';
	frame.style.left = '195px';
	frame.style.top = (largo - 440)/2 + 'px';
	
}

function hideOlvidaClaveLayer() {
	
  var layerOlvidaClave = document.getElementById('olvido_clave');
  layerOlvidaClave.style.visibility = 'hidden';	
  layerOlvidaClave.style.width = '0px';
  layerOlvidaClave.style.height = '0px';  
  closeLBoxWin();
  
}

