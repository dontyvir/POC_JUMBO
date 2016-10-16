package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.vte.utils.Cod_error;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * ViewLogonForm despliega el formulario de conexión a los compradores.
 * 
 * <p>Si hay un mensaje de error lo presenta en pantalla.</p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ViewLogonForm extends Command {

	/**
	 * Despliega el formulario de conexión para los compradores.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();

		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();
		
		ResourceBundle rb = ResourceBundle.getBundle("vte");
		
		// Recupera pagina desde web.xml
		String pag_form = this.path_html + getServletConfig().getInitParameter("pag_form");
		logger.info( "Template:"+pag_form );
		TemplateLoader load = new TemplateLoader(pag_form);
		ITemplate tem = load.getTemplate();

		IValueSet top = new ValueSet();

		
        //Despliega el Banner de inicio dependiendo del lugar desde donde se loguea
		top.setVariable("{bt_inicio}", "bt-iniciocontactotelefono.swf");
		if (session.getAttribute("bt_inicio") != null){
		    session.removeAttribute("bt_inicio");
		}
		session.setAttribute("bt_inicio", "bt-iniciocontactotelefono.swf");
		

		// Se almacena tracking en este sector

		// Se obtiene código de error si existe
		String cod_error = (String)session.getAttribute("cod_error");

		if (cod_error != null ) {
			logger.warn("Cod_error:"+cod_error);
			if( cod_error.compareTo(Cod_error.CLI_NO_EXISTE) == 0) {
				top.setVariable("{err_mensaje}", rb.getString("logon.mensaje.rut") );
			}
			else if( cod_error.compareTo(Cod_error.CLI_CLAVE_INVALIDA) == 0) {
				top.setVariable("{err_mensaje}", rb.getString("logon.mensaje.clave") );
			}
			else if( cod_error.compareTo(Cod_error.GEN_ERR_PERMISOS) == 0) {
				top.setVariable("{err_mensaje}", rb.getString("logon.mensaje.nopermisos") );
			} else {
				top.setVariable("{err_mensaje}", rb.getString("general.mensaje.error") + " (" + cod_error + ")" );
			}			
			session.removeAttribute("cod_error");
		} else {
			top.setVariable("{err_mensaje}", "");
		}

		String rut = "";
		String dv = "";
		logger.info("Validando Rut ......");
		if( arg0.getParameter("rut") != null )
			rut = arg0.getParameter("rut").toString(); 
		//if( arg0.getParameter("dv") != null )
		//	dv = arg0.getParameter("dv").toString();
		top.setVariable("{rut}", rut);			
		//top.setVariable("{dv}", dv);		
		//top.setVariable("{err_mensaje}", "");
		String result = tem.toString(top);

		out.print(result);
				
	}

}