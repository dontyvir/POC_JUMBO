package cl.bbr.vte.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.exception.VteException;

/**
 * <p>Comando que actualiza los datos de un comprador.</p>
 * <p>Se valida que ciertos parámetros sean de caracter obligatorio.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class UpdComprador extends Command {

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
			campos.add("cpr_id");
			campos.add("nombre");
			campos.add("paterno");
			campos.add("materno");
			campos.add("genero");
			campos.add("fono1");
			campos.add("email");
			campos.add("pregunta");
			campos.add("respuesta");
//campos.add("empresas");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException(
						"Falta parámetro obligatorio.");
			}
			
			//2.2 obtiene parametros desde el request
//long empresa_id = Long.parseLong(arg0.getParameter("empresas"));
			
			/*
			 * 3. Procesamiento Principal
			 */

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			CompradoresDTO comprador = new CompradoresDTO();
			
//comprador.setId_empresa(empresa_id);
			comprador.setCpr_id(Long.parseLong(arg0.getParameter("cpr_id")));
			comprador.setCpr_rut(Long.parseLong(arg0.getParameter("rut")));
			comprador.setCpr_dv(arg0.getParameter("dv"));
			comprador.setCpr_id(Long.parseLong(arg0.getParameter("cpr_id")));
			comprador.setCpr_nombres(arg0.getParameter("nombre"));
			comprador.setCpr_ape_pat(arg0.getParameter("paterno"));
			comprador.setCpr_ape_mat(arg0.getParameter("materno"));
			comprador.setCpr_genero(arg0.getParameter("genero"));
			comprador.setCpr_tipo("");
/*			if (arg0.getParameter("tipo") != null)
				comprador.setCpr_tipo("A");
			else
				comprador.setCpr_tipo("C");*/
			
			comprador.setCpr_fono1(arg0.getParameter("fono1"));
			comprador.setCpr_fono2(arg0.getParameter("fono2"));
			comprador.setCpr_fono3(arg0.getParameter("fono3"));
			comprador.setCpr_email(arg0.getParameter("email"));
			comprador.setCpr_estado(arg0.getParameter("estado"));
			comprador.setCpr_pregunta(arg0.getParameter("pregunta"));
			comprador.setCpr_respuesta(arg0.getParameter("respuesta"));
			if( arg0.getParameter("clave") != null && arg0.getParameter("clave").compareTo("") != 0 ) {
				comprador.setCpr_pass(Utils.encriptar( arg0.getParameter("clave")));
			}else{
			    comprador.setCpr_pass("");
			}
					
			biz.setModComprador(comprador);
			
		} catch (ParametroObligatorioException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
			UrlError = UrlError + fp.forward();
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
		
		// Redirecciona ok
		// Recupera pagina desde web.xml
		String dis_ok = getServletConfig().getInitParameter("dis_ok");
		arg1.sendRedirect(dis_ok);		
	}
}