package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * DelGenericProduct Comando Process
 * Elimina productos genericos
 * @author BBRI
 */

public class DelMarca extends Command {
	private final static long serialVersionUID = 1;
 
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	     
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     long paramId_marca=0L;
	     // 2. Procesa parámetros del request
	
	     logger.debug("Procesando parámetros...");
	
	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("mar_id") == null ){throw new ParametroObligatorioException("mar_id es nulo");}
	
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramId_marca = Long.parseLong(req.getParameter("mar_id")); //long:obligatorio:si
	
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("mar_id: " + paramId_marca);
	     
	     ForwardParameters fp = new ForwardParameters();
		 fp.add( req.getParameterMap() );
	     /*
	      * 3. Procesamiento Principal
	      */
	     
	     BizDelegate biz = new BizDelegate();
	     String mensElim = getServletConfig().getInitParameter("MensElim");
			
	     try{

	    	 MarcasDTO mrc = new MarcasDTO();
	    	 mrc.setId(paramId_marca);
			
			 boolean result= biz.setDelMarca(mrc);
			 logger.debug("result:"+result);
	     }catch(BocException e) {
	    	 logger.debug("Controlando excepción del AddProdcatweb: " + e.getMessage());
	    	 if ( e.getMessage().equals(Constantes._EX_MAR_ID_NO_EXISTE) ){
					logger.debug("El código de marca no existe");
					fp.add( "rc" , Constantes._EX_MAR_ID_NO_EXISTE );
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
