/*
 * Creado el 02-oct-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.fo.command;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;

/**
 * @author Sebastian
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class EliminaProductosCarro extends Command{

	/* (sin Javadoc)
	 * @see cl.bbr.fo.command.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        String respuesta = "OK";
		try {
            String idInvitado = "";
            long idCliente = 0L;
            String listaProd;
            
            // Recupera la sesión del usuario
            HttpSession session = arg0.getSession();

            idCliente = Long.parseLong((String)session.getAttribute("ses_cli_id"));
            if (session.getAttribute("ses_invitado_id") != null &&
                    !session.getAttribute("ses_invitado_id").toString().equals("")){
            	idInvitado = session.getAttribute("ses_invitado_id").toString();
            }

			
			listaProd = arg0.getParameter("productos");
			String[] listaProductos= listaProd.split(",");
			
			// Instancia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			for ( int i =0; i < listaProductos.length; i++){
				long idProducto = Long.parseLong(listaProductos[i]);
				biz.carroComprasDeleteProductoxId(idCliente,idProducto, idInvitado);
			}
		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
            respuesta = "Ocurrió un error al guardar los productos en el carro.";
		}
        
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        try {
            arg1.getWriter().write("<respuesta>" + respuesta + "</respuesta>");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
	}
}
