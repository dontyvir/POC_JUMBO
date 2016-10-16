package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar, Modificar y Eliminar un Motivo de Despachos
 * 
 * @author imoyano
 */

public class ModAddDelMotivoDesp extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ModAddDelMotivoDesp");
        
        String toDo = req.getParameter("do");
        ObjetoDTO motivo = new ObjetoDTO();
        String mensajeSistema = "";
        BizDelegate biz = new BizDelegate();
        
        try {
            if ( toDo.equalsIgnoreCase("DEL") ) {
                //BORRAR
                biz.delMotivoNCById( Long.parseLong(req.getParameter("id_objeto")) );
                
            } else {
                motivo.setNombre( req.getParameter("desc_objeto") );
                motivo.setActivado( req.getParameter("activado") );
                
                if ( Long.parseLong(req.getParameter("id_objeto")) == 0 ) {
                    //CREAR 
                    biz.addMotivoNC(motivo);
                
                } else {
                    //MODIFICAR
                    motivo.setIdObjeto(Long.parseLong(req.getParameter("id_objeto")));
                    biz.modMotivoNC(motivo);
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
        

        logger.debug("Fin ModAddDelMotivoDesp");
    }
}
