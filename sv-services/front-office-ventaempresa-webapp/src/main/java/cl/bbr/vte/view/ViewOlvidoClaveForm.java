package cl.bbr.vte.view;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * ViewOlvidoClaveForm despliega el formulario para ingresar rut del comprador.
 * 
 * <p>Si hay un mensaje de error lo presenta en pantalla.</p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ViewOlvidoClaveForm extends Command {

	/**
	 * Despliega el formulario para ingresar Rut del comprador.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {

		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();

	    // Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();

		// Recupera pagina desde web.xml
		String pag_form = this.path_html + getServletConfig().getInitParameter("pag_form");
		logger.info( "Template:"+pag_form );
		TemplateLoader load = new TemplateLoader(pag_form);
		ITemplate tem = load.getTemplate();

		IValueSet top = new ValueSet();
		
        //Despliega el Banner de inicio dependiendo del lugar desde donde se loguea
		top.setVariable("{bt_inicio}", session.getAttribute("bt_inicio").toString());

		if( arg0.getParameter("cod_error") != null ){
			top.setVariable("{msg_error}", "1");
			top.setVariable("{desc_msg_error}", "Rut ingresado no existe.");
		}
		
		String result = tem.toString(top);

		out.print(result);
	}

}