package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.RechPagoOPDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * LiberaOP Comando Process
 * comando que libera una op
 * @author BBRI
 */

public class RechazoPagoOP extends Command {

 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
     // 1. seteo de Variables del método
	 String url = "";
     String paramUrl = "";
     long paramId_op=0L;
     String paramObs ="";

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
     if ( req.getParameter("obs") == null ){throw new ParametroObligatorioException("obs es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramId_op = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si
     paramObs = req.getParameter("obs");

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_pedido: " + paramId_op);
     logger.debug("obs: " + paramObs);

     /*
      * 3. Procesamiento Principal
      */
     BizDelegate bizDelegate = new BizDelegate();
     RechPagoOPDTO coll = new RechPagoOPDTO();
     
     coll.setId_pedido(paramId_op);
     coll.setId_usuario(usr.getId_usuario());
     coll.setUsr_login(usr.getLogin());
     coll.setObservacion(paramObs);
     
     ForwardParameters fp = new ForwardParameters();
	 fp.add( req.getParameterMap() );
     
     try{
    	 boolean exito = bizDelegate.setRechazaPagoOP(coll);
    	 logger.debug("exito?"+exito);
    	 if(exito){
    		 //fp.add("mensaje",mensaje_exito);
    		 paramUrl += "&mensaje="+mensaje_exito;
    	 }else{
    		 //fp.add("mensaje",mensaje_fracaso);
    		 paramUrl += "&mensaje="+mensaje_fracaso;
    	 }
    	 
     }catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE)){
				logger.debug("El id del pedido es Inválido");
				fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE);
				paramUrl = UrlError + fp.forward();
			}
		}
     
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
