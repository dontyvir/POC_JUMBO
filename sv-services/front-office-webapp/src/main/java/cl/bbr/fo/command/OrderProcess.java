package cl.bbr.fo.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.log.Logging;

/**
 * Procesa orden de compras y la tranforma en un pedido
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class OrderProcess extends Command {

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
            throws Exception {
        
//        // Recupera la sesión del usuario
//        HttpSession session = arg0.getSession();
//        String horasDespachoEconomico = "";
//        List prodSustitutosNuevos = new ArrayList();
//        List sustitutosActualesDeCliente = new ArrayList();
//        
//        // Se revisan que los parámetros mínimos existan
//        if (!validateParametersLocal(arg0) || session.getAttribute("ses_zona_id") == null || session.getAttribute("ses_forma_despacho") == null ) {
//            this.getLogger().error("Faltan parámetros mínimos: " + Cod_error.REG_FALTAN_PARA);
//            session.setAttribute("cod_error", Cod_error.REG_FALTAN_PARA );
//            getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);
//            return;
//        }
//        
//        ResourceBundle rb = ResourceBundle.getBundle("fo");
//        
//        try {
//            // Instacia del bizdelegate
//            BizDelegate biz = new BizDelegate();
//
//            long id_cliente = Long.parseLong(session.getAttribute("ses_cli_id").toString());
//            boolean empleado = false;
//            boolean masparis = false;
//            boolean maseasy = false;
//
//            // Revisa que existan productos en el carro de compras
//            List lcarro = biz.carroComprasGetProductos( id_cliente, session.getAttribute("ses_loc_id").toString() );
//            if( lcarro.size() <= 0 ) {
//                // No existen productos se retorna error
//                this.getLogger().error("No existen productos en el carro: " + Cod_error.CHECKOUT_SIN_PROD);
//                session.setAttribute("cod_error", Cod_error.CHECKOUT_SIN_PROD );
//                getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);
//                return;             
//            }
//            
//            ClienteDTO cliente = biz.clienteGetById( id_cliente );
//            
//            // Carga los datos en el objeto
//            ProcInsPedidoDTO pedido = new ProcInsPedidoDTO();
//            
//            // datos pedido
//
//            pedido.setId_cliente( id_cliente );
//            if( session.getAttribute("ses_eje_id") != null )
//                pedido.setId_usuario_fono( Long.parseLong(session.getAttribute("ses_eje_id").toString()) );
//            if( arg0.getParameter("pol_sustitucion") != null ) {
//                String []pol_aux = arg0.getParameter("pol_sustitucion").split("--");
//                pedido.setPol_id( Long.parseLong( pol_aux[0] ) );
//                pedido.setPol_sustitucion( pol_aux[1] );
//            }
//            pedido.setSin_gente_op( Long.parseLong(arg0.getParameter("sin_gente_op")) );
//            if ( session.getAttribute("ses_forma_despacho").toString().equalsIgnoreCase("R") ) {
//                pedido.setSin_gente_txt( arg0.getParameter("retira_txt") );
//                if ( !arg0.getParameter("sin_gente_rut").toString().equalsIgnoreCase("") ) {
//                    pedido.setSin_gente_rut( Long.parseLong( arg0.getParameter("sin_gente_rut") ) );                    
//                } else {
//                    pedido.setSin_gente_rut( 0 );
//                }
//                pedido.setSin_gente_dv( arg0.getParameter("sin_gente_dv") );
//            } else {
//                pedido.setSin_gente_txt( arg0.getParameter("sin_gente_txt") );
//                pedido.setSin_gente_rut( 0 );
//                pedido.setSin_gente_dv( "" );    
//            }
//           
//            pedido.setTipo_doc(arg0.getParameter("tipo_documento"));
//            
//            // medio de pago
//            
//            pedido.setNum_mp( arg0.getParameter("num_tja").toString() );
//            pedido.setN_cuotas( Long.parseLong(arg0.getParameter("num_cuotas")) );
//            pedido.setObservacion( "" );
//            
//            if( Long.parseLong(arg0.getParameter("forma_pago")) == 1 ) { // Tarjeta jumbo más
//                pedido.setMedio_pago("CAT");
//                pedido.setNom_tbancaria("MAS JUMBO");
//            } else if( Long.parseLong(arg0.getParameter("forma_pago")) == 3 ) { // Tarjeta Easy más
//                pedido.setMedio_pago("CAT");
//                pedido.setNom_tbancaria("MAS EASY");
//                maseasy = true;
//            } else if( Long.parseLong(arg0.getParameter("forma_pago")) == 5 ) { // Medio de pago alternativo
//                    pedido.setMedio_pago("CAT");
//                    this.getLogger().debug( arg0.getParameter("ped_obs") );
//                    if( arg0.getParameter("ped_obs") != null )
//                        pedido.setObservacion( arg0.getParameter("ped_obs") );
//            } else if( Long.parseLong(arg0.getParameter("forma_pago")) == 4 ) { // Tarjeta Bancaria
//                pedido.setMedio_pago("TBK");
//                pedido.setNom_tbancaria(arg0.getParameter("nom_tban"));
//                pedido.setFecha_exp( arg0.getParameter("t_mes") + "" + arg0.getParameter("t_ano") );
//                pedido.setTb_banco( arg0.getParameter("t_banco").toString() );
//            } else if ( Long.parseLong(arg0.getParameter("forma_pago")) == 2 ) { // Tarjeta Paris
//                
//                //Se recupera si el cliente es Empleado Paris (1:Empleado, 0:No Empleado)
//                if (session.getAttribute("ses_empleado_paris") != null){
//                    if (((String)session.getAttribute("ses_empleado_paris")).equals("SI"))
//                        empleado = true;
//                } 
//                
//                if (empleado) {
//                    pedido.setNum_mp((String)session.getAttribute("ses_cta_empleado"));
//                    pedido.setMedio_pago("PAR");
//                    pedido.setClave_mp(arg0.getParameter("pe_clave"));
//                    //pedido.setMeses_librpago(0);
//                    pedido.setNom_tbancaria("PARIS EMPLEADO");
//                } else if (arg0.getParameter("tipo_paris").equals("PARIS")){ //Si es tarjeta paris normal
//                    pedido.setMedio_pago("PAR");
//                    //pedido.setMeses_librpago(Integer.parseInt(arg0.getParameter("p_meslibrepago")));
//                    
//                    //si el cliente es adicional
//                    if (Integer.parseInt(arg0.getParameter("titular")) == 0) { //
//                        pedido.setRut_tit(arg0.getParameter("rut_titular"));
//                        pedido.setDv_tit(arg0.getParameter("dv_titular"));
//                        pedido.setNom_tit(arg0.getParameter("nom_titular"));
//                        pedido.setApat_tit(arg0.getParameter("pat_titular"));
//                        pedido.setAmat_tit(arg0.getParameter("mat_titular"));
//                        pedido.setDir_tit(arg0.getParameter("dir_titular"));
//                        pedido.setDir_num_tit(arg0.getParameter("num_titular"));
//                        pedido.setNom_tbancaria("PARIS ADICIONAL");
//                    } else {
//                        pedido.setNom_tbancaria("PARIS TITULAR");
//                    }
//                } else if (arg0.getParameter("tipo_paris").equals("MASPARIS")) { //si es tarjeta mas paris
//                    pedido.setMedio_pago("CAT");
//                    pedido.setNom_tbancaria("MAS PARIS");
//                    masparis = true;
//                }
//                
//            }   
//            
//            // Datos despacho
//            
//            pedido.setId_jdespacho( Long.parseLong(arg0.getParameter("jdespacho")) );
//            pedido.setTipo_despacho( arg0.getParameter("tipo_despacho") );//R: RETIRO EN LOCAL
//            pedido.setId_zona( Long.parseLong( session.getAttribute("ses_zona_id").toString() ) );
//            pedido.setId_local_desp( Long.parseLong( session.getAttribute("ses_loc_id").toString() ) ); //Usado para el local de retiro
//            pedido.setFecha_despacho( arg0.getParameter("jfecha") );
//            horasDespachoEconomico = arg0.getParameter("horas_economico");
//            this.getLogger().debug( "HORA DE DESPACHO ECONOMICO: " + horasDespachoEconomico );
//            
//            this.getLogger().debug( "TIPO DESPACHO: " + pedido.getTipo_despacho() );
//            this.getLogger().debug( "ZONA DESPACHO: " + pedido.getId_zona() );
//            this.getLogger().debug( "FECHA DESPACHO: " + pedido.getFecha_despacho() );
//                        
//            // datos direccion despacho         
//            pedido.setDir_id( Long.parseLong(session.getAttribute("ses_dir_id").toString()) );
//            
//            // datos de facturación sólo si es factura
//            if( pedido.getTipo_doc().compareTo("F") == 0 ) {
//                pedido.setFac_rut( Long.parseLong(arg0.getParameter("fac_rut").replaceAll("\\.","").replaceAll("\\-","")) );                
//                pedido.setFac_dv(arg0.getParameter("fac_dv") );
//                pedido.setFac_razon(arg0.getParameter("fac_razon") );
//                pedido.setFac_direccion(arg0.getParameter("fac_direccion") );
//                pedido.setFac_fono(arg0.getParameter("fac_cod_1")+"-"+arg0.getParameter("fac_fono"));
//                pedido.setFac_giro(arg0.getParameter("fac_giro") );
//                pedido.setFac_ciudad(arg0.getParameter("fac_ciudad") );
//                pedido.setFac_comuna(arg0.getParameter("fac_comuna") );
//            }
//            
//            double cantidadProductos = 0.0;
//            double montoTotal = 0.0;  
//
//            // Detalle del pedido
//            List list_det = new ArrayList();        
//            // Recuperar los datos de los productos
//            for (int i = 0; i < lcarro.size(); i++) {
//                CarroCompraDTO prods = (CarroCompraDTO)lcarro.get(i);
//                ProcInsPedidoDetalleFODTO detalle = new ProcInsPedidoDetalleFODTO(); 
//                detalle.setId_producto_fo( Long.parseLong(prods.getPro_id()) );
//                detalle.setCant_solic( prods.getCantidad() );
//                detalle.setCon_nota( "" );
//                if (prods.getNota() == null) {
//                    detalle.setObservacion("");
//                } else {
//                    if (prods.getNota().equalsIgnoreCase("null")) {
//                        detalle.setObservacion("");
//                    } else {
//                        detalle.setObservacion( prods.getNota() );
//                    }
//                }
//                detalle.setPrecio_unitario( prods.getPrecio() );
//                detalle.setTipoSel(prods.getTipoSel());
//                detalle.setPrecio_lista( prods.getPrecio() );
//                list_det.add(detalle);
//                
//                cantidadProductos += prods.getCantidad();
//                montoTotal += ( prods.getCantidad() * prods.getPrecio() );
//            }
//
//            pedido.setCantidadProductos(cantidadProductos);
//            pedido.setMontoTotal(montoTotal);
//            pedido.setProductos( list_det );
//
//            // Listado de cupones   
//            List lst_cupones = new ArrayList();
//            
//            List l_cupones = null;
//            if( session.getAttribute("ses_cupones") != null )
//                l_cupones = (List)session.getAttribute("ses_cupones");
//            
//            if( l_cupones != null ) {
//                for( int f = 0; l_cupones != null && f < l_cupones.size(); f++ ) {
//                    FOTcpDTO cupon = (FOTcpDTO)l_cupones.get(f);
//                    CuponPedidoDTO cupped = new CuponPedidoDTO();
//                    cupped.setNro_tcp(cupon.getTcp_nro());
//                    cupped.setNro_cupon(cupon.getCupon());
//                    cupped.setCant_max(cupon.getTcp_max());
//                    lst_cupones.add(cupped);
//                }
//            }
//            pedido.setLst_cupones( lst_cupones );
//
//            // Listado de TCP
//            List lst_tcp = new ArrayList();
//            
//            List l_tcp = null;
//            if( session.getAttribute("ses_promo_tcp") != null )
//                l_tcp = (List)session.getAttribute("ses_promo_tcp");
//            
//            if( l_tcp != null ) {
//                for( int i = 0; l_tcp != null && i < l_tcp.size(); i++ ) {
//                    FOTcpDTO tcp = (FOTcpDTO)l_tcp.get(i);
//                    TcpPedidoDTO tcpped = new TcpPedidoDTO();
//                    tcpped.setNro_tcp( tcp.getTcp_nro() );
//                    tcpped.setCant_max( tcp.getTcp_max() );
//                    lst_tcp.add(tcpped);
//                }
//            }
//            pedido.setLst_tcp( lst_tcp );
//            
//            if ( arg0.getParameter("dispositivo") != null ) {
//                pedido.setDispositivo( arg0.getParameter("dispositivo") ); //Si es I es compra via IPHONE
//            } else {
//                pedido.setDispositivo( "B" );
//            }
//            
//            // Inserta pedido
//            long id_pedido = 0;
//            try {
//                // --- INI - LAYER EVENTOS PERSONALIZADOS - RESUMEN ---
//                EventoDTO evento = new EventoDTO();
//                String codLayerFlash = UtilsEventos.codHtmlDeFlash(Long.parseLong(session.getAttribute("ses_cli_rut").toString()), EventosConstants.PASO_RESUMEN);
//                if ( codLayerFlash.length() > 0 ) {
//                    evento = UtilsEventos.eventoMostrado( Long.parseLong(session.getAttribute("ses_cli_rut").toString()) );
//                }
//                session.setAttribute("codLayerFlash", codLayerFlash );
//                session.setAttribute("eventoLayerFlash", evento );
//                // --- FIN - LAYER EVENTOS PERSONALIZADOS - RESUMEN ---
//                /*
//                boolean esClienteConfiable = false;
//                if ( session.getAttribute("ses_cli_confiable") != null ) {
//                    if (session.getAttribute("ses_cli_confiable").toString().equalsIgnoreCase("S")) {
//                        esClienteConfiable = true;
//                    }
//                }                
//                id_pedido = biz.doInsPedido(pedido, esClienteConfiable);
//                */
//                id_pedido = biz.doInsPedido(pedido);
//                
//                biz.setCompraHistorica("", id_cliente, (long)cantidadProductos, id_pedido);
//            } catch (Exception ex ) {
//                ex.printStackTrace();
//                throw new CommandException( ex.getCause().getLocalizedMessage(), ex );
//            }
//            
//            this.getLogger().debug("OK Pedido insertado ");
//            
//            this.getLogger().info("Pedido insertado:" + id_pedido + " Browser:" + arg0.getHeader("User-Agent"));
//            
//            // Actualiza ranking de ventas de los productos
//            try {
//                biz.updateRankingVentas( id_cliente );
//            } catch (Exception e) {
//                e.printStackTrace();
//                this.getLogger().error( "Problemas actualizar ranking de ventas", e);
//            }
//            
//            // --- INI - Ingresamos los sustitutos            
//            try {
//                sustitutosActualesDeCliente = biz.productosSustitutosByCliente(id_cliente);
//                for ( int i = 0; i < lcarro.size(); i++ ) {
//                    boolean existe = false;
//                    CarroCompraDTO carro = (CarroCompraDTO) lcarro.get(i);
//                    for ( int j = 0; j < sustitutosActualesDeCliente.size(); j++ ) {
//                        ProductoDTO prod = (ProductoDTO) sustitutosActualesDeCliente.get(j);
//                        if ( Long.parseLong(carro.getPro_id()) == prod.getPro_id() ) {
//                            existe = true;
//                        }
//                        if ( existe ) {
//                            break;
//                        }
//                    }
//                    if ( !existe ) {
//                        prodSustitutosNuevos.add( carro );
//                    }
//                }
//                if (prodSustitutosNuevos.size() > 0) {
//                    //Tiene productos nuevos para asignar criterios, por default dejamos CRITERIO JUMBO
//                    biz.addSustitutosCliente(id_cliente, prodSustitutosNuevos);
//                }                
//                
//            } catch (Exception e) {
//                e.printStackTrace();
//                this.getLogger().error( "Problemas al insertar sustitutos: " + e.getMessage(), e);
//            }
//            // --- FIN - Ingresamos los sustitutos
//            
//            // Envia mail
//            try {
//                //Se lee el archivo para el mail
//                // Recupera pagina desde web.xml
//                String mail_tpl = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("arc_mail");
//                // Carga el template html
//                TemplateLoader mail_load = new TemplateLoader(mail_tpl);
//                ITemplate mail_tem = mail_load.getTemplate();
//                IValueSet mail_top = new ValueSet();
//                
//                Calendar cal = new GregorianCalendar();
//                long minutos = (cal.getTimeInMillis()-session.getCreationTime())/1000/60;
//                String  str_minuto = "";
//                if (minutos < 2){
//                    str_minuto = "minuto";
//                }else{
//                    str_minuto = "minutos";
//                }               
//                if (minutos <= Integer.parseInt(rb.getString("pedido.minutos"))){
//                    mail_top.setVariable("{msg_time_compra}", rb.getString("orderprint.mensaje1") + minutos +" "+str_minuto+ rb.getString("orderprint.mensaje2") );
//                }else{
//                    mail_top.setVariable("{msg_time_compra}", "");
//                }               
//                
//                mail_top.setVariable("{nombre_cliente}", cliente.getNombre() + " " + cliente.getApellido_pat() );
//                
//                PedidoDTO pedido1 = biz.getPedidoById(id_pedido);
//
//                String num_tarjeta = pedido1.getNum_mp();
//                //SOLO PARA EL CASO DEL BLOQUE SEL_CUOTA
//                IValueSet lista_boton = new ValueSet();
//                lista_boton.setVariable("{contador_form}", ""); 
//                List list_boton = new ArrayList();
//                list_boton.add(lista_boton);                
//                
//                mail_top.setVariable("{idped}", Utils.numOP(Long.parseLong(rb.getString("op.funcion.transformacion.a")),Long.parseLong(rb.getString("op.funcion.transformacion.b")),id_pedido)+"" );
//                mail_top.setVariable("{cantidad}", Formatos.formatoCantidad(pedido1.getCant_prods())+"" );
//                mail_top.setVariable("{monto}", Formatos.formatoPrecio(pedido1.getMonto()+pedido1.getCosto_despacho()) +"" );
//                mail_top.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle()+" "+pedido1.getDir_calle()+" "+pedido1.getDir_numero()+", "+pedido1.getDir_depto()+", "+pedido1.getNom_comuna());
//                
//                if( pedido1.getMedio_pago().compareTo("CAT") == 0 && num_tarjeta.compareTo("************1111") != 0) {
//                    if (masparis)
//                        mail_top.setVariable("{forma_pago}", "Paris Más" );
//                    else if (maseasy)
//                        mail_top.setVariable("{forma_pago}", "Easy Más" );
//                    else
//                        mail_top.setVariable("{forma_pago}", "Jumbo Más" ); 
//                } else if( pedido1.getMedio_pago().compareTo("CAT") == 0 && num_tarjeta.compareTo("************1111") == 0)
//                    mail_top.setVariable("{forma_pago}", "Cheque" );
//                else if( pedido1.getMedio_pago().compareTo("TBK") == 0 )
//                    mail_top.setVariable("{forma_pago}", "Tarjeta Bancaria" );
//                else if( pedido1.getMedio_pago().compareTo("PAR") == 0 )
//                    mail_top.setVariable("{forma_pago}", "Tarjeta Paris" );
//                
//                
//                String fecha = cl.bbr.jumbocl.common.utils.Formatos.frmFecha(pedido1.getFdespacho());
//                
//                // Cuando es despacho Economico se debe mostrar la Hora de Inicio
//                // y Fin de la Jornada completa del día. Ej.: 9:00 - 23:00
//                // E: Express, N: Normal, C: Económico
//                
//                boolean flag = false;
//                String DespHorarioEconomico = rb.getString("DespliegueHorarioEconomico");
//                //'C': Completa (09:00 - 23:00), 'P': Parcial (14:00 - 19:00)
//                if (DespHorarioEconomico.equalsIgnoreCase("C") && 
//                      pedido.getTipo_despacho().equalsIgnoreCase("C") && 
//                        !horasDespachoEconomico.equalsIgnoreCase("")){
//                    mail_top.setVariable("{fecha_tramo}", fecha + "&nbsp;&nbsp;" + horasDespachoEconomico );
//                    flag = true;
//                }
//                if (!flag){
//                    int posIni = pedido1.getHdespacho().indexOf(":", 3);
//                    int posFin = pedido1.getHfindespacho().indexOf(":", 3);
//                    mail_top.setVariable("{fecha_tramo}", fecha + "&nbsp;&nbsp;" + pedido1.getHdespacho().substring(0, posIni) + " - " + pedido1.getHfindespacho().substring(0, posFin) );
//                }
//
//                if(pedido1.getN_cuotas() == 0){
//                    lista_boton.setVariable("{num_cuotas}", "Sin cuotas");
//                }else{
//                    lista_boton.setVariable("{num_cuotas}", pedido1.getN_cuotas() + "");
//                }
//                
//                if( !(pedido1.getMedio_pago().compareTo("CAT") == 0 && num_tarjeta.compareTo("************1111") == 0)){
//                    mail_top.setDynamicValueSets("SEL_CUOTA", list_boton);//No agrega el bloque
//                }
//                
//                List textoDespacho = new ArrayList();
//                IValueSet filaDespacho = new ValueSet();
//                if (pedido1.getTipo_despacho().equalsIgnoreCase("R")) {
//                    mail_top.setVariable("{txt_monto}","Monto");
//                    mail_top.setVariable("{fc_hr_despacho}","Fecha y hora de retiro");
//                    filaDespacho.setVariable("{lugar_despacho}", pedido1.getIndicacion());
//                    LocalDTO loc = biz.getLocalRetiro(pedido1.getId_local());
//                    filaDespacho.setVariable("{dir_retiro_pedido}", "("+loc.getDireccion()+")");
//                    filaDespacho.setVariable("{id_local}", loc.getId_local()+"");
//                    textoDespacho.add(filaDespacho);
//                    mail_top.setDynamicValueSets("LUGAR_RETIRO", textoDespacho);
//                    
//                    textoDespacho = new ArrayList();
//                    filaDespacho = new ValueSet();                    
//                    filaDespacho.setVariable("{sin_gente_txt}", pedido1.getSin_gente_txt()+"");
//                    filaDespacho.setVariable("{sin_gente_rut}", cl.bbr.jumbocl.common.utils.Formatos.formatoRut( pedido1.getSin_gente_rut() ) +"-"+pedido1.getSin_gente_dv());
//                    textoDespacho.add(filaDespacho);
//                    mail_top.setDynamicValueSets("SIN_GENTE_RETIRO", textoDespacho);
//                } else {
//                    mail_top.setVariable("{txt_monto}","Monto (Despacho Incluido)");
//                    mail_top.setVariable("{fc_hr_despacho}","Fecha y hora del despacho");
//                    if ( pedido1.getDir_depto().length() > 0 ) {
//                        filaDespacho.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle()+" "+pedido1.getDir_calle()+" "+pedido1.getDir_numero()+", "+pedido1.getDir_depto()+", "+pedido1.getNom_comuna());
//                    } else {
//                        filaDespacho.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle()+" "+pedido1.getDir_calle()+" "+pedido1.getDir_numero()+", "+pedido1.getNom_comuna());
//                    }
//                    textoDespacho.add(filaDespacho);
//                    mail_top.setDynamicValueSets("LUGAR_DESPACHO", textoDespacho);
//                    
//                    textoDespacho = new ArrayList();
//                    filaDespacho = new ValueSet();                    
//                    filaDespacho.setVariable("{sin_gente_txt}", pedido1.getSin_gente_txt()+"");
//                    textoDespacho.add(filaDespacho);
//                    mail_top.setDynamicValueSets("SIN_GENTE_DESPACHO", textoDespacho);
//                }
//                
//                if (pedido1.getTipo_doc().compareTo("B") == 0){
//                    mail_top.setVariable("{tip_doc}","Boleta");
//                }else if(pedido1.getTipo_doc().compareTo("F") == 0){
//                    mail_top.setVariable("{tip_doc}","Factura");
//                }
//
//                //Categorías y Productos
//                List fm_cate = new ArrayList();
//                int contador=0;
//                long totalizador = 0;
//                
//                List productosPorCategoria = biz.getProductosSolicitadosById( id_pedido );
//                
//                //total_producto_pedido = 0;
//                double precio_total = 0;
//                for( int i = 0; i < productosPorCategoria.size(); i++ ) {                   
//                    CarroCompraCategoriasDTO cat = (CarroCompraCategoriasDTO) productosPorCategoria.get(i);
//                    
//                    IValueSet fila_cat = new ValueSet();
//                    fila_cat.setVariable("{categoria}", cat.getCategoria());
//                    
//                    List prod = cat.getCarroCompraProductosDTO();
//
//                    List fm_prod = new ArrayList();             
//                    for (int j = 0; j < prod.size(); j++) {                     
//                        CarroCompraProductosDTO producto = (CarroCompraProductosDTO) prod.get(j);
//                        
//                    //  total_producto_pedido += Math.ceil(producto.getCantidad());
//                        
//                        IValueSet fila_pro = new ValueSet();
//                        fila_pro.setVariable("{descripcion}", producto.getNombre());
//                        fila_pro.setVariable("{marca}", producto.getMarca());
//                        fila_pro.setVariable("{cod_sap}", producto.getCodigo());
//                        fila_pro.setVariable("{valor}", Formatos.formatoIntervalo(producto.getCantidad())+"");
//                        fila_pro.setVariable("{carr_id}", producto.getCar_id()+"");
//                        fila_pro.setVariable("{contador}", contador+"");
//                                            
//                        precio_total = 0;
//                        // Existe STOCK para el producto
//                        if (producto.getStock() != 0) {
//
//                            // Si el producto es con seleccion
//                            if (producto.getUnidad_tipo().charAt(0) == 'S') {
//
//                                IValueSet fila_lista_sel = new ValueSet();
//                                List aux_lista = new ArrayList();
//                                for (double v = 0; v <= producto.getInter_maximo(); v += producto.getInter_valor()) {
//                                    IValueSet aux_fila = new ValueSet();
//                                    aux_fila.setVariable("{valor}", Formatos.formatoIntervalo(v) + "");
//                                    aux_fila.setVariable("{opcion}", Formatos.formatoIntervalo(v) + "");
//                                    if( Formatos.formatoIntervalo(v).compareTo(Formatos.formatoIntervalo(producto.getCantidad())) == 0 )
//                                        aux_fila.setVariable("{selected}", "selected");
//                                    else
//                                        aux_fila.setVariable("{selected}", "");
//                                    aux_lista.add(aux_fila);
//                                }
//                                fila_lista_sel.setVariable("{contador}", contador+"");
//                                fila_lista_sel.setDynamicValueSets("CANTIDADES", aux_lista);
//                                
//                                List aux_blanco = new ArrayList();
//                                aux_blanco.add(fila_lista_sel);
//                                fila_pro.setDynamicValueSets("LISTA_SEL", aux_blanco);
//                            } else {
//                                IValueSet fila_lista_sel = new ValueSet();
//                                fila_lista_sel.setVariable("{contador}", contador+"");
//                                fila_lista_sel.setVariable("{valor}", producto.getCantidad() + "");
//                                fila_lista_sel.setVariable("{maximo}", producto.getInter_maximo() + "");
//                                fila_lista_sel.setVariable("{intervalo}", producto.getInter_valor() + "");
//                                List aux_blanco = new ArrayList();
//                                aux_blanco.add(fila_lista_sel);
//                                fila_pro.setDynamicValueSets("INPUT_SEL",aux_blanco);
//                            }
//                            
//                            if( producto.isCon_nota() == true ) {
//                                IValueSet set_nota = new ValueSet();
//                                set_nota.setVariable("{nota}", producto.getNota()+"");
//                                set_nota.setVariable("{contador}", contador+"");
//                                List aux_blanco = new ArrayList();
//                                aux_blanco.add(set_nota);
//                                fila_pro.setDynamicValueSets("NOTA", aux_blanco);                   
//                            }                       
//                            
//                            precio_total = Utils.redondear( producto.getPpum() * producto.getCantidad() );
//                            fila_pro.setVariable("{unidad}", producto.getTipre());
//                            fila_pro.setVariable("{precio_unitario}", Formatos.formatoPrecio(producto.getPpum()) );
//                            fila_pro.setVariable("{precio_total}", Formatos.formatoPrecio(precio_total) );              
//                            fila_pro.setVariable("{CLASE_TABLA}", "TablaDiponiblePaso1");
//                            fila_pro.setVariable("{CLASE_CELDA}", "celda1");
//                            fila_pro.setVariable("{NO_DISPONIBLE}", "");
//                            fila_pro.setVariable("{OPCION_COMPRA}", "1");
//                            totalizador += precio_total;
//                            
//                            contador++;
//                            fm_prod.add(fila_pro);
//                        }                       
//                    }
//                    fila_cat.setDynamicValueSets("PRODUCTOS", fm_prod);
//                    fm_cate.add(fila_cat);                  
//                }
//                mail_top.setDynamicValueSets("CATEGORIAS", fm_cate);
//
//                //String sector = "";
//                mail_top.setVariable("{total}", Formatos.formatoPrecio(totalizador)+"");
//                // Monto del descuento
//                double montoTotalDescuento = montoTotal - totalizador;
//                if ( montoTotalDescuento < 0 )
//                    montoTotalDescuento = 0;
//                mail_top.setVariable("{total_descuento}", Formatos.formatoPrecio(montoTotalDescuento)+"");
//                
//                String mail_result = mail_tem.toString(mail_top);
//                // Se envía mail al cliente
//                MailDTO mail = new MailDTO();
//                mail.setFsm_subject( rb.getString("mail.checkout.subject") );
//                mail.setFsm_data( mail_result );
//                mail.setFsm_destina( cliente.getEmail() );
//                mail.setFsm_remite( rb.getString( "mail.checkout.remite" ) );
//                biz.addMail( mail );
//            } catch (Exception e) {
//                e.printStackTrace();
//                this.getLogger().error( "Problemas con mail", e);
//            }
//            
//            // Elimina carro de compras (vacia)
//            biz.deleteCarroCompraAll( id_cliente );
//            
//            String dis_ok =  getServletConfig().getInitParameter("dis_ok");
//            dis_ok += "?psn=" + prodSustitutosNuevos.size() + "&psa=" + sustitutosActualesDeCliente.size();            
//            if (!horasDespachoEconomico.equalsIgnoreCase("")) {
//                dis_ok += "&horas_desp_eco="+horasDespachoEconomico;
//            }           
//            arg1.sendRedirect( dis_ok );
//            
//        } catch (Exception e) {
//            this.getLogger().error(e);
//            e.printStackTrace();            
//            if (Constantes._EX_VE_GP_EXCEDE_CAPAC.equalsIgnoreCase(e.getLocalizedMessage())) {
//                session.setAttribute("cod_error", Cod_error.CHECKOUT_SIN_CAPACIDAD );
//            } else {
//                session.setAttribute("cod_error", Cod_error.CHECKOUT_ERROR );
//            }
//            getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);           
//            
//        }
    }

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
        campos.add("total_compra");
        campos.add("num_tja");
        campos.add("num_cuotas");
        campos.add("jpicking");
        campos.add("jprecio");
        campos.add("jdespacho");
        campos.add("pol_sustitucion");
        campos.add("sin_gente_op");
        campos.add("tipo_documento");

        for (int i = 0; i < campos.size(); i++) {
            String campo = (String) campos.get(i);
            if (arg0.getParameter(campo) == null) {
                logger.error("Falta parámetro: " + campo + " en OrderProcess");
                return false;
            }
        }

        return true;

    }

}