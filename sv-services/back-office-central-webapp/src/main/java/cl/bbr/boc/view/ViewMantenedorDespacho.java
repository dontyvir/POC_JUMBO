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
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.pedidos.dto.EmpresaTransporteDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Entrega los datos para la configuración de Motivos y Responsables de Despachos
 * 
 * @author imoyano
 */
public class ViewMantenedorDespacho extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewMantenedorDespacho Execute");

	    //Objetos para pintar la pantalla
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
        
        String msj = "";
        
        if ( req.getParameter("mensaje") != null ) {
            msj = req.getParameter("mensaje");
        }
        
		BizDelegate biz = new BizDelegate();        
        
		// ---- Motivos de no cumplimiento de despacho o de reprogramacion ----
		List motivos = biz.getMotivosDespachoNCAll();
		ArrayList lMotivos = new ArrayList();
		
		for (int i = 0; i < motivos.size(); i++) {			
			IValueSet fila = new ValueSet();
			ObjetoDTO mot = (ObjetoDTO) motivos.get(i);
			fila.setVariable("{id}",String.valueOf(mot.getIdObjeto()));			
			fila.setVariable("{nombre}",mot.getNombre());
			if ( "S".equalsIgnoreCase( mot.getActivado() )) {
			    fila.setVariable("{activado}","Si");
			} else {
			    fila.setVariable("{activado}","No");
			}
			fila.setVariable("{est_activado}",mot.getActivado());
			lMotivos.add(fila);
		}
		if (lMotivos.size() > 0) {
		    top.setVariable("{msj_motivos}","");
		} else {
		    top.setVariable("{msj_motivos}","No existen motivos.");
		}
        
        // ---- Responsables de no cumplimiento de despacho o de reprogramacion ----
        List responsables = biz.getResponsablesDespachoNCAll();
        ArrayList lResponsables = new ArrayList();
        
        for (int i = 0; i < responsables.size(); i++) {          
            IValueSet fila = new ValueSet();
            ObjetoDTO resp = (ObjetoDTO) responsables.get(i);
            fila.setVariable("{id}",String.valueOf(resp.getIdObjeto()));         
            fila.setVariable("{nombre}",resp.getNombre());
            if ( "S".equalsIgnoreCase( resp.getActivado() )) {
                fila.setVariable("{activado}","Si");
            } else {
                fila.setVariable("{activado}","No");
            }
            fila.setVariable("{est_activado}",resp.getActivado());
            lResponsables.add(fila);
        }
        if (lResponsables.size() > 0) {
            top.setVariable("{msj_responsables}","");
        } else {
            top.setVariable("{msj_responsables}","No existen responsables.");
        }
        
        //---- Empresas de transporte
        List empresas = biz.getEmpresasTransporteAll();
        ArrayList lEmpresas = new ArrayList();
        
        for (int i = 0; i < empresas.size(); i++) {          
            IValueSet fila = new ValueSet();
            EmpresaTransporteDTO emp = (EmpresaTransporteDTO) empresas.get(i);
            fila.setVariable("{id}",String.valueOf(emp.getIdEmpresaTransporte()));         
            fila.setVariable("{nombre}",emp.getNombre());
            if ( "S".equalsIgnoreCase( emp.getActivado() )) {
                fila.setVariable("{activado}","Si");
            } else {
                fila.setVariable("{activado}","No");
            }
            fila.setVariable("{est_activado}",emp.getActivado());
            lEmpresas.add(fila);
        }
        if (lEmpresas.size() > 0) {
            top.setVariable("{msj_empresas}","");
        } else {
            top.setVariable("{msj_empresas}","No existen empresas.");
        }
        
        top.setDynamicValueSets("LIST_MOTIVOS", lMotivos);
        top.setDynamicValueSets("LIST_RESPONSABLES", lResponsables);
        top.setDynamicValueSets("LIST_EMPRESAS", lEmpresas);
        
		top.setVariable("{msj}", msj);
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
		
		logger.debug("Fin ViewMantenedorDespacho Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
