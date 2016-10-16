package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoPolSustDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar la indicación de un pedido
 * @author bbr
 *
 */
public class ModPolSustitOP extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramIdPedido=0L;
     long paramPolSustId=0L;

     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es null");}
     if ( req.getParameter("sel_psu") == null ){throw new ParametroObligatorioException("sel_psu es null");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramIdPedido = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si
     paramPolSustId = Long.parseLong(req.getParameter("sel_psu")); //long:obligatorio:si

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_pedido: " + paramIdPedido);
     logger.debug("pol_sust_id: " + paramPolSustId);
     
     ForwardParameters fp = new ForwardParameters();
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
  		ProcModPedidoPolSustDTO prm = new ProcModPedidoPolSustDTO();
  		
  		prm.setId_pedido(paramIdPedido);
        prm.setId_pol_sust(paramPolSustId);
        prm.setLogin(usr.getLogin());
        
  		boolean result = false;
  		try{
  			result = biz.setModPedidoPolSust(prm);
  			if(result){
  				String mens_exito = getServletConfig().getInitParameter("mens_exito");
  				paramUrl += "&mensaje="+mens_exito;
  			}
  		} catch(BocException e){
  			String UrlError = getServletConfig().getInitParameter("UrlError");
  			String mens_fracaso = getServletConfig().getInitParameter("mens_fracaso");
  			logger.debug("Controlando excepción: " + e.getMessage());
  			if (  e.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE) ){
				logger.debug("El código del pedido no existe");
				fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE );
				fp.add("mensaje", mens_fracaso);
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
