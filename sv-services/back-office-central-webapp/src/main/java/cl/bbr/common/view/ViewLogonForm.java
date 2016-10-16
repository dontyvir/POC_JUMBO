package cl.bbr.common.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega página de Login
 * @author jsepulveda
 * @param error (opcional)
 */
public class ViewLogonForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String paramRc = "";
		String msg	= "";
		long id_local = -1;
		
		// 1. Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
	
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		if (req.getParameter("rc") != null) {
			paramRc = req.getParameter("rc");
			logger.debug("paramRc: " + paramRc);
		}
		
		
		View salida = new View(res);

		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		if (paramRc.equals("1"))
			msg = "Usuario y/o contraseña incorrecta";
		else if (paramRc.equals("2"))
			msg = "Debe autenticarse para ingresar al sistema";
		else if (paramRc.equals("3"))
			msg = "Error: usuario autenticado en LDAP pero no dado de alta en este sistema";		
		else
			msg = "";

		top.setVariable("{err_msg}", msg);
		
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();
					
	}
	
}
