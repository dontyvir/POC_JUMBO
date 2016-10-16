package cl.bbr.boc.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.eventos.dto.PasoDTO;
import cl.bbr.jumbocl.eventos.dto.TipoEventoDTO;
import cl.bbr.jumbocl.eventos.utils.EventosConstants;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agrega un nuevo evento
 * 
 * @author imoyano
 */

public class ModEvento extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ModEvento Execute");

        String paramUrl = "";
        String TplFile = getServletConfig().getInitParameter("TplFile");
        
        BizDelegate biz = new BizDelegate();
        ForwardParameters fp = new ForwardParameters();
        //fp.add(req.getParameterMap());

        try {
            logger.debug("Vamos a modificar el evento...");
            EventoDTO evento = new EventoDTO();
            evento.setIdEvento(Long.parseLong( req.getParameter("id_evento") ));
            evento.setNombre(req.getParameter("nombre"));
            evento.setTitulo(req.getParameter("titulo_evento"));
            
            String descripcion = req.getParameter("descripcion");
            descripcion = descripcion.replaceAll("\r\n","<br>");            
            evento.setDescripcion(descripcion);
            
            logger.debug("descripcion:"+descripcion.length());
            
            TipoEventoDTO te = new TipoEventoDTO();
            te.setIdTipoEvento(Long.parseLong( req.getParameter("tipo_evento") ));
            evento.setTipoEvento(te);
            evento.setFechaInicio(req.getParameter("fecha_inicio"));
            evento.setFechaFin(req.getParameter("fecha_fin"));
            evento.setOcurrencia(Long.parseLong( req.getParameter("ocurrencia") ));
            evento.setNombreArchivo(req.getParameter("flash"));
            evento.setOrden(Long.parseLong( req.getParameter("orden") ));
            evento.setActivo(req.getParameter("estado"));
            evento.setValidacionManual(req.getParameter("validacion"));
            
            List listPasos = biz.getPasos();
            List pasosEvento = new ArrayList();
    		for (int i = 0; i < listPasos.size(); i++) {
    			PasoDTO paso = (PasoDTO) listPasos.get(i);
    			if (req.getParameter("check_"+paso.getIdPaso()) != null) { 
    			    pasosEvento.add(paso);
    			}    			
    		}
    		evento.setPasos(pasosEvento);
            biz.modEvento(evento);
            
            // liberamos el evento
            if (biz.setLiberaEvento(evento.getIdEvento())) {
//                logger.debug("Inicio del loggeo");
//                LogCasosDTO log2 = new LogCasosDTO(idCaso, estado, usr.getLogin(), "[BOC] " + CasosConstants.LOG_LIB_CASO);
//                bizDelegate.addLogCaso(log2);
//                logger.debug("Fin del loggeo");
            } else {
                logger.error("No se pudo liberar el evento: " + evento.getIdEvento());
            }
            
            fp.add("msje", EventosConstants.MSJ_MOD_EVENTO_EXITO);            
            paramUrl = TplFile + fp.forward();          

        } catch (BocException e) {
            logger.error("Existió un error al crear un evento:" + e.getMessage());
            res.sendRedirect( cmd_dsp_error +"?mensaje="+EventosConstants.MSJ_MOD_EVENTO_ERROR+"&PagErr=1");
            return;
        }
        
        logger.debug("Redireccionando a: " + paramUrl);
        logger.debug("Fin ModEvento Execute");
        res.sendRedirect(paramUrl);
    }
}
