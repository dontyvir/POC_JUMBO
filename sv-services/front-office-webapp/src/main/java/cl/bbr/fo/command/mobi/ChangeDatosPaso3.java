package cl.bbr.fo.command.mobi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.command.Command;
import cl.bbr.log.Logging;

/**
 * Permite la actualización de email y celular del cliente - mobile
 * 
 * @author imoyano
 * 
 */
public class ChangeDatosPaso3 extends Command {


	Logging logger = this.getLogger();

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		HttpSession session = arg0.getSession();
        String msg = "";
				
		try {
            
			// Cargan los datos
			String email1 = arg0.getParameter("email1");
			String dominio1 = arg0.getParameter("dominio1");
			String codigo2 = arg0.getParameter("fon_cod_2");
			String telefono2 = arg0.getParameter("fon_num_2");
		
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			// actualiza datos cliente
			biz.clienteChangeDatosPaso3( Long.parseLong(session.getAttribute("ses_cli_id").toString()), email1 + "@" + dominio1, codigo2, telefono2 );
			session.setAttribute("ses_cli_email", email1 + "@" + dominio1);
            msg = "OK";            

		} catch (Exception e) {
            msg = "Ocurrió un error al actualizar los datos";
		}
        
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        arg1.getWriter().write("<datos_objeto>");
        arg1.getWriter().write("<mensaje>" + msg + "</mensaje>");
        arg1.getWriter().write("</datos_objeto>");

	}
	


}
