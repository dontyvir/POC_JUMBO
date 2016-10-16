package cl.bbr.fo.command.mobi;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.command.Command;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;

/**
 * Entrega el total en pesos de los productos que tiene el cliente en el carro
 * 
 * Pagina para mobile
 * 
 * @author imoyano
 * 
 */
public class TotalCarro extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {
		    // Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
            
            long totalProductoPedido = 0;
			
			List listaCarro = biz.carroComprasGetProductos( cliente_id.longValue(), session.getAttribute("ses_loc_id").toString(), null );
            long total = 0;
			long precio_total = 0;
			for (int i = 0; i < listaCarro.size(); i++) {
                CarroCompraDTO car = (CarroCompraDTO) listaCarro.get(i);
                if (car.getStock() > 0) {
                    precio_total = Utils.redondearFO(car.getCantidad() * car.getPrecio());
                    totalProductoPedido += Math.ceil(car.getCantidad());
                    total += precio_total;	
                }
			}           
            
            arg1.setContentType("text/xml");
            arg1.setHeader("Cache-Control", "no-cache");
            arg1.setCharacterEncoding("UTF-8");
            arg1.getWriter().write("<datos_objeto>");
            arg1.getWriter().write("<total>" + Formatos.formatoPrecioFO(total) + "</total>");
            arg1.getWriter().write("<total_sf>" + total + "</total_sf>");
            arg1.getWriter().write("<cant_prod>"+ totalProductoPedido +"</cant_prod>");            
            arg1.getWriter().write("</datos_objeto>");

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}


	}

}