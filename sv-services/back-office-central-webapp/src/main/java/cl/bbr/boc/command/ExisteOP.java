package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * verifica si existe una Op 
 * @author BBRI
 */
public class ExisteOP extends Command {
 
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     long paramId_op=0L;

	     // 2. Procesa parámetros del request

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es nulo");}
	     
	     // 2.2 obtiene parametros desde el request
	     
	     paramId_op = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si
	     paramUrl = req.getParameter("url")+paramId_op;

	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_pedido: " + paramId_op);

	     ForwardParameters fp = new ForwardParameters();
		 fp.add( req.getParameterMap() );
			
	     /*
	      * 3. Procesamiento Principal
	      * Obtiene el id_usuario actualmente conectado.
	      * Revisa si la op existe
	      */
	     BizDelegate bizDelegate = new BizDelegate();

	     try{
	    	 boolean existe = bizDelegate.isPedidoById(paramId_op); 
	    	 logger.debug("existe:"+existe);
	     }catch(BocException e){
	    	 logger.debug("Controlando excepción: " + e.getMessage());
	    	 String UrlError = getServletConfig().getInitParameter("UrlError");
	    	 if (  e.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE) ){
					logger.debug("El código del pedido no existe");
					fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward();
	    	 } else {
					logger.debug("Controlando excepción: " + e.getMessage());
			}
	    		 
	     }
	     
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);

	 }//execute
}
