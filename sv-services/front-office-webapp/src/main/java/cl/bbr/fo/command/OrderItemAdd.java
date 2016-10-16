package cl.bbr.fo.command;

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
 * @author BBR e-commerce & retail
 *  
 */
public class OrderItemAdd extends Command {

	/**
	 * Despliega productos en el carro
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws CommandException 
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
		try {

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
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
					biz.carroComprasDeleteProductoxId(Long.parseLong(session.getAttribute("ses_cli_id").toString()),Long.parseLong(arg0.getParameter("id_prod_"+i)), null);
				}
			}
			
            //DONALD ver que se puedan agregar productos para clientes normales y q esto no pueda usarlo donald
			if ( hay_prod ) {
				biz.carroComprasInsertProducto(lista_carro, Long.parseLong(session.getAttribute("ses_cli_id").toString()), null );	
            }
			// Recupera parámetro desde web.xml
			String dis_ok =  getServletConfig().getInitParameter("dis_ok");
			if ( idsProductos.size() > 0 ) {
			    session.setAttribute("ids_prod_add", idsProductos);
			}
			arg1.sendRedirect( dis_ok );
			

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}

	}

}