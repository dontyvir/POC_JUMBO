package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;

/**
 * Modifica el nombre de la compra internet para el cliente invitado, ademas de guardar sus datos
 *  
 * @author carriagada-IT4B
 *  
 */
public class OrderSetListInvitado_OLD extends Command {

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
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
			
			String email_invitado =  arg0.getParameter("email_invitado");
            String codigo_telefono =  arg0.getParameter("codigo_invitado");
            String telefono_invitado =  arg0.getParameter("telefono_invitado");
            String opcion_compra =  arg0.getParameter("opcion_compra");
            String rut_invitado =  arg0.getParameter("rut_invitado");
            String dv_invitado =  arg0.getParameter("dv_invitado");
            String nombre_invitado =  arg0.getParameter("nombre_invitado");
            String apellido_invitado =  arg0.getParameter("apellido_invitado");
            
            ClienteDTO cliente = new ClienteDTO();
            cliente.setId(cliente_id);
            cliente.setEmail(email_invitado);
            cliente.setFon_cod_2(codigo_telefono);
            cliente.setFon_num_2(telefono_invitado);
            if (opcion_compra.equals("1")) {
                cliente.setRut(Long.parseLong(rut_invitado));
                cliente.setDv(dv_invitado);
                cliente.setNombre(nombre_invitado);
                cliente.setApellido_pat(apellido_invitado);
            }    
            
            biz.updateDatosInvitado(cliente, opcion_compra);
            
            
            String dis_ok = getServletConfig().getInitParameter("dis_ok");
			if( session.getAttribute("ses_eje_id") != null ) // Si el fono compras => redirecciona para allá
				dis_ok = getServletConfig().getInitParameter("dis_ok_fono");
			arg1.sendRedirect( dis_ok );
			
		} catch (Exception e) {
			this.getLogger().error(e);
			
			throw new CommandException( e );
		}
	}
}