package cl.bbr.fo.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.FuncionalException;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.log.Logging;

/**
 * 
 * Desabilita el envío de mails para la dupla rut-email
 * 
 * @author CAF
 *
 */
public class DesinscribeMail extends Command {

	/**
	 * Desabilita el envío de mails para la dupla rut-email
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws FuncionalException, Exception {

		try {
			String dis_err = getServletConfig().getInitParameter("dis_err");
			String dis_ok = getServletConfig().getInitParameter("dis_ok");
			boolean flag = false;
			//ResourceBundle rb = ResourceBundle.getBundle("fo");

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Se revisan que los parámetros mínimos existan
			if (ValidateParametersLocal(arg0, arg1) == false) {
				this.getLogger().error("Faltan parámetros mínimos (DesinscribeMail): "+ Cod_error.REG_FALTAN_PARA);
				session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);				
				return;
			}			
		
			// Se cargan los parámetros del request
			long rut  = Long.parseLong(arg0.getParameter("rut").replaceAll("\\.",""));
			String dv = arg0.getParameter("dv");
			String email = arg0.getParameter("email");
			String dominio = arg0.getParameter("dominio");
			
			this.getLogger().info("Rut:" + rut + "  Email:" + email + "@" + dominio);
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			// Se genera un cliente DTO
			ClienteDTO cliente;

			//Carga los datos del cliente en el objeto
			cliente = new ClienteDTO();
			cliente.setRut(rut);
			cliente.setDv(dv);
			cliente.setEmail(email + '@' + dominio);
			
			try {
				flag = biz.clienteDesinscribeMail(cliente);
				if (flag)
					arg1.sendRedirect(dis_ok);
				else
					arg1.sendRedirect(dis_err);
			} catch (Exception ex) {
				this.getLogger().error("DesinscribeMail RUT:" + rut, ex);
				getServletConfig().getServletContext().getRequestDispatcher( dis_err ).forward(arg0, arg1);
			}
		} catch (Exception ex) {
			this.getLogger().error("Problemas generales", ex);
			throw new SystemException(ex);
		}

	}

	/**
	 * Validación de campos de ingreso
	 * 
	 * @param arg0 HttpServletRequest
	 * @param arg1 HttpServletResponse
	 * @return true o false
	 */
	private boolean ValidateParametersLocal(HttpServletRequest arg0,
			HttpServletResponse arg1) {
		Logging logger = this.getLogger();

		ArrayList campos = new ArrayList();
		campos.add("rut");
		campos.add("dv");
		campos.add("email");
		campos.add("dominio");

		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null
					|| arg0.getParameter(campo).compareTo("") == 0 ) {
				logger.error("Falta parámetro: " + campo + " en DesinscribeMail");
				arg0.setAttribute("cod_error", Cod_error.GEN_FALTAN_PARA);
				return false;
			}
		}

		return true;

	}

}