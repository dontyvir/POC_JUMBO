package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.EstadoCasoDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Modifica datos del caso
 * 
 * @author imoyano
 */
public class ModificaCaso extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ModificaCaso Execute");

        String paramUrl 	= getServletConfig().getInitParameter("TplFile");
        long idCaso 		= 0;
        String selEstado 	= "";
        String comentarioBol = "Comentario ingresado: ";
        String satisfaccionCliente = "";

        if (req.getParameter("id_caso") != null) {
            idCaso = Long.parseLong(req.getParameter("id_caso"));
        }
        if (req.getParameter("sel_estados") != null) {
            selEstado = req.getParameter("sel_estados");
        }
        if (req.getParameter("comentario_log") != null) {
            comentarioBol += req.getParameter("comentario_log");
        }
        if (req.getParameter("satisfaccion_cliente") != null) {
            satisfaccionCliente += req.getParameter("satisfaccion_cliente");
        }

        logger.debug("url: " + paramUrl);
        logger.debug("idCaso: " + idCaso);
        logger.debug("selEstado:" + selEstado);
        logger.debug("comentarioBol ingresado:" + comentarioBol);
        logger.debug("satisfaccionCliente:" + satisfaccionCliente);

        BizDelegate bizDelegate = new BizDelegate();

        CasoDTO caso = bizDelegate.getCasoByIdCaso(idCaso);
        EstadoCasoDTO estado = new EstadoCasoDTO();
        estado.setIdEstado(Long.parseLong(selEstado));
        caso.setEstado(estado);
        caso.setSatisfaccionCliente(satisfaccionCliente);

        if (bizDelegate.modCaso(caso)) {
            logger.debug("Inicio del loggeo");
            LogCasosDTO log1 = new LogCasosDTO(idCaso, estado, usr.getLogin(),"[BOL] " + CasosConstants.LOG_MOD_FIN_CASO + comentarioBol);
            bizDelegate.addLogCaso(log1);
            logger.debug("Fin del loggeo");

            //una vez modificada, se libera el caso
            if (bizDelegate.setLiberaCaso(idCaso)) {
                logger.debug("Inicio del loggeo");
                LogCasosDTO log2 = new LogCasosDTO(idCaso, estado, usr.getLogin(), "[BOL] " + CasosConstants.LOG_LIB_CASO);
                bizDelegate.addLogCaso(log2);
                logger.debug("Fin del loggeo");
            } else {
                logger.error("No se pudo liberar el caso: " + idCaso);
            }
            paramUrl += "?msje=" + CasosConstants.MSJ_MOD_CASO_EXITO;

        } else {
            paramUrl += "?msje=" + CasosConstants.MSJ_MOD_CASO_ERROR;
        }
        
        logger.debug("Fin ModificaCaso Execute");
        res.sendRedirect(paramUrl);
    }
}
