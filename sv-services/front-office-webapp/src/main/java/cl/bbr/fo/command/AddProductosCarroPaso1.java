package cl.bbr.fo.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;

/**
 * Agrega producto al carro de compras
 * 
 * IDEM OrderItemAdd pero con ajax
 *  
 * @author imoyano
 *  
 */
public class AddProductosCarroPaso1 extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
        String respuesta = "OK";
		try {
            String invitado_id = "";
            long cli_id = 0L;
            
            // Recupera la sesión del usuario
            HttpSession session = arg0.getSession();

            cli_id = Long.parseLong((String)session.getAttribute("ses_cli_id"));
            if (session.getAttribute("ses_invitado_id") != null &&
                    !session.getAttribute("ses_invitado_id").toString().equals("")){
                invitado_id = session.getAttribute("ses_invitado_id").toString();
            }

			
			if( arg0.getParameter("max_productos") == null ) {
				throw new CommandException( "Parámetro no encontrado: máximo de productos" );
			}
			long max_productos = Long.parseLong(arg0.getParameter("max_productos"));			
			
			// Instancia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			List lista_carro = new ArrayList();
			List idsProductos = new ArrayList();
			
			boolean hay_prod = false;
			CarroCompraDTO carro = null;
			for ( int i =0; i <= max_productos; i++){
			
				if (arg0.getParameter("id_prod_"+i) != null 
						&& arg0.getParameter("cantidad_l"+i) != null 
						&& Double.parseDouble(arg0.getParameter("cantidad_l"+i)) != 0 ){
					
					carro = new CarroCompraDTO();					
					carro.setPro_id(arg0.getParameter("id_prod_"+i)+"");
					carro.setCantidad(Double.parseDouble(arg0.getParameter("cantidad_l"+i)));
					if( arg0.getParameter("nota") != null )
						carro.setNota(arg0.getParameter("nota"));
					else if( arg0.getParameter("nota"+i) != null )
						carro.setNota(arg0.getParameter("nota"+i));
					else
						carro.setNota("");
					carro.setLugar_compra(arg0.getParameter( "lugar_compra"+i ));
					idsProductos.add(""+carro.getPro_id());
					lista_carro.add(carro);
					hay_prod = true;
				} else if (arg0.getParameter("id_prod_"+i) != null 
						&& arg0.getParameter("cantidad_l"+i) != null 
						&& Double.parseDouble(arg0.getParameter("cantidad_l"+i)) == 0 ) {
					biz.carroComprasDeleteProductoxId(cli_id,Long.parseLong(arg0.getParameter("id_prod_"+i)), invitado_id);
				}
			}
			
            //DONALD ver que se puedan agregar productos para clientes normales y q esto no pueda usarlo donald
			if ( hay_prod ) {
				biz.carroComprasInsertProducto(lista_carro, cli_id, invitado_id);
            }
			if ( idsProductos.size() > 0 ) {
			    session.setAttribute("ids_prod_add", idsProductos);
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
            arg1.getWriter().write("<datos_objeto>");
            arg1.getWriter().write("<respuesta>" + respuesta + "</respuesta>");
            arg1.getWriter().write("</datos_objeto>");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
	}
}