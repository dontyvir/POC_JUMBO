package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.dto.ProductoCasoDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Permite Cambia a Envio de Dinero un producto de un Caso
 * 
 * @author imoyano
 */

public class CambiaEnvioDineroEnCaso extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo CambiaEnvioDineroEnCaso Execute");

        long idCaso 			= 0;
        long idProducto         = 0;
        String mensajeSistema   = "OK";
        String producto         = "";
       
        if (req.getParameter("id_caso") != null) {
            idCaso = Long.parseLong(req.getParameter("id_caso"));
        }
        if (req.getParameter("id_producto") != null) {
            idProducto = Long.parseLong(req.getParameter("id_producto"));
        }
        if (req.getParameter("producto") != null) {
            producto = req.getParameter("producto");
        }

        logger.debug("idCaso: " + idCaso);
        logger.debug("idProducto: " + idProducto);

        BizDelegate bizDelegate = new BizDelegate();
        
        CasoDTO caso = bizDelegate.getCasoByIdCaso(idCaso);

        ProductoCasoDTO prod = bizDelegate.getProductoById(idProducto);
        prod.setTipoAccion("P");
        
        if (bizDelegate.modProductoCaso(prod)) {
            logger.debug("Inicio del loggeo");
            LogCasosDTO log = new LogCasosDTO(idCaso, caso.getEstado(), usr.getLogin(), "[BOC] " + CasosConstants.MSJ_MOD_ENVIO + " (" + producto + ")");
            bizDelegate.addLogCaso(log);
            logger.debug("Fin del loggeo");            
        } else {
            mensajeSistema = CasosConstants.MSJ_ADD_PRODUCTO_ERROR;
        }
        
        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");

        res.getWriter().write("<respuesta>");
        res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        res.getWriter().write("</respuesta>");
        
        logger.debug("Fin CambiaEnvioDineroEnCaso Execute");
             
    }
}
