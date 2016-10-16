package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que permite hacer una relacion automatica entre los detalles de pedidos 
 * que coinciden con los picking realizados.
 * @author BBR
 *
 */

public class RelacionAutomaticaFormPicking extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		 // 1. seteo de Variables del método
	     String paramUrl 		  ="";
	     long paramIdRonda        =-1;
	    	     
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_ronda") == null ){throw new ParametroObligatorioException("id_ronda es null");}
	    
	     
	        // 2.2 obtiene parametros desde el request
	     paramUrl 	     = req.getParameter("url");
	     paramIdRonda    = Long.parseLong(req.getParameter("id_ronda"));
	     
	     	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("paramIdRonda: " + paramIdRonda);
	     
	     
	     ForwardParameters fp = new ForwardParameters();
	     fp.add( req.getParameterMap() );
	     logger.debug(fp.forward());
	     /*
	      * 3. Procesamiento Principal
	      */
	   	BizDelegate biz = new BizDelegate();
	     	
	 	try{
	 		
	 		biz.doRelacionAutomaticaFormPick(paramIdRonda);
	 	}catch(BolException e){
	 		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if (e.getMessage().matches("(?i).*"+Constantes._EX_RON_ID_INVALIDO+".*")){
				fp.add( "rc" , Constantes._EX_RON_ID_INVALIDO );
				paramUrl = UrlError + fp.forward(); 
			}
			else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
	 	}
	 

	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);

	 }
	
	
}
