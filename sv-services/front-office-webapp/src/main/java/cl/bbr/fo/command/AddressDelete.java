package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;

/**
 * 
 * Permite eliminar direcciones de despachos de los clientes
 * 
 * @author BBR e-commerce & retail
 *
 */
public class AddressDelete extends Command { 
	
	/**
	 * Elimina una dirección de despacho.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */	
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {
		
			long id = 0;
			if( arg0.getParameter("id") != null ) {
				// Recupera el Id del cliente a eliminar
				id = Long.parseLong(arg0.getParameter("id"));
			}
	
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
	
			biz.clienteDeleteDireccion(id);
			
			// Redirecciona ok
			// Recupera pagina desde web.xml
			String dis_ok = getServletConfig().getInitParameter("dis_ok");
			arg1.sendRedirect(dis_ok);
		
		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException(e);
		}		

	}
	
}