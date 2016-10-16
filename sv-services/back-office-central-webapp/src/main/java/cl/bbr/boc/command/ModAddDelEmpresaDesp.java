package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.EmpresaTransporteDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar, Modificar y Eliminar un Responsable de Despachos
 * 
 * @author imoyano
 */

public class ModAddDelEmpresaDesp extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ModAddDelEmpresaDesp");
        
        String toDo = req.getParameter("do");
        EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
        String mensajeSistema = "";
        BizDelegate biz = new BizDelegate();
        
        try {
            if ( toDo.equalsIgnoreCase("DEL") ) {
                //BORRAR
                biz.delEmpresaTransporteById( Long.parseLong(req.getParameter("id_objeto")) );
                
            } else {
                emp.setNombre( req.getParameter("desc_objeto") );
                emp.setActivado( req.getParameter("activado") );
                
                if ( Long.parseLong(req.getParameter("id_objeto")) == 0 ) {
                    //CREAR 
                    biz.addEmpresaTransporte(emp);
                
                } else {
                    //MODIFICAR
                    emp.setIdEmpresaTransporte(Long.parseLong(req.getParameter("id_objeto")));
                    biz.modEmpresaTransporte(emp);
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

        logger.debug("Fin ModAddDelEmpresaDesp");
    }
}
