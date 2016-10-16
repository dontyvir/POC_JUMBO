package cl.bbr.common.command;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.empresas.dto.UserDTO;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.utils.Cod_error;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.utils.Utils;

/**
 * Logon es un Servlet que permite a los ejecutivos conectarse al sitio.
 * <p>
 * Comportamiento:
 * <ul>
 * <li>revisa si el RUT y la clave del ejecutivo son correctos.</li>
 * <li>revisa si el RUT del comprador es correcto.</li>
 * <li>crea una session para su identificación. El ejecutivo desde ahora tiene
 * una sesión identificada. La sesión contiene la siguiente información: identificador ejecutivo. Ademas se crea 
 * otra session von la sig informacion del comprador, identificador comprador, rut, nombre
 * del comprador.</li>
 * <li>Si se especifica URL, el comando envía a esa URL cuando es exitosa la
 * conexión.</li>
 * </ul>
 * <p>
 * Excepciones:
 * <ul>
 * <li>Si el RUT del ejecutivo no existe</li>
 * <li>Si clave no coincide con la del ejecutivo</li>
 * <li>Si el RUT del comprador no existe</li>
 * </ul>
 * <p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class FonoLogon extends Command { 

	/**
	 * Permite la conexión del usuario. Debe validar que el comprador exista.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// Carga properties
		ResourceBundle rb = ResourceBundle.getBundle("vte");			

		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();
		
		try {

			// Se revisan que los parámetros mínimos existan
			ArrayList campos = new ArrayList();
			campos.add("rut");
			campos.add("clave");
			campos.add("rut2");
			campos.add("dv2");

			if (ValidateParameters(arg0, arg1, campos) == false) {
				logger.error("Faltan parámetros mínimos");
				throw new VteException( Cod_error.REG_FALTAN_PARA );
			}

			
			// Se cargan los parámetros del request a variables locales
			String rut_e = arg0.getParameter("rut").toString().replaceAll("\\.","").replaceAll("\\-","");
			String clave_e = arg0.getParameter("clave").toString();
			
			String rut2 = arg0.getParameter("rut2").toString().replaceAll("\\.","").replaceAll("\\-","");
			long rut_c = Long.parseLong(rut2);

			// Instancia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			UserDTO ejecutivo = (UserDTO)biz.getEjecutivoGetByRut(rut_e);
			
			// Se revisa si existe el ejecutivo
			if( ejecutivo == null) {
				logger.warn("Ejecutivo no existe " + rut_e );
				throw new VteException( Cod_error.CLI_NO_EXISTE );
			}
			logger.info("Ejecutivo existe " + rut_e );
			
			// Se valida si las claves de ejecutivo coinciden
			if( ejecutivo.getPassword().compareTo(Utils.encriptar(clave_e)) != 0 ) {
				logger.error("Problemas con clave ejecutivo ingresado " + rut_e );
				throw new VteException( Cod_error.CLI_CLAVE_INVALIDA );
			}
			logger.info( "Ejecutivo clave OK" );
			
			// Se almacena el id ejecutivo en la sesión
			session.setAttribute("ses_fono_id", ejecutivo.getId_usuario() + "" );
			
			// Recupera datos del comprador
			CompradoresDTO comprador = (CompradoresDTO)biz.getCompradoresByRut( rut_c );
			
			// Se revisa si existe el cliente
			if( comprador == null ) {
				logger.warn("Comprador no existe " + rut_c );
				throw new VteException( Cod_error.CLI_NO_EXISTE );
			}
			logger.info("Comprador existe " + rut_c );

			logger.info( "Nombre Comprador:" + comprador.getCpr_nombres() + " " + comprador.getCpr_ape_pat());

			// Se almacena el nombre del comprador en la sesión
			session.setAttribute("ses_com_id", comprador.getCpr_id() + "" );
			session.setAttribute("ses_com_nombre", comprador.getCpr_nombres() + " " + comprador.getCpr_ape_pat() );
			session.setAttribute("ses_com_rut", comprador.getCpr_rut() + "");
			session.setAttribute("ses_com_est", comprador.getCpr_estado() + "");
			
			// Para comprador
			// Se almacena el identificador de la sucursal y de la empresa en la sesión
			// Se recupera la primera empresa y su primera sucursal
			List l_comp = null;
			/*
			l_comp = biz.getListSucursalesByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			if( l_comp.size() > 0 ) {
			ComprXSucDTO dto = (ComprXSucDTO)l_comp.get(0);
				session.setAttribute("ses_suc_id", dto.getId_sucursal()+"");
				session.setAttribute("ses_emp_id", dto.getId_empresa()+"");
			}
			*/
			session.setAttribute("ses_suc_id", "0");
			session.setAttribute("ses_emp_id", "0");			

			// Para administrador
			// Se almacena el identificador de la sucursal y de la empresa en la sesión
			// Se recupera la primera empresa y su primera sucursal
			l_comp = biz.getListAdmSucursalesByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			if( l_comp.size() > 0 ) {
				SucursalesDTO dto_suc = (SucursalesDTO)l_comp.get(0);
				session.setAttribute("ses_adm_suc_id", dto_suc.getSuc_id()+"");
				session.setAttribute("ses_adm_emp_id", dto_suc.getSuc_emp_id()+"");
			}			
			
			// Caducar cotizaciones vencidas para las empresas que tiene acceso el comprador
			biz.CaducarCotizacion( comprador.getCpr_id() );
			
			
			// Recupera lista de empresas que tiene acceso el comprador	
			List listEmp = biz.getListAdmEmpresasByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			
			//Si se encuentran registros => usuario administrador
			if(listEmp.size() > 0){
				session.setAttribute("ses_tipo_usuario", "1");
			}else{
				session.setAttribute("ses_tipo_usuario", "0");
			}			
			
			// Redirecciona ok
			// Recupera pagina desde web.xml
			String dis_ok = getServletConfig().getInitParameter("dis_ok");
			arg1.sendRedirect(dis_ok);

		} catch (VteException e) {
			logger.warn( "Controlando excepción de negocios: " + e.getMessage());
			session.setAttribute("cod_error", e.getMessage() );
			getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);
			return;
		} catch (Exception e) {
			logger.warn( "Controlando excepción de sistema: " + e.getMessage());
			e.printStackTrace();
			throw new SystemException( e );
		}

	}
}