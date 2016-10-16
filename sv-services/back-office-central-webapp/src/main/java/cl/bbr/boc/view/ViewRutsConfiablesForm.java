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
import cl.bbr.jumbocl.clientes.dto.RutConfiableDTO;
import cl.bbr.jumbocl.clientes.dto.RutConfiableLogDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra el formulario para cargar RUT's de clientes confiables
 * @author imoyano
 */
public class ViewRutsConfiablesForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewRutsConfiablesForm Execute");
	    
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
		
		if (req.getParameter("mensaje") != null) {
		    mensaje = req.getParameter("mensaje").toString();
        }
		if (req.getParameter("cargo_rut") != null) {
		    cargaRut = req.getParameter("cargo_rut").toString();
        }
		
		List listruts = biz.getRutsConfiables();
		ArrayList ruts = new ArrayList();
		
		for (int i = 0; i < listruts.size(); i++) {			
			IValueSet fila = new ValueSet();
			RutConfiableDTO rut = (RutConfiableDTO)listruts.get(i);
            fila.setVariable("{nro_registro}",String.valueOf( i + 1 ));
			fila.setVariable("{cli_rut}",String.valueOf(rut.getRut()));
			fila.setVariable("{cli_dv}",String.valueOf(rut.getDv()));
			fila.setVariable("{fecha_creacion}", Formatos.frmFecha(rut.getFechaCreacion()));
			ruts.add(fila);
		}
		if (ruts.size() > 0 ) {
		    top.setVariable("{msj_ruts}", "");
		} else {
		    top.setVariable("{msj_ruts}", "No existen Rut's para mostrar.");
		}		
		
		top.setVariable("{mensaje}", mensaje);
		top.setVariable("{cargo_rut}", cargaRut);
		
		top.setDynamicValueSets("RUTS", ruts);
        
        List listLogs = biz.getLogRutsConfiables();
        ArrayList logs = new ArrayList();
        
        for (int i = 0; i < listLogs.size(); i++) {         
            IValueSet fila = new ValueSet();
            RutConfiableLogDTO log = (RutConfiableLogDTO) listLogs.get(i);
            fila.setVariable("{user}", log.getUsuario());
            fila.setVariable("{nombre}", log.getNombreUsuario());
            fila.setVariable("{fecha}", Formatos.frmFechaHoraSinSegundos(log.getFechaHora()));
            fila.setVariable("{descripcion}", log.getDescripcion());
            logs.add(fila);
        }
        if (logs.size() > 0 ) {
            top.setVariable("{msj_logs}", "");
        } else {
            top.setVariable("{msj_logs}", "No existe registro para mostrar");
        }
        top.setDynamicValueSets("LOGS", logs);
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());			

		logger.debug("Fin ViewRutsConfiablesForm Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
