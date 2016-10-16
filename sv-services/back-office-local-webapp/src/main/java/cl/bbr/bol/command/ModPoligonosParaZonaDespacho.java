package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.PoligonosZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que modifica las comunas de las zonas de despacho
 * @author mleiva
 */


public class ModPoligonosParaZonaDespacho extends Command {
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl				= "";
		String paramId_zona			= "";
		long id_zona				= -1;
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
		
		//2.2 Arreglo de select
		String[] poligonos_str = req.getParameterValues("poligonos_zona");
		long[] poligonos_long = null;
		if (poligonos_str != null){
			poligonos_long = new long[poligonos_str.length] ;
			for(int i=0;i<poligonos_str.length ; i++){
			    poligonos_long[i] = Long.parseLong(poligonos_str[i]);
				logger.debug("pol["+i+"]: "+poligonos_long[i]);
			}
		}
				
		
		//2.3 Parametro id_zona
		if ( req.getParameter("id_zona") == null){
			throw new ParametroObligatorioException("id zona es null");
		}
		
		paramId_zona = req.getParameter("id_zona");
		id_zona = Long.parseLong(paramId_zona);
		logger.debug("id_zona: " + paramId_zona);
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		
		PoligonosZonaDTO poligono = new PoligonosZonaDTO();
		poligono.setPoligonos(poligonos_long);
		poligono.setId_zona(id_zona);
		
		
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		String mensaje = "";
		
		try{
			biz.doActualizaPoligonosZonaDespacho(poligono);
			mensaje ="los datos han sido actualizados";
		}catch(BolException ex){
			if(ex.getMessage().equals(Constantes.PARAMETRO_OBLIGATORIO)){
				logger.debug("Faltan Parametros que son Obligatorios");
				fp.add( "rc" , Constantes.PARAMETRO_OBLIGATORIO );
				paramUrl = UrlError + fp.forward();
			}
			if(ex.getMessage().equals(Constantes._EX_ZONA_ID_INVALIDA)){
				logger.debug("El Id de la Zona es Inválido");
				fp.add( "rc" , Constantes._EX_ZONA_ID_INVALIDA );
				paramUrl = UrlError + fp.forward();
			}
			if(ex.getMessage().equals(Constantes._EX_LOCAL_ID_INVALIDO)){
				logger.debug("El Id del Local es inválido");
				fp.add( "rc" , Constantes._EX_LOCAL_ID_INVALIDO );
				paramUrl = UrlError + fp.forward();
			}
			mensaje = "las comunas no se pudieron actualizar";
		}
		paramUrl+="?id_zona="+id_zona+"&mensaje="+mensaje;
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	}
}
