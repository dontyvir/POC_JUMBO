package cl.bbr.boc.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.ZonaDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.JorDespachoCalDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class ViewSwitchJornadaForm extends Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7619358905285211710L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		HttpSession session = req.getSession();
		PedidoDTO oPedido = (PedidoDTO) session
				.getAttribute("##_PedidoDTO_x_Jornada");
		
		
		Enumeration params = req.getParameterNames();

        while (params.hasMoreElements()) {

            String p = (String) params.nextElement();
            System.out.print("ParameterName " + p + " ====>> " + req.getParameter(p) + "\n");
        }
        
		
		long idPedido = Long.parseLong(req.getParameter("id_pedido"));
		
		if (req.getParameter("id_pedido") == null) {
			throw new ParametroObligatorioException("id_pedido es null");
		}
		
		if (idPedido != oPedido.getId_pedido()) {
			throw new ParametroObligatorioException(
					"Solicitud de pedido no valida");
		}
		
		BizDelegate bizDelegate = new BizDelegate();
		
		//Selecciono una opcion cambio Jornada o Cambio Local
		if(req.getParameter("Cambiar") != null){
			
			if(req.getParameter("tipoCambio")!= null && "local".equals(req.getParameter("tipoCambio"))){
				
				//Desplegar calendario por ajax
				if(req.getParameter("action")!= null && "desplCal".equals(req.getParameter("action"))){										
					
					List direcciones = (List) session.getAttribute("##_DireccDesp_x_Jornada");
					long direccionId = 0;
					long id_zona=0;
					String paramFecha = null;
					boolean isRetiroLocal=false;
					
					if ( req.getParameter("txt_fecha") != null && req.getParameter("txt_fecha").matches("\\d{4}-\\d{2}-\\d{2}")){
						paramFecha = req.getParameter("txt_fecha");  
					}
					 
					if ( req.getParameter("direccionId") != null ) {
						if("R".equals(req.getParameter("direccionId"))){
							isRetiroLocal=true;
							if(req.getParameter("local_zona_retiro") != null){
								try {
									id_zona = Long.parseLong(req.getParameter("local_zona_retiro"));    
							    } catch (Exception e) {}  
							}				
						}else{
							try {
								direccionId = Long.parseLong(req.getParameter("direccionId"));    
						    } catch (Exception e) {}     
						}
					}		
								
										 
					if(direccionId != 0 && !isRetiroLocal){
						DireccionesDTO dirDto = null;
						Iterator it = direcciones.iterator();	
						while(it.hasNext()){
							dirDto = (DireccionesDTO) it.next();
							if(dirDto.getId_dir() == direccionId){
								dirDto = bizDelegate.getDireccionByIdDir(direccionId);
								break;
							}else{
								dirDto = null; 
							}
						}				
					
						if(dirDto != null){
							id_zona=dirDto.getId_zon();						
						}
					}
					
					String strCalendario = "";
					if(id_zona != 0)
						strCalendario = getCalendario (oPedido, paramFecha, id_zona);
					
					View salida = new View(res);
					salida.setHtmlOut(strCalendario);
					salida.Output();
				}
				
				//Cambia jornada el pedido
				else if(req.getParameter("action")!= null && "CambiarLocal".equals(req.getParameter("action"))){
				
					long idJdespacho = 0;
					String result ="";
					
					long id_zona_retiro=0;
					boolean isRetiroLocal=false;
					long direccionId = 0;
					
					try {//Jornada nueva seleccionada en el calendario.
						idJdespacho = Long.parseLong(req.getParameter("id_jdespacho"));    
				    } catch (Exception e) {}   
					
					//Direccion nueva o cambio a retiro local
					if ( req.getParameter("direccionId") != null ) {
						if("R".equals(req.getParameter("direccionId"))){
							isRetiroLocal=true;
							if(req.getParameter("local_zona_retiro") != null){
								try {
									id_zona_retiro = Long.parseLong(req.getParameter("local_zona_retiro"));    
							    } catch (Exception e) {}  
							}				
						}else{
							try {
								direccionId = Long.parseLong(req.getParameter("direccionId"));    
						    } catch (Exception e) {}     
						}
					}
					
					
					if(idJdespacho != 0 && (idJdespacho != oPedido.getId_jdespacho())){
					
						JorDespachoCalDTO oJorDespachoNuevaCalDTO = bizDelegate.getJornadaDespachoById(idJdespacho);
						long capPickingDisponible=0;
						long capDespacho=0;
						
						if(oJorDespachoNuevaCalDTO != null){
							capPickingDisponible=oJorDespachoNuevaCalDTO.getCapacPicking() - oJorDespachoNuevaCalDTO.getCapacOcupadaPicking();
							capDespacho=oJorDespachoNuevaCalDTO.getCapac_despacho() - oJorDespachoNuevaCalDTO.getCapac_ocupada();
						}
						
						if(oJorDespachoNuevaCalDTO != null && (capPickingDisponible >= oPedido.getCant_prods() && capDespacho > 0)){
							List locales = bizDelegate.getLocalesByZona(oJorDespachoNuevaCalDTO.getId_zona());
							cl.bbr.jumbocl.clientes.dto.LocalDTO localDto = null;
							LocalDTO localDtoNuevo = null;
							if(locales.size() >0 && locales != null){
								localDto = (cl.bbr.jumbocl.clientes.dto.LocalDTO) locales.get(0);
								
								localDtoNuevo = new LocalDTO();
								localDtoNuevo.setId_local(localDto.getId_local());
								localDtoNuevo.setCod_local(localDto.getCod_local());
								localDtoNuevo.setNom_local(localDto.getNom_local());
								localDtoNuevo.setDireccion(localDto.getDireccion());
								localDtoNuevo.setIdZonaRetiro(localDto.getId_zona_retiro());
								localDtoNuevo.setRetiroLocal(localDto.getRetirolocal());
								
							}
							
							long idJdespachoNueva=oJorDespachoNuevaCalDTO.getId_jdespacho();
							
							String tipoPrecioNuevo=req.getParameter("otroprecio");
							double precioNuevo=0;
							if(tipoPrecioNuevo != null){
								if("0".equals(tipoPrecioNuevo)){//Aplicar precio calendario
									try {
										precioNuevo = Double.parseDouble(req.getParameter("precio_"+idJdespachoNueva)); 
										if(precioNuevo > oPedido.getCosto_despacho())
											precioNuevo=oPedido.getCosto_despacho();
								    } catch (Exception e) {}  
									
								}else if("1".equals(tipoPrecioNuevo)){//Mantener precio cobrado
									precioNuevo = oPedido.getCosto_despacho();
								}else if("2".equals(tipoPrecioNuevo)){//Sobreescribir precio									
									try {
										precioNuevo = Double.parseDouble(req.getParameter("precioNuevoDesp")); 
										if(precioNuevo > oPedido.getCosto_despacho())
											precioNuevo=oPedido.getCosto_despacho();
								    } catch (Exception e) {}   
								}								
							}else{
								precioNuevo=oPedido.getCosto_despacho();
							}			
							
							boolean modificarJPicking = false;
				             if ( Utils.considerarPickingParaReprogramar( oPedido.getId_estado() )) {
				                 modificarJPicking = true;
				             }
							
							if(modificarJPicking && localDtoNuevo != null && bizDelegate.doReagendaDespachoLocal(oPedido, oJorDespachoNuevaCalDTO, localDtoNuevo, precioNuevo, isRetiroLocal, direccionId,  modificarJPicking)){	
								
								try {
									//Sumar reagendado
									bizDelegate.updPedidoReagendado(oPedido.getId_pedido());
								} catch (Exception e) {} 
					             
								try {
					            	 long idMotivo = Long.parseLong(req.getParameter("motivo"));    
					            	 long idResponsable = Long.parseLong(req.getParameter("responsable"));
					            	 if(idMotivo != 0  && idResponsable != 0)
					            		 bizDelegate.addMotivoResponsableReprogramacion(oPedido.getId_pedido(),idMotivo,idResponsable,oPedido.getId_jdespacho(),usr.getId_usuario());
					             } catch (Exception e) {} 
								
								try {
					            	 LogPedidoDTO oLog = new LogPedidoDTO();
					            	 oLog.setId_pedido(oPedido.getId_pedido());					            	 
					            	 oLog.setLog("[CAMBIO_LOCAL] Jornada Reagendada, se cambia jornada despacho: "+ oPedido.getId_jdespacho() +" --> "+idJdespachoNueva);
					            	 oLog.setUsuario(usr.getLogin());
					            	 bizDelegate.addLogPedido(oLog);
					            	 
					             } catch (Exception e) {} 
					         																	
								result+="<script>alert('Cambio realizado correctamente.');";
								result+="window.opener.location.reload();";
								result+="window.close();";
								result+="</script>";
								
							}else{
								result+="<script>alert('No es posible cambiar la jornada.');";
								result+="window.close();";
								result+="</script>";								
							}
						}else{
							result+="<script>alert('No es posible cambiar la jornada. (No existe capacidad disponible en la ventana seleccionada.)');";
							result+="window.close();";
							result+="</script>";
						}					
					}
					
					View salida = new View(res);
					salida.setHtmlOut(result);
					salida.Output();
				}			
				
				//Despliega datos del pedido direcciones del cliente y calendario seleccionado
				else{
					
					if ( Utils.considerarPickingParaReprogramar( oPedido.getId_estado() )) {		            
					
						String html = getServletConfig().getInitParameter("TplFileDirecciones");
						html = path_html + html;
											
						TemplateLoader load = new TemplateLoader(html);
						ITemplate tem = load.getTemplate();
						IValueSet top = new ValueSet();
						
						top.setVariable("{id_pedido}", String.valueOf(oPedido.getId_pedido()));
						top.setVariable("{id_cliente}", String.valueOf(oPedido.getId_cliente()));
						
						top.setVariable("{estado}", String.valueOf(oPedido.getEstado()));
						top.setVariable("{costo_despacho_formato}", Formatos.formatoPrecio(oPedido.getCosto_despacho())+"");
						top.setVariable("{costo_despacho}", Math.round(oPedido.getCosto_despacho())+"");
						
						top.setVariable("{id_local}", String.valueOf(oPedido.getId_local()));
						top.setVariable("{local}", String.valueOf(oPedido.getNom_local()));
						
						top.setVariable("{id_picking}", String.valueOf(oPedido.getId_jpicking()));
						top.setVariable("{id_despacho}", String.valueOf(oPedido.getId_jdespacho()));
						top.setVariable("{capac_picking}", String.valueOf(oPedido.getCant_prods()));
						
						if("R".equals(oPedido.getTipo_despacho())){
							top.setVariable("{direccion}","Retiro Local");
						}					
						
						long idCliente = oPedido.getId_cliente();
						List direcciones = bizDelegate.getDireccionesByIdCliente(idCliente);
						session.setAttribute("##_DireccDesp_x_Jornada",direcciones);
						
						List locales = bizDelegate.getLocales();
						List localesRetiro = new ArrayList();
						
						Iterator itl = locales.iterator();
						while(itl.hasNext()){						
							cl.bbr.jumbocl.clientes.dto.LocalDTO locDto = (cl.bbr.jumbocl.clientes.dto.LocalDTO) itl.next();
							if("S".equals(locDto.getRetirolocal())){
								IValueSet filaL = new ValueSet();
								filaL.setVariable("{id_zona}", String.valueOf(locDto.getId_zona_retiro()));
								filaL.setVariable("{nombre}", locDto.getNom_local());
								localesRetiro.add(filaL);
								if("R".equals(oPedido.getTipo_despacho()) && oPedido.getId_local() == locDto.getId_local()){
									filaL.setVariable("{selected_local}", "selected");
								}else{
									filaL.setVariable("{selected_local}", "");
								}
							}
						}
						if("R".equals(oPedido.getTipo_despacho())){
							top.setVariable("{ckeck_retiro}", "checked");
						}else{
							top.setVariable("{ckeck_retiro}", "");
						}
						
						top.setDynamicValueSets("LOCALESRETIRO", localesRetiro);
						
											
						Iterator it = direcciones.iterator();		
						ArrayList listDirecciones = new ArrayList();
						while(it.hasNext()){
							DireccionesDTO dirDto = (DireccionesDTO) it.next();
							IValueSet filax = new ValueSet();
							filax.setVariable("{id}", String.valueOf(dirDto.getId_dir()));
							filax.setVariable("{nombre}", dirDto.getAlias() +" "+ dirDto.getCalle()+ " "+ dirDto.getNumero() +" "+dirDto.getComuna());
							if(oPedido.getDir_id() == dirDto.getId_dir()){
								top.setVariable("{direccion}", dirDto.getAlias() +" "+ dirDto.getCalle()+ " "+ dirDto.getNumero() +" "+dirDto.getComuna()+" Poligono: "+dirDto.getId_poligono()+" Local:"+dirDto.getNom_local()+" "+dirDto.getNom_zona() );
								filax.setVariable("{checkDir}", "checked");
							}else{
								filax.setVariable("{checkDir}", "");
							}
							listDirecciones.add(filax); 
						}		
						
						top.setVariable("{strCalendario}", getCalendario (oPedido, null, oPedido.getId_zona()));
						top.setDynamicValueSets("DIRECCIONES", listDirecciones);
															
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
				        
				        top.setVariable("{medio_pago}", oPedido.getMedio_pago());
				        top.setVariable("{medio_pago_lc}", Constantes.MEDIO_PAGO_LINEA_CREDITO);
				       
				        top.setVariable("{fecha_max}", Formatos.fechaLiberacionReserva(oPedido.getMedio_pago(), oPedido.getFingreso(), "yyyyMMdd",1));
				        
				        top.setDynamicValueSets("RESPONSABLES", htmlResp);
				        top.setDynamicValueSets("MOTIVOS", htmlMotvs);
					
				        View salida = new View(res);
						String result = tem.toString(top);
						salida.setHtmlOut(result);
						salida.Output();
					}else{
						
						String result ="<script>alert('No es posible cambiar la jornada. (Pedido en estado no valido.)');";
						result+="window.close();";
						result+="</script>";	
						
						View salida = new View(res);
						salida.setHtmlOut(result);
						salida.Output();
					}
				}
			
			}else if(req.getParameter("tipoCambio")!= null && "jornada".equals(req.getParameter("tipoCambio"))){
				String location="ViewCalJorForm?id_pedido="+idPedido;
				res.sendRedirect(location);
			}else{
				String location="ViewSwitchJornadaForm?id_pedido="+idPedido;
				res.sendRedirect(location);
			}
			
		}//Menu inicio
		else{					
		
			// 1. Parámetros de inicialización servlet		
			String html = getServletConfig().getInitParameter("TplFile");
			html = path_html + html;								
			
			TemplateLoader load = new TemplateLoader(html);
			ITemplate tem = load.getTemplate();
			IValueSet top = new ValueSet();
							
			top.setVariable("{id_pedido}", String.valueOf(oPedido.getId_pedido()));
			top.setVariable("{id_cliente}", String.valueOf(oPedido.getId_cliente()));
			
			top.setVariable("{estado}", String.valueOf(oPedido.getEstado()));
			top.setVariable("{costo_despacho_formato}", Formatos.formatoPrecio(oPedido.getCosto_despacho())+"");
			top.setVariable("{costo_despacho}", Math.round(oPedido.getCosto_despacho())+"");
			
			top.setVariable("{id_local}", String.valueOf(oPedido.getId_local()));
			top.setVariable("{local}", String.valueOf(oPedido.getNom_local()));
			
			top.setVariable("{id_picking}", String.valueOf(oPedido.getId_jpicking()));
			top.setVariable("{id_despacho}", String.valueOf(oPedido.getId_jdespacho()));
			top.setVariable("{capac_picking}", String.valueOf(oPedido.getCant_prods()));
			
			View salida = new View(res);
			String result = tem.toString(top);
			salida.setHtmlOut(result);
			salida.Output();
		}
				
	}
	
	protected String getCalendario (PedidoDTO pedido, String paramFecha, long id_zona) throws Exception {
		

		String rc = "";
        int origen = 0; //Origen 1 = Monitor de despacho
        long idRuta = 0;        
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFileCalendario");
		html = path_html + html;
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		BizDelegate bizDelegate = new BizDelegate();
		        		
		long id_pedido = pedido.getId_pedido();
		double costo_despacho = pedido.getCosto_despacho();
		long cant_prods = pedido.getCant_prods();
		ZonaDTO zonadto = bizDelegate.getZonaById(id_zona);
		
		Date fecha = new Date();		
		if ( paramFecha != null ){
			fecha = new SimpleDateFormat("yyyy-MM-dd").parse(paramFecha);			
		} 		
		
		//Formateo de fecha y calendario
		Calendar cal1 = new GregorianCalendar();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);
		
		cal1.setTime(fecha);
		cal1.getTime();
		
		int year = cal1.get(Calendar.YEAR);
		int mes = cal1.get(Calendar.MONTH);
		int woy	 = cal1.get(Calendar.WEEK_OF_YEAR);
        if (mes == 11 && woy == 1) { // Diciembre
            year++;
        }	
        
		//	4.2 Listado de jornadas (horario)
		CalendarioDespachoDTO cal = bizDelegate.getCalendarioDespacho(woy,year,id_zona);
		
		// Obtenemos los objetos
		SemanasEntity sem = new SemanasEntity();
		sem = cal.getSemana();
		
		List horarios = new ArrayList();
		horarios = cal.getHorarios();
		
		List jornadas = new ArrayList();
		jornadas = cal.getJornadas();			
		
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
		//top.setVariable("{msg}", msg);
		
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
		

        
		// 6. Setea variables bloques
		top.setDynamicValueSets("listado", datos);
        
        top.setVariable("{id_ruta}", ""+idRuta);
        
        ArrayList btns = new ArrayList();
        IValueSet fila = new ValueSet();
        fila.setVariable("{id_pedido}", String.valueOf(id_pedido));
        
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
        
		return tem.toString(top);
			
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
