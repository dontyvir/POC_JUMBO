package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

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
import cl.bbr.jumbocl.eventos.dto.PasoDTO;
import cl.bbr.jumbocl.eventos.dto.TipoEventoDTO;
import cl.bbr.jumbocl.eventos.utils.EventosConstants;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra el formulario para crear un caso
 * @author imoyano
 */
public class ViewEventoForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewEventoForm Execute");
	    
	    long idEvento = 0;
	    
	    ResourceBundle rb = ResourceBundle.getBundle("confCasos");
		String pathImages = rb.getString("ruta_layers");
	    
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();		
		
		BizDelegate bizDelegate = new BizDelegate();		
		
		if (req.getParameter("id_evento") != null) {
		    idEvento = Long.parseLong(req.getParameter("id_evento").toString());
        }
		logger.debug("ID EVENTO: " + idEvento);
		
		if (idEvento == 0) {
			if (bizDelegate.getEventoEnEdicionByUsuario(usr) != 0) {
	            logger.debug("Usuario ya creó un evento y está en edición, no puede crear otro... ");
	            res.sendRedirect( cmd_dsp_error +"?mensaje="+EventosConstants.MSJ_ADD_EVENTO_EN_EDICION+"&PagErr=1");
	            return;            
	        }
		}		
		
		EventoDTO evento = new EventoDTO();
		if (idEvento != 0) {
		    evento = bizDelegate.getEvento(idEvento);
		    
		    //Dejamos marcado el evento en 'edicion'
		    if (bizDelegate.setModEvento(idEvento, usr.getId_usuario())) {
		        logger.debug("No se pudo dejar el evento marcado indicando que se encuentra en edición.");
		    }
		    
		}
		
		// ---- tipo de eventos ----
		List listTiposEventos = bizDelegate.getTiposEventos();
		ArrayList tipEvents = new ArrayList();		
		for (int i = 0; i < listTiposEventos.size(); i++) {			
			IValueSet fila = new ValueSet();
			TipoEventoDTO tipoEventos = (TipoEventoDTO)listTiposEventos.get(i);
			fila.setVariable("{id_tipo}",String.valueOf(tipoEventos.getIdTipoEvento()));
			fila.setVariable("{tipo}",tipoEventos.getNombre());
			if (evento.getTipoEvento().getIdTipoEvento() == tipoEventos.getIdTipoEvento()) {
			    fila.setVariable("{sel_tipo}", "selected");
			} else {
			    fila.setVariable("{sel_tipo}", "");
			}
			tipEvents.add(fila);
		}
		
		// ---- pasos ----
		List listPasos = bizDelegate.getPasos();
		ArrayList pasos = new ArrayList();
		boolean seleccionarPaso = false;
		for (int i = 0; i < listPasos.size(); i++) {
		    seleccionarPaso = false;
			IValueSet fila = new ValueSet();
			PasoDTO paso = (PasoDTO)listPasos.get(i);
			fila.setVariable("{id_paso}",String.valueOf(paso.getIdPaso()));
			fila.setVariable("{paso}",paso.getNombre());
			for (int j = 0; j < evento.getPasos().size(); j++) {
			    PasoDTO pasoSel = (PasoDTO)evento.getPasos().get(j);
			    if (paso.getIdPaso() == pasoSel.getIdPaso()) {
			        seleccionarPaso = true;
			    }				
			}
			if (seleccionarPaso) {
			    fila.setVariable("{checked_paso}", "checked");
			} else {
			    fila.setVariable("{checked_paso}", "");
			}
			pasos.add(fila);
		}
		
		// ---- Estados ----
		ArrayList estados = EventosUtil.generaComboEstado(evento.getActivo());
		
		//Informacion para la pagina
	    top.setDynamicValueSets("TIPOS_EVENTOS", tipEvents);
		top.setDynamicValueSets("PASOS", pasos);
		top.setDynamicValueSets("ESTADOS", estados);
		
		// Dejamos en blanco los campos		
		top.setVariable("{id_evento}",		String.valueOf(idEvento));
		top.setVariable("{nombre}",			evento.getNombre());
		if (evento.getTitulo() == null || evento.getTitulo() == "") {
		    top.setVariable("{titulo}",	"@nombre tenemos un mensaje para ti...");
		} else {
		    top.setVariable("{titulo}",	evento.getTitulo());    
		}
		top.setVariable("{descripcion}",	evento.getDescripcion().replaceAll("<br>","\r\n"));
		
		if (idEvento != 0) {
			top.setVariable("{fecha_inicio}",	Formatos.frmFecha(evento.getFechaInicio()));
			top.setVariable("{fecha_inicio_old}",Formatos.frmFecha(evento.getFechaInicio()));
			top.setVariable("{fecha_fin}", 		Formatos.frmFecha(evento.getFechaFin()));
			top.setVariable("{ocurrencia}", 	String.valueOf(evento.getOcurrencia()));
			top.setVariable("{orden}", 			String.valueOf(evento.getOrden()));
            if (evento.getValidacionManual().equalsIgnoreCase("S")) {
                top.setVariable("{validacion}","S");
                top.setVariable("{validacion_checked}","checked");
            } else {
                top.setVariable("{validacion}","N");
                top.setVariable("{validacion_checked}","");
            }
            
		} else {
		    top.setVariable("{fecha_inicio}",	"");
			top.setVariable("{fecha_inicio_old}","");
			top.setVariable("{fecha_fin}", 		"");
			top.setVariable("{ocurrencia}", 	"");
			top.setVariable("{orden}", 			"");
            top.setVariable("{validacion}","S");
            top.setVariable("{validacion_checked}","");            
		}
		
		top.setVariable("{flash}", 			evento.getNombreArchivo());
		
		top.setVariable("{fecha_actual}", EventosUtil.fechaActual());
		
		
		ResourceBundle rbEventos = ResourceBundle.getBundle("confEventos");
		top.setVariable("{fla_directorio}",	rbEventos.getString("dir_eventos"));
		//top.setVariable("{fla_ancho}",		rbEventos.getString("flash_ancho"));
		//top.setVariable("{fla_alto}", 		rbEventos.getString("flash_alto"));
		
		String codFlash = "";
		String flashAncho	= rbEventos.getString("flash_ancho");
        String flashAlto	= rbEventos.getString("flash_alto");
        codFlash			= rbEventos.getString("codigo_html");
    	codFlash 			= codFlash.replaceAll("@flash_ancho",flashAncho);
		codFlash 			= codFlash.replaceAll("@flash_alto",flashAlto);
		
		top.setVariable("{codigo_html}", 	codFlash);
		top.setVariable("{ruta_img_layer}", pathImages);
		
		top.setVariable("{nombre_pila_usuario}", usr.getNombre());
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());			

		logger.debug("Fin ViewEventoForm Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
