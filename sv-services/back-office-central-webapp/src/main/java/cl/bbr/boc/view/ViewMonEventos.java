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
import cl.bbr.jumbocl.eventos.dto.EventosCriterioDTO;
import cl.bbr.jumbocl.eventos.dto.TipoEventoDTO;
import cl.bbr.jumbocl.eventos.utils.EventosConstants;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra listado para el monitor de casos
 * 
 * @author imoyano
 */
public class ViewMonEventos extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewMonEventos Execute");
	    
	    //Variables
	    String filtroTipoEvento	= "T";
	    String filtroEstado		= "T";
	    String filtroFechaIni	= "";
	    String filtroFechaFin	= "";
	    String mensaje 			= "";
	    
	    long eventoEnEdicion	= 0;
	    boolean esPosibleModificar = false;
	    
	    int pag 			= 1;
		int regsperpage 	= 10;
		
		String accionVer 		= "<img src=\"img/info.gif\" border=\"0\" height=\"16\" width=\"16\" title=\"Ver detalle del evento\">";
		String accionEditar 	= "<img src=\"img/editicon.gif\" border=\"0\" height=\"17\" width=\"19\" title=\"Editar el evento\">";
		String accionEliminar 	= "<img src=\"img/trash.gif\" border=\"0\" height=\"16\" width=\"16\" title=\"Eliminar el evento\">";
		String accionRuts	 	= "<img src=\"img/formulario.jpg\" border=\"0\" height=\"13\" width=\"17\" title=\"Carga de RUT's\">";
	    
		//Objetos para pintar la pantalla
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//Sacamos la info de la pagina
		if (req.getParameter("msje") != null) {
		    mensaje = req.getParameter("msje").toString();
	    }
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
	    
	    //Paginacion
	    if (req.getParameter("pagina") != null) {
			pag = Integer.parseInt(req.getParameter("pagina"));
			logger.debug("pagina: " + req.getParameter("pagina"));
		} else {
			pag = 1; // por defecto mostramos la página 1
		}
	    regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
	    
	    //Verificamos si el usuario esta editando un evento
	    BizDelegate bizDelegate = new BizDelegate();
	    eventoEnEdicion = bizDelegate.getEventoEnEdicionByUsuario(usr);
	    
	    // Rescatamos la informacion que necesitamos
	    EventosCriterioDTO criterio = new EventosCriterioDTO(filtroEstado,filtroTipoEvento,filtroFechaIni,filtroFechaFin,pag,regsperpage);
		
	    logger.debug("vamos a buscar los eventos para el listado");		
		// ---- eventos ----
		List listEventos = bizDelegate.getEventosByCriterio(criterio);
		ArrayList datos = new ArrayList();
		for (int i = 0; i < listEventos.size(); i++) {	
		    esPosibleModificar = false;
			IValueSet fila = new ValueSet();
			EventoDTO evento = (EventoDTO)listEventos.get(i);
			fila.setVariable("{id_evento}" , String.valueOf(evento.getIdEvento()));
			fila.setVariable("{nombre}" , evento.getNombre());
			fila.setVariable("{descripcion}" , evento.getDescripcion());
			fila.setVariable("{fecha_creacion}" , Formatos.frmFecha(evento.getFechaCreacion()));
			fila.setVariable("{fecha_inicio}" , Formatos.frmFecha(evento.getFechaInicio()));
			fila.setVariable("{fecha_fin}" , Formatos.frmFecha(evento.getFechaFin()));
			fila.setVariable("{ocurrencia}" , String.valueOf(evento.getOcurrencia()));
			fila.setVariable("{ruts_total}" , String.valueOf(evento.getRutsTotal()));
			fila.setVariable("{ruts_evento}" , String.valueOf( bizDelegate.getCantidadRutsUsaronEvento(evento.getIdEvento()) ));
			fila.setVariable("{estado}" , evento.descripcionEstado());
			fila.setVariable("{color_fuente}", "#000000");
			
			fila.setVariable("{palito}" , "");
			fila.setVariable("{accion1}" , "");
			fila.setVariable("{accion2}" , "");
			fila.setVariable("{accion3}" , "");
			fila.setVariable("{accion4}" , "");
			fila.setVariable("{direccion1}" , "");
			fila.setVariable("{direccion2}" , "");
			fila.setVariable("{direccion3}" , "");
			fila.setVariable("{direccion4}" , "");
			
			if (evento.getIdUsuarioEditor() > 0) {
			    if (evento.getIdUsuarioEditor() == usr.getId_usuario()) {			        
					esPosibleModificar = true;										
			    }
			} else { 
			    if (eventoEnEdicion == 0) {
					esPosibleModificar = true;
			    }
			}
			if (esPosibleModificar) {
			    fila.setVariable("{palito}" , "&nbsp;");
			    fila.setVariable("{accion2}" , accionEditar);
				fila.setVariable("{direccion2}" , "ViewEventoForm?id_evento=" + evento.getIdEvento());
				fila.setVariable("{accion3}" , accionEliminar);
				if (evento.getRutsEvento() > 0) {
				    fila.setVariable("{direccion3}" , "javascript:alert('"+EventosConstants.MSJ_DEL_EVENTO_ERROR_UTILIZADO+"');");
				} else {
				    fila.setVariable("{direccion3}" , "javascript:eliminarEvento('" + evento.getIdEvento() + "','" + filtroTipoEvento + "','" + filtroEstado + "','" + filtroFechaIni + "','" + filtroFechaFin + "');");
				}
				fila.setVariable("{accion4}" , accionRuts);
				fila.setVariable("{direccion4}" , "ViewEventoRutsForm?id_evento=" + evento.getIdEvento());
			}
	        
			fila.setVariable("{accion1}" , accionVer);
			fila.setVariable("{direccion1}" , "ViewDetalleEvento?id_evento=" + evento.getIdEvento());
			
			datos.add(fila);			
		}
		
		if (listEventos.size() < 1 ) {
			top.setVariable("{mje1}","La consulta no arrojó resultados");
			top.setVariable("{dis}","disabled");
			
		} else {
			top.setVariable("{mje1}","");
			top.setVariable("{dis}","");
			
		}		
		
		// ---- Tipos de Eventos ----
		List listtipos = bizDelegate.getTiposEventos();
		ArrayList tipos = new ArrayList();
		
		for (int i = 0; i < listtipos.size(); i++) {			
			IValueSet fila = new ValueSet();
			TipoEventoDTO tipo = (TipoEventoDTO)listtipos.get(i);
			fila.setVariable("{id_tipo}",String.valueOf(tipo.getIdTipoEvento()));
			fila.setVariable("{tipo}",tipo.getNombre());
			if (filtroTipoEvento.equalsIgnoreCase(String.valueOf(tipo.getIdTipoEvento()))) {
				fila.setVariable("{sel_tipo}", "selected");
			} else {
				fila.setVariable("{sel_tipo}", "");
			}
			tipos.add(fila);
		}
		
		// ---- Estados ----
		ArrayList estados = EventosUtil.generaComboEstado(filtroEstado);		
		
		// Paginador para la pagina
		ArrayList pags = new ArrayList();
		double totalRegistros = bizDelegate.getCountEventosByCriterio(criterio);
		
		logger.debug("tot_reg: " + totalRegistros + "");
		double total_pag = (double) Math.ceil( totalRegistros / regsperpage );
		logger.debug ("round: " + total_pag);
		if (total_pag == 0) {
			total_pag = 1;
		}
		for (int i = 1; i <= total_pag; i++) {
			IValueSet fila = new ValueSet();
			fila.setVariable("{pag}", String.valueOf(i));
			if (i == pag) {
				fila.setVariable("{sel}", "selected");
			} else {
				fila.setVariable("{sel}", "");
			}
			pags.add(fila);
		}	
		//anterior y siguiente
		if( pag > 1) {
	    	int anterior = pag-1;
	    	top.setVariable("{anterior_label}","<< anterior");
	    	top.setVariable("{anterior}",String.valueOf(anterior));
	    } else if ( pag == 1) {
	    	top.setVariable("{anterior_label}","");
	    }	    
	    if (pag < total_pag) {
	    	int siguiente = pag+1;
	    	top.setVariable("{siguiente_label}","siguiente >>");
	    	top.setVariable("{siguiente}",String.valueOf(siguiente));
	    } else {
	    	top.setVariable("{siguiente_label}","");
	    }		
		
		//Informacion para la pagina
	    top.setDynamicValueSets("PAGINAS", pags);
		top.setDynamicValueSets("MON_EVENTOS", datos);
		top.setDynamicValueSets("TIPOS_EVENTO", tipos);
		top.setDynamicValueSets("ESTADOS_EVENTO", estados);
		
		top.setVariable("{sel_tipo_evento}",	filtroTipoEvento);
		top.setVariable("{sel_est}", 			filtroEstado);
		top.setVariable("{fc_ini}", 			filtroFechaIni);
		top.setVariable("{fc_fin}", 			filtroFechaFin);
		
		top.setVariable("{mensaje}", mensaje);	
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
		
		if (eventoEnEdicion != 0) {
			logger.debug("Evento en edicion: " + eventoEnEdicion);
			String var_usrped = " Ud. está editando el Evento: " +
								" <a href='ViewEventoForm?id_evento="+eventoEnEdicion+"&mod=0'> "+eventoEnEdicion+"</a> " +
								" (<a href ='LiberaEvento?id_evento="+eventoEnEdicion+"&mod=1'> Liberar Evento </a>) ";
			top.setVariable("{usr_evento_tomado}", var_usrped);			
		} else {
		    top.setVariable("{usr_evento_tomado}", "<a href=\"ViewEventoForm\">NUEVO</a>");		    
		}
		
		logger.debug("Fin ViewMonEventos Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
