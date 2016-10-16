package cl.bbr.vte.command;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.SystemException;

/**
 * Este comando muestra un formulario de cambio de clave 
 * de empresas a la persona responsable de este tema.
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class ChangePasswordForm extends Command {

	/**
	 * Formulario de cambio de clave
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();

		ResourceBundle rb = ResourceBundle.getBundle("vte");
		
		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("dis_er");
		logger.debug("UrlError: " + UrlError);
		
		ForwardParameters fp = new ForwardParameters();
		

		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();		
		

		try {
		
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.path.html") + "" + getServletConfig().getInitParameter("pag_form");
			
			// Carga el template html
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
	
			IValueSet top = new ValueSet();
	
			top.setVariable("{nombre_cliente}", session.getAttribute("ses_com_nombre")  );
			
			// Recupera código de error
			String cod_error = (String)session.getAttribute("cod_error");
	
			if (cod_error != null ) {
				top.setVariable("{err_mensaje}", rb.getString("changepasswordform.mensaje.error") );
				session.removeAttribute("cod_error");			
			} else {
				top.setVariable("{err_mensaje}", "");
			}
	
			String rut = "";
			String dv = "";
			if( arg0.getParameter("rut") != null )
				rut = arg0.getParameter("rut").toString(); 
			if( arg0.getParameter("dv") != null )
				dv = arg0.getParameter("dv").toString();
			top.setVariable("{rut}", rut);			
			top.setVariable("{dv}", dv);		
			
			String result = tem.toString(top);
	
			out.print(result);

		} catch (Exception e) {
			logger.warn("Controlando excepción de sistema: " + e.getMessage());
			e.printStackTrace();
			throw new SystemException(e);
		}
	}

}