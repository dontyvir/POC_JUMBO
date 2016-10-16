package cl.bbr.common.command;
 
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;

/**
 * Logoff es un Servlet que permite desconectarse a los compradores del sitio.
 * <p>
 * Comportamiento:
 * <ul>
 * <li>elimina la sessión.</li>
 * <li>el comando envía a la URL cuando es exitosa la conexión.</li>
 * </ul>
 * <p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class Logoff extends Command {

	/**
	 * Permite la desconexión del usuario.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws ServletException
	 * @throws IOException
	 */
	
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		
		// Se recupera la sesión del usuario
		HttpSession session = arg0.getSession();		
				
		// Recupera parámetro desde web.xml
		String dis_ok = "";
		if(session.getAttribute("ses_fono_id") != null)
			dis_ok = getServletConfig().getInitParameter("dis_ok_fono");
		else
			dis_ok = getServletConfig().getInitParameter("dis_ok");
		
		// Se desconecta la sesión
		session.invalidate();
		
		arg1.sendRedirect( dis_ok );
		
	}	

}