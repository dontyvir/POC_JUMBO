package cl.bbr.vte.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.vte.bizdelegate.BizDelegate;

/**
 * Crea el pedido en estado pre-ingresado, para ser pagado con webpay o cat
 *  
 * @author imoyano
 *  
 */
public class OrderCreate extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

        System.out.println("******* OrderCreate (VE) *******");
        String mensaje = "OK";
        PedidoDTO newPedido = new PedidoDTO();
        HttpSession session = arg0.getSession();
        
        long idPedido = 0;
        
        try {
            idPedido = Long.parseLong(arg0.getParameter("id_pedido"));
            BizDelegate biz = new BizDelegate();
            biz.actualizaSecuenciaPago(idPedido);
            newPedido = biz.getPedidoById(idPedido);
            
            session.setAttribute("ses_id_pedido", idPedido + "" );
            
        } catch (Exception e) {
            mensaje = "Error al intentar pagar su pedido";
        }
        
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        arg1.getWriter().write("<datos_objeto>");
        arg1.getWriter().write("<respuesta>" + mensaje + "</respuesta>");
        arg1.getWriter().write("<id_pedido>" + idPedido + newPedido.getSecuenciaPago() + "</id_pedido>");
        //Se envian dos ceros al final, ya que TBK_MONTO asume los dos ultimos digitos como valores decimales
        arg1.getWriter().write("<monto>" + new Double(newPedido.getMonto_reservado()).longValue() + "00</monto>");
        arg1.getWriter().write("<medio_pago>" + newPedido.getMedio_pago() + "</medio_pago>");
        arg1.getWriter().write("</datos_objeto>");
        return;
    }
}
