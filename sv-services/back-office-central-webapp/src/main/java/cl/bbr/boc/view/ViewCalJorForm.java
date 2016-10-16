package cl.bbr.boc.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.clientes.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega el calendario de despacho para una zona determinada
 * @author jsepulveda
 */
public class ViewCalJorForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String 	paramFecha 		= "";
		String 	param_id_pedido	= "";
		String  msg				= "";
		long 	id_pedido		= -1;
		long	id_zona			= -1;
		double 	costo_despacho = 0;
		String rc = "";
        int origen = 0; //Origen 1 = Monitor de despacho
        long idRuta = 0;        
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		
		// 2. Procesa parámetros del request
		
		// 2.0 parámetro msg
		if ( req.getParameter("msg") != null )
			msg = req.getParameter("msg");
		if (req.getParameter("rc") != null ) 
            rc = req.getParameter("rc");
        if (req.getParameter("origen") != null ) {
            try {
                origen = Integer.parseInt(req.getParameter("origen"));    
            } catch (Exception e) {}            
        }
        if ( req.getParameter("id_ruta") != null ) {
            try {
                idRuta = Long.parseLong(req.getParameter("id_ruta"));    
            } catch (Exception e) {}            
        }
		
		// 2.1 parámetro id_pedido
		if ( req.getParameter("id_pedido") == null ){
			throw new ParametroObligatorioException("id_pedido es null");
		}
        
		
		param_id_pedido = req.getParameter("id_pedido");
		id_pedido = Long.parseLong(param_id_pedido);
		logger.debug("id_pedido: " + param_id_pedido);
	
		
		// 2.2 Parámetro fecha
		paramFecha = req.getParameter("fecha");
		
		logger.debug("Parámetros procesados");

		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
	
		
		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();

		// 4.1 Obtenemos información del pedido
		PedidoDTO pedido = bizDelegate.getPedidosById(id_pedido); 
		
		//verificar si el pedido ya tiene asignado una jornada de despacho
		costo_despacho = pedido.getCosto_despacho();
		
		Date fecha = new Date();		
		if ( paramFecha != null ){
			fecha = new SimpleDateFormat("yyyy-MM-dd").parse(paramFecha);
			logger.debug("fecha formateada: " + paramFecha);				
		} else {	
			logger.debug("parámetro fecha vacío, se utilizará la fecha de hoy");	
		}
		logger.debug("paramFecha:"+paramFecha);
		
		//Formateo de fecha y calendario
		Calendar cal1 = new GregorianCalendar();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);
		logger.debug("cal1: " + cal1.toString());
		
		cal1.setTime(fecha);
		cal1.getTime();
		
		int year = cal1.get(Calendar.YEAR);
		int mes = cal1.get(Calendar.MONTH);
		int woy	 = cal1.get(Calendar.WEEK_OF_YEAR);
        if (mes == 11 && woy == 1) { // Diciembre
            year++;
        }
		logger.debug("Week of year: " + woy);
		logger.debug("mes : " + mes);
		logger.debug("Year: " + year);

		
		long cant_prods = pedido.getCant_prods();
		//String TipoDespacho = pedido.getTipo_despacho();
		
		id_zona = pedido.getId_zona();
		ZonaDTO zonadto = bizDelegate.getZonaById(id_zona);
        
		//	4.2 Listado de jornadas (horario)
		CalendarioDespachoDTO cal = bizDelegate.getCalendarioDespacho(woy,year,id_zona);
		
		//System.out.println("-->woy:" + woy);
		//System.out.println("-->year:" + year);
		//System.out.println("-->id_zona:" + id_zona);
		
		// Obtenemos los objetos
		SemanasEntity sem = new SemanasEntity();
		sem = cal.getSemana();
		
		List horarios = new ArrayList();
		horarios = cal.getHorarios();
		logger.debug("horarios.size = "+horarios.size());
		
		List jornadas = new ArrayList();
		jornadas = cal.getJornadas();
		
		if (jornadas == null)
			logger.debug("jornadas es null");		
		
		ArrayList datos = new ArrayList();
		
		//comparacion de fechas
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calAhora = new GregorianCalendar();
		calAhora.setFirstDayOfWeek(Calendar.MONDAY);
		long tiempoActual 	= calAhora.getTimeInMillis();
		
        boolean consideraPicking = false;
		if ( Utils.considerarPickingParaReprogramar( pedido.getId_estado() )) {
            consideraPicking = true;
        }
        boolean mostrarPrecios = false;
        if ( Utils.considerarPreciosParaReprogramar( pedido.getId_estado() )) {
            mostrarPrecios = true;
        }
		
		// Iteramos listado de horarios
		for (int i=0; i<horarios.size(); i++){
			IValueSet fila = new ValueSet();
			HorarioDespachoEntity hor = (HorarioDespachoEntity)horarios.get(i);
			fila.setVariable("{h_ini}"      , Formatos.frmHoraSola(hor.getH_ini().toString()));
			fila.setVariable("{h_fin}"      , Formatos.frmHoraSola(hor.getH_fin().toString()));
			fila.setVariable("{id_hor_desp}", String.valueOf(hor.getId_hor_desp()));
			if (paramFecha!=null) {
				fila.setVariable("{fecha_param}", paramFecha);
			} else {
				String strFecha;
				cal1.set(Calendar.DAY_OF_WEEK,Calendar.DATE);
				strFecha = new SimpleDateFormat("dd-MM-yyyy").format(cal1.getTime()); 
				fila.setVariable("{fecha_param}", strFecha);
			}	
 			
			// iteramos sobre las jornadas
			for (int j=0; j<jornadas.size();j++) {
				JornadaDespachoEntity jor = (JornadaDespachoEntity)jornadas.get(j);
				
				//obtener la fecha y hora
				String fecha_pick = jor.getFecha_picking()+" "+jor.getHoraIniPicking().toString();
				Date datePicking  = (Date)formatter.parse(fecha_pick);
				
                long hora_pick      = jor.getHoraIniPicking().getHours()*Constantes.HORA_EN_MILI_SEG;
				long tiempoPicking  = datePicking.getTime();
				long tiempoLimite 	= jor.getHrs_validacion()*Constantes.HORA_EN_MILI_SEG;
				long tiempoLimiteValidacionExpress = Constantes.HORAS_VALIDACION_EXPRESS*Constantes.HORA_EN_MILI_SEG;
                long horaInicioValidacionExpress   = Constantes.HORAS_INICIO_VALIDACION_EXPRESS*Constantes.HORA_EN_MILI_SEG;
				
				boolean flagTarExpress = false;
				
				int fechaActual  = 0;
				int fechaPicking = 0;
				int fechaDespacho= 0;

                String s = jor.getFecha() + " " + hor.getH_fin();
                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Calendar calFinDespacho = Calendar.getInstance();
                Date d1=df.parse(s);
                calFinDespacho.setTime(d1);
				
				// jornadas que pertenecen al horario i
				if ( hor.getId_hor_desp() == jor.getId_hor_desp() ) {
					
                    fechaActual  = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(calAhora.getTime()));
                    fechaPicking = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(jor.getFecha_picking()));
                    fechaDespacho= Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(jor.getFecha()));
                    
					//capacidad de picking, se mide por la cantidad de productos que tiene un pedido.
					//capacidad de despacho, se mide por número de pedidos, no por la cantidad de productos que tiene el pedido.
					switch(jor.getDay_of_week()){
					case 1:
						flagTarExpress = false;
						fila.setVariable("{id_jdespacho_lu}", String.valueOf( jor.getId_jdespacho() ) );
                        fila.setVariable("{fecha_lu}", "" + fechaDespacho );
                        
						if (tieneTarifaExpres(fechaActual, fechaDespacho, fechaPicking, zonadto.getEstado_tarifa_express(), hora_pick, tiempoLimiteValidacionExpress, horaInicioValidacionExpress, tiempoPicking, tiempoActual, cant_prods, jor, consideraPicking, calFinDespacho.getTimeInMillis())){
							if (mostrarPrecios) {
							    fila.setVariable("{precio_lu}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_express()))));                                
                            } else {
                                fila.setVariable("{precio_lu}", "");    
                            }
                            fila.setVariable("{precio2_lu}", String.valueOf( jor.getTarifa_express()) );
							fila.setVariable("{disabled_lu}", "");
							flagTarExpress = true;
						}
						
						//*****************************************************************************************
						if (!flagTarExpress) {
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_lu}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_normal()))));
                            } else {
                                fila.setVariable("{precio_lu}", "");
                            }
                            fila.setVariable("{precio2_lu}", String.valueOf( jor.getTarifa_normal()));
							if (pedido.getTipo_ve().equals("S")){
							    if (precioDeshabilitadoVe(jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
							        fila.setVariable("{disabled_lu}", "disabled" );
							    } else {
							        fila.setVariable("{disabled_lu}", "" );
							    }
							} else if (pedido.getTipo_ve().equals("N")) {
								if (precioDeshabilitado(cant_prods, jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
								    fila.setVariable("{disabled_lu}"	, "disabled" );
                                } else {
                                    fila.setVariable("{disabled_lu}"	, "" );
                                }
							}
						}
						//logger.debug("precio lu: " + jor.getPrecio());
						if ( pedido.getId_jdespacho() == jor.getId_jdespacho() )
							fila.setVariable("{sel_lu}"	, "checked" );
						else
							fila.setVariable("{sel_lu}"	, "" );
						break;
					case 2:
                        flagTarExpress = false;
						
						fila.setVariable("{id_jdespacho_ma}", String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{fecha_ma}", "" + fechaDespacho );
                        
                        if (tieneTarifaExpres(fechaActual, fechaDespacho, fechaPicking, zonadto.getEstado_tarifa_express(), hora_pick, tiempoLimiteValidacionExpress, horaInicioValidacionExpress, tiempoPicking, tiempoActual, cant_prods, jor, consideraPicking, calFinDespacho.getTimeInMillis())){
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_ma}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_express()))));
                            } else {
                                fila.setVariable("{precio_ma}", "");    
                            }
                            fila.setVariable("{precio2_ma}", String.valueOf( jor.getTarifa_express()));
							fila.setVariable("{disabled_ma}", "");
							flagTarExpress = true;
						}
						//*****************************************************************************************
						if (!flagTarExpress){
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_ma}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_normal()))));
                            } else {
                                fila.setVariable("{precio_ma}", "");    
                            }
                            fila.setVariable("{precio2_ma}", String.valueOf( jor.getTarifa_normal()));
							if (pedido.getTipo_ve().equals("S")){
							    if (precioDeshabilitadoVe(jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
							        fila.setVariable("{disabled_ma}", "disabled" );
							    } else {
							        fila.setVariable("{disabled_ma}", "" );
							    }
							} else if (pedido.getTipo_ve().equals("N")) {
                                if (precioDeshabilitado(cant_prods, jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
                                    fila.setVariable("{disabled_ma}"	, "disabled" );
                                } else {
                                    fila.setVariable("{disabled_ma}"	, "" );
                                }
							}
						}

						if ( pedido.getId_jdespacho() == jor.getId_jdespacho() )
							fila.setVariable("{sel_ma}"	, "checked" );
						else
							fila.setVariable("{sel_ma}"	, "" );
						break;
				
					case 3:
                        flagTarExpress = false;
						
						fila.setVariable("{id_jdespacho_mi}", String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{fecha_mi}", "" + fechaDespacho );
                        
						if (tieneTarifaExpres(fechaActual, fechaDespacho, fechaPicking, zonadto.getEstado_tarifa_express(), hora_pick, tiempoLimiteValidacionExpress, horaInicioValidacionExpress, tiempoPicking, tiempoActual, cant_prods, jor, consideraPicking, calFinDespacho.getTimeInMillis())){
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_mi}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_express()))));
                            } else {
                                fila.setVariable("{precio_mi}", "");    
                            }
                            fila.setVariable("{precio2_mi}", String.valueOf( jor.getTarifa_express()));
							fila.setVariable("{disabled_mi}", "");
							flagTarExpress = true;							
						}
						//*****************************************************************************************
						if (!flagTarExpress){
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_mi}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_normal()))) );
                            } else {
                                fila.setVariable("{precio_mi}", "");    
                            }
                            fila.setVariable("{precio2_mi}", String.valueOf( jor.getTarifa_normal()));
							if (pedido.getTipo_ve().equals("S")){
							    if (precioDeshabilitadoVe(jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
							        fila.setVariable("{disabled_mi}", "disabled" );
							    } else {
							        fila.setVariable("{disabled_mi}", "" );
							    }
							} else if (pedido.getTipo_ve().equals("N")){
                                if (precioDeshabilitado(cant_prods, jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
                                    fila.setVariable("{disabled_mi}"	, "disabled" );
                                } else {
                                    fila.setVariable("{disabled_mi}"	, "" );
                                }
							}
						}
						if ( pedido.getId_jdespacho() == jor.getId_jdespacho() )
							fila.setVariable("{sel_mi}"	, "checked" );
						else
							fila.setVariable("{sel_mi}"	, "" );
						break;
						
					case 4:
                        flagTarExpress = false;
						
						fila.setVariable("{id_jdespacho_ju}", String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{fecha_ju}", "" + fechaDespacho );
                        
						if (tieneTarifaExpres(fechaActual, fechaDespacho, fechaPicking, zonadto.getEstado_tarifa_express(), hora_pick, tiempoLimiteValidacionExpress, horaInicioValidacionExpress, tiempoPicking, tiempoActual, cant_prods, jor, consideraPicking, calFinDespacho.getTimeInMillis())){
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_ju}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_express()))));
                            } else {
                                fila.setVariable("{precio_ju}", "");
                            }
                            fila.setVariable("{precio2_ju}", String.valueOf( jor.getTarifa_express()));
							fila.setVariable("{disabled_ju}", "");
							flagTarExpress = true;							
						}
						//*****************************************************************************************
						if (!flagTarExpress){
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_ju}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_normal()))) );
                            } else {
                                fila.setVariable("{precio_ju}", "");    
                            }
                            fila.setVariable("{precio2_ju}", String.valueOf( jor.getTarifa_normal()));
							if (pedido.getTipo_ve().equals("S")){
							    if (precioDeshabilitadoVe(jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
							        fila.setVariable("{disabled_ju}", "disabled" );
							    } else {
							        fila.setVariable("{disabled_ju}", "" );
							    }
							} else if (pedido.getTipo_ve().equals("N")){
                                if (precioDeshabilitado(cant_prods, jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
                                    fila.setVariable("{disabled_ju}"	, "disabled" );
                                } else {
                                    fila.setVariable("{disabled_ju}"	, "" );
                                }
							}
						}
						if ( pedido.getId_jdespacho() == jor.getId_jdespacho() )
							fila.setVariable("{sel_ju}"	, "checked" );
						else
							fila.setVariable("{sel_ju}"	, "" );
						break;
						
					case 5:
                        flagTarExpress = false;
						
						fila.setVariable("{id_jdespacho_vi}", String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{fecha_vi}", "" + fechaDespacho );
                        
						if (tieneTarifaExpres(fechaActual, fechaDespacho, fechaPicking, zonadto.getEstado_tarifa_express(), hora_pick, tiempoLimiteValidacionExpress, horaInicioValidacionExpress, tiempoPicking, tiempoActual, cant_prods, jor, consideraPicking, calFinDespacho.getTimeInMillis())){
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_vi}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_express()))));
                            } else {
                                fila.setVariable("{precio_vi}", "");    
                            }
                            fila.setVariable("{precio2_vi}", String.valueOf( jor.getTarifa_express()));
							fila.setVariable("{disabled_vi}", "");
							flagTarExpress = true;							
						}
						//*****************************************************************************************
						if (!flagTarExpress){
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_vi}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_normal()))) );
                            } else {
                                fila.setVariable("{precio_vi}", "");
                            }
                            fila.setVariable("{precio2_vi}", String.valueOf( jor.getTarifa_normal()));
							if (pedido.getTipo_ve().equals("S")){
							    if (precioDeshabilitadoVe(jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
							        fila.setVariable("{disabled_vi}", "disabled" );
							    } else {
							        fila.setVariable("{disabled_vi}", "" );
							    }
							} else if (pedido.getTipo_ve().equals("N")){
                                if (precioDeshabilitado(cant_prods, jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
                                    fila.setVariable("{disabled_vi}"	, "disabled" );
                                } else {
                                    fila.setVariable("{disabled_vi}"	, "" );
                                }
							}
						}
						if ( pedido.getId_jdespacho() == jor.getId_jdespacho() )
							fila.setVariable("{sel_vi}"	, "checked" );
						else
							fila.setVariable("{sel_vi}"	, "" );
						break;
						
					case 6:
                        flagTarExpress = false;
						
						fila.setVariable("{id_jdespacho_sa}", String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{fecha_sa}", "" + fechaDespacho );
                        
						if (tieneTarifaExpres(fechaActual, fechaDespacho, fechaPicking, zonadto.getEstado_tarifa_express(), hora_pick, tiempoLimiteValidacionExpress, horaInicioValidacionExpress, tiempoPicking, tiempoActual, cant_prods, jor, consideraPicking, calFinDespacho.getTimeInMillis())){
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_sa}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_express()))));
                            } else {
                                fila.setVariable("{precio_sa}", "");    
                            }
                            fila.setVariable("{precio2_sa}", String.valueOf( jor.getTarifa_express()));
							fila.setVariable("{disabled_sa}", "");
							flagTarExpress = true;							
						}
						//*****************************************************************************************
						if (!flagTarExpress){
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_sa}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_normal()) )));
                            } else {
                                fila.setVariable("{precio_sa}", "");    
                            }
                            fila.setVariable("{precio2_sa}", String.valueOf( jor.getTarifa_normal()));
							if (pedido.getTipo_ve().equals("S")){
							    if (precioDeshabilitadoVe(jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
							        fila.setVariable("{disabled_sa}", "disabled" );
							    } else {
							        fila.setVariable("{disabled_sa}", "" );
							    }
							} else if (pedido.getTipo_ve().equals("N")){
                                if (precioDeshabilitado(cant_prods, jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
                                    fila.setVariable("{disabled_sa}"	, "disabled" );
                                } else {
                                    fila.setVariable("{disabled_sa}"	, "" );
                                }
							}
						}
						if ( pedido.getId_jdespacho() == jor.getId_jdespacho() )
							fila.setVariable("{sel_sa}"	, "checked" );
						else
							fila.setVariable("{sel_sa}"	, "" );
						break;
						
					case 7:
                        flagTarExpress = false;
						
						fila.setVariable("{id_jdespacho_do}", String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{fecha_do}", "" + fechaDespacho );
                        
						if (tieneTarifaExpres(fechaActual, fechaDespacho, fechaPicking, zonadto.getEstado_tarifa_express(), hora_pick, tiempoLimiteValidacionExpress, horaInicioValidacionExpress, tiempoPicking, tiempoActual, cant_prods, jor, consideraPicking, calFinDespacho.getTimeInMillis())){
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_do}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_express()))));
                            } else {
                                fila.setVariable("{precio_do}", "");    
                            }
                            fila.setVariable("{precio2_do}", String.valueOf( jor.getTarifa_express()));
							fila.setVariable("{disabled_do}", "");
							flagTarExpress = true;							
						}
						//*****************************************************************************************
						if (!flagTarExpress){
                            if (mostrarPrecios) {
                                fila.setVariable("{precio_do}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf( jor.getTarifa_normal()) )));
                            } else {
                                fila.setVariable("{precio_do}", "");
                            }
                            fila.setVariable("{precio2_do}", String.valueOf( jor.getTarifa_normal()));
							if (pedido.getTipo_ve().equals("S")){
							    if (precioDeshabilitadoVe(jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
							        fila.setVariable("{disabled_do}", "disabled" );
							    } else {
							        fila.setVariable("{disabled_do}", "" );
							    }
							} else if (pedido.getTipo_ve().equals("N")){
                                if (precioDeshabilitado(cant_prods, jor, tiempoPicking, tiempoActual, tiempoLimite, consideraPicking, calFinDespacho.getTimeInMillis())) {
                                    fila.setVariable("{disabled_do}"	, "disabled" );
                                } else {
                                    fila.setVariable("{disabled_do}"	, "" );
                                }
							}
						}
						if ( pedido.getId_jdespacho() == jor.getId_jdespacho() )
							fila.setVariable("{sel_do}"	, "checked" );
						else
							fila.setVariable("{sel_do}"	, "" );
						break;
                        
					}//end switch
				}//end if
			}//end for
			datos.add(fila);
		}//end for
			

		// 5. Setea variables del template
		if(paramFecha!=null){
			top.setVariable("{fecha_param}"	,paramFecha);
		}else{
			String strFecha;
			cal1.set(Calendar.DAY_OF_WEEK,Calendar.DATE);
			strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime()); 
			top.setVariable("{fecha_param}"	,strFecha);			
		}
		
		if (jornadas.size()<=0){
			top.setVariable("{mensaje}","No hay jornadas de despacho definidas");
		}else{
			top.setVariable("{mensaje}","");
		}
		
		top.setVariable("{f_ini}"        , Formatos.frmFecha( sem.getF_ini().toString()) );
		top.setVariable("{f_fin}"        , Formatos.frmFecha( sem.getF_fin().toString()) );
		top.setVariable("{ano}"          , String.valueOf(sem.getAno()));
		top.setVariable("{id_semana}"    , String.valueOf(sem.getId_semana()));
		top.setVariable("{n_semana}"     , String.valueOf(sem.getN_semana()));
		top.setVariable("{fecha}", "");
		top.setVariable("{capac_picking}", String.valueOf(cant_prods));
		top.setVariable("{id_pedido}"    , String.valueOf(id_pedido));
        
        //top.setVariable("{fecha_pedido}"    , Formatos.cambioFormatoFecha(pedido.getFingreso(), "yyyy-MM-dd", "yyyyMMdd"));
        top.setVariable("{fecha_max}"    , Formatos.fechaLiberacionReserva(pedido.getMedio_pago(), pedido.getFingreso(), "yyyyMMdd",1));
        
        if (pedido.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_CASOS_CTE)) {
            top.setVariable("{id_pedido_view}", "C" + pedido.getPedidoExt().getNroGuiaCaso() + " (" + pedido.getId_pedido() + ")");
        } else if (pedido.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_JV_CTE)) {
            top.setVariable("{id_pedido_view}", "JV" + pedido.getPedidoExt().getNroGuiaCaso() + " (" + pedido.getId_pedido() + ")");
        } else {
            top.setVariable("{id_pedido_view}", String.valueOf(pedido.getId_pedido()));
        }
        
        
        top.setVariable("{id_ruta}"    , String.valueOf(idRuta));
        top.setVariable("{origen}"    , String.valueOf(origen));
        top.setVariable("{medio_pago}"    , pedido.getMedio_pago());
        top.setVariable("{medio_pago_lc}"    , Constantes.MEDIO_PAGO_LINEA_CREDITO);
		top.setVariable("{zona}"         , String.valueOf(id_zona));
		top.setVariable("{msg}", msg);
		
		// Setea variables del header de la tabla (fechas)
		String StrFecha1;
		
		Calendar fecha1 = new GregorianCalendar();
		fecha1.setFirstDayOfWeek(Calendar.MONDAY);
		
		fecha1.setTime(sem.getF_ini());
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		StrFecha1 = new SimpleDateFormat("dd/MM/yyyy").format(fecha1.getTime());
		top.setVariable("{f_lu}", StrFecha1);

		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
		StrFecha1 = new SimpleDateFormat("dd/MM/yyyy").format(fecha1.getTime());
		top.setVariable("{f_ma}", StrFecha1);
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
		StrFecha1 = new SimpleDateFormat("dd/MM/yyyy").format(fecha1.getTime());
		top.setVariable("{f_mi}", StrFecha1);
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
		StrFecha1 = new SimpleDateFormat("dd/MM/yyyy").format(fecha1.getTime());
		top.setVariable("{f_ju}", StrFecha1);
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
		StrFecha1 = new SimpleDateFormat("dd/MM/yyyy").format(fecha1.getTime());
		top.setVariable("{f_vi}", StrFecha1);

		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		StrFecha1 = new SimpleDateFormat("dd/MM/yyyy").format(fecha1.getTime());
		top.setVariable("{f_sa}", StrFecha1);
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		StrFecha1 = new SimpleDateFormat("dd/MM/yyyy").format(fecha1.getTime());
		top.setVariable("{f_do}", StrFecha1);
		top.setVariable("{id_zona}", String.valueOf(id_zona));
		top.setVariable("{nom_zona}", zonadto.getNombre());
		
		top.setVariable( "{mns}"	, "");
		if ( rc.equals(Constantes._EX_JDESP_FALTAN_DATOS) ){
			top.setVariable( "{mns}", "<script language='JavaScript'>alert('El código de la jornada de despacho no existe');</script>" );
		}
        
		// 6. Setea variables bloques
		top.setDynamicValueSets("listado", datos);
        
        top.setVariable("{id_ruta}", ""+idRuta);
        
        ArrayList btns = new ArrayList();
        IValueSet fila = new ValueSet();
        fila.setVariable("{id_pedido}", param_id_pedido);
        
        if ( costo_despacho != 0 ) {
            fila.setVariable("{precio}", Math.round(costo_despacho)+"");
        } else {
            fila.setVariable("{precio}", "");
        }
        
        top.setVariable("{precio}", Math.round(costo_despacho)+"");
        
        
        List responsables = bizDelegate.getResponsablesDespachoNC();
        ArrayList htmlResp = new ArrayList();
        for (int i=0; i < responsables.size(); i++) {
            ObjetoDTO resp = (ObjetoDTO) responsables.get(i);
            IValueSet filax = new ValueSet();
            filax.setVariable("{id}", String.valueOf(resp.getIdObjeto()));
            filax.setVariable("{nombre}", resp.getNombre());
            htmlResp.add(filax);            
        }
                
        List motivos = bizDelegate.getMotivosDespachoNC();
        ArrayList htmlMotvs = new ArrayList();
        for (int i=0; i < motivos.size(); i++) {
            ObjetoDTO mot = (ObjetoDTO) motivos.get(i);
            IValueSet filax = new ValueSet();
            filax.setVariable("{id}", String.valueOf(mot.getIdObjeto()));
            filax.setVariable("{nombre}", mot.getNombre());
            htmlMotvs.add(filax);            
        }
        
        top.setDynamicValueSets("RESPONSABLES", htmlResp);
        top.setDynamicValueSets("MOTIVOS", htmlMotvs);
        
        btns.add(fila);
        if ( mostrarPrecios ) {
            top.setDynamicValueSets("MOSTRAR_PRECIOS", btns);
        } else {
            top.setDynamicValueSets("NO_MOSTRAR_PRECIOS", btns);
        }
        
        if ( consideraPicking ) {
            top.setVariable("{view_pick}", "");
        } else {
            top.setVariable("{view_pick}", "style=\"display:none\"");
        }
        
        if ( Utils.mostrarCheckSiPedidoEstuvoEnTransito(pedido.getId_estado()) ) {
            top.setVariable("{view_transito}", "");
        } else {        
            top.setVariable("{view_transito}", "style=\"display:none\"");
        }
        
        if ( origen == Constantes.REPROGRAMACION_ORIGEN_MONITOR_DESPACHO ) {            
            fila.setVariable("{id_ruta}", ""+idRuta);
            top.setDynamicValueSets("BOTONES_MON_DESPACHO", btns);
        } else {            
            top.setDynamicValueSets("BOTONES_MON_PEDIDO", btns);
        }
        
		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

    private boolean precioDeshabilitado(long cantProds, JornadaDespachoEntity jor, long tiempoPicking, 
            long tiempoActual, long tiempoLimite, boolean consideraPicking, long horaFinJorDespacho) {
        if (consideraPicking) {
            if ( ( cantProds > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
                    || ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 )
                    || ( (tiempoPicking - tiempoActual) < tiempoLimite) ) {
                return true;
            }
        } else {
            if ( ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 )  
                || ( tiempoActual > horaFinJorDespacho) ) {
                return true;
            }    
        }
        return false;
    }
    
    private boolean precioDeshabilitadoVe(JornadaDespachoEntity jor, long tiempoPicking, long tiempoActual,
            long tiempoLimite, boolean consideraPicking, long horaFinJorDespacho) {
        if (consideraPicking) {
            if ( ( jor.getCapac_despacho() == 0 ) || ( ( tiempoPicking - tiempoActual ) < tiempoLimite ) ) {
                return true;
            }
        } else {
            if ( ( jor.getCapac_despacho() == 0 ) || ( tiempoActual > horaFinJorDespacho ) ) {
                return true;
            }
        }
        return false;
    }

    private boolean tieneTarifaExpres(int fechaActual, int fechaDespacho, int fechaPicking, int estadoTarifaExpress,
            long hora_pick, long tiempoLimiteValidacionExpress, long horaInicioValidacionExpress,
            long tiempoPicking, long tiempoActual, long cant_prods, JornadaDespachoEntity jor, boolean consideraPicking, long horaFinJorDespacho) {
        
        if (consideraPicking) {
            if (fechaActual == fechaDespacho && fechaActual == fechaPicking){
                if (estadoTarifaExpress == 1){
                    if (((hora_pick - tiempoLimiteValidacionExpress ) > horaInicioValidacionExpress) && 
                            ((tiempoPicking - tiempoActual) > tiempoLimiteValidacionExpress) &&
                              (cant_prods < (jor.getCapac_picking() - jor.getCapac_picking_ocupada())) &&
                                ((jor.getCapac_despacho() - jor.getCapac_despacho_ocupada()) > 0) ) {
                        return true;
                    }        
                }
            }
        } else {            
            if ( fechaActual == fechaDespacho ){
                if ( estadoTarifaExpress == 1 ) {
                    if ( ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) > 0 ) 
                            && ( tiempoActual < horaFinJorDespacho ) ) {
                        return true;
                    }        
                }
            }
        }        
        return false;
    }

}