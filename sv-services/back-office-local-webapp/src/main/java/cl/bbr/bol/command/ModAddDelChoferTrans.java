package cl.bbr.bol.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.ChoferTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.EmpresaTransporteDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Modificar datos de Chofer de Transporte
 * 
 * @author imoyano
 */

public class ModAddDelChoferTrans extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ModAddDelChoferTrans [AJAX]");
        
        String toDo = req.getParameter("do");
        ChoferTransporteDTO chofer = new ChoferTransporteDTO();
        String mensajeSistema = "";
        BizDelegate biz = new BizDelegate();
        
        try {
            if ( toDo.equalsIgnoreCase("DEL") ) {
                //BORRAR
                biz.delChoferTransporteById( Long.parseLong(req.getParameter("id_objeto")) );
                biz.addLogChoferTransporte(Long.parseLong(req.getParameter("id_objeto")), "[BOL] Eliminación de Chofer", usr.getId_usuario());
                
            } else {
                chofer.setNombre( req.getParameter("nombre") );
                chofer.setRut( Long.parseLong(req.getParameter("rut")) );
                chofer.setDv( req.getParameter("dv") );
                chofer.setActivado( req.getParameter("activado") );
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte( Long.parseLong(req.getParameter("id_empresa")) );
                chofer.setEmpresaTransporte(emp);
                chofer.setIdLocal(usr.getId_local());
                if ( Long.parseLong(req.getParameter("id_chofer")) == 0 ) {
                    //CREAR 
                    long id = biz.addChoferTransporte(chofer);
                    biz.addLogChoferTransporte(id, "[BOL] Creación de Chofer", usr.getId_usuario());
                
                } else {
                    //MODIFICAR
                    chofer.setIdChofer(Long.parseLong(req.getParameter("id_chofer")));
                    biz.modChoferTransporte(chofer);
                    biz.addLogChoferTransporte(Long.parseLong(req.getParameter("id_chofer")), "[BOL] Modificación de Chofer", usr.getId_usuario());
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
		
        logger.debug("Fin ModAddDelChoferTrans [AJAX]");
    }
}
