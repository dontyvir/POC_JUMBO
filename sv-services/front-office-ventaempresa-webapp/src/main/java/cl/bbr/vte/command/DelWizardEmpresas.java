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
import cl.bbr.vte.empresas.dto.ComprXEmpDTO;


/**
 * 
 * <p>Este comando permite eliminar una empresa para un comprador por medio del Wizard
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class DelWizardEmpresas extends Command {   

	/**
	 * Elimina una empresa para un comprador por medio del Wizard
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
			campos.add("emp_id");
			if (ValidateParameters(arg0, arg1, campos) == false ) {
				throw new ParametroObligatorioException( "Falta parámetro obligatorio." );
			}
			
			//SE ELIMINA UNA SUURSAL ASOCIADA AL COMPRADOR 
			if(session.getAttribute("sess_empresasDTO") != null){
				
				List registros = (List) session.getAttribute("sess_empresasDTO");
				
				List lstaux2 = new ArrayList(); 

				for (int i = 0; i < registros.size(); i++) {
          			ComprXEmpDTO emp = (ComprXEmpDTO)registros.get(i);
          			if ((arg0.getParameter("emp_id") != null) && 
            			(Long.parseLong(arg0.getParameter("emp_id")) != emp.getId_empresa())) {
            			lstaux2.add(emp);
          			}
				}
				session.setAttribute("sess_empresasDTO", lstaux2);
			}
			
			//SE ELIMINA UNA SUURSAL ASOCIADA AL COMPRADOR 
			
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