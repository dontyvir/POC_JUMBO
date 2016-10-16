package cl.bbr.bol.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Devuelve las zonas de una comuna (Ajax)
 * 
 * @author imoyano
 */

public class ZonasByComuna extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ZonasByComuna [AJAX]");
        
        int idComuna = Integer.parseInt(req.getParameter("id_comuna").toString());
       
        BizDelegate biz = new BizDelegate();
        List zonas = new ArrayList();
    
        try {
            zonas = biz.getZonasByComuna(idComuna);
        } catch (Exception e) {}
        
		res.setContentType("text/html");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("<option value='0'>Seleccionar</option>\n");
        for (int i=0; i < zonas.size(); i++) {
            ZonaDTO zona = (ZonaDTO) zonas.get(i);
            res.getWriter().write("<option value='" + zona.getId_zona() + "'>" + zona.getNombre() + " (" + zona.getDescripcion() + ")</option>\n");
        }
        logger.debug("Fin ZonasByComuna [AJAX]");
    }
}
