package cl.bbr.boc.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.JornadaDTO;
import cl.bbr.jumbocl.casos.dto.QuiebreCasoDTO;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Retorna los datos de un pedido (Ajax)
 * 
 * @author imoyano
 */

public class DatosCasoConfig extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo DatosCasoConfig [AJAX]");
        
        List datos = new ArrayList();

        String tipo = req.getParameter("idPedido").toString();
        logger.debug("Tipo: " + tipo);

        BizDelegate bizDelegate = new BizDelegate();
        CasosUtil util = new CasosUtil();

        if (tipo.equalsIgnoreCase("Q")) {
	        // ---- Tipos de Quiebre ----
	        datos = bizDelegate.getQuiebres();
	        
        } else if (tipo.equalsIgnoreCase("R")) {
            // ---- Responsables --------
            datos = bizDelegate.getResponsables();
            
        } else if (tipo.equalsIgnoreCase("J")) {
            // ---- Jornada --------
            datos = bizDelegate.getJornadas();
            
        }

        res.setContentType("text/xml"); //("application/x-www-form-urlencoded");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8"); //("UTF-8");

        res.getWriter().write("<datos_objeto>");
        
        if (tipo.equalsIgnoreCase("Q")) {
	        // ---- Tipos de Quiebre ----
            for (int i = 0; i < datos.size(); i++) {
                QuiebreCasoDTO obj = (QuiebreCasoDTO) datos.get(i);
                res.getWriter().write("<objeto>");
                res.getWriter().write("<id_objeto>" + obj.getIdQuiebre() + "</id_pedido>");
                res.getWriter().write("<descripcion>" + util.frmTextoXml(obj.getNombre()) + "</descripcion>");
    	        res.getWriter().write("<puntaje>" + obj.getPuntaje() + "</puntaje>");
    	        res.getWriter().write("</objeto>");
            }
            
        } else if (tipo.equalsIgnoreCase("R")) {
            // ---- Responsables --------
            for (int i = 0; i < datos.size(); i++) {
                ObjetoDTO obj = (ObjetoDTO) datos.get(i);
                res.getWriter().write("<objeto>");
                res.getWriter().write("<id_objeto>" + obj.getIdObjeto() + "</id_pedido>");
                res.getWriter().write("<descripcion>" + util.frmTextoXml(obj.getNombre()) + "</descripcion>");
    	        res.getWriter().write("<puntaje>0</puntaje>");
    	        res.getWriter().write("</objeto>");
            }
            
        } else if (tipo.equalsIgnoreCase("J")) {
            // ---- Jornada --------
            for (int i = 0; i < datos.size(); i++) {
                JornadaDTO obj = (JornadaDTO) datos.get(i);
                res.getWriter().write("<objeto>");
                res.getWriter().write("<id_objeto>" + obj.getIdJornada() + "</id_pedido>");
                res.getWriter().write("<descripcion>" + util.frmTextoXml(obj.getDescripcion()) + "</descripcion>");
    	        res.getWriter().write("<puntaje>0</puntaje>");
    	        res.getWriter().write("</objeto>");
            }            
        
        }
        res.getWriter().write("</datos_objeto>");
        logger.debug("Fin DatosCasoConfig [AJAX]");
    }
}
