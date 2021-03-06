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
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.utils.Cod_error;

/**
 * <P>Este comando permite agregar una nueva direccion de despacho a la sucursal.</p>
 * <p>Se valida que ciertos par�metros sean de caracter obligatorio.</p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class AddNewDireccionDesp extends Command {

	/**
	 * Inserta una nueva direccion de despacho
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
		//fp.add( arg0.getParameterMap() );

		// 2. Procesa par�metros del request
		logger.debug("Procesando par�metros...");		
		
		try {

			// 2.1 revision de parametros obligatorios

			// Se revisan que los par�metros m�nimos existan
			ArrayList campos = new ArrayList();
			campos.add("lugar");
			campos.add("tipo_calle");
			campos.add("calle");
			campos.add("numero");
			campos.add("region");
			campos.add("comuna");
			campos.add("sucursales");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException(
						"Falta par�metro obligatorio.");
			}

			//2.2 obtiene parametros desde el request
			String lugar = arg0.getParameter("lugar");
			long tipo_calle = Long.parseLong(arg0.getParameter("tipo_calle"));
			String calle = arg0.getParameter("calle");
			String numero = arg0.getParameter("numero");
			String depto = arg0.getParameter("depto");
			long region = Long.parseLong(arg0.getParameter("region"));
			long comuna = Long.parseLong(arg0.getParameter("comuna"));
			String observacion = arg0.getParameter("observacion");
			String otracom = arg0.getParameter("nomComuna");
			String estado = Constantes.ESTADO_ACTIVADO;
			long cli_id = Long.parseLong(arg0.getParameter("sucursales"));

			/*
			 * 3. Procesamiento Principal
			 */
			
			DireccionesDTO direccion = new DireccionesDTO();

			direccion.setCom_id(comuna);
			direccion.setTip_id(tipo_calle);
			direccion.setCli_id(cli_id);
			direccion.setAlias(lugar);
			direccion.setCalle(calle);
			direccion.setNumero(numero);
			direccion.setDepto(depto);
			direccion.setComentarios(observacion);
			direccion.setReg_id(region);
			direccion.setEstado(estado);
			direccion.setOtra_comuna(otracom);

			//Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			long result = biz.insDirDespacho(direccion);
			logger.info("Se insert� la direccion.");

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