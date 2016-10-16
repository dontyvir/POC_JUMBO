package cl.bbr.fo.command;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;

/**
 * Actualiza un producto del carro - Ajax
 *  
 * @author imoyano
 *  
 */
public class ModProductoCarro extends Command {
	
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

			double cantidad = 0;
			long carr_id 	= 0;
            String nota     = "";
			
	    	cantidad = Double.parseDouble( arg0.getParameter("cantidad").toString());
			carr_id = Long.parseLong( arg0.getParameter("carr_id").toString());
            nota = arg0.getParameter("nota");
				
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

            //TODO DONALD ver si se cae si sessid viene null
			if ( cantidad > 0 ) {
				this.getLogger().debug( "update car_id:" + carr_id + " nota:" + nota + " cantidad:" + cantidad );
				biz.carroComprasUpdateProducto(cliente_id, carr_id, cantidad, nota, invitado_id, null);
			} else {
				this.getLogger().debug( "delete car_id:" + carr_id );
				biz.carroComprasDeleteProducto(cliente_id, carr_id, invitado_id);
			}
            
		} catch (Exception e) {
			this.getLogger().error(e);
            e.printStackTrace();
            respuesta = "Ocurrió un error al modificar el producto.";
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