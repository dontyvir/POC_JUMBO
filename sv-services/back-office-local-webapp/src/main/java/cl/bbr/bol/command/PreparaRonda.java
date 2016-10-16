package cl.bbr.bol.command;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Cambia el estado a una ronda de Creada a En Picking
 * @author jsepulveda
 */
public class PreparaRonda extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	
		// 1. Variables del m�todo
		
		// 1.1 par�metro Url
		String 	paramUrl		= "";
		String 	paramId_ronda 	= "";
		long	id_ronda 	= -1;

		// 2. Procesa par�metros del request
		logger.debug("Procesando par�metros...");

		//Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);
		
		// 2.1 Par�metro Url
		if ( req.getParameter("url") == null ){
			throw new ParametroObligatorioException("url es null");
		}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		// 2.2 Par�metro id_ronda
		if ( req.getParameter("id_ronda") == null ){
			throw new ParametroObligatorioException("id_ronda es null");
		}
		paramId_ronda = req.getParameter("id_ronda");
		id_ronda = Long.parseLong(paramId_ronda);
		
	
		// 3. Procesamiento Principal
		BizDelegate biz	= new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		try{		
			biz.PreparaRonda(id_ronda);
		}catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_RON_ID_INVALIDO)){
				logger.debug("Id de la ronda es inv�lido");
				fp.add( "rc" , Constantes._EX_RON_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}
			if(ex.getMessage().equals(Constantes._EX_RON_ID_NULA)){
				logger.debug("Id de la ronda viene Nula");
				fp.add( "rc" , Constantes._EX_RON_ID_NULA);
				paramUrl = UrlError + fp.forward();
			}
		}
		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
