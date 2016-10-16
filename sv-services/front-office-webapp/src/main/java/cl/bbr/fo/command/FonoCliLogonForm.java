package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.util.Cod_error;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * LogonForm es un Servlet que despliega el formulario de conexión de los
 * usuarios.
 * 
 * @author BBRI
 *  
 */
public class FonoCliLogonForm extends FonoCommand {

	/**
	 * Despliega el formulario de conexión.
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
        
        borrarSesion(session);
		
		ResourceBundle rb = ResourceBundle.getBundle("fo");
		
		// Recupera pagina desde web.xml
		String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
		TemplateLoader load = new TemplateLoader(pag_form);
		ITemplate tem = load.getTemplate();

		IValueSet top = new ValueSet();

		// Se obtiene código de error si existe
		String cod_error = (String)session.getAttribute("cod_error");

		if (cod_error != null ) {
			if( cod_error.compareTo(Cod_error.CLI_NO_EXISTE) == 0) {
				top.setVariable("{err_mensaje}", rb.getString("logonCli.mensaje.rut") );
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

    /**
     * @param session
     */
    private void borrarSesion( HttpSession session ) {
        if ( session.getAttribute("ses_cli_id") != null ) {
            session.removeAttribute("ses_cli_id");
        }
        if ( session.getAttribute("ses_cli_nombre") != null ) {
            session.removeAttribute("ses_cli_nombre");
        }
        if ( session.getAttribute("ses_cli_nombre_pila") != null ) {
            session.removeAttribute("ses_cli_nombre_pila");
        }
        if ( session.getAttribute("ses_cli_rut") != null ) {
            session.removeAttribute("ses_cli_rut");
        }
        if ( session.getAttribute("ses_cli_dv") != null ) {
            session.removeAttribute("ses_cli_dv");
        }
        if ( session.getAttribute("ses_cli_email") != null ) {
            session.removeAttribute("ses_cli_email");
        }
        if ( session.getAttribute("ses_cli_confiable") != null ) {
            session.removeAttribute("ses_cli_confiable");
        }
        
        if ( session.getAttribute("ses_loc_id") != null ) {
            session.removeAttribute("ses_loc_id");
        }
        if ( session.getAttribute("ses_dir_id") != null ) {
            session.removeAttribute("ses_dir_id");
        }
        if ( session.getAttribute("ses_dir_alias") != null ) {
            session.removeAttribute("ses_dir_alias");
        }
        if ( session.getAttribute("ses_zona_id") != null ) {
            session.removeAttribute("ses_zona_id");
        }
        if ( session.getAttribute("ses_forma_despacho") != null ) {
            session.removeAttribute("ses_forma_despacho");
        }
        
        if ( session.getAttribute("ses_promo_tcp") != null ) {
            session.removeAttribute("ses_promo_tcp");
        }
        
    }

}