package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.ComprXSucDTO;

/**
 * AddSucComprador Comando Process
 * Agrega la relacion entre comprador y sucursal, colocando tambien el tipo
 * 
 * @author BBRI
 */

public class AddSucComprador extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl 			="";
     long	paramId_sucursal	=-1;
     long	paramId_comprador	=-1;
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_sucursal") == null ){throw new ParametroObligatorioException("id_sucursal es null");}
     if ( req.getParameter("id_comprador") == null ){throw new ParametroObligatorioException("id_comprador es null");}
     

     // 2.2 obtiene parametros desde el request
     paramUrl 			= req.getParameter("url");
     paramId_sucursal	= Long.parseLong(req.getParameter("id_sucursal"));
     paramId_comprador	= Long.parseLong(req.getParameter("id_comprador"));
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_sucursal: " + paramId_sucursal);
     logger.debug("id_comprador: " + paramId_comprador);
     
     //ForwardParameters fp = new ForwardParameters();
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
  		ComprXSucDTO prm = new ComprXSucDTO();
  		prm.setId_sucursal(paramId_sucursal);
  		prm.setId_comprador(paramId_comprador);
     
  		//obtiene mensajes
		//String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
 		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
  	     
  		boolean result = false;
  		try{
  			result = biz.addRelSucursalComprador(prm);
  			logger.debug("result?"+result);
  			if(result){
  				paramUrl += "?id_comprador=" + paramId_comprador; // + "&msje=" +  mensaje_exito;
  			}else{
  				paramUrl += "?msje=" +  mensaje_fracaso;
  			}
  			
  		} catch(BocException e){
  	  		ForwardParameters fp = new ForwardParameters();
  	  	    fp.add( req.getParameterMap() );
  	  	    
     		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage()!= null && !e.getMessage().equals("") ){
				logger.debug("mensaje:"+e.getMessage());
				fp.add( "msje" , e.getMessage() );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}
  		
  		logger.debug("result:"+result);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
