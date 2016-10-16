package cl.bbr.fo.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.log.Logging;

/**
 * SaveList es un Servlet que permite guardar los productos del carro en una lista de compra.
 * <p>
 * Comportamiento:
 * <ul>
 * <li>revisa si el nombre de la lista existe en la BD.</li>
 * <li>si no existe almacena la lista con el nombre especificadoe
 * <li>Si existe se devuelve un mensaje.</li>
 * </ul>
 * </p>
 * 
 * @author CAF
 * 
 */
public class SaveList extends Command {

	/**
	 * Permite almacenar lista de compra.
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
		this.getLogger().debug("Ingresa a Guardar Lista");			
		try {
			// Carga properties
//			ResourceBundle rb = ResourceBundle.getBundle("fo");			

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Se revisan que los parámetros mínimos existan
			if ( !validateParametersLocal(arg0) ) {
				this.getLogger().error("Faltan parámetros mínimos (SaveList): " + Cod_error.REG_FALTAN_PARA);
//				codError = Cod_error.REG_FALTAN_PARA;
				mensajeSistema = "Ingrese el nombre de la lista";
			} else {
				// Se cargan los parámetros del request a variables locales
				String nombre = arg0.getParameter("nombre").toString();

				this.getLogger().debug("Almacena lista ( nombre:" + nombre);			
				
				// Instacia del bizdelegate
				BizDelegate biz = new BizDelegate();
			
				int res = biz.carroComprasSaveList(nombre, Long.parseLong(session.getAttribute("ses_cli_id").toString()) );
			
				if (res == 0) {
					mensajeSistema = "OK";
				} else if (res == 1){
					mensajeSistema = "El nombre ingresado ya existe como una lista";
					this.getLogger().debug("El nombre ingresado ya existe como una lista");
				} else {
					mensajeSistema = "No pudimos almacenar la lista, inténtelo nuevamente";
					this.getLogger().debug("No pudimos almacenar la lista, intentelo nuevamente");
				}
			}
		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
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
		campos.add("nombre");

		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null || arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo + " en SaveList");
				return false;
			}
		}
		return true;
	}
}