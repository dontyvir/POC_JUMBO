package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar los perfiles
 * @author bbr
 *
 */
public class ModPerCmd extends Command {
	private final static long serialVersionUID = 1;
 
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     long paramId_cmd = 0L;
	     long paramId_perf = 0L;
	     String paramAction = "";
	     
	     // 2. Procesa parámetros del request
	
	     logger.debug("Procesando parámetros...");
	
	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("cmd_cod") == null ){throw new ParametroObligatorioException("cmd_cod es nulo");}
	     if ( req.getParameter("per_id") == null ){throw new ParametroObligatorioException("per_id es nulo");}
	     if ( req.getParameter("accion") == null ){throw new ParametroObligatorioException("accion es nulo");}
	
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramId_cmd = Long.parseLong(req.getParameter("cmd_cod")); //long:obligatorio:si
	     paramId_perf = Long.parseLong(req.getParameter("per_id")); //long:obligatorio:si
	     paramAction = req.getParameter("accion"); //string:obligatorio:si
	
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("cmd_cod: " + paramId_cmd);
	     logger.debug("per_id: " + paramId_perf);
	     logger.debug("action: " + paramAction);

	     /*
	      * 3. Procesamiento Principal
	      */
	     BizDelegate biz = new BizDelegate();

			//si accion es agregar perfil al Usuario
			if (paramAction.equals("agregar")){
				boolean resb = biz.agregaCmdPerf(paramId_cmd, paramId_perf);
				logger.debug("agregar:"+resb);
				
			}
			else
			//si accion = eliminar:
			if (paramAction.equals("eliminar")){
				boolean resb = biz.elimCmdPerf(paramId_cmd, paramId_perf);
				logger.debug("eliminar:"+resb);
			}
	     
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);
	
	}//execute

}
