package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar la marca
 * @author bbr
 *
 */
public class ModMarca extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	 
     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramMar_id = 0L;
     String paramNombre = "";
     String paramEst = "";
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("mar_id") == null ){throw new ParametroObligatorioException("mar_id es nulo");}
     if ( req.getParameter("mar_nombre") == null ){throw new ParametroObligatorioException("mar_nombre es nulo");}
     if ( req.getParameter("mar_est") == null ){throw new ParametroObligatorioException("mar_est es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramMar_id = Long.parseLong(req.getParameter("mar_id"));
     paramNombre = req.getParameter("mar_nombre"); //string:obligatorio:no
     paramEst = req.getParameter("mar_est"); //string:obligatorio:no

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("mar_id: " + paramMar_id);
     logger.debug("mar_nombre: " + paramNombre);
     logger.debug("mar_est: " + paramEst);
     

     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate biz = new BizDelegate();
     	
     	MarcasDTO dto = new MarcasDTO();
     	
     	dto.setId(paramMar_id);
     	dto.setEstado(paramEst);
     	dto.setNombre(paramNombre);
     	
		
		// Ejecuta Operación
     	boolean result = biz.modMarca( dto );
     	logger.debug("result? "+result);
     	
	
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
