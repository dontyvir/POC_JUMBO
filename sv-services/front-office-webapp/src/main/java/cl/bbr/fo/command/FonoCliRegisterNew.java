package cl.bbr.fo.command;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;

import cl.bbr.fo.exception.FuncionalException;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.log.Logging;
import cl.jumbo.interfaces.ventaslocales.InterfazVentas;

/**
 * 
 * Registra los datos de un cliente nuevo desde Fono compras. Cliente y una dirección de despacho.
 * 
 * @author Administrador
 *
 */
public class FonoCliRegisterNew extends FonoCommand {

	/**
	 * Permite el registro de un usuario.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws FuncionalException, Exception {

		try {
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();

			// Se revisan que los parámetros mínimos existan
			if ( !validateParametersLocal(arg0) ) {
				this.getLogger().error("Faltan parámetros mínimos (FonoCliRegisterNew): "+ Cod_error.REG_FALTAN_PARA);
				session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er_para") ).forward(arg0, arg1);				
				return;
			}

			// Se cargan los parámetros del request
			long id = -1;
			long rut = Long.parseLong(arg0.getParameter("rut"));
			String dv = arg0.getParameter("dv");
			String nombre = arg0.getParameter("nombre");
			String apellido_pat = arg0.getParameter("ape_pat");
			String apellido_mat = arg0.getParameter("ape_mat");
			String clave_ant = Utils.genPasswordFO(8);
			String clave = Utils.encriptarFO( clave_ant );
			String email = arg0.getParameter("email1");
			String dominio = arg0.getParameter("dominio1");
			String estado = "C";
			String fon_cod_1 = arg0.getParameter("fon_cod_1");
			String fon_num_1 = arg0.getParameter("fon_num_1");
			String fon_cod_2 = arg0.getParameter("fon_cod_2");
			String fon_num_2 = arg0.getParameter("fon_num_2");
			String fon_cod_3 = arg0.getParameter("fon_cod_3");
			String fon_num_3 = arg0.getParameter("fon_num_3");
			String genero = arg0.getParameter("genero");
			int ano = Integer.parseInt(arg0.getParameter("ano"));
			int mes = Integer.parseInt(arg0.getParameter("mes")) - 1;
			int dia = Integer.parseInt(arg0.getParameter("dia"));
			// Para fecha de nacimiento
			Calendar cal = new GregorianCalendar(ano, mes, dia);
			long fec_nac = cal.getTimeInMillis();
			long fec_crea = 0;
			String pregunta = arg0.getParameter("pregunta");
			String respuesta = arg0.getParameter("respuesta");			

			// Se genera un cliente DTO
			ClienteDTO cliente;

			// Carga los datos del cliente en el objeto
			cliente = new ClienteDTO(id, rut, dv, nombre, apellido_pat,
					apellido_mat, clave, email + "@" + dominio, fon_cod_1, fon_num_1,
					fon_cod_2, fon_num_2, fon_cod_3, fon_num_3, 1, fec_crea, estado, fec_nac, genero, pregunta, respuesta);

			// Recuperar la lista de los despachos
			DireccionesDTO desp = new DireccionesDTO();

			desp.setAlias(arg0.getParameter("alias"));
			desp.setTipo_calle(Long.parseLong((arg0.getParameter("tipo_calle"))));
			desp.setCalle(arg0.getParameter("calle"));
			desp.setNumero(arg0.getParameter("numero"));
			desp.setDepto(arg0.getParameter("departamento"));
			desp.setCom_id(Long.parseLong(arg0.getParameter("comuna")));
			desp.setComentarios(arg0.getParameter("comentario"));
			desp.setReg_id(Long.parseLong(arg0.getParameter("region")));

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			try {
				// Registrar en últimas compras
			    Properties prop = new Properties();
			    FileInputStream fis = new FileInputStream(getServletContext().getRealPath("/")+rb.getString("conf.venta_cfg"));
			    prop.load(fis);
			    InterfazVentas interfaz = new InterfazVentas();
			    if( interfaz.tieneCompras(Integer.parseInt(arg0.getParameter("rut"))) == false ) {
			    	interfaz.clienteRegistrado(Integer.parseInt(arg0.getParameter("rut")));
			    }
			}
			catch (Exception ex) {
				this.getLogger().error("No se pudieron recuperar compras fisicas (FonoCliRegisterNew)",ex);
			}
			
			try {
				biz.clienteNewRegistro(cliente, desp);
			} catch (Exception ex) {
				this.getLogger().error("Grabar cliente nuevo", ex);
				session.setAttribute("cod_error", Cod_error.GEN_SQL_NO_SAVE );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);
				throw new FuncionalException(ex);
			}

			cliente = null;
			try {
				cliente = biz.clienteGetByRut(rut);
			} catch (Exception ex) {
				this.getLogger().error("Carga cliente RUT:" + rut, ex);
				session.setAttribute("cod_error", Cod_error.GEN_SQL_NO_SAVE );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);
				throw new FuncionalException(ex);
			}
			
			//En un futuro se verificará si es empleado
			session.setAttribute("ses_empleado_paris", "NO");
			
			// Se almacena el nombre del cliente en la sesión
			session.setAttribute("ses_cli_id", cliente.getId() + "");
			//[20121107avc
			session.setAttribute("ses_colaborador", String.valueOf(cliente.isColaborador()));
			//]20121107avc
			session.setAttribute("ses_cli_nombre", cliente.getNombre() + " " + cliente.getApellido_pat());
			session.setAttribute("ses_cli_nombre_pila", cliente.getNombre() );
			session.setAttribute("ses_cli_rut", cliente.getRut() + "");
			session.setAttribute("ses_cli_dv", cliente.getDv());

			// Recupera pagina desde web.xml
			String dis_ok = getServletConfig().getInitParameter("dis_ok");
			arg1.sendRedirect(dis_ok);

		} catch (Exception ex) {
			this.getLogger().error("Problemas generales", ex);
			throw new SystemException(ex);
		}

	}

	/**
	 * Validación de campos de ingreso
	 * 
	 * @param arg0 HttpServletRequest
	 * @param arg1 HttpServletResponse
	 * @return True o false
	 */
	private boolean validateParametersLocal(HttpServletRequest arg0) {
		Logging logger = this.getLogger();
		ArrayList campos = new ArrayList();
		campos.add("rut");
		campos.add("dv");
		campos.add("nombre");
		campos.add("ape_pat");
		campos.add("ape_mat");
		campos.add("email1");
		campos.add("dominio1");
		campos.add("fon_cod_1");
		campos.add("fon_num_1");
		campos.add("genero");
		campos.add("ano");
		campos.add("mes");
		campos.add("dia");
		campos.add("pregunta");
		campos.add("respuesta");
		
		campos.add("alias");
		campos.add("calle");
		campos.add("numero");
		campos.add("region");
		campos.add("comuna");

		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null || arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo + " en FonoCliRegisterNew");
				arg0.setAttribute("cod_error", Cod_error.GEN_FALTAN_PARA);
				return false;
			}
		}
		return true;

	}

}