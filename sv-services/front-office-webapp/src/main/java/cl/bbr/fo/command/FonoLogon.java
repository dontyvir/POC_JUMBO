package cl.bbr.fo.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.fonocompras.dto.UsuarioDTO;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.log.Logging;

/**
 * Logon es un Servlet que permite a los ejecutivos Fono compras conectarse al sitio.
 * 
 * @author BBRI
 * 
 */
public class FonoLogon extends FonoCommand {

	/**
	 * Permite la conexión de los ejecutivos.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		try {

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();

			// Se revisan que los parámetros mínimos existan
			if (ValidateParametersLocal(arg0, arg1) == false) {
				this.getLogger().error("Faltan parámetros mínimos (FonoLogon): " + Cod_error.REG_FALTAN_PARA);
				session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);
				return;
			}			

			// Se cargan los parámetros del request a variables locales
			String rut_s = arg0.getParameter("rut").replaceAll("\\.","");
			String rut = rut_s;
			//String dv = arg0.getParameter("dv");
			String clave = arg0.getParameter("clave").toString();

			this.getLogger().info("Intento conexión Fono Compras ( Usuario:" + rut + " )");

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			UsuarioDTO ejecutivo = biz.ejecutivoGetByRut( rut + "" );
			
			// Se revisa si existe el cliente
			if( ejecutivo == null ) {
				this.getLogger().info("Usuario no existe");
				session.setAttribute("cod_error", Cod_error.CLI_NO_EXISTE );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);				
				return;
			}			

			// Se valida si las claves coinciden
			this.getLogger().debug("clave e" +  ejecutivo.getPass() );
			this.getLogger().debug("clave f" +  Utils.encriptarFO(clave) );
			if( ejecutivo.getPass().compareTo(Utils.encriptarFO(clave)) != 0 ) {
				this.getLogger().info("Problemas con clave ingresada");
				session.setAttribute("cod_error", Cod_error.CLI_CLAVE_INVALIDA );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);
				return;
			}			
			
			// Se almacena el nombre del ejecutivo en la sesión
			session.setAttribute("ses_eje_id", ejecutivo.getId_usuario() + "" );
			session.setAttribute("ses_eje_nombre", ejecutivo.getNombre() + " " + ejecutivo.getApellido_p() );
			session.setAttribute("ses_eje_rut", ejecutivo.getLogin() + "" );
            
            session.setAttribute("ses_id", "");
            session.setAttribute("ses_invitado_id", "0");
            session.setAttribute("ses_comuna_cliente", "");

			// Redirecciona ok
			String dis_ok;
			String url = arg0.getParameter("url");
			if (url == null) {
				// Recupera pagina desde web.xml
				dis_ok = getServletConfig().getInitParameter("dis_ok");
			} else {
				dis_ok = url;
			}

			arg1.sendRedirect(dis_ok);

		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException( e );
		}

	}

	/**
	 * Valida parámetros mínimos necesarios
	 * 
	 * @param arg0	Request recibido desde el navegador
	 * @param arg1	Response recibido desde el navegador
	 * @return		True: ok, False: faltan parámetros
	 */
	private boolean ValidateParametersLocal(HttpServletRequest arg0,
			HttpServletResponse arg1) {
		Logging logger = this.getLogger();

		ArrayList campos = new ArrayList();
		campos.add("rut");
		//campos.add("dv");
		campos.add("clave");

		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null || arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo + " en : FonoLogon");
				return false;
			}
		}

		return true;

	}

}