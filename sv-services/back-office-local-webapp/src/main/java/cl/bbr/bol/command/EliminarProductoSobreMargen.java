package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Fecha de creación: 13/10/2011
 *
 * Metodo que se encarga de realizar la eliminacion de un detalle del reporte de productos 
 * sustitutos sobre margen
 * 
 */
public class EliminarProductoSobreMargen extends Command {

	/* Sobre escritura del metodo, que se encarga de realizar la eliminacion
	 * @see cl.bbr.common.framework.Command#Execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, cl.bbr.jumbocl.usuarios.dto.UserDTO)
	 */
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		//1. Variables del método
		String paramUrl = "";
		String idEliminar;
		//2. recupero del web.xml la url
		paramUrl = getServletConfig().getInitParameter("UrlResponse");
		logger.debug("paramUrl: " + paramUrl);
		//3. recupero el parametro
		idEliminar = req.getParameter("idEliminar");
		//4. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		if(idEliminar != null && !"".equals(idEliminar)){
			boolean resultado = biz.eliminarProductoSustitutoSobreMargen(Long.parseLong(idEliminar));
			if(!resultado){
				throw new BolException("No se pudo eliminar el detalle de producto sobre margen");
			}
		}
		//4. Redirecciona salida
		res.sendRedirect(paramUrl);
	}
	
}
