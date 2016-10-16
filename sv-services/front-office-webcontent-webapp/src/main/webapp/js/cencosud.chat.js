var CHAT_IS_ACTIVE	= true;	// Para manejar la activacion del chat
var NOMBRE 			= "";	// Nombre del cliente
var APELLIDO 		= "";	// Apelido del cliente
var DIA 			= "";	// lun,mar,mié,jue,vie,sáb,dom
var HORA 			= 0;	// La hora es un entero con el formato HHmm. Por ejemplo 21:52 hrs es igual a 2152

function launchChat() { 
  if ( CHAT_IS_ACTIVE ) {
    if ( !horario() ) {
      var msg_horario = 'Estimado ' + NOMBRE + ', \n\n\tEl horario de atención de nuestro WebChat es:\n\n\t- Lunes a Sábado de 8:00 a 22:00 hrs.\n\tDomingo de 9:00 a 22:00 hrs.';
      alert(msg_horario);
    } else {
      var mylink = "http://www.outside.cl/chat_jumbo/chat.cfm?nombre=" + NOMBRE + "&apellido=" + APELLIDO + "&empresa=jumbo";
      window.open(mylink, "WebChat_Jumbo", "location=0,menubar=0,toolbar=0,status=0,scrollbars=no,directories=0,width=580,height=440,top=10,left=10,resizable=no,dependent=0,personalbar=0");
    }
  }
}

function horario() {
  //alert("DIA:" + DIA + ", HORA:" + HORA);
  switch ( DIA.toLowerCase() ) {
    case "lun": case "mon": //alert("Monday!");
    case "mar": case "tue": //alert("Tuesday!");
    case "mié": case "wed": //alert("Wednesday!");
    case "jue": case "thu": //alert("Thursday!");
    case "vie": case "fri": //alert("Friday!");
    case "sáb": case "sat": //alert("Saturday!");      
      if ( 800 <= HORA && HORA <= 2200 )
        return true;
      break;
    case "dom": case "sun": //alert("Sunday!");
      if ( 900 <= HORA && HORA <= 2200 )
        return true;
      break;      
  }
  return false;
}


