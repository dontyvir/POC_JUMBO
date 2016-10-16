package cl.bbr.boc.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Elimina un producto de un caso (Ajax)
 * 
 * @author imoyano
 */

public class DelProductoDeCaso extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo DelProductoDeCaso [AJAX]");
        
        String mensajeSistema = "";

        long idProducto 		= Long.parseLong(req.getParameter("id_producto").toString());
        long idCaso 			= Long.parseLong(req.getParameter("id_caso").toString());
        String nombreProducto 	= req.getParameter("nombre_prod");

        logger.debug("ID de Producto	: " + idProducto);
        logger.debug("ID de Caso		: " + idCaso);
        logger.debug("Nombre de Producto: " + nombreProducto);

        BizDelegate bizDelegate = new BizDelegate();

        try {
            if (bizDelegate.delProductoCaso(idProducto)) {
                mensajeSistema = CasosConstants.MSJ_DEL_PRODUCTO_EXITO;
                logger.debug("Inicio del loggeo");
                CasoDTO caso = bizDelegate.getCasoByIdCaso(idCaso);
                LogCasosDTO log = new LogCasosDTO(caso.getIdCaso(), caso.getEstado(), usr.getLogin(), "[BOC] " + CasosConstants.LOG_DEL_PRD_CASO + " '" + nombreProducto + "'");
                bizDelegate.addLogCaso(log);
                logger.debug("Fin del loggeo");
            } else {
                mensajeSistema = CasosConstants.MSJ_DEL_PRODUCTO_ERROR;
            }

        } catch (BocException e) {
            e.printStackTrace();
            mensajeSistema = CasosConstants.MSJ_DEL_PRODUCTO_ERROR;
        }

        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");

        res.getWriter().write("<respuesta_sistema>");
        res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        res.getWriter().write("</respuesta_sistema>");

        logger.debug("Fin DelProductoDeCaso [AJAX]");
    }
}
