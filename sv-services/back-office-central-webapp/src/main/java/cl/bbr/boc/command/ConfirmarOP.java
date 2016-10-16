package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que realiza la validación de una OP. Valida la OP que está en la sesión del usuario. * 
 * @author jsepulveda
 */
public class ConfirmarOP extends Command {

	
	 
	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    
		 // 1. seteo de Variables del método
	     String 	paramUrl 	= "";
	     long		id_op		= -1;
	     
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){ throw new ParametroObligatorioException("url es null"); }
	      
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     
	     //obteniendo mensaje exito
		 String mensaje_confirm = getServletConfig().getInitParameter("MensajeExito");
		 logger.debug("mensaje_confirm: " + mensaje_confirm);

	     if ( usr.getId_pedido() > 0 )
	    	 id_op = usr.getId_pedido();
	     else
	     {
	    	 logger.error("Se ha invocado al comando ConfirmaOP sin tener una OP en edición en la sesión del usuario");
	    	 throw new SystemException("Se ha invocado al comando ConfirmaOP sin tener una OP en edición en la sesión del usuario");
	     }
	     
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_usuario actual:"+ usr.getId_usuario());
	     logger.debug("id_pedido: " + id_op);
	     
	     // 
	     ForwardParameters fp = new ForwardParameters();

	     
	     // 3. Procesamiento Principal
	     BizDelegate biz = new BizDelegate();
	     try{
		     boolean esValidada = biz.setConfirmarOP(id_op, usr);
		     if(esValidada){
		    	 //colocar el mensaje de exito
		    	 paramUrl += "&mensaje_confirm=" + mensaje_confirm;
		     }
		     
	     }catch(BocException e){
             String msg = e.getMessage();
             logger.debug("Error:"+e.getMessage());
             String UrlError = getServletConfig().getInitParameter("UrlError");
             logger.debug("Controlando excepción: " + e.getMessage());
             fp.add( "id_pedido" , id_op+"" );
             fp.add( "mod" , "1" );
             msg = msg.substring(msg.length()-6,msg.length());
             
             if (  msg.equals(Constantes._EX_OPE_ID_NO_EXISTE) ) {
                 logger.debug("El código del pedido no existe");
                 fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE );
                 paramUrl = UrlError + fp.forward();
             } else if (  msg.equals(Constantes._EX_OPE_TIENE_ALERTA_ACT) ) {
                 logger.debug("El pedido tiene alertas activas");
                 fp.add( "rc" , Constantes._EX_OPE_TIENE_ALERTA_ACT );
                 paramUrl = UrlError + fp.forward();
             } else {
                 logger.error("Excepción no controlada: " + e.getMessage());
                 throw new SystemException("Excepción No Controlada");
             }
             logger.debug("paramUrl:"+paramUrl);
	     }
	     
	     res.sendRedirect(paramUrl);
	 }
	
	
}
