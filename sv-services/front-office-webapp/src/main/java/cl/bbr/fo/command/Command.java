package cl.bbr.fo.command;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;

import cl.bbr.fo.exception.SystemException;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.pedidos.dto.DireccionMixDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.log.Logging;
//20120822 Andres Valle
//import com.ibm.websphere.models.config.applicationserver.webcontainer.Cookie;
//-20120822 Andres Valle

/**
 * Clase abstracta de la cual heredan todos los comandos.
 * 
 * @author BBR e-commerce & retail
 *  
 */
public abstract class Command extends HttpServlet implements Servlet {

	/**
	 * EAvendanoA
	 */
	public static final String KEY_OVERRIDE_WEBPAY = "cell/persistent/jumbo/front/webpay/override";
	public static final String ENABLE_OVERRIDE_WEBPAY = "SI";
	public static final String DISABLE_OVERRIDE_WEBPAY = "NO";
	public static final long ID_USUARIO_FONO_COMPRAS_GENERICO = 3587;
	
    static final long   ID_LOCAL_DONALD         = 1;
    static final long   ID_ZONA_DONALD          = 1;
    static final String NOMBRE_COMUNA_DONALD    = "Las Condes";
    
    static final String FACEBOOK_SESSION	= "FACEBOOK_SESSION";
    static final String JUMBO_SESSION		= "JUMBO_SESSION";
    

	/**
	 * Instancia un logger
	 */
	protected Logging logger = new Logging(this);

	/**
	 * C?digo de error
	 */
	protected String cod_error = "";

	/*
	 * (sin Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {

		// Seteamos el usuario de la session al log
		HttpSession session = arg0.getSession();

		javax.servlet.http.Cookie[] cookies = arg0.getCookies();
		
		javax.servlet.http.Cookie cookie = null;
		
		if (cookies !=null) {
			
			for(int i=0; i<cookies.length;i++) {
				if(cookies[i].getName().equals("JSESSIONID")) {
					cookie = cookies[i];
					break;
				}
			}
		}
		
		
		//TODO OPENDONALD Si es donald conectandose a algun lugar distinto de: home y categorias... se redirecciona a categorias
		//      System.out.println("SessID:" + session.getId());

		if (session.getAttribute("ses_cli_id") != null && session.getAttribute("ses_cli_nombre") != null)
			logger.setUsuario((String) session.getAttribute("ses_cli_id"), (String) session.getAttribute("ses_cli_nombre"));
		else 
//20120822 Andres Valle
			logger.setUsuario("GUEST", "GUEST");
//-20120822 Andres Valle			
        if (session != null)
            logger.setSession(session.getId().substring(session.getId().length()-5));
        
        if (session.getAttribute("ses_cupon_descuento_object")!= null){
        	removeSessionCuponDescuento(arg0);
        }
        
        
        logger.setUsrAgent(arg0.getHeader("User-Agent"));
        if (arg0.getHeader("X-Forwarded-For") != null)
            logger.setUsrIP(arg0.getHeader("X-Forwarded-For"));
        else if (arg0.getHeader("x-forwarded-for") != null) 
            logger.setUsrIP(arg0.getHeader("x-forwarded-for"));
        else if (arg0.getHeader("X_Forwarded_For") != null)
            logger.setUsrIP(arg0.getHeader("X_Forwarded_For"));
        else if (arg0.getHeader("HTTP_X_FORWARDED_FOR") != null)
            logger.setUsrIP(arg0.getHeader("HTTP_X_FORWARDED_FOR"));
        else
            logger.setUsrIP(arg0.getRemoteAddr());
        
		// Escribe informaci?n en el log al iniciar el comando
		logger.inicio_comando(this);
		boolean bol_res = true;
//20120822 Andres Valle
		if (cookie !=null) {
		logger.info("JSESSIONID:"+cookie.getValue()+" SESSIONID:"+session.getId());
		}
//-20120822		
		try {
			if (VerifyAccessControl(arg0, arg1) == false) {
				this.cod_error = Cod_error.GEN_ERR_PERMISOS;
				bol_res = false;
			} else if (this.ValidateParameters(arg0, arg1) == false) {
				logger.error("Faltan par?metros m?nimos (Command): " + Cod_error.GEN_FALTAN_PARA);
				this.cod_error = Cod_error.GEN_FALTAN_PARA;
				bol_res = false;
			} else if (!this.validateDonaldAccess(arg0, session)) {
				logger.error("No tiene permiso para esta secci?n: " + Cod_error.GEN_ERR_PERMISOS);
				this.cod_error = Cod_error.GEN_ERR_PERMISOS;
				bol_res = false;
				//Aca enviar a supermercado tal vez  
				arg1.sendRedirect("/FO/CategoryDisplay");
				return;
			} else {
				arg1.setHeader("Cache-control", "no-cache");
				arg1.setHeader("Pragma", "no-cache");
				arg1.setDateHeader("Expires", 0);
				arg1.setContentType("text/html");
				execute(arg0, arg1);
			}

			if (bol_res == false)
				ErrorTaskCommand(arg0, arg1);
			
			logger.fin_comando(this);

		} catch (Exception e) {
			if (e.getMessage() == null)
				logger.fatal("getmessage (Command): " + e.getClass().getName());
			else
				logger.fatal("getmessage (Command): " + e.getMessage());
			logger.fin_comando(this);
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			arg1.sendRedirect(rb.getString("command.dir_error"));
		}
	}

	/**
	 * @param arg0
	 * @return
	 */
	protected boolean validateDonaldAccess(HttpServletRequest arg0, HttpSession session) {
		//logger.debug( "ACA ESTAMOS->" + arg0.getRequestURI().toString() + " CON ses_cli_id:" + session.getAttribute("ses_cli_id") );
		/*INDRA 13-11-2012*/
		String servlet = "OrderSetListInvitado,OrderComplete,AjaxMensajeRegistrar,OrderDisplay,PasoDosAutocompletar,PasoDosResultado,PasoDosPub,AjaxDespachoChart,OrderItemDisplay,CategoryDisplay,LogonForm,ProductDisplay,AddProductosCarroPaso1,RegionesConCobertura,ComunasConCoberturaByRegion,ChangeComunaSession,ValidarProductoByLocal,ValidarProductoSinStock,PasoDosAgregarCarro,ModProductosCarroPaso1,CheckLogin,RegisterForm,RegisterCheck,RegisterNew,ComunasList,GifCaptcha,MailHome,SendAnswerQuestion,ChangeForgottenPasswordForm,ResetPasswordForm,ChangeForgottenPassword,ViewPromocionesCh,ComunasByRegion,ValidaGifCaptcha,ContactoWeb,MiCarro,Despacho,Pago,AjaxGetZona,AjaxCategoriasTop,AjaxCategoriasLeft,ValidaCarroCompras,ValidaHoraCompra,OrderCreate,AjaxPago,CuponUpd,AjaxMiCarro,ValidaProductosDespublicadosCarro,ValidaProductosDespublicadosListas,EliminaProductosCarro,Rfl,CuponDsctoCheck,AjaxExisteCliente,AjaxTraeDireccion,ClienteKCC,ClientePR,PreventaSG6";
		/*INDRA 13-11-2012*/
		String[] aux_text = servlet.split(",");
		if (session != null && session.getAttribute("ses_cli_id") == null) {
			return true;
		} else if ("1".equalsIgnoreCase(session.getAttribute("ses_cli_id").toString())) {
			for (int i = 0; i < aux_text.length; i++) {
				if (arg0.getRequestURI().toString().indexOf(aux_text[i]) > 0)
					return true;
			}
			return false;
		}
		return true;
	}

	/*
	 * (sin Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		doGet(arg0, arg1);
	}

	public void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		doGet(arg0, arg1);
	}

	/**
	 * Proceso que ejecuta el proceso, se implementa en las clases que heredan
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws Exception
	 */
	protected abstract void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception;

	/**
	 * Retorna la instancia del logger
	 * 
	 * @return logger
	 */
	protected Logging getLogger() {
		return logger;
	}

	/**
	 * Valida par?metros m?nimos
	 * 
	 * @param arg0
	 *           HttpServletRequest
	 * @param arg1
	 *           HttpServletResponse
	 * @return True o false
	 */
	protected boolean ValidateParameters(HttpServletRequest arg0, HttpServletResponse arg1) {
		return true;
	}

	/**
	 * Verifica control de acceso
	 * 
	 * @param arg0
	 *           HttpServletRequest
	 * @param arg1
	 *           HttpServletResponse
	 * @return True o false
	 * @throws SystemException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	protected boolean VerifyAccessControl(HttpServletRequest arg0, HttpServletResponse arg1) throws NumberFormatException, SystemException, IOException {

		ResourceBundle rb = ResourceBundle.getBundle("fo");
		String servlet = rb.getString("command.servlet.sin.control.acceso");
		String servlet_activo = rb.getString("command.servlet.sin.control.acceso_activo");
		String[] aux_text = servlet.split(",");
		
		if("1".equals(servlet_activo)){		
			for (int i = 0; i < aux_text.length; i++) {
				if (arg0.getRequestURI().toString().indexOf(aux_text[i]) > 0)
					return true;		
			}	
		}else{			
			return true;
		}
		
		return false;		
	}

	/**
	 * Reacciona ante errores del comando
	 * 
	 * @param arg1
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void ErrorTaskCommand(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		ResourceBundle rb = ResourceBundle.getBundle("fo");
		String cod_error = this.cod_error;
		HttpSession session = arg0.getSession();

		session.setAttribute("cod_error", cod_error);

		if (cod_error == Cod_error.GEN_ERR_PERMISOS) {
			arg1.sendRedirect(rb.getString("command.sin_permisos"));
		} else {
			arg1.sendRedirect(rb.getString("command.dir_error"));
		}
	}

	protected String getWebPayOverride() {
		String overrideWebPay = DISABLE_OVERRIDE_WEBPAY;
		Context ctx = null;
		try {
			ctx = new InitialContext();
			overrideWebPay = (String) ctx.lookup(KEY_OVERRIDE_WEBPAY);
			
			if ((DISABLE_OVERRIDE_WEBPAY.equals(overrideWebPay) || ENABLE_OVERRIDE_WEBPAY.equals(overrideWebPay))) {
				logger.info("El valor de override webpay es consistente");
			} else {
				logger.info("El valor de override webpay es inconsistente... se asigna valor por defecto...");
				overrideWebPay = DISABLE_OVERRIDE_WEBPAY;
			}
		} catch (NamingException e) {
			logger.fatal("Error obteniendo flag override_webpay : " + e.getMessage());
		} finally {
			try {
				ctx.close();
				ctx = null;
			} catch (Exception e) {
		    } 
		    ctx = null;
		}
		return overrideWebPay;
	}
	
    /**
     * @param session
     */
    protected void toMakeDonaldSession(HttpSession session) {
        session.setAttribute("ses_cli_id", "1" );
		//[20121107avc
		session.setAttribute("ses_colaborador", "false");
		//]20121107avc
        session.setAttribute("ses_cli_nombre", "Invitado");
        session.setAttribute("ses_cli_nombre_pila", "Invitado");
        session.setAttribute("ses_cli_apellido_pat", "");
        session.setAttribute("ses_cli_rut", "123123");
        session.setAttribute("ses_cli_dv", "3");
        session.setAttribute("ses_cli_email", "");
        if ( session.getAttribute("ses_eje_id") != null ) {
            session.removeAttribute("ses_eje_id");
        }
        session.setAttribute("ses_cli_confiable", "N");    
        session.setAttribute("ses_loc_id", "" + ID_LOCAL_DONALD);
        session.setAttribute("ses_dir_id", "1");
        session.setAttribute("ses_dir_alias", "");
        session.setAttribute("ses_zona_id", "" + ID_ZONA_DONALD);
        session.setAttribute("ses_forma_despacho", "N");
        session.setAttribute("ses_id", session.getId());
        session.setAttribute("ses_comuna_cliente", "");
        session.setAttribute("ses_oJsonFacebook", null);
    }

    
    protected void sessionStart(HttpServletRequest request,BizDelegate biz, ClienteDTO cliente, String via) throws Exception {
    	
    	HttpSession session = request.getSession(true);
			
		// Se almacena el nombre del cliente en la sesi?n
    	session.setAttribute("ses_via_sessionStart", via);
		session.setAttribute("ses_cli_id", cliente.getId() + "" );
		session.setAttribute("ses_colaborador", String.valueOf(cliente.isColaborador()));
		session.setAttribute("ses_cli_nombre", cliente.getNombre() + " " + cliente.getApellido_pat());
		session.setAttribute("ses_cli_nombre_pila", cliente.getNombre());
        session.setAttribute("ses_cli_apellido_pat", cliente.getApellido_pat());
		session.setAttribute("ses_cli_rut", cliente.getRut() + "");
		session.setAttribute("ses_cli_dv", cliente.getDv());
		session.setAttribute("ses_cli_email", cliente.getEmail());
        
        if ( session.getAttribute("ses_eje_id") != null ) {
            session.removeAttribute("ses_eje_id");
        }
        
        if ( biz.clienteEsConfiable( cliente.getRut() ) && !biz.tieneEventosActivosConValidacionManual( cliente.getRut() ) ) {
            session.setAttribute("ses_cli_confiable", "S");
        } else {
            session.setAttribute("ses_cli_confiable", "N");    
        }
        
        //to put in session, every parameter used by post-welcome
        //LBOX      -> LightBox del Home
        //LBOX_S    -> LightBox desde supermercado (categorias)
        boolean dirSelected = false;
        if ( request.getParameter("type_log") != null && "LBOX_S".equalsIgnoreCase( request.getParameter("type_log").toString() ) ) {
            if ( !"".equalsIgnoreCase( session.getAttribute("ses_comuna_cliente").toString() )) {
                //Donald ha seleccionado una comuna, entonces tengo que traer la direccion de esa comuna
                String[] loc = session.getAttribute("ses_comuna_cliente").toString().split("-=-");
                try {
                    DireccionesDTO dir = biz.getDireccionClienteByComuna( cliente.getId(), Long.parseLong( loc[1].toString() ) );
                    if ( dir.getId() > 0 ) {
                    	session.setAttribute("ses_zona_id", String.valueOf( dir.getZona_id() ));
                        session.setAttribute("ses_loc_id", String.valueOf( dir.getLoc_cod() ));
                        session.setAttribute("ses_dir_id", String.valueOf( dir.getId() ));
                        session.setAttribute("ses_dir_alias", dir.getAlias() );                        
                        session.setAttribute("ses_forma_despacho", "N");
                        dirSelected = true;
                    }
                } catch (Exception e) { }
            }
            String invitado_id = "";
            if (session.getAttribute("ses_invitado_id") != null && !session.getAttribute("ses_invitado_id").toString().equals("")){
                invitado_id = session.getAttribute("ses_invitado_id").toString();
            }
            //Si tiene carrodonald borrar lo que tenga antes
            if (!(session.getAttribute("ses_invitado_id_set")==null)){
            	long idCliente = cliente.getId();
            	String idSession =session.getAttribute("ses_invitado_id_set").toString();
            	long idInvitado = Long.parseLong(session.getAttribute("ses_cli_id_set").toString());
            	biz.convierteCarroDonaldAfterPago(idCliente, idSession, idInvitado);
        	}else{
        		biz.convierteCarroDonald(cliente.getId(), invitado_id);
        	}
        }
        
        if ( !dirSelected ) {
        	 try {
        		 DireccionMixDTO oDireccionMixDTO = biz.getDireccionIniciaSessionCliente(cliente.getId());
        		 if(oDireccionMixDTO.isDireccionMix()){
        			 session.setAttribute("ses_zona_id", String.valueOf(oDireccionMixDTO.getSes_zona_id() ));
                     session.setAttribute("ses_loc_id", String.valueOf( oDireccionMixDTO.getSes_loc_id() ));
                     session.setAttribute("ses_dir_id", String.valueOf( oDireccionMixDTO.getSes_dir_id() ));
                     session.setAttribute("ses_dir_alias", oDireccionMixDTO.getSes_dir_alias());                        
                     session.setAttribute("ses_forma_despacho", oDireccionMixDTO.getSes_forma_despacho()); 
        		 }else{
        			 session.setAttribute("ses_destination", "UltComprasForm?opcion=1");
        		 }
        	   } catch (Exception e) { 
        		   session.setAttribute("ses_destination", "UltComprasForm?opcion=1");
        	   }
        	
        	/*
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
                  
            	List listDir = biz.clienteAllDireccionesConCobertura( cliente.getId() );
            	
                if ( listDir.size() >0) {
                	DireccionesDTO dir = (DireccionesDTO) listDir.get(0);                     	

                	session.setAttribute("ses_zona_id", String.valueOf( dir.getZona_id() ));
                    session.setAttribute("ses_loc_id", String.valueOf( dir.getLoc_cod() ));
                    session.setAttribute("ses_dir_id", String.valueOf( dir.getId() ));
                    session.setAttribute("ses_dir_alias", dir.getAlias() );                    
                    session.setAttribute("ses_forma_despacho", "N");
                    
                } else {
                	session.setAttribute("ses_destination", "UltComprasForm?opcion=1");
                }
            }
            */
        }
    
        session.setAttribute("ses_id", "");
        session.setAttribute("ses_invitado_id", "0");

        
        if ( session.getAttribute("ses_cli_id") != null && 
        		!"1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString()) &&
        			session.getAttribute("ses_dir_id") != null && 
        				!"1".equalsIgnoreCase( session.getAttribute("ses_dir_id").toString() )) {
        	
            DireccionesDTO dir = biz.clienteGetDireccion(Long.parseLong(session.getAttribute("ses_dir_id").toString()));
            session.setAttribute("ses_comuna_cliente", ""+dir.getReg_id()+"-=-"+dir.getCom_id()+"-=-"+dir.getCom_nombre());
            
        } else {
            session.setAttribute("ses_comuna_cliente", ""); //Reg_id-=-Com_id-=-Com_nombre    
        }
    	
    }

    protected void removeSessionCuponDescuento(HttpServletRequest request){
		HttpSession session = request.getSession(true);            
		//servlet_en_donde_persiste
		String servlet = "Pago,CuponDsctoCheck,OrderCreate,OrderComplete,OrderDisplay,AjaxDespachoChart,GuardaDatosDespacho";
		String[] aux_text = servlet.split(",");
		boolean go = false;
		String uri = request.getRequestURI().toString();
		for (int i = 0; i < aux_text.length; i++) {
			String prefix = "/FO/"+aux_text[i];	  
			if (uri.startsWith(prefix)){
			    go = true;
			    break;
			}
		} 
		if (!go)
			session.setAttribute("ses_cupon_descuento_object",null); 
    }
    
    public boolean isNumeric( String s ){
        try{
            double y = Double.parseDouble( s );
            return true;
        }
        catch( NumberFormatException err ){
            return false;
        }
    }
}