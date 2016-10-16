package cl.cencosud.jumbo.ctrl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;

import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.promo.lib.dto.ProductoPromosDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantDeliveryWindow;
import cl.cencosud.jumbo.bizdelegate.BizDelegateDeliveryWindow;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.DeliveryWindow.GetInputDeliveryWindowDTO;
import cl.cencosud.jumbo.output.dto.DeliveryWindow.GetOutputDeliveryWindowDTO;

public class CtrlDeliveryWindow extends Ctrl {
	GetInputDeliveryWindowDTO deliveryWindowDTO;
	BizDelegateDeliveryWindow biz = new BizDelegateDeliveryWindow();
	ArrayList dayOfWeekDelivery = new ArrayList();
	
	/**
	* Constructor para asignar el objeto del tipo GetInputDeliveryWindowDTO.
	*/	
	public CtrlDeliveryWindow (GetInputDeliveryWindowDTO deliveryWindowDTO){
		super();
		this.deliveryWindowDTO = deliveryWindowDTO;
	}
	
	/**
	 * Valida datos necesarios para generar ventana de despacho y genera un objeto de salida del tipo GetOutputDeliveryWindowDTO. 
	 * <p>
	 * Este metodo toma los datos tipo de servicio, id de local, id de direccion y cliente, valor de carro,
	 * setea el objeto de salida outputDTO con el calendario de despacho y el status correspondiente 
	 * a lo procesado en el metodo.
	 * Retorna un objeto del tipo GetOutputDeliveryWindowDTO. 
	 *
	 * @return  GetOutputDeliveryWindowDTO, objeto de salida que implementa el metodo toJson.
	 * @see     GetOutputDeliveryWindowDTO
	 * @throws	GrabilityException
	 * @throws ParseException 
	 */
	public GetOutputDeliveryWindowDTO getDeliveryWindow() throws GrabilityException, ParseException{
		GetOutputDeliveryWindowDTO outputDTO = new GetOutputDeliveryWindowDTO(); 
		String delivery_type = deliveryWindowDTO.getShipping_type();
		int local_id = deliveryWindowDTO.getLocal_id();
		int address_id = deliveryWindowDTO.getAddress_id();
		long cli_id = deliveryWindowDTO.getClient_id();
		long total = deliveryWindowDTO.getCart_value_cat();
		String type = deliveryWindowDTO.getType();
		boolean esRetiroLocal = false;
        boolean isColaborador = false;
        boolean esCliConfiable = false;
        Date fechaActualSistema = biz.fechaActualBD();
        long cantProds=0;
        if (cli_id==0){
        	cantProds = biz.carroComprasGetCantidadProductos(cli_id,null);
        }else{
        	cantProds = ConstantDeliveryWindow.CANTIDAD_PRODUCTOS_DUMMY;
        }
		long idZona=0; 
		List listaLocalesRetiro = biz.localesRetiro();
		if (ConstantDeliveryWindow.TIPO_SERVICIO_RETIRO.equalsIgnoreCase(delivery_type)){
			for (int i = 0; i < listaLocalesRetiro.size(); i++) {
				LocalDTO local = (LocalDTO) listaLocalesRetiro.get(i);
				if (local_id==local.getId_local()){
					idZona = local.getIdZonaRetiro();
					break;
				}
			}
		}else {
			if (address_id!=0){
				DireccionesDTO direccion = biz.clienteGetDireccion(address_id);
				idZona= direccion.getZona_id();
			} else {
				throw new GrabilityException(ConstantDeliveryWindow.SC_ID_DIRECCION_INVALIDA, ConstantDeliveryWindow.MSG_ID_DIRECCION_INVALIDA);
			}
		}
		if (idZona==0){
			throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_LOCAL_SIN_RETIRO,ConstantDeliveryWindow.MSG_ERROR_LOCAL_SIN_RETIRO);
		}
		ZonaDTO zona = biz.getZonaDespachoById(idZona);
		List listaCarro = biz.carroComprasPorCategorias(cli_id, ""+local_id, null);
		ClientesDTO cliente = biz.getClienteById(cli_id);
        MiCarroDTO car;
        boolean descuento = false;
        //Total del Carro
        int diasCalendario= ConstantDeliveryWindow.DIAS_CALENDARIO;
        int diasPresentacionCalendario = ConstantDeliveryWindow.DIAS_PRESENTACION;
        int hhWebCliConfiables = ConstantDeliveryWindow.WEB_CLIENTE_CONFIABLES;
        
		if (cliente!=null){
			doRecalculoCriterio recalculoDTO = new doRecalculoCriterio();
			recalculoDTO.setCuotas( ConstantDeliveryWindow.CUOTAS_TARJETA );
			recalculoDTO.setF_pago(ConstantDeliveryWindow.FORMA_PAGO_PROMO);
			recalculoDTO.setId_local(local_id);
			recalculoDTO.setGrupos_tcp(new ArrayList()); //l_torec
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
	        if(( (zona.getEstado_descuento_tbk() & 1) == 1 && total >= zona.getMonto_descuento_tbk() ) || 
	            ( (zona.getEstado_descuento_tbk() & 2) == 2 && total >= zona.getMonto_descuento_pc_tbk() && biz.esPrimeraCompra(cli_id)) ||
	            ( cliente.isColaborador() )){
	            descuento = true;
	        }
	        isColaborador=cliente.isColaborador();
	        esCliConfiable = biz.clienteEsConfiable(cliente.getRut());
        }
        
        esRetiroLocal = biz.zonaEsRetiroLocal(idZona);

        boolean actualizaCapacidad = false;
        int pasoActual = -1;        
        boolean isTarifaUmbral=false;
        boolean isTarifaUmbralRetiro=false;
        long totalCencosud = total;
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
        
			
			generaCalendarioDias(cantProds, actualizaCapacidad, zona, diasCalendario, pasoActual, cantProds, esRetiroLocal, diasPresentacionCalendario, esCliConfiable, hhWebCliConfiables, descuento, fechaActualSistema, isTarifaUmbral, isTarifaUmbralRetiro, isColaborador, valorTarifaUmbralRetiro);
        	outputDTO.setDelivery_type(delivery_type);
        	outputDTO.setDay_of_week_for_delivery(dayOfWeekDelivery);
        	outputDTO.setStatus(Constant.SC_OK);
        	outputDTO.setError_message(Constant.MSG_OK);
        
		return outputDTO;
    }
	/**
	 * Muestra ventanas de despacho y devuelve un objeto ArrayList
	 * <p>
	 * Este metodo toma los datos cantidad de productos, funciones del carro, zona, dias del calendario, 
	 * si es retiro o despacho y datos necesarios del cliente
	 * setea el objeto de salida ArrayList con el calendario de despacho 
	 *
	 * @return  ArrayList, objeto de salida que implementa el metodo toJson.
	 * @see     ArrayList
	 * @throws	GrabilityException,ParseException 
	 */
	private ArrayList generaCalendarioDias(long cantProd, boolean actualizaCapacidad, ZonaDTO zona, int diasCalendario, int pasoActual, long cantProds, boolean esRetiroLocal,
        int diasPresentacionCalendario, boolean esCliConfiable, long tiempoLimiteVip, boolean descuento, Date fechaActualSistema, boolean isTarifaUmbral, boolean isTarifaUmbralRetiro, boolean isColaborador, long valorTarifaUmbralRetiro) throws GrabilityException,ParseException {
	    Calendar fechaActual = new GregorianCalendar();
	    fechaActual.setFirstDayOfWeek(Calendar.MONDAY);
	    int fcActual    = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(fechaActualSistema));
	    long tiempoLimiteCompraExpress    = Constantes.HORAS_COMPRA_EXPRESS * Constantes.HORA_EN_MILI_SEG;
	    long horaInicioValidacionExpress  = Constantes.HORAS_INICIO_VALIDACION_EXPRESS * Constantes.HORA_EN_MILI_SEG;
	    DateFormat formatter        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    long tiempoActual           = fechaActualSistema.getTime();
	    long diasPresentacion       = fechaActual.getTimeInMillis() + diasPresentacionCalendario * 24 * 60 * 60 * 1000;
	    int enabled =0;
	    String startTime = "";
	    String endTime= "";
	    String journeyPrice = "";
	    long deliveryJourneyId=0;
	    long pickingJourneyId=0;
	    String deliveryType="";
	    int diaSemana=0;
	    List horarios = biz.getCalendarioDespachoByDias(diasCalendario, zona.getId_zona());        	
	    for ( int i = 0; i < horarios.size(); i++ ) {
	    	ArrayList deliveryRanges = new ArrayList();
	    	LinkedHashMap dayOfWeek = new LinkedHashMap();
	        HorarioDespachoEntity ventana = (HorarioDespachoEntity) horarios.get(i);
	        Calendar fecha = new GregorianCalendar();
	        fecha.setFirstDayOfWeek(Calendar.MONDAY);
	        startTime = new SimpleDateFormat(Formatos.HOUR_CAL).format(ventana.getH_ini());
		    endTime = new SimpleDateFormat(Formatos.HOUR_CAL).format(ventana.getH_fin());
	        for ( int k = 0; k < ventana.getJornadas().size(); k++ ) {
	           	LinkedHashMap rangosdespacho = new LinkedHashMap();
	            JornadaDespachoEntity jor = (JornadaDespachoEntity) ventana.getJornadas().get(k);
	            diaSemana= jor.getFecha().getDay()-1;//new SimpleDateFormat(Formatos.DATE_CAL).format(jor.getFecha());
	                pickingJourneyId = jor.getId_jpicking();
		            deliveryJourneyId = jor.getId_jdespacho();
					int fcPicking   = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(jor.getFecha_picking()));
	                int fcDespacho  = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(jor.getFecha()));
                    long horaPick   = jor.getHoraIniPicking().getHours() * Constantes.HORA_EN_MILI_SEG;
                    String fechaPick            = jor.getFecha_picking() + " " + jor.getHoraIniPicking().toString();
                    Date datePicking            = formatter.parse(fechaPick);
                    long tiempoPicking          = datePicking.getTime();                        
                    long tiempoDespacho         = datePicking.getTime();
                    long tiempoLimite           = jor.getHrs_ofrecido_web() * 60 * 60 * 1000L;
                    boolean mostrarExpress = mostrarExpress(fcActual, fcDespacho, fcPicking, pasoActual, zona, horaPick, tiempoLimiteCompraExpress,horaInicioValidacionExpress, tiempoPicking, tiempoActual, cantProds, jor, esRetiroLocal);
                    boolean pasadoOSinCapacidad = esPasadoOSinCapacidad(cantProds, jor, tiempoDespacho, tiempoActual, tiempoLimite, diasPresentacion);                    
                    boolean mostrarExpressVip = false;
                    if ( esCliConfiable ) {
                        mostrarExpressVip = mostrarExpressVip(cantProds, jor, tiempoDespacho, tiempoActual, tiempoLimiteVip, zona);
                    }
                    if(isTarifaUmbral && !esRetiroLocal){                           	
						if(jor.getTarifa_umbral() != 0 && !pasadoOSinCapacidad){       
							journeyPrice = Formatos.formatoPrecioFO(jor.getTarifa_umbral());
							deliveryType = ConstantDeliveryWindow.TIPO_DESPACHO_UMBRAL;
							enabled = 1; 
						}else{
							enabled = 0;                    			
						}
                    }else if(isTarifaUmbralRetiro && esRetiroLocal){  
                    	journeyPrice = Formatos.formatoPrecioFO(valorTarifaUmbralRetiro);
						if(jor.getTarifa_umbral() != 0 && !pasadoOSinCapacidad){  
							deliveryType = ConstantDeliveryWindow.TIPO_DESPACHO_RETIRO;
							enabled = 1; 
						}else{
							enabled = 0;                  			
						}
                    }else{
                    	if ( mostrarExpress ) {	                    		
                            if (!descuento){
                            	journeyPrice = Formatos.formatoPrecioFO(jor.getTarifa_express());
                            }else{
                            	journeyPrice =	Formatos.formatoPrecioFO(ConstantDeliveryWindow.VALOR_DESPACHO_GRATUITO);
                            }
                            enabled = 1; 
							deliveryType = ConstantDeliveryWindow.TIPO_DESPACHO_EXPRESS;
                        } else {
                            if ( pasadoOSinCapacidad ) {                                
                                if ( mostrarExpressVip ) {
                                    if (!descuento){
                                    	journeyPrice = Formatos.formatoPrecioFO(jor.getTarifa_express());
                                    }else{
                                    	journeyPrice =	Formatos.formatoPrecioFO(ConstantDeliveryWindow.VALOR_DESPACHO_GRATUITO);
                                    }
                                    deliveryType = ConstantDeliveryWindow.TIPO_DESPACHO_EXPRESS;
    								enabled = 1; 
                                } else {
                                	enabled = 0;
                                }                                
                            } else {
                            	enabled = 1; 
                            	if ( esRetiroLocal ) {
                            		deliveryType = ConstantDeliveryWindow.TIPO_DESPACHO_RETIRO;
                            		if(isColaborador){
                            			journeyPrice =	Formatos.formatoPrecioFO(ConstantDeliveryWindow.VALOR_DESPACHO_GRATUITO);
                            		} else {
	                                    if (!descuento){
	                                    	journeyPrice = Formatos.formatoPrecioFO(jor.getTarifa_normal());
	                                    }else{
	                                    	journeyPrice =	Formatos.formatoPrecioFO(ConstantDeliveryWindow.VALOR_DESPACHO_GRATUITO);
	                                    }
	                                }
                                } else {
                                	deliveryType = ConstantDeliveryWindow.TIPO_DESPACHO_NORMAL;
                                	if(isColaborador){
                                		journeyPrice =	Formatos.formatoPrecioFO(ConstantDeliveryWindow.VALOR_DESPACHO_GRATUITO);
                                	} else {
                                		if (!descuento){
	                                    	journeyPrice = Formatos.formatoPrecioFO(jor.getTarifa_normal());
                                		} else {
	                                    	journeyPrice =	Formatos.formatoPrecioFO(ConstantDeliveryWindow.VALOR_DESPACHO_GRATUITO);
                                		}
                                	}
                                }
                            }
	                    }
				}
				if (enabled == 0) {
					deliveryType = "";
					journeyPrice = "-----";
				}
				rangosdespacho.clear();
				rangosdespacho.put(ConstantDeliveryWindow.ENABLED, "" + enabled);
				rangosdespacho.put(ConstantDeliveryWindow.JOURNEY_PRICE, journeyPrice);
				rangosdespacho.put(ConstantDeliveryWindow.DELIVERY_JOURNEY_ID, "" + deliveryJourneyId);
				rangosdespacho.put(ConstantDeliveryWindow.PICKING_JOURNEY_ID, "" + pickingJourneyId);
				rangosdespacho.put(ConstantDeliveryWindow.DELIVERY_TYPE, deliveryType);
				rangosdespacho.put(ConstantDeliveryWindow.DAY_OF_WEEK, ""+ diaSemana);
				deliveryRanges.add(rangosdespacho);

				enabled = 0;
				journeyPrice = null;
				deliveryJourneyId = 0;
				pickingJourneyId = 0;
				deliveryType = null;
				diaSemana =0;
			}
			dayOfWeek.put(ConstantDeliveryWindow.START_TIME, startTime);
			dayOfWeek.put(ConstantDeliveryWindow.END_TIME, endTime);
			dayOfWeek.put(ConstantDeliveryWindow.DELIVERY_RANGES, deliveryRanges);
			dayOfWeekDelivery.add(dayOfWeek);

		}
		return dayOfWeekDelivery;
	}

	private boolean mostrarExpressVip(long cantProds,
			JornadaDespachoEntity jor, long tiempoDespacho, long tiempoActual,
			long tiempoLimiteVip, ZonaDTO zonadto) {
		if ((cantProds < (jor.getCapac_picking() - jor
				.getCapac_picking_ocupada()))
				&& (zonadto.getEstado_tarifa_express() == 1)
				&& (jor.getTarifa_express() > 0)
				&& ((jor.getCapac_despacho() - jor.getCapac_despacho_ocupada()) > 0)
				&& ((tiempoDespacho - tiempoActual) > tiempoLimiteVip)) {
			return true;
		}
		return false;
	}

	private boolean esPasadoOSinCapacidad(long cantProds,
			JornadaDespachoEntity jor, long tiempoDespacho, long tiempoActual,
			long tiempoLimite, long diasPresentacion) {
		if ((cantProds > (jor.getCapac_picking() - jor
				.getCapac_picking_ocupada()))
				|| ((jor.getCapac_despacho() - jor.getCapac_despacho_ocupada()) <= 0)
				|| ((tiempoDespacho - tiempoActual) < tiempoLimite)
				|| (jor.getFecha().getTime() > diasPresentacion)) {
			return true;
		}
		return false;
	}

	private boolean mostrarExpress(int fcActual, int fcDespacho, int fcPicking,
			int pasoActual, ZonaDTO zonadto, long horaPick,
			long tiempoLimiteCompraExpress, long horaInicioValidacionExpress,
			long tiempoPicking, long tiempoActual, long cantProds,
			JornadaDespachoEntity jor, boolean esRetiroLocal) {
		if (!esRetiroLocal
				&& (fcActual == fcDespacho)
				&& (fcActual == fcPicking)
				&& (pasoActual == 3)
				&& (jor.getTarifa_express() > 0)
				&& (zonadto.getEstado_tarifa_express() == 1)
				&& ((horaPick - tiempoLimiteCompraExpress) > horaInicioValidacionExpress)
				&& ((tiempoPicking - tiempoActual) > tiempoLimiteCompraExpress)
				&& (cantProds < (jor.getCapac_picking() - jor
						.getCapac_picking_ocupada()))
				&& ((jor.getCapac_despacho() - jor.getCapac_despacho_ocupada()) > 0)) {
			return true;
		}
		return false;
	}

}
