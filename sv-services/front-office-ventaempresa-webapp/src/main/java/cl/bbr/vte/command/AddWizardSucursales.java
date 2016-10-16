package cl.bbr.vte.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.empresas.dto.ComprXSucDTO;


/**
 * 
 * <p>Este comando permite ingresar sucursales para un comprador por medio del Wizard
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class AddWizardSucursales extends Command {   

	/**
	 * Inserta sucursales para un comprador por medio del Wizard
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
			// Se revisan que los parámetros mínimos existan
			ArrayList campos = new ArrayList();
			campos.add("select_emp");
			campos.add("nom_emp");
			campos.add("select_suc");
			campos.add("nom_suc");
			
			if (ValidateParameters(arg0, arg1, campos) == false ) {
				throw new ParametroObligatorioException( "Falta parámetro obligatorio." );
			}
			
			//***********PARAMETROS DEL REQUEST PARA LA SUCURSAL************************************

			if(arg0.getParameter("select_emp") != null && arg0.getParameter("select_suc") != null){
			
				List lst_suc = new ArrayList();
				ComprXSucDTO comXsuc = new ComprXSucDTO();
				
				long id_emp = Long.parseLong(arg0.getParameter("select_emp")); 
				comXsuc.setId_empresa(id_emp);
				
				String nom_emp = arg0.getParameter("nom_emp");
				comXsuc.setNom_empresa(nom_emp);

				long id_suc = Long.parseLong(arg0.getParameter("select_suc")); 
				comXsuc.setId_sucursal(id_suc);
				
				String nom_suc = arg0.getParameter("nom_suc");
				comXsuc.setNom_sucursal(nom_suc);
				
			
				if(session.getAttribute("sess_sucursalesDTO") != null){
					lst_suc = (List) session.getAttribute("sess_sucursalesDTO");
				}
				
				lst_suc.add(comXsuc);
				session.setAttribute("sess_sucursalesDTO", lst_suc);
				
			}		
			
			//************FIN PARAMETROS REQUEST SUCURSAL Y SESSION********************************
				
		}catch (ParametroObligatorioException e) {
			logger.warn( "Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
			UrlError = UrlError + fp.forward();
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		}catch (Exception e) {
			logger.warn("Controlando excepción de sistema: " + e.getMessage());
			e.printStackTrace();
			throw new SystemException(e);
		}
			
		String dis_ok = getServletConfig().getInitParameter("dis_ok");
		logger.debug("Redireccionando a: " + dis_ok);
		arg1.sendRedirect(dis_ok);			
	}
}