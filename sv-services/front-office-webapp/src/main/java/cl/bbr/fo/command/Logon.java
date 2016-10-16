package cl.bbr.fo.command;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.promos.interfaces.ClienteTcpPromosCupones;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.ConsultaCuponesPorRutDTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.RespR1DTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.TcpDTO;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.FOTcpDTO;

/**
 * Logon es un Servlet que permite a los usuarios conectarse al sitio.
 * <p>
 * Comportamiento:
 * <ul>
 * <li>revisa si el RUT y la clave de un cliente son correctos.</li>
 * <li>crea una session para su identificación. El cliente desde ahora tiene
 * una sesión identificada. La sesión contiene la siguiente información: nombre
 * del cliente.</li>
 * <li>Si se especifica URL, el comando envía a esa URL cuando es exitosa la
 * conexión.</li>
 * </ul>
 * <p>
 * Excepciones:
 * <ul>
 * <li>Si el RUT del cliente no existe</li>
 * </ul>
 * <p>
 * 
 * @author BBRI
 * 
 */
public class Logon extends Command {

	/**
	 * Permite la conexión del usuario. Debe validar que el cliente exista.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		try {
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();

			// Se revisan que los parámetros mínimos existan
			if ( !ValidateParametersLocal(arg0) ) {
				this.getLogger().error("Faltan parámetros mínimos (Logon): " + Cod_error.REG_FALTAN_PARA);
				session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);
				return;
			}

			// Se cargan los parámetros del request a variables locales
			String rut_s = arg0.getParameter("rut").toString().replaceAll("\\.","").replaceAll("\\-","");
			
			String dv = arg0.getParameter("dv");			
			this.getLogger().info("Intento conexion ( RUT:" + rut_s + " DV:" + dv+ " )");			
			long rut = Long.parseLong(rut_s);
			String clave = arg0.getParameter("clave").toString();

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			ClienteDTO cliente = (ClienteDTO)biz.clienteGetByRut( rut );
			
			// Se revisa si existe el cliente
			if( cliente == null ) {
				this.getLogger().info("Cliente no existe");
				session.setAttribute("cod_error", Cod_error.CLI_NO_EXISTE );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);				
				return;
			}
			this.getLogger().info( "Cliente existe" );
			
			//Verificamos número de intententos de logeo
			
			Calendar cal = new GregorianCalendar();

			if (((cal.getTimeInMillis()-cliente.getFec_login()) <= Long.parseLong(rb.getString("logon.tiempo.segundos"))*1000) && cliente.getIntento() >= Long.parseLong(rb.getString("logon.intentos")) ){
				this.getLogger().info("Problemas con el numero de intentos");
				session.setAttribute("cod_error", "Se ha superado el número de intentos fallidos" );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);
				return;
			}
            
            // Se valida si las claves coinciden
			if( cliente.getClave().compareTo(Utils.encriptarFO(clave)) != 0 ) {
				//1: modifica campos, 0 : limpia campos
				biz.updateIntentos(cliente.getId(), 1); 				
				this.getLogger().info("Problemas con clave ingresada");
				session.setAttribute("cod_error", Cod_error.CLI_CLAVE_INVALIDA );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);
				return;
			}
			
			this.getLogger().info( "Cliente clave OK" );
			this.getLogger().info( "Nombre Cliente:" + cliente.getNombre() + " " + cliente.getApellido_pat());

            /**
             * Se verifica la caducidad de la clave del cliente
             */
            if (cliente.getEstado().compareTo("C") == 0) {
                logger.error("No tiene permisos suficientes: " + Cod_error.GEN_CAM_CLAVE + " Clave Caducada");
                session.setAttribute("cod_error", "Su Clave está Caducada");
                arg1.sendRedirect(rb.getString("command.cambio_clave"));
            }
            
            
			// Se almacena el nombre del cliente en la sesión
			session.setAttribute("ses_cli_id", cliente.getId() + "" );
			//[20121107avc
			session.setAttribute("ses_colaborador", String.valueOf(cliente.isColaborador()));
			//]20121107avc
			session.setAttribute("ses_cli_nombre", cliente.getNombre() + " " + cliente.getApellido_pat());
			session.setAttribute("ses_cli_nombre_pila", cliente.getNombre() );
			session.setAttribute("ses_cli_rut", cliente.getRut() + "");
			session.setAttribute("ses_cli_dv", cliente.getDv());
            if (session.getAttribute("ses_invitado_id") != null)
                session.removeAttribute("ses_invitado_id");
            
            //Se verifica si es empleado paris
			/*
			try {
				ClienteDeLogon cl = new ClienteDeLogon();
				cl.setRut(new Long(rut));
				cl.setDv(dv);
				LogonValida valida = new ValidaCuentaLogonProcess().processing(cl);
				if (valida.getCodigo() == 0) {
					if (valida.getEmpleado() == 1) {
						this.getLogger().info( "Cliente Empleado Paris" );
						session.setAttribute("ses_empleado_paris", "SI");
						session.setAttribute("ses_cta_empleado", valida.getNumeroCuenta());
					} else {
						this.getLogger().info( "Cliente No Empleado Paris" );
						session.setAttribute("ses_empleado_paris", "NO");
					}
				} else {
					this.getLogger().info( "Hubo un problema con ValidaCuentaLogon" );
					session.setAttribute("ses_empleado_paris", "NO");
				}
			} catch (Exception e){
				this.getLogger().error("Error al ejecutar el valida cuenta");
			}
            */

			// Recuperar TCP del cliente desde el servidor de promociones
			try {
				
				// Consulta por RUT al servidor
				ClienteTcpPromosCupones consulta = new ClienteTcpPromosCupones(); 
				consulta.setHost( rb.getString("promociones.consulta.cupones.host") );
				consulta.setPuerto( Integer.parseInt(rb.getString("promociones.consulta.cupones.puerto")) );
				
				ConsultaCuponesPorRutDTO in_cupon = new ConsultaCuponesPorRutDTO();
				int cod_loc_pos = biz.getCodigoLocalPos( Long.parseLong(session.getAttribute("ses_loc_id").toString()) );
				in_cupon.setCod_local_pos( cod_loc_pos );
				in_cupon.setNum_pos( cod_loc_pos ); // igual que el local pos
				in_cupon.setRut( Long.parseLong(session.getAttribute("ses_cli_rut").toString()) );
				in_cupon.setDocumento( 1 );
				in_cupon.setJournal( 0 ); 
				in_cupon.setOperador( Integer.parseInt(session.getAttribute("ses_cli_id").toString()) ); // ID del cliente
				RespR1DTO resp = consulta.ConsultaCuponesPorRut( in_cupon );
				
				if( resp != null && resp.getCod_ret().equals("00") ) {

					List l_tcp = new ArrayList();
					List l_tcps = resp.getTcps();
					for ( int i = 0; l_tcps != null && i < l_tcps.size(); i++ ) {
						
						TcpDTO tcp_promo = (TcpDTO) l_tcps.get(i);
						
						FOTcpDTO tcp = new FOTcpDTO();
						tcp.setTcp_nro( Integer.parseInt(tcp_promo.getNro_tcp()) );
						tcp.setTcp_max( tcp_promo.getCantidad() );
						l_tcp.add(tcp);
						
					}
					session.setAttribute("ses_promo_tcp", l_tcp);
					//20120907avc
					logger.info("Se establece el valor ses_promo_tcp en la session");
					//-20120907avc
					
				} else {
					this.getLogger().debug("Problema con TCP " + resp.getCod_ret() + " " + resp.getGlosa1() + " " + resp.getGlosa2() );
				}
				

			} catch (Exception e){
				this.getLogger().error("Error al recuperar TCP, cliente continua sin TCP");
			}
			
			// Redirecciona ok
			// Recupera pagina desde web.xml
			String dis_ok;
			String url = arg0.getParameter("url");
			if (url == null) {
				//1: modifica campos, 0 : limpia campos
				biz.updateIntentos(cliente.getId(), 0);
				//Recupera pagina desde web.xml
				dis_ok = getServletConfig().getInitParameter("dis_ok");
			} else {
				dis_ok = url;
			}

			this.getLogger().info("Redireccion URL:"+dis_ok);
			
			arg1.sendRedirect(dis_ok);

		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException( e );
		}

	}

	/**
	 * Valida parámetros mínimos necesarios
	 * 
	 * @param arg0	Request recibido desde el navegador
	 * @param arg1	Response recibido desde el navegador
	 * @return		True: ok, False: faltan parámetros
	 */
	private boolean ValidateParametersLocal(HttpServletRequest arg0) {
		Logging logger = this.getLogger();
		ArrayList campos = new ArrayList();
		campos.add("rut");
		campos.add("dv");
		campos.add("clave");
		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null || arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo + " en Logon");
				return false;
			}
		}
		return true;
	}
}