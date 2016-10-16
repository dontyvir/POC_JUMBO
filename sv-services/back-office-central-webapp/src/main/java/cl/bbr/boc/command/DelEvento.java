package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.eventos.utils.EventosConstants;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Eliminar un evento
 * 
 * @author imoyano
 */

public class DelEvento extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo DelEvento Execute");

        String paramUrl = "";
        String TplFile = getServletConfig().getInitParameter("TplFile");
        
        String filtroTipoEvento	= "";
        String filtroEstado 	= "";
        String filtroFechaIni 	= "";
        String filtroFechaFin 	= "";        
        
        if (req.getParameter("sel_tipo_evento") != null) {
	        filtroTipoEvento = req.getParameter("sel_tipo_evento").toString();
	    }
	    if (req.getParameter("sel_est") != null) {
	        filtroEstado = req.getParameter("sel_est").toString();
	    }
	    if (req.getParameter("fc_ini") != null) {
	        filtroFechaIni = req.getParameter("fc_ini").toString();
	    }
	    if (req.getParameter("fc_fin") != null) {
	        filtroFechaFin = req.getParameter("fc_fin").toString();
	    }        
        
        BizDelegate biz = new BizDelegate();
        ForwardParameters fp = new ForwardParameters();
        fp.add(req.getParameterMap());

        try {
            EventoDTO evento = new EventoDTO();
            evento.setIdEvento(Long.parseLong( req.getParameter("id_evento") ));
            
            logger.debug("Validamos que el evento no haya sido usado ");
            if (biz.eventoUtilizado(evento.getIdEvento())) {
                logger.error("No se puede eliminar el evento, ya que fue usado por algún cliente.");
                res.sendRedirect( cmd_dsp_error +"?mensaje=" + EventosConstants.MSJ_DEL_EVENTO_ERROR_UTILIZADO + "&PagErr=1");
                return;
            }
            
            logger.debug("Validamos que el evento no se encuentre tomado ");
            long idUserEditor = biz.getIdUsuarioEditorDeEvento(evento.getIdEvento());
            if (idUserEditor != 0 && idUserEditor != usr.getId_usuario()) {
                logger.error("No se puede eliminar el evento, ya q está tomado.");
                res.sendRedirect( cmd_dsp_error +"?mensaje=" + EventosConstants.MSJ_DEL_EVENTO_ERROR_TOMADO + "&PagErr=1");
                return;
            }            
            
            logger.debug("Vamos a eliminar el evento...");
            biz.delEvento(evento);
            
            fp.add("msje", EventosConstants.MSJ_DEL_EVENTO_EXITO);            
            fp.add("sel_tipo_evento",	filtroTipoEvento);
            fp.add("sel_est",			filtroEstado);
            fp.add("fc_ini", 			filtroFechaIni);
            fp.add("fc_fin", 			filtroFechaFin);
            
            paramUrl = TplFile + fp.forward();          

        } catch (BocException e) {
            logger.error("Existió un error al eliminar el evento:" + e.getMessage());
            res.sendRedirect( cmd_dsp_error +"?mensaje="+EventosConstants.MSJ_DEL_EVENTO_ERROR+"&PagErr=1");
            return;
        }
        
        logger.debug("Redireccionando a: " + paramUrl);
        logger.debug("Fin DelEvento Execute");
        res.sendRedirect(paramUrl);
    }

}
