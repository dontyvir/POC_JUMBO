package cl.bbr.bol.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.pedidos.collaboration.ProcIniciaFormPickingManualDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite finalizar el formulario de picking.
 * @author BBR
 *
 */
public class FinalizaFormPickManual extends Command{

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
			List perfiles = usr.getPerfiles();
			long perfil =0;
			
			
			boolean esPickeador = false;
			for (int i=0; i<perfiles.size();i++){
			PerfilesEntity per =	(PerfilesEntity)perfiles.get(i);
				
				perfil = per.getIdPerfil().longValue();
				if (perfil == Constantes.ID_PERFIL_PICKEADOR) {
					esPickeador=true;
					break;
				}
			}
			
			if (esPickeador){
				datos.setId_perfil(perfil);
				//datos.setId_perfil(Constantes.ID_PERFIL_PICKEADOR);
				datos.setLogin(usr.getLogin());
				biz.doFinalizaFormPickingManual(datos);
			}else{
				logger.debug("No es pickeador");
				fp.add( "rc" , Constantes._EX_VE_FPIK_NO_ES_PICKEADOR);
				paramUrl = UrlError + fp.forward();
			}
//			inicializa los pedidos relacionados al id_jpicking
			//biz.setPedidosByIdJornada(id_jpicking);

		}catch(BolException ex){
			if(ex.getMessage().matches("(?i).*"+Constantes._EX_RON_ID_INVALIDO+".*")){
				logger.debug("El id de la ronda es Inválido");
				fp.add( "rc" , Constantes._EX_RON_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}else 
			if (ex.getMessage().matches("(?i).*"+Constantes._EX_VE_FPIK_FINALIZA_MAL+".*")){
				logger.debug("Error en finalizar picking");
				fp.add( "rc" , Constantes._EX_VE_FPIK_FINALIZA_MAL);
				paramUrl = UrlError + fp.forward();
			}else
			if (ex.getMessage().matches("(?i).*"+Constantes._EX_RON_ID_INVALIDO+".*") ||
				ex.getMessage().matches("(?i).*"+Constantes._EX_RON_ESTADO_INADECUADO+".*")||
				ex.getMessage().matches("(?i).*"+Constantes._EX_OPE_REL_RONDA_NO_EXISTE+".*") ||
				ex.getMessage().matches("(?i).*"+Constantes._EX_OPE_REL_DET_NO_EXISTE+".*")||
				ex.getMessage().matches("(?i).*"+Constantes._EX_OPE_ID_NO_EXISTE+".*")){
				
				logger.debug("Error en al recepcionar ronda");
				fp.add( "rc" , Constantes._EX_VE_FPIK_RECEPCIONA_MAL);
				paramUrl = UrlError + fp.forward();
			}
		}		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
