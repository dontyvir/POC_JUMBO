package cl.bbr.fo.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;

import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.log.Logging;

/**
 * Comando que permite modificar direcciones de despacho de los clientes
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class AddressUpdate extends Command {

	/**
	 * Modificar direcciones de despacho de los clientes.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */	
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {		
				
			// Se recupera la sesión del usuario
			HttpSession session = arg0.getSession();				
	
			// Se revisan que los parámetros mínimos existan
			if ( !validateParametersLocal(arg0) ) {
				this.getLogger().error("Faltan parámetros mínimos (AddressUpdate): " + Cod_error.REG_FALTAN_PARA);
				session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);
				return;
			}
	
			// Instacia del bizdelegate
			Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
			
			BizDelegate biz = new BizDelegate();
			
			DireccionesDTO dir = new DireccionesDTO();
			
			dir.setCom_id(Long.parseLong((arg0.getParameter("comuna"))));		
			dir.setTipo_calle(Long.parseLong((arg0.getParameter("tipo_calle"))));	
			dir.setId_cliente(cliente_id.longValue());		
			dir.setAlias(arg0.getParameter("alias"));
			dir.setCalle(arg0.getParameter("calle"));
			dir.setNumero(arg0.getParameter("numero"));
			if( arg0.getParameter("departamento") != null )
				dir.setDepto(arg0.getParameter("departamento"));
			else
				dir.setDepto( "" );
			dir.setComentarios(arg0.getParameter("comentario"));
			dir.setId(Long.parseLong((arg0.getParameter("dir_id"))));
			
			biz.clienteUpdateDireccion(dir);
	
			// Redirecciona ok
			// Recupera pagina desde web.xml
			String dis_ok = getServletConfig().getInitParameter("dis_ok");
			arg1.sendRedirect(dis_ok);
		}
		catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException(e);
		}		

	}

	/**
	 * Valida parámetros mínimos necesarios
	 * 
	 * @param arg0	Request recibido desde el navegador
	 * @return		True: ok, False: faltan parámetros
	 */	
	private boolean validateParametersLocal(HttpServletRequest arg0) {
		Logging logger = this.getLogger();

		ArrayList campos = new ArrayList();
		campos.add("dir_id");
		campos.add("alias");
		campos.add("tipo_calle");
		campos.add("calle");
		campos.add("numero");
		campos.add("region");
		campos.add("comuna");

		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null || arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo + " en AddressUpdate");
				arg0.setAttribute("cod_error", Cod_error.GEN_FALTAN_PARA);
				return false;
			}
		}

		return true;

	}
	
}