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
import cl.bbr.fo.exception.FuncionalException;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;

/**
 * Envia correo lista de productos en excel
 * 
 * @author aperez
 *  
 */
public class EnviarCorreoComprasProList extends Command {
//20112012 VMatheu	
	protected void execute(HttpServletRequest req, HttpServletResponse res)
			throws FuncionalException, Exception {

		HttpSession session = req.getSession();

		BizDelegate biz = new BizDelegate();
		ResourceBundle rb = ResourceBundle.getBundle("fo");

		//variable que almacena la respuesta OK en caso que se procese con
		// exito, sino ERROR.
		String respuesta = null;
		//variable que almacena el email del cliente jumbo
		String emailTitular = "";
		//variable que almacena el valor de la solicitud
		String solicitud = req.getParameter("solicitud");

		try {
			Long rut = new Long(session.getAttribute("ses_cli_rut").toString());

			//Objeto que contiene datos del cliente jumbo
			ClienteDTO cliente = null;

			if (solicitud.equals("email")) {

				cliente = biz.clienteGetByRut(rut.longValue());
				emailTitular = cliente.getEmail();
				respuesta = "OK";
			} else if (solicitud.equals("enviarCorreo")) {

				cliente = biz.clienteGetByRut(rut.longValue());

				String correoEnvio = req.getParameter("correoEnvio");
				String tupla = req.getParameter("tupla");
				String ses_loc_id = session.getAttribute("ses_loc_id")
						.toString();
				long ses_cli_id = Long.parseLong(session.getAttribute(
						"ses_cli_id").toString());
				long ses_cli_rut = Long.parseLong(session.getAttribute(
						"ses_cli_rut").toString());

				String pag_form = rb.getString("conf.dir.html") + ""
						+ getServletConfig().getInitParameter("list_mail");

				String http = rb.getString("mail.lista.productos.www.jumbo");

				String urlDescargaExcel = http
				+ "FO/EnviarCorreoExcelDespliegue?solicitud=enviarExcel&tupla="
						+ tupla + "&ses_loc_id=" + ses_loc_id + "&ses_cli_id="
						+ ses_cli_id + "&ses_cli_rut=" + ses_cli_rut;
//--20112012 VMatheu				
				// Carga el template html
				//========================
				TemplateLoader load = new TemplateLoader(pag_form);
				ITemplate tem = load.getTemplate();
				IValueSet top = new ValueSet();

				top.setVariable("{nombre_cliente}", cliente.getNombre() + " "
						+ cliente.getApellido_pat());
				top.setVariable("{urlDescargaExcel}", urlDescargaExcel);

				String result = tem.toString(top);

				MailDTO mail = new MailDTO();
				mail
						.setFsm_subject(rb
								.getString("mail.lista.productos.asunto"));
				mail.setFsm_data(result);
				mail.setFsm_remite(rb.getString("mail.lista.productos.remite"));
				mail.setFsm_destina(correoEnvio);
				biz.addMail(mail);

				respuesta = "OK";

			}

		} catch (Exception e) {
			respuesta = "ERROR";
			this.getLogger().error(e);
		}

		//		se genera mensaje de salida JSON
		res.setContentType("text/xml");
		res.setHeader("Cache-Control", "no-cache");
		res.setCharacterEncoding("UTF-8");

		//vemos cual es el mensaje a desplegar
		res.getWriter().write("<comuna>");
		res.getWriter().write("<respuesta>" + respuesta + "</respuesta>");
		if (solicitud.equals("email")) {
			res.getWriter().write(
					"<emailTitular>" + emailTitular + "</emailTitular>");
		}

		res.getWriter().write("</comuna>");

	}
}