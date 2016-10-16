package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.eventos.utils.EventosConstants;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Permite liberar un Evento
 * 
 * @author imoyano
 */

public class LiberaEvento extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo LiberaEvento Execute");
        
        String paramUrl = "";
        long idEvento 	= 0;

        String TplFile = getServletConfig().getInitParameter("TplFile");
        ForwardParameters fp = new ForwardParameters();
        fp.add(req.getParameterMap());        
        
        if (req.getParameter("id_evento") != null) {
            idEvento = Long.parseLong(req.getParameter("id_evento"));
        }
        paramUrl = req.getParameter("url");

        logger.debug("TplFile: " + TplFile);
        logger.debug("idEvento: " + idEvento);

        BizDelegate bizDelegate = new BizDelegate();
        
        if (bizDelegate.setLiberaEvento(idEvento)) {
//            logger.debug("Inicio del loggeo");
//            LogCasosDTO log = new LogCasosDTO(idCaso, caso.getEstado(), usr.getLogin(), "[BOC] " + CasosConstants.LOG_LIB_CASO);
//            bizDelegate.addLogCaso(log);
//            logger.debug("Fin del loggeo");
            fp.add("msje", EventosConstants.MSJ_LIB_EVENTO_EXITO);
        } else {
            logger.debug(EventosConstants.MSJ_LIB_EVENTO_ERROR + " (ID EVENTO:"+ idEvento + ")");
            fp.add("msje", EventosConstants.MSJ_LIB_EVENTO_ERROR);
        }                    
        paramUrl = TplFile + fp.forward();        
        res.sendRedirect(paramUrl);
        logger.debug("Fin LiberaEvento Execute");
    }
}
