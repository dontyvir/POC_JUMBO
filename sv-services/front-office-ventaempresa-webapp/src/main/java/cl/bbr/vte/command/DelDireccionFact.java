package cl.bbr.vte.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.utils.Cod_error;

/**
 * <p>Comando que elimina una direccion de facturación.</p>
 * <p>Como parámetro obligatorio se necesita el identificador único de la dirección de facturación.</p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class DelDireccionFact extends Command {

	/**
	 * Elimina una direcion de facturación
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	public void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("dis_er");
		logger.debug("UrlError: " + UrlError);

		ForwardParameters fp = new ForwardParameters();
		//fp.add(arg0.getParameterMap());

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		try {

			// 2.1 revision de parametros obligatorios
			ArrayList campos = new ArrayList();
			campos.add("iddir");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException(
						"Falta parámetro obligatorio.");
			}

			//2.2 obtiene parametros desde el request
			long iddir = Long.parseLong(arg0.getParameter("iddir"));

			//2.3 log de parametros y valores
			logger.debug("iddir: " + iddir);

			/*
			 * 3. Procesamiento Principal
			 */
			BizDelegate biz = new BizDelegate();

			boolean result = biz.delDirFacturacion(iddir);
			logger.debug("Se elimino una direccion: " + iddir);
			fp.add("iddir", iddir + "");

		} catch (ParametroObligatorioException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", Cod_error.GEN_FALTAN_PARA );
			UrlError = UrlError + fp.forward();
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		} catch (VteException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", Cod_error.GEN_SQL_NO_SAVE );
			UrlError = UrlError + fp.forward();
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		} catch (Exception e) {
			logger.warn("Controlando excepción de sistema: " + e.getMessage());
			e.printStackTrace();
			throw new SystemException(e);
		}

		// 4. Redirecciona salida
		// Recupera pagina desde web.xml

		// Si hay URL como parametro
		String url_destino = arg0.getParameter("url");
		if( url_destino == null || url_destino.equals("") ) {
			url_destino = getServletConfig().getInitParameter("dis_ok");  
		}		
		
		String dis_ok = url_destino;
		logger.debug("Redireccionando a: " + dis_ok);
		arg1.sendRedirect(dis_ok);
	}

}