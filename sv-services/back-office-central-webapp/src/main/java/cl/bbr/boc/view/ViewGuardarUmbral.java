package cl.bbr.boc.view;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @version 1.0
 * El boton guardar umbral de htm Ficha_umbral , llega a esta clase , ejecuta la accion para guardar y
 * @return viewUmbralForm
 * Guarda umbral 
 * @author Rmellis
 */
public class ViewGuardarUmbral extends Command {

	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
				
	
		// 1. Parámetros de inicialización servlet
		BizDelegate bizDelegate = new BizDelegate();
		int id_local = Integer.parseInt(req.getParameter("id_local"));
		double u_monto = Double.parseDouble(req.getParameter("u_monto"));
		double u_unidades = Double.parseDouble(req.getParameter("u_unidad"));
		String u_activacion = req.getParameter("u_activacion");
		Date now = new Date();
		String nom_local= req.getParameter("nom_local");
		logger.debug("----Inicio de Logger ----");
		logger.debug("Nombre Usuario  : "+usr.getLogin());
		logger.debug("Id Local " + id_local);
		logger.debug("Umbral Unidades : "+u_unidades);
		logger.debug("Fecha  : "+now.toString());
		logger.debug("Nombre_local : "+ nom_local);
		logger.debug("Umbral Activacion  : "+ u_activacion);
		logger.debug("Umbral Monto   " + u_monto);
		logger.debug("----Fin de Logger ----");
		
		/**
		 * -Insertar Umbral
		 * 
		 * @param id_local
		 * @param fecha actual
		 * @param usuario actual
		 * @param u_unidades
		 * @param u_monto
		 * @param u_activacion
		 */
		
		
	long consulta = bizDelegate.consultaIdLocal(id_local);
		//si es 0 no tiene porcentajes ingresados
		// si hay porcentajes se actualizan los porcentajes.
	if (consulta > 0) { 
		bizDelegate.updateUmbral(id_local,now.toString(),usr.getLogin(),u_unidades,u_monto,u_activacion);

	} else {
		bizDelegate.insertarUmbral(id_local,now.toString(),usr.getLogin(),u_unidades,u_monto,u_activacion);
	}
		
		

	
		
		
		//--Genera la url a redireccionar
		//manda resuesta de ok para actualizar
		String paramUrl = "ViewUmbralForm?id_local="+id_local+"&nom_local="+nom_local+"&u_unidad="+u_unidades+"&u_monto="+u_monto+"&u_activacion="+u_activacion+"&fecha_modi="+now.toString()+"&respuesta_ok=Se Actualizó El Umbral Correctamente";
		logger.debug ("Url A pasar : - "+paramUrl);
		//--Redirecciona salida de la Url
		res.sendRedirect(paramUrl);
	}
	
}

