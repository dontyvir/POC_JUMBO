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
 * comando que permite agregar una jornada de Despacho
 * @author mleiva
  */


public class EliminarSectorPicking extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl			= "";
		String paramId_sector	= "";
		long id_sector			= 0L;
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		//logger.debug("UrlError: " + UrlError);		
		
		paramUrl = getServletConfig().getInitParameter("Url");
		// 2.1 Parámetro url (Obligatorio)
				
		if ( req.getParameter("id_sector") == null ){
			throw new ParametroObligatorioException("id_sector es null");
		}
		paramId_sector = req.getParameter("id_sector");
		id_sector = Long.parseLong(paramId_sector);
		
			// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		try{
			biz.doEliminaSectorPicking(id_sector);
			//paramUrl = paramUrl;
		}catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_ID_SECTOR_INVALIDO)){
				logger.debug("Id del sector es inválido");
				fp.add( "rc" , Constantes._EX_ID_SECTOR_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}
		}
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
