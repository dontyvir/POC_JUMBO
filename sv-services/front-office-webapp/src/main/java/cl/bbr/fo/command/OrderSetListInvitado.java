package cl.bbr.fo.command;

import java.io.IOException;

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
public class OrderSetListInvitado extends Command {

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
 	
        String respuesta = "OK";
        
		try {
						
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();			
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
			// Se agrego La id pedido
			String idpedido          = arg0.getParameter("id_pedido");
			//fin
			String email_invitado    =  arg0.getParameter("email_invitado");
            String codigo_telefono   =  arg0.getParameter("codigo_invitado");
            String telefono_invitado =  arg0.getParameter("telefono_invitado");
            String opcion_compra     =  arg0.getParameter("opcion_compra");
            String rut_invitado      =  arg0.getParameter("rut_invitado");
            String dv_invitado       =  arg0.getParameter("dv_invitado");
            String nombre_invitado   =  arg0.getParameter("nombre_invitado");
            String apellido_invitado =  arg0.getParameter("apellido_invitado");
            
//            ClienteDTO cliente = new ClienteDTO();
            ClienteDTO cliente = biz.clienteGetByRut(Long.parseLong(rut_invitado));
            
            if(rut_invitado.trim().length()>5 ){
            	if(cliente==null){
            		cliente = new ClienteDTO();
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
                    
                    //Cristian Valdebenito
                    if (idpedido!= null){
                    	int pedidoAux = Integer.parseInt(idpedido.substring(0,idpedido.length()-2));
                    biz.updatePedidoInvitado(pedidoAux,cliente.getRut(),cliente.getDv(), cliente.getNombre(),cliente.getApellido_pat());
                    }
                    
                    //String dis_ok = getServletConfig().getInitParameter("dis_ok");
        			//if( session.getAttribute("ses_eje_id") != null ) // Si el fono compras => redirecciona para allá
        			//	dis_ok = getServletConfig().getInitParameter("dis_ok_fono");
        			//arg1.sendRedirect( dis_ok );
            	}
                else
                	respuesta = "1";//"El cliente ya existe.";
            }
            else
            	respuesta = "2";//"El largo del rut debe ser mayor a 5";
            
            
            
			
		} catch (Exception e) {
            respuesta = "ERROR";
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException( e );
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