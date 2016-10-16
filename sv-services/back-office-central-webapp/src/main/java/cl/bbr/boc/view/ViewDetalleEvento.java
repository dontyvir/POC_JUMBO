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
import cl.bbr.jumbocl.eventos.dto.RutEventoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra el formulario para asignar RUT's a un evento
 * @author imoyano
 */
public class ViewDetalleEvento extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewDetalleEvento Execute");
	    
	    long idEvento = 0;
	    String mensaje = "";
	    String codFlash = "";
	    
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
		
		logger.debug("ID EVENTO: " + idEvento);
		
		EventoDTO evento = biz.getEvento(idEvento);
		UserDTO editor = new UserDTO();
		editor.setLogin("");
		if (evento.getIdUsuarioEditor() != 0) {
		    editor = biz.getUserById(evento.getIdUsuarioEditor());
		}
		
		top.setVariable("{id_evento}", 		String.valueOf(idEvento));
		top.setVariable("{nombre}", 		evento.getNombre());
		top.setVariable("{titulo}", 		evento.getTitulo());
		top.setVariable("{descripcion}", 	evento.getDescripcion());
		top.setVariable("{tipo_evento}", 	evento.getTipoEvento().getNombre());
		top.setVariable("{fecha_inicio}",	Formatos.frmFecha(evento.getFechaInicio()));
		top.setVariable("{fecha_fin}", 		Formatos.frmFecha(evento.getFechaFin()));
		top.setVariable("{ocurrencia}", 	String.valueOf(evento.getOcurrencia()));
		top.setVariable("{orden}", 			String.valueOf(evento.getOrden()));
		top.setVariable("{usuario_editor}", editor.getLogin());
		top.setVariable("{flash}", 			evento.getNombreArchivo());
		if (evento.getActivo().equalsIgnoreCase("S")) {
		    top.setVariable("{estado}", 	"Activado");
		} else {
		    top.setVariable("{estado}", 	"Desactivado");
		}
        if (evento.getValidacionManual().equalsIgnoreCase("S")) {
            top.setVariable("{validacion}",     "Si");
        } else {
            top.setVariable("{validacion}",     "No");
        }
		
		// ---- pasos ----
		ArrayList pasos = new ArrayList();			
		for (int j = 0; j < evento.getPasos().size(); j++) {
		    IValueSet fila = new ValueSet();
		    PasoDTO paso = (PasoDTO)evento.getPasos().get(j);
		    fila.setVariable("{id_paso}",String.valueOf(paso.getIdPaso()));
			fila.setVariable("{paso}",paso.getNombre());
			pasos.add(fila);    
		}		
				
		// ---- ruts -----
	    List listruts = biz.getRutsByEvento(idEvento);
		ArrayList ruts = new ArrayList();
		
		for (int i = 0; i < listruts.size(); i++) {			
			IValueSet fila = new ValueSet();
			RutEventoDTO rut = (RutEventoDTO)listruts.get(i);
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
		
		//----
		// Flash		
		if (evento.getNombreArchivo().length() > 0) {
		    
		    ResourceBundle rb = ResourceBundle.getBundle("confEventos");
	        String rutaFlash = rb.getString("dir_eventos");
	        String flashAncho = rb.getString("flash_ancho");
	        String flashAlto = rb.getString("flash_alto");
	        codFlash = rb.getString("codigo_html");
	        
	        String ext = evento.getNombreArchivo().substring(evento.getNombreArchivo().length()-4,evento.getNombreArchivo().length());
			if (ext.equalsIgnoreCase(".swf")) {
				codFlash = codFlash.replaceAll("@flash_ancho",flashAncho);
				codFlash = codFlash.replaceAll("@flash_alto",flashAlto);
				codFlash = codFlash.replaceAll("@nombre_archivo",rutaFlash+evento.getNombreArchivo());				    
			
			} else {
			    codFlash = "<font color=red>[El archivo debe tener extensión de tipo Flash]</font> <br><br>";
			}			
		}
		//----
		
		top.setVariable("{codFlash}", codFlash);
		top.setVariable("{mensaje}", mensaje);
		top.setDynamicValueSets("RUTS_EVENTO", ruts);
		top.setDynamicValueSets("PASOS", pasos);
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());			

		logger.debug("Fin ViewDetalleEvento Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}

    


}
