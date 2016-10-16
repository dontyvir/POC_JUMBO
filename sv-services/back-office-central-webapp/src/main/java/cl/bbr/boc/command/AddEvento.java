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

public class AddEvento extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AddEvento Execute");

        String paramUrl = "";
        String TplFile = getServletConfig().getInitParameter("TplFile");
        
        BizDelegate biz = new BizDelegate();
        ForwardParameters fp = new ForwardParameters();
        //fp.add(req.getParameterMap());

        try {            
            if (biz.getEventoEnEdicionByUsuario(usr) != 0) {
                logger.debug("Usuario ya creó un evento y está en edición, no puede crear otro... ");
                res.sendRedirect( cmd_dsp_error +"?mensaje="+EventosConstants.MSJ_ADD_EVENTO_EN_EDICION+"&PagErr=1");
                return;
            }
            logger.debug("Vamos a crear el nuevo evento...");
            EventoDTO evento = new EventoDTO();
            evento.setIdUsuarioCreador(usr.getId_usuario());
            evento.setNombre(req.getParameter("nombre"));
            evento.setTitulo(req.getParameter("titulo_evento"));
            
            String descripcion = req.getParameter("descripcion");
            
            logger.debug("descripcion:"+descripcion.length());
            
            descripcion = descripcion.replaceAll("\r\n","<br>");
            evento.setDescripcion(descripcion);
            
            TipoEventoDTO te = new TipoEventoDTO();
            te.setIdTipoEvento(Long.parseLong( req.getParameter("tipo_evento") ));
            evento.setTipoEvento(te);
            evento.setFechaInicio(req.getParameter("fecha_inicio"));
            evento.setFechaFin(req.getParameter("fecha_fin"));
            evento.setOcurrencia(Long.parseLong( req.getParameter("ocurrencia") ));
            evento.setNombreArchivo(req.getParameter("flash"));
            evento.setOrden(Long.parseLong( req.getParameter("orden") ));
            evento.setActivo("S");
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
    		
            biz.addEvento(evento);
            fp.add("msje", EventosConstants.MSJ_ADD_EVENTO_EXITO);            
            paramUrl = TplFile + fp.forward();          

        } catch (BocException e) {
            logger.error("Existió un error al crear un evento:" + e.getMessage());
            res.sendRedirect( cmd_dsp_error +"?mensaje="+EventosConstants.MSJ_ADD_EVENTO_ERROR+"&PagErr=1");
            return;
        }

        logger.debug("Redireccionando a: " + paramUrl);
        logger.debug("Fin AddEvento Execute");
        res.sendRedirect(paramUrl);
    }
}
