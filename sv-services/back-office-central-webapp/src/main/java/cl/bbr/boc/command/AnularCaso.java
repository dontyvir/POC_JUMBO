package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.EstadoCasoDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Permite Anular un Caso
 * 
 * @author imoyano
 */

public class AnularCaso extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AnularCaso Execute");

        long idCaso 			= 0;
        String comentarioAnular = "";
        String paramUrl 		= getServletConfig().getInitParameter("TplFile");
        String indicaciones     = "";
        String indicacionesOld  = "";

        if (req.getParameter("id_caso") != null) {
            idCaso = Long.parseLong(req.getParameter("id_caso"));
        }
        if (req.getParameter("comentario_anular") != null) {
            comentarioAnular = req.getParameter("comentario_anular");
        }
        if (req.getParameter("indicaciones") != null) {
            indicaciones = req.getParameter("indicaciones");
        }
        if (req.getParameter("indicaciones_copia") != null) {
            indicacionesOld = req.getParameter("indicaciones_copia");
        }

        logger.debug("url: " + paramUrl);
        logger.debug("idCaso: " + idCaso);
        logger.debug("comentarioAnular:" + comentarioAnular);

        BizDelegate bizDelegate = new BizDelegate();

        CasoDTO caso = bizDelegate.getCasoByIdCaso(idCaso);
        EstadoCasoDTO estado = new EstadoCasoDTO();
        estado.setIdEstado(8);
        caso.setEstado(estado);
        //---caso.setFcResolucionFinal("");

        if (bizDelegate.modCaso(caso)) {
            logger.debug("Inicio del loggeo");
            if ( !indicaciones.equalsIgnoreCase(indicacionesOld) ) {
                LogCasosDTO log = new LogCasosDTO(idCaso, estado, usr.getLogin(), "[BOC] " + CasosConstants.LOG_MOD_IND_CASO + indicaciones);
                bizDelegate.addLogCaso(log);
            }
            LogCasosDTO log = new LogCasosDTO(idCaso, estado, usr.getLogin(), "[BOC] " + CasosConstants.LOG_ANULA_CASO + comentarioAnular);
            bizDelegate.addLogCaso(log);
            logger.debug("Fin del loggeo");
            paramUrl += "?msje=" + CasosConstants.MSJ_ANULAR_CASO_EXITO;
        } else {
            paramUrl += "?msje=" + CasosConstants.MSJ_ANULAR_CASO_ERROR;
        }
        
        logger.debug("Fin AnularCaso Execute");
        res.sendRedirect(paramUrl);        
    }
}
