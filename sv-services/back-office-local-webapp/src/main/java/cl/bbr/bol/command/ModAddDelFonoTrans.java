package cl.bbr.bol.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.EmpresaTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.FonoTransporteDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Modificar datos de Fono de Transporte
 * 
 * @author imoyano
 */

public class ModAddDelFonoTrans extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ModAddDelFonoTrans [AJAX]");
        
        String toDo = req.getParameter("do");
        FonoTransporteDTO fono = new FonoTransporteDTO();
        String mensajeSistema = "";
        BizDelegate biz = new BizDelegate();
        
        try {
            if ( toDo.equalsIgnoreCase("DEL") ) {
                //BORRAR
                biz.delFonoTransporteById( Long.parseLong(req.getParameter("id_objeto")) );
                biz.addLogFonoTransporte(Long.parseLong(req.getParameter("id_objeto")), "[BOL] Eliminación de Fono", usr.getId_usuario());
                
            } else {
                fono.setCodigo( Long.parseLong(req.getParameter("codigo")) );
                fono.setNumero( Long.parseLong(req.getParameter("numero")) );
                fono.setNombre( req.getParameter("nombre") );
                fono.setActivado( req.getParameter("activado") );
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte( Long.parseLong(req.getParameter("id_empresa")) );
                fono.setEmpresaTransporte(emp);
                fono.setIdLocal(usr.getId_local());
                if ( Long.parseLong(req.getParameter("id_fono")) == 0 ) {
                    //CREAR 
                    long id = biz.addFonoTransporte(fono);
                    biz.addLogFonoTransporte(id, "[BOL] Creación de Fono", usr.getId_usuario());
                
                } else {
                    //MODIFICAR
                    fono.setIdFono(Long.parseLong(req.getParameter("id_fono")));
                    biz.modFonoTransporte(fono);
                    biz.addLogFonoTransporte(Long.parseLong(req.getParameter("id_fono")), "[BOL] Modificación de Fono", usr.getId_usuario());
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
		
        logger.debug("Fin ModAddDelFonoTrans [AJAX]");
    }
}
