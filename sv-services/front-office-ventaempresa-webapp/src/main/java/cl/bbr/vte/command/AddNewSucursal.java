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

/**
 * 
 * <p>Este comando permite ingresar una nueva sucursal a una empresa seleccionada.</p>
 * <p>Se valida que ciertos parámetros sean de caracter obligatorio.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class AddNewSucursal extends Command {   

	/**
	 * Inserta una nueva sucursal a una empresa
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
			ArrayList campos = new ArrayList();
			campos.add("empresas");
			campos.add("rut");
			campos.add("dv");
			campos.add("nombre");
			campos.add("rsocial");
			if (ValidateParameters(arg0, arg1, campos) == false ) {
				throw new ParametroObligatorioException( "Falta parámetro obligatorio." );
			}	
			
		     //2.2 obtiene parametros desde el request
			
			long rut = 0;
			if (arg0.getParameter("rut") != null && arg0.getParameter("rut").equals("") == false ) {
				rut  = Long.parseLong(arg0.getParameter("rut").replaceAll("\\.",""));
			}
			
			String dv = arg0.getParameter("dv");
			String nombre = arg0.getParameter("nombre");
			String social = arg0.getParameter("rsocial");
			String estado = arg0.getParameter("estados");
			long id_empresa = Long.parseLong(arg0.getParameter("empresas")); 
			String desc = arg0.getParameter("desc");
			String fono1 = arg0.getParameter("fono1");
			String fon_cod_1 = arg0.getParameter("fon_cod_1");
			String fono2 = arg0.getParameter("fono2");
			String fon_cod_2 = arg0.getParameter("fon_cod_2");
			String mail = arg0.getParameter("mail");
			String encargado = arg0.getParameter("encargado");
			if(encargado == null)
				encargado = "";
			
			SucursalesDTO suc = new SucursalesDTO();
			
			
			suc.setSuc_emp_id(id_empresa);
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
				
			//Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			long result = biz.setCreaSucursal(suc);
			logger.info("Se insertó la sucursal: " + result); 
	
			session.setAttribute("ses_wiz_suc_id", result+"");
			session.setAttribute("ses_wiz_emp_id", id_empresa+"");
			session.setAttribute("ses_adm_emp_id", id_empresa+"");
			
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