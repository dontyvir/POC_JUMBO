package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoIndicDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar la indicación de un pedido
 * @author bbr
 *
 */
public class ModPedidoIndic extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramIdPedido=0L;
     String paramIndic="";
     String paramObs="";
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es null");}
     if ( req.getParameter("indicacion") == null ){throw new ParametroObligatorioException("indicacion es null");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramIdPedido = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si
     paramIndic = req.getParameter("indicacion"); //long:obligatorio:si
     paramObs = req.getParameter("obs"); //long:obligatorio:no
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_pedido: " + paramIdPedido);
     logger.debug("indicacion: " + paramIndic);
     
     ForwardParameters fp = new ForwardParameters();
     String mensaje = "";
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
  		ProcModPedidoIndicDTO prm = new ProcModPedidoIndicDTO();
  		
  		prm.setId_pedido(paramIdPedido);
        prm.setIndicacion(paramIndic);
        prm.setUsr_login(usr.getLogin());
        prm.setMensaje(mensaje);
        prm.setObservacion(paramObs);
        
  		boolean result = false;
  		try{
  			result = biz.setModPedidoIndic(prm);
  		} catch(BocException e){
  			String UrlError = getServletConfig().getInitParameter("UrlError");
  			logger.debug("Controlando excepción: " + e.getMessage());
  			if (  e.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE) ){
				logger.debug("El código del pedido no existe");
				fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE );
				paramUrl = UrlError + fp.forward();
			} else{
				logger.debug("Controlando excepción: " + e.getMessage());
			}
  		}
  		
  		logger.debug("result:"+result);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
