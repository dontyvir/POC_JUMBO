package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.CambEnPagoOPDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class CambiaEnPagoOP extends Command {

	 
	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     long paramId_op=0L;

	     //Recupera pagina desde web.xml
	     String UrlError = getServletConfig().getInitParameter("UrlError");
	     logger.debug("UrlError: " + UrlError);
			
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");
	     String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");     
		 logger.debug("mensaje_fracaso: " + mensaje_fracaso);
		 String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito");     
		 logger.debug("mensaje_exito: " + mensaje_exito);
		 

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es nulo");}

	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramId_op = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si

	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_pedido: " + paramId_op);

	     /*
	      * 3. Procesamiento Principal
	      */
	     BizDelegate bizDelegate = new BizDelegate(); 
	     CambEnPagoOPDTO coll = new CambEnPagoOPDTO();
	     
	     coll.setId_pedido(paramId_op);
	     coll.setId_usuario(usr.getId_usuario());
	     coll.setUsr_login(usr.getLogin());
	     
	     ForwardParameters fp = new ForwardParameters();
		 fp.add( req.getParameterMap() );
	     
	     try{
	    	 boolean exito = bizDelegate.setCambiarEnPagoOP(coll);
	    	 logger.debug("exito?"+exito);
	    	 if(exito){
	    		 //fp.add("mensaje",mensaje_exito);
	    		 paramUrl += "&mensaje="+mensaje_exito;
	    	 }else{
	    		 //fp.add("mensaje",mensaje_fracaso);
	    		 paramUrl += "&mensaje="+mensaje_fracaso;
	    	 }
	    	 
	     }catch(BocException ex){
				if(ex.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE)){
					logger.debug("El id del pedido es Inválido");
					fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE);
					fp.add( "mensaje_rc" , "El id del pedido es Inválido");
					paramUrl = UrlError + fp.forward();
				}else if(ex.getMessage().equals(Constantes._EX_OPE_ESTADO_INADECUADO)){
					logger.debug("El pedido tiene estado inadecuado");
					fp.add( "rc" , Constantes._EX_OPE_ESTADO_INADECUADO);
					fp.add( "mensaje_rc" , "El pedido tiene estado inadecuado");
					paramUrl = UrlError + fp.forward();
				}
			}
	     
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);

	 }//execute
}
