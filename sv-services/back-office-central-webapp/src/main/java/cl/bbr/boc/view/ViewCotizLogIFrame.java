package cl.bbr.boc.view;

import java.util.ArrayList;
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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;
/**
 * despliega un listado con los log's del pedido
 * @author BBRI
 */
public class ViewCotizLogIFrame extends Command {

	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		long	id_cot		= -1;
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);		

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		if ( req.getParameter("id_cot") == null ){
			throw new ParametroObligatorioException("id_cot es null");
		}
		
		id_cot = Long.parseLong(req.getParameter("id_cot"));	
		logger.debug("id_cot: " + id_cot);

		// 3. Template (dejar tal cual)		
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
	
		// 4.  Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		// 4.1 Log
		List listaLog = new ArrayList();
		listaLog = bizDelegate.getLogsCotiz(id_cot);
		ArrayList log = new ArrayList();
		for (int i=0; i<listaLog.size(); i++){			
			IValueSet fila = new ValueSet();
			LogsCotizacionesDTO log1 = new LogsCotizacionesDTO();
			log1 = (LogsCotizacionesDTO)listaLog.get(i);
			fila.setVariable("{fecha}"		,Formatos.frmFechaHora(log1.getFec_ing()));
			fila.setVariable("{usuario}"	,log1.getUsuario());
			fila.setVariable("{log}"		,log1.getDescripcion());		
			
			log.add(fila);
		}	
		// 5. Setea variables del template
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("LOG_COT", log);
		// 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();
		
		
	
	}
}
