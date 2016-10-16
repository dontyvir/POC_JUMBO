package cl.bbr.bol.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.LogSimpleDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega el log de la ronda
 * @author mleiva
 */
public class ViewLogRonda extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		String 	param_id_ronda	= "";
		long	id_ronda		= -1;
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);		

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		if ( req.getParameter("id_ronda") == null ){
			throw new ParametroObligatorioException("id_ronda es null");
		}
		
		param_id_ronda	= req.getParameter("id_ronda");
		id_ronda = Long.parseLong(param_id_ronda);	
		logger.debug("id_ronda: " + param_id_ronda);
		
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
		listaLog = bizDelegate.getLogRonda(id_ronda);
		ArrayList log = new ArrayList();
		for (int i=0; i<listaLog.size(); i++){			
			IValueSet fila = new ValueSet();
			LogSimpleDTO log1 = new LogSimpleDTO();
			log1 = (LogSimpleDTO)listaLog.get(i);
			
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String strfecha = Formatos.frmFechaHora(log1.getFecha());
			
			fila.setVariable("{fecha}"		,strfecha);
			fila.setVariable("{usuario}"	,log1.getUsuario());
			fila.setVariable("{log}"		,log1.getDescripcion());							
			log.add(fila);
		}	
		// 5. Setea variables del template
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_log", log);
		// 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();
		
	
	}

}
