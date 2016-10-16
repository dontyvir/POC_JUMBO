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
import cl.bbr.vte.empresas.dto.ComprXEmpDTO;
import cl.bbr.vte.exception.VteException;

/**
 * 
 * <p>Este comando permite ingresar un nueva nueva relación empresa comprador.</p>
 * <p>Se valida que ciertos parámetros sean de caracter obligatorio.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class AddNewEmpCom extends Command {

	/**
	 * Inserta una nueva relacion empresa / comprador
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
		fp.add("cpr_id", Long.parseLong(arg0.getParameter("id_comprador_tmp"))+"");
		fp.add( arg0.getParameterMap() );

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");		

		try {

			// 2.1 revision de parametros obligatorios

			// Se revisan que los parámetros mínimos existan
			ArrayList campos = new ArrayList();
			campos.add("select_emp");
			campos.add("id_comprador_tmp");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException( "Falta parámetro obligatorio." );
			}	

		     //2.2 obtiene parametros desde el request
			long emp_id  = Long.parseLong(arg0.getParameter("select_emp"));
			
			long cpr_id = Long.parseLong(session.getAttribute("id_comprador_tmp")+"");			
			
			ComprXEmpDTO empXcom = new ComprXEmpDTO();
			
			empXcom.setId_empresa(emp_id);
			empXcom.setId_comprador(cpr_id);
			
			//Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			boolean result = biz.addRelEmpresaComprador(empXcom);
			logger.info("Se creo la relacion empresa / comprador: " + result); 
	
			
		} catch (ParametroObligatorioException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
			UrlError = UrlError + fp.forward();
			logger.info("URL: " + UrlError);
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		} catch (VteException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
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
			String dis_ok = getServletConfig().getInitParameter("dis_ok");
			logger.debug("Redireccionando a: " + dis_ok);
			arg1.sendRedirect(dis_ok);			
	}

}