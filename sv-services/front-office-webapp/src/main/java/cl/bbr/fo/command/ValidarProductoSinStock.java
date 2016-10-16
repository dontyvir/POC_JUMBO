package cl.bbr.fo.command;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.promo.lib.dto.ProductoStockDTO;

public class ValidarProductoSinStock extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		// TODO Apéndice de método generado automáticamente
		
		String mensajeSistema = "OK";
		BigDecimal cantidadMaxima = new BigDecimal(0);
		ProductoStockDTO producto = new ProductoStockDTO();
		try {
            BizDelegate biz = new BizDelegate();
            HttpSession session = arg0.getSession();
            long idProducto = Long.parseLong(arg0.getParameter("id_producto"));
            long idLocal = 0;
            if ( !"".equalsIgnoreCase( session.getAttribute("ses_loc_id").toString() ) ) {
                idLocal = Long.parseLong( session.getAttribute("ses_loc_id").toString() );
            }
            System.out.println(idLocal);
            producto = biz.productoTieneStockEnLocal(idProducto, idLocal);
            if(!producto.isTieneStock()) {
            	mensajeSistema = "Producto no disponible";
            }
//            if ( !biz.productoTieneStockEnLocal(idProducto, idLocal) ) {
//                mensajeSistema = "Producto no disponible";
//            }
        } catch (Exception e) {
            mensajeSistema = "Error al validar producto";
        }
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        
        //vemos cual es el mensaje a desplegar
        arg1.getWriter().write("<respuesta>");
        arg1.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        arg1.getWriter().write("<cantidadmaxima>" + producto.getCantidadMaxima() + "</cantidadmaxima>");
        arg1.getWriter().write("</respuesta>");

	}

}
