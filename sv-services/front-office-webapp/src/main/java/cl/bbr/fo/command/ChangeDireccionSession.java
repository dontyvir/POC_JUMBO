package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;

/**
 * cambia en session la direccion
 *  
 * @author imoyano
 *  
 */
public class ChangeDireccionSession extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        String mensajeSistema = "OK";
		try {
            HttpSession session = arg0.getSession();
            
            String aux = arg0.getParameter("loc_id");
            String[] aux_text = aux.split("--");
            session.setAttribute("ses_loc_id", aux_text[2]);
            session.setAttribute("ses_dir_id", aux_text[0]);
            session.setAttribute("ses_dir_alias", aux_text[1]);
            session.setAttribute("ses_zona_id", aux_text[3]);
            session.setAttribute("ses_forma_despacho", arg0.getParameter("forma_despacho"));
            
            if ( !"undefined".equalsIgnoreCase(arg0.getParameter("rbtnelicarro")) && !"".equalsIgnoreCase(arg0.getParameter("rbtnelicarro")) && arg0.getParameter("rbtnelicarro") != null ) {
                // Borrar carro si lo selecciona el cliente
                long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
                
                // Instacia del bizdelegate
                BizDelegate biz = new BizDelegate();
                String ses_invitado_id = session.getAttribute("ses_invitado_id").toString();
                biz.deleteCarroCompraAll(cliente_id, ses_invitado_id);            
            }
            
        } catch (Exception e) {
            mensajeSistema = "Error al rescatar la direccion";
        }
        
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        arg1.getWriter().write("<direccion>");
        arg1.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        arg1.getWriter().write("</direccion>");
	}
}