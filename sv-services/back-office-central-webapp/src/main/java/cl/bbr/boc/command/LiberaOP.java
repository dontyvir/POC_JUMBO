package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.AsignaOPDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * LiberaOP Comando Process comando que libera una op
 * 
 * @author BBRI
 */

public class LiberaOP extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res,
            UserDTO usr) throws Exception {
        
        String url = "";
        String paramUrl = "";
        long paramId_op = 0L;
        long paramId_motivo = 0L;
        String paramObs = "";
        int origen = 1;

        logger.debug("Procesando parámetros...");
        String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
        logger.debug("mensaje_fracaso: " + mensaje_fracaso);
        String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito");
        logger.debug("mensaje_exito: " + mensaje_exito);
        String paramHeader = getServletConfig().getInitParameter("headerlog");
        logger.debug("headerlog: " + paramHeader);

        if (req.getParameter("url") == null) {
            throw new ParametroObligatorioException("url es null");
        }
        if (req.getParameter("id_motivo") == null) {
            throw new ParametroObligatorioException("id_motivo es nulo");
        }
        if (req.getParameter("obs") == null) {
            throw new ParametroObligatorioException("obs es nulo");
        }
        if (req.getParameter("origen") == null) {
            throw new ParametroObligatorioException("origen es nulo");
        }
        paramId_op = usr.getId_pedido(); // Se saca el id de OP de la sesion del usuario
        
        paramUrl = req.getParameter("url");
        paramId_motivo = Long.parseLong(req.getParameter("id_motivo"));
        paramObs = req.getParameter("obs");
        origen = Integer.parseInt(req.getParameter("origen"));

        logger.debug("url: " + paramUrl);
        logger.debug("id_pedido: " + paramId_op);

        /*
         * 3. Procesamiento Principal Obtiene el id_usuario actualmente
         * conectado. Revisa si la op esta asignada a un usuario - si no esta
         * asignada no hace nada - si esta asignada nulifica el id_usuario
         * asignado si es equivalente al usuario conectado - respalda en el log
         * de la OP la liberación.
         */
        BizDelegate bizDelegate = new BizDelegate();
        AsignaOPDTO coll = new AsignaOPDTO();

        coll.setId_pedido(paramId_op);
        coll.setId_usuario(usr.getId_usuario());
        coll.setId_motivo(paramId_motivo);

        if (bizDelegate.setLiberaOP(coll) == true) {
            usr.setId_pedido(0);
            logger.debug("OP Liberada :" + paramId_op);
            //escribe en el log la liberacion
            LogPedidoDTO log = new LogPedidoDTO();
            log.setUsuario(usr.getLogin());
            log.setId_pedido(paramId_op);
            log.setId_motivo(paramId_motivo);
            log.setLog(paramHeader + " : " + paramObs);
            bizDelegate.addLogPedido(log);
            if ( origen == Constantes.LIBERA_OP_ORIGEN_FORM_PEDIDO ) {
                url = paramUrl + "&id_motivo=" + paramId_motivo + "&obs=" + paramObs + "&mensaje=" + mensaje_exito + "&ret=1";
            } else if ( origen == Constantes.LIBERA_OP_ORIGEN_MONITOR_PEDIDO ) {
                url = paramUrl + "?id_motivo=" + paramId_motivo + "&obs=" + paramObs + "&mensaje=" + mensaje_exito + "&ret=1";
            } else if ( origen == Constantes.LIBERA_OP_ORIGEN_DET_RUTA ) {
                url = paramUrl + "&mensaje_error=Pedido Liberado";
            } else if ( origen == Constantes.LIBERA_OP_ORIGEN_OP_PENDIENTES) {
                url = paramUrl + "?mensaje_error=Pedido Liberado";
            }
        } else {
            logger.debug("OP no Liberada :" + paramId_op);
            if ( origen == Constantes.LIBERA_OP_ORIGEN_FORM_PEDIDO) {
                url = paramUrl + "&id_motivo=" + paramId_motivo + "&obs=" + paramObs + "&mensaje=" + mensaje_fracaso + "&ret=0";
            } else if ( origen == Constantes.LIBERA_OP_ORIGEN_MONITOR_PEDIDO) {
                url = paramUrl + "?id_motivo=" + paramId_motivo + "&obs=" + paramObs + "&mensaje=" + mensaje_fracaso + "&ret=0";
            } else if ( origen == Constantes.LIBERA_OP_ORIGEN_DET_RUTA ) {
                url = paramUrl + "&mensaje_error=Pedido no pudo ser liberado";
            } else if ( origen == Constantes.LIBERA_OP_ORIGEN_OP_PENDIENTES) {
                url = paramUrl + "?mensaje_error=Pedido no pudo ser liberado";
            }
        }
        
        res.sendRedirect(url);
    }
}
