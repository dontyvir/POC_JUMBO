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
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.log.Logging;

/**
 * Permite el cambio de clave del cliente.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ChangeForgottenPassword extends Command {

	Logging logger = this.getLogger();
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		HttpSession session = arg0.getSession();
				
		try {
			// Se revisan que los parámetros mínimos existan
			if ( !validateParametersLocal(arg0)) {
				logger.error("Faltan parámetros mínimos (ChangeForgottenPassword): " + Cod_error.REG_FALTAN_PARA);
				session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA);
				ErrorTaskLocal(arg0, arg1);
				return;
			}

			// Cargan los datos
			String clave1 = arg0.getParameter("clave1");
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			String clave_hash = Utils.encriptarFO( clave1 );
			
			// Cambia clave del usuario
			long id  = Long.parseLong(arg0.getParameter("id").replaceAll("\\.",""));
			ClienteDTO rutaux = (ClienteDTO)biz.clienteGetById( id );
            
            //Antes de cambiar el password, debemos contrastar el rut ingresado al comienzo con el rut del cliente a modificar
            if ( arg0.getParameter("rut") == null || rutaux.getRut() != Long.parseLong(arg0.getParameter("rut")) ) {
                session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA);
                ErrorTaskLocal(arg0, arg1);
                return;
            }            
            
			biz.clienteChangePass( Long.parseLong(rutaux.getRut() + ""), clave_hash, "A" );
			biz.RecuperaClave_GuardaKeyCliente(rutaux.getId(), "");

            String dis_ok = getServletConfig().getInitParameter("dis_ok");
			session.removeAttribute("cod_error");
			arg1.sendRedirect(dis_ok);
		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			session.setAttribute("cod_error", Cod_error.GEN_SQL_NO_SAVE);
			ErrorTaskLocal(arg0, arg1);
			return;			
			//throw new CommandException( e );
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
	private boolean validateParametersLocal(HttpServletRequest arg0) {
		Logging logger = this.getLogger();

		ArrayList campos = new ArrayList();
		campos.add("clave1");
		campos.add("clave2");

		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null || arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo + " en ChangeForgottenPassword");
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
