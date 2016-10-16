package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Elimina una Pila de un Producto
 * 
 * @author imoyano
 */

public class DelPilaProducto extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo DelPilaProducto");
        
        long idProducto = Long.parseLong( req.getParameter("id_producto") );
        long idPila = Long.parseLong( req.getParameter("id_pila") );
        String mensajeSistema = "";
        BizDelegate biz = new BizDelegate();
        
        try {
            biz.delPilaProducto(idProducto, idPila);
            mensajeSistema = "OK";
        
        } catch (Exception e) {
            mensajeSistema = "Ocurrió un problema, por favor intente más tarde.";
        }

        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("<datos_objeto>");
        res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        res.getWriter().write("</datos_objeto>");            

        logger.debug("Fin DelPilaProducto");
    }
}
