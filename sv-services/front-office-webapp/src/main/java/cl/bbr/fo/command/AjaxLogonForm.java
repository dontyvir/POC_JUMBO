package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.exception.CommandException;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que entrega el header respectivo para el home del sitio
 * 
 * @author carriagada it4b
 * 
 */
public class AjaxLogonForm extends Command {
    

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		try {
            //arg1.setCharacterEncoding("UTF-8");
            arg1.setContentType("text/html; charset=utf-8");

			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
            
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
			
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			this.getLogger().debug( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();		
			
			//Setea Header Invitado o Registrado
            
            if (session.getAttribute("ses_cli_id") == null)
                top.setVariable("{nombre_cliente}", "Invitado");
            else
                if (session.getAttribute("ses_cli_nombre") != null) {
                	if(session.getAttribute("ses_cli_nombre").toString().indexOf(" ") != -1) {
                		top.setVariable("{nombre_cliente}", "Hola "+session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
                	} else {
                    top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
                	}           	
                } else {
                    top.setVariable("{nombre_cliente}", "Invitado");
                }
                    
            
            List viewLogin = new ArrayList();
            IValueSet fila = new ValueSet();
            
            if ((session.getAttribute("ses_comuna_cliente") != null) && ("".equalsIgnoreCase( session.getAttribute("ses_comuna_cliente").toString()) ) ) {
                fila.setVariable("{comuna_usuario}", NOMBRE_COMUNA_DONALD);
                fila.setVariable("{comuna_usuario_id}", "0");
            } else if (session.getAttribute("ses_comuna_cliente") != null){
                String[] loc = session.getAttribute("ses_comuna_cliente").toString().split("-=-");
                fila.setVariable("{comuna_usuario}", "" + loc[2]);
                fila.setVariable("{comuna_usuario_id}", "" + loc[1]);
            } else {
                fila.setVariable("{comuna_usuario}", NOMBRE_COMUNA_DONALD);
                fila.setVariable("{comuna_usuario_id}", "0"); 
            }
            
            viewLogin.add(fila);
            
            if ((session.getAttribute("ses_cli_id") == null) || ("1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() ))) {
                top.setDynamicValueSets("MOSTRAR_NO_LOGUEADO", viewLogin); 
            } else {
                if ((session.getAttribute("ses_cli_rut") != null)  && (session.getAttribute("ses_cli_rut").equals("123123")))
                    top.setDynamicValueSets("MOSTRAR_LOGUEADO_INVITADO", viewLogin);
                else
                    top.setDynamicValueSets("MOSTRAR_LOGUEADO", viewLogin); 
            } 
            
            String result = tem.toString(top);
            out.print(result);
            
        } catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		} 
	}
}