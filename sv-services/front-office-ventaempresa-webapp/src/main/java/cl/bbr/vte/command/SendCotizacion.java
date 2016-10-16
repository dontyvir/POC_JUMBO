package cl.bbr.vte.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;

/**
 * 
 * Permite agregar al detalle de la cotizacion (descripcion, precio unitario, cantidad)
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class SendCotizacion extends Command {

	/**
	 * Agrega al detalle de la cotizacion (descripcion, precio unitario, cantidad)
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	public void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		// Obtiene sessión
		HttpSession session = arg0.getSession();

		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("dis_er");
		logger.debug("UrlError: " + UrlError);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");		

		try {

			//Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			CotizacionesDTO cotizacion = new CotizacionesDTO();
			cotizacion.setCot_id(Long.parseLong(session.getAttribute("ses_cot_id").toString()));
			cotizacion.setCot_obs(arg0.getParameter("comentario"));
			cotizacion.setCot_fueramix(arg0.getParameter("prod_mix"));
			biz.SendCotizacion( cotizacion );
			session.setAttribute("ses_prod_mix", "" );
			session.setAttribute("ses_exito_envia_cot", "1" );
			logger.info("Se han modificado los productos del detalle de cotizacion.");
			
		} catch (Exception e) {
			logger.warn("Controlando excepción de sistema: " + e.getMessage());
			e.printStackTrace();
			throw new SystemException(e);
		}
			
		// 3. Redirecciona salida
		// Recupera pagina desde web.xml
		String dis_ok = getServletConfig().getInitParameter("dis_ok");
		logger.debug("Redireccionando a: " + dis_ok );
		arg1.sendRedirect(dis_ok);
	}

}