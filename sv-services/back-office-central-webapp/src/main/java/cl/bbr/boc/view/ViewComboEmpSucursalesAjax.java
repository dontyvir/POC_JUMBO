package cl.bbr.boc.view;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
/**
 * Con un ajax permite cambiar datos de la empresa y la sucursal sin cargar la pagina
 *  
 * @author BBRI
 */
public class ViewComboEmpSucursalesAjax extends Command {
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_empresa = 0;
		
		View salida = new View(res);
		BizDelegate bizDelegate = new BizDelegate();
		
		if (req.getParameter("id_empresa") != null && !req.getParameter("id_empresa").equals(""))
			id_empresa= Integer.parseInt(req.getParameter("id_empresa"));
		else {
			id_empresa= 0;
		}		
		
		logger.debug("id_empresa: "+id_empresa);
		
		//Listado de sucursales
		List lst_sucursales = bizDelegate.getSucursalesByEmpresaId(id_empresa);
		String result = "";
			
		if ( lst_sucursales.size() > 0 ){
			for (int i = 0; i < lst_sucursales.size(); i++) {
				SucursalesDTO suc = (SucursalesDTO) lst_sucursales.get(i);
				
				//SucursalesDTO sucDto = bizDelegate.getSucursalById(suc.getSuc_id());
				//logger.debug("Esto es lo que devuelve sucDto " + sucDto.getSuc_nombre() );
				result += suc.getSuc_id() + "|" + suc.getSuc_nombre() + " #";
				
				logger.debug(result);
	
			}
			result = result.substring(0,result.length()-1);
		}
		
		salida.setHtmlOut(result);
		salida.Output();
		
	}

	
}
