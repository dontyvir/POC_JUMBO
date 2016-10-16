package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.DireccionesDTO;

/**
 * DelDirDespacho Comando Process
 * Elimina una direccion de despacho de una sucursal, cambiando su estado
 * 
 * @author BBRI
 */

public class DelDirFacturacion extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl 			="";
     long 	paramId_dir			=-1;
     long 	paramId_sucursal	=-1;
     String UrlError = "";
     
     
 	ForwardParameters fp = new ForwardParameters();
	fp.add( req.getParameterMap() );
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_dir") == null ){throw new ParametroObligatorioException("id_dir es null");}
     if ( req.getParameter("id_sucursal") == null ){throw new ParametroObligatorioException("id_sucursal es null");}
     if ( req.getParameter("UrlError") == null ){throw new ParametroObligatorioException("UrlError es null");}

     // 2.2 obtiene parametros desde el request
     paramUrl 			= req.getParameter("url");
     paramId_dir		= Long.parseLong(req.getParameter("id_dir"));
     paramId_sucursal	= Long.parseLong(req.getParameter("id_sucursal"));
     UrlError           = req.getParameter("UrlError");
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_dir: " + paramId_dir);
     logger.debug("id_sucursal: " + paramId_sucursal);
     logger.debug("UrlError: " + UrlError);
     //ForwardParameters fp = new ForwardParameters();
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
  		DireccionesDTO prm = new DireccionesDTO ();
  		prm.setId(paramId_dir);
  		prm.setCli_id(paramId_sucursal);
     
  		//obtiene mensajes
		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
 		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
  	     
  		boolean result = false;
  		try{
  			result = biz.delDirFacturacion(prm.getId());
  			logger.debug("result?"+result);
  			if(result){
  				//paramUrl += "&id_dir=" + paramId_dir +"&id_sucursal=" + paramId_sucursal + "&msje=" +  mensaje_exito;
  				fp.add( "msje" , mensaje_exito);
  				paramUrl = paramUrl + fp.forward();
  			}else{
  				fp.add( "msje" , mensaje_fracaso);
  				paramUrl = UrlError + fp.forward();
  			}
  		 			
  			
  		} catch(BocException e){

  	  	    
     		logger.debug("Controlando excepción: " + e.getMessage());
			
			if ( e.getMessage()!=null &&  !e.getMessage().equals("")){
				logger.debug(e.getMessage());
				fp.add( "msje" , e.getMessage() );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}
  		

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
