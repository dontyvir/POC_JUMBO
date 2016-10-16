
package cl.bbr.common.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author BBRI
 *
 * Parámetros que recibe en el get/post:
 * @param url (obligatorio)
 * 
 * 
 */
public class ViewPrint extends Command {

		
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String url;
		
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);		
	
		// 2. Procesa parámetros del request
		if ( req.getParameter("url") == null ){
			throw new ParametroObligatorioException("url es null");
		}
		
		url	= req.getParameter("url");
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas	
		
		// 5. Setea variables del template
		top.setVariable("{url}",	url);
		
		// 6. Setea variables bloques
		
		// 7. Salida Final (se deja tal cual)
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	

	}
	
	
}
