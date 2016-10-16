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
 * Elimina un horario de picking para un local con todas sus 
 * jornadas de picking asociadas
 * @author jsepulveda
 */
public class ElimJornadaPicking extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl	= "";
		String paramId_hor_pick = "";
		String fecha_param = "";
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);		
	
		// 2.1 Parámetro url (Obligatorio)
		
		if ( req.getParameter("fecha") == null){
			throw new ParametroObligatorioException("fecha es null");			
		}
		fecha_param = req.getParameter("fecha");
		logger.debug("fecha: " + fecha_param);
		
		
		if ( req.getParameter("url") == null ){
			throw new ParametroObligatorioException("url es null");
		}
		paramUrl = req.getParameter("url")+"?fecha="+fecha_param;		
		
		logger.debug("url: " + paramUrl);
		
		// 2.2 Parámetro id_jor_pick (Obligatorio)
		if ( req.getParameter("id_hor_pick") == null){
			throw new ParametroObligatorioException("id_hor_pick es null");			
		}
		paramId_hor_pick = req.getParameter("id_hor_pick");
		logger.debug("id_hor_pick: " + paramId_hor_pick);
		
		// si falla el parseLong debiese levantar una excepción
		long id_hor_pick = Long.parseLong(paramId_hor_pick);
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		try{
			biz.DelJornadasPickingyHorario(id_hor_pick);
			logger.debug("Se eliminó jornada de picking correctamente");
			//paramUrl = paramUrl;
		}catch(BolException ex){
			logger.debug("Controlando excepción: " + ex.getMessage());
			if ( ex.getMessage().equals(Constantes._EX_ID_H_PICK_NO_EXISTE) ){
				logger.debug("El horario de picking no existe en el sistema");
				fp.add( "rc" , Constantes._EX_ID_H_PICK_NO_EXISTE );
				paramUrl = UrlError + fp.forward();
			}else if ( ex.getMessage().equals(Constantes._EX_JPICK_NO_DELETE) ){ 
				logger.debug(Constantes._EX_JPICK_NO_DELETE);
				logger.debug("La Jornada no se puede eliminar porque tiene pedidos asociados");
				fp.add( "rc" , Constantes._EX_JPICK_NO_DELETE );
				paramUrl = UrlError + fp.forward();				
			}
		}
		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}		
	
	
}
