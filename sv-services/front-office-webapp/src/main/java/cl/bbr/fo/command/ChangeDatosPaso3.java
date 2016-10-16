package cl.bbr.fo.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.log.Logging;

/**
 * Permite la actualización de email y celular del cliente.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ChangeDatosPaso3 extends Command {

	/**
	 * Instancia del logger inicializado en el command
	 */
	Logging logger = this.getLogger();

	/**
	 * Método que actualiza email y celular de un cliente
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		HttpSession session = arg0.getSession();
				
		try {

			// Se revisan que los parámetros mínimos existan
			if (ValidateParametersLocal(arg0, arg1) == false) {
				logger.error("Faltan parámetros mínimos (ChangeDatospaso3): " + Cod_error.REG_FALTAN_PARA);
				session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA);
				ErrorTaskLocal(arg0, arg1);
				return;
			}
			
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			
			// Recupera pagina desde web.xml
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("dis_ok");
			this.getLogger().info( "Template:" + pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
			
			//Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();	
			
			// Cargan los datos
			String email1 = arg0.getParameter("email1");
			String dominio1 = arg0.getParameter("dominio1");
			String codigo2 = arg0.getParameter("fon_cod_2");
			String telefono2 = arg0.getParameter("fon_num_2");
		
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			// actualiza datos cliente
			biz.clienteChangeDatosPaso3( Long.parseLong(session.getAttribute("ses_cli_id").toString()), email1 + "@" + dominio1, codigo2, telefono2 );
			
			IValueSet top = new ValueSet();
			top.setVariable("{email_cliente}", email1 + "@" + dominio1);
			session.setAttribute("ses_cli_email", email1 + "@" + dominio1);
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			this.getLogger().error("ChangeDatosPaso3 ID:" + Long.parseLong(session.getAttribute("ses_cli_id").toString()),e);
			String dis_err = getServletConfig().getInitParameter("dis_err");
			getServletConfig().getServletContext().getRequestDispatcher( dis_err ).forward(arg0, arg1);
		}

	}
	
	/**
	 * Método que valida el ingreso de los parámetros
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 */
	private boolean ValidateParametersLocal(HttpServletRequest arg0,
			HttpServletResponse arg1) {
		Logging logger = this.getLogger();

		ArrayList campos = new ArrayList();
		campos.add("email1");
		campos.add("dominio1");
		campos.add("email2");
		campos.add("dominio2");
		
		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null || arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo + " en ChangeDatosPaso3");
				return false;
			}
		}

		return true;

	}

	/**
	 * Maneja los errores que se producen
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * 
	 * @throws ServletException,
	 *             IOException
	 */
	private void ErrorTaskLocal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws ServletException, IOException {
		
		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();
		String cod_error = (String) session.getAttribute("cod_error");
		String dis_url = "";
		if (cod_error == Cod_error.GEN_FALTAN_PARA) {
			dis_url = getServletConfig().getInitParameter("dis_er_para");
			session.setAttribute("cod_error", cod_error);
		} else {
			dis_url = getServletConfig().getInitParameter("dis_er");
			session.setAttribute("cod_error", cod_error );
		}
		arg1.sendRedirect(dis_url);

	}

}
