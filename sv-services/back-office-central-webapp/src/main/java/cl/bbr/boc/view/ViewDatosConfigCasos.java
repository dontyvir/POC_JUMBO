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
import cl.bbr.jumbocl.casos.dto.JornadaDTO;
import cl.bbr.jumbocl.casos.dto.QuiebreCasoDTO;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Entrega los datos para la configuración de Casos
 * 
 * @author imoyano
 */
public class ViewDatosConfigCasos extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewDatosConfigCasos Execute");

	    //Objetos para pintar la pantalla
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		BizDelegate bizDelegate = new BizDelegate();

		// ---- Tipos de Quiebre ----
		List quiebres = bizDelegate.getQuiebres();
		ArrayList lQuiebre = new ArrayList();
		
		for (int i = 0; i < quiebres.size(); i++) {			
			IValueSet fila = new ValueSet();
			QuiebreCasoDTO quiebre = (QuiebreCasoDTO)quiebres.get(i);
			fila.setVariable("{id_quiebre}",String.valueOf(quiebre.getIdQuiebre()));			
			fila.setVariable("{descripcion_quiebre}",quiebre.getNombre());
			fila.setVariable("{puntaje_quiebre}",String.valueOf(quiebre.getPuntaje()));
			if (quiebre.getActivado().equals("1")) {
			    fila.setVariable("{activado}","Si");
			} else {
			    fila.setVariable("{activado}","No");
			}
			fila.setVariable("{est_activado}",quiebre.getActivado());
			lQuiebre.add(fila);
		}
		if (lQuiebre.size() > 0) {
		    top.setVariable("{msj_quiebre}","");
		} else {
		    top.setVariable("{msj_quiebre}","No existen tipos de quiebre.");
		}
		
		// ---- Responsables ----
		List responsables = bizDelegate.getResponsables();
		ArrayList lResposable = new ArrayList();
		
		for (int i = 0; i < responsables.size(); i++) {			
			IValueSet fila = new ValueSet();
			ObjetoDTO responsable = (ObjetoDTO)responsables.get(i);
			fila.setVariable("{id_responsable}",String.valueOf(responsable.getIdObjeto()));			
			fila.setVariable("{descripcion_responsable}",responsable.getNombre());
			if (responsable.getActivado().equals("1")) {
			    fila.setVariable("{activado}","Si");
			} else {
			    fila.setVariable("{activado}","No");
			}
			fila.setVariable("{est_activado}",responsable.getActivado());
			lResposable.add(fila);
		}
		if (lResposable.size() > 0) {
		    top.setVariable("{msj_responsable}","");
		} else {
		    top.setVariable("{msj_responsable}","No existen responsables.");
		}

		// ---- Jornadas ----
		List jornadas = bizDelegate.getJornadas();
		ArrayList lJornada = new ArrayList();
		
		for (int i = 0; i < jornadas.size(); i++) {			
			IValueSet fila = new ValueSet();
			JornadaDTO jornada = (JornadaDTO)jornadas.get(i);
			fila.setVariable("{id_jornada}",String.valueOf(jornada.getIdJornada()));			
			fila.setVariable("{descripcion_jornada}",jornada.getDescripcion());
			if (jornada.getActivado().equals("1")) {
			    fila.setVariable("{activado}","Si");
			} else {
			    fila.setVariable("{activado}","No");
			}
			fila.setVariable("{est_activado}",jornada.getActivado());
			lJornada.add(fila);
		}
		if (lJornada.size() > 0) {
		    top.setVariable("{msj_jornada}","");
		} else {
		    top.setVariable("{msj_jornada}","No existen jornadas.");
		}
        

        // ---- Motivos ----
        List motivos = bizDelegate.getMotivos();
        ArrayList lMotivos = new ArrayList();
        
        for (int i = 0; i < motivos.size(); i++) {         
            IValueSet fila = new ValueSet();
            ObjetoDTO motivo = (ObjetoDTO)motivos.get(i);
            fila.setVariable("{id_motivo}",String.valueOf(motivo.getIdObjeto()));            
            fila.setVariable("{descripcion_motivo}",motivo.getNombre());
            if (motivo.getActivado().equals("1")) {
                fila.setVariable("{activado}","Si");
            } else {
                fila.setVariable("{activado}","No");
            }
            fila.setVariable("{est_activado}",motivo.getActivado());
            lMotivos.add(fila);
        }
        if (lMotivos.size() > 0) {
            top.setVariable("{msj_motivo}","");
        } else {
            top.setVariable("{msj_motivo}","No existen motivos.");
        }

        top.setDynamicValueSets("LIST_MOTIVOS", lMotivos);
        top.setDynamicValueSets("LIST_QUIEBRES", lQuiebre);
		top.setDynamicValueSets("LIST_RESPONSABLES", lResposable);
		top.setDynamicValueSets("LIST_JORNADAS", lJornada);
		
		top.setVariable("{cantQuiebres}",		String.valueOf(lQuiebre.size()));
		top.setVariable("{cantResponsables}",	String.valueOf(lResposable.size()));
		top.setVariable("{cantJornadas}",		String.valueOf(lJornada.size()));
        top.setVariable("{cantMotivos}",       String.valueOf(lMotivos.size()));
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
		
		logger.debug("Fin ViewDatosConfigCasos Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
