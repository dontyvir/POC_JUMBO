package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;


/**
 * Descripci�n de la clase
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class RegisterFormGetDes extends Command {

	/**
	 * Descripci�n del m�todo
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

		// Recupera la sesi�n del usuario s�lo si existe
		HttpSession session = arg0.getSession();

		List lista = (List) session.getAttribute("despachos");
		
		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				if( arg0.getParameter("reg_id") != null && Integer.parseInt(arg0.getParameter("reg_id")) == i ) {
					DireccionesDTO dir = (DireccionesDTO) lista.get(i);
					out.print( i + "||");
					out.print( dir.getAlias() + "||");
					out.print( dir.getTipo_calle() + "||");
					out.print( dir.getCalle() + "||");
					out.print( dir.getNumero() + "||");
					out.print( dir.getDepto() + "||");
					out.print( dir.getReg_id() + "||");
					out.print( dir.getCom_id() + "||");
					out.print( dir.getComentarios() + "||" );
					return;
				}
			}
		}

	}

}