package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Permite cambiar un pedido que esta finalizado a en despacho
 * Solo desde el monitor de despacho
 * 
 * @author imoyano
 */

public class CambiaEstadoPedido extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo CambiaEstadoPedido Execute");
        
        String msg = "OK";
        BizDelegate biz = new BizDelegate();
        
        long idRuta = Long.parseLong(req.getParameter("id_ruta"));
        long idPedido = Long.parseLong(req.getParameter("id_pedido"));
        
        PedidoDTO ped = biz.getPedidosById(idPedido);
        
        if ( ped.getId_estado() != Constantes.ID_ESTAD_PEDIDO_FINALIZADO ) {
            msg = "Estado del pedido es incorrecto";
        } else {
            biz.setCambiaEstadoDespacho(idPedido, Constantes.ID_ESTAD_PEDIDO_EN_DESPACHO, usr.getLogin(), "[BOC] Se cambia estado de OP en forma manual. ");
            
            RutaDTO ruta = biz.getRutaById(idRuta);            
            LogRutaDTO log1 = new LogRutaDTO(idRuta, ruta.getEstado(), usr, "[BOC] OP " + idPedido + " queda 'En despacho' (cambio manual).");
            biz.addLogRuta( log1 );
        }
        
        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
        
        res.getWriter().write("<datos_trx>");
        res.getWriter().write("<mensaje>" + msg + "</mensaje>");
        res.getWriter().write("</datos_trx>");
        
        logger.debug("Fin CambiaEstadoPedido Execute");
    }
}
