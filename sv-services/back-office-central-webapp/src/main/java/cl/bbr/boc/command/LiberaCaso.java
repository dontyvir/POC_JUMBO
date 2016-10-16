package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Permite liberar un Caso
 * 
 * @author imoyano
 */

public class LiberaCaso extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo LiberaCaso Execute");
        
        String paramUrl = "";
        long idCaso 	= 0;

        if (req.getParameter("url") == null) {
            throw new ParametroObligatorioException("url es null");
        }
        if (req.getParameter("id_caso") != null) {
            idCaso = Long.parseLong(req.getParameter("id_caso"));
        }
        paramUrl = req.getParameter("url");

        logger.debug("url: " + paramUrl);
        logger.debug("idCaso: " + idCaso);

        BizDelegate bizDelegate = new BizDelegate();
        CasoDTO caso = bizDelegate.getCasoByIdCaso(idCaso);

        if (bizDelegate.setLiberaCaso(idCaso)) {
            logger.debug("Inicio del loggeo");
            LogCasosDTO log = new LogCasosDTO(idCaso, caso.getEstado(), usr.getLogin(), "[BOC] " + CasosConstants.LOG_LIB_CASO);
            bizDelegate.addLogCaso(log);
            logger.debug("Fin del loggeo");
            paramUrl += "?msje=" + CasosConstants.MSJ_LIB_CASO_EXITO;
        } else {
            logger.debug(CasosConstants.MSJ_LIB_CASO_ERROR + " (ID CASO :"+ idCaso + ")");
        }
        res.sendRedirect(paramUrl);
        
        logger.debug("Fin LiberaCaso Execute");
    }
}
