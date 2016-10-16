package cl.bbr.boc.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.eventos.utils.EventosConstants;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Utilizado para verificar si existe el evento (Ajax)
 * 
 * @author imoyano
 */

public class ValidaEvento extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ValidaEvento [AJAX]");
        
        String respuestaAjax = "";

        long idEvento = Long.parseLong(req.getParameter("id_evento").toString());
        logger.debug("idEvento: " + idEvento);
        String nombreEvento = req.getParameter("nombre_evento").toString();
        logger.debug("nombreEvento: " + nombreEvento);

        BizDelegate biz = new BizDelegate();
        EventoDTO evento = new EventoDTO();
        evento.setIdEvento(idEvento);
        evento.setNombre(nombreEvento);        
        
        logger.debug("Antes de crear o modificar el evento verficamos que no exista.");
		if (biz.existeEvento(evento)) {
            logger.debug("Evento a crear ya existe.");
            respuestaAjax = EventosConstants.MSJ_ADD_EVENTO_EXISTE;
        } else {
            respuestaAjax = "OK";
        }        
        
        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");

        res.getWriter().write("<datos_evento>");
        res.getWriter().write("<mensaje>" + respuestaAjax + "</mensaje>");
        res.getWriter().write("</datos_evento>");

        logger.debug("Fin ValidaEvento [AJAX]");
    }
}
