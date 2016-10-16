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
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.utils.Cod_error;

/**
 * <p>Comando que permite actualizar los datos de una sucursal.</p>
 * <p>Se valida que ciertos parámetros sean de caracter obligatorio.</p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class UpdSucursales extends Command {

	/**
	 * Modifica los datos de la sucursal
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
			campos.add("idsuc");
			campos.add("empresas");
			campos.add("rut");
			campos.add("dv");
			campos.add("nombre");
			campos.add("rsocial");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException(
						"Falta parámetro obligatorio.");
			}

			//2.2 obtiene parametros desde el request
			long idsuc = Long.parseLong(arg0.getParameter("idsuc"));
			long idemp = Long.parseLong(arg0.getParameter("empresas"));
			long rut = 	Long.parseLong(arg0.getParameter("rut"));
			String dv = arg0.getParameter("dv");
			String nombre = arg0.getParameter("nombre");
			String  social = arg0.getParameter("rsocial");
			String estado = arg0.getParameter("estados");
			String desc = arg0.getParameter("desc");
			String fono1 = arg0.getParameter("fono1");
			String fon_cod_1 = arg0.getParameter("fon_cod_1");
			String fono2 = arg0.getParameter("fono2");
			String fon_cod_2 = arg0.getParameter("fon_cod_2");
			String mail = arg0.getParameter("mail");
			String encargado = arg0.getParameter("encargado");
			if(encargado == null)
				encargado = "";			

			/*
			 * 3. Procesamiento Principal
			 */

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			SucursalesDTO suc = new SucursalesDTO();

			suc.setSuc_id(idsuc);	
			suc.setSuc_emp_id(idemp);
			suc.setSuc_rut(rut);
			suc.setSuc_dv(dv);
			suc.setSuc_nombre(nombre);
			suc.setSuc_razon(social);
			suc.setSuc_estado(estado);
			suc.setSuc_descr(desc);
			suc.setSuc_fono_num1(fono1);
			suc.setSuc_fono_cod1(fon_cod_1);
			suc.setSuc_fono_num2(fono2);
			suc.setSuc_fono_cod2(fon_cod_2);
			suc.setSuc_email(mail);
			suc.setSuc_pregunta(encargado);

			biz.setModSucursal(suc);

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