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
 * 
 * <p>Este comando permite ingresar un nuevo comprador por medio del Wizard
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class AddWizardCompradores extends Command {   

	/**
	 * Inserta un nuevo compradorpor medio del Wizard
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
			campos.add("rut");
			campos.add("dv");
			campos.add("nombre");
			campos.add("paterno");
			campos.add("materno");
			campos.add("genero");
			campos.add("fono1");
			campos.add("email");
			campos.add("clave");
			campos.add("pregunta");
			campos.add("respuesta");
			if (ValidateParameters(arg0, arg1, campos) == false ) {
				throw new ParametroObligatorioException( "Falta parámetro obligatorio." );
			}
			
			
			//***********PARAMETROS DEL REQUEST PARA EL COMPRADOR****************************
			
			if (arg0.getParameter("rut") != null){	
				CompradoresDTO compradoraux = new CompradoresDTO();
				
				long rut = 0;
				if (arg0.getParameter("rut") != null && arg0.getParameter("rut").equals("") == false ) {
					rut  = Long.parseLong(arg0.getParameter("rut").replaceAll("\\.",""));
					compradoraux.setCpr_rut(rut);
				}
				
				// Instancia del bizdelegate
				BizDelegate biz = new BizDelegate();
				
				// Recupera datos del comprador
				CompradoresDTO comprador = (CompradoresDTO)biz.getCompradoresByRut( rut );
				
				// Se revisa si existe el cliente
				if( comprador != null ) {
					logger.warn("Comprador existe " + rut );
					throw new VteException("El comprador ya existe");
				}
				
				logger.info("Comprador No existe " + rut );
				
	
				if(arg0.getParameter("dv") != null){
					String dv = arg0.getParameter("dv");
					compradoraux.setCpr_dv(dv);
				}
				
				if(arg0.getParameter("nombre") != null){
					String nombre = arg0.getParameter("nombre");
					compradoraux.setCpr_nombres(nombre);
				}
				
				if(arg0.getParameter("paterno") != null){
					String paterno = arg0.getParameter("paterno");
					compradoraux.setCpr_ape_pat(paterno);
				}
				
				if(arg0.getParameter("materno") != null){
					String materno = arg0.getParameter("materno");
					compradoraux.setCpr_ape_mat(materno);
				}
				
				if(arg0.getParameter("genero") != null){
					String genero = arg0.getParameter("genero");
					compradoraux.setCpr_genero(genero);
				}
				
				if(arg0.getParameter("fono1") != null){
					String fono1 = arg0.getParameter("fono1");	
					compradoraux.setCpr_fono1(fono1);
				}
				
				if(arg0.getParameter("fono2") != null){
					String fono2 = arg0.getParameter("fono2");
					compradoraux.setCpr_fono2(fono2);
				}
				
				if(arg0.getParameter("fono3") != null){
					String fono3 = arg0.getParameter("fono3");
					compradoraux.setCpr_fono3(fono3);
				}
				
				if(arg0.getParameter("email") != null){
					String email = arg0.getParameter("email");	
					compradoraux.setCpr_email(email);
				}
				
				if(arg0.getParameter("estado") != null){
					String estado = arg0.getParameter("estado");
					compradoraux.setCpr_estado(estado);
				}
				
				if(arg0.getParameter("clave") != null){
					String clave = Utils.encriptar( arg0.getParameter("clave"));
					compradoraux.setCpr_pass(clave);
				}
				
				if(arg0.getParameter("pregunta") != null){
					String pregunta = arg0.getParameter("pregunta");
					compradoraux.setCpr_pregunta(pregunta);
				}
				
				if(arg0.getParameter("respuesta") != null){
					String respuesta = arg0.getParameter("respuesta");
					compradoraux.setCpr_respuesta(respuesta);
				}
				
				if(arg0.getParameter("estado") != null){
					String estado = "";
					if (arg0.getParameter("estado") != null )
						estado = "A";
					else
						estado = "C";
					compradoraux.setCpr_tipo(estado);	
				}
				session.setAttribute("sess_compradoresDTO", compradoraux);
			}
			
			//***********PARAMETROS DEL REQUEST PARA EL COMPRADOR****************************
		} catch (VteException e) {
			logger.warn( "Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
			UrlError = UrlError + fp.forward();
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		} catch (ParametroObligatorioException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
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