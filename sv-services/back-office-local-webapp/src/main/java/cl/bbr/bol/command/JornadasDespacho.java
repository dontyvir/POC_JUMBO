package cl.bbr.bol.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Devuelve las zonas de una comuna (Ajax)
 * 
 * @author imoyano
 */

public class JornadasDespacho extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo JornadasDespacho [AJAX]");
        
        int idZona = Integer.parseInt(req.getParameter("zona").toString());
        String fecha = req.getParameter("fecha");
        
       
        BizDelegate biz = new BizDelegate();
        List jornadas = new ArrayList();
        
        String tipoPedido = "";
        
        if ( req.getParameter("tipo_pedido") != null ) {
            tipoPedido = req.getParameter("tipo_pedido");
        }
    
        try {
            jornadas = biz.getJornadasDespachoDisponiblesByZona(idZona, fecha, tipoPedido);
        } catch (Exception e) {}
        
		res.setContentType("text/html");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("<option value='0'>Seleccionar</option>\n");
        for (int i=0; i < jornadas.size(); i++) {
            JornadaDTO jor = (JornadaDTO) jornadas.get(i);
            res.getWriter().write("<option value='" + jor.getId_jornada() + "'>" + Formatos.frmHoraSola( jor.getH_inicio() ) + " " + Formatos.frmHoraSola( jor.getH_fin() ) + "</option>\n");
        }
        logger.debug("Fin JornadasDespacho [AJAX]");
    }
}
