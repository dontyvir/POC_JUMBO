package cl.bbr.vte.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.utils.Cod_error;

/**
 * <p>Comando que actualiza los datos de la dirección de despacho.</p>
 * <p>Se valida que ciertos parámetros sean de caracter obligatorio.</p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class UpdDireccionDesp extends Command {

	/**
	 * Modifica los datos de la dirección de despacho
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	public void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();

		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("dis_er");
		logger.debug("UrlError: " + UrlError);

		ForwardParameters fp = new ForwardParameters();
		//fp.add( arg0.getParameterMap() );

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		try {

			// 2.1 revision de parametros obligatorios
			ArrayList campos = new ArrayList();
			campos.add("comuna");
			campos.add("tipo_calle");
			campos.add("lugar");
			campos.add("calle");
			campos.add("numero");
			campos.add("iddir");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException(
						"Falta parámetro obligatorio.");
			}

			//2.2 obtiene parametros desde el request
			String cliente_id = arg0.getParameter("sucursales");


			/*
			 * 3. Procesamiento Principal
			 */

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			DireccionesDTO dir = new DireccionesDTO();

			dir.setCom_id(Long.parseLong((arg0.getParameter("comuna"))));
			dir.setTip_id(Long.parseLong((arg0.getParameter("tipo_calle"))));
			dir.setCli_id(Long.parseLong(cliente_id));
			dir.setAlias(arg0.getParameter("lugar"));
			dir.setCalle(arg0.getParameter("calle"));
			dir.setNumero(arg0.getParameter("numero"));
			if (arg0.getParameter("depto") != null)
				dir.setDepto(arg0.getParameter("depto"));
			else
				dir.setDepto("");
			dir.setComentarios(arg0.getParameter("observacion"));
			dir.setOtra_comuna(arg0.getParameter("otra"));
			dir.setId(Long.parseLong((arg0.getParameter("iddir"))));

			biz.modDirDespacho(dir);

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

		// Redirecciona ok
		// Recupera pagina desde web.xml
		String dis_ok = getServletConfig().getInitParameter("dis_ok");
		arg1.sendRedirect(dis_ok);

	}

}