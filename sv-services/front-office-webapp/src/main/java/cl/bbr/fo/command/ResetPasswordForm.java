package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import sun.misc.BASE64Encoder;
import cl.bbr.fo.bizdelegate.BizDelegate;

import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.common.utils.Utils;

/**
 * Servlet que permite modificar la clave. Para ello se necesita que el usuario
 * ingrese el rut y el sistema le enviará a su mail la nueva clave.
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ResetPasswordForm extends Command {

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
            throws Exception {

        // Recupera la sesión del usuario
        HttpSession session = arg0.getSession();
        String pag_form = "";
        int origen = 0;
        // Carga properties
        ResourceBundle rb_fo = ResourceBundle.getBundle("fo");

        // Recupera la salida para el servlet
        PrintWriter out = arg1.getWriter();

        try {

            // Recupera pagina desde web.xml
            origen = Integer.parseInt(arg0.getParameter("origen").toString());
            if (origen < 1){
                pag_form = rb_fo.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form0");
            }else{
                pag_form = rb_fo.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form1");
            }

            
            
            // Carga el template html
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();

            IValueSet top = new ValueSet();

            // Recupera código de error
            String cod_error = "";            
            if ( session.getAttribute("cod_error") != null ) {
                cod_error = (String)session.getAttribute("cod_error");
                session.removeAttribute("cod_error");
            }
            
            String rut = arg0.getParameter("rut").toString().replaceAll("\\.", "");
            
            // Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();

            ClienteDTO cliente = (ClienteDTO) biz.clienteGetByRut(Long.parseLong(rut));

            // Se revisa si existe el cliente
            if (cliente == null) {
                this.getLogger().info("Usuario no existe");
                cod_error = Cod_error.CLI_NO_EXISTE;
                top.setVariable("{pregunta}", "");
                top.setVariable("{id}", "");
            } else {
	        	  int port = arg0.getServerPort();
	        	  StringBuffer result = new StringBuffer();
	        	  result.append(arg0.getScheme()).append("://").append(arg0.getServerName());
	        	  if (port != 80) {
	        	    result.append(':').append(port);
	        	  }            	  
	            	String contextPath = result.toString()+"/FO";
	            	biz.recuperaClaveFO(cliente, contextPath);
            }

            if ( Cod_error.CLI_NO_EXISTE.equalsIgnoreCase( cod_error ) ) {
                top.setVariable("{err_mensaje}", "El RUT ingresado no existe.");                
            } else if ( Cod_error.CLI_RESPUESTA_INVALIDA.equalsIgnoreCase( cod_error ) ) {
                top.setVariable("{err_mensaje}", "Respuesta incorrecta.");                
            } else {
                top.setVariable("{err_mensaje}", "");
            }
            top.setVariable("{rut}", rut);
            String result = tem.toString(top);

            out.print(result);
        } catch (Exception e) {
            this.getLogger().error("Problema al solicitar clave", e);
            if (origen < 1){
                getServletConfig().getServletContext().getRequestDispatcher(getServletConfig().getInitParameter("dis_er0")).forward(arg0, arg1);
            }else{
                getServletConfig().getServletContext().getRequestDispatcher(getServletConfig().getInitParameter("dis_er1")).forward(arg0, arg1);
            }

        }

    }

}
