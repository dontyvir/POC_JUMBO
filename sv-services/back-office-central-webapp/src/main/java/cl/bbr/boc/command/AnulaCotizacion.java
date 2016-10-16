package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;

/**
 * AnulaOP Comando Process
 * comando que anula una Op
 * @author BBRI
 */

public class AnulaCotizacion extends Command {

 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
     // 1. seteo de Variables del método
	 String url;
     String paramUrl = "";
     long paramIdCot=0L;
     

     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");
     String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");     
	 logger.debug("mensaje_fracaso: " + mensaje_fracaso);
	 String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito");     
	 logger.debug("mensaje_exito: " + mensaje_exito);
	 String paramHeader = getServletConfig().getInitParameter("headerlog");
	 logger.debug("headerlog: " + paramHeader);
	 

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url_anucot") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("cot_id") == null ){throw new ParametroObligatorioException("cot_id es nulo");}
     
     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url_anucot");
     paramIdCot = Long.parseLong(req.getParameter("cot_id")); //long:obligatorio:si
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_cot: " + paramIdCot);

     // 3. Procesamiento Principal

     BizDelegate bizDelegate = new BizDelegate();
      
	 if (bizDelegate.setAnularCotizacion(paramIdCot)){
		 logger.debug("OP Anulada :"+ paramIdCot);
		 //escribe en el log la liberacion
		 String mjeLog = "Cotización ha sido anulada.";
		 LogsCotizacionesDTO log = new LogsCotizacionesDTO();
		 log.setUsuario(usr.getLogin());
		 log.setCot_id(paramIdCot);
		 log.setDescripcion(mjeLog);
		 		 
		 bizDelegate.addLogCotizacion(log);
		 paramUrl += "&msje=" +  mensaje_exito;	
	 }
	 else{
		 paramUrl += "&msje=" +  mensaje_fracaso;	
		 logger.debug("Cotizacion no Anulada :"+ paramIdCot);
	 	}
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
