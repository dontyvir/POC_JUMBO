package cl.bbr.fo.command;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.log.Logging;

/**
 * Comando que permite agregar nuevas direcciones de despacho a los clientes
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class AddressAdd extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {
				
		try {
			// Se recupera la sesión del usuario
			HttpSession session = arg0.getSession();		
			
			// Se revisan que los parámetros mínimos existan
			if (validateParametersLocal(arg0) == false) {
				this.getLogger().error("Faltan parámetros mínimos (AddressAdd): " + Cod_error.REG_FALTAN_PARA);
				session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);
				return;
			}		
	
			/*
			 * Se inserta una nueva dirección de despacho
			 */
			long com_id = Long.parseLong(arg0.getParameter("comuna"));
			long tipo_calle = Long.parseLong(arg0.getParameter("tipo_calle"));
			long cli_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
			String alias = arg0.getParameter("alias");
			String calle = arg0.getParameter("calle");
			String numero = arg0.getParameter("numero");
			String depto = "";
			if( arg0.getParameter("departamento") != null )
				depto = arg0.getParameter("departamento");
			String comentarios = arg0.getParameter("comentario");
			//String estado = null;
			String estado = "A";
		
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
	
			DireccionesDTO dir = new DireccionesDTO( -1, com_id, tipo_calle, cli_id, alias, calle, numero, depto, comentarios,-1, estado );
			
			biz.clienteInsertDireccion(dir);
			
			// Recupera pagina desde web.xml
			arg1.sendRedirect(getServletConfig().getInitParameter("dis_ok"));

		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException(e);
		}
			
	}

	/**
	 * Valida parámetros mínimos necesarios
	 * 
	 * @param arg0	Request recibido desde el navegador
	 * @param arg1	Response recibido desde el navegador
	 * @return		True: ok, False: faltan parámetros
	 */
	private boolean validateParametersLocal(HttpServletRequest arg0) {
		Logging logger = this.getLogger();
		ArrayList campos = new ArrayList();
		campos.add("alias");
		campos.add("tipo_calle");
		campos.add("calle");
		campos.add("numero");
		campos.add("region");
		campos.add("comuna");

		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null
					|| arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo + " en AddressAdd");
				arg0.setAttribute("cod_error", Cod_error.GEN_FALTAN_PARA);
				return false;
			}
		}
		return true;
	}

	/**
	 * Gatilla los eventos al existir error
	 * 
	 * @param arg0	Request recibido desde el navegador
	 * @param arg1	Response recibido desde el navegador
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void ErrorTaskLocal(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {

		String cod_error = (String) arg0.getAttribute("cod_error");
		String dis_url = "";
		if (cod_error == Cod_error.GEN_FALTAN_PARA) {
			dis_url = getServletConfig().getInitParameter("dis_er_para");
		} else {
			dis_url = getServletConfig().getInitParameter("dis_er");
		}
		arg1.sendRedirect(dis_url);
	}
}