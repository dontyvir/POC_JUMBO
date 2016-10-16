package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.AsignaOPDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * comando que asigna Op's
 * @author BBRI
 */
public class AsignaOP extends Command {

	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     String paramUrlfracaso="";
	     String mensaje_fracaso="";
	     String mensaje_fracaso2="";
	     String headerlog="";
         String origen = "";
	     
	     long paramId_op=0L;
	     long paramId_usuario_ped= 0L;
	     
	     logger.debug("AsignaOP...");
	     paramUrlfracaso = getServletConfig().getInitParameter("url_fracaso");		
			logger.debug("Url fracaso: " + paramUrlfracaso);
			
		 mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso1");		
			logger.debug("mensaje_fracaso1: " + mensaje_fracaso);
			
		 mensaje_fracaso2 = getServletConfig().getInitParameter("mensaje_fracaso2");		
			logger.debug("mensaje_fracaso2: " + mensaje_fracaso2);			
			
		 headerlog = getServletConfig().getInitParameter("headerlog");		
         logger.debug("headerlog: " + headerlog);
	     // 2. Procesa parámetros del request

	     logger.debug("Procesando parámetros...");

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){	    	 
	    	 throw new ParametroObligatorioException("url es null");
	     }
	     if ( req.getParameter("id_pedido") == null ){
	    	 throw new ParametroObligatorioException("id_pedido es nulo");
	     }
	     if ( req.getParameter("id_usuario_ped") == null ){
	    	 throw new ParametroObligatorioException("id_usuario_ped es nulo");
	     }
         if ( req.getParameter("origen") != null ){
             origen = req.getParameter("origen");
         }
	      
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramId_op = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si
	     
	     paramId_usuario_ped = Long.parseLong(req.getParameter("id_usuario_ped")); //long:obligatorio:si
	     
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_pedido: " + paramId_op);
	     logger.debug("id_usuario_ped de pedido: "+paramId_usuario_ped);
	     logger.debug("Usuario :id_usuario actual:"+ usr.getId_usuario());
	     logger.debug("Usuario :login:"+ usr.getLogin());	     
	     logger.debug("Usuario :id_pedido:"+usr.getId_pedido());
	     
	     ForwardParameters fp = new ForwardParameters();

	     /*
	      * 3. Procesamiento Principal
	      */
	     AsignaOPDTO dto = new AsignaOPDTO();
	     dto.setId_pedido(paramId_op);
	     dto.setId_usuario(usr.getId_usuario());
	     dto.setId_pedido_usr_act(usr.getId_pedido());
	     dto.setUsr_login(usr.getLogin());
	     dto.setLog(headerlog);
	     
	     BizDelegate bizDelegate = new BizDelegate();
	     boolean result = false;
	     
	     try{
	    	 result = bizDelegate.setAsignaOP(dto);
	    	 logger.debug("result?"+result);
	    	 if(result){
	    		 usr.setId_pedido(paramId_op);
	    	 }
	    	 paramUrl= paramUrl+"&origen=" + origen + "&mod=1";
	    	 
	     }catch(BocException e){
	    	 logger.debug("Error:"+e.getMessage());
	    	 String UrlError = getServletConfig().getInitParameter("url_fracaso");
	  			logger.debug("Controlando excepción: " + e.getMessage());
	  			if (  e.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE) ){
					logger.debug("El código del pedido no existe");
					fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward();
				} if (  e.getMessage().equals(Constantes._EX_OPE_USR_TIENE_OTRO_PED) ){
					logger.debug("El usuario debe liberar el pedido antes de asignar otro pedido");
					fp.add( "rc" , Constantes._EX_OPE_USR_TIENE_OTRO_PED );
					fp.add( "mensaje_error" , mensaje_fracaso2+usr.getId_pedido() );
					paramUrl = UrlError + fp.forward();
				} if (  e.getMessage().equals(Constantes._EX_OPE_TIENE_OTRO_USR) ){
					logger.debug("El pedido tiene otro usuario asignado");
					fp.add( "rc" , Constantes._EX_OPE_TIENE_OTRO_USR );
					fp.add( "mensaje_error" , mensaje_fracaso+usr.getLogin() );
					paramUrl = UrlError + fp.forward();
				}
			logger.debug("paramUrl:"+paramUrl);
	     }
	     
	     logger.debug("result:"+result);
	     
	     res.sendRedirect(paramUrl);
	 }
}
	 