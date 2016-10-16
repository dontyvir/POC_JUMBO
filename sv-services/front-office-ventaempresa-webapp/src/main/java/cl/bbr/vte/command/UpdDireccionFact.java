package cl.bbr.vte.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.utils.Cod_error;

/**
 * <p>Comando que actualiza los datos de la dirección de facturación.</p>
 * <p>Se valida que ciertos parámetros sean de caracter obligatorio.</p> 
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class UpdDireccionFact extends Command {

	/**
	 * Modifica los datos de la dirección de facturación
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
			campos.add("fono1");
			campos.add("ciudad");			
			campos.add("iddir");
			campos.add("sucursales");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException(
						"Falta parámetro obligatorio.");
			}

			//2.2 obtiene parametros desde el request
			String cliente_id = arg0.getParameter("sucursales");
			
			String estado = Constantes.ESTADO_ACTIVADO;

			BizDelegate biz = new BizDelegate();

			DirFacturacionDTO dir = new DirFacturacionDTO();

			dir.setDfac_com_id(Long.parseLong((arg0.getParameter("comuna"))));
			dir.setDfac_tip_id(Long
					.parseLong((arg0.getParameter("tipo_calle"))));
			dir.setDfac_cli_id(Long.parseLong(cliente_id));
			dir.setDfac_alias(arg0.getParameter("lugar"));
			dir.setDfac_calle(arg0.getParameter("calle"));
			dir.setDfac_numero(arg0.getParameter("numero"));
			dir.setDfac_depto(arg0.getParameter("depto"));
			dir.setDfac_comentarios(arg0.getParameter("observacion"));
			dir.setDfac_id(Long.parseLong((arg0.getParameter("iddir"))));
			dir.setDfac_estado(estado);

			String fono1 = arg0.getParameter("fono1");
			String fono2 = arg0.getParameter("fono2");
			String fono3 = arg0.getParameter("fono3");
			String ciudad = arg0.getParameter("ciudad");
			dir.setDfac_fono1(fono1);
			if( fono2 != null  )
				dir.setDfac_fono2(fono2);
			else
				dir.setDfac_fono2("");
			if( fono3 != null  )
				dir.setDfac_fono3(fono3);
			else
				dir.setDfac_fono3("");
			dir.setDfac_ciudad(ciudad);
			
			biz.modDirFacturacion(dir);

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