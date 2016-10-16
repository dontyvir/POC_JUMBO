package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.eventos.dto.RutEventoDTO;
import cl.bbr.jumbocl.eventos.utils.EventosConstants;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra el formulario para asignar RUT's a un evento
 * @author imoyano
 */
public class ViewEventoRutsForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewEventoRutsForm Execute");
	    
	    long idEvento = 0;
	    String mensaje = "";
	    String cargaRut = "";
	    
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		logger.debug("Template: " + html);
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		BizDelegate biz = new BizDelegate();
		
		if (req.getParameter("id_evento") != null) {
		    idEvento = Long.parseLong(req.getParameter("id_evento").toString());
        }
		
		if (req.getParameter("mensaje") != null) {
		    mensaje = req.getParameter("mensaje").toString();
        }
		
		if (req.getParameter("cargo_rut") != null) {
		    cargaRut = req.getParameter("cargo_rut").toString();
        }
		
		logger.debug("ID EVENTO: " + idEvento);
		
		EventoDTO evento = biz.getEvento(idEvento);
		
		// Validamos q no esté en edición
		logger.debug("Validamos que el evento no se encuentre tomado ");
        long idUserEditor = biz.getIdUsuarioEditorDeEvento(idEvento);
        if (idUserEditor != 0 && idUserEditor != usr.getId_usuario()) {
            logger.error("No se pueden asiganr Rut's al evento, ya q está tomado.");
            res.sendRedirect( cmd_dsp_error +"?mensaje=" + EventosConstants.MSJ_RUT_EVENTO_ERROR_TOMADO + "&PagErr=1");
            return;
        }
		
        // Dejamos marcado el evento en 'edicion'
	    if (biz.setModEvento(idEvento, usr.getId_usuario())) {
	        logger.debug("No se pudo dejar el evento marcado indicando que se encuentra en edición.");
	    }
	    
	    List listruts = biz.getRutsByEvento(idEvento);
		ArrayList ruts = new ArrayList();
		
		for (int i = 0; i < listruts.size(); i++) {
			IValueSet fila = new ValueSet();
			RutEventoDTO rut = (RutEventoDTO)listruts.get(i);
			fila.setVariable("{nro_registro}",String.valueOf( i + 1 ));
			fila.setVariable("{cli_rut}",String.valueOf(rut.getCliRut()));
			fila.setVariable("{cli_dv}",String.valueOf(rut.getCliDv()));
			fila.setVariable("{ocurrencia_max}",String.valueOf(evento.getOcurrencia()));
			fila.setVariable("{ocurrencia_por_rut}",String.valueOf(rut.getOcurrenciaPorRut()));
			fila.setVariable("{fecha_creacion}", Formatos.frmFecha(rut.getFechaCreacion()));
			fila.setVariable("{fecha_modificacion}", Formatos.frmFecha(rut.getFechaModificacion()));
			ruts.add(fila);
		}
		if (ruts.size() > 0 ) {
		    top.setVariable("{msj_ruts}", "");
		} else {
		    top.setVariable("{msj_ruts}", "No existen Rut's asociados al evento.");
		}
		
		top.setVariable("{id_evento}", String.valueOf(idEvento));
		top.setVariable("{mensaje}", mensaje);
		top.setVariable("{cargo_rut}", cargaRut);
		
		top.setDynamicValueSets("RUTS_EVENTO", ruts);
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());

		logger.debug("Fin ViewEventoRutsForm Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}
