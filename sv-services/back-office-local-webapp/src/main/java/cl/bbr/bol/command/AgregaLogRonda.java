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
 *  Comando que agrega un registro al log de la ronda
 *  @author mleiva
 */
public class AgregaLogRonda extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl	= "";
		String paramlog	= "";
		String paramId_ronda = "";
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		//Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);
		
		if ( req.getParameter("url") == null ){
			throw new ParametroObligatorioException("url es null");
		}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		if ( req.getParameter("id_ronda") == null){
			throw new ParametroObligatorioException("id_ronda es null");			
		}
		paramId_ronda = req.getParameter("id_ronda");
		logger.debug("url: " + paramUrl);
		long id_ronda = Long.parseLong(paramId_ronda);
		
		if ( req.getParameter("log") == null ){
			throw new ParametroObligatorioException("log es null");
		}
		paramlog = req.getParameter("log");
		logger.debug("log: " + paramlog);
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		try{
			biz.addLogRonda(id_ronda,usr.getLogin(),paramlog);
			//paramUrl = paramUrl;
		}catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_RON_ID_INVALIDO)){
				logger.debug("Id de la ronda es inválido");
				fp.add( "rc" , Constantes._EX_RON_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}
		}	
		
		
		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
}
