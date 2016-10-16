package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;

/**
 * Comando que permite modificar la información de la empresa, segun los datos 
 * ingresados en el formulario
 * @author BBR
 *
 */
public class DelProdCotizacion extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl 			="";
     long	paramIdDetCot  		= -1;
     long	paramIdCot  		= -1;
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("detcot_id") == null ){throw new ParametroObligatorioException("id_detcot es null");}
     if ( req.getParameter("cot_id") == null ){throw new ParametroObligatorioException("id_cot es null");}
     
        // 2.2 obtiene parametros desde el request
     paramUrl 			= req.getParameter("url");
     paramIdDetCot	    = Long.parseLong(req.getParameter("detcot_id"));
     paramIdCot	        = Long.parseLong(req.getParameter("cot_id"));
   
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_detcot: " + paramIdDetCot);
     logger.debug("id_cot: " + paramIdCot);
     

     
     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     
	//obtiene mensajes
	String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
	String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
     /*
      * 3. Procesamiento Principal
      */
   	BizDelegate bizDelegate = new BizDelegate();
     	
 	try{

 		boolean result = bizDelegate.delProductoCotizacion(paramIdDetCot);
		logger.debug("result?"+result);
		if(result){
			LogsCotizacionesDTO log = new LogsCotizacionesDTO();
			String mjelog = "Se ha eliminado un producto de la cotización."; 
			log.setCot_id(paramIdCot);
			log.setDescripcion(mjelog);
			log.setUsuario(usr.getLogin());
			bizDelegate.addLogCotizacion(log);
			paramUrl += "&msje=" +  mensaje_exito;
		}else{
			paramUrl += "&msje=" +  mensaje_fracaso;
		}
		
 	}catch(BocException e){
 		logger.debug("Controlando excepción: " + e.getMessage());
		String UrlError = getServletConfig().getInitParameter("UrlError");
		if ( e.getMessage()!= null && !e.getMessage().equals("") ){
			logger.debug(e.getMessage());
			fp.add( "msje" , e.getMessage() );
			fp.add( "id_detcot" , String.valueOf(paramIdDetCot) );
			paramUrl = UrlError + fp.forward(); 
		} else { 
			logger.debug("Controlando excepción: " + e.getMessage());
		}
 	}
 

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
