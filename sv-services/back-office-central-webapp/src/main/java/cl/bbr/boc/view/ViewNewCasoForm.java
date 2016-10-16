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
import cl.bbr.jumbocl.casos.dto.EstadoCasoDTO;
import cl.bbr.jumbocl.casos.dto.JornadaDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.casos.utils.CasosEstadosUtil;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra el formulario para crear un caso
 * @author imoyano
 */
public class ViewNewCasoForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewNewCasoForm Execute");
	    
	    //Variables
	    String mensajeError = "";	    
	    if (req.getParameter("mensaje_error") != null) {
	        mensajeError = req.getParameter("mensaje_error").toString();
	    }	    
	    
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();		
        
        ResourceBundle rb = ResourceBundle.getBundle("confCasos");
        long idCallCenter = Long.parseLong(rb.getString("id_call_center"));
        boolean esCallCenter = CasosUtil.esCallCenter(usr, idCallCenter);
        esCallCenter = true;
        
        ArrayList aCall = new ArrayList();
        if ( esCallCenter ) {
            IValueSet filaCall = new ValueSet();
            filaCall.setVariable("{vacio}","");
            aCall.add(filaCall);
        }
		BizDelegate bizDelegate = new BizDelegate();
		
		// ---- estados de casos ----
		List listestados = bizDelegate.getEstadosDeCasos();
		ArrayList edos = new ArrayList();		
		for (int i = 0; i < listestados.size(); i++) {
			IValueSet fila = new ValueSet();
			EstadoCasoDTO estados = (EstadoCasoDTO)listestados.get(i);
			fila.setVariable("{id_estado}",String.valueOf(estados.getIdEstado()));
			fila.setVariable("{estado}",estados.getNombre());
			if ( estados.getIdEstado() == CasosEstadosUtil.PRE_INGRESADO
					|| estados.getIdEstado() == CasosEstadosUtil.INGRESADO) {
			    
			    edos.add(fila);
			}
			
			//(13/10/2014) Req. Mejoras al Monitor de Casos (Extras) - NSepulveda.
			if ( estados.getIdEstado() == CasosEstadosUtil.PRE_INGRESADO){
				fila.setVariable("{sel_est}", "selected");
			}
			
		}
		
		// ---- locales ----
		List listLocales = bizDelegate.getLocales();
		ArrayList locs = new ArrayList();
		for (int i = 0; i < listLocales.size(); i++) {			
			IValueSet fila = new ValueSet();
			LocalDTO loc1 = (LocalDTO)listLocales.get(i);
			fila.setVariable("{local}",loc1.getNom_local());
			fila.setVariable("{id_local}",String.valueOf(loc1.getId_local()));
			fila.setVariable("{sel_loc}", "");
			locs.add(fila);
		}
		
		// ---- Jornadas ----
		List listJornadas = bizDelegate.getJornadasByEstado("1");
		ArrayList jornadas = new ArrayList();		
		for (int i = 0; i < listJornadas.size(); i++) {			
			IValueSet fila = new ValueSet();
			JornadaDTO jornada = (JornadaDTO)listJornadas.get(i);
			fila.setVariable("{id_jornada}",String.valueOf(jornada.getIdJornada()));
			fila.setVariable("{jornada}",jornada.getDescripcion());
			fila.setVariable("{sel_jorn}", "");
			jornadas.add(fila);
		}		
		
		//Informacion para la pagina
        top.setDynamicValueSets("CALL_CENTER", aCall);
        top.setDynamicValueSets("ESTADOS_CASOS", edos);
		top.setDynamicValueSets("JORNADAS_CASOS", jornadas);		
		top.setDynamicValueSets("LOCALES", locs);
		
		// Dejamos en blanco los campos		
		top.setVariable("{nro_pedido}",			"");
		top.setVariable("{direccion}",			"");
		top.setVariable("{comuna}",				"");
		top.setVariable("{fecha_pedido}", 		"");
		top.setVariable("{fecha_despacho}", 	"");
		top.setVariable("{fecha_ingreso_caso}", "");
		top.setVariable("{cli_rut}", 			"");
		top.setVariable("{cli_dv}", 			"");
		top.setVariable("{cli_nombre}", 		"");
		top.setVariable("{cli_fon_cod1}", 		"");
		top.setVariable("{cli_fon_num1}", 		"");
		top.setVariable("{cli_fon_cod2}", 		"");
		top.setVariable("{cli_fon_num2}", 		"");
		top.setVariable("{cli_fon_cod3}", 		"");
		top.setVariable("{cli_fon_num3}", 		"");
		top.setVariable("{cli_num_compras}", 	"");
		top.setVariable("{cli_num_casos}", 		"");
		top.setVariable("{fecha_resolucion}", 	"");
		
		//Configuramos los días minimos de resolucion
		top.setVariable("{fecha_resolucion_minima}", CasosUtil.fechaActualMasDiaResolucion(CasosConstants.DIAS_RESOLUCION_CASO));
		
		top.setVariable("{mensaje1}",mensajeError);		
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());			

		logger.debug("Fin ViewNewCasoForm Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
    
}
