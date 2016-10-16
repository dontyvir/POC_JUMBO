package cl.bbr.boc.view;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Ajax que permite mostrar las zonas de un local
 *  
 * @author BBRI
 */
public class ViewComboLocalZonasAjax extends Command {
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_local = 0;
		
		View salida = new View(res);
		BizDelegate biz = new BizDelegate();
		
		if (req.getParameter("id_local") != null && !req.getParameter("id_local").equals(""))
			id_local= Integer.parseInt(req.getParameter("id_local"));
		else {
			id_local= 0;
		}		
		
		logger.debug("id_local: "+id_local);
		
		//Obtenemos las Zonas
		
		List lst_zonas = biz.getZonasLocal(id_local);
		String result = "";
			
		if ( lst_zonas.size() > 0 ){
			for (int i = 0; i < lst_zonas.size(); i++) {
				ZonaDTO zon = (ZonaDTO) lst_zonas.get(i);
				
				result += zon.getId_zona() + "|" + zon.getNombre() + " #";
				
				logger.debug(result);
	
			}
			result = result.substring(0,result.length()-1);
		}
		
		salida.setHtmlOut(result);
		salida.Output();
		
	}

	
}
