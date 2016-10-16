package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar, Modificar y Eliminar un Responsable de Despachos
 * 
 * @author imoyano
 */

public class ModAddDelResponsableDesp extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ModAddDelResponsableDesp");
        
        String toDo = req.getParameter("do");
        ObjetoDTO responsable = new ObjetoDTO();
        String mensajeSistema = "";
        BizDelegate biz = new BizDelegate();
        
        try {
            if ( toDo.equalsIgnoreCase("DEL") ) {
                //BORRAR
                biz.delResponsableNCById( Long.parseLong(req.getParameter("id_objeto")) );
                
            } else {
                responsable.setNombre( req.getParameter("desc_objeto") );
                responsable.setActivado( req.getParameter("activado") );
                
                if ( Long.parseLong(req.getParameter("id_objeto")) == 0 ) {
                    //CREAR 
                    biz.addResponsableNC(responsable);
                
                } else {
                    //MODIFICAR
                    responsable.setIdObjeto(Long.parseLong(req.getParameter("id_objeto")));
                    biz.modResponsableNC(responsable);
                }
                
            }
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

        logger.debug("Fin ModAddDelResponsableDesp");
    }
}
