package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * comando que permite crear jornadas de Despacho en forma masiva
 * @author bbr
  */


public class CreaJornadaDespachoMasivo extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl			= "";
		long paramId_semana_origen	= -1;
		long paramId_semana_inicio	= -1;
		long paramId_semana_fin	= -1;
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);		
		
		// 2.1 Parámetro url (Obligatorio)
		if ( req.getParameter("url") == null ){ throw new ParametroObligatorioException("url es null"); }
		if ( req.getParameter("id_semana_origen") == null ){ throw new ParametroObligatorioException("id_semana_origen es null"); }
		if ( req.getParameter("id_semana_inicio") == null ){ throw new ParametroObligatorioException("id_semana_inicio es null"); }
		if ( req.getParameter("id_semana_fin") == null ){ throw new ParametroObligatorioException("id_semana_fin es null"); }
		
		paramUrl = req.getParameter("url");
		paramId_semana_origen = Long.parseLong(req.getParameter("id_semana_origen"));
		paramId_semana_inicio = Long.parseLong(req.getParameter("id_semana_inicio"));
		paramId_semana_fin = Long.parseLong(req.getParameter("id_semana_fin"));
		
		logger.debug("url			: " + paramUrl);
		logger.debug("id_semana_origen: " + paramId_semana_origen);
		logger.debug("id_semana_inicio: " + paramId_semana_inicio);
		logger.debug("id_semana_fin	: " + paramId_semana_fin);
		
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		
		//ProcCreaJornadaDespachoDTO dto = new ProcCreaJornadaDespachoDTO();
		
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		//try{
			boolean result = false; //biz.setCreaJornadaDespachoMasivo(dto);
			logger.debug("result: " + result); 
		/*}catch(BolException ex){
			logger.debug("Controlando excepción: " + ex.getMessage());
			if ( ex.getMessage().equals(Constantes._EX_JDESP_JORNADA_EXISTE) ){
				logger.debug("El horario ya existe en el sistema");
				fp.add( "rc" , Constantes._EX_JDESP_JORNADA_EXISTE );
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals( Constantes._EX_JDESP_FALTAN_DATOS ) ){ // Controlamos error faltan parametros
				logger.debug("faltan datos");
				fp.add( "rc" , Constantes._EX_JDESP_FALTAN_DATOS );
				paramUrl = UrlError + fp.forward();
			}
			if ( ex.getMessage().equals( Constantes._EX_SEMANA_ID ) ){
				logger.debug("no existe id de semana");
				fp.add( "rc" , Constantes._EX_SEMANA_ID);
				paramUrl = UrlError + fp.forward();
			}
			if ( ex.getMessage().equals( Constantes._EX_ZONA_ID_INVALIDA ) ){
				logger.debug("id de zona es inválida");
				fp.add( "rc" , Constantes._EX_ZONA_ID_INVALIDA);
				paramUrl = UrlError + fp.forward();
			}
		}*/
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
