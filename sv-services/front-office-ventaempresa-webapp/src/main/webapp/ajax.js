////////////////////////////////////////////////
	var request = false;
	var funcexec = '';
	function define_AJAXRPC(){
	   try {
		 request = new XMLHttpRequest();
	   } catch (trymicrosoft) {
		 try {
		   request = new ActiveXObject("Msxml2.XMLHTTP");
		 } catch (othermicrosoft) {
		   try {
			 request = new ActiveXObject("Microsoft.XMLHTTP");
		   } catch (failed) {
			 request = false;
		   }  
		 }
	   }
		if (!request)
			alert("Error initializing XMLHttpRequest!");
	}

	function send_AJAXRPC(method, url, mfunc){
		request.open(method, url, true);
		request.onreadystatechange = receive_AJAXRPC;
		funcexec = mfunc;
		request.send(null);
	}

	function receive_AJAXRPC(){
     if (request.readyState == 4) {
       if (request.status == 200) {
		 if (funcexec.length > 0) {
		 	text = funcexec+"('" + request.responseText + "')";
			eval(text);
		 }
		 else {
			alert('No se ha definido nombre de funcion para respuesta');
		 }
       } 
	   else {
         alert("status is " + request.status);
	   }
     }
	}

	/************************************************************************************************/
	/* Funcion exec_AJAXRPC																			*/
	/*		- metodo:	"POST" o "GET"																*/
	/*		- url:		Url de la CGI que se invocar?												*/
	/*		- fprocesa:	Funcion que procesara la respuesta. Esta funcion debe tener un unico		*/
	/*					parametro de entrada que corresponde al texto retornado por la CGI			*/
	/************************************************************************************************/
	function exec_AJAXRPC(metodo, url, fprocesa){
		define_AJAXRPC();
		send_AJAXRPC(metodo, url, fprocesa);
	}

////////////////////////////////////////////
