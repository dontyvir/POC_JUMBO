package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Cambia el estado a un despacho y agrega al log de despacho
 * @author jsepulveda
 */
public class CambiaEstadoDespacho extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		String 	paramId_pedido 	= "";
		String	paramId_estado 	= "";
		String	paramLog 		= "";
		long   	id_pedido 		= -1;
		long   	id_estado 		= -1;
		
		
		// 1. Variables del método
		String paramUrl	= "";
		
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
		
		if ( req.getParameter("id_pedido") == null ){
			throw new ParametroObligatorioException("id_pedido es null");
		}
		paramId_pedido = req.getParameter("id_pedido");
		id_pedido = Long.parseLong(paramId_pedido);
		logger.debug("id_pedido: " + paramId_pedido);
		
		if ( req.getParameter("id_estado") == null ){
			throw new ParametroObligatorioException("id_estado es null");
		}
		paramId_estado = req.getParameter("id_estado");
		id_estado	= Long.parseLong(paramId_estado);
		logger.debug("id_estado: " + paramId_estado);
	
		paramLog = req.getParameter("log");
		logger.debug("log: " + paramLog);
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		try{						
			biz.setCambiaEstadoDespacho(id_pedido,id_estado,usr.getLogin(),paramLog);			
			paramUrl += "&mensaje=datos actualizados correctamente";	
			//paramUrl = paramUrl;
		}
		catch(Exception ex){			
			paramUrl += "&mensaje=los datos no pudieron ser actualizados";	
			if(ex.getMessage().equals(Constantes._EX_PED_ID_INVALIDO)){
				logger.debug("Id del pedido es inválido");
				fp.add( "rc" , Constantes._EX_PED_ID_INVALIDO );
				paramUrl = UrlError + fp.forward();
			}
			if(ex.getMessage().equals(Constantes._EX_ESTADO_INVALIDO)){
				logger.debug("Id del estado es inválido");
				fp.add( "rc" , Constantes._EX_ESTADO_INVALIDO );
				paramUrl = UrlError + fp.forward();
			}
			if(ex.getMessage().equals(Constantes._EX_COT_ID_NO_EXISTE)){
				logger.debug("No puede encontrar datos de la cotizacion a la que el pedido pertenece");
				fp.add( "rc" , Constantes._EX_COT_ID_NO_EXISTE );
				paramUrl = UrlError + fp.forward();
			}
		}
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}		
	
}
