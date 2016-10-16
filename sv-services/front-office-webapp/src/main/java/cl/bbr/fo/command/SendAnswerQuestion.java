package cl.bbr.fo.command;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;

/**
 * SendAnswerQuestion permite realizar el envío de la respuesta a la pregunta sercreta vía mail
 * ingresar el id del cliente. El sistema enviará a su mail la respuesta a la pregunta.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class SendAnswerQuestion extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		HttpSession session = arg0.getSession();		
		ResourceBundle rb = ResourceBundle.getBundle("fo");
		
		try {
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			// recuperar identificador de la región de consulta			
			long id  = Long.parseLong(arg0.getParameter("id").replaceAll("\\.",""));
			
			ClienteDTO rutaux = (ClienteDTO)biz.clienteGetById( id );
	
			this.getLogger().info("Usuario solicita clave RUT:" + rutaux.getRut());			

			if (rutaux != null && rutaux.getRespuesta() != null){
				// Recupera pagina desde web.xml
				String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("arc_mail");
				// Carga el template html
				TemplateLoader load = new TemplateLoader(pag_form);
				ITemplate tem = load.getTemplate();
				IValueSet top = new ValueSet();
				
				top.setVariable("{respuesta}", rutaux.getRespuesta());
				top.setVariable("{nombre_cliente}", rutaux.getNombre()+" " + rutaux.getApellido_pat() );
				String result = tem.toString(top);
				// Se envía mail al cliente
				MailDTO mail = new MailDTO();
				mail.setFsm_subject( rb.getString("mail.pregunta.subject") );
				mail.setFsm_data( result );
				mail.setFsm_destina( rutaux.getEmail() );
				mail.setFsm_remite( rb.getString( "mail.rec_clave.remite" ) );
				biz.addMail( mail );
				
                getServletConfig().getServletContext().getRequestDispatcher(getServletConfig().getInitParameter("dis_ok")).forward(arg0, arg1);
                
			} else {				
                getServletConfig().getServletContext().getRequestDispatcher(getServletConfig().getInitParameter("dis_er")).forward(arg0, arg1);
			}
						
		} catch (Exception e) {
			this.getLogger().error( "Problema al solicitar clave", e);
			session.removeAttribute("cod_error");
			getServletConfig().getServletContext().getRequestDispatcher(getServletConfig().getInitParameter("dis_er")).forward(arg0, arg1);
		}			
	}
}
