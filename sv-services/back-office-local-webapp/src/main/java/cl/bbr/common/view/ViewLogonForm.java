package cl.bbr.common.view;

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
import cl.bbr.jumbocl.usuarios.dto.LocalDTO;
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
		String nom_local = "";
		
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
		if (req.getParameter("id_local") != null) {
			id_local = Long.parseLong(req.getParameter("id_local"));
			logger.debug("id_local: " + id_local);
		}
		if (req.getParameter("nom_local") != null) {
			nom_local = req.getParameter("nom_local");
			logger.debug("nom_local: " + nom_local);
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
		else  if (paramRc.equals("4"))
			msg = "Error: El usuario no puede acceder a este local "+nom_local;		
		else
			msg = "";

		top.setVariable("{err_msg}", msg);
		
		// 4.0 Bizdelegator
		BizDelegate bizDelegate = new BizDelegate();
		//4.3 Listado de locales
		List lst_loc = null;	
		lst_loc = bizDelegate.getLocales();
		ArrayList locs = new ArrayList();
		
		for (int i = 0; i < lst_loc.size(); i++) {			
			IValueSet fila = new ValueSet();
			LocalDTO loc1 = (LocalDTO)lst_loc.get(i);
			fila.setVariable("{id_loc}",String.valueOf(loc1.getId_local()));
			fila.setVariable("{local}",loc1.getNom_local());
			if (id_local==loc1.getId_local())
				fila.setVariable("{sel_loc}", "selected");
			else
				fila.setVariable("{sel_loc}", "");
			locs.add(fila);
		}
		
		top.setDynamicValueSets("LOCALES", locs);
		
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();
		//res.sendRedirect(paramUrl);
	}
}
