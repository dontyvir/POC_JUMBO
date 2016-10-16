package cl.bbr.boc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega monitor de jornadas, si no hay alguno de los parametros seleccionados, entonces despliega valores por defecto con la fecha actual
 * @author jsepulveda
 */
public class ViewActivaBolsa extends Command {
		
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		// 1. Parámetros de inicialización servlet
		BizDelegate bizDelegate = new BizDelegate();
				
		try{
			bizDelegate.activaBolsa();
		}catch (Exception ex) { 
			ex.printStackTrace();
		}
		String paramUrl = "ViewMonitorBolsas";
		res.sendRedirect(paramUrl);
	}
}
