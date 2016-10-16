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

/**
 * 
 * <p>Este comando permite ingresar un nueva nueva relaci�n comprador sucursal.</p>
 * <P>Se valida que exista el identificador �nico del comprador.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class AddNewComSuc extends Command {

	/**
	 * Inserta una nueva relacion sucursal / comprador
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
		//fp.add("cpr_id", session.getAttribute("id_comprador_tmp")+"");
		//fp.add( arg0.getParameterMap() );

		// 2. Procesa par�metros del request
		logger.debug("Procesando par�metros...");		

		try {

			// 2.1 revision de parametros obligatorios

			// Se revisan que los par�metros m�nimos existan
			ArrayList campos = new ArrayList();
			campos.add("id_cpr");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException( "Falta par�metro obligatorio." );
			}	

		     //2.2 obtiene parametros desde el request
			long idsuc  = Long.parseLong(session.getAttribute("session_idsuc")+"");
			
			long id_cpr = Long.parseLong(arg0.getParameter("id_cpr"));
			
			ComprXSucDTO comXsuc = new ComprXSucDTO();
			
			comXsuc.setId_sucursal(idsuc);
			comXsuc.setId_comprador(id_cpr);
			
			//Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			boolean result = biz.addRelSucursalComprador(comXsuc);
			logger.info("Se creo la relacion sucursal / comprador: " + result); 
	
			
		} catch (ParametroObligatorioException e) {
			logger.warn("Controlando excepci�n de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
			UrlError = UrlError + fp.forward();
			logger.info("URL: " + UrlError);
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		} catch (VteException e) {
			logger.warn("Controlando excepci�n de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
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