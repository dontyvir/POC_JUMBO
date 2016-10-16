package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.exception.SystemException;
//import cl.bbr.fo.parametros.dto.ParametroDTO;
//import cl.bbr.fo.promociones.dto.ProductoPromosDTO;
//import cl.bbr.fo.promociones.dto.PromocionDTO;
//import cl.bbr.fo.promociones.dto.doRecalculoCriterio;
//import cl.bbr.fo.promociones.dto.doRecalculoResultado;
//import cl.bbr.fo.util.Formatos;
//import cl.bbr.fo.util.Utils;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.CalendarioDespachoEconomico;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.utils.Cifrador;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.promo.lib.dto.ProductoPromosDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;

/**
 * Genera la pagina del calendario de despacho
 *  El calendario despliega las ventanas horarias para despacho normal, economico, express y retiro en local
 *  
 */
public class AjaxDespachoChart extends Command {  

    /**
	 * 
	 */
	private static final long serialVersionUID = -7952064486395032792L;

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
            throws Exception {

        try {            
            arg0.setCharacterEncoding("iso-8859-1");
            arg1.setContentType("text/html; charset=iso-8859-1");
            
            long total = 0;
            boolean descuento = false;
            
            // Carga properties
            ResourceBundle rb = ResourceBundle.getBundle("fo");
            long cantProds = Long.parseLong(rb.getString("despachochart.productos.promedio"));
            long hhWebCliConfiables = Long.parseLong(rb.getString("despachochart.hrscliconfiables"));
            int diasCalendario = Integer.parseInt(rb.getString("despachochart.dias.calendario"));
            int diasPresentacionCalendario = Integer.parseInt(rb.getString("despachochart.diaspresentacion"));
            boolean actualizaCapacidad = false;
            
            if(arg0.getParameter("actualiza_capacidad") != null) {
            	actualizaCapacidad = true;
            }
            

            // Recupera la sesión del usuario
            HttpSession session = arg0.getSession();
            
            session.setAttribute("descuento_despacho", "false");

            // Se recupera la salida para el servlet
            PrintWriter out = arg1.getWriter();
            
            IValueSet top = new ValueSet();
            
            
            // Recupera pagina desde web.xml
            String pagForm = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            this.getLogger().debug("[AjaxDespachoChart - Template] : " + pagForm);
            TemplateLoader load = new TemplateLoader(pagForm);
            ITemplate tem = load.getTemplate();

            BizDelegate biz = new BizDelegate();            
            Date fechaActualSistema = biz.fechaActualBD();           
            
            long cli_id = 0L;
            String idSession = null;
            
            cli_id = Long.parseLong((String)session.getAttribute("ses_cli_id"));
            if (session.getAttribute("ses_invitado_id") != null)
                idSession = session.getAttribute("ses_invitado_id").toString();
            
            
            long idZona = 0;
            if (arg0.getParameter("zona_id") != null) {
                idZona = Long.parseLong(arg0.getParameter("zona_id").toString());
            } else if (session.getAttribute("ses_zona_id") != null) {
                idZona = Long.parseLong(session.getAttribute("ses_zona_id").toString());
            }
            if (arg0.getParameter("cant_prod") != null){
                cantProds = Long.parseLong(arg0.getParameter("cant_prod"));
            }else if (arg0.getHeader("MyFOReferer") != null && "Pago".equals(arg0.getHeader("MyFOReferer"))) {             	
            	cantProds = biz.carroComprasGetCantidadProductos(Long.parseLong(session.getAttribute("ses_cli_id").toString()),idSession);
            }
                                   
          
            ZonaDTO zona = biz.getZonaDespachoById(idZona);
            List listaCarro = biz.carroComprasPorCategorias(cli_id, session.getAttribute("ses_loc_id").toString(), idSession);
            
            long precio_total = 0;
            MiCarroDTO car;
            
            //Recuperar los cupones y los tcp de la sesión
            List l_torec = new ArrayList();
            List l_tcp = null;
            if( session.getAttribute("ses_promo_tcp") != null ) {
                l_tcp = (List)session.getAttribute("ses_promo_tcp");
                l_torec.addAll(l_tcp);
            }
            
            if( session.getAttribute("ses_cupones") != null ) {
                List l_cupones = (List)session.getAttribute("ses_cupones");
                l_torec.addAll(l_cupones);
            }
            
            if (listaCarro != null && listaCarro.size() > 0) {
                listaCarro = biz.cargarPromocionesMiCarro(listaCarro, l_torec, Integer.parseInt(session.getAttribute("ses_loc_id").toString()));
            }
            
            //Total del Carro
            for (int i = 0; i < listaCarro.size(); i++) {
                car = (MiCarroDTO) listaCarro.get(i);
                //Información del producto
                if (car.tieneStock()) {
                    precio_total = Utils.redondearFO(car.getCantidad() * car.getPrecio());
                    total += precio_total;
                }
            }
            
            double descuento_promo_webpay = 0;
            String desc_promo_webpay = "";
            
            //Obtener datos de la promomocion TBK
            try {
                doRecalculoCriterio recalculoDTO = new doRecalculoCriterio();
                recalculoDTO.setCuotas( Integer.parseInt(rb.getString("promociones.tjacredito.cuotas")) );
                recalculoDTO.setF_pago( rb.getString("promociones.tjacredito.formapago") );
                recalculoDTO.setId_local( Integer.parseInt(session.getAttribute("ses_loc_id").toString()) );
                recalculoDTO.setGrupos_tcp(l_torec);

                List l_prod = new ArrayList();
                
                for (int i = 0; i < listaCarro.size(); i++) {
                    car = (MiCarroDTO) listaCarro.get(i);
                    if (car.tieneStock()) {
                        ProductoPromosDTO pro = new ProductoPromosDTO();
                        pro.setId_producto(car.getId_bo());
                        pro.setCod_barra(car.getCodbarra());
                        pro.setSeccion_sap(car.getCatsap());
                        pro.setCant_solicitada(car.getCantidad());
                        if(car.getPesable()!=null && car.getPesable().equals("S") )
                            pro.setPesable("P");
                        else
                            pro.setPesable("C");
                        pro.setPrecio_lista(car.getPrecio());
                        l_prod.add(pro);
                    }
                }
                
                recalculoDTO.setProductos( l_prod );

                if (l_prod != null && l_prod.size() > 0 ){
                    /////////////////////////////////////////////////////
                    //CALCULO
                    doRecalculoResultado resultado = biz.doRecalculoPromocion( recalculoDTO );
                    /////////////////////////////////////////////////////

                    List promocionesWEBPAY = new ArrayList();
                    List l_promo = resultado.getPromociones();
                    for( int i = 0; l_promo != null && i < l_promo.size(); i++ ) {
                        PromocionDTO promocion = (PromocionDTO)l_promo.get(i);
                        this.getLogger().debug("Promocion: " + promocion.getCod_promo() +"-"+ promocion.getDescuento1() +"-"+ promocion.getDescr());
                        desc_promo_webpay += promocion.getDescr()+"--"+Formatos.formatoPrecioFO(promocion.getDescuento1())+"#";
                        
                        IValueSet fila_promo_webpay = new ValueSet();
                        fila_promo_webpay.setVariable("{promo_descripcion}", promocion.getDescr());
                        fila_promo_webpay.setVariable("{promo_descuento}", Formatos.formatoPrecioFO(promocion.getDescuento1()));
                        promocionesWEBPAY.add(fila_promo_webpay);
                    }
                    top.setDynamicValueSets("PROMOCIONES_WEBPAY",promocionesWEBPAY);
                    descuento_promo_webpay = resultado.getDescuento_pedido();
                    if ( total < resultado.getDescuento_pedido() ) {
                        descuento_promo_webpay = total;
                    }
                }
            } catch( SystemException e ) {
                this.getLogger().error("Error en libreria de promociones, no presenta promociones: "+e.getMessage());
            }
                            
            if (session.getAttribute("ses_invitado_id") != null && !session.getAttribute("ses_invitado_id").toString().equals("0")) {
            	descuento = false;
            } else if(( (zona.getEstado_descuento_tbk() & 1) == 1 && total >= zona.getMonto_descuento_tbk() ) || 
                        ( (zona.getEstado_descuento_tbk() & 2) == 2 && total >= zona.getMonto_descuento_pc_tbk() && biz.esPrimeraCompra(cli_id)) ||
                        ( session.getAttribute("ses_colaborador").equals("true") )){
                        descuento = true;
                        session.setAttribute("descuento_despacho", "true");
            }

            boolean esRetiroLocal = biz.zonaEsRetiroLocal(idZona);
            
            boolean esCliConfiable = false;
            
            if ( session.getAttribute("ses_cli_confiable") != null ) {
                if (session.getAttribute("ses_cli_confiable").toString().equalsIgnoreCase("S")) {
                    esCliConfiable = true;
                }
            }
            int pasoActual = -1;        
            if ( arg0.getParameter("paso") != null ) {
                pasoActual = Integer.parseInt(arg0.getParameter("paso"));
            }
            
            boolean isColaborador=false;
            if( session.getAttribute("ses_colaborador").equals("true")){
            	isColaborador=true;
            }
            
            boolean isTarifaUmbral=false;
            boolean isTarifaUmbralRetiro=false;
            long totalCencosud = Double.valueOf((String.valueOf(session.getAttribute("##_total_descuento_promo_tmas")))).longValue();
            
            ParametroFoDTO oParametroDTO = biz.getParametroByKey("TARIFA_COMPRA_UMBRAL");
            ParametroFoDTO oParametroDTORetiro = biz.getParametroByKey("TARIFA_COMPRA_UMBRAL_RETIRO");
            ParametroFoDTO oParametroDTOValor = biz.getParametroByKey("VALOR_TARIFA_COMPRA_UMBRAL_RETIRO");
            
            long valorTarifaUmbralRetiro = Long.parseLong(oParametroDTOValor.getValor());
            
            if(isColaborador){
            	if(totalCencosud < Long.parseLong(oParametroDTO.getValor()))
            		isTarifaUmbral=true;
            	if(totalCencosud < Long.parseLong(oParametroDTORetiro.getValor()))
            		isTarifaUmbralRetiro=true;
            }else{
            	if(total < Long.parseLong(oParametroDTO.getValor()))
            		isTarifaUmbral=true;
            	if(total < Long.parseLong(oParametroDTORetiro.getValor()))
            		isTarifaUmbralRetiro=true;
            }
            
            
            
            //Traemos la info para el calendario sin solapas y que llena X dias
            generaCalendarioDias(session, cantProds, actualizaCapacidad, zona, diasCalendario, pasoActual, cantProds, esRetiroLocal, diasPresentacionCalendario, esCliConfiable, hhWebCliConfiables, descuento, fechaActualSistema, top, isTarifaUmbral, isTarifaUmbralRetiro, isColaborador, valorTarifaUmbralRetiro);
            
            top.setVariable("{cant_prod}",	cantProds + "");
            top.setVariable("{zona_id}", 	idZona + "");
            if ( zona.getMensaje_cal_despacho() != null && zona.getMensaje_cal_despacho().length() > 0 ) {
                top.setVariable("{msj_zona}", 	zona.getMensaje_cal_despacho() + "<br />");
            } else {
                top.setVariable("{msj_zona}", 	"");
            }

            if (arg0.getParameter("sel") != null && arg0.getParameter("sel").compareTo("1") == 0)
                top.setVariable("{sel}", "1");
            else
                top.setVariable("{sel}", "0");

            String result = tem.toString(top);
            out.print(result);
            
        } catch (Exception e) {
            this.getLogger().error(e);
            throw new CommandException(e);
        }
    }
    
    private List generaCalendarioDias(HttpSession session, long cantProd, boolean actualizaCapacidad, ZonaDTO zona, int diasCalendario, int pasoActual, long cantProds, boolean esRetiroLocal,
            int diasPresentacionCalendario, boolean esCliConfiable, long tiempoLimiteVip, boolean descuento, Date fechaActualSistema, IValueSet top,boolean isTarifaUmbral, boolean isTarifaUmbralRetiro, boolean isColaborador, long valorTarifaUmbralRetiro) throws Exception,SystemException, ParseException {
        
        List datos = new ArrayList();
        List economicos = iniciarEconomico(diasCalendario);
        BizDelegate biz = new BizDelegate();
        int ancho = 510/diasCalendario;
//        ------
        String horIniEconomico = "";
        String horFinEconomico = "";
        Calendar fechaActual = new GregorianCalendar();
        fechaActual.setFirstDayOfWeek(Calendar.MONDAY);
        int fcActual    = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(fechaActualSistema));
        long tiempoLimiteCompraExpress    = Constantes.HORAS_COMPRA_EXPRESS * Constantes.HORA_EN_MILI_SEG;
        long horaInicioValidacionExpress  = Constantes.HORAS_INICIO_VALIDACION_EXPRESS * Constantes.HORA_EN_MILI_SEG;
        DateFormat formatter        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long tiempoActual           = fechaActualSistema.getTime();
        long diasPresentacion       = fechaActual.getTimeInMillis() + diasPresentacionCalendario * 24 * 60 * 60 * 1000;
//        ------
        List cabecera = getCabecera(diasCalendario, fechaActual, ancho);
        top.setDynamicValueSets("CABECERA", cabecera);
        
        //Maxbell - Incosistencias v3
        List horarios = null;
        if(actualizaCapacidad) {
        	if(biz.getParametroByKey("ACTUALIZAR_CAPACIDAD").getValor().equals("TRUE")){        		
            	long id_jpicking = 0;
            	try {
            		id_jpicking = Integer.parseInt(session.getAttribute("jpicking").toString());
            	} catch(Exception ex) {}
            	if(id_jpicking > 0) {
            		biz.actualizarCapacidadOcupadaPicking(id_jpicking);
            		horarios = biz.getCalendarioDespachoByDias(diasCalendario, zona.getId_zona(), id_jpicking, (int)cantProd);
            	}
            } else {
            	horarios = biz.getCalendarioDespachoByDias(diasCalendario, zona.getId_zona()); 
            }
        } else{
        	horarios = biz.getCalendarioDespachoByDias(diasCalendario, zona.getId_zona());        	
        }

        ResourceBundle rb = ResourceBundle.getBundle("fo");
    	String keyFo = rb.getString("conf.fo.key");
    	String paramIdCifrado ="";
    	List listDespachoSession =new ArrayList();
    	
        // Llenamos las ventanas horarias
        List listHorarios = new ArrayList();
        for ( int i = 0; i < horarios.size(); i++ ) {
            HorarioDespachoEntity ventana = (HorarioDespachoEntity) horarios.get(i);
            IValueSet regHorario = new ValueSet();
            regHorario.setVariable("{ventana}", new SimpleDateFormat(Formatos.HOUR_CAL).format(ventana.getH_ini()) + " - " + new SimpleDateFormat(Formatos.HOUR_CAL).format(ventana.getH_fin()) );
            if ( i == 0 ) {
                horIniEconomico = new SimpleDateFormat(Formatos.HOUR_CAL).format(ventana.getH_ini());
            }
            horFinEconomico = new SimpleDateFormat(Formatos.HOUR_CAL).format(ventana.getH_fin());
            List listDias = new ArrayList();
            
            Calendar fecha = new GregorianCalendar();
            fecha.setFirstDayOfWeek(Calendar.MONDAY);
            for ( int j = 0; j < diasCalendario; j++ ) {            
                boolean tieneVentana = false;
                IValueSet dia = new ValueSet();
                JornadaDespachoEntity jorEco = new JornadaDespachoEntity();
                
                for ( int k = 0; k < ventana.getJornadas().size(); k++ ) {
                    JornadaDespachoEntity jor = (JornadaDespachoEntity) ventana.getJornadas().get(k);
                    String strFecha = new SimpleDateFormat(Formatos.DATE_CAL).format(fecha.getTime());
                    String strFechaEncriptado = "";
                    if ( new SimpleDateFormat(Formatos.DATE_CAL).format(jor.getFecha()).equals( strFecha ) ) {
                        tieneVentana = true;                        
//                      -----------
                        int fcPicking   = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(jor.getFecha_picking()));
                        int fcDespacho  = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(jor.getFecha()));
                        long horaPick   = jor.getHoraIniPicking().getHours() * Constantes.HORA_EN_MILI_SEG;
                        String fechaPick            = jor.getFecha_picking() + " " + jor.getHoraIniPicking().toString();
                        Date datePicking            = formatter.parse(fechaPick);
                        long tiempoPicking          = datePicking.getTime();                        
                        long tiempoDespacho         = datePicking.getTime();
                        long tiempoLimite           = jor.getHrs_ofrecido_web() * 60 * 60 * 1000L;
                        String horaLimiteExpress    = getHoraLimiteExpres(jor);
                        
                        boolean mostrarExpress = mostrarExpress(fcActual, fcDespacho, fcPicking, pasoActual, zona, horaPick,
                                tiempoLimiteCompraExpress,horaInicioValidacionExpress, tiempoPicking, tiempoActual,
                                cantProds, jor, esRetiroLocal);
                        boolean pasadoOSinCapacidad = esPasadoOSinCapacidad(cantProds, jor, tiempoDespacho, tiempoActual,
                                tiempoLimite, diasPresentacion);                    
                        boolean mostrarExpressVip = false;
                        if ( esCliConfiable ) {
                            mostrarExpressVip = mostrarExpressVip(cantProds, jor, tiempoDespacho, tiempoActual, tiempoLimiteVip, zona);
                        }
                        
                        if(isTarifaUmbral && !esRetiroLocal){                           	
                        	
							if(jor.getTarifa_umbral() != 0 && !pasadoOSinCapacidad){       
								
								/*NOV 2015*/
								String precioDespacho = Formatos.formatoPrecioFO(jor.getTarifa_umbral());
								String precioDespachoEncriptado= Cifrador.encriptar(keyFo, precioDespacho);
								strFechaEncriptado = Cifrador.encriptar(keyFo, strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin());
								paramIdCifrado = Cifrador.encriptar(keyFo, jor.getTarifa_umbral()+","+strFecha+","+ventana.getH_ini()+","+ventana.getH_fin()+",U");
								/*NOV 2015*/
								
								//String precioDespacho = Formatos.formatoPrecio(jor.getTarifa_umbral());																
								dia.setVariable("{view}", precioDespacho);
								//dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+precioDespacho+"', '"+jor.getId_jpicking()+"', '"+strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin()+"', 'U' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'U')");
								dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+precioDespachoEncriptado+"', '"+jor.getId_jpicking()+"', '"+strFechaEncriptado+"', 'U', '"+paramIdCifrado+"' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'U')");
								
								String[] obj ={ jor.getId_jdespacho()+"", jor.getId_jpicking()+"", jor.getTarifa_umbral()+"", strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin(), "U" };
								listDespachoSession.add(obj);
								
								dia.setVariable("{class}", "calendarioactivo");								
								dia.setVariable("{onmouseover}", "cambiarEstiloCal(this, 'calendarioOnMouseOver');");
	                            dia.setVariable("{onmouseout}", "cambiarEstiloCal(this, 'calendarioactivo');hideTip();");
							
							}else{
								 tieneVentana = false;                     			
							}
                        	
                        }
                        else if(isTarifaUmbralRetiro && esRetiroLocal){  
                        	
                        	//String precioDespacho = Formatos.formatoPrecio(valorTarifaUmbralRetiro);                        	
                        	String precioDespacho = Formatos.formatoPrecioFO(valorTarifaUmbralRetiro);
                        	String precioDespachoEncriptado= Cifrador.encriptar(keyFo, precioDespacho);                        	
                        	
							if(jor.getTarifa_umbral() != 0 && !pasadoOSinCapacidad){                        	
							
								/*NOV 2015*/
								paramIdCifrado = Cifrador.encriptar(keyFo, valorTarifaUmbralRetiro+","+strFecha+","+ventana.getH_ini()+","+ventana.getH_fin()+",U");
								strFechaEncriptado = Cifrador.encriptar(keyFo, strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin());
								/*NOV 2015*/								
								
								dia.setVariable("{view}", precioDespacho);
								//dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+precioDespacho+"', '"+jor.getId_jpicking()+"', '"+strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin()+"', 'U' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'U')");
								dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+precioDespachoEncriptado+"', '"+jor.getId_jpicking()+"', '"+strFechaEncriptado+"', 'U', '"+paramIdCifrado+"' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'U')");								
								dia.setVariable("{class}", "calendarioactivo");								
								dia.setVariable("{onmouseover}", "cambiarEstiloCal(this, 'calendarioOnMouseOver');");
	                            dia.setVariable("{onmouseout}", "cambiarEstiloCal(this, 'calendarioactivo');hideTip();");
	                            
	                            String[] obj ={ jor.getId_jdespacho()+"", jor.getId_jpicking()+"", valorTarifaUmbralRetiro+"", strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin(), "U" };
								listDespachoSession.add(obj);
							
							}else{
								 tieneVentana = false;                     			
							}
                        	
                        }                        
                        else{	                       
	                        
	                        if ( mostrarExpress ) {
	                     
	                            if (!descuento)
	                                dia.setVariable("{view}", "<img src=\"/FO_IMGS/img/estructura/calendario/expressCarro.gif\" width=\"21\" height=\"13\" /> " + Formatos.formatoPrecioFO(jor.getTarifa_express()));
	                            else
	                                dia.setVariable("{view}", "<img src=\"/FO_IMGS/img/estructura/calendario/expressCarro.gif\" width=\"21\" height=\"13\" /> " + "Gratis");
	                            
	                            
	                            /*NOV 2015*/
	                            String tarifaExpressCiFrado= Cifrador.encriptar(keyFo, Formatos.formatoPrecio(jor.getTarifa_express()));
	                            paramIdCifrado = Cifrador.encriptar(keyFo, jor.getTarifa_express()+","+strFecha+","+ventana.getH_ini()+","+ventana.getH_fin()+",E");
	                            strFechaEncriptado = Cifrador.encriptar(keyFo, strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin());
	                            /*NOV 2015*/
	                            
	                            dia.setVariable("{class}", "calendarioactivo");
	                            //dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+Formatos.formatoPrecioFO(jor.getTarifa_express())+"', '"+jor.getId_jpicking()+"', '"+strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin()+"', 'E' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'E')");
	                            dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+tarifaExpressCiFrado+"', '"+jor.getId_jpicking()+"', '"+strFechaEncriptado+"', 'E',  '"+paramIdCifrado+"'); seleccionarElementoCal(this, 'calendarioSeleccion', 'E')");
	                           
	                            dia.setVariable("{onmouseover}", "cambiarEstiloCal(this, 'calendarioOnMouseOver');");
                                dia.setVariable("{onmouseout}", "cambiarEstiloCal(this, 'calendarioactivo');hideTip();");
                                
                                String[] obj ={ jor.getId_jdespacho()+"", jor.getId_jpicking()+"", jor.getTarifa_express()+"", strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin(), "E" };
								listDespachoSession.add(obj);
	                            
	                        } else {
	                            if ( pasadoOSinCapacidad ) {                                
	                                if ( mostrarExpressVip ) {
	                                    dia.setVariable("{class}", "calendarioExpress");
	                                    if (!descuento)
	                                        dia.setVariable("{view}", "<img src=\"/FO_IMGS/img/estructura/calendario/expressCarro.gif\" width=\"21\" height=\"13\" /> " + Formatos.formatoPrecioFO(jor.getTarifa_express()));
	                                    else
	                                        dia.setVariable("{view}", "<img src=\"/FO_IMGS/img/estructura/calendario/expressCarro.gif\" width=\"21\" height=\"13\" /> " + "Gratis");
	                                    
	                                    
	                                    /*NOV 2015*/
	                                    String tarifaExpressCiFrado= Cifrador.encriptar(keyFo, Formatos.formatoPrecio(jor.getTarifa_express()));
	                                    paramIdCifrado = Cifrador.encriptar(keyFo, jor.getTarifa_express()+","+strFecha+","+ventana.getH_ini()+","+ventana.getH_fin()+",E");
	                                    strFechaEncriptado = Cifrador.encriptar(keyFo, strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin());
	                                    /*NOV 2015*/
	                                    
	                                    //dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+Formatos.formatoPrecio(jor.getTarifa_express())+"', '"+jor.getId_jpicking()+"', '"+strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin()+"', 'E' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'E')");
	                                    dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+tarifaExpressCiFrado+"', '"+jor.getId_jpicking()+"', '"+strFechaEncriptado+"', 'E', '"+paramIdCifrado+"' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'E')");
	                                    dia.setVariable("{onmouseover}", "cambiarEstiloCal(this, 'calendarioOnMouseOver');doTooltip(event, 'EXP', '" + horaLimiteExpress + "', '', 'S' );");
	                                    dia.setVariable("{onmouseout}", "cambiarEstiloCal(this, 'calendarioExpress');hideTip();");
	                                    
	                                    String[] obj ={ jor.getId_jdespacho()+"", jor.getId_jpicking()+"", jor.getTarifa_express()+"", strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin(), "E" };
	                                    listDespachoSession.add(obj);
	                                    
	                                } else {
	                                    tieneVentana = false;
	                                }                                
	                            } else {
	                            	if ( esRetiroLocal ) {
	                            		if(isColaborador){
	                            			 String valorUnoEncriptado = Cifrador.encriptar(keyFo, "1");
	                            			 paramIdCifrado = Cifrador.encriptar(keyFo, "1,"+strFecha+","+ventana.getH_ini()+","+ventana.getH_fin()+",R");
	                            			 strFechaEncriptado = Cifrador.encriptar(keyFo, strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin());
	                            			 dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+valorUnoEncriptado+"', '"+jor.getId_jpicking()+"', '"+strFechaEncriptado+"', 'R', '"+paramIdCifrado+"' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'R')");
	                            			 //dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','1', '"+jor.getId_jpicking()+"', '"+strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin()+"', 'R' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'R')");
			                                 dia.setVariable("{view}", "$1");
			                                 
			                                 String[] obj ={ jor.getId_jdespacho()+"", jor.getId_jpicking()+"", "1", strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin(), "R" };
			                                 listDespachoSession.add(obj);
	                            		}
	                            		else{	                            			
	                            			String tarifaNormalCiFrado= Cifrador.encriptar(keyFo, Formatos.formatoPrecio(jor.getTarifa_normal()));
	                            			paramIdCifrado = Cifrador.encriptar(keyFo, jor.getTarifa_normal()+","+strFecha+","+ventana.getH_ini()+","+ventana.getH_fin()+",R");
	                            			strFechaEncriptado = Cifrador.encriptar(keyFo, strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin());		                                    
	                            			dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+tarifaNormalCiFrado+"', '"+jor.getId_jpicking()+"', '"+strFechaEncriptado+"', 'R', '"+paramIdCifrado+"' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'R')");
		                                    //dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+Formatos.formatoPrecioFO(jor.getTarifa_normal())+"', '"+jor.getId_jpicking()+"', '"+strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin()+"', 'R' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'R')");
	                            			
	                            			String[] obj ={ jor.getId_jdespacho()+"", jor.getId_jpicking()+"", jor.getTarifa_normal()+"", strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin(), "R" };
			                                listDespachoSession.add(obj);

		                                    if (!descuento)
		                                        dia.setVariable("{view}", Formatos.formatoPrecioFO(jor.getTarifa_normal()));
		                                    else
		                                        dia.setVariable("{view}", "$1");
	                            		}
	                                    
	                                } else {
	                                	if(isColaborador){
	                                		String valorUnoEncriptado = Cifrador.encriptar(keyFo, "1");
	                                		paramIdCifrado = Cifrador.encriptar(keyFo, "1,"+strFecha+","+ventana.getH_ini()+","+ventana.getH_fin()+",N");
	                                		strFechaEncriptado = Cifrador.encriptar(keyFo, strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin());	                                		
	                                		dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+valorUnoEncriptado+"', '"+jor.getId_jpicking()+"', '"+strFechaEncriptado+"', 'N', '"+paramIdCifrado+"' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'N')");
	                                		//dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','1', '"+jor.getId_jpicking()+"', '"+strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin()+"', 'N' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'N')");
	                                		dia.setVariable("{view}", "$1");
	                                		
	                                		String[] obj ={ jor.getId_jdespacho()+"", jor.getId_jpicking()+"", "1", strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin(), "N" };
			                                listDespachoSession.add(obj);
	                                	}else{
	                                		String tarifaNormalCiFrado= Cifrador.encriptar(keyFo, Formatos.formatoPrecio(jor.getTarifa_normal()));
	                                		
	                            			paramIdCifrado = Cifrador.encriptar(keyFo, jor.getTarifa_normal()+","+strFecha+","+ventana.getH_ini()+","+ventana.getH_fin()+",R");
	                            			strFechaEncriptado = Cifrador.encriptar(keyFo, strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin());		                                    
	                            			dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+tarifaNormalCiFrado+"', '"+jor.getId_jpicking()+"', '"+strFechaEncriptado+"', 'N', '"+paramIdCifrado+"' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'N')");
		                                    //dia.setVariable("{onclick}", "send_despacho( '"+jor.getId_jdespacho()+"','"+Formatos.formatoPrecioFO(jor.getTarifa_normal())+"', '"+jor.getId_jpicking()+"', '"+strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin()+"', 'N' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'N')");
		                                    
		                                    String[] obj ={ jor.getId_jdespacho()+"", jor.getId_jpicking()+"", jor.getTarifa_normal()+"", strFecha + " " + ventana.getH_ini() + " - " + ventana.getH_fin(), "N" };
			                                listDespachoSession.add(obj);

		                                    if (!descuento){
		                                        dia.setVariable("{view}", Formatos.formatoPrecio(jor.getTarifa_normal()));
		                                    }else {
		                                    	if(isColaborador){
		                                    		dia.setVariable("{view}", "$1");
		                                    	}else{
		                                    		if (!descuento) {
		                                    			dia.setVariable("{view}", Formatos.formatoPrecio(jor.getTarifa_normal()));
		                                    		} else {		                                    			
		                                    			dia.setVariable("{view}", "$1");		                                    			
		                                    		}
		                                    	}
		                                    }
	                                	}
	                                }
	                                dia.setVariable("{class}", "calendarioactivo");
	                                dia.setVariable("{onmouseover}", "cambiarEstiloCal(this, 'calendarioOnMouseOver');");
	                                dia.setVariable("{onmouseout}", "cambiarEstiloCal(this, 'calendarioactivo');hideTip();");
	                                
	                            }
	                        }
	                        if ( tieneVentana ) {
	                            jorEco = jor;
	                        }
                        }
//                        -----------                        
                    }
                }
                if ( !tieneVentana ) {
                    dia.setVariable("{view}", "-----");
                    dia.setVariable("{class}", "calendarioactivo");
                    dia.setVariable("{onclick}", "");
                    dia.setVariable("{onmouseover}", "");
                    dia.setVariable("{onmouseout}", "");
                } else {
                    ((CalendarioDespachoEconomico)economicos.get(j)).setJornada(jorEco);
                    ((CalendarioDespachoEconomico)economicos.get(j)).sumaVentanas();
                }
                dia.setVariable("{ancho}",""+ancho);
                listDias.add(dia);
                fecha.add(Calendar.DAY_OF_YEAR, 1);
            }
            regHorario.setDynamicValueSets("DIAS", listDias);
            listHorarios.add(regHorario);
        }
        top.setDynamicValueSets("VENTANAS", listHorarios);
        
        if ( horIniEconomico != "" && horFinEconomico != "" && zona.getEstado_tarifa_economica() == 1 ) {
            List list = new ArrayList();
            IValueSet reg = new ValueSet();
            reg.setVariable("{ventana}", horIniEconomico + " - " + horFinEconomico );
            reg.setDynamicValueSets("DIAS", getVentanasEconomicas(horIniEconomico,horFinEconomico,economicos,horarios.size(),ancho,zona.getId_zona(),cantProds,descuento));
            reg.setDynamicValueSets("CABECERA", cabecera);
            list.add(reg);
            top.setDynamicValueSets("VENTANAS_ECO", list);    
        }
        
        session.setAttribute("listDespachoSession", listDespachoSession);
        return datos;
    }
    
    /**
     * @param diasCalendario
     * @return
     */
    private List iniciarEconomico(int diasCalendario) {
        List datos = new ArrayList();
        for ( int i = 0; i < diasCalendario; i++ ) {
            //Llenamos un objeto en blanco, para que despues lo llenemos
            CalendarioDespachoEconomico cal = new CalendarioDespachoEconomico();
            cal.setVentanasDiarias(0);
            datos.add(cal);
        }
        return datos;
    }
    
    /**
     * @param diasCalendario
     * @return
     * @throws SystemException
     */
    private List getVentanasEconomicas(String horIniEconomico, String horFinEconomico, List economicos, int totalHorarios, int ancho, long idZona, long cantProductos, boolean descuento) throws SystemException {
        Calendar fechaActual = new GregorianCalendar();
        BizDelegate biz = new BizDelegate();
        fechaActual.setFirstDayOfWeek(Calendar.MONDAY);
        List listFechas = new ArrayList();
        for ( int j = 0; j < economicos.size(); j++ ) {
            IValueSet regDia = new ValueSet();
            CalendarioDespachoEconomico cal = (CalendarioDespachoEconomico) economicos.get(j) ;
            String strFecha = new SimpleDateFormat(Formatos.DATE_CAL).format(fechaActual.getTime());
            if ( ( j != 0 ) && ( cal.getVentanasDiarias() > 0 ) && ( new SimpleDateFormat(Formatos.DATE_CAL).format(cal.getJornada().getFecha()).equals( strFecha ) ) && ( totalHorarios == cal.getVentanasDiarias() )) {
                if (!descuento)
                    regDia.setVariable("{view}", Formatos.formatoPrecioFO(cal.getJornada().getTarifa_economica()));
                else
                    regDia.setVariable("{view}", "Gratis");
                regDia.setVariable("{onclick}", "send_despacho( '"+biz.getJornadaDespachoMayorCapacidad(idZona, new SimpleDateFormat(Formatos.DATE_BD).format(fechaActual.getTime()) , cantProductos)+"','"+Formatos.formatoPrecioFO(cal.getJornada().getTarifa_economica())+"', '"+horIniEconomico+" - "+horFinEconomico+"', '"+new SimpleDateFormat(Formatos.YYYYMMDD).format(fechaActual.getTime())+"', 'C' ); seleccionarElementoCal(this, 'calendarioSeleccion', 'C')");
                regDia.setVariable("{onmouseover}","cambiarEstiloCal(this, 'calendarioOnMouseOver'); doTooltip(event, 'ECO', '"+horIniEconomico+"', '"+horFinEconomico+"', 'S');");
                regDia.setVariable("{onmouseout}","cambiarEstiloCal(this, 'calendarioActivoExtendido');hideTip();");
                
            } else {
                regDia.setVariable("{view}", "-----");
                regDia.setVariable("{onclick}","");
                regDia.setVariable("{onmouseover}","");
                regDia.setVariable("{onmouseout}","");
                
            }
            regDia.setVariable("{ancho}",""+ancho);
            fechaActual.add(Calendar.DAY_OF_YEAR, 1);
            listFechas.add(regDia);            
        }
        return listFechas;
    }
    
    /**
     * @return
     */
    private List getCabecera(int diasCalendario, Calendar fechaActual, int ancho) {
        List listFechas = new ArrayList();
        for ( int j = 0; j < diasCalendario; j++ ) {
            IValueSet regDia = new ValueSet();
            regDia.setVariable("{dia}", new SimpleDateFormat(Formatos.EEEEEEEEE, new Locale("es","ES")).format(fechaActual.getTime()) );
            regDia.setVariable("{fc}", new SimpleDateFormat(Formatos.DATE_CAL).format(fechaActual.getTime() ) );
            regDia.setVariable("{fecha}", new SimpleDateFormat(Formatos.YYYYMMDD).format(fechaActual.getTime()) );
            regDia.setVariable("{ancho}", ""+ancho );
            listFechas.add(regDia);
            fechaActual.add(Calendar.DAY_OF_YEAR, 1);
        }
        return listFechas;
    }
    
    /**
     * @param jor
     * @return
     */
    private String getHoraLimiteExpres(JornadaDespachoEntity jor) {
        GregorianCalendar horaPicking = new GregorianCalendar();
        horaPicking.setFirstDayOfWeek(Calendar.MONDAY);
        horaPicking.set(Calendar.HOUR_OF_DAY, jor.getHoraIniPicking().getHours());
        horaPicking.set(Calendar.MINUTE, jor.getHoraIniPicking().getMinutes());
        horaPicking.set(Calendar.SECOND, jor.getHoraIniPicking().getSeconds());
        horaPicking.add(Calendar.HOUR_OF_DAY, -(int)Constantes.HORAS_COMPRA_EXPRESS);
        return new SimpleDateFormat(Formatos.HOUR_CAL).format(new Date(horaPicking.getTimeInMillis()));        
    }

    /** 
     * @param tiempoDespacho Fecha de picking 
     */
    private boolean mostrarExpressVip(long cantProds, JornadaDespachoEntity jor, long tiempoDespacho, long tiempoActual, long tiempoLimiteVip, ZonaDTO zonadto) {
        if ( (cantProds < (jor.getCapac_picking() - jor.getCapac_picking_ocupada()))
                && ( zonadto.getEstado_tarifa_express() == 1 )
                && ( jor.getTarifa_express() > 0 )
                && ((jor.getCapac_despacho() - jor.getCapac_despacho_ocupada()) > 0)
                && ((tiempoDespacho - tiempoActual) > tiempoLimiteVip) ) {
            return true;
        }
        return false;
    }

    private boolean esPasadoOSinCapacidad(long cantProds, JornadaDespachoEntity jor, long tiempoDespacho, long tiempoActual, long tiempoLimite, long diasPresentacion) {
        if ((cantProds > (jor.getCapac_picking() - jor.getCapac_picking_ocupada()))
                || ((jor.getCapac_despacho() - jor.getCapac_despacho_ocupada()) <= 0)
                || ((tiempoDespacho - tiempoActual) < tiempoLimite)
                || (jor.getFecha().getTime() > diasPresentacion)) {
            return true;            
        }
        return false;
    }

    private boolean mostrarExpress(int fcActual, int fcDespacho, int fcPicking, int pasoActual, ZonaDTO zonadto, long horaPick, long tiempoLimiteCompraExpress, long horaInicioValidacionExpress, long tiempoPicking, long tiempoActual, long cantProds, JornadaDespachoEntity jor, boolean esRetiroLocal) {
        if ( !esRetiroLocal 
                && ( fcActual == fcDespacho ) 
                && ( fcActual == fcPicking ) 
                && ( pasoActual == 3 )
                && ( jor.getTarifa_express() > 0 )
                && ( zonadto.getEstado_tarifa_express() == 1 )
                && (( horaPick - tiempoLimiteCompraExpress) > horaInicioValidacionExpress )
                && (( tiempoPicking - tiempoActual) > tiempoLimiteCompraExpress ) 
                && ( cantProds < (jor.getCapac_picking() - jor.getCapac_picking_ocupada()) ) 
                && ( (jor.getCapac_despacho() - jor.getCapac_despacho_ocupada()) > 0 ) ) {
            return true;            
        }
        return false;
    }
    
}