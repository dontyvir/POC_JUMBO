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
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.utils.Cod_error;

/**
 * <p>Comando que elimina la relacion Sucursal / Comprador.</p> 
 * <p>Se valida que ciertos par�metros sean de caracter obligatorio.</p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class DelComSuc extends Command {

	/**
	 * Elimina la relacion Sucursal / Comprador 
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	public void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// Obtiene sessi�n
		HttpSession session = arg0.getSession();
		
		// 1. Par�metros de inicializaci�n servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("dis_er");
		logger.debug("UrlError: " + UrlError);
		
		ForwardParameters fp = new ForwardParameters();
		fp.add("cpr_id", session.getAttribute("id_comprador_tmp")+"");
		//fp.add( arg0.getParameterMap() );

		// 2. Procesa par�metros del request
		logger.debug("Procesando par�metros...");

		try {

			// 2.1 revision de parametros obligatorios
			ArrayList campos = new ArrayList();
			campos.add("idsuc");
			campos.add("idcom");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException(
						"Falta par�metro obligatorio.");
			}

			//2.2 obtiene parametros desde el request
			long idsuc = Long.parseLong(arg0.getParameter("idsuc"));
			long idcom = Long.parseLong(arg0.getParameter("idcom"));

			//2.3 log de parametros y valores
			logger.debug("idsuc: " + idsuc);

			/*
			 * 3. Procesamiento Principal
			 */
			BizDelegate biz = new BizDelegate();
			
			ComprXSucDTO comXSuc = new ComprXSucDTO();
			
			comXSuc.setId_comprador(idcom);
			comXSuc.setId_sucursal(idsuc);

			boolean result = biz.delRelSucursalComprador(comXSuc);


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