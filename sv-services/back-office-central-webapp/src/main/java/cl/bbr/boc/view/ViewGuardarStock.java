package cl.bbr.boc.view;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega monitor de jornadas, si no hay alguno de los parametros seleccionados, entonces despliega valores por defecto con la fecha actual
 * @author jsepulveda
 */
public class ViewGuardarStock extends Command {

	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		// 1. Parámetros de inicialización servlet
		BizDelegate bizDelegate = new BizDelegate();
		String id_sucursal = new String();
		String detalleLog = new String();
		
		// 2. Guardar el Stock
		for(Enumeration e = req.getParameterNames(); e.hasMoreElements(); ){
			String element = (String)e.nextElement();
			String cod_bolsa = new String();
			String cod_sucursal = new String();
			int stock = 0;
			if( element.indexOf("stock_") != -1){
				String[] bolsa = element.split("_");
				cod_bolsa = bolsa[1];
				cod_sucursal = bolsa[2];
				id_sucursal = cod_sucursal;
				stock = Integer.parseInt(req.getParameter(element));
				detalleLog += "- Cod Bolsa:" + cod_bolsa + "; Stock:"+stock;
				
				//Actualizar stock
				bizDelegate.actualizarStockBolsa(cod_bolsa, cod_sucursal, stock);
				
			}
		}
		
		// 3. insertar log bolsas
		String desc_sucursal = new String();
        
        if(usr.getLocal()==null || usr.getLocal().equals("")){
        	List lst_loc = bizDelegate.getLocales(usr.getId_local()+"");
    		
    		for (int i = 0; i < lst_loc.size(); i++) {
    			LocalDTO loc1 = (LocalDTO)lst_loc.get(i);
    			if (id_sucursal.equalsIgnoreCase(loc1.getId_local()+"")){
    				desc_sucursal = loc1.getNom_local();
    				break;
    			}
    		}
        }else{
        	desc_sucursal = usr.getLocal();
        }
		
		
		bizDelegate.insertarRegistroBitacoraBolsas("Actualizción stock bolsas " +
				" sucursal " + desc_sucursal + ", detalle:" + detalleLog,
				usr.getLogin(), usr.getId_local()+"");
		
		
		
		String paramUrl = "ViewMonitorBolsas?id_sucursal="+id_sucursal+"&respuesta_ok=Se actualizó stock exitosamente";
		res.sendRedirect(paramUrl);
	}
}
