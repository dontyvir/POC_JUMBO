var PASO_ACTUAL = -1;

function ir_mi_cuenta() {
  var shownLightBox = false;
  if ( PASO_ACTUAL == 0 ) {
    guardarDireccion();
  }
  if ( document.getElementById('cli_id') != null ) {
    if ( document.getElementById('cli_id').value == 1 && PASO_ACTUAL == 2 ) {
      document.getElementById('url_to').value = "BienvenidaForm";
      showLoginSession();
      shownLightBox = true;
    } 
  }
  if ( !shownLightBox ) {
    window.location.href = "BienvenidaForm";
  }
}

function ir_mis_listas() {
  var shownLightBox = false;
  if ( PASO_ACTUAL == 0 ) {
    guardarDireccion();
  }  
  //si estoy en la bienvenida debo guardar la direccion con ajax y en el UltComprasForm hay q sacar la logica
  if ( document.getElementById('cli_id') != null ) {
    if ( document.getElementById('cli_id').value == 1 && PASO_ACTUAL == 2 ) {
      document.getElementById('url_to').value = "UltComprasForm";
      showLoginSession();
      shownLightBox = true;
    } 
  }
  if ( !shownLightBox ) {
    window.location.href = "UltComprasForm";
  }
}

function ir_mis_listas2() {
//	showLoginSession();
//	shownLightBox = true;
  var shownLightBox = false;
  if ( PASO_ACTUAL == 0 ) {
    guardarDireccion();
  }  
  //si estoy en la bienvenida debo guardar la direccion con ajax y en el UltComprasForm hay q sacar la logica
  if ( document.getElementById('cli_id') != null ) {
    if ( document.getElementById('cli_id').value == 1 && PASO_ACTUAL == 2 ) {
      document.getElementById('url_to').value = "CategoryDisplay";//?cab=4016
      showLoginSession();
      shownLightBox = true;
    } 
  }
  if ( !shownLightBox ) {
    window.location.href = "UltComprasForm";
  }
}

function ir_supermercado() {
  if ( PASO_ACTUAL == 0 ) {
    guardarDireccion();
  }
  window.location.href = "CategoryDisplay";
}

function ir_pagar() {
  if ( PASO_ACTUAL == 0 ) {
    guardarDireccion();
  }
  var shownLightBox = false;
  //si estoy en la bienvenida debo guardar la direccion con ajax y en el UltComprasForm hay q sacar la logica
  if ( document.getElementById('cli_id') != null ) {
    if ( document.getElementById('cli_id').value == 1 && PASO_ACTUAL == 2 ) {
      document.getElementById('url_to').value = "CheckoutForm";
      showLoginSession();
      shownLightBox = true;
    } 
  }
  if ( !shownLightBox ) {
    window.location.href = "CheckoutForm";
  }
}
