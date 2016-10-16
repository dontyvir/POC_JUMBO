package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar la cantidad de productos
 * 
 * @author bbr
 *  
 */
public class ModCantidadProductos extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res,
            UserDTO usr) throws Exception {

        String urlDestino = getServletConfig().getInitParameter("UrlDestino");

        if (req.getParameter("id_pedido") == null) {
            throw new ParametroObligatorioException("id_pedido es nulo");
        }
        if (req.getParameter("id_detalle") == null) {
            throw new ParametroObligatorioException("id_detalle es nulo");
        }
        long idPedido = Long.parseLong(req.getParameter("id_pedido"));
        long idDetallePedido = Long.parseLong(req.getParameter("id_detalle"));
        String descProducto = req.getParameter("desc_producto");
        
        double cantidad = 0;

        if (req.getParameter("todos") != null) {
            cantidad = 0;
        } else {
            if (req.getParameter("cant_ped") != null) {
                cantidad = Double.parseDouble(req.getParameter("cant_ped"));
            }
        }

        ForwardParameters fp = new ForwardParameters();
        fp.add(req.getParameterMap());

        BizDelegate biz = new BizDelegate();

        try {
            String msjLog = "";
            biz.modProductoDePedido(idPedido, idDetallePedido, cantidad );
            
            if ( cantidad == 0 ) {
                fp.add("msj", "Producto eliminado del listado");
                msjLog = "Se ha eliminado del pedido el producto: ";                
            } else {
                fp.add("msj", "Cambio realizado exitosamente");   
                msjLog = "Se ha asignado la cantidad de " + cantidad + " al producto: ";
            }
            msjLog += descProducto;
            if ( msjLog.length() > 254 ) {
                msjLog = msjLog.substring(0,254);
            }
            
            //Guardamos en el log de pedido el cambio
            LogPedidoDTO log = new LogPedidoDTO();
            log.setUsuario(usr.getLogin());
            log.setId_pedido(idPedido);
            log.setLog(msjLog);
            biz.addLogPedido(log);
            
        } catch (BocException e) {
            fp.add("msj", "Error al modificar la cantidad del producto");
        }
        res.sendRedirect(urlDestino + fp.forward());
    }
}
