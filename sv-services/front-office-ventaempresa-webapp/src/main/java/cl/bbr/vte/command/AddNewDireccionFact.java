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
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.utils.Cod_error;

/**
 * <p>Este comando permite agrega una nueva direccion de facturación a la sucursal.</p>
 * <p>Se valida que ciertos parámetros sean de caracter obligatorio.</p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class AddNewDireccionFact extends Command {

	/**
	 * Inserta una nueva direccion de facturación
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

		ForwardParameters fp = new ForwardParameters();
		//fp.add( arg0.getParameterMap() );

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		try {

			// 2.1 revision de parametros obligatorios

			// Se revisan que los parámetros mínimos existan
			ArrayList campos = new ArrayList();
			campos.add("lugar");
			campos.add("tipo_calle");
			campos.add("calle");
			campos.add("numero");
			campos.add("fono1");
			campos.add("ciudad");
			campos.add("region");
			campos.add("comuna");
			campos.add("sucursales");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException(
						"Falta parámetro obligatorio.");
			}

			//2.2 obtiene parametros desde el request
			String lugar = arg0.getParameter("lugar");
			long tipo_calle = Long.parseLong(arg0.getParameter("tipo_calle"));
			String calle = arg0.getParameter("calle");
			String numero = arg0.getParameter("numero");
			String fono1 = arg0.getParameter("fono1");
			String fono2 = arg0.getParameter("fono2");
			String fono3 = arg0.getParameter("fono3");
			String ciudad = arg0.getParameter("ciudad");
			String depto = arg0.getParameter("depto");
			long comuna = Long.parseLong(arg0.getParameter("comuna"));
			String observacion = arg0.getParameter("observacion");
			String estado = "A";
			long cli_id = Long.parseLong(arg0.getParameter("sucursales"));

			/*
			 * 3. Procesamiento Principal
			 */			
			
			DirFacturacionDTO direccion = new DirFacturacionDTO();

			direccion.setDfac_com_id(comuna);
			direccion.setDfac_tip_id(tipo_calle);
			direccion.setDfac_cli_id(cli_id);
			direccion.setDfac_alias(lugar);
			direccion.setDfac_calle(calle);
			direccion.setDfac_numero(numero);
			direccion.setDfac_depto(depto);
			direccion.setDfac_comentarios(observacion);
			direccion.setDfac_estado(estado);
			direccion.setDfac_fono1(fono1);
			if( fono2 != null  )
				direccion.setDfac_fono2(fono2);
			else
				direccion.setDfac_fono2("");
			if( fono3 != null  )
				direccion.setDfac_fono3(fono3);
			else
				direccion.setDfac_fono3("");
			direccion.setDfac_ciudad(ciudad);

			//Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			long result = biz.insDirFacturacion(direccion);
			logger.info("Se insertó la direccion.");

		} catch (ParametroObligatorioException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", Cod_error.GEN_FALTAN_PARA );
			UrlError = UrlError + fp.forward();
			getServletConfig().getServletContext().getRequestDispatcher(
					UrlError).forward(arg0, arg1);
			return;
		} catch (VteException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", Cod_error.GEN_SQL_NO_SAVE );
			UrlError = UrlError + fp.forward();
			getServletConfig().getServletContext().getRequestDispatcher(
					UrlError).forward(arg0, arg1);
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