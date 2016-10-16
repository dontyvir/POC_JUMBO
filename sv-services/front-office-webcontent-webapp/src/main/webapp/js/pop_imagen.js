function desplegar_imagen(ruta,obj) {
var ancho = document.documentElement.scrollWidth; // screen.availWidth;
var largo = document.documentElement.scrollHeight; // screen.availHeight;

//alert(document.documentElement.scrollMargenTop);
//alert(ancho);
//alert(largo);



	var ima = document.getElementById("img_des");
	ima.src = ruta;	
	var pos = findPos(obj);
	
//alert(pos[0]);	
//alert(pos[1]);
	var desplegar = document.getElementById("desplegar");
	desplegar.style.left = pos[0] - 172 + 'px';
	if(pos[1] - 130 < 0){
		desplegar.style.top = '0px';
	}else{
		desplegar.style.top = pos[1] - 130 + 'px';
	}
	desplegar.style.width = 180 + 'px';
	desplegar.style.height = 180 + 'px';
	desplegar.style.visibility = 'visible'
}

function esconder_imagen(){
	var desplegar = document.getElementById("desplegar");
	desplegar.style.left = 0;
	desplegar.style.top = 0;
	desplegar.style.width = 0 + 'px';
	desplegar.style.height = 0 + 'px';
	desplegar.style.visibility = 'hidden'
}