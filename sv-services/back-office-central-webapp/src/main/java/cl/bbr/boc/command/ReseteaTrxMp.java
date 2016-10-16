package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * LiberaOP Comando Process
 * comando que libera una op
 * @author BBRI
 */

public class ReseteaTrxMp extends Command {

 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
     // 1. seteo de Variables del método
	 String url = "";
     String paramUrl = "";
     long paramId_op=0L;
     long paramId_trx=0L;
     String paramObs ="";
     int origen =1;

     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");
     String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");     
	 logger.debug("mensaje_fracaso: " + mensaje_fracaso);
	 String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito");     
	 logger.debug("mensaje_exito: " + mensaje_exito);
	 

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es nulo");}
     if ( req.getParameter("id_trx_mp") == null ){throw new ParametroObligatorioException("id_trx_mp es nulo");}
     if ( req.getParameter("origen") == null ){throw new ParametroObligatorioException("origen es nulo");}     
     paramId_op = usr.getId_pedido(); // Se saca el id de OP de la sesion del usuario
     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramId_op  = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si
     paramId_trx = Long.parseLong(req.getParameter("id_trx_mp")); //long:obligatorio:si
     //paramObs = req.getParameter("obs");
     origen = Integer.parseInt(req.getParameter("origen"));

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_pedido: " + paramId_op);
     logger.debug("id_trx_mp: " + paramId_trx);

     /*
      * 3. Procesamiento Principal
      */
     BizDelegate bizDelegate = new BizDelegate();
     

     //setea TrxMp a estado "CREADA"
     if(bizDelegate.setModEstadoTrxMp(paramId_trx, Constantes.ID_ESTAD_TRXMP_CREADA)){
         //setea OP a estado "EN PAGO"
         if(bizDelegate.setModEstadoPedido(paramId_op, Constantes.ID_ESTAD_PEDIDO_EN_PAGO)){
        	 LogPedidoDTO log = new LogPedidoDTO();
        	 log.setUsuario(usr.getLogin());
        	 log.setId_pedido(paramId_op);
        	 log.setLog("La TrxMp = <font color='FF0000'>" +paramId_trx + "</font> fue cambiada a estado 'CREADA'");		 
        	 bizDelegate.addLogPedido(log);
             url = paramUrl+"&mensaje="+mensaje_exito+"&ret=1";
         }else{
             logger.debug("No fue posible modificar el estado de la OP:"+ paramId_op);
             url = paramUrl+"&mensaje="+mensaje_fracaso+"&ret=1";
         }
     }else{
         logger.debug("No fue posible modificar el estado de la TrxMp:"+ paramId_trx);
         url = paramUrl+"&mensaje="+mensaje_fracaso+"&ret=1";
     }
     
     // 4. Redirecciona salida
     res.sendRedirect(url);

 }//execute

}
