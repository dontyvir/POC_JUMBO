package cl.bbr.fo.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.conexion.ConexionUtil;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDetalleFODTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.CuponPedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.TcpPedidoDTO;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.bbr.promo.lib.dto.FOTcpDTO;

/**
 * Crea el pedido en estado pre-ingresado, para ser pagado con webpay o cat
 * 
 *  
 */
public class OrderCreate extends Command {  
	
    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {

        ResourceBundle rb = ResourceBundle.getBundle("fo");
        String errormailTo = rb.getString("mailerror.to");
        String errormailFrom = rb.getString("mailerror.from");
        String errormailCc = rb.getString("mailerror.cc");
        String errormailSmtp = rb.getString("mailerror.smtp");
        long id_estado= 0;
        // Recupera la sesión del usuario
        String mensaje = "OK";
        HttpSession session = arg0.getSession();
        long idPedido = 0;
        long invitado_id=0;
        PedidoDTO newPedido = new PedidoDTO();

        // Se revisan que los parámetros mínimos existan
        if (!validateParametersLocal(arg0) || session.getAttribute("ses_zona_id") == null || session.getAttribute("ses_forma_despacho") == null) {
            this.getLogger().error("Faltan parámetros mínimos (OrderCreate): " + Cod_error.REG_FALTAN_PARA);
            mensaje = "Faltan parámetros para continuar";

        } else {
            try {
            	
                BizDelegate biz = new BizDelegate();

                this.getLogger().info(" [OrderCreate - ses_cli_id] : " + session.getAttribute("ses_cli_id").toString());

                long idCliente = Long.parseLong(session.getAttribute("ses_cli_id").toString());

                //se crea el Cliente si es invitado, se asocia su carro y sus
                // criterios de sustitución
                if(session.getAttribute("ses_cli_rut").toString().equals("123123")){
                	session.setAttribute("ses_invitado_id_set", session.getAttribute("ses_invitado_id"));
                    this.getLogger().info("El cliente era invitado. Vamos a crear uno no invitado");
                    //Se genera un cliente DTO
                    ClienteDTO cliente;
                   
                    long id = -1;
                    long id_dir = -1;
                    String respuesta = "";
                    long fec_crea = 0;
                    long recibeSms = 0;
                    this.getLogger().info("Intenta crear objeto cliente.");
                    cliente = new ClienteDTO();
                    cliente.setId(id);
                    cliente.setRut(Long.parseLong((String) session.getAttribute("ses_cli_rut")));
                    cliente.setDv((String) session.getAttribute("ses_cli_dv"));
                    cliente.setNombre((String) session.getAttribute("ses_cli_nombre"));
                    cliente.setApellido_pat((String) session.getAttribute("ses_cli_apellido_pat"));
                    cliente.setApellido_mat("");
                    cliente.setEmail((String) session.getAttribute("ses_cli_email"));
                    cliente.setRecibeSms(recibeSms);
                    cliente.setFec_crea(fec_crea);
                    cliente.setFec_nac(fec_crea);
                    cliente.setEstado("A");
                    cliente.setGenero("M");
                    cliente.setClave((String) session.getAttribute("ses_cli_rut").toString().substring(5));
                    cliente.setFon_cod_2(session.getAttribute("tel_despacho").toString());
                    cliente.setFon_num_2(session.getAttribute("ses_telefono").toString());
                    this.getLogger().info("Intenta crear objeto direcciones.");
                    DireccionesDTO desp = new DireccionesDTO();
                    if (((String) session.getAttribute("ses_forma_despacho")).equals("D")) {
                        desp.setAlias((String) session.getAttribute("ses_dir_alias"));
                        desp.setCalle((String) session.getAttribute("calle"));
                        desp.setTipo_calle(Long.parseLong((String) session.getAttribute("tipo_calle")));
                        desp.setNumero((String) session.getAttribute("numero"));
                        desp.setDepto((String) session.getAttribute("departamento"));
                        desp.setReg_id(Long.parseLong((String) session.getAttribute("region")));
                        desp.setCom_id(Long.parseLong((String) session.getAttribute("comuna")));
                        desp.setComentarios("");
                    }
                    this.getLogger().info("Llama a la creacion desde invitado.");
                    respuesta = biz.creaClienteDesdeInvitado(cliente, desp, (String) session.getAttribute("ses_forma_despacho"));
                    this.getLogger().info("Llego esta respuesta desde creaClienteDesdeInvitado.");
                    id = Long.parseLong(respuesta.split("--")[0]);
                    if (id == 0) {
                        this.getLogger().error("CUEK! el creaClienteDesdeInvitado respondio id = 0 resp=" + respuesta);
                        throw new CommandException();
                    }
                    id_dir = Long.parseLong(respuesta.split("--")[1]);
                    if ((((String) session.getAttribute("ses_forma_despacho")).equals("D")) && (id_dir == 0)) {
                        this.getLogger().error("CUEK! el creaClienteDesdeInvitado respondio id_dir = 0 resp=" + respuesta);
                        throw new CommandException();
                    }
                    idCliente = id;
                    session.setAttribute("ses_cli_id_set", id + "");
                    session.setAttribute("ses_cli_id", id + "");
        			//[20121107avc
        			session.setAttribute("ses_colaborador", String.valueOf(cliente.isColaborador()));
        			//]20121107avc
                    session.setAttribute("ses_dir_id", id_dir + "");
                    invitado_id = -1;
                    if (session.getAttribute("ses_invitado_id") != null && !session.getAttribute("ses_invitado_id").toString().equals("")) {
                        invitado_id = Long.parseLong(session.getAttribute("ses_invitado_id").toString());
                        //+20120926avc
                        id_estado= Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION; 
                        if (invitado_id == 0) {
                            invitado_id = -1;
                            logger.error("DEMONIOS! el ses_invitado_id convirtio a cero! Venia con:" + session.getAttribute("ses_invitado_id").toString());
                        }
                        //-20120926avc
                    } else {
                        //20120829 Aplicaciones
                        this.getLogger().error("[session.getAttribute : ].... sess_invitado_id es null o espacios");
                         
                        getLogger().info("Volcando ServletRequest...{HeaderNames}");
                        Enumeration headers = arg0.getHeaderNames();

                        while (headers.hasMoreElements()) {

                            String h = (String) headers.nextElement();
                            getLogger().error("HeaderName{" + h + "},value{" + arg0.getHeader(h) + "}");

                        }

                        getLogger().info("Volcando ServletRequest...{ParameterNames}");
                        Enumeration params = arg0.getParameterNames();

                        while (params.hasMoreElements()) {

                            String p = (String) params.nextElement();
                            getLogger().error("ParameterName{" + p + "},value{" + arg0.getParameter(p) + "}");

                        }

                        getLogger().info("Volcando ServletRequest...{AttributeNames}");
                        Enumeration attributes = arg0.getAttributeNames();

                        while (attributes.hasMoreElements()) {

                            String a = (String) attributes.nextElement();
                            getLogger().error("ParameterName{" + a + "},value{" + arg0.getAttribute(a) + "}");

                        }
                        //-20120829 Aplicaciones

                    }

                    //Se asocia el carro de compras al Nuevo ID del Cliente
                    this.getLogger().info("Vamos a asociar el nuevo invitado_id al carro (" + invitado_id + ")" + "(" + id_estado +")");
                    biz.reasignaCarroDelInvitado(id, invitado_id);

                    //Se asocian los criterios de sustitución al Nuevo ID de
                    // Cliente
                    this.getLogger().info("Vamos a reasignar los criterios de sust al nuevo invitado_id al carro (" + invitado_id + ")");
                    biz.reasignaSustitutosDelInvitado(id, invitado_id);
                }else {
                	
                	this.getLogger().info("Vamos a asociar el nuevo invitado_id al carro (" + invitado_id + ")" + "(" + id_estado +")");
                }
                if (session.getAttribute("ses_invitado_id_set")!=null){
                	
                	System.out.println("ses_invitado_id_set:"+session.getAttribute("ses_invitado_id_set"));
                }
                // Revisa que existan productos en el carro de compras
                List lcarro = biz.carroComprasGetProductos(idCliente, session.getAttribute("ses_loc_id_prod").toString(), null);
                //List lcarro = biz.carroComprasGetProductos(idCliente, "-1",
                // null);
                if (lcarro.size() <= 0) {
                    // No existen productos se retorna error
                    this.getLogger().error("No existen productos en el carro: " + Cod_error.CHECKOUT_SIN_PROD);
                    mensaje = "No existen productos en el carro";

                } else {
                    this.getLogger().info("Intenta borrar pedido pre ingresado ");
                    //Si pedido existe, limpiamos capacidades tomadas y se
                    // borra el pedido en estado 1
                    if (session.getAttribute("ses_id_pedido") != null) {
                        PedidoDTO pedidoOld = biz.getPedidoById(Long.parseLong(session.getAttribute("ses_id_pedido").toString()));
                        if (pedidoOld.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PRE_INGRESADO) {
                            biz.purgaPedidoPreIngresado(pedidoOld, idCliente);
                            //borramos de sesion el pedido antiguo
                            session.removeAttribute("ses_id_pedido");
                        }
                    }

                    // Carga los datos en el objeto
                    ProcInsPedidoDTO pedido = new ProcInsPedidoDTO();
             
                    // datos pedido
                    pedido.setId_cliente(idCliente);
                    pedido.setId_estado(id_estado);
                    if (arg0.getParameter("dispositivo") != null) {
                        pedido.setDispositivo(arg0.getParameter("dispositivo")); //Si
                        // es I
                        // es
                        // compra
                        // via
                        // IPHONE
                    } else {
                        pedido.setDispositivo("B");
                    }

                    String overrideWebPay = super.getWebPayOverride();

                    logger.debug("overrideWebPay: " + overrideWebPay);

                    if (session.getAttribute("ses_eje_id") != null) {

                        pedido.setId_usuario_fono(Long.parseLong(session.getAttribute("ses_eje_id").toString()));

                        if (arg0.getParameter("mobile_fono") != null) {

                            pedido.setDispositivo(arg0.getParameter("mobile_fono")); //Si
                                                                                     // es I
                                                                                     // es
                                                                                     // compra
                                                                                     // via
                                                                                     // mobile

                        }

                    } else if (ENABLE_OVERRIDE_WEBPAY.equals(overrideWebPay) && Long.parseLong(arg0.getParameter("forma_pago")) == 2) {

                        pedido.setId_usuario_fono(ID_USUARIO_FONO_COMPRAS_GENERICO);

                    }

                    if (arg0.getParameter("pol_sustitucion") != null) {
                        String[] pol_aux = arg0.getParameter("pol_sustitucion").split("--");
                        pedido.setPol_id(Long.parseLong(pol_aux[0]));
                        pedido.setPol_sustitucion(pol_aux[1]);
                    }
                    pedido.setSin_gente_op(0);
                    pedido.setObservacion(arg0.getParameter("observacion"));
//                  +20121018avc
                    if(session.getAttribute("ses_forma_despacho") == null) {
                        throw new Exception("sesion no tiene ATRIBUTO ses_forma_despacho");
                    }
//                  -20121018avc
                   
                    if (session.getAttribute("ses_forma_despacho").toString().equalsIgnoreCase("R")) {
                    	pedido.setSin_gente_txt(arg0.getParameter("retira_txt").replaceAll("\\+", " "));
                        //+20121018avc
                        if(arg0.getParameter("sin_gente_rut") == null)
                            throw new Exception("sesion no tiene PARAMETRO sin_gente_rut");
                        //-20121018avc
                        if (!arg0.getParameter("sin_gente_rut").toString().equalsIgnoreCase("")) {
                            pedido.setSin_gente_rut(Long.parseLong(arg0.getParameter("sin_gente_rut")));
                        } else {
                            pedido.setSin_gente_rut(0);
                        }
                        pedido.setSin_gente_dv(arg0.getParameter("sin_gente_dv"));
                    } else {
                        pedido.setSin_gente_txt(arg0.getParameter("sin_gente_txt"));
                        pedido.setSin_gente_rut(0);
                        pedido.setSin_gente_dv("");
                    }
                    pedido.setTipo_doc(arg0.getParameter("tipo_documento"));

                    //+20121018avc
                    if(arg0.getParameter("forma_pago") == null)
                        throw new Exception("sesion no tiene PARAMETRO forma_pago");
                    //-20121018avc
                    
                    if (Long.parseLong(arg0.getParameter("forma_pago")) == 1) {
                        pedido.setMedio_pago("CAT");   
                        pedido.setNom_tbancaria("TARJETA MAS"); 
                    } else if (Long.parseLong(arg0.getParameter("forma_pago")) == 2) {
                        pedido.setMedio_pago("TBK");
                        pedido.setNom_tbancaria("Transbank");
                    } else {
                        //Error, si medio de pago
                        this.getLogger().error("CUEK! el parametro del form forma_pago no es 1 ni 2.");
                        throw new CommandException();
                    }
                    logger.debug("WebPay........: " + Long.parseLong(arg0.getParameter("forma_pago")));
                    // Datos despacho
                    //+20121018avc
                    if(arg0.getParameter("tipo_despacho") == null)
                        throw new Exception("sesion no tiene PARAMETRO tipo_despacho");
                    //-20121018avc
                    if ("C".equalsIgnoreCase(arg0.getParameter("tipo_despacho"))) {
                        pedido.setId_jdespacho(Long.parseLong(arg0.getParameter("jdespacho").replaceAll("-", "")));
                    } else {
                        pedido.setId_jdespacho(Long.parseLong(arg0.getParameter("jdespacho")));
                    }
                    if (!(session.getAttribute("despacho_local")==null))
                    	pedido.setTipo_despacho((String) session.getAttribute("despacho_local"));
                    else	
                    	pedido.setTipo_despacho(arg0.getParameter("tipo_despacho"));//R:
                    //pedido.setTipo_despacho((String)session.getAttribute("ses_forma_despacho"));
                    // RETIRO
                    // EN
                    // LOCAL
                    //+20121018avc
                    if(session.getAttribute("ses_zona_id") == null)
                        throw new Exception("sesion no tiene ATRIBUTO ses_zona_id");
                    //-20121018avc
                    pedido.setId_zona(Long.parseLong(session.getAttribute("ses_zona_id").toString()));
                    //+20121018avc
                    if(session.getAttribute("ses_loc_id") == null)
                        throw new Exception("sesion no tiene ATRIBUTO ses_loc_id");
                    //-20121018avc
                    pedido.setId_local_desp(Long.parseLong(session.getAttribute("ses_loc_id").toString())); //Usado
                    // para
                    // el
                    // local
                    // de
                    // retiro
                    pedido.setFecha_despacho(arg0.getParameter("jfecha"));
                    session.setAttribute("ses_horas_economico", arg0.getParameter("horas_economico"));
                    this.getLogger().debug("TIPO DESPACHO: " + pedido.getTipo_despacho());
                    this.getLogger().debug("ZONA DESPACHO: " + pedido.getId_zona());
                    this.getLogger().debug("FECHA DESPACHO: " + pedido.getFecha_despacho());

                    // datos direccion despacho
                    pedido.setDir_id(Long.parseLong(session.getAttribute("ses_dir_id").toString()));

                    // datos de facturación sólo si es factura
                    if (pedido.getTipo_doc().compareTo("F") == 0) {
                        pedido.setFac_rut(Long.parseLong(arg0.getParameter("fac_rut").replaceAll("\\.", "").replaceAll("\\-", "")));
                        pedido.setFac_dv(arg0.getParameter("fac_dv"));
                        pedido.setFac_razon(arg0.getParameter("fac_razon"));
                        pedido.setFac_direccion(arg0.getParameter("fac_direccion"));
                        pedido.setFac_fono(arg0.getParameter("fac_cod_1") + "-" + arg0.getParameter("fac_fono"));
                        pedido.setFac_giro(arg0.getParameter("fac_giro"));
                        pedido.setFac_ciudad(arg0.getParameter("fac_ciudad"));
                        pedido.setFac_comuna(arg0.getParameter("fac_comuna"));
                    }
                    
// inicio cdd
                    boolean aplicaCuponDscto = false;
                    boolean isCuponDespacho = false;
                    boolean isCuponProducto = false;
                    boolean isCuponRubro = false;
                    boolean isCuponSeccion = false;
                    boolean isCuponCAT = false;
                    boolean isCuponTBK = false;
                    
                    CuponDsctoDTO cddto = null;            
                    List cuponProds = null;
                    
                    if (session.getAttribute("ses_cupon_descuento_object") != null) {
                    	cuponProds = new ArrayList();
                    	cddto = (CuponDsctoDTO) session.getAttribute("ses_cupon_descuento_object") ;
                    	if (cddto.getTipo().equals("D")) {
                    		isCuponDespacho = true;
                    	} else if (cddto.getTipo().equals("P")){
                    		isCuponProducto = true;
                    		cuponProds = biz.getProdsCupon(cddto.getId_cup_dto(), "P");
                    	} else if (cddto.getTipo().equals("R")){
                    		isCuponRubro = true;
                    		cuponProds = biz.getProdsCupon(cddto.getId_cup_dto(), "R");
                    	} else if (cddto.getTipo().equals("S") || cddto.getTipo().equals("TS")){
                    		isCuponSeccion = true;
                    		cuponProds = biz.getProdsCupon(cddto.getId_cup_dto(), "S");
                    	}
                    	if (cddto.getMedio_pago() == 0) {
                    		isCuponCAT = true;
                    		isCuponTBK = true;
                    	} else if (cddto.getMedio_pago() == 1) {
                    		isCuponCAT = true;
                    	} else {
                    		isCuponTBK = true;
                    	}
                    }
// fin cdd
                    
                    double cantidadProductos = 0.0;
                    double montoTotal = 0.0;
                    // Detalle del pedido
                    List list_det = new ArrayList();
                    // Recuperar los datos de los productos
                    for (int i = 0; i < lcarro.size(); i++) {
                        CarroCompraDTO prods = (CarroCompraDTO) lcarro.get(i);
                        ProcInsPedidoDetalleFODTO detalle = new ProcInsPedidoDetalleFODTO();
                        detalle.setId_producto_fo(Long.parseLong(prods.getPro_id()));
                        detalle.setCant_solic(prods.getCantidad());
                        detalle.setCon_nota("");
                        if (prods.getNota() == null) {
                            detalle.setObservacion("");
                        } else {
                            if (prods.getNota().equalsIgnoreCase("null")) {
                                detalle.setObservacion("");
                            } else {
                                detalle.setObservacion(prods.getNota());
                            }
                        }
                        detalle.setPrecio_unitario(prods.getPrecio());
                        detalle.setTipoSel(prods.getTipoSel());
                        detalle.setPrecio_lista(prods.getPrecio());
                        list_det.add(detalle);
                        cantidadProductos += prods.getCantidad();
                        montoTotal += (prods.getCantidad() * prods.getPrecio());

// inicio cdd
                        if(!aplicaCuponDscto) {
	                        if (isCuponProducto){
	                        	Iterator itProds = cuponProds.iterator();
	                        	while (itProds.hasNext()) {
	        						long productoCupon = Long.parseLong(itProds.next().toString());
	        						if(productoCupon == prods.getId_bo()){
	        							aplicaCuponDscto = true;
	        							if(cddto.getDespacho() == 1)
	        								isCuponDespacho = true;
	        							break;
	        						}
	                        	}
	                        } else if (isCuponRubro) {
	                        	Iterator itProds = cuponProds.iterator();
	                        	while (itProds.hasNext()) {
	                        		int productoCupon = Integer.parseInt(itProds.next().toString());
	        						int seccionRubro = Integer.parseInt(prods.getCatsap() + prods.getId_rubro()); 
	        						if(productoCupon == seccionRubro) {
	        							aplicaCuponDscto = true;
	        							if(cddto.getDespacho() == 1)
	        								isCuponDespacho = true;
	        							break;
	        						}
	                        	}              	
	                        } else if (isCuponSeccion) {
	                        	Iterator itProds = cuponProds.iterator();
	                        	while (itProds.hasNext()) {
	                        		int productoCupon = Integer.parseInt(itProds.next().toString());
	        						if(productoCupon == Integer.parseInt(prods.getCatsap())) {
	        							aplicaCuponDscto = true;
	        							if(cddto.getDespacho() == 1)
	        								isCuponDespacho = true;
	        							break;
	        						}
	                        	}
	                        }
                        }
// fin cdd
                        
                    }
                    pedido.setCantidadProductos(cantidadProductos);
                    pedido.setMontoTotal(montoTotal);

                    pedido.setProductos(list_det);

                    // Listado de cupones
                    List lst_cupones = new ArrayList();
                    List l_cupones = null;
                    if (session.getAttribute("ses_cupones") != null)
                        l_cupones = (List) session.getAttribute("ses_cupones");

                    if (l_cupones != null) {
                        this.getLogger().info("Entramos a loop l_cupones");
                        for (int f = 0; l_cupones != null && f < l_cupones.size(); f++) {
                            FOTcpDTO cupon = (FOTcpDTO) l_cupones.get(f);
                            CuponPedidoDTO cupped = new CuponPedidoDTO();
                            cupped.setNro_tcp(cupon.getTcp_nro());
                            cupped.setNro_cupon(cupon.getCupon());
                            cupped.setCant_max(cupon.getTcp_max());
                            lst_cupones.add(cupped);
                        }
                    }
                    pedido.setLst_cupones(lst_cupones);
                    
//                  Listado de TCP
                    List lst_tcp = new ArrayList();
//+ 20130128 AP
//                    Enumeration enum2 = session.getAttributeNames();
//					while (enum2.hasMoreElements()) {
//						String name = (String) enum2.nextElement();
//						logger.info("Session:::------------------>" + name
//								+ ":::" + session.getAttribute(name)
//								+ "<<--------");
//						System.out.println("Session: " + name + " - " + session.getAttribute(name));
//					}

					if (session.getAttribute("ses_promo_tcp") != null) {
						logger.info("ERROR_TCP[ERROR]: rut CON error" + session.getAttribute("ses_cli_rut"));
					} else {
						logger.info("ERROR_TCP[OK]: rut SIN error" + session.getAttribute("ses_cli_rut"));
					}
        			    List l_tcp = null;
                        if (session.getAttribute("ses_promo_tcp") != null)
                            l_tcp = (List) session.getAttribute("ses_promo_tcp");

                        if (l_tcp != null) {
                            this.getLogger().info("Entramos a loop l_tcp");
                            for (int i = 0; l_tcp != null && i < l_tcp.size(); i++) {
                                FOTcpDTO tcp = (FOTcpDTO) l_tcp.get(i);
                                TcpPedidoDTO tcpped = new TcpPedidoDTO();
                                tcpped.setNro_tcp(tcp.getTcp_nro());
                                tcpped.setCant_max(tcp.getTcp_max());
                                lst_tcp.add(tcpped);
                            }
                        }
                    pedido.setLst_tcp(lst_tcp);
        
// inicio cdd                    
                   /* if(session.getAttribute("ses_colaborador").equals("true")) {
                    	pedido.setCosto_desp(1);
                    } else*/
                    	
                    if (isCuponDespacho) {
                    	if (isCuponCAT && "CAT".equals(pedido.getMedio_pago())) {
	                    	pedido.setCosto_desp(1);
	                    } else if (isCuponTBK && "TBK".equals(pedido.getMedio_pago())) {
	                    	pedido.setCosto_desp(1);
	                    }
                    }
// fin cdd       
                    
                    // Inserta pedido
                    try {
                        // --- INI - LAYER EVENTOS PERSONALIZADOS - RESUMEN ---
                        /*
                         * EventoDTO evento = new EventoDTO(); String
                         * codLayerFlash =
                         * UtilsEventos.codHtmlDeFlash(Long.parseLong(session.getAttribute("ses_cli_rut").toString()),
                         * EventosConstants.PASO_RESUMEN); if
                         * (codLayerFlash.length() > 0) { evento =
                         * UtilsEventos.eventoMostrado(Long.parseLong(session.getAttribute("ses_cli_rut").toString())); }
                         * session.setAttribute("codLayerFlash", codLayerFlash);
                         * session.setAttribute("eventoLayerFlash", evento);
                         */
                        // --- FIN - LAYER EVENTOS PERSONALIZADOS - RESUMEN ---
                        this.getLogger().info("YAP! ahora vamos a intentar insertar el pedido.");
                        
// inicio cdd
                        if(!aplicaCuponDscto){
                        	cddto = null;
                        	cuponProds = null;
                        }
                        if (session.getAttribute("ses_cli_rut").toString().equalsIgnoreCase("123123")){//si es invitado pasamos los datos del cliente ingresados en la pagina de despacho al pedido para que se guarden al insertarlo
                        	String rut_s = session.getAttribute("rut1_inv").toString();
                            this.getLogger().info("Intento conexion ( RUT:" + rut_s + " )");
                            
                            rut_s = rut_s.replaceAll("\\.","").replaceAll("\\-","");
                            rut_s = rut_s.substring(0, rut_s.length()-1);
                            
                            pedido.setNom_tit(session.getAttribute("nombre_inv").toString());
	                        pedido.setApat_tit(session.getAttribute("ape_pat_inv").toString());
	                        pedido.setRut_tit(rut_s);
	                        
                        }
                        
                        boolean isColaborador=false;
                        if( session.getAttribute("ses_colaborador").equals("true")){
                        	isColaborador=true;
                        }
                        
                        if("R".equals(pedido.getTipo_despacho()) || isColaborador){
                        	pedido.setCosto_desp( Double.valueOf((String.valueOf(session.getAttribute("jprecio")))).longValue()); 
                        }
                        
                        if (session.getAttribute("ses_invitado_id") != null && !session.getAttribute("ses_invitado_id").toString().equals("0")) {
                        	pedido.setInvitado(true);
                        }          
                        
                        if (session.getAttribute("descuento_despacho") != null && session.getAttribute("descuento_despacho").toString().equals("true")) {
                        	pedido.setDescuentoDespacho(true);
                        }
                        
                        idPedido = biz.doInsPedidoNew(pedido, cddto, cuponProds);
// fin cdd
                        session.setAttribute("ses_id_pedido", String.valueOf(idPedido));

                        this.getLogger().info("Pedido pre-ingresado:" + idPedido + " Browser:" + arg0.getHeader("User-Agent"));
                        this.getLogger().info("Es invitado_id ?? (" + invitado_id + ")" + "(" + id_estado +")");
                        //Nos traemos nuevamente el pedido, por si el monto
                        // cambio
                        newPedido = biz.getPedidoById(idPedido);

                    } catch (Exception ex) {
                        this.getLogger().error("Fallo (Error: OrderCreate) ", ex);

                        //FIXME:Error capacidades
                        if (ex.getMessage() != null && ex.getMessage().indexOf("ERR_CAPACIDAD") != -1 ) {
							 mensaje = "ERR_CAPACIDAD";
						} else {
							//ex.printStackTrace();
	                        //20120910avc
	                        int count = getMailCount();
	                        if (count < 78) {
	                            mensaje = "Error al guardar los datos de su pedido";
	                            SendMail mail = new SendMail(errormailSmtp, errormailFrom, (String) session.getAttribute("emailError"), null);
	                            try {
	                                addMailCount();
	                                mail.enviar(errormailTo, errormailCc, "Error: OrderCreate(1). Total: " + session.getAttribute("totalcompra"));
	                                this.getLogger().error("MAIL: OrderCreate(1). Total: " + session.getAttribute("totalcompra"));
	                            } catch (Exception e1) {
	                                this.getLogger().error("Fallo envio de email (Error: OrderCreate) ", e1);
	                            }
	                        }
	                        //20120910-avc							
						} 
                    }
                }
            } catch (Exception e) {

                this.getLogger().error("Fallo (Error: OrderCreate)", e);

                //FIXME BORRAR

                e.printStackTrace();
                if (Constantes._EX_VE_GP_EXCEDE_CAPAC.equalsIgnoreCase(e.getLocalizedMessage())) {
                    this.getLogger().error("ERROR en las capacidades");
                }
                mensaje = "Error al guardar los datos de su pedido";
                SendMail mail = new SendMail(errormailSmtp, errormailFrom, (String) session.getAttribute("emailError"), null);
                try {
                    mail.enviar(errormailTo, errormailCc, "Error: OrderCreate(2). Total: " + session.getAttribute("totalcompra"));
                    this.getLogger().error("MAIL: OrderCreate(2). Total: " + session.getAttribute("totalcompra"));
                } catch (Exception e1) {
                    this.getLogger().error("Fallo envio de email (Error: OrderCreate)", e1);
                }
            }
        }
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        arg1.getWriter().write("<datos_objeto>");
        arg1.getWriter().write("<respuesta>" + mensaje + "</respuesta>");
        arg1.getWriter().write("<id_pedido>" + idPedido + newPedido.getSecuenciaPago() + "</id_pedido>");

        //Se envian dos ceros al final, ya que TBK_MONTO asume los dos ultimos
        // digitos como valores decimales
        arg1.getWriter().write("<monto>" + new Double(newPedido.getMonto_reservado()).longValue() + "00</monto>");
        arg1.getWriter().write("</datos_objeto>");
        return;
    }

    //  20120910avc
    /**
     * @param mail_send
     * @return
     */
    private int getMailCount() {
        Date currentDate;

        java.sql.Connection conn = new ConexionUtil().getConexion();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        int mail_send = 0;
        try {
            currentDate = dateFormat.parse(dateFormat.format(new Date()));
            Date last_update = null;
            Statement stm = conn.createStatement();

            ResultSet rs = stm.executeQuery("SELECT par1.PAR_VALOR as mail_send FROM FO_PARAMETROS par1 WHERE par1.PAR_LLAVE='MAIL_SEND' WITH UR");
            if (rs.next()) {
                mail_send = Integer.parseInt(rs.getString("mail_send"));
            } else {
        	    stm.executeUpdate("INSERT INTO FO_PARAMETROS(\"PAR_ID\", \"PAR_NOMBRE\", \"PAR_LLAVE\", \"PAR_VALOR\") values (1, 'MAIL_SEND', 'MAIL_SEND', '0')");
            }

            rs = stm.executeQuery("SELECT par2.PAR_VALOR as mail_last_update FROM FO_PARAMETROS par2 WHERE par2.PAR_LLAVE='MAIL_LAST_UPDATE' WITH UR");
            if (rs.next()) {
                try {
                    last_update = dateFormat.parse(rs.getString("mail_last_update"));

                } catch (ParseException e1) {
                    last_update = new Date();
                }

            } else {
                stm.executeUpdate("INSERT INTO FO_PARAMETROS(\"PAR_ID\", \"PAR_NOMBRE\", \"PAR_LLAVE\", \"PAR_VALOR\") values (2, 'MAIL_LAST_UPDATE', 'MAIL_LAST_UPDATE', '2012-01-01');");
            }

            if (currentDate.after(last_update))
                mail_send = 0;
            conn.close();
            
            if (rs != null)
                rs.close(); 
        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return mail_send;
    }

    private void addMailCount() {
        java.sql.Connection conn = new ConexionUtil().getConexion();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Statement stm;

        try {
            Date currentDate = dateFormat.parse(dateFormat.format(new Date()));
            stm = conn.createStatement();

            ResultSet rs = stm.executeQuery("SELECT par1.PAR_VALOR as mail_send FROM FO_PARAMETROS par1 WHERE par1.PAR_LLAVE='MAIL_SEND' WITH UR");

            int mail_send = 0;
            Date last_update = null;
            while (rs.next()) {
                mail_send = Integer.parseInt(rs.getString("mail_send"));
            }

            stm.executeUpdate("UPDATE FO_PARAMETROS SET PAR_VALOR='" + ++mail_send + "' WHERE PAR_LLAVE='MAIL_SEND'");
            stm.executeUpdate("UPDATE FO_PARAMETROS SET PAR_VALOR='" + dateFormat.format(currentDate) + "' WHERE PAR_LLAVE='MAIL_LAST_UPDATE'");
            conn.close();
            
            if (rs != null)
                rs.close(); 
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //20120910-avc
    /**
     * Valida parámetros mínimos necesarios
     * 
     * @param arg0
     *            Request recibido desde el navegador
     * @param arg1
     *            Response recibido desde el navegador
     * @return True: ok, False: faltan parámetros
     */
    private boolean validateParametersLocal(HttpServletRequest arg0) {
        Logging logger = this.getLogger();

        ArrayList campos = new ArrayList();
        campos.add("jpicking");
        campos.add("jprecio");
        campos.add("jdespacho");
        campos.add("pol_sustitucion");
        campos.add("sin_gente_txt");
        campos.add("tipo_documento");

        for (int i = 0; i < campos.size(); i++) {
            String campo = (String) campos.get(i);
            if (arg0.getParameter(campo) == null) {
                logger.error("Falta parámetro: " + campo + " en OrderCreate");
                return false;
            }
        }

        return true;

    }

}