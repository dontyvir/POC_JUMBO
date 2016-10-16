package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar sustitutos cuando tienen un formato sin precio
 * @author mleiva
 */


public class ModSustitutoFormato extends Command {	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl				= "";		
		String param_id_detalle		= "";
		String param_descripcion	= "";
		String param_precio			= "";
		double precio				= 0;
		long   id_detalle			= 0L; 		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);	
		
		// 2.1 Parámetro url (Obligatorio)
		if ( req.getParameter("url") == null ){
			throw new ParametroObligatorioException("url es null");
		}
		if ( req.getParameter("id_detalle") == null ){
			throw new ParametroObligatorioException("id_detalle es null");
		}
		if ( req.getParameter("descripcion") == null ){
			throw new ParametroObligatorioException("descripcion es null");
		}
		if ( req.getParameter("precio") == null ){
			throw new ParametroObligatorioException("precio es null");
		}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);

		param_descripcion = req.getParameter("descripcion");
		logger.debug("descripcion: "+param_descripcion);
		
		param_precio = req.getParameter("precio");
		precio = Double.parseDouble(param_precio);
		logger.debug("precio: "+param_precio);
		
		param_id_detalle = req.getParameter("id_detalle");
		id_detalle = Long.parseLong(param_id_detalle);
		logger.debug("id_detalle: "+param_id_detalle);
		
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		DetallePickingDTO sust= new DetallePickingDTO();
		sust.setDescripcion(param_descripcion);
		sust.setPrecio(precio);
		sust.setId_dpicking(id_detalle);
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		String mensaje = "";
		
		try{
			biz.setDetallePickingSustituto(sust);
			mensaje ="los datos han sido actualizados"; 
			logger.debug("bien");
		}catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_OPE_ID_DETPCK_NO_EXISTE)){
				logger.debug("id_detalle no existe");
				fp.add( "rc" , Constantes._EX_OPE_ID_DETPCK_NO_EXISTE );
				paramUrl = UrlError + fp.forward();
			}
			logger.debug("mal");
			mensaje = "los sustitutos no pudieron ser modificados";
		}	
		paramUrl+="?id_detalle="+id_detalle+"&mensaje="+mensaje;
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
