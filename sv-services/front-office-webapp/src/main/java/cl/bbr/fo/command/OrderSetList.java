package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;

/**
 * Modifica el nombre de la compra internet para el cliente
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class OrderSetList extends Command {

	/**
	 * Agrega una compra internet para el cliente
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
 	
		try {
						
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();			
			
			// Se recupera la salida para el servlet
			//PrintWriter out = arg1.getWriter();
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
			
			long opcionradio = Long.parseLong(arg0.getParameter("opcionradio"));
			String txtNewNombre =  arg0.getParameter("txtNewNombre");
			
			if(opcionradio == 1 && txtNewNombre != "" ){
				biz.updateNombreCompraHistorica(txtNewNombre, cliente_id );
			}
			
			String dis_ok = getServletConfig().getInitParameter("dis_ok");
			if( session.getAttribute("ses_eje_id") != null ) // Si el fono compras => redirecciona para allá
				dis_ok = getServletConfig().getInitParameter("dis_ok_fono");
			arg1.sendRedirect( dis_ok );
			
		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException( e );
		}
	}
}