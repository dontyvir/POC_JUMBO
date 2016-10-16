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
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.CompradoresDTO;

/**
 * <p>Comando que actualiza la clave y el estado del comprador .</p>
 * <p>Se valida que ciertos parámetros sean de caracter obligatorio.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ChangePassword extends Command {

	/**
	 * Modifica datos de un comprador
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
			
			//2.1 revision de parametros obligatorios
			ArrayList campos = new ArrayList();
			campos.add("clave1");
			campos.add("clave2");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException(
						"Falta parámetro obligatorio.");
			}
			
			// Cargan los datos
			String clave1 = arg0.getParameter("clave1");
		
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			String clave_hash = Utils.encriptar( clave1 );
			
			CompradoresDTO comp = new CompradoresDTO();
			
			comp.setCpr_id(Long.parseLong(session.getAttribute("ses_com_id").toString()));
			comp.setCpr_pass(clave_hash);
			comp.setCpr_estado(Constantes.ESTADO_ACTIVADO);
			
			biz.compradorChangePass(comp);
			
		} catch (ParametroObligatorioException e) {
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
		
		// Redirecciona ok
		// Recupera pagina desde web.xml
		String dis_ok = getServletConfig().getInitParameter("dis_ok");
		arg1.sendRedirect(dis_ok);		
	}
}