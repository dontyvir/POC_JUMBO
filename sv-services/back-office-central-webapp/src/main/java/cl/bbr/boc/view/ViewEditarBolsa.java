package cl.bbr.boc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.bolsas.dto.BolsaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega monitor de jornadas, si no hay alguno de los parametros seleccionados, entonces despliega valores por defecto con la fecha actual
 * @author jsepulveda
 */
public class ViewEditarBolsa extends Command {

	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		// 1. Parámetros de inicialización servlet
		BizDelegate bizDelegate = new BizDelegate();
		
		
		// 2. Actualizar bolsa de regalo
		BolsaDTO bolsa = new BolsaDTO();
		String cod_bolsa = new String();
		String desc_bolsa = new String();
		String cod_sap_bolsa = new String();
		String cod_barra_bolsa = new String();
		
		cod_bolsa = req.getParameter("cod_bolsa");
		desc_bolsa = req.getParameter("desc_bolsa");
		cod_barra_bolsa = req.getParameter("cod_barra_bolsa");
		
		bolsa.setCod_bolsa(cod_bolsa);
		bolsa.setDesc_bolsa(desc_bolsa);
		bolsa.setCod_barra_bolsa(cod_barra_bolsa);

		bizDelegate.actualizarBolsa(bolsa);
				
		
		// 3. insertar log bolsas
		bizDelegate.insertarRegistroBitacoraBolsas("Actualización de bolsa \""+desc_bolsa+"\" código " + cod_bolsa,
													usr.getLogin(), usr.getId_local()+"");
		
		
		String paramUrl = "ViewMonitorBolsas";
		res.sendRedirect(paramUrl);
	}
}
