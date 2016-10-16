package cl.bbr.bol.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.clientes.dto.ComunasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Devuelve las comunas por region (Ajax)
 * 
 * @author imoyano
 */

public class ComunasByRegion extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ComunasByRegion [AJAX]");
        
        int idRegion = Integer.parseInt(req.getParameter("id_region").toString());
       
        BizDelegate biz = new BizDelegate();
        List comunas = new ArrayList();
    
        try {
            comunas = biz.getComunasByRegion(idRegion);
        } catch (Exception e) {}
        
		res.setContentType("text/html");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("<option value='0'>Seleccionar</option>\n");
        for (int i=0; i < comunas.size(); i++) {
            ComunasDTO comuna = (ComunasDTO) comunas.get(i);
            res.getWriter().write("<option value='" + comuna.getId_comuna() + "'>" + comuna.getNombre() + "</option>\n");
        }
        logger.debug("Fin ComunasByRegion [AJAX]");
    }
}
