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
 * Comando que permite validar una op determinada
 * @author bbr
 */
public class ValidarOP extends Command {

	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     long paramId_op=0L;
	     String paramAccion = "";
	     
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){ throw new ParametroObligatorioException("url es null"); }
	     if ( req.getParameter("id_pedido") == null ){ throw new ParametroObligatorioException("id_pedido es nulo"); }
	     if ( req.getParameter("accion") == null ){ throw new ParametroObligatorioException("accion es nulo"); }
	      
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramId_op = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si
	     paramAccion = req.getParameter("accion");
	     
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_pedido: " + paramId_op);
	     logger.debug("id_usuario actual:"+ usr.getId_usuario());
	     logger.debug("accion: " + paramAccion);
	     
	     ForwardParameters fp = new ForwardParameters();

	     /*
	      * 3. Procesamiento Principal
	      * 
	      * Validación de la OP
	      * Paso 1: Eliminar las alertas del pedido a validar
	      * Paso 2: Revisar cada alerta
	      * Paso 3: Cambio de estado: si existen alertas -> En Validacion
	      * 						  no existen alertas -> Validada
	      */
	     BizDelegate biz = new BizDelegate();
	     try{
		     if(paramAccion.equals("Validar"))
		    	 biz.setValidaOP(paramId_op);
		     else{
		    	 biz.setConfirmarOP(paramId_op, usr);
		    	 //biz.agregaBolsaRegalo(paramId_op);
		     }
	     }catch(BocException e){
	    	 logger.debug("Error:"+e.getMessage());
	    	 String UrlError = getServletConfig().getInitParameter("UrlError");
	  			logger.debug("Controlando excepción: " + e.getMessage());
				fp.add( "id_pedido" , paramId_op+"" );
				fp.add( "mod" , "1" );
	  			if (  e.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE) ){
					logger.debug("El código del pedido no existe");
					fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward();
				} if (  e.getMessage().equals(Constantes._EX_OPE_TIENE_ALERTA_ACT) ){
					logger.debug("El pedido tiene alertas activas");
					fp.add( "rc" , Constantes._EX_OPE_TIENE_ALERTA_ACT );
					paramUrl = UrlError + fp.forward();
				} 
			logger.debug("paramUrl:"+paramUrl);
	     }
	     
	     res.sendRedirect(paramUrl);
	 }
}
	 