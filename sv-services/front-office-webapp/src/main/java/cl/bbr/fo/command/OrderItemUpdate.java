package cl.bbr.fo.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;

/**
 * Actualiza la cantidad de un producto en el carro
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class OrderItemUpdate extends Command {

	/**
	 * Despliega productos en el carro
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {
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

			long cant_registros = 0;
			if( arg0.getParameter("registros") != null )
				cant_registros = Long.parseLong(arg0.getParameter("registros"));
			
			double cantidad 	= 0;
			double cantidad_old = -1;
			long carr_id 		= 0;
			long idProducto		= 0;
			List idsProductos 	= new ArrayList();
			
			for( int i = 0; i < cant_registros; i++ ) {	
			    if (arg0.getParameter("cantidad"+i) == null) {
			        continue;
			    }
				//Se recupera la cantidad
				cantidad = Double.parseDouble( arg0.getParameter("cantidad"+i).toString());

				// Se recupera la cantidad anterior
				if( arg0.getParameter("cantidad_old"+i) != null )
					cantidad_old = Double.parseDouble( arg0.getParameter("cantidad_old"+i).toString());
				
				if( cantidad_old == cantidad )
					continue;
				
				// Se recupera el ID del carro
				carr_id = Long.parseLong( arg0.getParameter("carr_id"+i).toString());
				
				// Se recupera el ID del producto
				if (arg0.getParameter("id_producto") != null) {
				    idProducto = Long.parseLong( arg0.getParameter("id_producto").toString());
				}
				// Instacia del bizdelegate
				BizDelegate biz = new BizDelegate();

				String nota = "";
				
				if( arg0.getParameter("nota"+i) != null )
					nota = arg0.getParameter("nota"+i);

				//TODO DONALD ver si se cae si sessid viene null
				if ( cantidad > 0 ) {
					this.getLogger().info( "update car_id:" + carr_id + " nota:" + nota + " cantidad:" + cantidad );
					biz.carroComprasUpdateProducto(cliente_id, carr_id, cantidad, nota, invitado_id, null);
					idsProductos.add(""+idProducto);
				} else {
					this.getLogger().info( "delete car_id:" + carr_id );
					biz.carroComprasDeleteProducto(cliente_id, carr_id, invitado_id);
				}
			}
			
			if( arg0.getParameter("url") == null || arg0.getParameter("url").compareTo("")== 0 ) {
				// Recupera parámetro desde web.xml			    
			    if ( idsProductos.size() > 0 ) {
				    session.setAttribute("ids_prod_add", idsProductos);
				}			    
				String dis_ok =  getServletConfig().getInitParameter("dis_ok");
				arg1.sendRedirect( dis_ok );
			} else {
				//arg1.sendRedirect( arg0.getParameter("url").toString() );
				getServletConfig().getServletContext().getRequestDispatcher( arg0.getParameter("url").toString() ).forward(arg0, arg1);
			}

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}

	}

}