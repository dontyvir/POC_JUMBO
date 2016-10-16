package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando para seleccionar la direcciçon de despacho
 * @author bbr
 *
 */
public class SelDespAddr extends Command {

 
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     
     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramId_op=0L;
     long paramDir_id=0L;
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_op") == null ){throw new ParametroObligatorioException("id_op es nulo");}
     if ( req.getParameter("dir_id") == null ){throw new ParametroObligatorioException("dir_id es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramId_op = Long.parseLong(req.getParameter("id_op")); //long:obligatorio:si
     paramDir_id = Long.parseLong(req.getParameter("dir_id")); //long:obligatorio:si

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_op: " + paramId_op);
     logger.debug("dir_id: " + paramDir_id);
     /*
      * 3. Procesamiento Principal
      */

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
