package cl.bbr.vte.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.MailDTO;

/**
 * <p>Comando que actualiza la clave y el estado de un comprador.</p>
 * <p>Se valida que ciertos parámetros sean de caracter obligatorio.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ResetPassword extends Command {

	/**
	 * Modifica la clave y el estado de un comprador
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	public void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();
		
		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();	
		

		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("dis_er");
		logger.debug("UrlError: " + UrlError);
		
		ResourceBundle rb = ResourceBundle.getBundle("vte");

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		
		// Recupera pagina desde web.xml
		String pag_form = this.path_html + getServletConfig().getInitParameter("arc_mail");
		// Carga el template html
		TemplateLoader load = new TemplateLoader(pag_form);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		try {
			
			//2.1 revision de parametros obligatorios
			ArrayList campos = new ArrayList();
			campos.add("id");
			campos.add("respuesta");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException(
						"Falta parámetro obligatorio.");
			}

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			
			long id  = Long.parseLong(arg0.getParameter("id"));
			
			CompradoresDTO comp = (CompradoresDTO)biz.getCompradoresById( id );

			if (comp != null 
				&& comp.getCpr_respuesta() != null 
				&& arg0.getParameter("respuesta") != null 
				&& comp.getCpr_respuesta().equals(arg0.getParameter("respuesta").toString())){
			
				String clave = Utils.genPassword(8);
				String clave_hash = Utils.encriptar( clave );
				
				comp.setCpr_id(Long.parseLong(arg0.getParameter("id")));
				comp.setCpr_pass(clave_hash);
				comp.setCpr_estado("C");
				
				biz.compradorChangePass(comp);

				//Mail para ser enviado al comprador
				top.setVariable("{nueva_clave}", clave );
				top.setVariable("{nombre_cliente}", comp.getCpr_nombres()+" " + comp.getCpr_ape_pat() );
				
				String result = tem.toString(top);
				
				// Se envía mail al cliente
				MailDTO mail = new MailDTO();
				mail.setFsm_subject( rb.getString("mail.rec_clave.subject") );
				mail.setFsm_data( result );
				mail.setFsm_destina( comp.getCpr_email() );
				mail.setFsm_remite( rb.getString( "mail.rec_clave.remite" ) );
				biz.insMail( mail );
				
				out.print( "1" );
			}else{
				out.print( "0" );				
			}			
			
		} catch (Exception e) {
			this.getLogger().error( "Problema al solicitar clave", e);
			out.print( "0" );
		}
		
	}
}