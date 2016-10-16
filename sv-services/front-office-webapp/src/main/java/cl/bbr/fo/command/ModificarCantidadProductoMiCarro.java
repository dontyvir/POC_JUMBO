package cl.bbr.fo.command;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;

/**
 * Elimina un producto del Carro - Ajax
 * 
 * @author carriagada IT4B
 *  
 */
public class ModificarCantidadProductoMiCarro extends Command {
	
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {
        
        String respuesta = "OK";
		try {
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			//Se recupera la session del cliente	
			long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
            String invitado_id = "";
            if (session.getAttribute("ses_invitado_id") != null &&
                    !session.getAttribute("ses_invitado_id").toString().equals("")){
                invitado_id = session.getAttribute("ses_invitado_id").toString();
            }

			// Se recupera el ID del carro
			long idCarro = Long.parseLong( arg0.getParameter("id_carro").toString());
			// Se recupera la cantidad
            double cantidad = Double.parseDouble( arg0.getParameter("cantidad").toString());
				
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
            biz.modificarCantidadProductoMiCarro(cliente_id, idCarro, cantidad, invitado_id, null);
		} catch (Exception e) {
			this.getLogger().error(e);
            e.printStackTrace();
            respuesta = "Ocurrió un error al Modificar la cantidad del producto.";
		}
        
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        try {
            arg1.getWriter().write("<datos_objeto>");
            arg1.getWriter().write("<respuesta>" + respuesta + "</respuesta>");
            arg1.getWriter().write("</datos_objeto>");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
	}
}