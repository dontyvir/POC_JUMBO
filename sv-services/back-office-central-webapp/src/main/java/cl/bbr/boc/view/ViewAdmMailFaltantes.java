package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.informes.dto.MailSustitutosFaltantesDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Entrega los datos para la configuración de Casos
 * 
 * @author imoyano
 */
public class ViewAdmMailFaltantes extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewAdmMailFaltantes Execute");

	    //Objetos para pintar la pantalla
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		BizDelegate bizDelegate = new BizDelegate();

		List mails = bizDelegate.getMailsSyF();
		ArrayList mailsList = new ArrayList();
		
		for (int i = 0; i < mails.size(); i++) {			
			IValueSet fila = new ValueSet();
			MailSustitutosFaltantesDTO mail = (MailSustitutosFaltantesDTO) mails.get(i);
			fila.setVariable("{id}",String.valueOf(mail.getId()));			
			fila.setVariable("{nombre_completo}", mail.getNombre() + " " + mail.getApellido());
			fila.setVariable("{nombres}", mail.getNombre());
			fila.setVariable("{apellidos}", mail.getApellido());
			fila.setVariable("{mail}", mail.getMail());
			if (mail.getActivado().equals("1")) {
			    fila.setVariable("{estado}","Si");
			} else {
			    fila.setVariable("{estado}","No");
			}
			fila.setVariable("{est_activado}",mail.getActivado());
			fila.setVariable("{fc_creacion}", Formatos.frmFecha(mail.getFechaIngreso()));
			fila.setVariable("{fc_modificacion}", Formatos.frmFecha(mail.getFechaModificacion()));
			mailsList.add(fila);
		}
		if (mailsList.size() > 0) {
		    top.setVariable("{mje1}","");
		} else {
		    top.setVariable("{mje1}","No existen mail's para listar.");
		}
		
		top.setDynamicValueSets("MAILS_SYF", mailsList);
		
		top.setVariable("{cant_mails}", String.valueOf(mailsList.size()));
		
		top.setVariable("{mensaje_error}", "");
		top.setVariable("{mensaje}", "");
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
		
		logger.debug("Fin ViewAdmMailFaltantes Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
