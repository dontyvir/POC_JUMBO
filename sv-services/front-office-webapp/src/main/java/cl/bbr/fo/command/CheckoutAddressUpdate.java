package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;

/**
 * Modifica la dirección de despacho en el checkout
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class CheckoutAddressUpdate extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse res)
			throws CommandException {
		try {

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
            String mensajeSistema = "OK";
            String formaDespacho = arg0.getParameter("f_despacho").toString();
			String aux = arg0.getParameter("direccionid").toString();
			String[] aux_text = aux.split("--");
            String direccion = "--";

			session.setAttribute("ses_dir_id", aux_text[0]);
			session.setAttribute("ses_loc_id", aux_text[1]);
			session.setAttribute("ses_zona_id", aux_text[2]);
            
            session.setAttribute("ses_forma_despacho", formaDespacho);
            
            BizDelegate biz = new BizDelegate();
            if (!formaDespacho.equalsIgnoreCase("R")) {
                DireccionesDTO dir = biz.clienteGetDireccion(Long.parseLong(session.getAttribute("ses_dir_id").toString()));
                direccion = dir.getCalle()+" "+dir.getNumero()+" "+dir.getDepto()+", "+dir.getCom_nombre()+", "+dir.getReg_nombre();
            } else {
                LocalDTO loc = biz.getLocalRetiro(Long.parseLong(aux_text[1]));
                direccion = loc.getNom_local();
            }
            
            res.setContentType("text/xml");
            res.setHeader("Cache-Control", "no-cache");
            res.setCharacterEncoding("UTF-8");

            res.getWriter().write("<datos_producto>");
            res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
            res.getWriter().write("<forma_despacho>" + formaDespacho + "</forma_despacho>");
            res.getWriter().write("<direccion>" + direccion + "</direccion>");
            res.getWriter().write("<id_local>" + aux_text[1] + "</id_local>");
            res.getWriter().write("</datos_producto>");
			
		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}

	}

}