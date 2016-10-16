package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * BloqClient Comando Process
 * comando que permite bloquear clientes
 * @author BBRI
 */

public class BloqClient extends Command {

 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     
     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramId_cliente=0L;
     String paramAction="";
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_cliente") == null ){throw new ParametroObligatorioException("id_cliente es nulo");}
     if ( req.getParameter("action") == null ){throw new ParametroObligatorioException("action es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramId_cliente = Long.parseLong(req.getParameter("id_cliente")); //long:obligatorio:si
     paramAction = req.getParameter("action"); //string:obligatorio:si

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_cliente: " + paramId_cliente);
     logger.debug("action: " + paramAction);
     /*
      * 3. Procesamiento Principal
      */

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
