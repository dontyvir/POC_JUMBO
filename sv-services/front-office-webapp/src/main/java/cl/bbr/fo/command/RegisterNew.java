package cl.bbr.fo.command;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import net.sf.json.JSONObject;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.FuncionalException;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.log.Logging;
import cl.jumbo.interfaces.ventaslocales.InterfazVentas;

/**
 * 
 * Registra los datos de un cliente nuevo. Cliente y una dirección de despacho.
 * 
 * @author BBR e-commerce & retail
 *
 */
public class RegisterNew extends Command {

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

		String dis_err = getServletConfig().getInitParameter("dis_er");
        String respuesta = "OK";
        String idCliDir = "";
        long id = -1, idDir = -1; // 05/10/2012 : COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com} 
		try {

			ResourceBundle rb = ResourceBundle.getBundle("fo");

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();

			
			// Se revisan que los parámetros mínimos existan
			/*if (!validateParametersLocal(arg0)) {
				this.getLogger().error("Faltan parámetros mínimos: "+ Cod_error.REG_FALTAN_PARA);
				session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);				
				return;
			}*/		
		
			// Se cargan los parámetros del request
			//long id = -1, idDir = -1; // 05/10/2012 : COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com} 
			String cli_rut = arg0.getParameter("cli_rut").replaceAll("\\.","");
            String[] temp  = cli_rut.split("-");
            long rut = Long.parseLong(temp[0]);
			String dv = temp[1];
			String nombre = arg0.getParameter("nombre");
			String apellido_pat = arg0.getParameter("ape_pat");
			String clave = Utils.encriptarFO( arg0.getParameter("clave"));
			String email = arg0.getParameter("email");
			String fon_cod = arg0.getParameter("fon_cod");
			String fon_num = arg0.getParameter("fon_num");
            long id_region = Long.parseLong(arg0.getParameter("id_region"));
            long id_comuna = Long.parseLong(arg0.getParameter("id_comuna"));
            long idPedido = 0L;
            if (!(arg0.getParameter("idPedido")==null)){
            	idPedido = Long.parseLong(arg0.getParameter("idPedido"));
            }
            
            long envioEMail = 0;
            if ( arg0.getParameter("envioEMail").equals("true") ) {
                envioEMail = 1;
            }

			long envioSMS = 0;
			if ( arg0.getParameter("envioSMS").equals("true") ) {
			    envioSMS = 1;
			}


			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
            
            try {                
                ClienteDTO rutaux = biz.clienteGetByRut(rut);
                if ( rutaux != null ) {
                    respuesta = "CLIENTE_REGISTRADO";
                    this.getLogger().error("Cliente tratando de registrar ya existe (RegisterNew)");
                    session.setAttribute("cod_error", Cod_error.GEN_SQL_NO_SAVE );
                    //getServletConfig().getServletContext().getRequestDispatcher( dis_err ).forward(arg0, arg1);
                    //return;
                }                
            } catch (Exception e) {
                this.getLogger().error("Error Buscando al Cliente", e);
                session.setAttribute("cod_error", Cod_error.GEN_SQL_NO_SAVE );
                getServletConfig().getServletContext().getRequestDispatcher( dis_err ).forward(arg0, arg1);
                return;
            }
			
			
            if ( !respuesta.equals("CLIENTE_REGISTRADO") ) {
                // Carga los datos del cliente en el objeto
                //============================================
                ClienteDTO cliente = new ClienteDTO();
                //cliente = new ClienteDTO(id, rut, dv, nombre, apellido_pat, "", clave, email, fon_cod, 
                //      fon_num, "", "", "", "", Long.parseLong("10"), Long.parseLong("10"), "", Long.parseLong("10"), "", "", "");
                //VALORES INGRESADOS
                //====================
                cliente.setRut(rut);
                cliente.setDv(dv);
                cliente.setNombre(nombre);
                cliente.setApellido_pat(apellido_pat);
                cliente.setClave(clave);
                cliente.setEmail(email);
                cliente.setRecibeEMail(envioEMail);
                cliente.setRecibeSms(envioSMS);
                cliente.setFon_cod_1(fon_cod);
                cliente.setFon_num_1(fon_num);
                
                //cli_rut, cli_dv, cli_nombre, cli_apellido_pat, cli_clave, cli_email, cli_fon_cod_1, cli_fon_num_1
                
                //VALORES DUMMY
                cliente.setApellido_mat(""); // cli_apellido_mat
                cliente.setFon_cod_2(""); // cli_fon_cod_2
                cliente.setFon_num_2(""); // cli_fon_num_2
                //cliente.set(); // cli_rec_info
                //cliente.set(); // cli_fec_crea
                cliente.setEstado("A"); // cli_estado
                Calendar cal = Calendar.getInstance();
                cliente.setFec_nac(cal.getTimeInMillis()); // cli_fec_nac
                cliente.setGenero(" "); // cli_genero
                //cliente.set(); // cli_fec_act
                cliente.setPregunta("--"); // cli_pregunta
                cliente.setRespuesta("--"); // cli_respuesta
                
                //id = biz.clienteInsert(cliente);
                
                //Login facebook
                if ( arg0.getParameter("accesoLfckfield") != null && session.getAttribute("ses_oJsonFacebook") != null ) {
					JSONObject oJsonFromFacebook = (JSONObject) session.getAttribute("ses_oJsonFacebook");
					cliente.setId_facebook(oJsonFromFacebook.getString("id"));					
				}
                
                
                // CREA DIRECCIÓN DUMMY
                //=======================
                DireccionesDTO d = new DireccionesDTO();
                d.setCom_id(id_comuna);
				d.setReg_id(id_region);
                DireccionesDTO desp = biz.crearDireccionDummy(d); 
                
                try {
                    this.getLogger().debug("Registro RUT:" + rut + "-" + dv);
                    idCliDir = biz.clienteNewRegistro(cliente, desp);
                } catch (Exception ex) {
                    this.getLogger().error("Grabar cliente nuevo", ex);
                    session.setAttribute("cod_error", Cod_error.GEN_SQL_NO_SAVE );
                    getServletConfig().getServletContext().getRequestDispatcher( dis_err ).forward(arg0, arg1);
                    return;
                }

                String ids[] = idCliDir.split("-");
                id    = Long.parseLong(ids[0]);
                idDir = Long.parseLong(ids[1]);
                //registrar pedido-cliente
                if (idPedido>0){
                	JdbcPedidosDAO daopedido =  (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
                	daopedido.setIdClienteIntoPedido(id, idPedido);
                	PedidoDTO pedido = biz.getPedidoById(idPedido);
                	biz.setCompraHistorica("", id, pedido.getCant_prods(), idPedido);
                }
                
                if (id > 0){
                    try {
                        // REGISTRAR EN ÚLTIMAS COMPRAS
                        //================================
                        Properties prop = new Properties();
                        FileInputStream fis = new FileInputStream(getServletContext().getRealPath("/")+rb.getString("conf.venta_cfg"));
                        prop.load(fis);
                        InterfazVentas interfaz = new InterfazVentas();
                        //+20121025avc
                        if( interfaz.tieneCompras(new Long(rut).intValue()) == false ) {
                            interfaz.clienteRegistrado(new Long(rut).intValue());
                        }
                        //-20121025avc
                        
                    }catch (Exception ex) {
                        this.getLogger().error("No se pudieron rescatar las compras físicas del cliente (RegisterNew)",ex);
                    }
                    
                    long idInvitado = 0L;
                    if (session.getAttribute("ses_invitado_id") != null){
                        idInvitado = Long.parseLong((String)session.getAttribute("ses_invitado_id"));
                    }
                    
                    if (idInvitado > 0){
                        //Se asocia el carro de compras al Nuevo ID del Cliente
                        biz.reasignaCarroDelInvitado(id, idInvitado);
                        
                        //Se asocian los criterios de sustitución al Nuevo ID de Cliente
                        biz.reasignaSustitutosDelInvitado(id, idInvitado);
                    }


                    String pag_form = "";
                    /*if ( !biz.tieneDireccionesConCobertura(cliente.getId()) ) {
                        pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("mail_sc");
                    } else {*/
                        pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("arc_mail");                
                    //}

                    // Carga el template html
                    //========================
                    TemplateLoader load = new TemplateLoader(pag_form);
                    ITemplate tem = load.getTemplate();
                    IValueSet top = new ValueSet();
                    if (cliente.getGenero().equals("F")) {
                        top.setVariable("{genero}", "Bienvenida");
                    } else {
                        top.setVariable("{genero}", "Bienvenido");
                    }
                    top.setVariable("{nombre_cliente}", cliente.getNombre() + " " + cliente.getApellido_pat() );
                    top.setVariable("{clave}", arg0.getParameter("clave") );
                    String result = tem.toString(top);
                    
                    
                    // Se envía mail al cliente
                    //===========================
                    MailDTO mail = new MailDTO();
                    mail.setFsm_subject( rb.getString("mail.registro.subject") );
                    mail.setFsm_data( result );
                    mail.setFsm_destina( cliente.getEmail() );
                    mail.setFsm_remite( rb.getString( "mail.registro.remite" ) );
                    biz.addMail( mail );
                    
                    //En en futuro se verificará si es empleado
                    //session.setAttribute("ses_empleado_paris", "NO");
                    
                    // Se almacena el nombre del cliente en la sesión
                    session.setAttribute("ses_cli_id", id + "");
        			//[20121107avc
        			session.setAttribute("ses_colaborador", String.valueOf(cliente.isColaborador()));
        			//]20121107avc
                    session.setAttribute("ses_dir_id", idDir + "");
                    session.setAttribute("ses_cli_nombre", cliente.getNombre() + " "+ cliente.getApellido_pat());
                    session.setAttribute("ses_cli_nombre_pila", cliente.getNombre());
                    session.setAttribute("ses_cli_rut", cliente.getRut() + "");
                    session.setAttribute("ses_cli_dv", cliente.getDv());
                    session.setAttribute("ses_cli_email", cliente.getEmail());

                    // Recupera pagina desde web.xml
                    //String dis_ok = getServletConfig().getInitParameter("dis_ok");
                    //arg1.sendRedirect(dis_ok);
                }
                //INI - carro donald
                //========================
                //try {
                //    if ( "1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() ) && session.getAttribute("ses_invitado_id") != null ) {
//                            DireccionesDTO dir = biz.getDireccionClienteByComuna( cliente.getId(), Long.parseLong( loc[1].toString() ) );
//                            if ( dir.getId() > 0 ) {
//                                session.setAttribute("ses_loc_id", String.valueOf( dir.getLoc_cod() ));
//                                session.setAttribute("ses_dir_id", String.valueOf( dir.getId() ));
//                                session.setAttribute("ses_dir_alias", dir.getAlias() );
//                                session.setAttribute("ses_zona_id", String.valueOf( dir.getZona_id() ));
//                                session.setAttribute("ses_forma_despacho", "N");
//                            }
                        //Si tiene carrodonald borrar lo que tenga antes
                //        biz.convierteCarroDonald(cliente.getId(), session.getAttribute("ses_invitado_id").toString());    
                //    }
                //} catch (Exception e) { }
                //FIN - carro donald
            }
		} catch (Exception ex) {
			this.getLogger().error("Problemas generales", ex);
            respuesta = "Ocurrió un error al Registrar al Cliente.";
			throw new SystemException(ex);
		}
        
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        try {
            arg1.getWriter().write("<datos_objeto>");
            arg1.getWriter().write("<respuesta>" + respuesta + "</respuesta>");
            // 05/10/2012 : INICIO COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com} 
            if(respuesta.equalsIgnoreCase("OK"))
            	arg1.getWriter().write("<id>"+id+"</id>");
         // 05/10/2012 : FIN COREMETRICS
            arg1.getWriter().write("</datos_objeto>");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
	}

	/**
	 * Validación de campos de ingreso
	 * 
	 * @param arg0 HttpServletRequest
	 * @param arg1 HttpServletResponse
	 * @return true o false
	 */
	private boolean validateParametersLocal(HttpServletRequest arg0) {
		Logging logger = this.getLogger();

		ArrayList campos = new ArrayList();
        campos.add("cli_rut");
        campos.add("nombre");
        campos.add("ape_pat");
        campos.add("id_region");
        campos.add("id_comuna");
        campos.add("email");
        campos.add("envioEMail");
        campos.add("fon_cod");
        campos.add("fon_num");
        campos.add("envioSMS");
        campos.add("clave");
        
		//campos.add("skp");

		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null
					|| arg0.getParameter(campo).compareTo("") == 0 ) {
				logger.error("Falta parámetro: " + campo + " en RegisterNew");
				arg0.setAttribute("cod_error", Cod_error.GEN_FALTAN_PARA);
				return false;
			}
		}

		return true;

	}

}