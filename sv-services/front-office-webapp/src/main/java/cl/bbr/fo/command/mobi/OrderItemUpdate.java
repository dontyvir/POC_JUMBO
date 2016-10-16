package cl.bbr.fo.command.mobi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.command.Command;
import cl.bbr.fo.exception.CommandException;

/**
 * Actualiza la cantidad de un producto en el carro - mobile
 *  
 * @author imoyano
 *  
 */
public class OrderItemUpdate extends Command {

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {
		try {

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			//Se recupera la session del cliente	
			long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
			
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

                //TODO DONALD ver si se cae al enviar un null (mobile)
				if ( cantidad > 0 ) {
					this.getLogger().debug( "update car_id:" + carr_id + " nota:" + nota + " cantidad:" + cantidad );
					biz.carroComprasUpdateProducto(cliente_id, carr_id, cantidad, nota, null, null);
					idsProductos.add(""+idProducto);
				} else {
					this.getLogger().debug( "delete car_id:" + carr_id );
                    String msg = "";
                    try {
                        biz.carroComprasDeleteProducto(cliente_id, carr_id, null);
                        msg = "OK";
                    } catch (Exception e) {
                        msg = "El producto no pudo ser eliminado.";
                    }
                    arg1.setContentType("text/xml");
                    arg1.setHeader("Cache-Control", "no-cache");
                    arg1.setCharacterEncoding("UTF-8");
                    arg1.getWriter().write("<datos_objeto>");
                    arg1.getWriter().write("<mensaje>" + msg + "</mensaje>");
                    arg1.getWriter().write("</datos_objeto>");
                    return;                    
				}
			}
			
			if( arg0.getParameter("url") == null || arg0.getParameter("url").compareTo("")== 0 ) {
				// Recupera parámetro desde web.xml			    
			    if ( idsProductos.size() > 0 ) {
				    session.setAttribute("ids_prod_add", idsProductos);
				}
				String dis_ok =  getServletConfig().getInitParameter("dis_ok");
                if ( arg0.getParameter("pagina") != null && arg0.getParameter("paginado") != null ) {
                    dis_ok += "?pagina=" + arg0.getParameter("pagina") + "&paginado=" + arg0.getParameter("paginado"); 
                }                
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