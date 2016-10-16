package cl.bbr.bol.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.EmpresaTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.PatenteTransporteDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Modificar datos de Patente de Transporte
 * 
 * @author imoyano
 */

public class ModAddDelPatenteTrans extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ModAddDelPatenteTrans [AJAX]");
        
        String toDo = req.getParameter("do");
        PatenteTransporteDTO pat = new PatenteTransporteDTO();
        String mensajeSistema = "";
        BizDelegate biz = new BizDelegate();
        
        try {
            if ( toDo.equalsIgnoreCase("DEL") ) {
                //BORRAR
                biz.delPatenteTransporteById( Long.parseLong(req.getParameter("id_objeto")) );
                biz.addLogPatenteTransporte(Long.parseLong(req.getParameter("id_objeto")), "[BOL] Eliminación de Patente", usr.getId_usuario());
                
            } else {
                pat.setPatente( req.getParameter("patente") );
                pat.setActivado( req.getParameter("activado") );
                pat.setCantMaxBins( Long.parseLong(req.getParameter("bins")) );
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte( Long.parseLong(req.getParameter("id_empresa")) );
                pat.setEmpresaTransporte(emp);
                pat.setIdLocal(usr.getId_local());
                if ( Long.parseLong(req.getParameter("id_patente")) == 0 ) {
                    //CREAR 
                    long id = biz.addPatenteTransporte(pat);
                    biz.addLogPatenteTransporte(id, "[BOL] Creación de Patente", usr.getId_usuario());
                
                } else {
                    //MODIFICAR
                    pat.setIdPatente(Long.parseLong(req.getParameter("id_patente")));
                    biz.modPatenteTransporte(pat);
                    biz.addLogPatenteTransporte(Long.parseLong(req.getParameter("id_patente")), "[BOL] Modificación de Patente", usr.getId_usuario());
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
		
        logger.debug("Fin ModAddDelPatenteTrans [AJAX]");
    }
}
