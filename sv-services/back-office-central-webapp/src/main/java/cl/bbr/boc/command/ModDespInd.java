package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * ModDespInd Comando Process
 * @author bbr
 *
 */
public class ModDespInd extends Command {

 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
     
     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramId_pedido=0L;
     String paramIndicacion="";
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es nulo");}
     if ( req.getParameter("indicacion") == null ){throw new ParametroObligatorioException("indicacion es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramId_pedido = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si
     paramIndicacion = req.getParameter("indicacion"); //string:obligatorio:si

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_pedido: " + paramId_pedido);
     logger.debug("indicacion: " + paramIndicacion);
     /*
      * 3. Procesamiento Principal
      */

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
