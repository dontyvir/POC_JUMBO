package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.collaboration.ProcIniciaFormPickingManualDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite inicializar formulario de picking.
 * @author BBR
 *
 */
public class IniciaFormPickingLight extends Command{

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl	= "";
		String paramIdRonda ="";
		long id_ronda = -1;
		
		
		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);	
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros IniciaJornada...");
		
		//URL		
		if ( req.getParameter("url") == null ){	throw new ParametroObligatorioException("url es null");}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		if ( req.getParameter("id_ronda") == null ){throw new ParametroObligatorioException("id_jornada es null");}
		paramIdRonda	= req.getParameter("id_ronda");
		id_ronda = Long.parseLong(paramIdRonda);
		logger.debug("id_ronda: " + paramIdRonda);
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		// Inicia la jornada
		try{
			ProcIniciaFormPickingManualDTO datos = new ProcIniciaFormPickingManualDTO();
			datos.setId_ronda(id_ronda);
			datos.setId_usuario(usr.getId_usuario());
			datos.setLogin(usr.getLogin());
			biz.doIniciaFormPickingManual(datos);
//			inicializa los pedidos relacionados al id_jpicking
			//biz.setPedidosByIdJornada(id_jpicking);

		}catch(BolException ex){
		//	ex.printStackTrace();
			logger.debug("ex.getMessage(): "+ex.getMessage());
			
			if(ex.getMessage().matches("(?i).*"+Constantes._EX_RON_ID_INVALIDO+".*")){
				logger.debug("El id de la ronda es Inválido");
				fp.add( "rc" , Constantes._EX_RON_ID_INVALIDO);
				logger.debug("UrlError1: " + UrlError);
				paramUrl = UrlError + fp.forward();
			}else		
			if(ex.getMessage().matches("(?i).*"+Constantes._EX_RON_ESTADO_INADECUADO+".*")){
				logger.debug("Ronda en estado inadecuado");
				fp.add( "rc" , Constantes._EX_RON_ESTADO_INADECUADO);
				logger.debug("UrlError2: " + UrlError);
				paramUrl = UrlError + fp.forward();
			}		
			
		}		
		// 4. Redirecciona salida
		logger.debug("Redirecciona a: " + paramUrl);
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
