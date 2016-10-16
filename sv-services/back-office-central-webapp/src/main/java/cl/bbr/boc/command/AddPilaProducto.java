package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.PilaNutricionalDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaProductoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar una Pila de un Producto
 * 
 * @author imoyano
 */

public class AddPilaProducto extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AddPilaProducto");
        
        //id_producto
        //nutriente
        //nutriente_porcion
        //porcentaje
        
        PilaProductoDTO pilaProd = new PilaProductoDTO();
        pilaProd.setIdProductoFO( Long.parseLong( req.getParameter("id_producto") ) );
        PilaNutricionalDTO pila = new PilaNutricionalDTO();
        pila.setIdPila( Long.parseLong( req.getParameter("nutriente") ) );
        pilaProd.setPila(pila);        
        pilaProd.setNutrientePorPorcion( Double.parseDouble(req.getParameter("nutriente_porcion")) );
        pilaProd.setPorcentaje( Double.parseDouble(req.getParameter("porcentaje")) );
        
        String mensajeSistema = "";
        BizDelegate biz = new BizDelegate();
        
        try {
            biz.addPilaProducto( pilaProd );
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

        logger.debug("Fin AddPilaProducto");
    }
}
