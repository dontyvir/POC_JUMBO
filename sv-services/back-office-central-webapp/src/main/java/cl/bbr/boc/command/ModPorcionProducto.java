package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Modificar Porcion de Producto
 * 
 * @author imoyano
 */

public class ModPorcionProducto extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ModPorcionProducto");
        
        double porcion = Double.parseDouble( req.getParameter("porcion") );
        long idProducto = Integer.parseInt( req.getParameter("id_producto") );
        long unidadPorcion = Integer.parseInt( req.getParameter("unidad_porcion") );
        
        String mensajeSistema = "";
        BizDelegate biz = new BizDelegate();
        
        try {
            biz.modPorcionProducto( idProducto, porcion, unidadPorcion );
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

        logger.debug("Fin ModPorcionProducto");
    }
}
