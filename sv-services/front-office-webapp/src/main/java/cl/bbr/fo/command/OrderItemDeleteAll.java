package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;

/**
 * Vacía el carro de compras
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class OrderItemDeleteAll extends Command {

	/**
	 * Vacía el carro de compras
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();
		
		try {

			long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
			String ses_invitado_id = session.getAttribute("ses_invitado_id").toString();
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			biz.deleteCarroCompraAll(cliente_id, ses_invitado_id);
			
			arg0.setAttribute("rbtnelicarro", "1");
			
			getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_ok") ).forward(arg0, arg1);
			
			
			return;

		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
		}

	}

}