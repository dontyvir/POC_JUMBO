package cl.bbr.fo.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.log.Logging;

/**
 * DeleteList es un Servlet que permite eliminar una lista de compra (sólo internet).
 * <p>
 * Comportamiento:
 * <ul>
 * <li>elimina la listaen la BD.</li>
 * </ul>
 * </p>
 * 
 * @author CAF
 * 
 */
public class DeleteList extends Command {

	/**
	 * Permite eliminar una lista de compra.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		//variable guarda error
		String mensajeSistema = "";
//		String codError = "";
		try {
			// Carga properties
//			ResourceBundle rb = ResourceBundle.getBundle("fo");			

			// Recupera la sesión del usuario
//			HttpSession session = arg0.getSession();
			
			// Se revisan que los parámetros mínimos existan
			if ( !validateParametersLocal(arg0) ) {
				this.getLogger().error("Faltan parámetros mínimos (DeleteList): " + Cod_error.REG_FALTAN_PARA);
//				codError = Cod_error.REG_FALTAN_PARA;
				mensajeSistema = "No se pudo eliminar la lista, inténtelo nuevamente";
			} else {
				// Se cargan los parámetros del request a variables locales
				int id_lista = Integer.parseInt(arg0.getParameter("id_lista").toString());

				this.getLogger().debug("Elimina lista ( id:" + id_lista);			
				
				// Instacia del bizdelegate
				BizDelegate biz = new BizDelegate();
			
				int res = biz.carroComprasDeleteList(id_lista);
			
				if (res == 0) {
					mensajeSistema = "OK";
				} else {
					mensajeSistema = "No se pudo eliminar la lista, inténtelo nuevamente";
					this.getLogger().debug("No se pudo eliminar la lista, inténtelo nuevamente");
				}
			}
		} catch (Exception e) {
			this.getLogger().error(e);
			
			mensajeSistema = "Ocurrió un problema con el servidor";
			this.getLogger().error("SaveList - Ocurrió un problema con el servidor");
		}
		
		arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        
        //vemos cual es el mensaje a desplegar
        arg1.getWriter().write("<lista>");
        arg1.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        arg1.getWriter().write("</lista>");
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
		campos.add("id_lista");

		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null || arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo + " en DeleteList");
				return false;
			}
		}
		return true;
	}
}