package cl.bbr.fo.command;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.log.Logging;

/**
 * Permite la actualización de los datos de contacto del cliente.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ChangeDatosContacto extends Command {

	/**
	 * Instancia del logger inicializado en el command
	 */
	Logging logger = this.getLogger();

	/**
	 * Método que actualiza los datos de contacto de un cliente
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
				logger.error("Faltan parámetros mínimos (ChangeDatosContacto): " + Cod_error.REG_FALTAN_PARA);
				session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA);
				ErrorTaskLocal(arg0, arg1);
				return;
			}
			
			// Cargan los datos
            
            Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
            
			String email1 = arg0.getParameter("email1").toString() + "";
			String dominio1 = arg0.getParameter("dominio1").toString() + "";
            String codigo1 = arg0.getParameter("fon_cod_1").toString() + "";
            String telefono1 = arg0.getParameter("fon_num_1").toString() + "";
			String codigo2 = arg0.getParameter("fon_cod_2").toString() + "";
			String telefono2 = arg0.getParameter("fon_num_2").toString() + "";
            String codigo3 = arg0.getParameter("fon_cod_3").toString() + "";
            String telefono3 = arg0.getParameter("fon_num_3").toString() + "";
            
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			// actualiza datos cliente
            ClienteDTO cli = new ClienteDTO();
            cli.setId(cliente_id.longValue());
            cli.setEmail(email1 + "@" + dominio1);
            cli.setFon_cod_1(codigo1);
            cli.setFon_num_1(telefono1);
            cli.setFon_cod_2(codigo2);
            cli.setFon_num_2(telefono2);
            cli.setFon_cod_3(codigo3);
            cli.setFon_num_3(telefono3);
            
			biz.updateDatosContactoCliente( cli );
			
		} catch (Exception e) {
			this.getLogger().error("ChangeDatosContacto ID:" + Long.parseLong(session.getAttribute("ses_cli_id").toString()),e);
			String dis_err = getServletConfig().getInitParameter("dis_err");
			getServletConfig().getServletContext().getRequestDispatcher( dis_err ).forward(arg0, arg1);
		}

	}
	
	/**
	 * Método que valida el ingreso de los parámetroys
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
		campos.add("fon_cod_1");
        campos.add("fon_num_1");
		
		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null || arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo + " en ChangeDatosContacto");
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
