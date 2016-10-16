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
 *  Comando que agrega un registro al log del despacho
 *  @author mleiva
 */
public class AgregaLogDespacho extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl	= "";
		String paramLog = "";
		String paramId_pedido = "";
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
//		 2.2 Parámetro id_pedido (Obligatorio)
		if ( req.getParameter("id_pedido") == null){
			throw new ParametroObligatorioException("id_pedido es null");			
		}
		paramId_pedido = req.getParameter("id_pedido");
		logger.debug("url: " + paramUrl);
		
		// si falla el parseLong debiese levantar una excepción
		long id_pedido = Long.parseLong(paramId_pedido);
		
		// 2.3 Parámetro log (Opcional)
		paramLog = req.getParameter("log");
		logger.debug("log: " + paramLog);
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		try{
			biz.addLogDespacho(id_pedido,usr.getLogin(),paramLog); 
		}catch (BolException ex){
			if(ex.getMessage().equals(Constantes._EX_PED_ID_INVALIDO)){
				logger.debug("Id del pedido es inválido");
				fp.add( "rc" , Constantes._EX_PED_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}
		}
		
		
		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}		
	
}
