package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelGenericProductDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * DelGenericProduct Comando Process
 * Elimina productos genericos
 * @author BBRI
 */

public class DelGenericProduct extends Command {
	private final static long serialVersionUID = 1;
 
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	     
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     long paramId_producto=0L;
	     // 2. Procesa parámetros del request
	
	     logger.debug("Procesando parámetros...");
	
	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_producto") == null ){throw new ParametroObligatorioException("id_producto es nulo");}
	
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramId_producto = Long.parseLong(req.getParameter("id_producto")); //long:obligatorio:si
	
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_producto: " + paramId_producto);
	     
	     ForwardParameters fp = new ForwardParameters();
		 fp.add( req.getParameterMap() );
	     /*
	      * 3. Procesamiento Principal
	      */
	     
	     BizDelegate biz = new BizDelegate();
	     String mensElim = getServletConfig().getInitParameter("MensElim");
			
	     try{
			ProcDelGenericProductDTO param = new ProcDelGenericProductDTO();  
			param.setId_producto(paramId_producto);
			param.setMen_elim(mensElim);
			param.setUsr_login(usr.getLogin());
			
			biz.setDelGenericProduct(param);
	     }catch(BocException e) {
	    	 logger.debug("Controlando excepción del AddProdcatweb: " + e.getMessage());
	    	 if ( e.getMessage().equals(Constantes._EX_PROD_ID_NO_EXISTE) ){
					logger.debug("El código de producto ingresado no existe");
					fp.add( "rc" , Constantes._EX_PROD_ID_NO_EXISTE );
				    String UrlError = getServletConfig().getInitParameter("UrlError");
					paramUrl = UrlError + fp.forward(); 
			 } else {
					logger.debug("Controlando excepción: " + e.getMessage());
			 }
	     }
	
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);
	
	}//execute

}
