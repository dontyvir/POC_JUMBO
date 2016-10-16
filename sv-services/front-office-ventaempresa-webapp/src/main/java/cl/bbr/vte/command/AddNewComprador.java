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
 * <p>Este comando permite ingresar un nuevo comprador al sistema y asociarlo a la sucursal.</p>
 * <p>Se valida que ciertos parámetros sean de caracter obligatorio.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class AddNewComprador extends Command {   

	/**
	 * Inserta un nuevo comprador
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
			if (Long.parseLong(arg0.getParameter("l_comprador")) == 0){//Comprador nuevo
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
				campos.add("sucursales");
				if (ValidateParameters(arg0, arg1, campos) == false ) {
					throw new ParametroObligatorioException( "Falta parámetro obligatorio." );
				}	
			}
			
		     //2.2 obtiene parametros desde el request
			
			long rut = 0;
			if (arg0.getParameter("rut") != null && arg0.getParameter("rut").equals("") == false ) {
				rut  = Long.parseLong(arg0.getParameter("rut").replaceAll("\\.",""));
			}
			
			String dv = arg0.getParameter("dv");
			String nombre = arg0.getParameter("nombre");
			String paterno = arg0.getParameter("paterno");
			String materno = arg0.getParameter("materno");
			String genero = arg0.getParameter("genero");			
			String fono1 = arg0.getParameter("fono1");
			String fono2 = arg0.getParameter("fono2");
			String fono3 = arg0.getParameter("fono3");
			String email = arg0.getParameter("email");
			String estado = arg0.getParameter("estado");
			String clave = Utils.encriptar( arg0.getParameter("clave"));
			String pregunta = arg0.getParameter("pregunta");
			String respuesta = arg0.getParameter("respuesta");
			String tipo = arg0.getParameter("tipo");
			long id_comprador = Long.parseLong(arg0.getParameter("l_comprador"));
			if (arg0.getParameter("tipo") != null && arg0.getParameter("tipo").compareTo("") != 0)
				tipo = "A";
			else
				tipo = "C";
			
			long sucursal_id = Long.parseLong(arg0.getParameter("sucursales"));
			long empresa_id = Long.parseLong(arg0.getParameter("empresas"));
			
			
			CompradoresDTO comprador = new CompradoresDTO();
			
			comprador.setCpr_rut(rut);
			comprador.setCpr_dv(dv);
			comprador.setCpr_nombres(nombre);
			comprador.setCpr_ape_pat(paterno);
			comprador.setCpr_ape_mat(materno);
			comprador.setCpr_genero(genero);
			comprador.setCpr_fono1(fono1);
			comprador.setCpr_fono2(fono2);
			comprador.setCpr_fono3(fono3);
			comprador.setCpr_email(email);
			comprador.setCpr_estado(estado);
			comprador.setCpr_pass(clave);
			comprador.setCpr_pregunta(pregunta);
			comprador.setCpr_respuesta(respuesta);
			comprador.setCpr_tipo(tipo);
			comprador.setId_sucursal(sucursal_id);
			comprador.setId_empresa(empresa_id);
			comprador.setCpr_id(id_comprador);
			//Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			long result = biz.setCreaComprador(comprador);
			logger.info("Se insertó el comprador: "); 
	
			
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
			
			String dis_ok = getServletConfig().getInitParameter("dis_ok");
			logger.debug("Redireccionando a: " + dis_ok);
			arg1.sendRedirect(dis_ok);			

	}

}