package cl.bbr.fo.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.log.Logging;

/**
 * Logon es un Servlet que permite a los usuarios conectarse al sitio.
 *
 * @author BBR e-commerce & retail
 * 
 */
public class FonoCliLogon extends FonoCommand {

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

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();

			// Se revisan que los parámetros mínimos existan
			if ( !validateParametersLocal(arg0) ) {
				this.getLogger().error("Faltan parámetros mínimos (FonoCliLogon): " + Cod_error.REG_FALTAN_PARA);
				session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er_para") ).forward(arg0, arg1);
				return;
			}

			// Se cargan los parámetros del request a variables locales
			String rut_s = arg0.getParameter("rut").replaceAll("\\.","");
			long rut = Long.parseLong(rut_s);
			String dv = arg0.getParameter("dv").charAt(0) + "";	
			
			this.getLogger().info("Intento conexión ( RUT:" + rut + " DV:" + dv+ " )");

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			ClienteDTO cliente = (ClienteDTO)biz.clienteGetByRut( rut );
			
			// Se revisa si existe el cliente
			if( cliente == null ) {
				this.getLogger().info("Usuario no existe");
				session.setAttribute("cod_error", Cod_error.CLI_NO_EXISTE );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);				
				return;
			}
			
			//En un futuro se verificará si es empleado
			session.setAttribute("ses_empleado_paris", "NO");
			
			// Se almacena el nombre del cliente en la sesión
			session.setAttribute("ses_cli_id", cliente.getId() + "" );
			//[20121107avc
			session.setAttribute("ses_colaborador", String.valueOf(cliente.isColaborador()));
			//]20121107avc
			session.setAttribute("ses_cli_nombre", cliente.getNombre() + " " + cliente.getApellido_pat());
			session.setAttribute("ses_cli_nombre_pila", cliente.getNombre());
            session.setAttribute("ses_cli_apellido_pat", cliente.getApellido_pat());
			session.setAttribute("ses_cli_rut", cliente.getRut() + "");
			session.setAttribute("ses_cli_dv", cliente.getDv());
			session.setAttribute("ses_cli_email", cliente.getEmail());
            
            if ( biz.clienteEsConfiable( cliente.getRut() ) && !biz.tieneEventosActivosConValidacionManual( cliente.getRut() ) ) {
                session.setAttribute("ses_cli_confiable", "S");
            } else {
                session.setAttribute("ses_cli_confiable", "N");    
            }
			
            /***************************************************************/
            /**************   D A T O S   D I R E C C I O N   **************/
            /***************************************************************/
            
            PedidoDTO ultPedido = biz.getUltimoIdPedidoCliente(cliente.getId());
            String flag = "NO";
            if ( ultPedido != null && ultPedido.getId_pedido() != 0 ) {
                 List dirPed = biz.clienteAllDireccionesConCobertura( cliente.getId());                                               
                for( int k = 0; k < dirPed.size(); k++ ) {
                    DireccionesDTO dir2 = (DireccionesDTO) dirPed.get(k);
                    if(ultPedido.getId_zona() == dir2.getZona_id()){
                        session.setAttribute("ses_zona_id", String.valueOf( ultPedido.getId_zona() ));
                        session.setAttribute("ses_loc_id", String.valueOf( ultPedido.getId_local() ));
                        session.setAttribute("ses_dir_id", String.valueOf( ultPedido.getDir_id() ));
                        session.setAttribute("ses_dir_alias", ultPedido.getDir_calle());                        
                        session.setAttribute("ses_forma_despacho", ultPedido.getTipo_despacho()); 
                        flag = "SI";
                    }
                }
             
                if(flag.equalsIgnoreCase("NO")){
                    DireccionesDTO dir3 = (DireccionesDTO) biz.clienteAllDireccionesConCobertura( cliente.getId() ).get(0);
                    session.setAttribute("ses_zona_id", String.valueOf( dir3.getZona_id()  ));
                    session.setAttribute("ses_loc_id", String.valueOf( dir3.getLoc_cod() ));
                    session.setAttribute("ses_dir_id", String.valueOf( dir3.getId() ));
                    session.setAttribute("ses_dir_alias", dir3.getAlias());                        
                    session.setAttribute("ses_forma_despacho", "N"); 
                }                     
                
            } else {
                if ( biz.tieneDireccionesConCobertura( cliente.getId() ) ) {
                    DireccionesDTO dir = (DireccionesDTO) biz.clienteAllDireccionesConCobertura( cliente.getId() ).get(0);
                    session.setAttribute("ses_loc_id", String.valueOf( dir.getLoc_cod() ));
                    session.setAttribute("ses_dir_id", String.valueOf( dir.getId() ));
                    session.setAttribute("ses_dir_alias", dir.getAlias() );
                    session.setAttribute("ses_zona_id", String.valueOf( dir.getZona_id() ));
                    session.setAttribute("ses_forma_despacho", "N");
                    
                }
            }
            
            
            /***************************************************************/
            
            
            
			// Redirecciona ok
			// Recupera pagina desde web.xml
			String dis_ok;
			String url = arg0.getParameter("url");
			if (url == null) {
				// Recupera pagina desde web.xml
				dis_ok = getServletConfig().getInitParameter("dis_ok");
			} else {
				dis_ok = url;
			}

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
	private boolean validateParametersLocal(HttpServletRequest arg0) {
		Logging logger = this.getLogger();

		ArrayList campos = new ArrayList();
		campos.add("rut");
		campos.add("dv");

		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null || arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo + " en FonoCliLogon");
				return false;
			}
		}

		return true;

	}

}