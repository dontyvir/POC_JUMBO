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
 * Comando que agrega un registro al log de la jornada
 *  @author mleiva
 */
public class AgregaLogJornada extends Command{

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl	= "";
		String paramLog = "";
		String paramId_jornada = "";
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		//Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);
		
		// 2.1 Parámetro url (Obligatorio)
		if ( req.getParameter("url") == null ){
			throw new ParametroObligatorioException("url es null");
		}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		// 2.2 Parámetro id_jornada (Obligatorio)
		if ( req.getParameter("id_jornada") == null){
			throw new ParametroObligatorioException("id_jornada es null");			
		}
		paramId_jornada = req.getParameter("id_jornada");
		logger.debug("url: " + paramUrl);
		
		// si falla el parseLong debiese levantar una excepción
		long id_jornada = Long.parseLong(paramId_jornada);
		
		// 2.3 Parámetro log (Opcional)
		paramLog = req.getParameter("log");
		logger.debug("log: " + paramLog);
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		try{
			biz.addLogJornadaPick(id_jornada,usr.getLogin(),paramLog);
			//paramUrl = paramUrl;
		}catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_JPICK_ID_INVALIDO)){
				logger.debug("Id de la jornada es inválido");
				fp.add( "rc" , Constantes._EX_JPICK_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}
		}	
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}		
	
}
