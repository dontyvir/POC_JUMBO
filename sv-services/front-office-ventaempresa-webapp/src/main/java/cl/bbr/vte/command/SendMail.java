package cl.bbr.vte.command;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.empresas.dto.MailDTO;
import cl.bbr.vte.empresas.dto.SolRegDTO;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.bizdelegate.BizDelegate;

/**
 * Este comando permite enviar el mail con los datos de la solicitud de registro
 * de empresas a la persona responsable de este tema.
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class SendMail extends Command {

	/**
	 * Envia Mail al ejecutivo
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		ResourceBundle rb = ResourceBundle.getBundle("vte");
		
		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("dis_er");
		logger.debug("UrlError: " + UrlError);
		
		ForwardParameters fp = new ForwardParameters();
		

		// Envia mail
		try {
		
			// Validar parámetros mínimos
			ArrayList campos = new ArrayList();
			campos.add("nombre_contacto");		
			campos.add("cargo");
			campos.add("fono_contacto1");
			campos.add("email");
			campos.add("rutemp");
			campos.add("nombre_empresa");
			campos.add("razon");
			campos.add("rubro");
			campos.add("tamano_empresa");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException( "Falta Parámetro obligatorio" );
			}
		
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			// Recupera pagina desde web.xml 
			String mail_tpl = this.path_html + getServletConfig().getInitParameter("arc_mail");
			// Carga el template html para el ejecutivo
			TemplateLoader mail_load = new TemplateLoader(mail_tpl);
			ITemplate mail_tem = mail_load.getTemplate();
			IValueSet mail_top = new ValueSet();
			
			String mail_tpl_cli = this.path_html + getServletConfig().getInitParameter("arc_mail_cli");
			// Carga el template html para el cliente
			TemplateLoader mail_load_cli = new TemplateLoader(mail_tpl_cli);
			ITemplate mail_tem_cli = mail_load_cli.getTemplate();
			
			
			//DATOS EMPRESA
			String nombre_contacto = arg0.getParameter("nombre_contacto");
			String cargo = arg0.getParameter("cargo");
			String fono_contacto1 = arg0.getParameter("fono_contacto1");

			String fono_contacto2 = "";
			if (arg0.getParameter("fono_contacto2") != null) {
				fono_contacto2 = arg0.getParameter("fono_contacto2");
			}

			String fono_contacto3 = "";
			if (arg0.getParameter("fono_contacto3") != null) {
				fono_contacto3 = arg0.getParameter("fono_contacto3");
			}

			String email = arg0.getParameter("email");
			String rutemp = arg0.getParameter("rutemp");
			String nombre_empresa = arg0.getParameter("nombre_empresa");
			String razon = arg0.getParameter("razon");
			String rubro = arg0.getParameter("rubro");
			long tamano_empresa = Long.parseLong(arg0
					.getParameter("tamano_empresa"));
			String dir_com = arg0.getParameter("dir_com");
			String fon_cod_1 = arg0.getParameter("fon_cod_1");
			String fon_cod_2 = arg0.getParameter("fon_cod_2");
			String fon_cod_3 = arg0.getParameter("fon_cod_3");
			String ciudad = arg0.getParameter("ciudad");
			String comuna = arg0.getParameter("comuna");

			
			
			mail_top.setVariable("{nombre_contacto}", nombre_contacto + "");
			mail_top.setVariable("{cargo}", cargo + "");
			mail_top.setVariable("{fono_contacto1}", fono_contacto1);
			mail_top.setVariable("{fono_contacto2}", fono_contacto2);
			mail_top.setVariable("{fono_contacto3}", fono_contacto3);
			mail_top.setVariable("{email}", email);
			mail_top.setVariable("{rutemp}", rutemp + "");
			mail_top.setVariable("{nombre_empresa}", nombre_empresa);
			mail_top.setVariable("{razon}", razon);
			mail_top.setVariable("{rubro}", rubro);
			
			String text_tam_emp = "";
			if(tamano_empresa == 1){
				text_tam_emp = "Menos de 5 empleados";
			}else if(tamano_empresa == 2){
				text_tam_emp = "Más de 5 y menos de 10 empleados";
			}else if(tamano_empresa == 3){
				text_tam_emp = "Más de 10 empleados";
			}
			
			mail_top.setVariable("{tamano_empresa}", text_tam_emp + "");
			mail_top.setVariable("{direccion_comercial}", dir_com + "");
			mail_top.setVariable("{fon_cod_1}", fon_cod_1 + "");
			mail_top.setVariable("{fon_cod_2}", fon_cod_2 + "");
			mail_top.setVariable("{fon_cod_3}", fon_cod_3 + "");
			mail_top.setVariable("{ciudad}", ciudad + "");
			mail_top.setVariable("{comuna}", comuna + "");

			String mail_result = mail_tem.toString(mail_top);

			MailDTO mail = new MailDTO();
			
			//SE ENVIA MAIL AL EJECUTIVO
			
			SolRegDTO solreg = new SolRegDTO();
			solreg.setNom_contacto(nombre_contacto);
			solreg.setCargo(cargo);
			solreg.setTel_cod1(fon_cod_1);
			solreg.setTel_num1(fono_contacto1);
			solreg.setTel_cod2(fon_cod_2);
			solreg.setTel_num2(fono_contacto2);
			solreg.setTel_cod3(fon_cod_3);
			solreg.setTel_num3(fono_contacto3);
			solreg.setEmail(email);
			solreg.setRut_emp(rutemp);
			solreg.setNom_emp(nombre_empresa);
			solreg.setRaz_social(razon);
			solreg.setGiro(rubro);
			solreg.setTam_empresa(tamano_empresa);
			solreg.setDir_comercial(dir_com);
			solreg.setEstado("I");
			solreg.setCiudad(ciudad);
			solreg.setComuna(comuna);

			long id = 0;
			id = biz.setMailSolRegistro(solreg);//Se graba la solicitud de registro
			
			mail.setFsm_subject(rb.getString("mail.empresas.registro.subject")+" "+ id+", "+rb.getString("mail.empresas.registro.subject2")+" "+nombre_empresa);
			mail.setFsm_data(mail_result);
			mail.setFsm_destina(rb.getString("mail.empresas.registro.to"));
			mail.setFsm_remite(rb.getString("mail.empresas.registro.to"));
			
			biz.insMail(mail);//Se graba el mail para el ejecutivo
			//FIN SE ENVIA MAIL AL EJECUTIVO

			
			//SE ENVIA MAIL AL CLIENTE
			MailDTO mail_cli = new MailDTO();
			String mail_result_cli = mail_tem_cli.toString(mail_top);
			mail_cli.setFsm_subject(rb.getString("mail.empresas.registro.subject")+" "+ id+", "+rb.getString("mail.empresas.registro.subject2")+" "+nombre_empresa);
			mail_cli.setFsm_data(mail_result_cli);
			mail_cli.setFsm_remite(rb.getString("mail.empresas.registro.to"));
			mail_cli.setFsm_destina(arg0.getParameter("email"));
			biz.insMail(mail_cli);//Se graba el mail para el cliente
			//FIN SE ENVIA MAIL AL CLIENTE
			
		} catch (ParametroObligatorioException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
			UrlError = UrlError + fp.forward();
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		} catch (VteException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
			UrlError = UrlError + fp.forward();
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		} catch (Exception e) {
			logger.warn("Controlando excepción de sistema: " + e.getMessage());
			e.printStackTrace();
			throw new SystemException(e);
		}

		String dis_ok = getServletConfig().getInitParameter("dis_ok");
		arg1.sendRedirect(dis_ok);

	}

}