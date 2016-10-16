package cl.bbr.boc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.bolsas.dto.BolsaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelGenericProductDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega monitor de jornadas, si no hay alguno de los parametros seleccionados, entonces despliega valores por defecto con la fecha actual
 * @author jsepulveda
 */
public class ViewEliminarBolsa extends Command {

	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		// 1. Parámetros de inicialización servlet
		BizDelegate bizDelegate = new BizDelegate();
		
		
		// 2. Eliminar bolsa de regalo
		BolsaDTO bolsa = new BolsaDTO();
		String cod_bolsa = new String();
		String desc_bolsa = new String();
		String cod_barra_bolsa = new String();
		int id_producto = -1;
		
		cod_bolsa = req.getParameter("cod_bolsa");
		id_producto = Integer.parseInt(req.getParameter("id_producto"));
		
		bolsa.setCod_bolsa(cod_bolsa);
		
		bizDelegate.eliminarBolsaRegalo(bolsa);
		
		//id_producto
		ProcDelGenericProductDTO procparam = new ProcDelGenericProductDTO();
		procparam.setId_producto(id_producto);
		procparam.setUsr_login(usr.getLogin());
		procparam.setMen_elim("Eliminación de bolsa de regalo "+cod_bolsa);
		bizDelegate.setDelGenericProduct(procparam);
		
		// 3. insertar log bolsas
		bizDelegate.insertarRegistroBitacoraBolsas("Eliminación de bolsa código " + cod_bolsa,
													usr.getLogin(), usr.getId_local()+"");
		
		String paramUrl = "ViewMonitorBolsas";
		res.sendRedirect(paramUrl);
	}
}
