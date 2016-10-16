package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que registra un log en un pedido
 * @author bbr
 *
 */
public class RegLogOP extends Command {


 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     
     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramId_pedido = -1L;
     String paramTexto="";
     long paramId_motivo_nuevo=-1L;
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es nulo");}
     if ( req.getParameter("obs") == null ){throw new ParametroObligatorioException("obs es nulo");}
     if ( req.getParameter("id_motivo_nuevo") == null ){throw new ParametroObligatorioException("id_motivo_nuevo es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramId_pedido = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si
     paramTexto = req.getParameter("obs"); //string:obligatorio:si
     paramId_motivo_nuevo = Long.parseLong(req.getParameter("id_motivo_nuevo")); //long:obligatorio:si

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_pedido: " + paramId_pedido);
     logger.debug("obs: " + paramTexto);
     logger.debug("id_motivo_nuevo: " + paramId_motivo_nuevo);
     
     /*
      * 3. Procesamiento Principal
      */
     BizDelegate bizDelegate = new BizDelegate();
     
     LogPedidoDTO log = new LogPedidoDTO();
     log.setId_pedido(paramId_pedido);
     log.setLog(paramTexto);
     log.setId_motivo(paramId_motivo_nuevo);
     log.setUsuario(usr.getLogin());

     bizDelegate.addLogPedido(log);

     
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
