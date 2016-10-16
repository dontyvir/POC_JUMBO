package cl.bbr.vte.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.ModProdCotizacionesDTO;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.utils.Cod_error;

/**
 * <p>Comando que permite modificar datos(cantidad) de un producto asociado a una cotizacion.</p> 
 * <p>Se valida que ciertos par�metros sean de caracter obligatorio.</p> 
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class UpdProdCotizacion extends Command {

	/**
	 * MOdifica la cantidad de un producto asciado a una cotizacion
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	public void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// 1. Par�metros de inicializaci�n servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("dis_er");
		logger.debug("UrlError: " + UrlError);
		
		ForwardParameters fp = new ForwardParameters();
		//fp.add( arg0.getParameterMap() );

		// 2. Procesa par�metros del request
		logger.debug("Procesando par�metros...");

		try {

			// 2.1 revision de parametros obligatorios
			ArrayList campos = new ArrayList();
			campos.add("id");
			campos.add("cantidad");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException(
						"Falta par�metro obligatorio.");
			}

			//2.2 obtiene parametros desde el request
			long id = Long.parseLong(arg0.getParameter("id"));
			double cantidad = Double.parseDouble(arg0.getParameter("cantidad"));
			
			//2.3 log de parametros y valores
			logger.debug("id: " + id);

			/*
			 * 3. Procesamiento Principal
			 */
			BizDelegate biz = new BizDelegate();
			ModProdCotizacionesDTO modPro = new ModProdCotizacionesDTO();
			modPro.setDetcot_id(id);
			modPro.setDetcot_cantidad(cantidad);
			boolean result = biz.updCantProductoCotizacion(modPro);
			logger.debug("Se modifico un producto del carro de compra: " + id);

		} catch (ParametroObligatorioException e) {
			logger.warn("Controlando excepci�n de negocios: " + e.getMessage());
			fp.add("cod_error", Cod_error.GEN_FALTAN_PARA );
			UrlError = UrlError + fp.forward();
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		} catch (VteException e) {
			logger.warn("Controlando excepci�n de negocios: " + e.getMessage());
			fp.add("cod_error", Cod_error.GEN_SQL_NO_SAVE );
			UrlError = UrlError + fp.forward();
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		} catch (Exception e) {
			logger.warn("Controlando excepci�n de sistema: " + e.getMessage());
			e.printStackTrace();
			throw new SystemException(e);
		}

		// 4. Redirecciona salida
		// Recupera pagina desde web.xml

		String dis_ok = getServletConfig().getInitParameter("dis_ok");
		logger.debug("Redireccionando a: " + dis_ok);
		arg1.sendRedirect(dis_ok);
	}

}