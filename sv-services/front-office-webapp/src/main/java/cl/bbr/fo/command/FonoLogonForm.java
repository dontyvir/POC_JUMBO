package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.util.Cod_error;
import cl.bbr.fo.util.Tracking_web;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * LogonForm es un Servlet que despliega el formulario de conexión de los
 * ejecutivos de fono compras.
 * 
 * @author BBRI
 *  
 */
public class FonoLogonForm extends FonoCommand {

	/**
	 * Despliega el formulario de conexión para fono compras
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
		
		ResourceBundle rb = ResourceBundle.getBundle("fo");
		
		// Recupera pagina desde web.xml
		String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
		TemplateLoader load = new TemplateLoader(pag_form);
		ITemplate tem = load.getTemplate();

		IValueSet top = new ValueSet();

		// Se almacena tracking en este sector
		//Tracking_web.saveTracking( "FonoLogon", arg0 );

		// Se obtiene código de error si existe
		String cod_error = (String)session.getAttribute("cod_error");

		if (cod_error != null ) {
			if( cod_error.compareTo(Cod_error.CLI_NO_EXISTE) == 0) {
				top.setVariable("{err_mensaje}", rb.getString("logon.mensaje.rut") );
			} else if( cod_error.compareTo(Cod_error.REG_FALTAN_PARA) == 0) {
				top.setVariable("{err_mensaje}", rb.getString("general.mensaje.error.parametro") );
			} else if( cod_error.compareTo(Cod_error.CLI_CLAVE_INVALIDA) == 0) {
				top.setVariable("{err_mensaje}", rb.getString("logon.mensaje.clave") );
			} else {
				top.setVariable("{err_mensaje}", rb.getString("general.mensaje.error.nn") + " (" + cod_error + ")" );
			}
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
				
	}

}