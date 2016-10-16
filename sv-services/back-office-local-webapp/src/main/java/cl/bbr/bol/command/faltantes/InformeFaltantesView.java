/*
 * Creado el 31-oct-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.bol.command.faltantes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * @author Sebastian
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class InformeFaltantesView extends Command{
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		GregorianCalendar hoy = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		BizDelegate bizDelegate = new BizDelegate();
		long idLocal = usr.getId_local();
		
//		List listaJornadas = new ArrayList();
		
		int validacion = req.getParameter("parametroValidacion") != null ? Integer.parseInt((String)req.getParameter("parametroValidacion")) : 0;
		String fechaConsulta = req.getParameter("fecha") != null ? (String)req.getParameter("fecha") : "";
		long jornadaActual = (req.getParameter("jornada") != null && req.getParameter("jornada").compareToIgnoreCase("") != 0) ? Long.parseLong((String)req.getParameter("jornada")) : 0;
		
		
		
		/*CONTENIDO FIJO*/
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}", usr.getLocal());
		top.setVariable("{hdr_fecha}", new Date());
		/*CONTENIDO FIJO*/
			
		HashMap datosCabecera = bizDelegate.getDatosCabecera(validacion, fechaConsulta, jornadaActual, idLocal);
		
		/*CONTENIDO VARIABLE*/
		
		top.setVariable("{jornadaActual}",datosCabecera.get("jornadaConsulta"));
		top.setVariable("{fechaActual}",datosCabecera.get("fechaConsulta"));
		top.setVariable("{horarioActual}",datosCabecera.get("horarioConsulta"));
		top.setVariable("{horariosLocal}",datosCabecera.get("horariosLocal"));
		
		List listaHorarios = (List)datosCabecera.get("horariosLocal");
		List listaHorasLocal = new ArrayList();
        if (listaHorarios != null && listaHorarios.size() > 0) {
            for(int x = 0; x < listaHorarios.size(); x++){
            	HashMap horario = new HashMap();
            	horario = (HashMap)listaHorarios.get(x);
            	
            	IValueSet horasLocal = new ValueSet();
            	horasLocal.setVariable("{idHorario}", horario.get("idHorario"));
            	horasLocal.setVariable("{horarioLocal}", horario.get("horarioLocal"));
            	listaHorasLocal.add(horasLocal);
            }
            top.setDynamicValueSets("SELECT_JORNADAS", listaHorasLocal);
            
        }
        
	
		
		/*FIN CONTENIDO VARIABLE*/
		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
	
}
