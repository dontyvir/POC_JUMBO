package cl.bbr.vte.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.exception.VteException;

/**
 * 
 * <p>Este comando permite la ejecucion del Wizard, es decir, crea commprador, le asocia sucursales y le asocia empresas .</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class SetExecWizard extends Command {   

	/**
	 * Ejecucion del Wizard, es decir, crea commprador, le asocia sucursales y le asocia empresas.
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

			//Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			CompradoresDTO compradorDTO = (CompradoresDTO) session.getAttribute("sess_compradoresDTO");
			List lst_suc_DTO = (List) session.getAttribute("sess_sucursalesDTO");
			List lst_emp_DTO = (List) session.getAttribute("sess_empresasDTO");
			
			//SE ENVIA LA INFORMACION PARA SER PROCESADA
			
			boolean result = biz.setExecWizard(compradorDTO, lst_suc_DTO, lst_emp_DTO);
			logger.info("Se insertó el comprador con sus sucursales y empresas a traves del WIZARD : ");
			
			//Se limpian las variables de session : session sess_compradoresDTO - sess_sucursalesDTO - sess_empresasDTO
			session.removeAttribute("sess_compradoresDTO");
			session.removeAttribute("sess_sucursalesDTO");
			session.removeAttribute("sess_empresasDTO");
			
			//FIN
	
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
			
		String dis_ok = getServletConfig().getInitParameter("dis_ok");
		logger.debug("Redireccionando a: " + dis_ok);
		arg1.sendRedirect(dis_ok);			

	}
}