package cl.bbr.boc.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Para indicar que se dio aviso de la anulacion de boleta al local (Ajax)
 * 
 * @author imoyano
 */

public class LiberaAnularBoleta extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res,
            UserDTO usr) throws Exception {
        String mensajeSistema = "OK";
        BizDelegate bizDelegate = new BizDelegate();
        long idPedido = 0;

        try {
            if (req.getParameter("id_pedido") != null) {
                idPedido = Long.parseLong(req.getParameter("id_pedido").toString());
            }
            bizDelegate.marcaAnulacionBoletaEnLocal(idPedido, false);
            
            LogPedidoDTO log = new LogPedidoDTO();
            log.setUsuario(usr.getLogin());
            log.setId_pedido(idPedido);
            log.setId_motivo(1);
            log.setLog("El usuario confirma la anulación de la boleta en local");
            bizDelegate.addLogPedido(log);

        } catch (BocException e) {
            mensajeSistema = "Error al desmarcar la anulacion de boleta en el local. " + e.getMessage();

        }
        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");

        res.getWriter().write("<datos_objeto>");
        res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        res.getWriter().write("</datos_objeto>");
    }
}
