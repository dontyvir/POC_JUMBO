package cl.bbr.fo.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Logoff es un Servlet que permite desconectarse a los usuarios del sitio.
 * <p>
 * Comportamiento:
 * <ul>
 * <li>elimina la sessión.</li>
 * <li>el comando envía a la URL cuando es exitosa la conexión.</li>
 * </ul>
 * <p>
 * 
 * @author BBRI
 *  
 */
public class FonoLogoff extends FonoCommand {

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
				
		// Se desconecta la sesión
		session.invalidate();
		
		// Recupera parámetro desde web.xml
		String dis_ok = getServletConfig().getInitParameter("dis_ok");
		arg1.sendRedirect( dis_ok );
		
	}	

}