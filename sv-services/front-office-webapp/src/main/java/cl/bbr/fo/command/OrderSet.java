package cl.bbr.fo.command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Despliega pedido
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class OrderSet extends Command {

	/**
	 * Despliega pedido
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
 	
		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();
		
		try {
						
			// Carga properties
			//ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			this.getLogger().info("Check pedido");
			this.getLogger().info( "Pedido en session: " + session.getAttribute("sesspedido") );
			if( session.getAttribute("sesspedido") != null )
				out.print("1");
			else
				out.print("0");
			
		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			out.print("0");
		}
	}
		
}