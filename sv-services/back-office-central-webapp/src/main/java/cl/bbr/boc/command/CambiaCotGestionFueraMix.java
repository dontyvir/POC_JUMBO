package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;
import cl.bbr.jumbocl.common.codes.Constantes;

/**
 * Cambia estado de contizacion a Gestion Fuera de Mix
 * @author BBRI
 */

public class CambiaCotGestionFueraMix extends Command {

 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
     // 1. seteo de Variables del método
	 
     String paramUrl = "";
     long paramIdCot=0L;
     String mod = "";
     

     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");
     String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");     
	 logger.debug("mensaje_fracaso: " + mensaje_fracaso);
	 String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito");     
	 logger.debug("mensaje_exito: " + mensaje_exito);
	 paramUrl = getServletConfig().getInitParameter("url");
	 
     // 2.1 revision de parametros obligatorios
     
     if ( req.getParameter("cot_id") == null ){throw new ParametroObligatorioException("cot_id es nulo");}
     
    
     if ( req.getParameter("mod") == null ){throw new ParametroObligatorioException("mod es nulo");}
     mod = req.getParameter("mod");
     // 2.2 obtiene parametros desde el request
     paramIdCot = Long.parseLong(req.getParameter("cot_id")); //long:obligatorio:si
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_cot: " + paramIdCot);

     // 3. Procesamiento Principal

     BizDelegate bizDelegate = new BizDelegate();
      
	 if (bizDelegate.setModEstadoCotizacion(paramIdCot, Constantes.ID_EST_COTIZACION_GEST_FUERA_MIX)){
		 //escribe en el log la liberacion
		 String mjeLog = "Cotización ha cambiada a Gestión Fuera de Mix.";
		 LogsCotizacionesDTO log = new LogsCotizacionesDTO();
		 log.setUsuario(usr.getLogin());
		 log.setCot_id(paramIdCot);
		 log.setDescripcion(mjeLog);
		 		 
		 bizDelegate.addLogCotizacion(log);
		 paramUrl += paramIdCot+"&mod="+mod+"&msje=" +  mensaje_exito;	
	 }
	 else{
		 paramUrl += paramIdCot+"&mod="+mod+"&msje=" +  mensaje_fracaso;	
		 logger.debug("No fue posible cambiar el estado cotización :"+ paramIdCot);
	 	}
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
