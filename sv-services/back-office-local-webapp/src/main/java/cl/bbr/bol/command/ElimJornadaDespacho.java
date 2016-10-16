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
 * Comando que permite eliminar un horario para un local con todas sus 
 * jornadas de despacho asociadas
 * @author mleiva
 */
public class ElimJornadaDespacho extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl			= "";
		String paramId_hor_desp = "";		
		
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);		

		// 2.1 Parámetro url (Obligatorio)
		if ( req.getParameter("url") == null ){
			throw new ParametroObligatorioException("url es null");
		}		
		paramUrl = req.getParameter("url");		
		logger.debug("url: " + paramUrl);		
		
		// 2.2 Parámetro id_hor_desp (Obligatorio)
		if ( req.getParameter("id_hor_desp") == null){
			throw new ParametroObligatorioException("id_hor_desp es null");			
		}
		paramId_hor_desp = req.getParameter("id_hor_desp");
		logger.debug("id_hor_desp: " + paramId_hor_desp);
		
		// si falla el parseLong debiese levantar una excepción
		long id_hor_desp = Long.parseLong(paramId_hor_desp);
		

		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		try{
			biz.DelJornadasDespachoyHorario(id_hor_desp);
			logger.debug("Se eliminó correctamente la jornada de despacho");
		}catch(BolException ex){
			logger.debug("Controlando excepción: " + ex.getMessage());
			if ( ex.getMessage().equals(Constantes._EX_ID_H_DESP_NO_EXISTE) ){
				logger.debug("El horario no existe en el sistema");
				fp.add( "rc" , Constantes._EX_ID_H_DESP_NO_EXISTE );
				paramUrl = UrlError + fp.forward();
			}	
			else if ( ex.getMessage().equals("JD003") ){ 
				logger.debug(Constantes._EX_JDESP_NO_DELETE);
				logger.debug("El horario no se puede eliminar porque tiene pedidos asociados");
				fp.add( "rc" , Constantes._EX_JDESP_NO_DELETE );
				paramUrl = UrlError + fp.forward();
			}
			logger.debug("paramUrl ="+ paramUrl);
		}
		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
}
