package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.AsignaCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;

/**
 * LiberaOP Comando Process
 * comando que libera una op
 * @author BBRI
 */

public class LiberaCotizacion extends Command {

 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
     // 1. seteo de Variables del método
	 String url = "";
     String paramUrl = "";
     long paramId_cot=0L;
     int paramOrigen=0;
     

     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");
     String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");     
	 logger.debug("mensaje_fracaso: " + mensaje_fracaso);
	 String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito");     
	 logger.debug("mensaje_exito: " + mensaje_exito);
	 String paramHeader = getServletConfig().getInitParameter("headerlog");
	 logger.debug("headerlog: " + paramHeader);
	 

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("origen") == null ){throw new ParametroObligatorioException("origen es null");}
          
     paramId_cot = usr.getId_cotizacion(); // Se saca el id de cotización de la sesion del usuario
     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramOrigen =Integer.parseInt(req.getParameter("origen"));

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_cot: " + paramId_cot);

     /*
      * 3. Procesamiento Principal
      * Obtiene el id_usuario actualmente conectado.
      * Revisa si la cotizacion esta asignada a un usuario
      * - si no esta asignada no hace nada
      * - si esta asignada nulifica el id_usuario asignado si es equivalente al usuario conectado
      * 	- respalda en el log de la Cotización la liberación.
      */
     BizDelegate bizDelegate = new BizDelegate();
     AsignaCotizacionDTO coll = new AsignaCotizacionDTO();
     
     coll.setId_cotizacion(paramId_cot);
     coll.setId_usuario(usr.getId_usuario());
     
     
	 if (bizDelegate.setLiberaCotizacion(coll)){
		 usr.setId_cotizacion(0);		 
		 logger.debug("Cotización Liberada : "+ paramId_cot);
		 //escribe en el log la liberacion
		 LogsCotizacionesDTO log = new LogsCotizacionesDTO();
		 log.setUsuario(usr.getLogin());
		 log.setCot_id(paramId_cot);
		 log.setDescripcion(paramHeader);
		 bizDelegate.addLogCotizacion(log);		 
		 paramUrl += "?msje="+mensaje_exito;
	 	}
	 else{
		 logger.debug("Cotización no Liberada :"+ paramId_cot);
		/* if (paramOrigen == 1)
			 url = paramUrl+"&id_motivo="+paramId_motivo+"&obs="+paramObs+"&mensaje="+mensaje_fracaso+"&ret=0";
		 if (paramOrigen == 2)
			 url = paramUrl+"?id_motivo="+paramId_motivo+"&obs="+paramObs+"&mensaje="+mensaje_fracaso+"&ret=0";
			 */
	 	}
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
